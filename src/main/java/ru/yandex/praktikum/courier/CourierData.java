package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierData {

    @Description("Генератор тестовых данных для создания курьера")
    public Courier generateRandom() { // Измените сигнатуру метода на public Courier generateRandom() для возвращения объекта Courier
        final String login = RandomStringUtils.randomAlphabetic(10);
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login, password, firstName); // Создайте новый объект Courier с переданными параметрами и верните его
    }
}
