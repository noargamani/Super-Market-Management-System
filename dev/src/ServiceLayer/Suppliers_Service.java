package Service_layer;

import BusinessLayer.Constants;
import BusinessLayer.Item;
import BusinessLayer.Suppliers.Classes.*;
import BusinessLayer.Suppliers.Controllers.Suppliers_Controller;
import DataAcessLayer.DAO.DAO_MAIN;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;

import java.sql.SQLException;
import java.util.*;

public class Suppliers_Service implements Service {

    private final Suppliers_Controller SC;

    /**
     * Constructs a new instance of Suppliers_Service and initializes the Suppliers_Controller.
     */
    public Suppliers_Service() {
        SupplierDAO supplierDAO= DAO_MAIN.initControllers();
        SC = new Suppliers_Controller(supplierDAO);
    }

    /**
     * Retrieves the Suppliers_Controller instance.
     *
     * @return The Suppliers_Controller instance.
     */
    public Suppliers_Controller getSupplierController(){
        return SC;
    }

    /**
     * Adds a new supplier.
     *
     * @param name                The name of the supplier.
     * @param address             The address of the supplier.
     * @param manufactures        The list of manufactured items by the supplier.
     * @param contact_List        The sorted map of contact information for the supplier.
     * @param discountByTotalPrice The sorted map of discounts based on total price for the supplier.
     * @param discountByTotalQuantity The sorted map of discounts based on total quantity for the supplier.
     * @param supplier_card       The supplier's card information.
     * @param contract            The contract details of the supplier.
     */
    @Override
    public void addSupplier(String name, String address, ArrayList<String> manufactures, SortedMap<String, String> contact_List, SortedMap<Double, Integer> discountByTotalPrice, SortedMap<Integer, Integer> discountByTotalQuantity, Supplier_Card supplier_card, Contract contract) throws SQLException {
        SC.add_Supplier(name, address, manufactures, contact_List, discountByTotalPrice, discountByTotalQuantity, supplier_card, contract);
    }

    /**
     * Retrieves an item from a specific supplier.
     *
     * @param itemName       The name of the item.
     * @param itemManufacture The manufacture of the item.
     * @param supplierName   The name of the supplier.
     * @return The SupplierItem object if the item exists, null otherwise.
     */
    public SupplierItem getItem(String itemName, String itemManufacture , String supplierName) {
        if (SC.ItemExists(itemName, itemManufacture)) {
            return SC.getItem(itemName, itemManufacture , supplierName);
        } else {
            try {
                throw new Exception("Item Doesn't exist");
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Removes an item from a specific supplier.
     *
     * @param itemName       The name of the item.
     * @param itemManufacture The manufacture of the item.
     * @param supplierName   The name of the supplier.
     * @return true if the item is removed successfully, false otherwise.
     */
    public boolean removeItem(String itemName, String itemManufacture, String supplierName) {
        return SC.removeItem(itemName, itemManufacture , supplierName);
    }

    /**
     * Updates the status of an order for a specific supplier.
     *
     * @param orderId  The ID of the order.
     * @param supplier The name of the supplier.
     * @param status   The new status of the order.
     */
    public void updateOrderStatus(int orderId , String supplier, String status) throws Exception
    {
        SC.updateOrderStatus(orderId , supplier, status);
    }

    /**
     * Retrieves a supplier by username.
     *
     * @param username The username of the supplier.
     * @return The Supplier object if the supplier exists, null otherwise.
     */
    @Override
    public Supplier getSupplier(String username) throws Exception {
        if (SC.SupplierExist(username)) {
            return SC.getSupplier(username);
        } else {
            try {
                throw new Exception("Supplier Doesn't exist");
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * Adds an item to a supplier's inventory.
     *
     * @param supplier             The name of the supplier.
     * @param itemName             The name of the item.
     * @param itemManufacture      The manufacture of the item.
     * @param supplier_catalogID   The supplier's catalog ID for the item.
     * @param item_list_price      The list price of the item.
     * @param discountByQuantity   The sorted map of discounts based on quantity for the item.
     * @param quantity             The quantity of the item.
     * @param expiration           The expiration date of the item.
     */
    @Override
    public void addItem(String supplier, String itemName, String itemManufacture, int supplier_catalogID, double item_list_price, SortedMap<Integer, Integer> discountByQuantity, int quantity,Date expiration ) throws Exception{
        SC.addItem( supplier, itemName, itemManufacture , supplier_catalogID,  item_list_price, discountByQuantity, quantity,expiration);
    }

    /**
     * Adds an order for a supplier.
     *
     * @param items        The hashtable of item IDs and quantities to order.
     * @param supplierName The name of the supplier.
     */
    public void addOrder(Hashtable<Integer , Integer>items, String supplierName) throws Exception {
        SC.addOrder(items,supplierName);
    }

    /**
     * Makes an automatic order for the specified items.
     *
     * @param itemsToOrder The hashtable of items to order with their quantities.
     * @return The list of OrderForInventory objects representing the ordered items.
     */
    public ArrayList<Supplier.OrderForInventory> makeAutoOrder(Hashtable<Constants.Pair<String , String>, Integer> itemsToOrder) throws Exception {
        return SC.addInventoryOrder(itemsToOrder , Contract.Day.None);
    }

    /**
     * Retrieves the names of all suppliers.
     *
     * @return A set containing the names of all suppliers.
     */
    public Set<String> getSuppliersNames()
    {
        return SC.getSuppliers().keySet();
    }

    /**
     * Checks if a supplier name exists.
     *
     * @param name The name of the supplier to check.
     * @return true if the supplier name exists, false otherwise.
     */
    public boolean ContainsSupplierName(String name)
    {
        return SC.SupplierExist(name);
    }

    /**
     * Updates the contract of a supplier.
     *
     * @param supplierName The name of the supplier.
     * @param contract     The updated contract details.
     */
    public void UpdateContract(String supplierName, Contract contract) throws Exception {
        SC.UpdateContract(supplierName, contract);
    }

    /**
     * Makes a periodic order based on the current day.
     *
     * @param currentDay The current day for the periodic order.
     * @return The list of OrderForInventory objects representing the ordered items.
     */
    public ArrayList<Supplier.OrderForInventory> PeriodicOrder(Contract.Day currentDay) throws Exception {
        return SC.PeriodicOrder(currentDay);
    }

    /**
     * Makes a routine order for the specified items on the given days.
     *
     * @param quantityByItemNames The hashtable of item names and quantities to order.
     * @param days                The list of days for the routine order.
     */
    public void makeRoutineOrder(Hashtable<Constants.Pair<String, String>, Integer> quantityByItemNames, ArrayList<Contract.Day> days) {
        SC.AddPeriodicOrder(quantityByItemNames , days);
    }

    /**
     * Removes a routine order by its order ID.
     *
     * @param OrderID The ID of the routine order to remove.
     */
    public void removeRoutineOrder(Integer OrderID) throws SQLException {
        SC.RemovePeriodicOrder(OrderID);
    }

    /**
     * Updates a routine order with new item quantities and days.
     *
     * @param orderID            The ID of the routine order to update.
     * @param quantityByItemNames The hashtable of item names and quantities to update.
     * @param days               The list of days for the updated routine order.
     */
    public void updateRoutineOrder(int orderID, Hashtable<Constants.Pair<String, String>, Integer> quantityByItemNames, ArrayList<Contract.Day> days) throws SQLException {
        SC.UpdatePeriodicOrder(orderID , quantityByItemNames , days);
    }

    /**
     * Makes a need order for the specified item with a NeedOrderException.
     *
     * @param e The NeedOrderException containing the item information.
     * @return The list of OrderForInventory objects representing the ordered items.
     */
    public ArrayList<Supplier.OrderForInventory> makeNeedOrder(Item.NeedOrderException e) throws Exception {
        return SC.MakeNeedOrder(e.getItemName(), e.getManufacturer(), e.getAmount());
    }

    public boolean checkItemExistence(String itemName, String itemManufacture) {
        return SC.ItemExist(itemName, itemManufacture);
    }
}