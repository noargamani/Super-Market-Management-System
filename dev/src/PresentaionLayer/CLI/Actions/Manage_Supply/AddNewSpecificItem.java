package Presentaion_layer.CLI.Actions.Manage_Supply;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.Objects;
import java.util.Scanner;

public class AddNewSpecificItem {

    /**
     * Adds a specific item to the inventory.
     *
     * @param service The InventoryService instance used to add the specific item.
     */
    public static void Add(InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert the name of the item:");
        String itemName = sc.nextLine();
        System.out.println("Insert the manufacturer of the item:");
        String itemManufacturer = sc.nextLine();
        int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);

        System.out.println("Insert the ID of the item:");
        int itemID = sc.nextInt();

        try{
            service.FindSpecificItem(myCatalogNum, itemID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Insert item's cost price:");
        double costPrice = sc.nextDouble();
        System.out.println("Insert item's selling price:");
        double sellingPrice = sc.nextDouble();
        System.out.println("Insert expiration date: dd/mm/yyyy");
        sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String expiration = sc.nextLine();
        LocalDate expirationDate = LocalDate.parse(expiration, formatter);
        System.out.println("Is the item defective? Yes/No");
        String isDefective = sc.nextLine();
        boolean defective;
        String defectType;
        if (Objects.equals(isDefective, "Yes")) {
            defective = true;
            System.out.println("Insert the defect type:");
            defectType = sc.nextLine();
        } else if (Objects.equals(isDefective, "No")) {
            defective = false;
            defectType = "None";
        } else {
            System.out.println("This option not exist");
            return;
        }
        System.out.println("Insert the location of the item: Shelf/Warehouse");
        String location = sc.nextLine();
        if (!Objects.equals(location, "Shelf") && !Objects.equals(location, "Warehouse")) {
            System.out.println("This option not exist");
            return;
        }

        if(service.AddSpecificItem(myCatalogNum, itemID, costPrice, sellingPrice, expirationDate, defective, defectType, location)){
            System.out.println("Specific item added successfully");
        }
    }
}