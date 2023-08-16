package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;

public class SpecificItem {
    private int ID;
    private double CostPrice;
    private double SellingPrice;
    private final LocalDate Expiration;
    private Boolean Defective;
    private String Location;
    private String DefectType;
    private LocalDate DeliverTime;

    /**
     *This class represents a specific item in a store inventory.
     * @param ID The unique identifier for this item.
     * @param costPrice The price that the store paid for this item.
     * @param sellingPrice The price that the store is selling this item for.
     * @param expiration The date when this item will expire.
     * @param defective Whether this item has a defect or not.
     * @param defectType If Defective is True, this property describes the type of defect.
     * @param location The location in the store where this item is stocked.
     */
    public SpecificItem(int ID, double costPrice, double sellingPrice, LocalDate expiration, Boolean defective, String defectType, String location) {
        this.ID = ID;
        if(costPrice < 0)
            throw new IllegalArgumentException("Cost price cannot be negative");
        CostPrice = costPrice;
        if(sellingPrice < 0)
            throw new IllegalArgumentException("Selling price cannot be negative");
        SellingPrice = sellingPrice;
        Expiration = expiration;
        Defective = defective;
        Location = location;
        DefectType = defectType;
        DeliverTime = LocalDate.now();
    }

    /**
     * Constructs a new SpecificItem object with the given parameters.
     *
     * @param ID           the ID of the item
     * @param costPrice    the cost price of the item
     * @param expiration   the expiration date of the item
     * @param sellingPrice the selling price of the item
     * @throws IllegalArgumentException if the cost price is negative
     */
    public SpecificItem(int ID, double costPrice, LocalDate expiration, double sellingPrice) {
        this.ID = ID;
        if(costPrice < 0)
            throw new IllegalArgumentException("Cost price cannot be negative");
        CostPrice = costPrice;
        SellingPrice = sellingPrice;
        Expiration = expiration;
        Defective = false;
        Location = "Warehouse";
        DefectType = "None";
        DeliverTime = LocalDate.now();
    }

    /**
     * Sets the ID attribute of this SpecificItem instance.
     * @param ID The new ID value to assign to this item.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Sets the CostPrice attribute of this SpecificItem instance.
     * @param costPrice The new cost price to assign to this item.
     */
    public void setCostPrice(double costPrice) {
        CostPrice = costPrice;
    }

    /**
     * Sets the SellingPrice attribute of this SpecificItem instance.
     * @param sellingPrice The new selling price to assign to this item.
     */
    public void setSellingPrice(double sellingPrice) {
        SellingPrice = sellingPrice;
    }

    /**
     * Sets the Defective attribute of this SpecificItem instance.
     * @param defective true if the item is defective and false if not.
     */
    public void setDefective(Boolean defective) {
        Defective = defective;
    }

    /**
     * Sets the Location attribute of this SpecificItem instance.
     * @param location The new location to assign to this item.
     */
    public void setLocation(String location) {
        Location = location;
    }

    /**
     * Returns the ID attribute of this SpecificItem instance.
     * @return The ID of this item.
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the CostPrice attribute of this SpecificItem instance.
     * @return The cost price of this item.
     */
    public double getCostPrice() {
        return CostPrice;
    }

    /**
     * Returns the SellingPrice attribute of this SpecificItem instance.
     * @return The selling price of this item.
     */
    public double getSellingPrice() {
        return SellingPrice;
    }

    /**
     * Returns the Expiration attribute of this SpecificItem instance.
     * @return The expiration date of this item.
     */
    public LocalDate getExpiration() {
        return Expiration;
    }

    /**
     * Returns the Defective attribute of this SpecificItem instance.
     * @return True if the item is defective, False otherwise.
     */
    public Boolean getDefective() {
        return Defective;
    }

    /**
     * Returns the Location attribute of this SpecificItem instance.
     * @return returns the location of the specific item.
     */
    public String getLocation() {
        return Location;
    }

    /**
     * Returns the defect type of the item.
     * @return the defect type of the item.
     */
    public String getDefectType() {
        return DefectType;
    }

    /**
     * Sets the defect type of the item.
     * @param defectType the defect type to set
     */
    public void setDefectType(String defectType) {
        DefectType = defectType;
    }

    /**
     * Returns the delivery time of the item.
     * @return the delivery time of the item.
     */
    public LocalDate getDeliverTime(){return DeliverTime;}

    /**
     * Sets the delivery time of the item.
     * @param date the delivery time of the item.
     */
    public void setDeliverTime(LocalDate date){DeliverTime=date;}

    /**
     * Checks if the expiration date of the object is before or equal to the current date.
     * @return true if the object is expired, false otherwise.
     */
    public boolean isExpired(){
        return !Expiration.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return "SpecificItem{" +
                "ID=" + ID +
                ", CostPrice=" + CostPrice +
                ", SellingPrice=" + SellingPrice +
                ", Expiration=" + Expiration +
                ", Defective=" + Defective +
                ", Location='" + Location + '\'' +
                ", DefectType='" + DefectType + '\'' +
                ", DeliverTime=" + DeliverTime +
                '}';
    }
}