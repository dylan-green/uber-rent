package database;

import model.Report;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

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

    public boolean connect() {
    	try {
			connection = DriverManager.getConnection(ORACLE_URL, "ora_ahkiho", "a37653128");
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
    		System.out.println(e);
    		return false;
		}
	}

	/** Generates Daily Report (for all branches) **/
	public Report generateReport(String date) {
            try {

            	// Create statements for all SQL Queries
				Statement byBranchStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				Statement byBranchCarStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				Statement vehicleDeetsStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				Statement countTotalStm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				int i;

				// Processing for Report of Number of Rentals by Branch
				ResultSet branchRes = byBranchStm.executeQuery("select v.b_location as branch, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') group by v.b_location");
				String branchResCol[] = {"Branch", "Number of Rentals"};
				String branchResData[][] = new String[getRowCount(branchRes)][2];

				i = 0;
				while (branchRes.next()) {
					String b  = branchRes.getString("branch");
					int num = branchRes.getInt("numberOfRentals");
					branchResData[i][0] = b;
					branchResData[i][1] = String.valueOf(num);
					i++;
				}

				JTable branchTable = new JTable(branchResData, branchResCol);
				branchTable.setBounds(30,40,200,300);



				// Processing for Report of Number Of Rentals by Branch and Car Type
				ResultSet branchCarRes = byBranchCarStm.executeQuery("select v.b_location as branch, v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') group by v.b_location, v.vtname");
				String branchCarResColumn[] = {"Branch", "Car Type", "Number of Rentals"};
				String branchCarResData[][] = new String[getRowCount(branchCarRes)][3];

				i = 0;
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

				//Processing for Vehicle Details
				ResultSet vehicleDetails = vehicleDeetsStm.executeQuery("select v.b_location as branch, v.make, v.model, v.color, v.year, r.RENT_ID, r.FROMDATE from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY')");
				String vehicleResCol[] = {"Branch", "Make", "Model", "Colour", "Year", "Rent ID", "Rental Date"};
				String vehicleResData[][] = new String[getRowCount(vehicleDetails)][7];

				i = 0;
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
				branchCarTable.setBounds(30, 40, 200, 300);

				//Processing for Total Number of Rentals
				ResultSet totalRentals = countTotalStm.executeQuery("select COUNT(*) as count from rent where rent.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY')");
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

	private int getRowCount(ResultSet rs) throws SQLException {
		int rowCount = 0;
		rs.last();
		rowCount = rs.getRow();
		rs.beforeFirst();
		return rowCount;
	}

    public void generateReportByBranch(String branch) {
		try {
			//TODO ALSO TAKE IN DATE

			// total rentals on this date, for this branch
			PreparedStatement ps = connection.prepareStatement("select count(*) as count from rent r, vehicle v where r.vid = v.vid AND v.b_location = ? and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY')");
			ps.setString(1, branch);

			// rentals on this date, at this branch, by carType
			PreparedStatement ps2 = connection.prepareStatement("select v.vtname as carType, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid AND v.b_location = ? and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') group by v.vtname");
			// select v.vtname as carType, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid AND v.b_location = ? group by v.vtname;

			// vehicle details of all the vehicles rented out at this branch on this day
			PreparedStatement ps3 = connection.prepareStatement("select v.b_location as branch, v.make, v.model, v.color, v.year from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') and v.b_location = '20 Sunset Boulevard'");

			ResultSet rs = ps.executeQuery();
			ResultSet rs2 = ps2.executeQuery();
			ResultSet rs3 = ps3.executeQuery();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}