package com.shoa.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiojiang on 2019/11/13.
 */
public class BrowserUrlCrawler {
    private static int total = 10;
    private static int batchSize = 10;
    private static int count = 1;
    private static int timeout = 60;

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors
            .newSingleThreadScheduledExecutor();
    private static String url = "http://jiangxi.ilab-x.com/details/v5?id=5537";
    private static String browser = "chrome";
    private static final String USAGE = "\nUsage: java -jar BrowserUrlCrawler.jar <url> <total> <batchSize> <timeout> " +
            "\n";


    public static void main(String[] args) {
        if (args.length > 0) {
            url = args[0];
        }
        if (args.length > 1) {
            total = getInt(args[1]);
        }
        if (args.length > 2) {
            batchSize = getInt(args[2]);
        }
        if (args.length > 3) {
            timeout = getInt(args[3]);
        }
        if (args.length > 4) {
            browser = args[4];
        }
        log("Start auto crawling by " + browser + ". total=" + total + ", batchSize=" + batchSize + ", " +
                "timeout=" + timeout);
        start();
    }


    private static int getInt(String args) {
        try {
            return Integer.parseInt(args);
        } catch (Exception e) {
            System.out.println(USAGE);
        }
        return -1;
    }


    private static void start() {
        batchBrowse(url, batchSize);
        SCHEDULED_EXECUTOR_SERVICE.schedule(() -> kill(), timeout, TimeUnit.SECONDS);
    }

    private static void kill() {
        try {
            Runtime.getRuntime().exec("cmd /c taskkill /f /im " + browser + ".exe");
        } catch (IOException e) {
            log("Error killing chrome.exe." + e.getMessage());
        }
        log("Round(" + count + "*" + batchSize + ") complete.");
        count++;
        if (count > total / batchSize) {
            SCHEDULED_EXECUTOR_SERVICE.shutdown();
            log("Crawling complete.");
            return;
        }
        start();
    }

    private static void batchBrowse(String url, int size) {
        log("Round(" + count + "*" + size + ") start.");
        for (int i = 0; i < size; i++) {
            try {
                browse4(url);
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (Exception e) {
                log("Error starting " + browser + ".exe." + e.getMessage());
            }
        }
    }

    private static void browse4(String url) throws Exception {
        Runtime.getRuntime().exec("cmd /c start " + url);//启用cmd运行默认浏览器
    }

    private static void log(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        System.out.println(dateFormat.format(new Date()) + " " + str);
    }
}
