package cn.itcast.realtime.kanban.Storm;

import cn.itcast.realtime.kanban.domain.PaymentInfo;
import com.google.gson.Gson;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Created by maoxiangyi on 2017/11/13.
 */
public class ETLBolt extends BaseRichBolt {
    private OutputCollector collector;

    /**
     * 初始化方法
     * Map stormConf 应用能够得到的配置文件
     * TopologyContext context 上下文 一般没有什么用
     * OutputCollector collector 数据收集器
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 有个while不停的调用execute方法，每次调用都会发一个数据进行来。
     */
    @Override
    public void execute(Tuple input) {
        String json = input.getString(4);
        json = input.getStringByField("value");
        // 将json串转成 Java对象
        Gson gson = new Gson();
        PaymentInfo paymentInfo = gson.fromJson(json, PaymentInfo.class);
        // 其它的操作，比如说根据商品id查询商品的一级分类，二级分类，三级分类
        if(paymentInfo!=null){
            collector.emit(new Values(paymentInfo));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 声明 输出的是什么字段
        declarer.declare(new Fields("paymentInfo"));
    }
}
