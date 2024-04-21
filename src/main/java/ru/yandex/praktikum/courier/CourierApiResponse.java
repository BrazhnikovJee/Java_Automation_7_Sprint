package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import ru.yandex.praktikum.service.Service;

import static io.restassured.RestAssured.given;

public class CourierApiResponse extends Service {

    private static final String COURIER_API_PATH = "/api/v1/courier";
    private static final String COURIER_API_LOGIN_PATH = "/api/v1/courier/login";
    private static final String COURIER_API_DELETE_PATH = "/api/v1/courier/";

    @Description("Логин курьера")
    public Response login(Courier courier) {
        return given()
                .spec(getBaseSpecification())
                .and()
                .body(courier)
                .when()
                .post(COURIER_API_LOGIN_PATH);
    }

    @Description("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .spec(getBaseSpecification())
                .and()
                .body(courier)
                .log().all()
                .when()
                .post(COURIER_API_PATH);
    }

    @Description("Удаление курьера")
    public Response delete(String id) {
        return given()
                .spec(getBaseSpecification())
                .delete(COURIER_API_DELETE_PATH + id);
    }

}
