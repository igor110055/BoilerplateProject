
CREATE TABLE IF NOT EXISTS tb_cm_user_role_cd
(
    user_uid character varying(32),
    role_cd character varying(50),

    crt_user_uid character varying(32),
    crt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_user_role_cd_pkey PRIMARY KEY (user_uid,role_cd)
);
comment on table tb_cm_user_role_cd is '사용자역할코드';

comment on column tb_cm_user_role_cd.user_uid is '사용자uid';
comment on column tb_cm_user_role_cd.role_cd is '역할코드';

comment on column tb_cm_user_role_cd.crt_user_uid is '생성자no';
comment on column tb_cm_user_role_cd.crt_dtm is '생성일';
