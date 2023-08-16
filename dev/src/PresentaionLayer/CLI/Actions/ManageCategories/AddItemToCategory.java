package Presentaion_layer.CLI.Actions.ManageCategories;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.Scanner;

public class AddItemToCategory {

    /**
     * Adds an item to a category in the inventory.
     *
     * @param service The InventoryService instance used to add the item to the category.
     */
    public static void Add(InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert category ID:");
        int categoryID = sc.nextInt();

        try{
            service.FindCategoryByID(categoryID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        sc.nextLine();

        System.out.println("Insert the name of the item:");
        String itemName = sc.nextLine();
        System.out.println("Insert the manufacturer of the item:");
        String itemManufacturer = sc.nextLine();
        int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
        try{
            service.FindMyItem(myCatalogNum);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        if(service.AddItemToCategory(categoryID, myCatalogNum))
            System.out.println("Item added successfully");
    }
}