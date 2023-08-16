package DataAcessLayer.DTO.Suppliers;



public class ItemForOrderDTO {
    private int orderId;
    private int catalogId;
    private double totalPriceWithoutDiscount;
    private double totalDiscount;
    private double finalPrice;
    private int orderAmount;

    public ItemForOrderDTO(int orderId, int catalogId, double totalPriceWithoutDiscount, double totalDiscount, double finalPrice, int orderAmount) {
        this.orderId = orderId;
        this.catalogId = catalogId;
        this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
        this.totalDiscount = totalDiscount;
        this.finalPrice = finalPrice;
        this.orderAmount = orderAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public double getTotalPriceWithoutDiscount() {
        return totalPriceWithoutDiscount;
    }

    public void setTotalPriceWithoutDiscount(double totalPriceWithoutDiscount) {
        this.totalPriceWithoutDiscount = totalPriceWithoutDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    @Override
    public String toString() {
        return "ItemForOrderDTO{" +
                "orderId=" + orderId +
                ", catalogId=" + catalogId +
                ", totalPriceWithoutDiscount=" + totalPriceWithoutDiscount +
                ", totalDiscount=" + totalDiscount +
                ", finalPrice=" + finalPrice +
                ", orderAmount=" + orderAmount +
                '}';
    }
}

