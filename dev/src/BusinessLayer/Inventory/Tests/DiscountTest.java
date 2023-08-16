package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.Discount;
import BusinessLayer.Inventory.Classes.InventoryItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    LocalDate startDate1 = LocalDate.of(2023,4,1);
    LocalDate endDate1 = LocalDate.of(2023,4,16);
    LocalDate startDate2 = LocalDate.of(2023,4,25);
    LocalDate endDate2 = LocalDate.of(2023,4,26);

    List<InventoryItem> testList1 = new LinkedList<>();
    List<InventoryItem> testList2 = new LinkedList<>();

    Discount discount1 = new Discount(1, "Passover", 50, startDate1, endDate1, testList1);
    Discount discount2 = new Discount(2, "Independence Day", 30, startDate2, endDate2, testList2);
    @Test
    void getSaleName() {
        assertEquals("Passover", discount1.getSaleName());
        assertEquals("Independence Day", discount2.getSaleName());
    }

    @Test
    void getSaleNumber() {
        assertEquals(1, discount1.getSaleNumber());
        assertEquals(2, discount2.getSaleNumber());
    }

    @Test
    void getDiscount() {
        assertEquals(50, discount1.getDiscount());
        assertEquals(30, discount2.getDiscount());
    }

    @Test
    void getStartDate() {
        assertEquals(startDate1, discount1.getStartDate());
        assertEquals(startDate2, discount2.getStartDate());
    }

    @Test
    void getEndDate() {
        assertEquals(endDate1, discount1.getEndDate());
        assertEquals(endDate2, discount2.getEndDate());
    }

    @Test
    void getIncludeInDiscount() {
        InventoryItem inventoryItemInventory1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
        InventoryItem inventoryItemInventory2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);

        testList1.add(inventoryItemInventory1);
        testList1.add(inventoryItemInventory2);

        testList2.add(inventoryItemInventory1);

        assertEquals(testList1, discount1.getIncludeInDiscount());
        assertEquals(testList2, discount2.getIncludeInDiscount());
    }
}