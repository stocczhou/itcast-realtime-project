package cn.itcast.redis.api;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import redis.clients.jedis.BinaryJedisCluster;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

public class JedisMain {

    public static BinaryJedisCluster initJedisCluster() {
        //只给集群里一个实例就可以
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("redis", 6380));
        jedisClusterNodes.add(new HostAndPort("redis", 6381));
        return new BinaryJedisCluster(jedisClusterNodes);
    }
    public static void insertData2Redis() {
        BinaryJedisCluster jedisCluster = initJedisCluster();
        Stopwatch  stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10; i++) {
            String key = "key:" + i;
            jedisCluster.setex(key.getBytes(), 60 * 60, "value".getBytes());
        }
        System.out.println("time=" + stopwatch.toString());
    }

    public static void main(String[] args) {
        insertData2Redis();
    }

}
