
/* Populate the Customer table */
INSERT INTO customer VALUES (5551234, 'John', '222 Strawberry Fields', 'Leicester', 12938, 'F', 0);
INSERT INTO customer VALUES (5552331, 'Paul', '15 Abbey Road', 'London', 56731, 'F', 0);
INSERT INTO customer VALUES (5552511, 'Ringo', '251 Manchester Way', 'Liverpool', 85847, 'F', 0);
INSERT INTO customer VALUES (5551342, 'George', '8 Maxwell Drive', 'Sussex', 47292, 'F', 0);
INSERT INTO customer VALUES (5559705, 'Justin', '20 Timberlake Rd', 'Babeville', 39483, 'F', 0);
INSERT INTO customer VALUES (5550293, 'Will', '10 Hollywood Blvd', 'Bel Air', 60594, 'F', 0);

/* vehicle types */
INSERT INTO vehicletype VALUES('Truck',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Sedan',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Hatchback',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Coupe',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Wagon',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Van',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Hybrid',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('Convertible',NULL,500,150,45,50,20,5,50);
INSERT INTO vehicletype VALUES('SUV',NULL,500,150,45,50,20,5,50);

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

COMMIT;
