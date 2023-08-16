package DataAcessLayer.DAO.Tests;
import BusinessLayer.Inventory.Classes.InventoryItem;

import BusinessLayer.Inventory.Classes.SpecificItem;
import DataAcessLayer.DAO.Inventory.InventoryItemDAO;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;
import DataAcessLayer.DTO.Inventory.InventoryItemDTO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemDAOTest {


    @Test
    void addItem() {
        try {
            Repo.createTables(Repo.connect());
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            InventoryItem item = new InventoryItem(1, "Milk", "Tnuva", 20, 8.9);

            dao.addItem(item);
            InventoryItem returnItem = dao.getItemByCatalogNumber(1);
            assertEquals(item.getCatalogNumber(), returnItem.getCatalogNumber());
            assertEquals(item.getName(), returnItem.getName());
            assertEquals(item.getManufacturer(), returnItem.getManufacturer());
            assertEquals(item.getMinimumAmount(), returnItem.getMinimumAmount());
            assertEquals(item.getPrice(), returnItem.getPrice());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void buildItem() {
        try{
            Repo.createTables(Repo.connect());
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            InventoryItemDTO itemDTO = new InventoryItemDTO(252, "Apropo", "Osem", 30, 15, 30, 0, 5.5);

            InventoryItem item = dao.buildItem(itemDTO);
            assertEquals(item.getCatalogNumber(), 252);
            assertEquals(item.getName(), "Apropo");
            assertEquals(item.getManufacturer(), "Osem");
            assertEquals(item.getTotalAmount(), 30);
            assertEquals(item.getMinimumAmount(), 15);
            assertEquals(item.getWarehouseAmount(), 30);
            assertEquals(item.getShelvesAmount(), 0);
            assertEquals(item.getPrice(), 5.5);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getSpecificItemsByCatalogNumber() {
        try {
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            InventoryItem item = new InventoryItem(100, "Milk", "Tnuva", 20, 8.9);
            LocalDate expiration1 = LocalDate.of(2023,4,16);
            SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, expiration1, false, "None", "Shelf");
            item.AddSpecificItem(MilkTnuva,new SpecificItemDAO());
            dao.addItem(item);

            List<SpecificItem> specificItemList = dao.getSpecificItemsByCatalogNumber(100);
            if(!specificItemList.isEmpty()) {
                SpecificItem returnItem = specificItemList.get(0);
                assertEquals(MilkTnuva.getID(), returnItem.getID());
                assertEquals(MilkTnuva.getCostPrice(), returnItem.getCostPrice());
                assertEquals(MilkTnuva.getSellingPrice(), returnItem.getSellingPrice());
                assertEquals(MilkTnuva.getDefective(), returnItem.getDefective());
                assertEquals(MilkTnuva.getExpiration(), returnItem.getExpiration());
                assertEquals(MilkTnuva.getDefectType(), returnItem.getDefectType());
                assertEquals(MilkTnuva.getLocation(), returnItem.getLocation());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void updateItem() {
        try {
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            InventoryItem item = new InventoryItem(100, "Milk", "Tnuva", 20, 8.9);

            dao.addItem(item);

            item.setManufacturer("Tura");
            dao.updateItem(item);
            List<InventoryItem> items = dao.getAllItems();
            InventoryItem returnItem = items.get(0);
            assertEquals(item.getCatalogNumber(), returnItem.getCatalogNumber());
            assertEquals(item.getName(), returnItem.getName());
            assertEquals(item.getManufacturer(), returnItem.getManufacturer());
            assertEquals(item.getMinimumAmount(), returnItem.getMinimumAmount());
            assertEquals(item.getPrice(), returnItem.getPrice());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void remove() {
        try {
            Repo.createTables(Repo.connect());
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            InventoryItem item = new InventoryItem(1, "Milk", "Tnuva", 20, 8.9);

            dao.addItem(item);
            dao.remove(item);

            List<InventoryItem> items = dao.getAllItems();
            assertEquals(items.size(), 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getAllItems() {
        try {
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            InventoryItem item = new InventoryItem(100, "Milk", "Tnuva", 20, 8.9);

            dao.addItem(item);
            List<InventoryItem> items = dao.getAllItems();
            if(!items.isEmpty()) {
                InventoryItem returnItem = items.get(0);
                assertEquals(item.getCatalogNumber(), returnItem.getCatalogNumber());
                assertEquals(item.getName(), returnItem.getName());
                assertEquals(item.getManufacturer(), returnItem.getManufacturer());
                assertEquals(item.getMinimumAmount(), returnItem.getMinimumAmount());
                assertEquals(item.getPrice(), returnItem.getPrice());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getItemByCatalogNumber() {
        try {
            InventoryItemDAO dao = new InventoryItemDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            InventoryItem item = new InventoryItem(100, "Milk", "Tnuva", 20, 8.9);

            dao.addItem(item);
            InventoryItem returnItem = dao.getItemByCatalogNumber(100);
            assertEquals(item.getCatalogNumber(), returnItem.getCatalogNumber());
            assertEquals(item.getName(), returnItem.getName());
            assertEquals(item.getManufacturer(), returnItem.getManufacturer());
            assertEquals(item.getMinimumAmount(), returnItem.getMinimumAmount());
            assertEquals(item.getPrice(), returnItem.getPrice());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}