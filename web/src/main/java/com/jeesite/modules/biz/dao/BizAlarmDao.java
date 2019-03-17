/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.dao;

import java.util.List;
import java.util.Map;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizAlarm;

/**
 * 场所表DAO接口
 * 
 * @author 长江
 * @version 2019-01-12
 */
@MyBatisDao
public interface BizAlarmDao extends CrudDao<BizAlarm> {
	public List<Map<String, Object>> queryMap(Map<String,Object> param);
	public Long queryCount(Map<String,Object> param);
	public List<Map<String, Object>> queryByTradeTypeGroup(Map<String,Object> param);
	public List<Map<String, Object>> countByTradeTypeGroup(Map<String,Object> param);
}