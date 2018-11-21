package com.rioliu.test.rest;

import org.apache.commons.lang3.StringUtils;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

/**
 * Created by rioliu on Nov 13, 2018
 */
public class RestClientConfigHelper {

    private static RestClientConfigHelper rc;

    public synchronized static RestClientConfigHelper get() {

        if (rc == null) {
            rc = new RestClientConfigHelper();
        }

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        return rc;
    }

    private RestClientConfigHelper() {}

    public RestClientConfigHelper setHTTPProxy(String host, int port) {

        RestAssured.proxy(host, port);
        return rc;
    }

    public RestClientConfigHelper useRelaxedHTTPSValidation() {

        RestAssured.useRelaxedHTTPSValidation();
        return rc;
    }

    public RestClientConfigHelper useRelaxedHTTPSValidation(String protocol) {

        RestAssured.useRelaxedHTTPSValidation(StringUtils.isEmpty(protocol) ? "TLSv1.2" : protocol);
        return rc;
    }


}
