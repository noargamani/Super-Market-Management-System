package DataAcessLayer.DAO.Suppliers;

import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.*;

public class RoutineOrdersDAO {
    private Connection conn;

    // Constructor to initialize the database connection
    public RoutineOrdersDAO(){
        try {
            createTableIfNotExists();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Function to create the table if it doesn't exist
    private void createTableIfNotExists() throws SQLException {
        //System.out.println("Creating RoutineOrder table...!!!!!!!!!!!!!!!!!!");
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS RoutineOrder (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "day_of_week INTEGER NOT NULL," +
                "item_name TEXT NOT NULL," +
                "item_description TEXT NOT NULL," +
                "amount INTEGER NOT NULL" +
                ");\n";
        stmt.executeUpdate(sql);
        conn.close();
    }


    public void createRoutineOrder(int orderID, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> items) throws SQLException {
        this.conn = Repo.connect();
        int itemOrderId = 0;
        if(orderID == -1) {
            PreparedStatement IDstmt = conn.prepareStatement("select seq from sqlite_sequence where name= 'RoutineOrder' ");
            itemOrderId = IDstmt.executeQuery().getInt("seq");
        }
        else
        {
            itemOrderId = orderID;
        }
            for (Contract.Day day : items.keySet()) {
                Hashtable<Constants.Pair<String, String>, Integer> item = items.get(day);
                for (Constants.Pair<String, String> pair : item.keySet()) {
                    String itemName = pair.first;
                    String itemDescription = pair.second;
                    int amount = item.get(pair);
                    insertRoutineOrder(itemOrderId, day, itemName, itemDescription, amount);
                }
            }
        conn.close();
    }


    // Function to retrieve an order by ID
    public Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> getRoutineOrderByID(int orderId) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RoutineOrder WHERE order_id = ?");
        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> order = new Hashtable<>();
            Hashtable<Constants.Pair<String, String>, Integer> item = new Hashtable<>();
            item.put(new Constants.Pair<>(rs.getString("item_name"), rs.getString("item_description")), rs.getInt("amount"));
            order.put(Contract.Day.values()[rs.getInt("day_of_week")], item);
            conn.close();
            return order;
        } else {
            conn.close();
            return null;
        }
    }

    private void updateRoutineOrderByID(int orderId, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> order) throws SQLException {
        deleteRoutineOrderByOrderId(orderId);
        createRoutineOrder(orderId, order);
    }


    private void insertRoutineOrder(int orderId, Contract.Day dayOfWeek, String itemName, String itemDescription, int amount) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO RoutineOrder (order_id, day_of_week, item_name, item_description, amount) VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, orderId);
        stmt.setInt(2, dayOfWeek.ordinal()); // ordinal() returns the integer value of the enum constant
        stmt.setString(3, itemName);
        stmt.setString(4, itemDescription);
        stmt.setInt(5, amount);
        stmt.executeUpdate();
    }


    public void deleteRoutineOrderByOrderId(int orderId) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM RoutineOrder WHERE order_id = ?");
        stmt.setInt(1, orderId);
        stmt.executeUpdate();
        conn.close();
    }



    // Function to close the database connection
    public void close() throws SQLException {
        conn.close();
    }

    public void updateRoutineOrder(int orderID, Hashtable<Constants.Pair<String, String>, Integer> itemsToOrder, ArrayList<Contract.Day> days) throws SQLException {
        if(getRoutineOrderByID(orderID) != null)
        {
            Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> order = getRoutineOrderByID(orderID);
            for (Contract.Day day : days) {
                order.put(day, itemsToOrder);
            }
            updateRoutineOrderByID(orderID, order);
        }
        else{
            //DO NOTHING
        }
    }


    ////order id -> day -> itemCode TO amount
    public Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> getAllRoutineOrders() {
        Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> allRoutineOrders = new Hashtable<>();
        try {
            this.conn = Repo.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM RoutineOrder");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                Contract.Day day = Contract.Day.values()[rs.getInt("day_of_week")];
                String itemName = rs.getString("item_name");
                String itemDescription = rs.getString("item_description");
                int amount = rs.getInt("amount");
                Constants.Pair<String, String> item = new Constants.Pair<>(itemName, itemDescription);
                if (allRoutineOrders.containsKey(orderId)) {
                    Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> order = allRoutineOrders.get(orderId);
                    if (order.containsKey(day)) {
                        Hashtable<Constants.Pair<String, String>, Integer> itemToAmount = order.get(day);
                        itemToAmount.put(item, amount);
                    } else {
                        Hashtable<Constants.Pair<String, String>, Integer> itemToAmount = new Hashtable<>();
                        itemToAmount.put(item, amount);
                        order.put(day, itemToAmount);
                    }
                } else {
                    Hashtable<Constants.Pair<String, String>, Integer> itemToAmount = new Hashtable<>();
                    itemToAmount.put(item, amount);
                    Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> order = new Hashtable<>();
                    order.put(day, itemToAmount);
                    allRoutineOrders.put(orderId, order);
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRoutineOrders;
    }

}

