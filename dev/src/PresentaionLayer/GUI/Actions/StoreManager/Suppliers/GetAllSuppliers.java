package Presentaion_layer.GUI.Actions.StoreManager.Suppliers;

import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.Supplier_Card;
import Presentaion_layer.GUI.Actions.SupplierGUI.MakeNewContractGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.GetItemGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.RemoveItemGUI;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class GetAllSuppliers extends JFrame {

    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JComboBox<String> fieldComboBox;
    private JTextArea resultTextArea;

    public GetAllSuppliers(Hashtable<String,Supplier> suppliers) {
        this.service = service;

        setTitle("Get Suppliers Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1100, 300));
        initComponents(suppliers);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private static DefaultTableModel getTableModel(Hashtable<String,Supplier> suppliers) {
        String[] columnNames = {"Supplier Name", "Address", "Manufacturer", "Contact List",
                "Discount by Total Price", "Discount by Total Quantity",
                "Supplier Card", "Contract"};
        Object[][] data = new Object[suppliers.size()][columnNames.length];
        System.out.println(suppliers);
        int i=0;

      for(String s:suppliers.keySet()){
          try {


              Supplier supplier = suppliers.get(s);
              data[i][0] = supplier.getSupplier_name();
              data[i][1] = supplier.getAddress();
              data[i][2] = supplier.getManufactures();
              data[i][3] = supplier.getContact_List();
              data[i][4] = supplier.getDiscountByTotalPrice();
              data[i][5] = supplier.getDiscountByTotalQuantity();
              Supplier_Card card=supplier.getSupplier_card();
              data[i][6] = "BN number:"+card.getBN_number()+" "+card.getBank_account()+" "+card.getPay_condition();
              data[i][7] = supplier.getContract().toString();
          }
          catch (Exception e){

          }
            i++;

        }

        return new DefaultTableModel(data, columnNames);
    }
    private void initComponents(Hashtable<String,Supplier> suppliers) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        BackGroundDesign(mainPanel);

        JTable supplierTable = new JTable();
        supplierTable.setModel(getTableModel(suppliers));

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
