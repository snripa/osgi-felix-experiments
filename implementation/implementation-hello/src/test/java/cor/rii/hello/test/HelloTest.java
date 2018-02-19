package cor.rii.hello.test;

import org.junit.Test;
import org.rii.NettyServer;

public class HelloTest {


    @Test
    public void testInitNetty() throws Exception {
        new NettyServer();
    }

}
