package com.ecomm.configuration;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"com.ecomm"})
@EntityScan("com.ecomm.model")
@EnableJpaRepositories("com.ecomm.dao")
public class EcommApplication extends SpringBootServletInitializer implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(EcommApplication.class, args);
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {}

	@Override
	public void addFormatters(FormatterRegistry registry) {}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {}

	@Override
	public Validator getValidator() {
		return null;
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return null;
	}
}
