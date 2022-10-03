package ru.otus.core.repository.executor;

import ru.otus.core.sessionmanager.DataBaseOperationException;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public long executeStatement(Connection connection, String sql, List<Object> params) {
        System.out.println(sql);
        if (params.size() > 1) {
            String parms = params.stream().map(o -> "?").collect(Collectors.joining(","));
            sql = sql.substring(0, sql.length() - 2) + parms + ")";
        }
        try (var pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (var idx = 0; idx < params.size(); idx++) {
                if (params.get(idx) == null) {
                    pst.setObject(idx + 1, params.get(idx), Types.INTEGER);
                } else {
                    pst.setObject(idx + 1, params.get(idx));
                }
            }
            pst.executeUpdate();
            try (var rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeInsert error", ex);
        }
    }

    @Override
    public <T> Optional<T> executeSelect(Connection connection, String sql, List<Object> params, Function<ResultSet, T> rsHandler) {
        try (var pst = connection.prepareStatement(sql)) {
            for (var idx = 0; idx < params.size(); idx++) {
                pst.setObject(idx + 1, params.get(idx));
            }
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        } catch (SQLException ex) {
            throw new DataBaseOperationException("executeSelect error", ex);
        }
    }
}
