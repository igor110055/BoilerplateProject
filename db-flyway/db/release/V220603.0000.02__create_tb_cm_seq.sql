
CREATE TABLE IF NOT EXISTS tb_cm_seq
(
    seq_nm character varying(50) ,
    allocation_size bigint,
    col_nm character varying(50) ,
    crt_dtm timestamp without time zone NOT NULL,
    seq_no bigint,
    tb_nm character varying(50) ,
    updt_dtm timestamp without time zone NOT NULL,
    crt_usr_no bigint,
    updt_usr_no bigint,
    lst_seq_no_dtm timestamp without time zone NOT NULL,
    cycle_yyyy_yn character varying(1) ,
    cycle_yyyymm_yn character varying(1) ,
    cycle_yyyymmdd_yn character varying(1),
    rmk character varying(300) ,
    CONSTRAINT tb_cm_seq_pkey PRIMARY KEY (seq_nm)
);
comment on table tb_cm_seq is '시퀀스';

comment on column tb_cm_seq.seq_nm is '시퀀스명';
comment on column tb_cm_seq.allocation_size is '증가값';
comment on column tb_cm_seq.col_nm is '컬럼명';
comment on column tb_cm_seq.crt_dtm is '생성일';
comment on column tb_cm_seq.seq_no is '시퀀스값';
comment on column tb_cm_seq.tb_nm is '테이블명';
comment on column tb_cm_seq.updt_dtm is '수정일';
comment on column tb_cm_seq.crt_usr_no is '생성자no';
comment on column tb_cm_seq.updt_usr_no is '수정자no';
comment on column tb_cm_seq.cycle_yyyy_yn is '년도-반복';
comment on column tb_cm_seq.cycle_yyyymm_yn is '월-반복';
comment on column tb_cm_seq.cycle_yyyymmdd_yn is '일-반복';
comment on column tb_cm_seq.rmk is '비고';
