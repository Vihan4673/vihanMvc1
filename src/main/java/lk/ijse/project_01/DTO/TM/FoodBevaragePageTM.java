package lk.ijse.project_01.DTO.TM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodBevaragePageTM {
    private String id;
    private String name;
    private String category;
    private double price;
}
