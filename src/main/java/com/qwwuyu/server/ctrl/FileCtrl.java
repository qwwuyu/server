package com.qwwuyu.server.ctrl;

import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.Constant;
import com.qwwuyu.server.configs.SecretConfig;
import com.qwwuyu.server.filter.AuthRequired;
import com.qwwuyu.server.utils.J2EEUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AuthRequired(permit = Constant.PERMIT_ADMIN, code = HttpServletResponse.SC_UNAUTHORIZED)
@Controller
@RequestMapping("/file")
public class FileCtrl {
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        // 将当前上下文初始化给 CommonsMutipartResolver
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 检查form中是否有enctype="multipart/form-data"
        List<String> list = new ArrayList<>();
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 获取multiRequest 中所有的文件名
            Iterator<String> iterator = multiRequest.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile file = multiRequest.getFile(iterator.next());
                if (file != null) {
                    String oFilename = file.getOriginalFilename();
                    String fileName = oFilename == null ? file.getName() : oFilename;
                    if (fileName != null) {
                        file.transferTo(new File(SecretConfig.fileDir, fileName));
                        list.add(fileName);
                    }
                }
            }
        }
        J2EEUtil.render(response, J2EEUtil.getSuccessBean().setData(list));
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(HttpServletRequest request, @RequestParam(value = "name", required = false) String name, HttpServletResponse response) throws IOException {
        User user = (User) request.getAttribute(Constant.KEY_USER);
        if (user == null) throw new RuntimeException("user is null");
        String range = request.getHeader("range");
        if (J2EEUtil.isNull(response, name)) {
            response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }
        File file = new File(SecretConfig.fileDir, name);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }
        long left = 0, right = file.length() - 1, written = left;
        if (range != null) {
            try {
                written = left = Long.parseLong(range.replaceAll("[^=]+=([\\d]+)-([\\d]*)", "$1"));
                right = Long.parseLong(range.replaceAll("[^=]+=([\\d]+)-([\\d]*)", "$2"));
            } catch (Exception ignored) {
            }
        }
        if (0 > left || right >= file.length() || right < left) {
            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }
        if (range != null) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setHeader("Content-Range", String.format("bytes %d-%d/%d", left, right, file.length()));
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(name, "utf-8") + "\"");
        response.setHeader("Content-Length", String.valueOf(right - left + 1));
        response.setHeader("Accept-Ranges", "bytes");
        try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
            if (left != 0) {
                is.skip(left);
            }
            int read;
            byte[] bytes = new byte[1024 * 1024];
            while ((read = is.read(bytes)) != -1) {
                written += read;
                if (written > right) {
                    read = (int) (right + 1 + read - written);
                    os.write(bytes, 0, read);
                    break;
                }
                os.write(bytes, 0, read);
            }
            os.flush();
        }
    }
}
