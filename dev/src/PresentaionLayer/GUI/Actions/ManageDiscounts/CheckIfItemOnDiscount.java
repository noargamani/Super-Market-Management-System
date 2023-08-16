package Presentaion_layer.GUI.Actions.ManageDiscounts;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;
import BusinessLayer.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class CheckIfItemOnDiscount extends JFrame implements ActionListener {
    private InventoryService service;
    private JPanel Panel;
    private JTextField itemNameInput;
    private JTextField manufacturerInput;
    private JButton checkButton;
    private JPanel mainPanel;

    public CheckIfItemOnDiscount(InventoryService service){
        this.service = service;
        this.setTitle("Check If Item On Discount");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        checkButton = new JButton("Check");
        checkButton.addActionListener(this);
        ButtonDesign(checkButton);
        Panel.add(checkButton);

        mainPanel.add(Panel);

        this.add(mainPanel);
        this.pack();
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkButton) {

            if (!itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()) {
                String itemName = itemNameInput.getText();
                String itemManufacturer = manufacturerInput.getText();
                int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
                try {
                service.FindMyItem(myCatalogNum);
                CommandGUI.showAlertDialog("Check If Item On Discount Result", service.CheckIfItemOnDiscount(myCatalogNum), JOptionPane.WARNING_MESSAGE);
                } catch (NullPointerException exp){
                    CommandGUI.showAlertDialog("Check If Item On Discount Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                }
            } else
                CommandGUI.showAlertDialog("Check If Item On Discount Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
        }
    }
}
