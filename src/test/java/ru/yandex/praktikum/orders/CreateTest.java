package ru.yandex.praktikum.orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.order.Order;
import ru.yandex.praktikum.order.OrderApiResponse;
import ru.yandex.praktikum.service.Service;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateTest {

    private final List<String> color;
    private final OrderApiResponse orderApi = new OrderApiResponse();
    private Order order;
    private Response response;

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
        RestAssured.baseURI = Service.BASE_URL;
        order = new Order("Вадим", "Бражников", "Летчика Ульянина, 7", "4", "+7 977 403 84 22", 3, "2024-12-12", "Покатушки", color);
    }

    @After
    public void tearDown() {
        try {
            String orderId = response.then().extract().path("track").toString();
            orderApi.cancelOrder(orderId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка, что можно создать заказ с переданными наборами цветов (в параметрах)")
    public void paramCreateOrderTest() {
        response = orderCreate(order);
        compareTrackNotNull(response);
    }

    // Метод для шага "Создание заказа":
    @Step("Create order")
    public Response orderCreate(Order order){
        response = orderApi.createOrder(order);
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
