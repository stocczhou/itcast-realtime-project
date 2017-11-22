package cn.itcast.spider.yousu;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by maoxiangyi on 2017/11/14.
 */
public class QiDianSpider {
    public static void main(String[] args) {
        //1、set start url 你在起点上任意找一篇小说，第一章的URL
        String nextUrl = "https://read.qidian.com/chapter/_AaqI-dPJJ4uTkiRw_sFYA2/-doT6biEpYlOBDFlr9quQA2";
        while (nextUrl!=null) {
            try {
                //2、httpclient
                CloseableHttpClient httpClient = HttpClients.createDefault();
                //3.execute
                CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl));
                //4.get html dcoment
                String html = EntityUtils.toString(response.getEntity());
                //5.parse content and next url
                Document document = Jsoup.parse(html);
                //5.1 parse content
                Elements contents = document.select("[class=read-content j_readContent]");
                System.out.println(contents.text());
                System.out.println("------------------------------");
                Elements nextUrls = document.select("#j_chapterNext");
                //5.2 parser next url
                nextUrl = "http:" + nextUrls.get(0).attr("href");
                Thread.sleep(2 * 1000);
            }catch (Exception e){
                System.out.println(nextUrl);
                System.out.println(e);
            }
        }
    }
}
