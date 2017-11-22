package cn.itcast.bigdata.storm.wc;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

//import backtype.storm.task.OutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.topology.base.BaseRichBolt;
//import backtype.storm.tuple.Fields;
//import backtype.storm.tuple.Tuple;
//import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by maoxiangyi.cn on 2017/3/8.
 */
public class WordCountSplitSentenceBolt extends BaseRichBolt {
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {
        //1、获取上游发送的数据 通过字段的名称获取数据
        // 问题：这里的字段名称从来那里
        //回答：在本例中从上游的 WordCountFileSpout.declareOutputFields()方法中得知
        String sentence = input.getStringByField("sentence");
        //2、将数据进行切割,得到一个封装了单词的数组
        String[] words = sentence.split(" ");
        //3、发送一个一个单词
        for (String word:words){
            //单词每出现一次，就标记次数1
            collector.emit(new Values(word,1));
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //4、定义发送数据的字段个数和字段名称
        declarer.declare(new Fields("word","num"));
    }
}