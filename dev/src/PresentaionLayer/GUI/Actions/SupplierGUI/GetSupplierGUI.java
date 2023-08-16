package Presentaion_layer.GUI.Actions.SupplierGUI;

import BusinessLayer.Suppliers.Classes.Supplier;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.GetItemGUI;
import Presentaion_layer.GUI.Actions.Supplier_ItemGUI.RemoveItemGUI;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;
import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class GetSupplierGUI extends JFrame {
    private final boolean is_edit;
    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JComboBox<String> fieldComboBox;
    private JTextArea resultTextArea;

    public GetSupplierGUI(Suppliers_Service service, boolean is_edit) {
        this.service = service;
        this.is_edit = is_edit;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setPreferredSize(new Dimension(400, 300));
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();

        JPanel inputPanel = new JPanel(new FlowLayout());
        //JPanel inputPanel = new JPanel(new GridLayout(0, 1,10,10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(inputPanel);

        JLabel supplierNameLabel = new JLabel("Supplier Name:");
        LabelFontDesign(supplierNameLabel);
        supplierNameField = new JTextField(15);
        inputPanel.add(supplierNameLabel);
        inputPanel.add(supplierNameField);

        JPanel fieldPanel = new JPanel(new FlowLayout());
        JLabel fieldLabel = new JLabel("Select Field:");
        LabelFontDesign(fieldLabel);
        fieldComboBox = new JComboBox<>();
        BackGroundDesign(fieldPanel);
        fieldComboBox.setFont(new Font("Calibri", Font.BOLD,16));

        // Add options based on user's authorization
        if (is_edit) {
            setTitle("Manage Supplier Information");
            fieldComboBox.addItem("Update Order Status");
            fieldComboBox.addItem("Make New Contract");
        } else {
            setTitle("Get Supplier Information");
            fieldComboBox.addItem("Address");
            fieldComboBox.addItem("Manufactures");
            fieldComboBox.addItem("Contact List");
            fieldComboBox.addItem("Discount By Total Price");
            fieldComboBox.addItem("Discount By Total Quantity");
            fieldComboBox.addItem("Items");
            fieldComboBox.addItem("Supplier Card");
            fieldComboBox.addItem("Contract");
            fieldComboBox.addItem("Orders Sent to Supplier");
            fieldComboBox.addItem("Get Item");
            fieldComboBox.addItem("Orders");
        }

        fieldPanel.add(fieldLabel);
        fieldPanel.add(fieldComboBox);

        JButton getButton = new JButton("Get");
        ButtonDesign(getButton);

        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        mainPanel.add(getButton, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);

        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSupplierInformation();
            }
        });
    }


    private void getSupplierInformation() {
        String supplierName = supplierNameField.getText();
        int selectedFieldIndex = fieldComboBox.getSelectedIndex() + 1; // Convert to 1-based index

        try {
            Supplier supplier = service.getSupplier(supplierName);
            if(!is_edit && selectedFieldIndex > 11)
            {
                resultTextArea.setText("Invalid request");
                return;
            }
            else if(is_edit && selectedFieldIndex > 2)
            {
                resultTextArea.setText("Invalid request");
                return;
            }
            else if(is_edit)
            {
                selectedFieldIndex += 12;
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
                        JFrame frameUpdate = new JFrame();
                        frameUpdate.setLayout(new BorderLayout());

                        // Create the combo box and panel
                        JComboBox<String> myCombo = new JComboBox<>();
                        myCombo.setFont(new Font("Calibri", Font.BOLD, 16));
                        myCombo.addItem("Sent");
                        myCombo.addItem("Received");
                        myCombo.addItem("Delivered");
                        myCombo.addItem("Canceled");

                        JPanel fieldPanel = new JPanel(new FlowLayout());
                        fieldPanel.add(new JLabel("Select Field:"));
                        fieldPanel.add(myCombo);

                        // Create the button
                        JButton confirmButton = new JButton("Confirm");

                        // Add the panel and button to the frame
                        frameUpdate.add(fieldPanel, BorderLayout.CENTER);
                        frameUpdate.add(confirmButton, BorderLayout.SOUTH);

                        // Show the frame
                        frameUpdate.pack();
                        frameUpdate.setVisible(true);

                        // Action listener for the confirm button
                        confirmButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    int status_num = myCombo.getSelectedIndex() + 1; // Convert to 1-based index
                                    String status = getComboStatus(status_num);
                                    updateOrderStatus(supplierName, status);
                                }
                                catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                finally {
                                    frameUpdate.dispose();
                                }
                            }
                        });
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
    }

    private String getComboStatus(int statusNum) {
        String status = "";
        switch (statusNum) {
            case 1:
                status = "Sent";
                break;
            case 2:
                status = "Received";
                break;
            case 3:
                status = "Delivered";
                break;
            case 4:
                status = "Canceled";
                break;
            default:
                status = "Invalid request";
                break;
        }
        return status;
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
        try {
        String orderID = JOptionPane.showInputDialog(null, "Enter order ID:");
            service.updateOrderStatus(Integer.parseInt(orderID), supplierName, status);
            resultTextArea.setText("Order status updated successfully");
        } catch (Exception e) {
            // show error tab
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
