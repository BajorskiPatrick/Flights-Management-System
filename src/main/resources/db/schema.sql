create table if not exists passengers (
    id integer GENERATED ALWAYS AS IDENTITY primary key,
    name varchar(20) not null,
    surname varchar(30) not null,
    email varchar(40) not null,
    phoneNumber varchar(12) not null
);


create table if not exists flights (
    id integer GENERATED ALWAYS AS IDENTITY primary key,
    source varchar(50) not null,
    destination varchar(50) not null,
    duration integer not null,
    twoWay boolean not null
);


create table if not exists reservations (
    id integer GENERATED ALWAYS AS IDENTITY primary key,
    flightId integer not null,
    passengerId integer not null,
    seatNumber varchar(4) not null,
    tookPlace boolean not null,
    constraint reservations_flight_fk foreign key (flightId) references flights(id),
    constraint reservations_passenger_fk foreign key (passengerId) references passengers(id)
);


create table if not exists seats (
    flightId integer not null,
    seatNumber varchar(4) not null,
    available boolean not null,
    primary key(flightId, seatNumber),
    constraint seats_flight_fk foreign key (flightId) references flights(id)
);

