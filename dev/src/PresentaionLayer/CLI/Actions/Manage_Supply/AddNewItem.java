package Presentaion_layer.CLI.Actions.Manage_Supply;

import java.util.Scanner;

import Service_layer.InventoryService;

public class AddNewItem {

    /**
     * Add new item to the system
     */
    public static void Add(InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert item name:");
        String itemName = sc.nextLine();
        System.out.println("Insert manufacturer:");
        String manufacturer = sc.nextLine();
        System.out.println("Inset minimum amount of the item:");
        int minimumAmount = sc.nextInt();
        System.out.println("Inset price of the item:");
        double price = sc.nextDouble();
        boolean success=true;
        try {
            service.AddNewItem(itemName, manufacturer, minimumAmount, price);
        } catch (IllegalArgumentException e){
            System.out.println("This Item is already exist in the store");
            success=false;
        }

        if(success)
            System.out.println("Item added successfully");
    }
}