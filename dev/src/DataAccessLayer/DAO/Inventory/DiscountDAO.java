package DataAcessLayer.DAO.Inventory;

import BusinessLayer.Inventory.Classes.Discount;
import BusinessLayer.Inventory.Classes.InventoryItem;
import DataAcessLayer.DTO.Inventory.DiscountDTO;
import DataAcessLayer.Repo;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DiscountDAO {
    private Connection conn;
    private List<Discount> identityMap;

    /**
     * Constructs a new DiscountDAO object.
     *
     * This constructor establishes a connection to the database and initializes the identity map.
     *
     * @throws Exception If there is an error establishing a connection to the database.
     */
    public DiscountDAO() {
        try {
            this.conn = Repo.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.identityMap = new LinkedList<>();
        try {
            this.identityMap = getAllDiscounts();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Builds a Discount object from the provided DiscountDTO.
     *
     * This method constructs a new Discount object using the information from the DiscountDTO,
     * including the sale number, sale name, discount amount, start date, end date, and associated items.
     * The constructed Discount object is added to the identity map.
     *
     * @param discountDTO The DiscountDTO containing the discount information.
     * @return The constructed Discount object.
     * @throws SQLException If there is an error accessing the database.
     */
    public Discount buildDiscount(DiscountDTO discountDTO) throws SQLException {
        int saleNumber = discountDTO.getSaleNumber();
        String saleName = discountDTO.getSaleName();
        double discount = discountDTO.getDiscount();
        LocalDate startDate = discountDTO.getStartDate();
        LocalDate endDate = discountDTO.getEndDate();
        List<InventoryItem> items = getItemsBySaleNumber(saleNumber);
        Discount newDiscount = new Discount(saleNumber, saleName, discount, startDate,endDate, items);
        identityMap.add(newDiscount);
        return newDiscount;
    }

    /**
     * Retrieves a list of InventoryItems associated with the given sale number.
     *
     * This method retrieves the InventoryItems associated with the specified sale number
     * from the DiscountItem table in the database. It constructs InventoryItem objects based on the
     * retrieved data using the InventoryItemDAO, and returns a list of these items.
     *
     * @param saleNumber The sale number to retrieve the associated InventoryItems for.
     * @return A list of InventoryItem objects associated with the given sale number.
     *         If no items are found or an error occurs, null is returned.
     */
    public List<InventoryItem> getItemsBySaleNumber(int saleNumber){
        InventoryItemDAO ItemDAO = new InventoryItemDAO();
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM DiscountItem WHERE SaleNumber=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, saleNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                int catalogNumber = rs.getInt("CatalogNumber");
                InventoryItem item = ItemDAO.getItemByCatalogNumber(catalogNumber);
                items.add(item);
            }
            return items;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a Discount to the database.
     *
     * This method inserts the provided Discount object into the Discount table in the database.
     * It sets the values for the SaleNumber, SaleName, Discount, StartDate, and EndDate columns
     * based on the corresponding properties of the Discount object. If the Discount object
     * includes InventoryItems to be included in the discount, it calls the addItemsToDiscount method
     * to associate those items with the discount in the database. Finally, it adds the Discount object
     * to the identityMap for future reference.
     *
     * @param discount The Discount object to add to the database.
     */
    public void addDiscount(Discount discount){
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Discount (SaleNumber, SaleName, Discount, StartDate, EndDate) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, discount.getSaleNumber());
            stmt.setString(2, discount.getSaleName());
            stmt.setDouble(3, discount.getDiscount());
            stmt.setDate(4, Date.valueOf(discount.getStartDate()));
            stmt.setDate(5, Date.valueOf(discount.getEndDate()));
            stmt.executeUpdate();
            if(discount.getIncludeInDiscount().size() != 0){
                addItemsToDiscount(discount.getIncludeInDiscount(), discount.getSaleNumber());
            }
            identityMap.add(discount);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Associates InventoryItems with a Discount in the database.
     *
     * This method associates the provided list of InventoryItems with a specific discount
     * identified by the saleNumber in the DiscountItem table in the database. It iterates over
     * the items list and inserts each item's CatalogNumber along with the saleNumber into the
     * DiscountItem table. This establishes the association between the items and the discount
     * in the database.
     *
     * @param items The list of InventoryItems to associate with the discount.
     * @param saleNumber The sale number of the discount.
     */
    public void addItemsToDiscount(List<InventoryItem> items, int saleNumber){
        PreparedStatement statement = null;
        try{
            for(int i=0; i<items.size(); i++) {
                InventoryItem item = items.get(i);
                String sql = "INSERT INTO DiscountItem (SaleNumber, CatalogNumber)\n" +
                        "VALUES (?, ?);";
                statement = conn.prepareStatement(sql);
                statement.setInt(1, saleNumber);
                statement.setInt(2, item.getCatalogNumber());
                statement.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the information of a Discount in the database.
     *
     * This method updates the information of a discount in the database based on the provided
     * Discount object. It executes an SQL UPDATE statement to modify the SaleName, Discount,
     * StartDate, and EndDate columns in the Discount table for the discount with the matching
     * SaleNumber. The updated information is obtained from the Discount object.
     *
     * @param discount The Discount object containing the updated information.
     * @throws SQLException If an SQL exception occurs during the update process.
     */
    public void updateDiscount(Discount discount) throws SQLException {
        try{
            String sql = "UPDATE Discount SET SaleName = ?, Discount = ?, StartDate = ?, " +
                    "EndDate = ? WHERE SaleNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, discount.getSaleName());
            stmt.setDouble(2, discount.getDiscount());
            stmt.setDate(3, Date.valueOf(discount.getStartDate()));
            stmt.setDate(4, Date.valueOf(discount.getEndDate()));
            stmt.setInt(5, discount.getSaleNumber());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Retrieves a Discount object from the database based on the sale number.
     *
     * This method retrieves a Discount object from the database that matches the provided
     * saleNumber. It first checks if the discount is already present in the identity map,
     * and if so, it returns the cached discount. If the discount is not found in the
     * identity map, it executes an SQL SELECT statement to fetch the discount information
     * from the Discount table in the database. The fetched information is used to create
     * a new Discount object, which is then returned. If no discount is found for the
     * provided saleNumber, null is returned.
     *
     * @param saleNumber The sale number of the discount to retrieve.
     * @return The Discount object matching the sale number, or null if not found.
     */
    public Discount getDiscountBySaleNumber(int saleNumber){
        for (Discount discount : identityMap){
            if(discount.getSaleNumber() == saleNumber){
                return discount;
            }
        }
        String sql = "SELECT * FROM Discount WHERE SaleNumber=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, saleNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String saleName = rs.getString("SaleName");
                double Discount = rs.getDouble("Discount");
                LocalDate startDate = rs.getDate("StartDate").toLocalDate();
                LocalDate endDate = rs.getDate("EndDate").toLocalDate();
                List<InventoryItem> items = getItemsBySaleNumber(saleNumber);
                Discount discount = new Discount(saleNumber, saleName, Discount, startDate, endDate, items);
                return discount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes all discounts from the database.
     *
     * This method deletes all records from the Discount table in the database and clears
     * the identityMap of discounts.
     *
     * @throws SQLException if an error occurs while accessing the database
     */
    public void removeAll() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM Discount;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

            identityMap.clear();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all discounts from the database.
     *
     * This method retrieves all discounts from the Discount table in the database.
     * It executes an SQL SELECT statement to fetch the discount information for all
     * discounts. For each fetched discount, it checks if the discount is already present
     * in the identity map. If not, it creates a new Discount object using the fetched
     * information and adds it to the identity map and the list of discounts. Finally,
     * it returns the list of discounts.
     *
     * @return A list of all discounts retrieved from the database.
     * @throws SQLException If an error occurs while executing the SQL statement.
     */
    public List<Discount> getAllDiscounts() throws SQLException {
        List<Discount> discounts = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Discount") ;
            while (rs.next()) {
                int SaleNumber = rs.getInt("SaleNumber");
                Discount discount = getDiscountBySaleNumber(SaleNumber);

                if(discount != null) {
                    if (!identityMap.contains(discount))
                        identityMap.add(discount);
                    discounts.add(discount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }


    /**
     * Removes a discount from the database.
     *
     * This method removes a discount from the Discount table in the database based on the
     * provided saleNumber. It first retrieves the discount object using the getDiscountBySaleNumber
     * method and removes it from the identity map. Then, it executes an SQL DELETE statement to
     * remove the corresponding discount record from the Discount table in the database.
     *
     * @param saleNumber The sale number of the discount to be removed.
     * @throws SQLException If an error occurs while executing the SQL statement.
     */
    public void removeDiscount(int saleNumber) throws SQLException {
        try {
            Discount discount = getDiscountBySaleNumber(saleNumber);
            identityMap.remove(discount);

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM Discount WHERE SaleNumber = ?"
            );
            stmt.setInt(1, saleNumber);
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Removes all discount items from the DiscountItem table.
     *
     * @throws SQLException If a database access error occurs.
     */
    public void removeAllDiscountItem() throws SQLException{
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM DiscountItem;";
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}