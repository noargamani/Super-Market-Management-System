package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.InventoryItem;
import BusinessLayer.Inventory.Classes.SpecificItem;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;
import DataAcessLayer.DAO.Inventory.CategoryDAO;
import DataAcessLayer.DAO.Inventory.DiscountDAO;
import DataAcessLayer.DAO.Inventory.InventoryItemDAO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    SpecificItemDAO specificItemDAO = new SpecificItemDAO();
    InventoryItemDAO inventoryItemDAO = new InventoryItemDAO();
    CategoryDAO categoryDAO = new CategoryDAO();
    DiscountDAO discountDAO = new DiscountDAO();

    @Test
    void getCatalogNumber() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate expiration1 = LocalDate.of(2023,4,16);
        InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
        InventoryItem Item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 20);

        assertEquals(1, Item1.getCatalogNumber());
        assertEquals(2, Item2.getCatalogNumber());
    }

    @Test
    void getName() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate expiration1 = LocalDate.of(2023,4,16);
        InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
        InventoryItem Item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 20);

        assertEquals("Milk Tnuva 3%", Item1.getName());
        assertEquals("Organic eggs size L", Item2.getName());
    }

    @Test
    void getManufacturer() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate expiration1 = LocalDate.of(2023,4,16);
        InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
        InventoryItem Item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 20);

        assertEquals("Tnuva", Item1.getManufacturer());
        assertEquals("FreeChicken", Item2.getManufacturer());
    }

    @Test
    void getTotalAmount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate expiration1 = LocalDate.of(2023,4,16);
        InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
        InventoryItem Item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 20);

        SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, expiration1, false, "None", "shelf");
        Item1.AddSpecificItem(MilkTnuva, specificItemDAO);
        assertEquals(1, Item1.getTotalAmount());
        assertEquals(0, Item2.getTotalAmount());
    }


    @Test
    void getMinimumAmount() {
        try {
            Repo.createTables(Repo.connect());
            inventoryItemDAO.removeAll();
            categoryDAO.removeAll();
            discountDAO.removeAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        LocalDate expiration1 = LocalDate.of(2023,4,16);
        InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
        InventoryItem Item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 20);

        assertEquals(5, Item1.getMinimumAmount());
        assertEquals(5, Item2.getMinimumAmount());
    }

}