package com.zoctan.api.dto;

import com.zoctan.api.entity.Executeplanbatch;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class Testplanandbatch extends Executeplanbatch {

  public String getPlanname() {
    return planname;
  }

  public void setPlanname(String planname) {
    this.planname = planname;
  }

  private String planname;


  public Long getPlanid() {
    return planid;
  }

  public void setPlanid(Long planid) {
    this.planid = planid;
  }

  private Long planid;

  public String getBatchname() {
    return batchname;
  }

  public void setBatchname(String batchname) {
    this.batchname = batchname;
  }

  private String batchname;

  public Long getBatchid() {
    return batchid;
  }

  public void setBatchid(Long batchid) {
    this.batchid = batchid;
  }

  private Long batchid;


}
