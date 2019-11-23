package controller;

import database.DBConnectionHandler;
import model.Report;
import ui.DailyRentalReportUI;

public class URent {
    private DBConnectionHandler dbHandler = null;

    public URent() {
        dbHandler = new DBConnectionHandler();
        System.out.println(dbHandler);
    }

    /**
     * Here we add transactions to URent and 
     * their corresponding database actions
     * --------------------------------------
     * 
     * public void insertBranch(BranchModel model) {
     *      dbHandler.insertBranch(model);
     * }
     */

    public static void main(String[] args) {
        URent urent = new URent();
        urent.dbHandler.connect();
        Report report = urent.dbHandler.generateReport("2/11/2019");
        DailyRentalReportUI dailyRentalReportUI = new DailyRentalReportUI(report);
        Report branchReport = urent.dbHandler.generateReportByBranch("6520 Arabella Drive");
        DailyRentalReportUI branchReportUI = new DailyRentalReportUI(branchReport);
    }
}