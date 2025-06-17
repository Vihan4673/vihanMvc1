package lk.ijse.project_01.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodBevargePageDTO {
    private int id;
    private String name;
    private String category;
    private double price;
}
