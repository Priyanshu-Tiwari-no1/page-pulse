package com.priyanshu.page_pulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AuditRequest {

    @NotBlank(message = "URL is required")
    @Pattern(
        regexp = "^https?://.+",
        message = "URL must start with http:// or https://"
    )
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}