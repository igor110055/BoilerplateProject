INSERT INTO tb_cm_seq
(seq_nm,allocation_size,col_nm,crt_dtm,seq_no,tb_nm,updt_dtm,crt_usr_uid,updt_usr_uid,lst_seq_no_dtm,cycle_yyyy_yn, cycle_yyyymm_yn,cycle_yyyymmdd_yn,rmk)
VALUES 
('CM_USER_USER_NO_SEQ',1,'USER_NO',NOW(),1,'TB_CM_USER',NOW(),'1ece57a644176ec38de4fd351420205c','1ece57a644176ec38de4fd351420205c',NOW(),'N','N','N','사용자테이블채번')
ON CONFLICT (seq_nm) DO NOTHING;
