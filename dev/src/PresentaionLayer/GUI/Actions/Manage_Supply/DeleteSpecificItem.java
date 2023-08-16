package Presentaion_layer.GUI.Actions.Manage_Supply;

import Presentaion_layer.CLI.Actions.Order.Make_Auto_Order;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;
import BusinessLayer.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Presentaion_layer.GUI.CommandGUI.*;

public class DeleteSpecificItem extends JFrame implements ActionListener {

    InventoryService service;
    Suppliers_Service suppliersService;
    JPanel Panel;
    JTextField itemNameInput;
    JTextField manufacturerInput;
    JTextField idInput;
    JButton removeButton;
    private JPanel mainPanel;

    public DeleteSpecificItem(InventoryService service, Suppliers_Service suppliersService){
        this.service = service;
        this.suppliersService = suppliersService;
        setTitle("Delete Specific Item");
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

        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        ButtonDesign(removeButton);
        Panel.add(removeButton);

        mainPanel.add(Panel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if( e.getSource() == removeButton) {
            if( !itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()) {
                int myCatalogNum = Item.getItemCatalogNumber(itemNameInput.getText(), manufacturerInput.getText());

                try {
                    service.FindSpecificItem(myCatalogNum, Integer.parseInt(idInput.getText()));
                    CommandGUI.showAlertDialog("Delete Specific Item Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException exp4) {
                    CommandGUI.showAlertDialog("Delete Specific Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
                } catch (IllegalArgumentException exp1) {
                    try {
                        if (service.DeleteSpecificItem(Integer.parseInt(idInput.getText()), myCatalogNum))
                            CommandGUI.showAlertDialog("Delete Specific Item Result", "Item removed successfully", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Item.NeedOrderException exp2) {
                        try {
                            Make_Auto_Order.makeNeedOrder(suppliersService, exp2);
                            CommandGUI.showAlertDialog("Delete Specific Item Result", "The amount of the item is below the minimum amount defined for it", JOptionPane.WARNING_MESSAGE);
                        }
                        catch (Exception error){
                            CommandGUI.showAlertDialog("Delete Specific Item Result", "Cannot make a make need order because there is no supplier that supplies this item", JOptionPane.WARNING_MESSAGE);
                        }

                    }
                } catch (NullPointerException exp3){
                    CommandGUI.showAlertDialog("Delete Specific Item Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                }
            } else
                CommandGUI.showAlertDialog("Delete Specific Item Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);

        }
    }
}
