create table orders
(
    id         int auto_increment
        primary key,
    user_id    int      null,
    book_id    int      null,
    start_time datetime null,
    end_time   datetime null
);

INSERT INTO MagSubSys.orders (id, user_id, book_id, start_time, end_time) VALUES (3, 2, 12, '2022-11-13 17:00:33', '2023-05-13 17:00:33');
INSERT INTO MagSubSys.orders (id, user_id, book_id, start_time, end_time) VALUES (4, 2, 10, '2022-11-13 17:00:36', '2023-03-13 17:00:36');
INSERT INTO MagSubSys.orders (id, user_id, book_id, start_time, end_time) VALUES (7, 2, 7, '2022-11-13 18:30:43', '2022-12-13 18:30:43');
INSERT INTO MagSubSys.orders (id, user_id, book_id, start_time, end_time) VALUES (8, 2, 11, '2022-11-13 18:30:55', '2023-01-13 18:30:55');
