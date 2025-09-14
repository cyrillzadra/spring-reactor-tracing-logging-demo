package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.micrometer.context.ContextRegistry;

@SpringBootApplication
public class DemoApplication {

  private static ThreadLocal<String> CORRELATION_ID = ThreadLocal.withInitial(() -> "");

	public static void main(String[] args) {

    ContextRegistry.getInstance()
        .registerThreadLocalAccessor("traceId",
            CORRELATION_ID::get,
            CORRELATION_ID::set,
            CORRELATION_ID::remove);

    SpringApplication.run(DemoApplication.class, args);
	}

}
