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

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.qwwuyu.server.bean.ResponseBean;
import com.qwwuyu.server.bean.User;
import com.qwwuyu.server.configs.SecretConfig;
import com.qwwuyu.server.service.IFlagService;
import com.qwwuyu.server.service.IUserService;
import com.qwwuyu.server.utils.CommUtil;
import com.qwwuyu.server.utils.J2EEUtil;

@Controller
@RequestMapping("/test")
public class TestCtrl {
	@Resource private IUserService userService;
	@Resource private IFlagService service;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response) {
		if ("GET".equals(request.getMethod())) {
			Map<String, String[]> map = request.getParameterMap();
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			try {
				response.getWriter().write(JSON.toJSONString(new ResponseBean(1, "", map)));
			} catch (IOException e) {}
		}
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public void post(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> map = request.getParameterMap();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(JSON.toJSONString(new ResponseBean(1, "", map)));
		} catch (IOException e) {}
	}

	@RequestMapping(value = "/timeout", method = { RequestMethod.POST, RequestMethod.GET })
	public void timeout(HttpServletRequest request, HttpServletResponse response) {
		CommUtil.sleep(100000);
		post(request, response);
	}

	@RequestMapping(value = "/error", method = { RequestMethod.POST, RequestMethod.GET })
	public void error(HttpServletRequest request, HttpServletResponse response) {
		throw new RuntimeException("err");
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String token = request.getParameter("auth");
		if (J2EEUtil.isNull(response, token)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() != 5) {
			J2EEUtil.renderInfo(response, "权限不足");
			return;
		}
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
					file.transferTo(new File(SecretConfig.fileDir, fileName));
				}
			}
		}
		post(request, response);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE,
			headers = { "range" })
	public void download(@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "name", required = false) String name, @RequestHeader("range") String range, HttpServletResponse response)
			throws IOException {
		downloadFile(token, name, range, response);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void download(@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "name", required = false) String name, HttpServletResponse response) throws IOException {
		downloadFile(token, name, null, response);
	}

	private void downloadFile(String token, String name, String range, HttpServletResponse response) throws IOException {
		if (J2EEUtil.isNull(response, token)) return;
		User user = userService.selectByUser(new User().setToken(token));
		if (user == null) {
			J2EEUtil.renderInfo(response, "请先登录");
			return;
		}
		if (user.getAuth() != 5) {
			J2EEUtil.renderInfo(response, "权限不足");
			return;
		}
		File file = new File(SecretConfig.fileDir, name);
		System.out.println(1 + file.getAbsolutePath());
		if (!file.exists()) {
			System.out.println(2);
			response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
			return;
		}
		System.out.println(3);
		long left = 0, right = file.length() - 1, written = left;// 100 200 100
		if (range != null) {
			try {
				written = left = Long.parseLong(range.replaceAll("[^=]+=([\\d]+)\\-([\\d]*)", "$1"));
				right = Long.parseLong(range.replaceAll("[^=]+=([\\d]+)\\-([\\d]*)", "$2"));
			} catch (Exception e) {}
		}
		if (right >= file.length() || right < left || left < 0) {
			response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
			return;
		}
		if (range != null && left != 0) {
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", String.format("bytes %d-%d/%d", left, right, file.length()));
		}
		// HttpHeaders headers = new HttpHeaders();
		// headers.setContentDispositionFormData("attachment", "download.jpg");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
		response.setHeader("Content-Length", String.valueOf(right - left + 1));
		response.setHeader("Accept-Ranges", "bytes");
		try (InputStream is = new FileInputStream(file); OutputStream os = response.getOutputStream();) {
			if (left != 0) {
				is.skip(left);
			}
			int read = 0;
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