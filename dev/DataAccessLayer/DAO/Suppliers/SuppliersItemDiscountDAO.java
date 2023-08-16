package DataAcessLayer.DAO.Suppliers;





import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.SupplierItemDiscountDTO;
import DataAcessLayer.Repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliersItemDiscountDAO implements DAO {
    private Connection conn;

    public SuppliersItemDiscountDAO() {
        try {
            this.conn = Repo.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS TotalOrderDiscount (\n" +
                        "    supplier TEXT,\n" +
                        "    price REAL,\n" +
                        "    discount INTEGER,\n" +
                        "    type TEXT,\n" +
                        "    PRIMARY KEY (supplier, price)\n" +
                        ");"
        );
        stmt.executeUpdate();
    }

    public void insertTotalOrderDiscount(SupplierItemDiscountDTO totalOrderDiscount) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO TotalOrderDiscount(ItemId, price, discount, type) VALUES (?, ?, ?, ?)"
        );
        pstmt.setInt(1, totalOrderDiscount.getItemId());
        pstmt.setDouble(2, totalOrderDiscount.getPrice());
        pstmt.setInt(3, totalOrderDiscount.getDiscount());
        pstmt.setString(4, totalOrderDiscount.getType());
        pstmt.executeUpdate();
    }

    public ArrayList<SupplierItemDiscountDTO> getAllTotalOrderDiscounts() throws SQLException {
        ArrayList<SupplierItemDiscountDTO> totalOrderDiscounts = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT ItemId, price, discount, type FROM TotalOrderDiscount"
        );
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int supplier = rs.getInt("ItemId");
            double price = rs.getDouble("price");
            int discount = rs.getInt("discount");
            String type = rs.getString("type");
            SupplierItemDiscountDTO totalOrderDiscount = new SupplierItemDiscountDTO(supplier, price, discount, type);
            totalOrderDiscounts.add(totalOrderDiscount);
        }
        return totalOrderDiscounts;
    }



    @Override
    public void insert(Object d) {

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
}
