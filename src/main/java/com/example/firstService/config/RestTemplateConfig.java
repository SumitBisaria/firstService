package com.example.firstService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *Configuration class is used to build beans at application start-up.
 * @author SBisaria
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Bean method is used to build for {@link RestTemplate} instance with timeout.
     * @return instance of {@link RestTemplate}
     * @author SBisaria
     */
    @Bean
    public RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }
}
