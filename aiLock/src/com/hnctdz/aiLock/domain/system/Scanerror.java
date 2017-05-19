package com.hnctdz.aiLock.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Scanerror entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SCANERROR")
public class Scanerror implements java.io.Serializable {

	// Fields

	private String id;
	private String serverName;
	private String transTime;
	private String serverIp;
	private String errorDesc;
	private String errorInput;
	private String errorOut;
	private String phoneNo;

	// Constructors

	/** default constructor */
	public Scanerror() {
	}

	/** minimal constructor */
	public Scanerror(String id) {
		this.id = id;
	}

	/** full constructor */
	public Scanerror(String id, String serverName, String transTime,
			String serverIp, String errorDesc, String errorInput,
			String errorOut) {
		this.id = id;
		this.serverName = serverName;
		this.transTime = transTime;
		this.serverIp = serverIp;
		this.errorDesc = errorDesc;
		this.errorInput = errorInput;
		this.errorOut = errorOut;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "SERVER_NAME", length = 2000)
	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name = "TRANS_TIME", length = 20)
	public String getTransTime() {
		return this.transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	@Column(name = "SERVER_IP", length = 20)
	public String getServerIp() {
		return this.serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name = "ERROR_DESC", length = 100)
	public String getErrorDesc() {
		return this.errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	@Column(name = "ERROR_INPUT", length = 550)
	public String getErrorInput() {
		return this.errorInput;
	}

	public void setErrorInput(String errorInput) {
		this.errorInput = errorInput;
	}

	@Column(name = "ERROR_OUT", length = 500)
	public String getErrorOut() {
		return this.errorOut;
	}

	public void setErrorOut(String errorOut) {
		this.errorOut = errorOut;
	}

	@Column(name = "PHONE_NO", length = 20)
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}