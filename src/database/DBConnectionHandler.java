package database;

import model.Report;

import javax.swing.*;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

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

	private boolean insertRental(
			RentModel rent,
			String cardname,
			int cardNo,
			int expDate,
			int confnum) {
		try {
			/* Add the rental to the RENT table */
			PreparedStatement ps = connection.prepareStatement("INSERT INTO rent VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, rent.getRentId());
			ps.setInt(2, rent.getVid());
			ps.setInt(3, rent.getDlnum());
			ps.setDate(4, rent.getFrom());
			ps.setDate(5, null);
			ps.setString(6, cardname);
			ps.setInt(7, cardNo);
			ps.setInt(8, expDate);
			ps.setInt(9, confnum);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return false;
		}
	}

	private boolean setVehicleStatus(int vid, String status) {
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE vehicle SET vstatus = ? WHERE vid = ?");
			ps.setString(1, status);
			ps.setInt(2, vid);
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return false;
		}
	}

	public void rentWithoutReservation(
			String cardname,
			int cardNo,
			int expDate,
			String vtname,
			int dlnum) {
		try {
			String query =
			"SELECT vid " +
			"FROM vehicle v " +
			"WHERE v.vstatus='A' AND v.vtname=" + vtname;

			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if (!rs.next()) {
				throw new SQLException("No available vehicles found.");
			}

			int vid = rs.getInt("vid");
			Date from = new Date(System.currentTimeMillis());
			RentModel rent = new RentModel(vid, dlnum, from);

			if (!insertRental(rent, cardname, cardNo, expDate, 0)) {
				throw new SQLException("There was a problem creating this rental");
			}

			if (!setVehicleStatus(vid, "N")) {
				throw new SQLException("Vehicle " + vid + " is not available");
			}

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}	

	public void rentWithReservation(int confnum, String cardname, int cardNo, int expDate) {
		try {
			String query = 
			"SELECT vid, dlnum, fromDate " +  
			"FROM reservation r, vehicle v " +
			"WHERE r.vtname=v.vtname AND v.vstatus='A' AND r.confnum=" + confnum;
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if (!rs.next()) {
				throw new SQLException("No available vehicles found");
			}

			int vid = rs.getInt("vid");
			int dlnum = rs.getInt("dlnum");
			Date from = rs.getDate("fromdate");
			RentModel rent = new RentModel(vid, dlnum, from);

			if (!insertRental(rent, cardname, cardNo, expDate, confnum)) {
				throw new SQLException("There was a problem creating this rental");
			}

			if (!setVehicleStatus(vid, "N")) {
				throw new SQLException("Vehicle " + vid + " is not available");
			}

			connection.commit();
			stmt.close();

			/* Put together a receipt of the necessary information */
			StringBuilder receipt = new StringBuilder();
			receipt.append("CONFIRMING RENTAL " + rent.getRentId() + "\n");
			receipt.append("Date: " + from + "\n");
			receipt.append("Vehicle ID: " + vid + "\n");
			receipt.append("Driver: " + dlnum + "\n");
			System.out.print(receipt.toString());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void returnRental(int rentId) {
		try {
			String query =
			"SELECT fromdate, confnum, day_rate, v.vid "+
			"FROM rent r, vehicle v, vehicletype vt "+
			"WHERE r.vid=v.vid AND v.vtname=vt.vtname AND r.rent_id=" + rentId;
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
 
			if (!rs.next()) {
				throw new SQLException("No rental found with ID " + rentId);
			}

			int vid = rs.getInt("vid");
			Date from = rs.getDate("fromdate");
			Date to = new Date(System.currentTimeMillis());

			int rate = rs.getInt("day_rate");
			int confnum = rs.getInt("confnum");
			ReturnModel ret = new ReturnModel(rentId, from, to, rate);

			/* Inserting the new entry into the rent_return table */
			PreparedStatement ps = connection.prepareStatement("INSERT INTO rent_return VALUES (?,?,?)");
			ps.setInt(1, ret.getReturnId());
			ps.setDate(2, ret.getToDate());
			ps.setLong(3, ret.getValue());
			ps.executeUpdate();

			/* Updating the vehicle to set the status as Available */
			ps = connection.prepareStatement("UPDATE vehicle SET vstatus = ? WHERE vid = ?");
			ps.setString(1, "A");
			ps.setInt(2, vid);
			ps.executeUpdate();

			connection.commit();
			ps.close();
			stmt.close();

			/* Put together a receipt of the relevant information */
			StringBuilder receipt = new StringBuilder();
			receipt.append("RETURNING RENTAL " + rentId + "\n");
			if (confnum != 0) {
				receipt.append("Reservation #: " + confnum + "\n");
			}
			receipt.append("Date: " + to + "\n");
			receipt.append(ret.getDays() + " Days * $" + rate + ".00 PER DAY\n");
			receipt.append("Total: $" + ret.getValue() + ".00\n");

			System.out.print(receipt.toString());
		} catch (SQLException e) {
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

	/** Generates Daily Rental Report (for all branches) **/
	public Report generateRentalReport(String date) {
            try {
            	// Create statements for all SQL Queries
				PreparedStatement byBranchStm = connection.prepareStatement("select v.b_location as branch, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') group by v.b_location", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement byBranchCarStm = connection.prepareStatement("select v.b_location as branch, v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') group by v.b_location, v.vtname",
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement vehicleDeetsStm = connection.prepareStatement("select v.b_location as branch, v.make, v.model, v.color, v.year, r.RENT_ID, r.FROMDATE as rental_date from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				PreparedStatement countTotalStm = connection.prepareStatement("select COUNT(*) as count from rent where rent.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				byBranchStm.setString(1, date);
				byBranchCarStm.setString(1,date);
				vehicleDeetsStm.setString(1,date);
				countTotalStm.setString(1, date);

				// Processing for Report of Number of Rentals by Branch
				ResultSet branchRes = byBranchStm.executeQuery();

				String[] columns = {"branch", "numberOfRentals"};
				JTable branchTable = getTotalsTable(branchRes, columns);

				// Processing for Report of Number Of Rentals by Branch and Car Type
				ResultSet branchCarRes = byBranchCarStm.executeQuery();
				String[] byBranchCol = {"branch", "carType", "numberOfRentals"};
				JTable branchCarTable = byBranchTable(branchCarRes, byBranchCol);

				//Processing for Vehicle Details
				ResultSet vehicleDetails = vehicleDeetsStm.executeQuery();
				String vehicleResCol[] = {"branch", "make", "model", "color", "year", "rent_id", "rental_date"};
				JTable vTable = getVTable(vehicleDetails, vehicleResCol);

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

	private JTable getTotalsTable(ResultSet branchRes, String[] columns) throws SQLException {
		String totalsData[][] = new String[getRowCount(branchRes)][2];

		int i = 0;
		while (branchRes.next()) {
			String b  = branchRes.getString(columns[0]);
			int num = branchRes.getInt(columns[1]);
			totalsData[i][0] = b;
			totalsData[i][1] = String.valueOf(num);
			i++;
		}

		JTable totalsTable = new JTable(totalsData, columns);
		totalsTable.setBounds(30,40,200,300);
		return totalsTable;
	}

	private JTable getReturnByCarType(ResultSet returnsRes, String[] returnCols) throws SQLException {
		String returnsData[][] = new String[getRowCount(returnsRes)][4];

		int i = 0;
		while(returnsRes.next()) {
			String branch = returnsRes.getString(returnCols[0]);
			String cartype = returnsRes.getString(returnCols[1]);
			int numReturns = returnsRes.getInt(returnCols[2]);
			int revenue = returnsRes.getInt(returnCols[3]);
			returnsData[i][0] = branch;
			returnsData[i][1] = cartype;
			returnsData[i][2] = String.valueOf(numReturns);
			returnsData[i][3] = String.valueOf(revenue);
			i++;
		}

		JTable returnsTable = new JTable(returnsData, returnCols);
		returnsTable.setBounds(30, 40, 200, 300);
		return returnsTable;
	}

	private JTable getVTable(ResultSet vTableData, String[] vTableColumns) throws SQLException {
		String vehicleResData[][] = new String[getRowCount(vTableData)][7];

		int i = 0;
		while(vTableData.next()) {
			String bnch = vTableData.getString(vTableColumns[0]);
			String make = vTableData.getString(vTableColumns[1]);
			String model = vTableData.getString(vTableColumns[2]);
			String colour = vTableData.getString(vTableColumns[3]);
			String year = vTableData.getString(vTableColumns[4]);
			String rentId = vTableData.getString(vTableColumns[5]);
			String fromDate = vTableData.getString(vTableColumns[6]);
			vehicleResData[i][0] = bnch;
			vehicleResData[i][1] = make;
			vehicleResData[i][2] = model;
			vehicleResData[i][3] = colour;
			vehicleResData[i][4] = year;
			vehicleResData[i][5] = rentId;
			vehicleResData[i][6] = fromDate;
			i++;
		}
		JTable vTable = new JTable(vehicleResData, vTableColumns);
		vTable.setBounds(30, 40, 200, 300);
		return vTable;
	}

	private JTable byBranchTable(ResultSet branchCarRes, String[] byBranchCol) throws SQLException {
		String byBranchData[][] = new String[getRowCount(branchCarRes)][3];

		int i = 0;
		while(branchCarRes.next()) {
			String branch = branchCarRes.getString(byBranchCol[0]);
			String cartype = branchCarRes.getString(byBranchCol[1]);
			String key = branch + "-" + cartype;
			int numberOfRentals = branchCarRes.getInt(byBranchCol[2]);
			byBranchData[i][0] = branch;
			byBranchData[i][1] = cartype;
			byBranchData[i][2] = String.valueOf(numberOfRentals);
			i++;
		}

		JTable byBranchTable = new JTable(byBranchData, byBranchCol);
		byBranchTable.setBounds(30, 40, 200, 300);
		return byBranchTable;
	}

	private int getRowCount(ResultSet rs) throws SQLException {
		int rowCount = 0;
		rs.last();
		rowCount = rs.getRow();
		rs.beforeFirst();
		return rowCount;
	}

	/** Generates Daily Rental Report (for ONE branches) **/
    public Report generateRentalReportByBranch(String branch, String date) {
		try {
			PreparedStatement totalRentalsStm = connection.prepareStatement("select count(*) as count from rent r, vehicle v where r.vid = v.vid AND v.b_location = ? and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY')", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			PreparedStatement byCarTypeStm = connection.prepareStatement("select v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') and v.b_location = ? group by v.vtname", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			PreparedStatement vehicleDeetsStm = connection.prepareStatement("select v.B_LOCATION as branch, v.make, v.model, v.color, v.year, r.RENT_ID, r.FROMDATE as rental_date from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE(?,'DD/MM/YYYY') and v.b_location = ?", ResultSet.TYPE_SCROLL_INSENSITIVE,
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
			String vehicleResCol[] = {"branch", "make", "model", "color", "year", "rent_id", "rental_date"};
			JTable vehicleTable = getVTable(vehicleDetailRes, vehicleResCol);

			// # Rentals at this branch by Car Type on this Date
			String totalsCol[] = {"carType", "numberOfRentals"};
			JTable carTypeTable = getTotalsTable(carTypeRes, totalsCol);

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

	public Report generateReturnsReport() {
    	try {
			PreparedStatement vehicleInfoStm = connection.prepareStatement(
					"select v.b_location as branch, v.make, v.model, v.color, v.year, rr.RETURN_ID as return_id, rr.RETURN_DATE as return_date " +
							"from RENT_RETURN rr, rent r, vehicle v " +
							"where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID " +
							"order by v.B_LOCATION",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY

			);
			ResultSet vehicleInfo = vehicleInfoStm.executeQuery();
			String vehicleColumns[] = {"branch", "make", "model", "color", "year", "return_id", "return_date"};
			JTable vInfoTable = getVTable(vehicleInfo, vehicleColumns);



			PreparedStatement revenuePerCarAndBranchStm = connection.prepareStatement(
					"select v.B_LOCATION as branch, v.VTNAME as carType, count(v.vid) as num_returns, sum(rr.RETURN_VALUE) as revenue " +
							"from RENT_RETURN rr, rent r, vehicle v where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID " +
							"group by v.VTNAME, v.B_LOCATION " +
							"order by v.B_LOCATION",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY

			);
			ResultSet revenueByBranchAndCar = revenuePerCarAndBranchStm.executeQuery();
			String returnCols[] = {"branch", "carType", "num_returns", "revenue"};
			JTable returnByCarTable = getReturnByCarType(revenueByBranchAndCar, returnCols);


			PreparedStatement revenuePerBranchStm = connection.prepareStatement(
					"select v.B_LOCATION as branch, count(v.vid) as num_returns, sum(rr.RETURN_VALUE) as revenue " +
							"from RENT_RETURN rr, rent r, vehicle v " +
							"where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID " +
							"group by v.B_LOCATION order by v.B_LOCATION",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
			);
			ResultSet branchRevenue = revenuePerBranchStm.executeQuery();
			String byBranchCols[] = {"branch", "num_returns", "revenue"};
			JTable byBranchRevTable = byBranchTable(branchRevenue, byBranchCols);


			PreparedStatement totalRevenue = connection.prepareStatement("select sum(RETURN_VALUE) as totalRevenue from RENT_RETURN",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
			);
			ResultSet totalRevRes = totalRevenue.executeQuery();
			String totalRevenueValue = "";
			while (totalRevRes.next()) {
				totalRevenueValue = String.valueOf(totalRevRes.getString("totalRevenue"));
			}

			Report returnsReport = new Report(returnByCarTable, byBranchRevTable, vInfoTable, totalRevenueValue);
			return returnsReport;
		} catch (SQLException e) {
    		System.out.println(e);
		}
    	return null;
	}

	public Report generateReturnsReportOneBranch() {
    	//todo stub
		return null;
	}
}
