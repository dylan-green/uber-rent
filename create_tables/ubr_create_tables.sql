create table customer (
	customer_phone integer not null PRIMARY KEY,
	customer_name varchar(20) not null,
	customer_addr varchar(50) not null,
    customer_city varchar(20) not null,
	customer_dlnum integer not null,
    member_points integer
);

create table rent (
    rent_id integer not null PRIMARY KEY
);

create table rent_return (
    return_id integer not null PRIMARY KEY,
    return_date date not null,
    return_time varchar(5) not null,
    return_odometer integer not null,
    return_value integer not null,
    foreign key (return_id) references rent
);

commit;