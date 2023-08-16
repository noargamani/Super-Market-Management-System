package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.Category;
import BusinessLayer.Inventory.Classes.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    List<Category> testList = new LinkedList<>();
    List<InventoryItem> inventoryItemInventoryList = new LinkedList<>();
    Category category1 = new Category(1, "Dairy products");
    Category category2 = new Category(2, "Milk", category1);
    Category category3 = new Category(3, "750 ml", category2);
    InventoryItem inventoryItemInventory1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
    InventoryItem inventoryItemInventory2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);

    @Test
    void getCategoryID() {
        assertEquals(1, category1.getCategoryID());
        assertEquals(2, category2.getCategoryID());
        assertEquals(3, category3.getCategoryID());
    }

    @Test
    void getCategoryName() {
        assertEquals("Dairy products", category1.getCategoryName());
        assertEquals("Milk", category2.getCategoryName());
        assertEquals("750 ml", category3.getCategoryName());

    }

    @Test
    void getFatherCategory() {
        assertNull(category1.getFatherCategory());
        assertEquals(category1, category2.getFatherCategory());
        assertEquals(category2, category3.getFatherCategory());
    }

    @Test
    void getSubCategories() {
        testList.add(category2);
        testList.add(category3);

        category1.setSubCategories(testList);
        assertEquals(testList, category1.getSubCategories());

    }

    @Test
    void getItems() {
        inventoryItemInventoryList.add(inventoryItemInventory1);
        inventoryItemInventoryList.add(inventoryItemInventory2);

        category1.setItems(inventoryItemInventoryList);
        assertEquals(inventoryItemInventoryList, category1.getItems());
    }
}