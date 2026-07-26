package com.priyanshu.page_pulse.ratelimit;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final ConcurrentHashMap<String, RequestCounter> clients =
            new ConcurrentHashMap<>();


    private static final int MAX_REQUESTS = 10;

    private static final long TIME_WINDOW = 60000;


    public boolean allowRequest(String clientIp) {


        long currentTime = System.currentTimeMillis();


        RequestCounter counter =
                clients.get(clientIp);



        if(counter == null){

            clients.put(
                    clientIp,
                    new RequestCounter(currentTime,1)
            );

            return true;
        }



        if(currentTime - counter.timestamp > TIME_WINDOW){

            clients.put(
                    clientIp,
                    new RequestCounter(currentTime,1)
            );

            return true;
        }



        if(counter.count >= MAX_REQUESTS){

            return false;

        }


        counter.count++;

        return true;

    }



    private static class RequestCounter{

        long timestamp;

        int count;


        RequestCounter(long timestamp,int count){

            this.timestamp = timestamp;

            this.count = count;
        }
    }
}