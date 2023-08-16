package BusinessLayer.Inventory.Tests;

import BusinessLayer.Inventory.Classes.*;
import BusinessLayer.Inventory.Classes.Discount;
import BusinessLayer.Inventory.Classes.Report;
import BusinessLayer.Inventory.Classes.SpecificItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    List<InventoryItem> inventoryItemInventoryList = new LinkedList<>();
    List<SpecificItem> specificItemList = new LinkedList<>();
    List<Discount> discountList = new LinkedList<>();

    LocalDate expiration1 = LocalDate.of(2023,4,16);
    LocalDate expiration2 = LocalDate.of(2023,4,1);

    LocalDate startDate1 = LocalDate.of(2023,4,1);
    LocalDate endDate1 = LocalDate.of(2023,4,16);
    LocalDate startDate2 = LocalDate.of(2023,4,25);
    LocalDate endDate2 = LocalDate.of(2023,4,26);

    InventoryItem inventoryItemInventory1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
    InventoryItem inventoryItemInventory2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);

    SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, expiration1, false, "None", "shelf");
    SpecificItem OrganicEggs = new SpecificItem(210, 15, 20, expiration2, true, "Broken","shelf");

    List<InventoryItem> discountList1 = new LinkedList<>();
    List<InventoryItem> discountList2 = new LinkedList<>();

    Discount discount1 = new Discount(1, "Passover", 50, startDate1, endDate1, discountList1);
    Discount discount2 = new Discount(2, "Independence Day", 30, startDate2, endDate2, discountList2);

    Report itemReport = new InventoryReport(1, LocalDate.now(), inventoryItemInventoryList);
    Report specificItemReport = new DefectiveReport(2, LocalDate.now(), specificItemList);
    Report discountReport = new DiscountReport(3, LocalDate.now(), discountList);


    @Test
    void getReportDate() {
        assertEquals(LocalDate.now(), itemReport.getReportDate());
        assertEquals(LocalDate.now(), specificItemReport.getReportDate());
        assertEquals(LocalDate.now(), discountReport.getReportDate());
    }

}