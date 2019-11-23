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

    public static void main(String[] args) {
        URent rent = new URent();
        if (rent.dbHandler.login()) {
            /* Test your transactions here */
        } else {
            System.out.println("FAILURE!");
        }
        rent.dbHandler.connect();
        Report report = rent.dbHandler.generateReport("2/11/2019");
        DailyRentalReportUI dailyRentalReportUI = new DailyRentalReportUI(report);
        Report branchReport = rent.dbHandler.generateReportByBranch("6520 Arabella Drive");
        DailyRentalReportUI branchReportUI = new DailyRentalReportUI(branchReport);
    }
}