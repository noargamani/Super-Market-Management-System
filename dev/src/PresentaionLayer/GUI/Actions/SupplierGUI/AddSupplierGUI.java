package Presentaion_layer.GUI.Actions.SupplierGUI;

import BusinessLayer.Suppliers.Classes.Contract;
import BusinessLayer.Suppliers.Classes.Contract.Day;
import BusinessLayer.Suppliers.Classes.Supplier;
import BusinessLayer.Suppliers.Classes.Supplier_Card;
import DataAcessLayer.DAO.Suppliers.SupplierDAO;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static BusinessLayer.Constants.PhoneNumregex;
import static Presentaion_layer.GUI.CommandGUI.*;
import static Presentaion_layer.GUI.CommandGUI.BackGroundDesign;

public class AddSupplierGUI extends JFrame {

    private Suppliers_Service service;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField manufactureField;
    private DefaultListModel<String> manufactureListModel;
    private JTextField contactNameField;
    private JTextField contactPhoneField;
    private DefaultListModel<String> contactListModel;
    private JTextField discountTypeField;
    private JTextField discountValueField;
    private DefaultListModel<String> discountListModel;
    private JTextField bnNumberField;
    private JTextField bankAccountField;
    private JTextField payConditionField;
    private JTextField isShipField;
    private JTextField shipmentDaysField;
    private JTextField signDateField;
    private JTextField discountPercentageField;

    public AddSupplierGUI(Suppliers_Service service) {
        this.service = service;
        initializeComponents();
        pack();
    }

    private void initializeComponents() {
        setTitle("Add Supplier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 300);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 2,5,10));

        BackGroundDesign(mainPanel);
        BackGroundDesign(formPanel);

        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

// Supplier Name
        JLabel SupplierName = new JLabel("Supplier Name:");
        LabelFontDesign(SupplierName);
        formPanel.add(SupplierName);
        nameField = new JTextField();
        nameField.setToolTipText("Enter the name of the supplier");
        formPanel.add(nameField);

// Supplier Address
        JLabel SupplierAddress = new JLabel("Supplier Address:");
        LabelFontDesign(SupplierAddress);
        formPanel.add(SupplierAddress);
        addressField = new JTextField();
        addressField.setToolTipText("Enter the address of the supplier");
        formPanel.add(addressField);

// Manufactures
        JLabel Manufacture = new JLabel("Manufacture:");
        LabelFontDesign(Manufacture);
        formPanel.add(Manufacture);
        manufactureField = new JTextField();
        manufactureField.setToolTipText("Enter a manufacture");
        JButton addManufactureButton = new JButton("Add");
        ButtonDesign(addManufactureButton);

        addManufactureButton.setToolTipText("Click to add the manufacture");
        formPanel.add(manufactureField);
        formPanel.add(addManufactureButton);

        addManufactureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addManufacture();
            }
        });
        formPanel.add(manufactureField);
        formPanel.add(addManufactureButton);

        manufactureListModel = new DefaultListModel<>();
        JList<String> manufactureList = new JList<>(manufactureListModel);
        JScrollPane manufactureScrollPane = new JScrollPane(manufactureList);
        formPanel.add(manufactureScrollPane);

        // Contact List
        JLabel ContactName = new JLabel("Contact Name:");
        LabelFontDesign(ContactName);
        formPanel.add(ContactName);
        contactNameField = new JTextField();
        formPanel.add(contactNameField);

        JLabel ContactPhone = new JLabel("Contact Phone:");
        LabelFontDesign(ContactPhone);
        formPanel.add(ContactPhone);
        contactPhoneField = new JTextField();
        formPanel.add(contactPhoneField);

        JButton addContactButton = new JButton("Add");
        ButtonDesign(addContactButton);

        addContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });
        formPanel.add(addContactButton);

        contactListModel = new DefaultListModel<>();
        JList<String> contactList = new JList<>(contactListModel);
        JScrollPane contactScrollPane = new JScrollPane(contactList);
        formPanel.add(contactScrollPane);

        JLabel DiscountType = new JLabel("Discount Type:");
        LabelFontDesign(DiscountType);
        formPanel.add(DiscountType);
        discountTypeField = new JTextField();
        formPanel.add(discountTypeField);

        JLabel DiscountValue = new JLabel("Discount Value:");
        LabelFontDesign(DiscountValue);
        formPanel.add(DiscountValue);
        discountValueField = new JTextField();
        formPanel.add(discountValueField);

        JLabel DiscountPercentage = new JLabel("Discount Percentage:");
        LabelFontDesign(DiscountPercentage);
        formPanel.add(DiscountPercentage);
        discountPercentageField = new JTextField();
        formPanel.add(discountPercentageField);

        JButton addDiscountButton = new JButton("Add");
        ButtonDesign(addDiscountButton);

        addDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDiscount();
            }
        });
        formPanel.add(addDiscountButton);

        discountListModel = new DefaultListModel<>();
        JList<String> discountList = new JList<>(discountListModel);
        JScrollPane discountScrollPane = new JScrollPane(discountList);
        formPanel.add(discountScrollPane);

        // Supplier Card
        JLabel BNNumber = new JLabel("BN Number:");
        LabelFontDesign(BNNumber);
        formPanel.add(BNNumber);
        bnNumberField = new JTextField();
        formPanel.add(bnNumberField);

        JLabel BankAccount = new JLabel("Bank Account:");
        LabelFontDesign(BankAccount);
        formPanel.add(BankAccount);
        bankAccountField = new JTextField();
        formPanel.add(bankAccountField);

        JLabel PaymentCondition = new JLabel("Payment Condition:");
        LabelFontDesign(PaymentCondition);
        formPanel.add(PaymentCondition);
        payConditionField = new JTextField();
        formPanel.add(payConditionField);

        // Contract
        JLabel IsShip = new JLabel("Is Ship:");
        LabelFontDesign(IsShip);
        formPanel.add(IsShip);
        isShipField = new JTextField();
        formPanel.add(isShipField);

        JLabel ShipmentDays = new JLabel("Shipment Days (comma-separated):");
        LabelFontDesign(ShipmentDays);
        formPanel.add(ShipmentDays);
        shipmentDaysField = new JTextField();
        formPanel.add(shipmentDaysField);

        JLabel SignDate = new JLabel("Sign Date:");
        LabelFontDesign(SignDate);
        formPanel.add(SignDate);
        signDateField = new JTextField();
        formPanel.add(signDateField);

        // Buttons
        JButton addButton = new JButton("Add Supplier");
        ButtonDesign(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        ButtonDesign(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();

        BackGroundDesign(buttonPanel);

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addManufacture() {
        String manufacture = manufactureField.getText().trim();
        if (!manufacture.isEmpty()) {
            manufactureListModel.addElement(manufacture);
            manufactureField.setText("");
        }
    }

    private void addContact() {
        String contactName = contactNameField.getText().trim();
        String contactPhone = contactPhoneField.getText().trim();
        if (!contactName.isEmpty() && !contactPhone.isEmpty()) {
            if(!contactPhone.matches(PhoneNumregex))
            {
                JOptionPane.showMessageDialog(this, "Invalid phone number. Doesn't match phone format.", "Error", JOptionPane.ERROR_MESSAGE);
                contactPhoneField.setText("");
                return;
            }
            String contact = contactName + " (" + contactPhone + ")";
            contactListModel.addElement(contact);
            contactNameField.setText("");
            contactPhoneField.setText("");
        }
    }

    private void addDiscount() {
        String discountType = discountTypeField.getText().trim();
        String discountValue = discountValueField.getText().trim();
        String discountPercentage = discountPercentageField.getText().trim();

        if (!discountType.isEmpty() && !discountValue.isEmpty() && !discountPercentage.isEmpty()) {
            // Validate discount type
            discountType = discountType.toLowerCase();
            if (!discountType.equals("quantity") && !discountType.equals("price")) {
                JOptionPane.showMessageDialog(this, "Invalid discount type. Only 'Quantity' and 'Price' are allowed.");
                return;
            }

            // Validate discount value
            try {
                double value = Double.parseDouble(discountValue);
                if (value <= 0) {
                    JOptionPane.showMessageDialog(this, "Invalid discount value. Please enter a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid discount value. Please enter a valid number.");
                return;
            }

            // Validate discount percentage
            try {
                double percentage = Double.parseDouble(discountPercentage);
                if (percentage < 0 || percentage > 100) {
                    JOptionPane.showMessageDialog(this, "Invalid discount percentage. Please enter a value between 0 and 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid discount percentage. Please enter a valid number.");
                return;
            }

            String discount = discountType + ": " + discountValue + " (Percentage: " + discountPercentage + "%)";
            discountListModel.addElement(discount);
            discountTypeField.setText("");
            discountValueField.setText("");
            discountPercentageField.setText("");
        }
    }



    private void addSupplier() {
        try {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            ArrayList<String> manufactures = new ArrayList<>();
            for (int i = 0; i < manufactureListModel.getSize(); i++) {
                manufactures.add(manufactureListModel.getElementAt(i));
            }

            SortedMap<String, String> contactList = new TreeMap<>();
            for (int i = 0; i < contactListModel.getSize(); i++) {
                String contact = contactListModel.getElementAt(i);
                String[] contactSplit = contact.split("\\(");
                if (contactSplit.length == 2) {
                    String contactName = contactSplit[0].trim();
                    String contactPhone = contactSplit[1].replace(")", "").trim();
                    contactList.put(contactName, contactPhone);
                }
            }

            SortedMap<Double, Integer> discountByTotalPrice = new TreeMap<>();
            SortedMap<Integer, Integer> discountByTotalQuantity = new TreeMap<>();
            for (int i = 0; i < discountListModel.getSize(); i++) {
                String discount = discountListModel.getElementAt(i);
                String[] discountSplit = discount.split(":");
                if (discountSplit.length == 3) {
                    String discountType = discountSplit[0].trim();
                    String discountValue = discountSplit[1].split("\\(")[0].trim();
                    String discountPercentage = discountSplit[2].replace("%)", "").trim();
                    System.out.println("DISCOUNT:" + discountType + " " + discountValue + " " + discountPercentage);
                    try {
                        int percentage = Integer.parseInt(discountPercentage);
                        double value = Double.parseDouble(discountValue);
                        if (discountType.equalsIgnoreCase("Price")) {
                            discountByTotalPrice.put(value, percentage);
                        } else if (discountType.equalsIgnoreCase("Quantity")) {
                            int quantity = (int) value;
                            discountByTotalQuantity.put(quantity, percentage);
                        }
                    } catch (NumberFormatException e) {
                        // Ignore invalid discount values
                        throw new RuntimeException("Invalid discount value: " + discountValue);
                    }
                }
            }

            int bnNumber = 0;
            try {
                bnNumber = Integer.parseInt(bnNumberField.getText().trim());
            } catch (NumberFormatException e) {
                // Ignore invalid BN number
                if (!bnNumberField.getText().trim().isEmpty()) {
                    throw new RuntimeException("Invalid BN number: " + bnNumberField.getText().trim());
                }
            }

            String bankAccount = bankAccountField.getText().trim();
            String paymentCondition = payConditionField.getText().trim();

            boolean isShip = Boolean.parseBoolean(isShipField.getText().trim());

            List<Contract.Day> shipmentDays = new ArrayList<>();
            String shipmentDaysText = shipmentDaysField.getText().trim();
            if (!shipmentDaysText.isEmpty()) {
                String[] daysSplit = shipmentDaysText.split(",");
                for (String dayString : daysSplit) {
                    String dayTrimmed = dayString.trim();
                    if (!dayTrimmed.isEmpty()) {
                        Contract.Day day = Contract.Day.valueOf(dayTrimmed);
                        shipmentDays.add(day);
                    }
                }
            }

            System.out.println("discountByTotalPrice: " + discountByTotalPrice);
            System.out.println("discountByTotalQuantity: " + discountByTotalQuantity);

            String signDate = signDateField.getText().trim();

            Date date = new Date(signDate);
            Contract contract = new Contract(isShip, new ArrayList<>(shipmentDays), date);
            Supplier_Card card = new Supplier_Card(bnNumber, bankAccount, paymentCondition);


            // Add supplier to service
            service.addSupplier(name, address, manufactures, contactList, discountByTotalPrice,
                    discountByTotalQuantity, card, contract);

            // Show success message
            JOptionPane.showMessageDialog(this, "Supplier added successfully.");

            // Clear fields
            clearFields();
        } catch (Exception e) {
            // Show error message in message screen
            JOptionPane.showMessageDialog(this,"invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        addressField.setText("");
        manufactureListModel.clear();
        contactListModel.clear();
        discountListModel.clear();
        bnNumberField.setText("");
        bankAccountField.setText("");
        payConditionField.setText("");
        isShipField.setText("");
        shipmentDaysField.setText("");
        signDateField.setText("");
    }

}

