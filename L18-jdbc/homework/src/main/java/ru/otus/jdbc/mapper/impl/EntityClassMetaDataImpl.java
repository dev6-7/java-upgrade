package ru.otus.jdbc.mapper.impl;

import ru.otus.crm.model.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> obj;

    public EntityClassMetaDataImpl(Class<T> obj) {
        this.obj = obj;
    }

    @Override
    public String getName() {
        return obj.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return obj.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Field getIdField() {
        Field idField = Arrays.stream(obj.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> {
                    throw new NullPointerException("Class havent id field");
                });
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(obj.getDeclaredFields()).toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(obj.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
