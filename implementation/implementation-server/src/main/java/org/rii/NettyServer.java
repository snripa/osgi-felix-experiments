package org.rii;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer {
    private static Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private ServerBootstrap bootstrap;
    private ChannelFuture channelFuture;

    public boolean start(int port) {
        int bossThreads = 1;
        int workerThreads = 4;

        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreads, new DefaultThreadFactory("boss", true));
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(workerThreads, new DefaultThreadFactory("io", true));
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, eventLoopGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childHandler(new HttpChannelInitializer());

        InetSocketAddress socketAddress = new InetSocketAddress(port);
        channelFuture = bootstrap.bind(socketAddress).syncUninterruptibly();
        LOGGER.info("TCP server is started on port:" + port);
        return true;
    }

    public void stop() {
        bootstrap.config().group().shutdownGracefully().syncUninterruptibly();
        bootstrap.config().childGroup().shutdownGracefully().syncUninterruptibly();
        LOGGER.info("TCP server is stopped");
    }
}
