webpackJsonp([13],{SYIK:function(e,t,a){"use strict";t.c=function(e){return Object(n.a)({url:"/apicases/report/performance",method:"get",params:e})},t.i=function(e){return Object(n.a)({url:"/apicases/report/performance/search",method:"post",data:e})},t.b=function(e){return Object(n.a)({url:"/apicases/report/performance/findApicasereportWithNameandStatus",method:"post",data:e})},t.f=function(e){return Object(n.a)({url:"/apicases/report/performance/getperformancecasestatics",method:"post",data:e})},t.e=function(e){return Object(n.a)({url:"/apicases/report/performance/getperformanceallstatics",method:"post",data:e})},t.g=function(e){return Object(n.a)({url:"/apicases/report/performance/getperformanceslaverstatics",method:"post",data:e})},t.d=function(e){return Object(n.a)({url:"/apicases/report/performance/getperformanceCaseSandF",method:"post",data:e})},t.a=function(e){return Object(n.a)({url:"/apicases/report/performance",method:"post",data:e})},t.j=function(e){return Object(n.a)({url:"/apicases/report/performance",method:"put",data:e})},t.h=function(e){return Object(n.a)({url:"/apicases/report/performance/"+e,method:"delete"})};var n=a("vLgD")},d4Dj:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("Xxa5"),r=a.n(n),i=a("exGp"),l=a.n(i),s=a("Dd8w"),o=a.n(s),c=a("zXKF"),p=a("SYIK"),u=a("bR3O"),d=a("W5b6"),m=a("vuyV"),h=a("0xDb"),b=a("NYxO"),g={name:"集合性能报告",filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{execplanbatchtotal:0,searchexecplanbatch:{page:1,size:10,executeplanname:null,batchname:null,projectid:""},executeplanbatchList:[],activeName:"zero",chartPie:null,seriesData:[],casestaticsList:[],performancecasesstaticsList:[],salvercaseList:[],caseconditionreport:[],dispatchdata:[],itemKey:null,tmptestplanname:"",tmptestplanid:null,tmpbatchname:null,apireportList:[],apiList:[],planbatchList:[],execplanList:[],deployunitList:[],listLoading:!1,dicvisitypeQuery:{page:1,size:30,diccode:"httpvisittype"},total:0,conditiontotal:0,dispatchtotal:0,listQuery:{page:1,size:10,listLoading:!0,testplanname:"",testplanid:null,batchname:null},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改apireport",update:"修改apireport",add:"添加apireport"},btnLoading:!1,tmpapireport:{executeplanid:"",id:"",deployunitid:"",deployunitname:"",batchname:"",requestheader:"",requestdatas:"",visittype:"",path:"",url:"",requestmethod:"",memo:""},tmpquery:{page:1,size:10,testplanname:"",executeplanid:"",batchid:"",batchname:"",caseststus:"",projectid:""},tmpconditionquery:{page:1,size:10,executeplanid:0,batchid:0,batchname:""},tmpdispatchquery:{page:1,size:10,executeplanid:0,batchid:0,batchname:""},tmpexecplantype:{usetype:"",projectid:""},search:{page:1,size:10,testplanname:"",testplanid:null,batchname:null,projectid:""}}},computed:o()({},Object(b.b)(["name","sidebar","projectlist","projectid"])),created:function(){this.search.projectid=window.localStorage.getItem("pid"),this.tmpexecplantype.projectid=window.localStorage.getItem("pid"),this.tmpquery.projectid=window.localStorage.getItem("pid"),this.getexecplanList()},activated:function(){this.getexecplanList(),this.testplanselectChanged(this.tmpquery.testplanname)},mounted:function(){this.drawLine()},methods:{unix2CurrentTime:h.b,getexecuteplanbatchList:function(){var e=this;Object(d.c)(this.searchexecplanbatch).then(function(t){e.executeplanbatchList=t.data.list,e.execplanbatchtotal=t.data.total}).catch(function(t){e.$message.error("加载执行计划批次列表失败")})},execplanbatchhandleSizeChange:function(e){this.searchexecplanbatch.page=1,this.searchexecplanbatch.size=e,this.getexecuteplanbatchList()},execplanbatchhandleCurrentChange:function(e){this.searchexecplanbatch.page=e,this.getexecuteplanbatchList()},drawLine:function(){var e,t=a("XLwt").init(document.getElementById("mian"));e={title:{text:"性能用例执行结果比例",left:"center"},tooltip:{trigger:"item",formatter:"{a} <br/>{b} : {c} ({d}%)"},color:["#44c23f","#d7212f"],series:[{name:"类型",type:"pie",radius:"75%",center:["50%","48%"],data:this.seriesData}]},t.setOption(e)},getapireportList:function(){var e=this;this.$refs.tmpquery.validate(function(t){t&&Object(p.i)(e.tmpquery).then(function(t){e.apireportList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("加载api报告列表失败")})})},findconditionreport:function(){var e=this;Object(u.b)(this.tmpconditionquery).then(function(t){e.caseconditionreport=t.data.list,e.conditiontotal=t.data.total}).catch(function(t){e.$message.error("加载api报告列表失败")})},getperformanceallstatics:function(){var e=this;Object(p.e)(this.tmpconditionquery).then(function(t){e.casestaticsList=t.data}).catch(function(t){e.$message.error("加载api报告列表失败")})},getperformanceslaverstatics:function(){var e=this;Object(p.g)(this.tmpconditionquery).then(function(t){e.salvercaseList=t.data}).catch(function(t){e.$message.error("加载api报告列表失败")})},getDispatchWithstatus:function(){var e=this;Object(c.b)(this.tmpdispatchquery).then(function(t){e.dispatchdata=t.data.list,e.dispatchtotal=t.data.total}).catch(function(t){e.$message.error("加载api报告列表失败")})},searchcaseReportBy:function(){var e=this;this.$refs.tmpquery.validate(function(t){t&&Object(p.b)(e.tmpquery).then(function(t){e.apireportList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("加载用例结果报告列表失败")})})},getexecplanList:function(){var e=this;this.tmpexecplantype.usetype="性能",Object(m.e)(this.tmpexecplantype).then(function(t){e.execplanList=t.data}).catch(function(t){e.$message.error("加载计划列表失败")})},getperformanceCaseSandF:function(){var e=this;return l()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,Object(p.d)(e.tmpquery).then(function(t){e.seriesData=t.data}).catch(function(e){});case 2:case"end":return t.stop()}},t,e)}))()},getperformancestatics:function(){var e=this;return l()(r.a.mark(function t(){return r.a.wrap(function(t){for(;;)switch(t.prev=t.next){case 0:e.activeName="first",e.$refs.tmpquery.validate(function(t){t&&(e.getapireportList(),e.getperformanceallstatics(),e.getperformanceslaverstatics(),e.getperformanceCaseSandF(),e.drawLine(),e.getperformancecasestatics(),e.findconditionreport(),e.getDispatchWithstatus(),e.searchexecplanbatch.executeplanname=e.tmpquery.testplanname,e.searchexecplanbatch.batchname=e.tmpquery.batchname,e.getexecuteplanbatchList())});case 2:case"end":return t.stop()}},t,e)}))()},getperformancecasestatics:function(){var e=this;this.$refs.tmpquery.validate(function(t){t&&Object(p.f)(e.tmpquery).then(function(t){e.performancecasesstaticsList=t.data.list}).catch(function(e){})})},testplanselectChanged:function(e){for(var t=this,a=0;a<this.execplanList.length;a++)this.execplanList[a].executeplanname===e&&(this.tmpquery.executeplanid=this.execplanList[a].id,this.tmpconditionquery.executeplanid=this.execplanList[a].id,this.tmpdispatchquery.executeplanid=this.execplanList[a].id);Object(d.a)(this.tmpquery).then(function(e){t.planbatchList=e.data}).catch(function(e){t.$message.error("加载执行计划列表失败")})},testbatchselectChanged:function(e){this.tmpquery.batchname=e;for(var t=0;t<this.planbatchList.length;t++)this.planbatchList[t].batchname===e&&(this.tmpquery.batchid=this.planbatchList[t].id,this.tmpconditionquery.batchid=this.planbatchList[t].id,console.log("-----------------------------------------------"),console.log(this.tmpconditionquery.batchid),this.tmpdispatchquery.batchid=this.planbatchList[t].id,this.tmpdispatchquery.batchname=e,this.tmpconditionquery.batchname=e)},getIndex:function(e){return(this.tmpquery.page-1)*this.tmpquery.size+e+1},conditiongetIndex:function(e){return(this.tmpconditionquery.page-1)*this.tmpconditionquery.size+e+1},dispatchgetIndex:function(e){return(this.tmpdispatchquery.page-1)*this.tmpdispatchquery.size+e+1},handleSizeChange:function(e){this.tmpquery.page=1,this.tmpquery.size=e,this.getapireportList()},conditionhandleSizeChange:function(e){this.tmpconditionquery.page=1,this.tmpconditionquery.size=e,this.findconditionreport()},dispatchhandleSizeChange:function(e){this.tmpdispatchquery.page=1,this.tmpdispatchquery.size=e,this.getDispatchWithstatus()},handleCurrentChange:function(e){this.tmpquery.page=e,this.getapireportList()},conditionhandleCurrentChange:function(e){this.tmpconditionquery.page=e,this.findconditionreport()},dispatchhandleCurrentChange:function(e){this.tmpdispatchquery.page=e,this.getDispatchWithstatus()}}},f={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-form",{ref:"tmpquery",attrs:{inline:!0,model:e.tmpquery}},[a("el-form-item",[e.hasPermission("apireport:list")?a("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(t){return t.preventDefault(),e.getapireportList(t)}}},[e._v("刷新")]):e._e()],1),e._v(" "),e.hasPermission("apireport:search")?a("span",[a("el-form-item",{attrs:{label:"性能测试集合",prop:"testplanname",required:""}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"测试集合"},on:{change:function(t){return e.testplanselectChanged(t)}},model:{value:e.tmpquery.testplanname,callback:function(t){e.$set(e.tmpquery,"testplanname",t)},expression:"tmpquery.testplanname"}},[a("el-option",{attrs:{label:"请选择"}}),e._v(" "),e._l(e.execplanList,function(e,t){return a("div",{key:t},[a("el-option",{attrs:{label:e.executeplanname,value:e.executeplanname}})],1)})],2)],1),e._v(" "),a("el-form-item",{attrs:{label:"执行计划",prop:"batchname",required:""}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"执行计划"},on:{change:function(t){return e.testbatchselectChanged(t)}},model:{value:e.tmpquery.batchname,callback:function(t){e.$set(e.tmpquery,"batchname",t)},expression:"tmpquery.batchname"}},[a("el-option",{attrs:{label:"请选择"}}),e._v(" "),e._l(e.planbatchList,function(e,t){return a("div",{key:t},[a("el-option",{attrs:{label:e.batchname,value:e.batchname}})],1)})],2)],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.getperformancestatics}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),a("div",{staticClass:"dashboard-editor-container"},[a("github-corner",{staticClass:"github-corner"}),e._v(" "),a("el-row",{attrs:{gutter:50}},[a("el-col",{attrs:{xs:24,sm:24,lg:9}},[a("div",{staticClass:"chart-wrapper",attrs:{id:"22"}},[[a("div",{staticStyle:{width:"300px",height:"380px"},attrs:{id:"mian"}})]],2)]),e._v(" "),a("el-col",{attrs:{xs:24,sm:24,lg:15}},[a("div",{staticClass:"chart-wrapper",attrs:{id:"12"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],staticStyle:{width:"290vh",height:"100px"},attrs:{data:e.casestaticsList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"用例总数",align:"center",prop:"caseNum",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"线程总数",align:"center",prop:"threadnums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"循环总数",align:"center",prop:"loops",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"执行机总数",align:"center",prop:"slavernums",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"运行总数",align:"center",prop:"execCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"成功总数",align:"center",prop:"successCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"失败总数",align:"center",prop:"failCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"总耗时(秒)",align:"center",prop:"costtime",width:"90"}})],1)],1)]),e._v(" "),a("el-col",{attrs:{xs:24,sm:24,lg:15}},[a("div",{staticClass:"chart-wrapper",attrs:{id:"13"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,staticStyle:{width:"120vh",height:"120px"},attrs:{data:e.performancecasesstaticsList,size:"mini","element-loading-text":"loading",border:"","highlight-current-row":""}},[a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"用例",align:"center",prop:"casename",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"TPS",align:"center",prop:"tps",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"运行次数",align:"center",prop:"samples",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"错误次数",align:"center",prop:"errorcount",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"错误率%",align:"center",prop:"errorrate",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"average(ms)",align:"center",prop:"average",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"90th pct(ms)",align:"center",prop:"nzpct",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"95th pct(ms)",align:"center",prop:"nfpct",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"99th pct(ms)",align:"center",prop:"nnpct",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"消耗时间(s)",align:"center",prop:"runtime",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{label:"min(ms)",align:"center",prop:"min",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"max(ms)",align:"center",prop:"max",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"median(ms)",align:"center",prop:"median",width:"100"}})],1)],1)]),e._v(" "),a("el-col",{attrs:{xs:24,sm:24,lg:15}},[a("div",{staticClass:"chart-wrapper",attrs:{id:"14"}},[a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,staticStyle:{width:"100vh"},attrs:{data:e.salvercaseList,size:"mini","element-loading-text":"loading",border:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"分布执行机",align:"center",prop:"slaverName",width:"160"}}),e._v(" "),a("el-table-column",{attrs:{label:"线程数",align:"center",prop:"threadnums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"循环数",align:"center",prop:"loops",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"运行总数",align:"center",prop:"execCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"成功总数",align:"center",prop:"successCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"失败总数",align:"center",prop:"failCaseNums",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"耗时(秒)",align:"center",prop:"costtime",width:"110"}})],1)],1)])],1)],1),e._v(" "),a("el-tabs",{ref:"tabs",attrs:{type:"card"},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[a("el-tab-pane",{attrs:{label:"执行计划结果",name:"first"}},[[a("el-table",{key:e.itemKey,ref:"fileTable",attrs:{data:e.executeplanbatchList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"测试集合名",align:"center",prop:"executeplanname",width:"150"}}),e._v(" "),a("el-table-column",{attrs:{label:"执行计划","show-overflow-tooltip":!0,align:"center",prop:"batchname",width:"150"}}),e._v(" "),a("el-table-column",{attrs:{label:"测试场景","show-overflow-tooltip":!0,align:"center",prop:"scenename",width:"150"}}),e._v(" "),a("el-table-column",{attrs:{label:"状态",align:"center",prop:"status",width:"70"}}),e._v(" "),a("el-table-column",{attrs:{label:"来源",align:"center",prop:"source",width:"60"}}),e._v(" "),a("el-table-column",{attrs:{label:"执行类型",align:"center",prop:"exectype",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"执行时间",align:"center","show-overflow-tooltip":!0,prop:"execdate",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{label:"操作人",align:"center",prop:"creator",width:"70"}}),e._v(" "),a("el-table-column",{attrs:{label:"备注",align:"center",prop:"memo",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[""!==t.row.memo?a("span",{staticStyle:{color:"red"}},[e._v(e._s(t.row.memo))]):e._e()]}}])}),e._v(">\n\n            "),a("el-table-column",{attrs:{label:"创建时间","show-overflow-tooltip":!0,align:"center",prop:"createTime",width:"130"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"最后修改时间","show-overflow-tooltip":!0,align:"center",prop:"lastmodifyTime",width:"130"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n              ")]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.searchexecplanbatch.page,"page-size":e.searchexecplanbatch.size,total:e.execplanbatchtotal,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.execplanbatchhandleSizeChange,"current-change":e.execplanbatchhandleCurrentChange}})]],2),e._v(" "),a("el-tab-pane",{attrs:{label:"用例明细报告",name:"zero"}},[a("div",{staticClass:"filter-container"},[a("el-form",{attrs:{inline:!0}},[a("el-form-item",{attrs:{label:"状态:",prop:"testplanname"}},[a("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"值类型"},model:{value:e.tmpquery.caseststus,callback:function(t){e.$set(e.tmpquery,"caseststus",t)},expression:"tmpquery.caseststus"}},[a("el-option",{attrs:{label:"成功",value:"成功"}}),e._v(" "),a("el-option",{attrs:{label:"失败",value:"失败"}})],1)],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchcaseReportBy}},[e._v("查询")])],1)],1)],1),e._v(" "),[a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.apireportList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"执行计划",align:"center",prop:"batchname",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"用例名",align:"center",prop:"casename",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"API",align:"center",prop:"apiname",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"请求方式",align:"center",prop:"requestmethod",width:"80"}}),e._v(" "),a("el-table-column",{attrs:{label:"状态",align:"center",prop:"status",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return["失败"===t.row.status?a("span",{staticStyle:{color:"red"}},[e._v(e._s(t.row.status))]):a("span",{staticStyle:{color:"#37B328"}},[e._v(e._s(t.row.status))])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"微服务",align:"center",prop:"deployunitname",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{label:"请求地址",align:"center",prop:"url",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.url))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"请求头",align:"center",prop:"requestheader",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.requestheader))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"请求数据",align:"center",prop:"requestdatas",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top-start"}},[a("p",[e._v(e._s(t.row.requestdatas))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"响应",align:"center",prop:"respone",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.respone))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"断言",align:"center",prop:"expect",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.expect))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"断言结果",align:"center",prop:"assertvalue",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.assertvalue))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"运行时间(ms)",align:"center",prop:"runtime",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"异常信息",align:"center",prop:"errorinfo",width:"80"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.errorinfo))]),e._v(" "),""!==t.row.errorinfo?a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{staticStyle:{color:"red"},attrs:{size:"medium"}},[e._v("异常...")])],1):e._e(),e._v(" "),""===t.row.errorinfo?a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{staticStyle:{color:"green"},attrs:{size:"medium"}},[e._v("...")])],1):e._e()])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.search.page,"page-size":e.search.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})]],2),e._v(" "),a("el-tab-pane",{attrs:{label:"前置条件报告",name:"second"}},[[a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.caseconditionreport,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{domProps:{textContent:e._s(e.conditiongetIndex(t.$index))}})]}}])}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"集合/用例名",align:"center",prop:"planname",width:"180"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"执行计划名",align:"center",prop:"batchname",width:"180"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"条件名",align:"center",prop:"subconditionname",width:"200"}}),e._v(" "),a("el-table-column",{attrs:{label:"条件类型",align:"center",prop:"subconditiontype",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"条件结果",align:"center",prop:"conditionresult",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-popover",{attrs:{trigger:"hover",placement:"top"}},[a("p",[e._v(e._s(t.row.conditionresult))]),e._v(" "),a("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[a("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"条件状态",align:"center",prop:"conditionstatus",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return["失败"===t.row.conditionstatus?a("span",{staticStyle:{color:"red"}},[e._v(e._s(t.row.conditionstatus))]):a("span",{staticStyle:{color:"#37B328"}},[e._v(e._s(t.row.conditionstatus))])]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"消耗时长(ms)",align:"center",prop:"runtime",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.tmpconditionquery.page,"page-size":e.tmpconditionquery.size,total:e.conditiontotal,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.conditionhandleSizeChange,"current-change":e.conditionhandleCurrentChange}})]],2)],1)],1)},staticRenderFns:[]};var v=a("VU/8")(g,f,!1,function(e){a("lDNH")},"data-v-08975686",null);t.default=v.exports},lDNH:function(e,t){}});
//# sourceMappingURL=13.b041e7ec634a80dbb178.js.map