package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Id;
import ru.otus.crm.model.Manager;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Class<T> type;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, Class<T> clazz) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.type = clazz;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String query = entitySQLMetaData.getSelectByIdSql();
        return (Optional<T>) dbExecutor.executeSelect(connection, query, List.of(id), rs -> {
            try {
                if (type.equals(Client.class)) {
                    if (rs.next()) {
                        return new Client(rs.getLong("id"), rs.getString("name"));
                    }
                }

                if (type.equals(Manager.class)) {
                    if (rs.next()) {
                        return new Manager(rs.getLong("no"), rs.getString("label"), rs.getString("param1"));
                    }
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return (List<T>) dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                if (type.equals(Client.class)) {
                    List<Client> resultList = new ArrayList<>();
                    while (rs.next()) {
                        resultList.add(new Client(rs.getLong("id"),
                                rs.getString("name")));
                    }
                    return resultList;
                }
                if (type.equals(Manager.class)) {

                    List<Manager> resultList = new ArrayList<>();
                    while (rs.next()) {
                        resultList.add(new Manager(rs.getLong("no"), rs.getString("label"),
                                rs.getString("param1")));
                    }
                    return resultList;
                }

                return List.of();
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> values = new ArrayList<>();
        List<Field> fields = Arrays.stream(client.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                values.add(f.get(client));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        Map<String, Object> valuesMap = new HashMap<>();
        List<Field> fields = Arrays.stream(client.getClass().getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                valuesMap.put(f.getName(), f.get(client));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        StringBuffer sb = new StringBuffer("update %s set ");
        valuesMap.forEach((key, value) -> {
                    sb.append(key).append("=").append("'").append(value).append("'").append(",");
                }
        );
        //remove last ","
        if (valuesMap.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        try {
            Field id = Arrays.stream(client.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();
            id.setAccessible(true);
            sb.append(" where ").append(id.getName()).append("=").append(id.get(client));

        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
        String query = String.format(sb.toString(), client.getClass().getSimpleName().toLowerCase());
        dbExecutor.executeStatement(connection, query, List.of());
    }
}
