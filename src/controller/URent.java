package controller;

import database.DBConnectionHandler;

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
        urent.dbHandler.generateReport("2/11/2019");
        urent.dbHandler.generateReportByBranch("6520 Arabella Drive");
    }

    /** generates report **/
//    public void generateReport() {
//       dbHandler.generateReport();
//    }

    /** generates report for branch **/
//    public void generateReportForBranch(string branch) {
//        dbHandler.generateReportForBranch(branch);
//    }


}