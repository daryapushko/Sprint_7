package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.client.base.ClientConfiguration;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierLogin;

import static io.restassured.RestAssured.given;

public class CourierClient extends ClientConfiguration {
    private static final String NEWCOURIER_URI = URI + "api/v1/courier/";

    @Step("Create a new courier {courier}")
    public ValidatableResponse createNewCourier(Courier courier) {
        return given()
                .spec(getHeader())
                .body(courier)
                .when()
                .post(NEWCOURIER_URI)
                .then();
    }

    @Step("Login with a new courier {courierLogin}")
    public ValidatableResponse loginAsCourierAndCheckResponse(CourierLogin courierLogin) {
        return given()
                .spec(getHeader())
                .body(courierLogin)
                .when()
                .post(NEWCOURIER_URI + "login")
                .then();
    }

    @Step("Delete the previously created courier {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getHeader())
                .when()
                .delete(NEWCOURIER_URI + courierId)
                .then();
    }
}
