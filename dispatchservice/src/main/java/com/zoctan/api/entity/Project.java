package com.zoctan.api.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Project {
    /**
     * 项目Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 项目名
     */
    private String projectname;

    /**
     * 项目状态
     */
    private String status;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 项目描述
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


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private String creator;
    /**
     * 获取项目Id
     *
     * @return id - 项目Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置项目Id
     *
     * @param id 项目Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取项目名
     *
     * @return projectname - 项目名
     */
    public String getProjectname() {
        return projectname;
    }

    /**
     * 设置项目名
     *
     * @param projectname 项目名
     */
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    /**
     * 获取项目状态
     *
     * @return status - 项目状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置项目状态
     *
     * @param status 项目状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取项目描述
     *
     * @return memo - 项目描述
     */
    public String getMemo() {
        return memo;
    }

    /**
     * 设置项目描述
     *
     * @param memo 项目描述
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