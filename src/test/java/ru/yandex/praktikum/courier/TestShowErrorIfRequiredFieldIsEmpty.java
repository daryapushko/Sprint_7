package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.model.Courier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class TestShowErrorIfRequiredFieldIsEmpty {
    private final String login;
    private final String password;
    private boolean isCourierCreated;
    private CourierClient courierClient;
    private Courier courier;
    private int statusCode;

    public TestShowErrorIfRequiredFieldIsEmpty(String login, String password, boolean isCourierCreated) {
        this.login = login;
        this.password = password;
        this.isCourierCreated = isCourierCreated;
    }

    @Parameterized.Parameters(name = "Логин и пароль. Тестовые данные: {0} {1}. Создан ли курьер: {2}")
    public static Object[][] getCourierData() {
        return new Object[][]{
                {RandomStringUtils.randomAlphanumeric(3), "", false},
                {RandomStringUtils.randomAlphanumeric(3), null, false},
                {"", RandomStringUtils.randomAlphanumeric(3), false},
                {null, RandomStringUtils.randomAlphanumeric(3), false},
                {"", "", false},
                {"", null, false},
                {null, "", false},
                {null, null, false}
        };
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @DisplayName("Ошибка в ответе при отсутствии обязательного поля")
    @Description("Проверить, что если одного из полей нет, запрос возвращает ошибку")
    @Test
    public void shouldNotCreateCourierWithoutRequiredFields() {
        courier = new Courier();
        courier.setLoginAndPassword(this.login, this.password);
        ValidatableResponse createResponse = courierClient.createNewCourier(courier);
        statusCode = createResponse.extract().statusCode();
        String courierMessage = createResponse.extract().path("message");

        assertEquals("The status code is invalid", 400, statusCode);
        assertFalse("The courier is created", isCourierCreated);
        assertEquals("The courier is created", "Недостаточно данных для создания учетной записи", courierMessage);
    }

}
