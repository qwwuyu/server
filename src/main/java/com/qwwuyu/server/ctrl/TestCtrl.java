package com.qwwuyu.server.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.configs.SecretConfig;
import com.qwwuyu.server.service.IFlagService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.CommUtil;

@Controller
@RequestMapping("/test")
public class TestCtrl {
	@Resource
	private IUserService userService;
	@Resource
	private IFlagService service;

	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) {
		if ("GET".equals(request.getMethod())) {
			Map<String, String[]> map = request.getParameterMap();
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			try {
				response.getWriter().write(JSON.toJSONString(new ResponseBean(1, "", map)));
			} catch (IOException e) {
			}
		}
	}

	@RequestMapping("/post")
	public void post(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> map = request.getParameterMap();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(JSON.toJSONString(new ResponseBean(1, "", map)));
		} catch (IOException e) {
		}
	}

	@RequestMapping("/timeout")
	public void timeout(HttpServletRequest request, HttpServletResponse response) {
		CommUtil.sleep(100000);
		post(request, response);
	}

	@RequestMapping("/error")
	public void error(HttpServletRequest request, HttpServletResponse response) {
		throw new RuntimeException("err");
	}

	@RequestMapping("/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		// 将当前上下文初始化给 CommonsMutipartResolver
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iterator = multiRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile file = multiRequest.getFile(iterator.next().toString());
				if (file != null) {
					String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : file.getName();
					String path = SecretConfig.uploadDir + fileName;
					file.transferTo(new File(path));
				}
			}
		}
		post(request, response);
	}

	// @RequestMapping(value = "/download")
	private ResponseEntity<byte[]> download(HttpServletResponse response) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", "download.jpg");
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(SecretConfig.uploadDir + "1.jpg")), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		File file = new File(SecretConfig.uploadDir + "1.file");
		try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream();) {
			response.setHeader("Content-Disposition", "attachment; filename=\"download.file\"");
			response.addHeader("Content-Length", String.valueOf(file.length()));
			int read = 0;
			byte[] bytes = new byte[1024 * 1024];
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			os.flush();
		}
	}

	@RequestMapping(value = "/download2", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void downloadBig(HttpServletRequest request, HttpServletResponse response) throws IOException {
		File file = new File(SecretConfig.uploadDir + "2.file");
		try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream();) {
			response.setHeader("Content-Disposition", "attachment; filename=\"download.file\"");
			response.addHeader("Content-Length", String.valueOf(file.length()));
			int read = 0;
			byte[] bytes = new byte[1024 * 1024];
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			os.flush();
		}
	}
}