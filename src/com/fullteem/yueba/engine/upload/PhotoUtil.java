package com.fullteem.yueba.engine.upload;

import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.util.LogUtil;

public class PhotoUtil {
	public final static int HEADER_CODE = 201;
	public final static int USER_PHOTO = 202;
	public final static int UPLOAD_FAIL = 210;

	public final static String USER_PHOTO_PATH = GlobleConstant.USERPHOTO_DIR
			+ GlobleConstant.USERPHOTO_NAME;

	public final static int MAX_UPLOAD_PHOTO_NUM = 20;

	public static void printTrace(String content) {
		LogUtil.LogDebug("PhotoHandle", content, null);
	}

	public static String filterUserLogoUrl(String userLogoUrl) {
		String str = null;

		printTrace("user logo url:" + userLogoUrl);
//		if (Urls.isMumuBarHost()) {
//			
//		} else {
//			str = userLogoUrl;
//		}
//		所有上传图片都不需要加前缀
		if (userLogoUrl.contains(Urls.IMAGEHOST)) {
			str = userLogoUrl.replace(Urls.IMAGEHOST, "");
			printTrace("after handled, user logo url:" + str);
		}
		return str;
	}
}
