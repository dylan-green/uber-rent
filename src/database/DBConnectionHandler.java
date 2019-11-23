package database;

import model.Report;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

import model.ReturnModel;
import model.RentModel;

public class DBConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";
	private Connection connection = null;

    public DBConnectionHandler() {
        try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.print("here");
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	private void rollbackConnection() {
		try {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}

	public void insertBranch(String addr, String city) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO branch VALUES (?,?)");
			ps.setString(1, addr);
			ps.setString(2, city);

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void returnRental(int rentId) {
		try {
			String query =
			"SELECT fromdate, confnum, day_rate "+
			"FROM rent r, vehicle v, vehicletype vt "+
			"WHERE r.vid=v.vid AND v.vtname=vt.vtname AND r.rent_id=" + rentId;
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			rs.next();
			Date from = rs.getDate("fromdate");
			Date to = new Date(System.currentTimeMillis());

			int rate = rs.getInt("day_rate");
			int confnum = rs.getInt("confnum");
			ReturnModel ret = new ReturnModel(rentId, from, to, rate);

			PreparedStatement ps = connection.prepareStatement("INSERT INTO rent_return VALUES (?,?,?)");
			ps.setInt(1, ret.getReturnId());
			ps.setDate(2, ret.getToDate());
			ps.setLong(3, ret.getValue());

			ps.executeUpdate();
			connection.commit();

			ps.close();
			stmt.close();

			StringBuilder receipt = new StringBuilder();
			receipt.append("RETURNING RENTAL " + rentId + "\n");
			if (confnum != 0) {
				receipt.append("	RESERVATION: " + confnum + "\n");
			}
			receipt.append("	DATE: " + to + "\n");
			receipt.append("	" + ret.getDays() + " DAYS * $" + rate + ".00 PER DAY\n");
			receipt.append("	TOTAL: $" + ret.getValue() + ".00\n");

			System.out.print(receipt.toString());

		} catch (SQLException e) {
			// System.out.println("Could not find a rental with ID: " + rentId);
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public boolean login() {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, "ora_ahkiho", "a37653128");
			connection.setAutoCommit(false);

			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	/** Generates Daily Report (for all branches) **/
	public Report generateReport(String date) {
            try {
            	// Create statements for all SQL Queries
				PreparedStatement byBranchStm = connection.prepareStatement("select v.b_location as branch, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') group by v.b_location", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement byBranchCarStm = connection.prepareStatement("select v.b_location as branch, v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') group by v.b_location, v.vtname",
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement vehicleDeetsStm = connection.prepareStatement("select v.b_location as branch, v.make, v.model, v.color, v.year, r.RENT_ID, r.FROMDATE from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement countTotalStm = connection.prepareStatement("select COUNT(*) as count from rent where rent.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				byBranchStm.setString(1, date);
				byBranchCarStm.setString(1,date);
				vehicleDeetsStm.setString(1,date);
				countTotalStm.setString(1, date);

				// Processing for Report of Number of Rentals by Branch
				ResultSet branchRes = byBranchStm.executeQuery();

				JTable branchTable = getTotalRentalTable(branchRes, false);

				// Processing for Report of Number Of Rentals by Branch and Car Type
				ResultSet branchCarRes = byBranchCarStm.executeQuery();
				JTable branchCarTable = getNumRentalsTable(branchCarRes);

				//Processing for Vehicle Details
				ResultSet vehicleDetails = vehicleDeetsStm.executeQuery();
				JTable vTable = getVTable(vehicleDetails);

				//Processing for Total Number of Rentals
				ResultSet totalRentals = countTotalStm.executeQuery();
				String numRentals = "";
				while (totalRentals.next()) {
					numRentals = String.valueOf(totalRentals.getString("count"));
				}

				// create Report object to hold all the tables, and data
				Report report = new Report(branchCarTable, branchTable, vTable, numRentals);
				return report;
			} catch (SQLException e) {
				System.out.println(e);
			}
		return null;
	}

	private JTable getTotalRentalTable(ResultSet branchRes, boolean singleBranch) throws SQLException {
		String firstColumn = "";
		if (singleBranch) {
			firstColumn = "carType";
		} else {
			firstColumn = "branch";
		}
		String branchResCol[] = {firstColumn, "Number of Rentals"};
		String branchResData[][] = new String[getRowCount(branchRes)][2];

		int i = 0;
		while (branchRes.next()) {
			String b  = branchRes.getString(firstColumn);
			int num = branchRes.getInt("numberOfRentals");
			branchResData[i][0] = b;
			branchResData[i][1] = String.valueOf(num);
			i++;
		}

		JTable branchTable = new JTable(branchResData, branchResCol);
		branchTable.setBounds(30,40,200,300);
		return branchTable;
	}

	private JTable getNumRentalsTable(ResultSet branchCarRes) throws SQLException {
		String branchCarResColumn[] = {"Branch", "Car Type", "Number of Rentals"};
		String branchCarResData[][] = new String[getRowCount(branchCarRes)][3];

		int i = 0;
		while(branchCarRes.next()) {
			String branch = branchCarRes.getString("branch");
			String cartype = branchCarRes.getString("carType");
			String key = branch + "-" + cartype;
			int numberOfRentals = branchCarRes.getInt("numberOfRentals");
			branchCarResData[i][0] = branch;
			branchCarResData[i][1] = cartype;
			branchCarResData[i][2] = String.valueOf(numberOfRentals);
			i++;
		}

		JTable branchCarTable = new JTable(branchCarResData, branchCarResColumn);
		branchCarTable.setBounds(30, 40, 200, 300);
		return branchCarTable;
	}

	private JTable getVTable(ResultSet vehicleDetails) throws SQLException {
		String vehicleResCol[] = {"Branch", "Make", "Model", "Colour", "Year", "Rent ID", "Rental Date"};
		String vehicleResData[][] = new String[getRowCount(vehicleDetails)][7];

		int i = 0;
		while(vehicleDetails.next()) {
			String bnch = vehicleDetails.getString("branch");
			String make = vehicleDetails.getString("make");
			String model = vehicleDetails.getString("model");
			String colour = vehicleDetails.getString("color");
			String year = vehicleDetails.getString("year");
			String rentId = vehicleDetails.getString("rent_id");
			String fromDate = vehicleDetails.getString("fromdate");
			vehicleResData[i][0] = bnch;
			vehicleResData[i][1] = make;
			vehicleResData[i][2] = model;
			vehicleResData[i][3] = colour;
			vehicleResData[i][4] = year;
			vehicleResData[i][5] = rentId;
			vehicleResData[i][6] = fromDate;
			i++;
		}
		JTable vTable = new JTable(vehicleResData, vehicleResCol);
		vTable.setBounds(30, 40, 200, 300);
		return vTable;
	}

	private int getRowCount(ResultSet rs) throws SQLException {
		int rowCount = 0;
		rs.last();
		rowCount = rs.getRow();
		rs.beforeFirst();
		return rowCount;
	}

    public Report generateReportByBranch(String branch, String date) {
		try {
			PreparedStatement totalRentalsStm = connection.prepareStatement("select count(*) as count from rent r, vehicle v where r.vid = v.vid AND v.b_location = ? and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			PreparedStatement byCarTypeStm = connection.prepareStatement("select v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') and v.b_location = ? group by v.vtname", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			PreparedStatement vehicleDeetsStm = connection.prepareStatement("select v.B_LOCATION as branch, v.make, v.model, v.color, v.year, r.RENT_ID, r.FROMDATE from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') and v.b_location = ?", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//set location
			totalRentalsStm.setString(1, branch);
			totalRentalsStm.setString(2, date);
			byCarTypeStm.setString(2, branch);
			byCarTypeStm.setString(1, date);
			vehicleDeetsStm.setString(2, branch);
			vehicleDeetsStm.setString(1, date);

			ResultSet totalRentRes = totalRentalsStm.executeQuery();
			ResultSet carTypeRes = byCarTypeStm.executeQuery();
			ResultSet vehicleDetailRes = vehicleDeetsStm.executeQuery();

			// vehicle details of all the vehicles rented out at this branch on this day
			JTable vehicleTable = getVTable(vehicleDetailRes);

			// # Rentals at this branch by Car Type on this Date
			JTable carTypeTable = getTotalRentalTable(carTypeRes, true);

			// Total Number of Rentals At This Branch
			String numRentals = "";
			while (totalRentRes.next()) {
				numRentals = String.valueOf(totalRentRes.getString("count"));
			}

			Report report = new Report(null, carTypeTable, vehicleTable, numRentals);
			return report;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}
}