
CREATE TABLE IF NOT EXISTS tb_cm_user
(
    user_no bigint NOT NULL,
    email character varying(100) ,
    email_cert_yn character varying(1) ,
    email_cert_uuid character varying(100) ,
    rmk character varying(300) ,
    use_yn character varying(1) ,
    user_id character varying(100) ,
    user_nm character varying(100) ,
    pwd character varying(500) ,
    salt character varying(100) ,
    nick_nm character varying(100) ,
    birth character varying(8),
    gndr character varying(4),
    crt_user_no bigint,
    updt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    lst_acc_dtm timestamp without time zone,
    CONSTRAINT tb_cm_user_pkey PRIMARY KEY (user_no)
);
comment on table tb_cm_user is '사용자테이블';

comment on column tb_cm_user.user_no is '사용자번호';
comment on column tb_cm_user.email is '이메일';
comment on column tb_cm_user.email_cert_yn is '이메일인증여부';
comment on column tb_cm_user.email_cert_uuid is '이메일인증UUID';
comment on column tb_cm_user.rmk is '비고';
comment on column tb_cm_user.use_yn is '사용여부';
comment on column tb_cm_user.user_id is '사용자ID';
comment on column tb_cm_user.user_nm is '사용자명';
comment on column tb_cm_user.pwd is '패스워드';
comment on column tb_cm_user.salt is 'salt';
comment on column tb_cm_user.nick_nm is '닉네임';
comment on column tb_cm_user.birth is '생년월일';
comment on column tb_cm_user.gndr is '성별';
comment on column tb_cm_user.crt_user_no is '생성자no';
comment on column tb_cm_user.updt_user_no is '수정자no';
comment on column tb_cm_user.crt_dtm is '생성일';
comment on column tb_cm_user.updt_dtm is '수정일';
comment on column tb_cm_user.lst_acc_dtm is '마지막접속일자';
