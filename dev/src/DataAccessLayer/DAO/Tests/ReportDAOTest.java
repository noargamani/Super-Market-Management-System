package DataAcessLayer.DAO.Tests;

import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DAO.Inventory.ReportDAO;
import DataAcessLayer.DTO.Inventory.ReportDTO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportDAOTest {

    LocalDate date1 = LocalDate.of(2023,5,30);
    LocalDate date2 = LocalDate.of(2023,6,15);
    LocalDate startDate1 = LocalDate.of(2023,7,1);
    LocalDate endDate1 = LocalDate.of(2023,7,16);
    LocalDate startDate2 = LocalDate.of(2023,7,25);
    LocalDate endDate2 = LocalDate.of(2023,7,26);
    InventoryItem item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
    InventoryItem item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);
    List<InventoryItem> items = new ArrayList<>();
    SpecificItem MilkTnuva = new SpecificItem(110, 8, 10, date1, false, "None", "shelf");
    SpecificItem OrganicEggs = new SpecificItem(210, 15, 20, date2, true, "Broken","shelf");
    List<SpecificItem> specificItems = new ArrayList<>();
    List<InventoryItem> discountList1 = new LinkedList<>();
    List<InventoryItem> discountList2 = new LinkedList<>();
    Discount discount1 = new Discount(1, "Passover", 50, startDate1, endDate1, discountList1);
    Discount discount2 = new Discount(2, "Independence Day", 30, startDate2, endDate2, discountList2);
    List<Discount> discounts = new ArrayList<>();
    @Test
    void buildReport() {
        try{
            Repo.createTables(Repo.connect());
            ReportDTO reportDTO = new ReportDTO(5, date1, "InventoryReport");
            ReportDAO dao = new ReportDAO();
            dao.removeAll();

            Report report = dao.buildReport(reportDTO);
            assertEquals(report.getReportID(), 5);
            assertEquals(report.getReportDate(), date1);
            assertTrue(report instanceof InventoryReport);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void addReport() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);
            items.add(item2);

            MissingItemsReport report = new MissingItemsReport(8, date2, items);
            dao.addReport(report);

            Report returnReport = dao.getReportByID(8);
            MissingItemsReport missingItemsReport = (MissingItemsReport)returnReport;

            assertEquals(report.getReportID(), returnReport.getReportID());
            assertEquals(report.getReportDate(), returnReport.getReportDate());
            assertEquals(report.getItems(), missingItemsReport.getItems());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void updateReport() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            specificItems.add(OrganicEggs);

            DefectiveReport report = new DefectiveReport(9, date1, specificItems);
            dao.addReport(report);

            report.setReportDate(date2);
            dao.updateReport(report);
            Report returnReport = dao.getReportByID(9);
            assertEquals(report.getReportID(), returnReport.getReportID());
            assertEquals(report.getReportDate(), returnReport.getReportDate());
            List<SpecificItem> returnSpecificItems = dao.getSpecificItemsByReportID(9);
            if(!returnSpecificItems.isEmpty()) {
                SpecificItem returnSpecificItem = returnSpecificItems.get(0);
                assertEquals(OrganicEggs.getID(), returnSpecificItem.getID());
                assertEquals(OrganicEggs.getCostPrice(), returnSpecificItem.getCostPrice());
                assertEquals(OrganicEggs.getSellingPrice(), returnSpecificItem.getSellingPrice());
                assertEquals(OrganicEggs.getDefective(), returnSpecificItem.getDefective());
                assertEquals(OrganicEggs.getExpiration(), returnSpecificItem.getExpiration());
                assertEquals(OrganicEggs.getDefectType(), returnSpecificItem.getDefectType());
                assertEquals(OrganicEggs.getLocation(), returnSpecificItem.getLocation());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getReportByID() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);
            items.add(item2);

            InventoryReport report = new InventoryReport(50, date1, items);
            dao.addReport(report);

            Report returnReport = dao.getReportByID(50);

            InventoryReport inventoryReport = (InventoryReport) returnReport;

            assertEquals(report.getReportID(), returnReport.getReportID());
            assertEquals(report.getReportDate(), returnReport.getReportDate());
            assertEquals(report.getItems(), inventoryReport.getItems());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getItemsByReportID() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);

            InventoryReport report = new InventoryReport(50, date1, items);
            dao.addReport(report);


            List<InventoryItem> returnItems = dao.getItemsByReportID(50);

            if(!returnItems.isEmpty()) {
                InventoryItem returnItem = returnItems.get(0);
                assertEquals(item1.getCatalogNumber(), returnItem.getCatalogNumber());
                assertEquals(item1.getName(), returnItem.getName());
                assertEquals(item1.getManufacturer(), returnItem.getManufacturer());
                assertEquals(item1.getPrice(), returnItem.getPrice());
                assertEquals(item1.getMinimumAmount(), returnItem.getMinimumAmount());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getSpecificItemsByReportID() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            specificItems.add(MilkTnuva);

            DefectiveReport report = new DefectiveReport(9, date1, specificItems);
            dao.addReport(report);


            List<SpecificItem> returnSpecificItems = dao.getSpecificItemsByReportID(9);
            if(!returnSpecificItems.isEmpty()) {
                SpecificItem returnSpecificItem = returnSpecificItems.get(0);
                assertEquals(MilkTnuva.getID(), returnSpecificItem.getID());
                assertEquals(MilkTnuva.getCostPrice(), returnSpecificItem.getCostPrice());
                assertEquals(MilkTnuva.getSellingPrice(), returnSpecificItem.getSellingPrice());
                assertEquals(MilkTnuva.getDefective(), returnSpecificItem.getDefective());
                assertEquals(MilkTnuva.getExpiration(), returnSpecificItem.getExpiration());
                assertEquals(MilkTnuva.getDefectType(), returnSpecificItem.getDefectType());
                assertEquals(MilkTnuva.getLocation(), returnSpecificItem.getLocation());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getDiscountsByReportID() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            discounts.add(discount1);

            DiscountReport report = new DiscountReport(30, date2, discounts);
            dao.addReport(report);

            List<Discount> returnDiscounts = dao.getDiscountsByReportID(30);

            if(!returnDiscounts.isEmpty()) {
                Discount returnDiscount = returnDiscounts.get(0);
                assertEquals(discount1.getSaleNumber(), returnDiscount.getSaleNumber());
                assertEquals(discount1.getSaleName(), returnDiscount.getSaleName());
                assertEquals(discount1.getDiscount(), returnDiscount.getDiscount());
                assertEquals(discount1.getStartDate(), returnDiscount.getStartDate());
                assertEquals(discount1.getEndDate(), returnDiscount.getEndDate());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getAllReports() {
        try {
            ReportDAO dao = new ReportDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            specificItems.add(MilkTnuva);


            DefectiveReport report = new DefectiveReport(9, date1, specificItems);
            dao.addReport(report);

            List<Report> reports = dao.getAllReports();
            if(!reports.isEmpty()) {
                Report returnReport = reports.get(0);
                assertEquals(report.getReportID(), returnReport.getReportID());
                assertEquals(report.getReportDate(), report.getReportDate());
                List<SpecificItem> returnSpecificItems = dao.getSpecificItemsByReportID(9);
                if(!returnSpecificItems.isEmpty()) {
                    SpecificItem returnSpecificItem = returnSpecificItems.get(0);
                    assertEquals(MilkTnuva.getID(), returnSpecificItem.getID());
                    assertEquals(MilkTnuva.getCostPrice(), returnSpecificItem.getCostPrice());
                    assertEquals(MilkTnuva.getSellingPrice(), returnSpecificItem.getSellingPrice());
                    assertEquals(MilkTnuva.getDefective(), returnSpecificItem.getDefective());
                    assertEquals(MilkTnuva.getExpiration(), returnSpecificItem.getExpiration());
                    assertEquals(MilkTnuva.getDefectType(), returnSpecificItem.getDefectType());
                    assertEquals(MilkTnuva.getLocation(), returnSpecificItem.getLocation());
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}