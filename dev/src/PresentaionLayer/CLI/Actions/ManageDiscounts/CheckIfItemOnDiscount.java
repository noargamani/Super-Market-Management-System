package Presentaion_layer.CLI.Actions.ManageDiscounts;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.Scanner;

public class CheckIfItemOnDiscount {

    /**
     * Checks if an item is on discount in the inventory.
     *
     * @param service The InventoryService instance used to check the item.
     */
    public static void Check (InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert the name of the item:");
        String itemName = sc.nextLine();
        System.out.println("Insert the manufacturer of the item:");
        String itemManufacturer = sc.nextLine();
        int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
        service.CheckIfItemOnDiscount(myCatalogNum);
    }
}