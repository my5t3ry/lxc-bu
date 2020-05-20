package de.my53ry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class LcxBu {

  public LcxBu() {}

  public static void main(String[] args) {
    SpringApplication.run(LcxBu.class, args);
  }
}
