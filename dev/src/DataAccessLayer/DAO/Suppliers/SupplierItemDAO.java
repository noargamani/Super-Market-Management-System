package DataAcessLayer.DAO.Suppliers;
import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.SupplierItem;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.ItemDiscountDTO;
import DataAcessLayer.DTO.Suppliers.SupplierItemDTO;
import DataAcessLayer.Repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;

public class SupplierItemDAO implements DAO {
    private Connection conn;
    private  Hashtable<String, Set<Integer>> itemCode_To_Items;
    boolean itemsCodeFlag = false;
    private  SortedMap<Integer, SupplierItem> itemID_To_Item;
    boolean itemsIDFlag = false;
    private static Hashtable<String, Integer> itemCode_To_ItemsCatalogID;
    static boolean itemsIDToCatalogIDFlag = false;
    private ItemDiscountDAO itemDiscountDAO;



    public SupplierItemDAO()  {
        try {
            createTable();
            itemCode_To_Items = new Hashtable<>();
            itemID_To_Item = new TreeMap<>();
            itemDiscountDAO = new ItemDiscountDAO();
            //System.out.println("Table SupplierItem created successfully!!!!!!!!!");
            itemCode_To_ItemsCatalogID = new Hashtable<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Constants.Pair<Integer, Hashtable<String, Integer>> getItemCode_To_ItemCatalogID() {
        int max = 0;
        if(itemCode_To_ItemsCatalogID == null) {
            itemCode_To_ItemsCatalogID = new Hashtable<>();
            //TODO: change to flag when Merge
        } else if (true) {
            try {
                Connection conn = Repo.connect();
                PreparedStatement pstmt = conn.prepareStatement("SELECT Name, Manufacturer, CatalogNumber FROM InventoryItem");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int catalogID = rs.getInt("CatalogNumber");
                    itemCode_To_ItemsCatalogID.put((rs.getString("Name")+ Constants.Code+
                                    rs.getString("Manufacturer")), catalogID);
                    max = Math.max(max, catalogID);
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new Constants.Pair<>(max, itemCode_To_ItemsCatalogID);
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
                /*
        String remove = "DROP TABLE IF EXISTS SupplierItem";
        PreparedStatement rstmt = conn.prepareStatement(remove);
        rstmt.executeUpdate();
                 */
        //System.out.println("Table SupplierItem dropped successfully!!!!!!!!!");
        PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS SupplierItem (\n" +
                        "supplier_catalogID INTEGER,\n" +
                        "supplier TEXT,\n" +
                        "itemName TEXT,\n" +
                        "itemManufacture TEXT,\n" +
                        "totalAmount INTEGER,\n" +
                        "price REAL,\n" +
                        "quantity INTEGER,\n" +
                        "expiration DATE,\n" +
                        "itemId INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                        ");"
        );
        stmt.executeUpdate();
       // System.out.println("Table SupplierItem created successfully!!!!!!!!!");
        conn.close();
    }

    private int insertItem(SupplierItemDTO dto) throws SQLException {
        this.conn = Repo.connect();
        String sql = "INSERT INTO SupplierItem " +
                "(supplier_catalogID, supplier, itemName, itemManufacture, totalAmount, price, quantity, expiration) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dto.getSupplierCatalogId());
            statement.setString(2, dto.getSupplier());
            statement.setString(3, dto.getItemName());
            statement.setString(4, dto.getItemManufacture());
            statement.setInt(5, dto.getQuantity());
            statement.setDouble(6, dto.getPrice());
            statement.setInt(7, dto.getQuantity());
            statement.setDate(8, new Date(dto.getExpiration().getTime()));
            statement.executeUpdate();
            PreparedStatement stmt = conn.prepareStatement("select seq from sqlite_sequence where name=  'SupplierItem' ");
            int id = stmt.executeQuery().getInt("seq");
            this.conn.close();
            return id;
        }
    }


    public void update(SupplierItemDTO dto) throws SQLException {
        this.conn = Repo.connect();
        String sql = "UPDATE SupplierItem SET " +
                "supplier_catalogID = ?, " +
                "supplier = ?, " +
                "itemName = ?, " +
                "itemManufacture = ?, " +
                "totalAmount = ?, " +
                "price = ?, " +
                "quantity = ?, " +
                "expiration = ? " +
                "WHERE itemId = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dto.getSupplierCatalogId());
            statement.setString(2, dto.getSupplier());
            statement.setString(3, dto.getItemName());
            statement.setString(4, dto.getItemManufacture());
            statement.setInt(5, dto.getQuantity());
            statement.setDouble(6, dto.getPrice());
            statement.setInt(7, dto.getQuantity());
            statement.setDate(8, new Date(dto.getExpiration().getTime()));
            statement.setInt(9, dto.getItemId());
            statement.executeUpdate();
            this.conn.close();
        }
    }

    public SortedMap<Integer, SupplierItem> getBySupplier(String supplier) throws SQLException {
        this.conn = Repo.connect();

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        SortedMap<Integer,SupplierItem> supplierItems = new TreeMap<>();
        try {

            statement = conn.prepareStatement("SELECT * FROM SupplierItem WHERE supplier = ?");
            statement.setString(1,supplier);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("itemId");
                int supplier_catalogID = resultSet.getInt("supplier_catalogID");
                String itemName = resultSet.getString("itemName");
                String itemManufacture = resultSet.getString("itemManufacture");
                int totalAmount = resultSet.getInt("TotalAmount");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("Quantity");
                Date expiration = resultSet.getDate("expiration");

                SupplierItem supplierItem = new SupplierItem(itemId,itemName,itemManufacture,supplier_catalogID,price,itemDiscountDAO.findByItemId(itemId),quantity,supplier,expiration,itemId);
                supplierItems.put(itemId,supplierItem);
            }
        } catch (SQLException e) {
            System.err.println("Error getting supplier items by supplier: " + e.getMessage());
        } finally {
            this.conn.close();
        }
        return supplierItems;
    }

    public SortedMap<Integer, SupplierItem> getAll() throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        SortedMap<Integer,SupplierItem> supplierItems = new TreeMap<>();
        try {

            statement = conn.prepareStatement("SELECT * FROM SupplierItem");

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int itemId = resultSet.getInt("itemId");
                String supplier=resultSet.getString("supplier");
                int supplier_catalogID = resultSet.getInt("supplier_catalogID");
                String itemName = resultSet.getString("itemName");
                String itemManufacture = resultSet.getString("itemManufacture");
                int totalAmount = resultSet.getInt("TotalAmount");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("Quantity");
                Date expiration = resultSet.getDate("expiration");

                SupplierItem supplierItem = new SupplierItem(itemId,itemName,itemManufacture,supplier_catalogID,price,itemDiscountDAO.findByItemId(itemId),quantity,supplier,expiration,itemId);
                supplierItems.put(itemId,supplierItem);
            }
        } catch (SQLException e) {
            System.err.println("Error getting supplier items by supplier: " + e.getMessage());
        } finally {
            this.conn.close();
        }
        return supplierItems;
    }

    @Override
    public void insert(Object d) throws SQLException {
    try {
        SupplierItem supplierItem=(SupplierItem) d;
        SupplierItemDTO supplierItemDTO=new SupplierItemDTO(supplierItem.getItemId(),supplierItem.getSupplier_catalogID(),supplierItem.getSupplier(),supplierItem.getName(),supplierItem.getItemManufacture(),supplierItem.getTotalAmount(),supplierItem.getItem_list_price(),supplierItem.getQuantity(),supplierItem.getExpiration());

        int MY_ID = insertItem(supplierItemDTO);
        SortedMap<Integer,Integer> discount = supplierItem.getDiscountByQuantity();
        for(int quantity:discount.keySet()){
            itemDiscountDAO.insert(new ItemDiscountDTO(MY_ID,quantity,discount.get(quantity)));
        }
    }
    catch (Exception e){
        throw e;
    }
    }

    @Override
    public void delete(DTO d) {
        //delete item from database
        d = (SupplierItemDTO) d;
        String sql = "DELETE SupplierItem " +
                "WHERE itemId = " + ((SupplierItemDTO) d).getItemId();
    }

    @Override
    public void update(DTO d) {

    }

    @Override
    public DTO get(Object primary) {
        return null;
    }
    public SortedMap<Integer, SupplierItem> getItemID_To_Item() {
        try
        {
            if(true)
            {
                updateHashTables();
            }
            return itemID_To_Item;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Hashtable<String, Set<Integer>> getItemCode_To_Items() {
        try
        {
            if(true)
            {
                updateHashTables();
            }
            return itemCode_To_Items;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void updateHashTables() throws SQLException {
        Hashtable<String, Set<Integer>> M_itemCode_To_Items=new Hashtable<>();

        SortedMap<Integer, SupplierItem> supplierItemDTOS=getAll();
        for(SupplierItem supplierItem:supplierItemDTOS.values())
        {
            String itemCode=supplierItem.getItemCode();
            if(M_itemCode_To_Items.containsKey(itemCode))
            {
                Set<Integer> items=M_itemCode_To_Items.get(itemCode);
                items.add(supplierItem.getItemId());
                M_itemCode_To_Items.put(itemCode,items);
            }
            else
            {
                Set<Integer> items=new HashSet<>();
                items.add(supplierItem.getItemId());
                M_itemCode_To_Items.put(itemCode,items);
            }
        }
        itemCode_To_Items=M_itemCode_To_Items;
        itemsCodeFlag=true;

        itemID_To_Item = supplierItemDTOS;
        itemsIDFlag=true;
    }
}
