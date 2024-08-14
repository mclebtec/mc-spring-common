package com.mclebtec.client.connector.it;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mclebtec.client.connector.ClientConnectorConfig;
import com.mclebtec.client.connector.service.ClientConnectorService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ClientConnectorConfig.class)
@ContextConfiguration(classes = {ClientConnectorIT.class})
class ClientConnectorIT {

  @LocalServerPort
  private int port;

  @Autowired
  @Qualifier("clientHttpConnectorService")
  private ClientConnectorService clientHttpConnectorService;

  @Autowired
  @Qualifier("clientTemplateConnectorService")
  private ClientConnectorService clientTemplateConnectorService;

  @Autowired
  private Set<ClientConnectorService> clientConnectorServices;


  @Test
  void get_DataWithoutToken_Success() {
    //WHEN

    final Map<String, Object> response = clientTemplateConnectorService
        .get("https://dummy.restapiexample.com/api/v1/employees");

    //THEN
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("success", response.get("status")),
        () -> assertEquals("Successfully! All records has been fetched.", response
            .get("message"))
    );
  }

  @Test
  void post_DataWithoutToken_Success() {

    //WHEN
    final Map<String, Object> response = clientTemplateConnectorService.post(
        "https://dummy.restapiexample.com/api/v1/create", new HashMap<String, String>() {
          {
            put("name", "test");
            put("salary", "123");
            put("age", "23");
          }
        });

    //THEN
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("success", response.get("status")),
        () -> assertEquals("Successfully! Record has been added.", response.get("message"))
    );
  }

}
