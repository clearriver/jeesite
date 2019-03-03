SET SESSION FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS js_biz_place;

-- 场所表
CREATE TABLE js_biz_place
(
	place_code varchar(100) NOT NULL COMMENT '许可证号或编号',
	place_name varchar(200) NOT NULL COMMENT '主体名称',
	trade_type varchar(100) NOT NULL COMMENT '行业类型',
	city varchar(100) NOT NULL COMMENT '所属市',
	area varchar(100) NOT NULL COMMENT '所属县（区）',
	street varchar(300) COMMENT '详细地址',
	geo_coordinates varchar(100) COMMENT '地理坐标',
	representative varchar(200) COMMENT '法定代表人（主要负责人）',
	phone varchar(100) COMMENT '移动电话',
	business_status varchar(1) COMMENT '营业状态',
	rtsp_url varchar(100) COMMENT '实时视频流RTSP地址',
	alarm_type varchar(100) COMMENT '报警类型',
	alarm_time datetime COMMENT '报警时间',
	deal_way varchar(100) COMMENT '处置方式',
	oos_url varchar(300) COMMENT '报警视频及图像OSS存储URL地址',
	sign varchar(300) COMMENT '签名',
	PRIMARY KEY (place_code)
) COMMENT = '场所表';


/* Create Indexes */
CREATE INDEX idx_biz_place_pn ON js_biz_place (place_name ASC);

CREATE TABLE `js_biz_alarm` (
  `place_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '许可证号或编号',
  `alarm_code` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编号',
  `alarm_type` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警类型',
  `alarm_time` datetime DEFAULT NULL COMMENT '报警时间',
  `deal_way` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处置方式',
  `oos_url` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警视频及图像OSS存储URL地址',
  `video_url` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警视频及图像URL地址',
  `sign` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  `deal_result` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签名',
  PRIMARY KEY (`alarm_code`),
  KEY `idx_biz_alarm_pn` (`place_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='报警表';


CREATE TABLE `js_biz_rtsp_url` (
  `rtsp_url` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'RTSP_URL',
  `place_code` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '许可证号或编号',
  `online` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否在线',
  PRIMARY KEY (`rtsp_url`),
  KEY `idx_js_biz_rtsp_url_pn` (`rtsp_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='RTSP_URL';


CREATE TABLE `js_biz_media_server` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'id',
  `office` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地区',
  `domain_name` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '域名',
  `server_ip` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'IP',
  `server_port` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '端口',
  `server_type` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务器类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='流媒体服务器管理';

