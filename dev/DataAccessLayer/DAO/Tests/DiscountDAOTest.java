package DataAcessLayer.DAO.Tests;

import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DAO.Inventory.DiscountDAO;
import DataAcessLayer.DTO.Inventory.DiscountDTO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountDAOTest {

    LocalDate startDate1 = LocalDate.of(2023,7,1);
    LocalDate endDate1 = LocalDate.of(2023,7,16);
    InventoryItem item1 = new InventoryItem(1, "Milk Tnuva 3%", "Tnuva", 5, 2.5);
    InventoryItem item2 = new InventoryItem(2,"Organic eggs size L", "FreeChicken", 5, 2.5);
    List<InventoryItem> items = new ArrayList<>();
    @Test
    void buildDiscount() {
        try{
            Repo.createTables(Repo.connect());
            DiscountDTO discountDTO = new DiscountDTO(5, "Shavohot", 25.5, startDate1, endDate1);
            DiscountDAO dao = new DiscountDAO();
            dao.removeAll();

            Discount discount = dao.buildDiscount(discountDTO);
            assertEquals(discount.getSaleNumber(), discountDTO.getSaleNumber());
            assertEquals(discount.getSaleName(), discountDTO.getSaleName());
            assertEquals(discount.getDiscount(), discountDTO.getDiscount());
            assertEquals(discount.getStartDate(), discountDTO.getStartDate());
            assertEquals(discount.getEndDate(), discountDTO.getEndDate());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getItemsBySaleNumber() {
        try {
            DiscountDAO dao = new DiscountDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);

            Discount discount = new Discount(5, "Shavohot", 25.5, startDate1, endDate1, items);
            dao.addDiscount(discount);


            List<InventoryItem> returnItems = dao.getItemsBySaleNumber(5);
            if(!returnItems.isEmpty()) {
                InventoryItem returnItem = returnItems.get(0);
                assertEquals(item1.getCatalogNumber(), returnItem.getCatalogNumber());
                assertEquals(item1.getName(), returnItem.getName());
                assertEquals(item1.getManufacturer(), returnItem.getManufacturer());
                assertEquals(item1.getPrice(), returnItem.getPrice());
                assertEquals(item1.getMinimumAmount(), returnItem.getMinimumAmount());
            }

            // assertEquals(discount.getIncludeInDiscount(), returnItems);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void addDiscount() {
        try {
            DiscountDAO dao = new DiscountDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);
            items.add(item2);

            Discount discount = new Discount(5, "Shavohot", 25.5, startDate1, endDate1, items);
            dao.addDiscount(discount);
            dao.addItemsToDiscount(items, 5);

            Discount returnDiscount = dao.getDiscountBySaleNumber(5);

            assertEquals(discount.getSaleNumber(), returnDiscount.getSaleNumber());
            assertEquals(discount.getSaleName(), returnDiscount.getSaleName());
            assertEquals(discount.getDiscount(), returnDiscount.getDiscount());
            assertEquals(discount.getStartDate(), returnDiscount.getStartDate());
            assertEquals(discount.getEndDate(), returnDiscount.getEndDate());
            assertEquals(discount.getIncludeInDiscount(), discount.getIncludeInDiscount());
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Test
    void updateDiscount() {
        try {
            DiscountDAO dao = new DiscountDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);
            items.add(item2);

            Discount discount = new Discount(5, "Shavohot", 25.5, startDate1, endDate1, items);
            dao.addDiscount(discount);
            dao.addItemsToDiscount(items, 5);

            discount.setDiscount(30);
            dao.updateDiscount(discount);
            Discount returnDiscount = dao.getDiscountBySaleNumber(5);
            assertEquals(discount.getSaleNumber(), returnDiscount.getSaleNumber());
            assertEquals(discount.getSaleName(), returnDiscount.getSaleName());
            assertEquals(discount.getDiscount(), returnDiscount.getDiscount());
            assertEquals(discount.getStartDate(), returnDiscount.getStartDate());
            assertEquals(discount.getEndDate(), returnDiscount.getEndDate());
            assertEquals(discount.getIncludeInDiscount(), discount.getIncludeInDiscount());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getDiscountBySaleNumber() {
        try {
            DiscountDAO dao = new DiscountDAO();
            dao.removeAll();
            Repo.createTables(Repo.connect());
            items.add(item1);
            items.add(item2);

            Discount discount = new Discount(5, "Shavohot", 25.5, startDate1, endDate1, items);
            dao.addDiscount(discount);
            dao.addItemsToDiscount(items,5);

            Discount returnDiscount = dao.getDiscountBySaleNumber(5);

            assertEquals(discount.getSaleNumber(), returnDiscount.getSaleNumber());
            assertEquals(discount.getSaleName(), returnDiscount.getSaleName());
            assertEquals(discount.getDiscount(), returnDiscount.getDiscount());
            assertEquals(discount.getStartDate(), returnDiscount.getStartDate());
            assertEquals(discount.getEndDate(), returnDiscount.getEndDate());
            assertEquals(discount.getIncludeInDiscount(), discount.getIncludeInDiscount());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}