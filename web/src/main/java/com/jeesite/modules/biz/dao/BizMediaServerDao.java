/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.dao;

import java.util.List;
import java.util.Map;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizMediaServer;
import com.jeesite.modules.sys.entity.Office;

/**
 * 流媒体服务器管理DAO接口
 * @author 长江
 * @version 2019-03-03
 */
@MyBatisDao
public interface BizMediaServerDao extends CrudDao<BizMediaServer> {
	public List<BizMediaServer> queryBizMediaServer(Map<String,Object> param);
}