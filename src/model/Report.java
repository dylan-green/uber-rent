package model;

import java.sql.ResultSet;
import java.util.HashMap;

public class Report {
    private final ResultSet rsByBranch;
    private final ResultSet rsByBranchAndCarType;
    private final ResultSet rsVehicleDetails;
    private final int totalNumRentals;
    public Report(ResultSet branchData, ResultSet branchAndCarData, ResultSet vehicleDetails, int totalRentals) {
        this.rsByBranch = branchData;
        this.rsByBranchAndCarType = branchAndCarData;
        this.rsVehicleDetails = vehicleDetails;
        this.totalNumRentals = totalRentals;
    }
}
