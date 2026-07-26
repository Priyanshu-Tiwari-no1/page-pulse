package com.priyanshu.page_pulse.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;


@Component
public class RequestIdFilter implements Filter {


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest req =
                (HttpServletRequest) request;


        HttpServletResponse res =
                (HttpServletResponse) response;



        String requestId =
                UUID.randomUUID()
                .toString();



        res.setHeader(
                "X-Request-ID",
                requestId
        );



        System.out.println(
                "REQUEST START ID="
                + requestId
                + " URL="
                + req.getRequestURI()
        );



        long start =
                System.currentTimeMillis();



        chain.doFilter(request,response);



        long end =
                System.currentTimeMillis();



        System.out.println(
                "REQUEST END ID="
                + requestId
                + " TIME="
                + (end-start)
                +"ms"
        );

    }
}