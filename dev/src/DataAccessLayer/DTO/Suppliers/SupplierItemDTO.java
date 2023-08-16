package DataAcessLayer.DTO.Suppliers;

import DataAcessLayer.DTO.DTO;

import java.util.Date;

public class SupplierItemDTO extends DTO {
    private final int itemId;
    private final int supplierCatalogId;
    private final String supplier;
    private final String itemName;
    private final String itemManufacture;
    private final int totalAmount;
    private final double price;
    private final int quantity;
    private final Date expiration;

    public SupplierItemDTO(int itemId, int supplierCatalogId, String supplier, String itemName,
                           String itemManufacture, int totalAmount, double price, int quantity, Date expiration) {
        this.itemId = itemId;
        this.supplierCatalogId = supplierCatalogId;
        this.supplier = supplier;
        this.itemName = itemName;
        this.itemManufacture = itemManufacture;
        this.totalAmount = totalAmount;
        this.price = price;
        this.quantity = quantity;
        this.expiration = expiration;
    }

    public int getItemId() {
        return itemId;
    }

    public int getSupplierCatalogId() {
        return supplierCatalogId;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemManufacture() {
        return itemManufacture;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "SupplierItemDTO{" +
                "itemId=" + itemId +
                ", supplierCatalogId=" + supplierCatalogId +
                ", supplier='" + supplier + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemManufacture='" + itemManufacture + '\'' +
                ", totalAmount=" + totalAmount +
                ", price=" + price +
                ", quantity=" + quantity +
                ", expiration=" + expiration +
                '}';
    }
}
