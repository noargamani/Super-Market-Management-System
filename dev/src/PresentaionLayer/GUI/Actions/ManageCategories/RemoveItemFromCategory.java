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

public class RemoveItemFromCategory extends JFrame implements ActionListener {

    private InventoryService inventoryService;
    private JPanel commandPanel;
    private JTextField categoryIDField;
    private JTextField itemNameField;
    private JTextField itemManufacturerField;
    private JButton removeButton;
    private JPanel mainPanel;

    public RemoveItemFromCategory(InventoryService service){
        this.inventoryService = service;
        setTitle("Remove Item From Category");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setPreferredSize(new Dimension(600, 150));

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
        LabelFontDesign(itemManufacturerLabel);
        itemManufacturerField = new JTextField();

        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);

        commandPanel.add(categoryIDLabel);
        commandPanel.add(categoryIDField);
        commandPanel.add(itemNameLabel);
        commandPanel.add(itemNameField);
        commandPanel.add(itemManufacturerLabel);
        commandPanel.add(itemManufacturerField);
        ButtonDesign(removeButton);
        commandPanel.add(removeButton);

        mainPanel.add(commandPanel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == removeButton){
            JFrame frame = new JFrame();
            if (!categoryIDField.getText().isEmpty() && !itemNameField.getText().isEmpty() && !itemManufacturerField.getText().isEmpty()){
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

                if (inventoryService.RemoveItemFromCategory(categoryID, myCatalogNum)) {
                    JOptionPane.showMessageDialog(frame, "Item removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Item doesnt exist in the category", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Please insert legal values", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }
    }
}
