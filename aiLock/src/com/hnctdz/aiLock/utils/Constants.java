package com.hnctdz.aiLock.utils;

/** 
 * @ClassName Constants.java
 * @Author WangXiangBo 
 */
public class Constants {
	public static final String SESSION_USER = "userInfo";
	
	public static final String ALLOW_FILE_TYPE = ".jpg,.jpng,.png,.gif";
	public static final String VERSION_fILE_TYPE = ".ipa,.apk,.rar,.zip,.plist";
	public static final String VERSION_CODE_SEPARATOR = "~";
	
	public static final Integer ELECTRIC_LOCK = 2;
	
	//路径分隔符 windows:\\ liunx:/ 注：路劲中存在 /,windows也能成功访问，所以为了后台方便处理及保存进数据库，windows也采用 /
	public static final String FILE_SEPARATOR = CommonUtil.getPropertyValue("file.separator");
	public static final String SERVER_OS = CommonUtil.getPropertyValue("serverOS");//服务器操作系统
	
	
	public static final String FTP_IP = CommonUtil.getPropertyValue("ftp_ip");
	public static final String FTP_PORT = CommonUtil.getPropertyValue("ftp_port");
	public static final String FTP_USER_NAME = CommonUtil.getPropertyValue("ftp_user_name");
	public static final String FTP_USER_PASSWORD = CommonUtil.getPropertyValue("ftp_user_password");
	public static final String FTP_ROOT_DIRECTORY = CommonUtil.getPropertyValue("ftp_root_directory");//FTP根目录
	public static final String FTP_FILE_MAX_SIZE = "50";//上传文件最大50M
	
	public static final String SERVER_SETTING = CommonUtil.getPropertyValue("serverSetting");//服务器，0：测试服务器；1：正式服务器
	public static final String REQUEST_SERVER_PATH = CommonUtil.getPropertyValue("requestServerPath");//服务器请求路劲
	
	public static final String TO_KEY_EXPIRE_TIME = CommonUtil.getPropertyValue("to_key_expire_time");//令牌失效时间
	
	public static final String PER_DEFAULT_PASSW = CommonUtil.getPropertyValue("perDefaultPassw");//人员默认初始密码
	public static final Long DEFAULT_SMART_KEY_PASSW = 12341234L;
	
	public static final String LOCK_STATUS_TIMEOUT = CommonUtil.getPropertyValue("lockStatusTimeout");//有源门锁未收到状态脱机时间（单位分）
	
	/** 0：系统用户 */
	public static final String SYSUSER_LOGIN = "0";//系统用户
	/** 1：门锁使用人员 */
	public static final String PERSONNEL_LOGIN = "1";//门锁使用人员
	
	/** 1：无源锁 */
	public static final Integer PASSIVE_LOCK = 1;//无源锁
	/** 2：有源锁 */
	public static final Integer ACTIVE_LOCK = 2;//有源锁
	/** 1：有源门锁可配卡 */
	public static final Integer CAN_MATCH_CARD = 1;
	
	/** 1：管理员授权 */
	public static final Long AUTHORIZE_MANAGE = 1L;
	/** 2：人员转授权 */
	public static final Long AUTHORIZE_PER_TURN = 2L;
	
	/** 2：查询告警记录 */
	public static final Integer SELECT_ALARM = 2;
	
	public static final Integer KEY_PER_PAINING = CommonUtil.getIntProperty("keyPerPaining", 1);//智能钥匙是否要与人员匹配才可用
	public static final Integer REMOTE_UNLOCK_DIST = CommonUtil.getIntProperty("remoteUnlockDist", 200);//APP远程开门距离（单位米）
	
	public static final String REQUEST_LOCKDM_URL = CommonUtil.getPropertyValue("requestLockdmUrl");//请求门锁设备管理平台地址
	
	public static final int COLUMN = CommonUtil.getIntProperty("column", 9999);
	
	public static final int EXECUTE_COUNTS = CommonUtil.getIntProperty("executeCounts", 3);//请求门锁设备管理平台次数
	
	public static final String ENCIPHER_WAY =  CommonUtil.getPropertyValue("encipher_way");//加解密方式
	public static final String CHARTSET_NAME =  CommonUtil.getPropertyValue("chartSet_name");//加解密方式
	
	
	public static final String ERROR_ALARM_NUMBER = CommonUtil.getPropertyValue("errorAlarmNumber");//异常报警次数
	public static final String ERROR_IN_SCOPE = CommonUtil.getPropertyValue("errorInScope");//异常发生范围（单位：分钟）
	
}
