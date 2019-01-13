/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizPlace;

/**
 * 场所表DAO接口
 * @author 长江
 * @version 2019-01-12
 */
@MyBatisDao
public interface BizPlaceDao extends CrudDao<BizPlace> {
	
}