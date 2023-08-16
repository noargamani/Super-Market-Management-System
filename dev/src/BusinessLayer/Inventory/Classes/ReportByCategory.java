package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public class ReportByCategory extends Report{

    private List<InventoryItem> ItemsInCategory;

    /**
     * Creates a new report by category instance with the given report date and lists of items.
     * @param reportDate: The date of the report.
     * @param itemsInCategory: The list of general items included in the report.
     */
    public ReportByCategory(int reportID, LocalDate reportDate, List<InventoryItem> itemsInCategory){
        super(reportID, reportDate);
        ItemsInCategory = itemsInCategory;
    }

    /**
     * Returns a list of items that covered by the report.
     * @return A list of items that covered by the report.
     */
    public List<InventoryItem> getItems() {
        return ItemsInCategory;
    }

    /**
     * Update the list of items in the report.
     * @param itemsInCategory: A list of Item objects to be included in the report.
     */
    public void setItems(List<InventoryItem> itemsInCategory){
        ItemsInCategory = itemsInCategory;
    }

    /**
     * Returns the type of the report categorized by category.
     * @return the type of the report categorized by category.
     */
    public String getType() {
        return "ReportByCategory";
    }
}
