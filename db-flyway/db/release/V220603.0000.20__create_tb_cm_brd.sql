
CREATE TABLE IF NOT EXISTS tb_cm_brd
(
    grp_cd character varying(50),
    brd_seq bigint,
    brd_dpth bigint,
    brd_rply_ord bigint,
    ttl  character varying(300),
    cntnt text,
    del_yn character varying(1),    
    dslk_cnt bigint,
    lk_cnt bigint,
    prnt_brd_seq bigint,
    root_brd_seq bigint,
    
    crt_user_no bigint,
    updt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_brd_pkey PRIMARY KEY (brd_seq),
    CONSTRAINT tb_cm_brd_fkey_grp_cd FOREIGN KEY(grp_cd) REFERENCES tb_cm_brd_grp(grp_cd)
);
comment on table tb_cm_brd is '게시판';


comment on column tb_cm_brd.grp_cd is '게시판그룹코드';
comment on column tb_cm_brd.brd_seq is '게시물일련번호';
comment on column tb_cm_brd.brd_dpth is '게시물답글depth';
comment on column tb_cm_brd.brd_rply_ord is '게시물답글ord';
comment on column tb_cm_brd.ttl is '제목';
comment on column tb_cm_brd.del_yn is '삭제여부';
comment on column tb_cm_brd.dslk_cnt is '싫어요';
comment on column tb_cm_brd.lk_cnt is '좋아요';
comment on column tb_cm_brd.prnt_brd_seq is '부모게시물';
comment on column tb_cm_brd.root_brd_seq is 'root게시물';


comment on column tb_cm_brd.crt_user_no is '생성자no';
comment on column tb_cm_brd.updt_user_no is '수정자no';
comment on column tb_cm_brd.crt_dtm is '생성일';
comment on column tb_cm_brd.updt_dtm is '수정일';
