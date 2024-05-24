package com.api.autotest.model;

import java.util.Date;

/**
 * Created by harvey.xu on 2017/7/20.
 */
public class AutoTestTaskModel {

    private Long id;
    private String batchNo;
    private Integer batchStatus;
    private Integer env;
    private String creator;
    private Integer caseTotal;
    private Integer successCaseTotal;
    private Float successRate;
    private Date createTime;
    private Date updateTime;
    private String createTimeBegin;
    private String createTimeEnd;
    private Integer startNum;
    private Integer pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(Integer batchStatus) {
        this.batchStatus = batchStatus;
    }

    public Integer getEnv() {
        return env;
    }

    public void setEnv(Integer env) {
        this.env = env;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCaseTotal() {
        return caseTotal;
    }

    public void setCaseTotal(Integer caseTotal) {
        this.caseTotal = caseTotal;
    }

    public Integer getSuccessCaseTotal() {
        return successCaseTotal;
    }

    public void setSuccessCaseTotal(Integer successCaseTotal) {
        this.successCaseTotal = successCaseTotal;
    }

    public Float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Float successRate) {
        this.successRate = successRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Integer getStartNum() {
        return startNum;
    }

    public void setStartNum(Integer startNum) {
        this.startNum = startNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
