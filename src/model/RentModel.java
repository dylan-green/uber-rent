package model;

import java.sql.Date;

public class RentModel {
    private final int vid;
	private final int dlnum;
    private 	  Date to;
    private final Date from;
	private final int rentId;

    public RentModel(
        int vid,
        int dlnum,
        Date from
        ) {
        this.vid = vid;
        this.dlnum = dlnum;
		this.to = null;
		this.from = from;
		this.rentId = dlnum + vid;
    }

	public int getVid() {
		return vid;
	}

	public int getDlnum() {
		return dlnum;
	}

	public Date getFrom() {
		return from;
	}

	public int getRentId() {
		return rentId;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}
}