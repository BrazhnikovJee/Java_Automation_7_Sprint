package ru.yandex.praktikum.orders.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.order.Order;
import ru.yandex.praktikum.order.OrderApiResponse;
import ru.yandex.praktikum.service.Service;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {
    private final OrderApiResponse orderApi = new OrderApiResponse();

    // Метод для шага "Создание заказа":
    @Step("Create order")
    public Response orderCreate(Order order){
        Response response = orderApi.createOrder(order);
        printResponseBodyToConsole("Создание заказа: ", response, Service.NEED_DETAIL_LOG);
        return response;
    }

    @Step("Compare track is not null")
    public void compareTrackNotNull(Response response){
        response
                .then()
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue());
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(String headerText, Response response, boolean detailedLog){
        if (detailedLog)
            System.out.println(headerText + response.body().asString());
    }
}
