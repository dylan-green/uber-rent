package controller;

import database.DBConnectionHandler;

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
            // rent.dbHandler.rentWithReservation(333444, "Dylan Green", 123459, 0721);
            // rent.dbHandler.returnRental(6055231);
        } else {
            System.out.println("FAILURE!");
        }
    }
}