package Presentaion_layer.GUI.Actions.IssueReports;

import BusinessLayer.Inventory.Classes.Report;
import Presentaion_layer.GUI.CommandGUI;
import Service_layer.InventoryService;
import Service_layer.Suppliers_Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static Presentaion_layer.GUI.CommandGUI.*;

public class IssueReports extends JFrame implements ActionListener {

    private JPanel commandPanel;
    private JButton AllItemsButton;
    private JButton AllMissingItemsButton;
    private JButton DefectiveReportButton;
    private JButton CategoryReportButton;
    private JButton DiscountReportButton;
    private InventoryService inventoryService;
    private JPanel mainPanel;

    public IssueReports(InventoryService service){
         this.setTitle("Issue Reports");
         this.setSize(300, 300);
         this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

         this.inventoryService = service;
         this.mainPanel = new JPanel();

         commandPanel = new JPanel(new GridLayout(0, 1, 10, 10));
         AllItemsButton = new JButton("All items in stock");
         AllMissingItemsButton = new JButton("All missing items");
         DefectiveReportButton = new JButton("Defective report");
         CategoryReportButton = new JButton("Report by category");
         DiscountReportButton = new JButton("Discount report");

         String title = "Issue Reports";
         FontDesign(title, mainPanel, null);

         ButtonDesign(AllItemsButton);
         ButtonDesign(AllMissingItemsButton);
         ButtonDesign(DefectiveReportButton);
         ButtonDesign(CategoryReportButton);
         ButtonDesign(DiscountReportButton);

         BackGroundDesign(mainPanel);
         BackGroundDesign(commandPanel);

         AllItemsButton.addActionListener(this);
         AllMissingItemsButton.addActionListener(this);
         DefectiveReportButton.addActionListener(this);
         CategoryReportButton.addActionListener(this);
         DiscountReportButton.addActionListener(this);

         commandPanel.add(AllItemsButton);
         commandPanel.add(AllMissingItemsButton);
         commandPanel.add(DefectiveReportButton);
         commandPanel.add(CategoryReportButton);
         commandPanel.add(DiscountReportButton);

         mainPanel.add(commandPanel);
         this.add(mainPanel);
         this.pack();
         setVisible(true);
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame frame = new JFrame();
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainPanel.setVisible(false);
        commandPanel.setVisible(false);
        JPanel main = new JPanel();
        JPanel panel = new JPanel(new GridLayout(0, 2,10,10));
        JLabel reportIDLabel = new JLabel("Enter report ID:");
        reportIDLabel.setFont(new Font("Calibri", Font.BOLD,16));
        JLabel categoryLabel = new JLabel("Enter Category ID:");
        categoryLabel.setFont(new Font("Calibri", Font.BOLD,16));
        JTextField reportIDTextField = new JTextField();
        JTextField categoryTextField = new JTextField();
        JButton issueReportButton = new JButton("Issue Report");
        JButton addCategoryButton = new JButton("Add Another Category");
        panel.add(reportIDLabel);
        panel.add(reportIDTextField);
        List<Integer> categoryIDs = new ArrayList<>();

        ButtonDesign(issueReportButton);
        ButtonDesign(addCategoryButton);

        BackGroundDesign(panel);
        BackGroundDesign(main);

        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!categoryTextField.getText().isEmpty() && !reportIDTextField.getText().isEmpty()){
                    int catrgoryID;
                    try {
                        catrgoryID = Integer.parseInt(categoryTextField.getText());
                        categoryIDs.add(catrgoryID);
                        reportIDLabel.setEnabled(false);
                        reportIDTextField.setEnabled(false);
                        categoryTextField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(main, "Invalid Category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                else if (categoryTextField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(main, "Please enter category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (reportIDTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(main, "Please enter report ID first", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        if(e.getSource() == AllItemsButton){
            frame.setTitle("All Items Report");
            issueReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!reportIDTextField.getText().isEmpty()){
                        int reportID;
                        try {
                            reportID = Integer.parseInt(reportIDTextField.getText());

                        }catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Report report= inventoryService.TotalItemReport(reportID);
                            String info = report.getInfo();
                            showAlertDialog("All Items Report", info, JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(main, "Report with this ID already exist, please enter a different report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(main, "Please enter report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    }

                }
            });
        } else if(e.getSource() == AllMissingItemsButton){
            frame.setTitle("All Missing Items Report");
            issueReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!reportIDTextField.getText().isEmpty()){
                        int reportID;
                        try {
                            reportID = Integer.parseInt(reportIDTextField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Report report= inventoryService.MissingItemReport(reportID);
                            String info = report.getInfo();
                            if (info.isEmpty()){
                                showAlertDialog("Missing Items Report", "There are no missing items", JOptionPane.INFORMATION_MESSAGE);
                            }
                            else {
                                showAlertDialog("Missing Items Report", info, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(main, "Report with this ID already exist, please enter a different report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(main, "Please enter report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        } else if (e.getSource() == DefectiveReportButton) {
            frame.setTitle("Defective Report");
            issueReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!reportIDTextField.getText().isEmpty()){
                        int reportID;
                        try {
                            reportID = Integer.parseInt(reportIDTextField.getText());

                        }catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Report report = inventoryService.DefectiveReport(reportID);
                            String info = report.getInfo();
                            showAlertDialog("Defective Report",info,JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(main, "Report with this ID already exist, please enter a different report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(main, "Please enter report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            });
        }else if (e.getSource() == CategoryReportButton) {
            frame.setTitle("Report By Category");
            panel.setLayout(new GridLayout(0, 3,10,10));
            panel.add(new JLabel());
            panel.add(categoryLabel);
            panel.add(categoryTextField);
            panel.add(addCategoryButton);
            issueReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!reportIDTextField.getText().isEmpty() && !categoryIDs.isEmpty()){
                        int categoryID;
                        int reportID;
                        try {
                            categoryID =Integer.parseInt(categoryTextField.getText());
                            categoryIDs.add(categoryID);
                            categoryIDs.clear();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Category ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            reportID =Integer.parseInt(reportIDTextField.getText());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        try {
                            Report report= inventoryService.CategoryReport(reportID, categoryIDs);
                            String info = report.getInfo();
                            if (info == ""){
                                showAlertDialog("Report by category", "One of the categories does not exists", JOptionPane.WARNING_MESSAGE);
                            }
                            else {
                                showAlertDialog("Report by category", info, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(main, "Report with this ID already exist, please enter a different report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (reportIDTextField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(main, "Please enter report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (!categoryIDs.isEmpty()) {
                        JOptionPane.showMessageDialog(main, "Please enter at least one category ID", "Warning", JOptionPane.WARNING_MESSAGE);

                    }
                }

            });

        }else if (e.getSource() == DiscountReportButton) {
            frame.setTitle("Discounts Report");
            issueReportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!reportIDTextField.getText().isEmpty()){
                        int reportID;
                        try {
                            reportID = Integer.parseInt(reportIDTextField.getText());

                        }catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(main, "Invalid Report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        try {
                            Report report= inventoryService.AllDiscountReportGUI(reportID);
                            String info = report.getInfo();
                            if (info.isEmpty()){
                                showAlertDialog("All Discount Report", "There are no discounts", JOptionPane.INFORMATION_MESSAGE);

                            }
                            else {
                                showAlertDialog("All Discount Report", info, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(main, "Report with this ID already exist, please enter a different report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(main, "Please enter report ID", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }

            });
        }
        panel.add(issueReportButton);
        main.add(panel);
        this.add(main);
        this.pack();
    }
}
