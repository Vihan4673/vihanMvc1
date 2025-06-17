package lk.ijse.project_01.Model;

import lk.ijse.project_01.DTO.GuestDTO;
import lk.ijse.project_01.Util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuestModel {

    public String getNextGuestId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select GuestID from guest order by GuestID desc limit 1");
        char tableCharacter = 'G';

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            if (lastId != null && lastId.length() > 1) {
                String lastIdNumberString = lastId.substring(1);
                int lastIdNumber = Integer.parseInt(lastIdNumberString);
                int nextIdNumber = lastIdNumber + 1;
                return String.format("%c%03d", tableCharacter, nextIdNumber);
            }
        }
        return tableCharacter + "001";
    }

    public boolean saveGuest(GuestDTO guestDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into guest values (?,?,?,?,?)",
                guestDTO.getGuestId(),
                guestDTO.getName(),
                guestDTO.getNic(),
                guestDTO.getAddress(),
                guestDTO.getPhone()
        );
    }

    public boolean deleteGuest(String guestId) throws SQLException {
        return CrudUtil.execute("delete from guest where GuestID=?", guestId);
    }

    public static boolean updateGuest(GuestDTO guestDTO) throws SQLException {
        return CrudUtil.execute(
                "update guest set name=?, NIC=?, address=?, Contact=? where GuestID=?",
                guestDTO.getName(),
                guestDTO.getNic(),
                guestDTO.getAddress(),
                guestDTO.getPhone(),
                guestDTO.getGuestId()
        );
    }

    public ArrayList<GuestDTO> getAllGuest() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from guest");
        ArrayList<GuestDTO> guestDTOArrayList = new ArrayList<>();

        while (resultSet.next()) {
            guestDTOArrayList.add(new GuestDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }

        return guestDTOArrayList;
    }

    public ArrayList<String> getAllGuestId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select GuestID from guest");
        ArrayList<String> list = new ArrayList<>();

        while (rst.next()) {
            list.add(rst.getString(1));
        }

        return list;
    }

    public String findNameById(String selectedGuestId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select name from guest where GuestID=?", selectedGuestId);
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public GuestDTO getGuestByPhone(String phone) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM guest WHERE Contact = ?", phone);

        if (resultSet.next()) {
            return new GuestDTO(
                    resultSet.getString("GuestID"),
                    resultSet.getString("name"),
                    resultSet.getString("NIC"),
                    resultSet.getString("address"),
                    resultSet.getString("Contact")
            );
        }

        return null;
    }


    public boolean deleteCustomer(String guestId) throws SQLException {

        return deleteGuest(guestId);
    }

    public boolean saveCustomer(GuestDTO guestDTO) throws SQLException {

        return saveGuest(guestDTO);
    }
}
