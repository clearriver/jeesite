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
	PRIMARY KEY (place_code)
) COMMENT = '场所表';


/* Create Indexes */
CREATE INDEX idx_biz_place_pn ON js_biz_place (place_name ASC);