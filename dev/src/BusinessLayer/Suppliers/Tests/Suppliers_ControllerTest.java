package BusinessLayer.Suppliers.Tests;


import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.Supplier_Card;
import Service_layer.Suppliers_Service;

import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Suppliers_ControllerTest {
    static Suppliers_Service service;
    @BeforeAll
    static void setUp() throws SQLException {
        service=new Suppliers_Service() ;
        init(service);
    }

    @org.junit.jupiter.api.Test
    void add_Supplier() throws SQLException {
        ArrayList<String> man=new ArrayList<>();
        man.add("man1");
        man.add("man2");
        SortedMap<String,String> contact=new TreeMap<>();
        contact.put("a","0545346677");
        service.addSupplier("b","b",man,contact,new TreeMap<>(),new TreeMap<>(),new Supplier_Card(0,"1","2"),new Contract(true,new ArrayList<>(),new Date()));
        try {
            assertNotEquals(service.getSupplier("b"), null);
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void getSupplier() {
        try {
            Supplier supplier = service.getSupplier("a");
            assertNotEquals(supplier,null);
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void getSupplier_adress() {
        try {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getAddress(),"b");
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void getSupplier_manu() {
        try
        {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getManufactures().get(0),"man1");
            assertEquals(supplier.getManufactures().get(1),"man2");
        }
        catch (Exception e){
            fail();
        }
    }

    @org.junit.jupiter.api.Test
    void getSupplier_Card_BN() {
        try
        {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getSupplier_card().getBN_number(),0);
        }
        catch (Exception e){
            fail();
        }

    }
    @org.junit.jupiter.api.Test
    void getSupplier_Card_Bank() {
        try
        {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getSupplier_card().getBank_account(),"1");
        }
        catch (Exception e){
            fail();
        }

    }
    @org.junit.jupiter.api.Test
    void getSupplier_Card_condition() {
        try
        {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getSupplier_card().getBank_account(),"1");
        }
        catch (Exception e){
            fail();
        }

    }

    @org.junit.jupiter.api.Test
    void getSupplier_Contract_doesship() {
        try
        {
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getContract().isDoesShipment(),true);
        }
        catch (Exception e){
            fail();
        }

    }

    @org.junit.jupiter.api.Test
    void getSupplier_Contract_signdate() {
        try{
            Supplier supplier = service.getSupplier("a");
            assertEquals(supplier.getContract().getDate_sign(),null);
        }
        catch (Exception e){
            fail();
        }

    }

    @org.junit.jupiter.api.Test
    void getSupplier_Contract_contact() {
        try
        {
            Supplier supplier=service.getSupplier("a");
            assertEquals(supplier.getContact_List().get("a"),"0545346677");
        }
        catch (Exception e){
            fail();
        }

    }
    /**
     * init
     * @param service
     */
    public static void init(Suppliers_Service service) throws SQLException {
        ArrayList<String> man=new ArrayList<>();
        man.add("man1");
        man.add("man2");
        SortedMap<String,String> contact=new TreeMap<>();
        contact.put("a","0545346677");
        service.addSupplier("a","b",man,contact,new TreeMap<>(),new TreeMap<>(),new Supplier_Card(0,"1","2"),new Contract(true,new ArrayList<>(),new Date()));

    }
}