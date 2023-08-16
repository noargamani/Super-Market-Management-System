package DataAcessLayer.DAO.Suppliers;



import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.Supplier_Card;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.Repo;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.Suppliers.SupplierCardDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class SupplierCardDAO implements DAO {
    private Connection conn;
    Hashtable<Integer, Supplier_Card> supplierCards;

    public SupplierCardDAO() {
        try {
            supplierCards = new Hashtable<>();
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        String query = "CREATE TABLE IF NOT EXISTS Supplier_Card (\n" +
                "    BN_number INT PRIMARY KEY,\n" +
                "    pay_condition TEXT,\n" +
                "    bank_account TEXT\n" +
                ");";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.executeUpdate();
        this.conn.close();
    }

    public void insertSupplierCard(SupplierCardDTO supplierCardDTO) throws SQLException {
        this.conn = Repo.connect();
        String query = "INSERT INTO Supplier_Card(BN_number, pay_condition, bank_account) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, supplierCardDTO.getBnNumber());
        pstmt.setString(2, supplierCardDTO.getPayCondition());
        pstmt.setString(3, supplierCardDTO.getBankAccount());
        pstmt.executeUpdate();
        supplierCards.put(supplierCardDTO.getBnNumber(), new Supplier_Card(supplierCardDTO.getBnNumber(),
                supplierCardDTO.getBankAccount(), supplierCardDTO.getPayCondition()));
        this.conn.close();
    }

    public SupplierCardDTO getSupplierCardByBnNumber(int bnNumber) throws SQLException {
        this.conn = Repo.connect();
        String query = "SELECT * FROM Supplier_Card WHERE BN_number=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, bnNumber);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String payCondition = rs.getString("pay_condition");
            String bankAccount = rs.getString("bank_account");
            this.conn.close();
            return new SupplierCardDTO(bnNumber, payCondition, bankAccount);
        }
        this.conn.close();
        return null;
    }

    public void updateSupplierCard(SupplierCardDTO supplierCardDTO) throws SQLException {
        this.conn = Repo.connect();
        String query = "UPDATE Supplier_Card SET pay_condition=?, bank_account=? WHERE BN_number=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, supplierCardDTO.getPayCondition());
        pstmt.setString(2, supplierCardDTO.getBankAccount());
        pstmt.setInt(3, supplierCardDTO.getBnNumber());
        pstmt.executeUpdate();
        supplierCards.replace(supplierCardDTO.getBnNumber(), new Supplier_Card(supplierCardDTO.getBnNumber(),
                supplierCardDTO.getBankAccount(), supplierCardDTO.getPayCondition()));
        this.conn.close();
    }

    public void deleteSupplierCardByBnNumber(int bnNumber) throws SQLException {
        this.conn = Repo.connect();
        String query = "DELETE FROM Supplier_Card WHERE BN_number=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, bnNumber);
        pstmt.executeUpdate();
        supplierCards.remove(bnNumber);
        this.conn.close();
    }

    @Override
    public void insert(Object d) {
        Supplier s = (Supplier) d;
        Supplier_Card supplierCard= s.getSupplier_card();
        SupplierCardDTO supplierCardDTO = new SupplierCardDTO(supplierCard.getBN_number(),
                supplierCard.getPay_condition(), supplierCard.getBank_account());
        try {
            insertSupplierCard(supplierCardDTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public Supplier_Card getSupplierCardOfSupplier(Integer BN_number) {
        if(supplierCards.containsKey(BN_number))
            return supplierCards.get(BN_number);
        else {
            try {
                SupplierCardDTO supplierCardDTO = getSupplierCardByBnNumber(BN_number.intValue());
                if (supplierCardDTO != null) {
                    supplierCards.put(supplierCardDTO.getBnNumber(), new Supplier_Card(supplierCardDTO.getBnNumber(),
                            supplierCardDTO.getBankAccount(), supplierCardDTO.getPayCondition()));
                    return supplierCards.get(supplierCardDTO.getBnNumber());
                }
                else {
                    throw new Exception("Supplier card not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
