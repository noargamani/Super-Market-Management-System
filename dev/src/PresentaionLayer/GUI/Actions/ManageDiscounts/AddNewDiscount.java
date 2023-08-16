package Presentaion_layer.GUI.Actions.ManageDiscounts;

import BusinessLayer.Item;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddNewDiscount extends JFrame implements ActionListener{

    private InventoryService inventoryService;
    private JPanel commandPanel;
    private JTextField saleNumberField;
    private JTextField saleNameField;
    private JTextField discountField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JRadioButton itemRadioButton;
    private JRadioButton categoryRadioButton;
    private JTextField itemNameField;
    private JTextField itemManufacturerField;
    private JTextField categoryIDField;
    private JButton addButton;
    private JButton addCategoryIDButton;
    private JLabel itemNameLabel;
    private JLabel itemManufacturerLabel;
    private JLabel categoryIDLabel;
    private List<Integer> categoriesID;
    private JPanel mainPanel;

    public AddNewDiscount(InventoryService service){
        this.setTitle("Add New Discount");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.inventoryService = service;
        this.categoriesID=new LinkedList<>();

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        commandPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        BackGroundDesign(commandPanel);

        JLabel saleNumberLabel = new JLabel("Sale Number:");
        LabelFontDesign(saleNumberLabel);
        saleNumberField = new JTextField();
        JLabel saleNameLabel = new JLabel("Sale Name:");
        LabelFontDesign(saleNameLabel);
        saleNameField = new JTextField();
        JLabel discountLabel = new JLabel("Discount:");
        LabelFontDesign(discountLabel);
        discountField = new JTextField();
        JLabel startDateLabel = new JLabel("Start Date (dd/mm/yyyy):");
        LabelFontDesign(startDateLabel);
        startDateField = new JTextField();
        JLabel endDateLabel = new JLabel("End Date (dd/mm/yyyy):");
        LabelFontDesign(endDateLabel);
        endDateField = new JTextField();
        JLabel discountTypeLabel = new JLabel("Discount Type:");
        LabelFontDesign(discountTypeLabel);
        itemRadioButton = new JRadioButton("By Item");
        itemRadioButton.setFont(new Font("Calibri", Font.BOLD,16));
        itemRadioButton.setBackground(new Color(167, 196, 188));
        categoryRadioButton = new JRadioButton("By Category");
        categoryRadioButton.setFont(new Font("Calibri", Font.BOLD,16));
        categoryRadioButton.setBackground(new Color(167, 196, 188));
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(itemRadioButton);
        radioButtonGroup.add(categoryRadioButton);

        itemNameLabel = new JLabel("Item Name:");
        LabelFontDesign(itemNameLabel);
        itemNameLabel.setEnabled(false);
        itemNameField = new JTextField();
        itemNameField.setEnabled(false);

        itemManufacturerLabel = new JLabel("Item Manufacturer:");
        LabelFontDesign(itemManufacturerLabel);
        itemManufacturerLabel.setEnabled(false);
        itemManufacturerField = new JTextField();
        itemManufacturerField.setEnabled(false);

        categoryIDLabel = new JLabel("Category ID:");
        LabelFontDesign(categoryIDLabel);
        categoryIDLabel.setEnabled(false);
        categoryIDField = new JTextField();
        categoryIDField.setEnabled(false);

        addCategoryIDButton = new JButton("Add To Discount");
        addCategoryIDButton.setEnabled(false);
        addCategoryIDButton.addActionListener(this);

        itemRadioButton.addActionListener(this);
        categoryRadioButton.addActionListener(this);

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        ButtonDesign(addButton);

        commandPanel.add(saleNumberLabel);
        commandPanel.add(saleNumberField);
        commandPanel.add(new JLabel());
        commandPanel.add(saleNameLabel);
        commandPanel.add(saleNameField);
        commandPanel.add(new JLabel());
        commandPanel.add(discountLabel);
        commandPanel.add(discountField);
        commandPanel.add(new JLabel());
        commandPanel.add(startDateLabel);
        commandPanel.add(startDateField);
        commandPanel.add(new JLabel());
        commandPanel.add(endDateLabel);
        commandPanel.add(endDateField);
        commandPanel.add(new JLabel());
        commandPanel.add(discountTypeLabel);
        commandPanel.add(itemRadioButton);
        commandPanel.add(categoryRadioButton);
        commandPanel.add(itemNameLabel);
        commandPanel.add(itemNameField);
        commandPanel.add(new JLabel());
        commandPanel.add(itemManufacturerLabel);
        commandPanel.add(itemManufacturerField);
        commandPanel.add(new JLabel());
        commandPanel.add(categoryIDLabel);
        commandPanel.add(categoryIDField);
        commandPanel.add(addCategoryIDButton);
        commandPanel.add(addButton);

        mainPanel.add(commandPanel);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == itemRadioButton) {
            itemNameLabel.setEnabled(true);
            itemManufacturerLabel.setEnabled(true);
            itemNameField.setEnabled(true);
            itemManufacturerField.setEnabled(true);
            categoryIDField.setEnabled(false);
            addCategoryIDButton.setEnabled(false);
            categoryIDLabel.setEnabled(false);

        } else if (e.getSource() == categoryRadioButton) {
            itemNameLabel.setEnabled(false);
            itemManufacturerLabel.setEnabled(false);
            itemNameField.setEnabled(false);
            itemManufacturerField.setEnabled(false);
            categoryIDField.setEnabled(true);
            addCategoryIDButton.setEnabled(true);
            categoryIDLabel.setEnabled(true);

        }else if(e.getSource() == addCategoryIDButton) {
            int categoryID;
            try {
                categoryID = Integer.parseInt(categoryIDField.getText());
                categoriesID.add(categoryID);
                categoryIDField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Invalid category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

        }
        else if (e.getSource() == addButton){
            int saleNumber;

            if(saleNumberField.getText().isEmpty() || saleNameField.getText().isEmpty() || discountField.getText().isEmpty()
            || startDateField.getText().isEmpty() || endDateField.getText().isEmpty() || (itemRadioButton.isSelected() && itemNameField.getText().isEmpty() && itemManufacturerField.getText().isEmpty())
            || (categoryRadioButton.isSelected() && categoriesID.isEmpty()))
                CommandGUI.showAlertDialog("Add New Discount", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
            else {
                try {
                    saleNumber = Integer.parseInt(saleNumberField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid sale number", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String saleName = saleNameField.getText();

                double discount;
                try {
                    discount = Double.parseDouble(discountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid discount", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String startDateText = startDateField.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate startDate;
                try {
                    startDate = LocalDate.parse(startDateText, formatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid start date", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String endDateText = endDateField.getText();
                LocalDate endDate;
                try {
                    endDate = LocalDate.parse(endDateText, formatter);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainPanel, "Invalid end date", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (itemRadioButton.isSelected()) {
                    String itemName = itemNameField.getText();
                    String itemManufacturer = itemManufacturerField.getText();
                    int catalogNumber = Item.getItemCatalogNumber(itemName, itemManufacturer);
                    List<Integer> catalogNumbers = new LinkedList<>();
                    catalogNumbers.add(catalogNumber);
                    try {
                        inventoryService.addItemDiscount(saleNumber, saleName, discount, startDate, endDate, catalogNumbers);
                        JOptionPane.showMessageDialog(mainPanel, "Discount added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (categoryRadioButton.isSelected()) {
                    try {
                        inventoryService.addCategoryDiscount(saleNumber, saleName, discount, startDate, endDate, categoriesID);
                        JOptionPane.showMessageDialog(mainPanel, "Discount added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (Exception ex){
                            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a discount type", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                categoriesID.clear();
            }
        }
    }
}
