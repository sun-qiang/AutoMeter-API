package com.api.autotest.dto;

import java.util.Date;

public class TestvariablesValue {
    /**
     * Id
     */
    private Long id;



    /**
     * 计划Id
     */
    private Long planid;

    public Long getSlaverid() {
        return slaverid;
    }

    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
    }

    private Long slaverid;


    /**
     * 计划名
     */
    private String planname;

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    private String batchname;



    /**
     * 用例Id
     */
    private Long caseid;

    /**
     * 用例名
     */
    private String casename;

    /**
     * 变量Id
     */
    private Long variablesid;

    public Long getConditionid() {
        return conditionid;
    }

    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    public String getConditiontype() {
        return conditiontype;
    }

    public void setConditiontype(String conditiontype) {
        this.conditiontype = conditiontype;
    }

    private Long conditionid;

    private String conditiontype;




    /**
     * 变量名
     */
    private String variablesname;

    /**
     * 变量值
     */
    private String variablesvalue;


    public String getVariablestype() {
        return variablestype;
    }

    public void setVariablestype(String variablestype) {
        this.variablestype = variablestype;
    }

    private String variablestype;

    /**
     * 备注
     */
    private String memo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上一次修改时间
     */
    private Date lastmodifyTime;

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
     * 获取计划Id
     *
     * @return planid - 计划Id
     */
    public Long getPlanid() {
        return planid;
    }

    /**
     * 设置计划Id
     *
     * @param planid 计划Id
     */
    public void setPlanid(Long planid) {
        this.planid = planid;
    }

    /**
     * 获取计划名
     *
     * @return planname - 计划名
     */
    public String getPlanname() {
        return planname;
    }

    /**
     * 设置计划名
     *
     * @param planname 计划名
     */
    public void setPlanname(String planname) {
        this.planname = planname;
    }

    /**
     * 获取用例Id
     *
     * @return caseid - 用例Id
     */
    public Long getCaseid() {
        return caseid;
    }

    /**
     * 设置用例Id
     *
     * @param caseid 用例Id
     */
    public void setCaseid(Long caseid) {
        this.caseid = caseid;
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
     * 获取变量Id
     *
     * @return variablesid - 变量Id
     */
    public Long getVariablesid() {
        return variablesid;
    }

    /**
     * 设置变量Id
     *
     * @param variablesid 变量Id
     */
    public void setVariablesid(Long variablesid) {
        this.variablesid = variablesid;
    }

    /**
     * 获取变量名
     *
     * @return variablesname - 变量名
     */
    public String getVariablesname() {
        return variablesname;
    }

    /**
     * 设置变量名
     *
     * @param variablesname 变量名
     */
    public void setVariablesname(String variablesname) {
        this.variablesname = variablesname;
    }

    /**
     * 获取变量值
     *
     * @return variablesvalue - 变量值
     */
    public String getVariablesvalue() {
        return variablesvalue;
    }

    /**
     * 设置变量值
     *
     * @param variablesvalue 变量值
     */
    public void setVariablesvalue(String variablesvalue) {
        this.variablesvalue = variablesvalue;
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
}