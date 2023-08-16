package Service_layer;

import BusinessLayer.Inventory.Classes.*;
import BusinessLayer.Inventory.Controllers.ItemController;
import BusinessLayer.Inventory.Controllers.ReportController;
import BusinessLayer.Item;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class InventoryService {

    private final ItemController IC;
    private final ReportController RC;

    /**
     * Constructs an InventoryService with the specified ItemController and ReportController.
     *
     * @param itemController  The ItemController instance.
     * @param reportController The ReportController instance.
     */
    public InventoryService(ItemController itemController, ReportController reportController) {
        IC = itemController;
        RC = reportController;
    }

    /**
     * Deletes an item with the specified catalog number.
     *
     * @param catalogNumToDelete The catalog number of the item to delete.
     * @return True if the item is successfully deleted, false otherwise.
     */
    public boolean DeleteItem(int catalogNumToDelete) {
        try {
            IC.DeleteItem(catalogNumToDelete);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Updates the defective status and defect type of a specific item.
     *
     * @param myCatalogNum The catalog number of the item.
     * @param itemID       The ID of the specific item.
     * @param isDefective  True if the item is defective, false otherwise.
     * @param defectType   The defect type of the item.
     * @throws NullPointerException if the item doesn't exist.
     */
    public void UpdateDefectiveStatus(int myCatalogNum, int itemID, boolean isDefective, String defectType){
        InventoryItem inventoryItem = IC.FindMyItem(myCatalogNum);
        if (inventoryItem == null)
            throw new NullPointerException("This item doesn't exists");

        SpecificItem specItem = inventoryItem.FindSpecificItem(itemID);

        if (isDefective) {
            specItem.setDefective(true);
            specItem.setDefectType(defectType);
        } else if (!isDefective) {
            specItem.setDefective(false);
            specItem.setDefectType(defectType);
        }
        IC.UpdateSpecificItem(myCatalogNum, specItem);
    }

    /**
     * Adds a sub-category to a category.
     *
     * @param categoryID       The ID of the sub-category.
     * @param categoryName     The name of the sub-category.
     * @param fatherCategoryID The ID of the parent category.
     */
    public void addSubCategory(int categoryID, String categoryName, int fatherCategoryID){
        IC.addSubCategory(categoryID, categoryName, fatherCategoryID);
    }

    /**
     * Adds a father category.
     *
     * @param categoryID   The ID of the father category.
     * @param categoryName The name of the father category.
     */
    public void addFatherCategory(int categoryID, String categoryName){
        IC.addFatherCategory(categoryID, categoryName);
    }

    /**
     * Updates a category.
     *
     * @param categoryID       The ID of the category to update.
     * @param newCategoryName  The new name for the category.
     */
    public void updateCategory(int categoryID, String newCategoryName){
        IC.updateCategory(categoryID, newCategoryName);
    }

    /**
     * Finds a category by its ID.
     *
     * @param categoryID The ID of the category to find.
     * @throws NullPointerException if the category doesn't exist.
     */
    public void FindCategoryByID(int categoryID){
        Category category = IC.FindCategoryByID(categoryID);
        if (category == null)
            throw new NullPointerException("This category doesn't exists");
    }

    /**
     * Adds an item to a category.
     *
     * @param categoryID    The ID of the category.
     * @param catalogNumber The catalog number of the item.
     * @return true if the item was added successfully, false otherwise.
     */
    public boolean AddItemToCategory(int categoryID, int catalogNumber){
        Category category = IC.FindCategoryByID(categoryID);
        InventoryItem inventoryItem = IC.FindMyItem(catalogNumber);
        try{
            category.AddItemToCategory(inventoryItem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        List<InventoryItem> items = new LinkedList<>();
        items.add(inventoryItem);
        IC.addItemToCategory(items, categoryID);
        return true;
    }

    /**
     * Removes an item from a category.
     *
     * @param categoryID    The ID of the category.
     * @param catalogNumber The catalog number of the item.
     * @return true if the item was removed successfully, false otherwise.
     */
    public boolean RemoveItemFromCategory(int categoryID, int catalogNumber){
        Category category = IC.FindCategoryByID(categoryID);
        InventoryItem inventoryItem = IC.FindMyItem(catalogNumber);
        try{
            category.RemoveItemFromCategory(inventoryItem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        IC.removeItemFromCategory(catalogNumber, categoryID);
        return true;
    }

    /**
     * Adds an item discount.
     *
     * @param saleNumber     The sale number.
     * @param saleName       The name of the sale.
     * @param discount       The discount value.
     * @param startDate      The start date of the discount.
     * @param endDate        The end date of the discount.
     * @param catalogNumbers The catalog numbers of the items on discount.
     */
    public void addItemDiscount(int saleNumber, String saleName, double discount, LocalDate startDate, LocalDate endDate, List<Integer> catalogNumbers){
        IC.addItemDiscount(saleNumber, saleName, discount, startDate, endDate, catalogNumbers);
    }

    /**
     * Adds a category discount.
     *
     * @param saleNumber   The sale number.
     * @param saleName     The name of the sale.
     * @param discount     The discount value.
     * @param startDate    The start date of the discount.
     * @param endDate      The end date of the discount.
     * @param categoriesID The IDs of the categories on discount.
     */
    public void addCategoryDiscount(int saleNumber,String saleName, double discount, LocalDate startDate, LocalDate endDate, List<Integer> categoriesID){
        IC.addCategoryDiscount(saleNumber, saleName, discount, startDate, endDate, categoriesID);
    }

    /**
     * Checks if an item is on discount.
     *
     * @param catalogNumber The catalog number of the item.
     */
    public String CheckIfItemOnDiscount(int catalogNumber){
        Discount discount = IC.CheckIfItemOnDiscount(catalogNumber);
        String ans="";
        if (discount == null) {
            ans = "This item is not on discount";
            System.out.println(ans);
            return ans;
        }
        else {
            ans = "This item have " + discount.getDiscount() + "% discount";
            System.out.println(ans);
            return ans;
        }
    }

    /**
     * Generates a total item report.
     *
     * @param ReportID The ID of the report.
     * @return Total item Report.
     */
    public Report TotalItemReport(int ReportID){
        return RC.TotalItemReport(ReportID, IC.getTotalItem());
    }

    /**
     * Generates a missing item report.
     *
     * @param reportID The ID of the report.
     * @return Missing item Report.
     */
    public Report MissingItemReport(int reportID){
        return RC.MissingItemReport(reportID, IC.getTotalItem());
    }

    /**
     * Generates a defective item report.
     *
     * @param ReportID The ID of the report.
     * @return Defective Report.
     */
    public Report DefectiveReport(int ReportID){
        return RC.DefectiveReport(ReportID, IC.getTotalItem(), IC.getCategoryList());
    }

    /**
     * Removes expired discounts.
     */
    public void removeExpiredDiscounts() {
        IC.removeDiscount();
    }

    /**
     * Adds a new item.
     *
     * @param itemName       The name of the item.
     * @param manufacturer   The manufacturer of the item.
     * @param minimumAmount  The minimum amount of the item.
     * @param price          The price of the item.
     */
    public void AddNewItem(String itemName, String manufacturer, int minimumAmount, double price) throws IllegalArgumentException {
        IC.AddNewItem(itemName, manufacturer, minimumAmount, price);
    }

    /**
     * Finds an item by catalog number and returns its information.
     *
     * @param catalogNumToFind The catalog number to search for.
     * @return The information of the found item.
     * @throws NullPointerException if the item doesn't exist.
     */
    public String FindMyItem(int catalogNumToFind) {
        InventoryItem inventoryItem = IC.FindMyItem(catalogNumToFind);
        if(inventoryItem == null)
            throw new NullPointerException("This item doesn't exists");
        return inventoryItem.printInfo();
    }

    /**
     * Finds a specific item by catalog number and item ID.
     *
     * @param myCatalogNum The catalog number of the item to search in.
     * @param itemID       The ID of the specific item to search for.
     * @throws NullPointerException      if the item doesn't exist.
     * @throws IllegalArgumentException  if the specific item already exists.
     */
    public void FindSpecificItem(int myCatalogNum, int itemID) {
        InventoryItem inventoryItem = IC.FindMyItem(myCatalogNum);
        if (inventoryItem == null)
            throw new NullPointerException("This item doesn't exists");
        SpecificItem specificItem= inventoryItem.FindSpecificItem(itemID);
        if(specificItem != null)
            throw new IllegalArgumentException("This specific item already exists");
    }

    /**
     * Adds a specific item to an inventory item.
     *
     * @param myCatalogNum The catalog number of the inventory item.
     * @param itemID       The ID of the specific item.
     * @param costPrice    The cost price of the specific item.
     * @param sellingPrice The selling price of the specific item.
     * @param expirationDate The expiration date of the specific item.
     * @param defective    Whether the specific item is defective or not.
     * @param defectType   The defect type of the specific item.
     * @param location     The location of the specific item.
     * @return true if the specific item is added successfully, false otherwise.
     */
    public boolean AddSpecificItem(int myCatalogNum, int itemID, double costPrice, double sellingPrice, LocalDate expirationDate, boolean defective, String defectType, String location) {
        try {
            InventoryItem inventoryItem = IC.FindMyItem(myCatalogNum);
            if (sellingPrice == -1) {
                sellingPrice = inventoryItem.getPrice();
            }

            SpecificItem specificItem = new SpecificItem(itemID, costPrice,sellingPrice,expirationDate, defective, defectType, location);
            inventoryItem.AddSpecificItem(specificItem, IC.getSpecificItemDAO());
            IC.UpdateItem(myCatalogNum);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }

    /**
     * Generates an all discount report.
     *
     * @param ReportID The ID of the report.
     * @return The list of discounts in the report.
     */
    public List<Discount> AllDiscountReport(int ReportID){
        Report report = RC.AllDiscountReport(ReportID, IC.getDiscountList());
        return ((DiscountReport) report).getDiscounts();
    }

    /**
     * Generates an all discount report.
     *
     * @param ReportID The ID of the report.
     * @return All Discounts Report.
     */
    public Report AllDiscountReportGUI(int ReportID){
        return RC.AllDiscountReport(ReportID, IC.getDiscountList());
    }

    /**
     * Checks if a category exists based on its ID.
     *
     * @param categoryID The ID of the category to check.
     * @throws NullPointerException if the category doesn't exist.
     */
    public void checkCategoryByID(int categoryID){
        Category category = IC.FindCategoryByID(categoryID);
        if(category==null)
            throw new NullPointerException(categoryID +" doesn't exist");
    }

    /**
     * Generates a category report.
     *
     * @param ReportID         The ID of the report.
     * @param allCategoriesIDs The IDs of all categories to include in the report.
     */
    public Report CategoryReport(int ReportID, List<Integer> allCategoriesIDs){
        List<Category> categories = new LinkedList<>();
        for(int i=0; i<allCategoriesIDs.size(); i++) {
            Category category = IC.FindCategoryByID(allCategoriesIDs.get(i));
            categories.add(category);
        }
        return RC.CategoryReport(ReportID, categories);
    }

    /**
     * Checks the quantity of items and throws an exception if an order is needed.
     *
     * @throws Item.NeedOrderException if an order is needed.
     */
    public void checkQuantity() throws Item.NeedOrderException {
        try {
            IC.checkQuantity();
        } catch (Item.NeedOrderException e){
            throw new Item.NeedOrderException(e);
        }
    }

    /**
     * Deletes a specific item.
     *
     * @param ID             The ID of the specific item.
     * @param catalogNumber  The catalog number of the inventory item containing the specific item.
     * @return true if the specific item is deleted successfully, false otherwise.
     * @throws Item.NeedOrderException if an order is needed.
     */
    public boolean DeleteSpecificItem(int ID, int catalogNumber) throws Item.NeedOrderException {
        try {
            IC.DeleteSpecificItem(ID, catalogNumber);
        } catch (Item.NeedOrderException e){
            throw new Item.NeedOrderException(e);
        }
        return true;
    }

    public ItemController getIC() {
        return IC;
    }

    public void getAllItems(){
        IC.getAllItems();
    }

    public void getAllSpecificItems(){
        IC.getAllSpecificItems();
    }

    public void getAllDiscounts(){
        IC.getAllDiscounts();
    }

    public void getAllCategories(){
        IC.getAllCategories();
    }
}