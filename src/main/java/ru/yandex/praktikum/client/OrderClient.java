package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ClientConfiguration;
import ru.yandex.praktikum.model.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient extends ClientConfiguration {
    private static final String ORDER_LIST_URI = URI + "api/v1/orders/";

    @Step("Get order list")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getHeader())
                .when()
                .get(ORDER_LIST_URI)
                .then();
    }

    @Step("Create a new order {order}")
    public ValidatableResponse createNewOrder(OrderRequest orderRequest) {
        return given()
                .spec(getHeader())
                .body(orderRequest)
                .when()
                .post(ORDER_LIST_URI)
                .then();
    }

    @Step("Cancel the previously created order {orderTrack}")
    public ValidatableResponse cancelOrder(int orderTrack) {
        return given()
                .spec(getHeader())
                .when()
                .delete(ORDER_LIST_URI + "cancel/" + orderTrack)
                .then();
    }
}
