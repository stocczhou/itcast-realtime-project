package cn.itcast.realtime.ack;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.FailedException;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * BaseBasicBolt 自动帮你调用ack或者fail等方法。
 */
public class BaseBasicBoltTest extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
           throw  new FailedException("BaseBasicBoltTest 程序失败了");
//          等同于 BaseRichBolt的 collector.fail()
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
