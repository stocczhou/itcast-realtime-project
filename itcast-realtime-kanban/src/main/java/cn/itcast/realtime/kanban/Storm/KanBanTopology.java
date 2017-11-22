package cn.itcast.realtime.kanban.Storm;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 组装应用程序--驱动类
 */
public class KanBanTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //1、创建一个job(topology)
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //2、设置job的详细内容
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder("node01:9092","itcast_shop_order");
        builder.setGroupId("bigdata_kanban_order");
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();
        topologyBuilder.setSpout("KafkaSpout",new KafkaSpout<String,String>(kafkaSpoutConfig), 1);
        topologyBuilder.setBolt("ETLBolt",new ETLBolt(),1).shuffleGrouping("KafkaSpout");
        topologyBuilder.setBolt("ProcessBolt",new ProcessBolt(),1).shuffleGrouping("ETLBolt");
        //准备配置项
        Config config = new Config();
        config.setDebug(false);
        //3、提交job
        //提交由两种方式：一种本地运行模式、一种集群运行模式。
        if (args != null && args.length > 0) {
            //运行集群模式
            config.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        } else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("KanBanTopology", config, topologyBuilder.createTopology());
        }
    }
}
