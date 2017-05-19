package com.hnctdz.aiLock.https;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;


/** 
 * @ClassName HttpDeviceManageForRest.java
 * @Author WangXiangBo 
 */
public class HttpDeviceManageForRest {
	private static final Logger LOG = Logger.getLogger(HttpDeviceManageForRest.class);  
	private static HttpDeviceManageForRest httpDeviceManage;

	private HttpDeviceManageForRest() {
	}

	public static HttpDeviceManageForRest getInstance() {
		if (httpDeviceManage == null) {
			httpDeviceManage = new HttpDeviceManageForRest();
		}
		return httpDeviceManage;
	}
	
	public String httpRequestForLockdm(String restUrl) throws ErrorCodeException {
		String returnJson = "";
		int i;
		for (i = 0; i < Constants.EXECUTE_COUNTS; i++) {
			System.out.println("请求门锁平台："+ DateUtil.getDateTime());
			returnJson = sendHttpRequest(restUrl);
			System.out.println("收到门锁平台返回："+ DateUtil.getDateTime());
			if(returnJson.equalsIgnoreCase("Illegal request")){
				throw new ErrorCodeException(ErrorCode.ERROR_HTTP_WAS_BLOCKED);
			}
			String retCode = CommonUtil.getResultCode(returnJson);
			if (StringUtil.isNotBlank(retCode)) {
				break;
			}
			System.out.println("请求次数：" + (i + 1) + "，retCode:" + retCode);
		}
		if(i == Constants.EXECUTE_COUNTS){
			throw new ErrorCodeException(ErrorCode.ERROR_HTTP_REQUEST);
		}
		return returnJson;
	}
	
	private static String sendHttpRequest(String restUrl) throws ErrorCodeException{
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(AllClientPNames.CONNECTION_TIMEOUT,30000);
		httpClient.getParams().setParameter(AllClientPNames.SO_TIMEOUT, 30000);
		HttpGet httpGet = new HttpGet(restUrl);
		String json = "";
		HttpEntity entity = null;
		try {
			HttpResponse response = httpClient.execute(httpGet); 
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				entity = response.getEntity();
				if (entity != null) {
					json = EntityUtils.toString(response.getEntity());
				}
			} else {
				throw new ErrorCodeException(ErrorCode.ERROR_REQUEST_TIME_OUT);
			}
		} catch (ErrorCodeException e) {
			throw new ErrorCodeException(e.getMessage());
		} catch (Exception e) {
			LOG.error(e);
			throw new ErrorCodeException(ErrorCode.ERROR_HTTP_REQUEST);
		} finally {
			try {
				if (null != entity) {
					EntityUtils.consume(entity);
				}
				if (httpGet.isAborted()) {
					httpGet.abort();
				}
				if (null != httpClient) {
					httpClient.getConnectionManager().shutdown();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	
}
