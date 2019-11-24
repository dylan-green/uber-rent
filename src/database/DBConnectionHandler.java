package database;

import model.RentModel;
import model.ReportModel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.sql.*;

import model.ReportType;
import model.ReturnModel;

import model.Customer;
import model.Reservation;

public class DBConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
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

	/** Shared between both versions of rent
	 * 	rentWithReservation / rentWithoutReservation */
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
			if(confnum == 0) {
				ps.setNull(9, java.sql.Types.INTEGER);
			} else {
				ps.setInt(9, confnum);
			}
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
			return false;
		}
	}

	/** set status in vehicles table for v.vid/status */
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

	/** receipts specifically for rent with/without reservations */
	private String generateRentReceipt(RentModel rent, Date from, int vid, int dlnum) {
		/* Put together a receipt of the necessary information */
		StringBuilder receipt = new StringBuilder();
		receipt.append("CONFIRMING RENTAL " + rent.getRentId() + "\n");
		receipt.append("Date: " + from + "\n");
		receipt.append("Vehicle ID: " + vid + "\n");
		receipt.append("Driver: " + dlnum + "\n");

		return receipt.toString();
	}

	public String rentWithoutReso(
			String cardname,
			int cardNo,
			int expDate,
			String vtname,
			int dlnum) throws SQLException {
			PreparedStatement stmt = connection
					.prepareStatement("SELECT vid FROM vehicle WHERE vstatus='A' AND vtname=?");
			stmt.setString(1, vtname);

			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new SQLException("No available vehicles found\n");
			}

			int vid = rs.getInt("vid");
			Date from = new Date(System.currentTimeMillis());
			RentModel rent = new RentModel(vid, dlnum, from);

			if (!insertRental(rent, cardname, cardNo, expDate, 0)) {
				throw new SQLException("There was a problem creating this rental\n");
			}

			if (!setVehicleStatus(vid, "N")) {
				throw new SQLException("Vehicle " + vid + " is not available\n");
			}

			connection.commit();
			stmt.close();

			return generateRentReceipt(rent, from, vid, dlnum);
		}

	public String rentWithReso(int confnum, String cardname, int cardNo, int expDate) throws SQLException{
			PreparedStatement stmt = connection.prepareStatement("SELECT vid, dlnum, fromDate " +
			"FROM reservation r, vehicle v " +
					"WHERE r.vtname=v.vtname AND v.vstatus='A' AND r.confnum=?");
			stmt.setInt(1, confnum);

			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				throw new SQLException("No available vehicles found\n");
			}

			int vid = rs.getInt("vid");
			int dlnum = rs.getInt("dlnum");
			Date from = rs.getDate("fromdate");
			RentModel rent = new RentModel(vid, dlnum, from);

			if (!insertRental(rent, cardname, cardNo, expDate, confnum)) {
				throw new SQLException("There was a problem creating this rental\n");
			}

			if (!setVehicleStatus(vid, "N")) {
				throw new SQLException("Vehicle " + vid + " is not available\n");
			}

			connection.commit();
			stmt.close();

			return generateRentReceipt(rent, from, vid, dlnum);
		}

	public String returnRental(int rentId) throws SQLException{
			PreparedStatement stmt = connection.prepareStatement("SELECT fromdate, confnum, day_rate, v.vid FROM rent r, vehicle v, vehicletype vt WHERE r.vid=v.vid AND v.vtname=vt.vtname AND r.rent_id=?");

			stmt.setInt(1, rentId);
			ResultSet rs = stmt.executeQuery();

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

			return receipt.toString();
	}

	public boolean login() {
		try {
			if (connection != null) {
				connection.close();
			}
			System.out.println("ERROR: missing sqlplus credentials in DBConnectionHandler.login()");
			connection = DriverManager.getConnection(ORACLE_URL, "ora_sstarke", "a20448122");
			connection.setAutoCommit(false);

			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	public String makeReservationTransaction ( // todo THROW error if desired vehicle not available
		String res_vtname,
		int dlnum,
		String res_to_date,
		String res_from_date,
		String cust_name,
		String cust_addr,
		String cust_city,
		int cellphone) throws SQLException {
			// check for vehicle availability
			// If the customer’s desired vehicle is not available, an appropriate error message should be shown.
			//  The database state should reflect this at the end of the action.

			// check if dlnum refers to existing customer
			Statement stmt = null;
			String query = "SELECT * FROM customer WHERE dlnum=" + dlnum;
			//try {
				int res = -1;
				stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					res = rs.getInt("dlnum");
				};
				
				// not a customer so create new customer
				if (res == -1) {
					insertCustomer(new Customer(dlnum, cust_name, cust_addr, cust_city, cellphone)); 
				}

			//} 
			// catch (SQLException e) {
			// 	System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			// 	rollbackConnection();
			// }
			Reservation r = new Reservation(res_vtname, dlnum, res_from_date, res_to_date);
			isVehicleAvailable(res_vtname);
			insertReservation(r);
			StringBuilder receipt = new StringBuilder();
			receipt.append("RESERVATION: Confirmation Number:" + r.getConfnum() + "\n");
			receipt.append("To Date: " + r.getToDate() + "\n");
			receipt.append("From Date: " + r.getFromDate() + "\n");
			receipt.append("Vehicle Type: " + r.getVtname() + "\n");
			receipt.append("Drivers License: " + r.getCustDlnum() + "\n");
			return receipt.toString();
	}

	public void isVehicleAvailable(String vehicleType) throws SQLException {
		Statement stmt = null;
		String query = "SELECT v.vid FROM vehicle v WHERE v.vtname = '" + vehicleType + "' AND " + "v.vstatus = 'A'";

		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.next()) {
			throw new SQLException("No available vehicles found\n");
		}	
	}

	public void insertCustomer(Customer customer) throws SQLException {
		//try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?)");
			ps.setInt(1, customer.getDlnum());
			ps.setString(2, customer.getCustomerName());
			ps.setString(3, customer.getCustomerAddr());
			ps.setString(4, customer.getCustomerCity());
			ps.setInt(5, customer.getCellphone());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		//} 
		// catch (SQLException e) {
		// 	System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		// 	rollbackConnection();
		// }
	}

	public void insertReservation(Reservation reservation) throws SQLException {
		//try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY'))");
			ps.setInt(1, reservation.getConfnum());
			ps.setString(2, reservation.getVtname());
			ps.setInt(3, reservation.getCustDlnum());
			ps.setString(4, reservation.getFromDate());
			ps.setString(5, reservation.getToDate());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		//} 
		// catch (SQLException e) {
		// 	System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		// 	rollbackConnection();
		// }
	}

	public String availableVehicleTransaction(String vehicleType, String location, String toDate, String fromDate, String flag) throws SQLException {
		Statement stmt = null;
		Statement stmt2 = null;
		String query = "SELECT v.vid AS id, v.vlicense AS license, v.make AS make, v.model AS model, v.year AS year, v.color AS color, v.vstatus AS status, v.vtname AS name, v.b_location AS location, v.city AS city FROM vehicle v WHERE ";
		String queryCount = "SELECT COUNT(v.vid) AS count FROM vehicle v WHERE ";
		String queryRentedVehicles = "";
		Boolean andRequired = false;
		//String availableVehicleResults = "";

		// if vehicleType passed in then add to query
		if (!vehicleType.equals("Vehicle type")) {
			query = query + "v.vtname = '" + vehicleType + "' ";
			queryCount = queryCount + "v.vtname = '" + vehicleType + "' ";
			andRequired = true;
		}
		// if location passed in then add to query
		if (!location.equals("Location")) {
			if (andRequired) {
				query = query + "AND ";
				queryCount = queryCount + "AND ";
			}
			query = query + "v.b_location = '" + location + "' ";
			queryCount = queryCount + "v.b_location = '" + location + "' ";
			andRequired = true;
		}

		// if date exists, construct query that will get all the vehicles rented in this time period
		if (!toDate.equals("To Date") && !fromDate.equals("From Date")) {
			if (andRequired) {
				query = query + "AND ";
				queryCount = queryCount + "AND ";
			}
			queryRentedVehicles = "SELECT r.vid FROM rent r WHERE r.toDate >= TO_DATE(" + toDate + ",'DD/MM/YYYY')  AND r.fromDate <= TO_DATE(" + fromDate + ",'DD/MM/YYYY')";
			query = query + "v.vid NOT IN (" + queryRentedVehicles + ") ";
			queryCount = queryCount + "v.vid NOT IN (" + queryRentedVehicles + ") ";
			andRequired = true;
		} else {
			if (andRequired) {
				query = query + "AND ";
				queryCount = queryCount + "AND ";
			}
			query = query + "v.vstatus = " + "'A' ";
			queryCount = queryCount + "v.vstatus = " + "'A' ";

		}
		// TODO execute for both query and queryCount
		StringBuilder availableVehicleResults = new StringBuilder();
		availableVehicleResults.append("id                    license                 make                 model                 year                 color                 status                 name                 address                 city                 "+ "\n");
		try {
			stmt = connection.createStatement();
			System.out.print(query);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				//v.b_location AS location, v.city AS city 
				availableVehicleResults.append(rs.getString("id") + "              ");
				availableVehicleResults.append(rs.getString("license") + "              ");
				availableVehicleResults.append(rs.getString("make") + "              ");
				availableVehicleResults.append(rs.getString("model") + "              ");
				availableVehicleResults.append(rs.getString("year") + "              ");
				availableVehicleResults.append(rs.getString("color") + "              ");
				availableVehicleResults.append(rs.getString("status") + "              ");
				availableVehicleResults.append(rs.getString("name") + "              ");
				availableVehicleResults.append(rs.getString("location") + "              ");
				availableVehicleResults.append(rs.getString("city") + "              ");
				availableVehicleResults.append("\n");
			};
			
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

		StringBuilder availableVehicleCount = new StringBuilder();
		availableVehicleCount.append("Number of available vehicles for Type: " + vehicleType + " Location: " + location +" toDate: " + toDate + " fromDate: " + fromDate + "\n");
		try {
			stmt2 = connection.createStatement();
			System.out.print(queryCount);
			ResultSet rs = stmt.executeQuery(queryCount);
			while (rs.next()) {
				availableVehicleCount.append(rs.getInt("count"));
			};
			
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}

		if (flag.equals("count")) {
			return availableVehicleCount.toString();
		} else {
			return availableVehicleResults.toString();
		}
		

	}

	public void generateReportForBranch(String location, String city) {
		Statement stmt = null;
		String query = "SELECT v.vtname AS VehicleType, count(v.vid) AS Returned, SUM(vt.day_rate * (r.toDate - r.fromDate)) AS Revenue "
		+ "FROM vehicle v, rent r, rent_return rr, vehicletype vt "
		+ "WHERE rr.return_id = r.rent_id AND r.vid = v.vid AND v.vtname = vt.vtname AND v.b_location = '" + location + "' AND v.city = '" + city + "' "
		+ "GROUP BY v.vtname;";

		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				// 
			};
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}


	/** Generates Daily Rental Report (for all branches) **/
	public ReportModel generateRentalReport(String date) throws SQLException {
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
				ReportModel report = new ReportModel(branchCarTable, branchTable, vTable, numRentals, ReportType.RENTALS);
				return report;
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
    public ReportModel generateRentalReportByBranch(String branch, String date) throws SQLException {
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

			ReportModel report = new ReportModel(null, carTypeTable, vehicleTable, numRentals, ReportType.RENTALS);
			return report;
	}

	public ReportModel generateReturnsReport() throws SQLException {
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

			ReportModel returnsReport = new ReportModel(returnByCarTable, byBranchRevTable, vInfoTable, totalRevenueValue, ReportType.RETURN);
			return returnsReport;
	}

	public ReportModel generateReportForBranch(String location) throws SQLException{
		PreparedStatement vehicleDetailsStm = connection.prepareStatement(
				"select v.vtname as carType, v.make, v.model, v.color, v.year, rr.RETURN_ID as return_id, rr.RETURN_DATE as return_date from RENT_RETURN rr, rent r, vehicle v where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID and v.B_LOCATION = ? order by v.VTNAME",
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY

		);

		vehicleDetailsStm.setString(1, location);
		ResultSet vInfo = vehicleDetailsStm.executeQuery();
		String vCols[] = {"carType", "make", "model", "color", "year", "return_id", "return_date"};
		JTable vehicleInfoTable = getVTable(vInfo, vCols);

		PreparedStatement revByCarTypeStm = connection.prepareStatement(
				"select v.VTNAME as carType, count(v.vid) as num_returns, sum(rr.RETURN_VALUE) as revenue from RENT_RETURN rr, rent r, vehicle v\n" +
						"where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID and v.B_LOCATION = ? group by v.VTNAME",
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY
		);
		revByCarTypeStm.setString(1, location);
		ResultSet revByCarType = revByCarTypeStm.executeQuery();
		String retCols[] = {"carType", "num_returns", "revenue"};
		JTable revByCarTable = byBranchTable(revByCarType, retCols);

		PreparedStatement revStm = connection.prepareStatement("select sum(rr.RETURN_VALUE) as totalRevenue from RENT_RETURN rr, rent r, vehicle v\n" +
						"where rr.RETURN_ID = r.RENT_ID and r.VID = v.VID and v.B_LOCATION = ?",
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY
		);
		revStm.setString(1, location);
		ResultSet revAtBranch = revStm.executeQuery();
		String totalRevenueValue = "";
		while (revAtBranch.next()) {
			totalRevenueValue = String.valueOf(revAtBranch.getString("totalRevenue"));
		}

		ReportModel oneBranchReturnsReport = new ReportModel(null, revByCarTable, vehicleInfoTable, totalRevenueValue, ReportType.RETURN);
		return oneBranchReturnsReport;
	}
}