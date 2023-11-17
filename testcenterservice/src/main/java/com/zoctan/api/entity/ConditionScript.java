package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "condition_script")
public class ConditionScript {
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

    public String getConditiontype() {
        return conditiontype;
    }

    public void setConditiontype(String conditiontype) {
        this.conditiontype = conditiontype;
    }

    private String conditiontype;

    /**
     * 条件id，只取类型为用例
     */
    private Long conditionid;

    public String getConditionname() {
        return conditionname;
    }

    public void setConditionname(String conditionname) {
        this.conditionname = conditionname;
    }

    private String conditionname;


    public String getSubconditionname() {
        return subconditionname;
    }

    public void setSubconditionname(String subconditionname) {
        this.subconditionname = subconditionname;
    }

    private String subconditionname;
    /**
     * 脚本
     */
    private String script;

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
     * 获取条件id，只取类型为用例
     *
     * @return conditionid - 条件id，只取类型为用例
     */
    public Long getConditionid() {
        return conditionid;
    }

    /**
     * 设置条件id，只取类型为用例
     *
     * @param conditionid 条件id，只取类型为用例
     */
    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    /**
     * 获取脚本
     *
     * @return script - 脚本
     */
    public String getScript() {
        return script;
    }

    /**
     * 设置脚本
     *
     * @param script 脚本
     */
    public void setScript(String script) {
        this.script = script;
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