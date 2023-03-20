package ru.yandex.praktikum.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierGenerator;
import ru.yandex.praktikum.model.CourierLogin;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.junit.Assert.assertEquals;

public class TestShowErrorIfLoginOrPasswordAreWrong {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private String courierMessage;
    private int statusCode;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Авторизация не пройдет при неправильном пароле")
    @Description("Проверить, что система вернёт ошибку, если неправильно указать пароль")
    @Test
    public void shouldNotLoginWithIncorrectPassword() {
        courier = CourierGenerator.getRandomData();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        String initialPassword = courier.getPassword();
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        courier.setPassword(CourierGenerator.getRandomPassword().getPassword());
        ValidatableResponse loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Учетная запись не найдена", courierMessage);

        courier.setPassword(initialPassword);
        loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");
    }

    @DisplayName("Авторизация не пройдет при неправильном логине")
    @Description("Система вернёт ошибку, если неправильно указать логин")
    @Test
    public void shouldNotLoginWithIncorrectLogin() {
        courier = CourierGenerator.getRandomData();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        String initialLogin = courier.getLogin();
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        courier.setLogin(CourierGenerator.getRandomLogin().getLogin());
        ValidatableResponse loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierMessage = loginResponse.extract().path("message");

        assertEquals("The status code is invalid", HTTP_NOT_FOUND, statusCode);
        assertEquals("The login is still enabled", "Учетная запись не найдена", courierMessage);

        courier.setLogin(initialLogin);
        loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");
    }

    @After
    public void clearDown() {
        courierClient.deleteCourier(courierId);
    }
}