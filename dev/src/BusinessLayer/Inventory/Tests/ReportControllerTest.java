package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Controllers.*;
import BusinessLayer.Inventory.Controllers.ReportController;
import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DAO.Inventory.*;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportControllerTest {

    List<InventoryItem> inventoryItemInventoryList = new LinkedList<>();
    List<InventoryItem> missingItemsList = new LinkedList<>();
    List<SpecificItem> specificItemList = new LinkedList<>();
    List<Discount> discountList = new LinkedList<>();
    List<Category> categoryList = new LinkedList<>();

    LocalDate expiration1 = LocalDate.of(2023,6,16);
    LocalDate expiration2 = LocalDate.of(2023,6,1);

    LocalDate startDate1 = LocalDate.of(2023,4,1);
    LocalDate endDate1 = LocalDate.of(2023,4,16);
    LocalDate startDate2 = LocalDate.of(2023,4,25);
    LocalDate endDate2 = LocalDate.of(2023,4,26);

    InventoryItem inventoryItemInventory1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
    InventoryItem inventoryItemInventory2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);
    InventoryItem inventoryItemInventory3 = new InventoryItem(3, "White bread","Angel",3, 5.5);

    SpecificItem MilkTnuva;
    SpecificItem OrganicEggs;
    SpecificItem WhiteBread;

    List<InventoryItem> discountList1 = new LinkedList<>();
    List<InventoryItem> discountList2 = new LinkedList<>();

    Discount discount1 = new Discount(1, "Passover", 50, startDate1, endDate1, discountList1);
    Discount discount2 = new Discount(2, "Independence Day", 30, startDate2, endDate2, discountList2);
    Category category1 = new Category(1, "Dairy products");
    Category category2 = new Category(2, "Milk", category1);
    Category category3 = new Category(3, "750 ml", category2);

    InventoryItemDAO inventoryItemDAO = new InventoryItemDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    DiscountDAO discountDAO = new DiscountDAO();
    ReportDAO reportDAO = new ReportDAO();
    SpecificItemDAO specificItemDAO = new SpecificItemDAO();
    ItemController itemController = new ItemController(inventoryItemDAO, categoryDAO, discountDAO, specificItemDAO);
    ReportController reportController = new ReportController(reportDAO);

    @Test
    void TotalItemReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);
        inventoryItemInventoryList.add(inventoryItemInventory3);
        String TotalItemReportTest = "Catalog Number: 1 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Catalog Number: 2 Item Name: Organic eggs size L Manufacturer: FreeChicken Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Catalog Number: 3 Item Name: White bread Manufacturer: Angel Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n";
        assertEquals(TotalItemReportTest, reportController.TotalItemReport(2, inventoryItemInventoryList).getInfo());
    }

    @Test
    void missingItemReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);
        inventoryItemInventoryList.add(inventoryItemInventory3);

        for(int i=0; i<10; i++) {
            // Milk Tnuva 3% X 10
            MilkTnuva = new SpecificItem(110+i, 8, 10, expiration1, false, "None", "Shelf");
            inventoryItemInventory1.AddSpecificItem(MilkTnuva, specificItemDAO);

            // Organic Eggs size L X 10
            OrganicEggs = new SpecificItem(210+i, 15, 20, expiration1, false, "None","Shelf");
            inventoryItemInventory2.AddSpecificItem(OrganicEggs, specificItemDAO);
        }

        missingItemsList.add(inventoryItemInventory3);

        String missingItemReportTest = "Catalog Number: 3 Item Name: White bread Manufacturer: Angel Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n";
        assertEquals(missingItemReportTest, reportController.MissingItemReport(12, inventoryItemInventoryList).getInfo());
    }

    @Test
    void printItemsInCategory() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);

        category1.setItems(inventoryItemInventoryList);

        String ItemsInCategoryReportTest = "Dairy products:\n" +
                "Catalog Number: 1 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Catalog Number: 2 Item Name: Organic eggs size L Manufacturer: FreeChicken Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" ;

        assertEquals(ItemsInCategoryReportTest, reportController.PrintItemsInCategory(3, category1).getInfo());
    }

    @Test
    void categoryReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);
        inventoryItemInventoryList.add(inventoryItemInventory3);

        category1.AddItemToCategory(inventoryItemInventory1);
        category1.AddItemToCategory(inventoryItemInventory2);
        category2.AddItemToCategory(inventoryItemInventory3);

        String CategoryReportTest = "Dairy products:\n" +
                "Catalog Number: 1 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Catalog Number: 2 Item Name: Organic eggs size L Manufacturer: FreeChicken Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Milk:\n" +
                "Catalog Number: 3 Item Name: White bread Manufacturer: Angel Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "750 ml:\n"+
                "There are no items in the category\n";

        assertEquals(CategoryReportTest, reportController.CategoryReport(5, categoryList).getInfo());
    }

    @Test
    void defectiveReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);

        for(int i=0; i<10; i++) {
            // Milk Tnuva 3% X 10
            MilkTnuva = new SpecificItem(110+i, 8, 10, expiration1, false, "None", "Shelf");
            inventoryItemInventory1.AddSpecificItem(MilkTnuva, specificItemDAO);

            // Organic Eggs size L X 10
            OrganicEggs = new SpecificItem(210+i, 15, 20, expiration1, false, "None","Shelf");
            inventoryItemInventory2.AddSpecificItem(OrganicEggs, specificItemDAO);
        }

        OrganicEggs = new SpecificItem(220, 15, 20, expiration1, true, "Broken","Shelf");
        inventoryItemInventory2.AddSpecificItem(OrganicEggs, specificItemDAO);

        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        category1.setItems(inventoryItemInventoryList);

        String defectiveReportTest="Defective:\n" +
                "ID: 220 Name: Organic eggs size L Category: Dairy products Defect Type: Broken Is Expired: No Expiration: 2023-06-16\n" +
                "Expired:\n" +
                "There is no expired items\n";

        assertEquals(defectiveReportTest, reportController.DefectiveReport(6, inventoryItemInventoryList, categoryList).getInfo());

    }

    @Test
    void findCategory() {
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);
        inventoryItemInventoryList.add(inventoryItemInventory3);

        category1.AddItemToCategory(inventoryItemInventory1);
        category1.AddItemToCategory(inventoryItemInventory2);
        category2.AddItemToCategory(inventoryItemInventory1);

        assertEquals(category1, reportController.findCategory(inventoryItemInventory1,categoryList));
    }

    @Test
    void discountReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        discountList.add(discount1);
        discountList.add(discount2);

        discountList1.add(inventoryItemInventory1);
        discountList1.add(inventoryItemInventory2);

        discountList2.add(inventoryItemInventory3);

        String discountReportTest = "Catalog Number: 1 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n" +
                "Catalog Number: 2 Item Name: Organic eggs size L Manufacturer: FreeChicken Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0\n";

        //assertEquals(discountReportTest, reportController.DiscountReport("Passover",discountList).getInfo());

    }

    @Test
    void allDiscountReport() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
            reportDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }
        discountList.add(discount1);
        discountList.add(discount2);

        discountList1.add(inventoryItemInventory1);
        discountList1.add(inventoryItemInventory2);

        discountList2.add(inventoryItemInventory3);

        String allDiscountReportTest ="Sale Name: Passover Discount: 50.0 Start Date: 2023-04-01 End Date: 2023-04-16\n" +
                "Sale Name: Independence Day Discount: 30.0 Start Date: 2023-04-25 End Date: 2023-04-26\n";

        assertEquals(allDiscountReportTest,reportController.AllDiscountReport(9, discountList).getInfo());
    }
}