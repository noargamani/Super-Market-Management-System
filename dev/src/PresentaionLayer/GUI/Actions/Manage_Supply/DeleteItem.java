package Presentaion_layer.GUI.Actions.Manage_Supply;

import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;
import BusinessLayer.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class DeleteItem extends JFrame implements ActionListener {

    InventoryService service;
    JPanel Panel;
    JTextField itemNameInput;
    JTextField manufacturerInput;
    JButton removeButton;
    private JPanel mainPanel;

    public DeleteItem(InventoryService service){
        this.service = service;
        setTitle("Delete Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel = new JPanel();
        BackGroundDesign(mainPanel);
        Panel = new JPanel(new GridLayout(0, 2, 10, 10));
        BackGroundDesign(Panel);


        JLabel itemName = new JLabel("Insert item name:");
        LabelFontDesign(itemName);
        this.itemNameInput = new JTextField();
        Panel.add(itemName);
        Panel.add(itemNameInput);

        JLabel manufacturer = new JLabel("Insert manufacturer:");
        LabelFontDesign(manufacturer);
        this.manufacturerInput = new JTextField();
        Panel.add(manufacturer);
        Panel.add(manufacturerInput);

        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        Panel.add(removeButton);
        ButtonDesign(removeButton);

        mainPanel.add(Panel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == removeButton) {
            if (!itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()) {
                int myCatalogNum = Item.getItemCatalogNumber(itemNameInput.getText(), manufacturerInput.getText());
                if (service.DeleteItem(myCatalogNum))
                    CommandGUI.showAlertDialog("Delete Item Result", "Item removed successfully", JOptionPane.INFORMATION_MESSAGE);
                else
                    CommandGUI.showAlertDialog("Delete Item Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
            } else
                CommandGUI.showAlertDialog("Delete Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
        }
    }
}
