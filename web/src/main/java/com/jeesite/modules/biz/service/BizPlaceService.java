/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.dao.BizPlaceDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 场所表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service
@Transactional(readOnly=true)
public class BizPlaceService extends CrudService<BizPlaceDao, BizPlace> {
	
	/**
	 * 获取单条数据
	 * @param bizPlace
	 * @return
	 */
	@Override
	public BizPlace get(BizPlace bizPlace) {
		return super.get(bizPlace);
	}
	
	/**
	 * 查询分页数据
	 * @param bizPlace 查询条件
	 * @param bizPlace.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizPlace> findPage(BizPlace bizPlace) {
		return super.findPage(bizPlace);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizPlace
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizPlace bizPlace) {
		super.save(bizPlace);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(bizPlace.getId(), "bizPlace_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(bizPlace.getId(), "bizPlace_file");
	}
	
	/**
	 * 更新状态
	 * @param bizPlace
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizPlace bizPlace) {
		super.updateStatus(bizPlace);
	}
	
	/**
	 * 删除数据
	 * @param bizPlace
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizPlace bizPlace) {
		super.delete(bizPlace);
	}
	
}