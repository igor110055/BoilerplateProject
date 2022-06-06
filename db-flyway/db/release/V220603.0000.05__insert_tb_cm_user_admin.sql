INSERT INTO tb_cm_user
	(user_uid
	, crt_dtm
	, updt_dtm
	, email
	, email_cert_yn
	, email_cert_uuid
	, rmk
	, use_yn
	, user_id
	, user_nm
	, pwd
	, salt
	, nick_nm
	, crt_user_uid
	, updt_user_uid
	, birth
	, gndr)
	VALUES (
	  '1ece57a644176ec38de4fd351420205c'
	, NOW()
	, NOW()
	, 'admin@admin.com'
	, 'Y'
	, '236f553d-2139-408b-a57f-210489581ae5'
	, null
	, 'Y'
	, 'admin@admin.com'
	, '관리자'
	, '73dd5cbcc61e8ecfaf11c791266df4370e8b2d722813e086bbba521a15c8614aa6d3674e213bf740cf3f6ace423d79ffd22cfe7673930319dc3f743281bf1775'
	, 'sRw3ItZTMV0bNVTJtOsLVA=='
	, '관리자'
	, '1ece57a644176ec38de4fd351420205c'
	, '1ece57a644176ec38de4fd351420205c'
	, '1982'
	, 'm'
	)
    ON CONFLICT (user_uid) DO NOTHING;