package com.shoa.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by xiojiang on 2019/11/13.
 */
public class UrlCrawler {

  public static final String STRING = "https://v.colorv.com/play/a40091d5e7d55e5720ec1802a3af4737?u=640ada5960b02188&c=2&referrer_user_id=933494&share_time=1581901583.87&from=singlemessage&from=singlemessage&link=wechat";

  public static void main(String[] args) throws Exception {
    URL url = new URL(STRING);
    InetSocketAddress addr = new InetSocketAddress("10.144.1.10", 8080);
    Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
    conn.setRequestProperty("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 " +
            "Safari/537.36");
    InputStream is = conn.getInputStream();
    System.out.println(is.available());
    is.close();

  }
}
