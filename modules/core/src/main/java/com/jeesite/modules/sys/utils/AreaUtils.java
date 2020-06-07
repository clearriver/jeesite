/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;

/**
 * 
 * @author ThinkGem
 * @version 2017年3月1日
 */
public class AreaUtils {

	// 系统缓存常量
	public static final String CACHE_AREA_ALL_LIST = "areaAllList";
    public static final String CACHE_AREA_ALL_MAP = "areaAllMap";
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static AreaService areaService = SpringUtils.getBean(AreaService.class);
	}
	
	/**
	 * 获取所有区域列表（系统级别缓存）
	 * @return
	 */
	public static List<Area> getAreaAllList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)CacheUtils.get(CACHE_AREA_ALL_LIST);
		if (areaList == null){
			areaList = Static.areaService.findList(new Area());
			CacheUtils.put(CACHE_AREA_ALL_LIST, areaList);
		}
		return areaList;
	}
	public static Area getArea(String areaCode){
	        @SuppressWarnings("unchecked")
	        Map<String,Area> areaMap = (Map<String,Area>)CacheUtils.get(CACHE_AREA_ALL_MAP);
	        if (areaMap == null){
	          List<Area> areaList = (List<Area>)CacheUtils.get(CACHE_AREA_ALL_LIST);
	          if (areaList == null){
	              areaList = Static.areaService.findList(new Area());
	              CacheUtils.put(CACHE_AREA_ALL_LIST, areaList);
	          }
	          areaMap=areaList.stream().collect(Collectors.toMap(e->e.getAreaCode(),e->e,(k1,k2)->k2));
	          CacheUtils.put(CACHE_AREA_ALL_MAP, areaMap);
	        }
	        return areaMap.get(areaCode);
	}
	/**
	 * 清理区域缓存
	 */
	public static void clearCache(){
		CacheUtils.remove(CACHE_AREA_ALL_LIST);
	}
	
}
