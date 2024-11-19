package com.networknt.market.handler;

import com.networknt.body.BodyHandler;
import com.networknt.config.Config;

import com.networknt.handler.LightHttpHandler;
import com.networknt.http.HttpMethod;
import com.networknt.http.HttpStatus;
import com.networknt.http.MediaType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HeaderMap;
import com.networknt.market.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Map;

public class StoreProductsPostHandler implements LightHttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(StoreProductsPostHandler.class);

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // HeaderMap requestHeaders = exchange.getRequestHeaders();
        // Map<String, Deque<String>> queryParameters = exchange.getQueryParameters();
        Map<String, Object> bodyMap = (Map<String, Object>)exchange.getAttachment(BodyHandler.REQUEST_BODY);
        Product requestBody = Config.getInstance().getMapper().convertValue(bodyMap, Product.class);
        if(logger.isDebugEnabled()) logger.debug(requestBody.toString());
        String responseBody = "{}";
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        exchange.setStatusCode(HttpStatus.OK.value());
        exchange.getResponseSender().send(responseBody);
    }
}
