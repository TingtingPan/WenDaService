package com.example.wenDaService.configuration;

import com.example.wenDaService.interceptor.LoginRequiredInterceptor;
import com.example.wenDaService.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    //将interceptor配置到webServer里面。
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");//一定要注意顺序问题、因为loginRequired依赖上一个拦截器产生的hostholder
        super.addInterceptors(registry);
    }
}
