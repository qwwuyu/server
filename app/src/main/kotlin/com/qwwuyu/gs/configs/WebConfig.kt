package com.qwwuyu.gs.configs

import com.qwwuyu.gs.GsApplication
import com.qwwuyu.gs.filter.AuthInterceptor
import com.qwwuyu.gs.filter.IpInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.ErrorPageRegistrar
import org.springframework.boot.web.server.ErrorPageRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.io.FileUrlResource
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.WebContentInterceptor


@Configuration
class WebConfig : WebMvcConfigurer, ErrorPageRegistrar {
    private val logger = LoggerFactory.getLogger(WebConfig::class.java)

    @Autowired
    private lateinit var env: Environment

    @Bean
    fun envConfig(): EnvConfig {
        logger.debug("envConfig")
        return EnvConfig(env)
    }

    @Bean
    fun myMultipartResolver(): CommonsMultipartResolver {
        val commonsMultipartResolver = CommonsMultipartResolver(GsApplication.context)
        commonsMultipartResolver.setDefaultEncoding("utf-8")
        commonsMultipartResolver.setMaxUploadSize(512 * 1024 * 1024)
        commonsMultipartResolver.setMaxUploadSizePerFile(256 * 1024 * 1024)
        commonsMultipartResolver.setMaxInMemorySize(1024 * 1024)
        commonsMultipartResolver.setUploadTempDir(FileUrlResource(Constant.TMP_PATH))
        return commonsMultipartResolver
    }

    @Bean()
    fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val webContentInterceptor = WebContentInterceptor()
        registry.addInterceptor(webContentInterceptor)
        registry.addInterceptor(IpInterceptor()).addPathPatterns("/i/**")
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
    }

    override fun registerErrorPages(registry: ErrorPageRegistry) {
        val e401 = ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401")
        val e404 = ErrorPage(HttpStatus.NOT_FOUND, "/error/404")
        val e405 = ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/error/405")
        val e500 = ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500")
        registry.addErrorPages(e401, e404, e405, e500)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedMethods("*")
//            .allowCredentials(true)
//            .maxAge(3600)
    }
}