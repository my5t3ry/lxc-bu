package de.my53ry.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** User: my5t3ry Date: 5/20/20 4:44 AM */
public class AnnotationConfig {

  @Retention(RetentionPolicy.RUNTIME)
  public @interface Autowired {
    boolean required() default true;
  }
}
