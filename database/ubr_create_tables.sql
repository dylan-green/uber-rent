CREATE TABLE customer (
	dlnum INTEGER NOT NULL PRIMARY KEY,
	customer_name VARCHAR(20) NOT NULL,
	customer_addr VARCHAR(50) NOT NULL,
    customer_city VARCHAR(20) NOT NULL,
	cellphone INTEGER NOT NULL
);

CREATE TABLE vehicletype (
    vtname VARCHAR(20) NOT NULL PRIMARY KEY,
    features VARCHAR(10),
    day_rate INTEGER NOT NULL
);

CREATE TABLE reservation (
    confnum INTEGER NOT NULL PRIMARY KEY,
    vtname VARCHAR(20) NOT NULL,
    cust_dlnum INTEGER NOT NULL,
    fromDate DATE NOT NULL,
    fromTime TIMESTAMP NOT NULL,
    toDate DATE NOT NULL,
    toTime TIMESTAMP NOT NULL,
    FOREIGN KEY (vtname) REFERENCES vehicletype,
    FOREIGN KEY (cust_dlnum) REFERENCES customer
);

CREATE TABLE branch (
    b_location VARCHAR(25) NOT NULL,
    city VARCHAR(15) NOT NULL,
    PRIMARY KEY (b_location, city)
);

CREATE TABLE vehicle (
    vid INTEGER NOT NULL PRIMARY KEY,
    vlicense VARCHAR(6) NOT NULL,
    make VARCHAR(10) NOT NULL,
    model VARCHAR(15) NOT NULL,
    year INTEGER NOT NULL,
    color VARCHAR(10) NOT NULL,
    odometer INTEGER NOT NULL,
    vstatus VARCHAR(1) DEFAULT 'A',
    vtname VARCHAR(20) NOT NULL,
    b_location VARCHAR(25) NOT NULL,
    city VARCHAR(15) NOT NULL,
    FOREIGN KEY (vtname) REFERENCES vehicletype, 
    FOREIGN KEY (b_location, city) REFERENCES branch
);

CREATE TABLE rent (
    rent_id INTEGER NOT NULL PRIMARY KEY,
    vid INTEGER NOT NULL,
    cust_dlnum INTEGER NOT NULL,
    fromDate DATE NOT NULL,
    toDate DATE,
    odometer INTEGER NOT NULL,
    cardName VARCHAR(20) NOT NULL,
    cardNo INTEGER NOT NULL,
    expDate INTEGER NOT NULL,
    confnum INTEGER,
    FOREIGN KEY (vid) REFERENCES vehicle,
    FOREIGN KEY (cust_dlnum) REFERENCES customer,
    FOREIGN KEY (confnum) REFERENCES reservation
);

CREATE TABLE rent_return (
    return_id INTEGER NOT NULL PRIMARY KEY,
    return_date DATE NOT NULL,
    return_value INTEGER NOT NULL,
    FOREIGN KEY (return_id) REFERENCES rent
);

COMMIT;