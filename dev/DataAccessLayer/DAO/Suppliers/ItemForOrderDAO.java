package DataAcessLayer.DAO.Suppliers;



import BusinessLayer.Suppliers.Classes.ItemForOrder;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.ItemForOrderDTO;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class ItemForOrderDAO implements DAO {
    private Connection conn;

    public ItemForOrderDAO() {
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS Item_for_Order (\n" +
                "    OrderId INT,\n" +
                "    CatalogID INT,\n" +
                "    total_price_without_discount DOUBLE,\n" +
                "    total_discount DOUBLE,\n" +
                "    final_price DOUBLE,\n" +
                "    orderAmount INT,\n" +
                "    PRIMARY KEY (OrderId, CatalogID),\n" +
                "    FOREIGN KEY (OrderId) REFERENCES Orders(OrderId),\n" +
                "    FOREIGN KEY (CatalogID) REFERENCES SupplierItem(supplier_catalogID)\n" +
                ");\n");
        pstmt.executeUpdate();
        this.conn.close();
    }

    public void insertItemForOrder(ItemForOrderDTO itemForOrder) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Item_for_Order(OrderId, CatalogID, total_price_without_discount, total_discount, final_price, orderAmount) VALUES (?, ?, ?, ?, ?, ?)");
        pstmt.setInt(1, itemForOrder.getOrderId());
        pstmt.setInt(2, itemForOrder.getCatalogId());
        pstmt.setDouble(3, itemForOrder.getTotalPriceWithoutDiscount());
        pstmt.setDouble(4, itemForOrder.getTotalDiscount());
        pstmt.setDouble(5, itemForOrder.getFinalPrice());
        pstmt.setInt(6, itemForOrder.getOrderAmount());
        pstmt.executeUpdate();
        this.conn.close();
    }

    public ArrayList<ItemForOrderDTO> getAllItemsForOrder() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<ItemForOrderDTO> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Item_for_Order");

        while (rs.next()) {
            int orderId = rs.getInt("OrderId");
            int catalogId = rs.getInt("CatalogID");
            double totalPriceWithoutDiscount = rs.getDouble("total_price_without_discount");
            double totalDiscount = rs.getDouble("total_discount");
            double finalPrice = rs.getDouble("final_price");
            int orderAmount = rs.getInt("orderAmount");

            ItemForOrderDTO item = new ItemForOrderDTO(orderId, catalogId, totalPriceWithoutDiscount, totalDiscount, finalPrice, orderAmount);
            items.add(item);
        }
        this.conn.close();

        return items;
    }

    public Hashtable<Integer,ItemForOrder> getAllItemsForOrderByOrderId(int id) throws SQLException {
        this.conn = Repo.connect();
        Hashtable<Integer,ItemForOrder> items = new Hashtable<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Item_for_Order where OrderId="+String.valueOf(id));

        while (rs.next()) {
            int orderId = rs.getInt("OrderId");
            int catalogId = rs.getInt("CatalogID");
            double totalPriceWithoutDiscount = rs.getDouble("total_price_without_discount");
            double totalDiscount = rs.getDouble("total_discount");
            double finalPrice = rs.getDouble("final_price");
            int orderAmount = rs.getInt("orderAmount");

            ItemForOrder item = new ItemForOrder(orderId, catalogId, totalPriceWithoutDiscount, totalDiscount, finalPrice, orderAmount);
            items.put(item.getItemCatalogID(),item);
        }
        this.conn.close();

        return items;
    }

    @Override
    public void insert(Object d) {
     try {
         ItemForOrder item=(ItemForOrder) d;
         // ItemForOrderDTO itemForOrderDTO=new ItemForOrderDTO(item.)
     }
     catch (Exception e){

     }
    }

    @Override
    public void delete(DTO d) {

    }

    @Override
    public void update(DTO d) {

    }

    @Override
    public DTO get(Object primary) {
        return null;
    }

    public void deleteItemForOrderByOrderId(int orderID) throws SQLException {
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM Item_for_Order WHERE OrderId=" + orderID);
        this.conn.close();
    }

    public void updateItemsForOrder(int orderID, Hashtable<Integer, ItemForOrder> items) throws SQLException {
            deleteItemForOrderByOrderId(orderID);
            for (ItemForOrder item : items.values()) {
                insertItemForOrder(new ItemForOrderDTO(orderID, item.getItemCatalogID(),
                        item.getTotalPriceWithoutItemDisc(), item.getTotalDiscount(),
                        1, item.getOrderAmount()));
            }
    }
}