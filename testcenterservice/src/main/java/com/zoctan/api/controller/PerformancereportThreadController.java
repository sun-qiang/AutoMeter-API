package com.zoctan.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.StaticsDataForLine;
import com.zoctan.api.entity.PerformancereportCaseinfo;
import com.zoctan.api.entity.PerformancereportThread;
import com.zoctan.api.service.PerformancereportCaseinfoService;
import com.zoctan.api.service.PerformancereportThreadService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Condition;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SeasonFan
 * @date 2024/07/29
 */
@RestController
@RequestMapping("/performancereport/thread")
public class PerformancereportThreadController {
    @Resource
    private PerformancereportThreadService performancereportThreadService;
    @Resource

    private PerformancereportCaseinfoService performancereportCaseinfoService;


    @PostMapping
    public Result add(@RequestBody PerformancereportThread performancereportThread) {
        performancereportThreadService.save(performancereportThread);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        performancereportThreadService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody PerformancereportThread performancereportThread) {
        performancereportThreadService.update(performancereportThread);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        PerformancereportThread performancereportThread = performancereportThreadService.getById(id);
        return ResultGenerator.genOkResult(performancereportThread);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<PerformancereportThread> list = performancereportThreadService.listAll();
        PageInfo<PerformancereportThread> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final PerformancereportThread recipe) {
        performancereportThreadService.updateDic(recipe);
        return ResultGenerator.genOkResult();
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<PerformancereportThread> list = performancereportThreadService.findDicWithName(param);
        final PageInfo<PerformancereportThread> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


    @PostMapping("/gettimedatas")
    public Result gettimedatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportThread.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'Threads'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportThread> performancereportThreadList = performancereportThreadService.listByCondition(percon);

        Map<Long, Double> mapData = new HashMap<>();
        LinkedList<Long> ids = new LinkedList<>();
        for (PerformancereportThread thread : performancereportThreadList) {
            String Content = thread.getContent();

            JSONObject resultJson = JSONObject.parseObject(Content);
            JSONObject resultObj = resultJson.getJSONObject("result");
            JSONArray seriesArray = resultObj.getJSONArray("series");
            JSONObject seriesObj = seriesArray.getJSONObject(0);
            JSONArray data = seriesObj.getJSONArray("data");

            for (int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                long key = item.getLong(0);
                double value = item.getDouble(1);
                ids.add(key);
            }

        }
        Collections.sort(ids);
        LinkedList<String> listres = new LinkedList<>();
        for (Long key : ids) {
            Date date = new Date(key);
            // 格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = sdf.format(date);
            listres.add(formattedDate);

        }
        return ResultGenerator.genOkResult(listres);
    }

    @PostMapping("/gettimethreaddatas")
    public Result gettimethreaddatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportThread.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'Threads'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportThread> performancereportThreadList = performancereportThreadService.listByCondition(percon);

        Map<Long, Double> mapData = new HashMap<>();
        LinkedList<Long> ids = new LinkedList<>();
        for (PerformancereportThread thread : performancereportThreadList) {
            String Content = thread.getContent();

            JSONObject resultJson = JSONObject.parseObject(Content);
            JSONObject resultObj = resultJson.getJSONObject("result");
            JSONArray seriesArray = resultObj.getJSONArray("series");
            JSONObject seriesObj = seriesArray.getJSONObject(0);
            JSONArray data = seriesObj.getJSONArray("data");

            for (int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                long key = item.getLong(0);
                double value = item.getDouble(1);
                ids.add(key);
                mapData.put(key, value);
            }

        }
        Collections.sort(ids);
        LinkedList<Double> listres = new LinkedList<>();
        for (Long key : ids) {
            listres.add(mapData.get(key));

        }
        List<StaticsDataForLine> staticsDataForLineList = new ArrayList<>();
        StaticsDataForLine staticsDataForLine = new StaticsDataForLine();
        staticsDataForLine.setExecPlanName("并发线程");
        staticsDataForLine.setPassPecent(listres);
        staticsDataForLineList.add(staticsDataForLine);
        return ResultGenerator.genOkResult(staticsDataForLineList);
    }


    @PostMapping("/getresponetimexdatas")
    public Result getresponetimexdatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportCaseinfo.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'ResponeTimesOver'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportCaseinfo> performancereportCaseinfoList = performancereportCaseinfoService.listByCondition(percon);

        LinkedList<Long> ids = new LinkedList<>();

        if (performancereportCaseinfoList.size() > 0) {
            PerformancereportCaseinfo perf = performancereportCaseinfoList.get(0);
            String casename = performancereportCaseinfoList.get(0).getCasename();
            JSONObject resultJson = JSONObject.parseObject(perf.getContent());
            JSONArray seriesArray = resultJson.getJSONObject("result").getJSONArray("series");

            for (int i = 0; i < seriesArray.size(); i++) {
                JSONObject seriesObj = seriesArray.getJSONObject(i);
                if (casename.equals(seriesObj.getString("label"))) {
                    JSONArray data = seriesObj.getJSONArray("data");
                    for (int ii = 0; ii < data.size(); ii++) {
                        JSONArray item = data.getJSONArray(ii);
                        long key = item.getLong(0);
                        ids.add(key);
                    }
                    System.out.println(data);
                    break; // 找到后可选择退出循环
                }
            }
        }
        Collections.sort(ids);
        LinkedList<String> listres = new LinkedList<>();
        for (Long key : ids) {
            Date date = new Date(key);
            // 格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = sdf.format(date);
            listres.add(formattedDate);

        }
        return ResultGenerator.genOkResult(listres);
    }

    @PostMapping("/getresponetimeydatas")
    public Result getresponetimeydatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportCaseinfo.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'ResponeTimesOver'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportCaseinfo> performancereportCaseinfoList = performancereportCaseinfoService.listByCondition(percon);

        List<StaticsDataForLine> staticsDataForLineList = new ArrayList<>();
        for (PerformancereportCaseinfo thread : performancereportCaseinfoList) {
            String Content = thread.getContent();
            JSONObject resultJson = JSONObject.parseObject(Content);
            JSONArray seriesArray = resultJson.getJSONObject("result").getJSONArray("series");
            StaticsDataForLine staticsDataForLine = new StaticsDataForLine();
            staticsDataForLine.setExecPlanName(thread.getCasename());
            List<Double> ids = new ArrayList<>();

            for (int i = 0; i < seriesArray.size(); i++) {
                JSONObject seriesObj = seriesArray.getJSONObject(i);
                if (thread.getCasename().equals(seriesObj.getString("label"))) {
                    JSONArray data = seriesObj.getJSONArray("data");
                    for (int ii = 0; ii < data.size(); ii++) {
                        JSONArray item = data.getJSONArray(ii);
                        double value = item.getDouble(1);
                        ids.add(value);
                    }
                    break; // 找到后可选择退出循环
                }
            }
            staticsDataForLine.setPassPecent(ids);
            staticsDataForLineList.add(staticsDataForLine);
        }
        return ResultGenerator.genOkResult(staticsDataForLineList);
    }

    @PostMapping("/gettpsxdatas")
    public Result gettpsxdatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportCaseinfo.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'TPS'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportCaseinfo> performancereportCaseinfoList = performancereportCaseinfoService.listByCondition(percon);

        LinkedList<Long> ids = new LinkedList<>();

        if (performancereportCaseinfoList.size() > 0) {
            PerformancereportCaseinfo perf = performancereportCaseinfoList.get(0);
            String casename = performancereportCaseinfoList.get(0).getCasename();
            String successcasename = casename + "-success";
            String failcasename = casename + "-failure";
            JSONObject resultJson = JSONObject.parseObject(perf.getContent());
            JSONArray seriesArray = resultJson.getJSONObject("result").getJSONArray("series");

            for (int i = 0; i < seriesArray.size(); i++) {
                JSONObject seriesObj = seriesArray.getJSONObject(i);
                if ((successcasename.equals(seriesObj.getString("label"))) || (failcasename.equals(seriesObj.getString("label")))) {
                    JSONArray data = seriesObj.getJSONArray("data");
                    for (int ii = 0; ii < data.size(); ii++) {
                        JSONArray item = data.getJSONArray(ii);
                        long key = item.getLong(0);
                        ids.add(key);
                    }
                    System.out.println(data);
                    break; // 找到后可选择退出循环
                }
            }
        }
        Collections.sort(ids);
        LinkedList<String> listres = new LinkedList<>();
        for (Long key : ids) {
            Date date = new Date(key);
            // 格式化日期
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = sdf.format(date);
            listres.add(formattedDate);

        }
        return ResultGenerator.genOkResult(listres);
    }

    @PostMapping("/gettpsydatas")
    public Result gettpsydatas(@RequestBody final Map<String, Object> param) {
        long testplanid = Long.parseLong(param.get("testplanid").toString());
        long testscenenid = Long.parseLong(param.get("testscenenid").toString());
        String batchname = param.get("batchname").toString();

        Condition percon = new Condition(PerformancereportCaseinfo.class);
        percon.createCriteria().andCondition("execplanid = " + testplanid)
                .andCondition("batchname = '" + batchname + "'")
                .andCondition("contenttype = 'TPS'")
                .andCondition("testsceneid =" + testscenenid);
        List<PerformancereportCaseinfo> performancereportCaseinfoList = performancereportCaseinfoService.listByCondition(percon);

        List<StaticsDataForLine> staticsDataForLineList = new ArrayList<>();
        List<StaticsDataForLine> staticsDataForLineListResult = new ArrayList<>();

        for (PerformancereportCaseinfo thread : performancereportCaseinfoList) {
            String Content = thread.getContent();
            String casename = thread.getCasename();
            String successcasename = casename + "-success";
            String failcasename = casename + "-failure";
            JSONObject resultJson = JSONObject.parseObject(Content);
            JSONArray seriesArray = resultJson.getJSONObject("result").getJSONArray("series");
            StaticsDataForLine staticsDataForLine = new StaticsDataForLine();
            staticsDataForLine.setExecPlanName(thread.getCasename());
            List<Double> ids = new ArrayList<>();

            for (int i = 0; i < seriesArray.size(); i++) {
                JSONObject seriesObj = seriesArray.getJSONObject(i);
                if ((successcasename.equals(seriesObj.getString("label"))) || (failcasename.equals(seriesObj.getString("label")))) {
                    JSONArray data = seriesObj.getJSONArray("data");
                    for (int ii = 0; ii < data.size(); ii++) {
                        JSONArray item = data.getJSONArray(ii);
                        double value = item.getDouble(1);
                        ids.add(value);
                    }
                    break; // 找到后可选择退出循环
                }
            }
            staticsDataForLine.setPassPecent(ids);
            staticsDataForLineList.add(staticsDataForLine);
        }

        List<String> tpsNameList = new ArrayList<>();
        for (StaticsDataForLine staticsDataForLine : staticsDataForLineList) {
            if (!tpsNameList.contains(staticsDataForLine.getExecPlanName())) {
                tpsNameList.add(staticsDataForLine.getExecPlanName());
            }
        }
        for (String tpsname : tpsNameList) {
            StaticsDataForLine staticsDataForLinelast = new StaticsDataForLine();
            List<List<Double>> allLists = new ArrayList<>();
            for (StaticsDataForLine staticsDataForLine : staticsDataForLineList) {
                if (tpsname.equals(staticsDataForLine.getExecPlanName())) {
                    staticsDataForLinelast.setExecPlanName(staticsDataForLine.getExecPlanName());
                    List<Double> tmp = staticsDataForLine.getPassPecent();
                    allLists.add(tmp);
                }
            }
            List<Double> result = new ArrayList<>(allLists.get(0).size());
            for (int i = 0; i < allLists.get(0).size(); i++) {
                double sum = 0;
                for (List<Double> list : allLists) {
                    sum += list.get(i);
                }
                result.add(sum);
            }
            staticsDataForLinelast.setPassPecent(result);
            staticsDataForLineListResult.add(staticsDataForLinelast);
        }

        return ResultGenerator.genOkResult(staticsDataForLineListResult);
    }


}
