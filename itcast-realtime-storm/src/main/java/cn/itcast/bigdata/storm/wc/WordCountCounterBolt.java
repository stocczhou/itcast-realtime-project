package cn.itcast.bigdata.storm.wc;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

//import backtype.storm.task.OutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.topology.base.BaseRichBolt;
//import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maoxiangyi.cn on 2017/3/8.
 */
public class WordCountCounterBolt extends BaseRichBolt {
    private Map<String, Integer> wordCountMap;
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.wordCountMap = new HashMap<String, Integer>();
    }

    public void execute(Tuple input) {
        //1、获取上游发送的数据 通过字段的名称获取数据
        String word = input.getStringByField("word");
        Integer num = input.getIntegerByField("num");
        //2、计算单词出现的次数
        Integer oldNum = wordCountMap.get(word);
        if (oldNum != null) {
            wordCountMap.put(word, oldNum);
        } else {
            wordCountMap.put(word, num);
        }
        //3、打印 Map中的元素
        System.out.println(wordCountMap);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //4、由于 WordCountCounterBolt 不再朝下游发送数据，所以这里可以不用写
    }
}