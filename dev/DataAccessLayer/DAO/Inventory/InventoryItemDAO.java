package DataAcessLayer.DAO.Inventory;

import BusinessLayer.Inventory.Classes.*;
import DataAcessLayer.DTO.Inventory.InventoryItemDTO;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class InventoryItemDAO {

    private Connection connection;
    private  List<InventoryItem> identityMap;

    /**
     * Constructs an InventoryItemDAO object.
     *
     * Initializes the InventoryItemDAO by establishing a connection to the database and
     * creating an empty identityMap for storing inventory items.
     */
    public InventoryItemDAO() {
        try {
            this.connection = Repo.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.identityMap = new LinkedList<>();
        try {
            this.identityMap = getAllItems();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds an inventory item to the database and the identity map.
     *
     * Inserts the given inventory item into the InventoryItem table in the database,
     * along with its associated details such as catalog number, name, manufacturer,
     * total amount, minimum amount, warehouse amount, shelves amount, and price.
     * If the inventory item has specific items included, they are also added to the database.
     * Finally, the inventory item is added to the identity map for easy access.
     *
     * @param item The inventory item to be added.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void addItem(InventoryItem item) throws SQLException {
        PreparedStatement statement = null;

        try {
            String sql = "INSERT INTO InventoryItem (CatalogNumber, Name, Manufacturer, TotalAmount, MinimumAmount, WarehouseAmount, ShelvesAmount, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, item.getCatalogNumber());
            statement.setString(2, item.getName());
            statement.setString(3, item.getManufacturer());
            statement.setInt(4, item.getTotalAmount());
            statement.setInt(5, item.getMinimumAmount());
            statement.setInt(6, item.getWarehouseAmount());
            statement.setInt(7, item.getShelvesAmount());
            statement.setDouble(8, item.getPrice());
            statement.executeUpdate();

            if(item.getIncludeItems().size() != 0){
                addSpecificItemsToItem(item.getIncludeItems(), item.getCatalogNumber());
            }
            identityMap.add(item);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Adds specific items to an inventory item in the database.
     *
     * Inserts the given specific items into the SpecificItemInItem table in the database,
     * associating them with the specified inventory item identified by the catalog number.
     *
     * @param specificItemList The list of specific items to be added.
     * @param catalogNumber The catalog number of the inventory item.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void addSpecificItemsToItem(List<SpecificItem> specificItemList, int catalogNumber){
        PreparedStatement statement = null;
        try{
            for(int i=0; i<specificItemList.size(); i++) {
                SpecificItem item = specificItemList.get(i);
                String sql = "INSERT INTO SpecificItemInItem (CatalogNumber, ItemID)\n" +
                        "VALUES (?, ?);";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, catalogNumber);
                statement.setInt(2, item.getID());
                statement.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Builds an inventory item object from the provided inventory item DTO.
     *
     * Constructs an inventory item object using the information from the inventory item DTO,
     * including the catalog number, name, manufacturer, total amount, minimum amount,
     * warehouse amount, shelves amount, price, and the list of included specific items.
     *
     * @param inventoryItemDTO The inventory item DTO containing the item information.
     * @return The constructed inventory item object.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public InventoryItem buildItem(InventoryItemDTO inventoryItemDTO) throws SQLException{

        int catalogNumber = inventoryItemDTO.getCatalogNumber();
        String Name = inventoryItemDTO.getName();
        String Manufacturer = inventoryItemDTO.getManufacturer();
        int TotalAmount = inventoryItemDTO.getTotalAmount();
        int MinimumAmount = inventoryItemDTO.getMinimumAmount();
        int WarehouseAmount = inventoryItemDTO.getWarehouseAmount();
        int ShelvesAmount = inventoryItemDTO.getShelvesAmount();
        double Price = inventoryItemDTO.getPrice();
        List<SpecificItem> includedItems =null;
        try {
            includedItems = getSpecificItemsByCatalogNumber(catalogNumber);
        } catch (SQLException e){
            e.printStackTrace();
        }

        InventoryItem item = new InventoryItem(catalogNumber, Name, Manufacturer, TotalAmount, ShelvesAmount, WarehouseAmount, MinimumAmount, Price, includedItems);
        identityMap.add(item);
        return item;
    }

    /**
     * Retrieves a list of specific items associated with the given catalog number.
     *
     * Retrieves a list of specific items that are associated with the provided catalog number.
     * The method performs a database query to fetch the specific items linked to the catalog number
     * through the ItemInCategory table. It returns a list of SpecificItem objects.
     *
     * @param catalogNumber The catalog number of the item to retrieve specific items for.
     * @return A list of specific items associated with the catalog number.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public List<SpecificItem> getSpecificItemsByCatalogNumber(int catalogNumber) throws SQLException {
        List<SpecificItem> specificItems = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT SpecificItem.* FROM ItemInCategory " +
                "JOIN SpecificItem ON ItemInCategory.CatalogNumber = SpecificItem.CatalogNumber " +
                "WHERE ItemInCategory.CatalogNumber = ?")) {

            statement.setInt(1, catalogNumber);

            ResultSet resultSet = statement.executeQuery();
            SpecificItemDAO dao = new SpecificItemDAO();
            while (resultSet.next()) {
                int itemID = resultSet.getInt("CatalogNumber");
                SpecificItem specificItem = dao.getSpecificItemByID(itemID);
                specificItems.add(specificItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specificItems;
    }

    /**
     * Updates the details of an inventory item in the database.
     *
     * Updates the details of the provided inventory item in the database based on its catalog number.
     * The method performs an SQL update query to modify the corresponding record in the InventoryItem table.
     *
     * @param item The inventory item to be updated.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void updateItem(InventoryItem item) throws SQLException {
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE InventoryItem SET Name=?, Manufacturer=?, TotalAmount=?, MinimumAmount=?, WarehouseAmount=?, ShelvesAmount=?, Price=? WHERE CatalogNumber=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, item.getName());
            statement.setString(2, item.getManufacturer());
            statement.setInt(3, item.getTotalAmount());
            statement.setInt(4, item.getMinimumAmount());
            statement.setInt(5, item.getWarehouseAmount());
            statement.setInt(6, item.getShelvesAmount());
            statement.setDouble(7, item.getPrice());
            statement.setInt(8, item.getCatalogNumber());
            statement.executeUpdate();

            identityMap.add(item);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Removes an inventory item from the database.
     *
     * Removes the specified inventory item from the database based on its catalog number.
     * The method performs an SQL delete query to remove the corresponding record from the InventoryItem table.
     * If the item has specific items included, they will also be removed from the item before deletion.
     *
     * @param item The inventory item to be removed.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void remove(InventoryItem item) throws SQLException {

        PreparedStatement statement = null;
        List<SpecificItem> items = item.getIncludeItems();
        try {
            if(!item.getIncludeItems().isEmpty())
                removeSpecificItemsFromItem(item.getCatalogNumber());

            String sql = "DELETE FROM InventoryItem WHERE CatalogNumber=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, item.getCatalogNumber());
            statement.executeUpdate();

            identityMap.remove(item);
            removeSpecificItemsFromItem(item.getCatalogNumber());
            for (SpecificItem specificItem : items) {
                deleteSpecificItem(specificItem, item.getCatalogNumber());
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes specific items associated with an inventory item from the database.
     *
     * Removes the specific items that are associated with the inventory item identified by the specified catalog number.
     *      * The method performs an SQL delete query to remove the corresponding records from the SpecificItemInItem table.
     *
     * @param catalogNumber The catalog number of the inventory item.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void removeSpecificItemsFromItem(int catalogNumber) throws SQLException{
        PreparedStatement statement = null;

        try {
            String sql = "DELETE FROM SpecificItemInItem\n" +
                    "WHERE CatalogNumber = ?;";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, catalogNumber);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Deletes the specified specific item from the SpecificItem table based on the catalog number and item ID.
     *
     * @param specificItem The specific item to be deleted.
     * @param catalogNumber The catalog number of the item.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteSpecificItem(SpecificItem specificItem, int catalogNumber) throws SQLException {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM SpecificItem WHERE CatalogNumber = ? AND ItemID = ?"
            );
            stmt.setInt(1, catalogNumber);
            stmt.setInt(2, specificItem.getID());
            stmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes all inventory items from the database.
     *
     * Removes all inventory items by performing an SQL delete query on the InventoryItem table.
     * Additionally, it clears the identity map by removing all items from it.
     *
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public void removeAll() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM InventoryItem;";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            identityMap.clear();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all inventory items from the database.
     *
     * This method performs an SQL select query on the InventoryItem table to fetch all the items from the database.
     * For each item retrieved, it calls the `getItemByCatalogNumber` method to either get the item from the
     * identity map or instantiate a new InventoryItem object if it's not already present in the map. The retrieved
     * items are added to a list and returned.
     *
     * @return A list of all inventory items in the database.
     * @throws SQLException If an SQL exception occurs during the database operation.
     */
    public List<InventoryItem> getAllItems() throws SQLException {
        List<InventoryItem> inventoryItems = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM InventoryItem") ;
            while (rs.next()) {
                int catalogNumber = rs.getInt("CatalogNumber");
                InventoryItem item = getItemByCatalogNumber(catalogNumber);

                if(item != null) {
                    if (!identityMap.contains(item))
                        identityMap.add(item);
                    inventoryItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryItems;
    }

    /**
     * Retrieves an inventory item from the database based on the specified catalog number.
     *
     * @param catalogNumber The catalog number of the item.
     * @return The retrieved inventory item, or null if the item is not found.
     * @throws SQLException If a database access error occurs.
     */
    public InventoryItem getItemByCatalogNumber(int catalogNumber) throws SQLException{
        for (InventoryItem item : identityMap){
            if(item.getCatalogNumber() == catalogNumber){
                return item;
            }
        }

        String sql = "SELECT * FROM InventoryItem WHERE CatalogNumber=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, catalogNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String Name = rs.getString("Name");
                String Manufacturer = rs.getString("Manufacturer");
                int TotalAmount = rs.getInt("TotalAmount");
                int MinimumAmount = rs.getInt("MinimumAmount");
                int WarehouseAmount = rs.getInt("WarehouseAmount");
                int ShelvesAmount = rs.getInt("ShelvesAmount");
                double Price = rs.getDouble("Price");
                List<SpecificItem> includedItems = getSpecificItemsByCatalogNumber(catalogNumber);
                InventoryItem item = new InventoryItem(catalogNumber, Name, Manufacturer, TotalAmount, MinimumAmount, WarehouseAmount, ShelvesAmount, Price, includedItems);
                identityMap.add(item);
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}