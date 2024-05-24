package com.api.autotest.core;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * Created by fanseasn on 2020/12/8.
 */
/*
 @author Season
 @DESCRIPTION 
 @create 2020/12/8
*/
public class precondition extends AbstractJavaSamplerClient {
    // 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行，类似于LoadRunner中的init方法
    public void setupTest(JavaSamplerContext context) {
        super.setupTest(context);
        getLogger().info( "precondition setupTest 。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。:" );
//        String status="";
//        TestCore core = new TestCore(context,getLogger());
//        String errorinfo = "";
//        String caseid = context.getParameter("caseid");
//        String testplanid = context.getParameter("testplanid");
//        String batchid = context.getParameter("batchid");
//        String batchname = context.getParameter("batchname");
//        String slaverid = context.getParameter("slaverid");
//        String casetype = context.getParameter("casetype");
//        //前置条件
//        try {
//            core.fixprecondition(testplanid,caseid);
//            status="成功";
//        } catch (Exception e) {
//            status="失败";
//            errorinfo ="前置条件处理异常："+ e.getMessage().replace("'","");
//        }
//        finally {
//           String result= core.savetestcaseconditionresult(caseid,testplanid,batchid,batchname,slaverid,status,errorinfo,"前置",casetype);
//           getLogger().info(TestCore.logplannameandcasename + "处理前置条件完成:"+result);
//        }
    }

    // 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        //定义一个参数，显示到Jmeter的参数列表中，第一个参数为参数默认的显示名称，第二个参数为默认值
        params.addArgument("testplanid", "12");
        params.addArgument("caseid", "1");
        params.addArgument("batchid", "41");
        params.addArgument("slaverid", "5");
        params.addArgument("batchname", "xxxxxxxxxxxxxxxxxxxxx");
        params.addArgument("casetype", "性能");
        params.addArgument("mysqlurl", "jdbc:mysql://127.0.0.1:3306/testcenter?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        params.addArgument("mysqlusername", "root");
        params.addArgument("mysqlpassword", "root");
        return params;
    }

    // 测试执行的循环体，根据线程数和循环次数的不同可执行多次，类似于LoadRunner中的Action方法
    public SampleResult runTest(JavaSamplerContext ctx) {
//        SampleResult results = new SampleResult();
//        results.sampleEnd();
        return null;
    }

    //结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行，类似于LoadRunner中的end方法
    public void teardownTest(JavaSamplerContext ctx) {
        super.teardownTest(ctx);
    }

}
