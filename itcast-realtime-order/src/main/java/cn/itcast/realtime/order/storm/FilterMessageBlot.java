package cn.itcast.realtime.order.storm;

import cn.itcast.realtime.order.domain.PaymentInfo;
import com.google.gson.Gson;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Calendar;
import java.util.Map;

/**
 * Describe: 请补充类描述
 * Author:   maoxiangyi
 */
public class FilterMessageBlot extends BaseRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String paymentInfoStr  = input.getString(4);
        //将订单数据解析成JavaBean
        PaymentInfo paymentInfo = new Gson().fromJson(paymentInfoStr, PaymentInfo.class);
        // 过滤订单时间,如果订单时间在2015.11.11这天才进入下游开始计算
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 14) {
            return;
        }
        collector.emit(new Values(paymentInfoStr));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}
