package lk.ijse.project_01.Model;

import javafx.beans.property.*;
import javafx.scene.control.Button;

public class OrderItem {
    private final StringProperty item;
    private final IntegerProperty quantity;
    private final DoubleProperty unitPrice;
    private final DoubleProperty total;
    private final Button deleteButton;

    public OrderItem(String item, int quantity, double unitPrice, double total, Button deleteButton) {
        this.item = new SimpleStringProperty(item);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.total = new SimpleDoubleProperty(total);
        this.deleteButton = deleteButton;
    }

    public String getItem() {
        return item.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public double getTotal() {
        return total.get();
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public void setTotal(double total) {
        this.total.set(total);
    }


    public StringProperty itemProperty() {
        return item;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public DoubleProperty totalProperty() {
        return total;
    }
}
