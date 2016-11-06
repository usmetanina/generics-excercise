package com.github.drxaos.edu;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SqliteSavedList <E extends Serializable> extends AbstractList<E> implements SavedList{

    Connection connection = null;
    Statement stmt = null;

    private String tableName;
    private List list=new ArrayList();;

    public SqliteSavedList(String table) throws ClassNotFoundException, SQLException {

        this.tableName = table;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:list.db");
        connection.setAutoCommit(true);

        stmt = connection.createStatement();
        int count = 0;

        ResultSet res = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type ='table' and name='" + tableName + "';");

        while (res.next()) {
            count = res.getInt(1);
        }

        if (count == 0) {
            String sql = "CREATE TABLE " + this.tableName +
                        "(ID INT NOT NULL," +
                        " VALUE BLOB NOT NULL );";

            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            }
        else {
            load();
        }
    }

    public void load() {
    try {
        connection = DriverManager.getConnection("jdbc:sqlite:list.db");
        connection.setAutoCommit(true);
        stmt = connection.createStatement();

        ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName + ";");

        while (result.next()) {
            Object obj = result.getObject(2);
            E data = (E) obj;

            list.add((E) data);
        }

        result.close();
        stmt.close();
        connection.close();
    }
    catch (SQLException ex)
    {}
    }

    protected void deleteFromTable(int id) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:list.db");
        connection.setAutoCommit(true);
        stmt = connection.createStatement();

        stmt.executeUpdate("DELETE FROM " + tableName + " WHERE id = " + id);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = " + id);
        preparedStatement.executeUpdate();

        int currentId = id;
        for (int i = currentId; i < list.size(); i++) {
            id++;
            int oldDataId = id;
            oldDataId--;

            PreparedStatement preparedStatementUpdating = connection.prepareStatement("UPDATE " + tableName + " set id = ? where id = ?");
            preparedStatementUpdating.setInt(1, oldDataId);
            preparedStatementUpdating.setInt(2, id);
            preparedStatementUpdating.executeUpdate();
        }

        connection.close();
        saveToTable();
    }

    protected void saveToTable() throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite:list.db");
        connection.setAutoCommit(true);
        stmt = connection.createStatement();
        String sql;

        ResultSet result = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM " + tableName);
        result.next();
        int count = result.getInt("rowcount");
        result.close();

        for (int i = count; i < list.size(); i++) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?,?)");
            preparedStatement.setObject(1, list.get(i));
            preparedStatement.setObject(2, i);
            preparedStatement.executeUpdate();
        }

        stmt.close();
        connection.close();
    }

    protected void updateTable(int id, E elem) throws SQLException {

        connection = DriverManager.getConnection("jdbc:sqlite:list.db");

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + tableName + " set value = ? where id = ?");
        preparedStatement.setObject(1, elem);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();

        connection.close();
    }

    @Override
    public E get(int index) {
        return (E) list.get(index);
    }

    @Override
    public E set(int index, E element) {
        list.set(index, element);
        try
        {
            updateTable(index, element);
        }
        catch (SQLException ex)
        {
        }
        return this.get(index);
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        try
        {
            saveToTable();
        }
        catch (SQLException ex)
        {
        }
    }

    @Override
    public boolean add(E elem) {
        list.add(elem);
        try {
            saveToTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E obj=(E) this.list.remove(index);
        try
        {
            deleteFromTable(index);
        }
        catch (SQLException ex)
        {
        }
        return obj;
    }

    @Override
    public int size() {
        return list.size();
    }
}
