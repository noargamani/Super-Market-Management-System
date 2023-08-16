package DataAcessLayer.DTO.Suppliers;

import BusinessLayer.Suppliers.Classes.Contract;

import java.util.ArrayList;
import java.util.Date;

public class ContractDTO {
    private String supplierName;
    private boolean doesShipment;
    private ArrayList<Contract.Day> shipDays;
    private Date dateSign;

    public ContractDTO(String supplierName, boolean doesShipment, ArrayList<Contract.Day> shipDays, Date dateSign) {
        this.supplierName = supplierName;
        this.doesShipment = doesShipment;
        this.shipDays = shipDays;
        this.dateSign = dateSign;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public boolean isDoesShipment() {
        return doesShipment;
    }

    public ArrayList<Contract.Day> getShipDays() {
        return shipDays;
    }

    public Date getDateSign() {
        return dateSign;
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
                "supplierName=" + supplierName +
                ", doesShipment=" + doesShipment +
                ", shipDays=" + shipDays +
                ", dateSign=" + dateSign +
                '}';
    }
}