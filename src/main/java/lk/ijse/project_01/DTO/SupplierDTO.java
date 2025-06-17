package lk.ijse.project_01.DTO;

import java.util.Objects;

public class SupplierDTO {
    private String supplierId;
    private String name;
    private String nic;
    private String address;
    private String phone;
    private String email;
    private String supply;


    public SupplierDTO() {
    }


    public SupplierDTO(String supplierId, String name, String nic, String address, String phone, String email, String supply) {
        this.supplierId = supplierId;
        this.name = name;
        this.nic = nic;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.supply = supply;
    }


    public SupplierDTO(String name, String nic, String address, String phone, String email, String supply) {
        this.name = name;
        this.nic = nic;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.supply = supply;
    }


    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    @Override
    public String toString() {
        return "SupplierDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", name='" + name + '\'' +
                ", nic='" + nic + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", supply='" + supply + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierDTO)) return false;
        SupplierDTO that = (SupplierDTO) o;
        return Objects.equals(getSupplierId(), that.getSupplierId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getNic(), that.getNic()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPhone(), that.getPhone()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getSupply(), that.getSupply());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSupplierId(), getName(), getNic(), getAddress(), getPhone(), getEmail(), getSupply());
    }
}
