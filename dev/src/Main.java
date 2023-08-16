
import BusinessLayer.Constants;
import BusinessLayer.Inventory.Controllers.ItemController;
import BusinessLayer.Inventory.Controllers.ReportController;
import BusinessLayer.Suppliers.Classes.*;
import DataAcessLayer.DAO.Inventory.*;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;
import DataAcessLayer.DAO.Suppliers.SupplierItemDAO;
import Presentaion_layer.CLI.CommandCLI;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;


import javax.swing.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Main {

    /**
     * Initializes the suppliers by creating a sample supplier and supplier items.
     *
     * @param service The Suppliers_Service instance to initialize.
     * @throws Exception if an error occurs during initialization.
     */
    public static void initSuppliers(Suppliers_Service service) throws Exception{
        SupplierDAO supplierDAO= service.getSupplierController().getSupplierDAO();
        ArrayList<String>man=new ArrayList<>();
        man.add("man1");
        man.add("man2");
        SortedMap<String,String> contact_list=new TreeMap<>();
        contact_list.put("a","054544342");
        SortedMap<Integer,Integer> discountQ=new TreeMap<>();
        discountQ.put(10,20);
        SortedMap<Double,Integer> discountP=new TreeMap<>();
        discountP.put(10.0,20);
        Supplier supplier=new Supplier("y","adress",man,contact_list,discountP,discountQ,new Supplier_Card(72,"A","a"),new Contract(true,new ArrayList<>(),new Date()),supplierDAO);
        SupplierItem supplierItem1=new SupplierItem(5,"namf se","ma",1,50,new TreeMap<>(),20,"y",new Date(),7);
        SupplierItem supplierItem2=new SupplierItem(7,"name1vdv","ma",1,50,new TreeMap<>(),20,"y",new Date(),8);
        SortedMap<Integer,SupplierItem> supplierItemSortedMap=new TreeMap<>();
        supplierItemSortedMap.put(5,supplierItem1);
        supplierItemSortedMap.put(7,supplierItem2);
        supplier.setItems(supplierItemSortedMap);
        SupplierItemDAO supplierItemDAO=new SupplierItemDAO();

        System.out.println(supplierDAO.getSuppliers());
        System.out.println(supplierItemDAO.getAll());
    }

    public static void main(String[] args) {
        if(args.length != 2)
        {
            System.out.println("Invalid number of arguments");
            return;
        }
        String UI = args[0];
        String Role = args[1];
        Suppliers_Service suppliersService = new Suppliers_Service();
        InventoryItemDAO inventoryItemDAO = new InventoryItemDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        DiscountDAO discountDAO = new DiscountDAO();
        ReportDAO reportDAO = new ReportDAO();
        SpecificItemDAO specificItemDAO = new SpecificItemDAO();
        ItemController itemController = new ItemController(inventoryItemDAO, categoryDAO, discountDAO, specificItemDAO);
        ReportController reportController = new ReportController(reportDAO);
        InventoryService inventoryService = new InventoryService(itemController, reportController);

        // Create a new DayNotifier instance
        DayNotifier notifier = new DayNotifier(suppliersService, inventoryService);

        // Create a timer that runs the checkDay method every day at midnight
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    notifier.checkDay();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    inventoryService.checkQuantity();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                try
                {
                    inventoryService.removeExpiredDiscounts();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, getDelayUntilMidnight(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

        try {
            //initSuppliers(suppliersService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (UI.equals("GUI")) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CommandGUI commandGUI = new CommandGUI(Role, suppliersService, inventoryService,true);
                    commandGUI.setVisible(false);
                }
            });
        }
        else if (UI.equals("CLI")) {
            System.out.println("Welcome to Super Li!" + "\n");
            while (true) {
                try {
                    int com = 0;
                    if (Role.equals("StoreManager")) {
                        com = 4;
                    } else if (Role.equals("StoreKeeper")) {
                        com = 3;
                    } else if (Role.equals("SupplierConnector")) {
                        com = 2;
                    } else {
                        System.out.println("Invalid input please try again");
                        com = 1;
                    }
                    CommandCLI commandCLI = new CommandCLI(suppliersService, inventoryService);
                    String response = commandCLI.ProcessMain(com);
                    if (response.equals(Constants.exit)) {
                        System.out.println("bye");
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input please try again");
                }
            }
        }
        else
        {
            System.out.println("Invalid UI param input please try again");
        }
    }

    /**
     * Returns the delay in milliseconds until midnight.
     *
     * @return The delay in milliseconds until midnight.
     */
    private static long getDelayUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        Duration duration = Duration.between(now, midnight);
        return duration.toMillis();
    }

}