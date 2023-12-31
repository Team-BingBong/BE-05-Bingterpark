-- EventHall
INSERT INTO event_hall (event_name, address)
VALUES ('고척스카이돔', '서울 구로구 경인로 430');

-- Event
INSERT INTO event (title, description, running_time, started_at, ended_at, rating, genre, average_score, thumbnail,
                   booking_started_at, booking_ended_at, event_hall_id)
VALUES ('BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 'BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL', 120,
        '2024-01-05T10:00:00', '2025-01-06T12:00:00', '15세 이상 관람가', 'CONCERT', 0.0,
        'https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif', '2023-12-21T09:00:00',
        '2024-12-31T11:00:00', 1);

-- EventTime
INSERT INTO event_time (round, started_at, ended_at, event_id)
VALUES (1, '2024-01-01T10:00:00', '2024-01-01T12:00:00', 1);

-- EventSeatArea
INSERT INTO event_seat_area (price, area_type, event_id)
VALUES (100000, 'S', 1),
       (80000, 'R', 1);

-- EventSeat
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('A1', 'BEING_BOOKED', 1, 1)
     , ('A2', 'BEING_BOOKED', 1, 1)
     , ('A3', 'BEING_BOOKED', 1, 1)
     , ('E1', 'BEING_BOOKED', 2, 1)
     , ('E2', 'BEING_BOOKED', 2, 1)
     , ('E3', 'BEING_BOOKED', 2, 1);

-- EventReview
INSERT INTO event_review (score, content, event_id)
VALUES (5, 'Great event!', 1);

-- Member
INSERT INTO member (name, password, phone_number, email, birth_date, gender, street_address, detail_address, zip_code,
                    status, provider, role, last_login_at, last_password_updated_at)
VALUES ('김빙봉', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01012345678', 'member1@example.com',
        '1990-01-01', 'MALE', '경기도 성남시 분당구', '정든마을 101호',
        '12345', 'ACTIVE', 'KAKAO', 'ROLE_USER', '2024-01-01T10:00:00', '2024-01-01T10:00:00'),
       ('이행복', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01087654321', 'member2@example.com',
        '1992-02-02', 'FEMALE', '광주광역시 광산로 67',
        '103동 202호', '54321', 'ACTIVE', 'KAKAO', 'ROLE_USER', '2024-01-01T10:00:00', '2024-01-01T10:00:00');

-- Admin
INSERT INTO admin (name, email, password, phone_number, status, role, last_login_at, last_password_updated_at)
VALUES ('슈퍼관리자', 'superadmin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa',
        '01012345678', 'ACTIVE', 'ROLE_SUPERADMIN', NOW(), NOW()),
       ('관리자', 'admin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01012345678',
        'ACTIVE', 'ROLE_ADMIN', NOW(), NOW()),
       ('오래된 슈퍼관리자', 'oldSuperadmin@example.com', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa',
        '01012345678', 'ACTIVE', 'ROLE_SUPERADMIN', '2020-01-01', NOW());


-- Booking
INSERT INTO booking (id,
                     amount,
                     booking_name,
                     buyer_name,
                     buyer_phone_number,
                     status,
                     receipt_type,
                     member_id,
                     time_id,
                     created_at)
VALUES ('bookingCreateTestId',
        180000,
        '빙봉의 주문',
        '빙봉',
        '010-1234-5678',
        'WAITING_FOR_PAYMENT',
        'PICK_UP',
        1,
        1,
        CURRENT_TIMESTAMP),
       ('bookingCancelTestId',
        180000,
        '주영의 주문',
        '주영',
        '010-1234-5678',
        'PAYMENT_COMPLETED',
        'PICK_UP',
        1,
        1,
        CURRENT_TIMESTAMP);


-- Ticket
INSERT INTO ticket (booking_id, seat_id)
VALUES ('bookingCreateTestId', 1),
       ('bookingCreateTestId', 2),
       ('bookingCancelTestId', 3);


-- Payment
INSERT INTO payment (amount,
                     approved_at,
                     card_number,
                     installment_plan_months,
                     is_interest_free,
                     method,
                     payment_key,
                     requested_at,
                     status,
                     booking_id)
VALUES (180000,
        CURRENT_TIMESTAMP,
        '5555-5555-5555-5555',
        0,
        false,
        'CARD',
        'paymentkey1',
        CURRENT_TIMESTAMP,
        'READY',
        'bookingCreateTestId'),
       (180000,
        CURRENT_TIMESTAMP,
        '5555-5555-5555-5555',
        0,
        false,
        'CARD',
        'paymentkey2',
        CURRENT_TIMESTAMP,
        'DONE',
        'bookingCancelTestId')


