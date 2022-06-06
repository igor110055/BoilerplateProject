
CREATE TABLE IF NOT EXISTS tb_cm_brd_cmt
(
    brd_uid character varying(32),
    cmt_uid character varying(32),
    cmt_dpth bigint,
    cmt_rply_ord bigint,
    cntnt text,
    del_yn character varying(1),    
    dslk_cnt bigint,
    lk_cnt bigint,
    prnt_cmt_uid character varying(32),    
    root_cmt_uid character varying(32),    
    
    crt_user_uid character varying(32),    
    updt_user_uid character varying(32),    
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_brd_cmt_pkey PRIMARY KEY (cmt_uid),
    CONSTRAINT tb_cm_brd_cmt_fkey_brd_uid FOREIGN KEY(brd_uid) REFERENCES tb_cm_brd(brd_uid)
);
comment on table tb_cm_brd_cmt is '게시판댓글';


comment on column tb_cm_brd_cmt.brd_uid is '게시물uid';
comment on column tb_cm_brd_cmt.cmt_uid is '댓글uid';
comment on column tb_cm_brd_cmt.cmt_dpth is '댓글depth';
comment on column tb_cm_brd_cmt.cmt_rply_ord is '댓글ord';
comment on column tb_cm_brd_cmt.del_yn is '삭제여부';
comment on column tb_cm_brd_cmt.dslk_cnt is '싫어요';
comment on column tb_cm_brd_cmt.lk_cnt is '좋아요';
comment on column tb_cm_brd_cmt.prnt_cmt_uid is '부모댓글uid';
comment on column tb_cm_brd_cmt.root_cmt_uid is 'root댓글uid';

comment on column tb_cm_brd_cmt.crt_user_uid is '생성자uid';
comment on column tb_cm_brd_cmt.updt_user_uid is '수정자uid';
comment on column tb_cm_brd_cmt.crt_dtm is '생성일';
comment on column tb_cm_brd_cmt.updt_dtm is '수정일';
