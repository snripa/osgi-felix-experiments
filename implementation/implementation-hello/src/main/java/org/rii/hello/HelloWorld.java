package org.rii.hello;

import org.rii.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelloWorld {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    private static NettyServer nettyServer;

    public static void main(String[] args) throws InterruptedException, IOException {
        init();
    }

    public static void init() throws InterruptedException, IOException {
        final Properties properties = new Properties();
        try (final InputStream stream =
            HelloWorld.class.getResourceAsStream("/config.properties")) {
            if(stream == null){
                LOGGER.error("Failed to read config file. Aborting");
                return;
            }
            LOGGER.info("read properties file bytes: {}",  stream.available());
            properties.load(stream);
        }
        int port = Integer.parseInt(properties.getProperty("server.port", "4800"));
        LOGGER.info("Will try to start Netty server at port {}", port);
        nettyServer = new NettyServer();
        nettyServer.start(port);
    }

    public static void destroy() {
        nettyServer.stop();

    }
}
