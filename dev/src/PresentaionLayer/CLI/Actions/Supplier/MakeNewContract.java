package Presentaion_layer.CLI.Actions.Supplier;

import BusinessLayer.Suppliers.Classes.Contract;
import Service_layer.Suppliers_Service;

import java.util.Scanner;

import static Presentaion_layer.CLI.Actions.Supplier.Add_supplier.initContract;

public class MakeNewContract {

    /**
     * Creates a new contract for a supplier and updates it in the Suppliers_Service.
     *
     * @param service      The Suppliers_Service object.
     * @param supplierName The name of the supplier.
     */
    public static void makeNewContract(Suppliers_Service service , String supplierName) {
        try
        {
            Contract contract=initContract(new Scanner(System.in));
            service.UpdateContract(supplierName,contract);
            System.out.println("contract saved!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
