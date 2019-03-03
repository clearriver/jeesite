/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.biz.dao.BizMediaServerDao;
import com.jeesite.modules.biz.entity.BizMediaServer;
import com.jeesite.modules.sys.entity.Office;

/**
 * 流媒体服务器管理Service
 * @author 长江
 * @version 2019-03-03
 */
@Service
@Transactional(readOnly=true)
public class BizMediaServerService extends CrudService<BizMediaServerDao, BizMediaServer> {

	/**
	 * 获取单条数据
	 * @param bizMediaServer
	 * @return
	 */
	public List<BizMediaServer> queryBizMediaServer(Map<String,Object> param) {
		return dao.queryBizMediaServer(param);
	}
	
	/**
	 * 获取单条数据
	 * @param bizMediaServer
	 * @return
	 */
	@Override
	public BizMediaServer get(BizMediaServer bizMediaServer) {
		return super.get(bizMediaServer);
	}
	
	/**
	 * 查询分页数据
	 * @param bizMediaServer 查询条件
	 * @param bizMediaServer.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizMediaServer> findPage(BizMediaServer bizMediaServer) {
		return super.findPage(bizMediaServer);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizMediaServer
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizMediaServer bizMediaServer) {
		super.save(bizMediaServer);
	}
	
	/**
	 * 更新状态
	 * @param bizMediaServer
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizMediaServer bizMediaServer) {
		super.updateStatus(bizMediaServer);
	}
	
	/**
	 * 删除数据
	 * @param bizMediaServer
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizMediaServer bizMediaServer) {
		super.delete(bizMediaServer);
	}
	
}