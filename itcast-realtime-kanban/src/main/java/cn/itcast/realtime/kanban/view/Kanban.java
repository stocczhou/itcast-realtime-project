package cn.itcast.realtime.kanban.view;

import redis.clients.jedis.Jedis;

/**
 * Created by maoxiangyi on 2017/11/14.
 */
public class Kanban {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis",6379);
        while (true){
            System.out.println("kanban:total:ordernum 指标是"+jedis.get("kanban:total:ordernum"));
            System.out.println("kanban:total:orderPrice指标是"+jedis.get("kanban:total:orderPrice"));
            System.out.println("kanban:total:orderuser指标是"+jedis.get("kanban:total:orderuser"));
            System.out.println("---------------------------");
            System.out.println();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
