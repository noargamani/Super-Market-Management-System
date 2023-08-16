package DataAcessLayer.DAO.Inventory;

import BusinessLayer.Inventory.Classes.SpecificItem;
import DataAcessLayer.DTO.Inventory.SpecificItemDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import DataAcessLayer.Repo;

public class SpecificItemDAO {
    private Connection conn;
    private List<SpecificItem> identityMap;

    /**
     * Constructs a new instance of the SpecificItemDAO class.
     *
     * This constructor initializes a new SpecificItemDAO object. It establishes a connection to the database
     * by calling the "connect" method from the "Repo" class. If an exception occurs during the connection
     * process, it will be printed to the console. Additionally, it initializes an empty identity map
     * represented by the "identityMap" collection.
     */
    public SpecificItemDAO() {
        try {
            this.conn = Repo.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.identityMap = new ArrayList<>();
        try {
            this.identityMap = getAllItems();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a specific item to the database.
     *
     * This method inserts a new specific item into the "SpecificItem" table in the database.
     * It takes a SpecificItem object and the catalog number as parameters. The specific item's
     * properties such as ID, cost price, selling price, expiration date, defect status, defect type,
     * and location are retrieved from the SpecificItem object and inserted into the database using
     * a prepared statement. After the insertion, the specific item is added to the identity map.
     * If a SQLException occurs during the execution of the SQL statement, the exception is printed
     * to the console.
     *
     * @param specificItem   The SpecificItem object to be added.
     * @param catalogNumber  The catalog number of the item.
     * @throws SQLException if a database access error occurs or the SQL statement execution fails.
     */
    public void addSpecificItem(SpecificItem specificItem, int catalogNumber) throws SQLException {
        try {
            Date deliverTime;
            LocalDate FirstdeliverTime = specificItem.getDeliverTime();
            if(FirstdeliverTime == null) {
                deliverTime = Date.valueOf(LocalDate.now());
            }
            else {
                deliverTime = Date.valueOf(FirstdeliverTime);
                if (deliverTime == null) {
                    deliverTime = Date.valueOf(LocalDate.now());
                }
            }
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO SpecificItem (CatalogNumber, ItemID, CostPrice, SellingPrice, Expiration, Defective, DefectType, Location, DeliverTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            stmt.setInt(1, catalogNumber);
            stmt.setInt(2, specificItem.getID());
            stmt.setDouble(3, specificItem.getCostPrice());
            stmt.setDouble(4, specificItem.getSellingPrice());
            stmt.setDate(5, Date.valueOf(specificItem.getExpiration()));
            stmt.setBoolean(6, specificItem.getDefective());
            stmt.setString(7, specificItem.getDefectType());
            stmt.setString(8, specificItem.getLocation());
            stmt.setDate(9, deliverTime);
            stmt.executeUpdate();
            identityMap.add(specificItem);
            addSpecificItemsToItem(specificItem.getID(), catalogNumber);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a specific item to an inventory item by inserting a new row in the SpecificItemInItem table.
     *
     * @param specificItemID The ID of the specific item to be added.
     * @param catalogNumber The catalog number of the inventory item.
     * @throws SQLException If a database access error occurs.
     */
    public void addSpecificItemsToItem(int specificItemID, int catalogNumber) throws SQLException{
        PreparedStatement statement = null;
        try{
            String sql = "INSERT INTO SpecificItemInItem (CatalogNumber, ItemID)\n" +
                    "VALUES (?, ?);";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, catalogNumber);
            statement.setInt(2, specificItemID);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specific item from the database.
     *
     * This method removes a specific item from the "SpecificItem" table in the database based on
     * the provided catalog number and item ID. It executes a DELETE SQL statement using a prepared
     * statement, specifying the catalog number and item ID as parameters. After the deletion, the
     * specific item is removed from the identity map. If a SQLException occurs during the execution
     * of the SQL statement, the exception is printed to the console.
     *
     * @param specificItem   The SpecificItem object to be deleted.
     * @param catalogNumber  The catalog number of the item.
     * @throws SQLException if a database access error occurs or the SQL statement execution fails.
     */
    public void deleteSpecificItem(SpecificItem specificItem, int catalogNumber) throws SQLException {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM SpecificItem WHERE CatalogNumber = ? AND ItemID = ?"
            );
            stmt.setInt(1, catalogNumber);
            stmt.setInt(2, specificItem.getID());
            stmt.executeUpdate();
            identityMap.remove(specificItem);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Builds a SpecificItem object based on the provided SpecificItemDTO.
     *
     * This method takes a SpecificItemDTO object and extracts the necessary attributes to construct
     * a SpecificItem object. It retrieves the item ID, cost price, selling price, expiration date,
     * defect status, defect type, and location from the SpecificItemDTO. It then creates a new
     * SpecificItem object using these attributes and adds it to the identity map. Finally, it returns
     * the constructed SpecificItem object.
     *
     * @param specificItem The SpecificItemDTO containing the attributes of the SpecificItem.
     * @return The constructed SpecificItem object.
     */
    public SpecificItem buildSpecificItem(SpecificItemDTO specificItem){

        int itemID = specificItem.getID();
        double CostPrice = specificItem.getCostPrice();
        double SellingPrice = specificItem.getSellingPrice();
        LocalDate Expiration = specificItem.getExpiration();
        Boolean Defective = specificItem.isDefective();
        String DefectType = specificItem.getDefectType();
        String Location = specificItem.getLocation();
        SpecificItem specificItemOb = new SpecificItem(itemID, CostPrice, SellingPrice, Expiration, Defective, DefectType, Location);
        identityMap.add(specificItemOb);
        return specificItemOb;
    }

    /**
     * Updates the attributes of a SpecificItem in the database.
     *
     * This method updates the attributes of a SpecificItem in the database based on the provided
     * SpecificItem object. It constructs an SQL update statement to modify the CatalogNumber, ItemID,
     * CostPrice, SellingPrice, Expiration, Defective, Location, and DefectType fields of the SpecificItem.
     * The update is performed using a prepared statement, and the modified SpecificItem is added to the
     * identity map. If an SQLException occurs during the update process, it is printed to the standard
     * error output.
     *
     * @param specificItem The SpecificItem object containing the updated attribute values.
     * @throws SQLException If an SQL exception occurs while executing the update statement.
     */
    public void updateSpecificItem(int catalogNumber, SpecificItem specificItem) throws SQLException {
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE SpecificItem SET CatalogNumber=?, CostPrice=?, SellingPrice=?, Expiration=?, Defective=?, Location=?, DefectType=?, DeliverTime=? WHERE ItemID=?";
            statement = conn.prepareStatement(sql);
            statement.setInt(1,catalogNumber);
            statement.setDouble(2, specificItem.getCostPrice());
            statement.setDouble(3, specificItem.getSellingPrice());
            statement.setDate(4, Date.valueOf(specificItem.getExpiration()));
            statement.setBoolean(5, specificItem.getDefective());
            statement.setString(6, specificItem.getLocation());
            statement.setString(7, specificItem.getDefectType());
            statement.setDate(8,Date.valueOf(specificItem.getDeliverTime()));
            statement.setInt(9,specificItem.getID());
            statement.executeUpdate();

            if (!identityMap.contains(specificItem))
                identityMap.add(specificItem);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all SpecificItems from the database.
     *
     * This method retrieves all SpecificItems stored in the database and returns them as a List.
     * It executes an SQL SELECT query to fetch all rows from the SpecificItem table and constructs
     * SpecificItem objects based on the retrieved data. The SpecificItems are added to the identity map,
     * and the resulting List is returned. If an SQLException occurs during the retrieval process,
     * it is printed to the standard error output.
     *
     * @return A List containing all SpecificItems retrieved from the database.
     * @throws SQLException If an SQL exception occurs while executing the SELECT query.
     */
    public List<SpecificItem> getAllItems() throws SQLException {
        List<SpecificItem> items = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SpecificItem");
            while (rs.next()) {
                int id = rs.getInt("itemID");
                SpecificItem specificItem = getSpecificItemByID(id);
                if (specificItem != null) {
                    if (!identityMap.contains(specificItem))
                        identityMap.add(specificItem);
                    items.add(specificItem);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Retrieves a SpecificItem from the database based on its ID.
     *
     * This method retrieves a SpecificItem with the given ID from the database and returns it.
     * It first checks if the SpecificItem is already present in the identity map. If found, it is
     * immediately returned. Otherwise, it executes an SQL SELECT query to fetch the SpecificItem
     * from the database. If a SpecificItem with the given ID exists in the database, a new
     * SpecificItem object is constructed using the retrieved data and added to the identity map.
     * The constructed SpecificItem is then returned. If no SpecificItem is found with the given ID,
     * null is returned. If an SQLException occurs during the retrieval process, it is printed to
     * the standard error output.
     *
     * @param ID The ID of the SpecificItem to retrieve.
     * @return The SpecificItem with the given ID if found, or null if not found.
     * @throws SQLException If an SQL exception occurs while executing the SELECT query.
     */
    public SpecificItem getSpecificItemByID(int ID) throws SQLException{
        for (SpecificItem specificItem : identityMap){
            if(specificItem.getID() == ID){
                return specificItem;
            }
        }

        String sql = "SELECT * FROM SpecificItem WHERE ItemID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double costPrice = rs.getDouble("CostPrice");
                double sellingPrice = rs.getDouble("SellingPrice");
                LocalDate expiration = rs.getDate("Expiration").toLocalDate();
                boolean defective = rs.getBoolean("Defective");
                String location = rs.getString("Location");
                String defectType = rs.getString("DefectType");
                SpecificItem specificItem = new SpecificItem(ID,costPrice,sellingPrice,expiration,defective,defectType,location);
                LocalDate deliverTime = rs.getDate("DeliverTime").toLocalDate();
                specificItem.setDeliverTime(deliverTime);
                identityMap.add(specificItem);
                return specificItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes all SpecificItems from the database and clears the identity map.
     *
     * This method executes an SQL DELETE statement to remove all SpecificItems from the database.
     * After deleting the SpecificItems, it also clears the identity map to ensure that no
     * references to the removed SpecificItems remain. If an SQLException occurs during the deletion
     * process, it is printed to the standard error output.
     *
     * @throws SQLException If an SQL exception occurs while executing the DELETE statement.
     */
    public void removeAll() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM SpecificItem;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            for(int i=0; i<identityMap.size(); i++)
                identityMap.remove(identityMap.get(i));
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes all rows from the SpecificItemInItem table.
     */
    public void removeAllSpecificItemInItem() {
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM SpecificItemInItem;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}