INSERT INTO flights (departure, destination, departureDate, duration, twoWay) VALUES
    ('Warszawa', 'Berlin','2025-06-15 08:00', 90, FALSE),
    ('Kraków', 'Paryż', '2025-05-10 10:30', 120, TRUE),
    ('Gdańsk', 'Londyn', '2025-04-08 15:40', 150, FALSE),
    ('Wrocław', 'Rzym', '2025-03-27 21:10', 180, TRUE),
    ('Poznań', 'Barcelona', '2025-05-26 12:30', 160, FALSE),
    ('Katowice', 'Ateny', '2025-06-08 06:15', 210, TRUE),
    ('Łódź', 'Wiedeń', '2025-07-23 03:30', 100, FALSE),
    ('Szczecin', 'Oslo', '2025-08-03 17:00', 190, TRUE),
    ('Rzeszów', 'Dublin', '2025-07-18 19:20', 170, FALSE),
    ('Bydgoszcz', 'Sztokholm', '2025-06-30 20:00', 140, TRUE);

-- Wstawianie 40 pasażerów
INSERT INTO passengers (name, surname, email, phoneNumber) VALUES
    ('Jan', 'Kowalski', 'jan.kowalski@email.com', '123456789'),
    ('Anna', 'Nowak', 'anna.nowak@email.com', '987654321'),
    ('Piotr', 'Wiśniewski', 'piotr.wisniewski@email.com', '456123789'),
    ('Maria', 'Wójcik', 'maria.wojcik@email.com', '789123456'),
    ('Andrzej', 'Kowalczyk', 'andrzej.kowalczyk@email.com', '321654987'),
    ('Katarzyna', 'Kamińska', 'katarzyna.kaminska@email.com', '654987321'),
    ('Tomasz', 'Lewandowski', 'tomasz.lewandowski@email.com', '147258369'),
    ('Magdalena', 'Dąbrowska', 'magdalena.dabrowska@email.com', '258369147'),
    ('Paweł', 'Zieliński', 'pawel.zielinski@email.com', '369147258'),
    ('Monika', 'Szymańska', 'monika.szymanska@email.com', '159357486'),
    ('Marek', 'Woźniak', 'marek.wozniak@email.com', '357486159'),
    ('Agnieszka', 'Kozłowska', 'agnieszka.kozlowska@email.com', '486159357'),
    ('Grzegorz', 'Jankowski', 'grzegorz.jankowski@email.com', '753159486'),
    ('Barbara', 'Mazur', 'barbara.mazur@email.com', '159486753'),
    ('Dariusz', 'Kwiatkowski', 'dariusz.kwiatkowski@email.com', '357951486'),
    ('Ewa', 'Wojciechowska', 'ewa.wojciechowska@email.com', '951486357'),
    ('Robert', 'Krawczyk', 'robert.krawczyk@email.com', '456789123'),
    ('Joanna', 'Kaczmarek', 'joanna.kaczmarek@email.com', '789123456'),
    ('Michał', 'Piotrowski', 'michal.piotrowski@email.com', '123789456'),
    ('Aleksandra', 'Grabowska', 'aleksandra.grabowska@email.com', '789456123'),
    ('Krzysztof', 'Nowakowski', 'krzysztof.nowakowski@email.com', '456123789'),
    ('Małgorzata', 'Pawłowska', 'malgorzata.pawlowska@email.com', '123789456'),
    ('Adam', 'Michalski', 'adam.michalski@email.com', '789456123'),
    ('Elżbieta', 'Nowicka', 'elzbieta.nowicka@email.com', '456789123'),
    ('Zbigniew', 'Adamczyk', 'zbigniew.adamczyk@email.com', '321987654'),
    ('Teresa', 'Dudek', 'teresa.dudek@email.com', '987654321'),
    ('Rafał', 'Stępień', 'rafal.stepien@email.com', '654321987'),
    ('Halina', 'Witkowska', 'halina.witkowska@email.com', '321654987'),
    ('Artur', 'Sikora', 'artur.sikora@email.com', '654987321'),
    ('Iwona', 'Baran', 'iwona.baran@email.com', '987321654'),
    ('Mariusz', 'Rutkowski', 'mariusz.rutkowski@email.com', '321654987'),
    ('Dorota', 'Michalak', 'dorota.michalak@email.com', '654987321'),
    ('Łukasz', 'Szewczyk', 'lukasz.szewczyk@email.com', '987654321'),
    ('Beata', 'Ostrowska', 'beata.ostrowska@email.com', '654321987'),
    ('Stanisław', 'Tomaszewski', 'stanislaw.tomaszewski@email.com', '321987654'),
    ('Wiesława', 'Pietrzak', 'wieslawa.pietrzak@email.com', '987654321'),
    ('Henryk', 'Marciniak', 'henryk.marciniak@email.com', '654321987'),
    ('Renata', 'Wróbel', 'renata.wrobel@email.com', '321654987'),
    ('Sebastian', 'Jakubowski', 'sebastian.jakubowski@email.com', '654987321'),
    ('Urszula', 'Duda', 'urszula.duda@email.com', '987321654');

-- Wstawianie miejsc dla każdego lotu (30 miejsc na lot)
INSERT INTO seats (flightId, seatNumber, available) VALUES

(1, '1A', TRUE), (1, '1B', TRUE), (1, '1C', TRUE), (1, '1D', TRUE), (1, '1E', TRUE),
(1, '2A', TRUE), (1, '2B', TRUE), (1, '2C', TRUE), (1, '2D', TRUE), (1, '2E', TRUE),
(1, '3A', TRUE), (1, '3B', TRUE), (1, '3C', TRUE), (1, '3D', TRUE), (1, '3E', TRUE),
(1, '4A', TRUE), (1, '4B', TRUE), (1, '4C', TRUE), (1, '4D', TRUE), (1, '4E', TRUE),
(1, '5A', TRUE), (1, '5B', TRUE), (1, '5C', TRUE), (1, '5D', TRUE), (1, '5E', TRUE),
(1, '6A', TRUE), (1, '6B', TRUE), (1, '6C', TRUE), (1, '6D', TRUE), (1, '6E', TRUE),

(2, '1A', TRUE), (2, '1B', TRUE), (2, '1C', TRUE), (2, '1D', TRUE), (2, '1E', TRUE),
(2, '2A', TRUE), (2, '2B', TRUE), (2, '2C', TRUE), (2, '2D', TRUE), (2, '2E', TRUE),
(2, '3A', TRUE), (2, '3B', TRUE), (2, '3C', TRUE), (2, '3D', TRUE), (2, '3E', TRUE),
(2, '4A', TRUE), (2, '4B', TRUE), (2, '4C', TRUE), (2, '4D', TRUE), (2, '4E', TRUE),
(2, '5A', TRUE), (2, '5B', TRUE), (2, '5C', TRUE), (2, '5D', TRUE), (2, '5E', TRUE),
(2, '6A', TRUE), (2, '6B', TRUE), (2, '6C', TRUE), (2, '6D', TRUE), (2, '6E', TRUE),

(3, '1A', TRUE), (3, '1B', TRUE), (3, '1C', TRUE), (3, '1D', TRUE), (3, '1E', TRUE),
(3, '2A', TRUE), (3, '2B', TRUE), (3, '2C', TRUE), (3, '2D', TRUE), (3, '2E', TRUE),
(3, '3A', TRUE), (3, '3B', TRUE), (3, '3C', TRUE), (3, '3D', TRUE), (3, '3E', TRUE),
(3, '4A', TRUE), (3, '4B', TRUE), (3, '4C', TRUE), (3, '4D', TRUE), (3, '4E', TRUE),
(3, '5A', TRUE), (3, '5B', TRUE), (3, '5C', TRUE), (3, '5D', TRUE), (3, '5E', TRUE),
(3, '6A', TRUE), (3, '6B', TRUE), (3, '6C', TRUE), (3, '6D', TRUE), (3, '6E', TRUE),

(4, '1A', TRUE), (4, '1B', TRUE), (4, '1C', TRUE), (4, '1D', TRUE), (4, '1E', TRUE),
(4, '2A', TRUE), (4, '2B', TRUE), (4, '2C', TRUE), (4, '2D', TRUE), (4, '2E', TRUE),
(4, '3A', TRUE), (4, '3B', TRUE), (4, '3C', TRUE), (4, '3D', TRUE), (4, '3E', TRUE),
(4, '4A', TRUE), (4, '4B', TRUE), (4, '4C', TRUE), (4, '4D', TRUE), (4, '4E', TRUE),
(4, '5A', TRUE), (4, '5B', TRUE), (4, '5C', TRUE), (4, '5D', TRUE), (4, '5E', TRUE),
(4, '6A', TRUE), (4, '6B', TRUE), (4, '6C', TRUE), (4, '6D', TRUE), (4, '6E', TRUE),

(5, '1A', TRUE), (5, '1B', TRUE), (5, '1C', TRUE), (5, '1D', TRUE), (5, '1E', TRUE),
(5, '2A', TRUE), (5, '2B', TRUE), (5, '2C', TRUE), (5, '2D', TRUE), (5, '2E', TRUE),
(5, '3A', TRUE), (5, '3B', TRUE), (5, '3C', TRUE), (5, '3D', TRUE), (5, '3E', TRUE),
(5, '4A', TRUE), (5, '4B', TRUE), (5, '4C', TRUE), (5, '4D', TRUE), (5, '4E', TRUE),
(5, '5A', TRUE), (5, '5B', TRUE), (5, '5C', TRUE), (5, '5D', TRUE), (5, '5E', TRUE),
(5, '6A', TRUE), (5, '6B', TRUE), (5, '6C', TRUE), (5, '6D', TRUE), (5, '6E', TRUE),

(6, '1A', TRUE), (6, '1B', TRUE), (6, '1C', TRUE), (6, '1D', TRUE), (6, '1E', TRUE),
(6, '2A', TRUE), (6, '2B', TRUE), (6, '2C', TRUE), (6, '2D', TRUE), (6, '2E', TRUE),
(6, '3A', TRUE), (6, '3B', TRUE), (6, '3C', TRUE), (6, '3D', TRUE), (6, '3E', TRUE),
(6, '4A', TRUE), (6, '4B', TRUE), (6, '4C', TRUE), (6, '4D', TRUE), (6, '4E', TRUE),
(6, '5A', TRUE), (6, '5B', TRUE), (6, '5C', TRUE), (6, '5D', TRUE), (6, '5E', TRUE),
(6, '6A', TRUE), (6, '6B', TRUE), (6, '6C', TRUE), (6, '6D', TRUE), (6, '6E', TRUE),

(7, '1A', TRUE), (7, '1B', TRUE), (7, '1C', TRUE), (7, '1D', TRUE), (7, '1E', TRUE),
(7, '2A', TRUE), (7, '2B', TRUE), (7, '2C', TRUE), (7, '2D', TRUE), (7, '2E', TRUE),
(7, '3A', TRUE), (7, '3B', TRUE), (7, '3C', TRUE), (7, '3D', TRUE), (7, '3E', TRUE),
(7, '4A', TRUE), (7, '4B', TRUE), (7, '4C', TRUE), (7, '4D', TRUE), (7, '4E', TRUE),
(7, '5A', TRUE), (7, '5B', TRUE), (7, '5C', TRUE), (7, '5D', TRUE), (7, '5E', TRUE),
(7, '6A', TRUE), (7, '6B', TRUE), (7, '6C', TRUE), (7, '6D', TRUE), (7, '6E', TRUE),

(8, '1A', TRUE), (8, '1B', TRUE), (8, '1C', TRUE), (8, '1D', TRUE), (8, '1E', TRUE),
(8, '2A', TRUE), (8, '2B', TRUE), (8, '2C', TRUE), (8, '2D', TRUE), (8, '2E', TRUE),
(8, '3A', TRUE), (8, '3B', TRUE), (8, '3C', TRUE), (8, '3D', TRUE), (8, '3E', TRUE),
(8, '4A', TRUE), (8, '4B', TRUE), (8, '4C', TRUE), (8, '4D', TRUE), (8, '4E', TRUE),
(8, '5A', TRUE), (8, '5B', TRUE), (8, '5C', TRUE), (8, '5D', TRUE), (8, '5E', TRUE),
(8, '6A', TRUE), (8, '6B', TRUE), (8, '6C', TRUE), (8, '6D', TRUE), (8, '6E', TRUE),

(9, '1A', TRUE), (9, '1B', TRUE), (9, '1C', TRUE), (9, '1D', TRUE), (9, '1E', TRUE),
(9, '2A', TRUE), (9, '2B', TRUE), (9, '2C', TRUE), (9, '2D', TRUE), (9, '2E', TRUE),
(9, '3A', TRUE), (9, '3B', TRUE), (9, '3C', TRUE), (9, '3D', TRUE), (9, '3E', TRUE),
(9, '4A', TRUE), (9, '4B', TRUE), (9, '4C', TRUE), (9, '4D', TRUE), (9, '4E', TRUE),
(9, '5A', TRUE), (9, '5B', TRUE), (9, '5C', TRUE), (9, '5D', TRUE), (9, '5E', TRUE),
(9, '6A', TRUE), (9, '6B', TRUE), (9, '6C', TRUE), (9, '6D', TRUE), (9, '6E', TRUE),

(10, '1A', TRUE), (10, '1B', TRUE), (10, '1C', TRUE), (10, '1D', TRUE), (10, '1E', TRUE),
(10, '2A', TRUE), (10, '2B', TRUE), (10, '2C', TRUE), (10, '2D', TRUE), (10, '2E', TRUE),
(10, '3A', TRUE), (10, '3B', TRUE), (10, '3C', TRUE), (10, '3D', TRUE), (10, '3E', TRUE),
(10, '4A', TRUE), (10, '4B', TRUE), (10, '4C', TRUE), (10, '4D', TRUE), (10, '4E', TRUE),
(10, '5A', TRUE), (10, '5B', TRUE), (10, '5C', TRUE), (10, '5D', TRUE), (10, '5E', TRUE),
(10, '6A', TRUE), (10, '6B', TRUE), (10, '6C', TRUE), (10, '6D', TRUE), (10, '6E', TRUE);


INSERT INTO reservations (flightId, passengerId, seatNumber) VALUES

(1, 1, '1A'),
(3, 1, '2B'),
(5, 1, '3C'),

(2, 2, '4D'),
(7, 2, '5E'),

(4, 3, '6A'),

(1, 4, '1B'),
(6, 4, '2C'),
(9, 4, '3D'),

(3, 5, '4E'),
(8, 5, '5A'),

(10, 6, '6B'),

(2, 7, '1C',),
(4, 7, '2D',),
(6, 7, '3E',),

(5, 8, '4A'),
(10, 8, '5B'),

(7, 9, '6C'),

(1, 10, '2A'),
(3, 10, '3B'),
(5, 10, '4C'),

(8, 11, '5D'),
(10, 11, '6E'),

(9, 12, '1D'),

(2, 13, '2E'),
(4, 13, '3A'),
(6, 13, '4B'),

(7, 14, '5C'),
(9, 14, '6D'),

(1, 15, '1E'),

(3, 16, '2A'),
(5, 16, '3B'),
(7, 16, '4C'),

(4, 17, '5D'),
(8, 17, '6E'),

(10, 18, '1A'),

(2, 19, '2B'),
(6, 19, '3C'),
(9, 19, '4D'),

(1, 20, '5E'),
(5, 20, '6A'),

(3, 21, '1B'),

(4, 22, '2C'),
(7, 22, '3D'),
(10, 22, '4E'),

(6, 23, '5A'),
(8, 23, '6B'),

(2, 24, '1C'),

(1, 25, '2D'),
(3, 25, '3E'),
(5, 25, '4A'),

(7, 26, '5B'),
(9, 26, '6C'),

(4, 27, '1D'),

(6, 28, '2E'),
(8, 28, '3A'),
(10, 28, '4B'),

(2, 29, '5C'),
(5, 29, '6D'),

(1, 30, '1E'),

(3, 31, '2A'),
(7, 31, '3B'),
(9, 31, '4C'),

(4, 32, '5D'),
(8, 32, '6E'),

(10, 33, '1A'),

(2, 34, '2B'),
(6, 34, '3C'),
(9, 34, '4D'),

(1, 35, '5E'),
(5, 35, '6A'),

(3, 36, '1B'),

(4, 37, '2C'),
(7, 37, '3D'),
(10, 37, '4E'),

(6, 38, '5A'),
(8, 38, '6B'),

(2, 39, '1C'),

(1, 40, '2D'),
(3, 40, '3E'),
(5, 40, '4A');
