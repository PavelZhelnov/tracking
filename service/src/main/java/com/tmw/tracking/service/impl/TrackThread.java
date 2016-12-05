package com.tmw.tracking.service.impl;

import com.tmw.tracking.entity.TrackingSite;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.concurrent.Callable;

/**
 * Created by pzhelnov on 11/25/2016.
 */
public class TrackThread implements Callable<String> {
    private final TrackingSite trackingSite;
    private final String container;
    private final static Logger logger = LoggerFactory.getLogger(TrackingServiceImpl.class);

    public TrackThread(TrackingSite trackingSite, String container) {
        this.trackingSite = trackingSite;
        this.container = container;
    }

    @Override
    public String call() throws Exception {
        String url = MessageFormat.format(trackingSite.getGetUrl(), container);

        logger.info("Calling url "+ url);
        Document document = null;
        int count = 0;
        do {
            try {
                document = Jsoup.
                        connect(url).
                        //cookies(cookies).
                                userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2").
                        timeout(3000).
                        get();
            } catch (SocketTimeoutException exception) {
                logger.warn("Time out " + trackingSite.getGetUrl());
            }
        } while (count++ <3);
        if (document == null) {
            return null;
        }
        String result = analyze(document.html(), trackingSite);
        if (!result.isEmpty()) {
            logger.info("Got result!" + trackingSite.getGetUrl());
            return result;
        } else {
            return null;
        }
    }

    private String analyze(String response, TrackingSite trackingSite) {
        //failure first
        if (response == null || response.isEmpty()) {
            return "";
        }
        int failureIndex = response.indexOf(trackingSite.getFailureElement());
        if (failureIndex>0) {
            return "";
        }
        int firstIndex = response.indexOf(trackingSite.getSuccessElement());
        if (firstIndex<0) {
            return "";
        }
        String sub1 = response.substring(firstIndex);
        int encloseIndex = sub1.indexOf(trackingSite.getEnclose());
        String sub2 = encloseIndex>0?sub1.substring(0, encloseIndex):sub1;
        return Jsoup.parse(sub2).text();
    }


}