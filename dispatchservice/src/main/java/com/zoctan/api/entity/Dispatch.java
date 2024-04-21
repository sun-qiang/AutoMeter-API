package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Dispatch {
    /**
     * 环境Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getStopflag() {
        return stopflag;
    }

    public void setStopflag(String stopflag) {
        this.stopflag = stopflag;
    }

    private String stopflag;

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    private Long projectid;


    /**
     * 执行计划Id
     */
    private Long execplanid;

    public Long getCaseorder() {
        return caseorder;
    }

    public void setCaseorder(Long caseorder) {
        this.caseorder = caseorder;
    }

    private Long caseorder;



    public Long getSceneid() {
        return sceneid;
    }

    public void setSceneid(Long sceneid) {
        this.sceneid = sceneid;
    }

    public String getScenename() {
        return scenename;
    }

    public void setScenename(String scenename) {
        this.scenename = scenename;
    }

    private Long sceneid;
    private String scenename;


    public String getPlantype() {
        return plantype;
    }

    public void setPlantype(String plantype) {
        this.plantype = plantype;
    }

    private String plantype;

    /**
     * 执行计划名
     */
    private String execplanname;

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    private String batchname;


    /**
     * 执行机Id
     */
    private Long slaverid;

    public Long getBatchid() {
        return batchid;
    }

    public void setBatchid(Long batchid) {
        this.batchid = batchid;
    }

    private Long batchid;

    public String getCasejmxname() {
        return casejmxname;
    }

    public void setCasejmxname(String casejmxname) {
        this.casejmxname = casejmxname;
    }

    private String casejmxname;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    private String expect;

    public String getDeployunitname() {
        return deployunitname;
    }

    public void setDeployunitname(String deployunitname) {
        this.deployunitname = deployunitname;
    }

    private String deployunitname;

    /**
     * 执行机名
     */
    private String slavername;

    /**
     * 用例Id
     */
    private Long testcaseid;

    /**
     * 用例名
     */
    private String testcasename;

    /**
     * 待分配，已分配，已结束
     */
    private String status;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    private String memo;


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private String creator;


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
     * 获取环境Id
     *
     * @return id - 环境Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置环境Id
     *
     * @param id 环境Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取执行计划Id
     *
     * @return execplanid - 执行计划Id
     */
    public Long getExecplanid() {
        return execplanid;
    }

    /**
     * 设置执行计划Id
     *
     * @param execplanid 执行计划Id
     */
    public void setExecplanid(Long execplanid) {
        this.execplanid = execplanid;
    }

    /**
     * 获取执行计划名
     *
     * @return execplanname - 执行计划名
     */
    public String getExecplanname() {
        return execplanname;
    }

    /**
     * 设置执行计划名
     *
     * @param execplanname 执行计划名
     */
    public void setExecplanname(String execplanname) {
        this.execplanname = execplanname;
    }

    /**
     * 获取执行机Id
     *
     * @return slaverid - 执行机Id
     */
    public Long getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机Id
     *
     * @param slaverid 执行机Id
     */
    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
    }

    /**
     * 获取执行机名
     *
     * @return slavername - 执行机名
     */
    public String getSlavername() {
        return slavername;
    }

    /**
     * 设置执行机名
     *
     * @param slavername 执行机名
     */
    public void setSlavername(String slavername) {
        this.slavername = slavername;
    }

    /**
     * 获取用例Id
     *
     * @return testcaseid - 用例Id
     */
    public Long getTestcaseid() {
        return testcaseid;
    }

    /**
     * 设置用例Id
     *
     * @param testcaseid 用例Id
     */
    public void setTestcaseid(Long testcaseid) {
        this.testcaseid = testcaseid;
    }

    /**
     * 获取用例名
     *
     * @return testcasename - 用例名
     */
    public String getTestcasename() {
        return testcasename;
    }

    /**
     * 设置用例名
     *
     * @param testcasename 用例名
     */
    public void setTestcasename(String testcasename) {
        this.testcasename = testcasename;
    }

    /**
     * 获取待分配，已分配，已结束
     *
     * @return status - 待分配，已分配，已结束
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置待分配，已分配，已结束
     *
     * @param status 待分配，已分配，已结束
     */
    public void setStatus(String status) {
        this.status = status;
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


    public Long getThreadnum() {
        return threadnum;
    }

    public void setThreadnum(Long threadnum) {
        this.threadnum = threadnum;
    }

    public Long getLoops() {
        return loops;
    }

    public void setLoops(Long loops) {
        this.loops = loops;
    }

    private Long threadnum;
    private Long loops;
}