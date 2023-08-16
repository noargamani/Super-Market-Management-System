package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.SpecificItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SpecificItemTest {
    LocalDate expiration1 = LocalDate.of(2023,6,16);
    LocalDate expiration2 = LocalDate.of(2023,4,1);
    SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, expiration1, false, "None", "Shelf");
    SpecificItem OrganicEggs = new SpecificItem(210, 15, 20, expiration2, true, "Broken","Shelf");
    @Test
    void getID() {
        assertEquals(110,MilkTnuva.getID());
        assertEquals(210, OrganicEggs.getID());
    }

    @Test
    void getCostPrice() {
        assertEquals(8,MilkTnuva.getCostPrice());
        assertEquals(15, OrganicEggs.getCostPrice());
    }

    @Test
    void getSellingPrice() {
        assertEquals(10, MilkTnuva.getSellingPrice());
        assertEquals(20, OrganicEggs.getSellingPrice());
    }

    @Test
    void getExpiration() {
        assertEquals(expiration1, MilkTnuva.getExpiration());
        assertEquals(expiration2, OrganicEggs.getExpiration());
    }

    @Test
    void getDefective() {
        assertFalse(MilkTnuva.getDefective());
        assertTrue(OrganicEggs.getDefective());
    }

    @Test
    void getLocation() {
        assertEquals("Shelf", MilkTnuva.getLocation());
        assertEquals("Shelf", OrganicEggs.getLocation());
    }

    @Test
    void getDefectType() {
        assertEquals("None", MilkTnuva.getDefectType());
        assertEquals("Broken", OrganicEggs.getDefectType());
    }

    @Test
    void isExpired() {
        assertFalse(MilkTnuva.isExpired());
        assertTrue(OrganicEggs.isExpired());
    }
}