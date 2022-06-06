INSERT INTO tb_cm_user_role_cd (user_no, role_cd, crt_user_no, crt_dtm) VALUES (1, 'CM_ADMIN', 0, NOW()) ON CONFLICT (user_no,role_cd) DO NOTHING;
