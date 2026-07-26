package com.priyanshu.page_pulse;

import com.priyanshu.page_pulse.dto.AuditResponse;
import com.priyanshu.page_pulse.service.AuditService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
class AuditServiceTest {

    @Autowired
    private AuditService auditService;

    @BeforeEach
    void setup() {
        auditService.clearCache();
    }

    @Test
    void testAuditGoogle() {

        AuditResponse response = auditService.audit("https://google.com");

        System.out.println("Cached = " + response.isCached());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertFalse(response.isCached());
    }
}