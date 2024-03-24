package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "condition_order")
public class ConditionOrder {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public String getConditiontype() {
        return conditiontype;
    }

    public void setConditiontype(String conditiontype) {
        this.conditiontype = conditiontype;
    }

    private String conditiontype;
    /**
     * 父条件id
     */
    private Long conditionid;

    /**
     * 子条件id
     */
    private Long subconditionid;

    /**
     * 子条件类型
     */
    private String subconditiontype;

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    private String orderstatus;



    /**
     * 子条件名
     */
    private String subconditionname;

    public String getConditionname() {
        return conditionname;
    }

    public void setConditionname(String conditionname) {
        this.conditionname = conditionname;
    }

    private String conditionname;


    /**
     * 条件顺序
     */
    private Long conditionorder;

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
     * 获取父条件id
     *
     * @return conditionid - 父条件id
     */
    public Long getConditionid() {
        return conditionid;
    }

    /**
     * 设置父条件id
     *
     * @param conditionid 父条件id
     */
    public void setConditionid(Long conditionid) {
        this.conditionid = conditionid;
    }

    /**
     * 获取子条件id
     *
     * @return subconditionid - 子条件id
     */
    public Long getSubconditionid() {
        return subconditionid;
    }

    /**
     * 设置子条件id
     *
     * @param subconditionid 子条件id
     */
    public void setSubconditionid(Long subconditionid) {
        this.subconditionid = subconditionid;
    }

    /**
     * 获取子条件类型
     *
     * @return subconditiontype - 子条件类型
     */
    public String getSubconditiontype() {
        return subconditiontype;
    }

    /**
     * 设置子条件类型
     *
     * @param subconditiontype 子条件类型
     */
    public void setSubconditiontype(String subconditiontype) {
        this.subconditiontype = subconditiontype;
    }

    /**
     * 获取子条件名
     *
     * @return subconditionname - 子条件名
     */
    public String getSubconditionname() {
        return subconditionname;
    }

    /**
     * 设置子条件名
     *
     * @param subconditionname 子条件名
     */
    public void setSubconditionname(String subconditionname) {
        this.subconditionname = subconditionname;
    }

    /**
     * 获取条件顺序
     *
     * @return conditionorder - 条件顺序
     */
    public Long getConditionorder() {
        return conditionorder;
    }

    /**
     * 设置条件顺序
     *
     * @param conditionorder 条件顺序
     */
    public void setConditionorder(Long conditionorder) {
        this.conditionorder = conditionorder;
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