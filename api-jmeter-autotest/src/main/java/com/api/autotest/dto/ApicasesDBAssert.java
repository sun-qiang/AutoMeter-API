package com.api.autotest.dto;

import java.util.Date;

public class ApicasesDBAssert {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseid() {
        return caseid;
    }

    public void setCaseid(Long caseid) {
        this.caseid = caseid;
    }

    public Long getAssembleid() {
        return assembleid;
    }

    public void setAssembleid(Long assembleid) {
        this.assembleid = assembleid;
    }

    public Long getEnvid() {
        return envid;
    }

    public void setEnvid(Long envid) {
        this.envid = envid;
    }

    public String getAssemblename() {
        return assemblename;
    }

    public void setAssemblename(String assemblename) {
        this.assemblename = assemblename;
    }

    public String getEnviroment() {
        return enviroment;
    }

    public void setEnviroment(String enviroment) {
        this.enviroment = enviroment;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Long getExpectrecordsnums() {
        return expectrecordsnums;
    }

    public void setExpectrecordsnums(Long expectrecordsnums) {
        this.expectrecordsnums = expectrecordsnums;
    }

    private Long id;
    /**
     * 用例id
     */
    private Long caseid;
    private Long assembleid;
    private Long envid;
    private String assemblename;
    private String enviroment;
    private String expression;
    private Long expectrecordsnums;

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
