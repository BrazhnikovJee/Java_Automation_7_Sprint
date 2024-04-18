package ru.yandex.praktikum.orders.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.order.Order;
import ru.yandex.praktikum.order.OrderApiResponse;
import ru.yandex.praktikum.orders.steps.OrderSteps;
import ru.yandex.praktikum.service.Service;

import java.util.List;


@RunWith(Parameterized.class)
public class CreateTest extends Service {

    private final List<String> color;
    private final OrderApiResponse orderApi = new OrderApiResponse();
    private Order order;
    private Response response;
    private final OrderSteps orderSteps = new OrderSteps();

    public CreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "{index}: Цвет самоката: {0}")
    public static Object[][] createOrderWithDifferentColors() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()},
                {List.of("BLACK", "GREY")},
        };
    }

    @Before
    public void setUp() {
        order = new Order("Вадим", "Бражников", "Летчика Ульянина, 7", "4", "+7 977 403 84 22", 3, "2024-12-12", "Покатушки", color);
    }

    @After
    public void tearDown() {
        try {
            String orderId = response.then().extract().path("track").toString();
            orderApi.cancelOrder(orderId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка, что можно создать заказ с переданными наборами цветов (в параметрах)")
    public void paramCreateOrderTest() {
        response = orderSteps.orderCreate(order);
        orderSteps.compareTrackNotNull(response);
    }

}
