package com.api.autotest.dto;

import java.util.Date;

public class ApicasesDBAssertValue {
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    private Long id;

    public Long getDbassertid() {
        return dbassertid;
    }

    public void setDbassertid(Long dbassertid) {
        this.dbassertid = dbassertid;
    }



    public Long getRoworder() {
        return roworder;
    }

    public void setRoworder(Long roworder) {
        this.roworder = roworder;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getExpectvalue() {
        return expectvalue;
    }

    public void setExpectvalue(String expectvalue) {
        this.expectvalue = expectvalue;
    }

    public String getAssertcondition() {
        return assertcondition;
    }

    public void setAssertcondition(String assertcondition) {
        this.assertcondition = assertcondition;
    }

    /**
     * 用例id
     */
    private Long dbassertid;

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    private String fieldname;
    private Long roworder;
    private String valuetype;
    private String expectvalue;
    private String assertcondition;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上一次修改时间
     */
    private Date lastmodifyTime;

    /**
     * 创建者
     */
    private String creator;



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
