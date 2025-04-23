create table if not exists passengers (
    id int GENERATED ALWAYS AS IDENTITY primary key,
    name varchar(20) not null,
    surname varchar(30) not null,
    email varchar(40) not null,
    phoneNumber char(9) not null
);


create table if not exists flights (
    id int GENERATED ALWAYS AS IDENTITY primary key,
    departure varchar(50) not null,
    destination varchar(50) not null,
    departureDate timestamp not null,
    duration int not null,
    seatRowsAmount integer not null
);


create table if not exists seats (
    flightId int not null,
    seatNumber varchar(4) not null,
    available boolean not null,
    primary key(flightId, seatNumber),
    constraint seats_flight_fk foreign key (flightId) references flights(id) on delete cascade
);


create table if not exists reservations (
    id int GENERATED ALWAYS AS IDENTITY primary key,
    flightId int not null,
    passengerId int not null,
    seatNumber varchar(4) not null,
    constraint reservations_flight_fk foreign key (flightId) references flights(id) on delete cascade,
    constraint reservations_passenger_fk foreign key (passengerId) references passengers(id) on delete cascade,
    constraint reservations_seat_fk foreign key (flightId, seatNumber) references seats(flightId, seatNumber) on delete cascade,
    constraint reservations_flight_seat_unique unique (flightId, seatNumber)
);


CREATE TRIGGER if not exists reservation_deleted
AFTER DELETE ON reservations
FOR EACH ROW
CALL "lot.database.triggers.MakeSeatAvailableTrigger";

create trigger if not exists reservation_added
after insert on reservations
for each row
call "lot.database.triggers.MakeSeatUnavailableTrigger";

create trigger if not exists reservation_updated
after update on reservations
for each row
call "lot.database.triggers.ManageSeatAvailabilityAfterUpdateTrigger";
