package com.huichuang.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.huihuang.utils.CommonUtils;
import com.huihuang.utils.NetUtils;

import android.accounts.NetworkErrorException;
import android.util.Log;
/**
 * 网络请求类
 * @author yangcan
 *
 */
public class HttpUtils {
	private static HttpClient httpClient;
	private static final int DEFAULT_SOCKET_TIMEOUT=20*1000;
	private static final int DEFAULT_HOST_CONNECTIONS=10;
	private static final int DEFAULT_MAX_CONNECTIONS=30;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192; 
	/**
	 * 联网查询需要权限，用户名和密码的MAP和服务器的路径 返回 服务器返回的字符串
	 * 
	 * path是一个服务器网络地址的路径，包括了servlet:例如：http://localhost:8080/AndroidStr/login
	 * 网址+项目名+servlet名
	 * 
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String doGetByhttpUrlConnection(String path,
			Map<String, String> data) throws Exception {
		String result = "";
		// 把路径拼接完整
		String appendPath = appendPath(path, data);
		// url请求网络地址
		URL url = new URL(appendPath);
		// 打开 创建 服务器的连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET"); // 设置请求模式
		connection.setReadTimeout(5000); // 设置请求超时时间
		result = ServiceRespost(result, connection);
		return result;
	}

	/**
	 * 传入连接，和当前所需的返回值，此方法会自动判断，得到服务器的返回内容
	 * 
	 * @param result
	 * @param connection
	 * @return
	 * @throws IOException
	 * @throws NetworkErrorException
	 */
	private static String ServiceRespost(String result,
			HttpURLConnection connection) throws IOException,
			NetworkErrorException {
		int responseCode = connection.getResponseCode(); // 获得响应码
		// 200代表网络通常
		if (responseCode == 200) {
			// 获得输入流
			InputStream is = connection.getInputStream();
			// 字节的中转流
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] bs = new byte[1024];
			while ((len = is.read(bs)) != -1) {
				baos.write(bs, 0, len);
			}
			result = baos.toString();
			baos.close();
			is.close();
		} else {
			// 当响应码不是200就代表网络有问题，就抛出异常
			throw new NetworkErrorException();
		}
		return result;
	}

	/**
	 * doPost方式提交，传入路径，和参数map
	 * 
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String doPostByHttpUrlConnection(String path,
			Map<String, String> data) throws Exception {
		String result = "";
		// url请求网址地址 不带参数
		URL url = new URL(path);
		
		// 创建 打开 服务器连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST"); // 设置请求模式
		connection.setReadTimeout(5000); // 设置请求超时时间
		connection.setDoOutput(true); // 允许输出数据 这是doPost 和 doGet的区别之一
		// 获得输出流
		OutputStream outputStream = connection.getOutputStream();
		// 拼接参数，上面url的path已经把网络地址传过去了所以这里拼接参数地址就行了u
//		String appendPath = URLEncoder.encode(appendPath("", data),"utf-8");
		// 缓冲字符串，拼接参数用
				StringBuffer temPath = new StringBuffer();
				// 适用doGet 和 doPost方式 doGet是放入全路径，doPost是放入""它只要后面的参数即可
//				temPath.append(path.equals("") ? "" : "?");
				// 迭代map集合 拼接参数
				for (Map.Entry<String, String> map : data.entrySet()) {
					temPath.append(map.getKey());
					temPath.append("=");
					temPath.append(URLEncoder.encode(map.getValue(),"utf-8"));
					temPath.append("&");
				}
				// 多了一个 & 符号，我们替换掉
				temPath.replace(temPath.lastIndexOf("&"), temPath.length(), "");
		Log.i("LoginActivity", temPath.toString());
		// 这里之所以是字节数组的原因是，你可以在这里传入图片音频等非字符的东西
		outputStream.write(temPath.toString().getBytes());
		result = ServiceRespost(result, connection);
		return result;
	}

	/**
	 * 拼接字符串，适用于doPost 和 dpGet 方式
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	private static String appendPath(String path, Map<String, String> data) {
		// 缓冲字符串，拼接参数用
		StringBuffer temPath = new StringBuffer(path);
		// 适用doGet 和 doPost方式 doGet是放入全路径，doPost是放入""它只要后面的参数即可
		temPath.append(path.equals("") ? "" : "?");
		// 迭代map集合 拼接参数
		for (Map.Entry<String, String> map : data.entrySet()) {
			temPath.append(map.getKey());
			temPath.append("=");
			temPath.append(map.getValue());
			temPath.append("&");
		}
		// 多了一个 & 符号，我们替换掉
		temPath.replace(temPath.lastIndexOf("&"), temPath.length(), "");
		return temPath.toString();
	}

	/**
	 * 使用HttpClient的GET方法来请求服务器
	 * 
	 * @param path
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String doGetHttpClient(String path, Map<String, String> data)
			throws Exception {
		String result = "";
		// HttpClient
		HttpClient client = new DefaultHttpClient();
		// 构建一个GET请求
		HttpGet request = new HttpGet(appendPath(path, data));
		// 设置超时时间 获得参数，从参数中设置时间
		request.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		// 执行请求获得返回值
		HttpResponse response = client.execute(request);
		// 获得响应码
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			// 返回数据的实体类
			HttpEntity entity = response.getEntity();
			// 实体类中获得内容的数据流
			InputStream is = entity.getContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] bs = new byte[1024];
			while ((len = is.read(bs)) != -1) {
				outputStream.write(bs, 0, len);
			}

			result = outputStream.toString();
			outputStream.close();
			is.close();
		} else {
			throw new NetworkErrorException();
		}
		return result;
	}

	@SuppressWarnings("unused")
	private static synchronized HttpClient getHttpClient(){
		if(httpClient==null){
			final HttpParams params=new BasicHttpParams();
			ConnManagerParams.setTimeout(params, 1000);
			HttpConnectionParams.setConnectionTimeout(params, DEFAULT_SOCKET_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, DEFAULT_SOCKET_TIMEOUT);
		    ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
		    ConnManagerParams.setMaxTotalConnections(params, DEFAULT_MAX_CONNECTIONS);  
		    HttpProtocolParams.setUseExpectContinue(params, true);  
		    HttpConnectionParams.setStaleCheckingEnabled(params, false);  
		    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
		    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		    HttpClientParams.setRedirecting(params, false); 
		    String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";  
		    HttpProtocolParams.setUserAgent(params, userAgent); 
		    HttpConnectionParams.setTcpNoDelay(params, true);
		    HttpConnectionParams.setSocketBufferSize(params, DEFAULT_SOCKET_BUFFER_SIZE);
		    SchemeRegistry schemeRegistry = new SchemeRegistry();    
	        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));    
	        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443)); 
	        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);    
	        httpClient = new DefaultHttpClient(manager, params); 
		}
		return httpClient;
	}
	/**
	 * HttpClient POST提交方式
	 * 
	 * @param path 网络请求url
	 * @param data 要传递的参数
	 * @return 返回的结果
	 * @throws Exception
	 */
	public static String doPostHttpClient(String path, Map<String, String> data)
			throws Exception {
		String result = "";
		HttpClient client =new DefaultHttpClient();
		// 构建POST
		HttpPost request = new HttpPost(path);
		// 获得参数，从参数中设置超时时间
		request.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> map : data.entrySet()) {
			BasicNameValuePair basicNameValuePair = new BasicNameValuePair(
					map.getKey(), map.getValue());
			parameters.add(basicNameValuePair);
//			parameters.add(new BasicNameValuePair(name, value))
		}
		// 创建参数实体
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
				"utf-8");
		
		// 设置参数实体
		request.setEntity(entity);
		
		// 执行参数实体
		HttpResponse response = client.execute(request);
		
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			// 获得返回数据的实体
			InputStream is = response.getEntity().getContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] bs = new byte[1024];
			while ((len = is.read(bs)) != -1) {
				outputStream.write(bs, 0, len);
			}

			result = outputStream.toString();
			is.close();
			// 实体工具类获得实体的数据
			// result = EntityUtils.toString(entity2);
		}
		else {
			throw new NetworkErrorException();
		}
		return result;
	}
	/**
	 * 公共参数
	 * @param map
	 * @return
	 */
	public static Map<String, String>pubParameter(Map<String, String> map){
		map.put("serv_id", map.get("requestcode"));
		map.put("version", "1.0");
		map.put("source", "1");
		map.put("timestamp", CommonUtils.getTimestamp());
		return map;
		
	}
	public static Map<String, String>posParameter(){
		Map<String, String> map=new HashMap<String, String>();
		map.put("version", "1.0");
		map.put("source", "1");
		map.put("timestamp", CommonUtils.getTimestamp());
		return map;
		
	}
	public  static String appendpubPath(Map<String, String> data) {
		// 缓冲字符串，拼接参数用
		StringBuffer temPath = new StringBuffer();
		// 迭代map集合 拼接参数
		for (Map.Entry<String, String> map : data.entrySet()) {
			temPath.append(map.getKey());
			temPath.append("=");
			temPath.append(map.getValue());
			temPath.append("&");
		}
		// 多了一个 & 符号，我们替换掉
		temPath.replace(temPath.lastIndexOf("&"), temPath.length(), "");
		
		return temPath.toString();
	}
	/**
	 * 合成数据的map对象
	 * @param sid参数
	 * @param data参数
	 * @param t参数
	 * @return 合成的map对象
	 * @throws Exception
	 *//*
	public static String toData(String sid, String data, String t)
			throws Exception {
		Map<String, String> hm = new HashMap<String, String>();
		if (sid != null) {
			hm.put("sid", Constants.DESencrypt(sid));
		}
		if (data != null) {
			hm.put("data", Constants.DESencrypt(data));
		}
		if (t != null) {
			hm.put("t", t);
		}
		return doPostHttpClient(Constants.HTTP_URL, hm);
//		return doPostByHttpUrlConnection(Constants.HTTP_URL, hm);
//		return hm;
	}*/
}
