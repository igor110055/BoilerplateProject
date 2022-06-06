INSERT INTO tb_cm_role_cd
	(role_cd, role_nm, ord, rmk, use_yn, crt_user_uid, updt_user_uid, crt_dtm, updt_dtm)
	VALUES ('CM_ADMIN', 'CM관리자', '00000', null, 'Y', '1ece57a644176ec38de4fd351420205c', '1ece57a644176ec38de4fd351420205c', NOW(), NOW())
ON CONFLICT (role_cd) DO NOTHING;             