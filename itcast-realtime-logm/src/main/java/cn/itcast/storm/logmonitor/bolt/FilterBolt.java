package cn.itcast.storm.logmonitor.bolt;

import cn.itcast.storm.logmonitor.domain.Message;
import cn.itcast.storm.logmonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Describe: 过滤规则信息
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/11.
 */
//BaseRichBolt 需要手动调ack方法，BaseBasicBolt由storm框架自动调ack方法
public class FilterBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 4403082848102076593L;
    private static Logger logger = Logger.getLogger(FilterBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        //获取KafkaSpout发送出来的数据
        String line = input.getString(4);
        //对数据进行解析
        // appid   content
        //aid:1||msg:error java.lang.StackOverflowError
        Message message = MonitorHandler.parser(line);
        if (message == null) {
            return;
        }
        //判断规则，成功之后，将message对象发送的下游。
        //此时的message对象中包含：line,appId,ruleId,keyword
        if (MonitorHandler.trigger(message)) {
            collector.emit(new Values(message.getAppId(), message));
        }

        //定时更新规则信息 每十分钟更新一次规则库
        //1\每十分钟加载一次   通过计算分钟值，如果取模等于0就开始加载数据 reloaded
        // 2\一个worker中多个执行器只加载一次  加锁  reloaded
        //标识reloaded   在非10分钟的是一指被修改为true
        //               在10分钟加载一次的时候，如果第一个线程加载了数据，会将reload改为false。
        MonitorHandler.scheduleLoad();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("appId", "message"));
    }
}
