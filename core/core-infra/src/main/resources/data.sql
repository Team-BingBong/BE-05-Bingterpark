-- EventHall
INSERT INTO event_hall (event_name, address)
VALUES ('고척스카이돔', '서울 구로구 경인로 430');

-- Event
INSERT INTO event (title, description, running_time, started_at, ended_at, rating, genre, average_score, thumbnail,
                   booking_started_at, booking_ended_at, event_hall_id)
VALUES ('BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 'BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 120,
        '2024-01-01T10:00:00', '2025-01-01T12:00:00', '15세 이상 관람가', 'CONCERT', 0.0,
        'https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif', '2023-12-21T09:00:00',
        '2024-12-31T11:00:00', 1);

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

-- Role
INSERT INTO role (name)
VALUES ('ROLE_SUPERADMIN'),
       ('ROLE_ADMIN'),
       ('ROLE_USER'),
       ('ROLE_GUEST');


-- Member
INSERT INTO member (name, password, phone_number, email, birth_date, gender, street_address, detail_address, zip_code,
                    status, provider, role_id, last_login_at, last_password_updated_at)
VALUES ('김빙봉', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01012345678', 'john.doe@example.com',
        '1990-01-01', 'MALE', '123 Main St', 'Apt 101',
        '12345', 'ACTIVE', 'KAKAO', 3, '2024-01-01T10:00:00', '2024-01-01T10:00:00'),
       ('이행복', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01087654321', 'jane.smith@example.com',
        '1992-02-02', 'FEMALE', '456 Elm St',
        'Suite 202', '54321', 'ACTIVE', 'KAKAO', 3, '2024-01-01T10:00:00', '2024-01-01T10:00:00');

-- Admin
INSERT INTO admin (name, email, password, phone_number, status, role_id, last_login_at, last_password_updated_at)
VALUES ('슈퍼관리자', 'superadmin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa',
        '01012345678', 'ACTIVE', 1, NOW(), NOW()),
       ('관리자', 'admin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01012345678',
        'ACTIVE', 2, NOW(), NOW()),
       ('오래된 슈퍼관리자', 'oldSuperadmin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa',
        '01012345678', 'ACTIVE', 1, '2020-01-01', NOW());


