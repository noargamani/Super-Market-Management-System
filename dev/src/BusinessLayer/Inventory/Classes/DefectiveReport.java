package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public class DefectiveReport extends Report{

    private List<SpecificItem> SpecificItems;

    /**
     * Creates a new defective report instance with the given report date and lists specific items.
     * @param reportDate: The date of the report.
     * @param specificItems: The list of specific items included in the report.
     */
    public DefectiveReport(int reportID, LocalDate reportDate, List<SpecificItem> specificItems){
        super(reportID, reportDate);
        SpecificItems = specificItems;
    }

    /**
     * Update the list of specific items in the report.
     * @param specificItems: A list of SpecificItem objects to be included in the report.
     */
    public void setSpecificItems(List<SpecificItem> specificItems) {
        SpecificItems = specificItems;
    }

    /**
     * Returns a List of specific items associated with this Report.
     * @return A List of specific items associated with this Report.
     */
    public List<SpecificItem> getSpecificItems() {
        return SpecificItems;
    }

    @Override
    public String getType() {
        return "DefectiveReport";
    }
}