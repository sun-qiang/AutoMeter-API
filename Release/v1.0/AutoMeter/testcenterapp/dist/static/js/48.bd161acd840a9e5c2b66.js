webpackJsonp([48],{aWE7:function(e,t,i){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=i("Dd8w"),s=i.n(a),n=i("W/rB"),l=i("0xDb"),m=i("XcM5"),o=i("NYxO"),r=i("ggxm"),c=i("4I8B"),d=i("L6vm"),p=i("mKAx"),u={name:"测试环境",filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{dynamicWidth:0,id:null,itemKey:null,serviceitemKey:null,assembleitemKey:null,tmpserviceenviromentid:"",tmpservicedeployunitid:"",tmpservicemachinename:"",tmpservicedeployunitname:"",tmpenviromentname:"",tmpassemblemachinename:"",tmpassembleasname:"",enviromentList:[],machinenameList:[],macdepunitList:[],macassembleList:[],assembleypeList:[],listLoading:!1,total:0,servicetotal:0,assembletotal:0,dialogStatus:"add",deployservicedialogStatus:"add",deployassembledialogStatus:"add",deployunitVisible:!1,assembleVisible:!1,domianVisible:!1,dialogFormVisible:!1,adddeployservicedialogFormVisible:!1,adddeployassembledialogFormVisible:!1,AssembleDeployFormVisible:!1,ServiceDeployFormVisible:!1,textMap:{updateRole:"修改环境",update:"修改环境",add:"添加环境"},deployservicetextMap:{updateRole:"修改微服务配置",update:"修改微服务配置",add:"添加微服务配置"},deployassembletextMap:{updateRole:"修改组件配置",update:"修改组件配置",add:"添加组件配置"},diclevelQuery:{page:1,size:100,diccode:"machinedeploy"},btnLoading:!1,tmpenviroment:{id:"",enviromentname:"",envtype:"",memo:"",creator:"",projectid:""},tmpmacdepunit:{id:"",envid:"",machineid:"",depunitid:"",assembleid:"",assembletype:"微服务",enviromentname:"",machinename:"",deployunitname:"",domain:"",visittype:"",creator:"",projectid:""},tmpmacassemble:{id:"",envid:"",machineid:"",depunitid:"",assembleid:"",assemblename:"",assembletype:"组件",enviromentname:"",machinename:"",deployunitname:"",connectstr:"",domain:"",visittype:"",creator:"",memo:"",projectid:""},search:{page:1,size:10,enviromentname:null,creator:"",accountId:null,projectid:""},servicesearch:{page:1,size:10,machinename:null,depunitid:null,envid:null,deployunitname:null,assembletype:"微服务",projectid:""},assemblesearch:{page:1,size:10,machinename:null,depunitid:null,envid:null,deployunitname:null,assembletype:"组件",projectid:""},tmptest:{machineid:"",machinename:"",visittype:"",assembletype:"",domain:"",constr:""},searchproject:{projectid:""}}},created:function(){this.search.accountId=this.accountId,this.search.projectid=window.localStorage.getItem("pid"),this.searchproject.projectid=window.localStorage.getItem("pid"),this.servicesearch.projectid=window.localStorage.getItem("pid"),this.assemblesearch.projectid=window.localStorage.getItem("pid"),this.tmpmacdepunit.projectid=window.localStorage.getItem("pid"),this.tmpmacassemble.projectid=window.localStorage.getItem("pid"),this.getenviromentList(),this.getmachineLists(),this.getdepunitLists()},activated:function(){this.getmachineLists(),this.getdepunitLists()},updated:function(){this.dynamicWidth=this.$getOperatorWidth()},computed:s()({},Object(o.b)(["name","sidebar","projectlist","projectid","accountId"])),methods:{renderHeader:function(e,t){var i=t.column,a=(t.$index,document.getElementsByClassName("optionDiv")),s=[];return Array.prototype.forEach.call(a,function(e){e.innerText&&s.push(e.offsetWidth)}),s.length>0?(i.width=Math.max.apply(Math,s)+24,e("span",i.label)):(i.width=0,e("span",i.label))},unix2CurrentTime:l.b,runtest:function(e){var t=this;this.tmptest.machinename=this.macassembleList[e].machinename,this.tmptest.visittype=this.macassembleList[e].visittype,this.tmptest.domain=this.macassembleList[e].domain,this.tmptest.assembletype=this.macassembleList[e].assembletype,this.tmptest.constr=this.macassembleList[e].connectstr,this.tmptest.machineid=this.macassembleList[e].machineid,Object(p.d)(this.tmptest).then(function(){t.$message.success("连接成功！")}).catch(function(e){t.$message.error("添加失败")})},getdatabydiccodeList:function(){var e=this;Object(d.b)(this.diclevelQuery).then(function(t){e.assembleypeList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("加载中间件名字典列表失败")})},showUpdatemacdepunitDialog:function(e){this.adddeployservicedialogFormVisible=!0,this.deployservicedialogStatus="update",this.tmpmacdepunit.id=this.macdepunitList[e].id,this.tmpmacdepunit.envid=this.macdepunitList[e].envid,this.tmpmacdepunit.machineid=this.macdepunitList[e].machineid,this.tmpmacdepunit.depunitid=this.macdepunitList[e].depunitid,this.tmpmacdepunit.machinename=this.macdepunitList[e].machinename,this.tmpmacdepunit.enviromentname=this.macdepunitList[e].enviromentname,this.tmpmacdepunit.deployunitname=this.macdepunitList[e].deployunitname,this.tmpmacdepunit.assembletype=this.macdepunitList[e].assembletype,this.tmpmacdepunit.domain=this.macdepunitList[e].domain,this.tmpmacdepunit.assembleid=this.macdepunitList[e].assembleid,this.tmpmacdepunit.visittype=this.macdepunitList[e].visittype,this.tmpmacdepunit.creator=this.name,"ip"===this.tmpmacdepunit.visittype&&(this.domianVisible=!1,this.tmpmacdepunit.domain="/"),"域名"===this.tmpmacdepunit.visittype&&(this.domianVisible=!0)},showUpdatemacassembleDialog:function(e){this.adddeployassembledialogFormVisible=!0,this.deployassembledialogStatus="update",this.tmpmacassemble.id=this.macassembleList[e].id,this.tmpmacassemble.envid=this.macassembleList[e].envid,this.tmpmacassemble.machineid=this.macassembleList[e].machineid,this.tmpmacassemble.depunitid=this.macassembleList[e].depunitid,this.tmpmacassemble.machinename=this.macassembleList[e].machinename,this.tmpmacassemble.enviromentname=this.macassembleList[e].enviromentname,this.tmpmacassemble.deployunitname=this.macassembleList[e].deployunitname,this.tmpmacassemble.assembletype=this.macassembleList[e].assembletype,this.tmpmacassemble.domain=this.macassembleList[e].domain,this.tmpmacassemble.assembleid=this.macassembleList[e].assembleid,this.tmpmacassemble.visittype=this.macassembleList[e].visittype,this.tmpmacassemble.assemblename=this.macassembleList[e].assemblename,this.tmpmacassemble.connectstr=this.macassembleList[e].connectstr,this.tmpmacassemble.creator=this.name,"ip"===this.tmpmacassemble.visittype&&(this.domianVisible=!1,this.tmpmacassemble.domain="/"),"域名"===this.tmpmacassemble.visittype&&(this.domianVisible=!0),this.getdatabydiccodeList()},getdepunitLists:function(){var e=this;Object(m.e)(this.searchproject).then(function(t){e.deployUnitList=t.data,console.log(e.deployunitList)}).catch(function(t){e.$message.error("加载微服务列表失败")})},selectChangedVisittype:function(e){"域名"===e&&(this.domianVisible=!0,this.tmpmacdepunit.domain=""),"ip"===e&&(this.domianVisible=!1,this.tmpmacdepunit.domain="/")},selectChangedAandD:function(e){"组件"===e&&(this.deployunitVisible=!1,this.assembleVisible=!0),"微服务"===e&&(this.assembleVisible=!1,this.deployunitVisible=!0,this.domianVisible=!1),this.tmpmacdepunit.deployunitname="",this.tmpmacdepunit.assembleid="",this.tmpmacdepunit.depunitid="",this.tmpmacdepunit.visittype="",this.tmpmacdepunit.domain=""},selectChangedMN:function(e){for(var t=0;t<this.machinenameList.length;t++)this.machinenameList[t].machinename===e&&(this.tmpmacdepunit.machineid=this.machinenameList[t].id),console.log(this.machinenameList[t].id)},AssembleselectChangedMN:function(e){for(var t=0;t<this.machinenameList.length;t++)this.machinenameList[t].machinename===e&&(this.tmpmacassemble.machineid=this.machinenameList[t].id)},selectChangedDU:function(e){for(var t=0;t<this.deployUnitList.length;t++)this.deployUnitList[t].deployunitname===e&&(this.tmpmacdepunit.depunitid=this.deployUnitList[t].id);this.tmpmacdepunit.assembleid=""},selectChangedAS:function(e){for(var t=0;t<this.assembleList.length;t++)this.assembleList[t].assemblename===e&&(this.tmpmacdepunit.assembleid=this.assembleList[t].id);this.tmpmacdepunit.depunitid=""},getmachineLists:function(){var e=this;Object(c.b)(this.searchproject).then(function(t){e.machinenameList=t.data}).catch(function(t){e.$message.error("加载服务器列表失败")})},removemacdepunit:function(e){var t=this;this.$confirm("删除该微服务？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var i=t.macdepunitList[e].id;Object(r.f)(i).then(function(){t.$message.success("删除成功"),t.getmacdepunitList()})}).catch(function(){t.$message.info("已取消删除")})},removemacassemble:function(e){var t=this;this.$confirm("删除该组件？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){t.tmpmacassemble.id=t.macassembleList[e].id,t.tmpmacassemble.assembleid=t.macassembleList[e].assembleid,Object(r.c)(t.tmpmacassemble).then(function(){t.$message.success("删除成功"),t.getmacassembleList()})}).catch(function(){t.$message.info("已取消删除")})},updatemacdepunit:function(){var e=this;this.$refs.tmpmacdepunit.validate(function(t){t&&("ip"===e.tmpmacdepunit.visittype&&(e.tmpmacdepunit.domain=""),"组件"===e.tmpmacdepunit.assembletype&&(e.tmpmacdepunit.depunitid=""),"微服务"===e.tmpmacdepunit.assembletype&&(e.tmpmacdepunit.assembleid=""),Object(r.i)(e.tmpmacdepunit).then(function(){e.$message.success("更新成功"),e.getmacdepunitList(),e.adddeployservicedialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")}))})},updatemacassemble:function(){var e=this;this.$refs.tmpmacassemble.validate(function(t){t&&("ip"===e.tmpmacassemble.visittype&&(e.tmpmacassemble.domain=""),"组件"===e.tmpmacassemble.assembletype&&(e.tmpmacassemble.depunitid=""),Object(r.h)(e.tmpmacassemble).then(function(){e.$message.success("更新成功"),e.getmacassembleList(),e.adddeployassembledialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")}))})},addmacdepunit:function(){var e=this;this.$refs.tmpmacdepunit.validate(function(t){t&&Object(r.b)(e.tmpmacdepunit).then(function(){e.$message.success("添加成功"),e.getmacdepunitList(),e.adddeployservicedialogFormVisible=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1})})},addmacassemble:function(){var e=this;this.$refs.tmpmacassemble.validate(function(t){t&&Object(r.a)(e.tmpmacassemble).then(function(){e.$message.success("添加成功"),e.getmacassembleList(),e.adddeployassembledialogFormVisible=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1})})},getmacdepunitList:function(){var e=this;this.servicesearch.machinename=this.tmpservicemachinename,this.servicesearch.deployunitname=this.tmpservicedeployunitname,Object(r.e)(this.servicesearch).then(function(t){e.macdepunitList=t.data.list,e.servicetotal=t.data.total}).catch(function(t){e.$message.error("加载环境配置列表失败")})},searchserviceBy:function(){var e=this;this.servicesearch.page=1,Object(r.e)(this.servicesearch).then(function(t){e.serviceitemKey=Math.random(),e.macdepunitList=t.data.list,e.servicetotal=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmpservicemachinename=this.servicesearch.machinename,this.tmpservicedeployunitname=this.servicesearch.deployunitname},getmacassembleList:function(){var e=this;this.assemblesearch.machinename=this.tmpassemblemachinename,this.assemblesearch.deployunitname=this.tmpassembleasname,Object(r.d)(this.assemblesearch).then(function(t){e.macassembleList=t.data.list,e.assembletotal=t.data.total}).catch(function(t){e.$message.error("加载环境配置列表失败")})},searchassembleBy:function(){var e=this;this.assemblesearch.page=1,Object(r.d)(this.assemblesearch).then(function(t){e.assembleitemKey=Math.random(),e.macassembleList=t.data.list,e.assembletotal=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmpassemblemachinename=this.assemblesearch.machinename,this.tmpassembleasname=this.assemblesearch.deployunitname},getenviromentList:function(){var e=this;this.listLoading=!0,this.search.enviromentname=this.tmpenviromentname,this.search.creator=this.name,Object(n.f)(this.search).then(function(t){e.enviromentList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载环境列表失败")})},searchBy:function(){var e=this;this.search.page=1,this.listLoading=!0,Object(n.f)(this.search).then(function(t){e.itemKey=Math.random(),e.enviromentList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmpenviromentname=this.search.enviromentname,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(e){this.search.page=1,this.search.size=e,this.getenviromentList()},servicehandleSizeChange:function(e){this.servicesearch.page=1,this.servicesearch.size=e,this.getmacdepunitList()},assemblehandleSizeChange:function(e){this.assemblesearch.page=1,this.assemblesearch.size=e,this.getmacassembleList()},handleCurrentChange:function(e){this.search.page=e,this.getenviromentList()},servicehandleCurrentChange:function(e){this.servicesearch.page=e,this.getmacdepunitList()},assemblehandleCurrentChange:function(e){this.assemblesearch.page=e,this.getmacassembleList()},getIndex:function(e){return(this.search.page-1)*this.search.size+e+1},servicegetIndex:function(e){return(this.servicesearch.page-1)*this.servicesearch.size+e+1},assemblegetIndex:function(e){return(this.assemblesearch.page-1)*this.assemblesearch.size+e+1},showAddenviromentDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpenviroment.id="",this.tmpenviroment.enviromentname="",this.tmpenviroment.memo="",this.tmpenviroment.envtype="",this.tmpenviroment.creator=this.name,this.tmpenviroment.projectid=window.localStorage.getItem("pid")},addenviroment:function(){var e=this;this.$refs.tmpenviroment.validate(function(t){t&&(e.btnLoading=!0,Object(n.a)(e.tmpenviroment).then(function(){e.$message.success("添加成功"),e.getenviromentList(),e.dialogFormVisible=!1,e.btnLoading=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1}))})},showUpdateenviromentDialog:function(e){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpenviroment.id=this.enviromentList[e].id,this.tmpenviroment.enviromentname=this.enviromentList[e].enviromentname,this.tmpenviroment.envtype=this.enviromentList[e].envtype,this.tmpenviroment.memo=this.enviromentList[e].memo,this.tmpenviroment.creator=this.name,this.tmpenviroment.projectid=window.localStorage.getItem("pid")},showdeployserviceDialog:function(e){this.ServiceDeployFormVisible=!0,this.tmpmacdepunit.envid=this.enviromentList[e].id,this.tmpmacdepunit.enviromentname=this.enviromentList[e].enviromentname,this.servicesearch.envid=this.enviromentList[e].id,this.servicesearch.machinename="",this.servicesearch.deployunitname="",this.getmacdepunitList()},showdeployassemblyDialog:function(e){this.AssembleDeployFormVisible=!0,this.tmpmacassemble.envid=this.enviromentList[e].id,this.tmpmacassemble.enviromentname=this.enviromentList[e].enviromentname,this.assemblesearch.envid=this.enviromentList[e].id,this.assemblesearch.machinename="",this.assemblesearch.deployunitname="",this.getmacassembleList()},showadddeployserviceDialog:function(){this.adddeployservicedialogFormVisible=!0,this.deployservicedialogStatus="add",this.tmpmacdepunit.id="",this.tmpmacdepunit.machineid="",this.tmpmacdepunit.assembleid="",this.tmpmacdepunit.depunitid="",this.tmpmacdepunit.machinename="",this.tmpmacdepunit.deployunitname="",this.tmpmacdepunit.assembletype="微服务",this.tmpmacdepunit.domain="",this.tmpmacdepunit.visittype="",this.tmpmacdepunit.creator=this.name,this.deployunitVisible=!1,this.assembleVisible=!1,this.domianVisible=!1},showadddeployassembleDialog:function(){this.adddeployassembledialogFormVisible=!0,this.deployassembledialogStatus="add",this.tmpmacassemble.id="",this.tmpmacassemble.machineid="",this.tmpmacassemble.assemblename="",this.tmpmacassemble.assembleid="",this.tmpmacassemble.depunitid="",this.tmpmacassemble.machinename="",this.tmpmacassemble.deployunitname="",this.tmpmacassemble.assembletype="",this.tmpmacassemble.connectstr="",this.tmpmacassemble.domain="",this.tmpmacassemble.visittype="",this.tmpmacassemble.creator=this.name,this.getdatabydiccodeList()},updateenviroment:function(){var e=this;this.$refs.tmpenviroment.validate(function(t){t&&Object(n.g)(e.tmpenviroment).then(function(){e.$message.success("更新成功"),e.getenviromentList(),e.dialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")})})},removeenviroment:function(e){var t=this;this.$confirm("删除该测试环境？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var i=t.enviromentList[e].id;Object(n.e)(i).then(function(){t.$message.success("删除成功"),t.getenviromentList()})}).catch(function(){t.$message.info("已取消删除")})},isUniqueDetail:function(e){for(var t=0;t<this.enviromentList.length;t++)if(this.enviromentList[t].id!==e.id&&this.enviromentList[t].enviromentname===e.enviromentname)return this.$message.error("测试环境名已存在"),!1;return!0}}},h={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"app-container"},[i("div",{staticClass:"filter-container"},[i("el-form",{staticStyle:{width:"900px"},attrs:{inline:!0}},[i("el-form-item",[e.hasPermission("enviroment:list")?i("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(t){return t.preventDefault(),e.getenviromentList(t)}}},[e._v("刷新\n        ")]):e._e(),e._v(" "),e.hasPermission("enviroment:add")?i("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(t){return t.preventDefault(),e.showAddenviromentDialog(t)}}},[e._v("添加测试环境\n        ")]):e._e()],1),e._v(" "),e.hasPermission("enviroment:search")?i("span",[i("el-form-item",[i("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"测试环境名"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchBy(t)}},model:{value:e.search.enviromentname,callback:function(t){e.$set(e.search,"enviromentname",t)},expression:"search.enviromentname"}})],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchBy}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),i("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.enviromentList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[i("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[i("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"测试环境名","show-overflow-tooltip":!0,align:"center",prop:"enviromentname",width:"200"}}),e._v(" "),i("el-table-column",{attrs:{label:"环境类型",align:"center",prop:"envtype",width:"80"}}),e._v(" "),i("el-table-column",{attrs:{label:"描述",align:"center","show-overflow-tooltip":!0,prop:"memo",width:"130"}}),e._v(" "),i("el-table-column",{attrs:{label:"维护人",align:"center",prop:"creator",width:"80"}}),e._v(" "),i("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"140"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"140"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n      ")]}}])}),e._v(" "),e.hasPermission("enviroment:update")||e.hasPermission("enviroment:delete")?i("el-table-column",{attrs:{label:"管理",align:"center",width:"390"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.hasPermission("enviroment:update")&&t.row.id!==e.id?i("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.showUpdateenviromentDialog(t.$index)}}},[e._v("修改\n        ")]):e._e(),e._v(" "),e.hasPermission("enviroment:delete")&&t.row.id!==e.id?i("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.removeenviroment(t.$index)}}},[e._v("删除\n        ")]):e._e(),e._v(" "),e.hasPermission("enviroment:update")&&t.row.id!==e.id?i("el-button",{attrs:{type:"primary",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.showdeployserviceDialog(t.$index)}}},[e._v("配置微服务\n        ")]):e._e(),e._v(" "),e.hasPermission("enviroment:update")&&t.row.id!==e.id?i("el-button",{attrs:{type:"primary",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.showdeployassemblyDialog(t.$index)}}},[e._v("配置中间件\n        ")]):e._e()]}}],null,!1,3039606600)}):e._e()],1),e._v(" "),i("el-pagination",{attrs:{"current-page":e.search.page,"page-size":e.search.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),i("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],width:"800px",visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[i("el-form",{ref:"tmpenviroment",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpenviroment}},[i("el-form-item",{attrs:{label:"测试环境名",prop:"enviromentname",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:e.tmpenviroment.enviromentname,callback:function(t){e.$set(e.tmpenviroment,"enviromentname",t)},expression:"tmpenviroment.enviromentname"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"环境类型",prop:"envtype",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"环境类型"},model:{value:e.tmpenviroment.envtype,callback:function(t){e.$set(e.tmpenviroment,"envtype",t)},expression:"tmpenviroment.envtype"}},[i("el-option",{attrs:{label:"功能",value:"功能"}}),e._v(" "),i("el-option",{attrs:{label:"性能",value:"性能"}})],1)],1),e._v(" "),i("el-form-item",{attrs:{label:"备注",prop:"memo"}},[i("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpenviroment.memo,callback:function(t){e.$set(e.tmpenviroment,"memo",t)},expression:"tmpenviroment.memo"}})],1)],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.dialogStatus?i("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpenviroment.resetFields()}}},[e._v("重置\n      ")]):e._e(),e._v(" "),"add"===e.dialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addenviroment(t)}}},[e._v("添加\n      ")]):e._e(),e._v(" "),"update"===e.dialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updateenviroment(t)}}},[e._v("修改\n      ")]):e._e()],1)],1),e._v(" "),i("el-dialog",{attrs:{title:e.deployservicetextMap[e.deployservicedialogStatus],width:"800px",visible:e.adddeployservicedialogFormVisible},on:{"update:visible":function(t){e.adddeployservicedialogFormVisible=t}}},[i("el-form",{ref:"tmpmacdepunit",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpmacdepunit}},[i("el-form-item",{attrs:{label:"服务器",prop:"machinename",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{filterable:"",placeholder:"服务器"},on:{change:function(t){return e.selectChangedMN(t)}},model:{value:e.tmpmacdepunit.machinename,callback:function(t){e.$set(e.tmpmacdepunit,"machinename",t)},expression:"tmpmacdepunit.machinename"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.machinenameList,function(e,t){return i("div",{key:t},[i("el-option",{attrs:{label:e.machinename+" ："+e.ip,value:e.machinename,required:""}})],1)})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"微服务",prop:"deployunitname",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{filterable:"",placeholder:"微服务"},on:{change:function(t){return e.selectChangedDU(t)}},model:{value:e.tmpmacdepunit.deployunitname,callback:function(t){e.$set(e.tmpmacdepunit,"deployunitname",t)},expression:"tmpmacdepunit.deployunitname"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.deployUnitList,function(e,t){return i("div",{key:t},[i("el-option",{attrs:{label:e.deployunitname,value:e.deployunitname,required:""}})],1)})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"访问方式",prop:"visittype",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"访问方式"},on:{change:function(t){return e.selectChangedVisittype(t)}},model:{value:e.tmpmacdepunit.visittype,callback:function(t){e.$set(e.tmpmacdepunit,"visittype",t)},expression:"tmpmacdepunit.visittype"}},[i("el-option",{attrs:{label:"ip",value:"ip"}}),e._v(" "),i("el-option",{attrs:{label:"域名",value:"域名"}})],1)],1),e._v(" "),e.domianVisible?i("div",[i("el-form-item",{attrs:{label:"访问域名",prop:"domain",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{placeholder:"访问域名",required:""},model:{value:e.tmpmacdepunit.domain,callback:function(t){e.$set(e.tmpmacdepunit,"domain",t)},expression:"tmpmacdepunit.domain"}})],1)],1):e._e(),e._v(" "),e.assembleVisible?i("div",[i("el-form-item",{attrs:{label:"组件",prop:"deployunitname",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{filterable:"",placeholder:"组件"},on:{change:function(t){return e.selectChangedAS(t)}},model:{value:e.tmpmacdepunit.deployunitname,callback:function(t){e.$set(e.tmpmacdepunit,"deployunitname",t)},expression:"tmpmacdepunit.deployunitname"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.assembleList,function(e,t){return i("div",{key:t},[i("el-option",{attrs:{label:e.assemblename,value:e.assemblename,required:""}})],1)})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"访问方式",prop:"visittype",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"访问方式"},on:{change:function(t){return e.selectChangedVisittype(t)}},model:{value:e.tmpmacdepunit.visittype,callback:function(t){e.$set(e.tmpmacdepunit,"visittype",t)},expression:"tmpmacdepunit.visittype"}},[i("el-option",{attrs:{label:"ip",value:"ip"}}),e._v(" "),i("el-option",{attrs:{label:"域名",value:"域名"}})],1)],1),e._v(" "),e.domianVisible?i("div",[i("el-form-item",{attrs:{label:"访问域名",prop:"domain",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"110",placeholder:"访问域名",required:""},model:{value:e.tmpmacdepunit.domain,callback:function(t){e.$set(e.tmpmacdepunit,"domain",t)},expression:"tmpmacdepunit.domain"}})],1)],1):e._e()],1):e._e()],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.adddeployservicedialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.deployservicedialogStatus?i("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpmacdepunit.resetFields()}}},[e._v("重置\n      ")]):e._e(),e._v(" "),"add"===e.deployservicedialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addmacdepunit(t)}}},[e._v("添加\n      ")]):e._e(),e._v(" "),"update"===e.deployservicedialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updatemacdepunit(t)}}},[e._v("修改\n      ")]):e._e()],1)],1),e._v(" "),i("el-dialog",{attrs:{title:"微服务配置",width:"1100px",visible:e.ServiceDeployFormVisible},on:{"update:visible":function(t){e.ServiceDeployFormVisible=t}}},[i("div",{staticClass:"filter-container"},[i("el-form",{attrs:{inline:!0}},[i("el-form-item",[e.hasPermission("enviroment:search")?i("span",[i("el-form-item",{attrs:{label:"服务器:"}},[i("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"服务器"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchserviceBy(t)}},model:{value:e.servicesearch.machinename,callback:function(t){e.$set(e.servicesearch,"machinename",t)},expression:"servicesearch.machinename"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"微服务:"}},[i("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"微服务"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchserviceBy(t)}},model:{value:e.servicesearch.deployunitname,callback:function(t){e.$set(e.servicesearch,"deployunitname",t)},expression:"servicesearch.deployunitname"}})],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.searchserviceBy}},[e._v("查询")])],1)],1):e._e(),e._v(" "),e.hasPermission("enviroment:add")?i("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(t){return t.preventDefault(),e.showadddeployserviceDialog(t)}}},[e._v("配置微服务\n          ")]):e._e()],1)],1)],1),e._v(" "),i("el-table",{key:e.serviceitemKey,attrs:{data:e.macdepunitList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[i("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[i("span",{domProps:{textContent:e._s(e.servicegetIndex(t.$index))}})]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"环境","show-overflow-tooltip":!0,align:"center",prop:"enviromentname",width:"140"}}),e._v(" "),i("el-table-column",{attrs:{label:"服务器","show-overflow-tooltip":!0,align:"center",prop:"machinename",width:"130"}}),e._v(" "),i("el-table-column",{attrs:{label:"微服务","show-overflow-tooltip":!0,align:"center",prop:"deployunitname",width:"100"}}),e._v(" "),i("el-table-column",{attrs:{label:"访问方式",align:"center",prop:"visittype",width:"70"}}),e._v(" "),i("el-table-column",{attrs:{label:"访问域名","show-overflow-tooltip":!0,align:"center",prop:"domain",width:"100"}}),e._v(" "),i("el-table-column",{attrs:{label:"创建时间","show-overflow-tooltip":!0,align:"center",prop:"createTime",width:"140"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"最后修改时间","show-overflow-tooltip":!0,align:"center",prop:"lastmodifyTime",width:"140"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n        ")]}}])}),e._v(" "),e.hasPermission("macdepunit:update")||e.hasPermission("macdepunit:delete")?i("el-table-column",{attrs:{label:"管理",align:"center",width:"190"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.hasPermission("macdepunit:update")&&t.row.id!==e.id?i("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.showUpdatemacdepunitDialog(t.$index)}}},[e._v("修改\n          ")]):e._e(),e._v(" "),e.hasPermission("macdepunit:delete")&&t.row.id!==e.id?i("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.removemacdepunit(t.$index)}}},[e._v("删除\n          ")]):e._e()]}}],null,!1,1185958694)}):e._e()],1),e._v(" "),i("el-pagination",{attrs:{"current-page":e.servicesearch.page,"page-size":e.servicesearch.size,total:e.servicetotal,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.servicehandleSizeChange,"current-change":e.servicehandleCurrentChange}})],1),e._v(" "),i("el-dialog",{attrs:{title:"组件配置",width:"1230px",visible:e.AssembleDeployFormVisible},on:{"update:visible":function(t){e.AssembleDeployFormVisible=t}}},[i("div",{staticClass:"filter-container"},[i("el-form",{attrs:{inline:!0}},[i("el-form-item",[e.hasPermission("enviroment:search")?i("span",[i("el-form-item",{attrs:{label:"服务器:"}},[i("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"服务器"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchassembleBy(t)}},model:{value:e.assemblesearch.machinename,callback:function(t){e.$set(e.assemblesearch,"machinename",t)},expression:"assemblesearch.machinename"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"组件:"}},[i("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"组件"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchassembleBy(t)}},model:{value:e.assemblesearch.deployunitname,callback:function(t){e.$set(e.assemblesearch,"deployunitname",t)},expression:"assemblesearch.deployunitname"}})],1),e._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary"},on:{click:e.searchassembleBy}},[e._v("查询")])],1)],1):e._e(),e._v(" "),e.hasPermission("enviroment:add")?i("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(t){return t.preventDefault(),e.showadddeployassembleDialog(t)}}},[e._v("组件配置\n          ")]):e._e()],1)],1)],1),e._v(" "),i("el-table",{key:e.assembleitemKey,attrs:{data:e.macassembleList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[i("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[i("span",{domProps:{textContent:e._s(e.assemblegetIndex(t.$index))}})]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"环境",align:"center",prop:"enviromentname",width:"100"}}),e._v(" "),i("el-table-column",{attrs:{label:"服务器",align:"center",prop:"machinename",width:"90"}}),e._v(" "),i("el-table-column",{attrs:{label:"组件","show-overflow-tooltip":!0,align:"center",prop:"deployunitname",width:"70"}}),e._v(" "),i("el-table-column",{attrs:{label:"组件类型",align:"center",prop:"assembletype",width:"70"}}),e._v(" "),i("el-table-column",{attrs:{label:"连接字","show-overflow-tooltip":!0,align:"center",prop:"connectstr",width:"120"}}),e._v(" "),i("el-table-column",{attrs:{label:"访问方式",align:"center",prop:"visittype",width:"70"}}),e._v(" "),i("el-table-column",{attrs:{label:"域名","show-overflow-tooltip":!0,align:"center",prop:"domain",width:"80"}}),e._v(" "),i("el-table-column",{attrs:{label:"创建时间","show-overflow-tooltip":!0,align:"center",prop:"createTime",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),i("el-table-column",{attrs:{label:"最后修改时间","show-overflow-tooltip":!0,align:"center",prop:"lastmodifyTime",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n        ")]}}])}),e._v(" "),e.hasPermission("macdepunit:update")||e.hasPermission("macdepunit:delete")?i("el-table-column",{attrs:{label:"管理",align:"center","min-width":e.dynamicWidth},scopedSlots:e._u([{key:"default",fn:function(t){return[i("div",{staticClass:"optionDiv",staticStyle:{"white-space":"nowrap",display:"inline-block"}},[i("el-button",{attrs:{type:"primary",loading:e.btnLoading},nativeOn:{click:function(i){return i.preventDefault(),e.runtest(t.$index)}}},[e._v("测试")]),e._v(" "),e.hasPermission("macdepunit:update")&&t.row.id!==e.id?i("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.showUpdatemacassembleDialog(t.$index)}}},[e._v("修改\n          ")]):e._e(),e._v(" "),e.hasPermission("macdepunit:delete")&&t.row.id!==e.id?i("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(i){return i.preventDefault(),e.removemacassemble(t.$index)}}},[e._v("删除\n          ")]):e._e()],1)]}}],null,!1,1595457537)}):e._e()],1),e._v(" "),i("el-pagination",{attrs:{"current-page":e.assemblesearch.page,"page-size":e.assemblesearch.size,total:e.assembletotal,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.assemblehandleSizeChange,"current-change":e.assemblehandleCurrentChange}})],1),e._v(" "),i("el-dialog",{attrs:{title:e.deployassembletextMap[e.deployassembledialogStatus],width:"800px",visible:e.adddeployassembledialogFormVisible},on:{"update:visible":function(t){e.adddeployassembledialogFormVisible=t}}},[i("el-form",{ref:"tmpmacassemble",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpmacassemble}},[i("el-form-item",{attrs:{label:"中间件名",prop:"assemblename",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{type:"text",maxlength:"60","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpmacassemble.assemblename,callback:function(t){e.$set(e.tmpmacassemble,"assemblename",t)},expression:"tmpmacassemble.assemblename"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"服务器",prop:"machinename",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{filterable:"",placeholder:"服务器"},on:{change:function(t){return e.AssembleselectChangedMN(t)}},model:{value:e.tmpmacassemble.machinename,callback:function(t){e.$set(e.tmpmacassemble,"machinename",t)},expression:"tmpmacassemble.machinename"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.machinenameList,function(e,t){return i("div",{key:t},[i("el-option",{attrs:{label:e.machinename+" ："+e.ip,value:e.machinename,required:""}})],1)})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"中间件类型",prop:"assembletype",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"中间件类型"},on:{change:function(t){return e.selectChanged(t)}},model:{value:e.tmpmacassemble.assembletype,callback:function(t){e.$set(e.tmpmacassemble,"assembletype",t)},expression:"tmpmacassemble.assembletype"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.assembleypeList,function(e,t){return i("div",{key:t},[i("el-option",{attrs:{label:e.dicitmevalue,value:e.dicitmevalue,required:""}})],1)})],2)],1),e._v(" "),i("el-form-item",{attrs:{label:"连接字",prop:"connectstr",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{type:"textarea",maxlength:"60","prefix-icon":"el-icon-message","auto-complete":"off",placeholder:"账号，密码，端口，库名 用英文逗号分开，例子：root,root,3306,admin"},model:{value:e.tmpmacassemble.connectstr,callback:function(t){e.$set(e.tmpmacassemble,"connectstr",t)},expression:"tmpmacassemble.connectstr"}})],1),e._v(" "),i("el-form-item",{attrs:{label:"访问方式",prop:"visittype",required:""}},[i("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"访问方式"},on:{change:function(t){return e.selectChangedVisittype(t)}},model:{value:e.tmpmacassemble.visittype,callback:function(t){e.$set(e.tmpmacassemble,"visittype",t)},expression:"tmpmacassemble.visittype"}},[i("el-option",{attrs:{label:"ip",value:"ip"}}),e._v(" "),i("el-option",{attrs:{label:"域名",value:"域名"}})],1)],1),e._v(" "),e.domianVisible?i("div",[i("el-form-item",{attrs:{label:"访问域名",prop:"domain",required:""}},[i("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"110",placeholder:"访问域名",required:""},model:{value:e.tmpmacassemble.domain,callback:function(t){e.$set(e.tmpmacassemble,"domain",t)},expression:"tmpmacassemble.domain"}})],1)],1):e._e()],1),e._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.adddeployassembledialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.deployassembledialogStatus?i("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpmacassemble.resetFields()}}},[e._v("重置\n      ")]):e._e(),e._v(" "),"add"===e.deployassembledialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addmacassemble(t)}}},[e._v("添加\n      ")]):e._e(),e._v(" "),"update"===e.deployassembledialogStatus?i("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updatemacassemble(t)}}},[e._v("修改\n      ")]):e._e()],1)],1)],1)},staticRenderFns:[]},v=i("VU/8")(u,h,!1,null,null,null);t.default=v.exports}});
//# sourceMappingURL=48.bd161acd840a9e5c2b66.js.map