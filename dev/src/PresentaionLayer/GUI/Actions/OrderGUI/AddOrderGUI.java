package Presentaion_layer.GUI.Actions.OrderGUI;

import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.SupplierItem;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.SortedMap;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddOrderGUI extends JFrame {

    private Suppliers_Service service;

    public AddOrderGUI(Suppliers_Service service) {
        this.service = service;

        setTitle("Add Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2,10,10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(inputPanel);

        JTextField catalog_id_Text = new JTextField();
        JTextField itemQuantityField = new JTextField();
        JLabel supplierNameLabel = new JLabel("Supplier Name:");
        LabelFontDesign(supplierNameLabel);
        JTextField supplierNameField = new JTextField(15);

        //Add items
        Hashtable<Integer,Integer> supplierItems=new Hashtable<>();
        JLabel itemCatalogIDLabel = new JLabel("Item catalog id:");
        LabelFontDesign(itemCatalogIDLabel);

        inputPanel.add(itemCatalogIDLabel);
        inputPanel.add(catalog_id_Text);

        JLabel itemQuantity = new JLabel("Item Quantity:");
        LabelFontDesign(itemQuantity);
        inputPanel.add(itemQuantity);

        inputPanel.add(itemQuantityField);

        JButton addItem=new JButton("add item");
        ButtonDesign(addItem);
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {


                    if (catalog_id_Text.getText().length() != 0 && itemQuantityField.getText().length() != 0) {
                        SortedMap<Integer, SupplierItem> supplierItemSortedMap=service.getSupplierController().getSupplierDAO().getItemsBySupplier(supplierNameField.getText().toString());
                        System.out.println(supplierItemSortedMap);

                        SupplierItem item=supplierItemSortedMap.get(Integer.valueOf(catalog_id_Text.getText().toString()));
                        supplierItems.put(item.getSupplier_catalogID(),Integer.valueOf(itemQuantityField.getText()));
                        System.out.println(supplierItems);
                        System.out.println(item.getDiscountByQuantity());

                    }
                    else{
                        JOptionPane.showMessageDialog(AddOrderGUI.this, "Invalid input");

                    }
                }
                catch (Exception e12) {

                        JOptionPane.showMessageDialog(AddOrderGUI.this, "Invalid input");

                }
            }
        });
        inputPanel.add(addItem);

        inputPanel.add(new JLabel());
        inputPanel.add(supplierNameLabel);
        inputPanel.add(supplierNameField);

        JButton addButton = new JButton("Add Order");
        ButtonDesign(addButton);
        inputPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String supplierName = supplierNameField.getText();
                if (service.ContainsSupplierName(supplierName)) {
                    try {
                        service.addOrder(supplierItems, supplierName);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(AddOrderGUI.this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    JOptionPane.showMessageDialog(AddOrderGUI.this, "Order sent!");
                } else {
                    JOptionPane.showMessageDialog(AddOrderGUI.this, "Supplier does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton exitButton = new JButton("Exit");
        ButtonDesign(exitButton);
        inputPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // End the program run
            }
        });

        mainPanel.add(inputPanel);
        //mainPanel.add(addButton);
        //mainPanel.add(exitButton);

        add(mainPanel, BorderLayout.CENTER);
    }
}
