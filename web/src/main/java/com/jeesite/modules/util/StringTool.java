package com.jeesite.modules.util;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
public class StringTool {

	public static String addEle(String base,String add) {
		if(StringUtils.isBlank(base)) {
			return add==null?null:add.trim();
		}
		if(StringUtils.isBlank(add)) {
			return base.trim();
		}
		String[] r=addEle(base.split(","),add.split(","));
		
		return String.join(",",r);
	}
	public static String[] addEle(String[] base,String add[]) {
		if(base==null) {
			return add;
		}
		if(add==null) {
			return base;
		}
		HashSet<String> baseset = new HashSet<>(Arrays.asList(base));
		HashSet<String> addset = new HashSet<>(Arrays.asList(add));
		baseset.addAll(addset);
		return baseset.toArray(new String[0]);
	}
}
