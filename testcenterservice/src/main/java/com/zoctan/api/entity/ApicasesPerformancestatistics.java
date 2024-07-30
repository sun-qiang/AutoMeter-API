package com.zoctan.api.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "apicases_performancestatistics")
public class ApicasesPerformancestatistics {
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
    /**
     * 用例id
     */
    private Long caseid;

    public Long getSceneid() {
        return sceneid;
    }

    public void setSceneid(Long sceneid) {
        this.sceneid = sceneid;
    }

    private Long sceneid;

    /**
     * 执行计划id
     */
    private Long testplanid;

    /**
     * 批次
     */
    private String batchname;

    public String getScenename() {
        return scenename;
    }

    public void setScenename(String scenename) {
        this.scenename = scenename;
    }

    private String scenename;



    public String getExecuteplanname() {
        return executeplanname;
    }

    public void setExecuteplanname(String executeplanname) {
        this.executeplanname = executeplanname;
    }

    public String getCasename() {
        return casename;
    }

    public void setCasename(String casename) {
        this.casename = casename;
    }

    private String executeplanname;
    private String casename;



    /**
     * 运行时长,秒
     */
    private Double runtime;

    /**
     * 执行机id
     */
    private Long slaverid;

    /**
     * 样本
     */
    private Long samples;

    /**
     * 错误次数
     */
    private Long errorcount;

    /**
     * 错误率
     */
    private Double errorrate;

    /**
     * 平均数
     */
    private Double average;

    /**
     * 最小值
     */
    private Double min;

    /**
     * 最大值
     */
    private Double max;

    /**
     * 中间值
     */
    private Double median;

    /**
     * 90pct
     */
    private Double nzpct;

    /**
     * 95pct
     */
    private Double nfpct;

    /**
     * 99pct
     */
    private Double nnpct;

    /**
     * tps
     */
    private Double tps;

    /**
     * 每秒接受Kb数
     */
    private Double receivekbsec;

    /**
     * 每秒发送Kb数
     */
    private Double sendkbsec;

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
     * 获取用例id
     *
     * @return caseid - 用例id
     */
    public Long getCaseid() {
        return caseid;
    }

    /**
     * 设置用例id
     *
     * @param caseid 用例id
     */
    public void setCaseid(Long caseid) {
        this.caseid = caseid;
    }

    /**
     * 获取执行计划id
     *
     * @return testplanid - 执行计划id
     */
    public Long getTestplanid() {
        return testplanid;
    }

    /**
     * 设置执行计划id
     *
     * @param testplanid 执行计划id
     */
    public void setTestplanid(Long testplanid) {
        this.testplanid = testplanid;
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
     * 获取运行时长,秒
     *
     * @return runtime - 运行时长,秒
     */
    public Double getRuntime() {
        return runtime;
    }

    /**
     * 设置运行时长,秒
     *
     * @param runtime 运行时长,秒
     */
    public void setRuntime(Double runtime) {
        this.runtime = runtime;
    }

    /**
     * 获取执行机id
     *
     * @return slaverid - 执行机id
     */
    public Long getSlaverid() {
        return slaverid;
    }

    /**
     * 设置执行机id
     *
     * @param slaverid 执行机id
     */
    public void setSlaverid(Long slaverid) {
        this.slaverid = slaverid;
    }

    /**
     * 获取样本
     *
     * @return samples - 样本
     */
    public Long getSamples() {
        return samples;
    }

    /**
     * 设置样本
     *
     * @param samples 样本
     */
    public void setSamples(Long samples) {
        this.samples = samples;
    }

    /**
     * 获取错误次数
     *
     * @return errorcount - 错误次数
     */
    public Long getErrorcount() {
        return errorcount;
    }

    /**
     * 设置错误次数
     *
     * @param errorcount 错误次数
     */
    public void setErrorcount(Long errorcount) {
        this.errorcount = errorcount;
    }

    /**
     * 获取错误率
     *
     * @return errorrate - 错误率
     */
    public Double getErrorrate() {
        return errorrate;
    }

    /**
     * 设置错误率
     *
     * @param errorrate 错误率
     */
    public void setErrorrate(Double errorrate) {
        this.errorrate = errorrate;
    }

    /**
     * 获取平均数
     *
     * @return average - 平均数
     */
    public Double getAverage() {
        return average;
    }

    /**
     * 设置平均数
     *
     * @param average 平均数
     */
    public void setAverage(Double average) {
        this.average = average;
    }

    /**
     * 获取最小值
     *
     * @return min - 最小值
     */
    public Double getMin() {
        return min;
    }

    /**
     * 设置最小值
     *
     * @param min 最小值
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * 获取最大值
     *
     * @return max - 最大值
     */
    public Double getMax() {
        return max;
    }

    /**
     * 设置最大值
     *
     * @param max 最大值
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     * 获取中间值
     *
     * @return median - 中间值
     */
    public Double getMedian() {
        return median;
    }

    /**
     * 设置中间值
     *
     * @param median 中间值
     */
    public void setMedian(Double median) {
        this.median = median;
    }

    /**
     * 获取90pct
     *
     * @return nzpct - 90pct
     */
    public Double getNzpct() {
        return nzpct;
    }

    /**
     * 设置90pct
     *
     * @param nzpct 90pct
     */
    public void setNzpct(Double nzpct) {
        this.nzpct = nzpct;
    }

    /**
     * 获取95pct
     *
     * @return nfpct - 95pct
     */
    public Double getNfpct() {
        return nfpct;
    }

    /**
     * 设置95pct
     *
     * @param nfpct 95pct
     */
    public void setNfpct(Double nfpct) {
        this.nfpct = nfpct;
    }

    /**
     * 获取99pct
     *
     * @return nnpct - 99pct
     */
    public Double getNnpct() {
        return nnpct;
    }

    /**
     * 设置99pct
     *
     * @param nnpct 99pct
     */
    public void setNnpct(Double nnpct) {
        this.nnpct = nnpct;
    }

    /**
     * 获取tps
     *
     * @return tps - tps
     */
    public Double getTps() {
        return tps;
    }

    /**
     * 设置tps
     *
     * @param tps tps
     */
    public void setTps(Double tps) {
        this.tps = tps;
    }

    /**
     * 获取每秒接受Kb数
     *
     * @return receivekbsec - 每秒接受Kb数
     */
    public Double getReceivekbsec() {
        return receivekbsec;
    }

    /**
     * 设置每秒接受Kb数
     *
     * @param receivekbsec 每秒接受Kb数
     */
    public void setReceivekbsec(Double receivekbsec) {
        this.receivekbsec = receivekbsec;
    }

    /**
     * 获取每秒发送Kb数
     *
     * @return sendkbsec - 每秒发送Kb数
     */
    public Double getSendkbsec() {
        return sendkbsec;
    }

    /**
     * 设置每秒发送Kb数
     *
     * @param sendkbsec 每秒发送Kb数
     */
    public void setSendkbsec(Double sendkbsec) {
        this.sendkbsec = sendkbsec;
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