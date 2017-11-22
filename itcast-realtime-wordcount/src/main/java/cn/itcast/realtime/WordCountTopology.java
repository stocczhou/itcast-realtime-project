package cn.itcast.realtime;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;

/**
 * 组装应用程序--驱动类
 */
public class WordCountTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //1、创建一个job(topology)
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //2、设置job的详细内容
        topologyBuilder.setSpout("ReadFileSpout",new ReadFileSpout(),1);
        topologyBuilder.setBolt("SentenceSplitBolt",new SentenceSplitBolt(),1).shuffleGrouping("ReadFileSpout");
        topologyBuilder.setBolt("WordCountBolt",new WordCountBolt(),1).shuffleGrouping("SentenceSplitBolt");
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
            localCluster.submitTopology("wordcount", config, topologyBuilder.createTopology());
        }
    }
}
