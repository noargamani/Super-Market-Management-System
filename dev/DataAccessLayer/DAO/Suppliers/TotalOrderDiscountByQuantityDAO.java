package DataAcessLayer.DAO.Suppliers;





import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.Repo;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.Suppliers.TotalOrderDiscountByQuantityDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.SortedMap;

public class TotalOrderDiscountByQuantityDAO implements DAO {
    private Connection conn;
    private Hashtable<String, SortedMap<Integer,Integer>> discountByTotalQuantity;//quantity->discount

    private boolean flag;

    public TotalOrderDiscountByQuantityDAO() {
        try {
            //this.conn = Repo.connect();
            discountByTotalQuantity = new Hashtable<>();
            flag = false;
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS TotalOrderDiscountQuantity (\n" +
                        "    supplier TEXT,\n" +
                        "    quantity INTEGER,\n" +
                        "    discount INTEGER,\n" +
                        "    PRIMARY KEY (supplier, quantity)\n" +
                        ");"
        );
        stmt.executeUpdate();
        conn.close();
    }
    private void addDiscount(String supplier, int quantity, int discount)
    {
        if(discountByTotalQuantity.containsKey(supplier))
            discountByTotalQuantity.get(supplier).put(quantity, discount);
        else
        {
            SortedMap<Integer, Integer> discounts = new java.util.TreeMap<>();
            discounts.put(quantity, discount);
            discountByTotalQuantity.put(supplier, discounts);
        }
    }

    public void insertTotalOrderDiscount(TotalOrderDiscountByQuantityDTO totalOrderDiscount) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO TotalOrderDiscountQuantity(supplier, quantity, discount) VALUES (?, ?, ?)"
        );
        pstmt.setString(1, totalOrderDiscount.getSupplier());
        pstmt.setInt(2, totalOrderDiscount.getQuantity());
        pstmt.setInt(3, totalOrderDiscount.getDiscount());
        pstmt.executeUpdate();
        if(flag) {
            addDiscount(totalOrderDiscount.getSupplier(), totalOrderDiscount.getQuantity(), totalOrderDiscount.getDiscount());
        }
        conn.close();
    }

    public ArrayList<TotalOrderDiscountByQuantityDTO> getAllTotalOrderDiscounts() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<TotalOrderDiscountByQuantityDTO> totalOrderDiscounts = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT supplier, quantity, discount FROM TotalOrderDiscountQuantity"
        );
        ResultSet rs = stmt.executeQuery();
        flag = true;
        while (rs.next()) {
            String supplier = rs.getString("supplier");
            int quantity = rs.getInt("quantity");
            int discount = rs.getInt("discount");
            TotalOrderDiscountByQuantityDTO totalOrderDiscount = new TotalOrderDiscountByQuantityDTO(supplier, quantity, discount);
            addDiscount(supplier, quantity, discount);
            totalOrderDiscounts.add(totalOrderDiscount);
        }
        conn.close();
        return totalOrderDiscounts;
    }

    @Override
    public void insert(Object d) {
        Supplier s = (Supplier) d;
        s.getDiscountByTotalQuantity().forEach((quantity, discount) -> {
            try {
                insertTotalOrderDiscount(new TotalOrderDiscountByQuantityDTO(s.getSupplier_name(), quantity, discount));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

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

    public SortedMap<Integer, Integer> getOrderDiscountsOfSupplier(String supplierName) {
        try
        {
            this.conn = Repo.connect();
            if(discountByTotalQuantity.containsKey(supplierName))
                return discountByTotalQuantity.get(supplierName);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT quantity, discount FROM TotalOrderDiscountQuantity WHERE supplier = ?"
            );
            stmt.setString(1, supplierName);
            ResultSet rs = stmt.executeQuery();
            SortedMap<Integer, Integer> orderDiscounts = new java.util.TreeMap<>();
            while (rs.next()) {
                int quantity = rs.getInt("quantity");
                int discount = rs.getInt("discount");
                orderDiscounts.put(quantity, discount);
            }
            discountByTotalQuantity.put(supplierName, orderDiscounts);
            this.conn.close();
            return orderDiscounts;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
