package cn.itcast.realtime.loganalyzer.app.callback;



import cn.itcast.realtime.loganalyzer.app.domain.BaseRecord;
import cn.itcast.realtime.loganalyzer.storm.dao.LogAnalyzeDao;
import cn.itcast.realtime.loganalyzer.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;


public class HalfAppendCallBack implements Runnable {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) % 30 == 0) {
            String endTime = DateUtils.getDataTime(calendar);
            String startTime = DateUtils.before30Minute(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHalfAppendRecord(baseRecordList);
        }
    }
}
