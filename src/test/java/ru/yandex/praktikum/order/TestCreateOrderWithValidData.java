package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.model.OrderGenerator;
import ru.yandex.praktikum.model.OrderRequest;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class TestCreateOrderWithValidData {
    private final String[] color;
    private final boolean orderIsCreated;
    private OrderClient orderClient;
    private OrderRequest orderRequest;
    private int orderTrack;
    private int statusCode;

    public TestCreateOrderWithValidData(String[] color, boolean orderIsCreated) {
        this.color = color;
        this.orderIsCreated = orderIsCreated;
    }

    @Parameterized.Parameters(name = "Доступные цвета. Тестовые данные: {0} {1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"BLACK", "GREY"}, true},
                {new String[]{"BLACK"}, true},
                {new String[]{"GREY"}, true},
                {new String[]{""}, true},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @DisplayName("Создание заказа и проверка тела ответа с указанием разных цветов")
    @Description("Проверить, что можно указать один из цветов — BLACK или GREY;\n" +
            "    - можно указать оба цвета;\n" +
            "    - можно совсем не указывать цвет;\n" +
            "    - тело ответа содержит track") // описание теста
    @Test
    public void shouldCreateNewOrder() {
        orderRequest = OrderGenerator.getRandomDataWithBlankColor();
        orderRequest.setColor(this.color);
        ValidatableResponse createResponse = orderClient.createNewOrder(orderRequest);
        statusCode = createResponse.extract().statusCode();
        orderTrack = createResponse.extract().path("track");

        assertEquals("The status code is invalid", HTTP_CREATED, statusCode);
        assertTrue("The courier is not created", orderTrack != 0);
    }

    @After
    public void clearDown() {
        orderClient.cancelOrder(orderTrack);
    }
}
