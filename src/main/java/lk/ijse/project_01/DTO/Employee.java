package lk.ijse.project_01.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {
    String employeeId;
    String employeeName;
    String employeeAddress;
    String employeeTel;
    String employeePosition;
    double employeeSalary;

}
