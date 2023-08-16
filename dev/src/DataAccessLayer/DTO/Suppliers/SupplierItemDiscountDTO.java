package DataAcessLayer.DTO.Suppliers;




public class SupplierItemDiscountDTO {
    private int ItemId;
    private double price;
    private int discount;
    private String type;

    public SupplierItemDiscountDTO(int supplier, double price, int discount, String type) {
        this.ItemId = supplier;
        this.price = price;
        this.discount = discount;
        this.type = type;
    }

    public int getItemId() {
        return ItemId;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public String getType() {
        return type;
    }
}
