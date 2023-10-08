package com.zoctan.api.controller;

import com.alibaba.fastjson.JSON;
import com.zoctan.api.core.Scheduled.FunctionDispatchScheduleTask;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.core.service.HttpHeader;
import com.zoctan.api.core.service.Httphelp;
import com.zoctan.api.dto.Testplanandbatch;
import com.zoctan.api.entity.*;
import com.zoctan.api.mapper.*;
import com.zoctan.api.service.ExecuteplanService;
import com.zoctan.api.service.ExecuteplanbatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private ExecuteplanbatchMapper executeplanbatchMapper;
    @Autowired
    private ExecuteplanTestcaseMapper executeplanTestcaseMapper;
    @Autowired
    private ExecuteplanMapper executeplanMapper;
    @Autowired
    private SlaverMapper slaverMapper;
    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private ExecuteplanService executeplanService;

    @Resource
    private ExecuteplanbatchService executeplanbatchService;


    @PostMapping("/exec")
    public Result exec(@RequestBody Testplanandbatch planbatch) throws Exception {
        TestPlanCaseController.log.info("开始保存调度用例。。。。。。。。。。。。。。。。。。。。。。。。");
        Long execplanid = planbatch.getPlanid();
        String batchname = planbatch.getBatchname();
        Executeplanbatch epb = executeplanbatchMapper.getbatchidbyplanidandbatchname(execplanid, batchname);
        if(epb==null)
        {
            return ResultGenerator.genOkResult(" 未找到执行批次名： "+batchname);
        }
        Executeplan ep = executeplanMapper.findexplanWithid(execplanid);
        String Creator=ep.getCreator();
        if(ep==null)
        {
            return ResultGenerator.genOkResult("未找到此测试集合：" + execplanid);
        }
        List<ExecuteplanTestcase> caselist = executeplanTestcaseMapper.findcasebytestplanid(execplanid,ep.getUsetype());
        TestPlanCaseController.log.info("测试集合id" + execplanid+" 批次为："+batchname+" 获取用例数："+caselist.size());

        if (caselist.size() == 0) {
            return ResultGenerator.genOkResult("此测试集合:" + ep.getExecuteplanname()+" 还没用例，请先装载用例");
        }
        //获取对应计划类型的所有slaver
        //List<Slaver> slaverlist = slaverMapper.findslaverbytype(ep.getUsetype());
        List<Slaver> slaverlist = slaverMapper.findslaveralive(ep.getUsetype(),"已下线");

        //增加检测slaver是否正常，在salver的control做个检测的请求返回
        //slaverlist=GetAliveSlaver(slaverlist);
        List<List<Dispatch>> dispatchList=new ArrayList<>();
        if (slaverlist.size() == 0) {
            TestPlanCaseController.log.info("没有类型"+ep.getUsetype()+"的可用的执行机，请检查slaverservice是否在运行");
            return ResultGenerator.genOkResult("没有类型"+ep.getUsetype()+"的可用的执行机，请检查slaverservice是否在运行");
        } else {
            if(ep.getUsetype().equals("功能"))
            {
                if(ep.getRunmode().equalsIgnoreCase("单机运行"))
                {
                    List<Slaver> singleslaverlist=new ArrayList<>();
                    singleslaverlist.add(slaverlist.get(0));
                    dispatchList=FunctionDispatch(singleslaverlist,caselist,ep,epb);
                    TestPlanCaseController.log.info("单机运行slaver："+slaverlist.get(0).getSlavername());

                }
                if(ep.getRunmode().equalsIgnoreCase("多机并行")||ep.getRunmode().equalsIgnoreCase("多机执行"))
                {
                    TestPlanCaseController.log.info("多机并行slaver：");
                    dispatchList=FunctionDispatch(slaverlist,caselist,ep,epb);
                }
            }
            if(ep.getUsetype().equals("性能"))
            {
                dispatchList=PerformanceDispatch(slaverlist,caselist,ep,epb);
            }
        }
        for (List<Dispatch> li:dispatchList) {
            dispatchMapper.insertBatchDispatch(li);
            TestPlanCaseController.log.info("保存成功调度用例条数：" + li.size());
        }
        TestPlanCaseController.log.info("完成保存调度用例。。。。。。。。。。。。。。。。。。。。。。。。");
        return ResultGenerator.genOkResult();
    }


    @PostMapping("/TestPlanRun")
    public Result TestPlanRun(@RequestBody final Map<String, Object> param) throws UnsupportedEncodingException {
        String TestPlanName=param.get("TestCollectionName").toString();
        String BatchName=param.get("TestPlan").toString();
        String Source=param.get("Source").toString();
        long PlanID;
        Executeplan executeplan= executeplanService.getBy("executeplanname",TestPlanName);
        List<ExecuteplanTestcase> caselist=new ArrayList<>();
        Executeplanbatch executeplanbatch=new Executeplanbatch();
        if (executeplan != null)
        {
            PlanID=executeplan.getId();
            caselist = executeplanTestcaseMapper.findcasebytestplanid(PlanID,executeplan.getUsetype());
            TestPlanCaseController.log.info("测试集合id" + PlanID+" 批次为："+BatchName+" 获取用例数："+caselist.size());
            Condition con=new Condition(Executeplanbatch.class);
            con.createCriteria().andCondition("batchname = '" + BatchName + "'")
                    .andCondition("executeplanid = " + PlanID);
            if(executeplanbatchService.ifexist(con)>0)
            {
                return ResultGenerator.genFailedResult("该测试集合下已经存在此执行计划");
            }
            else {
                executeplanbatch.setStatus("待执行");
                executeplanbatch.setSource(Source);
                executeplanbatch.setBatchname(BatchName);
                executeplanbatch.setExecuteplanid(PlanID);
                executeplanbatch.setExecuteplanname(TestPlanName);
                executeplanbatchService.save(executeplanbatch);

                if (caselist.size() == 0) {
                    return ResultGenerator.genOkResult("此测试集合:" + executeplan.getExecuteplanname()+" 还没用例，请先装载用例");
                }
                else
                {
                    //获取对应计划类型的所有slaver
                    List<Slaver> slaverlist = slaverMapper.findslaveralive(executeplan.getUsetype(),"已下线");
                    //增加检测slaver是否正常，在salver的control做个检测的请求返回
                    List<List<Dispatch>> dispatchList=new ArrayList<>();
                    if (slaverlist.size() == 0) {
                        TestPlanCaseController.log.info("没有类型为"+executeplan.getUsetype()+"的可用的测试执行机，请联系AutoMeter管理员");
                        return ResultGenerator.genOkResult("没有类型为"+executeplan.getUsetype()+"的可用的测试执行机，请联系AutoMeter管理员");
                    } else {
                        if(executeplan.getUsetype().equals("功能"))
                        {
                            if(executeplan.getRunmode().equalsIgnoreCase("单机运行"))
                            {
                                List<Slaver> singleslaverlist=new ArrayList<>();
                                singleslaverlist.add(slaverlist.get(0));
                                dispatchList=FunctionDispatch(singleslaverlist,caselist,executeplan,executeplanbatch);
                                TestPlanCaseController.log.info("单机运行slaver："+slaverlist.get(0).getSlavername());

                            }
                            if(executeplan.getRunmode().equalsIgnoreCase("多机并行")||executeplan.getRunmode().equalsIgnoreCase("多机执行"))
                            {
                                TestPlanCaseController.log.info("多机并行slaver：");
                                dispatchList=FunctionDispatch(slaverlist,caselist,executeplan,executeplanbatch);
                            }
                        }
                        if(executeplan.getUsetype().equals("性能"))
                        {
                            dispatchList=PerformanceDispatch(slaverlist,caselist,executeplan,executeplanbatch);
                        }
                    }
                    for (List<Dispatch> li:dispatchList) {
                        dispatchMapper.insertBatchDispatch(li);
                        TestPlanCaseController.log.info("保存成功调度用例条数：" + li.size());
                    }
                    TestPlanCaseController.log.info("完成保存调度用例。。。。。。。。。。。。。。。。。。。。。。。。");
                    return ResultGenerator.genOkResult();
                }
            }
        }
        else
        {
            return ResultGenerator.genFailedResult("未找到此测试计划："+TestPlanName);
        }
    }



    //功能caselist平均分配给slaverlist，多余的分给最后一个list
    public  List<List<Dispatch>> FunctionDispatch(List<Slaver> slaverlist,List<ExecuteplanTestcase> caselist,Executeplan ep,Executeplanbatch epb)
    {
        int slavernums=slaverlist.size();
        if(caselist.size()<slavernums)
        {
            slavernums=caselist.size();
        }
        List<List<Dispatch>> LastDispatchList = new ArrayList<List<Dispatch>>();
        List<Dispatch> splitdispatchList;
        int sizemode = (caselist.size()) / slavernums;
        int sizeleft = (caselist.size()) % slavernums;
        int j = 0;
        int x = 0;
        for (int i = 0; i < slavernums; i++) {
            splitdispatchList = new ArrayList<Dispatch>();
            for (j = x; j < (sizemode + x); j++) {
                Long slaverid=slaverlist.get(i).getId();
                String slavername=slaverlist.get(i).getSlavername();
                ExecuteplanTestcase testcase=caselist.get(j);
                Dispatch dis =getdispatch(slaverid,slavername,testcase,ep,epb,testcase.getThreadnum(),testcase.getLoops());
                splitdispatchList.add(dis);
            }
            x = j;
            LastDispatchList.add(splitdispatchList);
        }
        if (sizeleft != 0) {
            for (int y = 1; y < sizeleft + 1; y++) {
                Long slaverid=slaverlist.get(slavernums-1).getId();
                String slavername=slaverlist.get(slavernums-1).getSlavername();
                ExecuteplanTestcase testcase=caselist.get(caselist.size() - y);
                Dispatch dis =getdispatch(slaverid,slavername,testcase,ep,epb,testcase.getThreadnum(),testcase.getLoops());
                LastDispatchList.get(LastDispatchList.size()-1).add(dis);
            }
        }
        return LastDispatchList;
    }

    public Dispatch getdispatch(Long slaverid,String slavername,ExecuteplanTestcase testcase,Executeplan ep,Executeplanbatch epb,Long ThreadNum,Long Loops)
    {
        Dispatch dis = new Dispatch();
        dis.setExpect(testcase.getExpect());
        dis.setExecplanid(ep.getId());
        dis.setTestcaseid(testcase.getTestcaseid());
        dis.setDeployunitname(testcase.getDeployunitname());
        dis.setStatus("待分配");
        dis.setBatchname(epb.getBatchname());
        dis.setBatchid(epb.getId());
        dis.setCasejmxname(testcase.getCasejmxname());
        dis.setExecplanname(ep.getExecuteplanname());
        dis.setCreator(ep.getCreator());
        dis.setSlaverid(slaverid);
        dis.setSlavername(slavername);
        dis.setTestcasename(testcase.getCasename());
        dis.setPlantype(ep.getUsetype());
        dis.setThreadnum(ThreadNum);
        dis.setLoops(Loops);
        return dis;
    }

    public  List<List<String>> SplitList(List<String> sList, int num) {

        if(sList.size()<num)
        {
            num=sList.size();
        }

        List<List<String>> eList = new ArrayList<List<String>>();
        List<String> gList;

        int size = (sList.size()) / num;
        int size2 = (sList.size()) % num;
        int j = 0;
        int xx = 0;
        for (int i = 0; i < num; i++) {
            gList = new ArrayList<String>();

            for (j = xx; j < (size + xx); j++) {
                gList.add(sList.get(j));
            }
            xx = j;
            eList.add(gList);
        }
        if (size2 != 0) {
            //gList = new ArrayList<String>();
            for (int y = 1; y < size2 + 1; y++) {
                eList.get(eList.size()-1).add(sList.get(sList.size() - y));
            }
        }
        return eList;
    }

    //性能用例拆分线程和循环
    public List<List<Dispatch>> PerformanceDispatch(List<Slaver> slaverlist,List<ExecuteplanTestcase> caselist,Executeplan ep,Executeplanbatch epb)
    {
        int slavernums=slaverlist.size();
        List<List<Dispatch>> LastDispatchList = new ArrayList<List<Dispatch>>();
        List<Dispatch> splitdispatchList=new ArrayList<>();
        for (ExecuteplanTestcase testcase: caselist) {
            Long ThreadNUms=testcase.getThreadnum();
            Long Loops=testcase.getLoops();
            Long Threadmode = ThreadNUms / slavernums;
            Long Loopsmode = Loops / slavernums;
            Long Threadleft = ThreadNUms % slavernums;
            Long Loopleft = Loops % slavernums;
            //拆分每个case线程和循环取模平均分配到slaver
            for(int i=0;i<slaverlist.size();i++)
            {
                Dispatch dis =getdispatch(slaverlist.get(i).getId(),slaverlist.get(i).getSlavername(),testcase,ep,epb,Threadmode,Loopsmode);
                splitdispatchList.add(dis);
            }
            //如果线程数或者循环数取余不为0，则把剩余的都分给最后一个slaver
            if(Threadleft!=0 || Loopleft!=0)
            {
                Dispatch dis=splitdispatchList.get(slaverlist.size()-1);
                dis.setThreadnum(dis.getThreadnum()+Threadleft);
                dis.setLoops(dis.getLoops()+Loopleft);
                splitdispatchList.set(slaverlist.size()-1,dis);
            }
        }
        LastDispatchList.add(splitdispatchList);
        return LastDispatchList;
    }


    public List<Slaver> GetAliveSlaver(List<Slaver> SlaverList)
    {
        List<Slaver> AliveList=new ArrayList<>();
        for (Slaver slaver:SlaverList) {
            String IP=slaver.getIp();
            String Port=slaver.getPort();
            String ServerUrl = "http://" + IP + ":" + Port + "/exectestplancase/test";
            ExecuteplanTestcase plancase=new ExecuteplanTestcase();
            String params = JSON.toJSONString(plancase);
            HttpHeader header = new HttpHeader();
            String respon="";
            try {
                 respon = Httphelp.doPost(ServerUrl, params, header, 5000);
            } catch (Exception e) {
                respon=e.getMessage();
            }
            if(respon.contains("200"))
            {
                AliveList.add(slaver);
            }
        }
        return AliveList;
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

    @GetMapping("/test")
    public Result gettest(@RequestBody ExecuteplanTestcase plancase) {
        //tpcservice.executeplancase(plancase.getExecuteplanid(),plancase.getTestcaseid());
        return ResultGenerator.genOkResult();
    }

}
