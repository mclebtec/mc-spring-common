package com.mclebtec.client.connector;

import com.mclebtec.error.domain.ErrorDomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ErrorDomainConfig.class})
@ComponentScan
public class ClientConnectorConfig {

}
