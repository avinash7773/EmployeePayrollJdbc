package com.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {CONSOLE_IO, DB_IO, FILE_IO}

    List<EmployeePayRollData> employeePayrollList;
    private static EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayRollData> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    public static void main(String[] args) {
        System.out.println("Wel-Come to EmployeePayrollservice");
        ArrayList<EmployeePayRollData> employeePayrollList = new ArrayList<EmployeePayRollData>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeeData(consoleInputReader);
        employeePayrollService.writeEmployeeData();
    }

        /**
         * @readEmployeeData method is for read data from console
         */
        private void readEmployeeData(Scanner consoleInputReader) {
            System.out.println("Enter Employee id: ");
            int id = consoleInputReader.nextInt();
            System.out.println("Enter Employee name ");
            String name = consoleInputReader.next();
            System.out.println("Enter Employee salary ");
            double salary = consoleInputReader.nextDouble();
            employeePayrollList.add(new EmployeePayRollData(id,name,salary));
        }

    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
            if(ioService.equals(IOService.DB_IO))
               return employeePayrollDBService.getAverageSalaryByGender();
        return null;
    }

    public List<EmployeePayRollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(ioService.DB_IO))
            this.employeePayrollList = employeePayrollDBService.readData();
        return this.employeePayrollList;
    }

    public List<EmployeePayRollData> readEmployeePayRollForDateRange(IOService ioService,
                                                                     LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate,endDate);
        return  null;
    }

    public boolean checkEmployeePayrollSyncWithDB(String name) {
            List<EmployeePayRollData> employeePayRollDataList = employeePayrollDBService.getEmployeePayrollData(name);
            return  employeePayRollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public void updateEmployeeSalary(String name, double salary) {
           int result = employeePayrollDBService.updateEmployeeData(name,salary);
           if (result == 0) return;
           EmployeePayRollData employeePayRollData = this.getEmployeePayrollData(name);
           if (employeePayRollData != null)
               employeePayRollData.salary = salary;
        }

    private EmployeePayRollData getEmployeePayrollData(String name) {
            return employeePayrollList.stream()
                    .filter(employeePayRollDataItem -> employeePayRollDataItem.name.equals(name))
                    .findFirst().orElse(null);
        }

    /**
     * @writeEmployeeData method is for writing the data
     */
    private void writeEmployeeData() {
        System.out.println("\nwriting employee payroll data to console\n"+ employeePayrollList);
    }

}

