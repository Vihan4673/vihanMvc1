package lk.ijse.project_01.Model;

import lk.ijse.project_01.DB.DBConnection;
import lk.ijse.project_01.DTO.SupplierDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        return DBConnection.getInstance().getConnection();
    }

    public String getNextSupplierId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT supplierId FROM Supplier ORDER BY supplierId DESC LIMIT 1";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("supplierId");

                if (lastId != null && lastId.length() > 1) {
                    try {
                        int idNum = Integer.parseInt(lastId.substring(1));
                        idNum++; // increment
                        return String.format("S%03d", idNum);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid supplierId format in DB: " + lastId);
                        return "S001";
                    }
                } else {
                    return "S001";
                }
            } else {
                return "S001";
            }
        }
    }

    public List<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Supplier";
        List<SupplierDTO> suppliers = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new SupplierDTO(
                        rs.getString("supplierId"),
                        rs.getString("name"),
                        rs.getString("nic"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("supply")
                ));
            }
        }

        return suppliers;
    }

    public boolean saveSupplier(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Supplier (supplierId, name, nic, address, phone, email, supply) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, supplier.getSupplierId());
            pst.setString(2, supplier.getName());
            pst.setString(3, supplier.getNic());
            pst.setString(4, supplier.getAddress());
            pst.setString(5, supplier.getPhone());
            pst.setString(6, supplier.getEmail());
            pst.setString(7, supplier.getSupply());

            return pst.executeUpdate() > 0;
        }
    }

    public boolean updateSupplier(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Supplier SET name=?, nic=?, address=?, phone=?, email=?, supply=? WHERE supplierId=?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getNic());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getPhone());
            pst.setString(5, supplier.getEmail());
            pst.setString(6, supplier.getSupply());
            pst.setString(7, supplier.getSupplierId());

            return pst.executeUpdate() > 0;
        }
    }

    public boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Supplier WHERE supplierId = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, supplierId);
            return pst.executeUpdate() > 0;
        }
    }


    }

