package cn.itcast.redis.api.boolmfilter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.BitSet;

/**
 * Created by maoxiangyi on 2017/11/20.
 */
public class Test {
    public static void main(String[] args) {
        Jedis jedis =new Jedis("redis",6379);
        //index1 ----> 用户记录某个指标的uv。
        // set
        jedis.sadd("index1:uv","陪你去看海-----用户1");
        jedis.sadd("index1:uv","用户2");
        jedis.sadd("index1:uv","用户3");
        jedis.sadd("index1:uv","用户4");
        jedis.sadd("index1:uv","用户5");
        jedis.sadd("index1:uv","用户6");
        Long num = jedis.scard("index1:uv");
        System.out.println(num);

        jedis.set("index1".getBytes(),new byte[10000]);
        byte[] bytes = jedis.get("index1".getBytes());

        BloomFilter4Redis bloomFilter4Redis = new BloomFilter4Redis<String>(byteArray2BitSet(bytes),0.001,1,8);
        for (int i = 0; i < 100; i++) {
            //插入元素
            bloomFilter4Redis.add("user"+i);
        }
        for (int i = 0; i < 100; i++) {
            boolean b = bloomFilter4Redis.contains("user" + i);
            System.out.println(b);
        }
        for (int i = 0; i < 10; i++) {
            boolean b = bloomFilter4Redis.contains("us1er" + i);
            System.out.println(b);
        }

    }

    public static BitSet byteArray2BitSet(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true
                        : false);
            }
        }
        return bitSet;
    }
}
