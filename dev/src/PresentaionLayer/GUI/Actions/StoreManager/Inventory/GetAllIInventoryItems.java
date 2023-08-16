package Presentaion_layer.GUI.Actions.StoreManager.Inventory;

import BusinessLayer.Inventory.Classes.InventoryItem;
import Presentaion_layer.GUI.Actions.SupplierGUI.MakeNewContractGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.GetItemGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.RemoveItemGUI;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class GetAllIInventoryItems extends JFrame {

    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JComboBox<String> fieldComboBox;
    private JTextArea resultTextArea;

    public GetAllIInventoryItems(List<InventoryItem> items) {
        this.service = service;
        setTitle("Get Inventory Items Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 300));
        initComponents(items);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static DefaultTableModel getTableModel(List<InventoryItem> items) {
        String[] columnNames = {"Catalog Number", "Name", "Manufacture", "Price",
                "Shelves amount", "Warehouse amount", "Quantity", "Minimum amount"};

        Object[][] data = new Object[items.size()][columnNames.length];
        System.out.println(items);
        int i=0;

      for(InventoryItem item:items){
          try {
              data[i][0] = item.getCatalogNumber();
              data[i][1] = item.getName();
              data[i][2] = item.getManufacturer();
              data[i][3] = item.getPrice();
              data[i][4] = item.getShelvesAmount();
              data[i][5] = item.getWarehouseAmount();
              data[i][6] = item.getTotalAmount();
              data[i][7] = item.getMinimumAmount();
          }
          catch (Exception e){

          }
            i++;

        }

        return new DefaultTableModel(data, columnNames);
    }

    private void initComponents(List<InventoryItem> items) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        BackGroundDesign(mainPanel);

        BackGroundDesign(mainPanel);
        JTable supplierTable = new JTable();
        supplierTable.setModel(getTableModel(items));
        supplierTable.setBackground(new Color(223, 238, 234));

        // Create a custom table header renderer
        JTableHeader tableHeader = supplierTable.getTableHeader();
        tableHeader.setFont(new Font("Calibri", Font.BOLD, 14)); // Set the desired font


        // Create a JScrollPane to hold the table
        JScrollPane scrollPane = new JScrollPane(supplierTable);

        // Change the background color of the JScrollPane
        scrollPane.getViewport().setBackground(new Color(167, 196, 188));

        mainPanel.add(scrollPane,BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);
    }

    private void openGetItemGUI(String supplierName, Suppliers_Service service) {
        // Create the GetItemGUI instance
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GetItemGUI(service, supplierName);
            }
        });
    }
    // remove item
    private void openRemoveItemGUI() {
        // Create the GetItemGUI instance
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RemoveItemGUI(service);
            }
        });
    }
    // make new contract
    private void openMakeNewContractGUI() {
        //first get order ID from user
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MakeNewContractGUI(service);
            }
        });
    }

    private void updateOrderStatus(String supplierName, String status) {
        //first get order ID from user
        String orderID = JOptionPane.showInputDialog(null, "Enter order ID:");
        try {
            service.updateOrderStatus(Integer.parseInt(orderID), supplierName, status);
            resultTextArea.setText("Order status updated successfully");
        } catch (Exception e) {
            resultTextArea.setText(e.getMessage());
        }
    }
}
