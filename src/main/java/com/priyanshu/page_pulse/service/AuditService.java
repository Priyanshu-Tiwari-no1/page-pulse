package com.priyanshu.page_pulse.service;

import com.priyanshu.page_pulse.cache.CacheEntry;
import com.priyanshu.page_pulse.dto.AuditResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuditService {

    // Cache to store URL audit results
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Cache TTL (Time To Live) from application.properties
    @Value("${cache.ttl}")
    private long cacheTtl;

    // HTTP Client with timeout and redirect support
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

public void clearCache() {
    cache.clear();
}

    public AuditResponse audit(String url) {

        // ==========================
        // STEP 1 : Check Cache First
        // ==========================
        CacheEntry cachedEntry = cache.get(url);

        if (cachedEntry != null) {

            long age = System.currentTimeMillis() - cachedEntry.getTimestamp();

            // If cache is still valid
            if (age < cacheTtl) {

                AuditResponse cached = cachedEntry.getResponse();

                return new AuditResponse(
                        cached.getUrl(),
                        cached.getStatusCode(),
                        cached.getResponseTime(),
                        cached.getTitle(),
                        true      // Data served from cache
                );
            }
        }

        try {

            // ==========================
            // STEP 2 : Start Timer
            // ==========================
            long start = System.currentTimeMillis();

            // ==========================
            // STEP 3 : Create HTTP Request
            // ==========================
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            // ==========================
            // STEP 4 : Send Request
            // ==========================
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // ==========================
            // STEP 5 : Stop Timer
            // ==========================
            long end = System.currentTimeMillis();

            // HTML Response
            String html = response.body();

            // Default Title
            String title = "No Title";

            // ==========================
            // STEP 6 : Extract HTML Title
            // ==========================
            int startIndex = html.toLowerCase().indexOf("<title>");
            int endIndex = html.toLowerCase().indexOf("</title>");

            if (startIndex != -1 && endIndex != -1) {
                title = html.substring(startIndex + 7, endIndex).trim();
            }

            // ==========================
            // STEP 7 : Create Response
            // ==========================
            AuditResponse auditResponse = new AuditResponse(
                    url,
                    response.statusCode(),
                    end - start,
                    title,
                    false      // Fresh response
            );

            // ==========================
            // STEP 8 : Store in Cache
            // ==========================
            cache.put(url, new CacheEntry(auditResponse, System.currentTimeMillis()));

            // ==========================
            // STEP 9 : Return Response
            // ==========================
            return auditResponse;

        }catch(IOException | InterruptedException e){

    e.printStackTrace();

    return new AuditResponse(

            url,

            500,

            0,

            e.getMessage(),

            false

    );

}
    }
}