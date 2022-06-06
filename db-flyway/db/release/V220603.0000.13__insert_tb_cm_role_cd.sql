INSERT INTO tb_cm_role_cd
	(role_cd, role_nm, ord, rmk, use_yn, crt_user_no, updt_user_no, crt_dtm, updt_dtm)
	VALUES ('CM_ADMIN', 'CM관리자', '00000', null, 'Y', 0, 0, NOW(), NOW())
ON CONFLICT (role_cd) DO NOTHING;             