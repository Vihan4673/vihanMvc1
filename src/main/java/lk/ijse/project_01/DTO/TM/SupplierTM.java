package lk.ijse.project_01.DTO.TM;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SupplierTM {

    private final StringProperty supplierId;
    private final StringProperty name;
    private final StringProperty nic;
    private final StringProperty address;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty supply;


    public SupplierTM() {
        this("", "", "", "", "", "", "");
    }


    public SupplierTM(String supplierId, String name, String nic, String address, String phone, String email, String supply) {
        this.supplierId = new SimpleStringProperty(supplierId != null ? supplierId : "");
        this.name = new SimpleStringProperty(name != null ? name : "");
        this.nic = new SimpleStringProperty(nic != null ? nic : "");
        this.address = new SimpleStringProperty(address != null ? address : "");
        this.phone = new SimpleStringProperty(phone != null ? phone : "");
        this.email = new SimpleStringProperty(email != null ? email : "");
        this.supply = new SimpleStringProperty(supply != null ? supply : "");
    }



    public String getSupplierId() {
        return supplierId.get();
    }

    public StringProperty supplierIdProperty() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId.set(supplierId != null ? supplierId : "");
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name != null ? name : "");
    }

    public String getNic() {
        return nic.get();
    }

    public StringProperty nicProperty() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic.set(nic != null ? nic : "");
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address != null ? address : "");
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone != null ? phone : "");
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email != null ? email : "");
    }

    public String getSupply() {
        return supply.get();
    }

    public StringProperty supplyProperty() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply.set(supply != null ? supply : "");
    }

    @Override
    public String toString() {
        return "SupplierTM{" +
                "supplierId='" + getSupplierId() + '\'' +
                ", name='" + getName() + '\'' +
                ", nic='" + getNic() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", supply='" + getSupply() + '\'' +
                '}';
    }
}
