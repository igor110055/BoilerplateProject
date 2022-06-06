
CREATE TABLE IF NOT EXISTS tb_cm_brd_grp
(
    grp_uid character varying(32),
    grp_nm character varying(50),
    ord character varying(5),
    rmk character varying(300),
    use_yn character varying(1),    
    
    crt_user_uid character varying(32),
    updt_user_uid character varying(32),
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_brd_grp_pkey PRIMARY KEY (grp_uid)
);
comment on table tb_cm_brd_grp is '게시판관리';


comment on column tb_cm_brd_grp.grp_uid is '게시판그룹uid';
comment on column tb_cm_brd_grp.grp_nm is '게시판그룹명';
comment on column tb_cm_brd_grp.ord is '정렬';
comment on column tb_cm_brd_grp.rmk is '비고';
comment on column tb_cm_brd_grp.use_yn is '사용여부';
comment on column tb_cm_brd_grp.crt_user_uid is '생성자uid';
comment on column tb_cm_brd_grp.updt_user_uid is '수정자uid';
comment on column tb_cm_brd_grp.crt_dtm is '생성일';
comment on column tb_cm_brd_grp.updt_dtm is '수정일';
