/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.biz.dao.BizRtspUrlDao;
import com.jeesite.modules.biz.entity.BizRtspUrl;

/**
 * 场所表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service
@Transactional(readOnly=true)
public class BizRtspUrlService extends CrudService<BizRtspUrlDao, BizRtspUrl> {
	
	/**
	 * 获取单条数据
	 * @param bizAlarm
	 * @return
	 */
	@Override
	public BizRtspUrl get(BizRtspUrl bizRtspUrl) {
		return super.get(bizRtspUrl);
	}
	/**
	 * 查询分页数据
	 * @param bizAlarm 查询条件
	 * @param bizAlarm.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizRtspUrl> findPage(BizRtspUrl bizRtspUrl) {
		return super.findPage(bizRtspUrl);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizAlarm
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizRtspUrl bizRtspUrl) {
		super.save(bizRtspUrl);
	}
	
	/**
	 * 更新状态
	 * @param bizAlarm
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizRtspUrl bizRtspUrl) {
		super.updateStatus(bizRtspUrl);
	}
	
	/**
	 * 删除数据
	 * @param bizAlarm
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizRtspUrl bizRtspUrl) {
		super.delete(bizRtspUrl);
	}
}