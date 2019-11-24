package ui;

import model.ReportModel;
import model.ReportType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DailyRentalReportUI {

    private JTabbedPane tabbedPane = new JTabbedPane();
    private JFrame f = new JFrame();
    private String[] columnNames = {};
    private Object[][] data = {};
    private DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        private static final long serialVersionUID = 1L;

        @Override
        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
    };

    public DailyRentalReportUI(ReportModel report) {
        if (report.getByCarTypeTable() != null) {
            tabbedPane.addTab("Branch and Car Type", new JScrollPane(report.getByCarTypeTable()));
        }
        tabbedPane.addTab("Branch", new JScrollPane(report.getByBranchTable()));
        tabbedPane.addTab("Vehicle Details", new JScrollPane(report.getVTable()));
        String tabTitle = "";
        if (report.getType() == ReportType.RENTALS) {
            tabTitle = "Total Number of Rentals";
        } else {
            tabTitle = "Total Revenue for Returns";
        }
        tabbedPane.addTab(tabTitle, new JScrollPane(new JLabel(report.getTotal())));
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.add(tabbedPane, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}