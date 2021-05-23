package com.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class MakeConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/employee_payroll_service?SSL=false";
        String userName = "root";
        String password = "Aiv@77094";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver load Successfully!!!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("can not find driver in the class path");
        }
        listDrivers();
        Connection connection;

        try {
            Connection connection1 = DriverManager.getConnection(jdbcUrl,userName,password);
            System.out.println("Connection SuccessFull!!!");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    /** listDrivers method is used for find list of driver
     *
     */
    public static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = driverList.nextElement();
            System.out.println(" "+driverClass.getClass().getName());
        }
    }


}
