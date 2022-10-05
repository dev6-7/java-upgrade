package ru.otus.jdbc.mapper.impl;

import ru.otus.crm.model.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        String tableName = entityClassMetaData.getName();
        String idFieldName = entityClassMetaData.getIdField().getName();

        return String.format("SELECT * FROM %s WHERE %s = ?", tableName, idFieldName);
    }

    @Override
    public String getInsertSql() {
        String tableName = entityClassMetaData.getName();
        List<Field> fields = (List<Field>) entityClassMetaData.getAllFields();
        String fieldNames = fields.stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        return String.format("insert into %s(%s) values (?)", tableName, fieldNames);
    }

    @Override
    public String getUpdateSql() {
        return "";
    }
}
