package model;

import java.sql.Date;

public class RentModel {
    private final int vid;
	private final int dlnum;
    private 	  Date to;
    private final Date from;
    private final int odometer;
    private final String cardname;
    private final int cardNo;
	private final int expDate;
	private final int rentId;

    public RentModel(
        int vid,
        int dlnum,
        Date from,
        int odometer,
        String cardname,
        int cardNo,
        int expDate
        ) {
        this.vid = vid;
        this.dlnum = dlnum;
		this.to = null;
		this.from = from;
        this.odometer = odometer;
        this.cardname = cardname;
        this.cardNo = cardNo;
		this.expDate = expDate;
		this.rentId = dlnum;
    }

	public int getRentId() {
		return this.rentId;
	}

	public int getVid() {
		return this.vid;
	}

	public int getDlnum() {
		return this.dlnum;
	}

	public Date getFromDate() {
		return this.from;
	}

	public Date getToDate() {
		return this.to;
	}

	public void setToDate(Date date) {
		this.to = date;
	}

	public int getOdometer() {
		return this.odometer;
	}

	public String getCardname() {
		return this.cardname;
	}

	public int getCardNo() {
		return this.cardNo;
	}

	public int getExpDate() {
		return this.expDate;
	}
}