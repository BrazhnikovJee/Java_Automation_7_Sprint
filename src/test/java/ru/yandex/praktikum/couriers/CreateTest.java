package ru.yandex.praktikum.couriers;


import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.yandex.praktikum.courier.CourierApiResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.courier.CourierData;
import ru.yandex.praktikum.courier.Courier;
import ru.yandex.praktikum.service.Service;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

public class CreateTest {

    private final CourierData courierData = new CourierData();
    private Courier courierRandom;
    private final CourierApiResponse courierApiResponse = new CourierApiResponse();

    @Before
    public void setUp() {
        RestAssured.baseURI = Service.BASE_URL; // Убедитесь, что у вас есть класс Service и переменная BASE_URL в нем
        courierRandom = courierData.generateRandom(); // Вызовите метод на объекте courierData
    }

    @After
    public void tearDown() {
        try {
            Response responseLogin = courierLogin(courierRandom);
            String courierId = responseLogin.then().extract().path("id").toString();
            courierDelete(courierId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Регистрация нового курьера")
    @Description("Проверка, что можно создать нового курьера с корректными введенными данными")
    public void createNewCourierPositiveTest() {
        Response response = courierCreate(courierRandom);
        compareResultToTrue(response, SC_CREATED);
    }

    @Test
    @DisplayName("Регистрация нового повторяющегося курьера")
    @Description("Проверка, что невозможно создать нового курьера, который уже существует")
    public void createNewDuplicateCourierTest() {
        // От первого ответ не нужен
        courierCreate(courierRandom);
        Response response = courierCreate(courierRandom);
        compareResultMessageToText(response, SC_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Регистрация нового курьера без логина")
    @Description("Проверка, что невозможно создать нового курьера без указания логина")
    public void createNewCourierNoLoginTest() {
        courierRandom.setLogin("");
        Response response = courierCreate(courierRandom);
        compareResultMessageToText(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }


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
    public void courierDelete(String courierId) {
        Response response = courierApiResponse.delete(courierId);
        printResponseBodyToConsole("Удаление курьера: ", response, Service.NEED_DETAIL_LOG);
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
}
