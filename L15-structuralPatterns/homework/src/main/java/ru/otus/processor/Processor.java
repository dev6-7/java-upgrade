package ru.otus.processor;

import ru.otus.model.Message;

public interface Processor {

    Message process(Message message);

    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field12 - done

    //todo: 3. Сделать процессор - done, который будет выбрасывать исключение в четную секунду
    // (сделайте тест с гарантированным результатом) - done
    //         Секунда должна определяться во время выполнения. - done
    //         Тест - важная часть задания - done
    // Обязательно посмотрите пример к паттерну Мементо! - done
}
