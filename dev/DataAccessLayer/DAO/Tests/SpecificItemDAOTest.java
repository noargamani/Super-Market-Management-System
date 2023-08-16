package DataAcessLayer.DAO.Tests;

import BusinessLayer.Inventory.Classes.InventoryItem;
import BusinessLayer.Inventory.Classes.SpecificItem;
import DataAcessLayer.DAO.Inventory.SpecificItemDAO;
import DataAcessLayer.DTO.Inventory.SpecificItemDTO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecificItemDAOTest {

    LocalDate expiration1 = LocalDate.of(2023,4,16);
    InventoryItem Item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 10);
    SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, expiration1, false, "None", "Shelf");

    @Test
    void addSpecificItem() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            dao.addSpecificItem(MilkTnuva, Item1.getCatalogNumber());
            List<SpecificItem> items = dao.getAllItems();
            SpecificItem returnSpecificItem = items.get(0);

            assertEquals(MilkTnuva.getID(), returnSpecificItem.getID());
            assertEquals(MilkTnuva.getCostPrice(), returnSpecificItem.getCostPrice());
            assertEquals(MilkTnuva.getSellingPrice(), returnSpecificItem.getSellingPrice());
            assertEquals(MilkTnuva.getExpiration(), returnSpecificItem.getExpiration());
            assertEquals(MilkTnuva.getDefective(), returnSpecificItem.getDefective());
            assertEquals(MilkTnuva.getDefectType(), returnSpecificItem.getDefectType());
            assertEquals(MilkTnuva.getLocation(), MilkTnuva.getLocation());

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void deleteSpecificItem() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            dao.addSpecificItem(MilkTnuva, Item1.getCatalogNumber());
            List<SpecificItem> items = dao.getAllItems();
            assertEquals(items.size(), 1);

            dao.deleteSpecificItem(MilkTnuva, Item1.getCatalogNumber());
            List<SpecificItem> itemss = dao.getAllItems();
            assertEquals(itemss.size(), 0);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void buildSpecificItem() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            SpecificItemDTO specificItemDTO = new SpecificItemDTO(100,110, 8, 10, expiration1, false, "None", "Shelf",LocalDate.now());
            SpecificItem specificItem = dao.buildSpecificItem(specificItemDTO);

            assertEquals(MilkTnuva.getID(), specificItem.getID());
            assertEquals(MilkTnuva.getCostPrice(), specificItem.getCostPrice());
            assertEquals(MilkTnuva.getSellingPrice(), specificItem.getSellingPrice());
            assertEquals(MilkTnuva.getExpiration(), specificItem.getExpiration());
            assertEquals(MilkTnuva.getDefective(), specificItem.getDefective());
            assertEquals(MilkTnuva.getDefectType(), specificItem.getDefectType());
            assertEquals(MilkTnuva.getLocation(), MilkTnuva.getLocation());

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void updateSpecificItem() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            SpecificItem MilkTura = new SpecificItem(100, 8.0, 10.0, LocalDate.now(), true, "None", "Shelf");
            dao.addSpecificItem(MilkTura, Item1.getCatalogNumber());

            assertFalse(MilkTura.getDefective());
            assertEquals(MilkTura.getDefectType(), "None");

            MilkTura.setDefective(true);
            MilkTura.setDefectType("Open Carton");
            dao.updateSpecificItem(MilkTura.getID(),MilkTura);

            assertTrue(MilkTura.getDefective());
            assertEquals(MilkTura.getDefectType(), "Open Carton");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Test
    void getAllItems() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            dao.addSpecificItem(MilkTnuva, Item1.getCatalogNumber());
            List<SpecificItem> items = dao.getAllItems();

            if (!items.isEmpty()) {
                SpecificItem returnSpecificItem = items.get(0);

                assertEquals(MilkTnuva.getID(), returnSpecificItem.getID());
                assertEquals(MilkTnuva.getCostPrice(), returnSpecificItem.getCostPrice());
                assertEquals(MilkTnuva.getSellingPrice(), returnSpecificItem.getSellingPrice());
                assertEquals(MilkTnuva.getExpiration(), returnSpecificItem.getExpiration());
                assertEquals(MilkTnuva.getDefective(), returnSpecificItem.getDefective());
                assertEquals(MilkTnuva.getDefectType(), returnSpecificItem.getDefectType());
                assertEquals(MilkTnuva.getLocation(), MilkTnuva.getLocation());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getSpecificItemByID() {
        try {
            Repo.createTables(Repo.connect());
            SpecificItemDAO dao = new SpecificItemDAO();
            dao.removeAll();

            dao.addSpecificItem(MilkTnuva, Item1.getCatalogNumber());
            SpecificItem returnSpecificItem =dao.getSpecificItemByID(MilkTnuva.getID());

            assertEquals(MilkTnuva.getID(), returnSpecificItem.getID());
            assertEquals(MilkTnuva.getCostPrice(), returnSpecificItem.getCostPrice());
            assertEquals(MilkTnuva.getSellingPrice(), returnSpecificItem.getSellingPrice());
            assertEquals(MilkTnuva.getExpiration(), returnSpecificItem.getExpiration());
            assertEquals(MilkTnuva.getDefective(), returnSpecificItem.getDefective());
            assertEquals(MilkTnuva.getDefectType(), returnSpecificItem.getDefectType());
            assertEquals(MilkTnuva.getLocation(), MilkTnuva.getLocation());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}