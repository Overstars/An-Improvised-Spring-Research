-- auto-generated definition
DROP TABLE IF EXISTS public.straycat_user_info;
create table if not exists
    straycat_user_info
    (
        USER_ID     varchar(30) NOT NULL primary key,
        LOGIN_NAME  varchar(30) NOT NULL,
        CELL_PHONE  varchar(30)          DEFAULT NULL,
        CREATE_TIME timestamp   NOT NULL DEFAULT now(),
        UPDATE_TIME timestamp            DEFAULT now()
);
COMMENT ON COLUMN straycat_user_info.USER_ID IS '用户ID';
COMMENT ON COLUMN straycat_user_info.LOGIN_NAME IS '登录名';
COMMENT ON COLUMN straycat_user_info.CELL_PHONE IS '手机号码';
COMMENT ON COLUMN straycat_user_info.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN straycat_user_info.UPDATE_TIME IS '更新时间';


-- 创建触发函数 --
CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建触发器  on 后面是对应数据库的表名--
CREATE TRIGGER update_customer_modtime BEFORE UPDATE ON straycat_user_info FOR EACH ROW EXECUTE PROCEDURE update_modified_column();