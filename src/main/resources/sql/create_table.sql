CREATE TABLE rss_source (
  -- RSS源ID，作为主键，自增
  rss_id INT AUTO_INCREMENT PRIMARY KEY,
  -- RSS源的名称，非空
  rss_name VARCHAR(255) NOT NULL,
  -- RSS源的地址，非空
  rss_url VARCHAR(255) NOT NULL,
  -- RSS源的类型，分为文章和视频两种，非空，1代表文章，2代表视频
  rss_type INT NOT NULL COMMENT '1 for article, 2 for video',
  -- RSS源的XML数据，可空
  rss_xml_data MEDIUMTEXT
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
