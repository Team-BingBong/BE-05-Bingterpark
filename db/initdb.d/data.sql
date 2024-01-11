-- EventHall
INSERT INTO event_hall (event_name, address)
VALUES ('고척스카이돔', '서울 구로구 경인로 430');

-- Event
INSERT INTO event (title, description, running_time, started_at, ended_at, rating, genre, average_score, thumbnail,
                   booking_started_at, booking_ended_at, event_hall_id)
VALUES ('BLACKPINK WORLD TOUR [BORN PINK] FINALE IN SEOUL', 'BLACKPINK WORLD TOUR [BORN PINK] FINALE IN SEOUL', 120,
        '2025-01-01 10:00:00', '2025-01-01 12:00:00', '15세 이상 관람가', 'CONCERT', 0.0,
        'https://ticketimage.interpark.com/Play/image/large/23/23011804_p.gif', '2023-12-21 09:00:00',
        '2024-12-31 11:00:00', 1);

-- EventTime
INSERT INTO event_time (round, started_at, ended_at, event_id)
VALUES (1, '2025-01-01 10:00:00', '2025-01-01 12:00:00', 1);

-- EventSeatArea
INSERT INTO event_seat_area (price, area_type, event_id)
VALUES (100000, 'S', 1),
       (80000, 'R', 1);

-- EventSeat
INSERT INTO event_seat (name, status, event_seat_area_id, event_time_id)
VALUES ('A1', 'AVAILABLE', 1, 1),
       ('A2', 'AVAILABLE', 1, 1),
       ('A3', 'AVAILABLE', 1, 1),
       ('E1', 'AVAILABLE', 2, 1),
       ('E2', 'AVAILABLE', 2, 1),
       ('E3', 'AVAILABLE', 2, 1);

-- EventReview
INSERT INTO event_review (score, content, event_id)
VALUES (5, 'Great event!', 1);

-- Member
INSERT INTO member (name, password, phone_number, email, birth_date, gender, street_address, detail_address, zip_code,
                    status, provider, role, last_login_at, last_password_updated_at)
VALUES ('김빙봉', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01012345678', 'member1@example.com',
        '1990-01-01', 'MALE', '경기도 성남시 분당구', '정든마을 101호',
        '12345', 'ACTIVE', 'KAKAO', 'ROLE_USER', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
       ('이행복', '$2a$10$tfdM.PjviEH0zMEXVYjH.ODJPSviQRrYpb17rdMjvJtbWbSnC8nTa', '01087654321', 'member2@example.com',
        '1992-02-02', 'FEMALE', '광주광역시 광산로 67',
        '103동 202호', '54321', 'ACTIVE', 'KAKAO', 'ROLE_USER', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

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
                     member_id,
                     time_id,
                     amount,
                     booking_name,
                     buyer_name,
                     buyer_phone_number,
                     receipt_type,
                     status,
                     created_at,
                     updated_at)
VALUES ('bookingTestId',
        1,
        1,
        180000,
        '1번 멤버의 주문',
        '1번',
        '010-1234-5678',
        'PICK_UP',
        'CANCELED',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- Ticket
INSERT INTO ticket (booking_id, seat_id)
VALUES ('bookingTestId', 3),
       ('bookingTestId', 4);

-- Payment
INSERT INTO payment (booking_id,
                     amount,
                     method,
                     card_issuer,
                     card_number,
                     installment_plan_months,
                     is_interest_free,
                     payment_key,
                     status,
                     requested_at,
                     approved_at,
                     created_at,
                     updated_at)
VALUES ('bookingTestId', 180000, 'CARD', 'HYUNDAI', '11111111****111*', 0, false, 'paymentkey', 'CANCELED',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Booking Cancel
INSERT INTO booking_cancel (booking_id, amount, reason, created_by, created_at, updated_at)
VALUES ('bookingTestId', 180000, '구매자 변심', 'test@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
