package de.my5t3ry.config;

import de.my5t3ry.command.backup.ListCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/** User: my5t3ry Date: 5/20/20 5:50 AM */
@Configuration(proxyBeanMethods = false)
@PropertySource(value = "classpath:application.properties", name = "default")
@PropertySource(value = "classpath:hibernate.properties", name = "hibenate")
public class Config {
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
        new PropertySourcesPlaceholderConfigurer();
    propertySourcesPlaceholderConfigurer.setLocation(
        new ClassPathResource("application.properties"));
    return propertySourcesPlaceholderConfigurer;
  }

  @Autowired public ListCommand listCommand;
}
