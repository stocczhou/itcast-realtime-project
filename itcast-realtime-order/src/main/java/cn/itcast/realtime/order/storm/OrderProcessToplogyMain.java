package cn.itcast.realtime.order.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class OrderProcessToplogyMain {
    public static void main(String[] args) throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //used by new storm-kafka-client spout code
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder("node01:9092", "itcast_shop_order");
        builder.setGroupId("test_storm_wc");
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();
        topologyBuilder.setSpout("readOrderInfo", new KafkaSpout<String, String>(kafkaSpoutConfig), 1);
        topologyBuilder.setBolt("processIndex", new FilterMessageBlot(), 2).localOrShuffleGrouping("readOrderInfo");
        topologyBuilder.setBolt("saveResult2Redis", new Save2RedisBlot(), 2).localOrShuffleGrouping("processIndex");
        Config conf = new Config();
        conf.setDebug(true);
        if (args != null && args.length > 0) {
            conf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, topologyBuilder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("orderPorcess", conf, topologyBuilder.createTopology());
//            Utils.sleep(10000);
//            cluster.shutdown();
        }
    }
}
