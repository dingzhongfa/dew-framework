package com.tairanchina.csp.dew.core.logger;

import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class DewTraceFeignClientWrap {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void before(Request request) throws IOException {
        logger.trace("Sending " + request.method() + " request to " + request.url());
    }

    public static void after(Response response) throws IOException {
        logger.trace("Received " + response.status() + " response for " +
                response.request().method() + " request to " + response.request().url());
    }

}
