package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public class Discount {

    private int SaleNumber;
    private String SaleName;
    private double Discount;
    private LocalDate StartDate;
    private LocalDate EndDate;
    private List<InventoryItem> IncludeInDiscount;

    /**
     * Creates a Discount object with the specified sale number, sale name, discount percentage, start date, end date, and a list of items included in the discount.
     * @param saleNumber: The unique sale number of the discount.
     * @param saleName: The name of the discount.
     * @param discount: The discount percentage.
     * @param startDate: The start date of the discount period.
     * @param endDate: The start date of the discount period.
     * @param includeInDiscount: The list of items included in the discount.
     */
    public Discount(int saleNumber, String saleName,double discount, LocalDate startDate, LocalDate endDate, List<InventoryItem> includeInDiscount) {
        SaleNumber = saleNumber;
        SaleName = saleName;
        Discount = discount;
        StartDate = startDate;
        EndDate = endDate;
        IncludeInDiscount = includeInDiscount;
    }

    /**
     * Returns the name of the discount sale.
     * @return the name of the discount sale
     */
    public String getSaleName(){ return SaleName;}

    /**
     * Returns the sale number of this discount.
     * @return the sale number of this discount
     */
    public int getSaleNumber() {
        return SaleNumber;
    }

    /**
     * Returns the discount value of this discount.
     * @return the discount value of this discount
     */
    public double getDiscount() {
        return Discount;
    }

    /**
     * Returns the start date of this discount.
     * @return The start date of this discount.
     */
    public LocalDate getStartDate() {
        return StartDate;
    }

    /**
     * Returns the end date of this discount.
     * @return The end date of this discount.
     */
    public LocalDate getEndDate() {
        return EndDate;
    }

    /**
     * Returns the list of items that are included in the discount.
     * @return The list of items that are included in the discount.
     */
    public List<InventoryItem> getIncludeInDiscount(){
        return IncludeInDiscount;
    }

    /**
     * Sets the sale name of the discount.
     * @param saleName: The new sale name of the discount.
     */
    public void setSaleName(String saleName){
        SaleName = saleName;
    }

    /**
     * Sets the sale number of the discount.
     * @param saleNumber: The new sale number of the discount.
     */
    public void setSaleNumber(int saleNumber) {
        SaleNumber = saleNumber;
    }

    /**
     * Sets the discount percentage for the sale.
     * @param discount: The new discount percentage.
     */
    public void setDiscount(double discount) {
        Discount = discount;
    }

    /**
     * Sets the start date of the sale.
     * @param startDate: The new start date.
     */
    public void setStartDate(LocalDate startDate) {
        StartDate = startDate;
    }

    /**
     * Sets the end date of the sale.
     * @param endDate: The new end date.
     */
    public void setEndDate(LocalDate endDate) {
        EndDate = endDate;
    }

    /**
     * Prints the information about the sale.
     */
    public String printInfo(){
        String info = "Sale Name: " + SaleName + " Discount: " + Discount +
                " Start Date: " + StartDate + " End Date: " + EndDate;
        System.out.println(info);
        return info;
    }
}
