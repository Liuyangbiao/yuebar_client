package com.fullteem.yueba.app.ui;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.interfaces.IGetMapPressPlace;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.CustemMapView;
import com.fullteem.yueba.widget.MyProgressDialog;
import com.fullteem.yueba.widget.TopTitleView;

public class CreateGroupActivity extends Activity {

	private TopTitleView toptitle;
	static CustemMapView mMapView = null;
	private MapController mMapController = null;
	public MKMapViewListener mMapListener = null;
	public ProgressDialog progressDialog;
	public ItemizedOverlay<OverlayItem> mAddrOverlay = null;
	public BMapManager mBMapManager = null;
	public static final String strKey = "3AB1810EBAAE0175EB41A744CF3B2D6497407B87";
	public AppContext appContext;
	private Drawable marker;
	private Spinner spinner;
	private String peopleNums;
	private EditText edtGroupPlace, edtGroupNickName, edtDesc;
	private ImageButton addGroup;
	private Bitmap groupPhoto;
	private String picName = "groupPhoto";
	private String picPath = GlobleConstant.USERPHOTO_DIR + picName + ".jpg";
	protected MyProgressDialog dialog;
	private UploadModel upModel;
	private Button btnSubmit;

	/**
	 * 选中点
	 */
	private GeoPoint pressGp;

	/**
	 * 坐标反查
	 */
	private MKSearch mkSearch;
	private String iconPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mBMapManager == null) {
			initEngineManager(this.getApplicationContext());
		}
		setContentView(R.layout.activity_creategroup);
		appContext = (AppContext) getApplication();
		initViews();
		bindViews();
		this.iconPath = "";
	}

	public void initViews() {
		mkSearch = new MKSearch();
		mkSearch.init(mBMapManager, new MySearchListener());
		mMapView = (CustemMapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(false);
		double latitude = appContext.getLocation().getLatitude() * 1e6;
		double longtitude = appContext.getLocation().getLongitude() * 1e6;
		GeoPoint point = new GeoPoint((int) latitude, (int) longtitude);
		pressGp = point;
		showMap(point);
		mMapView.setGetPressListener(new IGetMapPressPlace() {

			@Override
			public void getPoint(GeoPoint pt) {
				if (pt != null) {
					showMap(pt);
					pressGp = pt;
				}
			}
		});
	}

	/**
	 * 展示百度地图
	 * 
	 * @param point
	 */
	private void showMap(GeoPoint point) {
		// System.out.println("latitude:"+latitude/1e6+"    longtitude"+longtitude/1e6);
		if (marker == null) {
			marker = this.getResources().getDrawable(R.drawable.icon_marka);
			// 为maker定义位置和边界
			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight());
		}

		if (mAddrOverlay == null) {
			mAddrOverlay = new ItemizedOverlay<OverlayItem>(marker, mMapView);
		}

		mAddrOverlay.removeAll();
		// GeoPoint point = new GeoPoint((int) (latitude), (int) (longtitude));
		// point = CoordinateConvert.fromGcjToBaidu(point);
		OverlayItem addrItem = new OverlayItem(point, "", "定位点");
		mMapController.setCenter(point);
		mAddrOverlay.addItem(addrItem);
		mMapView.getOverlays().add(mAddrOverlay);
		mMapView.refresh();

		// 查询该经纬度值所对应的地址位置信息
		mkSearch.reverseGeocode(point);
	}

	/**
	 * 绑定view
	 */
	public void bindViews() {
		addGroup = (ImageButton) findViewById(R.id.addGroup);
		addGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CuttingPicturesUtil.isSDCardExisd()) {
					CuttingPicturesUtil.searhcAlbum(CreateGroupActivity.this,
							CuttingPicturesUtil.LOCAL_PHOTO);
				} else {
					showToast(getString(R.string.sdcard_not_exsit));
				}
			}
		});
		edtGroupPlace = (EditText) findViewById(R.id.edtGroupPlace);
		edtGroupNickName = (EditText) findViewById(R.id.edtGroupNickName);
		edtDesc = (EditText) findViewById(R.id.edtDesc);

		// 初始化控件
		spinner = (Spinner) findViewById(R.id.spinner);
		// 建立数据源
		final String[] mItems = getResources().getStringArray(
				R.array.spinnername);
		// 建立Adapter并且绑定数据源

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItems);
		// 绑定 Adapter到控件
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				peopleNums = mItems[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		toptitle = (TopTitleView) findViewById(R.id.toptile);
		toptitle.showLeftImag(R.drawable.back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				CreateGroupActivity.this.finish();
			}
		});
		toptitle.showCenterText(getString(R.string.group_title), null);

		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doCreateGroups();
			}
		});

	}

	/**
	 * 初始化百度引擎
	 * 
	 * @param context
	 */
	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(this.getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 界面监听
	 * 
	 * @author Administrator
	 * 
	 */
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(CreateGroupActivity.this, "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(CreateGroupActivity.this, "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Log.e("map", "permissio denied. check your app key");
			}
		}

	}

	/**
	 * 创建动作
	 */
	private void doCreateGroups() {
		showLoadingDialog();
		if (!TextUtils.isEmpty(edtGroupPlace.getText())
				&& !TextUtils.isEmpty(edtDesc.getText())
				&& !TextUtils.isEmpty(edtGroupNickName.getText())) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("userId", appContext.getUserInfo().getUserId());
			jsonObj.put("groupDesc", edtDesc.getText().toString());
			jsonObj.put("groupLocale  ", edtGroupPlace.getText().toString());
			jsonObj.put("groupName", edtGroupNickName.getText().toString());
			jsonObj.put("num", peopleNums);

			jsonObj.put("lng", appContext.getLocation().getLongitude());
			jsonObj.put("lat", appContext.getLocation().getLatitude());

			jsonObj.put("groupIcon", iconPath);
			
			UmengUtil.onEvent(CreateGroupActivity.this, "submit_button_hits");
			LogUtil.printUmengLog("submit_button_hits");

			HttpRequestFactory.getInstance().postRequest(Urls.CREATE_GROUPS,
					jsonObj, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							CommonModel cm = new CommonModel();
							cm = JsonUtil.convertJsonToObject(content,
									CommonModel.class);
							if (cm != null
									&& GlobleConstant.REQUEST_SUCCESS
											.equalsIgnoreCase(cm.getCode())) {
								msgHandler.obtainMessage(0, "创建群成功")
										.sendToTarget();

								finish();

							} else if (cm != null
									&& GlobleConstant.GROUP_IS_EXIST
											.equalsIgnoreCase(cm.getCode())) {
								msgHandler.obtainMessage(0, "群组已经存在")
										.sendToTarget();
							}
						}

						@Override
						public void onFinish() {
							super.onFinish();
							dismissLoadingDialog();
						}
					});
		} else {
			showToast(getString(R.string.please_input_all));
			dismissLoadingDialog();
		}
	}

	/**
	 * 展示toast
	 * 
	 * @param str
	 */
	private void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case CuttingPicturesUtil.CAMERA:
				PhotoUtil.printTrace("case CAMERA");
				File temp = new File(GlobleConstant.USERPHOTO_DIR
						+ GlobleConstant.USERPHOTO_NAME);
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						CreateGroupActivity.this, Uri.fromFile(temp)),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			case CuttingPicturesUtil.ZOOMPHOTO:
				PhotoUtil.printTrace("case ZOOMPHOTO");

				// userOldPhoto = userPhoto;
				groupPhoto = data.getExtras().getParcelable("data");
				addGroup.setImageBitmap(groupPhoto);
				LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
						100, 100);
				addGroup.setLayoutParams(ll);
				try {
					FileUtils.saveBitmapToPath(groupPhoto,
							GlobleConstant.USERPHOTO_DIR, picName);
				} catch (IOException e) {
					Log.e("REGISTER", e.toString());
					e.printStackTrace();
				}
				upLoadPic();
				break;
			case CuttingPicturesUtil.LOCAL_PHOTO:
				PhotoUtil.printTrace("case LOCAL_PHOTO");
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						CreateGroupActivity.this, data.getData()),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void showLoadingDialog() {
		dialog = new MyProgressDialog(this, true);
		dialog.show();
	}

	public void dismissLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	/**
	 * 上传图片
	 */
	public void upLoadPic() {
		File file = new File(picPath);
		HttpRequestFactory.getInstance().uploadFile(CreateGroupActivity.this,
				file, new CustomAsyncResponehandler() {

					@Override
					public void onStart() {

					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						PhotoUtil.printTrace("upLoadPic. onSuccess");
					}

					@Override
					public void onFailure(Throwable error, String content) {
						PhotoUtil.printTrace("upLoadPic. onFailure");
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						PhotoUtil
								.printTrace("upLoadPic. onSuccess with ResponeModel");

						UploadModel uploadModel = new UploadModel();
						uploadModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);

						if (uploadModel != null
								&& uploadModel.getResult() != null) {
							UploadDataModel upModel = uploadModel.getResult();
							iconPath = upModel.getSmallImageFile();
						}
					}

				});
	}

	Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(getApplicationContext(), msg.obj.toString(),
					Toast.LENGTH_SHORT).show();
		};
	};

	/**
	 * 实现MKSearchListener接口,用于实现异步搜索服务，得到搜索结果
	 */
	public class MySearchListener implements MKSearchListener {
		/**
		 * 根据经纬度搜索地址信息结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			edtGroupPlace.setText(result.strAddr);
		}

		/**
		 * 驾车路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
		}

		/**
		 * POI搜索结果（范围检索、城市POI检索、周边检索）
		 * 
		 * @param result
		 *            搜索结果
		 * @param type
		 *            返回结果类型（11,12,21:poi列表 7:城市列表）
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
		}

		/**
		 * 公交换乘路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
		}

		/**
		 * 步行路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	}

}
