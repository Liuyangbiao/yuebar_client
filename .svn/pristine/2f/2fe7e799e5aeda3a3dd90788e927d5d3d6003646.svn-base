package com.fullteem.yueba.util;

import java.util.Map;

import com.fullteem.yueba.app.AppContext;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
/**
 * 友盟统计工具类
 * @author jecinwang
 *
 */
public class UmengUtil {
	
	/**
	 * 统计友盟自定义事件
	 * @param context 上下文环境
	 * @param eventId 事件ID
	 */
	public static void onEvent(Context context, String eventId){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onEvent(context,eventId);
		}
	}
	
	/**
	 * 统计友盟自定义事件 行为属性
	 * @param context 上下文环境
	 * @param eventId 事件ID
	 */
	public static void onEvent(Context context, String eventId,Map<String, String> map){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onEvent(context,eventId,map);
		}
	}
	
	/**
	 * 统计时长  在activity的onResume中调用
	 * @param context
	 */
	public static void onResume(Context context){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onResume(context);;
		}
	}
	
	/**
	 * 统计时长  在activity的onPause中调用
	 * @param context
	 */
	public static void onPause(Context context){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onPause(context);;
		}
	}
	
	/**
	 * 统计时长  在BaseActivity的onResume中调用
	 * @param context
	 */
	public static void onBaseResume(Context context){
			MobclickAgent.onResume(context);;
	}
	
	/**
	 * 统计时长  在BaseActivity的onPause中调用
	 * @param context
	 */
	public static void onBasePause(Context context){
			MobclickAgent.onPause(context);;
	}
	
	/**
	 * 统计页面 在activity/Fragment中的onResume中调用
	 * @param context
	 */
	public static void onPageStart(Context context,String type){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onPageStart(type);
		}
	}
	
	/**
	 * 统计页面 在activity/Fragment中的onPause中调用
	 * @param context
	 */
	public static void onPageEnd(Context context,String type){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onPageEnd(type);
		}
	}
	
	/**
	 * 统计页面 在LoginActivity的onResume中调用
	 * @param context
	 */
	public static void onLoginPageStart(Context context,String type){
			MobclickAgent.onPageStart(type);
	}
	
	/**
	 * 统计页面 在LoginActivity的onPause中调用
	 * @param context
	 */
	public static void onLoginPageEnd(Context context,String type){
			MobclickAgent.onPageEnd(type);
	}
	
	
	/**
	 * 是否禁止默认的方式开始统计页面，在程序入口出调用
	 * @param context 上下文环境
	 * @param keepDefault 是否默认方式开始统计
	 */
	public static void openActivityDurationTrack(Context context,boolean keepDefault){
		MobclickAgent.openActivityDurationTrack(keepDefault);
	}
	
	/**
	 * 如果开发者调用Process.kill或者System.exit之类的方法杀死进程
	 * 调用此方法保存数据
	 * @param context 上下文环境
	 */
	public static void onKillProcess(Context context){
		AppContext appContext = (AppContext) context.getApplicationContext();
		int state = appContext.getServerHostData().getEUmeng();
		if(state != 0){
			MobclickAgent.onKillProcess(context);
		}
	}

	/**
	 * 产生数据发回到友盟服务器的频率
	 * @param context
	 */
	public static void updateOnlineConfig(Context context) {
			MobclickAgent.updateOnlineConfig(context);
	}
}
