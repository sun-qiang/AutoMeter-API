webpackJsonp([53],{CbCB:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("Dd8w"),s=a.n(i),n=a("zXKF"),l=a("0xDb"),c=a("vuyV"),o=a("W5b6"),p=a("NYxO"),r={name:"调度管理",filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{id:null,itemKey:null,tmpexecplanname:null,tmpbatchname:null,dispatchList:[],execplanList:[],planbatchList:[],listLoading:!1,total:0,dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改执行机",update:"修改执行机",add:"添加执行机"},btnLoading:!1,tmpdispatch:{id:"",dispatchname:"",ip:"",port:"",status:"",stype:"",memo:""},tmpbatchquery:{executeplanid:""},search:{page:1,size:10,execplanname:null,accountId:null,batchname:null,projectid:""}}},computed:s()({},Object(p.b)(["name","sidebar","projectlist","projectid","accountId"])),created:function(){this.search.accountId=this.accountId,this.search.projectid=window.localStorage.getItem("pid"),this.getallexplan(),this.getdispatchList()},activated:function(){this.getallexplan(),this.getdispatchList()},methods:{unix2CurrentTime:l.b,testplanselectChanged:function(t){for(var e=this,a=0;a<this.execplanList.length;a++)this.execplanList[a].executeplanname===t&&(this.tmpbatchquery.executeplanid=this.execplanList[a].id);Object(o.a)(this.tmpbatchquery).then(function(t){e.planbatchList=t.data,e.search.batchname=null}).catch(function(t){e.$message.error("加载批次列表失败")})},getdispatchList:function(){var t=this;this.search.execplanname=this.tmpexecplanname,this.search.batchname=this.tmpbatchname,this.listLoading=!0,Object(n.d)(this.search).then(function(e){t.dispatchList=e.data.list,t.total=e.data.total,t.listLoading=!1}).catch(function(e){t.$message.error("加载字典列表失败")})},getallexplan:function(){var t=this;Object(c.d)(this.search).then(function(e){t.execplanList=e.data}).catch(function(e){t.$message.error("加载测试集合列表失败")})},searchBy:function(){var t=this;this.search.page=1,this.listLoading=!0,Object(n.d)(this.search).then(function(e){t.itemKey=Math.random(),t.dispatchList=e.data.list,t.total=e.data.total}).catch(function(e){t.$message.error("搜索失败")}),this.tmpexecplanname=this.search.execplanname,this.tmpbatchname=this.search.batchname,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(t){this.search.page=1,this.search.size=t,this.getdispatchList()},handleCurrentChange:function(t){this.search.page=t,this.getdispatchList()},getIndex:function(t){return(this.search.page-1)*this.search.size+t+1},showAdddispatchDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpdispatch.id="",this.tmpdispatch.dispatchname="",this.tmpdispatch.ip="",this.tmpdispatch.port="",this.tmpdispatch.status="",this.tmpdispatch.stype="",this.tmpdispatch.memo=""},adddispatch:function(){var t=this;this.$refs.tmpdispatch.validate(function(e){e&&t.isUniqueDetail(t.tmpdispatch)&&(t.btnLoading=!0,Object(n.a)(t.tmpdispatch).then(function(){t.$message.success("添加成功"),t.getdispatchList(),t.dialogFormVisible=!1,t.btnLoading=!1}).catch(function(e){t.$message.error("添加失败"),t.btnLoading=!1}))})},showUpdatedispatchDialog:function(t){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpdispatch.id=this.dispatchList[t].id,this.tmpdispatch.dispatchname=this.dispatchList[t].dispatchname,this.tmpdispatch.ip=this.dispatchList[t].ip,this.tmpdispatch.port=this.dispatchList[t].port,this.tmpdispatch.status=this.dispatchList[t].status,this.tmpdispatch.stype=this.dispatchList[t].stype,this.tmpdispatch.memo=this.dispatchList[t].memo},updatedispatch:function(){var t=this;this.isUniqueDetail(this.tmpdispatch)&&Object(n.e)(this.tmpdispatch).then(function(){t.$message.success("更新成功"),t.getdispatchList(),t.dialogFormVisible=!1}).catch(function(e){t.$message.error("更新失败")})},removedispatch:function(t){var e=this;this.$confirm("删除该执行机？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var a=e.dispatchList[t].id;Object(n.c)(a).then(function(){e.$message.success("删除成功"),e.getdispatchList()})}).catch(function(){e.$message.info("已取消删除")})},isUniqueDetail:function(t){for(var e=0;e<this.dispatchList.length;e++)if(this.dispatchList[e].id!==t.id&&this.dispatchList[e].dispatchname===t.dispatchname)return this.$message.error("执行机名已存在"),!1;return!0}}},h={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-form",{attrs:{inline:!0}},[a("el-form-item",[t.hasPermission("dispatch:list")?a("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(e){return e.preventDefault(),t.getdispatchList(e)}}},[t._v("刷新")]):t._e()],1),t._v(" "),t.hasPermission("dispatch:search")?a("span",[a("el-form-item",{attrs:{label:"测试集合",prop:"execplanname"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"测试集合"},on:{change:function(e){return t.testplanselectChanged(e)}},model:{value:t.search.execplanname,callback:function(e){t.$set(t.search,"execplanname",e)},expression:"search.execplanname"}},[a("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.execplanList,function(t,e){return a("div",{key:e},[a("el-option",{attrs:{label:t.executeplanname,value:t.executeplanname}})],1)})],2)],1),t._v(" "),a("el-form-item",{attrs:{label:"批次",prop:"batchname"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"批次"},model:{value:t.search.batchname,callback:function(e){t.$set(t.search,"batchname",e)},expression:"search.batchname"}},[a("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.planbatchList,function(t,e){return a("div",{key:e},[a("el-option",{attrs:{label:t.batchname,value:t.batchname}})],1)})],2)],1),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:t.itemKey,attrs:{data:t.dispatchList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",{domProps:{textContent:t._s(t.getIndex(e.$index))}})]}}])}),t._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"执行机",align:"center",prop:"slavername",width:"150"}}),t._v(" "),a("el-table-column",{attrs:{label:"测试集合",align:"center",prop:"execplanname",width:"150"}}),t._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"执行计划",align:"center",prop:"batchname",width:"150"}}),t._v(" "),a("el-table-column",{attrs:{label:"测试场景",align:"center",prop:"scenename",width:"180"}}),t._v(" "),a("el-table-column",{attrs:{label:"执行用例",align:"center",prop:"testcasename",width:"180"}}),t._v(" "),a("el-table-column",{attrs:{label:"用例顺序",align:"center",prop:"caseorder",width:"80"}}),t._v(" "),a("el-table-column",{attrs:{label:"状态",align:"center",prop:"status",width:"100"}}),t._v(" "),a("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"140"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(t._s(t.unix2CurrentTime(e.row.createTime)))]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"140"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(t._s(t.unix2CurrentTime(e.row.lastmodifyTime))+"\n      ")]}}])}),t._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"异常详情",align:"center",prop:"memo",width:"350"},scopedSlots:t._u([{key:"default",fn:function(e){return[""!==e.row.memo?a("span",{staticStyle:{color:"red"}},[t._v(t._s(e.row.memo))]):t._e()]}}])}),t._v(">\n  ")],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.search.page,"page-size":t.search.size,total:t.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),a("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[a("el-form",{ref:"tmpdispatch",staticClass:"small-space",staticStyle:{width:"300px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"100px",model:t.tmpdispatch}},[a("el-form-item",{attrs:{label:"执行机名",prop:"dispatchname",required:""}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:t.tmpdispatch.dispatchname,callback:function(e){t.$set(t.tmpdispatch,"dispatchname",e)},expression:"tmpdispatch.dispatchname"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"ip",prop:"ip"}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpdispatch.ip,callback:function(e){t.$set(t.tmpdispatch,"ip",e)},expression:"tmpdispatch.ip"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"端口",prop:"port"}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpdispatch.port,callback:function(e){t.$set(t.tmpdispatch,"port",e)},expression:"tmpdispatch.port"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"状态",prop:"status"}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpdispatch.status,callback:function(e){t.$set(t.tmpdispatch,"status",e)},expression:"tmpdispatch.status"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"类型",prop:"stype"}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpdispatch.stype,callback:function(e){t.$set(t.tmpdispatch,"stype",e)},expression:"tmpdispatch.stype"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"备注",prop:"memo"}},[a("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpdispatch.memo,callback:function(e){t.$set(t.tmpdispatch,"memo",e)},expression:"tmpdispatch.memo"}})],1)],1),t._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{nativeOn:{click:function(e){e.preventDefault(),t.dialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),"add"===t.dialogStatus?a("el-button",{attrs:{type:"danger"},nativeOn:{click:function(e){return e.preventDefault(),t.$refs.tmpdispatch.resetFields()}}},[t._v("重置")]):t._e(),t._v(" "),"add"===t.dialogStatus?a("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.adddispatch(e)}}},[t._v("添加")]):t._e(),t._v(" "),"update"===t.dialogStatus?a("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.updatedispatch(e)}}},[t._v("修改")]):t._e()],1)],1)],1)},staticRenderFns:[]},d=a("VU/8")(r,h,!1,null,null,null);e.default=d.exports}});
//# sourceMappingURL=53.1d07dcd36f9a6dee6616.js.map