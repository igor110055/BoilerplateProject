INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01100', 'CM_1100', null, '프로그램관리', null , '1ece57a644176ec38de4fd351420205c', '1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01150', 'CM_1150', null, '프로그램조회팝업', null , '1ece57a644176ec38de4fd351420205c', '1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01200', 'CM_1200', null, '테이블도메인관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    


INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01300', 'CM_1300', null, '메뉴관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01400', 'CM_1400', null, '공통코드관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01600', 'CM_1600', null, '테이블시퀀스', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01840', 'CM_1840', null, 'SA에러로그', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    


INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '01850', 'CM_1850', null, '로그LIST', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;    

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '02300', 'CM_2300', null, '사용자조회', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '04100', 'CM_4100', null, '회원관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '04200', 'CM_4200', null, '역할관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '06100', 'CM_6100', null, '게시판관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '06200', 'CM_6200', null, '게시판', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '07100', 'CM_7100', null, '테이블관리', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '07200', 'CM_7200', null, '컬럼상세', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   

INSERT INTO tb_cm_pgm
	(category, dir_link, ord, pgm_id, pgm_link, pgm_nm, rmk, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM', null , '07300', 'CM_7300', null, 'SQL', null ,'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (pgm_id) DO NOTHING;   