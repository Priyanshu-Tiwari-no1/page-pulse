package com.priyanshu.page_pulse.cache;

import com.priyanshu.page_pulse.dto.AuditResponse;

public class CacheEntry {

    private final AuditResponse response;
    private final long timestamp;

    public CacheEntry(AuditResponse response, long timestamp) {
        this.response = response;
        this.timestamp = timestamp;
    }

    public AuditResponse getResponse() {
        return response;
    }

    public long getTimestamp() {
        return timestamp;
    }
}