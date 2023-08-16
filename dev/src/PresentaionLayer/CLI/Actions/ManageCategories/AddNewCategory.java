package Presentaion_layer.CLI.Actions.ManageCategories;

import Service_layer.InventoryService;

import java.util.Objects;
import java.util.Scanner;

public class AddNewCategory {

    /**
     * Adds a category to the inventory.
     *
     * @param service The InventoryService instance used to add the category.
     */
    public static void Add(InventoryService service) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert category ID:");
        int categoryID = sc.nextInt();
        System.out.println("Insert category name:");
        sc.nextLine();
        String categoryName = sc.nextLine();
        System.out.println("There is father category? Yes/No");
        String answer = sc.nextLine();
        if (Objects.equals(answer, "Yes")) {
            System.out.println("Insert category ID:");
            int fatherCategoryID = sc.nextInt();

            try {
                service.addSubCategory(categoryID, categoryName, fatherCategoryID);
            } catch (Exception e){
                System.out.println("This category ID is already exists");
            }
            System.out.println("Category added");
        } else if (Objects.equals(answer, "No")) {

            boolean success4=true;
            try {
                service.addFatherCategory(categoryID, categoryName);
            } catch (Exception e){
                System.out.println("This category ID is already exists");
                success4=false;
            }

            if(success4)
                System.out.println("Category added successfully");
        } else
            System.out.println("This option not exist");
    }
}
