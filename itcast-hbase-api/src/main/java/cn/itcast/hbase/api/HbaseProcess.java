package cn.itcast.hbase.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoxiangyi on 2017/11/20.
 */
public class HbaseProcess {
    public static void main(String[] args) throws IOException {
        //1.连接服务器 服务在哪里？
        //  hbase的两种连接方式：1）读取配置文件 只需要配置zookeeper 2）通过代码配置
        Configuration config = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(config);
//        managerTable(connection);
        //----------------对表进行操作-------------------
        Table yun011 = connection.getTable(TableName.valueOf("yun011"));
        //put 'tab_001','rowkey0001','cf1:bbbbb','value'
        Put put = new Put(Bytes.toBytes("rowkey0001"));
        put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("bbbb"), Bytes.toBytes("value"));
        yun011.put(put);
        //
        List<Put> list = new ArrayList<Put>();
        for (int i = 0; i < 10; i++) {
            Put put1 = new Put(Bytes.toBytes("rowkey0001"));
            put1.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("bbbb" + i), Bytes.toBytes("value" + i));
            list.add(put1);
        }
        yun011.put(list);

        Get get = new Get(Bytes.toBytes("rowkey0001"));
        Result result = yun011.get(get);
        List<Cell> columnCells = result.getColumnCells(Bytes.toBytes("cf1"), Bytes.toBytes("bbbb1"));
//        for (Cell columnCell : columnCells) {
//            System.out.println(new String(columnCell.getFamilyArray()));
//            System.out.println(Bytes.toString(columnCell.getFamilyArray()));
//            System.out.println(new String(columnCell.getFamilyArray(),"utf-8"));
//        }
        for (Cell c : result.rawCells()) {
            System.out.println(Bytes.toString(CellUtil.cloneRow(c))
                    + "==> " + Bytes.toString(CellUtil.cloneFamily(c))
                    + "{" + Bytes.toString(CellUtil.cloneQualifier(c))
                    + ":" + Bytes.toString(CellUtil.cloneValue(c)) + "}");
        }

//        for (KeyValue kv : result.list()) {
//            System.out.println("row:" + Bytes.toString(kv.getRow()));
//            System.out.println("family:" + Bytes.toString(kv.getFamily()));
//            System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
//            System.out.println("value:" + Bytes.toString(kv.getValue()));
//            System.out.println("timestamp:" + kv.getTimestamp());
//            System.out.println("-------------------------------------------");
//        }


    }

    private static void managerTable(Connection connection) throws IOException {
        //2. 创建表
        Admin admin = connection.getAdmin();
        if (admin.tableExists(TableName.valueOf("yun011"))) {
            //删除表，先禁用这个表，在drop
            admin.disableTable(TableName.valueOf("yun011"));
            admin.deleteTable(TableName.valueOf("yun011"));
            System.out.println("------------删除表-------------");
        }
        //建表的时候，必须有个列簇
        HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf("yun011"));
        descriptor.addFamily(new HColumnDescriptor("cf1"));
        admin.createTable(descriptor);
        System.out.println("---------------------建表成功-----------------");
        //4.修改表
        //添加一个列簇
        HTableDescriptor tableDescriptor = admin.getTableDescriptor(TableName.valueOf("yun011"));
        tableDescriptor.addFamily(new HColumnDescriptor("cf2"));
        System.out.println("----------又添加了一个列簇----------");
    }
}
