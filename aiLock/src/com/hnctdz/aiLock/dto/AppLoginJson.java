package com.hnctdz.aiLock.dto;

import com.google.gson.annotations.Expose;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.GsonUtil;

/**
 * @ClassName LoginJson.java
 * @Author WangXiangBo
 */
public class AppLoginJson extends GsonUtil{
	@Expose
	private String smartKeyPerId;
	@Expose
	private Long smartKeyPassw;
	@Expose
	private Integer KeyPerPaining;
	@Expose
	private Integer remoteUnlockDist;
	
	public AppLoginJson(){
//		this.setKeyPerPaining(Constants.KEY_PER_PAINING);
		this.setRemoteUnlockDist(Constants.REMOTE_UNLOCK_DIST);
	}
	
	public String getSmartKeyPerId() {
		return smartKeyPerId;
	}

	public void setSmartKeyPerId(String smartKeyPerId) {
		this.smartKeyPerId = smartKeyPerId;
	}

	public Long getSmartKeyPassw() {
		return smartKeyPassw;
	}

	public void setSmartKeyPassw(Long smartKeyPassw) {
		this.smartKeyPassw = smartKeyPassw;
	}

	public Integer getKeyPerPaining() {
		return KeyPerPaining;
	}

	public void setKeyPerPaining(Integer keyPerPaining) {
		KeyPerPaining = keyPerPaining;
	}

	public Integer getRemoteUnlockDist() {
		return remoteUnlockDist;
	}

	public void setRemoteUnlockDist(Integer remoteUnlockDist) {
		this.remoteUnlockDist = remoteUnlockDist;
	}

}
