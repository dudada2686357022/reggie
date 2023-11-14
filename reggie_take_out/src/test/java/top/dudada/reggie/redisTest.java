package top.dudada.reggie;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class redisTest {

    @Test
    public void test(){
        Jedis jedis = new Jedis("localhost", 6379);

        jedis.set("username","xiaoming");

        jedis.close();

    }
}
