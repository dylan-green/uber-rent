package model;

import java.sql.Date;

public class RentModel {
    private static int rentIdCounter = 1;

    private final int rentId;
    private final int vid;
    private final int dlnum;
    private final Date fromDate;
    private Date toDate;
    private final int odometer;
    private final String cardname;
    private final int cardNo;
    private final int expDate;
    private final int confnum;

    public RentModel(
        int vid,
        int dlnum,
        Date fromDate,
        int odometer,
        String cardname,
        int cardNo,
        int expDate
        ) {
        this.rentId = rentIdCounter;
        this.vid = vid;
        this.dlnum = dlnum;
        this.fromDate = fromDate;
        this.toDate = null;
        this.odometer = odometer;
        this.cardname = cardname;
        this.cardNo = cardNo;
        this.expDate = expDate;
        this.confnum = (int)Math.random() % 1000000;
        rentIdCounter++;
    }

	public int getRentId() {
		return rentId;
	}

	public int getVid() {
		return vid;
	}

	public int getDlnum() {
		return dlnum;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getOdometer() {
		return odometer;
	}

	public String getCardname() {
		return cardname;
	}

	public int getCardNo() {
		return cardNo;
	}

	public int getExpDate() {
		return expDate;
	}

	public int getConfnum() {
		return confnum;
	}

}