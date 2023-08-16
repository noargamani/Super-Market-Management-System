package Presentaion_layer.CLI.Actions.Manage_Supply;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.Scanner;

public class DeleteItem {

    /**
     * Deletes an item from the inventory.
     *
     * @param service The InventoryService instance used to delete the item.
     */
    public static void DeleteItem(InventoryService service){
        Scanner sc = new Scanner(System.in);

        System.out.println("Insert the name of the item:");
        String itemName = sc.nextLine();
        System.out.println("Insert the manufacturer of the item:");
        String itemManufacturer = sc.nextLine();
        int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);

        if(service.DeleteItem(myCatalogNum))
            System.out.println("Item deleted successfully");
        else
            System.out.println("This Item is not found in the store");
    }
}