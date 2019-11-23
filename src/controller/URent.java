package controller;

import database.DBConnectionHandler;
import model.Report;
import ui.DailyRentalReportUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class URent {
    private DBConnectionHandler dbHandler = null;

    public URent() {
        dbHandler = new DBConnectionHandler();
        System.out.println(dbHandler);
    }

    public void returnRental(int rentId) {
        dbHandler.returnRental(rentId);
    }

    public void generateDailyReport(String date) {
        Report report = dbHandler.generateRentalReport(date);
        DailyRentalReportUI dailyRentalReportUI = new DailyRentalReportUI(report);
    }

    public void generateDailyReportSingleBranch(String date, String branch) {
        Report branchReport = dbHandler.generateRentalReportByBranch(branch, date);
        DailyRentalReportUI branchReportUI = new DailyRentalReportUI(branchReport);
    }

    public void generateDailyReturnsReport() {
        Report returnsReport = dbHandler.generateReturnsReport();
        DailyRentalReportUI returnReportUI = new DailyRentalReportUI(returnsReport);
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
    private JPanel panelTwo = new JPanel(new FlowLayout());
    private JPanel panelOne = new JPanel(new FlowLayout());
    private JPanel panelThree = new JPanel(new FlowLayout());
    private JPanel panelFour = new JPanel(new FlowLayout());


    public MainPanel(URent rent) {
        panelOne.setBorder(new EmptyBorder(2,3,2,3));
        panelOne.add(rentalReportAllBranchBtn);
        JTextField dateEntryForAllBranches;
        dateEntryForAllBranches = new JTextField("Date For All Branches");
        dateEntryForAllBranches.setBounds(100,100,300,50); // TODO fix textfield size
        panelOne.add(dateEntryForAllBranches);
        f.add(panelOne, BorderLayout.NORTH);

        rentalReportAllBranchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String date = dateEntryForAllBranches.getText();
                System.out.println(date);
                rent.generateDailyReport(date);
            }
        });

        panelTwo.add(rentalReportOneBranchBtn);
        JTextField dateForOneBranchField, branchField;
        dateForOneBranchField = new JTextField("Date For One Branch");
        branchField = new JTextField("Branch for Single Branch Report");
        panelTwo.add(dateForOneBranchField);
        panelTwo.add(branchField);
        f.add(panelTwo, BorderLayout.SOUTH);


        rentalReportOneBranchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String dateForOneBranch = dateForOneBranchField.getText();
                String branchForSingleReport = branchField.getText();
                rent.generateDailyReportSingleBranch(dateForOneBranch, branchForSingleReport);
            }
        });

        JTextField rentIdField = new JTextField("Rent ID for Return");
        panelThree.add(returnBtn);
        panelThree.add(rentIdField);
        returnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int rentId = Integer.parseInt(rentIdField.getText());
                rent.returnRental(rentId);
            }
        });
        f.add(panelThree, BorderLayout.CENTER);

        panelFour.add(returnReportsBtn);
        returnReportsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                rent.generateDailyReturnsReport();
            }
        });
        f.add(panelFour, BorderLayout.EAST);

        f.setSize(800,500);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}