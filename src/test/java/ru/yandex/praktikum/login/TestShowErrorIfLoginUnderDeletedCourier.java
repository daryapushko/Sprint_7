package ru.yandex.praktikum.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierGenerator;
import ru.yandex.praktikum.model.CourierLogin;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

public class TestShowErrorIfLoginUnderDeletedCourier {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private int statusCode;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Авторизация удаленного курьера не пройдет")
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    @Test
    public void shouldNotLoginUnderDeletedCourier() {
        courier = CourierGenerator.getRandomData();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        ValidatableResponse loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        ValidatableResponse deleteResponse = courierClient.deleteCourier(courierId);
        statusCode = deleteResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_OK, statusCode);

        loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        String courierMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Учетная запись не найдена", courierMessage);
    }
}
