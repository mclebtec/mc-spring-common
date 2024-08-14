package com.mclebtec.client.connector.config;

import static java.net.http.HttpClient.newHttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

  @Bean(name = "objectMapperForRest")
  public ObjectMapper objectMapperForRest() {
    return new ObjectMapper();
  }

  @Bean(name = "restTemplate")
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean(name = "retryTemplate")
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();
    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(20000L);
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(5);
    retryTemplate.setRetryPolicy(retryPolicy);
    return retryTemplate;
  }

  @Bean(name = "httpClient")
  public HttpClient httpClient() {
    return newHttpClient();
  }

}
