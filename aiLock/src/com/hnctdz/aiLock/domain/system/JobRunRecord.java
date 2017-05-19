package com.hnctdz.aiLock.domain.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * JobRunRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "JOB_RUN_RECORD")
public class JobRunRecord implements java.io.Serializable {

	// Fields

	private String id;
	private String jobName;
	private Date runStartDate;
	private Date runEndDate;
	private String pcServerIp;
	private Long runResults;
	private String exceptionInfo;
	
	private Long runTime;

	// Constructors

	/** default constructor */
	public JobRunRecord() {
	}

	/** minimal constructor */
	public JobRunRecord(String id) {
		this.id = id;
	}

	/** full constructor */
	public JobRunRecord(String id, String jobName, Date runStartDate,
			Date runEndDate, String pcServerIp, Long runResults) {
		this.id = id;
		this.jobName = jobName;
		this.runStartDate = runStartDate;
		this.runEndDate = runEndDate;
		this.pcServerIp = pcServerIp;
		this.runResults = runResults;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "JOB_NAME", length = 100)
	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RUN_START_DATE", length = 7)
	public Date getRunStartDate() {
		return this.runStartDate;
	}

	public void setRunStartDate(Date runStartDate) {
		this.runStartDate = runStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RUN_END_DATE", length = 7)
	public Date getRunEndDate() {
		return this.runEndDate;
	}

	public void setRunEndDate(Date runEndDate) {
		this.runEndDate = runEndDate;
	}

	@Column(name = "PC_SERVER_IP", length = 50)
	public String getPcServerIp() {
		return this.pcServerIp;
	}

	public void setPcServerIp(String pcServerIp) {
		this.pcServerIp = pcServerIp;
	}

	@Column(name = "RUN_RESULTS", precision = 1, scale = 0)
	public Long getRunResults() {
		return this.runResults;
	}

	public void setRunResults(Long runResults) {
		this.runResults = runResults;
	}
	
	@Column(name = "EXCEPTION_INFO", length = 4000)
	public String getExceptionInfo() {
		return exceptionInfo;
	}

	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}

	@Transient
	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}

}