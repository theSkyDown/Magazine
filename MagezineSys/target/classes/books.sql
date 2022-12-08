create table books
(
    id        int auto_increment
        primary key,
    book_name varchar(64) null,
    img_src   varchar(64) not null,
    classify  varchar(20) null,
    price     double      null
);

INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (1, 'Bazaar', 'amusement/bazaar.png', '娱乐期刊', 17);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (2, 'Vogue', 'amusement/vogue.png', '娱乐期刊', 18.3);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (3, '柳叶刀', 'academic/柳叶刀.png', '学术期刊', 12);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (4, 'Science', 'academic/科学science.png', '学术期刊', 15);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (5, 'Nature', 'academic/自然nature.png', '学术期刊', 16);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (6, '艺术与设计', 'art/艺术与设计.png', '艺术期刊', 11);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (7, '设计艺术研究', 'art/设计艺术研究.png', '艺术期刊', 8);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (8, '意林', 'literature/意林.png', '文学期刊', 19);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (9, '萌芽', 'literature/萌芽.png', '文学期刊', 11);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (10, '读者', 'literature/读者.png', '文学期刊', 20);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (11, '中国国家地理', 'science/中国国家地理.png', '科普期刊', 8);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (12, '环球科学', 'science/环球科学ScientificAmerican.png', '科普期刊', 10);
INSERT INTO MagSubSys.books (id, book_name, img_src, classify, price) VALUES (13, '科学世界', 'science/科学世界.png', '科普期刊', 12);
