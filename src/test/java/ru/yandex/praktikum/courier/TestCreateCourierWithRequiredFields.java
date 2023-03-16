package ru.yandex.praktikum.courier;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCreateCourierWithRequiredFields {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private int statusCode;
    private boolean isCourierCreated;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Создать нового курьера с логином/паролем")
    @Description("Проверить, что чтобы создать курьера, нужно передать в ручку все обязательные поля") // описание теста
    @Test
    public void shouldCreateNewCourierOnlyWithLoginAndPassword() {
        courier = CourierGenerator.getRandomLoginAndPassword();
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        isCourierCreated = createResponse.extract().path("ok");

        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The courier is not created", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.loginAsCourierAndCheckResponse(CourierLogin.from(courier));
        courierId = loginResponse.extract().path("id");

        assertTrue("The courier ID is not provided", courierId != 0);
    }

    @After
    public void clearDown() {
        courierClient.deleteCourier(courierId);
    }
}
