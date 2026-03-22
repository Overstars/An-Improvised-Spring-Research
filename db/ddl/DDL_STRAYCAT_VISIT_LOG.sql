
CREATE TABLE straycat_visit_log (
    id VARCHAR(64) PRIMARY KEY NOT NULL,  -- UUID主键
    ip VARCHAR(45) NOT NULL,                        -- IPv4/IPv6最大长度45
    url TEXT,                               -- 访问路径（支持长URL）
    referrer TEXT,                                   -- 来源页面（允许NULL）
    ua TEXT,                                -- UserAgent原始文本
    screen VARCHAR(20),                              -- 分辨率（如"1920x1080"）
    lang VARCHAR(10),                                -- 语言代码（如"en-US"）
    create_time TIMESTAMPTZ NOT NULL DEFAULT NOW(),     -- 带时区的时间戳
    update_time TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 创建索引优化常用查询
CREATE INDEX idx_straycat_visit_log_create_time ON straycat_visit_log (create_time);
CREATE INDEX idx_straycat_visit_log_ip ON straycat_visit_log (ip);
CREATE INDEX idx_straycat_visit_log_id ON straycat_visit_log (id);

-- 创建触发函数 --
CREATE OR REPLACE FUNCTION update_modified_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = now();
RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建触发器  on 后面是对应数据库的表名--
CREATE TRIGGER update_customer_modtime BEFORE UPDATE ON straycat_visit_log FOR EACH ROW EXECUTE PROCEDURE update_modified_column();