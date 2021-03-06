/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fullteem.yueba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 评论用expandlistview
 * 
 * @author ssy
 * 
 */
public class CommentExpandListView extends ListView {
	// 最多显示的item个数
	private int MAXCOUNT = 5;

	public CommentExpandListView(Context context) {
		super(context);
	}

	public CommentExpandListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommentExpandListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	private void initChildHight() {
		if (getAdapter() == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = getAdapter().getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目

			View listItem = getAdapter().getView(i, null, this);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = getLayoutParams();
		params.height = totalHeight
				+ (getDividerHeight() * (getAdapter().getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		this.setLayoutParams(params);
	}

}
