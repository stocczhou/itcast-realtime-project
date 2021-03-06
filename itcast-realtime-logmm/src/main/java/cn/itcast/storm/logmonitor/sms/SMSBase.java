package cn.itcast.storm.logmonitor.sms;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class SMSBase implements Serializable {
    private static final long serialVersionUID = 6391455829450707212L;
    private static Logger logger = Logger.getLogger(SMSBase.class);
    private static String smsServiceUrl = "http://v.juhe.cn/sms/send?";
    private static String tokenKey = "6b87459b2b2594d078a1808cc830fc48";

    public static boolean sendSms(String mobile, String content) {
        HttpURLConnection httpconn = null;
        String result = "";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(smsServiceUrl);
            //以下是参数
            sb.append("mobile=").append(mobile)
                    .append("&tpl_id=").append(17979)
                    .append("&tpl_value=").append(URLEncoder.encode(content, "utf-8"))
                    .append("&key=").append(tokenKey);
            String urlStr = sb.toString();
            URL url = new URL(urlStr);
            httpconn = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            result = rd.readLine();
            System.out.println("====================================" + result);
            rd.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpconn != null) {
                httpconn.disconnect();
            }
            httpconn = null;
        }
        if (StringUtils.isNotBlank(result)) {
            if (result.contains("操作成功")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(sendSms("17600775784", "#appname#=zhangsan&#rid#=1&#keyword#=121212"));
    }
}
