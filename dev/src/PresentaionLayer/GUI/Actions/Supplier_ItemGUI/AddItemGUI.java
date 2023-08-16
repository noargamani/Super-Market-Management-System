package Presentaion_layer.GUI.Actions.Supplier_ItemGUI;

import Presentaion_layer.GUI.CommandGUI;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static Presentaion_layer.GUI.CommandGUI.*;
import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class AddItemGUI extends JFrame {
    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JTextField itemNameField;
    private JTextField itemManufactureField;
    private JTextField catalogIdField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField expirationDateField;
    private JTextField discountsTextArea;
    private JTextArea resultTextArea;

    public AddItemGUI(Suppliers_Service service) {
        this.service = service;

        setTitle("Add Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 600));
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        JPanel inputPanel = new JPanel(new GridLayout(13, 2,10,10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(inputPanel);

        JLabel supplierNameLabel = new JLabel("Supplier Name:");
        LabelFontDesign(supplierNameLabel);
        supplierNameField = new JTextField(15);

        JLabel itemNameLabel = new JLabel("Item Name:");
        LabelFontDesign(itemNameLabel);
        itemNameField = new JTextField(15);

        JLabel itemManufactureLabel = new JLabel("Item Manufacture:");
        LabelFontDesign(itemManufactureLabel);
        itemManufactureField = new JTextField(15);

        JLabel catalogIdLabel = new JLabel("Catalog ID:");
        LabelFontDesign(catalogIdLabel);
        catalogIdField = new JTextField(15);

        JLabel quantityLabel = new JLabel("Quantity:");
        LabelFontDesign(quantityLabel);
        quantityField = new JTextField(15);

        JLabel priceLabel = new JLabel("Price:");
        LabelFontDesign(priceLabel);
        priceField = new JTextField(15);

        JLabel expirationDateLabel = new JLabel("Expiration Date:");
        LabelFontDesign(expirationDateLabel);
        expirationDateField = new JTextField(15);

        discountsTextArea= new JTextField(15);
        inputPanel.add(supplierNameLabel);
        inputPanel.add(supplierNameField);
        inputPanel.add(itemNameLabel);

        inputPanel.add(itemNameField);
        inputPanel.add(itemManufactureLabel);
        inputPanel.add(itemManufactureField);
        inputPanel.add(catalogIdLabel);
        inputPanel.add(catalogIdField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        SortedMap<Integer,Integer> supplierItemsDiscounts=new TreeMap<>();
        JTextField quantity=new JTextField("quantity");
        JTextField discount=new JTextField("discount");

        JLabel Item_quantity = new JLabel("Item quantity:");
        LabelFontDesign(Item_quantity);
        inputPanel.add(Item_quantity);
        inputPanel.add(quantity);

        JLabel Item_discount = new JLabel("Item discount:");
        LabelFontDesign(Item_discount);
        inputPanel.add(Item_discount);
        inputPanel.add(discount);

        JButton addItem=new JButton("add discount");
        ButtonDesign(addItem);

        JTextField discounts=new JTextField();
        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    supplierItemsDiscounts.put(Integer.valueOf(quantity.getText()),Integer.valueOf(discount.getText()));
                    discounts.setText(supplierItemsDiscounts.toString());
                }
                catch (Exception e21){
                    JOptionPane.showMessageDialog(AddItemGUI.this,"invalid input","TITLE",ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(addItem);

        inputPanel.add(discounts);
        inputPanel.add(expirationDateLabel);
        inputPanel.add(expirationDateField);

        JButton addButton = new JButton("Add");
        ButtonDesign(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem(supplierItemsDiscounts);
            }
        });


        //   mainPanel.add(discountsLabel, BorderLayout.CENTER);
        // mainPanel.add(new JScrollPane(discountsTextArea), BorderLayout.CENTER);
        inputPanel.add(addButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        //resultTextArea = new JTextArea(10, 30);
        //resultTextArea.setEditable(false);
        //mainPanel.add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addItem(SortedMap<Integer, Integer> supplierItemsDiscounts) {
        try {
            String supplierName = supplierNameField.getText();
            String itemName = itemNameField.getText();
            String itemManufacture = itemManufactureField.getText();
            int catalogId = Integer.parseInt(catalogIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            String expirationDateInput = expirationDateField.getText();
            String discountsInput = discountsTextArea.getText();

            try {
                Date expirationDate = parseDate(expirationDateInput);
                // SortedMap<Integer, Integer> discounts = parseDiscounts(discountsInput);

                service.addItem(supplierName, itemName, itemManufacture, catalogId, price, supplierItemsDiscounts, quantity, expirationDate);
                JOptionPane.showMessageDialog(AddItemGUI.this, "Item added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(AddItemGUI.this, e.getMessage(), "Error", ERROR_MESSAGE);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(AddItemGUI.this,"invalid input","Error",ERROR_MESSAGE);
        }
    }

    private Date parseDate(String input) throws IllegalArgumentException {
        String[] split = input.split("\\s");
        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected format: <year> <month> <day>");
        }

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        return new Date(year, month, day);
    }

    private SortedMap<Integer, Integer> parseDiscounts(String input) throws IllegalArgumentException {
        SortedMap<Integer, Integer> discounts = new TreeMap<>();

        String[] lines = input.split("\\n");
        for (String line : lines) {
            String[] split = line.split("\\s");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid discount format. Expected format: <quantity> <discount>");
            }

            int quantity = Integer.parseInt(split[0]);
            int discount = Integer.parseInt(split[1]);

            discounts.put(quantity, discount);
        }

        return discounts;
    }
}
