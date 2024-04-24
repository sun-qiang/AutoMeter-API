package com.zoctan.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.*;
import com.zoctan.api.dto.PostMan.*;
import com.zoctan.api.dto.Swagger.*;
import com.zoctan.api.entity.*;
import com.zoctan.api.service.*;
import io.swagger.models.auth.In;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.math.raw.Mod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Zoctan
 * @date 2020/04/24
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private ApiService apiService;
    @Resource
    private ApiParamsService apiParamsService;
    @Resource
    private ApicasesService apicasesService;
    @Resource
    private ApiCasedataService apiCasedataService;
    @Resource
    private DeployunitService deployunitService;
    @Resource
    private DeployunitModelService deployunitModelService;

    @Resource
    private AccountRoleService accountRoleService;


    @PostMapping("/exportswagger")
    public Result exportswagger(@RequestParam("file") MultipartFile multipartFile, @RequestParam("deployid") String deployid, @RequestParam("deployunitname") String deployunitname, @RequestParam("apistyle") String apistyle, @RequestParam("creator") String creator, @RequestParam("projectid") String projectid, @RequestParam("mnickname") String mnickname, @RequestParam("creatorid") String creatorid, @RequestParam("mid") String mid) {
        File file = null;
        try {
            if (multipartFile.isEmpty()) {
                return ResultGenerator.genFailedResult("上传的文件为空，请检查");
            }
            Long deployunitid = Long.parseLong(deployid);
            Long pid = Long.parseLong(projectid);
            String fileName = multipartFile.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            file = new File(fileName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            if (".json".equals(suffixName)) {
                Gson gson = new Gson();
                String basePath = "";
                Map<String, Object> jsonmap = gson.fromJson(jsonString, Map.class);
                JsonElement jsonElement = JsonParser.parseString(jsonString);
                JsonObject jsonObj = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonElement swaggerelements = jsonObj.get("swagger");
                if (!swaggerelements.getAsString().contains("2.0")) {
                    return ResultGenerator.genFailedResult("请导入Swagger的2.0版本的json文件");
                }

                JsonElement tagselements = jsonObj.get("tags");
                JsonArray jsonArray = tagselements.getAsJsonArray();
                //保存模块
                HashMap<String, Long> modelmap = new HashMap<>();
                for (JsonElement ae : jsonArray) {
                    JsonObject tag = ae.getAsJsonObject();
                    JsonElement name = tag.get("name");
                    String modelname = name.getAsString();
                    Condition con = new Condition(DeployunitModel.class);
                    con.createCriteria().andCondition("deployunitid = " + deployunitid)
                            .andCondition("modelname = '" + modelname + "'");
                    if (deployunitModelService.ifexist(con) == 0) {
                        DeployunitModel deployunitModel = new DeployunitModel();
                        deployunitModel.setDeployunitid(deployunitid);
                        deployunitModel.setModelname(modelname);
                        deployunitModel.setCreateTime(new Date());
                        deployunitModel.setLastmodifyTime(new Date());
                        deployunitModel.setMemo("Swagger导入");
                        deployunitModel.setCreator(creator);
                        deployunitModelService.save(deployunitModel);
                        modelmap.put(modelname, deployunitModel.getId());
                    } else {
                        DeployunitModel deployunitModel = deployunitModelService.listByCondition(con).get(0);
                        modelmap.put(modelname, deployunitModel.getId());
                    }
                }
                //解析api保存
                JsonElement pathonject = jsonObj.get("paths");
                JsonObject postobjct = pathonject.getAsJsonObject();

                long apicounts = 0;
                HashMap<String, Object> ddd = new HashMap<>();
                try {
                    for (Object e : postobjct.entrySet()) {

                        Map.Entry entry = (Map.Entry) e;
                        ddd.put(String.valueOf(entry.getKey()), entry.getValue());
                        JsonObject jsonObject = (JsonObject) entry.getValue();
                        int menthodnums = 0;
                        for (Object jd : jsonObject.entrySet()) {

                            menthodnums++;
                            Map.Entry entryjs = (Map.Entry) jd;
                            JsonObject jsonObjectjd = (JsonObject) entryjs.getValue();
                            String jsonstring = jsonObjectjd.toString();
                            Type postType1 = new TypeToken<Post>() {
                            }.getType();
                            Post pathsob1 = gson.fromJson(jsonstring, postType1);
                            List<String> RequestCTList = pathsob1.getConsumes();

                            String Modelname = pathsob1.getTags().get(0);
                            Long ModelID = modelmap.get(pathsob1.getTags().get(0));
                            String RequestCT = "Form表单";
                            if (RequestCTList != null) {
                                if (RequestCTList.size() > 0) {
                                    String tmp = RequestCTList.get(0);
                                    if (tmp.equalsIgnoreCase("application/json")) {
                                        RequestCT = "JSON";
                                    }
                                }
                            }
                            //String ApiName = entry.getKey().toString().substring(entry.getKey().toString().lastIndexOf("/") + 1);

                            JsonObject jsonObjectsm = (JsonObject) entryjs.getValue();

                            String ApiName = jsonObjectsm.get("summary").getAsString();

                            if (menthodnums > 1) {
                                ApiName = ApiName + "-" + entryjs.getKey().toString();
                            }

                            Condition apicon = new Condition(Api.class);
                            apicon.createCriteria().andCondition("deployunitid = " + deployunitid)
                                    .andCondition("apiname = '" + ApiName + "'");
                            String VisiteType = entryjs.getKey().toString().toUpperCase();
                            if (apiService.ifexist(apicon) == 0) {
                                apicounts++;
                                Api api = new Api();
                                api.setCasecounts(new Long(0));
                                api.setVisittype(VisiteType);
                                api.setPath(entry.getKey().toString());
                                api.setRequestcontenttype(RequestCT);
                                api.setApiname(ApiName);
                                api.setDeployunitid(deployunitid);
                                api.setDeployunitname(deployunitname);
                                api.setApistyle(apistyle);
                                api.setCreateTime(new Date());
                                api.setLastmodifyTime(new Date());
                                api.setMemo(pathsob1.getDescription());
                                api.setProjectid(pid);
                                api.setModelname(Modelname);
                                api.setModelid(ModelID);
                                api.setCreator(creator);
                                api.setCreatorid(Long.parseLong(creatorid));
                                api.setMnickname(mnickname);
                                api.setMid(Long.parseLong(mid));
                                apiService.save(api);
                                System.out.println("url-------:" + entry.getKey() + " method-------:" + entryjs.getKey() + "  getDescription-------:" + pathsob1.getTags().get(0));
                                //api参数
                                swapiparmas(jsonObj, VisiteType, pathsob1, api.getId(), deployunitid, ApiName, deployunitname, RequestCT, creator);
                                //4.保存用例，用例值
                                SaveCaseData(ApiName, api.getId(), deployunitid, deployunitname, creator, Modelname, ModelID, pid);
                            } else {
                                //API已经存在，只保存用例，用例值
                                Api existApi = apiService.listByCondition(apicon).get(0);
                                if (existApi != null) {
                                    Long ExcistApiid = existApi.getId();
                                    SaveCaseData(ApiName, ExcistApiid, deployunitid, deployunitname, creator, Modelname, ModelID, pid);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ResultGenerator.genFailedResult("导入异常：" + ex.getMessage());
                } finally {
                    Deployunit deployunit = deployunitService.getById(deployunitid);
                    if (deployunit != null) {
                        deployunit.setApicounts(deployunit.getApicounts() + apicounts);
                        deployunitService.update(deployunit);
                    }
                }
            } else {
                return ResultGenerator.genFailedResult("导入异常,请导入Swagger的json文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailedResult("导入异常：" + e.getMessage());
        } finally {
            file.delete();
        }
        return ResultGenerator.genOkResult();
    }

    private void swapiparmas(JsonObject jsonObj, String VisiteType, Post pathsob1, long apiid, long deployunitid, String apiname, String deployunitname, String RequestCT, String creator) {
        List<parameters> parameterList = pathsob1.getParameters();
        for (parameters pmt : parameterList) {
            ApiParams apiParams = new ApiParams();

            //设置参数setPropertytype，setKeyname
            if (pmt.getIn().equals("body")) {
                apiParams.setPropertytype("Body");
                schema schema = pmt.getSchema();
                if (schema != null) {
                    String Ref = schema.get$ref();
                    if (Ref != null) {
                        String DefinaName = Ref.substring(Ref.lastIndexOf("/") + 1);
                        JSONObject js = SWGetDefinitions("definitions", DefinaName, jsonObj);
                        apiParams.setKeyname(js.toJSONString().replace("\\", ""));
                        apiParams.setKeydefaultvalue("NoForm");
                    }
                    items schemaItems = schema.getItem();
                    if (schemaItems != null) {
                        String DefinaName = schemaItems.get$ref().substring(schemaItems.get$ref().lastIndexOf("/") + 1);
                        JSONObject js = SWGetDefinitions("definitions", DefinaName, jsonObj);
                        if (schema.getType().equals("array")) {
                            apiParams.setKeyname("[" + js.toJSONString().replace("\\", "") + "]");

                        } else {
                            apiParams.setKeyname(js.toJSONString().replace("\\", ""));
                        }
                        apiParams.setKeydefaultvalue("NoForm");
                    }
                }
            }
            if (pmt.getIn().equals("path") || pmt.getIn().equals("query")) {
                apiParams.setPropertytype("Params");
                apiParams.setKeyname(pmt.getName());
                apiParams.setKeydefaultvalue("");
            }
            if (pmt.getIn().equals("header")) {
                apiParams.setPropertytype("Header");
                apiParams.setKeyname(pmt.getName());
                apiParams.setKeydefaultvalue("");
            }
            if (pmt.getIn().equals("formData")) {
                apiParams.setPropertytype("Body");
                apiParams.setKeyname(pmt.getName());
                apiParams.setKeydefaultvalue("");
            }
            //设置参数name
//            if (RequestCT.equalsIgnoreCase("Form表单")) {
//                apiParams.setKeyname(pmt.getName());
//                apiParams.setKeydefaultvalue("");
//            } else {
//                schema schema = pmt.getSchema();
//                if (schema != null) {
//                    String Ref = schema.get$ref();
//                    if (Ref != null) {
//                        String DefinaName = Ref.substring(Ref.lastIndexOf("/") + 1);
//                        JSONObject js = SWGetDefinitions("definitions", DefinaName, jsonObj);
//                        apiParams.setKeyname(js.toJSONString().replace("\\", ""));
//                        apiParams.setKeydefaultvalue("NoForm");
//                    }
//                }
//            }
            apiParams.setApiid(apiid);
            apiParams.setApiname(apiname);
            apiParams.setDeployunitid(deployunitid);
            apiParams.setDeployunitname(deployunitname);
            apiParams.setKeytype(SWGetparamsType(pmt.getType()));
            apiParams.setCreateTime(new Date());
            apiParams.setLastmodifyTime(new Date());
            apiParams.setCreator(creator);
            apiParamsService.save(apiParams);
        }
    }

    private String SWGetparamsType(String source) {
        String Destination = "String";
        if (source == null) {
            return "JSON";
        }
        if (source.equalsIgnoreCase("string")) {
            Destination = "String";
        }
        if (source.equalsIgnoreCase("integer")) {
            Destination = "Number";
        }

        return Destination;
    }

    private JSONObject SWGetDefinitions(String NodeName, String ChildNodeName, JsonObject jsonObj) {
        JsonElement Root = jsonObj.get(NodeName);
        JsonObject RootOB = Root.getAsJsonObject();
        JsonElement Child = RootOB.get(ChildNodeName);
        JsonObject ChildOB = Child.getAsJsonObject();
        JsonElement PropertyChild = ChildOB.get("properties");
        JsonObject PropertyOB = PropertyChild.getAsJsonObject();

        JSONObject ResultOnject = new JSONObject();
        for (Object e : PropertyOB.entrySet()) {
            Map.Entry entry = (Map.Entry) e;
            JsonObject jsonObjectjd = (JsonObject) entry.getValue();
            for (Object nametype : jsonObjectjd.entrySet()) {
                Map.Entry entrytypr = (Map.Entry) nametype;
                if (entrytypr.getKey().toString().equalsIgnoreCase("type")) {
                    if (entrytypr.getValue().toString().replace("\"", "").equals("integer")) {
                        ResultOnject.fluentPut(entry.getKey().toString(), 0);
                    }
                    if (entrytypr.getValue().toString().replace("\"", "").equals("array")) {
                        ResultOnject.fluentPut(entry.getKey().toString(), new ArrayList<>());
                    }
                    if (entrytypr.getValue().toString().replace("\"", "").equals("string")) {
                        ResultOnject.fluentPut(entry.getKey().toString(), "");
                    }
                    System.out.println("jsonname-------:" + entry.getKey() + " jsontype-------:" + entrytypr.getValue());
                }
                if (entrytypr.getKey().toString().startsWith("$ref")) {
                    String ref = entrytypr.getValue().toString().substring(entrytypr.getValue().toString().lastIndexOf("/") + 1);
                    ref = ref.replace("\"", "");
                    JSONObject js = SWGetDefinitions("definitions", ref, jsonObj);
                    ResultOnject.put(entry.getKey().toString(), js);
                }

                if (entrytypr.getKey().toString().equalsIgnoreCase("items")) {
                    JsonObject jsonObjectitem = (JsonObject) entrytypr.getValue();
                    for (Object item : jsonObjectitem.entrySet()) {
                        Map.Entry entryitem = (Map.Entry) item;
                        if (entryitem.getKey().toString().startsWith("$ref")) {
                            String ref = entryitem.getValue().toString().substring(entryitem.getValue().toString().lastIndexOf("/") + 1);
                            ref = ref.replace("\"", "");
                            JSONObject js = SWGetDefinitions("definitions", ref, jsonObj);
                            ArrayList<JSONObject> list = new ArrayList<>();
                            list.add(js);
                            ResultOnject.put(entry.getKey().toString(), list);
                        }
                    }
                }
            }
        }
        return ResultOnject;
    }

    @PostMapping("/exportpostman")
    public Result exportpostman(@RequestParam("file") MultipartFile multipartFile, @RequestParam("deployid") String deployid, @RequestParam("deployunitname") String deployunitname, @RequestParam("apistyle") String apistyle, @RequestParam("creator") String creator, @RequestParam("projectid") String projectid, @RequestParam("mnickname") String mnickname, @RequestParam("creatorid") String creatorid, @RequestParam("mid") String mid) {
        File file = null;
        try {
            if (multipartFile.isEmpty()) {
                return ResultGenerator.genFailedResult("上传的文件为空，请检查");
            }
            Long deployunitid = Long.parseLong(deployid);
            Long pid = Long.parseLong(projectid);
            String fileName = multipartFile.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            file = new File(fileName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            String jsonString = FileUtils.readFileToString(file, "UTF-8");
            if (".json".equals(suffixName) || ".txt".equals(suffixName)) {
                Gson gson = new Gson();
                Map<String, Object> jsonmap = gson.fromJson(jsonString, Map.class);
                String GroupJson = gson.toJson(jsonmap.get("item"));
                if (GroupJson.equalsIgnoreCase("null")) {
                    return ResultGenerator.genFailedResult("导入异常,请导入PostMan2.X版本的json文件");
                }

                HashMap<String, Long> modelmap = new HashMap<>();
                JsonParser parser = new JsonParser();
                JsonElement el = parser.parse(GroupJson);
                JsonArray jsonArray = null;
                if (el.isJsonArray()) {
                    jsonArray = el.getAsJsonArray();
                }
                Iterator it = jsonArray.iterator();
                while (it.hasNext()) {
                    JsonElement element = (JsonElement) it.next();
                    String modelname = "默认模块";
                    if (element.getAsJsonObject().get("item") != null) {
                        modelname = element.getAsJsonObject().get("name").getAsString();
                    }
                    Condition con = new Condition(DeployunitModel.class);
                    con.createCriteria().andCondition("deployunitid = " + deployunitid)
                            .andCondition("modelname = '" + modelname + "'");
                    DeployunitModel deployunitModel = new DeployunitModel();
                    if (deployunitModelService.ifexist(con) == 0) {
                        deployunitModel.setDeployunitid(deployunitid);
                        deployunitModel.setModelname(modelname);
                        deployunitModel.setCreateTime(new Date());
                        deployunitModel.setLastmodifyTime(new Date());
                        deployunitModel.setMemo("PostMan导入");
                        deployunitModel.setCreator(creator);
                        deployunitModelService.save(deployunitModel);
                        //modelmap.put(modelname, deployunitModel.getId());
                    } else {
                        deployunitModel = deployunitModelService.listByCondition(con).get(0);
                        //modelmap.put(modelname, deployunitModel.getId());
                    }
                    modelmap.put(modelname, deployunitModel.getId());
                }
                HashMap<String, ApiInfo> apiInfoHashMap = new HashMap<>();
                GetItemArray(GroupJson, gson, apistyle, deployunitid, deployunitname, creator, apiInfoHashMap, modelmap, "", pid,creatorid,mid,mnickname);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailedResult("导入异常：" + e.getMessage());
        } finally {
            file.delete();
        }
        return ResultGenerator.genOkResult();
    }


    private void GetItemArray(String GroupJson, Gson gson, String apistyle, Long deployunitid, String deployunitname, String creator, HashMap<String, ApiInfo> apiInfoHashMap, HashMap<String, Long> modelmap, String Modelname, Long pid, String creatorid, String mid, String mnickname) throws Exception {
        List<String> resu = GetJson(GroupJson);
        for (String apiinfojson : resu) {
            Map<String, Object> apiinfomap = gson.fromJson(apiinfojson, Map.class);
            String apiJson = gson.toJson(apiinfomap.get("item"));
            if (!apiJson.equalsIgnoreCase("null")) {
                Modelname = apiinfomap.get("name").toString();
                recitem(apiJson, gson, apistyle, deployunitid, deployunitname, creator, apiInfoHashMap, modelmap, Modelname, pid,creatorid,mid,mnickname);
            } else {
                Modelname = "默认模块";
                Type apiinfoType = new TypeToken<ApiInfo>() {
                }.getType();
                //System.out.println("apiinfojson-------:" + apiinfojson);
                ApiInfo apiInfo = gson.fromJson(apiinfojson, apiinfoType);
                if (!apiInfoHashMap.containsKey(apiInfo.getName())) {
                    ExportData(apiInfo, apistyle, deployunitid, deployunitname, creator, modelmap, Modelname, pid,creatorid,mid,mnickname);
                }
                apiInfoHashMap.put(apiInfo.getName(), apiInfo);
            }
        }
    }

    private void recitem(String GroupJson, Gson gson, String apistyle, Long deployunitid, String deployunitname, String creator, HashMap<String, ApiInfo> apiInfoHashMap, HashMap<String, Long> modelmap, String Modelname, Long pid, String creatorid, String mid, String mnickname) throws Exception {
        List<String> resu = GetJson(GroupJson);
        for (String apiinfojson : resu) {
            Map<String, Object> apiinfomap = gson.fromJson(apiinfojson, Map.class);
            String apiJson = gson.toJson(apiinfomap.get("item"));
            if (!apiJson.equalsIgnoreCase("null")) {
//                if(Modelname.equalsIgnoreCase(""))
//                {
//                    Modelname = apiinfomap.get("name").toString();
//                }
                recitem(apiJson, gson, apistyle, deployunitid, deployunitname, creator, apiInfoHashMap, modelmap, Modelname, pid,creatorid,mid,mnickname);
            } else {
                Type apiinfoType = new TypeToken<ApiInfo>() {
                }.getType();
                //System.out.println("apiinfojson-------:" + apiinfojson);
                ApiInfo apiInfo = gson.fromJson(apiinfojson, apiinfoType);
                if (!apiInfoHashMap.containsKey(apiInfo.getName())) {
                    ExportData(apiInfo, apistyle, deployunitid, deployunitname, creator, modelmap, Modelname, pid,creatorid,mid,mnickname);
                }
                apiInfoHashMap.put(apiInfo.getName(), apiInfo);
            }
        }
    }

    private void ExportData(ApiInfo apiInfo, String apistyle, Long deployunitid, String deployunitname, String creator, HashMap<String, Long> modelmap, String Modelname, Long PID, String creatorid, String mid, String mnickname) {
        String AllPath = "";
        if (apiInfo.getRequest().getUrl() != null) {
            ArrayList<String> UrlPath = apiInfo.getRequest().getUrl().getPath();
            for (String path : UrlPath) {
                AllPath = AllPath + "/" + path;
            }
        }
        String VisitType = apiInfo.getRequest().getMethod();
        //取到postman第一个接口名做为api名
        String ApiName = apiInfo.getName();
        Api api = new Api();
        api.setVisittype(VisitType);
        api.setApiname(ApiName);
        api.setApistyle(apistyle);
        api.setModelname(Modelname);
        api.setProjectid(PID);
        long ModelID = 0;
        if (modelmap.containsKey(Modelname)) {
            ModelID = modelmap.get(Modelname);
            api.setModelid(ModelID);
        } else {
            api.setModelid(new Long(0));
        }
        String RequestCT = "Form表单";
        if (apiInfo.getRequest().getBody() != null) {
            if (apiInfo.getRequest().getBody().getOptions() != null) {
                RequestCT = apiInfo.getRequest().getBody().getOptions().getRaw().getLanguage().toUpperCase();
            } else {
                RequestCT = "TEXT";
            }
        } else //到header中补偿下请求格式
        {
            List<Header> headerList = apiInfo.getRequest().getHeader();
            for (Header header : headerList) {
                if (header.getKey().equalsIgnoreCase("Content-Type")) {
                    String Value = header.getValue();
                    String[] Array = Value.split("/");
                    if (Array.length > 1) {
                        RequestCT = Array[1].toUpperCase();
                    }
                }
            }
        }
        api.setRequestcontenttype(RequestCT);
        api.setDeployunitid(deployunitid);
        api.setDeployunitname(deployunitname);
        api.setCreator(creator);

        api.setCreatorid(Long.parseLong(creatorid));
        api.setMnickname(mnickname);
        api.setMid(Long.parseLong(mid));

        api.setCreateTime(new Date());
        api.setLastmodifyTime(new Date());
        api.setPath(AllPath);

        List<Header> headerList = apiInfo.getRequest().getHeader();
        List<Query> queryList = new ArrayList<>();
        if (apiInfo.getRequest().getUrl() != null) {
            if (apiInfo.getRequest().getUrl().getQuery() != null) {
                queryList = apiInfo.getRequest().getUrl().getQuery();
            }
        }

        Condition con = new Condition(Api.class);
        con.createCriteria().andCondition("apiname = '" + ApiName + "'")
                .andCondition("deployunitid = " + api.getDeployunitid());
//                .andCondition("visittype = '" + api.getVisittype() + "'")
//                .andCondition("deployunitid = " + api.getDeployunitid());
        if (apiService.ifexist(con) == 0) {
            apiService.save(api);
            Long Apiid = api.getId();

            long deployid = api.getDeployunitid();
            Deployunit deployunit = deployunitService.getById(deployid);
            deployunit.setApicounts(deployunit.getApicounts() + 1);
            deployunitService.update(deployunit);

            //保存api参数
            //1.保存Header参数
            List<ApiParams> apiHeaderParamsList = new ArrayList<>();
            for (Header header : headerList) {
                ApiParams apiParams = GetApiParam(Apiid, ApiName, creator, deployunitid, deployunitname, header.getKey(), header.getValue(), "", "Header");
                apiHeaderParamsList.add(apiParams);
            }
            if (apiHeaderParamsList.size() > 0) {
                apiParamsService.save(apiHeaderParamsList);
            }
            //2.保存Params参数
            List<ApiParams> apiParamsList = new ArrayList<>();
            for (Query query : queryList) {
                ApiParams apiParams = GetApiParam(Apiid, ApiName, creator, deployunitid, deployunitname, query.getKey(), query.getValue(), "String", "Params");
                apiParamsList.add(apiParams);
            }
            if (apiParamsList.size() > 0) {
                apiParamsService.save(apiParamsList);
            }
            //3.保存Body参数
            if (apiInfo.getRequest().getBody() != null) {
                String mode = apiInfo.getRequest().getBody().getMode();
                if (mode.equalsIgnoreCase("raw")) {
                    String BodyKeyValue = apiInfo.getRequest().getBody().getRaw();
                    String KeyType = "TEXT";
                    if (apiInfo.getRequest().getBody().getOptions() != null) {
                        KeyType = apiInfo.getRequest().getBody().getOptions().getRaw().getLanguage().toUpperCase();
                    }
                    ApiParams apiParams = GetApiParam(Apiid, ApiName, creator, deployunitid, deployunitname, BodyKeyValue, "NoForm", KeyType, "Body");
                    apiParamsService.save(apiParams);
                }
                if (mode.equalsIgnoreCase("urlencoded")) {
                    List<ApiParams> apiBodyParamsUrlencoderList = new ArrayList<>();
                    List<Urlencoder> BodyparamsList = apiInfo.getRequest().getBody().getUrlencoded();
                    for (Urlencoder query : BodyparamsList) {
                        ApiParams apiParams = GetApiParam(Apiid, ApiName, creator, deployunitid, deployunitname, query.getKey(), query.getValue(), "String", "Body");
                        apiBodyParamsUrlencoderList.add(apiParams);
                    }
                    if (apiBodyParamsUrlencoderList.size() > 0) {
                        apiParamsService.save(apiBodyParamsUrlencoderList);
                    }
                }

                if (mode.equalsIgnoreCase("formdata")) {
                    List<ApiParams> apiBodyParamsformdataList = new ArrayList<>();
                    List<Formdata> formdataList = apiInfo.getRequest().getBody().getFormdata();
                    for (Formdata query : formdataList) {
                        if (query.getKey().equalsIgnoreCase("file")) {
                            query.setValue("file");
                        }
                        ApiParams apiParams = GetApiParam(Apiid, ApiName, creator, deployunitid, deployunitname, query.getKey(), query.getValue(), "String", "Body");
                        apiBodyParamsformdataList.add(apiParams);
                    }
                    if (apiBodyParamsformdataList.size() > 0) {
                        apiParamsService.save(apiBodyParamsformdataList);
                    }
                }
            }
            //4.保存用例，用例值
            SaveCaseData(ApiName, Apiid, deployunitid, deployunitname, creator, Modelname, ModelID, PID);
        } else {
            //API已经存在，只保存用例，用例值
            //Api existApi = apiService.getapibydvap(deployunitid, VisitType, AllPath);
            Api existApi = apiService.listByCondition(con).get(0);
            if (existApi != null) {
                Long ExcistApiid = existApi.getId();
                SaveCaseData(ApiName, ExcistApiid, deployunitid, deployunitname, creator, Modelname, ModelID, PID);
            }
        }
        // }
    }

    private List<String> GetJson(String itemJson) throws Exception {
        List<String> result = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonElement el = parser.parse(itemJson);

            JsonArray jsonArray = null;
            if (el.isJsonArray()) {
                jsonArray = el.getAsJsonArray();
            }

            Iterator it = jsonArray.iterator();
            while (it.hasNext()) {
                JsonElement element = (JsonElement) it.next();
                result.add(element.toString());
            }
        } catch (Exception ex) {
            throw new Exception("PostMan导入的json文件内容不是合法的json，请检查！");
        }

        return result;
    }

    private ApiParams GetApiParam(Long Apiid, String ApiName, String creator, Long deployunitid, String deployunitname, String keyname, String Keydefaultvalue, String Keytype, String ParamType) {
        ApiParams apiParams = new ApiParams();
        apiParams.setApiid(Apiid);
        apiParams.setApiname(ApiName);
        apiParams.setCreator(creator);
        apiParams.setDeployunitid(deployunitid);
        apiParams.setDeployunitname(deployunitname);
        apiParams.setCreateTime(new Date());
        apiParams.setLastmodifyTime(new Date());
        apiParams.setPropertytype(ParamType);
        apiParams.setKeyname(keyname);
        apiParams.setKeydefaultvalue(Keydefaultvalue);
        apiParams.setKeytype(Keytype);
        return apiParams;
    }

    private Apicases GetApiCase(String ApiName, Long Apiid, Long deployunitid, String deployunitname, String creator, String Modelname, Long ModelID, Long PID) {
        Apicases apicases = new Apicases();
        apicases.setApiname(ApiName);
        apicases.setApiid(Apiid);
        apicases.setCasename(ApiName);
        apicases.setCreateTime(new Date());
        apicases.setLastmodifyTime(new Date());
        apicases.setCasetype("功能");
        apicases.setLevel(new Long(0));
        apicases.setDeployunitid(deployunitid);
        apicases.setDeployunitname(deployunitname);
        apicases.setThreadnum(new Long(1));
        apicases.setLoops(new Long(1));
        apicases.setCasecontent("");
        apicases.setExpect("");
        apicases.setCasejmxname("httpapi");
        apicases.setCreator(creator);
        apicases.setMiddleparam("");
        apicases.setMemo(ApiName);
        apicases.setCasecontent(ApiName);
        apicases.setModelid(ModelID);
        apicases.setModelname(Modelname);
        apicases.setProjectid(PID);
        return apicases;
    }

    private ApiCasedata GetApicaseData(String ApiName, Long Apicaseid, String ProperTy, String Key, String Value, String ParamType) {
        ApiCasedata apiCasedata = new ApiCasedata();
        apiCasedata.setCasename(ApiName);
        apiCasedata.setCaseid(Apicaseid);
        apiCasedata.setPropertytype(ProperTy);
        apiCasedata.setApiparam(Key);
        apiCasedata.setApiparamvalue(Value);
        apiCasedata.setParamstype(ParamType);
        apiCasedata.setCreateTime(new Date());
        apiCasedata.setLastmodifyTime(new Date());
        apiCasedata.setMemo("");
        return apiCasedata;
    }

    private void SaveCaseData(String ApiName, Long Apiid, Long deployunitid, String deployunitname, String creator, String ModelName, Long ModelID, Long Pid) {  //List<Header> headerList, List<Query> queryList,
        //4.保存用例
        Apicases apicases = GetApiCase(ApiName, Apiid, deployunitid, deployunitname, creator, ModelName, ModelID, Pid);
        Condition con = new Condition(Apicases.class);
        con.createCriteria().andCondition("apiid = " + apicases.getApiid())
                .andCondition("casename = '" + apicases.getCasename() + " '")
                .andCondition("deployunitid = " + apicases.getDeployunitid());
        if (apicasesService.ifexist(con) == 0) {
            apicasesService.save(apicases);
            Api api = apiService.getById(apicases.getApiid());
            long casecount = api.getCasecounts();
            api.setCasecounts(casecount + 1);
            apiService.updateApi(api);

            Long Apicaseid = apicases.getId();
            //5.保存用例Header数据
            List<ApiCasedata> Headercasedata = new ArrayList<>();
            List<ApiParams> headerList = apiParamsService.findApiParamsbypropertytype(Apiid, "Header");
            for (ApiParams header : headerList) {
                ApiCasedata apiCasedata = GetApicaseData(ApiName, Apicaseid, "Header", header.getKeyname(), header.getKeydefaultvalue(), "");
                Headercasedata.add(apiCasedata);
            }
            if (Headercasedata.size() > 0) {
                apiCasedataService.save(Headercasedata);
            }
            //6.保存用例Params数据

            List<ApiParams> queryList = apiParamsService.findApiParamsbypropertytype(Apiid, "Params");
            List<ApiCasedata> Paramscasedata = new ArrayList<>();
            for (ApiParams query : queryList) {
                ApiCasedata apiCasedata = GetApicaseData(ApiName, Apicaseid, "Params", query.getKeyname(), query.getKeydefaultvalue(), "String");
                Paramscasedata.add(apiCasedata);
            }
            if (Paramscasedata.size() > 0) {
                apiCasedataService.save(Paramscasedata);
            }
            //7.保存用例Body数据
            List<ApiCasedata> BodyParamscasedata = new ArrayList<>();
            List<ApiParams> BodyParamsList = apiParamsService.findApiParamsbypropertytype(Apiid, "Body");
            for (ApiParams params : BodyParamsList) {
                if (params.getKeydefaultvalue().equalsIgnoreCase("NoForm")) {
                    ApiCasedata apiCasedata = GetApicaseData(ApiName, Apicaseid, "Body", "Body", params.getKeyname(), params.getKeytype());
                    BodyParamscasedata.add(apiCasedata);
                } else {
                    ApiCasedata apiCasedata = GetApicaseData(ApiName, Apicaseid, "Body", params.getKeyname(), params.getKeydefaultvalue(), params.getKeytype());
                    BodyParamscasedata.add(apiCasedata);
                }
            }
            if (BodyParamscasedata.size() > 0) {
                apiCasedataService.save(BodyParamscasedata);
            }
        }
    }

    @PostMapping
    public Result add(@RequestBody Api api) {
        Condition con = new Condition(Api.class);
        con.createCriteria().andCondition("projectid = " + api.getProjectid())
                .andCondition("deployunitname = '" + api.getDeployunitname() + "'")
                .andCondition("apiname = '" + api.getApiname().replace("'", "''") + "'");
        if (apiService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult("此微服务下已经存在此API");
        } else {
            apiService.save(api);
            long deployid = api.getDeployunitid();
            Deployunit deployunit = deployunitService.getById(deployid);
            deployunit.setApicounts(deployunit.getApicounts() + 1);
            deployunitService.update(deployunit);
            return ResultGenerator.genOkResult();
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {

        List<Apicases> apicasesList = apicasesService.getcasebyapiid(id);
        if (apicasesList.size() > 0) {
            return ResultGenerator.genFailedResult("当前API还存在测试用例，无法删除,请先删除对应的测试用例");
        } else {
            Api api = apiService.getById(id);
            apiService.deleteById(id);
            apiParamsService.deletebyApiid(id);

            long deployid = api.getDeployunitid();
            Deployunit deployunit = deployunitService.getById(deployid);
            deployunit.setApicounts(deployunit.getApicounts() - 1);
            deployunitService.update(deployunit);
            return ResultGenerator.genOkResult();
        }
    }

    @PostMapping("/removebatchapi")
    public Result removebatchapi(@RequestBody List<Api> apiList) {
        for (Api api : apiList) {
            long id = api.getId();
            List<Apicases> apicasesList = apicasesService.getcasebyapiid(id);
            if (apicasesList.size() > 0) {
                return ResultGenerator.genFailedResult("当前API还存在测试用例，无法删除,请先删除对应的测试用例");
            } else {
                apiService.deleteById(id);
                apiParamsService.deletebyApiid(id);

                long deployid = api.getDeployunitid();
                Deployunit deployunit = deployunitService.getById(deployid);
                deployunit.setApicounts(deployunit.getApicounts() - 1);
                deployunitService.update(deployunit);
            }
        }
        return ResultGenerator.genOkResult();
    }

    @PatchMapping
    public Result update(@RequestBody Api api) {
        apiService.update(api);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Api api = apiService.getById(id);
        return ResultGenerator.genOkResult(api);
    }

    @GetMapping("/getapinum")
    public Result getapinum(@RequestParam long projectid) {
        Integer apinum = apiService.getapinum(projectid);
        return ResultGenerator.genOkResult(apinum);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Api> list = apiService.listAll();
        PageInfo<Api> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    /**
     * 根据微服务id获取apis
     */
    @PostMapping("/getapibydeployunitid")
    public Result getapibydeployunitid(@RequestBody final Map<String, Object> param) {
        String deployunitid = param.get("sourcedeployunitid").toString();
        final List<Api> list = this.apiService.getapibydeployunitid(Long.parseLong(deployunitid));
        return ResultGenerator.genOkResult(list);
    }

    @GetMapping("/getstaticsdeployapi")
    public Result getstaticsdeployapi(@RequestParam long projectid) {
        List<Api> list = apiService.getstaticsdeployapi(projectid);
        List<StaticsDataForPie> result = new ArrayList<>();
        for (Api api : list) {
            StaticsDataForPie staticsDataForPie = new StaticsDataForPie();
            staticsDataForPie.setValue(api.getId());
            staticsDataForPie.setName(api.getDeployunitname());
            result.add(staticsDataForPie);
        }
        return ResultGenerator.genOkResult(result);
    }


    @GetMapping("/apibydeploy")
    public Result listbydeploy(@RequestParam long deployunitid, @RequestParam long modelid) {
        List<Api> list = apiService.listAllbydeploy(deployunitid, modelid);
        return ResultGenerator.genOkResult(list);
    }

    @GetMapping("/getresponetypebydeployandapiname")
    public Result getresponetypebydeployandapiname(@RequestParam String deployunitname, @RequestParam String apiname) {
        Api apilist = apiService.getresponetypebydeployandapiname(deployunitname, apiname);
        return ResultGenerator.genOkResult(apilist);
    }

    /**
     * 更新自己的资料
     */
    @PutMapping("/detail")
    public Result updateDeploy(@RequestBody final Api api) {
        long apiid = api.getId();
        Long creatorid = api.getCreatorid();
        AccountRole accountRole= accountRoleService.getBy("accountId",creatorid);
        if(accountRole==null)
        {
            return ResultGenerator.genFailedResult("当前用户不存在");
        }
        Api apiexist = apiService.getBy("id", apiid);
        if (apiexist != null) {
            if (creatorid.equals(apiexist.getMid()) || accountRole.getRoleId()==1) {
                Condition con = new Condition(Api.class);
                con.createCriteria().andCondition("projectid = " + api.getProjectid())
                        .andCondition("deployunitname = '" + api.getDeployunitname() + "'")
                        .andCondition("apiname = '" + api.getApiname().replace("'", "''") + "'")
                        .andCondition("id <> " + api.getId());
                if (apiService.ifexist(con) > 0) {
                    return ResultGenerator.genFailedResult("此微服务下已经存在此API");
                } else {
                    api.setCreatorid(apiexist.getCreatorid());
                    this.apiService.updateApi(api);
                    return ResultGenerator.genOkResult();
                }
            } else {
                return ResultGenerator.genFailedResult("当前api只有维护人或者管理员可以修改");
            }
        } else {
            return ResultGenerator.genFailedResult("当前api不存在");
        }
    }

    /**
     * 输入框查询
     */
    @PostMapping("/search")
    public Result search(@RequestBody final Map<String, Object> param) {
        Integer page = Integer.parseInt(param.get("page").toString());
        Integer size = Integer.parseInt(param.get("size").toString());
        PageHelper.startPage(page, size);
        final List<Api> list = this.apiService.findApiWithName(param);
        final PageInfo<Api> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }


    @PostMapping("/copyapi")
    public Result copyapi(@RequestBody final Map<String, Object> param) {
        String sourceapiid = param.get("sourceapiid").toString();
        String sourcedeployunitid = param.get("sourcedeployunitid").toString();
        String sourcedeployunitname = param.get("sourcedeployunitname").toString();
        String objectdeployunitid = param.get("objectdeployunitid").toString();
        String objectdeployunitname = param.get("objectdeployunitname").toString();
        String newapiname = param.get("newapiname").toString();

        Condition con = new Condition(Api.class);
        con.createCriteria().andCondition("deployunitid = " + objectdeployunitid)
                .andCondition("apiname = '" + newapiname + "'");
        if (apiService.ifexist(con) > 0) {
            return ResultGenerator.genFailedResult(objectdeployunitname + "已存在存在此API名");
        } else {
            //复制api
            Api api = apiService.getBy("id", Long.parseLong(sourceapiid));
            Condition apiparamscon = new Condition(ApiParams.class);
            apiparamscon.createCriteria().andCondition("apiid = " + Long.parseLong(sourceapiid));
            List<ApiParams> apiParamsList = apiParamsService.listByCondition(apiparamscon);
            api.setDeployunitid(Long.parseLong(objectdeployunitid));
            api.setDeployunitname(objectdeployunitname);
            api.setApiname(newapiname);
            api.setId(null);
            Date date = new Date();
            api.setCreateTime(date);
            api.setLastmodifyTime(date);
            apiService.save(api);
            long deployid = api.getDeployunitid();
            Deployunit deployunit = deployunitService.getById(deployid);
            deployunit.setApicounts(deployunit.getApicounts() + 1);
            deployunitService.update(deployunit);

            Long ApiId = api.getId();
            //复制api参数
            for (ApiParams apiParams : apiParamsList) {
                apiParams.setApiid(ApiId);
                apiParams.setApiname(newapiname);
                apiParams.setDeployunitid(Long.parseLong(objectdeployunitid));
                apiParams.setDeployunitname(objectdeployunitname);
                apiParams.setId(null);
                apiParams.setCreateTime(date);
                apiParams.setLastmodifyTime(date);
                apiParamsService.save(apiParams);
            }
            return ResultGenerator.genOkResult();
        }
    }
}