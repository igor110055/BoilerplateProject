
CREATE TABLE IF NOT EXISTS tb_cm_bizactor_log
(
    seq bigint ,
    bizactor_log_id character varying(50),
    act_id character varying(200),
    in_dt_name character varying(200),
    out_dt_name character varying(200),
    in_json_str text,
    out_json_str text,
    in_ds_str text,
    out_ds_str text,

    gap bigint,
    client_ip character varying(20),
    err_str text,
    log_date timestamp without time zone NOT NULL,
    CONSTRAINT tb_cm_bizactor_log_pkey PRIMARY KEY (bizactor_log_id)
);
comment on table tb_cm_bizactor_log is '비즈액터로그';

comment on column tb_cm_bizactor_log.seq is '일련번호';
comment on column tb_cm_bizactor_log.bizactor_log_id is 'uuid';
comment on column tb_cm_bizactor_log.act_id is 'BR명';
comment on column tb_cm_bizactor_log.in_dt_name is '인풋-테이블명';
comment on column tb_cm_bizactor_log.out_dt_name is '아웃풋-테이블명';
comment on column tb_cm_bizactor_log.in_json_str is '인풋-데이터JSON';
comment on column tb_cm_bizactor_log.out_json_str is '아웃풋-데이터JSON';
comment on column tb_cm_bizactor_log.in_json_str is '인풋-데이터JSON';
comment on column tb_cm_bizactor_log.in_ds_str is '인풋-DS XML';
comment on column tb_cm_bizactor_log.out_ds_str is '아웃풋-DS XML';
comment on column tb_cm_bizactor_log.gap is '실행시간';
comment on column tb_cm_bizactor_log.client_ip is '클라이언트 IP';
comment on column tb_cm_bizactor_log.err_str is '에러 메시지';
comment on column tb_cm_bizactor_log.log_date is '기록 시간';
