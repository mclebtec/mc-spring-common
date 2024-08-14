package com.mclebtec.client.connector.service.impl;

import static com.mclebtec.client.connector.enums.Common.AZURE_API_KEY;
import static com.mclebtec.client.connector.enums.Common.BEARER;
import static com.mclebtec.client.connector.enums.Common.TOKEN;
import static com.mclebtec.error.domain.enums.ValidationErrors.CONNECTIVITY_ERROR;
import static java.net.URI.create;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mclebtec.client.connector.exception.ClientConnectorException;
import com.mclebtec.client.connector.service.ClientConnectorService;
import java.net.HttpRetryException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service("clientHttpConnectorService")
@RequiredArgsConstructor
public class ClientHttpConnectorServiceImpl implements ClientConnectorService {

  private final HttpClient httpClient;
  private final RetryTemplate retryTemplate;
  private final ObjectMapper objectMapperForRest;

  @Override
  public Map<String, Object> connect(final String url,
      final HttpMethod method,
      final String token,
      final Object body) {
    try {
      log.info("Connecting via http::method::{}::url-details::{}::body-details::{}::auth-token::{}",
          method, url, body, token);

      Map<String, Object> finalResponse = new HashMap<>();

      retryTemplate.execute(context -> {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(create(url))
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(ACCEPT, APPLICATION_JSON_VALUE)
            .method(method.toString(), ofString(ofNullable(body).map(Object::toString).orElse("")));

        if (Objects.nonNull(token)) {
          requestBuilder.header(AUTHORIZATION, "%s%s".formatted(BEARER.getKey(), token))
              .header(TOKEN.getKey(), token)
              .header(AZURE_API_KEY.getKey(), token);
        }

        HttpRequest request = requestBuilder.build();

        log.info("Connecting via http::start-time::{}", now().toEpochMilli());

        HttpResponse<String> response = httpClient.send(request,
            HttpResponse.BodyHandlers.ofString());

        log.info("Connecting via http::end-time::{}", now().toEpochMilli());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {

          Map<String, Object> responseDetails = objectMapperForRest.readValue(response.body(),
              new TypeReference<HashMap<String, Object>>() {
              });

          log.debug("Returning after success response::{}", responseDetails);
          finalResponse.putAll(responseDetails);
          return true;

        } else {

          throw new HttpRetryException(response.toString(), response.statusCode());

        }

      });

      return finalResponse;

    } catch (Exception ex) {

      log.error("Error occurred in connectivity::{}", ex.getMessage(), ex);

      throw new ClientConnectorException(CONNECTIVITY_ERROR, ex.getLocalizedMessage(),
          ex.getCause());

    }
  }

}
