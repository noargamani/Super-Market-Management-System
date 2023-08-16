package Presentaion_layer.GUI.Actions.Manage_Supply;

import BusinessLayer.Item;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddNewSpecificItem extends JFrame implements ActionListener {

    InventoryService service;
    JPanel Panel;
    JTextField itemNameInput;
    JTextField manufacturerInput;
    JTextField idInput;
    JTextField costInput;
    JTextField sellingInput;
    JTextField expirationInput;
    JCheckBox defective;
    JTextField defectTypeInput;
    JComboBox locationInput;
    JButton addButton;
    private JPanel mainPanel;

    public AddNewSpecificItem(InventoryService service){
        this.service = service;
        setTitle("Add New Specific Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        Panel = new JPanel(new GridLayout(0, 2, 10, 10));
        BackGroundDesign(Panel);

        JLabel itemName = new JLabel("Insert the name of the item:");
        LabelFontDesign(itemName);
        this.itemNameInput = new JTextField();
        Panel.add(itemName);
        Panel.add(itemNameInput);

        JLabel manufacturer = new JLabel("Insert the manufacturer of the item:");
        LabelFontDesign(manufacturer);
        this.manufacturerInput = new JTextField();
        Panel.add(manufacturer);
        Panel.add(manufacturerInput);

        JLabel id = new JLabel("Insert the ID of the item:");
        LabelFontDesign(id);
        this.idInput = new JTextField();
        Panel.add(id);
        Panel.add(idInput);

        JLabel cost = new JLabel("Insert item's cost price:");
        LabelFontDesign(cost);
        this.costInput = new JTextField();
        Panel.add(cost);
        Panel.add(costInput);

        JLabel selling = new JLabel("Insert item's selling price:");
        LabelFontDesign(selling);
        this.sellingInput = new JTextField();
        Panel.add(selling);
        Panel.add(sellingInput);

        JLabel expiration = new JLabel("Insert expiration date: dd/mm/yyyy");
        LabelFontDesign(expiration);
        this.expirationInput = new JTextField();
        Panel.add(expiration);
        Panel.add(expirationInput);

        defective = new JCheckBox();
        defective.setText("Is the item defective?");
        defective.setFont(new Font("Calibri", Font.BOLD,16));
        defective.setBackground(new Color(167, 196, 188));
        Panel.add(defective);

        JLabel nothing = new JLabel("");
        Panel.add(nothing);

        JLabel defectTypeLabel = new JLabel("Insert the defect type:");
        LabelFontDesign(defectTypeLabel);
        this.defectTypeInput = new JTextField();
        defectTypeLabel.setEnabled(false);
        defectTypeInput.setEnabled(false);
        Panel.add(defectTypeLabel);
        Panel.add(defectTypeInput);

        defective.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // Trigger an ActionEvent manually when selection state changes
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    defectTypeLabel.setEnabled(true);
                    defectTypeInput.setEnabled(true);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    defectTypeLabel.setEnabled(false);
                    defectTypeInput.setEnabled(false);
                }
            }
        });

        JLabel location = new JLabel("Insert the location of the item:");
        LabelFontDesign(location);

        String[] options = {"Shelf","Warehouse"};
        this.locationInput = new JComboBox(options);
        locationInput.setFont(new Font("Calibri", Font.BOLD,16));
        Panel.add(location);
        Panel.add(locationInput);

        addButton = new JButton("Add");
        addButton.addActionListener(this);
        ButtonDesign(addButton);
        Panel.add(addButton);

        mainPanel.add(Panel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == addButton) {
            if( !itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()){
                try {
                    int myCatalogNum = Item.getItemCatalogNumber(itemNameInput.getText(), manufacturerInput.getText());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate expirationDate = LocalDate.parse(expirationInput.getText(), formatter);
                    service.FindSpecificItem(myCatalogNum, Integer.parseInt(idInput.getText()));

                    if (defective.isSelected() && defectTypeInput.getText().isEmpty()) {
                        CommandGUI.showAlertDialog("Add New Specific Item Result", "Please insert defect type", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (service.AddSpecificItem(myCatalogNum, Integer.parseInt(idInput.getText()), Double.parseDouble(costInput.getText()), Double.parseDouble(sellingInput.getText()), expirationDate, defective.isSelected(), defectTypeInput.getText(), (String) locationInput.getSelectedItem()))
                        CommandGUI.showAlertDialog("Add New Specific Item Result", "Item added successfully", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException | DateTimeParseException exp1) {
                    CommandGUI.showAlertDialog("Add New Specific Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
                } catch (Exception exp3) {
                    CommandGUI.showAlertDialog("Add New Specific Item Result", exp3.getMessage(), JOptionPane.WARNING_MESSAGE);
                }
            } else {
                CommandGUI.showAlertDialog("Add New Specific Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
