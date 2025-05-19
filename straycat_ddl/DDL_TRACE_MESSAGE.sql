-- auto-generated definition
DROP TABLE IF EXISTS public.straycat_trace_message;
create table if not exists
    straycat_trace_message (
        TRACENO varchar(64) NOT NULL primary key,
        CREATE_TIME timestamp NOT NULL DEFAULT now(),
        UPDATE_TIME timestamp DEFAULT now()
    );
COMMENT ON COLUMN straycat_trace_message.TRACENO IS '交易流水号';
COMMENT ON COLUMN straycat_trace_message.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN straycat_trace_message.UPDATE_TIME IS '更新时间';

-- 创建触发函数 --
CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建触发器  on 后面是对应数据库的表名--
CREATE TRIGGER update_customer_modtime BEFORE UPDATE ON straycat_trace_message FOR EACH ROW EXECUTE PROCEDURE update_modified_column();