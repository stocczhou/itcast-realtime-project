package cn.itcast.bigdata.storm.wc;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

//import backtype.storm.spout.SpoutOutputCollector;
//import backtype.storm.task.TopologyContext;
//import backtype.storm.topology.OutputFieldsDeclarer;
//import backtype.storm.topology.base.BaseRichSpout;
//import backtype.storm.tuple.Fields;
//import backtype.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by maoxiangyi.cn on 2017/3/8.
 * <p>
 * WordCountFileSpout 需要继承
 */
public class WordCountFileSpout extends BaseRichSpout {
    private SpoutOutputCollector spoutOutputCollector;
    private BufferedReader bufferedReader;

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
        //1、获取文件路径信息，文件的路径从驱动类中传送过来
        String filePath = (String) map.get("the_words_file_path");
        //2、创建一个BufferedReader来读取数据信息
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void nextTuple() {
        String sentence;
        try {
            //3、一遍读取数据一遍发送数据
            if (bufferedReader != null) {
                while ((sentence = bufferedReader.readLine()) != null) {
                    spoutOutputCollector.emit(new Values(sentence));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //4、定义发送出去的数据有几个字段、字段叫做什么
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }
}