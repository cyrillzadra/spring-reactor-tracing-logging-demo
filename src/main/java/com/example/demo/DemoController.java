package com.example.demo;

import java.util.function.Function;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.observation.ObservationRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        .doOnChannelInit((obs, ch, addr) -> ch.pipeline().addFirst("test", new ChannelInboundHandlerAdapter() {

          @Override
          public void channelActive(ChannelHandlerContext ctx) throws Exception {
            try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder()
                .build()
                .setThreadLocalsFrom(ctx.channel())) {
              super.channelActive(ctx);
            }
          }

          @Override
          public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder()
                .build()
                .setThreadLocalsFrom(ctx.channel())) {
              super.channelRead(ctx, msg);
            }
          }

          @Override
          public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder()
                .build()
                .setThreadLocalsFrom(ctx.channel())) {
              super.channelReadComplete(ctx);
            }
          }

          @Override
          public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder()
                .build()
                .setThreadLocalsFrom(ctx.channel())) {
              super.exceptionCaught(ctx, cause);
            }
          }
        }));

    return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

}
