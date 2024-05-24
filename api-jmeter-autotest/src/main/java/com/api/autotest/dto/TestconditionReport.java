package com.api.autotest.dto;

import java.util.Date;

public class TestconditionReport {

    private Long id;
    /**
     * 条件id
     */
    private Long conditionid;

    public Long getSubconditionid() {
        return subconditionid;
    }

    public void setSubconditionid(Long subconditionid) {
        this.subconditionid = subconditionid;
    }

    private Long subconditionid;

    public String getSubconditionname() {
        return subconditionname;
    }

    public void setSubconditionname(String subconditionname) {
        this.subconditionname = subconditionname;
    }

    private String subconditionname;

    public Long getTestplanid() {
        return testplanid;
    }

    public void setTestplanid(Long testplanid) {
        this.testplanid = testplanid;
    }

    public String getPlanname() {
        return planname;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    private Long testplanid;
    private String planname;
    private String batchname;

    public String getSubconditiontype() {
        return subconditiontype;
    }

    public void setSubconditiontype(String subconditiontype) {
        this.subconditiontype = subconditiontype;
    }

    private String subconditiontype;



    /**
     * 条件类型，接口，数据库，其他
     */
    private String conditiontype;

    /**
     * 子条件id，接口，db，nosql条件id
     */
    /**
     * 接口返回，数据库返回结果等等
     */
    private String conditionresult;

    /**
     * 条件完成状态，成功，失败
     */
    private String conditionstatus;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;


    /**
     * 运行时长
     */
    private Long runtime;

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
     * 获取条件id
     *
     * @return conditionid - 条件id
     */
    public Long getConditionid() {
        return conditionid;
    }

    /**
     * 设置条件id
     *
     * @param conditionid 条件id
     */
    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    /**
     * 获取条件类型，接口，数据库，其他
     *
     * @return conditiontype - 条件类型，接口，数据库，其他
     */
    public String getConditiontype() {
        return conditiontype;
    }

    /**
     * 设置条件类型，接口，数据库，其他
     *
     * @param conditiontype 条件类型，接口，数据库，其他
     */
    public void setConditiontype(String conditiontype) {
        this.conditiontype = conditiontype;
    }

    /**
     * 获取接口返回，数据库返回结果等等
     *
     * @return conditionresult - 接口返回，数据库返回结果等等
     */
    public String getConditionresult() {
        return conditionresult;
    }

    /**
     * 设置接口返回，数据库返回结果等等
     *
     * @param conditionresult 接口返回，数据库返回结果等等
     */
    public void setConditionresult(String conditionresult) {
        this.conditionresult = conditionresult;
    }

    /**
     * 获取条件完成状态，成功，失败
     *
     * @return conditionstatus - 条件完成状态，成功，失败
     */
    public String getConditionstatus() {
        return conditionstatus;
    }

    /**
     * 设置条件完成状态，成功，失败
     *
     * @param conditionstatus 条件完成状态，成功，失败
     */
    public void setConditionstatus(String conditionstatus) {
        this.conditionstatus = conditionstatus;
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