package com.waes.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@ComponentScan(value={
		"com.waes.repository",
		"com.waes.service",
		"com.waes.web"})
public class ApplicationConfig {

	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
	    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	    loggingFilter.setIncludeClientInfo(true);
	    loggingFilter.setIncludeQueryString(true);
	    loggingFilter.setIncludePayload(true);
	    return loggingFilter;
	}
	
	@Bean
	public FilterRegistrationBean<CommonsRequestLoggingFilter> loggingFilterRegistration() {
	    FilterRegistrationBean<CommonsRequestLoggingFilter> registration = 
	    		new FilterRegistrationBean<CommonsRequestLoggingFilter>(requestLoggingFilter());
	    registration.addUrlPatterns("/v1/diff/*");
	    return registration;
	}
}
