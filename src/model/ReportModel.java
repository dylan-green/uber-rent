package model;

import javax.swing.*;

public class ReportModel {
    private final JTable byCarTypeTable;
    private final JTable byBranchTable;
    private final JTable vehicleDetails;
    private final String total;
    private final ReportType type;
    public ReportModel(JTable branchCarTypeTable, JTable branchTable, JTable vTable, String numRentals, ReportType type) {
        this.byCarTypeTable = branchCarTypeTable;
        this.byBranchTable = branchTable;
        this.vehicleDetails = vTable;
        this.total = numRentals;
        this.type = type;
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

    public ReportType getType() { return this.type; }
}
