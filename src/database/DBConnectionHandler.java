package database;

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

import model.ReturnModel;
import model.RentModel;

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
	
			connection = DriverManager.getConnection(ORACLE_URL, "ora_sstarke", "a20448122");
			connection.setAutoCommit(false);
	
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			return false;
		}
	}

	public void makeReservationTransaction(
		String res_vtname,
		int dlnum,
		Date res_to_date, 
		Date res_from_date,
		String cust_name,
		String cust_addr,
		String cust_city,
		int cellphone) {
			// check for vehicle availability
			// If the customer’s desired vehicle is not available, an appropriate error message should be shown.
			//  The database state should reflect this at the end of the action.

			// check if dlnum refers to existing customer
			Statement stmt = null;
			String query = "SELECT * FROM customer WHERE dlnum=" + dlnum;
			try {
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

			} catch (SQLException e) {
				System.out.println(EXCEPTION_TAG + " " + e.getMessage());
				rollbackConnection();
			}	
			insertReservation(new Reservation(res_vtname, dlnum, res_from_date, res_to_date));
			// upon success show detail TODO
	}

	public void insertCustomer(Customer customer) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?)");
			ps.setInt(1, customer.getDlnum());
			ps.setString(2, customer.getCustomerName());
			ps.setString(3, customer.getCustomerAddr());
			ps.setString(4, customer.getCustomerCity());
			ps.setInt(5, customer.getCellphone());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void insertReservation(Reservation reservation) {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO reservation VALUES (?,?,?,?,?)");
			ps.setInt(1, reservation.getConfnum());
			ps.setString(2, reservation.getVtname());
			ps.setInt(3, reservation.getCustDlnum());
			ps.setDate(4, reservation.getFromDate());
			ps.setDate(5, reservation.getToDate());

			ps.executeUpdate();
			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			rollbackConnection();
		}
	}

	public void availableVehicleTransaction(String vehicleType, String location, int toDate, int fromDate ) {
		Statement stmt = null;
		String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.vstatus, v.vtname, v.b_location, v.city FROM vehicle v WHERE ";
		String queryCount = "SELECT COUNT(v.vid) FROM vehicle v WHERE ";
		String queryRentedVehicles = "";
		Boolean andRequired = false;

		// if vehicleType passed in then add to query
		if (vehicleType != "") {
			query = query + "v.vtname = '" + vehicleType + "' ";
			queryCount = queryCount + "v.vtname = '" + vehicleType + "' ";
			andRequired = true;
		}
		// if location passed in then add to query
		if (location != "") {
			if (andRequired) {
				query = query + "AND ";
				queryCount = queryCount + "AND ";
			}
			query = query + "v.b_location = '" + location + "' ";
			queryCount = queryCount + "v.b_location = '" + location + "' ";
			andRequired = true;
		}

		// if date exists, construct query that will get all the vehicles rented in this time period
		if (toDate != 0 && fromDate != 0) {
			if (andRequired) {
				query = query + "AND ";
				queryCount = queryCount + "AND ";
			}
			queryRentedVehicles = "SELECT r.vid FROM rent r WHERE r.toDate >= " + toDate + " AND " + "r.fromDate <= " + fromDate;
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
		// try {
		// 	stmt = connection.createStatement();
		// 	ResultSet rs = stmt.executeQuery(query);
		// 	while (rs.next()) {
		// 		//
		// 	};
			
		// } catch (SQLException e) {
		// 	System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		// 	rollbackConnection();
		// }

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

}
