INSERT INTO customer VALUES (5551234, 'John', '222 Strawberry Fields', 'Leicester', 12938, 'F', 0);
INSERT INTO customer VALUES (5552331, 'Paul', '15 Abbey Road', 'London', 56731, 'F', 0);
INSERT INTO customer VALUES (5552511, 'Ringo', '251 Manchester Way', 'Liverpool', 85847, 'F', 0);
INSERT INTO customer VALUES (5551342, 'George', '8 Maxwell Drive', 'Sussex', 47292, 'F', 0);

INSERT INTO customer VALUES (5559705, 'Justin', '20 Timberlake Rd', 'Babeville', 39483, 'F', 0);
INSERT INTO customer VALUES (5550293, 'Will', '10 Hollywood Blvd', 'Bel Air', 60594, 'F', 0);

/*remove vtstatus? fix branch_location vs city*/
INSERT INTO vehicle VALUES (1, "AAA111", "toyota", "yaris", 2019, "blue", 2000, "available", "economy", "south", "vancouver");
INSERT INTO vehicle VALUES (2, "AAA222", "toyota", "echo", 2019, "red", 10000, "available", "compact", "east", "vancouver");
INSERT INTO vehicle VALUES (3, "AAA333", "toyota", "camry", 2019, "sliver", 10000, "available", "compact", "north", "vancouver");
INSERT INTO vehicle VALUES (4, "AAA444", "toyota", "prius", 2019, "black", 10000, "available", "hybrid", "west", "vancouver");
INSERT INTO vehicle VALUES (5, "AAA555", "toyota", "sienna", 2019, "white", 15000, "available", "suv", "east", "vancouver");
INSERT INTO vehicle VALUES (6, "AAA666", "toyota", "avalon", 2019, "white", 1500, "available", "economy", "north", "vancouver");
INSERT INTO vehicle VALUES (7, "AAA888", "toyota", "yaris", 2019, "blue", 4000, "available", "economy", "west", "vancouver");
INSERT INTO vehicle VALUES (8, "AAA999", "toyota", "camry", 2019, "black", 12000, "available", "economy", "south", "vancouver");
INSERT INTO vehicle VALUES (9, "BBB111", "toyota", "prius", 2019, "black", 80000, "available", "hybrid", "east", "vancouver");
INSERT INTO vehicle VALUES (10, "BBB222", "toyota", "yaris", 2019, "blue", 12000, "available", "compact", "east", "vancouver");
INSERT INTO vehicle VALUES (11, "BBB333", "toyota", "yaris", 2019,"white", 12000, "available", "compact", "south", "vancouver");
INSERT INTO vehicle VALUES (12, "CCC555", "toyota", "yaris", 2019, "red", 7800, "available", "compact", "east", "vancouver");
INSERT INTO vehicle VALUES (13, "CCC666", "toyota", "camry", 2019, "blue", 4000, "available", "compact", "north", "vancouver");
INSERT INTO vehicle VALUES (14, "CCC888", "toyota", "prius", 2019, "sliver", 2000, "available", "hybrid", "noth", "vancouver");
INSERT INTO vehicle VALUES (15, "CCC999", "toyota", "sienna", 2019, "white", 8000, "available", "compact", "south", "vancouver");
INSERT INTO vehicle VALUES (16, "CCC111", "toyota", "prius", 2019, "red", 10000, "available", "hybrid", "west", "vancouver");v
INSERT INTO vehicle VALUES (17, "DDD111", "toyota", "avalon", 2019, "white", 12000, "available", "compact", "west", "vancouver");
INSERT INTO vehicle VALUES (18, "EEE111", "toyota", "sienna", 2019, "sliver", 142000, "available", "suv", "west", "vancouver");

INSERT INTO vehicletype VALUES ("economy", "keyless entry", 100, 25, 8, 80, 30, 50, 10);
INSERT INTO vehicletype VALUES ("compact", "keyless entry", 100, 25, 8, 80, 30, 50, 10);
INSERT INTO vehicletype VALUES ("hybrid", "electricity", 120, 28, 10, 80, 30, 50, 10);
INSERT INTO vehicletype VALUES ("suv", "keyless entry", 100, 25, 8, 80, 30, 50, 10);

COMMIT;
