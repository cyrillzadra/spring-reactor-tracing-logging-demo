package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class DemoController {


  @GetMapping
  public String get() {
    getWebClient().get().uri("https://httpbin.org/get").retrieve()
        .bodyToMono(String.class).contextCapture().block();
    return "Hello, World!";
  }

  private static WebClient getWebClient() {
    return WebClient.builder().build();
  }

}
