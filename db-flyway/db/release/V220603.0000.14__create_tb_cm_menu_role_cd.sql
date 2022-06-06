
CREATE TABLE IF NOT EXISTS tb_cm_menu_role_cd
(
    role_cd character varying(50) ,
    menu_cd character varying(50),

    crt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_menu_role_cd_pkey PRIMARY KEY (role_cd,menu_cd)
);
comment on table tb_cm_menu_role_cd is '메뉴역할코드';

comment on column tb_cm_menu_role_cd.role_cd is '역할코드';
comment on column tb_cm_menu_role_cd.menu_cd is '메뉴코드';
comment on column tb_cm_menu_role_cd.crt_user_no is '생성자no';
comment on column tb_cm_menu_role_cd.crt_dtm is '생성일';
