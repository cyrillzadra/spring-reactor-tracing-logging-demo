package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class DemoController {

  private final WebClient.Builder webClientBuilder;

  public DemoController(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @GetMapping
  public String get() {
    getWebClient().get().uri("https://httpbin.org/get").retrieve()
        .bodyToMono(String.class).contextCapture().block();
    return "Hello, World!";
  }

  private WebClient getWebClient() {
    return webClientBuilder.build();
  }

}
