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

import static org.apache.http.HttpStatus.SC_OK;

public class DeleteTest extends Service {


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
    @DisplayName("Удаление курьера")
    @Description("Проверка, что курьера можно удалить")
    public void deleteCourierPositiveTest() {
        courierSteps.courierCreate(courierRandom);
        Response responseLogin = courierSteps.courierLogin(courierRandom);
        String courierId = responseLogin.then().extract().path("id").toString();
        Response response = courierSteps.courierDelete(courierId);
        courierSteps.compareResultToTrue(response, SC_OK);
    }
}
