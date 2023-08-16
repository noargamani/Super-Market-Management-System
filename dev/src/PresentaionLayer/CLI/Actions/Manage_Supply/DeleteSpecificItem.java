package Presentaion_layer.CLI.Actions.Manage_Supply;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.Scanner;

public class DeleteSpecificItem {

    /**
     * Deletes a specific item from the inventory.
     *
     * @param service The InventoryService instance used to delete the specific item.
     * @return An Item.NeedOrderException object if the specific item deletion triggered the need for an order, or null otherwise.
     */
    public static Item.NeedOrderException DeleteSpecificItem(InventoryService service) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Insert the name of the item:");
        String itemName = sc.nextLine();
        System.out.println("Insert the manufacturer of the item:");
        String itemManufacturer = sc.nextLine();
        int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);

        System.out.println("Insert the ID of the Specific item:");
        int ID = sc.nextInt();

        try {
            if(service.DeleteSpecificItem(ID, myCatalogNum))
                System.out.println("Specific Item deleted successfully");
            else
                System.out.println("This Specific Item is not found in the store");
        } catch (Item.NeedOrderException e){
            return e;
        }
        return null;
    }
}