package Presentaion_layer.CLI;

import BusinessLayer.Inventory.Classes.SpecificItem;
import BusinessLayer.Item;
import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Order;
import BusinessLayer.Suppliers.Classes.Supplier;
import Presentaion_layer.CLI.Actions.IssueReports.IssueReports;
import Presentaion_layer.CLI.Actions.ManageCategories.AddItemToCategory;
import Presentaion_layer.CLI.Actions.ManageCategories.AddNewCategory;
import Presentaion_layer.CLI.Actions.ManageCategories.RemoveItemFromCategory;
import Presentaion_layer.CLI.Actions.ManageDiscounts.AddNewDiscount;
import Presentaion_layer.CLI.Actions.ManageDiscounts.CheckIfItemOnDiscount;
import Presentaion_layer.CLI.Actions.Manage_Supply.*;
import Presentaion_layer.CLI.Actions.Supplier_Item.Add_Item;
import Presentaion_layer.CLI.Actions.Order.Add_order;
import Presentaion_layer.CLI.Actions.Order.Make_Auto_Order;
import Presentaion_layer.CLI.Actions.Supplier.Add_supplier;
import Presentaion_layer.CLI.Actions.Supplier.Get_Supplier;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllOrders;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllRoutineOrders;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class CommandCLI {
    final Suppliers_Service suppliersService;
    final InventoryService inventoryService;

    /**
     * Constructs a Command object with the specified Suppliers_Service and InventoryService.
     *
     * @param suppliersService The Suppliers_Service object to associate with the Command.
     * @param inventoryService The InventoryService object to associate with the Command.
     */
    public CommandCLI(Suppliers_Service suppliersService, InventoryService inventoryService){
        this.suppliersService = suppliersService;
        this.inventoryService = inventoryService;
    }

    /**
     * Processes the main command and returns the corresponding result.
     *
     * @param command The main command to process.
     * @return The result of processing the command.
     */
    public String ProcessMain(int command) {
        if(command==1){
            return Constants.exit;
        }
        else if(command==2){
            return SupplierWindow();
        }
        else if(command==3){
            return InventoryWindow();
        }
        else if(command==4){
            return StoreManagerWindow();
        }
        return "";
    }

    // should have all getters methods and only them
    private String StoreManagerWindow() {
        while (true) {
            try {

                System.out.println("write the number of the operation you want to do\n" +
                        "1.Manage Inventory\n" +
                        "2.Manage Suppliers\n" +
                        "3.Get All Inventory Items\n" +
                        "4.Get All Specific Items\n" +
                        "5.Get All Discounts\n"+
                        "6.Get All Categories\n"+
                        "7.Get Routine Order\n"+
                        "8.Get Orders From Suppliers\n"+
                        "9.Exit\n");
                System.out.println("Enter command");
                Scanner scanner = new Scanner(System.in);
                int com = scanner.nextInt();
                String response = this.ProcessStoreManager(com);
                if (response.equals(Constants.exit)) {
                    return Constants.exit;
                }
                if(response.equals(Constants.goBack)){
                    return "";
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private String ProcessStoreManager(int com) {
        if(com==1){
            InventoryWindow();
        }
        else if(com==2){
            SupplierWindow();
        }
        else if(com==3){
            inventoryService.getAllItems();
        }else if(com==4){
            try
            {
                List<SpecificItem> specific = inventoryService.getIC().getSpecificItemDAO().getAllItems();
                for(SpecificItem s:specific)
                    System.out.println(s);
            } catch (SQLException e) {
                System.out.println("Error: problem with DB connection");
            }
        }else if(com==5){
            inventoryService.getAllDiscounts();
        }else if(com==6){
            inventoryService.getAllCategories();
        }else if(com==7){
            Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> orders = suppliersService.getSupplierController().getSupplierDAO().getAllPeriodicOrders();
            System.out.println(orders);
        }else if(com==8){
            Hashtable<String, Supplier> suppliers=suppliersService.getSupplierController().getSuppliers();
            Hashtable<Integer, Order> orderHashtable=new Hashtable<>();
            for(String s:suppliers.keySet())
                orderHashtable.putAll(suppliersService.getSupplierController().getSupplierDAO().getOrdersTable(s));
            for(Integer i:orderHashtable.keySet())
                System.out.println(orderHashtable.get(i));
        }else if(com==9){
            return Constants.exit;
        }
        return "";
    }

    /**
     * Displays the SupplierWindow and processes the user's input.
     *
     * @return The result of processing the user's input.
     */
    public String SupplierWindow() {
        while (true) {
            try {

                System.out.println("Write the number of the operation you want to do\n" +
                        "1.Add Supplier\n" +
                        "2.Get Supplier\n" +
                        "3.Add Item\n" +
                        "4.Exit\n");
                System.out.println("Enter command");
                Scanner scanner = new Scanner(System.in);
                int com = scanner.nextInt();
                String response = this.ProcessSuppliers(com);
                if (response.equals(Constants.exit)) {
                    return Constants.exit;
                }
                if(response.equals(Constants.goBack)){
                    return "";
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    /**
     * Processes the user's input in the SupplierWindow.
     *
     * @param command The command entered by the user.
     * @return The result of processing the command.
     */
    public String ProcessSuppliers(int command) throws SQLException {
        if(command==1){
            Add_supplier.Add(suppliersService);
        }
        else if(command==2){
            Get_Supplier.Get(suppliersService, true);
        }
        else if(command==3){
            Add_Item.Add(suppliersService);
        }
        else if (command==4){
            return Constants.exit;
        }
        else
        {
            throw new IllegalArgumentException("Invalid input please try again");
        }
        return "";
    }

    /**
     * Displays the InventoryWindow and processes the user's input.
     *
     * @return The result of processing the user's input.
     */
    public String InventoryWindow()
    {
        while (true) {
            try {

                System.out.println("write the number of the operation you want to do\n"+
                        "1.Issue Reports\n"+
                        "2.Add New Item\n" +
                        "3.Add New Specific Item\n"+
                        "4.Delete Specific Item\n"+
                        "5.Delete Item\n"+
                        "6.Update Defective Status\n" +
                        "7.Add New Category\n"+
                        "8.Add Item To Category\n"+
                        "9.Remove Item From Category\n"+
                        "10.Add New Discount\n"+
                        "11.Check If Item On Discount\n"+
                        "12.Add Order From Supplier\n" +
                        "13.Add Need Order\n" +
                        "14.Add Routine Order\n" +
                        "15.Update Routine Order\n" +
                        "16.Remove Routine Order\n" +
                        "17.Exit\n");

                System.out.println("Enter command");
                Scanner scanner = new Scanner(System.in);
                int com = scanner.nextInt();
                String response = this.ProcessInventory(com);
                if (response.equals(Constants.exit)) {
                    return Constants.exit;
                }
                if(response.equals(Constants.goBack))
                {
                    return Constants.goBack;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    /**
     * Processes the user's input in the InventoryWindow.
     *
     * @param command The command entered by the user.
     * @return The result of processing the command.
     */
    public String ProcessInventory(int command) throws Exception {
        if(command==1){
            IssueReports.Repost(inventoryService);
        }
        else if(command==2){
            AddNewItem.Add(inventoryService);
        }
        else if(command==3){
            AddNewSpecificItem.Add(inventoryService);
        }
        else if(command==4){
            Item.NeedOrderException e = DeleteSpecificItem.DeleteSpecificItem(inventoryService);
            if(e != null)
            {
                Make_Auto_Order.makeNeedOrder(suppliersService, e);
            }
        }
        else if(command==5){
            DeleteItem.DeleteItem(inventoryService);
        }
        else if(command==6){
            UpdateDefectiveStatus.Update(inventoryService);
        }
        else if(command==7)
        {
            AddNewCategory.Add(inventoryService);
        }
        else if (command == 8)
        {
            AddItemToCategory.Add(inventoryService);
        }
        else if (command == 9)
        {
            RemoveItemFromCategory.Remove(inventoryService);
        }
        else if (command == 10)
        {
            AddNewDiscount.Add(inventoryService);
        }
        else if (command == 11)
        {
            CheckIfItemOnDiscount.Check(inventoryService);
        }else if(command==12){
            Add_order.Add(suppliersService);
        }
        else if(command==13){
            Make_Auto_Order.make_Auto_Order(suppliersService);
        }
        else if(command==14)
        {
            Make_Auto_Order.make_Routine_Order(suppliersService);
        }
        else if (command==15){
            Make_Auto_Order.remove_Routine_Order(suppliersService);
        }
        else if (command==16){
            try {
                Make_Auto_Order.update_Routine_Order(suppliersService);
            } catch (Exception e){

            }
        }
        else if (command ==17)
        {
            return Constants.exit;
        }
        else
        {
            throw new IllegalArgumentException("Invalid input please try again");
        }
        return "";
    }




}