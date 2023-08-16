package BusinessLayer.Inventory.Classes;

import BusinessLayer.Item;
import BusinessLayer.Suppliers.Controllers.Suppliers_Controller;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InventoryItem extends Item {

    private double price;
    private int ShelvesAmount;
    private int WarehouseAmount;
    private List<SpecificItem> IncludeItems;

    /**
     * Creates a new instance of the Item class.
     * @param catalogNumber:  The catalog number of the item.
     * @param name: The name of the item.
     * @param manufacturer: The name of the item's manufacturer.
     * @param minimumAmount: The minimum amount of the item that should be kept in the store.
     * @param Price: The price of the item
     */
    public InventoryItem(int catalogNumber, String name, String manufacturer, int minimumAmount, double Price){
        super(catalogNumber, name, manufacturer, 0, minimumAmount);
        ShelvesAmount = 0;
        WarehouseAmount = 0;
        IncludeItems = new LinkedList<>();
        price = Price;
    }

    /**
     * Creates a new instance of the Item class.
     * @param catalogNumber:  The catalog number of the item.
     * @param name: The name of the item.
     * @param manufacturer: The name of the item's manufacturer.
     * @param minimumAmount: The minimum amount of the item that should be kept in the store.
     * @param Price: The price of the item
     * @param items: The specific items that include this item
     */
    public InventoryItem(int catalogNumber, String name, String manufacturer, int totalAmount, int shelvesAmount, int warehouseAmount, int minimumAmount, double Price, List<SpecificItem> items){
        super(catalogNumber, name, manufacturer, totalAmount, minimumAmount);
        ShelvesAmount = shelvesAmount;
        WarehouseAmount = warehouseAmount;
        price = Price;
        IncludeItems = items;
    }

    /**
     * Returns the number of the item that is currently on the shelves in the store.
     * @return the total amount of the item in the store.
     */
    public int getShelvesAmount() {
        return ShelvesAmount;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    /**
     * Returns the number of the item that is currently in the warehouse in the store.
     * @returnthe number of the item that is currently in the warehouse in the store.
     */
    public int getWarehouseAmount() {
        return WarehouseAmount;
    }

    /**
     * Sets the catalog number of the specific item.
     * @param catalogNumber: The new catalog number of the item.
     */

    /**
     * Sets the number of items currently on shelves.
     * @param shelvesAmount: The number of items currently on shelves.
     */
    public void setShelvesAmount(int shelvesAmount) {
        ShelvesAmount = shelvesAmount;
    }

    /**
     * Sets the amount of the item in the warehouse.
     * @param warehouseAmount: The new amount of the item in the warehouse.
     */
    public void setWarehouseAmount(int warehouseAmount) {
        WarehouseAmount = warehouseAmount;
    }

    /**
     * Adds a specific item to the item's list of included items.
     * @param SItem: the specific item to be added
     */
    public void AddSpecificItem(SpecificItem SItem, SpecificItemDAO dao){
        // Check if the item already exists in the inventory
        for(int i=0; i< IncludeItems.size(); i++){
            if(IncludeItems.get(i).getID() == SItem.getID())
                throw new IllegalArgumentException("This item is already exist");
        }

        // If the item is not already in the inventory, add it
        IncludeItems.add(SItem);
        try {
            dao.addSpecificItem(SItem, getCatalogNumber());

            this.TotalAmount++;
        } catch (SQLException e){
            e.printStackTrace();
        }

        // If the item is located on a shelf, increment the shelf count, otherwise increment the warehouse count
        if(Objects.equals(SItem.getLocation(), "Shelf"))
            this.ShelvesAmount++;
        else
            this.WarehouseAmount++;
    }

    /**
     * Deletes an item with the specified ID from the store.
     * @param ID: the ID of the item to delete
     */
    public void DeleteSpecificItem(int ID, SpecificItemDAO dao)throws NeedOrderException {
        // Check if the inventory is empty
        if(IncludeItems.isEmpty())
            throw new IllegalArgumentException("This Item is not found in the store");

        int found=0;
        // Find the item with the given ID
        for(int i=0; i<IncludeItems.size(); i++){
            SpecificItem item = IncludeItems.get(i);
            if(item.getID() == ID) {
                // If the item is located on a shelf, decrement the shelf count, otherwise decrement the warehouse count
                if(Objects.equals(IncludeItems.get(i).getLocation(), "Shelf"))
                    this.ShelvesAmount--;
                else
                    this.WarehouseAmount--;

                // Remove the item from the inventory and decrement the total count
                IncludeItems.remove(IncludeItems.get(i));
                try {
                    dao.deleteSpecificItem(item, getCatalogNumber());

                    this.TotalAmount--;
                } catch (SQLException e){
                    e.printStackTrace();
                }
                try{
                    removeOne();
                }
                catch (NeedOrderException e){
                    throw new NeedOrderException(e);
                }
                found++;
                break;
            }
        }
        // If the item is not found in the inventory, throw an exception
        if(found==0)
            throw new IllegalArgumentException("This Item is not found in the store");
    }

    /**
     * Finds an item with the specified ID in the store.
     * @param ID: Finds an item with the specified ID in the store.
     * @return the item with the specified ID, or null if it is not found in the store
     */
    public SpecificItem FindSpecificItem(int ID){
        // Check if the inventory is empty
        if(IncludeItems.isEmpty())
            return null;

        // Find the item with the given ID
        for(int i=0; i<IncludeItems.size(); i++){
            if(IncludeItems.get(i).getID() == ID) {
                return IncludeItems.get(i);
            }
        }
        // If the item is not found in the inventory, return null
        return null;
    }

    /**
     * Prints information about the item.
     */
    public String printInfo(){
        String info = "Catalog Number: " + CatalogNumber + " Item Name: " + Name +
                " Manufacturer: " + Manufacturer + " Total Amount: " + TotalAmount + " Amount On Shelves: " +
                ShelvesAmount + " Amount In Warehouse: " + WarehouseAmount;
        System.out.println(info);
        return info;
    }

    /**
     * Returns the list of items included in the store.
     * @returnthe list of items included in the store
     */
    public List<SpecificItem> getIncludeItems() {
        return IncludeItems;
    }

    /**
     * Returns the price of the item.
     * @return the price of the item.
     */
    public double getPrice() {
        return price;
    }


}



