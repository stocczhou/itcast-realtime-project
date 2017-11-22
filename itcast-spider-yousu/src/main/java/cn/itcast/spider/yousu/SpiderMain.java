package cn.itcast.spider.yousu;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by maoxiangyi on 2017/11/14.
 */
public class SpiderMain {
    public static void main(String[] args) throws IOException {
        //1、set start url
        String jpsd = "http://www.yousuu.com/booklist?s=digest";
        //2、httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //3.execute
        CloseableHttpResponse response = httpClient.execute(new HttpGet(jpsd));
        //4.get html dcoment
        String html = EntityUtils.toString(response.getEntity());
        //5.parser itemlist
        Document doc = Jsoup.parse(html);
        Elements aTags = doc.select(".list-item a[href^=/booklist]");
        for (Element aTag : aTags) {
            //booklist-item
            String href = aTag.attr("href");
            //2、httpclient
            CloseableHttpClient hc = HttpClients.createDefault();
            //3.execute
            CloseableHttpResponse res = hc.execute(new HttpGet("http://www.yousuu.com"+href));
            //4.get html dcoment
            String value = EntityUtils.toString(res.getEntity());
            //5.parser itemlist
            Document doc1 = Jsoup.parse(value);
            //6.parser book id
            Elements elements = doc1.select(".booklist-item");
            for (Element element : elements) {
                System.out.println("----------------- http://www.yousuu.com/book/"+element.attr("id").substring(9));
            }
            System.out.println();
        }


//        String nextUrl = "https://read.qidian.com/chapter/_AaqI-dPJJ4uTkiRw_sFYA2/TPYBmAARksLgn4SMoDUcDQ2";
//        while (nextUrl!=null) {
//            try {
//                //2、httpclient
//                CloseableHttpClient httpClient = HttpClients.createDefault();
//                //3.execute
//                CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl));
//                //4.get html dcoment
//                String html = EntityUtils.toString(response.getEntity());
//                //5.parse content and next url
//                Document document = Jsoup.parse(html);
//                //5.1 parse content
//                Elements contents = document.select("[class=read-content j_readContent]");
//                System.out.println(contents.text());
//                System.out.println("------------------------------");
//                Elements nextUrls = document.select("#j_chapterNext");
//                //5.2 parser next url
//                nextUrl = "http:" + nextUrls.get(0).attr("href");
//                Thread.sleep(2 * 1000);
//            }catch (Exception e){
//                System.out.println(nextUrl);
//                System.out.println(e);
//            }
    }
}
