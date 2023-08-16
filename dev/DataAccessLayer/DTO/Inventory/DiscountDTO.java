package DataAcessLayer.DTO.Inventory;

import java.time.LocalDate;

public class DiscountDTO {
    private int saleNumber;
    private String saleName;
    private double discount;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs a DiscountDTO object with the specified sale number, sale name, discount, start date, and end date.
     *
     * @param saleNumber The number of the sale.
     * @param saleName   The name of the sale.
     * @param discount   The discount value.
     * @param startDate  The start date of the sale.
     * @param endDate    The end date of the sale.
     */
    public DiscountDTO(int saleNumber, String saleName, double discount, LocalDate startDate, LocalDate endDate) {
        this.saleNumber = saleNumber;
        this.saleName = saleName;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the number of the sale.
     *
     * @return The sale number.
     */
    public int getSaleNumber() {
        return saleNumber;
    }

    /**
     * Sets the number of the sale.
     *
     * @param saleNumber The sale number to set.
     */
    public void setSaleNumber(int saleNumber) {
        this.saleNumber = saleNumber;
    }

    /**
     * Returns the name of the sale.
     *
     * @return The sale name.
     */
    public String getSaleName() {
        return saleName;
    }

    /**
     * Sets the name of the sale.
     *
     * @param saleName The sale name to set.
     */
    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    /**
     * Returns the discount value.
     *
     * @return The discount value.
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets the discount value.
     *
     * @param discount The discount value to set.
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * Returns the start date of the sale.
     *
     * @return The start date of the sale.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the sale.
     *
     * @param startDate The start date of the sale to set.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the sale.
     *
     * @return The end date of the sale.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the sale.
     *
     * @param endDate The end date of the sale to set.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
