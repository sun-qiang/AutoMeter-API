package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "testplan_testscene")
public class TestplanTestscene {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 测试场景id
     */
    private Long testscenenid;

    /**
     * 场景名
     */
    private String scenename;

    /**
     * 集合id
     */
    private Long testplanid;

    /**
     * 集合名
     */
    private String planname;

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
     * 顺序
     */
    private Long ordernum;

    /**
     * 操作人
     */
    private String creator;

    /**
     * 项目id
     */
    private Long projectid;

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
     * 获取测试场景id
     *
     * @return testscenenid - 测试场景id
     */
    public Long getTestscenenid() {
        return testscenenid;
    }

    /**
     * 设置测试场景id
     *
     * @param testscenenid 测试场景id
     */
    public void setTestscenenid(Long testscenenid) {
        this.testscenenid = testscenenid;
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
     * 获取集合id
     *
     * @return testplanid - 集合id
     */
    public Long getTestplanid() {
        return testplanid;
    }

    /**
     * 设置集合id
     *
     * @param testplanid 集合id
     */
    public void setTestplanid(Long testplanid) {
        this.testplanid = testplanid;
    }

    /**
     * 获取集合名
     *
     * @return planname - 集合名
     */
    public String getPlanname() {
        return planname;
    }

    /**
     * 设置集合名
     *
     * @param planname 集合名
     */
    public void setPlanname(String planname) {
        this.planname = planname;
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
     * 获取顺序
     *
     * @return ordernum - 顺序
     */
    public Long getOrdernum() {
        return ordernum;
    }

    /**
     * 设置顺序
     *
     * @param ordernum 顺序
     */
    public void setOrdernum(Long ordernum) {
        this.ordernum = ordernum;
    }

    /**
     * 获取操作人
     *
     * @return creator - 操作人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置操作人
     *
     * @param creator 操作人
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
}