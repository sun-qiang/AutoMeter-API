webpackJsonp([14],{H5nf:function(e,t){},SYIK:function(e,t,r){"use strict";t.c=function(e){return Object(a.a)({url:"/apicases/report/performance",method:"get",params:e})},t.i=function(e){return Object(a.a)({url:"/apicases/report/performance/search",method:"post",data:e})},t.b=function(e){return Object(a.a)({url:"/apicases/report/performance/findApicasereportWithNameandStatus",method:"post",data:e})},t.f=function(e){return Object(a.a)({url:"/apicases/report/performance/getperformancecasestatics",method:"post",data:e})},t.e=function(e){return Object(a.a)({url:"/apicases/report/performance/getperformanceallstatics",method:"post",data:e})},t.g=function(e){return Object(a.a)({url:"/apicases/report/performance/getperformanceslaverstatics",method:"post",data:e})},t.d=function(e){return Object(a.a)({url:"/apicases/report/performance/getperformanceCaseSandF",method:"post",data:e})},t.a=function(e){return Object(a.a)({url:"/apicases/report/performance",method:"post",data:e})},t.j=function(e){return Object(a.a)({url:"/apicases/report/performance",method:"put",data:e})},t.h=function(e){return Object(a.a)({url:"/apicases/report/performance/"+e,method:"delete"})};var a=r("vLgD")},Xjku:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=r("SYIK"),i=r("XcM5"),n=r("vuyV"),o=r("W5b6"),p={filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{tmptestplanname:"",tmptestplanid:null,tmpbatchname:null,apiperformancereportList:[],apiList:[],planbatchList:[],execplanList:[],deployunitList:[],listLoading:!1,dicvisitypeQuery:{page:1,size:30,diccode:"httpvisittype"},total:0,listQuery:{page:1,size:10,listLoading:!0,testplanname:"",testplanid:null,batchname:null},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改apiperformancereport",update:"修改apiperformancereport",add:"添加apiperformancereport"},btnLoading:!1,tmpapiperformancereport:{executeplanid:"",id:"",deployunitid:"",deployunitname:"",batchname:"",requestheader:"",requestdatas:"",apiperformancereportname:"",visittype:"",path:"",url:"",requestmethod:"",memo:""},tmpexecplantype:{usetype:""},search:{page:1,size:10,testplanname:"",testplanid:null,batchname:null}}},created:function(){this.getexecplanList(),this.getdepunitList(),this.getapiperformancereportList()},methods:{unix2CurrentTime:r("0xDb").b,testplanselectChanged:function(e){for(var t=this,r=0;r<this.execplanList.length;r++)this.execplanList[r].executeplanname===e&&(this.tmpapiperformancereport.executeplanid=this.execplanList[r].id);Object(o.a)(this.tmpapiperformancereport).then(function(e){t.planbatchList=e.data}).catch(function(e){t.$message.error("加载执行计划列表失败")})},selectChanged:function(e){for(var t=0;t<this.deployunitList.length;t++)this.deployunitList[t].deployunitname===e&&(this.tmpapiperformancereport.deployunitid=this.deployunitList[t].id),console.log(this.deployunitList[t].id)},getapiperformancereportList:function(){var e=this;this.listLoading=!0,Object(a.c)(this.listQuery).then(function(t){e.apiperformancereportList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载api报告列表失败")})},getexecplanList:function(){var e=this;this.tmpexecplantype.usetype="性能",Object(n.e)(this.tmpexecplantype).then(function(t){e.execplanList=t.data}).catch(function(t){e.$message.error("加载计划列表失败")})},getdepunitList:function(){var e=this;Object(i.d)(this.listQuery).then(function(t){e.deployunitList=t.data.list}).catch(function(t){e.$message.error("加载微服务列表失败")})},searchBy:function(){var e=this;this.btnLoading=!0,this.listLoading=!0,this.search.page=this.listQuery.page,this.search.size=this.listQuery.size,this.search.testplanid=this.tmpapiperformancereport.executeplanid,Object(a.i)(this.search).then(function(t){e.apiperformancereportList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmptestplanname=this.search.testplanname,this.tmptestplanid=this.search.testplanid,this.tmpbatchname=this.search.batchname,this.listLoading=!1,this.btnLoading=!1},searchBypageing:function(){var e=this;this.btnLoading=!0,this.listLoading=!0,this.listQuery.testplanname=this.tmptestplanname,this.listQuery.testplanid=this.tmptestplanid,this.listQuery.batchname=this.tmpbatchname,Object(a.i)(this.listQuery).then(function(t){e.apiperformancereportList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(e){this.listQuery.size=e,this.listQuery.page=1,this.searchBypageing()},handleCurrentChange:function(e){this.listQuery.page=e,this.searchBypageing()},getIndex:function(e){return(this.listQuery.page-1)*this.listQuery.size+e+1},showAddapiperformancereportDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpapiperformancereport.id="",this.tmpapiperformancereport.deployunitid="",this.tmpapiperformancereport.deployunitname="",this.tmpapiperformancereport.apiperformancereportname="",this.tmpapiperformancereport.visittype="",this.tmpapiperformancereport.path="",this.tmpapiperformancereport.memo=""},addapiperformancereport:function(){var e=this;this.$refs.tmpapiperformancereport.validate(function(t){t&&e.isUniqueDetail(e.tmpapiperformancereport)&&(e.btnLoading=!0,Object(a.a)(e.tmpapiperformancereport).then(function(){e.$message.success("添加成功"),e.getapiperformancereportList(),e.dialogFormVisible=!1,e.btnLoading=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1}))})},showUpdateapiperformancereportDialog:function(e){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpapiperformancereport.id=this.apiperformancereportList[e].id,this.tmpapiperformancereport.deployunitid=this.apiperformancereportList[e].deployunitid,this.tmpapiperformancereport.deployunitname=this.apiperformancereportList[e].deployunitname,this.tmpapiperformancereport.apiperformancereportname=this.apiperformancereportList[e].apiperformancereportname,this.tmpapiperformancereport.visittype=this.apiperformancereportList[e].visittype,this.tmpapiperformancereport.path=this.apiperformancereportList[e].path,this.tmpapiperformancereport.memo=this.apiperformancereportList[e].memo},updateapiperformancereport:function(){var e=this;this.isUniqueDetail(this.tmpapiperformancereport)&&Object(a.j)(this.tmpapiperformancereport).then(function(){e.$message.success("更新成功"),e.getapiperformancereportList(),e.dialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")})},removeapiperformancereport:function(e){var t=this;this.$confirm("删除该apiperformancereport？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var r=t.apiperformancereportList[e].id;Object(a.h)(r).then(function(){t.$message.success("删除成功"),t.getapiperformancereportList()})}).catch(function(){t.$message.info("已取消删除")})},isUniqueDetail:function(e){console.log(e.id);for(var t=0;t<this.apiperformancereportList.length;t++)if(this.apiperformancereportList[t].id!==e.id&&(console.log(this.apiperformancereportList[t].id),this.apiperformancereportList[t].apiperformancereportname===e.apiperformancereportname))return this.$message.error("apiperformancereport名已存在"),!1;return!0}}},s={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container"},[r("div",{staticClass:"filter-container"},[r("el-form",{attrs:{inline:!0}},[r("el-form-item",[e.hasPermission("apiperformancereport:list")?r("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(t){return t.preventDefault(),e.getapiperformancereportList(t)}}},[e._v("刷新")]):e._e()],1),e._v(" "),e.hasPermission("apiperformancereport:search")?r("span",[r("el-form-item",{attrs:{label:"测试集合",prop:"testplanname"}},[r("el-select",{attrs:{placeholder:"测试集合"},on:{change:function(t){return e.testplanselectChanged(t)}},model:{value:e.search.testplanname,callback:function(t){e.$set(e.search,"testplanname",t)},expression:"search.testplanname"}},[r("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.execplanList,function(e,t){return r("div",{key:t},[r("el-option",{attrs:{label:e.executeplanname,value:e.executeplanname}})],1)})],2)],1),e._v(" "),r("el-form-item",{attrs:{label:"执行计划",prop:"batchname"}},[r("el-select",{attrs:{placeholder:"执行计划"},model:{value:e.search.batchname,callback:function(t){e.$set(e.search,"batchname",t)},expression:"search.batchname"}},[r("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.planbatchList,function(e,t){return r("div",{key:t},[r("el-option",{attrs:{label:e.batchname,value:e.batchname}})],1)})],2)],1),e._v(" "),r("el-form-item",[r("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchBy}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],attrs:{data:e.apiperformancereportList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[r("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"执行计划",align:"center",prop:"batchname",width:"80"}}),e._v(" "),r("el-table-column",{attrs:{label:"用例名",align:"center",prop:"casename",width:"120"}}),e._v(" "),r("el-table-column",{attrs:{label:"API",align:"center",prop:"apiname",width:"80"}}),e._v(" "),r("el-table-column",{attrs:{label:"请求方式",align:"center",prop:"requestmethod",width:"80"}}),e._v(" "),r("el-table-column",{attrs:{label:"状态",align:"center",prop:"status",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return["失败"===t.row.status?r("span",{staticStyle:{color:"red"}},[e._v(e._s(t.row.status))]):r("span",{staticStyle:{color:"#37B328"}},[e._v(e._s(t.row.status))])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"微服务",align:"center",prop:"deployunitname",width:"100"}}),e._v(" "),r("el-table-column",{attrs:{label:"请求地址",align:"center",prop:"url",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.url))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"请求头",align:"center",prop:"requestheader",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.requestheader))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"请求数据",align:"center",prop:"requestdatas",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.requestdatas))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"响应",align:"center",prop:"respone",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.respone))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"断言",align:"center",prop:"expect",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.expect))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"断言结果",align:"center",prop:"assertvalue",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.assertvalue))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"运行时间(ms)",align:"center",prop:"runtime",width:"100"}}),e._v(" "),r("el-table-column",{attrs:{label:"异常信息",align:"center",prop:"errorinfo",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[r("el-popover",{attrs:{trigger:"hover",placement:"top"}},[r("p",[e._v(e._s(t.row.errorinfo))]),e._v(" "),r("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[r("el-tag",{attrs:{size:"medium"}},[e._v("...")])],1)])]}}])}),e._v(" "),r("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"120"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])})],1),e._v(" "),r("el-pagination",{attrs:{"current-page":e.listQuery.page,"page-size":e.listQuery.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),r("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[r("el-form",{ref:"tmpapiperformancereport",staticClass:"small-space",staticStyle:{width:"300px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"100px",model:e.tmpapiperformancereport}},[r("el-form-item",{attrs:{label:"apiperformancereport名",prop:"apiperformancereportname",required:""}},[r("el-input",{attrs:{type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:e.tmpapiperformancereport.apiperformancereportname,callback:function(t){e.$set(e.tmpapiperformancereport,"apiperformancereportname",t)},expression:"tmpapiperformancereport.apiperformancereportname"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"访问方式",prop:"visittype",required:""}},[r("el-select",{attrs:{placeholder:"访问方式"},model:{value:e.tmpapiperformancereport.visittype,callback:function(t){e.$set(e.tmpapiperformancereport,"visittype",t)},expression:"tmpapiperformancereport.visittype"}},[r("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.visittypeList,function(e,t){return r("div",{key:t},[r("el-option",{attrs:{label:e.dicitmevalue,value:e.dicitmevalue,required:""}})],1)})],2)],1),e._v(" "),r("el-form-item",{attrs:{label:"资源路径",prop:"path",required:""}},[r("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpapiperformancereport.path,callback:function(t){e.$set(e.tmpapiperformancereport,"path",t)},expression:"tmpapiperformancereport.path"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"微服务",prop:"deployunitname",required:""}},[r("el-select",{attrs:{placeholder:"微服务"},on:{change:function(t){return e.selectChanged(t)}},model:{value:e.tmpapiperformancereport.deployunitname,callback:function(t){e.$set(e.tmpapiperformancereport,"deployunitname",t)},expression:"tmpapiperformancereport.deployunitname"}},[r("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),e._v(" "),e._l(e.deployunitList,function(e,t){return r("div",{key:t},[r("el-option",{attrs:{label:e.deployunitname,value:e.deployunitname,required:""}})],1)})],2)],1),e._v(" "),r("el-form-item",{attrs:{label:"备注",prop:"memo"}},[r("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpapiperformancereport.memo,callback:function(t){e.$set(e.tmpapiperformancereport,"memo",t)},expression:"tmpapiperformancereport.memo"}})],1)],1),e._v(" "),r("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.dialogStatus?r("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpapiperformancereport.resetFields()}}},[e._v("重置")]):e._e(),e._v(" "),"add"===e.dialogStatus?r("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addapiperformancereport(t)}}},[e._v("添加")]):e._e(),e._v(" "),"update"===e.dialogStatus?r("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updateapiperformancereport(t)}}},[e._v("修改")]):e._e()],1)],1)],1)},staticRenderFns:[]};var l=r("VU/8")(p,s,!1,function(e){r("H5nf")},null,null);t.default=l.exports}});
//# sourceMappingURL=14.7a5525a470ee803460db.js.map