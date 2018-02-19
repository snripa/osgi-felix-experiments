package org.rii;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

class HttpChannelInitializer extends ChannelInitializer {


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        pipeline.addLast("pingRestHandler", new PingRequestHandler());
    }
}
