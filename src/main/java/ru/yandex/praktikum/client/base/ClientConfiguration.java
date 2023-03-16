package ru.yandex.praktikum.client.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ClientConfiguration {
    protected static final String URI = "https://qa-scooter.praktikum-services.ru/";

    protected RequestSpecification getHeader() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(URI)
                .build();
    }
}
