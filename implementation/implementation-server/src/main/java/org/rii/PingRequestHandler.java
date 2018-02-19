package org.rii;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;

import java.io.OutputStream;
import java.util.Date;

public class PingRequestHandler extends ChannelInboundHandlerAdapter {

    private static final String PING_PATH = "/ping";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpRequest request = (HttpRequest) msg;
        String uri = request.uri();
        if (uri != null && uri.startsWith(PING_PATH)) {
            handlePing(request, ctx);
            ReferenceCountUtil.release(msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void handlePing(HttpRequest request, ChannelHandlerContext ctx) {

        Health resp = new Health().setHealthy(true);

        HttpResponse response;

        ByteBuf content = ctx.alloc().buffer();
        try {
            objectMapper.writeValue((OutputStream) new ByteBufOutputStream(content), resp);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        } catch (Exception e) {
            System.err.println("Failed to serialize message, cause:" + e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaderNames.DATE, new Date());

        // Decide whether to close the connection or not.
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            HttpUtil.setContentLength(response, content.readableBytes());
            HttpUtil.setKeepAlive(response, true);
        }

        ChannelFuture future = ctx.writeAndFlush(response);

        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
