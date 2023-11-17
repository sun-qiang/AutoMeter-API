package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "executeplan_testcase")
public class ExecuteplanTestcase {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 执行计划id
     */
    private Long executeplanid;

    public Long getApiid() {
        return apiid;
    }

    public void setApiid(Long apiid) {
        this.apiid = apiid;
    }

    private Long apiid;

    public Long getDeployunitid() {
        return deployunitid;
    }

    public void setDeployunitid(Long deployunitid) {
        this.deployunitid = deployunitid;
    }

    private Long deployunitid;

    public Long getCaseorder() {
        return caseorder;
    }

    public void setCaseorder(Long caseorder) {
        this.caseorder = caseorder;
    }

    private Long caseorder;

    /**
     * 微服务
     */
    private String deployunitname;

    public String getExecuteplanname() {
        return executeplanname;
    }

    public void setExecuteplanname(String executeplanname) {
        this.executeplanname = executeplanname;
    }

    private String executeplanname;


    /**
     * API名
     */
    private String apiname;

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    private String expect;


    /**
     * 用例id
     */
    private Long testcaseid;

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






    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    /**
     * 用例名
     */
    private String casename;

    public String getCasejmxname() {
        return casejmxname;
    }

    public void setCasejmxname(String casejmxname) {
        this.casejmxname = casejmxname;
    }

    private String casejmxname;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 创建者
     */
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
     * 获取执行计划id
     *
     * @return executeplanid - 执行计划id
     */
    public Long getExecuteplanid() {
        return executeplanid;
    }

    /**
     * 设置执行计划id
     *
     * @param executeplanid 执行计划id
     */
    public void setExecuteplanid(Long executeplanid) {
        this.executeplanid = executeplanid;
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
}