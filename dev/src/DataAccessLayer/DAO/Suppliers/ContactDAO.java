package DataAcessLayer.DAO.Suppliers;


import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.Repo;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.Suppliers.ContactDTO;

import java.sql.*;
import java.util.*;

public class ContactDAO implements DAO {
    private Connection conn;
    private Hashtable<String, SortedMap<String,String>> contact_List;// name->phone number


    public ContactDAO() {
        try {
            contact_List=new Hashtable<>();
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        conn = Repo.connect();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Contact (\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  supplierName TEXT,\n" +
                "  name TEXT,\n" +
                "  phone TEXT,\n" +
                "  FOREIGN KEY (supplierName) REFERENCES Supplier(name)\n" +
                ");\n");
        conn.close();
    }

    private void insertContact(ContactDTO contact) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Contact(supplierName, name, phone) VALUES (?,?, ?)");
        pstmt.setString(1, contact.getSupplierName());
        pstmt.setString(2, contact.getName());
        pstmt.setString(3, contact.getPhone());
        pstmt.executeUpdate();
        conn.close();
    }

    public ArrayList<ContactDTO> getAllContacts() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<ContactDTO> contacts = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Contact");

        while (rs.next()) {
            int id = rs.getInt("id");
            String supplierName = rs.getString("supplierName");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            ContactDTO contact = new ContactDTO(id, supplierName , name, phone);
            contacts.add(contact);
        }
        conn.close();
        return contacts;
    }

    //SortedMap<String,String> contact_List of supplier
    public SortedMap<String,String> getContactListOfSupplier(String supplier_name) {
        try {
            if (contact_List.containsKey(supplier_name)) {
                System.out.println("contact list of supplier " + supplier_name + " already exists");
            } else {
                ArrayList<ContactDTO> contacts = getAllContactsFromSupplier(supplier_name);
                SortedMap<String,String> sup_contacts = new TreeMap<>();
                for (ContactDTO contactDTO : contacts) {
                    sup_contacts.put(contactDTO.getName(),contactDTO.getPhone());
                }
                this.contact_List.put(supplier_name, sup_contacts);
            }
            return contact_List.get(supplier_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact_List.get(supplier_name);
    }

    private ArrayList<ContactDTO> getAllContactsFromSupplier(String supplierName) {
        ArrayList<ContactDTO> contacts = new ArrayList<>();
        try {
            this.conn = Repo.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Contact WHERE supplierName = '" + supplierName + "'");

            while (rs.next()) {
                int id = rs.getInt("id");
                String suppName = rs.getString("supplierName");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                ContactDTO contact = new ContactDTO(id, suppName , name, phone);
                contacts.add(contact);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void insert(Object d) {
        try {
            Supplier s = (Supplier) d;
            for(String key:s.getContact_List().keySet()) {
                ContactDTO contractDTO = new ContactDTO(1, s.getSupplier_name(),key,s.getContact_List().get(key));
                insertContact(contractDTO);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*

    int autoIncKeyFromApi = -1;

    rs = stmt.getGeneratedKeys();

    if (rs.next()) {
        autoIncKeyFromApi = rs.getInt(1);
    } else {

        // throw an exception from here
    }

    System.out.println("Key returned from getGeneratedKeys():"
            + autoIncKeyFromApi);
} finally {

        if (rs != null) {
        try {
        rs.close();
        } catch (SQLException ex) {
        // ignore
        }
        }

        if (stmt != null) {
        try {
        stmt.close();
        } catch (SQLException ex) {
        // ignore
        }
       }

     */

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
