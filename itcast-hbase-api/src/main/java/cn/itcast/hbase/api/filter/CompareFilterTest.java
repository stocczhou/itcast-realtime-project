package cn.itcast.hbase.api.filter;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.junit.Test;

import java.io.IOException;

/**
 * 比较过滤器有几种？
 * RowFilter   基于RowKey的过滤
 * FamilyFilter 基于列簇的过滤
 * QualifierFilter 基于字段的过滤
 * ValueFilter 基于值的过滤
 * DependentColumnFilter 参考值过滤器
 * <p>
 * 比较运算符？
 * LESS 匹配小于设定值的值
 * LESS_OR_EQUAL 匹配小于或等于设定值的值
 * EQUAL 匹配等于设定值的值
 * NOT_EQUAL 匹配与设定值不相等的值
 * GREATER_OR_EQUAL 匹配大于或等于设定值的值
 * GREATER 匹配大于设定值的值
 * NO_OP 排除一切值
 * <p>
 * 比较器有哪些？
 * BinaryComparator 使用Bytes.compareTo()比较当前的阈值
 * BinaryPrefixComparator 与上面的相似，使用Bytes.compareTo()进行匹配，但是是从左端开始前缀匹配
 * NullComparator 不做匹配，只判断当前值不是null
 * BitComparator 通过BitWiseOp类提供的按位与（AND）、或（OR）、异或（XOR）操作执行位级比较。
 * RegexStringComparator 根据一个正则表达式，在实例化这个比较器的时候去匹配表中的数据。
 * SubStringComparator 把阈值和表中数据String实例，同时通过contains()操作匹配字符串
 * <p>
 * 注意：
 * BitComparator、RegexStringComparator、SubStringComparator三种比较器只能与EQUAL和NOT EQUAL搭配使用
 */
public class CompareFilterTest {

    @Test
    public void testCompareFilter() throws IOException {
        //创建RowFilter过滤器
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL
                , new BinaryComparator("maoxiangyi".getBytes()));
        //创建familyFilter过滤器
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.NO_OP,
                new BinaryComparator("baseInfo".getBytes()));
        //创建qualifierFilter过滤器
        QualifierFilter qualifierFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator("name".getBytes()));
        //创建valueFilter过滤器
        ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryComparator("maoxiangyi".getBytes()));

        //2、创建扫描器
        Scan scan = new Scan();
        scan.addColumn("baseInfo".getBytes(), "name".getBytes());
        //3、将过滤器设置到扫描器中
        scan.setFilter(rowFilter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }

    @Test
    public void testRowFilter() throws IOException {
        //1、创建RowFilter过滤器
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL
                , new BinaryComparator("maoxiangyi".getBytes()));
        //2、创建扫描器
        Scan scan = new Scan();
        scan.addColumn("baseInfo".getBytes(), "name".getBytes());
        //3、将过滤器设置到扫描器中
        scan.setFilter(rowFilter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }

    @Test
    public void testFamilyFilter() throws IOException {
        //1、创建FamilyFilter过滤器
        FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.NO_OP, new BinaryComparator("baseInfo".getBytes()));
        //2、创建扫描器
        Scan scan = new Scan();
        //3、将过滤器设置到扫描器中
        scan.setFilter(familyFilter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }

    @Test
    public void tesQualifierFilter() throws IOException {
        //1、创建过滤器
        QualifierFilter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator("name".getBytes()));
        //2、创建扫描器
        Scan scan = new Scan();
        //3、将过滤器设置到扫描器中
        scan.setFilter(filter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }

    @Test
    public void tesValueFilter() throws IOException {
        //1、创建过滤器
        ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator("maoxiangyi".getBytes()));
        //2、创建扫描器
        Scan scan = new Scan();
        //3、将过滤器设置到扫描器中
        scan.setFilter(filter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }

    @Test
    public void tesDependentColumnFilter() throws IOException {
        //1、创建过滤器
        Filter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator("maoxiangyi".getBytes()));
        //2、创建扫描器
        Scan scan = new Scan();
        //3、将过滤器设置到扫描器中
        scan.setFilter(filter);
        //4、获取HBase的表
        Connection connection = ConnectionUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("itcast_user_profile"));
        //5、扫描HBase的表（注意过滤操作是在服务器进行的，也即是在regionServer进行的）
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            System.out.println(r);
        }
    }


}
