package model;

import javax.swing.*;
import java.sql.ResultSet;

public class Report {
//    private final String[][] branchCarTypeData;
//    private final String[] branchCarTypeColumns;
    private final JTable branchByCarTypeTable;
    public Report(JTable table) {
//        this.branchCarTypeData = branchAndCarData;
//        this.branchCarTypeColumns = branchCarTypeColumns;
        this.branchByCarTypeTable = table;
    }

    public JTable getBranchAndCarTable() {
        return this.branchByCarTypeTable;
    }
}
