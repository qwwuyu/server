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
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.WebContentInterceptor
import java.util.concurrent.TimeUnit


@Configuration
class WebConfig : WebMvcConfigurer, ErrorPageRegistrar {
    private val logger = LoggerFactory.getLogger(WebConfig::class.java)

    @Autowired
    private lateinit var env: Environment

    @Bean
    fun envConfig(): EnvConfig {
        return EnvConfig(env)
    }

    @Bean
    fun myMultipartResolver(): CommonsMultipartResolver {
        val commonsMultipartResolver = CommonsMultipartResolver(GsApplication.context)
        commonsMultipartResolver.setDefaultEncoding("utf-8")
        commonsMultipartResolver.setMaxUploadSize(512 * 1024 * 1024)
        commonsMultipartResolver.setMaxUploadSizePerFile(256 * 1024 * 1024)
        commonsMultipartResolver.setMaxInMemorySize(1024 * 1024)
        return commonsMultipartResolver
    }

    @Bean()
    fun authInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val webContentInterceptor = WebContentInterceptor()
        webContentInterceptor.addCacheMapping(CacheControl.noCache(), "/html/**")
        webContentInterceptor.addCacheMapping(CacheControl.maxAge(1, TimeUnit.DAYS), "/res/**")
        webContentInterceptor.addCacheMapping(CacheControl.maxAge(100, TimeUnit.DAYS), "/tres/**")
        webContentInterceptor.addCacheMapping(CacheControl.maxAge(100, TimeUnit.DAYS), "/favicon.ico")

        registry.addInterceptor(webContentInterceptor)
        registry.addInterceptor(IpInterceptor()).addPathPatterns("/i/**")
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
    }

    override fun registerErrorPages(registry: ErrorPageRegistry) {
        val e404 = ErrorPage(HttpStatus.NOT_FOUND, "/error404")
        val e500 = ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error500")
        registry.addErrorPages(e404, e500)
    }
}