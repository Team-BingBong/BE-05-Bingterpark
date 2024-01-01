-- EventHall
INSERT INTO event_hall (event_name, address)
VALUES ('고척스카이돔', '서울 구로구 경인로 430');

-- EventHallSeat
INSERT INTO event_hall_seat (name, eventhall_id)
VALUES ('A1', 1);

-- Event
INSERT INTO event (title, description, running_time, started_at, ended_at, rating, genre, average_score, thumbnail,
                   booking_started_at, booking_ended_at, event_hall_id)
VALUES ('BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 'BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 120,
        '2024-01-01T10:00:00', '2024-01-01T12:00:00', '???', 'CONCERT', 0.0,
        'https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif', '2023-12-21T09:00:00',
        '2023-12-31T11:00:00', 1);

-- EventTime
INSERT INTO event_time (round, started_at, ended_at, event_id)
VALUES (1, '2024-01-01T10:00:00', '2024-01-01T12:00:00', 1);

-- EventSeatArea
INSERT INTO event_seat_area (price, area_type, event_id)
VALUES (100000, 'S', 1);
INSERT INTO event_seat_area (price, area_type, event_id)
VALUES (80000, 'R', 1);

-- EventSeat
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('A1', 'BEING_BOOKED', 1, 1);
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('A2', 'BEING_BOOKED', 1, 1);
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('A3', 'BEING_BOOKED', 1, 1);
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('E1', 'BEING_BOOKED', 2, 1);
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('E2', 'BEING_BOOKED', 2, 1);
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('E3', 'BEING_BOOKED', 2, 1);
