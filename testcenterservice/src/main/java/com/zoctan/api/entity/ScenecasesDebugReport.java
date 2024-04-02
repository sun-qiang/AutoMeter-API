package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "scenecases_debug_report")
public class ScenecasesDebugReport {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用例id
     */
    private Long caseid;

    /**
     * 执行计划id
     */
    private Long testplanid;

    /**
     * 批次
     */
    private String batchname;

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    private String casename;


    /**
     * 运行结果，成功，失败，异常
     */
    private String status;

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
     * 地址
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestmethod;

    /**
     * 项目id
     */
    private Long projectid;

    /**
     * 场景id
     */
    private Long sceneid;

    /**
     * 场景名
     */
    private String scenename;

    /**
     * 返回结果
     */
    private String respone;

    /**
     * 断言详细经过
     */
    private String assertvalue;

    /**
     * 期望值
     */
    private String expect;

    /**
     * 错误信息
     */
    private String errorinfo;

    /**
     * 请求头数据
     */
    private String requestheader;

    /**
     * 请求数据
     */
    private String requestdatas;

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

    /**
     * 获取地址
     *
     * @return url - 地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置地址
     *
     * @param url 地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取请求方式
     *
     * @return requestmethod - 请求方式
     */
    public String getRequestmethod() {
        return requestmethod;
    }

    /**
     * 设置请求方式
     *
     * @param requestmethod 请求方式
     */
    public void setRequestmethod(String requestmethod) {
        this.requestmethod = requestmethod;
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

    /**
     * 获取场景id
     *
     * @return sceneid - 场景id
     */
    public Long getSceneid() {
        return sceneid;
    }

    /**
     * 设置场景id
     *
     * @param sceneid 场景id
     */
    public void setSceneid(Long sceneid) {
        this.sceneid = sceneid;
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
     * 获取错误信息
     *
     * @return errorinfo - 错误信息
     */
    public String getErrorinfo() {
        return errorinfo;
    }

    /**
     * 设置错误信息
     *
     * @param errorinfo 错误信息
     */
    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    /**
     * 获取请求头数据
     *
     * @return requestheader - 请求头数据
     */
    public String getRequestheader() {
        return requestheader;
    }

    /**
     * 设置请求头数据
     *
     * @param requestheader 请求头数据
     */
    public void setRequestheader(String requestheader) {
        this.requestheader = requestheader;
    }

    /**
     * 获取请求数据
     *
     * @return requestdatas - 请求数据
     */
    public String getRequestdatas() {
        return requestdatas;
    }

    /**
     * 设置请求数据
     *
     * @param requestdatas 请求数据
     */
    public void setRequestdatas(String requestdatas) {
        this.requestdatas = requestdatas;
    }
}