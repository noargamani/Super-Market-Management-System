package Presentaion_layer.GUI.Actions.SupplierGUI;

import BusinessLayer.Suppliers.Classes.Contract;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;

import static Presentaion_layer.GUI.CommandGUI.*;

public class MakeNewContractGUI extends JFrame {
    private Suppliers_Service service;
    private JTextField supplierNameField;
    private JCheckBox shipmentCheckBox;
    private JComboBox<String> daysComboBox;
    private JTextField shipDaysField;
    private JTextField dateSignField;
    private DefaultListModel<String> selectedDaysListModel;
    private JList<String> selectedDaysList;

    public MakeNewContractGUI(Suppliers_Service service) {
        this.service = service;

        setTitle("Make New Contract");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300,300);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(0, 3, 0, 10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(inputPanel);

        JLabel supplierNameLabel = new JLabel("Supplier Name:");
        LabelFontDesign(supplierNameLabel);
        supplierNameField = new JTextField();
        supplierNameField.setPreferredSize(new Dimension(20, 10));
        inputPanel.add(supplierNameLabel);
        inputPanel.add(supplierNameField);
        inputPanel.add(new JLabel());

        JCheckBox shipmentCheckBox = new JCheckBox();
        shipmentCheckBox.setText("Does Shipment?");
        shipmentCheckBox.setFont(new Font("Calibri", Font.BOLD,16));
        shipmentCheckBox.setBackground(new Color(167, 196, 188));
        inputPanel.add(shipmentCheckBox);
        inputPanel.add(new JLabel());
        inputPanel.add(new JLabel());


        JLabel daysLabel = new JLabel("Select Day:");
        LabelFontDesign(daysLabel);

        String[] allDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        daysComboBox = new JComboBox<>(allDays);
        daysComboBox.setEnabled(false); // Initially disabled

        shipmentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isShipmentSelected = shipmentCheckBox.isSelected();
                daysComboBox.setEnabled(isShipmentSelected);
            }
        });

        JButton addButton = new JButton("Add");
        ButtonDesign(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDay = (String) daysComboBox.getSelectedItem();
                if (!selectedDaysListModel.contains(selectedDay)) {
                    selectedDaysListModel.addElement(selectedDay);
                    daysComboBox.removeItem(selectedDay);
                }
            }
        });

        JLabel selectedDaysLabel = new JLabel("Selected Days:");
        LabelFontDesign(selectedDaysLabel);

        selectedDaysListModel = new DefaultListModel<>();
        selectedDaysList = new JList<>(selectedDaysListModel);

        inputPanel.add(daysLabel);
        inputPanel.add(daysComboBox);
        inputPanel.add(addButton);

        inputPanel.add(selectedDaysLabel);
        inputPanel.add(new JScrollPane(selectedDaysList));
        inputPanel.add(new JLabel());

        JButton saveButton = new JButton("Save");
        ButtonDesign(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveContract();
            }
        });

        inputPanel.add(saveButton);

        mainPanel.add(inputPanel);

        add(mainPanel);
    }

    private void saveContract() {
        String supplierName = supplierNameField.getText();
        boolean doesShipment = shipmentCheckBox.isSelected();
        String shipDaysText = shipDaysField.getText();
        String dateSignText = dateSignField.getText();

        try {
            ArrayList<Contract.Day> shipDays = parseShipDays(shipDaysText);
            Date dateSign = parseDateSign(dateSignText);

            Contract contract = new Contract(doesShipment, shipDays, dateSign);
            service.UpdateContract(supplierName, contract);
            JOptionPane.showMessageDialog(this, "Contract saved!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Contract.Day> parseShipDays(String shipDaysText) {
        ArrayList<Contract.Day> shipDays = new ArrayList<>();
        shipDaysText = shipDaysText.replaceAll("\\s+", "");
        String[] shipDaysTextArr = shipDaysText.split(",");
        for (String shipDayText : shipDaysTextArr) {
            Contract.Day shipDay = Contract.Day.getDay(Integer.parseInt(shipDayText));
            shipDays.add(shipDay);
        }
        return shipDays;
    }

    private Date parseDateSign(String dateSignText) {
        Date dateSign = new Date();
        dateSignText = dateSignText.replaceAll("\\s+", "");
        String[] dateSignTextArr = dateSignText.split("/");
        if (dateSignTextArr.length != 3) {
            throw new IllegalArgumentException("Date sign must be in format: dd/mm/yyyy");
        }
        int day = Integer.parseInt(dateSignTextArr[0]);
        int month = Integer.parseInt(dateSignTextArr[1]);
        int year = Integer.parseInt(dateSignTextArr[2]);
        dateSign.setDate(day);
        dateSign.setMonth(month);
        dateSign.setYear(year);
        return dateSign;
    }
}
