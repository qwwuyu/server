package com.qwwuyu.gs.controller.admin

import com.qwwuyu.gs.configs.Constant
import com.qwwuyu.gs.configs.EnvConfig
import com.qwwuyu.gs.entity.FileBean
import com.qwwuyu.gs.entity.FileResultEntity
import com.qwwuyu.gs.entity.ResponseBean
import com.qwwuyu.gs.filter.AuthRequired
import com.qwwuyu.gs.helper.FileDownloadHelper
import com.qwwuyu.gs.service.UserService
import com.qwwuyu.gs.utils.AppUtil
import com.qwwuyu.lib.clazz.MultipartFileSender
import com.qwwuyu.lib.utils.CommUtil
import com.qwwuyu.lib.utils.FileUtils
import com.qwwuyu.lib.utils.toMD5
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.concurrent.withLock

@Controller
@RequestMapping("/i/file/")
@AuthRequired(permit = Constant.PERMIT_ADMIN, code = HttpServletResponse.SC_UNAUTHORIZED)
class FileCtrl {
    @Resource
    private lateinit var userService: UserService

    @Resource
    private lateinit var multipartResolver: CommonsMultipartResolver

    @RequestMapping(value = ["query"], method = [RequestMethod.POST])
    fun query(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        val file = getFile(path)
        if (!FileUtils.isDirectory(file)) {
            AppUtil.renderInfo(response, "目录未符合")
            return
        }
        val files = file.listFiles() ?: emptyArray()
        val list = files.map { cf ->
            if (cf.isDirectory) {
                FileBean(cf.name, true, null, null, null)
            } else {
                val md5 = "${cf.name}${cf.length()}${cf.lastModified()}".toMD5().substring(0, 20)
                FileBean(cf.name, false, cf.lastModified(), FileUtils.getFileSize(cf.length()), md5)
            }
        }.toMutableList()
        list.sortWith(Comparator { lhs: FileBean, rhs: FileBean ->
            if (lhs.dir && !rhs.dir) return@Comparator -1
            if (!lhs.dir && rhs.dir) return@Comparator 1
            return@Comparator lhs.name.compareTo(rhs.name)
        })
        AppUtil.render(response, AppUtil.getSuccessBean().data(list))
    }

    @RequestMapping(value = ["upload"], method = [RequestMethod.POST])
    fun upload(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        val parent = getFile(path)
        if (!FileUtils.isDirectory(parent)) {
            AppUtil.renderInfo(response, "目录未符合")
            return
        }
        // 检查form中是否有enctype="multipart/form-data"
        val entity = FileResultEntity()
        if (multipartResolver.isMultipart(request)) {
            val multiRequest = request as MultipartHttpServletRequest
            // 获取multiRequest 中所有的文件名
            val iterator = multiRequest.fileNames
            while (iterator.hasNext()) {
                val multipartFile = multiRequest.getFile(iterator.next()) ?: continue
                //非文件
                val oFilename = multipartFile.originalFilename
                val fileName = oFilename ?: multipartFile.name
                //未获取文件名
                if (CommUtil.isEmpty(fileName)) continue
                val file = getFile(parent, fileName)
                when {
                    file == null -> entity.setFailureFile(fileName)
                    file.exists() -> entity.setExistFile(fileName)
                    else -> {
                        multipartFile.transferTo(file)
                        entity.setSuccessFile(fileName)
                    }
                }
            }
        }
        AppUtil.render(response, AppUtil.getSuccessBean().info(null).data(entity))
    }

    @RequestMapping(value = ["delete"], method = [RequestMethod.POST])
    fun delete(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        val file = getFile(path)
        if (!FileUtils.isFile(file)) {
            AppUtil.renderInfo(response, "文件未符合")
            return
        }
        if (file.delete()) {
            AppUtil.render(response, AppUtil.getSuccessBean().info("删除成功"))
        } else {
            AppUtil.render(response, AppUtil.getErrorBean().info("删除失败"))
        }
    }

    @RequestMapping(value = ["deleteDir"], method = [RequestMethod.POST])
    fun deleteDir(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        val file = getFile(path)
        if (!FileUtils.isDirectory(file)) {
            AppUtil.renderInfo(response, "目录未符合")
            return
        }
        val files = file.listFiles()
        if (files != null && files.isNotEmpty()) {
            AppUtil.render(response, AppUtil.getErrorBean().info("无法删除非空目录"))
        } else if (file.delete()) {
            AppUtil.render(response, AppUtil.getSuccessBean().info("删除成功"))
        } else {
            AppUtil.render(response, AppUtil.getErrorBean().info("删除失败"))
        }
    }

    @AuthRequired(anth = false)
    @RequestMapping(value = ["open", "open/*"])
    fun open(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        checkMd5(request, response, path, false)
    }

    @AuthRequired(anth = false)
    @RequestMapping(path = ["download", "download/*"])
    fun download(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("path") path: String) {
        checkMd5(request, response, path, true)
    }

    private fun checkMd5(request: HttpServletRequest, response: HttpServletResponse, _path: String, download: Boolean) {
        _path.let { path ->
            if (!path.matches("/[\\dA-F]{20}/.+".toRegex())) return@let
            val md5 = path.substring(1, 21)
            val file = getFile(path.substring(21))
            val pathStart = path.substring(21, path.lastIndexOf("/") + 1)
            val parent = file.parentFile
            if (!(parent != null && parent.isDirectory)) return@let
            val filterFile = (parent.listFiles() ?: emptyArray()).filter { it.isFile }.firstOrNull { cf ->
                md5 == "${cf.name}${cf.length()}${cf.lastModified()}".toMD5().substring(0, 20)
            } ?: return@let
            transferFile(request, response, pathStart + filterFile.name, download)
            return
        }
        val user = AppUtil.checkPermit(
            Constant.PERMIT_ADMIN, HttpServletResponse.SC_UNAUTHORIZED, expire = true, render = true,
            userService = userService, request = request, response = response
        )
        if (user != null) {
            transferFile(request, response, _path, download)
        }
    }

    private fun transferFile(
        request: HttpServletRequest,
        response: HttpServletResponse,
        path: String,
        download: Boolean
    ) {
        val file = getFile(path)
        if (!FileUtils.isFile(file)) {
            AppUtil.renderInfo(response, "文件未符合", HttpServletResponse.SC_NOT_FOUND)
            return
        }
        val renderInfo = MultipartFileSender(file.toPath(), request, response, download).serveResource()
        renderInfo?.let {
            AppUtil.renderInfo(response, it.msg, it.code)
        }
    }

    @RequestMapping(value = ["rename"], method = [RequestMethod.POST])
    fun rename(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("path") path: String,
        @RequestParam("dest") newPath: String
    ) {
        val file = getFile(path)
        if (!FileUtils.exists(file)) {
            AppUtil.renderInfo(response, "文件未符合")
            return
        }
        val dest = getFile(newPath)
        if (dest.exists()) {
            AppUtil.renderInfo(response, "文件未符合")
            return
        }
        if (file.renameTo(dest)) {
            AppUtil.render(response, AppUtil.getSuccessBean().info("操作成功"))
        } else {
            AppUtil.render(response, AppUtil.getErrorBean().info("操作失败"))
        }
    }

    @RequestMapping(value = ["createDir"], method = [RequestMethod.POST])
    fun createDir(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("path") path: String,
        @RequestParam("dirName") dirName: String
    ) {
        if (dirName.matches(".*[\\\\/:*?\"<>|]+.*".toRegex())) {
            AppUtil.renderInfo(response, "不能包含特殊字符\\/:*?\"<>|")
            return
        }
        val file = getFile(path)
        if (!file.isDirectory) {
            AppUtil.renderInfo(response, "文件夹不存在")
            return
        }
        val dirFile = File(file, dirName)
        if (dirFile.exists()) {
            AppUtil.renderInfo(response, "文件夹已存在")
            return
        }
        if (dirFile.mkdir()) {
            AppUtil.render(response, AppUtil.getSuccessBean().info("创建文件夹成功"))
        } else {
            AppUtil.render(response, AppUtil.getErrorBean().info("创建文件夹失败"))
        }
    }

    @RequestMapping(value = ["downloadFile"], method = [RequestMethod.POST])
    fun downloadFile(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam("path") path: String,
        @RequestParam("downloadUrl") downloadUrl: String?
    ) {
        val time = System.currentTimeMillis()
        val downloadDir = getFile(path)
        if (!downloadDir.isDirectory) {
            AppUtil.renderInfo(response, "文件夹不存在")
            return
        }
        val url = URL(downloadUrl)
        val con = url.openConnection()
        val code = (con as HttpURLConnection).responseCode
        if (code != HttpServletResponse.SC_OK) {
            CommUtil.closeStream(con.getInputStream())
            AppUtil.renderInfo(response, "状态码错误:$code")
            return
        }
        val file = File(downloadDir, FileDownloadHelper.getNameForDisposition(con, url))
        if (file.exists()) {
            CommUtil.closeStream(con.getInputStream())
            AppUtil.renderInfo(response, "文件已存在:" + file.name)
            return
        }
        val lock = ReentrantLock()
        val condition = lock.newCondition()
        if (!FileDownloadHelper.download(con, file) { lock.withLock { condition.signalAll() } }) {
            CommUtil.closeStream(con.getInputStream())
            AppUtil.renderInfo(response, "有正在进行的任务")
            return
        }
        val diffTime = System.currentTimeMillis() - time
        lock.withLock {
            condition.await(3000 - diffTime, TimeUnit.MILLISECONDS)
        }
        val progress = FileDownloadHelper.progress
        when (progress?.state) {
            FileDownloadHelper.DOWNLOADING -> AppUtil.render(
                response,
                ResponseBean(Constant.HTTP_DOWNLOADING, progress.progressText())
            )
            FileDownloadHelper.DOWNLOAD_ERROR -> AppUtil.render(
                response,
                AppUtil.getErrorBean().info("下载失败:" + progress.errorText())
            )
            else -> AppUtil.render(response, AppUtil.getSuccessBean().info("结束:" + progress?.progressText()))
        }
    }

    @RequestMapping(value = ["checkDownloadFile"], method = [RequestMethod.GET])
    fun checkDownloadFile(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestParam(name = "cancel", required = false) cancel: String?
    ) {
        val progress = FileDownloadHelper.progress
        when {
            progress == null -> AppUtil.render(response, AppUtil.getSuccessBean().info("没有任务"))
            progress.state == FileDownloadHelper.DOWNLOADING -> AppUtil.render(
                response,
                AppUtil.getSuccessBean().info(progress.progressText())
            )
            progress.state == FileDownloadHelper.DOWNLOAD_ERROR -> AppUtil.render(
                response,
                AppUtil.getSuccessBean().info("下载失败:" + progress.errorText())
            )
            else -> AppUtil.render(response, AppUtil.getSuccessBean().info("结束:" + progress.progressText()))
        }
        if ("true" == cancel) FileDownloadHelper.cancelDownload()
    }

    companion object {
        private fun getFile(path: String): File {
            return File(EnvConfig.instance.fileDir, path)
        }

        private fun getFile(parent: File, path: String): File? {
            val file = File(parent, path)
            val isChild = FileUtils.isChild(parent, file)
            return if (isChild) {
                file.canonicalFile
            } else {
                null
            }
        }
    }
}