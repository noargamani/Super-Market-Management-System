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

public class UpdateDefectiveStatus extends JFrame implements ActionListener {

    InventoryService service;
    JPanel Panel;
    JTextField itemNameInput;
    JTextField manufacturerInput;
    JTextField idInput;
    JButton updateButton;
    private JPanel mainPanel;

    public UpdateDefectiveStatus(InventoryService service){
        this.service = service;
        setTitle("Update Defective Status");
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

        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        Panel.add(updateButton);
        ButtonDesign(updateButton);
        Panel.add(updateButton);

        mainPanel.add(Panel);
        add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateButton) {
            if( !itemNameInput.getText().isEmpty() && !manufacturerInput.getText().isEmpty()) {
                String itemName = itemNameInput.getText();
                String itemManufacturer = manufacturerInput.getText();
                int myCatalogNum = Item.getItemCatalogNumber(itemName, itemManufacturer);
                int itemID=0;
                try {
                    itemID = Integer.parseInt(idInput.getText());
                    service.FindSpecificItem(myCatalogNum, Integer.parseInt(idInput.getText()));
                    CommandGUI.showAlertDialog("Update Defective Status Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException exp4) {
                    CommandGUI.showAlertDialog("Update Defective Status Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
                }catch (IllegalArgumentException exp) {
                    boolean defective = false;
                    String defectType = "None";

                    JFrame frame = new JFrame();
                    int option = JOptionPane.showConfirmDialog(frame, "Is the item defective?", "Defective Status", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        defectType = JOptionPane.showInputDialog(frame, "Insert the defect type:");
                        defective = true;
                        if (defectType.isEmpty()) {
                            CommandGUI.showAlertDialog("Add New Specific Item Result", "Please insert defect type", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else if (option == JOptionPane.NO_OPTION) {
                        defectType = "None";
                    }

                    try {
                        service.UpdateDefectiveStatus(myCatalogNum, itemID, defective, defectType);
                        CommandGUI.showAlertDialog("Update Defective Status Result", "Defective status updated successfully", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        CommandGUI.showAlertDialog("Update Defective Status Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                    }
                }catch (NullPointerException exp3){
                    CommandGUI.showAlertDialog("Update Defective Status Result", "This Item isn't exist in the store", JOptionPane.WARNING_MESSAGE);
                }
            } else
                CommandGUI.showAlertDialog("Update Defective Status Result", "Please insert legal values", JOptionPane.WARNING_MESSAGE);
        }
    }
}

