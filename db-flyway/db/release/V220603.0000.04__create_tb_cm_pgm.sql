
CREATE TABLE IF NOT EXISTS tb_cm_pgm
(
    pgm_id character varying(50) ,
    category character varying(100) ,
    dir_link character varying(1000) ,
    ord character varying(5) ,
    pgm_link character varying(200) ,
    pgm_nm character varying(100) ,
    rmk character varying(300),
    crt_user_uid  character varying(32),
    updt_user_uid  character varying(32),
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_pgm_pkey PRIMARY KEY (pgm_id)
);
comment on table tb_cm_pgm is '프로그램';

comment on column tb_cm_pgm.category is '프로그램category';
comment on column tb_cm_pgm.dir_link is '물리경로';
comment on column tb_cm_pgm.ord is '정렬';
comment on column tb_cm_pgm.pgm_id is '프로그램ID';
comment on column tb_cm_pgm.pgm_link is '프로그램url';
comment on column tb_cm_pgm.pgm_nm is '프로그램명';
comment on column tb_cm_pgm.rmk is '비고';
comment on column tb_cm_pgm.crt_user_uid is '생성자uid';
comment on column tb_cm_pgm.updt_user_uid is '수정자uid';
comment on column tb_cm_pgm.crt_dtm is '생성일';
comment on column tb_cm_pgm.updt_dtm is '수정일';