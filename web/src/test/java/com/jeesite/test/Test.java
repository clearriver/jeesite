package com.jeesite.test;

public class Test {

	public static void main(String[] args) {
//		String key=com.jeesite.modules.sys.utils.ConfigUtils.ALLATORIxDEMO(";E;\18=O-NfU&U<l)O;K'N,");
//		System.out.println(com.jeesite.common.config.Global.getConfig(key));
		com.jeesite.modules.sys.utils.PwdUtils.encryptPassword("123456");
		com.jeesite.modules.sys.utils.PwdUtils.validatePassword("123456","00b43bc8445d049e8b4f1dcb63c3fbfd366bd05e4abbe3665b492cc2");
	}

}
