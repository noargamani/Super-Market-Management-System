package BusinessLayer.Inventory.Classes;
import java.time.LocalDate;
import java.util.List;

public class DiscountReport extends Report{

    private List<Discount> discounts;

    /**
     * Creates a new discount report instance with the given report date and discounts.
     * @param reportDate: The date of the report.
     * @param discounts: The list of sales included in the report.
     */
    public DiscountReport (int reportID,LocalDate reportDate, List<Discount> discounts){
        super(reportID, reportDate);
        this.discounts = discounts;
    }

    /**
     * Sets the list of discounts in the report object to the given list.
     * @param discounts: A list of Discount objects to be set in the report.
     */
    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    /**
     * Returns the list of discounts included in the report.
     * @return A List of Discount objects included in the report.
     */
    public List<Discount> getDiscounts() {
        return discounts;
    }

    @Override
    public String getType() {
        return "DiscountReport";
    }
}