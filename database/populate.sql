
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
INSERT INTO vehicletype VALUES('Truck',NULL,600);
INSERT INTO vehicletype VALUES('Sedan',NULL,400);
INSERT INTO vehicletype VALUES('Hatchback',NULL,400);
INSERT INTO vehicletype VALUES('Coupe',NULL,400);
INSERT INTO vehicletype VALUES('Wagon',NULL,500);
INSERT INTO vehicletype VALUES('Van',NULL,600);
INSERT INTO vehicletype VALUES('Hybrid',NULL,400);
INSERT INTO vehicletype VALUES('Convertible',NULL,550);
INSERT INTO vehicletype VALUES('SUV',NULL,550);

INSERT INTO reservation 
VALUES(111222,'SUV',5551234,TO_DATE('19/11/2019','DD/MM/YYYY'),TO_DATE('30/11/2019','DD/MM/YYYY'));

INSERT INTO reservation 
VALUES(222333,'Hatchback',5552511,TO_DATE('19/11/2019','DD/MM/YYYY'),TO_DATE('30/11/2019','DD/MM/YYYY'));

INSERT INTO reservation 
VALUES(333444,'Sedan',5550293,TO_DATE('19/11/2019','DD/MM/YYYY'),TO_DATE('30/11/2019','DD/MM/YYYY'));

INSERT INTO reservation 
VALUES(123456,'Hybrid',2817594,TO_DATE('10/11/2019','DD/MM/YYYY'),TO_DATE('20/11/2019','DD/MM/YYYY'));

INSERT INTO vehicle VALUES (
    384957,
    '989RNL',
    'Mazda',
    'Mazda3 Sport',
    2012,
    'white',
    'A',
    'Hatchback',
    '5238 Somerville Street',
    'Vancouver'
);

INSERT INTO vehicle VALUES (
    504938,
    '844TYJ',
    'Volkswagen',
    'Jetta GTI',
    2001,
    'silver',
    'A',
    'Sedan',
    '6520 Arabella Drive',
    'Keystone'
);

INSERT INTO vehicle VALUES (
    493027,
    '887BRO',
    'Jeep',
    'Liberty T',
    2000,
    'white',
    'A',
    'SUV',
    '6520 Arabella Drive',
    'Keystone'
);

INSERT INTO vehicle VALUES (
    593028,
    'AXH29G',
    'Nissan',
    'X-Trail',
    2004,
    'tan',
    'A',
    'SUV',
    '20 Sunset Boulevard',
    'Los Angeles'
);

INSERT INTO vehicle VALUES (
    794029,
    'AXH29G',
    'Nissan',
    '370Z Roadster',
    2004,
    'red',
    'N',
    'Convertible',
    '20 Sunset Boulevard',
    'Los Angeles'
);

/* RENT */
/* rent_id integer, vid integer, cell integer fromdate DATE */
INSERT INTO rent VALUES (
    1,
    593028,
    5551234,
    TO_DATE('1/11/2019','DD/MM/YYYY'),
    NULL,
    'Dale Cooper',
    19127,
    0820,
    111222
);

INSERT INTO rent VALUES (
    2,
    504938,
    5552331,
    TO_DATE('5/11/2019','DD/MM/YYYY'),
    NULL,
    'Laura Palmer',
    19387,
    0820,
    222333
);

INSERT INTO rent VALUES (
    3,
    593028,
    5552511,
    TO_DATE('10/11/2019','DD/MM/YYYY'),
    NULL,
    'BOB',
    79357,
    0820,
    111222
);

INSERT INTO rent VALUES (
    4,
    504938,
    5551342,
    TO_DATE('14/11/2019','DD/MM/YYYY'),
    NULL,
    'Gordon Cole',
    13235,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    5,
    493027,
    5550293,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    'Dougie Jones',
    19352,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    6,
    493027,
    5559705,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    'Janey-E Jones',
    19357,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    7,
    384957,
    0405932,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    90000,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

-- -- /* addition to populate * /
-- -- /* add more vehicles (toyotas) */
INSERT INTO vehicle VALUES (11111113, 'AAA333', 'toyota', 'camry', 2019, 'sliver', 'A', 'Convertible', '5238 Somerville Street', 'Vancouver');
INSERT INTO vehicle VALUES (11111114, 'AAA444', 'toyota', 'prius', 2019, 'black', 'A', 'Hybrid', '6520 Arabella Drive', 'Keystone');
INSERT INTO vehicle VALUES (11111115, 'AAA555', 'toyota', 'sienna', 2019, 'white', 'A', 'SUV', '3256 Wright Avenue', 'Boulder');
INSERT INTO vehicle VALUES (11111116, 'AAA666', 'toyota', 'avalon', 2019, 'white', 'A', 'Hatchback', '5238 Somerville Street', 'Vancouver');
INSERT INTO vehicle VALUES (11111117, 'AAA888', 'toyota', 'yaris', 2019, 'blue', 'A', 'Hatchback', '6520 Arabella Drive', 'Keystone');
INSERT INTO vehicle VALUES (11111118, 'AAA999', 'toyota', 'camry', 2019, 'black', 'A', 'Hatchback', '2020 5th Avenue', 'New York');
INSERT INTO vehicle VALUES (11111119, 'BBB111', 'toyota', 'prius', 2019, 'black', 'A', 'Hybrid', '3256 Wright Avenue', 'Boulder');
INSERT INTO vehicle VALUES (11111110, 'BBB222', 'toyota', 'yaris', 2019, 'blue', 'A', 'Convertible', '3256 Wright Avenue', 'Boulder');
INSERT INTO vehicle VALUES (11111121, 'BBB333', 'toyota', 'yaris', 2019,'white', 'A', 'Convertible', '2020 5th Avenue', 'New York');
INSERT INTO vehicle VALUES (11111122, 'CCC555', 'toyota', 'yaris', 2019, 'red', 'A', 'Convertible', '3256 Wright Avenue', 'Boulder');
INSERT INTO vehicle VALUES (11111123, 'CCC666', 'toyota', 'camry', 2019, 'blue', 'A', 'Convertible', '5238 Somerville Street', 'Vancouver');
INSERT INTO vehicle VALUES (11111124, 'CCC888', 'toyota', 'prius', 2019, 'sliver', 'A', 'Hybrid', '2020 5th Avenue', 'New York');
INSERT INTO vehicle VALUES (11111125, 'CCC999', 'toyota', 'sienna', 2019, 'white', 'A', 'Convertible', '2020 5th Avenue', 'New York');
INSERT INTO vehicle VALUES (11111126, 'CCC111', 'toyota', 'prius', 2019, 'red', 'A', 'Hybrid', '6520 Arabella Drive', 'Keystone');

-- /* add more rents */

INSERT INTO rent VALUES (
    8,
    11111121,
    0405932,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    90000,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    9,
    11111118,
    0405932,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    90000,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    10,
    11111117,
    0405932,
    TO_DATE('18/11/2019','DD/MM/YYYY'),
    NULL,
    90000,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    11,
    11111113,
    5551234,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    2500,
    'Dale Cooper',
    19127,
    0820,
    111222
);

INSERT INTO rent VALUES (
    12,
    11111114,
    5552331,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    1200,
    'Laura Palmer',
    19387,
    0820,
    222333
);

INSERT INTO rent VALUES (
    13,
    111222345,
    5552511,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    10298,
    'BOB',
    79357,
    0820,
    111222
);

INSERT INTO rent VALUES (
    14,
    11111117,
    5551342,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    7831,
    'Gordon Cole',
    13235,
    0820,
    NULL
);


INSERT INTO rent VALUES (
    15,
    11111113,
    5551234,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    2500,
    'Dale Cooper',
    19127,
    0820,
    111222
);

INSERT INTO rent VALUES (
    16,
    11111114,
    5552331,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    1200,
    'Laura Palmer',
    19387,
    0820,
    222333
);

INSERT INTO rent VALUES (
    17,
    111222345,
    5552511,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    10298,
    'BOB',
    79357,
    0820,
    111222
);

INSERT INTO rent VALUES (
    18,
    11111117,
    5551342,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    7831,
    'Gordon Cole',
    13235,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    19,
    11111118,
    5550293,
    TO_DATE('2/11/2019','DD/MM/YYYY'),
    NULL,
    3219,
    'Dougie Jones',
    19352,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    20,
    11111121,
    5559705,
    TO_DATE('3/11/2019','DD/MM/YYYY'),
    NULL,
    37190,
    'Janey-E Jones',
    19357,
    0820,
    NULL
);

INSERT INTO rent VALUES (
    21,
    11111119,
    0405932,
    TO_DATE('3/11/2019','DD/MM/YYYY'),
    NULL,
    'Shelly Johnson',
    1235261782,
    0820,
    NULL
);

COMMIT;