package com.fullteem.yueba.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseListAdapter;

/**
 * 下拉选择框，满足应用里面一些需要用到下拉控件的弹窗
 * 
 * @author ssy
 * 
 */
public class ChoseDialog extends Activity {

	private TextView tvTitle;
	private ListView listViewChose;
	private Button btnOK;
	private List<String> strList;
	private Intent intent;
	private PullDownAdapter pullAdapter;

	// 标题
	public static final String TITLE = "title";

	// 多选与单选
	public static final String CHOSE_TYPE = "chosetype";

	// 下拉type
	public static final String PULL_TYPE = "pulltype";

	// 已选中的字段
	public static final String CHOSED_TEXT = "chosedtext";

	// 单选
	public static final int SINGLE_TYPE = 0;

	// 多选
	public static final int MORE_TYPE = 1;

	// 是否单选
	private boolean isSingleChose;

	// 下拉布局形式
	private int pullStyle;

	// 星座
	public static final int CONSTELLATION = 101;
	// 行业
	public static final int INDUSTRY = 102;
	// 爱好
	public static final int HOBBY = 103;
	// 音乐
	public static final int MUSIC_STYLE = 104;

	// 返回字段
	public static final int RETUEN_STR_CODE = 201;

	// 传递list
	public static final String INTENT_LIST = "strlist";

	private String[] dataArray;

	private String chosedText = "";

	// 记录上一个记录下的view
	private ViewGroup lastViewGroup;

	// 传递进来的StringList
	private List<String> intentList;

	// 返回选中的id
	private List<String> choseIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_chose_dialog);
		initView();
		initDate();
		setListeners();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		listViewChose = (ListView) findViewById(R.id.listView_chose);
		btnOK = (Button) findViewById(R.id.btnOK);
	}

	/**
	 * 初始化数据
	 */
	private void initDate() {
		strList = new ArrayList<String>();
		choseIds = new ArrayList<String>();
		intent = getIntent();
		String title = intent.getStringExtra(TITLE);
		int choseType = intent.getIntExtra(CHOSE_TYPE, 0);
		pullStyle = intent.getIntExtra(PULL_TYPE, 0);
		Bundle bundle = getIntent().getExtras();
		intentList = bundle.getStringArrayList(INTENT_LIST);

		// 设置标题
		tvTitle.setText(title);

		// 设置是否可单选
		if (choseType == SINGLE_TYPE) {
			isSingleChose = true;
		} else {
			isSingleChose = false;
		}

		// 设置下拉布局形式
		// switch (pullStyle) {
		// case CONSTELLATION:
		// dataArray = getResources().getStringArray(R.array.constellation);
		// break;
		// case INDUSTRY:
		// dataArray = getResources().getStringArray(R.array.industry);
		// break;
		// case HOBBY:
		// dataArray = getResources().getStringArray(R.array.hobby);
		// break;
		// case MUSIC_STYLE:
		// dataArray = getResources().getStringArray(R.array.music_style);
		// break;
		// default:
		// break;
		// }
		//
		// for (String str : dataArray) {
		// strList.add(str);
		// }

		strList.addAll(intentList);

	}

	/**
	 * 添加监听
	 */
	private void setListeners() {
		pullAdapter = new PullDownAdapter(this, strList);
		listViewChose.setAdapter(pullAdapter);

		// 点击确认
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(CHOSED_TEXT, chosedText);
				setResult(RESULT_OK, intent);
				System.out.println(chosedText);
				finish();
			}
		});

		// 选中的
		listViewChose.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewGroup contairView = (ViewGroup) pullAdapter.getView(
						position, view, parent);
				TextView tv = (TextView) contairView.getChildAt(0);
				CheckBox cb = (CheckBox) contairView.getChildAt(1);
				cb.performClick();
				// 若单选
				if (isSingleChose) {
					if (lastViewGroup != null) {
						((CheckBox) lastViewGroup.getChildAt(1)).performClick();
					}
					chosedText = tv.getText().toString();
				}
				// 多选
				else if (cb.isChecked()) {
					chosedText = chosedText.length() != 0 ? chosedText + "、"
							+ tv.getText().toString() : tv.getText().toString();
				}
				lastViewGroup = contairView;
			}
		});

	}

	/**
	 * 下拉adapter
	 * 
	 * @author Administrator
	 * 
	 */
	class PullDownAdapter extends BaseListAdapter<String> {

		public PullDownAdapter(Activity context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler vh;
			if (convertView == null) {
				vh = new ViewHodler();
				convertView = LayoutInflater.from(ChoseDialog.this).inflate(
						R.layout.adapter_chose_item, null);
				vh.tvValueName = (TextView) convertView
						.findViewById(R.id.tvValue);
				vh.cbCheckBox = (CheckBox) convertView
						.findViewById(R.id.cbCheckBox);
				convertView.setTag(vh);
			} else {
				vh = (ViewHodler) convertView.getTag();
			}
			vh.tvValueName.setText(strList.get(position));
			return convertView;
		}
	}

	/**
	 * viewholder
	 * 
	 * @author Administrator
	 * 
	 */
	class ViewHodler {
		TextView tvValueName;
		CheckBox cbCheckBox;
	}

}
