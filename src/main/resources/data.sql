insert into user (email, name, created_at, updated_at) values ('heeouo@kookmin.ac.kr', '구다희', now(), now());
insert into user (email, name, created_at, updated_at) values ('heeouo@konkuk.ac.kr', '첩자', now(), now());
insert into user (email, name, created_at, updated_at) values ('jasonyoo950909@gmail.com', '유경원', now(), now());

insert into food (user_id, name, start_date, created_at, updated_at) values (1, '커피', '2021-11-22', now(), now());
insert into food (user_id, name, start_date, created_at, updated_at) values (1, '토샷추', '2021-11-24', now(), now());
insert into food (user_id, name, start_date, created_at, updated_at) values (1, '초콜릿', '2021-11-20', now(), now());
insert into food (user_id, name, start_date, created_at, updated_at) values (1, '탄산음료', '2021-11-25', now(), now());

insert into review (food_id, date, title, content, fasted, created_at, updated_at) values (1, '2021-11-22', '1일차', '이정도는 만만하지', 1, now(), now());
insert into review (food_id, date, title, content, fasted, created_at, updated_at) values (1, '2021-11-23', '2일차', '죽겠어요', 1, now(), now());
insert into review (food_id, date, title, content, fasted, created_at, updated_at) values (1, '2021-11-24', '3일차', '과제 마감 N개', 0, now(), now());
insert into review (food_id, date, title, content, fasted, created_at, updated_at) values (1, '2021-11-25', '4일차', '카페인 디톡스', 1, now(), now());
insert into review (food_id, date, title, content, fasted, created_at, updated_at) values (1, '2021-11-26', '5일차', '커피 없이는 코드가 안 나와요...', 0, now(), now());
