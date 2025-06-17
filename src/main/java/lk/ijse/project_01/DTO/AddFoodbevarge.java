package lk.ijse.project_01.DTO;

public class AddFoodbevarge {
    private String id;
    private String name;
    private String category;
    private double price;

    public AddFoodbevarge(String id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getItemId() {
        return id;
    }

}
