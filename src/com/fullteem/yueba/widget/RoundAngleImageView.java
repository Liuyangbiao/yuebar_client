package com.fullteem.yueba.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class RoundAngleImageView extends ImageView {
	
	private static final ScaleType SCALE_TYPE = ScaleType.MATRIX;

	private Paint paint;

	public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(SCALE_TYPE);
		paint = new Paint();
	}

	public RoundAngleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setScaleType(SCALE_TYPE);
		paint = new Paint();
	}

	public RoundAngleImageView(Context context) {
		super(context);
		super.setScaleType(SCALE_TYPE);
		paint = new Paint();
	}

	/**
	 * 绘制圆角矩形图片
	 * 
	 * @author caizhiming
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bitmap;
		Drawable drawable = getDrawable();
		if (null != drawable && drawable instanceof BitmapDrawable) {
			bitmap = ((BitmapDrawable) drawable).getBitmap();
		} else {
			//super.onDraw(canvas);
			Bitmap bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight(), Config.ARGB_8888);
			Canvas ca = new Canvas(bm);
			drawable.setBounds(0, 0, ca.getWidth(), ca.getHeight());
			drawable.draw(ca);
			bitmap = bm;
		}
		
		Bitmap b = getRoundBitmap(bitmap, 10);
		final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
		final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
		paint.reset();
		canvas.drawBitmap(b, rectSrc, rectDest, paint);
	}

	/**
	 * 获取圆角矩形图片方法
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            ,一般设置成14
	 * @return Bitmap
	 * @author caizhiming
	 */
	private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		int x = bitmap.getWidth();

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
