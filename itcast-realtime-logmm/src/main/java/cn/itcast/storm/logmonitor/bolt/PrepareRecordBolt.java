package cn.itcast.storm.logmonitor.bolt;

import cn.itcast.storm.logmonitor.domain.Message;
import cn.itcast.storm.logmonitor.domain.Record;
import cn.itcast.storm.logmonitor.utils.MonitorHandler;
import cn.itcast.storm.logmonitor.utils.RedisPool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Describe: 将触发信息保存到mysql数据库中
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/11.
 */
//BaseRichBolt 需要手动调ack方法，BaseBasicBolt由storm框架自动调ack方法
public class PrepareRecordBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 1408316104365959776L;
    private Jedis jedis;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        jedis = RedisPool.getJedis();
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        Message message = (Message) input.getValueByField("message");
        String appId = input.getStringByField("appId");


        //将触发规则的信息进行通知-------------如何处理重复通知，相当于一个异常通知了很多遍？
        // appid，ruleid，line，keyword
        //设计思路：appid+ruleid组成唯一的key 保存到的redis中
        String key = appId + "_" + message.getRuleId();
        boolean tag = jedis.exists(key);//访问redis之后的返回值
        if (tag) {
            return;
        }
        //保存key到redis
        //设置redis的key的过期时间 10分钟
        jedis.setex(key, 60 * 10, "");



        //开始发送告警信息
        MonitorHandler.notifly(appId, message);
//        message  多了发送短信和发送邮件的状态
        Record record = new Record();
        try {
            BeanUtils.copyProperties(record, message);
            collector.emit(new Values(record));
        } catch (Exception e) {
            throw new FailedException("告警模块失败....");
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }

}
