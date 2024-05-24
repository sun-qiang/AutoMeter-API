package com.api.autotest.dto;

import java.util.Date;

public class ApicasesReportstatics {
    /**
     * 执行计划id
     */
    private String testplanid;

    public String getDeployunitid() {
        return deployunitid;
    }

    public void setDeployunitid(String deployunitid) {
        this.deployunitid = deployunitid;
    }

    private String deployunitid;

    /**
     * 批次
     */
    private String batchname;


    public String getTestplanname() {
        return testplanname;
    }

    public void setTestplanname(String testplanname) {
        this.testplanname = testplanname;
    }

    private String testplanname;

    /**
     * 执行机id
     */
    private String slaverid;

    /**
     * 用例总数
     */
    private String totalcases;

    /**
     * 用例总数
     */
    private String totalpasscases;

    /**
     * 用例总数
     */
    private String totalfailcases;

    /**
     * 运行时长
     */
    private String runtime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上一次修改时间
     */
    private Date lastmodifyTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 获取执行计划id
     *
     * @return testplanid - 执行计划id
     */
    public String getTestplanid() {
        return testplanid;
    }

    /**
     * 设置执行计划id
     *
     * @param testplanid 执行计划id
     */
    public void setTestplanid(String testplanid) {
        this.testplanid = testplanid;
    }

    /**
     * 获取批次
     *
     * @return batchname - 批次
     */
    public String getBatchname() {
        return batchname;
    }

    /**
     * 设置批次
     *
     * @param batchname 批次
     */
    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    /**
     * 获取执行机id
     *
     * @return slaverid - 执行机id
     */
    public String getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机id
     *
     * @param slaverid 执行机id
     */
    public void setSlaverid(String slaverid) {
        this.slaverid = slaverid;
    }

    /**
     * 获取用例总数
     *
     * @return totalcases - 用例总数
     */
    public String getTotalcases() {
        return totalcases;
    }

    /**
     * 设置用例总数
     *
     * @param totalcases 用例总数
     */
    public void setTotalcases(String totalcases) {
        this.totalcases = totalcases;
    }

    /**
     * 获取用例总数
     *
     * @return totalpasscases - 用例总数
     */
    public String getTotalpasscases() {
        return totalpasscases;
    }

    /**
     * 设置用例总数
     *
     * @param totalpasscases 用例总数
     */
    public void setTotalpasscases(String totalpasscases) {
        this.totalpasscases = totalpasscases;
    }

    /**
     * 获取用例总数
     *
     * @return totalfailcases - 用例总数
     */
    public String getTotalfailcases() {
        return totalfailcases;
    }

    /**
     * 设置用例总数
     *
     * @param totalfailcases 用例总数
     */
    public void setTotalfailcases(String totalfailcases) {
        this.totalfailcases = totalfailcases;
    }

    /**
     * 获取运行时长
     *
     * @return runtime - 运行时长
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * 设置运行时长
     *
     * @param runtime 运行时长
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取上一次修改时间
     *
     * @return lastmodify_time - 上一次修改时间
     */
    public Date getLastmodifyTime() {
        return lastmodifyTime;
    }

    /**
     * 设置上一次修改时间
     *
     * @param lastmodifyTime 上一次修改时间
     */
    public void setLastmodifyTime(Date lastmodifyTime) {
        this.lastmodifyTime = lastmodifyTime;
    }

    /**
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
}