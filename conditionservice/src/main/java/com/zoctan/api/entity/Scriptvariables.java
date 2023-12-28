package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Scriptvariables {
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


    public Long getConditionid() {
        return conditionid;
    }

    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    public String getConditionname() {
        return conditionname;
    }

    public void setConditionname(String conditionname) {
        this.conditionname = conditionname;
    }

    private Long conditionid;
    private String conditionname;

    /**
     * 变量名
     */
    private String scriptvariablesname;

    /**
     * 变量描述
     */
    private String variablesdes;

    /**
     * 变量值类型
     */
    private String valuetype;

    /**
     * 备注
     */
    private String memo;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

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
     * 获取变量名
     *
     * @return scriptvariablesname - 变量名
     */
    public String getScriptvariablesname() {
        return scriptvariablesname;
    }

    /**
     * 设置变量名
     *
     * @param scriptvariablesname 变量名
     */
    public void setScriptvariablesname(String scriptvariablesname) {
        this.scriptvariablesname = scriptvariablesname;
    }

    /**
     * 获取变量描述
     *
     * @return variablesdes - 变量描述
     */
    public String getVariablesdes() {
        return variablesdes;
    }

    /**
     * 设置变量描述
     *
     * @param variablesdes 变量描述
     */
    public void setVariablesdes(String variablesdes) {
        this.variablesdes = variablesdes;
    }

    /**
     * 获取变量值类型
     *
     * @return valuetype - 变量值类型
     */
    public String getValuetype() {
        return valuetype;
    }

    /**
     * 设置变量值类型
     *
     * @param valuetype 变量值类型
     */
    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
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