package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "apicases_report")
public class ApicasesReport extends Apicases {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    private Long projectid;

    /**
     * 用例id
     */
    private Long caseid;


    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    private String batchname;

    @Override
    public String getCasename() {
        return casename;
    }

    @Override
    public void setCasename(String casename) {
        this.casename = casename;
    }

    private String casename;


    /**
     * 执行计划id
     */
    private Long testplanid;

    /**
     * 运行结果，成功，失败，异常
     */
    private String status;

    /**
     * 返回结果
     */
    private String respone;
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

    public String getRequestheader() {
        return requestheader;
    }

    public void setRequestheader(String requestheader) {
        this.requestheader = requestheader;
    }

    public String getRequestdatas() {
        return requestdatas;
    }

    public void setRequestdatas(String requestdatas) {
        this.requestdatas = requestdatas;
    }

    private String requestheader;

    private String requestdatas;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestmethod() {
        return requestmethod;
    }

    public void setRequestmethod(String requestmethod) {
        this.requestmethod = requestmethod;
    }

    private String url;

    private String requestmethod;

    /**
     * 断言详细经过
     */
    private String assertvalue;

    /**
     * 运行时长
     */
    private Long runtime;

    /**
     * 期望值
     */
    private String expect;

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    private String errorinfo;

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
     * 获取用例id
     *
     * @return caseid - 用例id
     */
    public Long getCaseid() {
        return caseid;
    }

    /**
     * 设置用例id
     *
     * @param caseid 用例id
     */
    public void setCaseid(Long caseid) {
        this.caseid = caseid;
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
     * 获取运行结果，成功，失败，异常
     *
     * @return status - 运行结果，成功，失败，异常
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置运行结果，成功，失败，异常
     *
     * @param status 运行结果，成功，失败，异常
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取返回结果
     *
     * @return respone - 返回结果
     */
    public String getRespone() {
        return respone;
    }

    /**
     * 设置返回结果
     *
     * @param respone 返回结果
     */
    public void setRespone(String respone) {
        this.respone = respone;
    }

    /**
     * 获取断言详细经过
     *
     * @return assertvalue - 断言详细经过
     */
    public String getAssertvalue() {
        return assertvalue;
    }

    /**
     * 设置断言详细经过
     *
     * @param assertvalue 断言详细经过
     */
    public void setAssertvalue(String assertvalue) {
        this.assertvalue = assertvalue;
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
     * 获取期望值
     *
     * @return expect - 期望值
     */
    public String getExpect() {
        return expect;
    }

    /**
     * 设置期望值
     *
     * @param expect 期望值
     */
    public void setExpect(String expect) {
        this.expect = expect;
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