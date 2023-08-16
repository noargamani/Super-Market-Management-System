package DataAcessLayer.DTO.Suppliers;


import java.util.ArrayList;

public class SupplierDTO {
    private int supplier_ID;
    private String supplier_name;
    private String address;
    private int Bn_number;

    public SupplierDTO(int supplier_ID, String supplier_name, String address, int Bn_number) {
        this.supplier_ID = supplier_ID;
        this.supplier_name = supplier_name;
        this.address = address;
        this.Bn_number = Bn_number;
    }

    public int getSupplier_ID() {
        return supplier_ID;
    }

    public void setSupplier_ID(int supplier_ID) {
        this.supplier_ID = supplier_ID;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getBn_number() {
        return Bn_number;
    }

    public void setBn_number(int Bn_number) {
        this.Bn_number = Bn_number;
    }

}
