package com.priyanshu.page_pulse.controller;

import com.priyanshu.page_pulse.dto.AuditRequest;
import com.priyanshu.page_pulse.dto.AuditResponse;
import com.priyanshu.page_pulse.ratelimit.RateLimitService;
import com.priyanshu.page_pulse.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AuditController {


    private final AuditService auditService;

    private final RateLimitService rateLimitService;



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



        // Get client IP
        String clientIp =
                httpRequest.getRemoteAddr();



        // Rate limit check
        boolean allowed =
                rateLimitService.allowRequest(clientIp);



        if(!allowed){

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(
                            "Rate limit exceeded. Try again later."
                    );
        }



        System.out.println(
                "Audit request received from IP: "
                + clientIp
        );



        AuditResponse response =
                auditService.audit(
                        request.getUrl()
                );


        return ResponseEntity.ok(response);
    }

}