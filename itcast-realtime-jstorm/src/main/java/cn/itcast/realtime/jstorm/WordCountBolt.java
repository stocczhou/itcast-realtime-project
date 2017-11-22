package cn.itcast.realtime.jstorm;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maoxiangyi on 2017/11/13.
 */
public class WordCountBolt extends BaseRichBolt {
    private OutputCollector collector;
    private HashMap<String, Integer> wordCountMap;

    /**
     * 初始化方法
     * Map stormConf 应用能够得到的配置文件
     * TopologyContext context 上下文 一般没有什么用
     * OutputCollector collector 数据收集器
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        //todo 连接数据 连接redis 连接hdfs
        wordCountMap = new HashMap<String, Integer>();
    }

    /**
     * 有个while不停的调用execute方法，每次调用都会发一个数据进行来。
     */
    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("word");
        Integer num = input.getIntegerByField("num");
        // 先判断这个单词是否出现过
        if (wordCountMap.containsKey(word)) {
            Integer oldNum = wordCountMap.get(word);
            wordCountMap.put(word, oldNum + num);
        } else {
            wordCountMap.put(word, num);
        }
        System.out.println(wordCountMap);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 声明 输出的是什么字段
        declarer.declare(new Fields("fenshou"));
    }
}
