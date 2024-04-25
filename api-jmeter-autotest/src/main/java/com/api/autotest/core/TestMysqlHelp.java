package com.api.autotest.core;

import com.api.autotest.common.utils.HttpParamers;
import com.api.autotest.common.utils.MysqlConnectionUtils;
import com.api.autotest.dto.ApicasesReportstatics;
import com.api.autotest.dto.RequestObject;
import com.api.autotest.dto.TestconditionReport;
import com.api.autotest.dto.TestvariablesValue;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.log.Logger;
import org.apache.poi.hslf.record.HSLFEscherRecordFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.api.autotest.core.TestCaseData.logplannameandcasename;


public class TestMysqlHelp {
    private Logger logger = null;

    public TestMysqlHelp(String MysqlUrl, String MysqlUserName, String MysqlPass, Logger log) {
        logger = log;
        GetDBConnection(MysqlUrl, MysqlUserName, MysqlPass);
    }

    public void GetDBConnection(String mysqluel, String mysqlusername, String mysqlpass) {
        MysqlConnectionUtils.initDbResource(mysqluel, mysqlusername, mysqlpass);
    }

    //获取计划批次的数据统计
    public ArrayList<HashMap<String, String>> GetStatic(String planid, String Batchname) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
//            String sql = "select sum(totalcases) as tc,sum(totalpasscases) as tpc ,sum(totalfailcases) as tfc from apicases_reportstatics where testplanid=" + planid + " and batchname='" + Batchname + "'";
            String sql = "select count(*) as tc from dispatch where execplanid=" + planid + " and batchname='" + Batchname + "'";

            logger.info(logplannameandcasename + "获取数据库 获取统计 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取统计异常...........: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> GetStaticSuccess(String planid, String Batchname,String status) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "select count(*) as tcp from apicases_report where testplanid=" + planid + " and batchname='" + Batchname + "' and status='"+status+"'";
            logger.info(logplannameandcasename + "获取数据库 获取统计 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取统计异常...........: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> GetStaticStop(String planid, String Batchname,String status) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "select count(*) as tsp from dispatch where execplanid=" + planid + " and batchname='" + Batchname + "' and status='"+status+"'";
            logger.info(logplannameandcasename + "获取数据库 获取统计 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取统计异常...........: " + e.getMessage());
        }
        return list;
    }


    //获取账号数据
    public ArrayList<HashMap<String, String>> findWithUsername(String username) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT a.* FROM account a where a.name = '" + username + "'";
            logger.info(logplannameandcasename + "获取数据库 获取账号 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取账号异常...........: " + e.getMessage());
        }
        return list;
    }


    //获取数据库用例相关数据
    public ArrayList<HashMap<String, String>> findDicNameValueWithCode(String DicCode) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT a.dicitemname,a.dicitmevalue FROM dictionary a where a.diccode = '" + DicCode + "'";
            logger.info(logplannameandcasename + "获取数据库 获取字典值caseid result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取字典值异常...........: " + e.getMessage());
        }
        return list;
    }

    //获取计划批次
    public ArrayList<HashMap<String, String>> Getplan(String planid) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT a.* FROM executeplan a where a.id = " + planid;
            logger.info(logplannameandcasename + "获取数据库 获取测试集合 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取测试集合异常...........: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> Getplanmessage(String planid,String messagetype) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT a.* FROM plannmessage a where a.executeplanid = " + planid+ " and messagetype='"+messagetype+"'";
            logger.info(logplannameandcasename + "获取数据库 获取测试集合 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取测试集合异常...........: " + e.getMessage());
        }
        return list;
    }

    //获取计划批次
    public ArrayList<HashMap<String, String>> GetplanBatchCreator(String planid, String BatchName) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT a.* FROM executeplanbatch a where a.executeplanid = " + planid + " and a.batchname='" + BatchName + "'";
            logger.info(logplannameandcasename + "获取数据库 获取计划批次 result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取计划批次异常...........: " + e.getMessage());
        }
        return list;
    }

    //获取数据库用例相关数据
    public ArrayList<HashMap<String, String>> getcaseData(String Sql) {
        logger.info(logplannameandcasename + "获取数据库 Sql is:  " + Sql);
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            list = MysqlConnectionUtils.query(Sql);
//            for (HashMap<String, String> maplog : list) {
//                for (String Key : maplog.keySet()) {
//                    //logger.info("获取数据的字段名为:  " + Key + "  字段值为：" + maplog.get(Key));
//                }
//            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 Sql is:  " + Sql + "  数据库异常：" + e.getMessage());
        }
        logger.info(logplannameandcasename + "获取数据库 list size is:  " + list.size());
        return list;
    }

    // 获取用例期望值
    public String getcaseValue(String key, ArrayList<HashMap<String, String>> list) {
        if (list.size() > 0) {
            HashMap<String, String> hs = list.get(0);
            String value = hs.get(key);
            if (value != null) {
                value = value.trim();
            }
            return value;
        } else {
            return "";
        }
    }


    //获取变量值类型
    public String GetVariablesDataType(String VariablesName, long projectid) {
        String ValueType = "";
        try {
            String sql = "select valuetype from testvariables where  testvariablesname='" + VariablesName + "' and projectid=" + projectid;
            logger.info(logplannameandcasename + "获取数据库 获取变量值类型 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                ValueType = result.get(0).get("valuetype");
            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取变量值类型异常...........: " + e.getMessage());
        }
        return ValueType;
    }

    //获取变量值类型
    public String GetScriptVariablesDataType(String VariablesName, long projectid) {
        String ValueType = "";
        try {
            String sql = "select valuetype from scriptvariables where  scriptvariablesname='" + VariablesName + "' and projectid=" + projectid;
            logger.info(logplannameandcasename + "获取数据库 获取变量值类型 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                ValueType = result.get(0).get("valuetype");
            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取脚本变量值类型异常...........: " + e.getMessage());
        }
        return ValueType;
    }

    //获取数据库变量值类型
    public String GetDBVariablesDataType(String VariablesName, long projectid) {
        String ValueType = "";
        try {
            String sql = "select valuetype from dbvariables where  dbvariablesname='" + VariablesName + "' and projectid=" + projectid;
            logger.info(logplannameandcasename + "获取数据库 获取数据库变量值类型 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                ValueType = result.get(0).get("valuetype");
            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取数据库变量值类型异常...........: " + e.getMessage());
        }
        return ValueType;
    }

    //获取接口变量列表
    public ArrayList<HashMap<String, String>> GetInterfaceVariables() {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select testvariablesname from testvariables";
            logger.info(logplannameandcasename + "获取数据库 GetInterfaceVariables result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 GetInterfaceVariables...........: " + e.getMessage());
        }
        return result;
    }

    //根据变量名获取caseid
    public String GetCaseIdByVariablesName(String VariablesName) {
        String CaseID = "";
        try {
            String sql = "select caseid from apicases_variables where  variablesname='" + VariablesName + "'";
            logger.info(logplannameandcasename + "获取数据库 根据变量名获取caseid result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                CaseID = result.get(0).get("caseid");
            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据变量名获取caseid异常...........: " + e.getMessage());
        }
        return CaseID;
    }

    //获取变量值
    public String GetVariablesValues(String PlanID, String TestCaseId, String BatchName, String VariablesName) {
        String VariablesResult = "";
        try {
            String sql = "select variablesvalue from testvariables_value where planid=" + PlanID + " and caseid=" + TestCaseId + " and batchname= '" + BatchName + "'" + " and variablesname='" + VariablesName + "'";
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次中条件接口获取的中间变量 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            if (result.size() > 0) {
                VariablesResult = result.get(0).get("variablesvalue");
            }
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次中条件接口获取的中间变量异常...........: " + e.getMessage());
        }
        return VariablesResult;
    }


    //根据目标类型和id获取延时条件
    public ArrayList<HashMap<String, String>> GetConditionDelayByObjectIDAndType(Long Objectid, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_delay where conditionid=" + Objectid + " and conditiontype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 根据目标类型和id获取延时条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据目标类型和id获取延时条件异常...........: " + e.getMessage());
        }
        return result;
    }


    //根据目标类型和id获取接口条件
    public ArrayList<HashMap<String, String>> GetConditionApiByObjectIDAndType(Long Objectid, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_api where conditionid=" + Objectid + " and conditiontype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 GetConditionApiByObjectIDAndType result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 GetConditionApiByObjectIDAndType异常...........: " + e.getMessage());
        }
        return result;
    }

    public ArrayList<HashMap<String, String>> GetConditionDBByObjectIDAndType(Long Objectid, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_db where conditionid=" + Objectid + " and conditiontype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 获取场景数据库条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取场景数据库条件异常...........: " + e.getMessage());
        }
        return result;
    }

    public ArrayList<HashMap<String, String>> GetConditionScriptByObjectIDAndType(Long Objectid, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_script where conditionid=" + Objectid + " and conditiontype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 获取场景脚本条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取场景脚本条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //根据目标类型和用例id获取接口条件
    public ArrayList<HashMap<String, String>> GetConditionApiByCaseIDAndType(Long caseid, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_api where caseid=" + caseid + " and conditiontype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 根据目标类型和用例id获取接口条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据目标类型和用例id获取接口条件异常...........: " + e.getMessage());
        }
        return result;
    }


    //根据plan,batchname,case,secne获取功能报告
    public ArrayList<HashMap<String, String>> GetReportByPBST(Long Planid, String Batchname, Long Caseid, Long Sceneid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from apicases_report where testplanid=" + Planid + " and caseid=" + Caseid + " and sceneid=" + Sceneid + " and batchname='" + Batchname + "'";
            logger.info(logplannameandcasename + "获取数据库 获取场景用例id result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取场景用例id异常...........: " + e.getMessage());
        }
        return result;
    }

    //根据reportid获取扩展信息
    public ArrayList<HashMap<String, String>> GetReportEXByReportid(Long ReportID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from apicases_report_extinfo where reportid=" + ReportID;
            logger.info(logplannameandcasename + "获取数据库 根据reportid获取扩展信息 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据reportid获取扩展信息 异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取场景用例id
    public ArrayList<HashMap<String, String>> GetSceneID(Long Caseid, Long Sceneid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from testscene_testcase where testscenenid=" + Sceneid + " and testcaseid=" + Caseid;
            logger.info(logplannameandcasename + "获取数据库 获取场景用例id result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取场景用例id异常...........: " + e.getMessage());
        }
        return result;
    }

    //根据场景id获取场景用例
    public ArrayList<HashMap<String, String>> GetSceneCaseByID(Long Sceneid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from testscene_testcase where testscenenid=" + Sceneid;
            logger.info(logplannameandcasename + "获取数据库 根据场景id获取场景用例 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据场景id获取场景用例异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取条件
    public ArrayList<HashMap<String, String>> GetConditionByPlanIDAndConditionType(Long Caseid, String ConditionType, String ObjectType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from testcondition where objectid=" + Caseid + " and conditiontype='" + ConditionType + "' and objecttype='" + ObjectType + "'";
            logger.info(logplannameandcasename + "获取数据库 获取条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取条件顺序
    public ArrayList<HashMap<String, String>> GetConditionOrderByID(Long ConditionID,String ConditionType) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_order where subconditionid=" + ConditionID+ " and conditiontype='" + ConditionType  + "' order by conditionorder  asc";
            logger.info(logplannameandcasename + "获取数据库 获取条件顺序 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取条件顺序异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取接口条件
    public ArrayList<HashMap<String, String>> GetApiConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_api where conditionid=" + ConditionID;
            logger.info(logplannameandcasename + "获取数据库 获取接口条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取接口条件异常...........: " + e.getMessage());
        }
        return result;
    }


    //获取接口条件
    public ArrayList<HashMap<String, String>> GetApiCaseReportByPBCID(String planid, String batchname, String caseid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from apicases_report where testplanid=" + planid + " and caseid=" + caseid + " and batchname='" + batchname + "'";
            logger.info(logplannameandcasename + "获取数据库 获取接口条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取接口条件异常...........: " + e.getMessage());
        }
        return result;
    }


    //获取报告扩展信息
    public ArrayList<HashMap<String, String>> GetApiCaseReportExtByID(String reportid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from apicases_report_extinfo where reportid=" + reportid;
            logger.info(logplannameandcasename + "获取数据库 获取接口条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取接口条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取接口条件
    public ArrayList<HashMap<String, String>> GetApiConditionByID(Long ID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_api where id=" + ID;
            logger.info(logplannameandcasename + "获取数据库 获取接口条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取接口条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取脚本条件
    public ArrayList<HashMap<String, String>> GetScriptConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_script where conditionid=" + ConditionID;
            logger.info(logplannameandcasename + "获取数据库 获取脚本条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取脚本条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取脚本条件
    public ArrayList<HashMap<String, String>> GetScriptConditionByID(Long ID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_script where conditionid=" + ID;
            logger.info(logplannameandcasename + "获取数据库 获取脚本条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取脚本条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取延时条件
    public ArrayList<HashMap<String, String>> GetDelayConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_delay where conditionid=" + ConditionID;
            logger.info(logplannameandcasename + "获取数据库 获取延时条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取延时条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取延时条件
    public ArrayList<HashMap<String, String>> GetDelayConditionByID(Long ID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_delay where conditionid=" + ID;
            logger.info(logplannameandcasename + "获取数据库 获取延时条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取延时条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取数据库条件
    public ArrayList<HashMap<String, String>> GetDBConditionByConditionID(Long ConditionID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_db where conditionid=" + ConditionID;
            logger.info(logplannameandcasename + "获取数据库 获取数据库条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取数据库条件异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取数据库条件
    public ArrayList<HashMap<String, String>> GetDBConditionByID(Long ID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select * from condition_db where conditionid=" + ID;
            logger.info(logplannameandcasename + "获取数据库 获取数据库条件 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取数据库条件异常...........: " + e.getMessage());
        }
        return result;
    }


    //获取条件报告结果
    public ArrayList<HashMap<String, String>> Gettestconditionreport(Long Planid, String Batchname, Long ConditionID,String subconditiontype) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testcondition_report where testplanid=" + Planid + " and batchname='" + Batchname + "' and subconditionid=" + ConditionID+" and subconditiontype='" + subconditiontype+"'" ;
            logger.info(logplannameandcasename + "获取数据库 获取条件报告结果 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取条件报告结果 异常...........: " + e.getMessage());
        }
        return result;
    }

    //保存条件结果
    public void SubConditionReportSave(TestconditionReport testconditionReport) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "insert testcondition_report (conditiontype,subconditionid,conditionresult,conditionstatus,runtime,create_time,lastmodify_time,creator,batchname,planname,testplanid,subconditiontype,status,subconditionname)" +
                " values('" + testconditionReport.getConditiontype() + "', " + testconditionReport.getSubconditionid() + ", '" + testconditionReport.getConditionresult() + "', '" + testconditionReport.getConditionstatus() + "', " + testconditionReport.getRuntime() + ", '" + dateNowStr + "', '" + dateNowStr + "','admin'" + ", '" + testconditionReport.getBatchname().replace("'", "''") + "',  '" + testconditionReport.getPlanname().replace("'", "''") + "'," + testconditionReport.getTestplanid() + ", '" + testconditionReport.getSubconditiontype() + "', '" + testconditionReport.getStatus() + "', '" + testconditionReport.getSubconditionname().replace("'", "''") + "')";
        logger.info(logplannameandcasename + "获取数据库 接口条件报告结果 result sql is...........: " + sql);
        try {
            logger.info(logplannameandcasename + "获取数据库 接口条件报告结果 result sql is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 接口条件报告结果 result 异常...........: " + exception.getMessage());
        }
    }

    //获取变量结果
    public ArrayList<HashMap<String, String>> GetTestVariablesValue(Long Planid, String Batchname, Long VariablesID,Long conditionid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testvariables_value where planid=" + Planid + " and batchname='" + Batchname + "' and variablesid=" + VariablesID+ " and conditionid=" + conditionid+ " and conditiontype='scencecase'" ;
            logger.info(logplannameandcasename + "获取数据库 获取变量结果 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 获取变量结果 异常...........: " + e.getMessage());
        }
        return result;
    }

    //保存变量结果
    public void testVariablesValueSave(TestvariablesValue testvariablesValue) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "insert testvariables_value (planid,planname,caseid,casename,variablesid,variablesname,variablesvalue,memo,create_time,lastmodify_time,batchname,variablestype,slaverid,conditionid,conditiontype)" +
                " values(" + testvariablesValue.getPlanid() + ", '" + testvariablesValue.getPlanname().replace("'", "''") + "', " + testvariablesValue.getCaseid() + ", '" + testvariablesValue.getCasename().replace("'", "''") + "', " + testvariablesValue.getVariablesid() + ", '" + testvariablesValue.getVariablesname().replace("'", "''") + "', '" + testvariablesValue.getVariablesvalue().replace("'", "''") + "', '" + testvariablesValue.getMemo().replace("'", "''") + "' , '" + dateNowStr + "', '" + dateNowStr + "', '" + testvariablesValue.getBatchname().replace("'", "''") + "', '" + testvariablesValue.getVariablestype().replace("'", "''") + "',"+ testvariablesValue.getSlaverid()+","+ testvariablesValue.getConditionid()+",'"+testvariablesValue.getConditiontype()+"')";
        logger.info(logplannameandcasename + "获取数据库 保存变量结果 result sql is...........: " + sql);
        try {
            logger.info(logplannameandcasename + "获取数据库 保存变量结果 result sql is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 保存变量结果 result 异常...........: " + exception.getMessage());
        }
    }

    //更新变量结果
    public void testVariablesValueUpdate(Long Planid, String Batchname, Long VariablesID, String NewValue) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "update testvariables_value set variablesvalue='" + NewValue + "' where planid=" + Planid + " and batchname='" + Batchname + "' and variablesid=" + VariablesID;
        logger.info(logplannameandcasename + "获取数据库 保存变量结果 result sql is...........: " + sql);
        try {
            logger.info(logplannameandcasename + "获取数据库 保存变量结果 result sql is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 保存变量结果 result 异常...........: " + exception.getMessage());
        }
    }

    //查询场景用例
    public ArrayList<HashMap<String, String>> GetSceneCase(Long SceneID, Long CaseID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testscene_testcase where testscenenid=" + SceneID + " and testcaseid=" + CaseID;
            logger.info(logplannameandcasename + "获取数据库 查询场景用例 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询场景用例异常...........: " + e.getMessage());
        }
        return result;
    }

    //查询用例变量
    public ArrayList<HashMap<String, String>> GetApiCaseVaribales(Long CaseID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testvariables where caseid=" + CaseID;
            logger.info(logplannameandcasename + "获取数据库 查询用例变量 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询用例变量异常...........: " + e.getMessage());
        }
        return result;
    }

    //获取随机变量
    public ArrayList<HashMap<String, String>> GetRadomVariables() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT variablesname,variablestype,variablecondition FROM variables ";
            logger.info(logplannameandcasename + "获取随机变量  result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取随机变量 异常...........: " + e.getMessage());
        }
        return list;
    }

    //获取数据库关联变量
    public ArrayList<HashMap<String, String>> getbyconditionid(long dbconditionid) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM dbvariables where conditionid= " + dbconditionid;
            logger.info(logplannameandcasename + "获取数据库关联变量  result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库关联变量 异常...........: " + e.getMessage());
        }
        return list;
    }

    //获取数据库关联变量
    public ArrayList<HashMap<String, String>> getscriptvariablesbyconditionid(long conditionid) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM scriptvariables where conditionid= " + conditionid;
            logger.info(logplannameandcasename + "获取数据库关联变量  result sql is...........: " + sql);
            list = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库关联变量 异常...........: " + e.getMessage());
        }
        return list;
    }


    //根据planid，batchname，sceneid获取planbatch
    public ArrayList<HashMap<String, String>> GetBatchByPBS(String planid,String batchname,String Sceneid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from executeplanbatch where executeplanid=" + planid+" and sceneid="+Sceneid+" and batchname='"+batchname+"'";
            logger.info(logplannameandcasename + "获取数据库 根据planid，batchname，sceneid获取planbatch result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 根据planid，batchname，sceneid获取planbatch...........: " + e.getMessage());
        }
        return result;
    }

    //查询场景
    public ArrayList<HashMap<String, String>> GetSceneByID(String Sceneid) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testscene where id=" + Sceneid;
            logger.info(logplannameandcasename + "获取数据库 查询场景 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询场景异常...........: " + e.getMessage());
        }
        return result;
    }

    //查询变量
    public ArrayList<HashMap<String, String>> GetVaribales(String VaribaleID) {
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        try {
            String sql = "select *  from testvariables where id=" + VaribaleID;
            logger.info(logplannameandcasename + "获取数据库 查询变量 result sql is...........: " + sql);
            result = MysqlConnectionUtils.query(sql);
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询变量异常...........: " + e.getMessage());
        }
        return result;
    }

    // 获取用例Header，params，Body，Dubbo数据
    public HashMap<String, String> fixhttprequestdatas(String MapType, ArrayList<HashMap<String, String>> casedatalist) {
        HashMap<String, String> DataMap = new HashMap<>();
        for (HashMap<String, String> data : casedatalist) {
            String propertytype = data.get("propertytype");
            if (propertytype.equals(MapType)) {
                DataMap.put(data.get("apiparam").trim(), data.get("apiparamvalue").trim());
            }
        }
        return DataMap;
    }

    public HashMap<String, String> getparamsdatabytype(String MapType, ArrayList<HashMap<String, String>> casedatalist) {
        HashMap<String, String> DataMap = new HashMap<>();
        for (HashMap<String, String> data : casedatalist) {
            String propertytype = data.get("paramstype");
            if (propertytype.equals(MapType)) {
                DataMap.put(data.get("keyname").trim(), data.get("keyvalue").trim());
            }
        }
        return DataMap;
    }

    // 记录用例测试结果
    public int savetestcaseresult(boolean status, long time, String respone, String assertvalue, String errorinfo, RequestObject requestObject, JavaSamplerContext context) {
        int returnkey = 0;
        try {
            String resulttable = "";
            String casetype = "";
            String testplanid = "";
            String sceneid = "";
            String scenename = "";
            String projectid = "";
            String caseid = "";
            String slaverid = "";
            String expect = "";
            String batchname = "";
            String header = "";
            String Params = "";
            Map<String, Object> paramsmap = new HashMap<>();
            String PostData = "";
            String Url = "";
            String Method = "";
            if (requestObject == null) {
                casetype = context.getParameter("casetype");
                projectid = context.getParameter("projectid");
                testplanid = context.getParameter("testplanid");
                caseid = context.getParameter("caseid");
                slaverid = context.getParameter("slaverid");
                expect = context.getParameter("expect");
                Url = context.getParameter("resource");
                Method = context.getParameter("RequestmMthod");
                batchname = context.getParameter("batchname").replace("'", "''");
            } else {
                casetype = requestObject.getCasetype();// context.getParameter("casetype");
                testplanid = requestObject.getTestplanid();// context.getParameter("testplanid");
                projectid = requestObject.getProjectid();
                caseid = requestObject.getCaseid();// context.getParameter("caseid");
                slaverid = requestObject.getSlaverid();// context.getParameter("slaverid");
                expect = requestObject.getExpect();// context.getParameter("expect");
                batchname = requestObject.getBatchname().replace("'", "''");// context.getParameter("batchname");
                Url = requestObject.getResource().replace("'", "''");
                Method = requestObject.getRequestmMthod().toUpperCase();
                sceneid = requestObject.getSceneid().toString();
                scenename = requestObject.getScenename();
            }
            Map<String, Object> headermap = requestObject.getHeader().getParams();
            for (String key : headermap.keySet()) {
                header = header + key + " ：" + headermap.get(key);
            }
            header = header.replace("'", "''");
            paramsmap = requestObject.getParamers().getParams();
            for (String key : paramsmap.keySet()) {
                Params = Params + key + " ：" + paramsmap.get(key) + " ";
            }
            if (!Params.isEmpty()) {
                PostData = "参数：" + Params;
            } else {
                PostData = requestObject.getPostData();
            }
            PostData = PostData.replace("'", "''");

            if (casetype.equals("功能")) {
                resulttable = "apicases_report";
            }
            if (casetype.equals("性能")) {
                resulttable = "apicases_report_performance";
            }
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(d);
            String sql = "";
            if (status) {
                sql = "insert " + resulttable + " (caseid,testplanid,batchname,slaverid,status,respone,assertvalue,runtime,expect,errorinfo,create_time,lastmodify_time,creator,requestheader,requestdatas,url,requestmethod,projectid, sceneid, scenename)" +
                        " values(" + caseid + "," + testplanid + ", '" + batchname + "', " + slaverid + ", '成功" + "' , '" + respone.replace("'", "''") + "' ,'" + assertvalue.replace("'", "''") + "', " + time + ",'" + expect.replace("'", "''") + "','" + errorinfo + "','" + dateNowStr + "', '" + dateNowStr + "','admin', '" + header + "', '" + PostData + "', '" + Url + "', '" + Method + "'," + projectid + "," + sceneid + ",'" + scenename + "')";
            } else {
                sql = "insert  " + resulttable + " (caseid,testplanid,batchname,slaverid,status,respone,assertvalue,runtime,expect,errorinfo,create_time,lastmodify_time,creator,requestheader,requestdatas,url,requestmethod,projectid, sceneid, scenename)" +
                        " values(" + caseid + "," + testplanid + ", '" + batchname + "', " + slaverid + ", '失败" + "' , '" + respone.replace("'", "''") + "','" + assertvalue.replace("'", "''") + "'," + time + ",'" + expect.replace("'", "''") + "','" + errorinfo + "','" + dateNowStr + "','" + dateNowStr + "','admin', '" + header + "', '" + PostData + "', '" + Url + "', '" + Method + "'," + projectid + "," + sceneid + ",'" + scenename + "')";
            }
            logger.info(logplannameandcasename + "获取数据库 测试结果 result sql is...........: " + sql);
            returnkey = MysqlConnectionUtils.updatewithkey(sql);
            logger.info(logplannameandcasename + "获取数据库 记录用例测试结果 result sql is...........: " + returnkey);
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "获取数据库 记录用例测试结果异常...........: " + ex.getMessage());
        }
        return returnkey;
    }

    // 记录用例测试结果
    public void SaveReportStatics(ApicasesReportstatics apicasesReportstatics) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "insert apicases_reportstatics (testplanid,deployunitid,batchname,slaverid,totalcases,totalpasscases,totalfailcases,runtime,create_time,lastmodify_time,creator)" +
                " values(" + apicasesReportstatics.getTestplanid() + "," + apicasesReportstatics.getDeployunitid() + ", '" + apicasesReportstatics.getBatchname().replace("'", "''") + "', " + apicasesReportstatics.getSlaverid() + ", " + apicasesReportstatics.getTotalcases() + ", " + apicasesReportstatics.getTotalpasscases() + ", " + apicasesReportstatics.getTotalfailcases() + ", " + apicasesReportstatics.getRuntime() + ", '" + dateNowStr + "', '" + dateNowStr + "','admin')";
        logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 result sql is...........: " + sql);
        try {
            logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 result sql is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 result 异常...........: " + exception.getMessage());
        }
    }

    // 记录用例测试结果扩展信息
    public void savetestcaseextresult(String reportid, String reportextinfo, String projectid) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "insert apicases_report_extinfo (reportid,responeinfo,create_time,lastmodify_time,projectid)" +
                " values(" + reportid + ", '" + reportextinfo.replace("\\", "\\\\") + "', " + "'" + dateNowStr + "', '" + dateNowStr + "', " + projectid + ")";
        logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 XXXXXXXXXXXXXXXXXXXXXXXresult sql is...........: " + sql);
        try {
            logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 result sql is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 功能测试统计结果 result 异常...........: " + exception.getMessage());
        }
    }


    //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
    public void PlanBatchAllDipatchFinish(ApicasesReportstatics apicasesReportstatics) {
        long DispatchNotFinishNums = 0;
        try {
            String sql = "select count(*) as nums from dispatch where execplanid=" + apicasesReportstatics.getTestplanid() + " and batchname= '" + apicasesReportstatics.getBatchname() + "' and status in('待分配','已分配')";
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否已经全部完成 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            DispatchNotFinishNums = Long.parseLong(getcaseValue("nums", result));
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否已经全部完成异常...........: " + e.getMessage());
        }
        if (DispatchNotFinishNums > 0) {
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度未完成数量：" + DispatchNotFinishNums);
        } else {
            UpdateReportStatics(apicasesReportstatics.getTestplanid(), apicasesReportstatics.getBatchname(), "已完成");
        }
    }

    //查询此计划下的批次调度是否已经全部完成，如果完成，刷新计划批次状态为finish
    public long PlanBatchAllDipatchFinish(String Testplanid, String batchname) {
        long DispatchNotFinishNums = 0;
        try {
            String sql = "select count(*) as nums from dispatch where execplanid=" + Testplanid + " and batchname= '" + batchname + "' and status in('待分配','已分配')";
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否已经全部完成 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            DispatchNotFinishNums = Long.parseLong(getcaseValue("nums", result));
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否已经全部完成异常...........: " + e.getMessage());
        }
        return DispatchNotFinishNums;
    }

    //查询此计划下的批次调度是否有已取消
    public long PlanBatchAllDipatchCancel(String Testplanid, String batchname) {
        long DispatchCancelNums = 0;
        try {
            String sql = "select count(*) as nums from dispatch where execplanid=" + Testplanid + " and batchname= '" + batchname + "' and status ='" + "已取消'";
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否有已取消 result sql is...........: " + sql);
            ArrayList<HashMap<String, String>> result = MysqlConnectionUtils.query(sql);
            DispatchCancelNums = Long.parseLong(getcaseValue("nums", result));
        } catch (Exception e) {
            logger.info(logplannameandcasename + "获取数据库 查询计划下的批次调度是否有已取消异常...........: " + e.getMessage());
        }
        return DispatchCancelNums;
    }

    // 更新计划批次状态
    public void UpdateReportStatics(String Planid, String BatchName, String status) {
        String UpdateSql = "update  executeplanbatch set status='" + status + "' where executeplanid=" + Planid + " and batchname= '" + BatchName + "'";
        logger.info(logplannameandcasename + "获取数据库 更新计划批次状态结果完成  sql is...........: " + UpdateSql);
        try {
            logger.info(logplannameandcasename + "获取数据库 更新计划批次状态结果完成 result sql is...........: " + MysqlConnectionUtils.update(UpdateSql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 更新计划批次状态结果完成 result 异常...........: " + exception.getMessage());
        }
    }

    // 更新计划批次场景状态
    public void UpdateBatchScene(String planid, String batchname, String sceneid, String status) {
        String UpdateSql = "update  executeplanbatch set status='" + status + "' where executeplanid=" + planid + " and sceneid=" + sceneid + " and batchname= '" + batchname + "'";
        logger.info(logplannameandcasename + "获取数据库 更新计划批次场景状态结果完成  sql is...........: " + UpdateSql);
        try {
            logger.info(logplannameandcasename + "获取数据库 更新计划批次场景状态结果完成 result sql is...........: " + MysqlConnectionUtils.update(UpdateSql));
        } catch (Exception exception) {
            logger.info(logplannameandcasename + "获取数据库 更新计划批次场景状态结果完成 result 异常...........: " + exception.getMessage());
        }
    }


    // 更新Slaver状态
    public void UpdateSlaverStatus(String Slaverid, String status) throws Exception {
        String UpdateSql = "update  slaver set status='" + status + "' where id=" + Slaverid;
        logger.info(logplannameandcasename + "获取数据库 更新Slaver状态结果完成  sql is...........: " + UpdateSql);
        logger.info(logplannameandcasename + "获取数据库 更新Slaver状态结果完成 result sql is...........: " + MysqlConnectionUtils.update(UpdateSql));
    }

    // 更新用例调度结果
    public void updatedispatchcasestatus(String testplanid,String Caseid, String slaverid, String sceneid, String batchname,String status) {
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(d);
            String sql = "";
            sql = "update dispatch set status='"+status+"',lastmodify_time='" + dateNowStr + "' where slaverid=" + slaverid + " and execplanid=" + testplanid + " and sceneid=" + sceneid + " and batchname='" + batchname+ "' and testcaseid=" + Caseid ;
            logger.info(logplannameandcasename + "获取数据库 更新调度用例状态 result sql is...........: " + sql);
            logger.info(logplannameandcasename + "获取数据库 更新用例调度结果 is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "获取数据库 更新用例调度结果异常...........: " + ex.getMessage());
        }
    }

    public void updatebatchdispatchcasestatus(String testplanid, String slaverid, String sceneid, String batchname,String status) {
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(d);
            String sql = "";
            sql = "update dispatch set status='"+status+"',lastmodify_time='" + dateNowStr + "' where slaverid=" + slaverid + " and execplanid=" + testplanid + " and sceneid=" + sceneid + " and batchname='" + batchname+"'";
            logger.info(logplannameandcasename + "获取数据库 更新调度用例状态 result sql is...........: " + sql);
            logger.info(logplannameandcasename + "获取数据库 更新用例调度结果 is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "获取数据库 更新用例调度结果异常...........: " + ex.getMessage());
        }
    }

    // 新增性能日志用例记录结果
    public void generalperformancelogfile(String testplanid, String caseid, String slaverid, String batchid, String filename, String status) {
        try {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNowStr = sdf.format(d);
            String sql = "";
            sql = "insert performancereportfilelog (execplanid,batchid,caseid,slaverid,filename,status,create_time,lastmodify_time)" +
                    " values(" + testplanid + "," + batchid + " , " + caseid + ", " + slaverid + " , '" + filename + "' , '" + status + "' , '" + dateNowStr + "', '" + dateNowStr + "' )";
            logger.info(logplannameandcasename + "获取数据库 新增性能日志用例记录结果 result sql is...........: " + sql);
            logger.info(logplannameandcasename + "获取数据库 新增性能日志用例记录结果 is...........: " + MysqlConnectionUtils.update(sql));
        } catch (Exception ex) {
            logger.info(logplannameandcasename + "获取数据库 新增性能日志用例记录结果...........: " + ex.getMessage());
        }
    }

    //生成性能报告目录
    public void genealperformacestaticsreport(String testclass, String batchname, String testplanid, String batchid, String slaverid, String caseid, String casereportfolder, double costtime, String Creator) throws Exception {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        String sql = "";
        sql = "insert performancereportsource (planid,batchid,batchname,slaverid,caseid,testclass,runtime,source,status,create_time,lastmodify_time,creator,totalcasenums,totalcasepassnums,totalcasefailnums)" +
                " values(" + testplanid + "," + batchid + ", '" + batchname.replace("'", "''") + "', " + slaverid + ", " + caseid + " , '" + testclass + "' ," + costtime + " , '" + casereportfolder + "', '待解析', '" + dateNowStr + "', '" + dateNowStr + "', '" + Creator + "' , 0,0,0)";
        logger.info(logplannameandcasename + "获取数据库 保存性能统计结果 sql is...........: " + sql);
        logger.info(logplannameandcasename + "获取数据库 保存性能统计结果 is...........: " + MysqlConnectionUtils.update(sql));
    }
}
