package Presentaion_layer.CLI.Actions.Manage_Supply;
import Service_layer.InventoryService;

import java.util.Objects;
import java.util.Scanner;
public class UpdateDefectiveStatus {

    /**
     * Updates the defective status of an item in the inventory.
     *
     * @param service The InventoryService instance used to update the defective status.
     */
    public static void Update(InventoryService service){
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert the catalog number of the item:");
        int myCatalogNum = sc.nextInt();
        System.out.println("Insert the ID of the item:");
        int itemID = sc.nextInt();

        boolean flag=false;
        boolean Defective = false;
        String defectType = "None";

        while(!flag) {
            System.out.println("Is the item defective? Yes/No");
            sc.nextLine();
            String isDefective = sc.nextLine();

            if (Objects.equals(isDefective, "Yes")) {
                System.out.println("Insert the defect type:");
                defectType = sc.nextLine();
                Defective = true;
                flag=true;

            } else if (Objects.equals(isDefective, "No")) {
                defectType = "None";
                Defective = false;
                flag=true;
            } else
                System.out.println("This option not exist");
        }

        try{
            service.UpdateDefectiveStatus(myCatalogNum, itemID, Defective, defectType);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Defective status updated successfully");
    }
}
