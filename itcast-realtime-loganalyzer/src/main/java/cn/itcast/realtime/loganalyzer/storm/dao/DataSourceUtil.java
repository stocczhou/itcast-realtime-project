package cn.itcast.realtime.loganalyzer.storm.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class DataSourceUtil {
    private static Logger logger = Logger.getLogger(DataSourceUtil.class);
    private static DataSource dataSource;

    static {
        dataSource = new ComboPooledDataSource("logAnalyze");
    }

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new ComboPooledDataSource();
        }
        return dataSource;
    }

    public static void main(String[] args) {

    }
}
