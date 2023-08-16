package Presentaion_layer.GUI.Actions.StoreManager.Suppliers;

import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Order;
import Presentaion_layer.GUI.Actions.SupplierGUI.MakeNewContractGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.GetItemGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.RemoveItemGUI;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.Hashtable;

import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class GetAllRoutineOrders extends JFrame {

    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JComboBox<String> fieldComboBox;
    private JTextArea resultTextArea;

    public GetAllRoutineOrders(Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> items) {
        this.service = service;

        setTitle("Get Routine Orders Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 300));
        initComponents(items);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private static DefaultTableModel getTableModel(Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> items) {
        String[] columnNames = {"ID", "Day", "Item + Manufacture", "Quantity"};

        Object[][] data = new Object[items.size()][columnNames.length];
        System.out.println(items);
        int i=0;

      for(Integer s:items.keySet()){
          try {


              Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>> supplier = items.get(s);
              for(Contract.Day day:supplier.keySet()){
                  Hashtable<Constants.Pair<String, String>, Integer> order=supplier.get(day);
                    for(Constants.Pair<String,String> pair:order.keySet()){
                        data[i][0] = s;
                        data[i][1] = day;
                        data[i][2] = pair.first+" "+pair.second;
                        data[i][3] = order.get(pair);

                    }
              }


          }
          catch (Exception e){

          }
            i++;

        }

        return new DefaultTableModel(data, columnNames);
    }
    private void initComponents(Hashtable<Integer, Hashtable<Contract.Day, Hashtable<Constants.Pair<String, String>, Integer>>> items) {
        JPanel mainPanel = new JPanel(new BorderLayout());
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


   /* private void getSupplierInformation() {
        String supplierName = supplierNameField.getText();
        int selectedFieldIndex = fieldComboBox.getSelectedIndex() + 1; // Convert to 1-based index

        try {
            Supplier supplier = service.getSupplier(supplierName);
            if(!can_edit && selectedFieldIndex > 11)
            {
                resultTextArea.setText("Invalid request");
                return;
            }
            if (supplier != null) {
                switch (selectedFieldIndex) {
                    case 1:
                        resultTextArea.setText("Supplier address: " + supplier.getAddress());
                        break;
                    case 2:
                        resultTextArea.setText("Supplier manufactures: " + supplier.getManufactures());
                        break;
                    case 3:
                        resultTextArea.setText("Supplier contact list: " + supplier.getContact_List());
                        break;
                    case 4:
                        resultTextArea.setText("Supplier discount by price: " + supplier.getDiscountByTotalPrice());
                        break;
                    case 5:
                        resultTextArea.setText("Supplier discount by quantity: " + supplier.getDiscountByTotalQuantity());
                        break;
                    case 6:
                        resultTextArea.setText("Supplier's items: " + supplier.getItems());
                        break;
                    case 7:
                        resultTextArea.setText("Supplier card: " + supplier.getSupplier_card());
                        break;
                    case 8:
                        resultTextArea.setText("Supplier contract: " + supplier.getContract());
                        break;
                    case 9:
                        resultTextArea.setText("Supplier's orders: " + supplier.getFinalOrders());
                        break;
                    case 10:
                        resultTextArea.setText(supplier.getItems().toString());
                        openGetItemGUI(supplierName, service);
                        break;
                    case 11:
                        resultTextArea.setText("Supplier orders: " + supplier.getOrders());
                        break;
                    case 12:
                        resultTextArea.setText(supplier.getItems().toString());
                        openRemoveItemGUI();
                        // Call the method to handle "Remove Item" functionality
                        // Remove_Item.remove_Item(service, supplierName);
                        break;
                    case 13:
                        // Show a dialog or input field to get the status command
                        String status = JOptionPane.showInputDialog(null, "Enter status command:");
                        updateOrderStatus(supplierName, status);
                        break;
                    case 14:
                        openMakeNewContractGUI();
                        break;
                    default:
                        resultTextArea.setText("Invalid request");
                        break;
                }
            } else {
                resultTextArea.setText("Supplier not found");
            }
        } catch (Exception e) {
            resultTextArea.setText(e.getMessage());
        }
    }*/

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
