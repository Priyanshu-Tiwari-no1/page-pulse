package com.priyanshu.page_pulse;

import com.priyanshu.page_pulse.dto.AuditResponse;
import com.priyanshu.page_pulse.service.AuditService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CacheTest {

    @Autowired
    private AuditService auditService;

    @BeforeEach
    void setup() {
        auditService.clearCache();
    }

    @Test
    void testCacheBehaviour() {

        AuditResponse first =
                auditService.audit("https://google.com");

        AuditResponse second =
                auditService.audit("https://google.com");

        assertNotNull(first);
        assertNotNull(second);

        assertFalse(first.isCached());

        assertTrue(second.isCached());
    }
}