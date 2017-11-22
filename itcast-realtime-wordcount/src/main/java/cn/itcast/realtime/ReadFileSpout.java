package cn.itcast.realtime;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Spout 需要继承一个模板
 */
public class ReadFileSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    /**
     * Map conf 应用程序能够读取的配置文件
     * TopologyContext context 应用程序的上下文
     * SpoutOutputCollector collector Spout输出的数据丢给SpoutOutputCollector。
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        //1、Kafka 连接  / MYSQL 连接  /Redis 连接
        //todo
        //2、将SpoutOutputCollector复制给成员变量
        this.collector = collector;
    }

    /**
     * storm框架有个while循环，一直在nextTuple
     */
    @Override
    public void nextTuple() {
        // 发送数据，使用collector.emit方法
        // Values extends ArrayList<Object>
        collector.emit(new Values("i love u"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("biaobai"));
    }
}
