package com.qwwuyu.server.configs;

import com.qwwuyu.server.filter.AuthInterceptor;
import com.qwwuyu.server.filter.IpInterceptor;
import com.qwwuyu.server.utils.LogUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.qwwuyu.server.ctrl"})
public class WebConfigurer extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LogUtils.i("WebConfigurer addInterceptors");
        registry.addInterceptor(new IpInterceptor()).addPathPatterns("/i/**");
        registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
