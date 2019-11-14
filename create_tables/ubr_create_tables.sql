CREATE TABLE customer (
	customer_phone INTEGER NOT NULL PRIMARY KEY,
	customer_name VARCHAR(20) NOT NULL,
	customer_addr VARCHAR(50) NOT NULL,
    customer_city VARCHAR(20) NOT NULL,
	customer_dlnum INTEGER NOT NULL,
    is_club_member VARCHAR(1) DEFAULT 'F',
    member_points INTEGER DEFAULT 0
);

CREATE TABLE rent (
    rent_id INTEGER NOT NULL PRIMARY KEY
);

CREATE TABLE rent_return (
    return_id INTEGER NOT NULL PRIMARY KEY,
    return_date DATE NOT NULL,
    return_time TIMESTAMP NOT NULL,
    return_odometer INTEGER NOT NULL,
    return_value INTEGER NOT NULL,
    FOREIGN KEY (return_id) REFERENCES rent
);

COMMIT;