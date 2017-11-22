package cn.itcast.realtime.jstorm;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by maoxiangyi on 2017/11/13.
 */
public class SentenceSplitBolt extends BaseRichBolt {
    private OutputCollector collector;
    /**
     *  初始化方法
     *  Map stormConf 应用能够得到的配置文件
     *  TopologyContext context 上下文 一般没有什么用
     *  OutputCollector collector 数据收集器
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        //todo 连接数据 连接redis 连接hdfs
    }

    /**
     *  有个while不停的调用execute方法，每次调用都会发一个数据进行来。
     */
    @Override
    public void execute(Tuple input) {
//        String sentence = input.getString(0);
        // 底层先通过 biaobai 这个字段在map中找到对应的index角标值，然后再valus中获取对应数据。
        String sentence = input.getStringByField("biaobai");
        // todo 切割
        String[] strings = sentence.split(" ");
        for (String word : strings) {
            // todo 输出数据
            collector.emit(new Values(word,1));
        }
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 声明 输出的是什么字段
        declarer.declare(new Fields("word","num"));
    }
}
