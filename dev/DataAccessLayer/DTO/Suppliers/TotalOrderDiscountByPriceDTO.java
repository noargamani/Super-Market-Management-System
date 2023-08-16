package DataAcessLayer.DTO.Suppliers;




public class TotalOrderDiscountByPriceDTO {
    private String supplier;
    private double price;
    private int discount;

    public TotalOrderDiscountByPriceDTO(String supplier, double price, int discount) {
        this.supplier = supplier;
        this.price = price;
        this.discount = discount;
    }

    public String getSupplier() {
        return supplier;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }
}
