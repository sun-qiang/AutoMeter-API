package com.api.autotest.common.utils;

import java.io.IOException;

/**
 * Created by harvey.xu on 2017/12/6.
 */
public class ReportHelper {

//    public static void performanceReportHandler(AutoTestPerformanceTaskModel autoTestPerformanceTaskModel) throws IOException, InterruptedException {
//        String batchNo = autoTestPerformanceTaskModel.getBatchNo();
//        String serviceName = autoTestPerformanceTaskModel.getServiceName();
//        String methodName = autoTestPerformanceTaskModel.getMethodName();
//        String createDate = DateUtil.date2StringFormat(new Date(), "yyyyMMdd");
//        String localReportPath = "/home/wuser/production/apps/ifpaymock/webapps/ROOT/frontend/report/performance/"
//                + serviceName + "/" + methodName
//                + "/" + createDate + "/";
//        System.out.println("localReportPath = " + localReportPath);
//
//        boolean isExist;
//        int i = 0;
//        while (i < 10) {
//            isExist = FileUtils.checkExist(localReportPath + batchNo + ".zip");
//            if (isExist) {
//                System.out.println(localReportPath + batchNo + ".zip" + "--文件存在!");
//                // 解压缩
//                String unzipCmd = "cd " + localReportPath + ";" +
//                        "rm -rf " + batchNo + ";" +
//                        "unzip " + batchNo + ".zip;" +
//                        "rm -rf " + batchNo + ".zip";
//                System.out.println("unzipCmd = " + unzipCmd);
//                ShellUtils shellUtils = new ShellUtils();
//                shellUtils.executeShell(unzipCmd);
//                break;
//            } else {
//                System.out.println(localReportPath + batchNo + ".zip" + "--文件不存在!");
//                Thread.sleep(6000);
//                i++;
//            }
//        }
//
//    }

    /**
     * 解析jtl文件获取report参数
     * @param
     * @return
     * @throws IOException
     */
//    public static TestPerformanceTaskDto jmeterReportParser(String filePath) throws IOException {
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            sb.append(line);
//        }
//        reader.close();
//        String statisticsTableStr;
//        String sourceStr = sb.substring(sb.indexOf("{"), sb.lastIndexOf("}") + 1);
//        if (sourceStr != null && sourceStr.contains("statisticsTable")) {
//            statisticsTableStr = (sourceStr.substring(sourceStr.indexOf("createTable($(\"#statisticsTable\"), ") + 35, sourceStr.length())).split(", function")[0];
//        } else {
//            System.out.println("There is no statisticsTable!");
//            return null;
//        }
//        JSONObject statisticsTableJson = JSONObject.parseObject(statisticsTableStr);
//        JSONArray titlesArr = statisticsTableJson.getJSONArray("titles");
//        JSONArray dataArr = statisticsTableJson.getJSONObject("overall").getJSONArray("data");
//
//        Map<String, String> reportMap = new HashMap<>();
//
//        for (int i = 0; i < titlesArr.size(); i++) {
//            reportMap.put(String.valueOf(titlesArr.get(i)), String.valueOf(dataArr.get(i)));
//        }
//        TestPerformanceTaskDto autoTestPerformanceTaskModel = new TestPerformanceTaskDto();
//        autoTestPerformanceTaskModel.setLabel(reportMap.get("Label"));
//        autoTestPerformanceTaskModel.setSamples(reportMap.get("#Samples"));
//        autoTestPerformanceTaskModel.setAverage(reportMap.get("Average response time"));
//        autoTestPerformanceTaskModel.setPct90(reportMap.get("90th pct"));
//        autoTestPerformanceTaskModel.setPct95(reportMap.get("95th pct"));
//        autoTestPerformanceTaskModel.setPct99(reportMap.get("99th pct"));
//        autoTestPerformanceTaskModel.setMin(reportMap.get("Min"));
//        autoTestPerformanceTaskModel.setMax(reportMap.get("Max"));
//        autoTestPerformanceTaskModel.setError(reportMap.get("Error %"));
//        autoTestPerformanceTaskModel.setThroughPut(reportMap.get("Throughput"));
//        return autoTestPerformanceTaskModel;
//    }

    public static void main(String[] args) {
        String filePath = "D:\\workspace\\ifpay_autotest\\ifpay-mock-tools\\src\\main\\webapp\\frontend\\report\\performance\\Finance\\FinanceTradeApi\\20171207\\201712052134433912\\report\\report_1\\content\\js\\dashboard.js";
//        try {
//            //System.out.println(jmeterReportParser(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
