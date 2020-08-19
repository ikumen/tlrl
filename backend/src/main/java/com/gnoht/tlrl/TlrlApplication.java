package com.gnoht.tlrl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gnoht.tlrl.bookmark.converters.ReadStatusConverter;
import com.gnoht.tlrl.bookmark.converters.SharedStatusConverter;

@SpringBootApplication
public class TlrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlrlApplication.class, args);
	}

	@Configuration
	public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
      registry.addConverter(new ReadStatusConverter());
      registry.addConverter(new SharedStatusConverter());
    }
	  
	}
}
