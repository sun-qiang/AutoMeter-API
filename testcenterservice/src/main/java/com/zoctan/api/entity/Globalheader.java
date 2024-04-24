package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Globalheader {
    /**
     * 主键Id
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


    /**
     * 全局header名
     */
    private String globalheadername;

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
     * 获取全局header名
     *
     * @return globalheadername - 全局header名
     */
    public String getGlobalheadername() {
        return globalheadername;
    }

    /**
     * 设置全局header名
     *
     * @param globalheadername 全局header名
     */
    public void setGlobalheadername(String globalheadername) {
        this.globalheadername = globalheadername;
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