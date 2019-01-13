/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.jeesite.modules.Application;

/**
 * 初始化核心表数据
 * @author ThinkGem
 * @version 2017-10-22
 */
@ActiveProfiles("test")
@SpringBootTest(classes=Application.class)
@Rollback(false)
public class InitCoreData extends com.jeesite.modules.sys.db.InitCoreData {
	/**
	 可以直接在Eclipse里找到com.jeesite.test.InitCoreData.java文件并打开，然后在空白处右键，
	 点击 Run As -> JUnit Test 运行单元测试，进行初始化数据库脚本。为了防止误操作，你还需要：
	 打开 Run Configurations 找到 Arguments 选项卡，在 VM arguments 里增加 “-Djeesite.initdata=true” 参数，
	 点击Run运行，执行完成后建议将该单元测试 Run Configuration 删除掉，防止误操作，不小心再把你的有用数据清理掉。
	 * */
	@Test
	public void initCoreData() throws Exception{
		createTable();
		initLog();
		initArea("3700","3701","3702");
		initConfig();
		initModule();
		initDict();
		initRole();
		initMenu();
		initUser();
		initOffice();
		initCompany();
		initPost();
		initEmpUser();
		initMsgPushJob();
		initGenTestData();
		initGenTreeData();
	}
	
}
