# Getting Started

Problem: Trace context (traceId/spanId) is lost when execution moves from imperative to reactive stack in Spring Boot
WebFlux applications

Log output - once execution moves to reactor threads (reactor-http-epoll-2), the traceId and spanId become empty.

```
2025-09-15 12:36:53,576 TRACE o.s.web.reactive.function.client.ExchangeFunctions [http-nio-8080-exec-1,68c7ec45b6874bbab5fcc24f8270fa64,2b7f6f1407273b13] [d18c84b] HTTP GET https://httpbin.org/get, headers={masked}
2025-09-15 12:36:53,626 DEBUG reactor.netty.resources.PooledConnectionProvider   [http-nio-8080-exec-1,68c7ec45b6874bbab5fcc24f8270fa64,2b7f6f1407273b13] Creating a new [http] client pool [PoolFactory{evictionInterval=PT0S, leasingStrategy=fifo, maxConnections=500, maxIdleTime=-1, maxLifeTime=-1, metricsEnabled=false, pendingAcquireMaxCount=1000, pendingAcquireTimeout=45000}] for [httpbin.org/<unresolved>:443]
2025-09-15 12:36:53,657 DEBUG reactor.netty.resources.PooledConnectionProvider   [reactor-http-epoll-2,,] [88d25bd5] Created a new pooled channel, now: 0 active connections, 0 inactive connections 0 pending acquire requests.
2025-09-15 12:36:53,665 DEBUG reactor.netty.tcp.SslProvider                      [reactor-http-epoll-2,,] [88d25bd5] SSL enabled using engine SSLEngine[hostname=httpbin.org, port=443, Session(1757932613661|SSL_NULL_WITH_NULL_NULL)] and SNI httpbin.org/<unresolved>:443
2025-09-15 12:36:53,672 DEBUG reactor.netty.transport.TransportConfig            [reactor-http-epoll-2,,] [88d25bd5] Initialized pipeline DefaultChannelPipeline{(reactor.left.sslHandler = io.netty.handler.ssl.SslHandler), (reactor.left.sslReader = reactor.netty.tcp.SslProvider$SslReadHandler), (reactor.left.httpCodec = io.netty.handler.codec.http.HttpClientCodec), (reactor.left.httpDecompressor = io.netty.handler.codec.http.HttpContentDecompressor), (reactor.right.reactiveBridge = reactor.netty.channel.ChannelOperationsHandler)}
2025-09-15 12:36:53,822 DEBUG reactor.netty.transport.TransportConnector         [reactor-http-epoll-2,,] [88d25bd5] Connecting to [httpbin.org/18.215.192.30:443].
2025-09-15 12:36:53,917 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,68c7ec45b6874bbab5fcc24f8270fa64,2b7f6f1407273b13] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Registering pool release on close event for channel
2025-09-15 12:36:53,918 DEBUG reactor.netty.resources.PooledConnectionProvider   [reactor-http-epoll-2,68c7ec45b6874bbab5fcc24f8270fa64,2b7f6f1407273b13] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Channel connected, now: 1 active connections, 0 inactive connections 0 pending acquire requests.
2025-09-15 12:36:54,173 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}, [connected])
2025-09-15 12:36:54,179 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [configured])
2025-09-15 12:36:54,179 DEBUG reactor.netty.http.client.HttpClientConnect        [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Handler is being applied: {uri=https://httpbin.org/get, method=GET}
2025-09-15 12:36:54,179 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/get, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [request_prepared])
2025-09-15 12:36:54,181 DEBUG reactor.netty.http.client.HttpClientOperations     [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] No sendHeaders() called before complete, sending zero-length header
2025-09-15 12:36:54,182 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/get, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [request_sent])
2025-09-15 12:36:54,821 DEBUG reactor.netty.http.client.HttpClientOperations     [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Received response (auto-read:false) : RESPONSE(decodeResult: success, version: HTTP/1.1)
HTTP/1.1 200 OK
Date: <filtered>
Content-Type: <filtered>
Content-Length: <filtered>
Connection: <filtered>
Server: <filtered>
Access-Control-Allow-Origin: <filtered>
Access-Control-Allow-Credentials: <filtered>
2025-09-15 12:36:54,822 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/get, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [response_received])
2025-09-15 12:36:54,823 TRACE o.s.web.reactive.function.client.ExchangeFunctions [reactor-http-epoll-2,68c7ec45b6874bbab5fcc24f8270fa64,2b7f6f1407273b13] [d18c84b] [88d25bd5-1] Response 200 OK, headers={masked}
2025-09-15 12:36:54,829 DEBUG reactor.netty.channel.FluxReceive                  [reactor-http-epoll-2,68c7ec45b6874bbab5fcc24f8270fa64,b5fcc24f8270fa64] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] [terminated=false, cancelled=false, pending=0, error=null]: subscribing inbound receiver
2025-09-15 12:36:54,831 DEBUG reactor.netty.http.client.HttpClientOperations     [reactor-http-epoll-2,,] [88d25bd5-1, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Received last HTTP packet
2025-09-15 12:36:54,832 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/get, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [response_completed])
2025-09-15 12:36:54,832 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] onStateChange(GET{uri=/get, connection=PooledConnection{channel=[id: 0x88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443]}}, [disconnecting])
2025-09-15 12:36:54,832 DEBUG r.netty.resources.DefaultPooledConnectionProvider  [reactor-http-epoll-2,,] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Releasing channel
2025-09-15 12:36:54,833 DEBUG reactor.netty.resources.PooledConnectionProvider   [reactor-http-epoll-2,,] [88d25bd5, L:/172.16.44.117:35858 - R:httpbin.org/18.215.192.30:443] Channel cleaned, now: 0 active connections, 1 inactive connections 0 pending acquire requests.

```

How to reproduce this issue:

1. Start DemoApplication
2. Call the endpoint (see curl command below)

```
curl http://localhost:8080
```


