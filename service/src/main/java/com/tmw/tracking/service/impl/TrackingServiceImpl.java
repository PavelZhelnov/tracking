package com.tmw.tracking.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.dao.TrackingSiteDao;
import com.tmw.tracking.entity.TrackingSite;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.service.UserService;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by pzhelnov on 11/18/2016.
 */
@Singleton
public class TrackingServiceImpl implements TrackingService {

    private final UserService userService;
    private final TrackingSiteDao trackingSiteDao;
    private final static Logger logger = LoggerFactory.getLogger(TrackingServiceImpl.class);

    @Inject
    public TrackingServiceImpl(UserService userService, TrackingSiteDao trackingSiteDao) {
        this.userService = userService;
        this.trackingSiteDao = trackingSiteDao;
    }

    @Override
    public String trackContainer(String containerNumber) {
        try {
            return sendGet(containerNumber);
        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    private String sendGet(String container) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<TrackingSite> trackingSiteList = trackingSiteDao.getTrackingSites();
        List<Future<String>> futureList = new ArrayList<Future<String>>();
        try {

            for (TrackingSite trackingSite: trackingSiteList) {
                if (trackingSite.getGetUrl() == null) {
                    continue;
                }
                Future<String> future = executorService.submit(new TrackThread(trackingSite, container));
                futureList.add(future);

            }

            for (Future<String> future: futureList) {
                String result = future.get(1, TimeUnit.MINUTES);

                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            executorService.shutdownNow();
        }


        return "Not successfull!";
    }

    /*private Map<String, String> firstStep(String url) {
        try {
            Connection.Response response = Jsoup.connect("http://"+url).
                    userAgent("Mozilla/5.0").
                    timeout(3000).
                    method(Connection.Method.GET).execute();

            return response.cookies();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<String, String>();

    }*/

}
