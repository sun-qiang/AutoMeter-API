package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "testscene_performance")
public class TestscenePerformance {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 场景id
     */
    private Long testsceneid;

    /**
     * 场景名
     */
    private String scenename;

    /**
     * 并发线程
     */
    private Long targetconcurrency;

    /**
     * 启动时间
     */
    private Long rampuptime;

    /**
     * 阶梯次数
     */
    private Long stepscount;

    /**
     * 持续时间
     */
    private Long holdtime;

    /**
     * 时间单位
     */
    private String timeunit;

    /**
     * 循环次数
     */
    private Long iterations;

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
     * 获取场景id
     *
     * @return testsceneid - 场景id
     */
    public Long getTestsceneid() {
        return testsceneid;
    }

    /**
     * 设置场景id
     *
     * @param testsceneid 场景id
     */
    public void setTestsceneid(Long testsceneid) {
        this.testsceneid = testsceneid;
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
     * 获取并发线程
     *
     * @return targetconcurrency - 并发线程
     */
    public Long getTargetconcurrency() {
        return targetconcurrency;
    }

    /**
     * 设置并发线程
     *
     * @param targetconcurrency 并发线程
     */
    public void setTargetconcurrency(Long targetconcurrency) {
        this.targetconcurrency = targetconcurrency;
    }

    /**
     * 获取启动时间
     *
     * @return rampuptime - 启动时间
     */
    public Long getRampuptime() {
        return rampuptime;
    }

    /**
     * 设置启动时间
     *
     * @param rampuptime 启动时间
     */
    public void setRampuptime(Long rampuptime) {
        this.rampuptime = rampuptime;
    }

    /**
     * 获取阶梯次数
     *
     * @return stepscount - 阶梯次数
     */
    public Long getStepscount() {
        return stepscount;
    }

    /**
     * 设置阶梯次数
     *
     * @param stepscount 阶梯次数
     */
    public void setStepscount(Long stepscount) {
        this.stepscount = stepscount;
    }

    /**
     * 获取持续时间
     *
     * @return holdtime - 持续时间
     */
    public Long getHoldtime() {
        return holdtime;
    }

    /**
     * 设置持续时间
     *
     * @param holdtime 持续时间
     */
    public void setHoldtime(Long holdtime) {
        this.holdtime = holdtime;
    }

    /**
     * 获取时间单位
     *
     * @return timeunit - 时间单位
     */
    public String getTimeunit() {
        return timeunit;
    }

    /**
     * 设置时间单位
     *
     * @param timeunit 时间单位
     */
    public void setTimeunit(String timeunit) {
        this.timeunit = timeunit;
    }

    /**
     * 获取循环次数
     *
     * @return iterations - 循环次数
     */
    public Long getIterations() {
        return iterations;
    }

    /**
     * 设置循环次数
     *
     * @param iterations 循环次数
     */
    public void setIterations(Long iterations) {
        this.iterations = iterations;
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