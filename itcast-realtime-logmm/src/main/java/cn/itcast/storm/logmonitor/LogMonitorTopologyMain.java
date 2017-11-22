package cn.itcast.storm.logmonitor;

import cn.itcast.storm.logmonitor.bolt.FilterBolt;
import cn.itcast.storm.logmonitor.bolt.PrepareRecordBolt;
import cn.itcast.storm.logmonitor.bolt.SaveMessage2MySql;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

/**
 * Describe: 日志监控系统驱动类
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/10.
 */
public class LogMonitorTopologyMain {

    public static void main(String[] args) throws Exception {
        // 使用TopologyBuilder进行构建驱动类
        // 1. 创建TopologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        // 2.设置 KafkaSpout
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder("node01:9092","log_monitor");
        builder.setGroupId("itcast_logmonitor_group");
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();
        topologyBuilder.setSpout("WordCountFileSpout",new KafkaSpout<String,String>(kafkaSpoutConfig), 1);
       // 3.设置bolt
        topologyBuilder.setBolt("filter-bolt", new FilterBolt(), 1).localOrShuffleGrouping("WordCountFileSpout");
        topologyBuilder.setBolt("prepareRecord-bolt", new PrepareRecordBolt(), 1).fieldsGrouping("filter-bolt", new Fields("appId"));
        topologyBuilder.setBolt("saveMessage-bolt", new SaveMessage2MySql(), 1).localOrShuffleGrouping("prepareRecord-bolt");

        //4.启动topology的配置信息
        Config conf = new Config();
        //TOPOLOGY_DEBUG(setDebug), 当它被设置成true的话， storm会记录下每个组件所发射的每条消息。
        //这在本地环境调试topology很有用， 但是在线上这么做的话会影响性能的。
        conf.setDebug(false);
        //storm的运行有两种模式: 本地模式和分布式模式.
        if (args != null && args.length > 0) {
            //定义你希望集群分配多少个工作进程给你来执行这个topology
            conf.setNumWorkers(1);
            //向集群提交topology
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, topologyBuilder.createTopology());
        } else {
            //本地测试用的，表示一个组件最多给三个并发度，不管你代码中设置多少个并发度，哪怕是100个并发度。
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("LogMonitorTopology", conf, topologyBuilder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}
