package com.jdbc;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayRollData {

        int id;
        String name;
        double salary;
        LocalDate startDate;

        public EmployeePayRollData(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

    public EmployeePayRollData(int id, String name, double salary, LocalDate startDate) {
        this(id,name,salary);
        this.startDate = startDate;

    }

    @Override
        public String toString() {
            return "EmployeePayrollData{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", salary=" + salary +
                    '}';
        }
        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayRollData that = (EmployeePayRollData) o;
        return id == that.id && Double.compare(that.salary, salary) == 0
                && Objects.equals(name, that.name) && Objects.equals(startDate, that.startDate);
    }

}

