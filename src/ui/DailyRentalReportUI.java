package ui;

import model.Report;

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

    public DailyRentalReportUI(Report report) {
        if (report.getByCarTypeTable() != null) {
            tabbedPane.addTab("Branch and Car Type", new JScrollPane(report.getByCarTypeTable()));
        }
        tabbedPane.addTab("Branch", new JScrollPane(report.getByBranchTable()));
        tabbedPane.addTab("Vehicle Details", new JScrollPane(report.getVTable()));
        tabbedPane.addTab("Total Number of Rentals", new JScrollPane(new JLabel(report.getTotal()))); //TODO dynamically change tab name
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.add(tabbedPane, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}