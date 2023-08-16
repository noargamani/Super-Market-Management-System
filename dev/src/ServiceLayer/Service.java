package Service_layer;

import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.Supplier_Card;

import java.sql.SQLException;
import java.util.*;

public interface Service {
    /**
     * add supplier
     * @param name
     * @param address
     * @param manufactures
     * @param contact_List
     * @param discountByTotalPrice
     * @param discountByTotalQuantity
     * @param supplier_card
     * @param contract
     */
    void addSupplier(String name, String address, ArrayList<String> manufactures, SortedMap<String,String> contact_List, SortedMap<Double,Integer> discountByTotalPrice, SortedMap<Integer,Integer> discountByTotalQuantity, Supplier_Card supplier_card, Contract contract) throws SQLException;

    /**
     * get supplier
     * @param username
     */
    Supplier getSupplier(String username) throws Exception;

    /**
     * add item
     * @param itemName
     * @param supplier_catalogID
     * @param item_list_price
     * @param discountByQuantity
     * @param quantity
     */
    void addItem(String supplier, String itemName, String itemManufacture, int supplier_catalogID, double item_list_price, SortedMap<Integer, Integer> discountByQuantity, int quantity,Date expiration) throws SQLException, Exception;

    /**
     * add order from supplier
     * @param items
     * @param supplierName
     */
    void addOrder(Hashtable<Integer , Integer> items, String supplierName) throws Exception;

    /**
     * Generates an auto-order for inventory based on the provided items and quantities.
     *
     * @param itemsToOrder A Hashtable mapping item name and manufacture to the quantity to be ordered.
     * @return An ArrayList of Supplier.OrderForInventory objects representing the auto-order.
     */
    ArrayList<Supplier.OrderForInventory> makeAutoOrder(Hashtable<Constants.Pair<String , String>, Integer> itemsToOrder) throws Exception;
}

