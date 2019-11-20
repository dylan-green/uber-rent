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
        System.out.println("WE ON ONE!");
    }
}