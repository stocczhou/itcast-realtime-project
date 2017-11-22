package cn.itcast.storm.logmonitor.bolt;

import cn.itcast.storm.logmonitor.domain.Record;
import cn.itcast.storm.logmonitor.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/11.
 */
public class SaveMessage2MySql extends BaseBasicBolt {
    private static final long serialVersionUID = 1607569271861767853L;
    private static Logger logger = Logger.getLogger(SaveMessage2MySql.class);
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField("record");
        //目前的状态是来一次保存一次数据，能否实现批量保存？
        //如果是你来做，如何做？
        MonitorHandler.save(record);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}

