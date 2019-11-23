package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;

import model.ReturnModel;
import model.RentModel;

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

	public void rentWithoutReservation(
			String cardname,
			int cardNo,
			int expDate,
			String vtname,
			int dlnum) {
		try {
			String query = 
			"SELECT vid, odometer " +
			"FROM vehicle v " +
			"WHERE v.vstatus='A' AND v.vtname=" + vtname;
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if (!rs.next()) {
				throw new SQLException("No available vehicles found.");
			}
				
			System.out.println(query);

		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void rentWithReservation(int confnum, String cardname, int cardNo, int expDate) {
		try {
			String query = 
			"SELECT vid, dlnum, fromDate, odometer " +  
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
			int odometer = rs.getInt("odometer");
			RentModel rent = 
					new RentModel(vid, dlnum, from, odometer, cardname, cardNo, expDate);
			
			/* Add the rental to the RENT table */
			PreparedStatement ps = connection.prepareStatement("INSERT INTO rent VALUES (?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, rent.getRentId());
			ps.setInt(2, vid);
			ps.setInt(3, dlnum);
			ps.setDate(4, from);
			ps.setDate(5, null);
			ps.setInt(6, odometer);
			ps.setString(7, cardname);
			ps.setInt(8, cardNo);
			ps.setInt(9, expDate);
			ps.setInt(10, confnum);
			ps.executeUpdate();

			/* Set the vehicle status to 'N' Not Available */
			ps = connection.prepareStatement("UPDATE vehicle SET vstatus = ? WHERE vid = ?");
			ps.setString(1, "N");
			ps.setInt(2, vid);
			ps.executeUpdate();

			connection.commit();
			ps.close();
			stmt.close();

			/* Put together a receipt of the necessary information */
			StringBuilder receipt = new StringBuilder();
			receipt.append("CONFIRMING RENTAL " + rent.getRentId() + "\n");
			receipt.append("Date: " + from + "\n");
			receipt.append("Vehicle ID: " + vid + "\n");
			receipt.append("Current Mileage: " + odometer + "km \n");
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
	
			connection = DriverManager.getConnection(ORACLE_URL, "ora_gdylan", "a52143104");
			connection.setAutoCommit(false);
	
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}
}