package DataAcessLayer.DAO.Suppliers;



import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier;
import DataAcessLayer.DAO.DAO;
import DataAcessLayer.DTO.DTO;
import DataAcessLayer.DTO.Suppliers.ContractDTO;
import DataAcessLayer.Repo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

//DONE

public class ContractDAO implements DAO {
    private Connection conn;
    private Hashtable<String, Contract> contracts;
    private boolean flag;

    public ContractDAO() {
        try {
            contracts=new Hashtable<>();
            flag = false;
            createTable();
        }
        catch (Exception e){

        }
    }

    public void createTable() throws SQLException {
        this.conn = Repo.connect();
        String remove = "DROP TABLE IF EXISTS contracts";
        PreparedStatement rstmt = conn.prepareStatement(remove);
        rstmt.executeUpdate();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS contracts (\n" +
                "    supplierName TEXT PRIMARY KEY,\n" +
                "    does_shipment BOOLEAN NOT NULL,\n" +
                "    ship_days TEXT NOT NULL,\n" +
                "    date_sign DATE NOT NULL\n" +
                ");\n");
        this.conn.close();
    }

    public void insertContract(ContractDTO contract) throws SQLException {
        this.conn = Repo.connect();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contracts(supplierName, does_shipment, ship_days, date_sign) VALUES (?, ?, ?, ?)");
        pstmt.setString(1, contract.getSupplierName());
        pstmt.setBoolean(2, contract.isDoesShipment());
        pstmt.setString(3, contract.getShipDays().toString());
        pstmt.setDate(4, new java.sql.Date(contract.getDateSign().getTime()));
        pstmt.executeUpdate();
        contracts.put(contract.getSupplierName(), new Contract(contract.isDoesShipment(), contract.getShipDays(), contract.getDateSign()));
        this.conn.close();
    }

    public ArrayList<ContractDTO> getAllContracts() throws SQLException {
        this.conn = Repo.connect();
        ArrayList<ContractDTO> contracts = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM contracts");

        while (rs.next()) {
            String supplierName = rs.getString("supplierName");
            boolean doesShipment = rs.getBoolean("does_shipment");
            ArrayList<Contract.Day> shipDays = parseShipDaysString(rs.getString("ship_days"));
            Date dateSign = rs.getDate("date_sign");
            ContractDTO contract = new ContractDTO(supplierName, doesShipment, shipDays, dateSign);
            contracts.add(contract);
            this.conn.close();
            this.contracts.put(supplierName, new Contract(doesShipment, shipDays, dateSign));
        }

        return contracts;
    }

    public Contract getContractOfSupplier(String supplierName) throws SQLException {
        if (!contracts.containsKey(supplierName)) {
            fetchContractOfSupplier(supplierName);
        }
        return contracts.get(supplierName);
    }
    public ArrayList<Contract> getAllSuppliersContracts() throws SQLException {
        if(flag){
            return new ArrayList<>(contracts.values());
        }
        this.conn = Repo.connect();
        ArrayList<Contract> contracts = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM contracts");

        while (rs.next()) {
            String supplierName = rs.getString("supplierName");
            boolean doesShipment = rs.getBoolean("does_shipment");
            ArrayList<Contract.Day> shipDays = parseShipDaysString(rs.getString("ship_days"));
            Date dateSign = rs.getDate("date_sign");
            Contract contract = new Contract(doesShipment, shipDays, dateSign);
            contracts.add(contract);
            this.contracts.put(supplierName, new Contract(doesShipment, shipDays, dateSign));
            flag = true;
        }
        this.conn.close();

        return contracts;
    }

    private void fetchContractOfSupplier(String supplierName) throws SQLException {
        //select the contract of the supplier (by searching for values with supplier name
        this.conn = Repo.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM contracts WHERE supplierName = '" + supplierName + "'");
        if (rs.next()) {
            boolean doesShipment = rs.getBoolean("does_shipment");
            ArrayList<Contract.Day> shipDays = parseShipDaysString(rs.getString("ship_days"));
            Date dateSign = rs.getDate("date_sign");
            contracts.put(supplierName, new Contract(doesShipment, shipDays, dateSign));
        }
        this.conn.close();

    }

    private ArrayList<Contract.Day> parseShipDaysString(String shipDaysString) {
        ArrayList<Contract.Day> shipDays = new ArrayList<>();
        String[] shipDaysArray = shipDaysString.split(", ");
        for (String shipDay : shipDaysArray) {
            String s=shipDay.replace("[","");
            s=s.replace("]","");
            if(!s.equals("")) {
                shipDays.add(Contract.Day.valueOf(s));
            }
        }
        return shipDays;
    }


    @Override
    public void insert(Object d) {
        try {

            Supplier s = (Supplier) d;
            ContractDTO contractDTO = new ContractDTO(s.getSupplier_name(), s.getContract().isDoesShipment(), s.getContract().getShip_days(), s.getContract().getDate_sign());
            insertContract(contractDTO);
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
