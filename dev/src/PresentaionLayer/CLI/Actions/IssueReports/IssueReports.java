package Presentaion_layer.CLI.Actions.IssueReports;

import Service_layer.InventoryService;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class IssueReports {

    /**
     * Issues reports based on user input.
     *
     * @param service The InventoryService instance used to generate reports.
     */
    public static void Repost(InventoryService service){
        System.out.println("Issue reports");
        System.out.println("Which report would you like to issue?");
        System.out.println("1 : Report for all items in stock");
        System.out.println("2 : Report for all missing items");
        System.out.println("3 : Defective report");
        System.out.println("4 : Report by category");
        System.out.println("5 : Discount report");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        switch (input) {
            case 1:
                int reportID1;
                sc.nextLine();
                System.out.println("Insert report ID:");
                reportID1 = sc.nextInt();
                service.TotalItemReport(reportID1);
                break;
            case 2:
                int reportID2;
                sc.nextLine();
                System.out.println("Insert report ID:");
                reportID2 = sc.nextInt();
                service.MissingItemReport(reportID2);
                break;
            case 3:
                int reportID3;
                sc.nextLine();
                System.out.println("Insert report ID:");
                reportID3 = sc.nextInt();
                service.DefectiveReport(reportID3);
                break;
            case 4:
                int reportID4;
                sc.nextLine();
                System.out.println("Insert report ID:");
                reportID4 = sc.nextInt();
                String answer = "Yes";
                int categoryID;
                sc.nextLine();
                List<Integer> allCategoriesIDs = new LinkedList<>();
                while (!Objects.equals(answer, "No")) {
                    System.out.println("Insert category ID:");
                    categoryID = sc.nextInt();
                    try{
                        service.checkCategoryByID(categoryID);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        break;
                    }
                    allCategoriesIDs.add(categoryID);
                    System.out.println("Would you like to add more categories to the report? Yes/No");
                    answer = sc.next();
                    if(!Objects.equals(answer, "No") && !Objects.equals(answer, "Yes")) {
                        System.out.println("This option not exist");
                        System.out.println("Would you like to add more Categories to the report? Yes/No");
                        answer = sc.nextLine();
                    }
                }
                service.CategoryReport(reportID4, allCategoriesIDs);
                break;
            case 5:
                int reportID5;
                sc.nextLine();
                System.out.println("Insert report ID:");
                reportID5 = sc.nextInt();
                if(service.AllDiscountReport(reportID5).size() == 0)
                    System.out.println("There is no discounts");
                break;
            default:
                System.out.println("This option not exist");
                break;
        }
    }
}