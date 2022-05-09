package cn.luxun.utils;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @Description:[ HttpUtils ]
 */
public class HttpUtil {

	/**
	 * 方法描述: 发送get请求
	 *
	 * @param url
	 * @throws
	 * @Return {@link String}
	 */
	public static String get(String url) throws Exception {
		// 创建HTTPClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String str = null;
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
			System.out.println("执行请求：" + httpget.getURI());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				System.out.println("----------------------------------------");
				System.out.println("返回响应: " + response.getStatusLine());
				str = EntityUtils.toString(response.getEntity());
				// System.out.println("响应内容：" + str);
				System.out.println("----------------------------------------");
				httpget.abort();
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return str;
	}

}