package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final MessageOriginator messageOriginator = new MessageOriginator();

    @Override
    public void onUpdated(Message msg) {
        messageOriginator.saveState(msg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(messageOriginator.findMessage(id));
    }
}
