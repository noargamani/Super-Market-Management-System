package DataAcessLayer.DAO.Suppliers;





import DataAcessLayer.DTO.Suppliers.ItemDiscountDTO;
import DataAcessLayer.Repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ItemDiscountDAO {

    private Connection connection;

    public ItemDiscountDAO() {
        try {
            createTable();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.connection = Repo.connect();
        /*
        String remove = "DROP TABLE IF EXISTS ItemDiscount";
        PreparedStatement rstmt = connection.prepareStatement(remove);
        rstmt.executeUpdate();

         */
        PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS ItemDiscount (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    itemId INTEGER,\n" +
                        "    quantity INTEGER,\n" +
                        "    discount INTEGER\n" +
                        ");\n"
        );
        stmt.executeUpdate();
        //System.out.println("Table ItemDiscount created successfully!!!!!!!!!");
        connection.close();
    }

    public List<ItemDiscountDTO> findAll() throws SQLException {
        this.connection = Repo.connect();
        List<ItemDiscountDTO> itemDiscounts = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, itemId, quantity, discount FROM ItemDiscount");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ItemDiscountDTO itemDiscount = new ItemDiscountDTO(
                    resultSet.getInt("id"),
                    resultSet.getInt("itemId"),
                    resultSet.getInt("quantity"),
                    resultSet.getInt("discount")
            );
            itemDiscounts.add(itemDiscount);
        }
        connection.close();
        return itemDiscounts;
    }

    public ItemDiscountDTO findById(int id) throws SQLException {
        this.connection = Repo.connect();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT itemId, quantity, discount FROM ItemDiscount WHERE id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        ItemDiscountDTO itemDiscount = null;
        if (resultSet.next()) {
            itemDiscount = new ItemDiscountDTO(
                    id,
                    resultSet.getInt("itemId"),
                    resultSet.getInt("quantity"),
                    resultSet.getInt("discount")
            );
        }
        connection.close();
        return itemDiscount;
    }

    public SortedMap<Integer,Integer> findByItemId(int itemId) throws SQLException {
        this.connection = Repo.connect();
        SortedMap<Integer,Integer> itemDiscounts = new TreeMap<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, quantity, discount FROM ItemDiscount WHERE itemId = ?");
        statement.setInt(1, itemId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ItemDiscountDTO itemDiscount = new ItemDiscountDTO(
                    resultSet.getInt("id"),
                    itemId,
                    resultSet.getInt("quantity"),
                    resultSet.getInt("discount")
            );
            itemDiscounts.put(itemDiscount.getQuantity(),itemDiscount.getDiscount());
        }
        connection.close();
        return itemDiscounts;
    }

    public void insert(ItemDiscountDTO itemDiscount) throws SQLException {
        this.connection = Repo.connect();
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO ItemDiscount (itemId, quantity, discount) VALUES (?, ?, ?)");
        statement.setInt(1, itemDiscount.getItemId());
        statement.setInt(2, itemDiscount.getQuantity());
        statement.setInt(3, itemDiscount.getDiscount());
        statement.executeUpdate();
        connection.close();
    }

    public void update(ItemDiscountDTO itemDiscount) throws SQLException {
        this.connection = Repo.connect();
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE ItemDiscount SET itemId = ?, quantity = ?, discount = ? WHERE id = ?");
        statement.setInt(1, itemDiscount.getItemId());
        statement.setInt(2, itemDiscount.getQuantity());
        statement.setInt(3, itemDiscount.getDiscount());
        statement.setInt(4, itemDiscount.getId());
        statement.executeUpdate();
        connection.close();
    }

    public void delete(int id) throws SQLException {
        this.connection = Repo.connect();
        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM ItemDiscount WHERE id = ?");
        statement.setInt(1, id);
        statement.executeUpdate();
        connection.close();
    }
}