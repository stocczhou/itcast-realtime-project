package cn.itcast.bigdata.storm.wc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

//import backtype.storm.Config;
//import backtype.storm.LocalCluster;
//import backtype.storm.StormSubmitter;
//import backtype.storm.generated.AlreadyAliveException;
//import backtype.storm.generated.AuthorizationException;
//import backtype.storm.generated.InvalidTopologyException;
//import backtype.storm.topology.TopologyBuilder;

/**
 * Created by maoxiangyi on 2017/3/8.
 */
public class WordCountTopologyMain {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        //1、配置用户自定义的component 以及component之间的数据流转的策略
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("WordCountFileSpout", new WordCountFileSpout(), 1);
        topologyBuilder.setBolt("WordCountSplitKafkaMessageBolt", new WordCountSplitSentenceBolt(), 1).shuffleGrouping("WordCountFileSpout");
        topologyBuilder.setBolt("WordCountCounterBolt", new WordCountCounterBolt(), 1).shuffleGrouping("WordCountSplitKafkaMessageBolt");

        //2、配置当前topology运行的参数
        Config conf = new Config();
        conf.put("the_words_file_path","E:/itcast/env/storm_home/sentence.txt");

        //3、提交任务 任务提交有两种模式 一种是本地模式 一种是集群模式
        if (args != null && args.length > 0) {
            conf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, topologyBuilder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("wordCount", conf, topologyBuilder.createTopology());
            //一旦启动永不停止 想停止，只能手动停止
//			Utils.sleep(10000);
//			cluster.shutdown();
        }
    }
}
