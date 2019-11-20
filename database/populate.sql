
/* Branches */

INSERT INTO branch VALUES ('20 Sunset Boulevard','Los Angeles');
INSERT INTO branch VALUES ('2020 5th Avenue','New York');
INSERT INTO branch VALUES ('775 Chipeta Drive','Ridgway');
INSERT INTO branch VALUES ('3256 Wright Avenue','Boulder');
INSERT INTO branch VALUES ('6520 Arabella Drive','Keystone');
INSERT INTO branch VALUES ('5238 Somerville Street','Vancouver');


/* Populate the Customer table */
INSERT INTO customer VALUES (5551234, 'John', '222 Strawberry Fields', 'Leicester', 12938, 'F', 0);
INSERT INTO customer VALUES (5552331, 'Paul', '15 Abbey Road', 'London', 56731, 'F', 0);
INSERT INTO customer VALUES (5552511, 'Ringo', '251 Manchester Way', 'Liverpool', 85847, 'F', 0);
INSERT INTO customer VALUES (5551342, 'George', '8 Maxwell Drive', 'Sussex', 47292, 'F', 0);
INSERT INTO customer VALUES (5559705, 'Justin Timberlake', '20 Timberlake Rd', 'Babeville', 39483, 'F', 0);
INSERT INTO customer VALUES (5550293, 'Will Smith', '10 Hollywood Blvd', 'Bel Air', 60594, 'F', 0);
INSERT INTO customer VALUES (0405932, 'Nick Carter', '19 Venice Blvd', 'Santa Monica', 16049, 'F', 0);
INSERT INTO customer VALUES (8392855, 'Aaron Carter', 'Aarons House', 'Los Angeles', 20405, 'F', 0);
INSERT INTO customer VALUES (0897829, 'Hillary Duff', 'Aarons House', 'Los Angeles', 12345, 'F', 0);
INSERT INTO customer VALUES (9705938, 'Lance Bass', '12 Washup Lane', 'New York', 48594, 'F', 0);
INSERT INTO customer VALUES (2817594, 'Joey Fatone', '1515 NSync Drive', 'Nashville', 69504, 'F', 0);
INSERT INTO customer VALUES (2039844, 'JC Chasez', '1515 NSync Drive', 'Nashville', 16958, 'F', 0);

/* vehicle types */
INSERT INTO vehicletype VALUES('Truck',NULL,600,200,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Sedan',NULL,400,100,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Hatchback',NULL,400,100,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Coupe',NULL,400,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Wagon',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Van',NULL,600,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Hybrid',NULL,400,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Convertible',NULL,550,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('SUV',NULL,550,150,45,50,20,5,50);

INSERT INTO reservation VALUES(
    111222,
    'Hybrid',
    5551234,
    TO_DATE('19/11/2019','DD/MM/YYYY'),
    CURRENT_TIMESTAMP,
    TO_DATE('30/11/2019', 'DD/MM/YYYY'),
    CURRENT_TIMESTAMP
);

INSERT INTO reservation VALUES(
    222333,
    'Truck',
    5552511,
    TO_DATE('19/11/2019','DD/MM/YYYY'),
    CURRENT_TIMESTAMP,
    TO_DATE('30/11/2019', 'DD/MM/YYYY'),
    CURRENT_TIMESTAMP
);

INSERT INTO reservation VALUES(
    333444,
    'Sedan',
    5550293,
    TO_DATE('19/11/2019','DD/MM/YYYY'),
    CURRENT_TIMESTAMP,
    TO_DATE('30/11/2019', 'DD/MM/YYYY'),
    CURRENT_TIMESTAMP
);

/* vstatus, */
INSERT INTO vehicle VALUES (
    102030405,
    '989RNL',
    'Mazda',
    'Mazda3 Sport',
    2012,
    'white',
    104000,
    'Hatchback',
    '5238 Somerville Street',
    'Vancouver'
);

INSERT INTO vehicle VALUES (
    111222345,
    '844TYJ',
    'Volkswagen',
    'Jetta GTI',
    2001,
    'silver',
    200000,
    'Sedan',
    '6520 Arabella Drive',
    'Keystone'
);

INSERT INTO vehicle VALUES (
    99999999,
    '887BRO',
    'Jeep',
    'Liberty T',
    2000,
    'white',
    220000,
    'SUV',
    '6520 Arabella Drive',
    'Keystone'
);

INSERT INTO vehicle VALUES (
    89898989,
    'AXH29G',
    'Nissan',
    'X-Trail',
    2004,
    'tan',
    400000,
    'SUV',
    '20 Sunset Boulevard',
    'Los Angeles'
);

COMMIT;
