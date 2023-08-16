package Presentaion_layer.GUI.Actions.ManageCategories;


import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;
import Presentaion_layer.GUI.CommandGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddNewCategory extends JFrame implements ActionListener {

    InventoryService service;
    private JPanel commandPanel;
    private JTextField categoryIDField;
    private JTextField categoryNameField;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JTextField fatherCategoryIDField;
    private JLabel fatherCategoryIDLabel;
    private JButton addButton;
    private JPanel mainPanel;


    public AddNewCategory(InventoryService service){
        this.service = service;
        setTitle("Add New Category");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        commandPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        BackGroundDesign(commandPanel);

        JLabel categoryIDLabel = new JLabel("Category ID:");
        LabelFontDesign(categoryIDLabel);
        categoryIDField = new JTextField();
        JLabel categoryNameLabel = new JLabel("Category Name:");
        LabelFontDesign(categoryNameLabel);
        categoryNameField = new JTextField();
        JLabel hasFatherCategoryLabel = new JLabel("There is a father category?");
        LabelFontDesign(hasFatherCategoryLabel);
        yesRadioButton = new JRadioButton("Yes");
        yesRadioButton.setBackground(new Color(167, 196, 188));
        noRadioButton = new JRadioButton("No");
        noRadioButton.setBackground(new Color(167, 196, 188));
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(yesRadioButton);
        radioButtonGroup.add(noRadioButton);

        fatherCategoryIDLabel = new JLabel("Father Category ID:");
        LabelFontDesign(fatherCategoryIDLabel);
        fatherCategoryIDField = new JTextField();
        fatherCategoryIDLabel.setEnabled(false);
        fatherCategoryIDField.setEnabled(false);

        yesRadioButton.addActionListener(this);
        noRadioButton.addActionListener(this);

        addButton = new JButton("Add");
        addButton.addActionListener(this);

        commandPanel.add(categoryIDLabel);
        commandPanel.add(categoryIDField);
        commandPanel.add(categoryNameLabel);
        commandPanel.add(categoryNameField);
        commandPanel.add(hasFatherCategoryLabel);
        commandPanel.add(yesRadioButton);
        commandPanel.add(new JLabel()); // empty cell for formatting
        commandPanel.add(noRadioButton);
        commandPanel.add(fatherCategoryIDLabel);
        commandPanel.add(fatherCategoryIDField);
        ButtonDesign(addButton);
        commandPanel.add(addButton);

        mainPanel.add(commandPanel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean enabled = yesRadioButton.isSelected();
        if (e.getSource() == yesRadioButton) {
            fatherCategoryIDLabel.setEnabled(enabled);
            fatherCategoryIDField.setEnabled(enabled);
        } else if (e.getSource() == noRadioButton) {
            fatherCategoryIDLabel.setEnabled(enabled);
            fatherCategoryIDField.setEnabled(enabled);
        } else if (e.getSource() == addButton) {
            JFrame frame = new JFrame();
            if(!categoryIDField.getText().isEmpty() && !categoryNameField.getText().isEmpty() && ((enabled && !fatherCategoryIDField.getText().isEmpty()) || (!enabled))){
                int categoryID;
                try {
                    categoryID = Integer.parseInt(categoryIDField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String categoryName = categoryNameField.getText();

                if (yesRadioButton.isSelected()) {
                    int fatherCategoryID;
                    try {
                        fatherCategoryID = Integer.parseInt(fatherCategoryIDField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid father category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        service.addSubCategory(categoryID, categoryName, fatherCategoryID);
                        JOptionPane.showMessageDialog(frame, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "This category ID already exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (noRadioButton.isSelected()) {
                    try {
                        service.addFatherCategory(categoryID, categoryName);
                        JOptionPane.showMessageDialog(frame, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "This category ID already exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an option", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(frame, "Please insert legal values", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }
    }
}
