package cn.itcast.realtime.loganalyzer.storm.bolt;


import cn.itcast.realtime.loganalyzer.storm.domain.LogMessage;
import cn.itcast.realtime.loganalyzer.storm.utils.LogAnalyzeHandler;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

public class ProcessMessage extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LogMessage logMessage = (LogMessage) input.getValueByField("message");
        LogAnalyzeHandler.process(logMessage);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
