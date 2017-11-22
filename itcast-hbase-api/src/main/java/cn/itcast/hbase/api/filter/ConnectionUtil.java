package cn.itcast.hbase.api.filter;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class ConnectionUtil {
    private static Connection connection;

    static {
        try {
            //通过读取配置连接HBase
            connection = ConnectionFactory.createConnection();
            //通过hardcode连接HBase
//            Configuration configuration = new Configuration();
//            configuration.set("hbase.zookeeper.quorum", "zk01:2181,zk02:2181,zk03:2181");
//            connection = ConnectionFactory.createConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
