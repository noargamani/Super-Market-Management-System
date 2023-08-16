package DataAcessLayer.DTO.Inventory;

import java.time.LocalDate;

public class ReportDTO {
    private int reportID;
    private LocalDate reportDate;
    private String type;

    /**
     * Constructs a ReportDTO object with the specified report ID, report date, and type.
     *
     * @param reportID   The ID of the report.
     * @param reportDate The date of the report.
     * @param type       The type of the report.
     */
    public ReportDTO(int reportID, LocalDate reportDate, String type) {
        this.reportID = reportID;
        this.reportDate = reportDate;
        this.type = type;
    }

    /**
     * Returns the report ID.
     *
     * @return The report ID.
     */
    public int getReportID() {
        return reportID;
    }

    /**
     * Sets the report ID.
     *
     * @param reportID The report ID to set.
     */
    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    /**
     * Returns the report date.
     *
     * @return The report date.
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * Sets the report date.
     *
     * @param reportDate The report date to set.
     */
    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * Returns the type of the report.
     *
     * @return The report type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the report.
     *
     * @param type The report type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
}
