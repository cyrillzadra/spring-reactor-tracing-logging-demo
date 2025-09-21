package com.example.demo;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyTraceContextHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder().build().setThreadLocalsFrom(ctx.channel())) {
      super.channelActive(ctx);
    }
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder().build().setThreadLocalsFrom(ctx.channel())) {
      super.channelRead(ctx, msg);
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder().build().setThreadLocalsFrom(ctx.channel())) {
      super.channelReadComplete(ctx);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    try (ContextSnapshot.Scope scope = ContextSnapshotFactory.builder().build().setThreadLocalsFrom(ctx.channel())) {
      super.exceptionCaught(ctx, cause);
    }
  }
}