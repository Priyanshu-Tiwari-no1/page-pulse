package com.priyanshu.page_pulse.controller;

import com.priyanshu.page_pulse.dto.AuditRequest;
import com.priyanshu.page_pulse.dto.AuditResponse;
import com.priyanshu.page_pulse.ratelimit.RateLimitService;
import com.priyanshu.page_pulse.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class AuditController {


    private final AuditService auditService;

    private final RateLimitService rateLimitService;


    private static final Logger logger =
            LoggerFactory.getLogger(AuditController.class);



    public AuditController(
            AuditService auditService,
            RateLimitService rateLimitService) {

        this.auditService = auditService;
        this.rateLimitService = rateLimitService;
    }



    @PostMapping("/audit")
    public ResponseEntity<?> auditUrl(
            @Valid @RequestBody AuditRequest request,
            HttpServletRequest httpRequest) {


        String requestId =
                UUID.randomUUID().toString();


        String clientIp =
                httpRequest.getRemoteAddr();



        logger.info(
                "REQUEST_START id={} ip={} url={}",
                requestId,
                clientIp,
                request.getUrl()
        );



        boolean allowed =
                rateLimitService.allowRequest(clientIp);



        if(!allowed){


            logger.warn(
                    "RATE_LIMIT_BLOCK id={} ip={}",
                    requestId,
                    clientIp
            );


            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(
                            Map.of(
                                    "requestId",requestId,
                                    "error",
                                    "RATE_LIMIT_EXCEEDED",
                                    "message",
                                    "Too many requests"
                            )
                    );
        }




        AuditResponse response =
                auditService.audit(
                        request.getUrl()
                );



        logger.info(
                "REQUEST_END id={} status={}",
                requestId,
                response.getStatusCode()
        );



        return ResponseEntity.ok(response);

    }

}