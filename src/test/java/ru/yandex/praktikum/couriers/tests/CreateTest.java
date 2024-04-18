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

import static org.apache.http.HttpStatus.*;


public class CreateTest extends Service {

    private final CourierData courierData = new CourierData();
    private Courier courierRandom;
    private final CourierSteps courierSteps = new CourierSteps();


    @Before
    public void setUp() {
        courierRandom = courierData.generateRandom(); // Вызовите метод на объекте courierData
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
    @DisplayName("Регистрация нового курьера")
    @Description("Проверка, что можно создать нового курьера с корректными введенными данными")
    public void createNewCourierPositiveTest() {
        Response response = courierSteps.courierCreate(courierRandom);
        courierSteps.compareResultToTrue(response, SC_CREATED);
    }

    @Test
    @DisplayName("Регистрация нового повторяющегося курьера")
    @Description("Проверка, что невозможно создать нового курьера, который уже существует")
    public void createNewDuplicateCourierTest() {
        // От первого ответ не нужен
        courierSteps.courierCreate(courierRandom);
        Response response = courierSteps.courierCreate(courierRandom);
        courierSteps.compareResultMessageToText(response, SC_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Регистрация нового курьера без логина")
    @Description("Проверка, что невозможно создать нового курьера без указания логина")
    public void createNewCourierNoLoginTest() {
        courierRandom.setLogin("");
        Response response = courierSteps.courierCreate(courierRandom);
        courierSteps.compareResultMessageToText(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }


}
