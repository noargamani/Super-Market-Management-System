package DataAcessLayer.DAO.Suppliers;





import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.Repo;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.Suppliers.TotalOrderDiscountByPriceDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.SortedMap;

public class TotalOrderDiscountByPriceDAO implements DAO {
    private Connection conn;
    private Hashtable<String, SortedMap<Double,Integer>> discountByTotalPrice;//price->discount

    private boolean flag;

    public TotalOrderDiscountByPriceDAO() {
        try {
            discountByTotalPrice = new Hashtable<>();
            flag = false;
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        conn = Repo.connect();
        PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS TotalOrderDiscountPrice (\n" +
                        "    supplier TEXT,\n" +
                        "    price REAL,\n" +
                        "    discount INTEGER,\n" +
                        "    type TEXT,\n" +
                        "    PRIMARY KEY (supplier, price)\n" +
                        ");"
        );
        stmt.executeUpdate();
        conn.close();
    }

    private void addDiscount(String supplier, double price, int discount)
    {
        if(discountByTotalPrice.containsKey(supplier))
            discountByTotalPrice.get(supplier).put(price, discount);
        else
        {
            SortedMap<Double, Integer> discounts = new java.util.TreeMap<>();
            discounts.put(price, discount);
            discountByTotalPrice.put(supplier, discounts);
        }
    }

    public void insertTotalOrderDiscount(TotalOrderDiscountByPriceDTO totalOrderDiscount) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO TotalOrderDiscountPrice(supplier, price, discount) VALUES (?, ?, ?)"
        );
        pstmt.setString(1, totalOrderDiscount.getSupplier());
        pstmt.setDouble(2, totalOrderDiscount.getPrice());
        pstmt.setInt(3, totalOrderDiscount.getDiscount());
        pstmt.executeUpdate();
        if(flag) {
            addDiscount(totalOrderDiscount.getSupplier(), totalOrderDiscount.getPrice(), totalOrderDiscount.getDiscount());
        }
        conn.close();
    }

    public ArrayList<TotalOrderDiscountByPriceDTO> getAllTotalOrderDiscounts() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<TotalOrderDiscountByPriceDTO> totalOrderDiscounts = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT supplier, price, discount, type FROM TotalOrderDiscountPrice"
        );
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String supplier = rs.getString("supplier");
            double price = rs.getDouble("price");
            int discount = rs.getInt("discount");
            TotalOrderDiscountByPriceDTO totalOrderDiscount = new TotalOrderDiscountByPriceDTO(supplier, price, discount);
            totalOrderDiscounts.add(totalOrderDiscount);
            addDiscount(supplier, price, discount);
        }
        flag = true;
        conn.close();
        return totalOrderDiscounts;
    }

    @Override
    public void insert(Object d) {
        Supplier s = (Supplier) d;
        s.getDiscountByTotalPrice().forEach((price, discount) -> {
            try {
                insertTotalOrderDiscount(new TotalOrderDiscountByPriceDTO(s.getSupplier_name(), price, discount));
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

    public SortedMap<Double, Integer> getOrderDiscountsOfSupplier(String supplierName) {
        try
        {
            conn = Repo.connect();
            if(discountByTotalPrice.containsKey(supplierName))
                return discountByTotalPrice.get(supplierName);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT price, discount FROM TotalOrderDiscountPrice WHERE supplier = ?"
            );
            stmt.setString(1, supplierName);
            ResultSet rs = stmt.executeQuery();
            SortedMap<Double, Integer> orderDiscounts = new java.util.TreeMap<>();
            while (rs.next()) {
                double price = rs.getDouble("price");
                int discount = rs.getInt("discount");
                orderDiscounts.put(price, discount);
            }
            discountByTotalPrice.put(supplierName, orderDiscounts);
            conn.close();
            return orderDiscounts;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
