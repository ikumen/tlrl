package com.gnoht.tlrl;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
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

	  @Bean
    public RestTemplate restTemplate() {
	    return new RestTemplate();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
      registry.addConverter(new ReadStatusConverter());
      registry.addConverter(new SharedStatusConverter());
    }
	}
	
	@Configuration
	@EnableAsync
	public class AsyncConfig implements AsyncConfigurer {
	  
    @Override
    public Executor getAsyncExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(10);
      executor.setMaxPoolSize(50);
      executor.setQueueCapacity(20);
      executor.setThreadNamePrefix("AsyncExecutor-");
      executor.initialize();
      return executor;
    }
	}
}
