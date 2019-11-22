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
            rent.dbHandler.rentWithReservation(111222, "Dylan Green", 99887766, 1201);
        } else {
            System.out.println("FAILURE!");
        }
    }
}