package model;

import java.sql.Date;
import java.util.Random;

public class Reservation {

    private final int confnum;
    private final String vtname;
    private final int cust_dlnum;
    private final String fromDate;
    private final String toDate;

    public Reservation(
            String vtname,
            int cust_dlnum,
            String fromDate,
            String toDate) {
        this.confnum = new Random().nextInt(10000);
        this.vtname = vtname;
        this.cust_dlnum = cust_dlnum;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getConfnum() {
        return this.confnum;
    }

	public String getVtname() {
		return this.vtname;
	}

	public int getCustDlnum() {
		return this.cust_dlnum;
	}

	public String getFromDate() {
		return this.fromDate;
	}

	public String getToDate() {
		return this.toDate;
	}

}