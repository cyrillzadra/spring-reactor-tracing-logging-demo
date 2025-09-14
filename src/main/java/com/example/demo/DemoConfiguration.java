package com.example.demo;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Hooks;

@Configuration
public class DemoConfiguration {


  @PostConstruct
  public void init() {
    Hooks.enableAutomaticContextPropagation();
  }

}
