package cn.itcast.realtime.ack;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AckSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;

    //初始化方法
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    //上帝之手，循环调用，每调用过一次就发送一条消息
    public void nextTuple() {
        //生产一条数据
        String uuid = UUID.randomUUID().toString().replace("_", "");
        collector.emit(new Values("我是一条消息"),new Values("我是一条消息"));
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //定义发送的字段
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("uuid"));
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功" + msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败" + msgId);
        System.out.println("我要重发信息，啦啦啦" + msgId);
        collector.emit((List)msgId,msgId );
    }
}
