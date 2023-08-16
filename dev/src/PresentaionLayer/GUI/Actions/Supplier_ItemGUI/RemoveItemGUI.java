package Presentaion_layer.GUI.Actions.Supplier_ItemGUI;

import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;
import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class RemoveItemGUI extends JFrame {
    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JTextField itemNameField;
    private JTextField itemManufactureField;
    private JTextArea resultTextArea;

    public RemoveItemGUI(Suppliers_Service service) {
        this.service = service;

        setTitle("Remove Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setPreferredSize(new Dimension(400, 300));
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        JPanel inputPanel = new JPanel(new GridLayout(3, 2,10,10));

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

        inputPanel.add(supplierNameLabel);
        inputPanel.add(supplierNameField);
        inputPanel.add(itemNameLabel);
        inputPanel.add(itemNameField);
        inputPanel.add(itemManufactureLabel);
        inputPanel.add(itemManufactureField);

        JButton removeButton = new JButton("Remove");
        ButtonDesign(removeButton);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeItem();
            }
        });

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(removeButton, BorderLayout.CENTER);

        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        mainPanel.add(new JScrollPane(resultTextArea), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void removeItem() {
        String supplierName = supplierNameField.getText();
        String itemName = itemNameField.getText();
        String itemManufacture = itemManufactureField.getText();

        try {
            boolean status = service.removeItem(itemName, itemManufacture, supplierName);
            resultTextArea.setText("Item removed: " + status);
        } catch (Exception e) {
            resultTextArea.setText(e.getMessage());
        }
    }
}
