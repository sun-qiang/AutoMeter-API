package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.StaticsDataForLine;
import com.zoctan.api.entity.Executeplan;
import com.zoctan.api.entity.StaticsDeployunitandcases;
import com.zoctan.api.service.StaticsDeployunitandcasesService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SeasonFan
 * @date 2021/04/15
 */
@RestController
@RequestMapping("/statics/deployunitandcases")
public class StaticsDeployunitandcasesController {
    @Resource
    private StaticsDeployunitandcasesService staticsDeployunitandcasesService;

    @PostMapping
    public Result add(@RequestBody StaticsDeployunitandcases staticsDeployunitandcases) {
        staticsDeployunitandcasesService.save(staticsDeployunitandcases);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        staticsDeployunitandcasesService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody StaticsDeployunitandcases staticsDeployunitandcases) {
        staticsDeployunitandcasesService.update(staticsDeployunitandcases);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        StaticsDeployunitandcases staticsDeployunitandcases = staticsDeployunitandcasesService.getById(id);
        return ResultGenerator.genOkResult(staticsDeployunitandcases);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<StaticsDeployunitandcases> list = staticsDeployunitandcasesService.listAll();
        PageInfo<StaticsDeployunitandcases> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @GetMapping("/getdeployunitstatics")
    public Result getplanstatics(@RequestParam long projectid,@RequestParam long deployratelimit) {
        List<String> lastdaylist = new ArrayList<>();
        for (int i = 15; i > 0; i--) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -i);
            int Year = calendar.get(Calendar.YEAR);
            int MONTH = calendar.get(Calendar.MONTH) + 1;
            int Day = calendar.get(Calendar.DAY_OF_MONTH);
            String MONTHS = String.valueOf(MONTH);
            String DayS = String.valueOf(Day);;

            if (MONTH < 10) {
                MONTHS = "0" + MONTH;
            }
            if (Day < 10) {
                DayS = "0" + Day;
            }
            lastdaylist.add(Year + "-" + MONTHS + "-" + DayS + " 00:00:00");
        }

//        Condition con = new Condition(StaticsDeployunitandcases.class);
//        con.createCriteria().andCondition("projectid = " + projectid)
//                .andCondition("statics_date>='" + lastdaylist.get(0) + "'")
//                .andCondition("statics_date<='" + lastdaylist.get(14) + "'");
//        con.orderBy("passrate desc");
//        List<StaticsDeployunitandcases> listrange = staticsDeployunitandcasesService.listByCondition(con);//.listAll();
        List<StaticsDeployunitandcases> listrange = staticsDeployunitandcasesService.getdeployunitpassratestatics(lastdaylist.get(0),lastdaylist.get(14),projectid,deployratelimit);//.listAll();



        List<String> depList = new ArrayList<>();
        for (StaticsDeployunitandcases s : listrange) {
            if (!depList.contains(s.getDeployunitname())) {
                depList.add(s.getDeployunitname());
            }
        }
        List<StaticsDataForLine> staticsDataForLineList = new ArrayList<>();
        for (String depststis : depList) {
            HashMap<String, List<Double>> tmp = new HashMap<>();
            List<Double> planstaticsdatelist = new ArrayList<>();
            tmp.put(depststis, planstaticsdatelist);
            for (String statisdate : lastdaylist) {
                StaticsDeployunitandcases tmpstatis = existornot(statisdate, depststis, listrange);
                if (tmpstatis == null) {
                    tmp.get(depststis).add(0.0);
                } else {
                    tmp.get(depststis).add(tmpstatis.getPassrate());
                }


//                Condition con = new Condition(StaticsDeployunitandcases.class);
//                con.createCriteria().andCondition("projectid = " + projectid)
//                        .andCondition("statics_date=" + statisdate + "'");
//                List<StaticsDeployunitandcases> list = staticsDeployunitandcasesService.listByCondition(con);//.listAll();
//                HashMap<String, List<Double>> tmp = new HashMap<>();
//                for (StaticsDeployunitandcases staticsDeployunitandcases : list) {
//                    if (!tmp.containsKey(staticsDeployunitandcases.getDeployunitname())) {
//                        List<Double> planstaticsdatelist = new ArrayList<>();
//                        planstaticsdatelist.add(staticsDeployunitandcases.getPassrate());
//                        tmp.put(staticsDeployunitandcases.getDeployunitname(), planstaticsdatelist);
//                    } else {
//                        tmp.get(staticsDeployunitandcases.getDeployunitname()).add(staticsDeployunitandcases.getPassrate());
//                    }
//                }
//                for (String PlanName : tmp.keySet()) {
//                    StaticsDataForLine staticsDataForLine = new StaticsDataForLine();
//                    staticsDataForLine.setExecPlanName(PlanName);
//                    staticsDataForLine.setPassPecent(tmp.get(PlanName));
//                    staticsDataForLineList.add(staticsDataForLine);
//                }
            }
            StaticsDataForLine staticsDataForLine = new StaticsDataForLine();
            staticsDataForLine.setExecPlanName(depststis);
            staticsDataForLine.setPassPecent(tmp.get(depststis));
            staticsDataForLineList.add(staticsDataForLine);
        }


        return ResultGenerator.genOkResult(staticsDataForLineList);
    }

    private StaticsDeployunitandcases existornot(String day, String deploname, List<StaticsDeployunitandcases> range) {
        StaticsDeployunitandcases reeturn = null;
        for (StaticsDeployunitandcases ss : range) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(ss.getStaticsDate());
            if (formattedDate.equals(day) && ss.getDeployunitname().equals(deploname)) {
                reeturn = ss;
                break;
            }

        }
        return reeturn;
    }
}
