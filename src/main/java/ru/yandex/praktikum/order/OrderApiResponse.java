package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import ru.yandex.praktikum.service.Service;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApiResponse extends Service {
    public static final String ORDER_API_PATH = "/api/v1/orders";
    public static final String ORDER_CANCEL_API_PATH = "/api/v1/orders";

    @Description("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .spec(getBaseSpecification())
                .and()
                .body(order)
                .when()
                .post(ORDER_API_PATH);
    }

    @Description("Отмена заказа")
    public Response cancelOrder(String orderId) {
        String orderData = "{ \"track\": " + orderId + "}";
        return given()
                .spec(getBaseSpecification())
                .and()
                .body(orderData)
                .when()
                .put(ORDER_CANCEL_API_PATH);
    }

    @Description("Получение списка заказов")
    public Response orderList() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(ORDER_API_PATH);
    }

}
