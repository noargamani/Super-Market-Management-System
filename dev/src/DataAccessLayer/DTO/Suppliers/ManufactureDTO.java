package DataAcessLayer.DTO.Suppliers;

public class ManufactureDTO {
    private int id;
    private String supplier;
    private String name;

    public ManufactureDTO(int id, String supplier, String name) {
        this.id = id;
        this.supplier = supplier;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getName() {
        return name;
    }
}