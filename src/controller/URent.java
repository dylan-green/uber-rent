package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import database.DBConnectionHandler;
import model.ReportModel;
import ui.DailyRentalReportUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Customer;
import model.Reservation;
import java.sql.SQLException;

public class URent {
    private DBConnectionHandler dbHandler = null;

    public URent() {
        dbHandler = new DBConnectionHandler();
        System.out.println(dbHandler);
    }

    public void makeReservation(String res_vtname, int dlnum, String res_to_date, String res_from_date, 
    String cust_name, String cust_addr, String cust_city, int cellphone) {
        try {
           String receipt = dbHandler.makeReservationTransaction(res_vtname, dlnum, res_to_date, res_from_date, cust_name, cust_addr, cust_city, cellphone);
           JOptionPane.showMessageDialog(new JFrame(),  receipt, "Reservation Confirmation", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error! Sorry something went wrong! \n" + e.toString(),
                    "Oops!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void viewVehicles(String vehicleType, String location, String toDate, String fromDate) {
        try {
            String results = dbHandler.availableVehicleTransaction(vehicleType, location, toDate, fromDate);
            JOptionPane.showMessageDialog(new JFrame(),  results, "Available Vehicles", JOptionPane.INFORMATION_MESSAGE);
         } catch (SQLException e) {
             JOptionPane.showMessageDialog(new JFrame(),
                     "Error! Sorry something went wrong! \n" + e.toString(),
                     "Oops!",
                     JOptionPane.ERROR_MESSAGE);
         }
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
    private JPanel panelTwo = new JPanel(new FlowLayout());
    private JPanel panelOne = new JPanel(new FlowLayout());
    private JPanel panelThree = new JPanel(new FlowLayout());
    private JPanel panelFour = new JPanel(new FlowLayout());
    private JPanel panelFive = new JPanel(new FlowLayout());
    private JPanel panelSix = new JPanel(new FlowLayout());


    public MainPanel(URent rent) {
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new GridLayout(3,3));

        JTextField vehicle_type2, location2, toDate2, fromDate2;
        vehicle_type2 = new JTextField("Vehicle type");
        location2 = new JTextField("Location");
        toDate2 = new JTextField("To Date");
        fromDate2 = new JTextField("From Date");
        panelTwo.add(vehicle_type2);
        panelTwo.add(location2);
        panelTwo.add(toDate2);
        panelTwo.add(fromDate2);


        // set up reservation inputs
        JTextField vehicle_type, dlnumber, to_date, from_date, cust_name, cust_addr, cust_city, cellphone;
        vehicle_type = new JTextField("Vehicle type");
        dlnumber = new JTextField("Drivers license number");
        to_date = new JTextField("To date");
        from_date = new JTextField("From date");
        cust_name = new JTextField("Customer name");
        cust_addr = new JTextField("Customer address");
        cust_city = new JTextField("Customer city");
        cellphone = new JTextField("Cellphone");
        panelOne.add(vehicle_type);
        panelOne.add(dlnumber);
        panelOne.add(to_date);
        panelOne.add(from_date);
        panelOne.add(cust_name);
        panelOne.add(cust_addr);
        panelOne.add(cust_city);
        panelOne.add(cellphone);

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

        reserveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String vehicleType = vehicle_type.getText();
                int dlnum = Integer.parseInt(dlnumber.getText());
                String toDate = to_date.getText();
                String fromDate = from_date.getText();
                String custName = cust_name.getText();
                String custAddr = cust_addr.getText();
                String custCity = cust_city.getText();
                int cust_cellphone = Integer.parseInt(cellphone.getText());

                rent.makeReservation(vehicleType, dlnum, toDate, fromDate, custName, custAddr, custCity, cust_cellphone);
            }
        });

        viewVehiclesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String vehicleType;
                String location;
                String toDate;
                String fromDate;
                vehicleType = vehicle_type2.getText();
                location = location2.getText();
                toDate = toDate2.getText();
                fromDate = fromDate2.getText();
                rent.viewVehicles(vehicleType, location, toDate, fromDate);
            }
        });

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

        f.add(masterPanel);
        f.setSize(800,300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}