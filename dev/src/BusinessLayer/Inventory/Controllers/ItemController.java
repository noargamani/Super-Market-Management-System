package BusinessLayer.Inventory.Controllers;
import BusinessLayer.Inventory.Classes.*;
import BusinessLayer.Item;
import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.SupplierItem;
import DataAcessLayer.DAO.Inventory.CategoryDAO;
import DataAcessLayer.DAO.Inventory.DiscountDAO;
import DataAcessLayer.DAO.Inventory.InventoryItemDAO;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;
import DataAcessLayer.Repo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import java.util.Random;

public class ItemController {
    private static InventoryItemDAO InventoryItemMapper;
    private CategoryDAO CategoryMapper;
    private DiscountDAO DiscountMapper;
    private static SpecificItemDAO specificItemMapper;


    /**
     * Constructs a new ItemController object with the given data access objects.
     *
     * @param inventoryItemDAO the data access object for inventory items
     * @param categoryDAO      the data access object for categories
     * @param discountDAO      the data access object for discounts
     */
    public ItemController(InventoryItemDAO inventoryItemDAO, CategoryDAO categoryDAO, DiscountDAO discountDAO, SpecificItemDAO specificItemDAO) {
        try {
            Repo.createTables(Repo.connect());
            InventoryItemMapper = inventoryItemDAO;
            CategoryMapper = categoryDAO;
            DiscountMapper = discountDAO;
            specificItemMapper = specificItemDAO;
        } catch (SQLException e){
            e.printStackTrace();
        }
        //init();
    }

    /**
     * Initializes the inventory by adding specific items and updating the inventory items.
     */
    public void init() {
        LocalDate expiration1 = LocalDate.of(2023, 6, 16);
        LocalDate expiration2 = LocalDate.of(2023, 6, 20);
        // Milk Tnuva 3% X 20
        AddNewItem( "Milk Tnuva 3%", "Tnuva", 5, 7.90);
        int catalogNumber1 = Item.getItemCatalogNumber("Milk Tnuva 3%", "Tnuva");
        InventoryItem Milk = FindMyItem(catalogNumber1);
        SpecificItem MilkTnuva;

        // Organic Eggs size L X 20
        AddNewItem("Organic eggs size L", "FreeChicken", 5, 8.90);
        int catalogNumber2 = Item.getItemCatalogNumber("Organic eggs size L", "FreeChicken");
        InventoryItem Eggs = FindMyItem(catalogNumber2);
        SpecificItem OrganicEggs;

        // White bread x 20
        AddNewItem( "White bread", "Angel", 3, 7.10);
        int catalogNumber3 = Item.getItemCatalogNumber("White bread", "Angel");
        InventoryItem Bread = FindMyItem(catalogNumber3);
        SpecificItem WhiteBread;

        // Cucumber  x 20
        AddNewItem( "Cucumber", "Organic", 3, 3.90);
        int catalogNumber4 = Item.getItemCatalogNumber("Cucumber", "Organic");
        InventoryItem Cucumber = FindMyItem(catalogNumber4);
        SpecificItem cuc;

        // Tomato x 20
        AddNewItem("Tomato", "Organic", 3, 1.90);
        int catalogNumber5 = Item.getItemCatalogNumber("Tomato", "Organic");
        InventoryItem Tomato = FindMyItem(catalogNumber5);
        SpecificItem tom;

        for (int i = 0; i < 10; i++) {
            // Milk Tnuva 3% X 10
            MilkTnuva = new SpecificItem(110 + i, 8, 10, expiration1, false, "None", "Shelf");
            Milk.AddSpecificItem(MilkTnuva, specificItemMapper);

            // Organic Eggs size L X 10
            OrganicEggs = new SpecificItem(210 + i, 15, 20, expiration1, false, "None", "Shelf");
            Eggs.AddSpecificItem(OrganicEggs, specificItemMapper);

            // White bread x 10
            WhiteBread = new SpecificItem(310 + i, 10, 15, expiration1, false, "None", "Shelf");
            Bread.AddSpecificItem(WhiteBread, specificItemMapper);


            // Cucumber  x 10
            cuc = new SpecificItem(410 + i, 3, 7, expiration1, false, "None", "Shelf");
            Cucumber.AddSpecificItem(cuc, specificItemMapper);


            // Tomato x 10
            tom = new SpecificItem(510 + i, 2, 5, expiration1, false, "None", "Shelf");
            Tomato.AddSpecificItem(tom, specificItemMapper);

            try {
                InventoryItemMapper.updateItem(Milk);
                InventoryItemMapper.updateItem(Eggs);
                InventoryItemMapper.updateItem(Bread);
                InventoryItemMapper.updateItem(Cucumber);
                InventoryItemMapper.updateItem(Tomato);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 10; i++) {
            // Milk Tnuva 3% X 10
            MilkTnuva = new SpecificItem(120 + i, 8, 10, expiration2, false, "None", "Warehouse");
            Milk.AddSpecificItem(MilkTnuva, specificItemMapper);

            // Organic Eggs size L X 10
            OrganicEggs = new SpecificItem(220 + i, 15, 20, expiration2, false, "None", "Warehouse");
            Eggs.AddSpecificItem(OrganicEggs, specificItemMapper);

            // White bread x 10
            WhiteBread = new SpecificItem(320 + i, 10, 15, expiration2, false, "None", "Warehouse");
            Bread.AddSpecificItem(WhiteBread, specificItemMapper);

            // Cucumber  x 10
            cuc = new SpecificItem(420 + i, 3, 7, expiration2, false, "None", "Warehouse");
            Cucumber.AddSpecificItem(cuc, specificItemMapper);

            // Tomato x 10
            tom = new SpecificItem(520 + i, 2, 5, expiration2, false, "None", "Warehouse");
            Tomato.AddSpecificItem(tom, specificItemMapper);
        }

        try {
            InventoryItemMapper.updateItem(Milk);
            InventoryItemMapper.updateItem(Eggs);
            InventoryItemMapper.updateItem(Bread);
            InventoryItemMapper.updateItem(Cucumber);
            InventoryItemMapper.updateItem(Tomato);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the SpecificItemDAO object associated with this instance.
     * @return The SpecificItemDAO object.
     */
    public SpecificItemDAO getSpecificItemDAO(){
        return specificItemMapper;
    }

    /**
     * Returns a List of all items in the inventory system.
     * @return a List of all items in the inventory system
     */
    public List<InventoryItem> getTotalItem(){
        List<InventoryItem> res =null;
        try {
            res = InventoryItemMapper.getAllItems();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Returns a List of all categories in the inventory system.
     * @return a List of all categories in the inventory system
     */
    public List<Category> getCategoryList()
    {
        List<Category> res =null;
        try {
            res = CategoryMapper.getAllCategories();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Returns a List of all discounts in the inventory system.
     * @return a List of all discounts in the inventory system
     */
    public List<Discount> getDiscountList() {
        List<Discount> res =null;
        try {
            res = DiscountMapper.getAllDiscounts();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    // Item Functions
    /**
     * Adds a new Item to the inventory system with the specified catalog number, name, manufacturer, and minimum amount.
     * If an item with the specified catalog number already exists in the system, an IllegalArgumentException is thrown.
     * @param name the name of the new item to be added
     * @param manufacturer the manufacturer of the new item to be added
     * @param minimumAmount the minimum amount of the new item to be added
     * @throws IllegalArgumentException if an item with the specified catalog number already exists in the system
     */
    public void AddNewItem(String name, String manufacturer, int minimumAmount, double price){
        int catalogNumber = Item.getItemCatalogNumber(name, manufacturer);
        if(price < 0)
            throw new IllegalArgumentException("Price can't be negative");
        try{

            if(InventoryItemMapper.getItemByCatalogNumber(catalogNumber)==null){
                InventoryItem newItem = new InventoryItem(catalogNumber, name, manufacturer, minimumAmount, price); // create a new item object
                InventoryItemMapper.addItem(newItem);
            } else
                throw new IllegalArgumentException(catalogNumber + " already exists in the system");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the item with the specified catalog number from the inventory system.
     * If the item is not found in the system, an IllegalArgumentException is thrown.
     * @param catalogNumber the catalog number of the item to be removed from the inventory system
     * @throws IllegalArgumentException if the item is not found in the system
     */
    public void DeleteItem(int catalogNumber){
        try{
            InventoryItem item =InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
            if(item == null)
                throw new IllegalArgumentException("This item is not exist");
            else {
                InventoryItemMapper.remove(item);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an inventory item with the specified catalog number.
     * @param catalogNumber The catalog number of the item to update.
     * @throws IllegalArgumentException if the item does not exist.
     */
    public void UpdateItem(int catalogNumber){
        try{
            InventoryItem item =InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
            if(item == null)
                throw new IllegalArgumentException("This item is not exist");
            else {
                InventoryItemMapper.updateItem(item);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Deletes a specific item with the specified ID and catalog number.
     * @param ID The ID of the specific item to delete.
     * @param catalogNumber The catalog number of the item to which the specific item belongs.
     * @throws IllegalArgumentException if the specific item or the item does not exist.
     * @throws Item.NeedOrderException if the item needs to be ordered.
     */
    public void DeleteSpecificItem(int ID, int catalogNumber) throws Item.NeedOrderException {
        try{
            SpecificItem specificItem =specificItemMapper.getSpecificItemByID(ID);
            if(specificItem == null)
                throw new IllegalArgumentException("This item is not exist");
            else {
                InventoryItem item =InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
                item.DeleteSpecificItem(ID, specificItemMapper);
                specificItemMapper.deleteSpecificItem(specificItem, catalogNumber);
                UpdateItem(catalogNumber);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the item with the specified catalog number from the inventory system, or null if the item is not found.
     * @param catalogNumber the catalog number of the item to be retrieved from the inventory system
     * @return the item with the specified catalog number, or null if the item is not found
     */
    public InventoryItem FindMyItem(int catalogNumber){
        InventoryItem item = null;
        try{
            item = InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return item; // if the item is not found, return null
    }

    /**
     * Returns the inventory item with the specified catalog number.
     * @param catalogNumber The catalog number of the item to find.
     * @return The inventory item if found, or null if not found.
     */
    public static InventoryItem staticFindMyItem(int catalogNumber){
        InventoryItem item = null;
        try{
            item = InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return item; // if the item is not found, return null
    }

    /**
     * Updates a specific item with the specified catalog number and new specific item details.
     * @param catalogNumber The catalog number of the item to update.
     * @param specificItem The new specific item details.
     */
    public void UpdateSpecificItem(int catalogNumber, SpecificItem specificItem){
        try {
            specificItemMapper.updateSpecificItem(catalogNumber, specificItem);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Category Functions
    /**
     * Adds a list of inventory items to the specified category.
     * @param items The list of inventory items to add.
     * @param categoryNumber The category number to which the items should be added.
     */
    public void addItemToCategory(List<InventoryItem> items, int categoryNumber){
        CategoryMapper.addToItemsInCategory(items, categoryNumber);
    }

    /**
     * Removes an item from the specified category.
     * @param categoryNumber The category number from which the item should be removed.
     * @param categoryID The ID of the item to be removed from the category.
     */
    public void removeItemFromCategory( int categoryNumber, int categoryID){
        CategoryMapper.removeItemFromCategory(categoryNumber, categoryID);
    }

    /**
     * Adds a new parent category to the inventory system with the specified ID and name.
     * If a category with the specified ID already exists, an IllegalArgumentException is thrown.
     * @param categoryID the ID of the new parent category to be added to the inventory system
     * @param categoryName the name of the new parent category to be added to the inventory system
     * @throws IllegalArgumentException if a category with the specified ID already exists
     */
    public void addFatherCategory(int categoryID, String categoryName){
        try {
            Category category = CategoryMapper.getCategoryById(categoryID);
            if(category != null) // if a category with the specified ID already exists
                throw new IllegalArgumentException("This category ID is already exists");

            Category newCategory = new Category(categoryID, categoryName); // create a new category with the specified ID and name
            CategoryMapper.addCategory(newCategory); // add the new category to the Category Mapper

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a new subcategory to the inventory system with the specified ID, name, and parent category name.
     * If a category with the specified ID already exists, an IllegalArgumentException is thrown.
     * If the parent category with the specified name is not found in the inventory system, an IllegalArgumentException is thrown.
     * @param categoryID the ID of the new subcategory to be added to the inventory system
     * @param categoryName the name of the new subcategory to be added to the inventory system
     * @param fatherCategoryID the ID of the parent category for the new subcategory to be added to the inventory system
     * @throws IllegalArgumentException if a category with the specified ID already exists or the parent category with the specified name is not found in the inventory system
     */
    public void addSubCategory(int categoryID, String categoryName, int fatherCategoryID){
        try{
            Category category = CategoryMapper.getCategoryById(categoryID);
            if(category != null) // if a category with the specified ID already exists
                throw new IllegalArgumentException("This category ID is already exists");

            Category fatherCategory = CategoryMapper.getCategoryById(fatherCategoryID);
            Category newCategory = new Category(categoryID, categoryName, fatherCategory);
            CategoryMapper.addCategory(newCategory); // add the new category to the Category Mapper
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the name of the category with the given ID in the list of categories.
     * @param categoryID the ID of the category to update.
     * @param categoryName the new name of the category.
     * @throws IllegalArgumentException if no category with the given ID is found in the list of categories.
     */
    public void updateCategory(int categoryID, String categoryName){
        try{
            Category category = CategoryMapper.getCategoryById(categoryID);
            if(category == null) // if a category with the specified ID exists
                throw new IllegalArgumentException("This category ID is not exists");

            category.setCategoryName(categoryName);
            CategoryMapper.updateCategory(category);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Discount Functions
    /**
     * Adds a new discount for a list of items.
     * @param saleNumber the unique number of the sale.
     * @param saleName the name of the sale.
     * @param discountPercentage the percentage discount to apply to the items.
     * @param startDate the start date of the sale.
     * @param endDate the end date of the sale.
     * @param catalogNumbers the list of catalog numbers for the items to discount.
     */
    public void addItemDiscount(int saleNumber, String saleName, double discountPercentage, LocalDate startDate, LocalDate endDate, List<Integer> catalogNumbers){
        try{
            Discount discount = DiscountMapper.getDiscountBySaleNumber(saleNumber);
            if(discount != null) // if a discount with the sale number already exists
                throw new IllegalArgumentException("This sale number is already exists");

            List<InventoryItem> inventoryItemList = new LinkedList<>();
            InventoryItem item;
            // Add all the items that belong to this discount
            for (Integer catalogNumber : catalogNumbers) {
                item = InventoryItemMapper.getItemByCatalogNumber(catalogNumber);
                if(item == null){
                    throw new IllegalArgumentException("This item doesnt exists");
                }
                inventoryItemList.add(item);
            }

            Discount newDiscount = new Discount(saleNumber, saleName, discountPercentage, startDate, endDate, inventoryItemList);
            DiscountMapper.addDiscount(newDiscount);  // add the new discount to the Discount Mapper
            //DiscountMapper.addItemsToDiscount(inventoryItemList, saleNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a new discount for a list of categories.
     * @param saleNumber the unique number of the sale.
     * @param saleName the name of the sale.
     * @param discountPercentage the percentage discount to apply to the items.
     * @param startDate the start date of the sale.
     * @param endDate the end date of the sale.
     * @param categoriesID the list of IDs of the categories to discount.
     */
    public void addCategoryDiscount(int saleNumber,String saleName, double discountPercentage, LocalDate startDate, LocalDate endDate, List<Integer> categoriesID){
        try{
            Discount discount = DiscountMapper.getDiscountBySaleNumber(saleNumber);
            if(discount != null) // if a discount with the sale number already exists
                throw new IllegalArgumentException("This sale number is already exists");

            List<InventoryItem> inventoryItemsList = new LinkedList<>();
            List <InventoryItem> tempList;
            Category category;
            // Add all the items that belong to this discount
            for (Integer categoryID : categoriesID) {
                category = CategoryMapper.getCategoryById(categoryID);
                if(category == null){
                    throw new IllegalArgumentException("The category ID " + categoryID + " doesn't exists");
                }
                tempList = category.getItems();
                inventoryItemsList.addAll(tempList);
            }

            Discount newDiscount = new Discount(saleNumber, saleName, discountPercentage, startDate, endDate, inventoryItemsList);
            DiscountMapper.addDiscount(newDiscount);  // add the new discount to the Discount Mapper

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Finds a category by its ID.
     * @param categoryID the ID of the category to find.
     * @return the category with the specified ID, or null if no such category exists.
     */
    public Category FindCategoryByID(int categoryID){
        Category category = null;
        try {
            category = CategoryMapper.getCategoryById(categoryID);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return category; // if the item is not found, return null
    }

    /**
     * Checks if an item is currently on discount and returns the discount if so.
     * @param catalogNumber the catalog number of the item to check.
     * @return the discount object that includes the item, or null if no discount is currently active for the item.
     */
    public Discount CheckIfItemOnDiscount(int catalogNumber){
        List<Discount> allDiscounts = getDiscountList();
        for (int i=0; i<allDiscounts.size(); i++){
            List<InventoryItem> allItemsInDiscount = allDiscounts.get(i).getIncludeInDiscount();
            for(int j=0; j<allItemsInDiscount.size(); j++){
                if(allItemsInDiscount.get(i).getCatalogNumber() == catalogNumber)
                    return allDiscounts.get(i);
            }
        }
        return null;
    }

    /**
     * Removes expired discounts from the discount list.
     * This method iterates through the list of discounts and removes any discounts that have already expired,
     * based on their end dates.
     */
    public void removeDiscount() {
        try {
            List<Discount> allDiscount = getDiscountList();
            Discount discount;
            for (Discount value : allDiscount) {
                discount = value;
                if (discount.getEndDate().isBefore(LocalDate.now()))
                    DiscountMapper.removeDiscount(discount.getSaleNumber());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives an order for inventory from a supplier and adds the items to the inventory.

     * @param orderForInventory The order for inventory received from the supplier.
     */
    public static void ReceiveOrderFromSupplier(Supplier.OrderForInventory orderForInventory)
    {
        ArrayList<Supplier.itemsInInventoryOrder> items = orderForInventory.getItems();
        for (Supplier.itemsInInventoryOrder item : items) {
            Multi_Add(item);
        }
    }

    /**
     * Adds multiple specific items to the inventory based on the supplier's order.
     *
     * @param item the supplier's item order containing the item ID, final cost, expiration date, and quantity
     */
    private static void Multi_Add(Supplier.itemsInInventoryOrder item) {
        try {
            SupplierItem supplierItem = item.getMySupplierItem();
            int myCatalogNum = Item.getItemCatalogNumber(supplierItem.getName(), supplierItem.getManufacturer());
            InventoryItem inventoryItem = ItemController.staticFindMyItem(myCatalogNum);
            int j = 0;
            Random rand = new Random();
            for (int i = 0; i < item.getQuantity(); i++) {
                int m = item.getItemID()+ j*111;
                boolean flag = true;
                while (flag) {
                    j++;
                    int rand_int = rand.nextInt(222);
                    m = m*rand_int;
                    try {
                        inventoryItem.AddSpecificItem(new SpecificItem(m, item.getFinalCost(), item.getExpirationDate(), inventoryItem.getPrice()), specificItemMapper);
                        flag = false;
                    }
                    catch (Exception e){
                    }
                }
                InventoryItemMapper.updateItem(inventoryItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the quantity of each inventory item and throws a NeedOrderException if the total amount is below the minimum amount.
     *
     * @throws Item.NeedOrderException if an item needs to be ordered due to low quantity
     */
    public void checkQuantity() throws Item.NeedOrderException {
        List<InventoryItem> items = getTotalItem();
        for (InventoryItem item : items) {
            if (item.getTotalAmount() < item.getMinimumAmount())
                try{
                    item.make_need_order();
                }
                catch (Item.NeedOrderException e){
                    throw new Item.NeedOrderException(e);
                }

        }
    }

    public void getAllItems(){
       List<InventoryItem> allItems = getTotalItem();
        for (InventoryItem Item : allItems) {
            Item.printInfo();
        }
    }

    public void getAllSpecificItems(){
        List<InventoryItem> allItems = getTotalItem();
        for (InventoryItem Item : allItems) {
            List<SpecificItem> allSpecificItems = Item.getIncludeItems();
            for (SpecificItem allSpecificItem : allSpecificItems) {
                System.out.println(allSpecificItem.toString());
            }
        }
    }

    public void getAllDiscounts(){
        try {
            List<Discount> allDiscount = DiscountMapper.getAllDiscounts();
            for (Discount discount : allDiscount){
                discount.printInfo();
            }
        } catch (SQLException e){

        }
    }

    public void getAllCategories(){
        try {
            List<Category> allCategories = CategoryMapper.getAllCategories();
            for (Category category : allCategories) {
                System.out.println(category.printInfo());
            }
        } catch (SQLException e) {

        }
    }
}