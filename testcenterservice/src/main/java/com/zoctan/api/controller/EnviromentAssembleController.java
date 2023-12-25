package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.entity.Enviroment;
import com.zoctan.api.entity.EnviromentAssemble;
import com.zoctan.api.entity.Macdepunit;
import com.zoctan.api.entity.Machine;
import com.zoctan.api.service.EnviromentAssembleService;
import com.zoctan.api.service.MacdepunitService;
import com.zoctan.api.service.MachineService;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author SeasonFan
 * @date 2020/11/06
 */
@RestController
@RequestMapping("/enviroment_assemble")
public class EnviromentAssembleController {
    @Resource
    private EnviromentAssembleService enviromentAssembleService;
    @Resource
    private MachineService machineService;
    @Resource
    private MacdepunitService macdepunitService;


    @PostMapping
    public Result add(@RequestBody EnviromentAssemble enviromentAssemble) {
        Condition con = new Condition(EnviromentAssemble.class);
        con.createCriteria().andCondition("projectid = " + enviromentAssemble.getProjectid())
                .andCondition("assembletype = '" + enviromentAssemble.getAssembletype() + "'")
                .andCondition("assemblename = '" + enviromentAssemble.getAssemblename().replace("'", "''") + "'");
        if (enviromentAssembleService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("已存在相同的环境组件");
        } else {
            enviromentAssembleService.save(enviromentAssemble);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        List<Macdepunit> macdepunitList = macdepunitService.findassemblebyassid(id);
        if (macdepunitList.size() > 0) {
            return ResultGenerator.genFailedResult("当前组件在环境部署中还在使用，无法删除！");
        } else {
            enviromentAssembleService.deleteById(id);
            return ResultGenerator.genOkResult();
        }
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final EnviromentAssemble enviromentAssemble) {
        Condition con = new Condition(EnviromentAssemble.class);
        con.createCriteria().andCondition("projectid = " + enviromentAssemble.getProjectid())
                .andCondition("assembletype = '" + enviromentAssemble.getAssembletype() + "'")
                .andCondition("assemblename = '" + enviromentAssemble.getAssemblename().replace("'", "''") + "'")
                .andCondition("id <> " + enviromentAssemble.getId());
        if (enviromentAssembleService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("环境组件已经存在");
        } else {

            this.enviromentAssembleService.updateenviromentassemble(enviromentAssemble);
            return ResultGenerator.genOkResult();
        }
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        EnviromentAssemble enviromentAssemble = enviromentAssembleService.getById(id);
        return ResultGenerator.genOkResult(enviromentAssemble);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<EnviromentAssemble> list = enviromentAssembleService.listAll();
        PageInfo<EnviromentAssemble> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


    @GetMapping("/getassemblename")
    public Result listall(@RequestParam long projectid) {
        Condition con = new Condition(EnviromentAssemble.class);
        con.createCriteria().andCondition("projectid = " + projectid);
        List<EnviromentAssemble> list = enviromentAssembleService.listByCondition(con);
//        List<EnviromentAssemble> list = enviromentAssembleService.listAll();
        return ResultGenerator.genOkResult(list);
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<EnviromentAssemble> list = this.enviromentAssembleService.findassembleWithName(param);
        final PageInfo<EnviromentAssemble> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @PostMapping("/runtest")
    public Result runtest(@RequestBody final Map<String, Object> param) throws SQLException {
        Long machineid = Long.parseLong(param.get("machineid").toString());
        String machinename = param.get("machinename").toString();
        String visittype = param.get("visittype").toString();
        String assembletype = param.get("assembletype").toString();
        String ConStr = param.get("constr").toString();

        String[] ConnetcArray = ConStr.split(",");
        if (ConnetcArray.length < 4) {
            return ResultGenerator.genFailedResult("连接字格式错误，请检查：" + ConStr);
        }

        String username = ConnetcArray[0];
        String pass = ConnetcArray[1];
        String port = ConnetcArray[2];
        String dbname = ConnetcArray[3];
        String DBUrl = "";
        if (assembletype.equals("mysql")) {
            DBUrl = "jdbc:mysql://";
        }
        if (assembletype.equals("oracle")) {
            DBUrl = "jdbc:oracle://";
        }

        String Url = "";
        if (visittype.equalsIgnoreCase("IP")) {
            Machine machine = machineService.getBy("id", machineid);
            if (machine == null) {
                return ResultGenerator.genFailedResult(machinename + " 该服务器不存在，请检查是否已经被删除！");
            }
            Url = machine.getIp();
            DBUrl = DBUrl + Url + ":" + port + "/" + dbname;
        } else {
            String domain = param.get("domain").toString();
            Url = domain;
            DBUrl = DBUrl + Url + "/" + dbname;
        }
        String LastDBUrl = DBUrl + "?useUnicode=true&useSSL=false&allowMultiQueries=true&characterEncoding=utf-8&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(LastDBUrl, username, pass);//获取连接
        } catch (Exception ex) {
            return ResultGenerator.genFailedResult("连接失败,请检查连接字：" + DBUrl + " ，异常原因：" + ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ResultGenerator.genOkResult("连接成功！");
    }
}
