package com.iishanto.springforwardproxy.conifg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.EventRecordingLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {
    @Bean
    RestClient getRestClient(){
        return RestClient.builder().build();
    }

    @Bean
    RestTemplate getRestTemplate(){
        var restTemplate=new RestTemplate();
        return restTemplate;
    }

    @Bean
    Logger getLog4jLogger(){
        return LoggerFactory.getLogger("SFP");
    }
}
