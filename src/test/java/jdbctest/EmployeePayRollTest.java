package jdbctest;

import com.jdbc.EmployeePayRollData;
import com.jdbc.EmployeePayrollService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.jdbc.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayRollTest {

    @Test
    void givenEmployeePayRollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayRollData> employeePayRollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayRollData.size());

    }

    @Test
    void givenNewSalaryForEmployee_WhenUpdateShouldSyncWithDB() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayRollData> employeePayRollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollSyncWithDB("Terisa");
        Assert.assertTrue(result);

    }

    @Test
    void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayRollData> employeePayRollData =
                employeePayrollService.readEmployeePayRollForDateRange(DB_IO,startDate,endDate);
        Assert.assertEquals(3,employeePayRollData.size());

    }

    @Test
    void givenPayrollData_WhenAverageSalaryRetrieved_ShouldReturnProperValue() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String , Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(20000.00) &&
                averageSalaryByGender.get("F").equals(3000000.00));
    }
}
