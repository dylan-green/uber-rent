package model;

import java.sql.Date;

public class ReturnModel {
    private final int returnId;
    private final long value;
    private final int rate;
    private final Date from;
    private final Date to;
    private long days;
    
    public ReturnModel(int returnId, Date from, Date to, int rate) {
        this.returnId = returnId;
        this.from = to;
        this.to = to;
        this.rate = rate;
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
    
    private long calculateValue() {
        System.out.println("FROM: " + from);
        System.out.println("TO: " + to);
        this.days = ((to.getTime() - from.getTime()) / (24 * 60 * 60 * 1000));
        return this.days * this.rate;
    }
}