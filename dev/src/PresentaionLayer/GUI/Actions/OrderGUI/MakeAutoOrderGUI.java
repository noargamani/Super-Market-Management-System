package Presentaion_layer.GUI.Actions.OrderGUI;

import BusinessLayer.Constants;
import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Supplier;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import static Presentaion_layer.GUI.CommandGUI.*;

public class MakeAutoOrderGUI extends JFrame {

    private Suppliers_Service service;

    public MakeAutoOrderGUI(Suppliers_Service service) {
        this.service = service;

        setTitle("Manage Orders");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(inputPanel);

        String title= "Manage Orders";
        FontDesign(title, mainPanel, null);

        JButton addOrderButton = new JButton("Add Order From Supplier");
        ButtonDesign(addOrderButton);
        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddOrderGUI addOrderGUI = new AddOrderGUI(service);
                addOrderGUI.setVisible(true);
            }
        });

        JButton makeAutoOrderButton = new JButton("Add Need Order");
        ButtonDesign(makeAutoOrderButton);
        makeAutoOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.Pair result = make_Auto_Order(service);
                if (!(boolean) result.second)
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, result.first, "Error", JOptionPane.ERROR_MESSAGE);
                else
                JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, result.first);
            }
        });

        JButton makeRoutineOrderButton = new JButton("Add Routine Order");
        ButtonDesign(makeRoutineOrderButton);
        makeRoutineOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    make_Routine_Order(service);
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, "Routine order made successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton removeRoutineOrderButton = new JButton("Remove Routine Order");
        ButtonDesign(removeRoutineOrderButton);
        removeRoutineOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(remove_Routine_Order()) {
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, "Routine order removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, "Routine order does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton updateRoutineOrderButton = new JButton("Update Routine Order");
        ButtonDesign(updateRoutineOrderButton);
        updateRoutineOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(update_Routine_Order()) {
                    JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, "Routine order updated successfully.");
                } else {
                    //JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, "Routine order does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inputPanel.add(addOrderButton);
        inputPanel.add(makeAutoOrderButton);
        inputPanel.add(makeRoutineOrderButton);
        inputPanel.add(removeRoutineOrderButton);
        inputPanel.add(updateRoutineOrderButton);

        mainPanel.add(inputPanel);

        add(mainPanel, BorderLayout.CENTER);
    }



    private Constants.Pair<String, Boolean> make_Auto_Order(Suppliers_Service service) {
        try {
            StringBuilder result = new StringBuilder();
            Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemNames = getItemsToOrder();
            ArrayList<Supplier.OrderForInventory> orders = service.makeAutoOrder(QuantityByItemNames);
            for (Supplier.OrderForInventory order : orders) {
                result.append("\n").append(order.toString());
            }
            return new Constants.Pair(result.toString(), true);
        } catch (Exception e) {
            return new Constants.Pair<>(e.getMessage(), false);
        }
    }

    private void make_Routine_Order(Suppliers_Service service) {
        ArrayList<Contract.Day> days = getDays();
        Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemNames = getItemsToOrder();
        service.makeRoutineOrder(QuantityByItemNames, days);
    }

    private ArrayList<Contract.Day> getDays() {
        ArrayList<Contract.Day> days = new ArrayList<>();
        String[] options = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        int selectedOption = JOptionPane.showOptionDialog(
                MakeAutoOrderGUI.this,
                "Select the days for the routine order:",
                "Routine Order Days",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (selectedOption != JOptionPane.CLOSED_OPTION) {
            days.add(Contract.Day.values()[selectedOption]);
        }
        return days;
    }

    private Hashtable<Constants.Pair<String, String>, Integer> getItemsToOrder() {
        Hashtable<Constants.Pair<String, String>, Integer> QuantityByItem = new Hashtable<>();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        JTextField itemNameField = new JTextField();
        JTextField itemManufactureField = new JTextField();
        JTextField itemQuantityField = new JTextField();
        panel.add(new JLabel("Item Name:"));
        panel.add(itemNameField);
        panel.add(new JLabel("Item Manufacture:"));
        panel.add(itemManufactureField);
        panel.add(new JLabel("Item Quantity:"));
        panel.add(itemQuantityField);

        int result;
        do {
            result = JOptionPane.showConfirmDialog(null, panel, "Add Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String itemName = itemNameField.getText();
                String itemManufacture = itemManufactureField.getText();
                String itemQuantityString = itemQuantityField.getText();

                if (!itemName.isEmpty() && !itemManufacture.isEmpty() && !itemQuantityString.isEmpty()) {
                    try {
                        int itemQuantity = Integer.parseInt(itemQuantityString);
                        QuantityByItem.put(new Constants.Pair<>(itemName, itemManufacture), itemQuantity);
                        if(!service.checkItemExistence(itemName, itemManufacture))
                        {
                            JOptionPane.showMessageDialog(null, "Item does not exist. Please enter a valid item.", "Error", JOptionPane.ERROR_MESSAGE);
                            QuantityByItem.remove(new Constants.Pair<>(itemName, itemManufacture));
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid item quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } while (result == JOptionPane.OK_OPTION);

        return QuantityByItem;
    }

    public boolean remove_Routine_Order() {
        int orderID = -2;
        try {
            orderID = getMyOrderID();
            service.removeRoutineOrder(orderID);
            return true;
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private int getMyOrderID() {
        String orderID;
        orderID = JOptionPane.showInputDialog(MakeAutoOrderGUI.this, "Enter order ID (to stop enter -1):");
        int myOrderID = Integer.parseInt(orderID);
        return myOrderID;
    }

    public boolean update_Routine_Order() {
        try {
            int orderID = getMyOrderID();
            ArrayList<Contract.Day> days = getDays();
            Hashtable<Constants.Pair<String, String>, Integer> QuantityByItemNames = getItemsToOrder();
            service.updateRoutineOrder(orderID, QuantityByItemNames, days);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(MakeAutoOrderGUI.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
