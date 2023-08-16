package BusinessLayer.Inventory.Classes;

import java.time.LocalDate;
import java.util.List;

public abstract class Report {
        private int ReportID;
        private LocalDate ReportDate;
        private String Info;

        /**
         * Creates a new report instance with the given report date, lists of items, specific items,
         * sales, and discounts. The report ID is incremented by 1 for each new report instance.
         * @param reportDate: The date of the report.
         */
        public Report(int reportID, LocalDate reportDate){
                ReportID = reportID;
                ReportDate = reportDate;
                Info = "";
        }

        /**
         * Returns the ID of the report.
         * @return The ID of the report.
         */
        public int getReportID() {
                return ReportID;
        }

        /**
         * Returns the date on which the report was generated.
         * @return the date on which the report was generated.
         */
        public LocalDate getReportDate() {
                return ReportDate;
        }

        /**
         * Returns the additional information about the report.
         * @return The additional information about the report.
         */
        public String getInfo(){
                return Info;
        }

        /**
         * Sets the ID of the report.
         * @param reportID: The new ID of the report.
         */
        public void setReportID(int reportID) {
                ReportID = reportID;
        }

        /**
         * Sets the report date of this Report object.
         * @param reportDate: The new report date to set.
         */
        public void setReportDate(LocalDate reportDate) {
                ReportDate = reportDate;
        }


        /**
         * Sets the info attribute of the Report object to the specified string.
         * @param newInfo: The new string to set as the Report object's info attribute.
         */
        public void setInfo(String newInfo){
                Info = newInfo;
        }

        /**
         * Returns the type of the object.
         * @return the type of the object.
         */
        public abstract String getType();

}