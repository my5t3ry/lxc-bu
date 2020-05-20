package de.my5t3ry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/** User: my5t3ry Date: 5/20/20 5:50 AM */
@Configuration(proxyBeanMethods = false)
@PropertySource(value = "classpath:application.properties", name = "default")
@PropertySource(value = "classpath:hibernate.properties", name = "hibenate")
public class Config {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {

        return new PropertySourcesPlaceholderConfigurer();
    }
}
