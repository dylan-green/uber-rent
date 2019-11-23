package model;

import javax.swing.*;

public class Report {
    private final JTable byCarTypeTable;
    private final JTable byBranchTable;
    private final JTable vehicleDetails;
    private final String total;
    public Report(JTable branchCarTypeTable, JTable branchTable, JTable vTable, String numRentals) {
        this.byCarTypeTable = branchCarTypeTable;
        this.byBranchTable = branchTable;
        this.vehicleDetails = vTable;
        this.total = numRentals;
    }

    public JTable getByCarTypeTable() {
        return this.byCarTypeTable;
    }

    public JTable getByBranchTable() {
        return this.byBranchTable;
    }

    public JTable getVTable() {
        return this.vehicleDetails;
    }

    public String getTotal() {
        return this.total;
    }
}
