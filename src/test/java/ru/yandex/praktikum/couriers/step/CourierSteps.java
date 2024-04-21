package ru.yandex.praktikum.couriers.step;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.courier.Courier;
import ru.yandex.praktikum.courier.CourierApiResponse;
import ru.yandex.praktikum.service.Service;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps {
    private final CourierApiResponse courierApiResponse = new CourierApiResponse();

    // Метод для шага "Создать курьера":
    @Step("Create courier")
    public Response courierCreate(Courier courier) {
        Response response = courierApiResponse.create(courier);
        printResponseBodyToConsole("Создание курьера: ", response, Service.NEED_DETAIL_LOG);
        return response;
    }

    // Метод для шага "Авторизация курьера":
    @Step("Login courier")
    public Response courierLogin(Courier courier) {
        Response response = courierApiResponse.login(courier);
        printResponseBodyToConsole("Авторизация курьера: ", response, Service.NEED_DETAIL_LOG);
        return response;
    }

    // Метод для шага "Удалить курьера":
    @Step("Delete courier by id")
    public Response courierDelete(String courierId) {
        Response response = courierApiResponse.delete(courierId);
        printResponseBodyToConsole("Удаление курьера: ", response, Service.NEED_DETAIL_LOG);
        return response;
    }

    @Step("Compare result to true")
    public void compareResultToTrue(Response response, int statusCode) {
        response
                .then()
                .assertThat()
                .log().all()
                .statusCode(statusCode)
                .body("ok", is(true));
    }

    @Step("Compare result message to something")
    public void compareResultMessageToText(Response response, int statusCode, String text) {
        response
                .then()
                .log().all()
                .statusCode(statusCode)
                .and()
                .assertThat()
                .body("message", is(text));
    }

    // Метод для шага "Вывести тело ответа в консоль":
    @Step("Print response body to console")
    public void printResponseBodyToConsole(String headerText, Response response, boolean detailedLog) {
        if (detailedLog)
            System.out.println(headerText + response.body().asString());
    }

    @Step("Compare id is not null")
    public void compareIdNotNull(Response response) {
        response
                .then()
                .assertThat()
                .log().all()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

}
