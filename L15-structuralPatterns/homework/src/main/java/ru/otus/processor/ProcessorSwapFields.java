package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorSwapFields implements Processor {

    @Override
    public Message process(Message message) {
        String field12 = message.getField12();
        String field11 = message.getField11();
        return message.toBuilder().field12(field11).field11(field12).build();
    }
}
