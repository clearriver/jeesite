/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.dao.BizPlaceDao;
import com.jeesite.modules.file.utils.FileUploadUtils;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.utils.UserUtils;

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

	/**
	 * 导入用户数据
	 * @param file 导入的用户数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	@Transactional(readOnly=false)
	public String importData(MultipartFile file, Boolean isUpdateSupport) {
		if (file == null){
			throw new ServiceException("请选择导入的数据文件！");
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<BizPlace> list = ei.getDataList(BizPlace.class);
			for (BizPlace bizPlace : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(bizPlace);
					// 验证是否存在这个用户
					BizPlace b=this.get(bizPlace.getPlaceCode());
					if(b==null) {
						bizPlace.setIsNewRecord(true);
						this.save(bizPlace);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + bizPlace.getPlaceCode() + " 导入成功");
					}else if (isUpdateSupport){
//						ei.getDataRowNum()
						this.save(b);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + bizPlace.getPlaceCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、账号 " + bizPlace.getPlaceCode()+ " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、账号 " + bizPlace.getPlaceCode()+ " 导入失败：";
					if (e instanceof ConstraintViolationException){
						List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
						for (String message : messageList) {
							msg += message + "; ";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

}