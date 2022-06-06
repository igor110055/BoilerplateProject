
CREATE TABLE IF NOT EXISTS tb_cm_menu_role_cd
(
    role_cd character varying(50) ,
    menu_cd character varying(50),

    crt_user_uid character varying(32),
    crt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_menu_role_cd_pkey PRIMARY KEY (role_cd,menu_cd)
);
comment on table tb_cm_menu_role_cd is '메뉴역할코드';

comment on column tb_cm_menu_role_cd.role_cd is '역할코드';
comment on column tb_cm_menu_role_cd.menu_cd is '메뉴코드';
comment on column tb_cm_menu_role_cd.crt_user_uid is '생성자uid';
comment on column tb_cm_menu_role_cd.crt_dtm is '생성일';
