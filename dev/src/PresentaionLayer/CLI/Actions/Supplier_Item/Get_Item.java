package Presentaion_layer.CLI.Actions.Supplier_Item;

import BusinessLayer.Suppliers.Classes.SupplierItem;
import Service_layer.Suppliers_Service;

import java.util.Scanner;

public class Get_Item {

    /**
     * Retrieves details of an item from the supplier's inventory.
     *
     * @param service      The Suppliers_Service object.
     * @param supplierName The name of the supplier.
     */
    public static void Get(Suppliers_Service service , String supplierName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Item's suppliers");
        String name=terminal_IO(scanner,"Item name");
        String man=terminal_IO(scanner,"Item Manufacture");
        SupplierItem supplierItem = service.getItem(name , man , supplierName);

        String itemMenu="get item's details\n" +
                "1.itemId\n" +
                "2.Item Supplier Info\n" +
                "3.Item Name\n" +
                "4.Item discount by quantity:\n" +
                "5.Item Code\n" +
                "6.Item ListPrice\n" +
                "7.Item TOTAL INFO";

        System.out.println(itemMenu);
        try{
            String input=terminal_IO(scanner,"<field>");
            int request=Integer.parseInt(input);
            if(request==1){
                System.out.println("Item ID: "+ supplierItem.getCatalogNumber());
            }
            else if(request==5){
                System.out.println("Item Code:"+ supplierItem.getItemCode());
            }
            else if(request==2){
                System.out.println("Item Supplier Info:"+ supplierItem.getSupplier().toString()+ " " + supplierItem.getSupplier_catalogID());
            }
            else if(request==3){
                System.out.println("Item Name:"+ supplierItem.getName());
            }
            else if(request==4){
                System.out.println("Item discount by quantity:"+ supplierItem.getDiscountByQuantity());
            }
            else if(request==6){
                System.out.println("Item ListPrice:"+ supplierItem.getItem_list_price());
            }
            else if(request==7){
                System.out.println("Item TOTAL INFO:"+ supplierItem.toString());
            }
            else
            {
                System.out.println("Invalid request");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
