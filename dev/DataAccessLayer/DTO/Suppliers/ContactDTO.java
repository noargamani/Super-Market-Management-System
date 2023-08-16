package DataAcessLayer.DTO.Suppliers;


public class ContactDTO {
    private int id;
    private String supplierName;
    private String name;
    private String phone;

    public ContactDTO(int id, String supplierName, String name, String phone) {
        this.id = id;
        this.supplierName = supplierName;
        this.name = name;
        this.phone = phone;
    }
    public int getId() {
        return id;
    }
    public String getSupplierName() {
        return supplierName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

}

