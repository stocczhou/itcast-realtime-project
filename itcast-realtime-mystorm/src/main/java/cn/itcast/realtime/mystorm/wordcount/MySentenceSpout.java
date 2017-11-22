package cn.itcast.realtime.mystorm.wordcount;


import cn.itcast.realtime.mystorm.core.BaseSpout;
import cn.itcast.realtime.mystorm.core.MyContext;
import cn.itcast.realtime.mystorm.core.MySpoutOutputCollector;

import java.util.Map;
import java.util.Random;

/**
 * Created by maoxiangyi on 2016/6/7.
 */
public class MySentenceSpout extends BaseSpout {
    private MyContext context;
    private MySpoutOutputCollector collector;
    private Random rand;
    public void open(Map conf, MyContext context, MySpoutOutputCollector collector) {
        this.context = context;
        this.collector = collector;
        this.rand = new Random();
    }

    public void nextTuple() {
        String[] sentences = new String[] { "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon", "the cow jumped over the moon" };
        String sentence = sentences[rand.nextInt(sentences.length)];
        collector.emit(sentence);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
