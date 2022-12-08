create table users
(
    id        int auto_increment
        primary key,
    username  varchar(30)          not null,
    phone     varchar(11)          not null,
    password  varchar(64)          null,
    address   varchar(125)         null,
    isManager tinyint(1) default 0 null,
    constraint phone
        unique (phone)
);

INSERT INTO MagSubSys.users (id, username, phone, password, address, isManager) VALUES (1, 'root', 'root', '63a9f0ea7bb98050796b649e85481845', null, 1);
INSERT INTO MagSubSys.users (id, username, phone, password, address, isManager) VALUES (2, 'admin', 'admin', '4297f44b13955235245b2497399d7a93', '', 0);
INSERT INTO MagSubSys.users (id, username, phone, password, address, isManager) VALUES (3, 'eccentric', '17373011502', '9ece87024fa76853cdfe1667d68ca579', '海南省乐东县', 0);
INSERT INTO MagSubSys.users (id, username, phone, password, address, isManager) VALUES (4, 'user_60666', '13676511965', '01cfcd4f6b8770febfb40cb906715822', '中国', 0);
