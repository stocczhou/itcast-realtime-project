package cn.itcast.hbase.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * 操作表：创建表、修改表、删除表
 */
public class OperationTable {

    public static void main(String[] args) throws IOException {
        // 1.连接HBase
        // 1.1 HBaseConfiguration.create(); 有两种方式获得配置文件，一种是手动配置，一种是读取配置文件
        Configuration config = HBaseConfiguration.create();
        // 1.2 创建一个连接
        Connection connection = ConnectionFactory.createConnection(config);
        // 1.3 从连接中获得一个Admin对象
        Admin admin = connection.getAdmin();
        // 2.创建表
        // 2.1 判断表是否存在
        TableName tableName = TableName.valueOf("yun011");
        if (!admin.tableExists(tableName)) {
            //2.2 如果表不存在就创建一个表
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            hTableDescriptor.addFamily(new HColumnDescriptor("cf1"));
            admin.createTable(hTableDescriptor);
            System.out.println("创建表");
        }
    }
}
