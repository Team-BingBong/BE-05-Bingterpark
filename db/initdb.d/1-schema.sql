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

CREATE TABLE booking
(
    id                     VARCHAR(255) NOT NULL PRIMARY KEY,
    created_at             TIMESTAMP(6),
    updated_at             TIMESTAMP(6),
    amount                 INT          NOT NULL,
    booking_name           VARCHAR(255) NOT NULL,
    buyer_name             VARCHAR(255) NOT NULL,
    buyer_phone_number     VARCHAR(255) NOT NULL,
    detail_address         VARCHAR(255),
    receipt_type           VARCHAR(255) NOT NULL,
    recipient_name         VARCHAR(255),
    recipient_phone_number VARCHAR(255),
    status                 VARCHAR(255) NOT NULL,
    street_address         VARCHAR(255),
    zip_code               VARCHAR(255),
    member_id              BIGINT       NOT NULL,
    time_id                BIGINT       NOT NULL
);

CREATE TABLE booking_cancel
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    amount     INT          NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    reason     VARCHAR(255) NOT NULL,
    booking_id VARCHAR(255) NOT NULL
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
    genre              VARCHAR(50),
    running_time       INT,
    started_at         TIMESTAMP(6),
    thumbnail          TEXT,
    title              VARCHAR(100),
    rating             VARCHAR(50), -- 12세 이상 관람가
    event_hall_id      BIGINT
);

CREATE TABLE event_hall
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    address    VARCHAR(255),
    name       VARCHAR(50)
);

CREATE TABLE event_hall_seat
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at   TIMESTAMP(6),
    updated_at   TIMESTAMP(6),
    name         VARCHAR(20),
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
    name               VARCHAR(20),
    status             VARCHAR(50),
    event_seat_area_id BIGINT,
    event_time_id      BIGINT
);

CREATE TABLE event_seat_area
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    price      INT,
    area_type  VARCHAR(50),
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

CREATE TABLE payment
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at              TIMESTAMP(6),
    updated_at              TIMESTAMP(6),
    account_number          VARCHAR(255),
    amount                  INT          NOT NULL,
    approved_at             TIMESTAMP(6),
    bank_code               VARCHAR(255),
    card_issuer             VARCHAR(255),
    card_number             VARCHAR(255),
    depositor_name          VARCHAR(255),
    due_date                TIMESTAMP(6),
    failed_msg              VARCHAR(255),
    installment_plan_months INT,
    is_interest_free        BOOLEAN,
    method                  VARCHAR(255),
    payment_key             VARCHAR(255),
    refund_account_number   VARCHAR(255),
    refund_bank_code        VARCHAR(255),
    refund_holder_name      VARCHAR(255),
    requested_at            TIMESTAMP(6),
    status                  VARCHAR(255) NOT NULL,
    booking_id              VARCHAR(255),
    FOREIGN KEY (booking_id) REFERENCES booking (id)
);

CREATE TABLE ticket
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    booking_id VARCHAR(255) NOT NULL,
    seat_id    BIGINT       NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES booking (id),
    FOREIGN KEY (seat_id) REFERENCES event_seat (id)
);
