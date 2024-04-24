package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Apicases {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mid;

    public Long getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(Long creatorid) {
        this.creatorid = creatorid;
    }

    private Long creatorid;

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getMnickname() {
        return mnickname;
    }

    public void setMnickname(String mnickname) {
        this.mnickname = mnickname;
    }

    private String mnickname;

    private Long modelid;

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

    private String modelname;


    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    private Long projectid;

    /**
     * API
     */
    private String apiname;

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

    public Long getThreadnum() {
        return threadnum;
    }

    public void setThreadnum(Long threadnum) {
        this.threadnum = threadnum;
    }

    private Long threadnum;

    public Long getLoops() {
        return loops;
    }

    public void setLoops(Long loops) {
        this.loops = loops;
    }

    private Long loops;

    public String getMiddleparam() {
        return middleparam;
    }

    public void setMiddleparam(String middleparam) {
        this.middleparam = middleparam;
    }

    private String middleparam;

    /**
     * 微服务
     */
    private String deployunitname;

    /**
     * 用例名
     */
    private String casename;

    /**
     * 用例jmx名，和jmx文件名对齐
     */
    private String casejmxname;

    /**
     * 用例内容，以英文逗号分开，提供jar获取自定义期望结果A：1的值，入参为冒号左边的内容
     */
    private String casecontent;


    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    private String  casetype;

    /**
     * 期望值
     */
    private String expect;


    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
    /**
     * 优先级
     */
    private Long level;

    /**
     * 备注
     */
    private String memo;

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
     * 获取API
     *
     * @return apiname - API
     */
    public String getApiname() {
        return apiname;
    }

    /**
     * 设置API
     *
     * @param apiname API
     */
    public void setApiname(String apiname) {
        this.apiname = apiname;
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
     * 获取用例jmx名，和jmx文件名对齐
     *
     * @return casejmxname - 用例jmx名，和jmx文件名对齐
     */
    public String getCasejmxname() {
        return casejmxname;
    }

    /**
     * 设置用例jmx名，和jmx文件名对齐
     *
     * @param casejmxname 用例jmx名，和jmx文件名对齐
     */
    public void setCasejmxname(String casejmxname) {
        this.casejmxname = casejmxname;
    }

    /**
     * 获取用例内容，以英文逗号分开，提供jar获取自定义期望结果A：1的值，入参为冒号左边的内容
     *
     * @return casecontent - 用例内容，以英文逗号分开，提供jar获取自定义期望结果A：1的值，入参为冒号左边的内容
     */
    public String getCasecontent() {
        return casecontent;
    }

    /**
     * 设置用例内容，以英文逗号分开，提供jar获取自定义期望结果A：1的值，入参为冒号左边的内容
     *
     * @param casecontent 用例内容，以英文逗号分开，提供jar获取自定义期望结果A：1的值，入参为冒号左边的内容
     */
    public void setCasecontent(String casecontent) {
        this.casecontent = casecontent;
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
     * 获取备注
     *
     * @return memo - 备注
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置备注
     *
     * @param memo 备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
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