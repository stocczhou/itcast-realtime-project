package cn.itcast.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 *  Kafka Consumer 代码
 */
public class TestTopicConsumer {
    public static void main(String[] args) {
        // 1、准备配置文件
        Properties props = new Properties();
        props.put("bootstrap.servers", "node01:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 2、创建KafkaConsumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);
        // 3、订阅数据，这里的topic可以是多个
        kafkaConsumer.subscribe(Arrays.asList("test"));
        // 4、获取数据
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic = %s,offset = %d, key = %s, value = %s%n",record.topic(), record.offset(), record.key(), record.value());
            }
        }
    }
}
