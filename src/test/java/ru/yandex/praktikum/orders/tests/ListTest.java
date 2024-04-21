package ru.yandex.praktikum.orders.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.order.OrderApiResponse;
import ru.yandex.praktikum.orders.steps.OrderSteps;
import ru.yandex.praktikum.service.Service;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class ListTest extends Service {
    OrderSteps orderSteps = new OrderSteps();
    private final OrderApiResponse orderApi = new OrderApiResponse();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void getOrderListTest() {
        Response response = orderApi.orderList();
        orderSteps.printResponseBodyToConsole("Список заказов: ", response, Service.NEED_DETAIL_LOG);
        response
                .then()
                .statusCode(SC_OK).assertThat().body("orders", notNullValue());
    }
}
