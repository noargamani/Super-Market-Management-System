package Presentaion_layer.GUI.Actions.Supplier_ItemGUI;

import BusinessLayer.Suppliers.Classes.SupplierItem;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;
import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class GetItemGUI extends JFrame {
    private JTextField itemNameField;
    private JTextField manufactureField;
    private JButton getItemButton;
    private JTextArea resultArea;
    private Suppliers_Service service;
    private String supplierName;

    public GetItemGUI(Suppliers_Service service, String supplierName) {
        this.service = service;
        this.supplierName = supplierName;

        setTitle("Get Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setPreferredSize(new Dimension(400, 300));

        itemNameField = new JTextField(20);
        manufactureField = new JTextField(20);
        getItemButton = new JButton("Get Item");
        ButtonDesign(getItemButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        JPanel inputPanel = new JPanel();
        BackGroundDesign(inputPanel);

        JLabel ItemName = new JLabel("Item Name:");
        LabelFontDesign(ItemName);
        inputPanel.add(ItemName);
        inputPanel.add(itemNameField);

        JLabel Manufacture = new JLabel("Manufacture:");
        LabelFontDesign(Manufacture);
        inputPanel.add(Manufacture);
        inputPanel.add(manufactureField);
        inputPanel.add(getItemButton);

        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(resultScrollPane, BorderLayout.CENTER);

        getItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String manufacture = manufactureField.getText();
                getItem(itemName, manufacture);
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void getItem(String itemName, String manufacture) {
        try {
            JOptionPane.showMessageDialog(this, "Getting item: " + itemName + " from manufacture: " + manufacture);
            SupplierItem supplierItem = service.getItem(itemName, manufacture, supplierName);
            if (supplierItem != null) {
                String result = "Item ID: " + supplierItem.getCatalogNumber() + "\n" +
                        "Item Supplier Info: " + supplierItem.getSupplier().toString() + " " + supplierItem.getSupplier_catalogID() + "\n" +
                        "Item Name: " + supplierItem.getName() + "\n" +
                        "Item discount by quantity: " + supplierItem.getDiscountByQuantity() + "\n" +
                        "Item ListPrice: " + supplierItem.getItem_list_price() + "\n" +
                        "Item TOTAL INFO: " + supplierItem.toString();
                resultArea.setText(result);
            } else {
                resultArea.setText("Item not found.");
                JOptionPane.showMessageDialog(this, "Sadly, Item wasn't found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
