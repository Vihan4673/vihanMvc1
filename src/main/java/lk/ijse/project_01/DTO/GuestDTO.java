package lk.ijse.project_01.DTO;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class GuestDTO {
    private String guestId;
    private String name;
    private String nic;
    private String address;
    private String phone;

}