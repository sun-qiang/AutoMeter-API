package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2022/01/03
 */
@RestController
@RequestMapping("/condition/order")
public class ConditionOrderController {
    @Resource
    private ConditionOrderService conditionOrderService;

    @Resource
    private ConditionApiService conditionApiService;
    @Resource
    private ConditionDbService conditionDbService;
    @Resource
    private ConditionScriptService conditionScriptService;

    @Resource
    private ConditionDelayService conditionDelayService;

    @PostMapping
    public Result add(@RequestBody ConditionOrder conditionOrder) {
        conditionOrderService.save(conditionOrder);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        conditionOrderService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody ConditionOrder conditionOrder) {
        conditionOrderService.update(conditionOrder);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        ConditionOrder conditionOrder = conditionOrderService.getById(id);
        return ResultGenerator.genOkResult(conditionOrder);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ConditionOrder> list = conditionOrderService.listAll();
        PageInfo<ConditionOrder> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


    @PostMapping("/addconditionorder")
    public Result addconditionorder(@RequestBody final List<ConditionOrder> conditionOrderList) {
        conditionOrderService.updateBatchconditionOrderList(conditionOrderList);
        return ResultGenerator.genOkResult();
    }

    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        List<ConditionOrder> conditionOrderList=conditionOrderService.findconditionorderWithid(param);
//        HashMap<Long,ConditionOrder> conditionorderApiHashMap=getSubConditionOrderMap(Conditionid,"接口");
//        HashMap<Long,ConditionOrder> conditionorderDBHashMap=getSubConditionOrderMap(Conditionid,"数据库");
//        HashMap<Long,ConditionOrder> conditionorderScriptHashMap=getSubConditionOrderMap(Conditionid,"脚本");
//        HashMap<Long,ConditionOrder> conditionorderDelayHashMap=getSubConditionOrderMap(Conditionid,"延时");
//
//
//        Condition apicondition=new Condition(ConditionApi.class);
//        apicondition.createCriteria().andCondition("conditionid="+Conditionid);
//        List<ConditionApi> conditionApiList=conditionApiService.listByCondition(apicondition);
//
//        Condition dbcondition=new Condition(ConditionDb.class);
//        dbcondition.createCriteria().andCondition("conditionid="+Conditionid);
//        List<ConditionDb> conditionDbList=conditionDbService.listByCondition(dbcondition);
//
//        Condition scriptcondition=new Condition(ConditionScript.class);
//        scriptcondition.createCriteria().andCondition("conditionid="+Conditionid);
//        List<ConditionScript> conditionScriptList=conditionScriptService.listByCondition(scriptcondition);
//
//
//
//        Condition delaycondition=new Condition(ConditionDelay.class);
//        delaycondition.createCriteria().andCondition("conditionid="+Conditionid);
//        List<ConditionDelay> conditionDelayList=conditionDelayService.listByCondition(delaycondition);
//
//        int Subconditionnums=conditionApiList.size()+conditionDbList.size()+conditionScriptList.size()+conditionDelayList.size();
//        //如果条件顺序表有数据，说明已经排过条件顺序
//        if(conditionOrderList.size()>0)
//        {
//            //父条件下的所有子条件都已排序
//            if(conditionOrderList.size()==Subconditionnums)
//            {
//                return ResultGenerator.genOkResult(conditionOrderList);
//            }
//            //条件排序后，新增了条件
//            if(conditionOrderList.size()<Subconditionnums)
//            {
//                //接口条件中是否有新增，如有增加到返回的list
//                for (ConditionApi api:conditionApiList) {
//                    if(!conditionorderApiHashMap.containsKey(api.getId()))
//                    {
//                        conditionOrderList=getNewOrderlist(api.getConditionid(),api.getConditionname(),"接口",api.getSubconditionname(),api.getId(),conditionOrderList);
//                    }
//                }
//                //数据库条件中是否有新增，如有增加到返回的list
//                for (ConditionDb db:conditionDbList) {
//                    if(!conditionorderDBHashMap.containsKey(db.getId()))
//                    {
//                        conditionOrderList=getNewOrderlist(db.getConditionid(),db.getConditionname(),"数据库",db.getSubconditionname(),db.getId(),conditionOrderList);
//                    }
//                }
//                //脚本条件中是否有新增，如有增加到返回的list
//                for (ConditionScript script:conditionScriptList) {
//                    if(!conditionorderScriptHashMap.containsKey(script.getId()))
//                    {
//                        conditionOrderList=getNewOrderlist(script.getConditionid(),script.getConditionname(),"脚本",script.getSubconditionname(),script.getId(),conditionOrderList);
//                    }
//                }
//
//                //延时条件中是否有新增，如有增加到返回的list
//                for (ConditionDelay delay:conditionDelayList) {
//                    if(!conditionorderDelayHashMap.containsKey(delay.getId()))
//                    {
//                        conditionOrderList=getNewOrderlist(delay.getConditionid(),delay.getConditionname(),"延时",delay.getSubconditionname(),delay.getId(),conditionOrderList);
//                    }
//                }
//            }
//        }
//        else //还没有条件排序
//        {
//            for (ConditionApi api : conditionApiList) {
//                conditionOrderList = getNewOrderlist(api.getConditionid(), api.getConditionname(), "接口", api.getSubconditionname(), api.getId(), conditionOrderList);
//            }
//            //数据库条件中是否有新增，如有增加到返回的list
//            for (ConditionDb db : conditionDbList) {
//                conditionOrderList = getNewOrderlist(db.getConditionid(), db.getConditionname(), "数据库", db.getSubconditionname(), db.getId(), conditionOrderList);
//            }
//            //脚本条件中是否有新增，如有增加到返回的list
//            for (ConditionScript script : conditionScriptList) {
//                conditionOrderList = getNewOrderlist(script.getConditionid(), script.getConditionname(), "脚本", script.getSubconditionname(), script.getId(), conditionOrderList);
//            }
//            //延时条件中是否有新增，如有增加到返回的list
//            for (ConditionDelay delay : conditionDelayList) {
//                conditionOrderList = getNewOrderlist(delay.getConditionid(), delay.getConditionname(), "延时", delay.getSubconditionname(), delay.getId(), conditionOrderList);
//            }
//        }
        return ResultGenerator.genOkResult(conditionOrderList);
    }

    private HashMap<Long,ConditionOrder> getSubConditionOrderMap(Long conditionid,String SubType)
    {
        HashMap<Long,ConditionOrder> conditionorderApiHashMap=new HashMap<>();
        List<ConditionOrder> conditionOrderList=conditionOrderService.findconditionorderWithidandtype(conditionid,SubType);
        for (ConditionOrder conditionOrder : conditionOrderList) {
            conditionorderApiHashMap.put(conditionOrder.getSubconditionid(),conditionOrder);
        }
        return conditionorderApiHashMap;
    }

    private List<ConditionOrder> getNewOrderlist(Long Conditionid,String Conditionname,String SubType,String SubConditionname,Long SubConditionid,List<ConditionOrder>conditionOrderList)
    {
        ConditionOrder conditionOrder1=new ConditionOrder();
        conditionOrder1.setConditionid(Conditionid);
        conditionOrder1.setConditionname(Conditionname);
        conditionOrder1.setOrderstatus("未排序");
        conditionOrder1.setSubconditiontype(SubType);
        conditionOrder1.setSubconditionname(SubConditionname);
        conditionOrder1.setSubconditionid(SubConditionid);
        conditionOrderList.add(conditionOrder1);
        return conditionOrderList;
    }


}
