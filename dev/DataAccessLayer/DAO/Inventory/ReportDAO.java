package DataAcessLayer.DAO.Inventory;

import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DTO.Inventory.ReportDTO;
import DataAcessLayer.Repo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReportDAO {
    private Connection conn;
    private  List<Report> identityMap;

    /**
     * Constructs a new ReportDAO object.
     *
     * This constructor initializes the ReportDAO object by establishing a connection to the database using the
     * `connect` method from the `Repo` class. If an exception occurs during the connection process, the exception
     * stack trace is printed. An empty ArrayList is also created to serve as the identity map for the report objects.
     */
    public ReportDAO() {
        try {
            this.conn = Repo.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.identityMap = new ArrayList<>();
        this.identityMap = getAllReports();
    }

    /**
     * Builds a report object based on the provided ReportDTO.
     *
     * This method takes a ReportDTO object as input and extracts the necessary information such as reportID, reportDate,
     * and type. Depending on the type of the report, different types of reports (DefectiveReport, DiscountReport,
     * ReportByCategory, MissingItemsReport, or InventoryReport) are created and returned. The specificItems, discounts,
     * or items associated with the report are retrieved from the database using helper methods. The created report object
     * is added to the identity map before returning it.
     *
     * @param reportDTO the ReportDTO object containing the report information
     * @return the created report object based on the ReportDTO
     */
    public Report buildReport(ReportDTO reportDTO){
        int reportID = reportDTO.getReportID();
        LocalDate reportDate = reportDTO.getReportDate();
        String type = reportDTO.getType();
        if (Objects.equals(type, "DefectiveReport")){
            List<SpecificItem> specificItems = getSpecificItemsByReportID(reportID);
            Report report = new DefectiveReport(reportID, reportDate, specificItems);
            identityMap.add(report);
            return report;
        }
        else if(Objects.equals(type, "DiscountReport")){
            List<Discount> discounts = getDiscountsByReportID(reportID);
            Report report = new DiscountReport(reportID, reportDate, discounts);
            identityMap.add(report);
            return report;
        }
        else if (type.equals("ReportByCategory") || type.equals("MissingItemsReport") || type.equals("InventoryReport")){
            List<InventoryItem> items = getItemsByReportID(reportID);
            if(type.equals("ReportByCategory")){
                Report report = new ReportByCategory(reportID, reportDate, items);
                identityMap.add(report);
            }
            else if(type.equals("MissingItemsReport")){
                Report report = new MissingItemsReport(reportID, reportDate, items);
                identityMap.add(report);
                return report;
            }
            else if (type.equals("InventoryReport")) {
                Report report = new InventoryReport(reportID, reportDate, items);
                identityMap.add(report);
                return report;
            }
        }
        return null;
    }

    /**
     * Adds a report to the database and updates the associated items or specific items based on the report type.
     *
     * This method takes a Report object as input and inserts the report information (ReportID, ReportDate, Type) into
     * the "Report" table in the database. Depending on the type of the report, the associated items or specific items
     * are retrieved from the report object and added to the report using helper methods. The updated report object is
     * then added to the identity map.
     *
     * @param report the Report object to be added to the database
     */
    public void addReport(Report report) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Report (ReportID, ReportDate, Type) VALUES (?, ?, ?)");
            stmt.setInt(1, report.getReportID());
            stmt.setDate(2, java.sql.Date.valueOf(report.getReportDate()));
            stmt.setString(3, report.getType());
            stmt.executeUpdate();
            if(report.getType().equals("MissingItemsReport")){
                MissingItemsReport missingItemsReport = (MissingItemsReport) report;
                if(missingItemsReport.getItems().size() != 0){
                    addItemsToReport(missingItemsReport.getItems(), report.getReportID());
                }
            } else if (report.getType().equals("InventoryReport")) {
                InventoryReport inventoryReport = (InventoryReport) report;
                if(inventoryReport.getItems().size() != 0){
                    addItemsToReport(inventoryReport.getItems(), report.getReportID());
                }
            } else if (report.getType().equals("ReportByCategory")) {
                ReportByCategory reportByCategory = (ReportByCategory) report;
                if(reportByCategory.getItems().size() != 0){
                    addItemsToReport(reportByCategory.getItems(), report.getReportID());
                }
            } else if (report.getType().equals("DefectiveReport")) {
                DefectiveReport defectiveReport = (DefectiveReport) report;
                if(defectiveReport.getSpecificItems().size() != 0){
                    addSpecificItemsToReport(defectiveReport.getSpecificItems(), report.getReportID());
                }

            } else if (report.getType().equals("DiscountReport")){
                DiscountReport discountReport = (DiscountReport) report;
                if(discountReport.getDiscounts().size() != 0){
                    addDiscountsToReport(discountReport.getDiscounts(), report.getReportID());
                }
            }
            identityMap.add(report);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the provided list of inventory items to the specified report in the database.
     *
     * This method takes a list of InventoryItem objects and an integer reportID as input. It iterates over the items
     * in the list and inserts each item's CatalogNumber and the given reportID into the "ReportItem" table in the database.
     *
     * @param items    the list of inventory items to be added to the report
     * @param reportID the ID of the report to which the items will be added
     */
    public void addItemsToReport(List<InventoryItem> items, int reportID){
        PreparedStatement statement = null;
        try{
            for(int i=0; i<items.size(); i++) {
                InventoryItem item = items.get(i);
                String sql = "INSERT INTO ReportItem (reportID, CatalogNumber)\n" +
                        "VALUES (?, ?);";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, reportID);
                statement.setInt(2, item.getCatalogNumber());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds the provided list of specific items to the specified report in the database.
     *
     * This method takes a list of SpecificItem objects and an integer reportID as input. It iterates over the items
     * in the list and inserts each item's ID and the given reportID into the "ReportSpecificItem" table in the database.
     *
     * @param items    the list of specific items to be added to the report
     * @param reportID the ID of the report to which the items will be added
     */
    public void addSpecificItemsToReport(List<SpecificItem> items, int reportID){
        PreparedStatement statement = null;
        try{
            for(int i=0; i<items.size(); i++) {
                SpecificItem item = items.get(i);
                String sql = "INSERT INTO ReportSpecificItem (reportID, ID)\n" +
                        "VALUES (?, ?);";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, reportID);
                statement.setInt(2, item.getID());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds the provided list of discounts to the specified report in the database.
     *
     * This method takes a list of Discount objects and an integer reportID as input. It iterates over the discounts
     * in the list and inserts each discount's SaleNumber and the given reportID into the "ReportDiscount" table in the database.
     *
     * @param discounts the list of discounts to be added to the report
     * @param reportID  the ID of the report to which the discounts will be added
     */
    public void addDiscountsToReport(List<Discount> discounts, int reportID){
        PreparedStatement statement = null;
        try{
            for(int i=0; i<discounts.size(); i++) {
                Discount discount = discounts.get(i);
                String sql = "INSERT INTO ReportDiscount (reportID, SaleNumber)\n" +
                        "VALUES (?, ?);";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, reportID);
                statement.setInt(2, discount.getSaleNumber());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the specified report in the database with the new report date and type.
     *
     * This method takes a Report object as input and updates the corresponding record in the "Report" table in the database.
     * It retrieves the report's date and type, and then performs an update operation with the new values using the report's ID.
     *
     * @param report the report object to be updated in the database
     */
    public void updateReport(Report report) {
        try {
            String reportType = "";
            if(report instanceof DefectiveReport){
                reportType = "DefectiveReport";
            }
            else if(report instanceof MissingItemsReport){
                reportType = "MissingItemsReport";
            }
            else if(report instanceof DiscountReport){
                reportType = "DiscountReport";
            }
            else if(report instanceof InventoryReport){
                reportType = "InventoryReport";
            }
            else if(report instanceof ReportByCategory){
                reportType = "ReportByCategory";
            }
            PreparedStatement stmt = conn.prepareStatement("UPDATE Report SET ReportDate=?, Type=? WHERE ReportID=?");
            stmt.setDate(1, Date.valueOf(report.getReportDate()));
            stmt.setString(2, reportType);
            stmt.setInt(3, report.getReportID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a report from the database based on the specified report ID.
     *
     * This method searches for the report in the identity map first. If the report is found, it is returned directly.
     * If the report is not found in the identity map, a database query is executed to retrieve the report information
     * from the "Report" table. The report's date and type are extracted from the result set, and based on the type,
     * the corresponding report object is created with the associated data (specific items, discounts, or inventory items).
     *
     * @param reportID the ID of the report to retrieve
     * @return the retrieved report object, or null if the report is not found
     */
    public Report getReportByID(int reportID) {
        for (Report report : identityMap){
            if(report.getReportID() == reportID){
                return report;
            }
        }
        String sql = "SELECT * FROM Report WHERE ReportID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, reportID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDate reportDate = rs.getDate("ReportDate").toLocalDate();
                String type = rs.getString("Type");
                if(type.equals("DefectiveReport")) {
                    List<SpecificItem> specificItems = getSpecificItemsByReportID(reportID);
                    Report report = new DefectiveReport(reportID, reportDate, specificItems);
                    return report;
                }
                else if (type.equals("DiscountReport")){
                    List<Discount> discounts = getDiscountsByReportID(reportID);
                    Report report = new DiscountReport(reportID, reportDate, discounts);
                    return report;
                }
                else{
                    List<InventoryItem> items= getItemsByReportID(reportID);
                    if(type.equals("InventoryReport")){
                        Report report = new InventoryReport(reportID, reportDate, items);
                        return report;
                    }
                    else if(type.equals("MissingItemsReport")){
                        Report report = new MissingItemsReport(reportID, reportDate, items);
                        return report;
                    }
                    else if(type.equals("ReportByCategory")){
                        Report report = new ReportByCategory(reportID, reportDate, items);
                        return report;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of inventory items associated with the specified report ID.
     *
     * This method queries the "ReportItem" table in the database to fetch the catalog numbers of the items
     * associated with the given report ID. For each catalog number, it uses the InventoryItemDAO to retrieve
     * the corresponding inventory item object and adds it to the list. The populated list of inventory items
     * is returned.
     *
     * @param reportID the ID of the report for which to retrieve the associated inventory items
     * @return a list of inventory items associated with the report, or null if an error occurs
     */
    public List<InventoryItem> getItemsByReportID(int reportID){
        InventoryItemDAO ItemDAO = new InventoryItemDAO();
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM ReportItem WHERE ReportID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, reportID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int catalogNumber = rs.getInt("CatalogNumber");
                InventoryItem item = ItemDAO.getItemByCatalogNumber(catalogNumber);
                items.add(item);
            }
            return items;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of specific items associated with the specified report ID.
     *
     * This method queries the "ReportSpecificItem" table in the database to fetch the IDs of specific items
     * associated with the given report ID. It then uses the SpecificItemDAO to retrieve the specific item
     * objects based on the IDs. The retrieved items are added to a list and returned.
     *
     * @param reportID the ID of the report for which to retrieve the associated specific items
     * @return a list of specific items associated with the report ID, or null if an error occurs
     */
    public List<SpecificItem> getSpecificItemsByReportID(int reportID){
        SpecificItemDAO specificItemDAO = new SpecificItemDAO();
        List<SpecificItem> specificItems = new ArrayList<>();
        String sql = "SELECT * FROM ReportSpecificItem WHERE ReportID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, reportID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int ID = rs.getInt("ID");
                SpecificItem specificItem = specificItemDAO.getSpecificItemByID(ID);
                specificItems.add(specificItem);
            }
            return specificItems;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of discounts associated with the specified report ID.
     *
     * This method queries the "ReportDiscount" table in the database to fetch the sale numbers of discounts
     * associated with the given report ID. It then uses the DiscountDAO to retrieve the discount objects based
     * on the sale numbers. The retrieved discounts are added to a list and returned.
     *
     * @param reportID the ID of the report for which to retrieve the associated discounts
     * @return a list of discounts associated with the report ID, or null if an error occurs
     */
    public List<Discount> getDiscountsByReportID(int reportID){
        DiscountDAO discountDAO = new DiscountDAO();
        List<Discount> discounts = new ArrayList<>();
        String sql = "SELECT * FROM ReportDiscount WHERE ReportID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, reportID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int saleNumber = rs.getInt("SaleNumber");
                Discount discount = discountDAO.getDiscountBySaleNumber(saleNumber);
                discounts.add(discount);
            }
            return discounts;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a list of all reports stored in the system.
     *
     * This method queries the "Report" table in the database to fetch all report records. It iterates over
     * the result set and creates the corresponding report objects based on the report type. Depending on the
     * type, the method calls helper methods to retrieve the specific items, discounts, or inventory items
     * associated with each report. The reports are added to a list and returned.
     *
     * @return a list of all reports stored in the system, or an empty list if no reports exist or an error occurs
     */
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Report");
            while (rs.next()) {
                int reportID = rs.getInt("ReportID");
                LocalDate reportDate = rs.getDate("ReportDate").toLocalDate();
                String type = rs.getString("Type");
                Report report = null ;
                if(type.equals("DefectiveReport")){
                    report = new DefectiveReport(reportID, reportDate, getSpecificItemsByReportID(reportID));
                }
                else if (type == "DiscountReport"){
                    report = new DiscountReport(reportID, reportDate, getDiscountsByReportID(reportID));
                }
                else if (type == "InventoryReport") {
                    report = new InventoryReport(reportID, reportDate, getItemsByReportID(reportID));
                }
                else if (type == "MissingItemsReport") {
                    report = new MissingItemsReport(reportID, reportDate, getItemsByReportID(reportID));
                }
                else if (type == "ReportByCategory") {
                    report = new ReportByCategory(reportID, reportDate, getItemsByReportID(reportID));
                }
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    /**
     * Retrieves a list of all reports stored in the system.
     *
     * This method queries the "Report" table in the database to fetch all report records. It iterates over
     * the result set and creates the corresponding report objects based on the report type. Depending on the
     * type, the method calls helper methods to retrieve the specific items, discounts, or inventory items
     * associated with each report. The reports are added to a list and returned.
     *
     * @return a list of all reports stored in the system, or an empty list if no reports exist or an error occurs
     */
    public void removeAll() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM Report;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            identityMap.clear();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
