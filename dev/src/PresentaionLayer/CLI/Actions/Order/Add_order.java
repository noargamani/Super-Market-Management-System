package Presentaion_layer.CLI.Actions.Order;


import BusinessLayer.Suppliers.Classes.Supplier;
import Service_layer.Suppliers_Service;

import java.util.Hashtable;
import java.util.Scanner;

public class Add_order {

    /**
     * add order
     * @param service
     */
    public static void Add(Suppliers_Service service){
        try {
            Scanner scanner = new Scanner(System.in);
            //name
            String name = terminal_IO(scanner, "Supplier name");
            while (!service.ContainsSupplierName(name)) {
                System.out.println("Supplier is not exist. please enter exist supplier");
                name = terminal_IO(scanner, "Supplier name");
            }
            Supplier supplier = service.getSupplier(name);
            Hashtable<Integer, Integer> items = new Hashtable<>();
            init_items(scanner, items);
            service.addOrder(items, supplier.getSupplier_name());
            System.out.println("Order sent!");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes items by collecting their details from the user.
     *
     * @param scanner The Scanner object used to collect user input.
     * @param items   The Hashtable to store the item details (catalog ID and quantity).
     */
    private static void init_items(Scanner scanner,Hashtable<Integer , Integer> items) {
        String input="";

        while(!input.equals("0")){
            try {

                System.out.println("Enter new Item details. to stop enter 0");
                int supplier_catalogID = Integer.parseInt(terminal_IO(scanner, "Item's catalog id"));
                if (supplier_catalogID != 0) {
                    int quantity = Integer.parseInt(terminal_IO(scanner, "Item's quantity"));
                    try
                    {
                        items.put(supplier_catalogID , quantity);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                input = String.valueOf(supplier_catalogID);
            }
            catch (Exception e){
                System.out.println("invalid catalog id");
            }
        }
    }

    /**
     *
     * @param scanner
     * @param input
     * @return terminal response
     */
    public static String terminal_IO(Scanner scanner, String input) {
        System.out.println("Enter "+input);
        String name = scanner.nextLine();
        System.out.println(input+": "+name);
        return name;
    }
}
