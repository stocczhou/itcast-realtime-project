package cn.itcast.realtime.flume.log;

import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;

/**
 * Created by maoxiangyi on 2017/11/15.
 */
public class MyRollingFileAppender extends RollingFileAppender
{
    public boolean isAsSevereAsThreshold(Priority priority)
    {
        //只判断是否相等，而不判断优先级。也就是说只保存一种数据。
        return getThreshold().equals(priority);
    }
}
