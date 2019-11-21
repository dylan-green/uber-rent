
/* Branches */

INSERT INTO branch VALUES ('20 Sunset Boulevard','Los Angeles');
INSERT INTO branch VALUES ('2020 5th Avenue','New York');
INSERT INTO branch VALUES ('775 Chipeta Drive','Ridgway');
INSERT INTO branch VALUES ('3256 Wright Avenue','Boulder');
INSERT INTO branch VALUES ('6520 Arabella Drive','Keystone');
INSERT INTO branch VALUES ('5238 Somerville Street','Vancouver');
INSERT INTO branch VALUES ('77 Rokewood Avenue','Stellenbosch');


/* Populate the Customer table */
INSERT INTO customer VALUES (5551234, 'John', '222 Strawberry Fields', 'Leicester', 12938);
INSERT INTO customer VALUES (5552331, 'Paul', '15 Abbey Road', 'London', 56731);
INSERT INTO customer VALUES (5552511, 'Ringo', '251 Manchester Way', 'Liverpool', 85847);
INSERT INTO customer VALUES (5551342, 'George', '8 Maxwell Drive', 'Sussex', 47292);
INSERT INTO customer VALUES (5559705, 'Justin Timberlake', '20 Timberlake Rd', 'Babeville', 39483);
INSERT INTO customer VALUES (5550293, 'Will Smith', '10 Hollywood Blvd', 'Bel Air', 60594);
INSERT INTO customer VALUES (0405932, 'Nick Carter', '19 Venice Blvd', 'Santa Monica', 16049);
INSERT INTO customer VALUES (8392855, 'Aaron Carter', 'Aarons House', 'Los Angeles', 20405);
INSERT INTO customer VALUES (0897829, 'Hillary Duff', 'Aarons House', 'Los Angeles', 12345);
INSERT INTO customer VALUES (9705938, 'Lance Bass', '12 Washup Lane', 'New York', 48594);
INSERT INTO customer VALUES (2817594, 'Joey Fatone', '1515 NSync Drive', 'Nashville', 69504);
INSERT INTO customer VALUES (2039844, 'JC Chasez', '1515 NSync Drive', 'Nashville', 16958);

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
    'A',
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
    'A',
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
    'A',
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
    'A',
    'SUV',
    '20 Sunset Boulevard',
    'Los Angeles'
);

/* RENT */
/* rent_id integer, vid integer, cell integer fromdate DATE */
INSERT INTO rent VALUES (
    1,
    89898989,
    5551234,
    TO_DATE('1/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('3/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    2500,
    'Dale Cooper',
    19127,
    0820,
    111222
);

INSERT INTO rent VALUES (
    2,
    102030405,
    5552331,
    TO_DATE('5/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('9/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    1200,
    'Laura Palmer',
    19387,
    0820,
    222333
);

INSERT INTO rent VALUES (
    3,
    111222345,
    5552511,
    TO_DATE('10/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('12/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    10298,
    'BOB',
    79357,
    0820,
    111222
);

INSERT INTO rent VALUES (
    4,
    99999999,
    5551342,
    TO_DATE('14/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('19/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    7831,
    'Gordon Cole',
    13235,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    5,
    89898989,
    5550293,
    TO_DATE('19/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('20/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    3219,
    'Dougie Jones',
    19352,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    6,
    99999999,
    5559705,
    TO_DATE('25/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('28/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    37190,
    'Janey-E Jones',
    19357,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    7,
    89898989,
    0405932,
    TO_DATE('28/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    TO_DATE('30/11/2019','DD/MM/YYYY'),
    TO_TIMESTAMP( '23/12/2011 13:01', 'DD/MM/YYYY HH24:MI'),
    90000,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

COMMIT;