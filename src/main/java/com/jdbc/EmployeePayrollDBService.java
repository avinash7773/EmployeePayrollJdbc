package com.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayRollDataStatement;

    private EmployeePayrollDBService() {

    }

    public static EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;

    }

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
        return this.getEmployeePayrollDateUsingDB(sql);
    }

    public List<EmployeePayRollData> getEmployeePayrollData(String name) {
        List<EmployeePayRollData> employeePayRollList = null;
        if (this.employeePayRollDataStatement == null)
            this.PreparedStatementForEmployeeData();
        try {
             employeePayRollDataStatement.setString(1,name);
            ResultSet resultSet = employeePayRollDataStatement.executeQuery();
            employeePayRollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollList;
    }

    private List<EmployeePayRollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayRollData> employeePayRollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate  = resultSet.getDate("start").toLocalDate();
                employeePayRollDataList.add(new EmployeePayRollData(id,name,salary,startDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollDataList;
    }

    private void PreparedStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee_payroll where name = ?";
            employeePayRollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<EmployeePayRollData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("select * from employee_payroll WHERE START BETWEEN '%s' AND '%s'",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDateUsingDB(sql);
    }

    private List<EmployeePayRollData> getEmployeePayrollDateUsingDB(String sql) {
        List<EmployeePayRollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    public int updateEmployeeDataUsingStatement(String name, double salary)  {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s' ",salary,name);
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return 0;
    }



}
