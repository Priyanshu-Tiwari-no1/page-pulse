package com.priyanshu.page_pulse.dto;

public class AuditResponse {

    private String url;
    private int statusCode;
    private long responseTime;
    private String title;
    private boolean cached;

    public AuditResponse() {
    }

    public AuditResponse(String url, int statusCode, long responseTime, String title, boolean cached) {
        this.url = url;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
        this.title = title;
        this.cached = cached;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}