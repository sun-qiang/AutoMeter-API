package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "apicases_reportstatics")
public class ApicasesReportstatics {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 执行计划id
     */
    private Long testplanid;

    public Long getDeployunitid() {
        return deployunitid;
    }

    public void setDeployunitid(Long deployunitid) {
        this.deployunitid = deployunitid;
    }

    private Long deployunitid;

    private String batchname;

    public String getDeployunitname() {
        return deployunitname;
    }

    public void setDeployunitname(String deployunitname) {
        this.deployunitname = deployunitname;
    }

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    public String getExecuteplanname() {
        return executeplanname;
    }

    public void setExecuteplanname(String executeplanname) {
        this.executeplanname = executeplanname;
    }

    private String deployunitname;

    private Long projectid;
    private String executeplanname;



    /**
     * 批次
     */

    /**
     * 执行机id
     */
    private Long slaverid;

    /**
     * 用例总数
     */
    private Long totalcases;

    /**
     * 用例总数
     */
    private Long totalpasscases;

    /**
     * 用例总数
     */
    private Long totalfailcases;

    /**
     * 运行时长
     */
    private Long runtime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上一次修改时间
     */
    @Column(name = "lastmodify_time")
    private Date lastmodifyTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 获取Id
     *
     * @return id - Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置Id
     *
     * @param id Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取执行计划id
     *
     * @return testplanid - 执行计划id
     */
    public Long getTestplanid() {
        return testplanid;
    }

    /**
     * 设置执行计划id
     *
     * @param testplanid 执行计划id
     */
    public void setTestplanid(Long testplanid) {
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
    public Long getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机id
     *
     * @param slaverid 执行机id
     */
    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
    }

    /**
     * 获取用例总数
     *
     * @return totalcases - 用例总数
     */
    public Long getTotalcases() {
        return totalcases;
    }

    /**
     * 设置用例总数
     *
     * @param totalcases 用例总数
     */
    public void setTotalcases(Long totalcases) {
        this.totalcases = totalcases;
    }

    /**
     * 获取用例总数
     *
     * @return totalpasscases - 用例总数
     */
    public Long getTotalpasscases() {
        return totalpasscases;
    }

    /**
     * 设置用例总数
     *
     * @param totalpasscases 用例总数
     */
    public void setTotalpasscases(Long totalpasscases) {
        this.totalpasscases = totalpasscases;
    }

    /**
     * 获取用例总数
     *
     * @return totalfailcases - 用例总数
     */
    public Long getTotalfailcases() {
        return totalfailcases;
    }

    /**
     * 设置用例总数
     *
     * @param totalfailcases 用例总数
     */
    public void setTotalfailcases(Long totalfailcases) {
        this.totalfailcases = totalfailcases;
    }

    /**
     * 获取运行时长
     *
     * @return runtime - 运行时长
     */
    public Long getRuntime() {
        return runtime;
    }

    /**
     * 设置运行时长
     *
     * @param runtime 运行时长
     */
    public void setRuntime(Long runtime) {
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