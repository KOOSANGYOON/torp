INSERT INTO user (id, user_id, password, name, deleted) values (1, 'javajigi', 'test', '자바지기', false);
INSERT INTO user (id, user_id, password, name, deleted) values (2, 'sanjigi', 'test', '산지기', false);
INSERT INTO user (id, user_id, password, name, deleted) values (3, 'koo', 'test', '구상윤', false);
INSERT INTO user (id, user_id, password, name, deleted) values (4, 'pobi', 'test', '박재성교수님', false);
INSERT INTO user (id, user_id, password, name, deleted) values (5, 'testman', 'testman', 'imTestMan', false);

INSERT INTO to_do_board (id, deleted, title, writer_id) values (1, false, '영어점수올리기', 1);
INSERT INTO to_do_board (id, deleted, title, writer_id) values (2, false, '영어점수올리기', 3);
INSERT INTO to_do_board (id, deleted, title, writer_id) values (3, false, '알고리즘점수올리기', 3);

INSERT INTO to_do_deck (id, deleted, title, to_do_board_id, writer_id) values (1, false, '한글안깨짐', 2, 3);
INSERT INTO to_do_deck (id, deleted, title, to_do_board_id, writer_id) values (1, false, '제발한글안깨짐', 3, 3);
