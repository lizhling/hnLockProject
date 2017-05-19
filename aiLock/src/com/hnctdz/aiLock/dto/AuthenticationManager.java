package com.hnctdz.aiLock.dto;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hnctdz.aiLock.dto.Authentication;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName AuthenticationManager.java
 * @Author lizhilin 
 */
public class AuthenticationManager {
	private static final AuthenticationManager instance = new AuthenticationManager();
	
	private Map<String, Authentication> authMaps = new ConcurrentHashMap<String, Authentication>();
	
	private Map<String, String> anonymityMaps = new ConcurrentHashMap<String, String>();
	
	public AuthenticationManager(){
	}
	
    public static AuthenticationManager getInstance() {
        return instance;
    }
    
    public Authentication generateToken(String loginUser, Long loginId, String userType, Long orgId){
    	String randAuthStr = CommonUtil.getRandomNumberString(9);//生成9位随机数
		String authenStr = randAuthStr + DateUtil.getDateTime2()+userType+loginId;

		Authentication authentication = getAuthenticationForMap(loginUser);//从内存取出令牌
		
		if(authentication == null){
			authentication = new Authentication();
		}
		authentication.setUserToken(authenStr);
		authentication.setLastAccessDate(new Date());
		authentication.setLoginId(loginId);
		authentication.setUserType(userType);
		authentication.setOrgId(orgId);
		
		authMaps.put(loginUser, authentication);
		
		return authentication;
    }
    
	/**
	 * 客户端令牌验证
	 * @param authentication
	 * @return
	 */
	public Authentication checkAuthentication(String loginUser) {
		String checkResult = "";
		Authentication authenticationBean = getAuthenticationForMap(loginUser);//从内存取出令牌
		if(authenticationBean != null){
			String toKeyExpireTime = Constants.TO_KEY_EXPIRE_TIME;//获取令牌失效时间
			Date lastAccessDate = authenticationBean.getLastAccessDate();//最后访问时间
			long day = (new Date().getTime() - lastAccessDate.getTime()) / (60 * 60 * 1000);
			//验证访问是否过期
			if(day >= Long.valueOf(toKeyExpireTime)){
				checkResult = "登录超时，请重新登录！";
			} else {
				authenticationBean.setLastAccessDate(new Date());
			}
		}else{
			authenticationBean = new Authentication();
			checkResult = "非法请求，请先登录！";
		}
		authenticationBean.setCheckResult(checkResult);
		return authenticationBean;
	}
	
	public String generateAnonymityToken(String imei){
		String firsts = CommonUtil.randomString("abcdefghijklmnopqrstuvwxyz",1);//获取首字符(key的起始索引位)
		char[] chars = firsts.toCharArray(); //把字符中转换为字符数组 
		int first = (int)chars[0] - 90;//转发成ASCII码
		
		String randAuthStr = CommonUtil.getRandomLettAndDigStr(first - 1);
		String desKey = CommonUtil.getRandomLettAndDigStr(42 - first);//生成随机key
		String anonymityToken = firsts + randAuthStr + desKey;
		anonymityMaps.put(imei, anonymityToken);
		
		return anonymityToken;
    }
	
	public Authentication getAuthenticationForMap(String phone_no){
		return authMaps.get(phone_no);
	}
	
	public void removeAuthentication(String phone_no){
		authMaps.remove(phone_no);
	}

	public String getAnonymityTokenForMap(String imei) {
		String anonymitytoken = anonymityMaps.get(imei);
		anonymityMaps.remove(imei);
		return anonymitytoken;
	}

	public void removeAnonymityToken(String imei) {
		anonymityMaps.remove(imei);
	}
	
}
