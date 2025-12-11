WITH RECURSIVE seq AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM seq
    WHERE n < 300000
)
INSERT INTO reservations (
    reservation_head_count,
    reservation_request,
    reservation_total_price,
    reservation_status,
    user_id,
    schedule_id
)
SELECT
    1 AS reservation_head_count,
    CONCAT('부하 테스트 예약 ', n) AS reservation_request,
    100000 AS reservation_total_price,
    'DEPOSIT_COMPLETED' AS reservation_status,
    1 AS user_id,
    1 AS schedule_id
FROM seq;