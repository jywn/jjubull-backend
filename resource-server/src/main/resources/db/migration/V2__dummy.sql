-- user 20만건
INSERT INTO users (
    user_sub,
    user_provider,
    user_username,
    user_nickname,
    user_grade,
    user_phone
)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 200000
)
SELECT
    CONCAT('sub_', n) AS user_sub,                     -- 임의의 문자열
    CASE WHEN n % 2 = 0 THEN 'GOOGLE' ELSE 'KAKAO' END AS user_provider,  -- 번갈아서
    CONCAT('user', n) AS user_username,
    CONCAT('nick', n) AS user_nickname,
    'BASIC' AS user_grade,
    CONCAT('010-0000-', LPAD(n % 10000, 4, '0')) AS user_phone
FROM seq;

-- ship 5000건
INSERT INTO ships (
    ship_max_head_count,
    ship_fish_type,
    ship_price,
    ship_notification
)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 5000
)
SELECT
    500000 AS ship_max_head_count,
    '쭈꾸미' AS ship_fish_type,
    n AS ship_price,
    '도시락 중식 제공합니다.' AS ship_notification
FROM seq;

-- schedule 2025년 매일 1건
INSERT INTO schedules (
    schedule_departure,
    schedule_current_head_count,
    schedule_tide,
    schedule_status,
    schedule_type,
    ship_id
)
WITH RECURSIVE days AS (
    SELECT DATE('2025-01-01') AS d, 1 AS n
    UNION ALL
    SELECT DATE_ADD(d, INTERVAL 1 DAY), n + 1
    FROM days
    WHERE d < '2025-12-31'
)
SELECT
    CONCAT(d, ' 04:00:00') AS schedule_departure,
    0 AS schedule_current_head_count,
    4 AS schedule_tide,

    /* n을 기반으로 상태 순환: WAITING → CONFIRMED → DELAYED → CANCELED */
    CASE (n % 4)
        WHEN 1 THEN 'WAITING'
        WHEN 2 THEN 'CONFIRMED'
        WHEN 3 THEN 'DELAYED'
        WHEN 0 THEN 'CANCELED'
        END AS schedule_status,

    'NORMAL' AS schedule_type,

    /* ship_id: 100~5000 사이 랜덤 */
    FLOOR(RAND() * (5000 - 100 + 1)) + 100 AS ship_id

FROM days;

-- reservation 50만건
INSERT INTO reservations (
    reservation_head_count,
    reservation_request,
    reservation_total_price,
    reservation_status,
    user_id,
    schedule_id
)
WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 500000
)
SELECT /*+ SET_VAR(cte_max_recursion_depth = 500000) */
    -- 1~4명 랜덤 비슷하게 분포
    ((n - 1) % 4) + 1                         AS reservation_head_count,
    CONCAT('부하 테스트 예약 ', n)             AS reservation_request,
    100000                                    AS reservation_total_price,
    'DEPOSIT_COMPLETED'                       AS reservation_status,

    -- user_id: 1 ~ max_user_id 범위에서 균등 분포
    ((n - 1) % u.max_user_id) + 1             AS user_id,

    -- schedule_id: 1 ~ max_schedule_id 범위에서 균등 분포
    ((n - 1) % s.max_schedule_id) + 1         AS schedule_id
FROM seq
         CROSS JOIN (SELECT MAX(user_id)      AS max_user_id      FROM users)     u
         CROSS JOIN (SELECT MAX(schedule_id)  AS max_schedule_id  FROM schedules) s;