webpackJsonp([63],{"5qdU":function(t,i,e){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var n=e("Dd8w"),o=e.n(n),a=e("IBLw"),s=e("jAkc"),c=e("vuyV"),l=e("2d0t"),d=e("W+bg"),r=e("XcM5"),p=e("0xDb"),u=e("NYxO"),m={name:"父条件管理",filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{id:null,itemKey:null,tmpconditionname:"",tmpobjecttype:"",execplanList:[],conditionList:[],conditionorderList:[],saveconditionorderList:[],apiList:[],caseList:[],deployunitList:[],enviromentnameList:[],machinenameList:[],testcasevisible:!1,executeplanVisible:!1,ConditionOrderdialogFormVisible:!1,listLoading:!1,total:0,deployunitquery:{deployunitid:"",modelid:0,deployunitname:""},apiquery:{apiid:"",deployunitid:"",casedeployunitname:"",caseapiname:""},listQuery:{page:1,size:10,listLoading:!0},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改条件",update:"修改条件",add:"添加条件"},btnLoading:!1,tmpcondition:{id:"",conditionname:"",objecttype:"",objectid:"",objectname:"",apiid:"",deployunitid:"",conditiontype:"",memo:"",creator:"",deployunitname:"",apiname:"",projectid:""},tmpconditionorder:{id:"",conditionid:"",subconditionid:"",conditionname:"",subconditionname:"",subconditiontype:"",conditionorder:"",orderstatus:"",creator:""},search:{page:1,size:10,conditionname:null,objecttype:null,projectid:""}}},computed:o()({},Object(u.b)(["name","sidebar","projectlist","projectid"])),created:function(){this.search.projectid=window.localStorage.getItem("pid"),this.getconditionList(),this.getdepunitLists()},activated:function(){this.getdepunitLists()},methods:{unix2CurrentTime:p.b,moveUp:function(t,i){if(t>0){var e=this.conditionorderList[t-1];this.conditionorderList.splice(t-1,1),this.conditionorderList.splice(t,0,e)}else alert("已经是第一条，不可上移");console.log(this.conditionorderList)},moveDown:function(t,i){if(console.log("下移",t,i),t+1===this.conditionorderList.length)alert("已经是最后一条，不可下移");else{console.log(t);var e=this.conditionorderList[t+1];this.conditionorderList.splice(t+1,1),this.conditionorderList.splice(t,0,e)}},targetChanged:function(t){"测试集合"===t&&(this.executeplanVisible=!0,this.testcasevisible=!1,this.tmpcondition.objectid="",this.tmpcondition.objectname="",this.tmpcondition.apiid=0,this.tmpcondition.deployunitid=0,this.getallexplan()),"调试用例"===t&&(this.executeplanVisible=!1,this.testcasevisible=!1,this.tmpcondition.objectid=0,this.tmpcondition.objectname="调试用例",this.tmpcondition.apiid=0,this.tmpcondition.deployunitid=0),"测试用例"===t&&(this.executeplanVisible=!1,this.testcasevisible=!0,this.tmpcondition.objectid="",this.tmpcondition.objectname="",this.tmpcondition.deployunitname="",this.tmpcondition.deployunitname="",this.tmpcondition.apiname="")},selectChanged:function(t){var i=this;this.tmpcondition.apiname="";for(var e=0;e<this.deployunitList.length;e++)this.deployunitList[e].deployunitname===t&&(this.deployunitquery.deployunitid=this.deployunitList[e].id,this.apiquery.deployunitid=this.deployunitList[e].id,this.tmpcondition.deployunitid=this.deployunitList[e].id);this.deployunitquery.deployunitname=t,this.tmpcondition.apiname="",this.tmpcondition.objectname="",Object(d.d)(this.deployunitquery).then(function(t){i.apiList=t.data}).catch(function(t){i.$message.error("加载api列表失败")})},apiselectChanged:function(t){var i=this;this.tmpcondition.objectname="";for(var e=0;e<this.apiList.length;e++)this.apiList[e].apiname===t&&(this.apiquery.apiid=this.apiList[e].id,this.tmpcondition.apiid=this.apiList[e].id);this.apiquery.caseapiname=t,this.apiquery.casedeployunitname=this.deployunitquery.deployunitname,Object(l.d)(this.apiquery).then(function(t){i.caseList=t.data}).catch(function(t){i.$message.error("加载api用例列表失败")})},execplanChanged:function(t){for(var i=0;i<this.execplanList.length;i++)this.execplanList[i].executeplanname===t&&(this.tmpcondition.objectid=this.execplanList[i].id,this.tmpcondition.objectname=this.execplanList[i].executeplanname)},testcaseChanged:function(t){for(var i=0;i<this.caseList.length;i++)this.caseList[i].casename===t&&(this.tmpcondition.objectid=this.caseList[i].id)},getdepunitLists:function(){var t=this;this.listLoading=!0,Object(r.e)(this.search).then(function(i){t.deployunitList=i.data,t.listLoading=!1}).catch(function(i){t.$message.error("加载微服务列表失败")})},getallexplan:function(){var t=this;Object(c.d)(this.search).then(function(i){t.execplanList=i.data}).catch(function(i){t.$message.error("加载测试集合列表失败")})},getconditionList:function(){var t=this;this.listLoading=!0,this.search.enviromentname=this.tmpenviromentname,this.search.objecttype=this.tmpobjecttype,Object(a.f)(this.search).then(function(i){t.conditionList=i.data.list,t.total=i.data.total,t.listLoading=!1}).catch(function(i){t.$message.error("加载条件列表失败")})},searchBy:function(){var t=this;this.search.page=1,this.listLoading=!0,Object(a.f)(this.search).then(function(i){t.itemKey=Math.random(),t.conditionList=i.data.list,t.total=i.data.total}).catch(function(i){t.$message.error("搜索失败")}),this.tmpconditionname=this.search.conditionname,this.tmpobjecttype=this.search.objecttype,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(t){this.search.page=1,this.search.size=t,this.getconditionList()},handleCurrentChange:function(t){this.search.page=t,this.getconditionList()},getIndex:function(t){return(this.search.page-1)*this.search.size+t+1},showAddconditionDialog:function(){this.dialogFormVisible=!0,this.testcasevisible=!1,this.dialogStatus="add",this.tmpcondition.id="",this.tmpcondition.conditionname="",this.tmpcondition.objectid="",this.tmpcondition.objectname="",this.tmpcondition.deployunitname="",this.tmpcondition.apiname="",this.tmpcondition.objecttype="",this.tmpcondition.conditiontype="",this.tmpcondition.memo="",this.tmpcondition.creator=this.name,this.tmpcondition.projectid=window.localStorage.getItem("pid")},addcondition:function(){var t=this;this.$refs.tmpcondition.validate(function(i){i&&(t.btnLoading=!0,Object(a.a)(t.tmpcondition).then(function(){t.$message.success("添加成功"),t.getconditionList(),t.dialogFormVisible=!1,t.btnLoading=!1,t.testcasevisible=!1,t.tmpcondition.id="",t.tmpcondition.conditionname="",t.tmpcondition.objectid="",t.tmpcondition.objectname="",t.tmpcondition.objecttype="",t.tmpcondition.deployunitname="",t.tmpcondition.apiname="",t.tmpcondition.memo=""}).catch(function(i){t.$message.error("添加失败"),t.btnLoading=!1}))})},saveconditionorder:function(){var t=this;this.saveconditionorderList=[];for(var i=0;i<this.conditionorderList.length;i++)this.saveconditionorderList.push({conditionid:this.conditionorderList[i].conditionid,subconditionid:this.conditionorderList[i].subconditionid,subconditiontype:this.conditionorderList[i].subconditiontype,orderstatus:"已排序",subconditionname:this.conditionorderList[i].subconditionname,conditionname:this.conditionorderList[i].conditionname,conditionorder:i+1,creator:this.name});console.log(this.saveconditionorderList),Object(s.a)(this.saveconditionorderList).then(function(){t.$message.success("条件顺序保存成功")}).catch(function(i){t.$message.error("条件顺序保存失败")}),this.ConditionOrderdialogFormVisible=!1},showUpdateconditionDialog:function(t){this.getallexplan(),this.dialogFormVisible=!0,this.dialogStatus="update","测试集合"===this.conditionList[t].objecttype&&(this.executeplanVisible=!0,this.testcasevisible=!1),"测试用例"===this.conditionList[t].objecttype&&(this.testcasevisible=!0,this.executeplanVisible=!1),this.tmpcondition.id=this.conditionList[t].id,this.tmpcondition.objectid=this.conditionList[t].objectid,this.tmpcondition.conditionname=this.conditionList[t].conditionname,this.tmpcondition.apiname=this.conditionList[t].apiname,this.tmpcondition.deployunitname=this.conditionList[t].deployunitname,this.tmpcondition.objectname=this.conditionList[t].objectname,this.tmpcondition.objecttype=this.conditionList[t].objecttype,this.tmpcondition.conditiontype=this.conditionList[t].conditiontype,this.tmpcondition.memo=this.conditionList[t].memo,this.tmpcondition.creator=this.name},showconditionorderDialog:function(t){this.ConditionOrderdialogFormVisible=!0,this.tmpconditionorder.conditionid=this.conditionList[t].id,this.searchConditionorder()},searchConditionorder:function(){var t=this;Object(s.c)(this.tmpconditionorder).then(function(i){t.conditionorderList=i.data}).catch(function(i){t.$message.error("查询条件顺序失败")})},updatecondition:function(){var t=this;this.$refs.tmpcondition.validate(function(i){i&&Object(a.g)(t.tmpcondition).then(function(){t.$message.success("更新成功"),t.getconditionList(),t.dialogFormVisible=!1}).catch(function(i){t.$message.error("更新失败")})})},removecondition:function(t){var i=this;this.$confirm("删除该条件？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var e=i.conditionList[t].id;Object(a.e)(e).then(function(){i.$message.success("删除成功"),i.getconditionList()})}).catch(function(){i.$message.info("已取消删除")})},isUniqueDetail:function(t){for(var i=0;i<this.conditionList.length;i++)if(this.conditionList[i].id!==t.id&&this.conditionList[i].enviromentname===t.enviromentname&&this.conditionList[i].machinename===t.machinename)return this.$message.error("条件名已存在"),!1;return!0}}},h={render:function(){var t=this,i=t.$createElement,e=t._self._c||i;return e("div",{staticClass:"app-container"},[e("div",{staticClass:"filter-container"},[e("el-form",{attrs:{inline:!0}},[e("el-form-item",[t.hasPermission("condition:list")?e("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(i){return i.preventDefault(),t.getconditionList(i)}}},[t._v("刷新")]):t._e(),t._v(" "),t.hasPermission("condition:add")?e("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(i){return i.preventDefault(),t.showAddconditionDialog(i)}}},[t._v("添加条件")]):t._e()],1),t._v(" "),t.hasPermission("condition:search")?e("span",[e("el-form-item",{attrs:{label:"父条件名："}},[e("el-input",{attrs:{clearable:"",placeholder:"条件名"},nativeOn:{keyup:function(i){return!i.type.indexOf("key")&&t._k(i.keyCode,"enter",13,i.key,"Enter")?null:t.searchBy(i)}},model:{value:t.search.conditionname,callback:function(i){t.$set(t.search,"conditionname",i)},expression:"search.conditionname"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"目标类型："}},[e("el-select",{attrs:{placeholder:"目标类型",clearable:""},model:{value:t.search.objecttype,callback:function(i){t.$set(t.search,"objecttype",i)},expression:"search.objecttype"}},[e("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),e("el-option",{attrs:{label:"测试集合",value:"测试集合"}}),t._v(" "),e("el-option",{attrs:{label:"测试用例",value:"测试用例"}})],1)],1),t._v(" "),e("el-form-item",[e("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()],1)],1),t._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:t.itemKey,attrs:{data:t.conditionList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[e("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:t._u([{key:"default",fn:function(i){return[e("span",{domProps:{textContent:t._s(t.getIndex(i.$index))}})]}}])}),t._v(" "),e("el-table-column",{attrs:{label:"父条件名",align:"center",prop:"conditionname",width:"120"}}),t._v(" "),e("el-table-column",{attrs:{label:"条件目标",align:"center",prop:"objectname",width:"120"}}),t._v(" "),e("el-table-column",{attrs:{label:"目标类型",align:"center",prop:"objecttype",width:"100"}}),t._v(" "),e("el-table-column",{attrs:{label:"条件类型",align:"center",prop:"conditiontype",width:"100"}}),t._v(" "),e("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"备注",align:"center",prop:"memo",width:"50"}}),t._v(" "),e("el-table-column",{attrs:{label:"操作人",align:"center",prop:"creator",width:"70"}}),t._v(" "),e("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"140"},scopedSlots:t._u([{key:"default",fn:function(i){return[t._v(t._s(t.unix2CurrentTime(i.row.createTime)))]}}])}),t._v(" "),e("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"140"},scopedSlots:t._u([{key:"default",fn:function(i){return[t._v(t._s(t.unix2CurrentTime(i.row.lastmodifyTime))+"\n        ")]}}])}),t._v(" "),t.hasPermission("condition:update")||t.hasPermission("condition:delete")?e("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:t._u([{key:"default",fn:function(i){return[t.hasPermission("condition:update")&&i.row.id!==t.id?e("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(e){return e.preventDefault(),t.showUpdateconditionDialog(i.$index)}}},[t._v("修改")]):t._e(),t._v(" "),t.hasPermission("condition:delete")&&i.row.id!==t.id?e("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(e){return e.preventDefault(),t.removecondition(i.$index)}}},[t._v("删除")]):t._e(),t._v(" "),t.hasPermission("condition:update")&&i.row.id!==t.id?e("el-button",{attrs:{type:"primary",size:"mini"},nativeOn:{click:function(e){return e.preventDefault(),t.showconditionorderDialog(i.$index)}}},[t._v("子条件排序")]):t._e()]}}],null,!1,1385280596)}):t._e()],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.search.page,"page-size":t.search.size,total:t.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),e("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(i){t.dialogFormVisible=i}}},[e("el-form",{ref:"tmpcondition",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:t.tmpcondition}},[e("el-form-item",{attrs:{label:"父条件名",prop:"conditionname",required:""}},[e("el-input",{attrs:{type:"text",maxlength:"40","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:t.tmpcondition.conditionname,callback:function(i){t.$set(t.tmpcondition,"conditionname",i)},expression:"tmpcondition.conditionname"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"目标类型",prop:"objecttype",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"目标类型"},on:{change:function(i){return t.targetChanged(i)}},model:{value:t.tmpcondition.objecttype,callback:function(i){t.$set(t.tmpcondition,"objecttype",i)},expression:"tmpcondition.objecttype"}},[e("el-option",{attrs:{label:"测试用例",value:"测试用例"}}),t._v(" "),e("el-option",{attrs:{label:"测试集合",value:"测试集合"}})],1)],1),t._v(" "),t.testcasevisible?e("div",[e("el-form-item",{attrs:{label:"微服务",prop:"deployunitname",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"微服务"},on:{change:function(i){return t.selectChanged(i)}},model:{value:t.tmpcondition.deployunitname,callback:function(i){t.$set(t.tmpcondition,"deployunitname",i)},expression:"tmpcondition.deployunitname"}},[e("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.deployunitList,function(t,i){return e("div",{key:i},[e("el-option",{attrs:{label:t.deployunitname,value:t.deployunitname,required:""}})],1)})],2)],1),t._v(" "),e("el-form-item",{attrs:{label:"API",prop:"apiname",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"API"},on:{change:function(i){return t.apiselectChanged(i)}},model:{value:t.tmpcondition.apiname,callback:function(i){t.$set(t.tmpcondition,"apiname",i)},expression:"tmpcondition.apiname"}},[e("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.apiList,function(t,i){return e("div",{key:i},[e("el-option",{attrs:{label:t.apiname,value:t.apiname,required:""}})],1)})],2)],1),t._v(" "),e("el-form-item",{attrs:{label:"用例",prop:"objectname",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"用例"},on:{change:function(i){return t.testcaseChanged(i)}},model:{value:t.tmpcondition.objectname,callback:function(i){t.$set(t.tmpcondition,"objectname",i)},expression:"tmpcondition.objectname"}},[e("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.caseList,function(t,i){return e("div",{key:i},[e("el-option",{attrs:{label:t.casename,value:t.casename,required:""}})],1)})],2)],1)],1):t._e(),t._v(" "),t.executeplanVisible?e("div",[e("el-form-item",{attrs:{label:"测试集合",prop:"objectname",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"测试集合"},on:{change:function(i){return t.execplanChanged(i)}},model:{value:t.tmpcondition.objectname,callback:function(i){t.$set(t.tmpcondition,"objectname",i)},expression:"tmpcondition.objectname"}},[e("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.execplanList,function(t,i){return e("div",{key:i},[e("el-option",{attrs:{label:t.executeplanname,value:t.executeplanname}})],1)})],2)],1)],1):t._e(),t._v(" "),e("el-form-item",{attrs:{label:"条件类型",prop:"conditiontype",required:""}},[e("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"条件类型"},model:{value:t.tmpcondition.conditiontype,callback:function(i){t.$set(t.tmpcondition,"conditiontype",i)},expression:"tmpcondition.conditiontype"}},[e("el-option",{attrs:{label:"前置条件",value:"前置条件"}})],1)],1),t._v(" "),e("el-form-item",{attrs:{label:"备注",prop:"memo"}},[e("el-input",{attrs:{type:"text",maxlength:"100","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmpcondition.memo,callback:function(i){t.$set(t.tmpcondition,"memo",i)},expression:"tmpcondition.memo"}})],1)],1),t._v(" "),e("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{nativeOn:{click:function(i){i.preventDefault(),t.dialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),"add"===t.dialogStatus?e("el-button",{attrs:{type:"danger"},nativeOn:{click:function(i){return i.preventDefault(),t.$refs.tmpcondition.resetFields()}}},[t._v("重置")]):t._e(),t._v(" "),"add"===t.dialogStatus?e("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(i){return i.preventDefault(),t.addcondition(i)}}},[t._v("添加")]):t._e(),t._v(" "),"update"===t.dialogStatus?e("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(i){return i.preventDefault(),t.updatecondition(i)}}},[t._v("修改")]):t._e()],1)],1),t._v(" "),e("el-dialog",{attrs:{title:"子条件顺序",visible:t.ConditionOrderdialogFormVisible},on:{"update:visible":function(i){t.ConditionOrderdialogFormVisible=i}}},[e("el-form",{staticClass:"small-space",staticStyle:{width:"600px","margin-left":"30px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px"}},[e("el-table",{staticStyle:{width:"100%"},attrs:{data:t.conditionorderList,border:"",fit:"","highlight-current-row":""}},[e("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:t._u([{key:"default",fn:function(i){return[e("span",{domProps:{textContent:t._s(t.getIndex(i.$index))}})]}}])}),t._v(" "),e("el-table-column",{attrs:{prop:"conditionname",align:"center",label:"父条件",width:"150px"}}),t._v(" "),e("el-table-column",{attrs:{prop:"subconditionname",align:"center",label:"子条件",width:"80px"}}),t._v(" "),e("el-table-column",{attrs:{prop:"subconditiontype",align:"center",label:"条件类型",width:"70px"}}),t._v(" "),e("el-table-column",{attrs:{prop:"orderstatus",align:"center",label:"状态",width:"60px"}}),t._v(" "),e("el-table-column",{attrs:{prop:"conditionorder",align:"center",label:"顺序",width:"50px"}}),t._v(" "),e("el-table-column",{attrs:{label:"操作排序",align:"center",width:"140px"},scopedSlots:t._u([{key:"default",fn:function(i){return[e("el-button",{attrs:{size:"mini",disabled:0===i.$index},on:{click:function(e){return t.moveUp(i.$index,i.row)}}},[e("i",{staticClass:"el-icon-arrow-up"})]),t._v(" "),e("el-button",{attrs:{size:"mini",disabled:i.$index===t.conditionorderList.length-1},on:{click:function(e){return t.moveDown(i.$index,i.row)}}},[e("i",{staticClass:"el-icon-arrow-down"})])]}}])})],1)],1),t._v(" "),e("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{nativeOn:{click:function(i){i.preventDefault(),t.ConditionOrderdialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"success"},nativeOn:{click:function(i){return i.preventDefault(),t.saveconditionorder(i)}}},[t._v("保存")])],1)],1)],1)},staticRenderFns:[]},b=e("VU/8")(m,h,!1,null,null,null);i.default=b.exports}});
//# sourceMappingURL=63.5609743426f2688dfede.js.map