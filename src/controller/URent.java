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

    public void returnRental(int rentId) {
        dbHandler.returnRental(rentId);
    }

    public void generateDailyReport() {
        Report report = dbHandler.generateReport("2/11/2019");
        DailyRentalReportUI dailyRentalReportUI = new DailyRentalReportUI(report);
    }

    public void generateDailyReportSingleBranch() {
        Report branchReport = dbHandler.generateReportByBranch("6520 Arabella Drive");
        DailyRentalReportUI branchReportUI = new DailyRentalReportUI(branchReport);
    }

    public static void main(String[] args) {
        URent rent = new URent();
        if (rent.dbHandler.login()) {
            /* Test your transactions here */
            rent.generateDailyReport();
            rent.generateDailyReportSingleBranch();
        } else {
            System.out.println("FAILURE!");
        }
    }
}