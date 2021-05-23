package com.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employee_payroll_service?SSL=false";
        String userName = "root";
        String password = "Aiv@77094";
        Connection connection;
        System.out.println("Connecting to database:"+ jdbcUrl);
        connection = DriverManager.getConnection(jdbcUrl,userName,password);
        return connection;
    }

    public List<EmployeePayRollData> readData() {
        String sql = "select * from employee_payroll";
         List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate  = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayRollData(id,name,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }
}
