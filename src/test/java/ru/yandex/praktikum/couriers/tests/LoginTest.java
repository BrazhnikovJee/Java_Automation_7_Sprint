package ru.yandex.praktikum.couriers.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.courier.Courier;
import ru.yandex.praktikum.courier.CourierData;
import ru.yandex.praktikum.couriers.step.CourierSteps;
import ru.yandex.praktikum.service.Service;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;


public class LoginTest extends Service {


    private final CourierData courierData = new CourierData();
    private Courier courierRandom;
    private final CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp() {
        courierRandom = courierData.generateRandom();
    }

    @After
    public void tearDown() {
        try {
            Response responseLogin = courierSteps.courierLogin(courierRandom);
            String courierId = responseLogin.then().extract().path("id").toString();
            courierSteps.courierDelete(courierId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Логин существующего курьера")
    @Description("Проверка, что можно авторизоваться с корректными введенными данными")
    public void loginCourierPositiveTest() {
        courierSteps.courierCreate(courierRandom);
        Response response = courierSteps.courierLogin(courierRandom);
        courierSteps.compareIdNotNull(response);
    }

    @Test
    @DisplayName("Логин курьера без логина")
    @Description("Проверка, что невозможно авторизоваться без логина")
    public void loginCourierWithoutLoginNameTest() {
        courierRandom.setLogin("");
        Response response = courierSteps.courierLogin(courierRandom);
        courierSteps.compareResultMessageToText(response, SC_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    @Description("Проверка, что невозможно авторизоваться без пароля")
    public void loginCourierWithoutPasswordTest() {
        courierRandom.setPassword("");
        Response response = courierSteps.courierLogin(courierRandom);
        courierSteps.compareResultMessageToText(response, SC_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Проверка, что невозможно авторизоваться незарегистрированному курьеру")
    public void loginCourierNotExistedTest() {
        Response response = courierSteps.courierLogin(courierRandom);
        courierSteps.compareResultMessageToText(response, SC_NOT_FOUND, "Учетная запись не найдена");
    }

}
