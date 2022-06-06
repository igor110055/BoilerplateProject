
CREATE TABLE IF NOT EXISTS tb_cm_brd
(
    grp_uid character varying(32),
    brd_uid character varying(32),
    brd_dpth bigint,
    brd_rply_ord bigint,
    ttl  character varying(300),
    cntnt text,
    del_yn character varying(1),    
    dslk_cnt bigint,
    lk_cnt bigint,
    prnt_brd_uid character varying(32),
    root_brd_uid character varying(32),
    
    crt_user_uid character varying(32),
    updt_user_uid  character varying(32),
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_brd_pkey PRIMARY KEY (brd_uid),
    CONSTRAINT tb_cm_brd_fkey_grp_uid FOREIGN KEY(grp_uid) REFERENCES tb_cm_brd_grp(grp_uid)
);
comment on table tb_cm_brd is '게시판';


comment on column tb_cm_brd.grp_uid is '게시판그룹uid';
comment on column tb_cm_brd.brd_uid is '게시물물uid';
comment on column tb_cm_brd.brd_dpth is '게시물답글depth';
comment on column tb_cm_brd.brd_rply_ord is '게시물답글ord';
comment on column tb_cm_brd.ttl is '제목';
comment on column tb_cm_brd.del_yn is '삭제여부';
comment on column tb_cm_brd.dslk_cnt is '싫어요';
comment on column tb_cm_brd.lk_cnt is '좋아요';
comment on column tb_cm_brd.prnt_brd_uid is '부모게시물uid';
comment on column tb_cm_brd.root_brd_uid is 'root게시물uid';


comment on column tb_cm_brd.crt_user_uid is '생성자uid';
comment on column tb_cm_brd.updt_user_uid is '수정자uid';
comment on column tb_cm_brd.crt_dtm is '생성일';
comment on column tb_cm_brd.updt_dtm is '수정일';
