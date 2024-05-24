package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "globalheader_params")
public class GlobalheaderParams {
    /**
     * 主键Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * globalheaderId
     */
    private Long globalheaderid;

    public String getEncyptype() {
        return encyptype;
    }

    public void setEncyptype(String encyptype) {
        this.encyptype = encyptype;
    }

    private String encyptype;
    /**
     * key名
     */
    private String keyname;

    /**
     * 值
     */
    private String keyvalue;

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
     * 获取globalheaderId
     *
     * @return globalheaderid - globalheaderId
     */
    public Long getGlobalheaderid() {
        return globalheaderid;
    }

    /**
     * 设置globalheaderId
     *
     * @param globalheaderid globalheaderId
     */
    public void setGlobalheaderid(Long globalheaderid) {
        this.globalheaderid = globalheaderid;
    }

    /**
     * 获取key名
     *
     * @return keyname - key名
     */
    public String getKeyname() {
        return keyname;
    }

    /**
     * 设置key名
     *
     * @param keyname key名
     */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    /**
     * 获取值
     *
     * @return keyvalue - 值
     */
    public String getKeyvalue() {
        return keyvalue;
    }

    /**
     * 设置值
     *
     * @param keyvalue 值
     */
    public void setKeyvalue(String keyvalue) {
        this.keyvalue = keyvalue;
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