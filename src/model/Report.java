package model;

import javax.swing.*;

public class Report {
    private final JTable branchByCarTypeTable;
    private final JTable branchTable;
    private final JTable vehicleDetails;
    private final String totalNumRentals;
    public Report(JTable branchCarTypeTable, JTable branchTable, JTable vTable, String numRentals) {
        this.branchByCarTypeTable = branchCarTypeTable;
        this.branchTable = branchTable;
        this.vehicleDetails = vTable;
        this.totalNumRentals = numRentals;
    }

    public JTable getBranchAndCarTable() {
        return this.branchByCarTypeTable;
    }

    public JTable getBranchTable() {
        return this.branchTable;
    }

    public JTable getVTable() {
        return this.vehicleDetails;
    }

    public String getTotalNumRentals() {
        return this.totalNumRentals;
    }
}
