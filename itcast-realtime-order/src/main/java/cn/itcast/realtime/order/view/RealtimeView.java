package cn.itcast.realtime.order.view;

import redis.clients.jedis.Jedis;

public class RealtimeView {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis", 6379);
        while (true) {
            System.out.println("-------------实时订单统计---------------");
            //计算订单的总数
            System.out.println("-总订单数-" + jedis.get("orderTotalNum"));
            //计算总的销售额
            System.out.println("-总订单金额-" + jedis.get("orderTotalPrice"));
            //计算折扣后的销售额
            System.out.println("-优惠成交价-" + jedis.get("orderPromotionPrice"));
            //计算实际交易额
            System.out.println("-实际支付价格-" + jedis.get("orderTotalRealPay"));
            System.out.println("-总订单人数-" + jedis.get("userNum"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
