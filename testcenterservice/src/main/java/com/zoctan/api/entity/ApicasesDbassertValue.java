package com.zoctan.api.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "apicases_dbassert_value")
public class ApicasesDbassertValue {
    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用例id
     */
    private Long dbassertid;

    /**
     * 列名
     */
    private String fieldname;

    public String getAssertcondition() {
        return assertcondition;
    }

    public void setAssertcondition(String assertcondition) {
        this.assertcondition = assertcondition;
    }

    private String assertcondition;
    
    /**
     * 行号
     */
    private Long roworder;

    /**
     * 值类型
     */
    private String valuetype;

    public String getExpectvalue() {
        return expectvalue;
    }

    public void setExpectvalue(String expectvalue) {
        this.expectvalue = expectvalue;
    }

    private String expectvalue;


    /**
     * 创建人
     */
    private String creator;

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
     * @return dbassertid - 用例id
     */
    public Long getDbassertid() {
        return dbassertid;
    }

    /**
     * 设置用例id
     *
     * @param dbassertid 用例id
     */
    public void setDbassertid(Long dbassertid) {
        this.dbassertid = dbassertid;
    }

    /**
     * 获取列名
     *
     * @return fieldname - 列名
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     * 设置列名
     *
     * @param fieldname 列名
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    /**
     * 获取行号
     *
     * @return roworder - 行号
     */
    public Long getRoworder() {
        return roworder;
    }

    /**
     * 设置行号
     *
     * @param roworder 行号
     */
    public void setRoworder(Long roworder) {
        this.roworder = roworder;
    }

    /**
     * 获取值类型
     *
     * @return valuetype - 值类型
     */
    public String getValuetype() {
        return valuetype;
    }

    /**
     * 设置值类型
     *
     * @param valuetype 值类型
     */
    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
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
}