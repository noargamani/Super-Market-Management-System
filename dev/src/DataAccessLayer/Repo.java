package DataAcessLayer;



import DataAcessLayer.DAO.Suppliers.ManufactureDAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Repo {
    static String url = "jdbc:sqlite::DataAcessLayer:test.db";

    public static Connection connect() throws SQLException {
        Connection conn = null;
        try {
            File dbFile = new File("test.db");
            boolean tablesExist=true;
            if (!dbFile.exists()) {
                dbFile.createNewFile();
                tablesExist=false;
            }
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            if(!tablesExist){
                createTables(DriverManager.getConnection(url));
            }
            conn = DriverManager.getConnection(url);

            //  System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.out.println("Error connecting to SQLite: " + e.getMessage());
        }

        return conn;
    }


    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
/*
        //contract db
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS contracts (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    does_shipment BOOLEAN NOT NULL,\n" +
                "    ship_days TEXT NOT NULL,\n" +
                "    date_sign DATE NOT NULL\n" +
                ");\n");

        //manufacture
        ManufactureDAO dao=new ManufactureDAO();
        dao.createTable();



        //item for order
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Item_for_Order (\n" +
                "    OrderId INT,\n" +
                "    CatalogID INT,\n" +
                "    total_price_without_discount DOUBLE,\n" +
                "    total_discount DOUBLE,\n" +
                "    final_price DOUBLE,\n" +
                "    orderAmount INT,\n" +
                "    PRIMARY KEY (OrderId, CatalogID),\n" +
                "    FOREIGN KEY (OrderId) REFERENCES Orders(OrderId),\n" +
                "    FOREIGN KEY (CatalogID) REFERENCES Catalog(CatalogID)\n" +
                ");\n");

        //Order
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Orders (\n" +
                "    OrderId INTEGER PRIMARY KEY,\n" +
                "    Status TEXT,\n" +
                "    dateOrderIssued DATE,\n" +
                "    supplierName TEXT,\n" +
                "    totalQuantity INTEGER,\n" +
                "    price_before_discount REAL\n" +
                ");\n");


        //SUPPLIER DISCOUNT
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TotalOrderDiscount (\n" +
                "    supplier TEXT,\n" +
                "    price REAL,\n" +
                "    discount INTEGER,\n" +
                "    type TEXT,\n" +
                "    PRIMARY KEY (supplier, price)\n" +
                ");\n");

        //SUPPLIER DISCOUNT
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SupplierItemDiscountDTO (\n" +
                "    ItemId INTEGER,\n" +
                "    price REAL,\n" +
                "    discount INTEGER,\n" +
                "    type TEXT,\n" +
                "    PRIMARY KEY (ItemId, price)\n" +
                ");\n");


        //SUPPLIER CARD
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Supplier_Card (\n" +
                "    BN_number INT PRIMARY KEY,\n" +
                "    pay_condition TEXT,\n" +
                "    bank_account TEXT\n" +
                ");\n");


        //Contact list
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Contact (\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "  supplierName TEXT,\n" +
                "  name TEXT,\n" +
                "  phone TEXT\n" +
                "  FOREIGN KEY (supplierName) REFERENCES Supplier(name)\n" +
                ");\n");

            //supplier

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Supplier (\n" +
                "    supplier_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    supplier_name TEXT,\n" +
                "    address TEXT,\n" +
                "    Bn_number INTEGER,\n" +
                "    FOREIGN KEY (Bn_number) REFERENCES Bn_table(Bn_number)\n" +
                ");\n");


          stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SupplierItem (\n" +
                  "   Id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                  "    itemId INTEGER ,\n" +
                  "    supplier_catalogID INT ,\n" +
                  "    supplier TEXT,\n" +
                  "    itemName TEXT,\n" +
                  "    itemManufacture TEXT,\n" +
                  "    TotalAmount INT,\n" +
                  "    price double,\n" +
                  "    Quantity INT,\n" +
                  "    expiration date\n" +

                  ");\n");

          //ITEM Q DISCOUNT
          stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ItemDiscount (\n" +
                  "  id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                  "  itemId INT,\n" +
                  "  quantity INT,\n" +
                  "  discount INT\n" +
                  ");\n");*/
        // add more table creation statements as needed

        /**
         Inventory

         */
        //Category
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Category (\n" +
                "  CategoryID INTEGER PRIMARY KEY,\n" +
                "  CategoryName TEXT,\n" +
                "  FatherCategory INTEGER\n" +
                ");\n");

        //SubCategories
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SubCategoryInCategory(\n" +
                "  CategoryID INTEGER NOT NULL,\n" +
                "  SubCategoryID INTEGER NOT NULL,\n" +
                "  PRIMARY KEY (CategoryID, SubCategoryID),\n" +
                "  FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),\n" +
                "  FOREIGN KEY (SubCategoryID) REFERENCES Category(CategoryID)\n" +
                ");\n");

        //ItemsInCategory
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ItemInCategory (\n" +
                "  CategoryID INTEGER NOT NULL,\n" +
                "  CatalogNumber INTEGER NOT NULL,\n" +
                "  PRIMARY KEY (CategoryID, CatalogNumber),\n" +
                "  FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID),\n" +
                "  FOREIGN KEY (CatalogNumber) REFERENCES InventoryItem(CatalogNumber)\n" +
                ");\n");

        //Discount
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Discount (\n" +
                "    SaleNumber INTEGER PRIMARY KEY,\n" +
                "    SaleName TEXT,\n" +
                "    Discount DOUBLE,\n" +
                "    StartDate DATE,\n" +
                "    EndDate DATE\n" +
                ");\n");


        //Report
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Report (\n" +
                "  ReportID INT PRIMARY KEY,\n" +
                "  ReportDate DATE,\n" +
                "  Type Text CHECK(Type IN('DefectiveReport', 'DiscountReport', 'InventoryReport', 'MissingItemsReport', 'ReportByCategory'))\n" +
                ");\n");

        // InventoryItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS InventoryItem (\n" +
                "    CatalogNumber INTEGER PRIMARY KEY,\n" +
                "    Name TEXT,\n" +
                "    Manufacturer TEXT,\n" +
                "    TotalAmount INTEGER,\n" +
                "    MinimumAmount INTEGER,\n" +
                "    WarehouseAmount INTEGER,\n" +
                "    ShelvesAmount INTEGER,\n" +
                "    Price DOUBLE\n" +
                ");\n");

        // SpecificItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SpecificItem (\n" +
                "    CatalogNumber INTEGER NOT NULL,\n" +
                "    ItemID INTEGER NOT NULL,\n" +
                "    CostPrice DOUBLE,\n" +
                "    SellingPrice DOUBLE,\n" +
                "    Expiration DATE,\n" +
                "    Defective BOOLEAN,\n" +
                "    Location TEXT,\n" +
                "    DefectType TEXT,\n" +
                "    DeliverTime DATE, \n" +
                "    PRIMARY KEY (CatalogNumber, ItemID)\n" +
                "    FOREIGN KEY (CatalogNumber) REFERENCES InventoryItem(CatalogNumber)\n" +
                ");\n");

        //SpecificItemInItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SpecificItemInItem (\n" +
                "  CatalogNumber INTEGER NOT NULL,\n" +
                "  ItemID INTEGER NOT NULL,\n" +
                "  PRIMARY KEY (CatalogNumber, ItemID),\n" +
                "  FOREIGN KEY (CatalogNumber) REFERENCES Item(CatalogNumber),\n" +
                "  FOREIGN KEY (ItemID) REFERENCES SpecificItem(ItemID)\n" +
                ");\n");


        // ReportItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ReportItem (\n" +
                "  ReportID INT NOT NULL,\n" +
                "  CatalogNumber INT NOT NULL,\n" +
                "  PRIMARY KEY (ReportID, CatalogNumber),\n" +
                "  FOREIGN KEY (ReportID) REFERENCES Report(ReportID),\n" +
                "  FOREIGN KEY (CatalogNumber) REFERENCES InventoryItem(CatalogNumber)\n" +
                ");\n");

        // ReportSpecificItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ReportSpecificItem (\n" +
                "  ReportID INT NOT NULL,\n" +
                "  ID INT NOT NULL,\n" +
                "  PRIMARY KEY (ReportID, ID),\n" +
                "  FOREIGN KEY (ReportID) REFERENCES Report(ReportID)\n" +
                "  FOREIGN KEY (ID) REFERENCES SpecificItem(ID)\n" +
                ");\n");

        // ReportDiscount
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ReportDiscount (\n" +
                "  ReportID INT NOT NULL,\n" +
                "  SaleNumber INT NOT NULL,\n" +
                "  PRIMARY KEY (ReportID, SaleNumber),\n" +
                "  FOREIGN KEY (ReportID) REFERENCES Report(ReportID)\n" +
                "  FOREIGN KEY (SaleNumber) REFERENCES Discount(SaleNumber)\n" +
                ");\n");

        // DiscountItem
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DiscountItem (\n" +
                "  SaleNumber INT NOT NULL,\n" +
                "  CatalogNumber INT NOT NULL,\n" +
                "  PRIMARY KEY (SaleNumber, CatalogNumber),\n" +
                "  FOREIGN KEY (SaleNumber) REFERENCES Discount(SaleNumber)\n" +
                "  FOREIGN KEY (CatalogNumber) REFERENCES InventoryItem(CatalogNumber)\n" +
                ");\n");
    }
}