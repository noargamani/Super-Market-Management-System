package Presentaion_layer.CLI.Actions.Supplier_Item;

import Service_layer.Suppliers_Service;

import java.util.*;

public class Add_Item {

    /**
     * Adds a new item to the supplier's inventory in the Suppliers_Service.
     *
     * @param service The Suppliers_Service object.
     */
    public static void Add(Suppliers_Service service){
        Scanner scanner = new Scanner(System.in);
        //name
        String name=terminal_IO(scanner,"Supplier name");
        while (!service.ContainsSupplierName(name)){
            System.out.println("Supplier is not exist. please enter exist supplier");
            name=terminal_IO(scanner,"Supplier name");
        }
        try {

            String itemName = terminal_IO(scanner, "Item name");
            String itemManufacture = terminal_IO(scanner, "Item manufacture");
            int supplier_catalogID = Integer.parseInt(terminal_IO(scanner, "Item's catalog id"));
            int quantity = Integer.parseInt(terminal_IO(scanner, "Item's quantity"));
            double price=Double.parseDouble(terminal_IO(scanner, "Item's price"));

            String input= terminal_IO(scanner, "Expiration date, in format <year> <month> <day> ");
            String[] split=input.split("\\s");
            ArrayList<Integer> times=new ArrayList<>();
            for(String s:split){
                times.add(Integer.valueOf(s));
            }
            Date date = new Date(times.get(0),times.get(1),times.get(2));

            SortedMap<Integer,Integer> q_to_Discount=new TreeMap<>();
            init_Discounts(scanner,q_to_Discount);
            service.addItem(name,itemName, itemManufacture ,supplier_catalogID,price,q_to_Discount,quantity,date);
            System.out.println("Item saved");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Initializes the discounts for items based on user input.
     *
     * @param scanner      The Scanner object to read user input.
     * @param q_to_Discount The SortedMap to store the quantity-to-discount mappings.
     */
    private static void init_Discounts(Scanner scanner, SortedMap<Integer, Integer> q_to_Discount) {
       String input="";
        while(!input.equals( "0")) {
            System.out.println("Enter all Discounts in this format <quantity> <discount>. to stop enter 0");
            input=terminal_IO(scanner,"Discounts");
            if(!input.equals("0")) {
                try {


                    String[] split = input.split("\\s");

                    try {
                        int quantity = Integer.parseInt(split[0]);
                        int discount = Integer.parseInt(split[1]);
                        q_to_Discount.put(quantity, discount);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                catch (Exception e){
                    System.out.println("Invalid input");
                }
            }
        }
    }

    /**
     * Reads user input from the terminal and returns the response.
     *
     * @param scanner The Scanner object to read user input.
     * @param input   The prompt message for the user input.
     * @return The terminal response provided by the user.
     */
    public static String terminal_IO(Scanner scanner, String input) {
        System.out.println("Enter "+input);
        String name = scanner.nextLine();
        System.out.println(input+": "+name);
        return name;
    }
}
