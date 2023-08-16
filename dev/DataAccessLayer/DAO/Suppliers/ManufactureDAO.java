package DataAcessLayer.DAO.Suppliers;

import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.ManufactureDTO;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class ManufactureDAO implements DAO {
    private Hashtable<String, ArrayList<String>> manufactures;
    private Connection conn;

    public ManufactureDAO() {
        try {
            manufactures = new Hashtable<>();
            createTable();
        }
        catch (Exception e) {
            // handle exception
        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS manufacture (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    supplier TEXT,\n" +
                "    name TEXT NOT NULL\n" +
                ");\n");
        this.conn.close();
    }

    public void insertManufacture(ManufactureDTO manufacture) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO manufacture(supplier, name) VALUES (?, ?)");
        pstmt.setString(1, manufacture.getSupplier());
        pstmt.setString(2, manufacture.getName());
        pstmt.executeUpdate();
        this.conn.close();
    }

    public ArrayList<ManufactureDTO> getAllManufactures() throws SQLException {
        ArrayList<ManufactureDTO> manufactures = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM manufacture");

        while (rs.next()) {
            int id = rs.getInt("id");
            String supplier = rs.getString("supplier");
            String name = rs.getString("name");
            ManufactureDTO manufacture = new ManufactureDTO(id, supplier, name);
            manufactures.add(manufacture);
        }

        return manufactures;
    }
    public ArrayList<String> getManufacturesNamesOfSupplier(String supplier_name) {
        try {
            this.conn = Repo.connect();
            if (manufactures.containsKey(supplier_name)) {
                return manufactures.get(supplier_name);
            } else {
                ArrayList<ManufactureDTO> manufactures = getAllManufacturesFromSupplier(supplier_name);
                ArrayList<String> manufactures_names = new ArrayList<>();
                for (ManufactureDTO manufacture : manufactures) {
                    manufactures_names.add(manufacture.getName());
                }
                this.manufactures.put(supplier_name, manufactures_names);
            }
            this.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manufactures.get(supplier_name);
    }
    public ArrayList<ManufactureDTO> getAllManufacturesFromSupplier(String sup) throws SQLException {
        this.conn = Repo.connect();
        ArrayList<ManufactureDTO> manufactures = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM manufacture WHERE supplier = '" + sup + "'");

        while (rs.next()) {
            int id = rs.getInt("id");
            String supplier = rs.getString("supplier");
            String name = rs.getString("name");
            ManufactureDTO manufacture = new ManufactureDTO(id, supplier, name);
            manufactures.add(manufacture);
        }
        this.conn.close();
        return manufactures;
    }

    @Override
    public void insert(Object d) {
        try {


            Supplier s = (Supplier) d;
            for(String man:s.getManufactures()) {
                ManufactureDTO contractDTO = new ManufactureDTO(1,s.getSupplier_name(),man);
                insertManufacture(contractDTO);
            }

        }catch (Exception e){

        }
    }

    public void insertManufacture(String supplier,String manufacture) {
        try {
            ManufactureDTO manufactureDTO = new ManufactureDTO(1,supplier,manufacture);
            insertManufacture(manufactureDTO);
        }catch (Exception e){

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
}