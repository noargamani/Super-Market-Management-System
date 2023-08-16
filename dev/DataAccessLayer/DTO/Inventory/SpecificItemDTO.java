package DataAcessLayer.DTO.Inventory;

import java.time.LocalDate;

public class SpecificItemDTO {
    private int catalogNumber;
    private int ID;
    private double costPrice;
    private double sellingPrice;
    private LocalDate expiration;
    private boolean defective;
    private String location;
    private String defectType;
    private LocalDate DeliverTime;


    /**
     * Constructs a SpecificItemDTO object with the specified catalog number, ID, cost price, selling price, expiration date, defective flag, defect type, and location.
     *
     * @param catalogNumber The catalog number of the specific item.
     * @param ID            The ID of the specific item.
     * @param costPrice     The cost price of the specific item.
     * @param sellingPrice  The selling price of the specific item.
     * @param expiration    The expiration date of the specific item.
     * @param defective     A flag indicating whether the specific item is defective or not.
     * @param defectType    The type of defect in the specific item.
     * @param location      The location of the specific item.
     */
    public SpecificItemDTO(int catalogNumber, int ID, double costPrice, double sellingPrice, LocalDate expiration, boolean defective, String defectType,String location, LocalDate date) {
        this.catalogNumber = catalogNumber;
        this.ID = ID;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.expiration = expiration;
        this.defective = defective;
        this.location = location;
        this.defectType = defectType;
        this.DeliverTime = date;
    }

    // getters and setters
    /**
     * Returns the catalog number of the specific item.
     *
     * @return The catalog number.
     */
    public int getCatalogNumber() {
        return catalogNumber;
    }

    /**
     * Sets the catalog number of the specific item.
     *
     * @param catalogNumber The catalog number to set.
     */
    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    /**
     * Returns the ID of the specific item.
     *
     * @return The ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the ID of the specific item.
     *
     * @param id The ID to set.
     */
    public void setID(int id) {
        this.ID = id;
    }

    /**
     * Returns the cost price of the specific item.
     *
     * @return The cost price.
     */
    public double getCostPrice() {
        return costPrice;
    }

    /**
     * Sets the cost price of the specific item.
     *
     * @param costPrice The cost price to set.
     */
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    /**
     * Returns the selling price of the specific item.
     *
     * @return The selling price.
     */
    public double getSellingPrice(){return sellingPrice;}

    /**
     * Sets the selling price of the specific item.
     *
     * @param sellingPrice The selling price to set.
     */
    public void setSellingPrice(double sellingPrice){this.sellingPrice = sellingPrice;}

    /**
     * Returns the expiration date of the specific item.
     *
     * @return The expiration date.
     */
    public LocalDate getExpiration() {
        return expiration;
    }

    /**
     * Sets the expiration date of the specific item.
     *
     * @param expiration The expiration date to set.
     */
    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    /**
     * Returns whether the specific item is defective or not.
     *
     * @return {@code true} if the specific item is defective, {@code false} otherwise.
     */
    public boolean isDefective() {
        return defective;
    }
    /**
     * Sets whether the specific item is defective or not.
     *
     * @param defective The defective flag to set.
     */
    public void setDefective(boolean defective) {
        this.defective = defective;
    }

    /**
     * Returns the location of the specific item.
     *
     * @return The location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the specific item.
     *
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Returns the defect type of the specific item.
     *
     * @return The defect type.
     */
    public String getDefectType() {
        return defectType;
    }

    /**
     * Sets the defect type of the specific item.
     *
     * @param defectType The defect type to set.
     */
    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    /**
     * Returns the delivery time for an order.
     *
     * @return The delivery time as a LocalDate object.
     */
    public LocalDate getDeliverTime(){return DeliverTime;}

    /**
     * Sets the delivery time for an order.
     *
     * @param date The delivery time to be set, as a LocalDate object.
     */
    public void setDeliverTime(LocalDate date){DeliverTime=date;}
}