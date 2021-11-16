package orm;

import orm.annotations.Column;
import orm.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static orm.EntityManagerUtils.*;

public class EntityManager<E> implements DBContext<E> {
    private static final String SELECT_STAR_FROM = "SELECT * FROM ";
    private static final String INSERT_QUERY = "INSERT INTO %s (%s) VALUE (%s) ;";
    private static final String UPDATE_QUERY = "UPDATE %s SET %s WHERE %s ;";
    private static final String DELETE_QUERY = "DELETE FROM %s WHERE %s ;";
    private Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws IllegalAccessException, SQLException {
        Field primaryKey = getId(entity.getClass());
        primaryKey.setAccessible(true);
        Object value = primaryKey.get(entity);
        return (value != null && (int) value > 0) ? this.doUpdate(entity, primaryKey) : this.doInsert(entity, primaryKey);
    }

    @Override
    public Iterable<E> find(Class<E> table) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return this.find(table,null);
    }

    @Override
    public Iterable<E> find(Class<E> table, String where) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        Statement statement = connection.createStatement();
        String tableName = getTableName(table);
        String query = String.format("%s %s %s ;", SELECT_STAR_FROM, tableName,
                where != null ? " WHERE " + where : "");
        ResultSet resultSet = statement.executeQuery(query);
        List<E> entities = new ArrayList<>();
        while (resultSet.next()){
            E entity = table.getDeclaredConstructor().newInstance();
            fillEntity(table, resultSet, entity);
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public E findFirst(Class<E> table) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        return findFirst(table, "null");
    }

    @Override
    public E findFirst(Class<E> table, String where) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {
        Statement statement = connection.createStatement();
        String tableName = getTableName(table);
        String query = String.format("%s %s %s LIMIT 1;", SELECT_STAR_FROM, tableName,
                where != null ? " WHERE " + where : "");
        ResultSet resultSet = statement.executeQuery(query);
        E entity = table.getDeclaredConstructor().newInstance();
        resultSet.next();
        fillEntity(table, resultSet, entity);
        return entity;
    }

    @Override
    public E findById(Class<E> table, int id) throws IllegalAccessException, SQLException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return findFirst(table, String.format("id = %d", id));
    }

    @Override
    public int delete(Class<E> table, int id) throws SQLException {
        String query = String.format(DELETE_QUERY, getTableName(table), "id = " + id);
        return executeUpdate(connection, query);
    }


    // Utility methods
    private boolean doInsert(Object entity, Field primary) throws SQLException {
        String tableName = getTableName(entity.getClass());
        String columns = String.join(", ", getFieldNames(entity));
        String values = String.join(", ", getFieldValues(entity));
        String query = String.format(INSERT_QUERY, tableName, columns, values);
        return connection.prepareStatement(query).execute();
    }

    private boolean doUpdate(Object entity, Field primaryKey) throws IllegalAccessException, SQLException {
        String tableName = getTableName(entity.getClass());
        Function<Field, String> getFieldNameAndValue = (Field f) -> {
            f.setAccessible(true);
            try {
                return String.format(" %s = %s",
                        f.isAnnotationPresent(Id.class)
                                ? "id" : f.getAnnotation(Column.class).name(),
                        f.getType() == String.class || f.getType() == LocalDate.class
                                ? "'" + f.get(entity) + "'"
                                : f.get(entity).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return "";
        };
        List<String> setFieldNameAndValues = Arrays.stream(entity.getClass().getDeclaredFields())
                .map(getFieldNameAndValue)
                .collect(Collectors.toList());

        String insertQuery = String.format(UPDATE_QUERY,
                tableName, String.join(", ", setFieldNameAndValues),
                " id = " + primaryKey.get(entity));

        return connection.prepareStatement(insertQuery).execute();
    }


}