package com.jdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "select gender, AVG(salary) as avg_salary FROM employee_payroll GROUP BY gender";
        Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double avgSalary = resultSet.getDouble("avg_salary");
                genderToAverageSalaryMap.put(gender,avgSalary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genderToAverageSalaryMap;
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


    public EmployeePayRollData addEmployeeToPayrollUC7(String name, double salary, LocalDate startDate, String gender) {
        int employeeId = -1;
        EmployeePayRollData employeePayRollData = null;
        String sql = String.format("INSERT INTO employee_payroll (name,gender,salary,start) " + "VALUES ('%s', '%s', '%s', '%s')"
        ,name,gender,salary, Date.valueOf(startDate));
        try {
            Connection connection = this.getConnection();
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
            employeePayRollData = new  EmployeePayRollData(employeeId,name,salary,startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollData;
    }

    public EmployeePayRollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) {
        int employeeId = -1;
        EmployeePayRollData employeePayRollData = null;
        Connection connection = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try(Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO employee_payroll (name,gender,salary,start) " + "VALUES ('%s', '%s', '%s', '%s')"
                    ,name,gender,salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) employeeId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
                e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        try(Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxable_pay = salary - deductions;
            double tax = taxable_pay * 0.1;
            double net_pay  = salary - tax;
            String sql = String.format("INSERT INTO payroll_details (employee_id, basic_pay, deductions, taxable_pay, tax, net_pay)"+
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                    employeeId, salary, deductions, taxable_pay, tax, net_pay);
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                employeePayRollData = new  EmployeePayRollData(employeeId,name,salary,startDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayRollData;
    }
}
