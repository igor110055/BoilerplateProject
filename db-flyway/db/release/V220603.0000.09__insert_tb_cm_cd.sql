INSERT INTO tb_cm_grp_cd
	(grp_cd, grp_nm, ord, rmk, use_yn, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('USE_YN', '사용여부', '1', null, 'Y', 0, 0, NOW(), NOW())
ON CONFLICT (grp_cd) DO NOTHING;    

INSERT INTO tb_cm_cd
	(grp_cd, cd, cd_nm, ord, rmk, use_yn, attr1, attr2, attr3, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('USE_YN', 'Y', 'Y', '1', null, 'Y', null, null, null, 0, 0, NOW(), NOW())
ON CONFLICT (grp_cd, cd) DO NOTHING;
INSERT INTO tb_cm_cd
	( grp_cd, cd, cd_nm, ord, rmk, use_yn, attr1, attr2, attr3, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('USE_YN', 'N', 'N', '2', null, 'Y', null, null, null, 0, 0, NOW(), NOW())
ON CONFLICT (grp_cd, cd) DO NOTHING;

INSERT INTO tb_cm_grp_cd
	(grp_cd, grp_nm, ord, rmk, use_yn, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('PGM_CATEGORY', '프로그램카테고리', '2', null, 'Y', 0, 0, NOW(), NOW())
ON CONFLICT (grp_cd) DO NOTHING;   

INSERT INTO tb_cm_cd
	(grp_cd, cd, cd_nm, ord, rmk, use_yn, attr1, attr2, attr3, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('PGM_CATEGORY', 'CM', '공통', '1', null, 'Y', null, null, null, 0, 0, NOW(), NOW())
ON CONFLICT (grp_cd, cd) DO NOTHING;
