package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Enviromentvariables {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * key名
     */
    private String variablesname;

    /**
     * 环境id
     */
    private Long envid;

    /**
     * 环境名
     */
    private String envname;

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
     * 项目id
     */
    private Long projectid;

    private String variablesvalue;

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
     * 获取key名
     *
     * @return variablesname - key名
     */
    public String getVariablesname() {
        return variablesname;
    }

    /**
     * 设置key名
     *
     * @param variablesname key名
     */
    public void setVariablesname(String variablesname) {
        this.variablesname = variablesname;
    }

    /**
     * 获取环境id
     *
     * @return envid - 环境id
     */
    public Long getEnvid() {
        return envid;
    }

    /**
     * 设置环境id
     *
     * @param envid 环境id
     */
    public void setEnvid(Long envid) {
        this.envid = envid;
    }

    /**
     * 获取环境名
     *
     * @return envname - 环境名
     */
    public String getEnvname() {
        return envname;
    }

    /**
     * 设置环境名
     *
     * @param envname 环境名
     */
    public void setEnvname(String envname) {
        this.envname = envname;
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
     * @return variablesvalue
     */
    public String getVariablesvalue() {
        return variablesvalue;
    }

    /**
     * @param variablesvalue
     */
    public void setVariablesvalue(String variablesvalue) {
        this.variablesvalue = variablesvalue;
    }
}