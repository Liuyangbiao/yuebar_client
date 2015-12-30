package com.fullteem.yueba.app;

import java.io.File;

import android.os.Environment;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 * 
 * @author bin
 * @version 1.0.0
 * @created 2014-2-24
 */

public class AppConfig {

	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	public final static String APP_PATH = "yueba";
	public final static String SAVE_IMAGE_PATH = "imgcache";
	public final static String CONF_VOICE = "perf_voice";
	public final static String DEFAULT_SAVE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ APP_PATH
			+ File.separator;
	public final static String CHATPHOTO_PATH = DEFAULT_SAVE_PATH + "Chat"
			+ File.separator;
}
