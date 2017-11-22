package cn.itcast.realtime.loganalyzer.storm;


import cn.itcast.realtime.loganalyzer.storm.bolt.MessageFilterBolt;
import cn.itcast.realtime.loganalyzer.storm.bolt.ProcessMessage;
import cn.itcast.realtime.loganalyzer.storm.spout.RandomSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.Utils;

public class LogAnalyzeTopologyMain {
    public static void main(String[] args) throws  Exception{
        TopologyBuilder builder = new TopologyBuilder();
        // 编写一个随机的Spout，模拟发送数据。 如果要真实上限，需要有数据生产到Kafka
        builder.setSpout("kafka-spout", new RandomSpout(), 2);
        builder.setBolt("MessageFilter-bolt",new MessageFilterBolt(),3).shuffleGrouping("kafka-spout");
        builder.setBolt("ProcessMessage-bolt",new ProcessMessage(),2).fieldsGrouping("MessageFilter-bolt", new Fields("type"));
        Config topologConf = new Config();
        if (args != null && args.length > 0) {
            topologConf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, builder.createTopology());
        } else {
            topologConf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("LogAnalyzeTopologyMain", topologConf, builder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}
