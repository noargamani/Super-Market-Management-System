package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public class InventoryReport extends Report{

    private List<InventoryItem> inventoryItemInventories;

    /**
     * Creates a new inventory report instance with the given report date and lists of items.
     * @param reportDate: The date of the report.
     * @param inventoryItemInventories: The list of general items included in the report.
     */
    public InventoryReport(int reportID, LocalDate reportDate, List<InventoryItem> inventoryItemInventories){
        super(reportID, reportDate);
        this.inventoryItemInventories = inventoryItemInventories;
    }

    /**
     * Returns a list of items that covered by the report.
     * @return A list of items that covered by the report.
     */
    public List<InventoryItem> getItems() {
        return inventoryItemInventories;
    }

    /**
     * Update the list of items in the report.
     * @param inventoryItemInventories: A list of Item objects to be included in the report.
     */
    public void setItems(List<InventoryItem> inventoryItemInventories){
        this.inventoryItemInventories = inventoryItemInventories;
    }

    /**
     * Returns the type of the inventory report.
     * @return the type of the inventory report.
     */
    public String getType() {
        return "InventoryReport";
    }

}