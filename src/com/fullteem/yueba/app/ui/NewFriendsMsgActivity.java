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
package com.fullteem.yueba.app.ui;

import java.util.List;

import android.view.View;
import android.widget.ListView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.NewFriendsMsgAdapter;
import com.fullteem.yueba.db.InviteMessgeDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.InviteMessage;

/**
 * 申请与通知
 * 
 */
public class NewFriendsMsgActivity extends BaseActivity {
	public NewFriendsMsgActivity() {
		super(R.layout.activity_new_friends_msg);
	}

	private ListView listView;

	public void back(View view) {
		finish();
	}

	@Override
	public void initViews() {
		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		// 设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		appContext.getContactList().get(GlobleConstant.NEW_FRIENDS_USERNAME)
				.setUnreadMsgCount(0);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

}
