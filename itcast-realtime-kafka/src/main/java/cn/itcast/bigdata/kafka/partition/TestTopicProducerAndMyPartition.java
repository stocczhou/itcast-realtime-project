package cn.itcast.bigdata.kafka.partition;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Kafka producer生产代码
 */
public class TestTopicProducerAndMyPartition {
    public static void main(String[] args) throws InterruptedException {
        //1、准备配置文件
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("partitioner.class","cn.itcast.bigdata.kafka.partition.MyPartition");

        //2、创建KafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);
        for (int i=0;i<100;i++){
            //3、发送数据
            kafkaProducer.send(new ProducerRecord<String, String>("test",0,"key01"+i,"value"+i));
            Thread.sleep(1000);
        }
    }
}
