package Presentaion_layer.GUI.Actions.ManageCategories;


import BusinessLayer.Item;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddItemToCategory extends JFrame implements ActionListener {

    private InventoryService inventoryService;
    private JPanel commandPanel;
    private JTextField categoryIDField;
    private JTextField itemNameField;
    private JTextField itemManufacturerField;
    private JButton addButton;
    private JPanel mainPanel;


    public AddItemToCategory(InventoryService service){
        this.inventoryService = service;
        setTitle("Add Item To Category");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        commandPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        BackGroundDesign(commandPanel);

        JLabel categoryIDLabel = new JLabel("Category ID:");
        LabelFontDesign(categoryIDLabel);
        categoryIDField = new JTextField();
        JLabel itemNameLabel = new JLabel("Item Name:");
        LabelFontDesign(itemNameLabel);
        itemNameField = new JTextField();
        JLabel itemManufacturerLabel = new JLabel("Item Manufacturer:");
        itemManufacturerField = new JTextField();
        LabelFontDesign(itemManufacturerLabel);

        addButton = new JButton("Add");
        addButton.addActionListener(this);

        commandPanel.add(categoryIDLabel);
        commandPanel.add(categoryIDField);
        commandPanel.add(itemNameLabel);
        commandPanel.add(itemNameField);
        commandPanel.add(itemManufacturerLabel);
        commandPanel.add(itemManufacturerField);
        commandPanel.add(addButton);
        ButtonDesign(addButton);

        mainPanel.add(commandPanel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addButton){
            JFrame frame = new JFrame();
            if(!categoryIDField.getText().isEmpty() && !itemNameField.getText().isEmpty() && !itemManufacturerField.getText().isEmpty()){
                int categoryID;
                try {
                    categoryID = Integer.parseInt(categoryIDField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    inventoryService.FindCategoryByID(categoryID);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String itemName = itemNameField.getText();
                String itemManufacturer = itemManufacturerField.getText();
                int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
                try {
                    inventoryService.FindMyItem(myCatalogNum);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (inventoryService.AddItemToCategory(categoryID, myCatalogNum)) {
                    JOptionPane.showMessageDialog(frame, "Item added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Please insert legal values", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
