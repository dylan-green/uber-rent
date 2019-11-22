package database;

import model.Report;

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
    		System.out.println("wrong");
    		return false;
		}
	}

	/** Generates Daily Report (for all branches) **/
	public void generateReport(String date) {
            try {
                Statement statement = connection.createStatement();
				Statement statement2 = connection.createStatement();
				Statement statement3 = connection.createStatement();
				Statement statement4 = connection.createStatement();

				//TODO: use variable date for these
                ResultSet branchResults = statement.executeQuery("select v.b_location as branch, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') group by v.b_location;");
                ResultSet branchAndTypeResults = statement2.executeQuery("select v.b_location as branch, v.vtname as cartype, COUNT(*) as numberOfRentals from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY') group by v.b_location, v.vtname;");
                ResultSet totalRentals = statement3.executeQuery("select COUNT(*) as count from rent");
                ResultSet vehicleDetails = statement4.executeQuery("select v.b_location as branch, v.make, v.model, v.color, v.year from rent r, vehicle v where r.vid = v.vid and r.FROMDATE = TO_DATE('2/11/2019','DD/MM/YYYY')");

				HashMap branchData = new HashMap();
				HashMap branchTypeData = new HashMap();
				int rentalCount = 0;

				//TODO instantiate Report object with resultant tables instead, move processing somewhere else, whereever report will be rendered

				while (totalRentals.next()) {
					rentalCount = totalRentals.getInt("count");
					System.out.print(rentalCount);
				}

				while(branchResults.next()){
					String id  = branchResults.getString("branch");
					int numberOfRentals = branchResults.getInt("numberOfRentals");
					branchData.put(id, numberOfRentals);
				}

				while(branchAndTypeResults.next()) {
					String branch = branchAndTypeResults.getString("branch");
					String cartype = branchAndTypeResults.getString("carType");
					String key = branch + "-" + cartype;
					int numberOfRentals = branchAndTypeResults.getInt("numberOfRentals");
					branchTypeData.put(key, numberOfRentals);
				}
////				Report dailyReport = new Report(branchData, rentalCount, branchTypeData);
//				System.out.print(dailyReport);
            } catch (SQLException e) {
                System.out.println(e);
            }
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