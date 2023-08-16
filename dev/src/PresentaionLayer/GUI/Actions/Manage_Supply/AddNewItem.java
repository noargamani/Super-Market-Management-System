package Presentaion_layer.GUI.Actions.Manage_Supply;

import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class AddNewItem extends JFrame implements ActionListener {

    InventoryService service;
    JPanel Panel;
    JTextField itemNameInput;
    JTextField manufacturerInput;
    JTextField minimumAmountInput;
    JTextField priceInput;
    JButton addButton;
    private JPanel mainPanel;

    public AddNewItem(InventoryService service){
        this.service = service;

        setTitle("Add New Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        Panel = new JPanel(new GridLayout(0, 2, 10, 10));
        BackGroundDesign(Panel);

        JLabel itemName = new JLabel("Insert item name:");
        this.itemNameInput = new JTextField();
        LabelFontDesign(itemName);
        Panel.add(itemName);
        Panel.add(itemNameInput);

        JLabel manufacturer = new JLabel("Insert manufacturer:");
        this.manufacturerInput = new JTextField();
        LabelFontDesign(manufacturer);
        Panel.add(manufacturer);
        Panel.add(manufacturerInput);

        JLabel minimumAmount = new JLabel("Inset minimum amount of the item:");
        this.minimumAmountInput = new JTextField();
        LabelFontDesign(minimumAmount);
        Panel.add(minimumAmount);
        Panel.add(minimumAmountInput);

        JLabel price = new JLabel("Inset price of the item:");
        this.priceInput = new JTextField();
        LabelFontDesign(price);
        Panel.add(price);
        Panel.add(priceInput);

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
    public void actionPerformed(ActionEvent e){
        if( e.getSource() == addButton) {
            if (!itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()
                    && !minimumAmountInput.getText().isEmpty() && !priceInput.getText().isEmpty()) {

                try {
                    service.AddNewItem(itemNameInput.getText(), manufacturerInput.getText(), Integer.parseInt(minimumAmountInput.getText()), Double.parseDouble(priceInput.getText()));
                } catch (NumberFormatException exp) {
                    CommandGUI.showAlertDialog("Add New Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
                    return;
                } catch (IllegalArgumentException ex) {
                    CommandGUI.showAlertDialog("Add New Item Result", "This Item is already exist in the store", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                CommandGUI.showAlertDialog("Add New Item Result", "Item added successfully", JOptionPane.INFORMATION_MESSAGE);

            } else
                CommandGUI.showAlertDialog("Add New Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
        }
    }
}
