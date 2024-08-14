package com.mclebtec.client.connector.service;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Map;
import org.springframework.http.HttpMethod;

public interface ClientConnectorService {

  default Map<String, Object> get(final String url) {
    return this.connect(url, GET, null, null);
  }

  default Map<String, Object> get(final String url, final String token) {
    return this.connect(url, GET, token, null);
  }

  default Map<String, Object> post(final String url, final Object body) {
    return this.connect(url, POST, null, body);
  }

  default Map<String, Object> post(final String url, final String token, final Object body) {
    return this.connect(url, POST, token, body);
  }

  default Map<String, Object> put(final String url, final Object body) {
    return this.connect(url, PUT, null, body);
  }

  default Map<String, Object> put(final String url, final String token, final Object body) {
    return this.connect(url, PUT, token, body);
  }

  default Map<String, Object> delete(final String url) {
    return this.connect(url, DELETE, null, null);
  }

  default Map<String, Object> delete(final String url, final String token) {
    return this.connect(url, DELETE, token);
  }

  default Map<String, Object> delete(final String url, final Object body) {
    return this.connect(url, DELETE, null, body);
  }

  default Map<String, Object> delete(final String url, final String token, final Object body) {
    return this.connect(url, DELETE, token, body);
  }

  default Map<String, Object> connect(final String url, final HttpMethod method,
      final String token) {
    return this.connect(url, method, token, null);
  }

  Map<String, Object> connect(final String url, final HttpMethod method, final String token,
      Object body);

}
