package com.fullteem.yueba.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月26日&emsp;上午10:44:59
 * @use 播放音效
 */
public class SoundPoolUtil {
	private static SoundPool pool;

	public static void playSound(Context context, int resId) {
		// 指定声音池的最大音频流数目为1，声音品质为5
		pool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
		// 载入音频流，返回在池中的id
		final int sourceid = pool.load(context, resId, 0);

		// 如果SoundPool刚调完加载load函数之后，直接调用SoundPool的play函数可能出现 error
		// "sample 1 not READY"
		pool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// 播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度
				pool.play(sourceid, 1, 1, 0, 0, 1.2f);
			}
		});
	}
}
