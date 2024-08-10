package com.mclebtec.document.processor.config;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {

  @Bean
  @Description("Thymeleaf template resolver serving HTML")
  public ClassLoaderTemplateResolver htmlTemplateLoader() {
    ClassLoaderTemplateResolver htmlTemplateLoader = new ClassLoaderTemplateResolver();
    htmlTemplateLoader.setPrefix("/");
    htmlTemplateLoader.setSuffix(".html");
    htmlTemplateLoader.setTemplateMode(HTML);
    htmlTemplateLoader.setCharacterEncoding(UTF_8.name());
    htmlTemplateLoader.setOrder(1);
    return htmlTemplateLoader;
  }

  @Bean
  public SpringTemplateEngine templateEngine(final ClassLoaderTemplateResolver htmlTemplateLoader) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(htmlTemplateLoader);
    return templateEngine;
  }

}
