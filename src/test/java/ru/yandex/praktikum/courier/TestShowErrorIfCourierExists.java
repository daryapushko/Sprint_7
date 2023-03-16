package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierGenerator;

import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.junit.Assert.assertEquals;

public class TestShowErrorIfCourierExists {
    private CourierClient courierClient;
    private Courier courier;
    private int statusCode;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Тело ответа содержит ошибку, если создается такой же курьер")
    @Description("Нельзя создать двух одинаковых курьеров")
    @Test
    public void shouldNotCreateAnyEqualCourier() {
        courier = CourierGenerator.getRandomData();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        ValidatableResponse createEqualCourierResponse = courierClient.createNewCourier(courier);
        statusCode = createEqualCourierResponse.extract().statusCode();
        String isCourierCreated = createEqualCourierResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_CONFLICT, statusCode);
        assertEquals("The courier is already created", "Этот логин уже используется", isCourierCreated);
    }

    @DisplayName("Тело ответа содержит ошибку, если используется тот же логин")
    @Description("Если создать пользователя с логином, который уже есть, возвращается ошибка")
    @Test
    public void shouldNotCreateCourierWithExistingLogin() {
        courier = CourierGenerator.getRandomData();
        String courierLogin = courier.getLogin();
        Courier courier2 = CourierGenerator.getRandomData();
        courier2.setLogin(courierLogin);
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        ValidatableResponse createCourierWithExistingLoginResponse = courierClient.createNewCourier(courier2);
        statusCode = createCourierWithExistingLoginResponse.extract().statusCode();
        String isCourierCreated = createCourierWithExistingLoginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_CONFLICT, statusCode);
        assertEquals("The courier is already created", "Этот логин уже используется", isCourierCreated);
    }
}
