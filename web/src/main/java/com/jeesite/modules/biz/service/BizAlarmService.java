/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.biz.dao.BizAlarmDao;
import com.jeesite.modules.biz.entity.BizAlarm;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 场所表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service
@Transactional(readOnly=true)
public class BizAlarmService extends CrudService<BizAlarmDao, BizAlarm> {
	
	/**
	 * 获取单条数据
	 * @param bizPlace
	 * @return
	 */
	@Override
	public BizAlarm get(BizAlarm bizAlarm) {
		return super.get(bizAlarm);
	}
	/**
	 * 获取单条数据
	 * @param bizPlace
	 * @return
	 */
	public List<Map<String, Object>> queryMap(Map<String,Object> param) {
		return this.dao.queryMap(param);
	}
	/**
	 * 查询分页数据
	 * @param bizPlace 查询条件
	 * @param bizPlace.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizAlarm> findPage(BizAlarm bizAlarm) {
		return super.findPage(bizAlarm);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param bizPlace
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizAlarm bizAlarm) {
		super.save(bizAlarm);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(bizAlarm.getId(), "bizAlarm_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(bizAlarm.getId(), "bizAlarm_file");
	}
	
	/**
	 * 更新状态
	 * @param bizAlarm
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizAlarm bizAlarm) {
		super.updateStatus(bizAlarm);
	}
	
	/**
	 * 删除数据
	 * @param bizAlarm
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizAlarm bizAlarm) {
		super.delete(bizAlarm);
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
			List<BizAlarm> list = ei.getDataList(BizAlarm.class);
			for (BizAlarm bizAlarm : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(bizAlarm);
					// 验证是否存在这个用户
					BizAlarm b=this.get(bizAlarm.getAlarmCode());
					if(b==null) {
						bizAlarm.setIsNewRecord(true);
						this.save(bizAlarm);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + bizAlarm.getAlarmCode() + " 导入成功");
					}else if (isUpdateSupport){
//						ei.getDataRowNum()
						this.save(b);
						successNum++;
						successMsg.append("<br/>" + successNum + "、账号 " + bizAlarm.getAlarmCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、账号 " + bizAlarm.getAlarmCode()+ " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、账号 " + bizAlarm.getAlarmCode()+ " 导入失败：";
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