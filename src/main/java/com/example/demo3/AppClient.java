package com.example.demo3;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named
@ConversationScoped
public class AppClient implements Serializable {
    public void callProcedure() throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        CallableStatement callableStatement = connection.prepareCall("{call demo1(?)}");
        callableStatement.registerOutParameter(1, Types.INTEGER);
        callableStatement.execute();
        System.out.println(callableStatement.getInt(1));
    }

    public List<String[]> getEvents() throws SQLException {
        Connection connection = ConnectionUtils.getConnection();
        Statement statement = connection.createStatement();
        List<String[]> result = new ArrayList<>();
        ResultSet rs = statement.executeQuery("select * from event");
        while (rs.next()){
            result.add(new String[] {rs.getString(1), rs.getString(2)});
        }

        return result;
    }

    public static void main(String[] args) throws SQLException {
        new AppClient().callProcedure();
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void insertEvent() {
        Connection connection = ConnectionUtils.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into event(id, name) values(?, ?)");
            preparedStatement.setInt(1,this.getId());
            preparedStatement.setString(2,this.getName());
            preparedStatement.executeUpdate();
            status = "insert success";
        } catch (SQLException e) {
            status = "insert false";
        }
    }
}
