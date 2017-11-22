package cn.itcast.realtime.jstorm;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * 组装应用程序--驱动类
 */
public class WordCountTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException, AlreadyAliveException {
        //1、创建一个job(topology)
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //2、设置job的详细内容
        topologyBuilder.setSpout("ReadFileSpout", new ReadFileSpout(), 1);
        topologyBuilder.setBolt("SentenceSplitBolt", new SentenceSplitBolt(), 1).shuffleGrouping("ReadFileSpout");
        topologyBuilder.setBolt("WordCountBolt", new WordCountBolt(), 1).shuffleGrouping("SentenceSplitBolt");
        //准备配置项
        Config config = new Config();
        config.setDebug(false);
        //3、提交job
        //提交由两种方式：一种本地运行模式、一种集群运行模式。
        if (args != null && args.length > 0) {
            //运行集群模式
            config.setNumWorkers(1);
            StormSubmitter.submitTopology(args[0], config, topologyBuilder.createTopology());
        } else {
            LocalCluster localCluster = new LocalCluster();
            StormTopology topology = topologyBuilder.createTopology();
            localCluster.submitTopology("wordcount", config, topology);
        }
    }
}
