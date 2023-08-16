package DataAcessLayer.DTO.Suppliers;



import java.util.Date;

public class OrderDTO {
    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                ", status='" + status + '\'' +
                ", dateOrderIssued=" + dateOrderIssued +
                ", supplierName='" + supplierName + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", priceBeforeDiscount=" + priceBeforeDiscount +
                '}';
    }

    private int orderId;
    private String status;
    private Date dateOrderIssued;
    private String supplierName;
    private int totalQuantity;
    private double priceBeforeDiscount;

    public OrderDTO(int orderId, String status, Date dateOrderIssued, String supplierName, int totalQuantity, double priceBeforeDiscount) {
        this.orderId = orderId;
        this.status = status;
        this.dateOrderIssued = dateOrderIssued;
        this.supplierName = supplierName;
        this.totalQuantity = totalQuantity;
        this.priceBeforeDiscount = priceBeforeDiscount;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateOrderIssued() {
        return dateOrderIssued;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }
}
