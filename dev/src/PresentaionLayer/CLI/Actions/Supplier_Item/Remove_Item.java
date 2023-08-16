package Presentaion_layer.CLI.Actions.Supplier_Item;

import Service_layer.Suppliers_Service;

import java.util.Scanner;

public class Remove_Item {

    /**
     * Removes an item from the supplier's inventory.
     *
     * @param service      The Suppliers_Service object.
     * @param supplierName The name of the supplier.
     * @return A message indicating the status of the item removal.
     */
    public static String remove_Item(Suppliers_Service service , String supplierName) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Item to Remove");
            String name=terminal_IO(scanner,"Item Name");
            String Manufacture=terminal_IO(scanner,"Item Manufacture");

            boolean status = service.removeItem(name,Manufacture , supplierName);
            return "Item removed" + status;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Reads user input from the terminal.
     *
     * @param scanner The Scanner object to read user input.
     * @param input   The input prompt.
     * @return The user's input as a String.
     */
    public static String terminal_IO(Scanner scanner, String input) {
        System.out.println("Enter "+input);
        String name = scanner.nextLine();
        System.out.println(input+": "+name);
        return name;
    }
}
