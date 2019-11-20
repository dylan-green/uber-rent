package model;


public class Rent {

    private final int rentID;
    private final int vid;
    private final int cellphone;
    private final int odometer;
    private final String cardname;
    private final long cardnum;
    private final int expdate;
    private final int confnum;

    public Rent(
            int rentId,
            int vid,
            int cellphone,
            int odometer,
            String cardname,
            long cardnum,
            int expdate,
            int confnum) {
        this.rentID = rentId;
        this.vid = vid;
        this.cellphone = cellphone;
        this.odometer = odometer;
        this.cardname = cardname;
        this.cardnum = cardnum;
        this.expdate = expdate;
        this.confnum = confnum;
    }

    public int getRentId() {
        return this.rentID;
    }

    public int getVid() {
        return this.vid;
    }

	public int getRentID() {
		return rentID;
	}

	public int getCellphone() {
		return cellphone;
	}

	public int getOdometer() {
		return odometer;
	}

	public String getCardname() {
		return cardname;
	}

	public long getCardnum() {
		return cardnum;
	}

	public int getExpdate() {
		return expdate;
	}

	public int getConfnum() {
		return confnum;
	}

}