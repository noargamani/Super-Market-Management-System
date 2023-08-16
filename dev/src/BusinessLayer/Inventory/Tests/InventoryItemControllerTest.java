package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.*;
import BusinessLayer.Inventory.Controllers.*;
import BusinessLayer.Item;
import DataAcessLayer.DAO.Inventory.CategoryDAO;
import DataAcessLayer.DAO.Inventory.DiscountDAO;
import DataAcessLayer.DAO.Inventory.InventoryItemDAO;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemControllerTest {
    List<InventoryItem> inventoryItemInventoryList = new LinkedList<>();
    List<SpecificItem> specificItemList = new LinkedList<>();
    List<Discount> discountList = new LinkedList<>();
    List<Category> categoryList = new LinkedList<>();

    LocalDate expiration1 = LocalDate.of(2023,4,16);
    LocalDate expiration2 = LocalDate.of(2023,4,1);

    LocalDate startDate1 = LocalDate.of(2023,4,1);
    LocalDate endDate1 = LocalDate.of(2023,4,16);
    LocalDate startDate2 = LocalDate.of(2023,4,25);
    LocalDate endDate2 = LocalDate.of(2023,8,26);

    InventoryItem inventoryItem1 = new InventoryItem(0, "Milk Tnuva 3%", "Tnuva", 5 , 8);
    InventoryItem inventoryItem2 = new InventoryItem(1,"Organic eggs size L", "FreeChicken", 5 , 7);
    InventoryItem inventoryItem3 = new InventoryItem(2, "White bread","Angel",3 , 5);

    SpecificItem MilkTnuva;
    SpecificItem OrganicEggs;
    SpecificItem WhiteBread;

    List<InventoryItem> discountList1 = new LinkedList<>();
    List<InventoryItem> discountList2 = new LinkedList<>();

    Discount discount1 = new Discount(16, "Passover", 50, startDate1, endDate1, discountList1);
    Discount discount2 = new Discount(23, "Independence Day", 30, startDate2, endDate2, discountList2);

    Category category1 = new Category(1, "Dairy products");
    Category category2 = new Category(2, "Milk", category1);
    Category category3 = new Category(3, "750 ml", category2);

    InventoryItemDAO inventoryItemDAO = new InventoryItemDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    DiscountDAO discountDAO = new DiscountDAO();
    SpecificItemDAO specificItemDAO = new SpecificItemDAO();
    ItemController itemController = new ItemController(inventoryItemDAO, categoryDAO, discountDAO, specificItemDAO);
    @Test
    void getTotalItem() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder getTotalItemTest1 = new StringBuilder();

        inventoryItemInventoryList.add(inventoryItem1);
        inventoryItemInventoryList.add(inventoryItem2);
        inventoryItemInventoryList.add(inventoryItem3);

        for (InventoryItem inventoryItemInventory : inventoryItemInventoryList) {
            getTotalItemTest1.append(inventoryItemInventory.printInfo()).append("\n");
        }

        StringBuilder getTotalItemTest2 = new StringBuilder();
        itemController.AddNewItem("Milk Tnuva 3%", "Tnuva", 5, 7);
        itemController.AddNewItem("Organic eggs size L", "FreeChicken", 5, 7);
        itemController.AddNewItem("White bread","Angel",3 , 7);

        for(int i=0; i<itemController.getTotalItem().size(); i++){
            getTotalItemTest2.append(itemController.getTotalItem().get(i).printInfo()).append("\n");
        }

        assertEquals(getTotalItemTest1.toString(),getTotalItemTest2.toString());
    }
    @Test
    void getCategoryList() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder getCategoryListTest1 = new StringBuilder();
        StringBuilder getCategoryListTest2 = new StringBuilder();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        for (Category category: categoryList){
            getCategoryListTest1.append(category.printInfo()).append("\n");
        }

        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        itemController.addSubCategory(category2.getCategoryID(), category2.getCategoryName(), category3.getCategoryID());
        itemController.addFatherCategory(category3.getCategoryID(), category3.getCategoryName());

        for (Category category: itemController.getCategoryList()){
            getCategoryListTest2.append(category.printInfo()).append("\n");
        }

        assertEquals(getCategoryListTest1.toString(), getCategoryListTest2.toString());
    }

    @Test
    void getDiscountList() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder getDiscountListTest1 = new StringBuilder();
        StringBuilder getDiscountListTest2 = new StringBuilder();

        discountList.add(discount1);
        discountList.add(discount2);

        for(Discount discount: discountList){
            getDiscountListTest1.append(discount.printInfo()).append("\n");
        }

        List<Integer> catalogNumbers1= new ArrayList<>();
        List<Integer> catalogNumbers2= new ArrayList<>();

        itemController.addItemDiscount(11, "Passover", 50, startDate1, endDate1, catalogNumbers1);
        itemController.addItemDiscount(12, "Independence Day", 30, startDate2, endDate2,catalogNumbers2);

        for (Discount discount: itemController.getDiscountList()){
            getDiscountListTest2.append(discount.printInfo()).append("\n");
        }

        assertEquals(getDiscountListTest1.toString(), getDiscountListTest2.toString());
    }
    @Test
    void addNewItem() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        String addNewItemTest1 ="Catalog Number: 0 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0";

        itemController.AddNewItem( "Milk Tnuva 3%", "Tnuva", 0, 6);
        int catalogNumber = Item.getItemCatalogNumber("Milk Tnuva 3%", "Tnuva");
        String addNewItemTest2 = itemController.FindMyItem(catalogNumber).printInfo();

        assertEquals(addNewItemTest1, addNewItemTest2);
    }

    @Test
    void deleteItem() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        itemController.AddNewItem( "Milk Tnuva 3%", "Tnuva", 0, 6);
        int catalogNumber = Item.getItemCatalogNumber("Milk Tnuva 3%", "Tnuva");
        itemController.DeleteItem(catalogNumber);
        assertEquals(0,itemController.getTotalItem().size());
    }

    @Test
    void findMyItem() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        String findMyItemTest1 ="Catalog Number: 0 Item Name: Milk Tnuva 3% Manufacturer: Tnuva Total Amount: 0 Amount On Shelves: 0 Amount In Warehouse: 0";
        itemController.AddNewItem( "Milk Tnuva 3%", "Tnuva", 0, 6);
        int catalogNumber = Item.getItemCatalogNumber("Milk Tnuva 3%", "Tnuva");
        String findMyItemTest2 = itemController.FindMyItem(catalogNumber).printInfo();

        assertEquals(findMyItemTest1, findMyItemTest2);
    }

    @Test
    void addFatherCategory() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder addFatherCategoryTest1 = new StringBuilder();
        categoryList.add(category1);
        for (Category category: categoryList){
            addFatherCategoryTest1.append(category.printInfo()).append("\n");
        }

        StringBuilder addFatherCategoryTest2 = new StringBuilder();
        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());

        for (Category category: itemController.getCategoryList()){
            addFatherCategoryTest2.append(category.printInfo()).append("\n");
        }

        assertEquals(addFatherCategoryTest1.toString(), addFatherCategoryTest2.toString());
    }

    @Test
    void addSubCategory() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        StringBuilder addSubCategoryTest1 = new StringBuilder();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        for (Category category: categoryList){
            addSubCategoryTest1.append(category.printInfo()).append("\n");
        }

        StringBuilder addSubCategoryTest2 = new StringBuilder();
        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        itemController.addSubCategory(category2.getCategoryID(), category2.getCategoryName(), category3.getCategoryID());
        itemController.addFatherCategory(category3.getCategoryID(), category3.getCategoryName());

        for (Category category: itemController.getCategoryList()){
            addSubCategoryTest2.append(category.printInfo()).append("\n");
        }

        assertEquals(addSubCategoryTest1.toString(), addSubCategoryTest2.toString());
    }

    @Test
    void updateCategory() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        itemController.updateCategory(category1.getCategoryID(), category2.getCategoryName());

        assertEquals(itemController.FindCategoryByID(category1.getCategoryID()).getCategoryName(), category2.getCategoryName());
    }

    @Test
    void findCategoryByName() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        category1.AddItemToCategory(inventoryItem1);

        StringBuilder findCategoryByNameTest1 = new StringBuilder();
        categoryList.add(category1);
        for (Category category: categoryList){
            findCategoryByNameTest1.append(category.printInfo()).append("\n");
        }

        StringBuilder findCategoryByNameTest2 = new StringBuilder();
        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        Category controllerCategory = itemController.FindCategoryByID(category1.getCategoryID());
        controllerCategory.AddItemToCategory(inventoryItem1);
        findCategoryByNameTest2.append(controllerCategory.printInfo()).append("\n");
        assertEquals(findCategoryByNameTest1.toString(), findCategoryByNameTest2.toString());
    }

    @Test
    void addItemDiscount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        String addItemDiscountTest1 = "Sale Name: Passover Discount: 50.0 Start Date: 2023-04-01 End Date: 2023-04-16\n";
        StringBuilder addItemDiscountTest2 = new StringBuilder();
        List<Integer> catalogNumbers= new ArrayList<>();
        itemController.addItemDiscount(20, "Passover", 50, startDate1, endDate1, catalogNumbers);
        for(Discount discount: itemController.getDiscountList()){
            addItemDiscountTest2.append(discount.printInfo()).append("\n");
        }
        assertEquals(addItemDiscountTest1, addItemDiscountTest2.toString());
    }

    @Test
    void addCategoryDiscount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        String addCategoryDiscountTest1 = "Sale Name: Passover Discount: 30.5 Start Date: 2023-04-01 End Date: 2023-04-16\n";
        StringBuilder addCategoryDiscountTest2 = new StringBuilder();
        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        itemController.AddNewItem( inventoryItem1.getName(), inventoryItem1.getManufacturer(), inventoryItem1.getMinimumAmount(), inventoryItem1.getPrice());
        category1.AddItemToCategory(inventoryItem1);
        List<Integer> categoryIDList= new ArrayList<>();
        categoryIDList.add(category1.getCategoryID());
        itemController.addCategoryDiscount(3, "Passover", 30.5, startDate1, endDate1, categoryIDList);
        for(Discount discount: itemController.getDiscountList()){
            addCategoryDiscountTest2.append(discount.printInfo()).append("\n");
        }
        assertEquals(addCategoryDiscountTest1, addCategoryDiscountTest2.toString());
    }

    @Test
    void findCategoryByID() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        category1.AddItemToCategory(inventoryItem1);

        StringBuilder findCategoryByIDTest1 = new StringBuilder();
        categoryList.add(category1);
        for (Category category: categoryList){
            findCategoryByIDTest1.append(category.printInfo()).append("\n");
        }

        StringBuilder findCategoryByIDTest2 = new StringBuilder();
        itemController.addFatherCategory(category1.getCategoryID(), category1.getCategoryName());
        Category controllerCategory = itemController.FindCategoryByID(category1.getCategoryID());
        controllerCategory.AddItemToCategory(inventoryItem1);
        findCategoryByIDTest2.append(controllerCategory.printInfo()).append("\n");
        assertEquals(findCategoryByIDTest1.toString(), findCategoryByIDTest2.toString());
    }

    @Test
    void checkIfItemOnDiscount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        String checkIfItemOnDiscountTest1 = "Sale Name: Passover Discount: 50.0 Start Date: 2023-04-01 End Date: 2023-08-26";

        itemController.AddNewItem(inventoryItem1.getName(), inventoryItem1.getManufacturer(), inventoryItem1.getMinimumAmount(), inventoryItem1.getPrice());
        int catalogNumber = Item.getItemCatalogNumber(inventoryItem1.getName(), inventoryItem1.getManufacturer());
        List<Integer> catalogNumbers = new LinkedList<>();
        catalogNumbers.add(catalogNumber);
        itemController.addItemDiscount(1, "Passover", 50, startDate1, endDate2, catalogNumbers);

        Discount foundDiscount = itemController.CheckIfItemOnDiscount(catalogNumber);
        String checkIfItemOnDiscountTest2 = foundDiscount.printInfo();
        assertEquals(checkIfItemOnDiscountTest1, checkIfItemOnDiscountTest2);
        assertNull(itemController.CheckIfItemOnDiscount(2));
    }

    @Test
    void removeDiscount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        itemController.AddNewItem(inventoryItem1.getName(), inventoryItem1.getManufacturer(), inventoryItem1.getMinimumAmount(), inventoryItem1.getPrice());
        int catalogNumber = Item.getItemCatalogNumber(inventoryItem1.getName(), inventoryItem1.getManufacturer());
        List<Integer> catalogNumbers = new LinkedList<>();
        catalogNumbers.add(catalogNumber);
        itemController.addItemDiscount(7, "Passover", 50, endDate1, endDate1, catalogNumbers);
        itemController.removeDiscount();
        assertNull(itemController.CheckIfItemOnDiscount(1));
    }
}