-- 샘플 데이터 입력
-- 유저 등록
insert into user_tb(username, password, fullname, created_at)
values	('aaaa', '1234', '길동', now());
insert into user_tb(username, password, fullname, created_at)
values	('bbbb', '1234', '둘리', now());
insert into user_tb(username, password, fullname, created_at)
values	('cccc', '1234', '마이', now());

-- 기본 계좌 등록
insert into account_tb(number, password, balance, user_id, created_at)
values	('1111', '1234', 1300, 1, now());
insert into account_tb(number, password, balance, user_id, created_at)
values	('2222', '1234', 1100, 2, now());
insert into account_tb(number, password, balance, user_id, created_at)
values	('3333', '1234', 0, 3, now());

-- 이체 내역 등록 
-- 1번 계좌 -> 2번 계좌, 100원 이체
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (100, 900, 1100, 1, 2, now());
-- 1번 계좌 100원 출금
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (100, 800, null, 1, null, now());
-- 1번 계좌 500원 입금
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (500, null, 1300, null, 1, now());
-- 2번 계좌 300원 출금
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (300, 800, null, 2, null, now());
-- 3번 계좌 500원 입금
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (500, null, 500, null, 3, now());
-- 3번 계좌 -> 1번계좌 200원 이체
insert into history_tb(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values (500, 300, 1500, 3, 1, now());

