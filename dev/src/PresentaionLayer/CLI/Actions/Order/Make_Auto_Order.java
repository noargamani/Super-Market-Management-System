package Presentaion_layer.CLI.Actions.Order;

import BusinessLayer.Item;
import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier;
import Service_layer.Suppliers_Service;


import java.sql.SQLException;
import java.util.*;

public class Make_Auto_Order {

    /**
     * Converts a string representation of a day of the week to the corresponding Contract.Day enum value.
     *
     * @param dayString The string representation of the day of the week.
     * @return The Contract.Day enum value corresponding to the given dayString, or Contract.Day.None if no match is found.
     */
    public static Contract.Day getDayOfWeek(String dayString) {
        switch (dayString.toLowerCase()) {
            case "sunday":
                return Contract.Day.Sunday;
            case "monday":
                return Contract.Day.Monday;
            case "tuesday":
                return Contract.Day.Tuesday;
            case "wednesday":
                return Contract.Day.Wednesday;
            case "thursday":
                return Contract.Day.Thursday;
            case "friday":
                return Contract.Day.Friday;
            case "saturday":
                return Contract.Day.Saturday;
            default:
                return Contract.Day.None;
        }
    }

    /**
     * Generates an automatic order for items based on their quantity requirements.
     *
     * @param service The Suppliers_Service instance used to make the auto order.
     * @return A string representation of the auto order, including the details of the ordered items.
     */
    public static String make_Auto_Order(Suppliers_Service service) {
        try {
            StringBuilder result = new StringBuilder();
            Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemNames = getItemsToOrder();
            ArrayList<Supplier.OrderForInventory> orders = service.makeAutoOrder(QuantityByItemNames);
            for (Supplier.OrderForInventory order : orders) {
                result.append("\n").append(order.toString());
            }
            return result.toString();
        }
        catch (Exception e){
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Makes a routine order to suppliers based on the specified items and days.
     *
     * @param service The Suppliers_Service instance used to make the routine order.
     * @return An empty string.
     */
    public static String make_Routine_Order(Suppliers_Service service)
    {
        try {
            ArrayList<Contract.Day> days = getDays();
            Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemNames = getItemsToOrder();
            service.makeRoutineOrder(QuantityByItemNames, days);
        }
        catch (Exception e){
            return "Error: " + e.getMessage();
        }
        return "";
    }

    /**
     * Retrieves a list of Contract.Day values based on user input.
     *
     * @return An ArrayList containing the selected days of the week.
     */
    private static ArrayList<Contract.Day> getDays() {
        Scanner scanner = new Scanner(System.in);
        String day = "";
        ArrayList<Contract.Day> days = new ArrayList<>();
        while(!day.equals("0")){
            try {
                System.out.println("Enter new Day. to stop enter 0");
                day = String.valueOf(terminal_IO(scanner, "Day"));
                days.add(getDayOfWeek(day));
            }
            catch (Exception e){
                System.out.println("invalid Item Name");
            }
        }
        return days;
    }

    /**
     * Retrieves a hashtable of items to be ordered based on user input.
     *
     * @return A hashtable containing the item names, manufacturers, and quantities to order.
     */
    private static Hashtable<Constants.Pair<String , String>, Integer> getItemsToOrder() {
        Scanner scanner = new Scanner(System.in);
        String itemName = "";
        Hashtable<Constants.Pair<String , String>, Integer> QuantityByItem = new Hashtable<>();
        while(!itemName.equals("0")){
            try {

                System.out.println("Enter new Item details. to stop enter 0");
                itemName = String.valueOf(terminal_IO(scanner, "Item's Name"));
                if (!itemName.equals("0")) {

                    String itemManufacture = String.valueOf(terminal_IO(scanner, "Item's Manufacture"));
                    int quantity = Integer.parseInt(terminal_IO(scanner, "Item's quantity"));
                    try
                    {
                        if(containsKey(QuantityByItem.keySet() , itemName ,itemManufacture))
                        {
                            throw new Exception("Same Item Inserted Twice, Re-check list");
                        }
                        Constants.Pair<String , String> itemProp = new Constants.Pair<>(itemName , itemManufacture);
                        QuantityByItem.put(itemProp, quantity);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
            }
            catch (Exception e){
                System.out.println("invalid Item Name");
            }
        }
        return QuantityByItem;
    }

    /**
     * Checks if the given item name and manufacturer exist in the set of pairs.
     *
     * @param S                The set of pairs to check.
     * @param itemName         The item name to search for.
     * @param itemManufacture  The item manufacturer to search for.
     * @return True if the pair exists in the set, false otherwise.
     */
    private static boolean containsKey(Set<Constants.Pair<String , String>> S , String itemName , String itemManufacture)
    {
        for (Constants.Pair p : S)
        {
            if(p.first == itemName && p.second == itemManufacture)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads user input from the terminal for the specified input prompt.
     *
     * @param scanner The scanner object used for input.
     * @param input   The input prompt to display.
     * @return The user input as a string.
     */
    public static String terminal_IO(Scanner scanner, String input) {
        System.out.println("Enter "+input);
        String name = scanner.nextLine();
        System.out.println(input+": "+name);
        return name;
    }

    /**
     * Removes a routine order based on user input.
     *
     * @param suppliersService The Suppliers_Service object to perform the removal.
     */
    public static void remove_Routine_Order(Suppliers_Service suppliersService) {
        Scanner scanner = new Scanner(System.in);
        int orderID = -2;
            try {
                orderID = getMyOrderID(scanner);
                if (orderID != -1) {
                    suppliersService.removeRoutineOrder(orderID);
                }
            }
            catch (Exception e){
                System.out.println("invalid Item Name");
            }
    }

    /**
     * Retrieves the order ID based on user input.
     *
     * @param scanner The scanner object used for input.
     * @return The order ID entered by the user.
     */
    private static int getMyOrderID(Scanner scanner) {
        String orderID;
        System.out.println("Enter orderID. to stop enter -1");
        orderID = String.valueOf(terminal_IO(scanner, "orderID"));
        int myOrderID = Integer.parseInt(orderID);
        return myOrderID;
    }

    /**
     * Updates a routine order based on user input.
     *
     * @param suppliersService     The Suppliers_Service object to perform the update.
     * @return An empty string indicating the operation is complete.
     */
    public static String update_Routine_Order(Suppliers_Service suppliersService) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int orderID = getMyOrderID(scanner);
        ArrayList<Contract.Day> days = getDays();
        Hashtable<Constants.Pair<String , String>, Integer> QuantityByItemNames = getItemsToOrder();
        suppliersService.updateRoutineOrder(orderID, QuantityByItemNames, days);
        return "";
    }

    /**
     * Generates a need order based on the supplied NeedOrderException.
     *
     * @param suppliersService The Suppliers_Service object to generate the need order.
     * @param e                The NeedOrderException containing the necessary information.
     * @return An ArrayList of Supplier.OrderForInventory objects representing the generated need order.
     */
    public static  ArrayList<Supplier.OrderForInventory> makeNeedOrder(Suppliers_Service suppliersService, Item.NeedOrderException e) throws Exception {
        return suppliersService.makeNeedOrder(e);
    }
}

