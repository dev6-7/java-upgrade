package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorEvenSecondCheck implements Processor {

    public static class EvenSecondException extends RuntimeException {
        public EvenSecondException() {
            super("Second is even. BOOM.");
        }
    }

    @Override
    public Message process(Message message) {
        var second = LocalDateTime.now().getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException();
        }
        return message;
    }
}
