package DataAcessLayer.DAO.Tests;

import BusinessLayer.Inventory.Classes.Category;
import BusinessLayer.Inventory.Classes.InventoryItem;
import DataAcessLayer.DAO.Inventory.CategoryDAO;
import DataAcessLayer.DTO.Inventory.CategoryDTO;
import DataAcessLayer.Repo;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest {

    Category category1 = new Category(1, "Dairy products");
    Category category2 = new Category(2, "Milk", category1);
    Category category3 = new Category(3, "750 ml", category2);

    @Test
    void buildCategory() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            CategoryDTO dto = new CategoryDTO(1, "Dairy products",-1);
            Category category = dao.buildCategory(dto);
            assertEquals(category.getCategoryID(), dto.getCategoryId());
            assertEquals(category.getCategoryName(), dto.getCategoryName());
            assertNull(category.getFatherCategory());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void addCategory() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            dao.addCategory(category1);
            Category returnCategory = dao.getCategoryById(category1.getCategoryID());
            assertEquals(category1.getCategoryID(), returnCategory.getCategoryID());
            assertEquals(category1.getCategoryName(), returnCategory.getCategoryName());
            assertNull(returnCategory.getFatherCategory());
            List<Category> categories = dao.findSubCategoriesByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getSubCategories().size(); i++){
                assertEquals(category1.getSubCategories().get(i).getCategoryID(), categories.get(i).getCategoryID());
            }
            List<InventoryItem> items = dao.findItemsByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getItems().size(); i++){
                assertEquals(category1.getItems().get(i).getCatalogNumber(), items.get(i).getCatalogNumber());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void updateCategory() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            Category category = new Category(4, "Clean products");

            dao.addCategory(category);
            Category returnCategory1 = dao.getCategoryById(4);
            assertEquals("Clean products", returnCategory1.getCategoryName());

            category.setCategoryName("Cleaning products");
            dao.updateCategory(category);
            Category returnCategory2 = dao.getCategoryById(4);
            assertEquals("Cleaning products", returnCategory2.getCategoryName());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getCategoryById() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            dao.addCategory(category1);
            Category returnCategory = dao.getCategoryById(category1.getCategoryID());
            assertEquals(category1.getCategoryID(), returnCategory.getCategoryID());
            assertEquals(category1.getCategoryName(), returnCategory.getCategoryName());
            assertNull(returnCategory.getFatherCategory());
            List<Category> categories = dao.findSubCategoriesByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getSubCategories().size(); i++){
                assertEquals(category1.getSubCategories().get(i).getCategoryID(), categories.get(i).getCategoryID());
            }
            List<InventoryItem> items = dao.findItemsByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getItems().size(); i++){
                assertEquals(category1.getItems().get(i).getCatalogNumber(), items.get(i).getCatalogNumber());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void findItemsByCategory() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            dao.addCategory(category1);
            Category returnCategory = dao.getCategoryById(category1.getCategoryID());

            List<InventoryItem> items = dao.findItemsByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getItems().size(); i++){
                assertEquals(category1.getItems().get(i).getCatalogNumber(), items.get(i).getCatalogNumber());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void findSubCategoriesByCategory() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            dao.addCategory(category1);
            Category returnCategory = dao.getCategoryById(category1.getCategoryID());

            List<Category> categories = dao.findSubCategoriesByCategory(returnCategory.getCategoryID());
            for(int i=0; i<category1.getSubCategories().size(); i++){
                assertEquals(category1.getSubCategories().get(i).getCategoryID(), categories.get(i).getCategoryID());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void getAllCategories() {
        try {
            Repo.createTables(Repo.connect());
            CategoryDAO dao = new CategoryDAO();
            dao.removeAll();

            dao.addCategory(category1);
            dao.addCategory(category2);
            dao.addCategory(category3);

            List<Category> categoryList = new LinkedList<>();
            categoryList.add(category1);
            categoryList.add(category2);
            categoryList.add(category3);

            List<Category> returnCategoryList = dao.getAllCategories();

            for(int i=0; i<categoryList.size(); i++){
                assertEquals(categoryList.get(i).getCategoryID(), returnCategoryList.get(i).getCategoryID());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}