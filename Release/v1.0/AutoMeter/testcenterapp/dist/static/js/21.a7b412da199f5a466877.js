webpackJsonp([21],{"d+Di":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("Dd8w"),s=i.n(a),n=i("vLgD");function r(t){return Object(n.a)({url:"/apicases/performancestatistics/search",method:"post",data:t})}var c=i("XcM5"),p=i("vuyV"),l=i("W5b6"),o=i("0xDb"),m=i("NYxO"),u={name:"性能统计报告",filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{tmptestplanname:"",tmptestplanid:null,tmpbatchname:null,visittypeList:[],apiperformancestatisticsList:[],apiList:[],planbatchList:[],execplanList:[],deployunitList:[],listLoading:!1,dicvisitypeQuery:{page:1,size:30,diccode:"httpvisittype"},total:0,listQuery:{page:1,size:20,listLoading:!0,testplanname:"",testplanid:null,batchname:null},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改apiperformancestatistics",update:"修改apiperformancestatistics",add:"添加apiperformancestatistics"},btnLoading:!1,tmpapiperformancestatistics:{executeplanid:"",id:"",deployunitid:"",deployunitname:"",batchname:"",apiperformancestatisticsname:"",visittype:"",path:"",memo:""},tmpexecplantype:{usetype:"",projectid:""},search:{page:1,size:10,testplanname:"",testplanid:null,batchname:null,projectid:""}}},computed:s()({},Object(m.b)(["name","sidebar","projectlist","projectid"])),created:function(){this.search.projectid=window.localStorage.getItem("pid"),this.tmpexecplantype.projectid=window.localStorage.getItem("pid"),this.getexecplanList(),this.getdepunitList()},activated:function(){this.getexecplanList()},methods:{unix2CurrentTime:o.b,testplanselectChanged:function(t){for(var e=this,i=0;i<this.execplanList.length;i++)this.execplanList[i].executeplanname===t&&(this.tmpapiperformancestatistics.executeplanid=this.execplanList[i].id);Object(l.b)(this.tmpapiperformancestatistics).then(function(t){e.planbatchList=t.data}).catch(function(t){e.$message.error("加载执行计划列表失败")})},selectChanged:function(t){for(var e=0;e<this.deployunitList.length;e++)this.deployunitList[e].deployunitname===t&&(this.tmpapiperformancestatistics.deployunitid=this.deployunitList[e].id),console.log(this.deployunitList[e].id)},getapiperformancestatisticsList:function(){var t,e=this;this.listLoading=!0,(t=this.listQuery,Object(n.a)({url:"/apicases/performancestatistics",method:"get",params:t})).then(function(t){e.apiperformancestatisticsList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载api报告列表失败")})},getexecplanList:function(){var t=this;this.tmpexecplantype.usetype="性能",Object(p.e)(this.tmpexecplantype).then(function(e){t.execplanList=e.data}).catch(function(e){t.$message.error("加载计划列表失败")})},getdepunitList:function(){var t=this;Object(c.d)(this.listQuery).then(function(e){t.deployunitList=e.data.list}).catch(function(e){t.$message.error("加载微服务列表失败")})},searchBy:function(){var t=this;this.btnLoading=!0,this.listLoading=!0,this.search.page=this.listQuery.page,this.search.size=this.listQuery.size,this.search.testplanid=this.tmpapiperformancestatistics.executeplanid,r(this.search).then(function(e){t.apiperformancestatisticsList=e.data.list,t.total=e.data.total}).catch(function(e){t.$message.error("搜索失败")}),this.tmptestplanname=this.search.testplanname,this.tmptestplanid=this.search.testplanid,this.tmpbatchname=this.search.batchname,this.listLoading=!1,this.btnLoading=!1},searchBypageing:function(){var t=this;this.btnLoading=!0,this.listLoading=!0,this.listQuery.testplanname=this.tmptestplanname,this.listQuery.testplanid=this.tmptestplanid,this.listQuery.batchname=this.tmpbatchname,r(this.listQuery).then(function(e){t.apiperformancestatisticsList=e.data.list,t.total=e.data.total}).catch(function(e){t.$message.error("搜索失败")}),this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(t){this.listQuery.size=t,this.listQuery.page=1,this.searchBypageing()},handleCurrentChange:function(t){this.listQuery.page=t,this.searchBypageing()},getIndex:function(t){return(this.listQuery.page-1)*this.listQuery.size+t+1},showAddapiperformancestatisticsDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpapiperformancestatistics.id="",this.tmpapiperformancestatistics.deployunitid="",this.tmpapiperformancestatistics.deployunitname="",this.tmpapiperformancestatistics.apiperformancestatisticsname="",this.tmpapiperformancestatistics.visittype="",this.tmpapiperformancestatistics.path="",this.tmpapiperformancestatistics.memo=""},addapiperformancestatistics:function(){var t=this;this.$refs.tmpapiperformancestatistics.validate(function(e){var i;e&&t.isUniqueDetail(t.tmpapiperformancestatistics)&&(t.btnLoading=!0,(i=t.tmpapiperformancestatistics,Object(n.a)({url:"/apicases/performancestatistics",method:"post",data:i})).then(function(){t.$message.success("添加成功"),t.getapiperformancestatisticsList(),t.dialogFormVisible=!1,t.btnLoading=!1}).catch(function(e){t.$message.error("添加失败"),t.btnLoading=!1}))})},showUpdateapiperformancestatisticsDialog:function(t){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpapiperformancestatistics.id=this.apiperformancestatisticsList[t].id,this.tmpapiperformancestatistics.deployunitid=this.apiperformancestatisticsList[t].deployunitid,this.tmpapiperformancestatistics.deployunitname=this.apiperformancestatisticsList[t].deployunitname,this.tmpapiperformancestatistics.apiperformancestatisticsname=this.apiperformancestatisticsList[t].apiperformancestatisticsname,this.tmpapiperformancestatistics.visittype=this.apiperformancestatisticsList[t].visittype,this.tmpapiperformancestatistics.path=this.apiperformancestatisticsList[t].path,this.tmpapiperformancestatistics.memo=this.apiperformancestatisticsList[t].memo},updateapiperformancestatistics:function(){var t,e=this;this.isUniqueDetail(this.tmpapiperformancestatistics)&&(t=this.tmpapiperformancestatistics,Object(n.a)({url:"/apicases/performancestatistics",method:"put",data:t})).then(function(){e.$message.success("更新成功"),e.getapiperformancestatisticsList(),e.dialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")})},removeapiperformancestatistics:function(t){var e=this;this.$confirm("删除该apiperformancestatistics？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var i,a=e.apiperformancestatisticsList[t].id;(i=a,Object(n.a)({url:"/apicases/performancestatistics/"+i,method:"delete"})).then(function(){e.$message.success("删除成功"),e.getapiperformancestatisticsList()})}).catch(function(){e.$message.info("已取消删除")})},isUniqueDetail:function(t){console.log(t.id);for(var e=0;e<this.apiperformancestatisticsList.length;e++)if(this.apiperformancestatisticsList[e].id!==t.id&&(console.log(this.apiperformancestatisticsList[e].id),this.apiperformancestatisticsList[e].apiperformancestatisticsname===t.apiperformancestatisticsname))return this.$message.error("apiperformancestatistics名已存在"),!1;return!0}}},d={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"app-container"},[i("div",{staticClass:"filter-container"},[i("el-form",{attrs:{inline:!0}},[i("el-form-item",[t.hasPermission("apiperformancestatistics:list")?i("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(e){return e.preventDefault(),t.getapiperformancestatisticsList(e)}}},[t._v("刷新")]):t._e()],1),t._v(" "),t.hasPermission("apiperformancestatistics:search")?i("span",[i("el-form-item",{attrs:{label:"测试集合",prop:"testplanname"}},[i("el-select",{attrs:{filterable:"",placeholder:"测试集合"},on:{change:function(e){return t.testplanselectChanged(e)}},model:{value:t.search.testplanname,callback:function(e){t.$set(t.search,"testplanname",e)},expression:"search.testplanname"}},[i("el-option",{attrs:{label:"请选择"}}),t._v(" "),t._l(t.execplanList,function(t,e){return i("div",{key:e},[i("el-option",{attrs:{label:t.executeplanname,value:t.executeplanname}})],1)})],2)],1),t._v(" "),i("el-form-item",{attrs:{label:"执行计划",prop:"batchname"}},[i("el-select",{attrs:{filterable:"",placeholder:"执行计划"},model:{value:t.search.batchname,callback:function(e){t.$set(t.search,"batchname",e)},expression:"search.batchname"}},[i("el-option",{attrs:{label:"请选择"}}),t._v(" "),t._l(t.planbatchList,function(t,e){return i("div",{key:e},[i("el-option",{attrs:{label:t.batchname,value:t.batchname}})],1)})],2)],1),t._v(" "),i("el-form-item",[i("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()],1)],1),t._v(" "),i("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],attrs:{data:t.apiperformancestatisticsList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[i("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:t._u([{key:"default",fn:function(e){return[i("span",{domProps:{textContent:t._s(t.getIndex(e.$index))}})]}}])}),t._v(" "),i("el-table-column",{attrs:{label:"测试集合",align:"center",prop:"executeplanname",width:"120"}}),t._v(" "),i("el-table-column",{attrs:{label:"用例",align:"center",prop:"casename",width:"120"}}),t._v(" "),i("el-table-column",{attrs:{label:"执行计划",align:"center",prop:"batchname",width:"120"}}),t._v(" "),i("el-table-column",{attrs:{label:"TPS",align:"center",prop:"tps",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"运行次数",align:"center",prop:"samples",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"错误次数",align:"center",prop:"errorcount",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"错误率",align:"center",prop:"errorrate",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"average(ms)",align:"center",prop:"average",width:"100"}}),t._v(" "),i("el-table-column",{attrs:{label:"min(ms)",align:"center",prop:"min",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"max(ms)",align:"center",prop:"max",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"median(ms)",align:"center",prop:"median",width:"100"}}),t._v(" "),i("el-table-column",{attrs:{label:"90th pct(ms)",align:"center",prop:"nzpct",width:"100"}}),t._v(" "),i("el-table-column",{attrs:{label:"95th pct(ms)",align:"center",prop:"nfpct",width:"100"}}),t._v(" "),i("el-table-column",{attrs:{label:"99th pct(ms)",align:"center",prop:"nnpct",width:"100"}}),t._v(" "),i("el-table-column",{attrs:{label:"消耗时间(s)",align:"center",prop:"runtime",width:"120"}}),t._v(" "),i("el-table-column",{attrs:{label:"receivekbsec",align:"center",prop:"receivekbsec",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"sendkbsec",align:"center",prop:"sendkbsec",width:"80"}}),t._v(" "),i("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"120"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(t._s(t.unix2CurrentTime(e.row.createTime)))]}}])})],1),t._v(" "),i("el-pagination",{attrs:{"current-page":t.listQuery.page,"page-size":t.listQuery.size,total:t.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),i("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[i("el-form",{ref:"tmpapiperformancestatistics",staticClass:"small-space",staticStyle:{width:"300px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"100px",model:t.tmpapiperformancestatistics}},[i("el-form-item",{attrs:{label:"apiperformancestatistics名",prop:"apiperformancestatisticsname",required:""}},[i("el-input",{attrs:{type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:t.tmpapiperformancestatistics.apiperformancestatisticsname,callback:function(e){t.$set(t.tmpapiperformancestatistics,"apiperformancestatisticsname",e)},expression:"tmpapiperformancestatistics.apiperformancestatisticsname"}})],1),t._v(" "),i("el-form-item",{attrs:{label:"访问方式",prop:"visittype",required:""}},[i("el-select",{attrs:{placeholder:"访问方式"},model:{value:t.tmpapiperformancestatistics.visittype,callback:function(e){t.$set(t.tmpapiperformancestatistics,"visittype",e)},expression:"tmpapiperformancestatistics.visittype"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.visittypeList,function(t,e){return i("div",{key:e},[i("el-option",{attrs:{label:t.dicitmevalue,value:t.dicitmevalue,required:""}})],1)})],2)],1),t._v(" "),i("el-form-item",{attrs:{label:"资源路径",prop:"path",required:""}},[i("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpapiperformancestatistics.path,callback:function(e){t.$set(t.tmpapiperformancestatistics,"path",e)},expression:"tmpapiperformancestatistics.path"}})],1),t._v(" "),i("el-form-item",{attrs:{label:"微服务",prop:"deployunitname",required:""}},[i("el-select",{attrs:{placeholder:"微服务"},on:{change:function(e){return t.selectChanged(e)}},model:{value:t.tmpapiperformancestatistics.deployunitname,callback:function(e){t.$set(t.tmpapiperformancestatistics,"deployunitname",e)},expression:"tmpapiperformancestatistics.deployunitname"}},[i("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.deployunitList,function(t,e){return i("div",{key:e},[i("el-option",{attrs:{label:t.deployunitname,value:t.deployunitname,required:""}})],1)})],2)],1),t._v(" "),i("el-form-item",{attrs:{label:"备注",prop:"memo"}},[i("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpapiperformancestatistics.memo,callback:function(e){t.$set(t.tmpapiperformancestatistics,"memo",e)},expression:"tmpapiperformancestatistics.memo"}})],1)],1),t._v(" "),i("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[i("el-button",{nativeOn:{click:function(e){e.preventDefault(),t.dialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),"add"===t.dialogStatus?i("el-button",{attrs:{type:"danger"},nativeOn:{click:function(e){return e.preventDefault(),t.$refs.tmpapiperformancestatistics.resetFields()}}},[t._v("重置")]):t._e(),t._v(" "),"add"===t.dialogStatus?i("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.addapiperformancestatistics(e)}}},[t._v("添加")]):t._e(),t._v(" "),"update"===t.dialogStatus?i("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.updateapiperformancestatistics(e)}}},[t._v("修改")]):t._e()],1)],1)],1)},staticRenderFns:[]};var h=i("VU/8")(u,d,!1,function(t){i("gO8o")},null,null);e.default=h.exports},gO8o:function(t,e){}});
//# sourceMappingURL=21.a7b412da199f5a466877.js.map