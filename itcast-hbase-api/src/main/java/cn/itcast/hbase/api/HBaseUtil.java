package cn.itcast.hbase.api;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HBaseUtil {
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

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param family    列簇
     * @throws Exception
     */
    public static void createTable(String tableName, String... family)
            throws Exception {
        if (StringUtils.isBlank(tableName) || ArrayUtils.isEmpty(family)) {
            throw new RuntimeException("参数非法");
        }
        Admin admin = connection.getAdmin();
        TableName hBaseTableName = TableName.valueOf(tableName);
        if (admin.tableExists(hBaseTableName)) {
            throw new RuntimeException("table Exists!");
        }
        HTableDescriptor desc = new HTableDescriptor(hBaseTableName);
        for (int i = 0; i < family.length; i++) {
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(family[i]);
            columnDescriptor.setVersions(3,5);
            desc.addFamily(columnDescriptor);
        }
        admin.createTable(desc);
    }

    public static void deleteTable(String tableName) throws IOException {
        Admin admin = connection.getAdmin();
        TableName hBaseTableName = TableName.valueOf(tableName);
        admin.disableTable(hBaseTableName);
        admin.deleteTable(hBaseTableName);
    }

    /**
     * 增加数据
     *
     * @param rowKey           行键
     * @param tableName        表名
     * @param family           列簇
     * @param fieldAndValueMap 列簇中的字段（列）和名称
     * @throws IOException
     */
    public static void insert( String tableName,String rowKey, String family, Map<String, String> fieldAndValueMap)
            throws IOException {
        //检查字段是否合法
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(family) || fieldAndValueMap == null || fieldAndValueMap.size() == 0) {
            throw new RuntimeException("参数非法");
        }
        //准备数据
        byte[] familyBytes = family.getBytes();
        List<Put> putArrayList = new ArrayList<Put>();
        for (Map.Entry entry : fieldAndValueMap.entrySet()) {
            Put put = new Put(Bytes.toBytes(rowKey));
            String field = (String) entry.getKey();
            String value = (String) entry.getValue();
            put.addColumn(familyBytes, field.getBytes(), value.getBytes());
            putArrayList.add(put);
        }
        //添加数据
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        table.put(putArrayList);
    }

    public static void deleteColumn(String tableName, String rowKey,
                                    String familyName, String columnName) throws IOException {
        //准备删除语句
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(familyName.getBytes(), columnName.getBytes());
        //删除字段（列）
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        table.delete(delete);
    }

    public static void deleteAllColumn(String tableName, String rowKey)
            throws IOException {
        //准备删除语句
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        //删除字段（列）
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        table.delete(delete);
    }


    /**
     * 更新某一个字段（列）的数据
     *
     * @param tableName  表名
     * @param rowKey     行键
     * @param familyName 列簇
     * @param columnName 字段（列）
     * @param value      要更新的值
     * @throws IOException
     */
    public static void updateColumn(String tableName, String rowKey,
                                    String familyName, String columnName, String value)
            throws IOException {
        //准备数据
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(familyName.getBytes(), columnName.getBytes(), value.getBytes());
        //更新数据
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        table.put(put);
    }

    /**
     * 获取一条记录的信息
     *
     * @param tableName 表名
     * @param rowKey    行键
     * @return
     * @throws IOException
     */
    public static List<Cell> getSingleRowKeyResult(String tableName, String rowKey)
            throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        return getSingleRowKeyResult(tableName, get);
    }

    /**
     * 查询表中某一个列簇的数据
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列簇
     * @throws IOException
     */
    public static List<Cell> getSingleRowKeyResultByFamily(String tableName, String rowKey,
                                                           String familyName) throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addFamily(familyName.getBytes()); // 获取指定列族和列修饰符对应的列
        return getSingleRowKeyResult(tableName, get);
    }

    /**
     * 查询表中某一个列簇的某一个字段（列）
     *
     * @param tableName  表名
     * @param rowKey     rowKey
     * @param familyName 列簇
     * @param columnName 字段（列）名
     * @throws IOException
     */
    public static List<Cell> getSingleRowKeyResultByColumn(String tableName, String rowKey,
                                                           String familyName, String columnName) throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
        return getSingleRowKeyResult(tableName, get);
    }

    public static List<Cell> getSingleRowKeyResultByVersion(String tableName, String rowKey,
                                                            String familyName, String columnName) throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        get.setMaxVersions();
        return getSingleRowKeyResult(tableName, get);
    }

    private static List<Cell> getSingleRowKeyResult(String tableName, Get get) throws IOException {
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        Result result = table.get(get);
        return result == null ? null : result.listCells();
    }

    /**
     * 获取表中所有的数据
     * 注意：大表慎用此方法。线上的数据量可能会很大
     *
     * @param tableName 表名
     * @throws IOException
     */
    public static List<Cell> getBatchResultScanByDefaultScan(String tableName) throws IOException {
        Scan scan = new Scan();
        List<Cell> cells = getBatchResult(tableName, scan);
        return cells;
    }


    /**
     * 按照rowKey的范围进行查询
     *
     * @param tableName   表名
     * @param startRowKey 起始Key
     * @param stopRowKey  终止Key
     * @throws IOException
     */
    public static List<Cell> getBatchResultByArea(String tableName, String startRowKey,
                                                  String stopRowKey) throws IOException {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(startRowKey));
        scan.setStopRow(Bytes.toBytes(stopRowKey));
        return getBatchResult(tableName, scan);
    }

    private static List<Cell> getBatchResult(String tableName, Scan scan) throws IOException {
        List<Cell> cells = new ArrayList<Cell>();
        ResultScanner rs = null;
        TableName hBaseTableName = TableName.valueOf(tableName);
        Table table = connection.getTable(hBaseTableName);
        try {
            rs = table.getScanner(scan);
            Result next = rs.next();
            while (next != null) {
                cells.addAll(next.listCells());
                next = rs.next();
            }
        } finally {
            rs.close();
        }
        return cells;
    }

}
