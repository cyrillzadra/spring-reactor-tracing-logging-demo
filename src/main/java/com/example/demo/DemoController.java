package com.example.demo;

import java.util.function.Function;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.observation.ObservationRegistry;
import reactor.netty.http.client.HttpClient;

@RestController
public class DemoController {

  private final WebClient.Builder webClientBuilder;

  public DemoController(WebClient.Builder webClientBuilder, ObservationRegistry observationRegistry) {
    this.webClientBuilder = webClientBuilder;
    reactor.netty.Metrics.observationRegistry(observationRegistry);
  }

  @GetMapping
  public String get() {
    getWebClient().get().uri("https://httpbin.org/get").retrieve().bodyToMono(String.class).block();
    return "Hello, World!";
  }

  private WebClient getWebClient() {
    HttpClient httpClient = HttpClient.create()
        .wiretap(true)
        .compress(true)
        .metrics(true, Function.identity())
        .doOnChannelInit(
            (obs, ch, addr) -> ch.pipeline().addFirst("nettyTraceContextHandler", new NettyTraceContextHandler()));

    return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }


}
