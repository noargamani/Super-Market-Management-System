package DataAcessLayer.DAO.Suppliers;


import BusinessLayer.Suppliers.Classes.ItemForOrder;
import BusinessLayer.Suppliers.Classes.Order;
import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.ItemForOrderDTO;
import DataAcessLayer.DTO.Suppliers.OrderDTO;
import DataAcessLayer.Repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Hashtable;

public class OrderDAO{
    private Connection conn;

    ItemForOrderDAO itemForOrderDAO;

    private Hashtable<String, Hashtable<Integer, Order>> Orders;

    boolean OrdersFlag;

    public OrderDAO() {
        try {
            Orders = new Hashtable<>();
            itemForOrderDAO = new ItemForOrderDAO();
            OrdersFlag = false;
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Orders (\n" +
                "    OrderId INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Status TEXT,\n" +
                "    dateOrderIssued DATE,\n" +
                "    supplierName TEXT,\n" +
                "    totalQuantity INTEGER,\n" +
                "    price_before_discount REAL\n" +
                ");\n");
        conn.close();
    }

    private int insertOrder(OrderDTO order) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Orders(Status, dateOrderIssued, supplierName, totalQuantity, price_before_discount) VALUES (?, ?, ?, ?, ?)");
        pstmt.setString(1, order.getStatus());
        pstmt.setDate(2, new java.sql.Date(order.getDateOrderIssued().getTime()));
        pstmt.setString(3, order.getSupplierName());
        pstmt.setInt(4, order.getTotalQuantity());
        pstmt.setDouble(5, order.getPriceBeforeDiscount());
        pstmt.executeUpdate();
        PreparedStatement stmt = conn.prepareStatement("select seq from sqlite_sequence where name= 'Orders' ");
        int id = stmt.executeQuery().getInt("seq");
        this.conn.close();
        return id;
    }

    public ArrayList<OrderDTO> getAllOrdersSQL() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<OrderDTO> orders = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Orders");

        while (rs.next()) {
            int orderId = rs.getInt("OrderId");
            String status = rs.getString("Status");
            Date dateOrderIssued = rs.getDate("dateOrderIssued");
            String supplierName = rs.getString("supplierName");
            int totalQuantity = rs.getInt("totalQuantity");
            double priceBeforeDiscount = rs.getDouble("price_before_discount");
            OrderDTO order = new OrderDTO(orderId, status, dateOrderIssued, supplierName, totalQuantity, priceBeforeDiscount);

            orders.add(order);
        }
        this.conn.close();

        return orders;
    }
    public Hashtable<String, Hashtable<Integer,Order>> getAllOrders()
    {
        try {
            if (OrdersFlag) {
                return Orders;
            }
            ArrayList<OrderDTO> orders = getAllOrdersSQL();
            for(OrderDTO order : orders)
            {
                getAllOrdersBySupplier(order.getSupplierName());
            }
            OrdersFlag = true;
            return Orders;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Hashtable<Integer,Order> getAllOrdersBySupplier(String supplier) throws SQLException {
        this.conn = Repo.connect();
        if(Orders.containsKey(supplier))
            return Orders.get(supplier);
        Hashtable<Integer,Order> orders = new Hashtable<>();
        //Statement stmt = conn.createStatement();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Orders Where supplierName=?");
        stmt.setString(1,supplier);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {
            int orderId = rs.getInt("OrderId");
            String status = rs.getString("Status");
            Date dateOrderIssued = rs.getDate("dateOrderIssued");
            String supplierName = rs.getString("supplierName");
            int totalQuantity = rs.getInt("totalQuantity");
            double priceBeforeDiscount = rs.getDouble("price_before_discount");

            Hashtable<Integer,ItemForOrder> items=itemForOrderDAO.getAllItemsForOrderByOrderId(orderId);
            Order order1=new Order(orderId, BusinessLayer.Suppliers.Classes.Order.Status.valueOf(status),
                    LocalDate.of(dateOrderIssued.getYear(),dateOrderIssued.getMonth(),dateOrderIssued.getDay()+1),
                    1,supplier,totalQuantity,items,priceBeforeDiscount);
            orders.put(orderId,order1);
        }
        Orders.put(supplier,orders);
        this.conn.close();

        return orders;
    }

    public Order insert(Object d) {
       try {
           Order order=(Order) d;
           String supplier=order.getSupplier();
           Date dateOrderIssued=Date.valueOf(order.getDateOrderIssued());
           OrderDTO orderDTO=new OrderDTO(order.getOrderID(), order.getStatus().toString(),dateOrderIssued,supplier,order.getTotalQuantity(),order.getTPriceWithoutDiscount());
           int orderId = insertOrder(orderDTO);
           Order newOrder= new Order(orderId,order.getStatus(),order.getDateOrderIssued(),1, order.getSupplier(),order.getTotalQuantity()
                   ,order.getItems(),order.getTPriceWithoutDiscount());
           for (ItemForOrder item:order.getItems().values()){
               ItemForOrderDTO itemForOrderDTO=new ItemForOrderDTO(orderId,item.getItemCatalogID(),item.getTotalPriceWithoutItemDisc(),item.getTotalDiscount(),0,item.getOrderAmount());
               itemForOrderDAO.insertItemForOrder(itemForOrderDTO);
           }
           if(Orders.containsKey(supplier))
               Orders.get(supplier).put(orderId,newOrder);
           else{
               Hashtable<Integer,Order> orders=new Hashtable<>();
               orders.put(orderId,newOrder);
               Orders.put(supplier,orders);
           }
           return newOrder;
       }
       catch (Exception e){
           System.out.println(e.getMessage());
       }
       return null;
    }

    public void remove(int orderID, String supplier) throws SQLException {
        try {
            deleteOrder(orderID);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if(Orders.containsKey(supplier))
            Orders.get(supplier).remove(orderID);
    }

    private void deleteOrder(int orderID) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Orders WHERE OrderId = ?");
        pstmt.setInt(1, orderID);
        pstmt.executeUpdate();
        itemForOrderDAO.deleteItemForOrderByOrderId(orderID);
        this.conn.close();
    }

    public void updateSupplierOrder(int orderID, String supplier, Order order) {
        try {
            updateOrder(orderID, order);
            if(Orders.containsKey(supplier))
                Orders.get(supplier).replace(orderID, order);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

        private void updateOrder(int orderID, Order order) throws SQLException {
            this.conn = Repo.connect();
            LocalDate dateOrderIssued = order.getDateOrderIssued();
            Date date = Date.valueOf(dateOrderIssued);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Orders SET Status = ?, dateOrderIssued = ?, supplierName = ?, totalQuantity = ?, price_before_discount = ? WHERE OrderId = ?");
            pstmt.setString(1, order.getStatus().toString());
            pstmt.setDate(2, date);
            pstmt.setString(3, order.getSupplier());
            pstmt.setInt(4, order.getTotalQuantity());
            pstmt.setDouble(5, order.getTPriceWithoutDiscount());
            pstmt.setInt(6, orderID);
            pstmt.executeUpdate();
            itemForOrderDAO.updateItemsForOrder(orderID, order.getItems());
            this.conn.close();
        }

    public DTO get(Object primary) {
        return null;
    }

    //TODO: implement
    public Order CreateOrder(Order order) {
        return insert(order);
    }

    public Order.Status updateOrderStatus(String supplierName, int orderId, String status) {
        try {
            Order order = Orders.get(supplierName).get(orderId);
            order.setStatus(Order.Status.valueOf(status));
            updateOrder(orderId, order);
            return order.getStatus();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addItemToOrder(int orderID, int catalogID, int quantity, Supplier supplier) {
        try {
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
