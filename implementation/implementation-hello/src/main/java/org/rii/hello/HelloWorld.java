package org.rii.hello;

import org.rii.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorld.class);

    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("Initializing Galaxy protocol: started");
        LOGGER.error("Initializing Galaxy protocol: done (test error level log)");

        LOGGER.info("Starting Netty server");
        new NettyServer().start(8888);
        Thread.currentThread().join();
        LOGGER.info("Netty Server started");
    }
}
