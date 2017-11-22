package cn.itcast.realtime.ack;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

public class AckTopology {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //1、准备任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new AckSpout(), 1);
        topologyBuilder.setBolt("bolt1", new Bolt1(),1).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("bolt2", new Bolt2(),1).shuffleGrouping("bolt1");
        topologyBuilder.setBolt("bolt3", new Bolt3(),1).shuffleGrouping("bolt2");
        topologyBuilder.setBolt("bolt4", new Bolt4(),1).shuffleGrouping("bolt3");

        Config config = new Config();
        config.setMaxSpoutPending(1000);
        //当前topology上启动几个ackbolt。默认每个worker上是一个。
        config.setNumWorkers(2);
        config.setDebug(false);
        StormTopology stormTopology = topologyBuilder.createTopology();
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount", config, stormTopology);

    }
}
