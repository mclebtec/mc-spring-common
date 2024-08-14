package com.mclebtec.client.connector.service.impl;

import static com.mclebtec.client.connector.enums.Common.AZURE_API_KEY;
import static com.mclebtec.client.connector.enums.Common.BEARER;
import static com.mclebtec.client.connector.enums.Common.TOKEN;
import static com.mclebtec.error.domain.enums.ValidationErrors.CONNECTIVITY_ERROR;
import static java.time.Instant.now;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mclebtec.client.connector.exception.ClientConnectorException;
import com.mclebtec.client.connector.service.ClientConnectorService;
import java.net.HttpRetryException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service("clientTemplateConnectorService")
@RequiredArgsConstructor
public class ClientTemplateConnectorServiceImpl implements ClientConnectorService {

  private final RestTemplate restTemplate;
  private final RetryTemplate retryTemplate;
  private final ObjectMapper objectMapperForRest;

  @Override
  public Map<String, Object> connect(final String url,
      final HttpMethod method,
      final String token,
      final Object body) {

    try {

      log.debug(
          "Connecting via rest template::method::{}::url-details::{}::body-details::{}::auth-token::{}",
          method, url, body, token);

      Map<String, Object> finalResponse = new HashMap<>();

      retryTemplate.execute(context -> {

        final HttpHeaders requestHeaders = new HttpHeaders();

        if (Objects.nonNull(token)) {
          requestHeaders.add(AUTHORIZATION, "%s%s".formatted(BEARER.getKey(), token));
          requestHeaders.add(TOKEN.getKey(), token);
          requestHeaders.add(AZURE_API_KEY.getKey(), token);
        }

        requestHeaders.setContentType(APPLICATION_JSON);
        requestHeaders.setAccept(of(APPLICATION_JSON));

        final HttpEntity<Object> httpEntity = ofNullable(body)
            .map(bdy -> new HttpEntity<>(bdy, requestHeaders))
            .orElse(new HttpEntity<>(requestHeaders));

        log.debug("Connecting via rest template::start-time::{}", now().toEpochMilli());

        final ResponseEntity<String> response = restTemplate.exchange(url, method, httpEntity,
            String.class);

        log.debug("Connecting via rest template::end-time::{}", now().toEpochMilli());

        if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {

          final Map<String, Object> responseDetails =
              objectMapperForRest.readValue(response.getBody(),
                  new TypeReference<HashMap<String, Object>>() {
                  });

          log.debug("Returning after success response::{}", responseDetails);
          finalResponse.putAll(responseDetails);
          return true;

        } else {

          throw new HttpRetryException(response.toString(), response.getStatusCode().value());

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
