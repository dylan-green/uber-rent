package controller;

import database.DBConnectionHandler;
import model.ReportModel;
import ui.DailyRentalReportUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class URent {
    private DBConnectionHandler dbHandler = null;

    public URent() {
        dbHandler = new DBConnectionHandler();
        System.out.println(dbHandler);
    }

    public void returnRental(int rentId) {
        try {
           String receipt = dbHandler.returnRental(rentId);
           JOptionPane.showMessageDialog(new JFrame(),  receipt, "Receipt for Return", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rentWithReso(int confnum, String cardname, int cardNo, int expDate) {
        try {
            String rentReceipt = dbHandler.rentWithReso(confnum, cardname, cardNo, expDate);
            JOptionPane.showMessageDialog(new JFrame(),  rentReceipt, "Receipt for Rental", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void rentWithoutReso(String cardname,  int cardNo, int expDate, String vtname, int dlnum) {
        try {
            String rentalReceipt = dbHandler.rentWithoutReso(cardname, cardNo, expDate, vtname, dlnum);
            JOptionPane.showMessageDialog(new JFrame(), rentalReceipt, "Receipt for Rental", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generateDailyReport(String date) {
        try {
            ReportModel report = dbHandler.generateRentalReport(date);
            DailyRentalReportUI dailyRentalReportUI = new DailyRentalReportUI(report);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generateDailyReportSingleBranch(String date, String branch) {
        try {
            ReportModel branchReport = dbHandler.generateRentalReportByBranch(branch, date);
            DailyRentalReportUI branchReportUI = new DailyRentalReportUI(branchReport);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generateDailyReturnsReport() {
        try {
            ReportModel returnsReport = dbHandler.generateReturnsReport();
            DailyRentalReportUI returnReportUI = new DailyRentalReportUI(returnsReport);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        URent rent = new URent();
        if (rent.dbHandler.login()) {
            /* Test your transactions here */
            MainPanel mp = new MainPanel(rent);
        } else {
            System.out.println("FAILURE!");
        }
    }
}

class MainPanel {
    private JFrame f = new JFrame("Uber Rent");
    private JButton rentalReportAllBranchBtn = new JButton("Generate Daily Rental Report");
    private JButton rentalReportOneBranchBtn = new JButton("Generate Branch Daily Rental Report");
    private JButton reserveBtn = new JButton("Reserve");
    private JButton viewVehiclesBtn = new JButton("View Available Vehicles");
    private JButton returnReportsBtn = new JButton("Return Report For Today");
    private JButton returnBtn = new JButton("Return");
    private JButton rentWithResoBtn = new JButton("Rent w/ Reservation");
    private JButton rentWithoutResoBtn = new JButton("Rent w/o Reservation");
    private JPanel panelTwo = new JPanel(new FlowLayout());
    private JPanel panelOne = new JPanel(new FlowLayout());
    private JPanel panelThree = new JPanel(new FlowLayout());
    private JPanel panelFour = new JPanel(new FlowLayout());
    private JPanel panelFive = new JPanel(new FlowLayout());
    private JPanel panelSix = new JPanel(new FlowLayout());
    private JPanel panelSeven = new JPanel(new FlowLayout());
    private JPanel panelEight = new JPanel(new FlowLayout());


    public MainPanel(URent rent) {
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new GridLayout(4,4));

        panelOne.add(reserveBtn);
        panelTwo.add(viewVehiclesBtn);

        masterPanel.add(panelOne);
        masterPanel.add(panelTwo);

        JTextField dateEntryForAllBranches;
        dateEntryForAllBranches = new JTextField("Date For All Branches");
        dateEntryForAllBranches.setBounds(100,100,300,50);
        panelThree.add(dateEntryForAllBranches);
        panelThree.add(rentalReportAllBranchBtn);
        masterPanel.add(panelThree);

        rentalReportAllBranchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String date = dateEntryForAllBranches.getText();
                System.out.println(date);
                rent.generateDailyReport(date);
            }
        });

        JTextField dateForOneBranchField, branchField;
        dateForOneBranchField = new JTextField("Date For One Branch");
        branchField = new JTextField("Branch for Single Branch Report");
        panelFour.add(dateForOneBranchField);
        panelFour.add(branchField);
        panelFour.add(rentalReportOneBranchBtn);
        masterPanel.add(panelFour);


        rentalReportOneBranchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String dateForOneBranch = dateForOneBranchField.getText();
                String branchForSingleReport = branchField.getText();
                rent.generateDailyReportSingleBranch(dateForOneBranch, branchForSingleReport);
            }
        });

        JTextField rentIdField = new JTextField("Rent ID for Return");
        panelFive.add(rentIdField);
        returnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int rentId = Integer.parseInt(rentIdField.getText());
                rent.returnRental(rentId);
            }
        });
        panelFive.add(returnBtn);
        masterPanel.add(panelFive);

        panelSix.add(returnReportsBtn);
        returnReportsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                rent.generateDailyReturnsReport();
            }
        });
        masterPanel.add(panelSix);

        JTextField confNumField, cardNameField, cardNoField, expDateField;
        confNumField = new JTextField("Confirmation Number");
        cardNoField = new JTextField("Credit Card Number (No Spaces)");
        cardNameField = new JTextField("Card name (e.g. MasterCard/Visa)");
        expDateField = new JTextField("Expiry Date (MMYY)");
        panelSeven.add(confNumField);
        panelSeven.add(cardNoField);
        panelSeven.add(cardNameField);
        panelSeven.add(expDateField);
        panelSeven.add(rentWithResoBtn);

        rentWithResoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confNum = Integer.parseInt(confNumField.getText());
                String cardName = cardNameField.getText();
                int cardNo = Integer.parseInt(cardNoField.getText());
                int expDate = Integer.parseInt(expDateField.getText());
                rent.rentWithReso(confNum, cardName, cardNo, expDate);
            }
        });
        masterPanel.add(panelSeven);



        JTextField ccNameField, ccNoField, ccExpiryField, typeField, licenseField;
        licenseField = new JTextField("Driver's License #");
        ccNoField = new JTextField("Credit Card Number (No Spaces)");
        ccNameField = new JTextField("Card name (e.g. MasterCard/Visa)");
        ccExpiryField = new JTextField("Expiry Date (MMYY)");
        typeField = new JTextField("Vehicle Type");
        panelEight.add(licenseField);
        panelEight.add(ccNoField);
        panelEight.add(ccNameField);
        panelEight.add(ccExpiryField);
        panelEight.add(typeField);
        panelEight.add(rentWithoutResoBtn);

        rentWithoutResoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dlnum = Integer.parseInt(licenseField.getText());
                String cardName = ccNameField.getText();
                int cardNo = Integer.parseInt(ccNoField.getText());
                int expDate = Integer.parseInt(ccExpiryField.getText());
                String vtname = typeField.getText();
                rent.rentWithoutReso(cardName, cardNo, expDate, vtname, dlnum);
            }
        });
        masterPanel.add(panelEight);

        f.add(masterPanel);
        f.setSize(800,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}