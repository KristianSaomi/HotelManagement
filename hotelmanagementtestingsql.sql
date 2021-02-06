-- DROP DATABASE HotelManagementTest;

CREATE DATABASE HotelManagementTest;
USE HotelManagementTest;

Create table Menu(
itemId INT PRIMARY KEY NOT NULL,
item CHAR(150) NOT NULL,
itemPrice INT NOT NULL default 0
);

INSERT INTO Menu(itemId, item, itemPrice)VALUE
(0,"Welcome drink",0),
(1,"Soda",40),
(2,"Rosa Prosecco - Champagne",400),
(3, "Röd vin",280),
(4,"Tea / coffé",50),
(5,"Mango / Ananas / Strawberry / Lemon - Fresh juice",180),
(6,"Pasta med bacon & champinjoner i krämig sås",300),
(7,"Stekt lax med citronsås & kokt potatis",380),
(8,"Kycklingfilé planka, mozarella, rödvinsås & stekta grönsaker",270),
(9,"Ärtsoppa , pannkaka med sylt & grädde",250),
(10,"Löv biff - persiljesmör & stekta grönsaker",300
);
 
Create table Customer(
customerId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
firstName CHAR(150) NOT NULL,
lastName CHAR(150) NOT NULL,
ssn CHAR(150) NOT NULL,
email CHAR(150) NOT NULL,
phone CHAR(15) NOT NULL,
bankinfo CHAR (40) NOT NULL,
totalGuest int not null
)AUTO_INCREMENT = 1; 

INSERT INTO Customer(firstName, lastName, ssn, email, phone, bankinfo, totalGuest)VALUE
("Lisa","Saomi","19930102-1334","Lisa@Gmail.com","+46735642214", "7845-2314",2),
("Felicia","Hellman","19981207-2313","Felicia@gmail.com","+46735642236","7845-7771",1),
("Alexander","Andersson","19961207-2314","Alexander@gmail.com","+46732142214","7845-2323",1),
("Robin","Ericsson","19851207-2315","Robin@gmail.com","+46735653214", "7845-5541",1
);
select* from customer;

CREATE table Purchase (
purchasedId INT PRIMARY KEY auto_increment NOT NULL,
customerId int NOT NULL,
itemId int not null,
FOREIGN KEY (customerId) REFERENCES Customer(customerId),
FOREIGN KEY (itemId) REFERENCES Menu(itemId)
)auto_increment = 1;

INSERT INTO Purchase(customerId,itemId)VALUE
(1,1),
(1,1),
(1,1),
(1,1),
(2,2),
(1,0),
(2,0),
(3,0),
(4,0),
(2,1
);

select * from purchase;

Create table RoomInfo(
roomId INT PRIMARY KEY NOT NULL,
roomType CHAR(200) NOT NULL,
roomInfo char(200) not null,
roomPrice int not null
);

Select * from RoomInfo;

INSERT INTO RoomInfo(roomId, roomType,roomInfo, roomPrice)VALUE
(1,"Superior","Double and 2 singel beds, livingroom with Led 85' Tv(All channels), a luxury bathroom with spa, 1 minibar and balcony with view",2000),
(2,"Deluxe","Double bed, a luxury bathroom, Led 55', 1 minibar, small balcony",1500),
(3,"Standard","Singel beds or 1 dubble bed, minibar, small cozy bathroom, small balcony",1000
);

Create table RoomToRent(
roomNumber INT PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
roomId int NOT NULL,
FOREIGN KEY (roomId) REFERENCES roomInfo(roomId)
)AUTO_INCREMENT = 200;

INSERT INTO RoomToRent(roomNumber, roomId)VALUE
(200,1),
(201,1),
(202,1),
(203,2),
(204,2),
(205,2),
(206,2),
(207,3),
(208,3),
(209,3),
(210,3),
(211,3
);

Create table Package(
PackageId INT PRIMARY KEY NOT NULL,
packageType CHAR(150) NOT NULL,
packageInstractions CHAR(150) not null,
packagePrice int not null
); 

INSERT INTO Package(PackageId, packageType, packageInstractions, packagePrice)VALUE
(1,"All inclusive","Free access",1000),
(2,"Halvpension","3 meal & 3 drinks per day",750),
(3,"Free breakfast","Breakfast",500
);

select * from package;

Create table Bookings(
rentId INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
customerId int not null,
roomNumber int not null UNIQUE ,
numberOfNight int not null,
packageId int not null,
checkIn DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
checkOut Char(150) not null DEFAULT 'Pending',
FOREIGN KEY (packageId) REFERENCES Package(packageId),
FOREIGN KEY (customerId) REFERENCES Customer(customerId),
FOREIGN KEY (roomNumber) REFERENCES RoomToRent(roomNumber)
)AUTO_INCREMENT = 1;

INSERT INTO Bookings(customerId, roomNumber, numberOfNight, packageId)VALUE
(1,200,2,1),
(2,208,2,2),
(3,205,2,3),
(4,202,2,3
); 
select * from bookings;

-- Recent customer ID -- 
CREATE VIEW customerIdRecent AS
SELECT
customerId
FROM Customer
ORDER BY customerId DESC
LIMIT 1
;
select * from customerIdRecent; 
-- Create view for recipte --
-- create VIEW test as
-- SELECT 
-- purchase.customerId,bookings.roomnumber, sum(menu.itemPrice) as 'ItemsBought',roomInfo.roomPrice as 'roomprice',Package.packagePrice as 'packageprice', bookings.numberOfNight as numberOfNights, customer.totalGuest as TOTALGUESTS, ((((roomprice + packageprice) * bookings.numberOfNight) * customer.totalguest) + sum(menu.itemPrice)) as 'TOTALRECEIPT'
-- from purchase
-- JOIN bookings on bookings.customerId = purchase.customerId
-- JOIN menu on menu.itemId = purchase.itemId
-- jOIN ROOMTORENT ON ROOMTORENT.ROOMNUMBER = BOOKINGS.ROOMNUMBER
-- JOIN ROOMINFO ON ROOMINFO.ROOMID = ROOMTORENT.ROOMID
-- JOIN PACKAGE ON PACKAGE.PACKAGEID = BOOKINGS.PACKAGEID
-- join customer on customer.customerid = bookings.customerid
-- GROUP by purchase.customerId;
-- select * from test;

create view testar as
select bookings.customerId, 
package.packagePrice, roominfo.roomPrice,bookings.numberofnight, customer.totalGuest,
(((roomprice + packageprice) * bookings.numberOfNight) * customer.totalguest) as 'HotelPayments', sum(menu.itemPrice) as 'ExtraPayment',
(((roomprice + packageprice) * bookings.numberOfNight) * customer.totalguest + sum(menu.itemPrice)) 'TotalPayments'
from Customer
JOIN bookings on bookings.customerId = customer.CustomerId
left JOIN purchase on purchase.customerId = customer.customerId
left Join menu on menu.itemId = purchase.itemId
JOIN package on package.packageId = bookings.packageId
JOIN roomToRent on roomToRent.roomNumber = bookings.roomNumber
JOIN roominfo on roominfo.roomId = roomToRent.roomId
group by bookings.customerid
;
select * from testar;

create view test2 as 
select 
roomtorent.roomNumber, roominfo.roomType
from roomtorent
join roominfo on roominfo.roomid = roomtorent.roomid
left join bookings on bookings.roomNumber = roomtorent.roomNumber
WHERE bookings.checkIn IS NULL OR bookings.checkOut LIKE '2%' ;
select * from test2;

Create view test3 as 
select
concat(customer.firstname, " ", customer.lastName) as Name,
customer.customerId AS CustomerID,
bookings.roomNumber AS RoomNumber, bookings.numberofnight AS NumberOfNights, 
bookings.packageid AS PackageId, customer.totalguest as TotalGuests, 
bookings.checkin AS CheckIn, bookings.checkout AS CheckOut, testar.TotalPayments
from bookings
join testar on bookings.customerId = testar.customerId
join customer on customer.customerid = bookings.customerid;
select * from test3;

alter view test4 as
select
bookings.customerid, bookings.roomNumber
from purchase
join bookings on bookings.customerId = purchase.customerId;
select * from test4;
