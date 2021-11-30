package ru.otus.provider;

public class DateTimeProviderImpl implements DateTimeProvider{

    @Override
    public long getSecond() {
        return System.currentTimeMillis();
    }
}
