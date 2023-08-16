package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public class MissingItemsReport extends Report{

    private List<InventoryItem> MissingItems;

    /**
     * Creates a new report instance with the given report date and lists of items.
     * @param reportDate: The date of the report.
     * @param missingItems: The list of general items included in the report.
     */
    public MissingItemsReport(int reportID, LocalDate reportDate, List<InventoryItem> missingItems){
        super(reportID, reportDate);
        MissingItems = missingItems;
    }

    /**
     * Returns a list of items that covered by the report.
     * @return A list of items that covered by the report.
     */
    public List<InventoryItem> getItems() {
        return MissingItems;
    }

    /**
     * Update the list of items in the report.
     * @param missingItems: A list of Item objects to be included in the report.
     */
    public void setItems(List<InventoryItem> missingItems){
        MissingItems = missingItems;
    }

    /**
     * Returns the type of the missing items report.
     * @return the type of the missing items report.
     */
    public String getType() {
        return "MissingItemsReport";
    }
}
