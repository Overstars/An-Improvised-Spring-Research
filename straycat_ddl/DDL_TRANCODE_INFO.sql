create table if not exists
    trancode_info (
        TRANCODE varchar(64) NOT NULL primary key,
        SYSTEM varchar(64) NOT NULL,
        CREATE_TIME timestamp NOT NULL DEFAULT now(),
        UPDATE_TIME timestamp DEFAULT now()
    );
-- 关联外系统操作、后台微服务名称、登记日期、开发人、开发CQ单 --
COMMENT ON COLUMN trancode_info.TRANCODE IS '交易码';
COMMENT ON COLUMN trancode_info.SYSTEM IS '目标系统';
COMMENT ON COLUMN trancode_info.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN trancode_info.UPDATE_TIME IS '更新时间';