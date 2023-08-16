package DataAcessLayer.DTO.Suppliers;

public class ItemDiscountDTO {
    private int id;
    private int itemId;
    private int quantity;
    private int discount;

    public ItemDiscountDTO(int itemId, int quantity, int discount) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.discount = discount;
    }

    public ItemDiscountDTO(int id, int itemId, int quantity, int discount) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getDiscount() {
        return discount;
    }
}


