package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Testscene {
    /**
     * 场景Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getCasenums() {
        return casenums;
    }

    public void setCasenums(Long casenums) {
        this.casenums = casenums;
    }

    private Long casenums;

    /**
     * 场景名
     */
    private String scenename;

    /**
     * 运行类型，function，performance，来区分分配什么slaver
     */
    private String usetype;

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
     * 项目id
     */
    private Long projectid;

    /**
     * 获取场景Id
     *
     * @return id - 场景Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置场景Id
     *
     * @param id 场景Id
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取运行类型，function，performance，来区分分配什么slaver
     *
     * @return usetype - 运行类型，function，performance，来区分分配什么slaver
     */
    public String getUsetype() {
        return usetype;
    }

    /**
     * 设置运行类型，function，performance，来区分分配什么slaver
     *
     * @param usetype 运行类型，function，performance，来区分分配什么slaver
     */
    public void setUsetype(String usetype) {
        this.usetype = usetype;
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