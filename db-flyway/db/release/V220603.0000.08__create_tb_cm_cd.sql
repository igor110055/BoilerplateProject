
CREATE TABLE IF NOT EXISTS tb_cm_cd
(
    grp_cd character varying(100) ,
    cd character varying(100) ,
    cd_nm character varying(100) ,
    ord character varying(5) ,
    rmk character varying(4000),
    use_yn character varying(1) ,

    attr1 character varying(100) ,
    attr2 character varying(100) ,
    attr3 character varying(100) ,

    crt_user_no bigint,
    updt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_cd_pkey PRIMARY KEY (grp_cd,cd)
);
comment on table tb_cm_cd is '공통코드';

comment on column tb_cm_cd.grp_cd is '코드그룹';
comment on column tb_cm_cd.cd is '코드명';
comment on column tb_cm_cd.cd_nm is '코드명';
comment on column tb_cm_cd.ord is '정렬';
comment on column tb_cm_cd.rmk is '비고';
comment on column tb_cm_cd.use_yn is '사용여부';
comment on column tb_cm_cd.crt_user_no is '생성자no';
comment on column tb_cm_cd.updt_user_no is '수정자no';
comment on column tb_cm_cd.crt_dtm is '생성일';
comment on column tb_cm_cd.updt_dtm is '수정일';