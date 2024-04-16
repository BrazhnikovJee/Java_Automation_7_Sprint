package ru.yandex.praktikum.orders;

import ru.yandex.praktikum.order.OrderApiResponse;
import ru.yandex.praktikum.service.Service;

import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class ListTest {

    private final OrderApiResponse orderApi = new OrderApiResponse();

    @Before
    public void setUp() {
        RestAssured.baseURI = Service.BASE_URL;
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов")
    public void getOrderListTest() {
        Response response = orderApi.orderList();
        printResponseBodyToConsole("Список заказов: ", response, Service.NEED_DETAIL_LOG);
        response
                .then()
                .statusCode(SC_OK).assertThat().body("orders", notNullValue());
    }

    @Step("Print response body to console")
    public void printResponseBodyToConsole(String headerText, Response response, boolean detailedLog){
        if (detailedLog)
            System.out.println(headerText + response.body().asString());
    }
}