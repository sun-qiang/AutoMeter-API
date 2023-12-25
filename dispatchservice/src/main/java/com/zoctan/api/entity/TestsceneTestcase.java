package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "testscene_testcase")
public class TestsceneTestcase {
    /**
     * 主键Id
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


    public Long getLoopnums() {
        return loopnums;
    }

    public void setLoopnums(Long loopnums) {
        this.loopnums = loopnums;
    }

    public Long getThreadnums() {
        return threadnums;
    }

    public void setThreadnums(Long threadnums) {
        this.threadnums = threadnums;
    }

    private Long threadnums;
    private Long loopnums;

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

    /**
     * 测试场景id
     */
    private Long testscenenid;


    public Long getModelid() {
        return modelid;
    }

    public void setModelid(Long modelid) {
        this.modelid = modelid;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    private Long modelid;
    private String modelname;



    /**
     * 场景名
     */
    private String scenename;

    /**
     * apiid
     */
    private Long apiid;

    /**
     * 微服务id
     */
    private Long deployunitid;

    /**
     * 微服务
     */
    private String deployunitname;

    /**
     * API名
     */
    private String apiname;

    /**
     * 用例id
     */
    private Long testcaseid;

    /**
     * 用例名
     */
    private String casename;

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
     * 操作人
     */
    private String creator;

    /**
     * 用例顺序
     */
    private Long caseorder;

    /**
     * 项目id
     */
    private Long projectid;

    /**
     * 获取主键Id
     *
     * @return id - 主键Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键Id
     *
     * @param id 主键Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取测试场景id
     *
     * @return testscenenid - 测试场景id
     */
    public Long getTestscenenid() {
        return testscenenid;
    }

    /**
     * 设置测试场景id
     *
     * @param testscenenid 测试场景id
     */
    public void setTestscenenid(Long testscenenid) {
        this.testscenenid = testscenenid;
    }

    /**
     * 获取场景名
     *
     * @return scenename - 场景名
     */
    public String getScenename() {
        return scenename;
    }

    /**
     * 设置场景名
     *
     * @param scenename 场景名
     */
    public void setScenename(String scenename) {
        this.scenename = scenename;
    }

    /**
     * 获取apiid
     *
     * @return apiid - apiid
     */
    public Long getApiid() {
        return apiid;
    }

    /**
     * 设置apiid
     *
     * @param apiid apiid
     */
    public void setApiid(Long apiid) {
        this.apiid = apiid;
    }

    /**
     * 获取微服务id
     *
     * @return deployunitid - 微服务id
     */
    public Long getDeployunitid() {
        return deployunitid;
    }

    /**
     * 设置微服务id
     *
     * @param deployunitid 微服务id
     */
    public void setDeployunitid(Long deployunitid) {
        this.deployunitid = deployunitid;
    }

    /**
     * 获取微服务
     *
     * @return deployunitname - 微服务
     */
    public String getDeployunitname() {
        return deployunitname;
    }

    /**
     * 设置微服务
     *
     * @param deployunitname 微服务
     */
    public void setDeployunitname(String deployunitname) {
        this.deployunitname = deployunitname;
    }

    /**
     * 获取API名
     *
     * @return apiname - API名
     */
    public String getApiname() {
        return apiname;
    }

    /**
     * 设置API名
     *
     * @param apiname API名
     */
    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    /**
     * 获取用例id
     *
     * @return testcaseid - 用例id
     */
    public Long getTestcaseid() {
        return testcaseid;
    }

    /**
     * 设置用例id
     *
     * @param testcaseid 用例id
     */
    public void setTestcaseid(Long testcaseid) {
        this.testcaseid = testcaseid;
    }

    /**
     * 获取用例名
     *
     * @return casename - 用例名
     */
    public String getCasename() {
        return casename;
    }

    /**
     * 设置用例名
     *
     * @param casename 用例名
     */
    public void setCasename(String casename) {
        this.casename = casename;
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
     * 获取操作人
     *
     * @return creator - 操作人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置操作人
     *
     * @param creator 操作人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取用例顺序
     *
     * @return caseorder - 用例顺序
     */
    public Long getCaseorder() {
        return caseorder;
    }

    /**
     * 设置用例顺序
     *
     * @param caseorder 用例顺序
     */
    public void setCaseorder(Long caseorder) {
        this.caseorder = caseorder;
    }

    /**
     * 获取项目id
     *
     * @return projectid - 项目id
     */
    public Long getProjectid() {
        return projectid;
    }

    /**
     * 设置项目id
     *
     * @param projectid 项目id
     */
    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }
}