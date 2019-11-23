package model;

import java.sql.Date;
import java.util.Random;

public class Reservation {

    private final int confnum;
    private final String vtname;
    private final int cust_dlnum;
    private final Date fromDate;
    private final Date toDate;

    public Reservation(
            String vtname,
            int cust_dlnum,
            Date fromDate,
            Date toDate) {
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

	public Date getFromDate() {
		return this.fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

}