package com.fullteem.yueba.net.http;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.net.Urls;

/**
 * 本类为简单的Post/Get请求工具类，方便发送请求，封装程度比较低，自由度相对较高，需要使用者自己完成解析， 后面等项目差不多了会继续增加简单的封装
 * 
 * @author ssy
 * 
 */
public class HttpRequestTask extends AsyncTask<RequestConfig, Integer, String> {
	private static String TAG = "HttpRequestTask";
	private static String DATA_ERROE = "data error";
	private static String TIME_OUT = "time out";
	private IHttpTaskCallBack httpcall;
	private BufferedReader reader;
	StringBuffer sb;
	private int requestFlag;
	private Map<String, Object> objectData;
	private String WebAddress;

	// 最多能够容忍请求发送失败的次数为3次，默认为0
	private int fialdCount = 0;

	public HttpRequestTask(IHttpTaskCallBack httpcall) {
		this.httpcall = httpcall;
	}

	/**
	 * 后台执行的请求操作
	 */
	@Override
	protected String doInBackground(RequestConfig... params) {
		RequestConfig requestConfig = params[0];
		requestFlag = requestConfig.getRequestFlag();
		objectData = requestConfig.getObjectData();
		WebAddress = requestConfig.getWebAddress();

		// if (requestConfig.getPostFlag() == GlobleConstant.postJson) {
		// fialdCount = 0;
		// isFirstFaild = true;
		// return CreatePostJsonRequst(requestConfig.getWebAddress(),
		// requestConfig.getObjectData());
		// } else if (requestConfig.getPostFlag() == GlobleConstant.upLoad) {
		// return createUploadRequest(requestConfig.getUploadPath());
		// } else if (requestConfig.getPostFlag() ==
		// GlobleConstant.postNormJson) {
		// return CreateNormJsonRequest(requestConfig.getWebAddress(),
		// requestConfig.getObjectData());
		// }
		// return CreatePostRequst(requestConfig.getWebAddress(),
		// requestConfig.getObjectData());

		if (requestConfig.getPostFlag() == GlobleConstant.GET) {
			return CreateGetRequest(requestConfig.getWebAddress());
		} else if (requestConfig.getPostFlag() == GlobleConstant.UPLOAD) {
			return createUploadRequest(requestConfig.getUploadPath());
		}
		return CreateGetRequest(requestConfig.getWebAddress());
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 创建post请求,返回一个String的result
	 */
	// private String CreatePostRequst(String url, Map<String, Object> data) {
	// HttpPost httpPost = new HttpPost(url);
	// 设置HTTP POST请求参数必须用NameValuePair对象
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// HttpResponse httpResponse = null;

	// try {
	// MultipartEntity mpEntity = new MultipartEntity();
	// if (data != null && data.size() > 0) {
	// for (@SuppressWarnings("rawtypes")
	// Map.Entry entry : data.entrySet()) {
	// String key = (String) entry.getKey();
	// // params.add(new BasicNameValuePair(key,
	// // paramsValue.get(key)));
	// mpEntity.addPart(fbp);
	// }
	// } else {
	// Log.e(TAG, "请仔细检查参数是否出现异常");
	// }
	// StringEntity strEntiny = new StringEntity(data.toString(), "utf-8");
	// strEntiny.setContentType("application/json");
	// 设置httpPost请求参数
	// httpPost.setEntity(strEntiny);
	// HttpClient httpClient = new DefaultHttpClient();

	// 设置超时时间
	// httpClient.getParams().setParameter(
	// CoreConnectionPNames.CONNECTION_TIMEOUT,
	// GlobleConstant.TIMEOUT);
	// httpResponse = httpClient.execute(httpPost);

	// DebugUtil.LogInfo("",httpResponse.getStatusLine().getStatusCode());
	// if (httpResponse.getStatusLine().getStatusCode() == 200) {
	// 第三步，使用getEntity方法活得返回结果
	// String result = EntityUtils.toString(httpResponse.getEntity());
	// DebugUtil.LogInfo("", "data:" + data.toString());
	// DebugUtil.LogInfo("", result);
	// return result;
	// }
	// }
	// // 连接超时
	// catch (ConnectTimeoutException e) {
	// return TIME_OUT;
	// } catch (Exception e) {
	// return DATA_ERROE;
	// }
	// return DATA_ERROE;
	//
	// }

	/**
	 * 发送Json格式Post请求
	 * 
	 * @param myurl
	 *            url
	 * @param data
	 *            map
	 * @return String
	 */
	private synchronized String CreatePostJsonRequst(String myurl,
			Map<String, Object> data) {
		try {
			// 创建连接
			URL url = new URL(myurl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(GlobleConstant.TIMEOUT);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");

			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			JSONObject obj = new JSONObject();

			if (data != null && data.size() > 0) {
				for (@SuppressWarnings("rawtypes")
				Map.Entry entry : data.entrySet()) {
					String key = (String) entry.getKey();
					Object value = data.get(key);
					obj.accumulate(key, value);
				}
			} else {
				Log.v(TAG, "请仔细检查参数是否出现异常");
			}
			// String str = obj.toString();
			// str = str.replaceAll("\\", "");
			out.write(obj.toString().getBytes("UTF-8"));
			Log.v(TAG, "发送请求:" + obj.toString());
			out.flush();
			out.close();

			InputStream is = connection.getInputStream();
			InputStreamReader ir = new InputStreamReader(is);
			// 读取响应
			reader = new BufferedReader(ir);
			String lines;
			sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			Log.v(TAG, sb.toString());
			reader.close();
			// 断开连接
			connection.disconnect();
			connection = null;
			is.close();
			return sb.toString();
		} catch (ProtocolException e) {
			Log.v(TAG, "请求失败异常，重新发送一次：" + e.toString());
			if (fialdCount >= 3) {
				sb = new StringBuffer(DATA_ERROE);
				fialdCount = 0;
			} else {
				fialdCount++;
				CreatePostJsonRequst(WebAddress, objectData);
			}
		} catch (ConnectTimeoutException e) {
			sb = new StringBuffer(TIME_OUT);
			Log.v(TAG, e.toString());
		} catch (FileNotFoundException e) {
			// if (isFirstFaild) {
			// isFirstFaild = false;
			// CreatePostJsonRequst(WebAddress, objectData);
			// } else {
			// DebugUtil.LogError(TAG, e.toString());
			// }
			sb = new StringBuffer(DATA_ERROE);
		}
		// catch (IOException e) {9999++
		// Log.e(TAG, e.toString());
		// }
		catch (Exception e) {
			sb = new StringBuffer(DATA_ERROE);
			Log.v(TAG, e.toString());
		}
		return sb.toString();
	}

	/**
	 * android 标准json请求
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public String CreateNormJsonRequest(String url, Map<String, Object> data) {
		HttpPost request = new HttpPost(url);
		JSONObject param = new JSONObject();
		String retSrc = null;
		// MultipartEntity mpEntity = new MultipartEntity();
		if (data != null && data.size() > 0) {
			for (@SuppressWarnings("rawtypes")
			Map.Entry entry : data.entrySet()) {
				String key = (String) entry.getKey();
				try {
					param.put(key, data.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			Log.e(TAG, "请仔细检查参数是否出现异常");
		}
		// 绑定到请求 Entry
		StringEntity se;
		try {
			se = new StringEntity(param.toString(), "utf-8");
			se.setContentType("application/json");
			Log.v("NormRequest", param.toString());
			request.setEntity(se);
			// 发送请求
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			// 得到应答的字符串
			retSrc = EntityUtils.toString(httpResponse.getEntity());
			Log.v("NormRequest", retSrc);
			return retSrc;
		} catch (ConnectTimeoutException e) {
			return TIME_OUT;
		} catch (Exception e) {
			e.printStackTrace();
			return DATA_ERROE;
		}
	};

	/**
	 * 创建上传请求
	 */
	public String createUploadRequest(String path) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(Urls.uploadMemberImage_action);
			final int skipBytes = 0;
			// httppost.addHeader("_x_range", skipBytes+"");

			// String filePath="E://short10.ts";
			// String filePath = "E://0f28.wav";
			FileBody bin = new FileBody(new File(path));

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("fileData", bin);
			httppost.setEntity(reqEntity);

			Log.v(TAG, "executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			Log.v("", response.getStatusLine().toString());
			if (resEntity != null) {
				Log.v("",
						"Response content length: "
								+ resEntity.getContentLength());
			}
			String result = EntityUtils.toString(resEntity);
			Log.v("", result);
			return result;

		} catch (Exception e) {
			Log.e(TAG, e.toString());

		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}
		return DATA_ERROE;
	}

	/**
	 * 处理返回结果
	 */
	@Override
	protected void onPostExecute(String result) {
		if (DATA_ERROE.equalsIgnoreCase(result)) {
			httpcall.TaskFaild(result);
		} else if (TIME_OUT.equalsIgnoreCase(result)) {
			httpcall.TimeOut(result);
		} else {
			httpcall.TaskSuccess(result, requestFlag);
		}
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public String CreateGetRequest(String url) {
		Log.v(TAG, url);
		String result = "";
		BufferedReader in = null;
		try {
			// String urlName = url + "?" + params;
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
