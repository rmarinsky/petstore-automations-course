package com.swagger.api.controller.common;


import io.restassured.response.Response;
import lombok.AllArgsConstructor;

import java.util.stream.Stream;


public class ResponseExpectMessages {

    private final Response targetResponse;

    public ResponseExpectMessages(Response targetResponse) {
        this.targetResponse = targetResponse;
    }

    public String expectedStatusCode(StatusCode expectedStatusCode) {
        return SBB.sbb().n()
                .append("Expected status code:").w().append(expectedStatusCode.name()).w().append(expectedStatusCode.code).n()
                .append("Actual status code:").w().append(StatusCode.byValue(targetResponse.statusCode())).w().sQuoted(targetResponse.statusCode()).n()
                .append("Actual response body:").n()
                .append(targetResponse.body().asString()).n()
                .bld();
    }

    public String expectedResponseBodyClass(Class expectedClass) {
        return SBB.sbb().n()
                .append("Unexpected response body:").n()
                .append(targetResponse.asString()).n()
                .append("Expected body type:").w().sQuoted(expectedClass.getSimpleName()).n()
                .append("With fields:").n()
                .append(expectedClass.getDeclaredFields())
                .bld();
    }

    @AllArgsConstructor
    public enum StatusCode {

        CREATED(201), OK(200), ACCEPTED(202), NO_CONTENT(204),
        REDIRECT(302),
        BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404), PROXY_AUTH_REQUIRED(407), CONFLICT(409), TOO_LARGE(413),
        SERVER_ERROR(500), SERVICE_UNAVAILABLE(503), GATEWAY_TIMEOUT(504);

        public final int code;

        public static StatusCode byValue(int value) {
            return Stream.of(StatusCode.values())
                    .filter(code -> code.code == value).findFirst()
                    .orElseThrow(() -> new RuntimeException(SBB.sbb("No such status code known").w().append(value).bld()));
        }
    }

}
