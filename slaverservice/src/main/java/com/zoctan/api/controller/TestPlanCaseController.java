package com.zoctan.api.controller;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.entity.Dictionary;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.*;
import com.zoctan.api.service.TestPlanCaseService;
import com.zoctan.api.service.impl.TestPlanCaseServiceImpl;
import com.zoctan.api.util.IPHelpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import com.alibaba.fastjson.JSON;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Zoctan
 * @date 2020/04/17
 */
@Slf4j
@RestController
@RequestMapping("/exectestplancase")
public class TestPlanCaseController {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Autowired
    private ExecuteplanbatchMapper executeplanbatchMapper;
    @Autowired
    private ExecuteplanTestcaseMapper executeplanTestcaseMapper;
    @Autowired
    private ExecuteplanMapper executeplanMapper;
    @Autowired
    private ExecuteplanService executeplanService;
    @Autowired
    private ExecuteplanbatchService executeplanbatchService;
    @Autowired
    private SlaverMapper slaverMapper;
    @Autowired(required = false)
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private DispatchMapper dispatchMapper;
    @Autowired(required = false)
    private DeployunitService deployunitService;
    @Autowired(required = false)
    private ApicasesMapper apicasesMapper;
    @Autowired(required = false)
    private TestPlanCaseService tpcservice;
    @Autowired(required = false)
    private ApicasesReportService apicasereportservice;
    @Autowired(required = false)
    private ApicasesService apicasesService;
    @Autowired(required = false)
    private ApicasesVariablesService apicasesVariablesService;
    @Autowired(required = false)
    private TestvariablesValueService testvariablesValueService;
    @Autowired(required = false)
    private ApiCasedataService apiCasedataService;
    @Autowired(required = false)
    private TestvariablesService testvariablesService;
    @Autowired(required = false)
    private ApiParamsService apiParamsService;
    @Autowired(required = false)
    private ApiService apiService;
    @Autowired(required = false)
    private ExecuteplanService epservice;
    @Autowired(required = false)
    private MacdepunitService macdepunitService;
    @Autowired(required = false)
    private MachineService machineService;
    @Autowired(required = false)
    private ApicasesAssertService apicasesAssertService;
    @Autowired(required = false)
    private ExecuteplanParamsService executeplanParamsService;
    @Autowired(required = false)
    private ApicasesReportPerformanceMapper apicasesReportPerformanceMapper;

    @Autowired(required = false)
    private VariablesService variablesService;

    @Autowired(required = false)
    private DbvariablesService dbvariablesService;

    @Autowired(required = false)
    private GlobalheaderuseService globalheaderuseService;

    @Autowired(required = false)
    private GlobalheaderParamsService globalheaderParamsService;

    @Autowired(required = false)
    private GlobalvariablesService globalvariablesService;

    @Autowired(required = false)
    private TestsceneDispatchService testsceneDispatchService;

    @Autowired(required = false)
    private TestsceneTestcaseService testsceneTestcaseService;

    @PostMapping("/exec")
    //    public Result exec(@RequestBody List<TestplanCase> plancaseList) {
    public Result exec(@RequestBody Testplanandbatch planbatch) throws Exception {
        // 调用testcenter需要模拟下admin登录，调用Request URL: http://localhost:8080/account/token  {name: "admin", password: "admin123"}
        // 在请求头里面加上Authorization = token
        Long execplanid = planbatch.getPlanid();
        String batchname = planbatch.getBatchname();
        Executeplanbatch epb = executeplanbatchMapper.getbatchidbyplanidandbatchname(execplanid, batchname);
        // 检查plan当前的状态，如果状态为new，stop，finish继续执行
        Executeplan ep = executeplanMapper.findexplanWithid(execplanid);
        List<ExecuteplanTestcase> caselist = executeplanTestcaseMapper.findcasebytestplanid(execplanid);
        TestPlanCaseController.log.info("计划id" + execplanid + " 批次为：" + batchname + " 获取用例数：" + caselist.size());
//        InetAddress address = InetAddress.getLocalHost();
//        String ip = address.getHostAddress();
        String ip = IPHelpUtils.getInet4Address();
        TestPlanCaseController.log.info("当前机器的IP为：" + ip);
        List<Slaver> slaverlist = slaverMapper.findslaverbyip(ip);
        List<Dispatch> dispatchList = new ArrayList<>();
        if (slaverlist.size() == 0) {
            TestPlanCaseController.log.info("当前执行机的IP为：" + ip + " 还未注册，请先完成注册");
            throw new Exception("当前执行机的IP为：" + ip + " 还未注册，请先完成注册");
        } else {
            Long slaverid = slaverlist.get(0).getId();
            String slavername = slaverlist.get(0).getSlavername();
            TestPlanCaseController.log.info("slaverid：" + slaverid + " slavername ：" + slavername);
            for (ExecuteplanTestcase testcase : caselist) {
                //需要执行的用例，先进入调度，由调度定时器统一执行
                Dispatch dis = new Dispatch();
                dis.setExpect(testcase.getExpect());
                dis.setExecplanid(execplanid);
                dis.setTestcaseid(testcase.getTestcaseid());
                dis.setDeployunitname(testcase.getDeployunitname());
                dis.setStatus("待分配");
                dis.setBatchname(batchname);
                dis.setBatchid(epb.getId());
                dis.setCasejmxname(testcase.getCasejmxname());
                dis.setExecplanname(ep.getExecuteplanname());
                dis.setSlaverid(slaverid);
                dis.setSlavername(slavername);
                dis.setTestcasename(testcase.getCasename());
                dis.setPlantype(ep.getUsetype());
                dis.setThreadnum(testcase.getThreadnum());
                dis.setLoops(testcase.getLoops());
                dispatchList.add(dis);
                //dispatchService.save(dis);
            }
        }
        dispatchMapper.insertBatchDispatch(dispatchList);
        TestPlanCaseController.log.info("完成保存调度用例条数：" + dispatchList.size());
        return ResultGenerator.genOkResult();
    }

    public boolean jmeterclassexistornot(String jarpath, String jmeterclassname) {
        boolean flag = false;
        try {
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            File f = new File(jarpath);
            URL url1 = f.toURI().toURL();
            URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());

            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(jarpath);
            //返回zip文件条目的枚举
            Enumeration<JarEntry> enumFiles = jar.entries();
            JarEntry entry;

            //测试此枚举是否包含更多的元素
            while (enumFiles.hasMoreElements()) {
                entry = (JarEntry) enumFiles.nextElement();
                if (entry.getName().indexOf("META-INF") < 0) {
                    String classFullName = entry.getName();
                    if (classFullName.indexOf(".class") > 0) {
                        //去掉后缀.class
                        String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                        if (className.equals(jmeterclassname)) {
                            flag = true;
                        }
                        //打印类名
                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~打印类名:~~~~~~~~~~~~~~~~~~~~~~~~~" + className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @PostMapping("/test")
    public Result gettest(@RequestBody ExecuteplanTestcase plancase) {
        //tpcservice.executeplancase(plancase.getExecuteplanid(),plancase.getTestcaseid());
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/execperformancetest")
    public Result execperformancetest(@RequestBody Executeplanbatch dispatch) throws Exception {
        long Execplanid = dispatch.getExecuteplanid();
        String Batchname = dispatch.getBatchname();
        Executeplan executeplan = executeplanService.getBy("id", Execplanid);
        String property = System.getProperty("os.name");
        String ip =  IPHelpUtils.getInet4Address();
        List<Slaver> slaverlist = slaverMapper.findslaverbyip(ip);
        if (slaverlist.size() == 0) {
            TestPlanCaseController.log.error("性能任务-没有找到slaver。。。。。。。。" + "未找到ip为：" + ip + "的slaver，请检查调度中心-执行节点");
            return ResultGenerator.genFailedResult("未找到ip为：" + ip + "的slaver");
        }
        Long SlaverId = slaverlist.get(0).getId();


        String ProjectPath = System.getProperty("user.dir");
        String JmeterPath = "";
        String JmxPath = "";
        String JmeterPerformanceReportPath = "";
        String JmeterPerformanceReportLogFilePath = "";

        if (ProjectPath.contains("slaverservice")) {
            if (property.toLowerCase().startsWith("win")) {
                JmeterPath = ProjectPath + "\\apache-jmeter-5.3\\bin";
                JmxPath = ProjectPath + "\\servicejmxcase";
                JmeterPerformanceReportPath = ProjectPath + "\\performancereport";
                JmeterPerformanceReportLogFilePath = ProjectPath + "\\performancereportlogfile";
            } else {
                JmeterPath = ProjectPath + "/apache-jmeter-5.3/bin";
                JmxPath = ProjectPath + "/servicejmxcase";
                JmeterPerformanceReportPath = ProjectPath + "/performancereport";
                JmeterPerformanceReportLogFilePath = ProjectPath + "/performancereportlogfile";
            }
        } else {
            if (property.toLowerCase().startsWith("win")) {
                JmeterPath = ProjectPath + "\\slaverservice\\apache-jmeter-5.3\\bin";
                JmxPath = ProjectPath + "\\slaverservice\\servicejmxcase";
                JmeterPerformanceReportPath = ProjectPath + "\\slaverservice\\performancereport";
                JmeterPerformanceReportLogFilePath = ProjectPath + "\\slaverservice\\performancereportlogfile";
            } else {
                JmeterPath = ProjectPath + "/slaverservice/apache-jmeter-5.3/bin";
                JmxPath = ProjectPath + "/slaverservice/servicejmxcase";
                JmeterPerformanceReportPath = ProjectPath + "/slaverservice/performancereport";
                JmeterPerformanceReportLogFilePath = ProjectPath + "/slaverservice/performancereportlogfile";
            }
        }
        File dir = new File(JmeterPerformanceReportPath);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
            TestPlanCaseController.log.info("创建性能报告目录performancereport完成 :" + JmeterPerformanceReportPath);
        }
        File dirlog = new File(JmeterPerformanceReportLogFilePath);
        if (!dirlog.exists()) {// 判断目录是否存在
            dirlog.mkdir();
            TestPlanCaseController.log.info("创建性能报告日志目录performancereport完成 :" + JmeterPerformanceReportLogFilePath);
        }

        List<TestsceneDispatch> testsceneDispatchList = testsceneDispatchService.findscenebypbs(Execplanid, Batchname, SlaverId);

        String Scenename = testsceneDispatchList.get(0).getScenename();
        Long Sceneid = testsceneDispatchList.get(0).getTestsceneid();
        Long Batchid = testsceneDispatchList.get(0).getBatchid();

        List<TestsceneTestcase> testsceneTestcaseList = testsceneTestcaseService.findcasebyscenenid(Sceneid);
        long threads = testsceneDispatchList.get(0).getTargetconcurrency();
        long loops = testsceneDispatchList.get(0).getIterations();

        List<String> javasamplelist = new ArrayList<>();
        for (TestsceneTestcase tesc : testsceneTestcaseList) {
            long caseid = tesc.getTestcaseid();
            String classname = tesc.getCasename();
            String jsample = GetJavaSample(classname, String.valueOf(caseid));
            javasamplelist.add(jsample);
        }
        String jmxcontent = GetJmxFile(javasamplelist);
        TestPlanCaseController.log.info(" jmxcontent： " + jmxcontent);

        FileWriter fw = null;
        String Filepath = "";
        String JmxFileName = Execplanid + "-" + Batchid + "-" + Sceneid;
        try {
            String os = System.getProperty("os.name");
            if (os != null && os.toLowerCase().startsWith("windows")) {
                Filepath = JmxPath + "\\" + JmxFileName + ".jmx";
            } else {
                Filepath = JmxPath + "/" + JmxFileName + ".jmx";
            }
            fw = new FileWriter(Filepath, true);
            fw.write(jmxcontent);
        } catch (Exception ex) {
            TestPlanCaseController.log.error("保存jmx文件发生异常，请检查!" + ex.getMessage());
            throw new Exception("执行机Slaver运行性能测试保存jmx文件发生异常异常：" + ex.getMessage());
//            return ResultGenerator.genFailedResult("保存jmx文件发生异常，请检查!" + ex.getMessage());
        } finally {
            if (null != fw) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    TestPlanCaseController.log.error("保存jmx文件close发生异常，请检查!" + ex.getMessage());
                }
            }
        }
        try {
            // 增加逻辑 获取计划的当前状态，如果为stop，放弃整个循环执行,return 掉
            tpcservice.ExecuteHttpPerformancePlanScene(Filepath, url.trim(), username.trim(), password.trim(), executeplan.getExecuteplanname(), Scenename, SlaverId, executeplan.getId(), Sceneid, Batchid, Batchname, JmeterPath, JmxPath, JmeterPerformanceReportPath, JmeterPerformanceReportLogFilePath, threads, loops, executeplan.getCreator());
            // 更新调度表对应用例状态为已分配
            dispatchMapper.updatedispatchstatusbyname("已分配", dispatch.getSlaverid(), dispatch.getExecuteplanid(), Batchname, Sceneid);
            TestPlanCaseController.log.info("性能任务-。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。更新dispatch状态为已分配.....开始调用jmeter..。。。。。。。。。。。。。。。。。。。。。。。。。" + dispatch.getId());
            slaverMapper.updateSlaverStaus(SlaverId, "运行中");
            executeplanbatchMapper.updatebatchstatus(dispatch.getExecuteplanid(), dispatch.getBatchname(), "运行中");
            TestPlanCaseController.log.info("性能任务-。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。调用jmeter完成..。。。。。。。。。。。。。。。。。。。。。。。。。" + dispatch.getId());
            //}
        } catch (Exception ex) {
            dispatchMapper.updatescenedispatchstatusandmemo("调度异常", "执行机Slaver运行性能测试异常：" + ex.getMessage(), dispatch.getSlaverid(), dispatch.getExecuteplanid(), Batchid, Sceneid);
            TestPlanCaseController.log.info("性能任务-调度异常。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。ExecuteHttpPerformancePlanScene异常报错..。。。。。。。。。。。。。。。。。。。。。。。。。" + ex.getMessage());
//            return ResultGenerator.genFailedResult(ex.getMessage());
            throw new Exception("执行机Slaver运行性能测试异常：" + ex.getMessage());
        }
        //}
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/execfunctiontest")
    public Result execfunctiontest(@RequestBody List<Executeplanbatch> executeplanbatchList) throws Exception {
        String property = System.getProperty("os.name");
//        String ip = null;
//        ip = IPHelpUtils.getInet4Address();
//        List<Slaver> slaverlist = slaverMapper.findslaverbyip(ip);
//        if (slaverlist.size() == 0) {
//            TestPlanCaseController.log.error("功能任务-没有找到slaver。。。。。。。。" + "未找到ip为：" + ip + "的slaver，请检查调度中心-执行节点");
//            return ResultGenerator.genFailedResult("未找到IP为：" + ip + "的slaver");
//        }
//        Long SlaverId = slaverlist.get(0).getId();
//        try {
        String ProjectPath = System.getProperty("user.dir");
        String JmeterPath = "";
        String JmxPath = "";
        if (ProjectPath.contains("slaverservice")) {
            if (property.toLowerCase().startsWith("win")) {
                JmeterPath = ProjectPath + "\\apache-jmeter-5.3\\bin";
                JmxPath = ProjectPath + "\\servicejmxcase";
            } else {
                JmeterPath = ProjectPath + "/apache-jmeter-5.3/bin";
                JmxPath = ProjectPath + "/servicejmxcase";
            }

        } else {
            if (property.toLowerCase().startsWith("win")) {
                JmeterPath = ProjectPath + "\\slaverservice\\apache-jmeter-5.3\\bin";
                JmxPath = ProjectPath + "\\slaverservice\\servicejmxcase";
            } else {
                JmeterPath = ProjectPath + "/slaverservice/apache-jmeter-5.3/bin";
                JmxPath = ProjectPath + "/slaverservice/servicejmxcase";
            }
        }
        String SceneIDs = "";
        long SlaverId = 0;
        long planid = 0;
        String batchname = "";
        String planname = "";
        for (Executeplanbatch executeplanbatch : executeplanbatchList) {
            planid = executeplanbatch.getExecuteplanid();
            batchname = executeplanbatch.getBatchname();
            planname = executeplanbatch.getExecuteplanname();
            Long Sceneid = executeplanbatch.getSceneid();
            SceneIDs = SceneIDs + Sceneid + ",";
            SlaverId = executeplanbatch.getSlaverid();
        }
        try {
            tpcservice.ExecuteHttpPlanFunctionCase(SlaverId, planid,planname, batchname, JmeterPath, JmxPath, SceneIDs, url, username, password, SlaverId);
            for (Executeplanbatch executeplanbatch : executeplanbatchList) {
                Long Sceneid = executeplanbatch.getSceneid();
                Long batchid = executeplanbatch.getId();
                SlaverId = executeplanbatch.getSlaverid();
                dispatchMapper.updatedispatchstatus("已分配", SlaverId, planid, batchid, Sceneid);
                executeplanbatch.setStatus("执行中");
                executeplanbatchService.update(executeplanbatch);
            }
            slaverMapper.updateSlaverStaus(SlaverId, "运行中");
        } catch (Exception ex) {
            TestPlanCaseController.log.info("调用JmeterCMD异常：。。。。。。。。" + ex.getMessage());
            throw new Exception(ex.getMessage());
//            return ResultGenerator.genFailedResult(ex.getMessage());
        }
        return ResultGenerator.genOkResult();
    }


    private String GetJavaSample(String Classname,String caseid)
    {
        String InitBody="<JavaSampler guiclass=\"JavaTestSamplerGui\" testclass=\"JavaSampler\" testname=\"autometerpr\" enabled=\"true\">\n" +
                "          <elementProp name=\"arguments\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" enabled=\"true\">\n" +
                "            <collectionProp name=\"Arguments.arguments\">\n" +
                "              <elementProp name=\"mysqlurl\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">mysqlurl</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${mysqlurl}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"mysqlusername\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">mysqlusername</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${mysqlusername}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"mysqlpassword\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">mysqlpassword</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${mysqlpassword}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"slaverId\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">slaverId</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${slaverid}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"planid\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">planid</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${testplanid}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"batchname\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">batchname</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${batchname}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"sceneid\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">sceneid</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${sceneid}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"scenename\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">scenename</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${scenename}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"batchid\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">batchid</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${batchid}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"caseid\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">caseid</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">000</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"reportlogfolder\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">reportlogfolder</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${reportlogfolder}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "              <elementProp name=\"casereportfolder\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">casereportfolder</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">${casereportfolder}</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>\n" +
                "            </collectionProp>\n" +
                "          </elementProp>\n" +
                "          <stringProp name=\"classname\">com.api.autohttpf.test.httpapitestcase.HttpApiPerformance</stringProp>\n" +
                "        </JavaSampler>\n" +
                "        <hashTree/>";

        String CaseValue=" <elementProp name=\"caseid\" elementType=\"Argument\">\n" +
                "                <stringProp name=\"Argument.name\">caseid</stringProp>\n" +
                "                <stringProp name=\"Argument.value\">000</stringProp>\n" +
                "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "              </elementProp>";
        String NewCaseValue=CaseValue.replace("000",caseid);
        String ClassValue="testname=\"autometerpr\"";
        String NewClassValue=ClassValue.replace("autometerpr",Classname);
        String Last=InitBody.replace(ClassValue,NewClassValue);
        Last=Last.replace(CaseValue,NewCaseValue);
        return Last;
    }

    private String GetJmxFile(List<String> Bodys) {
        String Header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jmeterTestPlan version=\"1.2\" properties=\"5.0\" jmeter=\"5.3\">\n" +
                "  <hashTree>\n" +
                "    <TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"HttpPerformance\" enabled=\"true\">\n" +
                "      <stringProp name=\"TestPlan.comments\">HttpPerformanceParams</stringProp>\n" +
                "      <boolProp name=\"TestPlan.functional_mode\">false</boolProp>\n" +
                "      <boolProp name=\"TestPlan.serialize_threadgroups\">true</boolProp>\n" +
                "      <elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"用户定义的变量\" enabled=\"true\">\n" +
                "        <collectionProp name=\"Arguments.arguments\">\n" +
                "          <elementProp name=\"thread\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">thread</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(thread,1)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"loops\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">loops</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(loops,1)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"testplanid\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">testplanid</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(testplanid,13)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"batchname\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">batchname</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(batchname,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"sceneid\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">sceneid</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(sceneid,1)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"scenename\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">scenename</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(scenename,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"slaverid\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">slaverid</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(slaverid,5)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"batchid\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">batchid</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(batchid,25)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"casereportfolder\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">casereportfolder</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(casereportfolder,/Users/fanseasn/Desktop/testresult/13-2-vvvvvv1111111)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"mysqlurl\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">mysqlurl</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(mysqlurl,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"mysqlusername\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">mysqlusername</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(mysqlusername,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"mysqlpassword\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">mysqlpassword</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(mysqlpassword,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"creator\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">creator</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(creator,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"reportlogfolder\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">reportlogfolder</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(reportlogfolder,)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"projectid\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">projectid</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(projectid,1)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "        </collectionProp>\n" +
                "      </elementProp>\n" +
                "      <stringProp name=\"TestPlan.user_define_classpath\"></stringProp>\n" +
                "    </TestPlan>\n" +
                "    <hashTree>\n" +
                "      <ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"sceneperformance\" enabled=\"true\">\n" +
                "        <stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>\n" +
                "        <elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"循环控制器\" enabled=\"true\">\n" +
                "          <boolProp name=\"LoopController.continue_forever\">false</boolProp>\n" +
                "          <stringProp name=\"LoopController.loops\">${loops}</stringProp>\n" +
                "        </elementProp>\n" +
                "        <stringProp name=\"ThreadGroup.num_threads\">${thread}</stringProp>\n" +
                "        <stringProp name=\"ThreadGroup.ramp_time\"></stringProp>\n" +
                "        <longProp name=\"ThreadGroup.start_time\">1502182031000</longProp>\n" +
                "        <longProp name=\"ThreadGroup.end_time\">1502182031000</longProp>\n" +
                "        <boolProp name=\"ThreadGroup.scheduler\">false</boolProp>\n" +
                "        <stringProp name=\"ThreadGroup.duration\"></stringProp>\n" +
                "        <stringProp name=\"ThreadGroup.delay\"></stringProp>\n" +
                "        <boolProp name=\"ThreadGroup.same_user_on_next_iteration\">true</boolProp>\n" +
                "      </ThreadGroup>\n" +
                "      <hashTree>";
        String Tail = " </hashTree>\n" +
                "    </hashTree>\n" +
                "  </hashTree>\n" +
                "</jmeterTestPlan>";

        String BodyAll = "";
        for (String body : Bodys) {
            BodyAll = BodyAll + body;
        }
        return Header + BodyAll + Tail;
    }

    public JmeterPerformanceObject GetJmeterPerformanceCaseData(Dispatch dispatch) throws Exception {
        JmeterPerformanceObject jmeterPerformanceObject = new JmeterPerformanceObject();
        jmeterPerformanceObject.setTestplanid(dispatch.getExecplanid());
        jmeterPerformanceObject.setCaseid(dispatch.getTestcaseid());
        jmeterPerformanceObject.setSlaverid(dispatch.getSlaverid());
        jmeterPerformanceObject.setBatchid(dispatch.getBatchid());
        jmeterPerformanceObject.setCasename(dispatch.getTestcasename());
        jmeterPerformanceObject.setBatchname(dispatch.getBatchname());
        jmeterPerformanceObject.setExecuteplanname(dispatch.getExecplanname());
        Apicases apicases = apicasesService.getBy("id", dispatch.getTestcaseid());
        if (apicases == null) {
            throw new Exception("未找到用例，请检查是否被删除！");
        }
        jmeterPerformanceObject.setCasetype(apicases.getCasetype());
        Api api = apiService.getBy("id", apicases.getApiid());
        if (api == null) {
            throw new Exception("未找到用例的API，请检查是否被删除！");
        }
        jmeterPerformanceObject.setApistyle(api.getApistyle());
        jmeterPerformanceObject.setRequestmMthod(api.getVisittype());
        jmeterPerformanceObject.setRequestcontenttype(api.getRequestcontenttype());
        jmeterPerformanceObject.setResponecontenttype(api.getResponecontenttype());
        Deployunit deployunit = deployunitService.getBy("id", api.getDeployunitid());
        if (deployunit == null) {
            throw new Exception("未找到用例的API所在的微服务，请检查是否被删除！");
        }
        jmeterPerformanceObject.setProtocal(deployunit.getProtocal());

        Executeplan executeplan = epservice.getBy("id", dispatch.getExecplanid());
        if (executeplan == null) {
            throw new Exception("未找到用例的测试集合，请检查是否被删除！");
        }
        Long EnvID = executeplan.getEnvid();
        Macdepunit macdepunit = macdepunitService.getmacdepbyenvidanddepid(EnvID, deployunit.getId());
        if (macdepunit != null) {
            String testserver = "";
            String resource = "";
            String BaseUrl = deployunit.getBaseurl();
            String ApiUrl = api.getPath();
            if (!ApiUrl.startsWith("/")) {
                ApiUrl = "/" + ApiUrl;
            }
            if (macdepunit.getVisittype().equalsIgnoreCase("ip")) {
                Long MachineID = macdepunit.getMachineid();
                Machine machine = machineService.getBy("id", MachineID);
                if (machine == null) {
                    throw new Exception("未在环境中部署找到服务器，请检查是否被删除！");
                }
                jmeterPerformanceObject.setMachineip(machine.getIp());
                testserver = machine.getIp();

                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + BaseUrl + ApiUrl;
                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + ":" + deployunit.getPort() + "/" + BaseUrl + ApiUrl;
                    }
                    TestPlanCaseController.log.info("GetJmeterPerformanceCaseData resource ip is:" + resource);
                }
            } else {
                testserver = macdepunit.getDomain();
                if (BaseUrl == null || BaseUrl.isEmpty()) {
                    resource = deployunit.getProtocal() + "://" + testserver + ApiUrl;
                } else {
                    if (BaseUrl.startsWith("/")) {
                        resource = deployunit.getProtocal() + "://" + testserver + BaseUrl + ApiUrl;

                    } else {
                        resource = deployunit.getProtocal() + "://" + testserver + "/" + BaseUrl + ApiUrl;
                    }
                }
                TestPlanCaseController.log.info("GetJmeterPerformanceCaseData resource domain is:" + resource);
            }
            jmeterPerformanceObject.setDeployunitvisittype(macdepunit.getVisittype());
            jmeterPerformanceObject.setResource(resource.trim());
        } else {
            throw new Exception("未在环境中部署用例API所在的微服务，请检查是否被删除！");
        }

        List<ApicasesAssert> apicasesAssertList = apicasesAssertService.findAssertbycaseid(dispatch.getTestcaseid().toString());
        if (apicasesAssertList.size() > 0) {
            String ExpectJson = JSON.toJSONString(apicasesAssertList);
            jmeterPerformanceObject.setExpect(ExpectJson);
        } else {
            jmeterPerformanceObject.setExpect("");
        }

        jmeterPerformanceObject.setMysqlurl(url.trim());
        jmeterPerformanceObject.setMysqlusername(username.trim());
        jmeterPerformanceObject.setMysqlpassword(password.trim());
        return jmeterPerformanceObject;
    }

    public JmeterPerformanceObject GetJmeterPerformanceCaseRequestData(JmeterPerformanceObject jmeterPerformanceObject, Dispatch dispatch, Api api) throws Exception {
        String PlanID = String.valueOf(jmeterPerformanceObject.getTestplanid());
        String BatchName = jmeterPerformanceObject.getBatchname();
        String RequestContentType = jmeterPerformanceObject.getRequestcontenttype();
        long Caseid = jmeterPerformanceObject.getCaseid();
        Apicases apicases = apicasesService.getBy("id", Caseid);
        long projectid = apicases.getProjectid();

        List<ApiCasedata> apiCasedataList = apiCasedataService.getcasedatabycaseid(dispatch.getTestcaseid());

        TestPlanCaseController.log.info("GetJmeterPerformanceCaseRequestData :" + PlanID + " BatchName:" + BatchName);
        List<TestvariablesValue> testvariablesValueList = testvariablesValueService.gettvlist(Long.parseLong(PlanID), BatchName, "接口");

        List<TestvariablesValue> DBtestvariablesValueList = testvariablesValueService.gettvlist(Long.parseLong(PlanID), BatchName, "数据库");

        TestPlanCaseController.log.info("gettvlist size :" + testvariablesValueList.size());

        HashMap<String, String> InterFaceMap = new HashMap<>();
        for (TestvariablesValue te : testvariablesValueList) {
            InterFaceMap.put(te.getVariablesname(), te.getVariablesvalue());
            TestPlanCaseController.log.info("接口变量：" + te.getVariablesname() + " 接口变量值: " + te.getVariablesvalue());
        }

        HashMap<String, String> DBMap = new HashMap<>();
        for (TestvariablesValue te : DBtestvariablesValueList) {
            DBMap.put(te.getVariablesname(), te.getVariablesvalue());
        }

        Condition gvcon = new Condition(Globalvariables.class);
        gvcon.createCriteria().andCondition("projectid = " + projectid);
        List<Globalvariables> globalvariablesList = globalvariablesService.listByCondition(gvcon);

        HashMap<String, String> GlobalVariablesHashMap = new HashMap<>();
        for (Globalvariables va : globalvariablesList) {
            GlobalVariablesHashMap.put(va.getKeyname(), va.getKeyvalue());
        }


        //1.Url替换接口变量
        String RequestUrl = jmeterPerformanceObject.getResource();
        for (String VariableName : InterFaceMap.keySet()) {
            String UseVariableName = "<" + VariableName + ">";
            if (RequestUrl.contains(UseVariableName)) {
                String VariableValue = InterFaceMap.get(VariableName);
                RequestUrl = RequestUrl.replace(UseVariableName, VariableValue);
            }
        }
        //2.Url替换数据库变量
        for (String VariableName : DBMap.keySet()) {
            String UseVariableName = "<<" + VariableName + ">>";
            if (RequestUrl.contains(UseVariableName)) {
                String VariableValue = DBMap.get(VariableName);
                RequestUrl = RequestUrl.replace(UseVariableName, VariableValue);
            }
        }

        //3.全局变量Url替换
        for (Globalvariables variables : globalvariablesList) {
            String VariableName = "$" + variables.getKeyname() + "$";
            if (RequestUrl.contains(VariableName)) {
                Object VariableValue = variables.getKeyvalue();
                RequestUrl = RequestUrl.replace(VariableName, VariableValue.toString());
            }
        }

        jmeterPerformanceObject.setResource(RequestUrl);
        //String Caseid=dispatch.getTestcaseid().toString();
        HashMap<String, Object> HeaderMap = new HashMap<>();
        HashMap<String, Object> ParamsMap = new HashMap<>();
        HashMap<String, Object> BodyMap = new HashMap<>();
        String PostData = "";
        HashMap<String, String> globalheaderParamsHashMap = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        params.put("executeplanid", dispatch.getExecplanid());
        List<Globalheaderuse> globalheaderuseList = globalheaderuseService.searchheaderbyepid(params);
        for (ApiCasedata apiCasedata : apiCasedataList) {
            String ParamName = apiCasedata.getApiparam();
            String Paramvalue = apiCasedata.getApiparamvalue();
            String DataType = apiCasedata.getParamstype();
            if (apiCasedata.getPropertytype().equalsIgnoreCase("Params")) {
                Object Result = Paramvalue;
                if ((Paramvalue.contains("<") && Paramvalue.contains(">")) || (Paramvalue.contains("<<") && Paramvalue.contains(">>")) || (Paramvalue.contains("[") && Paramvalue.contains("]")) || (Paramvalue.contains("$") && Paramvalue.contains("$"))) {
                    Result = GetVaraibaleValue(Paramvalue, InterFaceMap, DBMap, GlobalVariablesHashMap, projectid);
                }
                Object LastObjectValue = GetDataByType(Result.toString(), DataType);
                ParamsMap.put(ParamName, LastObjectValue);
            }
            if (apiCasedata.getPropertytype().equalsIgnoreCase("Header")) {
                globalheaderParamsHashMap.put(ParamName, Paramvalue);
                TestPlanCaseController.log.info("Header用例参数：" + ParamName + " Header用例Value: " + Paramvalue);
                //获取所有的全局header，k,v,如果有和用例header相同参数名则覆盖
//                for (Globalheaderuse globalheaderuse :globalheaderuseList) {
//                    Map<String, Object> headeridparams=new HashMap<>();
//                    headeridparams.put("globalheaderid",globalheaderuse.getGlobalheaderid());
//                    TestPlanCaseController.log.info("全局Header参数--globalheadername："+globalheaderuse.getGlobalheadername());
//                    globalheaderParamsList= globalheaderParamsService.findGlobalheaderParamsWithName(headeridparams);
//                    for (GlobalheaderParams globalheaderParams : globalheaderParamsList) {
//                        TestPlanCaseController.log.info("全局Header参数globalheaderParams："+globalheaderParams.getKeyname()+" globalheaderparamvalue:"+globalheaderParams.getKeyvalue());
//                        globalheaderParamsHashMap.put(globalheaderParams.getKeyname(), globalheaderParams.getKeyvalue());
//                    }
//                }
//                Object Result = Paramvalue;
//                if ((Paramvalue.contains("<") && Paramvalue.contains(">")) || (Paramvalue.contains("<<") && Paramvalue.contains(">>")) || (Paramvalue.contains("[") && Paramvalue.contains("]")) || (Paramvalue.contains("$") && Paramvalue.contains("$"))) {
//                    Result = GetVaraibaleValue(Paramvalue, InterFaceMap, DBMap,GlobalVariablesHashMap);
//                }
//                HeaderMap.put(ParamName, Result);
//                TestPlanCaseController.log.info("最终Header参数："+ParamName+" 最终Header的Value: "+Result);
            }
            if (apiCasedata.getPropertytype().equalsIgnoreCase("Body")) {
                if (RequestContentType.equalsIgnoreCase("Form表单")) {
                    Object Result = Paramvalue;
                    if ((Paramvalue.contains("<") && Paramvalue.contains(">")) || (Paramvalue.contains("<<") && Paramvalue.contains(">>")) || (Paramvalue.contains("[") && Paramvalue.contains("]")) || (Paramvalue.contains("$") && Paramvalue.contains("$"))) {
                        Result = GetVaraibaleValue(Paramvalue, InterFaceMap, DBMap, GlobalVariablesHashMap, projectid);
                    }
                    Object LastObjectValue = GetDataByType(Result.toString(), DataType);
                    BodyMap.put(ParamName, LastObjectValue);
                } else {
                    PostData = Paramvalue;
                    //替换接口变量
                    for (String VariableName : InterFaceMap.keySet()) {
                        String UseVariableName = "<" + VariableName + ">";
                        if (PostData.contains(UseVariableName)) {
                            String VariableValue = InterFaceMap.get(VariableName);
                            PostData = PostData.replace(UseVariableName, VariableValue);
                        }
                    }
                    //替换数据库变量
                    for (String VariableName : DBMap.keySet()) {
                        String UseVariableName = "<<" + VariableName + ">>";
                        if (PostData.contains(UseVariableName)) {
                            String VariableValue = DBMap.get(VariableName);
                            PostData = PostData.replace(UseVariableName, VariableValue);
                        }
                    }

                    //替换全局变量
                    for (Globalvariables variables : globalvariablesList) {
                        String VariableName = "$" + variables.getKeyname() + "$";
                        if (PostData.contains(VariableName)) {
                            Object VariableValue = GlobalVariablesHashMap.get(variables.getKeyname());
                            PostData = PostData.replace(VariableName, VariableValue.toString());
                        }
                    }
                }
            }
        }

        //获取所有的全局header，k,v,如果有和用例header相同参数名则覆盖
        for (Globalheaderuse globalheaderuse : globalheaderuseList) {
            Map<String, Object> headeridparams = new HashMap<>();
            headeridparams.put("globalheaderid", globalheaderuse.getGlobalheaderid());
            TestPlanCaseController.log.info("全局Header参数--globalheadername：" + globalheaderuse.getGlobalheadername());
            List<GlobalheaderParams> globalheaderParamsList = globalheaderParamsService.findGlobalheaderParamsWithName(headeridparams);
            for (GlobalheaderParams globalheaderParams : globalheaderParamsList) {
                globalheaderParamsHashMap.put(globalheaderParams.getKeyname(), globalheaderParams.getKeyvalue());
                TestPlanCaseController.log.info("全局Header参数globalheaderParams：" + globalheaderParams.getKeyname() + " globalheaderparamvalue:" + globalheaderParams.getKeyvalue());
            }
        }
        for (String ParamName : globalheaderParamsHashMap.keySet()) {
            String Paramvalue = globalheaderParamsHashMap.get(ParamName);
            Object Result = Paramvalue;
            if ((Paramvalue.contains("<") && Paramvalue.contains(">")) || (Paramvalue.contains("<<") && Paramvalue.contains(">>")) || (Paramvalue.contains("[") && Paramvalue.contains("]")) || (Paramvalue.contains("$") && Paramvalue.contains("$"))) {
                Result = GetVaraibaleValue(Paramvalue, InterFaceMap, DBMap, GlobalVariablesHashMap, projectid);
            }
            HeaderMap.put(ParamName, Result);
            TestPlanCaseController.log.info("最终Header参数：" + ParamName + " 最终Header的Value: " + Result);
        }

        if (HeaderMap.size() > 0) {
            jmeterPerformanceObject.setHeadjson(JSON.toJSONString(HeaderMap));
        } else {
            jmeterPerformanceObject.setHeadjson("");
        }
        if (ParamsMap.size() > 0) {
            jmeterPerformanceObject.setParamsjson(JSON.toJSONString(ParamsMap));
        } else {
            jmeterPerformanceObject.setParamsjson("");
        }
        if (BodyMap.size() > 0) {
            jmeterPerformanceObject.setBodyjson(JSON.toJSONString(BodyMap));
        } else {
            jmeterPerformanceObject.setBodyjson("");
        }

        jmeterPerformanceObject.setPostdata(PostData);

        //增加随机变量json
        Condition rdcon = new Condition(Variables.class);
        rdcon.createCriteria().andCondition("projectid = " + projectid);
        List<Variables> variablesList = variablesService.listByCondition(rdcon);
        String variablesjson = "";
        if (variablesList.size() > 0) {
            variablesjson = JSON.toJSONString(variablesList);
        }
        jmeterPerformanceObject.setRadomvariablejson(variablesjson);

        //断言
        List<ApicasesAssert> apicasesAssertList = apicasesAssertService.findAssertbycaseid(dispatch.getTestcaseid().toString());
        if (apicasesAssertList.size() > 0) {
            String ExpectJson = JSON.toJSONString(apicasesAssertList);
            jmeterPerformanceObject.setExpect(ExpectJson);
        } else {
            jmeterPerformanceObject.setExpect("");
        }
        return jmeterPerformanceObject;
    }

    //判断是否有拼接
    private boolean GetSubOrNot(HashMap<String, String> VariablesMap, String Value, String prefix, String profix) {
        boolean flag = false;
        for (String Key : VariablesMap.keySet()) {
            String ActualValue = prefix + Key + profix;
            if (Value.contains(ActualValue)) {
                String LeftValue = Value.replace(ActualValue, "");
                if (LeftValue.length() > 0) {
                    //表示有拼接
                    return true;
                } else {
                    return false;
                }
            }
        }
        return flag;
    }

    private Object GetVaraibaleValue(String Value, HashMap<String, String> InterfaceMap, HashMap<String, String> DBMap, HashMap<String, String> globalvariablesMap, long projectid) throws Exception {
        Object ObjectValue = Value;
        boolean exist = false; //标记是否Value有变量处理，false表示没有对应的子条件处理过

        //参数值替换接口变量
        for (String interfacevariablesName : InterfaceMap.keySet()) {
            boolean flag = GetSubOrNot(InterfaceMap, Value, "<", ">");
            if (Value.contains("<" + interfacevariablesName + ">")) {
                exist = true;
                String ActualValue = InterfaceMap.get(interfacevariablesName);
                if (flag) {
                    //有拼接认为是字符串
                    Value = Value.replace("<" + interfacevariablesName + ">", ActualValue);
                    ObjectValue = Value;
                } else {
                    //无拼接则转换成具体类型,根据变量名获取变量类型
                    Condition tvcon = new Condition(Testvariables.class);
                    tvcon.createCriteria().andCondition("projectid = " + projectid).andCondition("testvariablesname= '" + interfacevariablesName + "'");
                    List<Testvariables> variablesList = testvariablesService.listByCondition(tvcon);
                    Testvariables testvariables = variablesList.get(0);// testvariablesService.getBy("testvariablesname", interfacevariablesName);//  testMysqlHelp.GetVariablesDataType(interfacevariablesName);
                    if (testvariables == null) {
                        ObjectValue = "未找到变量：" + Value + "绑定的接口用例，请检查变量管理-用例变量中是否存在此变量绑定的接口用例";
                    } else {
                        ObjectValue = GetDataByType(ActualValue, testvariables.getValuetype());
                    }
                }
            }
        }
        //参数值替换数据库变量
        for (String DBvariablesName : DBMap.keySet()) {
            boolean flag = GetSubOrNot(DBMap, Value, "<<", ">>");
            if (Value.contains("<<" + DBvariablesName + ">>")) {
                exist = true;
                String ActualValue = DBMap.get(DBvariablesName);
                if (flag) {
                    //有拼接认为是字符串
                    Value = Value.replace("<<" + DBvariablesName + ">>", ActualValue);
                    ObjectValue = Value;
                } else {
                    //无拼接则转换成具体类型,根据变量名获取变量类型
                    Condition dbcon = new Condition(Dbvariables.class);
                    dbcon.createCriteria().andCondition("projectid = " + projectid).andCondition("dbvariablesname= '" + DBvariablesName + "'");
                    List<Dbvariables> variablesList = dbvariablesService.listByCondition(dbcon);
                    Dbvariables dbvariables = variablesList.get(0);// dbvariablesService.getBy("dbvariablesname", DBvariablesName);
                    if (dbvariables == null) {
                        ObjectValue = "未找到变量：" + Value + " 请检查变量管理-数据库变量中是否存在此变量";
                    } else {
                        ObjectValue = GetDataByType(ActualValue, dbvariables.getValuetype());
                    }
                }
            }
        }
        //参数值替换全局变量
        for (String variables : globalvariablesMap.keySet()) {
            boolean flag = GetSubOrNot(globalvariablesMap, Value, "$", "$");
            if (Value.contains("$" + variables + "$")) {
                exist = true;
                if (flag) {
                    Object GlobalVariableValue = globalvariablesMap.get(variables);
                    Value = Value.replace("$" + variables + "$", GlobalVariableValue.toString());
                    ObjectValue = Value;
                } else {
                    ObjectValue = globalvariablesMap.get(variables);
                }
            }
        }
        if (!exist) {
            throw new Exception("当前用例参数值中存在变量：" + Value + " 未找到对应值，请检查是否有配置对应变量的子条件获取此变量值");
        }
        return ObjectValue;
    }


//    private HashMap<String, Object> GetHeaderFromTestPlanParam(HashMap<String, Object> HeaderMap, Dispatch dispatch, HashMap<String, String> InterfaceMap, HashMap<String, String> DBMap) throws Exception {
//        HashMap<String, Object> resultmap = new HashMap<>();
//        //List<ExecuteplanParams> executeplanHeaderParamList = executeplanParamsService.getParamsbyepid(dispatch.getExecplanid(), "Header");
//        Map<String,Object>params=new HashMap<>();
//        params.put("executeplanid",dispatch.getExecplanid());
//        List<Globalheaderuse> globalheaderuseList = globalheaderuseService.searchheaderbyepid(params);
//        List<GlobalheaderParams> globalheaderParamsList=new ArrayList<>();
//        HashMap<String, String> globalheaderParamsHashMap = new HashMap<>();
//
//        for (String HeaderName:HeaderMap.keySet()) {
//            globalheaderParamsHashMap.put(HeaderName,HeaderMap.get(HeaderName).toString());
//        }
//
//        //获取所有的全局header，k,v,如果有和用例header相同参数名则覆盖
//        for (Globalheaderuse globalheaderuse :globalheaderuseList) {
////            Condition con=new Condition(GlobalheaderParams.class);
////            con.createCriteria().andCondition("globalheaderid = " + globalheaderuse.getGlobalheaderid());
//            Map<String, Object> headeridparams=new HashMap<>();
//            params.put("globalheaderid",globalheaderuse.getGlobalheaderid());
//            globalheaderParamsList= globalheaderParamsService.findGlobalheaderParamsWithName(headeridparams);
//            for (GlobalheaderParams globalheaderParams : globalheaderParamsList) {
//                if (!globalheaderParamsHashMap.containsKey(globalheaderParams.getKeyname())) {
//                    globalheaderParamsHashMap.put(globalheaderParams.getKeyname(), globalheaderParams.getKeyvalue());
//                }
//            }
//        }
//        for (String ParamName : globalheaderParamsHashMap.keySet()) {
//            String ParamValue = globalheaderParamsHashMap.get(ParamName);
//            Object Result = ParamValue;
//            if ((ParamValue.contains("<") && ParamValue.contains(">")) || (ParamValue.contains("<<") && ParamValue.contains(">>")) || (ParamValue.contains("[") && ParamValue.contains("]"))) {
//                Result = GetVaraibaleValue(ParamValue, InterfaceMap, DBMap);
//            }
//            resultmap.put(ParamName, Result);
//        }
//        return HeaderMap;
//    }

    public JmeterPerformanceObject GetJmeterPerformance(Dispatch dispatch) throws Exception {
        Apicases apicases = apicasesService.getBy("id", dispatch.getTestcaseid());
        JmeterPerformanceObject jmeterPerformanceObject = GetJmeterPerformanceCaseData(dispatch);
        jmeterPerformanceObject.setProjectid(apicases.getProjectid());
        Api api = apiService.getBy("id", apicases.getApiid());
        jmeterPerformanceObject = GetJmeterPerformanceCaseRequestData(jmeterPerformanceObject, dispatch, api);
        return jmeterPerformanceObject;
    }

    public void JmeterClassNotExist(Dispatch dis, String jmeterclassname, String casename) {
        // 未找到用例对应的jmeter-class文件，当前用例失败，并且记录计划状态
        ApicasesReport ar = new ApicasesReport();
        ar.setTestplanid(dis.getExecplanid());
        ar.setCaseid(dis.getTestcaseid());
        ar.setCasename(dis.getTestcasename());
        ar.setErrorinfo("性能任务-执行用例：" + casename + " |未找到用例对应的jmeter-class类：" + jmeterclassname + " 请检查是否已经开发提交");
        ar.setBatchname(dis.getBatchname());
        ar.setExpect(dis.getExpect());
        ar.setStatus("失败");
        ar.setSlaverid(dis.getSlaverid());
        ar.setRuntime(new Long(0));
        Long planid = dis.getExecplanid();

        apicasesReportPerformanceMapper.addcasereport(ar);
        //epservice.updatetestplanstatus(planid, "fail");
        //PerformanceDispatchScheduleTask.log.info("性能任务-未找到用例对应的jmeter-class类......." + jmeterclassname);
    }

    public boolean JmeterClassExist(String jmeterclassname, String JmeterPath) {
        String JmeterExtJarPath = JmeterPath.replace("bin", "lib");
        String JarPath = JmeterExtJarPath + "/ext/api-jmeter-autotest-1.0.jar";
        boolean flag = false;
        try {
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            File f = new File(JarPath);
            URL url1 = f.toURI().toURL();
            URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread().getContextClassLoader());

            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(JarPath);
            //返回zip文件条目的枚举
            Enumeration<JarEntry> enumFiles = jar.entries();
            JarEntry entry;

            //测试此枚举是否包含更多的元素
            while (enumFiles.hasMoreElements()) {
                entry = (JarEntry) enumFiles.nextElement();
                if (entry.getName().indexOf("META-INF") < 0) {
                    String classFullName = entry.getName();
                    if (classFullName.indexOf(".class") > 0) {
                        //去掉后缀.class
                        String className = classFullName.substring(0, classFullName.length() - 6).replace("/", ".");
                        if (className.equals(jmeterclassname)) {
                            flag = true;
                        }
                        //打印类名
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void FunJmeterClassNotExist(Dispatch dis, String jmeterclassname, String casename) {
        // 未找到用例对应的jmeter-class文件，当前用例失败，并且记录计划状态
        ApicasesReport ar = new ApicasesReport();
        ar.setTestplanid(dis.getExecplanid());
        ar.setCaseid(dis.getTestcaseid());
        ar.setCasename(dis.getTestcasename());
        ar.setErrorinfo("功能任务-执行用例：" + casename + " |未找到用例对应的jmeter-class类：" + jmeterclassname + " 请检查是否已经开发提交");
        ar.setBatchname(dis.getBatchname());
        ar.setExpect(dis.getExpect());
        ar.setStatus("失败");
        ar.setRuntime(new Long(0));
        Long planid = dis.getExecplanid();

        apicasereportservice.addcasereport(ar);
        epservice.updatetestplanstatus(planid, "fail");
        TestPlanCaseController.log.info("功能任务-未找到用例对应的jmeter-class类......." + jmeterclassname);
    }

    //根据数据类型转换
    private Object GetDataByType(String Data, String ValueType) {
        Object Result = new Object();
        if (ValueType.equalsIgnoreCase("Number")) {
            try {
                Result = Long.parseLong(Data);
            } catch (Exception ex) {
                Result = "变量值：" + Data + " 不是数字类型，请检查！";
            }
        }
        if (ValueType.equalsIgnoreCase("Json")) {
            try {
                Result = JSON.parse(Data);
            } catch (Exception ex) {
                Result = "变量值：" + Data + " 不是数字类型，请检查！";
            }
        }
        if (ValueType.equalsIgnoreCase("String") || ValueType.isEmpty()) {
            Result = Data;
        }
        if (ValueType.equalsIgnoreCase("Array")) {
            String[] Array = Data.split(",");
            Result = Array;
        }
        if (ValueType.equalsIgnoreCase("Bool")) {
            try {
                Result = Boolean.parseBoolean(Data);
            } catch (Exception ex) {
                Result = "变量值：" + Data + " 不是布尔类型，请检查！";
            }
        }
        return Result;
    }


    private Object GetVariablesDataType(String Value, String PlanId, String BatchName, String Caseid) throws Exception {
        Object Result = "";
        Value = Value.substring(1);

        Testvariables testvariables = testvariablesService.getBy("testvariablesname", Value);
        if (testvariables == null) {
            throw new Exception("未找到变量：" + Value + "绑定的接口用例，请检查变量管理-变量管理中是否存在此变量");
        }
        String ValueType = testvariables.getValuetype();

        Condition con = new Condition(ApicasesVariables.class);
        con.createCriteria().andCondition("variablesname = '" + Value.replace("'", "''") + "'").andCondition(" caseid = " + Caseid);
        List<ApicasesVariables> apicasesVariablesList = apicasesVariablesService.listByCondition(con);
        if (apicasesVariablesList.size() == 0) {
            throw new Exception("未找到变量：" + Value + "绑定的接口用例，请检查变量管理-用例变量中是否存在此变量绑定的接口用例");
        }
        //根据用例参数值是否以$开头，如果是则认为是变量通过变量表取到变量值
        TestvariablesValue testvariablesValue = testvariablesValueService.gettestvariablesvalue(Long.parseLong(PlanId), Long.parseLong(Caseid), Value, BatchName);
        if (testvariablesValue != null) {
            String VariablesNameValue = testvariablesValue.getVariablesvalue();
            Result = GetDataByType(VariablesNameValue, ValueType);
        } else {
            throw new Exception("未找到变量：" + Value + "的值，请检查变量管理-变量结果中是否存在此变量值");
        }
        return Result;
    }

    //功能用例平均分配
    public List<List<Dispatch>> FunctionDispatch(int Jmeternums, List<Dispatch> dispatchList) {
        if (dispatchList.size() < Jmeternums) {
            Jmeternums = dispatchList.size();
        }
        List<List<Dispatch>> LastDispatchList = new ArrayList<List<Dispatch>>();
        List<Dispatch> splitdispatchList;
        int sizemode = (dispatchList.size()) / Jmeternums;
        int sizeleft = (dispatchList.size()) % Jmeternums;
        int j = 0;
        int x = 0;
        for (int i = 0; i < Jmeternums; i++) {
            splitdispatchList = new ArrayList<Dispatch>();
            for (j = x; j < (sizemode + x); j++) {
                Dispatch dis = dispatchList.get(j);
                splitdispatchList.add(dis);
            }
            x = j;
            LastDispatchList.add(splitdispatchList);
        }
        if (sizeleft != 0) {
            for (int y = 1; y < sizeleft + 1; y++) {
                Dispatch dis = dispatchList.get(dispatchList.size() - y);
                LastDispatchList.get(LastDispatchList.size() - 1).add(dis);
            }
        }
        return LastDispatchList;
    }

    public int GetJmeterProcess(String DictionaryCode, String DicType) {
        List<Dictionary> slavermaxfunthreaddic = dictionaryMapper.findDicNameValueWithCode(DictionaryCode);

        int JmeterProcess = 1;
        //字典表未配置，默认取一条
        if (slavermaxfunthreaddic.size() == 0) {
            TestPlanCaseController.log.info("功能任务-字典表未配置" + DicType + "slaver并发执行jmerter进程个数，默认为1");
        } else {
            String slavermaxthread = slavermaxfunthreaddic.get(0).getDicitmevalue();
            try {
                JmeterProcess = Integer.valueOf(slavermaxthread);
            } catch (Exception ex) {
                TestPlanCaseController.log.error("功能任务-字典表未正确配置" + DicType + "slaver并发执行jmerter进程个数，默认为1");
            }
            TestPlanCaseController.log.info("功能任务-获取字典表slaver并发执行jmerter进程个数：。。。。。。。。" + slavermaxthread);
        }
        return JmeterProcess;
    }

    public HashMap<String, List<Dispatch>> GetProtocolDispatch(List<Dispatch> dispatchList) {
//    public  List<Dispatch> GetProtocolDispatch(List<Dispatch> dispatchList) {
        List<Dispatch> dispatchResultList = new ArrayList<>();
        HashMap<Long, List<Dispatch>> GroupDispatch = new HashMap<Long, List<Dispatch>>();

        //获取计划id分组中的第一组列表
        for (Dispatch dispatch : dispatchList) {
            Long planid = dispatch.getExecplanid();
            if (!GroupDispatch.containsKey(planid)) {
                List<Dispatch> dispatchListtmp = new ArrayList<>();
                dispatchListtmp.add(dispatch);
                GroupDispatch.put(planid, dispatchListtmp);
            } else {
                GroupDispatch.get(planid).add(dispatch);
            }
        }
        for (Long planid : GroupDispatch.keySet()) {
            dispatchResultList = GroupDispatch.get(planid);
            break;
        }

//        //获取微服务分组列表
//        HashMap<String, List<Dispatch>> DeployUnitGroupDispatch = new HashMap<String, List<Dispatch>>();
//        for (Dispatch dispatch : dispatchResultList) {
//            String DeployUnit = dispatch.getDeployunitname();
//            if (!DeployUnitGroupDispatch.containsKey(DeployUnit)) {
//                List<Dispatch> dispatchListtmp = new ArrayList<>();
//                dispatchListtmp.add(dispatch);
//                DeployUnitGroupDispatch.put(DeployUnit, dispatchListtmp);
//            } else {
//                DeployUnitGroupDispatch.get(DeployUnit).add(dispatch);
//            }
//        }
//
//        //合并协议列表
        HashMap<String, List<Dispatch>> ProtocolGroupDispatch = new HashMap<String, List<Dispatch>>();
//
//        for (String DeployUnit : DeployUnitGroupDispatch.keySet()) {
//            Deployunit deployunit = deployunitService.findDeployNameValueWithCode(DeployUnit);
//            String Protocal = deployunit.getProtocal();
//            if (Protocal.equalsIgnoreCase("http") || Protocal.equalsIgnoreCase("https")) {
//                ProtocolGroupDispatch = MergeCaseList(ProtocolGroupDispatch, DeployUnitGroupDispatch, DeployUnit, "http");
//            }
//            if (Protocal.equalsIgnoreCase("rpc")) {
//                ProtocolGroupDispatch = MergeCaseList(ProtocolGroupDispatch, DeployUnitGroupDispatch, DeployUnit, "rpc");
//            }
//        }
        ProtocolGroupDispatch.put("http", dispatchResultList);
        return ProtocolGroupDispatch;
    }

    public HashMap<String, List<Dispatch>> MergeCaseList(HashMap<String, List<Dispatch>> ProtocolGroupDispatch, HashMap<String, List<Dispatch>> DeployUnitGroupDispatch, String DeployUnit, String Protocol) {
        HashMap<String, List<Dispatch>> ProtocolGroupResultDispatch = ProtocolGroupDispatch;
        if (!ProtocolGroupResultDispatch.containsKey(Protocol)) {
            List<Dispatch> dispatchListtmp = new ArrayList<>();
            for (Dispatch dis : DeployUnitGroupDispatch.get(DeployUnit)) {
                dispatchListtmp.add(dis);
            }
            ProtocolGroupResultDispatch.put(Protocol, dispatchListtmp);
        } else {
            for (Dispatch dis : DeployUnitGroupDispatch.get(DeployUnit)) {
                ProtocolGroupResultDispatch.get(Protocol).add(dis);
            }
        }
        return ProtocolGroupResultDispatch;
    }


}

