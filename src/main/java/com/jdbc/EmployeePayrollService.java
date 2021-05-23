package com.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    public EmployeePayrollService() {

    }

    public enum IOService {CONSOLE_IO, DB_IO, FILE_IO}
        public List<EmployeePayRollData> employeePayrollList = new ArrayList<>();

        public EmployeePayrollService(ArrayList<EmployeePayRollData> employeePayrollList) {
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

    public List<EmployeePayRollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(ioService.DB_IO))
            this.employeePayrollList = new EmployeePayrollDBService().readData();
        return this.employeePayrollList;
    }

    /**
     * @writeEmployeeData method is for writing the data
     */
    private void writeEmployeeData() {
        System.out.println("\nwriting employee payroll data to console\n"+ employeePayrollList);
    }

}

