package lk.ijse.project_01.Model;

import lk.ijse.project_01.DTO.AddFoodbevarge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;

import static lk.ijse.project_01.Util.CrudUtil.getConnection;

public class FoodBevarageAddPageModel {




    public String getLastItemIdByPrefix(String prefix) throws SQLException {
        String sql = "SELECT id FROM fooditem WHERE id LIKE ? ORDER BY id DESC LIMIT 1";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, prefix + "%");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
            return null;
        }
    }

    public List<AddFoodbevarge> getAllItems() throws SQLException {
        String sql = "SELECT * FROM fooditem";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<AddFoodbevarge> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new AddFoodbevarge(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price")
                ));
            }
            return list;
        }
    }

    public boolean addItem(AddFoodbevarge item) throws SQLException {
        String sql = "INSERT INTO fooditem (id, name, category, price) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setString(3, item.getCategory());
            pst.setDouble(4, item.getPrice());
            return pst.executeUpdate() > 0;
        }
    }

    public boolean updateItem(AddFoodbevarge item) throws SQLException {
        String sql = "UPDATE fooditem SET name=?, category=?, price=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, item.getName());
            pst.setString(2, item.getCategory());
            pst.setDouble(3, item.getPrice());
            pst.setString(4, item.getId());
            return pst.executeUpdate() > 0;
        }
    }

    public boolean deleteItem(String id) throws SQLException {
        String sql = "DELETE FROM fooditem WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, id);
            return pst.executeUpdate() > 0;
        }
    }
}
