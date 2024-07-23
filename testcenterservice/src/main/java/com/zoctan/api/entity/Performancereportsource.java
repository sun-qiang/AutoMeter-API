package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Performancereportsource {
    /**
     * 环境Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 执行计划Id
     */
    private Long planid;

    /**
     * 批次Id
     */
    private Long batchid;

    public Long getSceneid() {
        return sceneid;
    }

    public void setSceneid(Long sceneid) {
        this.sceneid = sceneid;
    }

    private Long sceneid;




    /**
     * 批次
     */
    private String batchname;

    /**
     * 执行机Id
     */
    private Long slaverid;

    /**
     * 用例Id
     */
    private Long caseid;

    /**
     * 解析文件匹配sample
     */
    private String testclass;

    /**
     * 运行时长
     */
    private Double runtime;

    /**
     * 解析文件目录
     */
    private String source;

    /**
     * 待解析，已解析
     */
    private String status;

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
     * 创建人
     */
    private String creator;

    /**
     * 用例数
     */
    private Long totalcasenums;

    /**
     * 用例成功数
     */
    private Long totalcasepassnums;

    /**
     * 用例失败数
     */
    private Long totalcasefailnums;

    /**
     * 获取环境Id
     *
     * @return id - 环境Id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置环境Id
     *
     * @param id 环境Id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取执行计划Id
     *
     * @return planid - 执行计划Id
     */
    public Long getPlanid() {
        return planid;
    }

    /**
     * 设置执行计划Id
     *
     * @param planid 执行计划Id
     */
    public void setPlanid(Long planid) {
        this.planid = planid;
    }

    /**
     * 获取批次Id
     *
     * @return batchid - 批次Id
     */
    public Long getBatchid() {
        return batchid;
    }

    /**
     * 设置批次Id
     *
     * @param batchid 批次Id
     */
    public void setBatchid(Long batchid) {
        this.batchid = batchid;
    }

    /**
     * 获取批次
     *
     * @return batchname - 批次
     */
    public String getBatchname() {
        return batchname;
    }

    /**
     * 设置批次
     *
     * @param batchname 批次
     */
    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    /**
     * 获取执行机Id
     *
     * @return slaverid - 执行机Id
     */
    public Long getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机Id
     *
     * @param slaverid 执行机Id
     */
    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
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
     * 获取解析文件匹配sample
     *
     * @return testclass - 解析文件匹配sample
     */
    public String getTestclass() {
        return testclass;
    }

    /**
     * 设置解析文件匹配sample
     *
     * @param testclass 解析文件匹配sample
     */
    public void setTestclass(String testclass) {
        this.testclass = testclass;
    }

    /**
     * 获取运行时长
     *
     * @return runtime - 运行时长
     */
    public Double getRuntime() {
        return runtime;
    }

    /**
     * 设置运行时长
     *
     * @param runtime 运行时长
     */
    public void setRuntime(Double runtime) {
        this.runtime = runtime;
    }

    /**
     * 获取解析文件目录
     *
     * @return source - 解析文件目录
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置解析文件目录
     *
     * @param source 解析文件目录
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取待解析，已解析
     *
     * @return status - 待解析，已解析
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置待解析，已解析
     *
     * @param status 待解析，已解析
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取用例数
     *
     * @return totalcasenums - 用例数
     */
    public Long getTotalcasenums() {
        return totalcasenums;
    }

    /**
     * 设置用例数
     *
     * @param totalcasenums 用例数
     */
    public void setTotalcasenums(Long totalcasenums) {
        this.totalcasenums = totalcasenums;
    }

    /**
     * 获取用例成功数
     *
     * @return totalcasepassnums - 用例成功数
     */
    public Long getTotalcasepassnums() {
        return totalcasepassnums;
    }

    /**
     * 设置用例成功数
     *
     * @param totalcasepassnums 用例成功数
     */
    public void setTotalcasepassnums(Long totalcasepassnums) {
        this.totalcasepassnums = totalcasepassnums;
    }

    /**
     * 获取用例失败数
     *
     * @return totalcasefailnums - 用例失败数
     */
    public Long getTotalcasefailnums() {
        return totalcasefailnums;
    }

    /**
     * 设置用例失败数
     *
     * @param totalcasefailnums 用例失败数
     */
    public void setTotalcasefailnums(Long totalcasefailnums) {
        this.totalcasefailnums = totalcasefailnums;
    }
}