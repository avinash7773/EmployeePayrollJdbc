package jdbctest;

import com.jdbc.EmployeePayRollData;
import com.jdbc.EmployeePayrollService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}
