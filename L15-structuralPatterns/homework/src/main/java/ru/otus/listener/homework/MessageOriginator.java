package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;

public class MessageOriginator {
    private Map<Long, Message> history = new HashMap<>();

    public void saveState(Message msg) {
        history.put(msg.getId(), Message.copy(msg));
    }

    public Message findMessage(long id){
        return history.get(id);
    }
}
