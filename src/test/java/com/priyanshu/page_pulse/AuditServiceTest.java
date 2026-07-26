package com.priyanshu.page_pulse;

import com.priyanshu.page_pulse.dto.AuditResponse;
import com.priyanshu.page_pulse.service.AuditService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AuditServiceTest {


    @Autowired
    private AuditService auditService;


    @Test
    void testAuditGoogle() {

        AuditResponse response =
                auditService.audit("https://google.com");


        assertNotNull(response);

        assertEquals(
                200,
                response.getStatusCode()
        );

        assertFalse(
                response.isCached()
        );
    }
}