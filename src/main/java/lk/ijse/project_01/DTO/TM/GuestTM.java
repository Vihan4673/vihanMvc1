package lk.ijse.project_01.DTO.TM;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GuestTM {

    private String GuestId;
    private String name;
    private String nic;
    private String address;
    private String phone;
}