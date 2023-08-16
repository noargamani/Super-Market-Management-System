package DataAcessLayer.DAO.Suppliers;



import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.*;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.Repo;
import DataAcessLayer.DTO.Suppliers.SupplierDTO;
import DataAcessLayer.DTO.Suppliers.SupplierItemDTO;

import java.sql.*;
import java.util.*;

public class SupplierDAO implements DAO {
    private Connection conn;
    private  Hashtable<String, Supplier> suppliers;
    boolean suppliersFlag;

    ContactDAO contactDAO;
    ManufactureDAO manufactureDAO;
    TotalOrderDiscountByPriceDAO totalOrderDiscountByPriceDAO;
    TotalOrderDiscountByQuantityDAO totalOrderDiscountByQuantityDAO;
    ContractDAO contractDAO;
    SupplierCardDAO supplierCardDAO;
    SupplierItemDAO supplierItemDAO;
    OrderDAO orderDAO;
    RoutineOrdersDAO routineOrdersDAO;

    //Supplier Name -> ENTITY
    //private Hashtable<String, SortedMap<Integer, SupplierItem>> items;//catalog number->item>
    //public int supplier_id=0;
    //public static int CurrOrder=0;
    //public int currPeriodicOrder=0;

    public int itemId=0;
    //public Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> periodicOrders=new Hashtable<>(); //order id -> day -> itemCode TO amount


    public SupplierDAO() {

        try {
            //this.conn = Repo.connect();
            //System.out.println("SupplierDAOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            supplierCardDAO=new SupplierCardDAO();
            contractDAO=new ContractDAO();
            createTable();
            suppliers=new Hashtable<>();
            //items=new Hashtable<>();
            contactDAO=new ContactDAO();
            manufactureDAO=new ManufactureDAO();
            totalOrderDiscountByPriceDAO=new TotalOrderDiscountByPriceDAO();
            totalOrderDiscountByQuantityDAO=new TotalOrderDiscountByQuantityDAO();
            supplierItemDAO=new SupplierItemDAO();
            orderDAO=new OrderDAO();
            routineOrdersDAO=new RoutineOrdersDAO();
            suppliersFlag=false;
            if(!this.conn.isClosed())
            {
                this.conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
         /*

        String remove = "DROP TABLE IF EXISTS Supplier";
        PreparedStatement rstmt = conn.prepareStatement(remove);
        rstmt.executeUpdate();

         */
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Supplier (\n" +
                "    supplier_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    supplier_name TEXT,\n" +
                "    address TEXT,\n" +
                "    Bn_number INTEGER,\n" +
                "    FOREIGN KEY (Bn_number) REFERENCES Supplier_Card(Bn_number)\n" +
                ");\n");
        this.conn.close();
    }

    private void insertSupplier(SupplierDTO supplier) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Supplier(supplier_name, address, Bn_number) VALUES (?, ?, ?)");
        stmt.setString(1, supplier.getSupplier_name());
        stmt.setString(2, supplier.getAddress());
        stmt.setInt(3, supplier.getBn_number());
        stmt.executeUpdate();
    }

    private void updateInDict(SupplierDTO supplier) throws SQLException {
        String supplier_name = supplier.getSupplier_name();
        int Bn_number = supplier.getBn_number();
        ArrayList<String> manufactures = this.manufactureDAO.getManufacturesNamesOfSupplier(supplier_name);
        SortedMap<String, String> contacts = this.contactDAO.getContactListOfSupplier(supplier_name);
        SortedMap<Double, Integer> totalOrderDiscountByPrice = this.totalOrderDiscountByPriceDAO.getOrderDiscountsOfSupplier(supplier_name);
        SortedMap<Integer, Integer> totalOrderDiscountByQuantity = this.totalOrderDiscountByQuantityDAO.getOrderDiscountsOfSupplier(supplier_name);
        Supplier_Card supplierCard = this.supplierCardDAO.getSupplierCardOfSupplier(Bn_number);
        Contract contract = this.contractDAO.getContractOfSupplier(supplier_name);
        suppliers.put(supplier.getSupplier_name(), new Supplier(supplier_name, supplier.getSupplier_ID(), supplier.getAddress(),
                manufactures, contacts, totalOrderDiscountByPrice, totalOrderDiscountByQuantity, supplierCard, contract, this));
    }

    private ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<SupplierDTO> suppliers = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier");

        while (rs.next()) {
            int supplier_ID = rs.getInt("supplier_ID");
            String supplier_name = rs.getString("supplier_name");
            String address = rs.getString("address");
            int Bn_number = rs.getInt("Bn_number");
            SupplierDTO supplier = new SupplierDTO(supplier_ID, supplier_name, address,Bn_number);
            suppliers.add(supplier);
        }
        this.conn.close();

        return suppliers;
    }

    @Override
    public void insert(Object d) throws SQLException {
        try {
            this.conn = Repo.connect();
            Supplier s = (Supplier) d;
            String supplier_name = s.getSupplier_name();
            SupplierDTO supplierDTO = new SupplierDTO(1, supplier_name, s.getAddress(), s.getSupplier_card().getBN_number());
            insertSupplier(supplierDTO);
            this.contactDAO.insert(s);
            this.contractDAO.insert(s);
            this.supplierCardDAO.insert(s);
            this.totalOrderDiscountByPriceDAO.insert(s);
            this.totalOrderDiscountByQuantityDAO.insert(s);
            this.manufactureDAO.insert(s);
            for(SupplierItem supplierItem:s.getItems().values()){
               supplierItemDAO.insert(supplierItem);
            }
            this.conn.close();
            System.out.println("Supplier inserted successfully");
            updateInDict(supplierDTO);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public void delete(DTO d) {

    }

    @Override
    public void update(DTO d) {

    }
    public ArrayList<String> getSuppliersManufactures(String supplier_name)
    {
        return manufactureDAO.getManufacturesNamesOfSupplier(supplier_name);
    }
    public SortedMap<String,String> getContact_List(String supplier_name)
    {
        return contactDAO.getContactListOfSupplier(supplier_name);
    }
    public SortedMap<Double, Integer> getDiscountByTotalPrice(String supplier_name)
    {
        return this.totalOrderDiscountByPriceDAO.getOrderDiscountsOfSupplier(supplier_name);
    }
    public SortedMap<Integer, Integer> getDiscountByTotalQuantity(String supplier_name)
    {
        return this.totalOrderDiscountByQuantityDAO.getOrderDiscountsOfSupplier(supplier_name);
    }
    public Contract getContractOfSupplier(String supplier_name)
    {
        try
        {
            return this.contractDAO.getContractOfSupplier(supplier_name);
        }
        catch (Exception e)
        {
            System.out.println("failed");
        }
        return null;
    }


    @Override
    public DTO get(Object primary) {
        return null;
    }

    public  Hashtable<String, Supplier> getSuppliers() {
        if(!suppliersFlag) {
            try {
                ArrayList<SupplierDTO> suppliersDTO = getAllSuppliers();
                for (SupplierDTO supplierDTO : suppliersDTO) {
                    String supplier_name = supplierDTO.getSupplier_name();
                    if(suppliers.containsKey(supplier_name)) {
                        continue;
                    }
                    getSupplierByName(supplier_name);
                }
                suppliersFlag = true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return suppliers;
    }

    public Supplier getSupplierByName(String supplierName) throws Exception {
        try {
            if (suppliers.containsKey(supplierName)) {
                return suppliers.get(supplierName);
            } else {
                try {
                    SupplierDTO supplierDTO = getSupplierDTOByName(supplierName);
                    if (supplierDTO == null) {
                        return null;
                    }
                    String supplier_name = supplierDTO.getSupplier_name();
                    ArrayList<String> manufactures = getSuppliersManufactures(supplier_name);
                    SortedMap<String, String> contacts = getContact_List(supplier_name);
                    SortedMap<Double, Integer> discountsByPrice = getDiscountByTotalPrice(supplier_name);
                    SortedMap<Integer, Integer> discountsByQuantity = getDiscountByTotalQuantity(supplier_name);
                    Contract contract = this.contractDAO.getContractOfSupplier(supplier_name);
                    Supplier_Card supplierCard = this.supplierCardDAO.getSupplierCardOfSupplier(supplierDTO.getBn_number());


                    Supplier supplier = new Supplier(supplierDTO.getSupplier_name(), supplierDTO.getSupplier_ID(),
                            supplierDTO.getAddress(), manufactures, contacts, discountsByPrice, discountsByQuantity,
                            supplierCard, contract, this);

                    suppliers.put(supplier.getSupplier_name(), supplier);
                    return supplier;
                } catch (SQLException e) {
                    throw new Exception("Database error");
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private SupplierDTO getSupplierDTOByName(String supplierName) {
        //search for supplier with that name in the database:
        try {
            this.conn = Repo.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier WHERE supplier_name = '" + supplierName + "'");
            if(rs.next()) {
                int supplier_ID = rs.getInt("supplier_ID");
                String address = rs.getString("address");
                int Bn_number = rs.getInt("Bn_number");
                SupplierDTO supplier = new SupplierDTO(supplier_ID, supplierName, address,Bn_number);
                this.conn.close();
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Hashtable<String, Set<Integer>> getItemCode_To_Items() {
        try
        {
            Hashtable<String, Set<Integer>> x = supplierItemDAO.getItemCode_To_Items();
            return x;
        }
        catch (Exception e)
        {
            System.out.println("failed");
            return null;
        }
    }

    public SortedMap<Integer, SupplierItem> getItemID_To_Item() {
        try
        {
            return supplierItemDAO.getItemID_To_Item();
        }
        catch (Exception e)
        {
            System.out.println("failed");
            return null;
        }
    }

    public void addSupplierItem(SupplierItem supplierItem, String supplier) throws Exception {
        try {
            supplierItemDAO.insert(supplierItem);
            Update_ItemID_to_Item(supplierItem, supplier);
            Update_ItemCode_to_ItemID(supplierItem);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private void Update_ItemCode_to_ItemID(SupplierItem supplierItem) {
        String toAdd = supplierItem.getItemCode();
        if (!getItemCode_To_Items().containsKey(toAdd)) {
            getItemCode_To_Items().put(toAdd, new HashSet<>());
        }
        getItemCode_To_Items().get(toAdd).add(itemId);
    }

    private void Update_ItemID_to_Item(SupplierItem supplierItem, String supplier) {
        //getSuppliers().get(supplier).getItems().put(supplierItem.getSupplier_catalogID(), supplierItem);
        getItemID_To_Item().put(itemId, supplierItem);
    }

    public SupplierItem removeItem(String supplier, int supplierCatalogID) {
        SupplierItem item;
        try {
            item = getSupplierByName(supplier).getItemByCatalogID(supplierCatalogID);
            if (item == null) {
                return null;
            }
            getItemID_To_Item().remove(supplierCatalogID);
            getItemCode_To_Items().get(item.getItemCode()).remove(item.getItemId());
            getSuppliers().get(supplier).getItems().remove(supplierCatalogID);
            SupplierItemDTO supplierItemDTO = new SupplierItemDTO(item.getItemId(), item.getSupplier_catalogID(), item.getSupplier(), item.getName(), item.getItemManufacture(), item.getTotalAmount(), item.getItem_list_price(), item.getQuantity(), item.getExpiration());
            try {
                supplierItemDAO.delete(supplierItemDTO);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return item;
    }
    public ArrayList<Supplier> getSuppliersBySupplyDay(Contract.Day supplyDay)
    {
        if(supplyDay == null || supplyDay == Contract.Day.None)
        {
            return new ArrayList<>(getSuppliers().values());
        }
        suppliers = getSuppliers();
        ArrayList<Supplier> suppliersBySupplyDay = new ArrayList<>();
        for (Supplier supplier : suppliers.values())
        {
            if(supplier.getContract().getShip_days().contains(supplyDay))
            {
                suppliersBySupplyDay.add(supplier);
            }
        }
        return suppliersBySupplyDay;
    }
    //Need Order By Supplier (look at the lists/ Hashtable)
    public Order CreateOrder(Order order)
    {
        return this.orderDAO.CreateOrder(order);
        //TODO: add to order table
    }

    public boolean containsSupplier(String name) {
        getSuppliers();
        if(suppliers == null) {
            return false;
        }
        return suppliers.containsKey(name);
    }

    public SortedMap<Integer, SupplierItem> getItemsBySupplier(String supplierName) {
        try{
            return supplierItemDAO.getBySupplier(supplierName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SupplierItem getItemByCatalogID(int catalogId, String supplierName) {
        try {
            SortedMap<Integer, SupplierItem> items = supplierItemDAO.getBySupplier(supplierName);
            if(items == null) {
                return null;
            }
            return items.get(catalogId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Hashtable<Integer, Order> getOrdersTable(String supplierName) {
        try {
            return orderDAO.getAllOrdersBySupplier(supplierName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeOrder(int orderID, String supplierName) {
        try {
            orderDAO.remove(orderID, supplierName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order.Status updateOrderStatus(String supplierName, int orderId, String status) throws Exception{
        try {
            return orderDAO.updateOrderStatus(supplierName, orderId, status);
        } catch (Exception e) {
            throw e;
        }
    }

    public void addItemToOrder(int orderID, int catalogID, int quantity, Supplier supplier) {
        orderDAO.addItemToOrder(orderID, catalogID, quantity, supplier);
    }

    public void AddPeriodicOrder(Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> orderForDay) {
        try
        {
            routineOrdersDAO.createRoutineOrder(-1 , orderForDay);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void RemovePeriodicOrder(int orderID) throws SQLException {
        try {
            routineOrdersDAO.deleteRoutineOrderByOrderId(orderID);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public void UpdatePeriodicOrder(int orderID, Hashtable<Constants.Pair<String, String>, Integer> itemsToOrder, ArrayList<Contract.Day> days) throws SQLException {
        try
        {
            routineOrdersDAO.updateRoutineOrder(orderID, itemsToOrder, days);
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> getAllPeriodicOrders() {
        try
        {
            return routineOrdersDAO.getAllRoutineOrders();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void updateQuantity(int orderID, String supplierName, boolean add) {
            Order myOrder = orderDAO.getAllOrders().get(supplierName).get(orderID);
            myOrder.getItems().forEach((k, v) -> {
                SupplierItem item = getItemByCatalogID(k, supplierName);
                if(add)
                    item.setQuantity(item.getQuantity() + v.getOrderAmount());
                else
                    item.setQuantity(item.getQuantity() - v.getOrderAmount());
                SupplierItemDTO dto = new SupplierItemDTO(item.getItemId(), item.getSupplier_catalogID(),
                        item.getSupplier(), item.getName(), item.getItemManufacture(), item.getTotalAmount(), item.getItem_list_price(), item.getQuantity(), item.getExpiration());
                try {
                    supplierItemDAO.update(dto);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
    }
}
