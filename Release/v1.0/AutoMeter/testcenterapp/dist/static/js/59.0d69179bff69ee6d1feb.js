webpackJsonp([59],{"7faG":function(t,i,n){"use strict";Object.defineProperty(i,"__esModule",{value:!0});var e=n("Dd8w"),o=n.n(e),s=n("sc77"),c=n("IBLw"),a=n("0xDb"),r=n("NYxO"),d={filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{id:null,itemKey:null,tmpconditionname:"",conditionList:[],scriptconditionList:[],listLoading:!1,total:0,listQuery:{page:1,size:10,listLoading:!0},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改脚本条件",update:"修改脚本条件",add:"添加脚本条件"},btnLoading:!1,tmpconditionquery:{objecttype:"",projectid:""},tmpscriptcondition:{id:"",subconditionname:"",conditionid:"",conditionname:"",script:"",creator:"",projectid:""},search:{page:1,size:10,conditionname:null,projectid:""}}},computed:o()({},Object(r.b)(["name","sidebar","projectlist","projectid"])),created:function(){this.search.projectid=window.localStorage.getItem("pid"),this.tmpconditionquery.projectid=window.localStorage.getItem("pid"),this.getscriptconditionList(),this.getalltestconditionbytype()},activated:function(){this.getalltestconditionbytype()},methods:{unix2CurrentTime:a.b,ConditionselectChanged:function(t){for(var i=0;i<this.conditionList.length;i++)this.conditionList[i].conditionname===t&&(this.tmpscriptcondition.conditionid=this.conditionList[i].id)},getscriptconditionList:function(){var t=this;this.listLoading=!0,this.search.conditionname=this.tmpconditionname,Object(s.d)(this.search).then(function(i){t.scriptconditionList=i.data.list,t.total=i.data.total,t.listLoading=!1}).catch(function(i){t.$message.error("加载测试脚本条件列表失败")})},getalltestconditionbytype:function(){var t=this;this.listLoading=!0,this.tmpconditionquery.objecttype="测试用例",Object(c.d)(this.tmpconditionquery).then(function(i){t.conditionList=i.data,t.total=i.data.total,t.listLoading=!1}).catch(function(i){t.$message.error("加载条件列表失败")})},searchBy:function(){var t=this;this.search.page=1,this.listLoading=!0,Object(s.d)(this.search).then(function(i){t.itemKey=Math.random(),t.scriptconditionList=i.data.list,t.total=i.data.total}).catch(function(i){t.$message.error("搜索失败")}),this.tmpconditionname=this.search.conditionname,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(t){this.search.page=1,this.search.size=t,this.getscriptconditionList()},handleCurrentChange:function(t){this.search.page=t,this.getscriptconditionList()},getIndex:function(t){return(this.search.page-1)*this.search.size+t+1},showAddscriptconditionDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpscriptcondition.id="",this.tmpscriptcondition.subconditionname="",this.tmpscriptcondition.conditionname="",this.tmpscriptcondition.script="",this.tmpscriptcondition.creator=this.name,this.tmpscriptcondition.projectid=window.localStorage.getItem("pid")},addscriptcondition:function(){var t=this;this.$refs.tmpscriptcondition.validate(function(i){i&&(t.btnLoading=!0,Object(s.a)(t.tmpscriptcondition).then(function(){t.$message.success("添加成功"),t.getscriptconditionList(),t.dialogFormVisible=!1,t.btnLoading=!1}).catch(function(i){t.$message.error("添加失败"),t.btnLoading=!1}))})},showUpdatescriptconditionDialog:function(t){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpscriptcondition.id=this.scriptconditionList[t].id,this.tmpscriptcondition.subconditionname=this.scriptconditionList[t].subconditionname,this.tmpscriptcondition.conditionname=this.scriptconditionList[t].conditionname,this.tmpscriptcondition.script=this.scriptconditionList[t].script,this.tmpscriptcondition.creator=this.name;for(var i=0;i<this.conditionList.length;i++)this.conditionList[i].conditionname===this.scriptconditionList[t].conditionname&&(this.tmpscriptcondition.conditionid=this.conditionList[i].id)},updatescriptcondition:function(){var t=this;this.$refs.tmpscriptcondition.validate(function(i){i&&Object(s.e)(t.tmpscriptcondition).then(function(){t.$message.success("更新成功"),t.getscriptconditionList(),t.dialogFormVisible=!1}).catch(function(i){t.$message.error("更新失败")})})},removescriptcondition:function(t){var i=this;this.$confirm("删除该脚本条件？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var n=i.scriptconditionList[t].id;Object(s.c)(n).then(function(){i.$message.success("删除成功"),i.getscriptconditionList()})}).catch(function(){i.$message.info("已取消删除")})},isUniqueDetail:function(t){for(var i=0;i<this.scriptconditionList.length;i++)if(this.scriptconditionList[i].id!==t.id&&this.scriptconditionList[i].enviromentname===t.enviromentname&&this.scriptconditionList[i].machinename===t.machinename)return this.$message.error("脚本条件名已存在"),!1;return!0}}},l={render:function(){var t=this,i=t.$createElement,n=t._self._c||i;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-form",{attrs:{inline:!0}},[n("el-form-item",[t.hasPermission("scriptcondition:list")?n("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(i){return i.preventDefault(),t.getscriptconditionList(i)}}},[t._v("刷新")]):t._e(),t._v(" "),t.hasPermission("scriptcondition:add")?n("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(i){return i.preventDefault(),t.showAddscriptconditionDialog(i)}}},[t._v("添加脚本条件")]):t._e()],1),t._v(" "),t.hasPermission("scriptcondition:search")?n("span",[n("el-form-item",{attrs:{label:"父条件名:"}},[n("el-select",{attrs:{filterable:"",clearable:"",placeholder:"父条件名"},model:{value:t.search.conditionname,callback:function(i){t.$set(t.search,"conditionname",i)},expression:"search.conditionname"}},[n("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),t._l(t.conditionList,function(t,i){return n("div",{key:i},[n("el-option",{attrs:{label:t.conditionname,value:t.conditionname}})],1)})],2)],1),t._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()],1)],1),t._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:t.itemKey,attrs:{data:t.scriptconditionList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:t._u([{key:"default",fn:function(i){return[n("span",{domProps:{textContent:t._s(t.getIndex(i.$index))}})]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"子条件名",align:"center",prop:"subconditionname",width:"200"}}),t._v(" "),n("el-table-column",{attrs:{label:"父条件名",align:"center",prop:"conditionname",width:"200"}}),t._v(" "),n("el-table-column",{attrs:{label:"脚本",align:"center",prop:"script",width:"150"},scopedSlots:t._u([{key:"default",fn:function(i){return[n("el-popover",{attrs:{trigger:"hover",placement:"top"}},[n("p",[t._v(t._s(i.row.script))]),t._v(" "),n("div",{staticClass:"name-wrapper",attrs:{slot:"reference"},slot:"reference"},[n("el-tag",{attrs:{size:"medium"}},[t._v("...")])],1)])]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"操作人",align:"center",prop:"creator",width:"70"}}),t._v(" "),n("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:t._u([{key:"default",fn:function(i){return[t._v(t._s(t.unix2CurrentTime(i.row.createTime)))]}}])}),t._v(" "),n("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"160"},scopedSlots:t._u([{key:"default",fn:function(i){return[t._v(t._s(t.unix2CurrentTime(i.row.lastmodifyTime))+"\n      ")]}}])}),t._v(" "),t.hasPermission("scriptcondition:update")||t.hasPermission("scriptcondition:delete")?n("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:t._u([{key:"default",fn:function(i){return[t.hasPermission("scriptcondition:update")&&i.row.id!==t.id?n("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),t.showUpdatescriptconditionDialog(i.$index)}}},[t._v("修改")]):t._e(),t._v(" "),t.hasPermission("scriptcondition:delete")&&i.row.id!==t.id?n("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),t.removescriptcondition(i.$index)}}},[t._v("删除")]):t._e()]}}],null,!1,2966294022)}):t._e()],1),t._v(" "),n("el-pagination",{attrs:{"current-page":t.search.page,"page-size":t.search.size,total:t.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),n("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(i){t.dialogFormVisible=i}}},[n("el-form",{ref:"tmpscriptcondition",staticClass:"small-space",staticStyle:{width:"600px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:t.tmpscriptcondition}},[n("el-form-item",{attrs:{label:"子条件名",prop:"subconditionname",required:""}},[n("el-input",{attrs:{type:"text",maxlength:"30","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:t.tmpscriptcondition.subconditionname,callback:function(i){t.$set(t.tmpscriptcondition,"subconditionname",i)},expression:"tmpscriptcondition.subconditionname"}})],1),t._v(" "),n("el-form-item",{attrs:{label:"父条件名",prop:"conditionname",required:""}},[n("el-select",{staticStyle:{width:"100%"},attrs:{filterable:"",placeholder:"父条件名"},on:{change:function(i){return t.ConditionselectChanged(i)}},model:{value:t.tmpscriptcondition.conditionname,callback:function(i){t.$set(t.tmpscriptcondition,"conditionname",i)},expression:"tmpscriptcondition.conditionname"}},[n("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.conditionList,function(t,i){return n("div",{key:i},[n("el-option",{attrs:{label:t.conditionname,value:t.conditionname,required:""}})],1)})],2)],1),t._v(" "),n("el-form-item",{attrs:{label:"Java代码",prop:"script",required:""}},[n("el-input",{attrs:{type:"textarea",rows:"10",cols:"50",maxlength:"4000",placeholder:"Java 代码"},model:{value:t.tmpscriptcondition.script,callback:function(i){t.$set(t.tmpscriptcondition,"script",i)},expression:"tmpscriptcondition.script"}})],1)],1),t._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{nativeOn:{click:function(i){i.preventDefault(),t.dialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),"add"===t.dialogStatus?n("el-button",{attrs:{type:"danger"},nativeOn:{click:function(i){return i.preventDefault(),t.$refs.tmpscriptcondition.resetFields()}}},[t._v("重置")]):t._e(),t._v(" "),"add"===t.dialogStatus?n("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(i){return i.preventDefault(),t.addscriptcondition(i)}}},[t._v("添加")]):t._e(),t._v(" "),"update"===t.dialogStatus?n("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(i){return i.preventDefault(),t.updatescriptcondition(i)}}},[t._v("修改")]):t._e()],1)],1)],1)},staticRenderFns:[]},p=n("VU/8")(d,l,!1,null,null,null);i.default=p.exports}});
//# sourceMappingURL=59.0d69179bff69ee6d1feb.js.map