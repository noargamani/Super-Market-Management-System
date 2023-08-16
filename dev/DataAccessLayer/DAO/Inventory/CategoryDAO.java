package DataAcessLayer.DAO.Inventory;

import DataAcessLayer.DTO.Inventory.CategoryDTO;
import BusinessLayer.Inventory.Classes.Category;
import BusinessLayer.Inventory.Classes.InventoryItem;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.*;

public class CategoryDAO {
    private Connection conn;
    private List<Category> identityMap;

    /**
     * Constructs a new CategoryDAO object.
     * Initializes the connection to the database and creates an identity map.
     */
    public CategoryDAO() {
        try {
            this.conn = Repo.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.identityMap = new LinkedList<>();
        try {
            this.identityMap = getAllCategories();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Builds and returns a Category object based on the provided CategoryDTO.
     * Retrieves the category ID, name, father category ID, inventory items, and subcategories from the CategoryDTO.
     * If a father category ID is specified, retrieves the corresponding father category.
     * Populates the inventory items and subcategories lists by querying the database.
     * Creates a new Category object with the retrieved information and adds it to the identity map.
     * Returns the constructed Category object.
     *
     * @param categoryDTO The CategoryDTO object containing the category information.
     * @return The constructed Category object.
     * @throws SQLException If there is an error accessing the database.
     */
    public Category buildCategory(CategoryDTO categoryDTO) throws SQLException{
        int categoryID = categoryDTO.getCategoryId();
        String categoryName = categoryDTO.getCategoryName();
        int fatherCategoryID = categoryDTO.getFatherCategory();
        Category futherCategory = null;
        try {
            if(fatherCategoryID != -1)
                futherCategory = getCategoryById(fatherCategoryID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<InventoryItem> inventoryItems = findItemsByCategory(categoryID);
        List<Category> subCategories = null;
        try {
            subCategories = findSubCategoriesByCategory(categoryID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Category category = new Category(categoryID, categoryName, futherCategory, subCategories, inventoryItems);
        identityMap.add(category);
        return category;
    }


    /**
     * Adds a Category object to the database and recursively adds its subcategories and associated items.
     * Inserts the category ID, category name, and father category ID into the Category table.
     * Recursively adds subcategories by calling the addCategory method for each subcategory.
     * Adds the associated items to the ItemsInCategory table.
     *
     * @param category The Category object to be added.
     * @throws SQLException If there is an error accessing the database.
     */
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO Category(CategoryID, CategoryName, FatherCategory) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, category.getCategoryID());
            statement.setString(2, category.getCategoryName());
            Category fatherCategory = category.getFatherCategory();
            int fatherID = -1;
            if (fatherCategory != null)
                fatherID = fatherCategory.getCategoryID();
            statement.setInt(3, fatherID);

            CategoryDAO dao = new CategoryDAO();
            if(!category.getSubCategories().isEmpty()){
                List<Category> subCategories = category.getSubCategories();
                for (Category subCategory : subCategories) {
                    dao.addCategory(subCategory);
                }
            }

            if(!category.getItems().isEmpty()){
                dao.addToItemsInCategory(category.getItems(), category.getCategoryID());
            }
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds the specified list of InventoryItems to the ItemsInCategory table,
     * associating them with the given catalogNumber (category ID).
     *
     * @param items        The list of InventoryItems to be added.
     * @param CategoryID The ID of the category to retrieve.
     * @throws SQLException If there is an error accessing the database.
     */
    public void addToItemsInCategory(List<InventoryItem> items, int CategoryID){
        PreparedStatement statement = null;
        try{
            for (InventoryItem item : items) {
                String sql = "INSERT INTO ItemInCategory (CategoryID, CatalogNumber)\n" +
                        "VALUES (?, ?);\n";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, CategoryID);
                statement.setInt(2, item.getCatalogNumber());
                statement.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes an item from a category based on the specified catalog number and category ID.
     *
     * @param catalogNumber The catalog number of the item to be removed.
     * @param categoryID The ID of the category from which the item should be removed.
     */
    public void removeItemFromCategory(int catalogNumber, int categoryID){
        String query = "DELETE FROM ItemInCategory WHERE CategoryID = ? AND CatalogNumber = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, categoryID);
            statement.setInt(2, catalogNumber);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the specified Category in the database with the new information.
     *
     * @param category The Category object to be updated.
     * @throws SQLException If there is an error accessing the database.
     */
    public void updateCategory(Category category) throws SQLException{

        for(int i=0; i< identityMap.size(); i++){
            if(identityMap.get(i).getCategoryID() == category.getCategoryID())
                identityMap.remove(identityMap.get(i));
        }

        try (PreparedStatement statement = conn.prepareStatement("UPDATE Category SET CategoryName = ?, " +
                "FatherCategory = ? WHERE CategoryID = ?")) {

            statement.setString(1, category.getCategoryName());
            if(category.getFatherCategory() != null)
                statement.setInt(2, category.getFatherCategory().getCategoryID());
            else
                statement.setInt(2, -1);
            statement.setInt(3, category.getCategoryID());
            statement.executeUpdate();
            identityMap.add(category);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a Category object from the database based on the specified category ID.
     *
     * @param categoryId The ID of the category to retrieve.
     * @return The Category object corresponding to the given category ID, or null if not found.
     * @throws SQLException If there is an error accessing the database.
     */
    public Category getCategoryById(int categoryId) throws SQLException {
        if(categoryId == -1)
            return null;
        for(Category category : identityMap){
            if(category.getCategoryID() == categoryId)
                return category;
        }

        String sql = "SELECT * FROM Category WHERE CategoryID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String categoryName = rs.getString("CategoryName");
                    int fatherCategoryID = rs.getInt("FatherCategory");
                    Category futherCategory = null;
                    if(fatherCategoryID != -1)
                        getCategoryById(fatherCategoryID);

                    List<InventoryItem> inventoryItems = findItemsByCategory(categoryId);
                    List<Category> subCategories = findSubCategoriesByCategory(categoryId);
                    Category category = new Category(categoryId, categoryName, futherCategory, subCategories, inventoryItems);
                    identityMap.add(category);
                    return category;
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Retrieves a list of InventoryItem objects associated with the specified category ID.
     *
     * @param categoryID The ID of the category.
     * @return A list of InventoryItem objects belonging to the specified category.
     * @throws SQLException If there is an error accessing the database.
     */
    public List<InventoryItem> findItemsByCategory(int categoryID) throws SQLException{
        List<InventoryItem> itemList = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT CatalogNumber\n" +
                "FROM ItemInCategory\n" +
                "WHERE CategoryID = ?;")) {

            statement.setInt(1, categoryID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int itemID = resultSet.getInt("CatalogNumber");
                    InventoryItemDAO itemDAO = new InventoryItemDAO();
                    InventoryItem item = itemDAO.getItemByCatalogNumber(itemID);
                    itemList.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemList;
    }

    /**
     * Retrieves a list of subcategories associated with the specified category ID.
     *
     * @param categoryID The ID of the category.
     * @return A list of Category objects representing the subcategories of the specified category.
     * @throws SQLException If there is an error accessing the database.
     */
    public List<Category> findSubCategoriesByCategory(int categoryID) throws SQLException {
        List<Category> subCategoryList = new LinkedList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT SubCategoryID\n" +
                "FROM SubCategoryInCategory\n" +
                "WHERE CategoryID = ?;")) {

            statement.setInt(1, categoryID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int subCategoryID = resultSet.getInt("CategoryID");
                    String subCategoryName = resultSet.getString("CategoryName");
                    int fatherCategoryID = resultSet.getInt("FatherCategory");
                    Category futherCategory = null;
                    if(fatherCategoryID != -1)
                        futherCategory = getCategoryById(fatherCategoryID);
                    List<InventoryItem> inventoryItems = findItemsByCategory(categoryID);
                    List<Category> subSubCategoryList = findSubCategoriesByCategory(subCategoryID);
                    Category subCategory = new Category(subCategoryID, subCategoryName, futherCategory, subSubCategoryList, inventoryItems);
                    subCategoryList.add(subCategory);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subCategoryList;
    }

    /**
     * Retrieves a list of all categories from the database.
     *
     * @return A list of Category objects representing all the categories.
     * @throws SQLException If there is an error accessing the database.
     */
    public List<Category> getAllCategories() throws SQLException{
        String sql = "SELECT * FROM Category";
        List<Category> categories = new LinkedList<>();
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                int CategoryID = rs.getInt("CategoryID");
                Category category = getCategoryById(CategoryID);
                if(category != null) {
                    if (!identityMap.contains(category))
                        identityMap.add(category);
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Removes all categories from the database.
     *
     * @throws SQLException If there is an error accessing the database.
     */
    public void removeAll() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM Category;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            identityMap.clear();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes all items from the ItemInCategory table and clears the identity map.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void removeAllItemInCategory() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM ItemInCategory;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            identityMap.clear();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}