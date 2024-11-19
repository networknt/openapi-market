package com.networknt.market.handler;

import com.networknt.client.http.Http2ServiceRequest;
import com.networknt.config.Config;

import com.networknt.config.JsonMapper;
import com.networknt.handler.LightHttpHandler;
import com.networknt.http.*;
import com.networknt.market.MarketConfig;
import com.networknt.market.ServiceRef;
import com.networknt.status.Status;
import com.networknt.utility.Constants;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class StoreProductsGetHandler implements LightHttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(StoreProductsGetHandler.class);
    public static final String referenceAPI = "referenceAPI";
    public static final String GENERIC_EXCEPTION = "ERR10014";

    MarketConfig config = (MarketConfig) Config.getInstance().getJsonObjectConfig(MarketConfig.CONFIG_NAME, MarketConfig.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap responseHeaders = new HeaderMap();
        responseHeaders.add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        try {
            Http2ServiceRequest getStoreData = getHttp2ServiceRequest(referenceAPI);
            List list = getStoreData.callForTypedObject(List.class).get();
            exchange.setStatusCode(HttpStatus.OK.value());
            exchange.getResponseSender().send(JsonMapper.toJson(list));
        } catch (Exception e) {
            logger.error("API call error: ", e);
            Status status = new Status(GENERIC_EXCEPTION, e.getMessage());
            setExchangeStatus(exchange, status);
        }
    }

    private ServiceRef getService(String name) {
        return config.getApiServiceRef().get(name);
    }

    private Http2ServiceRequest getHttp2ServiceRequest(String serviceName) throws Exception {
        ServiceRef serviceRef = getService(serviceName);
        if(serviceRef == null) throw new Exception("Missing service config in market.yml");
        String proxyUrl = config.getProxyUrl();
        if(logger.isDebugEnabled()) logger.debug("proxyUrl = " + proxyUrl);
        Http2ServiceRequest http2ServiceRequest = new Http2ServiceRequest(new URI(proxyUrl), serviceRef.getPath(), new HttpString(serviceRef.getMethod()));
        http2ServiceRequest.addRequestHeader("Host", "localhost");
        http2ServiceRequest.addRequestHeader(Constants.TRACEABILITY_ID_STRING, "traceabilityId");
        if(serviceRef.getServiceUrl() != null) {
            http2ServiceRequest.addRequestHeader(Constants.SERVICE_URL_STRING, serviceRef.getServiceUrl());
        } else {
            http2ServiceRequest.addRequestHeader(Constants.SERVICE_ID_STRING, serviceRef.getServiceId());
        }
        return http2ServiceRequest;
    }
}
