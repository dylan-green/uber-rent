package model;

import java.sql.Date;

public class ReturnModel {
    private final int returnId;
    private final int rate;
    private final Date from;
    private final Date to;
    private long days;
    private long value;
    
    public ReturnModel(int returnId, Date from, Date to, int rate) {
        this.returnId = returnId;
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.days = calculateDays();
        this.value = calculateValue();
    }

	public int getReturnId() {
		return returnId;
	}

    public Date getFromDate() {
        return from;
    }
    
    public Date getToDate() {
		return to;
	}

    public long getValue() {
        return value;
    }

    public long getDays() {
        return days;
    }

    private long calculateDays() {
        return ((this.to.getTime() - this.from.getTime()) / (24 * 60 * 60 * 1000));
    }
    
    private long calculateValue() {
        return this.days * this.rate;
    }
}