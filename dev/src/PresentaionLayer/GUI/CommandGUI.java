package Presentaion_layer.GUI;

import BusinessLayer.Constants;
import BusinessLayer.Inventory.Classes.Category;
import BusinessLayer.Inventory.Classes.Discount;
import BusinessLayer.Inventory.Classes.InventoryItem;
import BusinessLayer.Inventory.Classes.SpecificItem;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Order;
import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.SupplierItem;
import Presentaion_layer.GUI.Actions.IssueReports.IssueReports;
import Presentaion_layer.GUI.Actions.ManageCategories.AddItemToCategory;
import Presentaion_layer.GUI.Actions.ManageCategories.AddNewCategory;
import Presentaion_layer.GUI.Actions.ManageCategories.RemoveItemFromCategory;
import Presentaion_layer.GUI.Actions.ManageDiscounts.AddNewDiscount;
import Presentaion_layer.GUI.Actions.ManageDiscounts.CheckIfItemOnDiscount;
import Presentaion_layer.GUI.Actions.Manage_Supply.*;
import Presentaion_layer.GUI.Actions.OrderGUI.AddOrderGUI;
import Presentaion_layer.GUI.Actions.OrderGUI.MakeAutoOrderGUI;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllItems;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllOrders;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllRoutineOrders;
import Presentaion_layer.GUI.Actions.StoreManager.Suppliers.GetAllSuppliers;
import Presentaion_layer.GUI.Actions.StoreManager.Inventory.GetAllICategory;
import Presentaion_layer.GUI.Actions.StoreManager.Inventory.GetAllIDiscount;
import Presentaion_layer.GUI.Actions.StoreManager.Inventory.GetAllIIInventory_Specifics_items;
import Presentaion_layer.GUI.Actions.StoreManager.Inventory.GetAllIInventoryItems;
import Presentaion_layer.GUI.Actions.SupplierGUI.AddSupplierGUI;
import Presentaion_layer.GUI.Actions.SupplierGUI.GetSupplierGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.AddItemGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;
import java.util.SortedMap;

public class CommandGUI extends JFrame{
    final Suppliers_Service suppliersService;
    final InventoryService inventoryService;

    public CommandGUI(String role, Suppliers_Service suppliersService, InventoryService inventoryService, boolean closeInExit) {
        this.suppliersService = suppliersService;
        this.inventoryService = inventoryService;
        openWindowBasedOnRole(role, closeInExit);
    }

    private void openWindowBasedOnRole(String role, boolean closeInExit) {
        if (role.equals("SupplierConnector")) {
            openSupplierWindow(closeInExit);
        } else if (role.equals("StoreKeeper")) {
            openInventoryWindow(closeInExit);
        } else if (role.equals("StoreManager")) {
            openStoreManagerWindow();
        }
        else {
            JOptionPane.showMessageDialog(this, "Invalid Role!!!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSupplierWindow(boolean closeInExit) {
        JFrame supplierFrame = new JFrame("Supplier Window");
        supplierFrame.setSize(100, 100);
        if (closeInExit){
            supplierFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        else{
            supplierFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }


        ImageIcon icon = new ImageIcon("super.png");

        supplierFrame.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel commandPanel = new JPanel(new GridLayout(0, 1, 0, 10));

        String welcome = " Welcome to Suppliers Module ";
        FontDesign(welcome, mainPanel, icon);
        BackGroundDesign(mainPanel);
        BackGroundDesign(commandPanel);

        JButton addSupplierButton = new JButton("Add Supplier");
        JButton getSupplierButton = new JButton("Get Supplier");
        JButton manageSupplierButton = new JButton("Manage Supplier");
        JButton addItemButton = new JButton("Add Item");

        JButton exitButton = new JButton("Exit");

        ButtonDesign(addSupplierButton);
        ButtonDesign(getSupplierButton);
        ButtonDesign(manageSupplierButton);
        ButtonDesign(addItemButton);
        ButtonDesign(exitButton);

        addSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSupplierGUI addSupplierGUI = new AddSupplierGUI(suppliersService);
                addSupplierGUI.setVisible(true);
            }
        });

        getSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetSupplierGUI getSupplierGUI = new GetSupplierGUI(suppliersService, false);
                getSupplierGUI.setVisible(true);
            }
        });
        manageSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetSupplierGUI getSupplierGUI = new GetSupplierGUI(suppliersService, true);
                getSupplierGUI.setVisible(true);
            }
        });

        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddItemGUI addItemGUI = new AddItemGUI(suppliersService);
                addItemGUI.setVisible(true);
            }
        });



        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(closeInExit)
                    System.exit(0);
                supplierFrame.dispose();
            }
        });

        commandPanel.add(addSupplierButton);
        commandPanel.add(getSupplierButton);
        commandPanel.add(manageSupplierButton);
        commandPanel.add(addItemButton);
        commandPanel.add(exitButton);

        mainPanel.add(commandPanel, BorderLayout.EAST);

        supplierFrame.getContentPane().add(mainPanel);
        supplierFrame.pack(); // Fit the frame to the panel size
        supplierFrame.setVisible(true);
    }

    private void openInventoryWindow(boolean closeInExit) {
        JFrame inventoryFrame = new JFrame("Inventory Window");
        inventoryFrame.setSize(400, 400);
        if (closeInExit){
            inventoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        else{
            inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        ImageIcon icon = new ImageIcon("super.png");
        inventoryFrame.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel();
        JPanel commandPanel = new JPanel(new GridLayout(0, 1, 0, 10)); // Set vertical gap of 10 pixels

        String welcome = " Welcome to Inventory Module ";
        FontDesign(welcome, mainPanel, icon);
        BackGroundDesign(mainPanel);
        BackGroundDesign(commandPanel);

        JButton issueReportsButton = new JButton("Issue Reports");
        JButton addNewItemButton = new JButton("Add New Item");
        JButton addNewSpecificItemButton = new JButton("Add New Specific Item");
        JButton deleteSpecificItemButton = new JButton("Delete Specific Item");
        JButton deleteItemButton = new JButton("Delete Item");
        JButton updateDefectiveStatusButton = new JButton("Update Defective Status");
        JButton addNewCategoryButton = new JButton("Add New Category");
        JButton addItemToCategoryButton = new JButton("Add Item To Category");
        JButton removeItemFromCategoryButton = new JButton("Remove Item From Category");
        JButton addNewDiscountButton = new JButton("Add New Discount");
        JButton checkItemOnDiscountButton = new JButton("Check If Item On Discount");
        JButton makeAutoOrderButton = new JButton("Manage Orders");
        JButton exitButton = new JButton("Exit");

        ButtonDesign(issueReportsButton);
        ButtonDesign(addNewItemButton);
        ButtonDesign(addNewSpecificItemButton);
        ButtonDesign(deleteSpecificItemButton);
        ButtonDesign(deleteItemButton);
        ButtonDesign(updateDefectiveStatusButton);
        ButtonDesign(addNewCategoryButton);
        ButtonDesign(addItemToCategoryButton);
        ButtonDesign(removeItemFromCategoryButton);
        ButtonDesign(addNewDiscountButton);
        ButtonDesign(checkItemOnDiscountButton);
        ButtonDesign(makeAutoOrderButton);
        ButtonDesign(exitButton);

        issueReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IssueReports(inventoryService);
            }
        });

        addNewItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewItem(inventoryService);
            }
        });

        addNewSpecificItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewSpecificItem(inventoryService);
            }
        });

        deleteSpecificItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteSpecificItem(inventoryService, suppliersService);
            }
        });

        deleteItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteItem(inventoryService);
            }
        });

        updateDefectiveStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateDefectiveStatus(inventoryService);
            }
        });

        addNewCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewCategory(inventoryService);
            }
        });

        addItemToCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddItemToCategory(inventoryService);
            }
        });

        removeItemFromCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new RemoveItemFromCategory(inventoryService);
            }
        });

        addNewDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewDiscount(inventoryService);
            }
        });

        checkItemOnDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckIfItemOnDiscount(inventoryService);
            }
        });

        makeAutoOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MakeAutoOrderGUI makeAutoOrderGUI = new MakeAutoOrderGUI(suppliersService);
                makeAutoOrderGUI.setVisible(true);
            }
        });


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(closeInExit)
                    System.exit(0);
                inventoryFrame.dispose();
            }
        });

        commandPanel.add(issueReportsButton);
        commandPanel.add(addNewItemButton);
        commandPanel.add(addNewSpecificItemButton);
        commandPanel.add(deleteSpecificItemButton);
        commandPanel.add(deleteItemButton);
        commandPanel.add(updateDefectiveStatusButton);
        commandPanel.add(addNewCategoryButton);
        commandPanel.add(addItemToCategoryButton);
        commandPanel.add(removeItemFromCategoryButton);
        commandPanel.add(addNewDiscountButton);
        commandPanel.add(checkItemOnDiscountButton);
        commandPanel.add(makeAutoOrderButton);
        commandPanel.add(exitButton);

        mainPanel.add(commandPanel, BorderLayout.EAST);

        inventoryFrame.add(mainPanel);
        inventoryFrame.pack(); // Fit the frame to the panel size
        inventoryFrame.setVisible(true);
    }

    private void openStoreManagerWindow() {
        JFrame storeManagerFrame = new JFrame("Store Manager Window");
        storeManagerFrame.setSize(400, 400);
        storeManagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("super.png");
        storeManagerFrame.setIconImage(icon.getImage());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel commandPanel = new JPanel(new GridLayout(0, 1,0,0));


        String welcome = " Welcome to Store Manager Module ";
        FontDesign(welcome, mainPanel, icon);
        BackGroundDesign(mainPanel);
        BackGroundDesign(commandPanel);

        JButton getSupplierButton = new JButton("Get Suppliers");
        JButton getItemsButton = new JButton("Get supplier Items");
        JButton getOrdersButton = new JButton("Get Orders From Suppliers");
        JButton getRoutineOrdersButton = new JButton("Get Routine Orders");
        JButton get_inventory_items = new JButton("Get inventory Items");
        JButton get_inventory_Specific_items = new JButton("Get inventory Specific Items");
        JButton get_category = new JButton("Get category");
        JButton get_discount = new JButton("Get discount");
        JButton issueReportsButton = new JButton("Issue Reports");
        JButton checkItemOnDiscountButton = new JButton("Check If Item On Discount");
        JButton exitButton = new JButton("Exit");

        ButtonDesign(getSupplierButton);
        ButtonDesign(getItemsButton);
        ButtonDesign(getOrdersButton);
        ButtonDesign(getRoutineOrdersButton);
        ButtonDesign(get_inventory_items);
        ButtonDesign(get_inventory_Specific_items);
        ButtonDesign(get_category);
        ButtonDesign(get_discount);

        ButtonDesign(issueReportsButton);
        ButtonDesign(checkItemOnDiscountButton);
        ButtonDesign(exitButton);

        getSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hashtable<String, Supplier> suppliers=suppliersService.getSupplierController().getSuppliers();
                GetAllSuppliers getSupplierGUI = new GetAllSuppliers(suppliers);
                getSupplierGUI.setVisible(true);
            }
        });

        issueReportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IssueReports(inventoryService);
            }
        });

        checkItemOnDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckIfItemOnDiscount(inventoryService);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SortedMap<Integer, SupplierItem> suppliers=suppliersService.getSupplierController().getSupplierDAO().getItemID_To_Item();
                GetAllItems getSupplierGUI = new GetAllItems(suppliers);
                getSupplierGUI.setVisible(true);
            }
        });

        getOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hashtable<String, Supplier> suppliers=suppliersService.getSupplierController().getSuppliers();

                Hashtable<Integer, Order> orderHashtable=new Hashtable<>();
                for(String s:suppliers.keySet())
                orderHashtable.putAll(suppliersService.getSupplierController().getSupplierDAO().getOrdersTable(s));
                GetAllOrders getSupplierGUI = new GetAllOrders(orderHashtable);
                getSupplierGUI.setVisible(true);
            }
        });

        getRoutineOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> suppliers = suppliersService.getSupplierController().getSupplierDAO().getAllPeriodicOrders();

              GetAllRoutineOrders getSupplierGUI = new GetAllRoutineOrders(suppliers);
                getSupplierGUI.setVisible(true);
            }
        });

        get_inventory_items.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<InventoryItem> suppliers=inventoryService.getIC().getTotalItem();
                GetAllIInventoryItems getSupplierGUI = new GetAllIInventoryItems(suppliers);
            }
        });

        get_inventory_Specific_items.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<SpecificItem> suppliers = inventoryService.getIC().getSpecificItemDAO().getAllItems();
                    GetAllIIInventory_Specifics_items getSupplierGUI = new GetAllIIInventory_Specifics_items(suppliers);
                }
                catch (Exception e1){

                }
            }
        });

        get_category.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Category> suppliers = inventoryService.getIC().getCategoryList();
                GetAllICategory getSupplierGUI = new GetAllICategory(suppliers);

            }
        });

        get_discount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Discount> suppliers = inventoryService.getIC().getDiscountList();
                GetAllIDiscount getSupplierGUI = new GetAllIDiscount(suppliers);
            }
        });

        JLabel Inventory = new JLabel("Inventory");
        Inventory.setForeground( new Color(47, 93, 98));
        Inventory.setFont(new Font("Impact", Font.PLAIN, 25));
        Inventory.setHorizontalAlignment(SwingConstants.CENTER);

        JButton InventoryWindow = new JButton("Inventory Module");
        ButtonDesign(InventoryWindow);

        InventoryWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInventoryWindow(false);
            }
        });

        commandPanel.add(Inventory);
        commandPanel.add(InventoryWindow);
        commandPanel.add(get_inventory_items);
        commandPanel.add(get_inventory_Specific_items);
        commandPanel.add(get_category);
        commandPanel.add(get_discount);
        commandPanel.add(getRoutineOrdersButton);
        commandPanel.add(getOrdersButton);
        commandPanel.add(issueReportsButton);
        commandPanel.add(checkItemOnDiscountButton);


        JLabel Suppliers = new JLabel("Suppliers");
        Suppliers.setForeground( new Color(47, 93, 98));
        Suppliers.setFont(new Font("Impact", Font.PLAIN, 25));
        Suppliers.setHorizontalAlignment(SwingConstants.CENTER);

        JButton SuppliersWindow = new JButton("Suppliers Module");
        ButtonDesign(SuppliersWindow);
        SuppliersWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSupplierWindow(false);
            }
        });

        commandPanel.add(Suppliers);
        commandPanel.add(SuppliersWindow);
        commandPanel.add(getSupplierButton);
        commandPanel.add(getItemsButton);
        JLabel emptyLine = new JLabel("");
        commandPanel.add(emptyLine);
        commandPanel.add(exitButton);

        mainPanel.add(commandPanel, BorderLayout.EAST);

        storeManagerFrame.add(mainPanel);
        storeManagerFrame.pack();
        storeManagerFrame.setVisible(true);
    }

    public static void FontDesign(String str, JPanel panel, ImageIcon image){
        JLabel txt = new JLabel(str);
        if(image != null)
            txt.setIcon(image);
        txt.setHorizontalTextPosition(JLabel.CENTER);
        txt.setVerticalTextPosition(JLabel.TOP);
        txt.setForeground( new Color(47, 93, 98));
        txt.setFont(new Font("Impact", Font.PLAIN, 40));
        panel.add(txt);
    }

    public static void ButtonDesign(JButton button){
        button.setPreferredSize(new Dimension(200, 40)); // Set width and height
        button.setFocusable(false);
        button.setBackground(new Color(94, 139, 126));
        button.setBorder(BorderFactory.createLineBorder(new Color(223, 238, 234)));
        button.setForeground(new Color(223, 238, 234));
        button.setFont(new Font("Impact", Font.PLAIN, 15));
    }

    public static void BackGroundDesign(JPanel panel){
        panel.setBackground(new Color(167, 196, 188));
    }

    public static void LabelFontDesign(JLabel label){
        label.setFont(new Font("Calibri", Font.BOLD,16));
    }

    public static void showAlertDialog(String title, String message, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(null, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
