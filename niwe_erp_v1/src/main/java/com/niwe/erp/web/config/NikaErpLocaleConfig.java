package com.niwe.erp.web.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.niwe.erp.core.converter.StringToItemClassificationConverter;
import com.niwe.erp.core.converter.StringToItemCountryConverter;
import com.niwe.erp.core.service.CoreCountryService;
import com.niwe.erp.core.service.CoreItemClassificationService;

import lombok.AllArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
@AllArgsConstructor
public class NikaErpLocaleConfig implements WebMvcConfigurer {
	private final CoreItemClassificationService coreItemClassificationService;
	private final CoreCountryService coreCountryService;

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		// resolver.setDefaultLocale(Locale.FRANCE);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToItemCountryConverter(coreCountryService));
		registry.addConverter(new StringToItemClassificationConverter(coreItemClassificationService));
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer javaTimeCustomizer() {
		return builder -> builder.modules(new JavaTimeModule());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(31556926); // 1
																												// year
	}
}
