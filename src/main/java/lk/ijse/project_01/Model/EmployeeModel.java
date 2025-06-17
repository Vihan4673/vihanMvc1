package lk.ijse.project_01.Model;

import lk.ijse.project_01.DTO.Employee;
import lk.ijse.project_01.DB.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee (EmployeeID, eName, phoneNo, address, postion, salary) VALUES (?,?,?,?,?,?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, employee.getEmployeeId());
        pstm.setObject(2, employee.getEmployeeName());
        pstm.setObject(3, employee.getEmployeeTel());
        pstm.setObject(4, employee.getEmployeeAddress());
        pstm.setObject(5, employee.getEmployeePosition());
        pstm.setObject(6, employee.getEmployeeSalary());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM employee WHERE EmployeeID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET eName = ?, phoneNo = ?, address = ?, postion = ?, salary = ? WHERE EmployeeID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, employee.getEmployeeName());
        pstm.setObject(2, employee.getEmployeeTel());
        pstm.setObject(3, employee.getEmployeeAddress());
        pstm.setObject(4, employee.getEmployeePosition());
        pstm.setObject(5, employee.getEmployeeSalary());
        pstm.setObject(6, employee.getEmployeeId());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchId(String id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE EmployeeID = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getString("EmployeeID"),
                    resultSet.getString("eName"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("postion"),
                    resultSet.getDouble("salary")
            );
        }
        return null;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        List<Employee> empList = new ArrayList<>();
        while (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getString("EmployeeID"),
                    resultSet.getString("eName"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("postion"),
                    resultSet.getDouble("salary")
            );
            empList.add(employee);
        }
        return empList;
    }

    public static List<String> getNameList() throws SQLException {
        String sql = "SELECT eName FROM employee";
        ResultSet resultSet = DBConnection.getInstance().getConnection().prepareStatement(sql).executeQuery();

        List<String> nameList = new ArrayList<>();
        while (resultSet.next()) {
            nameList.add(resultSet.getString("eName"));
        }
        return nameList;
    }

    public static String getName(String employeeId) throws SQLException {
        String sql = "SELECT eName FROM employee WHERE employeeId = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, employeeId);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("eName");
        }
        return null;
    }

    public static Employee searchByName(String nameValue) throws SQLException {
        String sql = "SELECT * FROM employee WHERE eName = ?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, nameValue);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new Employee(
                    resultSet.getString("EmployeeIDzz"),
                    resultSet.getString("eName"),
                    resultSet.getString("address"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("postion"),
                    resultSet.getDouble("salary")
            );
        }
        return null;
    }
}
