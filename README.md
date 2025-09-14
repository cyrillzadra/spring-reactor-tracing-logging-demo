# Getting Started

Problem: Trace context (traceId/spanId) is lost when execution moves from imperative to reactive stack in Spring Boot
WebFlux applications

Log output - once execution moves to reactor threads (reactor-http-epoll-3), the traceId and spanId become empty.

```
2025-09-14 15:59:20,064 TRACE o.s.web.reactive.function.client.ExchangeFunctions [http-nio-8080-exec-1,68c6ca3839216185f4ca70c9a8ee1823,a98bd42951d46230] [774c1457] HTTP GET https://httpbin.org/get, headers={masked}
2025-09-14 15:59:20,164 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c] REGISTERED
2025-09-14 15:59:20,200 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c] CONNECT: httpbin.org/34.238.12.187:443
2025-09-14 15:59:20,335 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c, L:/172.16.44.117:57450 - R:httpbin.org/34.238.12.187:443] ACTIVE
2025-09-14 15:59:20,435 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c, L:/172.16.44.117:57450 - R:httpbin.org/34.238.12.187:443] READ COMPLETE
2025-09-14 15:59:20,458 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c, L:/172.16.44.117:57450 - R:httpbin.org/34.238.12.187:443] READ COMPLETE
2025-09-14 15:59:20,571 TRACE o.s.web.reactive.function.client.WebClient         [reactor-http-epoll-3,,] [9660dd0c-1, L:/172.16.44.117:57450 - R:httpbin.org/34.238.12.187:443] WRITE: 155B GET /get HTTP/1.1
```

How to reproduce this issue:

1. Start DemoApplication
2. Call the endpoint (see curl command below)

```
curl http://localhost:8080
```


