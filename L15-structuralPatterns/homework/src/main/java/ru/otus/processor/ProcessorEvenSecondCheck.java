package ru.otus.processor;

import ru.otus.model.Message;
import ru.otus.provider.DateTimeProvider;

public class ProcessorEvenSecondCheck implements Processor {

    public static class EvenSecondException extends RuntimeException {
        public EvenSecondException() {
            super("Second is even. BOOM.");
        }
    }

    private DateTimeProvider dateTimeProvider;

    public ProcessorEvenSecondCheck(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        var second = dateTimeProvider.getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException();
        }
        return message;
    }
}
