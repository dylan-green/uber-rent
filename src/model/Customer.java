package model;

public class Customer {

    private final int dlnum;
    private final String customer_name;
    private final String customer_addr;
    private final String customer_city;
    private final int cellphone;

    public Customer(
            int dlnum,
            String customer_name,
            String customer_addr,
            String customer_city,
            int cellphone) {
                
        this.dlnum = dlnum;
        this.customer_name = customer_name;
        this.customer_addr = customer_addr;
        this.customer_city = customer_city;
        this.cellphone = cellphone;
    }

    public int getDlnum() {
        return this.dlnum;
    }

	public String getCustomerName() {
		return this.customer_name;
	}

	public String getCustomerAddr() {
		return this.customer_addr;
	}

	public String getCustomerCity() {
		return this.customer_city;
	}

	public int getCellphone() {
		return this.cellphone;
	}

}