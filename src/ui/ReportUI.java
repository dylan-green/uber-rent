package ui;

import model.Report;

import javax.swing.*;

public class ReportUI {
    private JFrame f;

    public ReportUI(Report report) {
        f = new JFrame();
        f.setTitle("uber-rent");
        JScrollPane sp = new JScrollPane(report.getBranchAndCarTable());
        f.add(sp);
        f.setSize(300, 400);
        f.setVisible(true);
    }
}