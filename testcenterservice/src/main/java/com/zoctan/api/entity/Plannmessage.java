package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

public class Plannmessage {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 集合id
     */
    private Long executeplanid;

    /**
     * 通知类型
     */
    private String messagetype;

    /**
     * 维护者id
     */
    private Long mid;

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
     * hookurl
     */
    private String hookcontent;

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
     * 获取集合id
     *
     * @return executeplanid - 集合id
     */
    public Long getExecuteplanid() {
        return executeplanid;
    }

    /**
     * 设置集合id
     *
     * @param executeplanid 集合id
     */
    public void setExecuteplanid(Long executeplanid) {
        this.executeplanid = executeplanid;
    }

    /**
     * 获取通知类型
     *
     * @return messagetype - 通知类型
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * 设置通知类型
     *
     * @param messagetype 通知类型
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    /**
     * 获取维护者id
     *
     * @return mid - 维护者id
     */
    public Long getMid() {
        return mid;
    }

    /**
     * 设置维护者id
     *
     * @param mid 维护者id
     */
    public void setMid(Long mid) {
        this.mid = mid;
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
     * 获取hookurl
     *
     * @return hookcontent - hookurl
     */
    public String getHookcontent() {
        return hookcontent;
    }

    /**
     * 设置hookurl
     *
     * @param hookcontent hookurl
     */
    public void setHookcontent(String hookcontent) {
        this.hookcontent = hookcontent;
    }
}