CREATE TABLE admin
(
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at               TIMESTAMP(6),
    updated_at               TIMESTAMP(6),
    last_login_at            TIMESTAMP(6) NOT NULL,
    last_password_updated_at TIMESTAMP(6) NOT NULL,
    email                    VARCHAR(255) NOT NULL,
    name                     VARCHAR(255) NOT NULL,
    password                 VARCHAR(255) NOT NULL,
    phone_number             VARCHAR(255) NOT NULL,
    role                     VARCHAR(255),
    status                   VARCHAR(255) NOT NULL
);

CREATE TABLE event
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         TIMESTAMP(6),
    updated_at         TIMESTAMP(6),
    average_score      FLOAT(53),
    booking_ended_at   TIMESTAMP(6),
    booking_started_at TIMESTAMP(6),
    description        TEXT,
    ended_at           TIMESTAMP(6),
    genre              VARCHAR(255),
    running_time       INT,
    started_at         TIMESTAMP(6),
    thumbnail          TEXT,
    title              VARCHAR(255),
    rating             VARCHAR(255),
    event_hall_id      BIGINT
);

CREATE TABLE event_hall
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    address    VARCHAR(255),
    event_name VARCHAR(255)
);

CREATE TABLE event_hall_seat
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6),
    name         VARCHAR(255),
    eventhall_id BIGINT,
    FOREIGN KEY (eventhall_id) REFERENCES event_hall (id)
);

CREATE TABLE event_image
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    url        TEXT,
    event_id   BIGINT,
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE event_review
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    content    TEXT,
    score      INT,
    event_id   BIGINT,
    member_id  BIGINT,
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE event_seat
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at         TIMESTAMP(6),
    updated_at         TIMESTAMP(6),
    name               VARCHAR(255),
    status             VARCHAR(255),
    event_seat_area_id BIGINT,
    event_time_id      BIGINT
);

CREATE TABLE event_seat_area
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    price      INT,
    area_type  VARCHAR(255),
    event_id   BIGINT,
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE event_time
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    ended_at   TIMESTAMP(6),
    round      INT,
    started_at TIMESTAMP(6),
    event_id   BIGINT,
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE member
(
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at               TIMESTAMP(6),
    updated_at               TIMESTAMP(6),
    last_login_at            TIMESTAMP(6) NOT NULL,
    last_password_updated_at TIMESTAMP(6) NOT NULL,
    birth_date               VARCHAR(255),
    detail_address           VARCHAR(255),
    email                    VARCHAR(255) NOT NULL,
    gender                   VARCHAR(255),
    name                     VARCHAR(255) NOT NULL,
    password                 VARCHAR(255),
    phone_number             VARCHAR(255),
    provider                 VARCHAR(255),
    role                     VARCHAR(255),
    status                   VARCHAR(255) NOT NULL,
    street_address           VARCHAR(255),
    zip_code                 VARCHAR(255)
);


-- 예매

CREATE TABLE IF NOT EXISTS booking
(
    id                     CHAR(13)        NOT NULL PRIMARY KEY, -- 예매 번호
    amount                 INT UNSIGNED    NOT NULL,             -- 결제 금액
    booking_name           VARCHAR(255)    NOT NULL,             -- 예매 명
    status                 VARCHAR(20)     NOT NULL,             -- 예매 상태
    buyer_name             VARCHAR(100)    NOT NULL,             -- 구매자 명
    buyer_phone_number     VARCHAR(20)     NOT NULL,             -- 구매자 전화번호
    receipt_type           VARCHAR(20)     NOT NULL,             -- 티켓 수령 타입
    recipient_name         VARCHAR(100),                         -- 수령인 명
    recipient_phone_number VARCHAR(20),                          -- 수령인 전화번호
    street_address         VARCHAR(255),                         -- 수령지 도로명 주소
    detail_address         VARCHAR(255),                         -- 수령지 상세 주소
    zip_code               CHAR(5),                              -- 수령지 우편 번호
    member_id              BIGINT UNSIGNED NOT NULL,             -- 회원 id
    time_id                BIGINT UNSIGNED NOT NULL,             -- 공연 회차 id
    created_at             DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at             DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX booking_idx_buyer_name (buyer_name),
    INDEX booking_idx_buyer_phone_number (buyer_phone_number),
    INDEX booking_idx_member_id (member_id),
    INDEX booking_idx_time_id (time_id),
    INDEX booking_idx_created_at (created_at),
    INDEX booking_idx_updated_at (updated_at)
);

CREATE TABLE IF NOT EXISTS payment
(
    id                      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 결제 id
    payment_key             VARCHAR(50),                                -- 결제 키
    status                  VARCHAR(20)  NOT NULL,                      -- 결제 상태
    amount                  INT UNSIGNED NOT NULL,                      -- 결제 가격
    method                  VARCHAR(20),                                -- 결제 수단
    card_issuer             VARCHAR(20),                                -- 카드 발급사
    card_number             VARCHAR(20),                                -- 카드 번호
    installment_plan_months INT UNSIGNED,                               -- 할부 개월 수
    is_interest_free        BOOLEAN,                                    -- 무이자 여부
    bank_code               CHAR(4),                                    -- 은행 코드
    account_number          VARCHAR(20),                                -- 가상 계좌 번호
    depositor_name          VARCHAR(20),                                -- 입금자 명
    due_date                DATETIME,                                   -- 입금 기한
    refund_account_number   VARCHAR(20),                                -- 환불 계좌 번호
    refund_bank_code        CHAR(2),                                    -- 환불 은행 코드
    refund_holder_name      VARCHAR(20),                                -- 환불 계좌 예금주
    failed_msg              VARCHAR(255),                               -- 결제 실패 메시지
    requested_at            DATETIME,                                   -- 결제 요청 날짜
    approved_at             DATETIME,                                   -- 결제 승인 날짜
    booking_id              CHAR(13)     NOT NULL,                      -- 예매 번호
    created_at              DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX payment_idx_payment_key (payment_key),
    INDEX payment_idx_booking_id (booking_id)
);

CREATE TABLE IF NOT EXISTS ticket
(
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 티켓 id
    seat_id    BIGINT UNSIGNED NOT NULL,                   -- 공연 좌석 id
    booking_id CHAR(13)        NOT NULL,                   -- 예매 번호
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX ticket_idx_seat_id (seat_id),
    INDEX ticket_idx_booking_id (booking_id)
);

CREATE TABLE IF NOT EXISTS booking_cancel
(
    id         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY, -- 예매 취소 id
    amount     INT UNSIGNED NOT NULL,                      -- 환불 금액
    reason     VARCHAR(100) NOT NULL,                      -- 취소 사유
    created_by VARCHAR(50)  NOT NULL,                      -- 취소 요청자
    booking_id CHAR(13)     NOT NULL,                      -- 예매 번호
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX booking_cancel_idx_booking_id (booking_id)
);
