
CREATE TABLE IF NOT EXISTS tb_cm_user_role_cd
(
    user_no bigint,
    role_cd character varying(50),

    crt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_user_role_cd_pkey PRIMARY KEY (user_no,role_cd)
);
comment on table tb_cm_user_role_cd is '사용자역할코드';

comment on column tb_cm_user_role_cd.user_no is '사용자No';
comment on column tb_cm_user_role_cd.role_cd is '역할코드';

comment on column tb_cm_user_role_cd.crt_user_no is '생성자no';
comment on column tb_cm_user_role_cd.crt_dtm is '생성일';
