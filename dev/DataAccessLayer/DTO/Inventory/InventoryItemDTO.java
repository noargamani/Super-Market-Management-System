package DataAcessLayer.DTO.Inventory;

public class InventoryItemDTO {
    private int catalogNumber;
    private String name;
    private String manufacturer;
    private int totalAmount;
    private int minimumAmount;
    private int warehouseAmount;
    private int shelvesAmount;
    private double price;

    /**
     * Constructs an InventoryItemDTO object with the specified catalog number, name, manufacturer,
     * total amount, minimum amount, warehouse amount, shelves amount, and price.
     *
     * @param catalogNumber   The catalog number of the inventory item.
     * @param name            The name of the inventory item.
     * @param manufacturer    The manufacturer of the inventory item.
     * @param totalAmount     The total amount of the inventory item.
     * @param minimumAmount   The minimum amount required for the inventory item.
     * @param warehouseAmount The amount of the inventory item in the warehouse.
     * @param shelvesAmount   The amount of the inventory item on the shelves.
     * @param price           The price of the inventory item.
     */
    public InventoryItemDTO(int catalogNumber, String name, String manufacturer, int totalAmount, int minimumAmount, int warehouseAmount, int shelvesAmount, double price) {
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.manufacturer = manufacturer;
        this.totalAmount = totalAmount;
        this.minimumAmount = minimumAmount;
        this.warehouseAmount = warehouseAmount;
        this.shelvesAmount = shelvesAmount;
        this.price = price;
    }

    /**
     * Returns the catalog number of the inventory item.
     *
     * @return The catalog number.
     */
    public int getCatalogNumber() {
        return catalogNumber;
    }

    /**
     * Sets the catalog number of the inventory item.
     *
     * @param catalogNumber The catalog number to set.
     */
    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    /**
     * Returns the name of the inventory item.
     *
     * @return The name of the inventory item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the inventory item.
     *
     * @param name The name of the inventory item to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the manufacturer of the inventory item.
     *
     * @return The manufacturer of the inventory item.
     */
    public String getManufacturer() {
        return manufacturer;
    }
    /**
     * Sets the manufacturer of the inventory item.
     *
     * @param manufacturer The manufacturer of the inventory item to set.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Returns the total amount of the inventory item.
     *
     * @return The total amount of the inventory item.
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount of the inventory item.
     *
     * @param totalAmount The total amount of the inventory item to set.
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Returns the minimum amount required for the inventory item.
     *
     * @return The minimum amount required.
     */
    public int getMinimumAmount() {
        return minimumAmount;
    }

    /**
     * Sets the minimum amount required for the inventory item.
     *
     * @param minimumAmount The minimum amount required to set.
     */
    public void setMinimumAmount(int minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    /**
     * Returns the amount of the inventory item in the warehouse.
     *
     * @return The amount in the warehouse.
     */
    public int getWarehouseAmount() {
        return warehouseAmount;
    }

    /**
     * Sets the amount of the inventory item in the warehouse.
     *
     * @param warehouseAmount The amount in the warehouse to set.
     */
    public void setWarehouseAmount(int warehouseAmount) {
        this.warehouseAmount = warehouseAmount;
    }

    /**
     * Returns the amount of the inventory item on the shelves.
     *
     * @return The amount on the shelves.
     */
    public int getShelvesAmount() {
        return shelvesAmount;
    }

    /**
     * Sets the amount of the inventory item on the shelves.
     *
     * @param shelvesAmount The amount on the shelves to set.
     */
    public void setShelvesAmount(int shelvesAmount) {
        this.shelvesAmount = shelvesAmount;
    }

    /**
     * Returns the price of the inventory item.
     *
     * @return The price of the inventory item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the inventory item.
     *
     * @param price The price of the inventory item to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the InventoryItemDTO object.
     *
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        return "InventoryItemDTO{" +
                "catalogNumber=" + catalogNumber +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", totalAmount=" + totalAmount +
                ", minimumAmount=" + minimumAmount +
                ", warehouseAmount=" + warehouseAmount +
                ", shelvesAmount=" + shelvesAmount +
                ", price=" + price +
                '}';
    }
}