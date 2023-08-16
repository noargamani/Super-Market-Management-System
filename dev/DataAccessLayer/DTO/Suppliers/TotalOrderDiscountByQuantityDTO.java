package DataAcessLayer.DTO.Suppliers;

public class TotalOrderDiscountByQuantityDTO {
    private String supplier;
    private int quantity;
    private int discount;

    public TotalOrderDiscountByQuantityDTO(String supplier, int quantity, int discount) {
        this.supplier = supplier;
        this.quantity = quantity;
        this.discount = discount;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }
}

