/* ===========================
    COMMON USER TABLE
   =========================== */
CREATE TABLE users
(
    user_id       BIGINT       NOT NULL AUTO_INCREMENT,
    user_sub      VARCHAR(255) NULL,
    user_provider VARCHAR(255) NULL,
    user_username VARCHAR(255) NULL,
    user_nickname VARCHAR(255) NULL,
    user_grade    VARCHAR(255) NULL,
    user_phone    VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

/* ===========================
    RESOURCE SERVER TABLES
   =========================== */

CREATE TABLE coupons
(
    coupon_id         BIGINT       NOT NULL AUTO_INCREMENT,
    coupon_expires_at DATETIME     NULL,
    coupon_type       VARCHAR(255) NULL,
    coupon_status     VARCHAR(255) NULL,
    user_id           BIGINT       NULL,
    CONSTRAINT pk_coupons PRIMARY KEY (coupon_id)
);

CREATE TABLE message_logs
(
    message_log_id              BIGINT       NOT NULL AUTO_INCREMENT,
    message_log_time_stamp      DATETIME     NULL,
    message_log_recipient_phone VARCHAR(255) NULL,
    message_log_content         VARCHAR(255) NULL,
    message_log_result          VARCHAR(255) NULL,
    CONSTRAINT pk_message_logs PRIMARY KEY (message_log_id)
);

CREATE TABLE ships
(
    ship_id             BIGINT       NOT NULL AUTO_INCREMENT,
    ship_max_head_count INT          NULL,
    ship_fish_type      VARCHAR(255) NULL,
    ship_price          INT          NULL,
    ship_notification   VARCHAR(255) NULL,
    CONSTRAINT pk_ships PRIMARY KEY (ship_id)
);

CREATE TABLE schedules
(
    schedule_id                 BIGINT       NOT NULL AUTO_INCREMENT,
    schedule_departure          DATETIME     NULL,
    schedule_current_head_count INT          NULL,
    schedule_tide               INT          NULL,
    schedule_status             VARCHAR(255) NULL,
    schedule_type               VARCHAR(255) NULL,
    ship_id                     BIGINT       NULL,
    CONSTRAINT pk_schedules PRIMARY KEY (schedule_id)
);

CREATE TABLE reservations
(
    reservation_id          BIGINT       NOT NULL AUTO_INCREMENT,
    reservation_head_count  INT          NULL,
    reservation_request     VARCHAR(255) NULL,
    reservation_total_price INT          NULL,
    reservation_status      VARCHAR(255) NULL,
    user_id                 BIGINT       NULL,
    schedule_id             BIGINT       NULL,
    coupon_id               BIGINT       NULL,
    CONSTRAINT pk_reservations PRIMARY KEY (reservation_id)
);

/* 통계 테이블 */
CREATE TABLE statistics (
                            statistic_id BIGINT NOT NULL AUTO_INCREMENT,

                            statistic_daily_visited   INT NOT NULL,
                            statistic_daily_reserved  INT NOT NULL,
                            statistic_daily_deposited INT NOT NULL,
                            statistic_daily_sales     INT NOT NULL,

                            statistic_deposit_expired INT NOT NULL,
                            statistic_deposit_24hour  INT NOT NULL,

                            statistic_date DATE NOT NULL,

                            PRIMARY KEY (statistic_id),
                            UNIQUE KEY uq_statistics_date (statistic_date)
);

/* ===========================
    AUTH SERVER TABLES
   =========================== */

CREATE TABLE oauth2_users
(
    oauth2_user_id       BIGINT       NOT NULL AUTO_INCREMENT,
    oauth2_user_provider VARCHAR(255) NULL,
    oauth2_user_sub      VARCHAR(255) NULL,
    user_grade           VARCHAR(255) NULL,
    user_name            VARCHAR(255) NULL,
    CONSTRAINT pk_oauth2_users PRIMARY KEY (oauth2_user_id)
);

CREATE TABLE refresh_tokens
(
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    refresh_token_user_id    BIGINT       NULL,
    refresh_token_value      VARCHAR(255) NULL,
    refresh_token_expires_at DATETIME     NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

/* ===========================
    UNIQUE CONSTRAINTS
   =========================== */

ALTER TABLE oauth2_users
    ADD CONSTRAINT uc_oauth2_users_provider_sub
        UNIQUE (oauth2_user_provider, oauth2_user_sub);

/* ===========================
    FOREIGN KEYS
   =========================== */

ALTER TABLE coupons
    ADD CONSTRAINT fk_coupons_user
        FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservations_coupon
        FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id);

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservations_schedule
        FOREIGN KEY (schedule_id) REFERENCES schedules (schedule_id);

ALTER TABLE reservations
    ADD CONSTRAINT fk_reservations_user
        FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE schedules
    ADD CONSTRAINT fk_schedules_ship
        FOREIGN KEY (ship_id) REFERENCES ships (ship_id);