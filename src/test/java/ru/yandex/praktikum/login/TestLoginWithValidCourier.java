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
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLoginWithValidCourier {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private int statusCode;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Авторизация курьера с валидными данными")
    @Description("Проверить, что курьер может авторизоваться;\n" +
            "    - для авторизации нужно передать все обязательные поля;\n" +
            "    - успешный запрос возвращает id")
    @Test
    public void shouldLoginWithCreatedCourier() {
        courier = CourierGenerator.getRandomData();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);

        ValidatableResponse loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertTrue("The courier ID is not provided", courierId != 0);
    }

    @After
    public void clearDown() {
        courierClient.deleteCourier(courierId);
    }
}
