
CREATE TABLE IF NOT EXISTS tb_cm_menu
(
    menu_cd character varying(50) ,
    menu_kind character varying(1) ,
    menu_lvl character varying(50) ,
    menu_nm character varying(100) ,
    menu_path character varying(1000) ,
    ord character varying(5) ,
    pgm_id character varying(50) ,
    prnt_menu_cd character varying(50) ,
    rmk character varying(4000),
    crt_user_no bigint,
    updt_user_no bigint,
    crt_dtm timestamp without time zone NOT NULL,
    updt_dtm timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_menu_pkey PRIMARY KEY (menu_cd)
);
comment on table tb_cm_menu is '메뉴';

comment on column tb_cm_menu.menu_cd is '메뉴코드';
comment on column tb_cm_menu.menu_kind is '메뉴종류';
comment on column tb_cm_menu.menu_lvl is '메뉴레벨';
comment on column tb_cm_menu.menu_nm is '메뉴명';
comment on column tb_cm_menu.menu_path is '메뉴path';

comment on column tb_cm_menu.ord is '정렬';
comment on column tb_cm_menu.pgm_id is '프로그램ID';
comment on column tb_cm_menu.prnt_menu_cd is '상위메뉴';
comment on column tb_cm_menu.rmk is '비고';
comment on column tb_cm_menu.crt_user_no is '생성자no';
comment on column tb_cm_menu.updt_user_no is '수정자no';
comment on column tb_cm_menu.crt_dtm is '생성일';
comment on column tb_cm_menu.updt_dtm is '수정일';