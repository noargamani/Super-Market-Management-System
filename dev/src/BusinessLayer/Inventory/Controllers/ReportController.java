package BusinessLayer.Inventory.Controllers;

import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DAO.Inventory.ReportDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ReportController {
    private ReportDAO ReportMapper;

    /**
     * Constructs a new ReportController with the specified ReportDAO.
     * Initializes the ReportMapper and removes all existing reports from the database.
     *
     * @param reportDAO The ReportDAO to use for report-related operations.
     */
    public ReportController(ReportDAO reportDAO) {
        ReportMapper = reportDAO;
        try {
            ReportMapper.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns a report containing the information of all the items
     * @param ReportID the ID of the report
     * @param totalInventoryItemInventory the list of all items to be included in the report
     * @return the report containing the information of all items
     */
    public Report TotalItemReport(int ReportID, List<InventoryItem> totalInventoryItemInventory){
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            // StringBuilder to accumulate the information of all items
            StringBuilder info = new StringBuilder();
            for (InventoryItem inventoryItemInventory : totalInventoryItemInventory) {
                // Append the information of each item to the StringBuilder
                info.append(inventoryItemInventory.printInfo());
                info.append("\n");

            }
            // Create the report object with today's date and information of all items
            report = new InventoryReport(ReportID, LocalDate.now(), totalInventoryItemInventory);
            ReportMapper.addReport(report);
            // Set the information of all items in the report
            report.setInfo(info.toString());
        }

        // Return the report
        return report;
    }

    /**
     * Returns a report containing the information of all the missing items
     * @param ReportID the ID of the report
     * @param totalInventoryItemInventory the list of all items to be checked for missing items
     * @return the report containing the information of all missing items
     */
    public Report MissingItemReport(int ReportID, List<InventoryItem> totalInventoryItemInventory){
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            // Initialize the counter for missing items to zero
            int countMissing = 0;
            // StringBuilder to accumulate the information of all missing items
            StringBuilder info = new StringBuilder();
            // List to hold all the missing items
            List<InventoryItem> missingInventoryItemInventories = new LinkedList<>();

            for (InventoryItem inventoryItemInventory : totalInventoryItemInventory) {
                // Check if the item has total amount zero
                if (inventoryItemInventory.getTotalAmount() == 0) {
                    // Append the information of each missing item to the StringBuilder
                    info.append(inventoryItemInventory.printInfo());
                    info.append("\n");
                    // Add the missing item to the list of missing items
                    missingInventoryItemInventories.add(inventoryItemInventory);
                    // Increment the counter for missing items
                    countMissing ++;
                }
            }
            // If there are no missing items, print a message
            if(countMissing == 0)
                System.out.println("There are no missing items");

            // Create the report object with today's date and information of missing items
            report = new MissingItemsReport(ReportID, LocalDate.now(), missingInventoryItemInventories);
            ReportMapper.addReport(report);
            // Set the information of missing items in the report
            report.setInfo(info.toString());
        }

        // Return the report
        return report;
    }

    /**
     * Returns a report containing the information of all the items in the specified category
     * @param ReportID the ID of the report
     * @param category the category whose items are to be included in the report
     * @return the report containing the information of all items in the category
     */
    public Report PrintItemsInCategory(int ReportID, Category category){
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            // Get the list of items in the category
            List<InventoryItem> tempInventoryItemListInventory = category.getItems();
            // StringBuilder to accumulate the information of the category and its items
            StringBuilder info = new StringBuilder();
            // Append the information of the category to the StringBuilder
            info.append(category.printInfo());
            // Create the report object with today's date and information of the items in the category
            report = new ReportByCategory(ReportID, LocalDate.now(), tempInventoryItemListInventory);
            ReportMapper.addReport(report);
            // Set the information of the category and its items in the report
            report.setInfo(info.toString());
        }

        // Return the report
        return report;
    }

    /**
     * Returns a report containing the information of all the categories and their items
     * @param ReportID the ID of the report
     * @param CategoryList the list of all categories whose items are to be included in the report
     * @return the report containing the information of all categories and their items
     */
    public Report CategoryReport(int ReportID, List<Category> CategoryList){
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            // List to hold all the items in all the categories
            List<InventoryItem> categoryInventoryItemInventories = new ArrayList<>();
            // StringBuilder to accumulate the information of all the categories and their items
            StringBuilder info = new StringBuilder();
            for (Category category : CategoryList) {
                // Append the information of each category to the StringBuilder
                info.append(category.printInfo());
                // Add all the items in the category to the list of all items
                categoryInventoryItemInventories.addAll(category.getItems());
            }
            // Create the report object with today's date and information of all items in all categories
            report = new ReportByCategory(ReportID, LocalDate.now(), categoryInventoryItemInventories);
            ReportMapper.addReport(report);
            // Set the information of all categories and their items in the report
            report.setInfo(info.toString());
        }

        // Return the report
        return report;
    }

    /**
     * Generates a defective report for a given list of items and categories
     * @param ReportID the ID of the report
     * @param totalInventoryItemInventory List of all items
     * @param allCategories List of all categories
     * @return Report object representing the defective items
     */
    public Report DefectiveReport(int ReportID, List<InventoryItem> totalInventoryItemInventory, List<Category> allCategories){
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            // Create a new list to hold all defective items
            List<SpecificItem> DefectiveItems = new LinkedList<>();
            // Create a string builder to build the report message
            StringBuilder info = new StringBuilder();
            info.append("Defective:").append("\n");
            String answer = "No";
            int flag1 = 0;
            // Iterate over each item in TotalItem
            for (InventoryItem inventoryItemInventory : totalInventoryItemInventory) {
                // Find the category of the current item
                Category defectiveCategory = findCategory(inventoryItemInventory,allCategories);
                String defectiveCategoryName= "";
                // If the category is null, set the name to "None"
                if(defectiveCategory == null)
                    defectiveCategoryName ="None";
                    // Otherwise, set the name to the category's name
                else{
                    defectiveCategoryName = defectiveCategory.getCategoryName();
                }
                // Get a list of all specific items for the current item
                List<SpecificItem> specificItems = inventoryItemInventory.getIncludeItems();
                // Iterate over each specific item
                for (SpecificItem specificItem : specificItems) {
                    // If the specific item is defective
                    if (specificItem.getDefective()) {
                        // If the specific item is also expired, set the answer to "Yes"
                        if(specificItem.isExpired())
                            answer = "Yes";
                        // Append the item's information to the report message
                        info.append("ID: ").append(specificItem.getID()).append(" Name: ").append(inventoryItemInventory.getName()).append(" Category: ").append(defectiveCategoryName).append(" Defect Type: ").append(specificItem.getDefectType()).append(" Is Expired: ").append(answer).append(" Expiration: ").append(specificItem.getExpiration()).append("\n");
                        // Add the specific item to the list of defective items
                        DefectiveItems.add(specificItem);
                        flag1++;
                    }
                }
            }
            // If there were no defective items, add a message to the report message
            if(flag1==0)
                info.append("There is no expired items").append("\n");
            // Add a header for expired items to the report message
            info.append("Expired:").append("\n");
            int flag2 = 0;
            // Iterate over each item in TotalItem
            for (InventoryItem inventoryItemInventory : totalInventoryItemInventory) {
                // Find the category of the current item
                Category expiredCategory = findCategory(inventoryItemInventory,allCategories);
                String expiredCategoryName= "";
                // If the category is null, set the name to "None"
                if(expiredCategory == null)
                    expiredCategoryName ="None";
                    // Otherwise, set the name to the category's name
                else{
                    expiredCategoryName = expiredCategory.getCategoryName();
                }

                // Get a list of all specific items for the current item
                List<SpecificItem> specificItems = inventoryItemInventory.getIncludeItems();
                // Iterate over each specific item
                for (SpecificItem specificItem : specificItems) {
                    // If the specific item is expired
                    if (specificItem.isExpired()) {
                        // Append the item's information to the report message
                        info.append("ID: ").append(specificItem.getID()).append(" Name: ").append(inventoryItemInventory.getName()).append(" Category: ").append(expiredCategoryName).append(" Expiration: ").append(specificItem.getExpiration()).append("\n");
                        if (!DefectiveItems.contains(specificItem)){
                            DefectiveItems.add(specificItem);
                        }
                        flag2++;
                    }
                }
            }
            // If there were no expired items, add a message to the report message
            if(flag2==0)
                info.append("There is no expired items").append("\n");
            // Create the report object with today's date and information of all defective and expired items
            report = new DefectiveReport(ReportID, LocalDate.now(), DefectiveItems);
            ReportMapper.addReport(report);
            // Set the information of all defective and expired items
            report.setInfo(info.toString());
            System.out.println(info);
        }
        // Return the report
        return report;
    }

    /**
     * Given an Item and a List of Categories, this method finds the Category to which the Item belongs
     *
     * @param inventoryItemInventory The Item to be searched for in the Categories
     * @param categories The List of Categories to be searched
     * @return The Category to which the Item belongs or null if it doesn't belong to any Category in the List
     */
    public Category findCategory(InventoryItem inventoryItemInventory, List<Category> categories){
        // Loop through each Category in the List
        for(Category category: categories){
            // If the Category contains the Item
            if(category.getItems().contains(inventoryItemInventory)){
                // If the Category has no father Category
                if(category.getFatherCategory()==null){
                    // Return the Category
                    return category;
                }
            }
        }
        // If the Item doesn't belong to any Category in the List, return null
        return null;
    }

    /**
     * Generates a report of all discounts currently available in the store.
     * @param ReportID the ID of the report
     * @param allDiscounts A list of all discounts to include in the report.
     * @return A Report object containing information on all discounts.
     */
    public Report AllDiscountReport(int ReportID, List<Discount> allDiscounts)
    {
        Report report = null;
        if(ReportMapper.getReportByID(ReportID) == null){
            StringBuilder info = new StringBuilder();
            // Iterate over each discount in the list and append its information to the StringBuilder
            for (Discount discount: allDiscounts){
                info.append(discount.printInfo()).append("\n");
            }

            // Create a new report with the current date and the list of all discounts
            report = new DiscountReport(ReportID, LocalDate.now(), allDiscounts);
            ReportMapper.addReport(report);
            // Set the information of the report to the content of the StringBuilder
            report.setInfo(info.toString());
        }

        // Return the report
        return report;
    }

}