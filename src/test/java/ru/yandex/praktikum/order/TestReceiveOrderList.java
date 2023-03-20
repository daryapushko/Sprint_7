package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.model.Orders;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestReceiveOrderList {
    private OrderClient orderClient;
    private int statusCode;
    private Orders orders;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @DisplayName("Получение списка заказов")
    @Description("Проверь, что в тело ответа возвращается список заказов") // описание теста
    @Test
    public void shouldReceiveOderList() {
        ValidatableResponse receiveResponse = orderClient.getOrderList();
        statusCode = receiveResponse.extract().statusCode();
        orders = receiveResponse.extract().body().as(Orders.class);

        assertEquals("The status code is invalid", HTTP_OK, statusCode);
        assertNotNull("The list of orders is not provided", orders);
    }
}
