package cn.itcast.realtime.kanban.Storm;

import cn.itcast.realtime.kanban.domain.PaymentInfo;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maoxiangyi on 2017/11/13.
 */
public class ProcessBolt extends BaseRichBolt {
    private Jedis jedis;

    /**
     * 初始化方法
     * Map stormConf 应用能够得到的配置文件
     * TopologyContext context 上下文 一般没有什么用
     * OutputCollector collector 数据收集器
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        jedis = new Jedis("redis", 6379);
    }

    /**
     * 有个while不停的调用execute方法，每次调用都会发一个数据进行来。
     */
    @Override
    public void execute(Tuple input) {
        //获取上游发送的javabean
        PaymentInfo value = (PaymentInfo) input.getValue(0);
        //先计算总数据 来一条算一条
        jedis.incrBy("kanban:total:ordernum",1);
        jedis.incrBy("kanban:total:orderPrice",value.getPayPrice());
        jedis.incrBy("kanban:total:orderuser",1);

        //计算商家（店铺的销售情况）
        String shopId = value.getShopId();
        jedis.incrBy("kanban:shop:"+shopId+":ordernum",1);
        jedis.incrBy("kanban:shop:"+shopId+":orderPrice",value.getPayPrice());
        jedis.incrBy("kanban:shop:"+shopId+":orderuser",1);

        //计算每个品类（品类id）一级品类
        String Level1 = value.getLevel1();
        jedis.incrBy("kanban:shop:"+Level1+":ordernum",1);
        jedis.incrBy("kanban:shop:"+Level1+":orderPrice",value.getPayPrice());
        jedis.incrBy("kanban:shop:"+Level1+":orderuser",1);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
