
CREATE TABLE IF NOT EXISTS tb_cm_brd_cmt
(
    brd_seq bigint,
    cmt_seq bigint,
    cmt_dpth bigint,
    cmt_rply_ord bigint,
    cntnt text,
    del_yn character varying(1),    
    dslk_cnt bigint,
    lk_cnt bigint,
    prnt_cmt_seq bigint,
    root_cmt_seq bigint,
    
    crt_user_no bigint,
    updt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_brd_cmt_pkey PRIMARY KEY (cmt_seq),
    CONSTRAINT tb_cm_brd_cmt_fkey_brd_seq FOREIGN KEY(brd_seq) REFERENCES tb_cm_brd(brd_seq)
);
comment on table tb_cm_brd_cmt is '게시판댓글';


comment on column tb_cm_brd_cmt.brd_seq is '게시물일련번호';
comment on column tb_cm_brd_cmt.cmt_seq is '댓글일련번호';
comment on column tb_cm_brd_cmt.cmt_dpth is '댓글depth';
comment on column tb_cm_brd_cmt.cmt_rply_ord is '댓글ord';
comment on column tb_cm_brd_cmt.del_yn is '삭제여부';
comment on column tb_cm_brd_cmt.dslk_cnt is '싫어요';
comment on column tb_cm_brd_cmt.lk_cnt is '좋아요';
comment on column tb_cm_brd_cmt.prnt_cmt_seq is '부모댓글';
comment on column tb_cm_brd_cmt.root_cmt_seq is 'root댓글';

comment on column tb_cm_brd_cmt.crt_user_no is '생성자no';
comment on column tb_cm_brd_cmt.updt_user_no is '수정자no';
comment on column tb_cm_brd_cmt.crt_dtm is '생성일';
comment on column tb_cm_brd_cmt.updt_dtm is '수정일';
