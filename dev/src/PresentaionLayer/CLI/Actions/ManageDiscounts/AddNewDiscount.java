package Presentaion_layer.CLI.Actions.ManageDiscounts;

import BusinessLayer.Item;
import Service_layer.InventoryService;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddNewDiscount {

    /**
     * Adds a new discount to the inventory.
     *
     * @param service The InventoryService instance used to add the discount.
     */
    public static void Add(InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert sale number:");
        int saleNumber = sc.nextInt();
        System.out.println("Insert sale name:");
        sc.nextLine();
        String saleName = sc.nextLine();
        System.out.println("Insert discount:");
        double discount = sc.nextDouble();
        System.out.println("Insert start date: dd/mm/yyyy");

        sc.nextLine();
        String start = sc.nextLine();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate startDate = LocalDate.parse(start, formatter1);

        System.out.println("Insert end date: dd/mm/yyyy");
        String end = sc.nextLine();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate endDate = LocalDate.parse(end, formatter2);
        System.out.println("1 : Add new discount by item");
        System.out.println("2 : Add new discount by category");
        int subInput = sc.nextInt();
        if (subInput == 1) {
            List<Integer> catalogNumbers = new LinkedList<>();

            System.out.println("Insert the name of the item:");
            sc.nextLine();
            String itemName = sc.nextLine();
            System.out.println("Insert the manufacturer of the item:");
            String itemManufacturer = sc.nextLine();
            int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
            catalogNumbers.add(myCatalogNum);

            service.addItemDiscount(saleNumber, saleName, discount, startDate, endDate, catalogNumbers);
            System.out.println("Discount added successfully");
        } else if (subInput == 2) {
            List<Integer> categoriesID = new LinkedList<>();
            String answer = "Yes";
            int categoryID = 0;
            while (!Objects.equals(answer, "No")) {
                System.out.println("Insert category ID:");
                categoryID = sc.nextInt();
                categoriesID.add(categoryID);
                System.out.println("Would you like to add more categories to the discount? Yes/No");
                sc.nextLine();
                answer = sc.nextLine();
                if(!Objects.equals(answer, "No") && !Objects.equals(answer, "Yes")) {
                    System.out.println("This option not exist");
                    System.out.println("Would you like to add more categories to the discount? Yes/No");
                    answer = sc.nextLine();
                }
            }
            service.addCategoryDiscount(saleNumber, saleName, discount, startDate, endDate, categoriesID);
            System.out.println("Discount added successfully");
        } else
            System.out.println("This option not exist");
    }
}