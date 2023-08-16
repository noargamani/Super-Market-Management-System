package BusinessLayer.Inventory.Classes;

import java.util.LinkedList;
import java.util.List;
public class Category {

    private int CategoryID;
    private String CategoryName;
    private Category FatherCategory;
    private List<Category> SubCategories;
    private List<InventoryItem> inventoryItems;

    /**
     * Constructs a new Category object with the specified category ID and category name.
     * @param categoryID: the unique identifier for the category
     * @param categoryName: the name of the category
     */
    public Category(int categoryID, String categoryName) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        FatherCategory = null;
        SubCategories = new LinkedList<>();
        inventoryItems = new LinkedList<>();
    }

    /**
     * Constructs a new Category object with the given category ID, name and father category.
     * @param categoryID: the unique identifier for the category
     * @param categoryName: the name of the category
     * @param fatherCategory: the father category of this category, or null if this category has no father
     */
    public Category(int categoryID, String categoryName, Category fatherCategory) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        FatherCategory = fatherCategory;
        SubCategories = new LinkedList<>();
        inventoryItems = new LinkedList<>();
    }

    /**
     * Constructs a new Category object with the given category ID, name and father category.
     * @param categoryID: the unique identifier for the category
     * @param categoryName: the name of the category
     * @param fatherCategory: the father category of this category, or null if this category has no father
     * @param subCategories: the categories that belong lo this category
     * @param InventoryItems: the items that belong to this category
     */
    public Category(int categoryID, String categoryName, Category fatherCategory, List<Category> subCategories, List<InventoryItem> InventoryItems) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        FatherCategory = fatherCategory;
        SubCategories = subCategories;
        inventoryItems = InventoryItems;
    }

    /**
     * Returns the ID of the category.
     * @return An integer representing the ID of the category.
     */
    public int getCategoryID() {
        return CategoryID;
    }

    /**
     * Returns the name of this category.
     * @return The name of this category.
     */
    public String getCategoryName() {
        return CategoryName;
    }

    /**
     * Set the ID of the Category object.
     * @param categoryID: An integer representing the ID of the category.
     */
    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    /**
     * Set the name of the category.
     * @param categoryName: Set the name of the category.
     */
    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    /**
     * Returns the father category of this category.
     * @return The father category of this category.
     */
    public Category getFatherCategory() {
        return FatherCategory;
    }

    /**
     * Returns a list of subcategories that belong to this category.
     * @return A list of Category objects representing the subcategories of this category
     */
    public List<Category> getSubCategories() {
        return SubCategories;
    }

    /**
     * Returns a list of Item objects that belong to this Category.
     * @return A list of Item objects that belong to this Category.
     */
    public List<InventoryItem> getItems() {
        return inventoryItems;
    }

    /**
     * Sets the parent category of the current category.
     * @param fatherCategory: The parent category to be set
     */
    public void setFatherCategory(Category fatherCategory) {
        FatherCategory = fatherCategory;
    }

    /**
     * Sets the sub-categories of this category to the specified list of categories.
     * @param subCategories: The list of sub-categories to set for this category.
     */
    public void setSubCategories(List<Category> subCategories) {
        SubCategories = subCategories;
    }

    /**
     * Sets the list of items of this category.
     * @param inventoryItems: The list of items to set.
     */
    public void setItems(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    /**
     * Add an item to the category.
     * @param inventoryItemInventory: An Item object to be added to the category.
     */
    public void AddItemToCategory(InventoryItem inventoryItemInventory){
        // Check if the item already exists in the category by comparing their catalog numbers
        if(!inventoryItems.isEmpty()){
            for(int i = 0; i< inventoryItems.size(); i++){
                // If the item already exists, throw an exception
                if(inventoryItems.get(i).getCatalogNumber() == inventoryItemInventory.getCatalogNumber())
                    throw new IllegalArgumentException("This item is already exist in this category");
            }
        }

        // If the item doesn't exist, add it to the category's item list
        inventoryItems.add(inventoryItemInventory);

    }

    /**
     * Removes an item from the list of items in this category.
     * @param inventoryItemInventory: The item to remove from this category.
     */
    public void RemoveItemFromCategory(InventoryItem inventoryItemInventory){
        // Check if the category is not empty
        if(inventoryItems.isEmpty())
            throw new IllegalArgumentException("This item is not exist in this category");

        // Initialize a flag for checking if the item exists in the category
        int flag = 0;
        // Iterate over the items in the category and check if the given item exists in it
        for(int i = 0; i< inventoryItems.size(); i++){
            if(inventoryItems.get(i).getCatalogNumber() == inventoryItemInventory.getCatalogNumber())
                flag++;
        }

        // If the item exists, remove it from the category
        if(flag!=0)
            inventoryItems.remove(inventoryItemInventory);
            // If the item does not exist, throw an exception
        else
            throw new IllegalArgumentException("This item is not exist in this category");
    }

    /**
     * Returns a String containing information about the category and its items.
     * If there are no items in the category, the String will indicate so.
     * Otherwise, the String will list information about each item in the category.
     *
     * @return a String containing information about the category and its items
     */
    public String printInfo(){
        StringBuilder info = new StringBuilder();
        info.append(CategoryName).append(":");
        info.append("\n");
        if(inventoryItems.isEmpty()){ // if there are no items in the category
            info.append("There are no items in the category");
            info.append("\n");
        }
        else{ // if there are items in the category
            for(InventoryItem inventoryItems : inventoryItems){ // iterate through each item in the category
                info.append(inventoryItems.printInfo());
                info.append("\n");
            }
        }
        return info.toString();
    }
}