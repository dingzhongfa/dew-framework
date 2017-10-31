package com.tairanchina.csp.dew.core.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class DewTraceRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.trace("Sending " + request.getMethod() + " request to " + request.getURI());
        ClientHttpResponse response = execution.execute(request, body);
        logger.trace("Received " + response.getRawStatusCode() + " response for " +
                request.getMethod() + " request to " + request.getURI());
        return response;
    }

}
