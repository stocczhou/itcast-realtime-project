package cn.itcast.bigdata.kafka;

/**
 * Created by maoxiangyi on 2017/11/10.
 */
public class FindCGPartition {
    public static void main(String[] args) {
        System.out.println(Math.abs("console-consumer-57186".hashCode()) % 50);
    }
}
