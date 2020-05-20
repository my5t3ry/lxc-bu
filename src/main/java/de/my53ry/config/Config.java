package de.my53ry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/** User: my5t3ry Date: 5/20/20 5:50 AM */
@Configuration(proxyBeanMethods = false)
@PropertySource(value = "classpath:application.properties", name = "default")
public class Config {}
