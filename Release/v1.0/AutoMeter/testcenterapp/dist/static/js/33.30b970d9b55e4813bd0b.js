webpackJsonp([33],{jmYg:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=a("Dd8w"),l=a.n(i),s=a("IRn5"),r=a("0xDb"),n=a("NYxO"),o={name:"随机变量",filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{id:null,itemKey:null,tmpvariablesname:"",variablecondition:!1,variableconditionplaceholder:"1.数字范围使用英文逗号分开，例如：1,100 \n 2.字符串长度例如：4",variablesList:[],listLoading:!1,total:0,dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改变量",update:"修改变量",add:"添加变量"},btnLoading:!1,tmpvariables:{id:"",variablesname:"",variablestype:"",variablecondition:"",memo:"",creator:"",projectid:""},search:{page:1,size:10,accountId:null,variablesname:null,projectid:""}}},created:function(){this.tmpvariables.creator=this.name,this.search.accountId=this.accountId,this.search.projectid=window.localStorage.getItem("pid"),this.getvariablesList()},computed:l()({},Object(n.b)(["name","sidebar","projectlist","projectid","accountId"])),methods:{unix2CurrentTime:r.b,getvariablesList:function(){var e=this;this.listLoading=!0,this.search.variablesname=this.tmpvariablesname,Object(s.c)(this.search).then(function(t){e.variablesList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载环境列表失败")})},searchBy:function(){var e=this;this.search.page=1,this.listLoading=!0,Object(s.c)(this.search).then(function(t){e.itemKey=Math.random(),e.variablesList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmpvariablesname=this.search.variablesname,this.listLoading=!1,this.btnLoading=!1},variablestypeselectChanged:function(e){this.variablecondition="随机字符串"===e||"随机整数"===e||"随机小数"===e||"当前日期"===e||"随机数组值"===e},handleSizeChange:function(e){this.search.page=1,this.search.size=e,this.getvariablesList()},handleCurrentChange:function(e){this.search.page=e,this.getvariablesList()},getIndex:function(e){return(this.search.page-1)*this.search.size+e+1},showAddvariablesDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpvariables.id="",this.tmpvariables.variablesname="",this.tmpvariables.memo="",this.tmpvariables.variablecondition="",this.tmpvariables.variablestype="",this.tmpvariables.creator=this.name,this.tmpvariables.projectid=window.localStorage.getItem("pid")},addvariables:function(){var e=this;this.$refs.tmpvariables.validate(function(t){t&&(e.btnLoading=!0,Object(s.a)(e.tmpvariables).then(function(){e.$message.success("添加成功"),e.getvariablesList(),e.dialogFormVisible=!1,e.btnLoading=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1}))})},showUpdatevariablesDialog:function(e){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpvariables.id=this.variablesList[e].id,this.tmpvariables.variablesname=this.variablesList[e].variablesname,this.tmpvariables.variablestype=this.variablesList[e].variablestype,"随机字符串"===this.tmpvariables.variablestype||"随机整数"===this.tmpvariables.variablestype||"随机小数"===this.tmpvariables.variablestype?this.variablecondition=!0:this.variablecondition=!1,this.tmpvariables.variablecondition=this.variablesList[e].variablecondition,this.tmpvariables.memo=this.variablesList[e].memo,this.tmpvariables.creator=this.name},updatevariables:function(){var e=this;this.$refs.tmpvariables.validate(function(t){t&&Object(s.d)(e.tmpvariables).then(function(){e.$message.success("更新成功"),e.getvariablesList(),e.dialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")})})},removevariables:function(e){var t=this;this.$confirm("删除该随机变量？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var a=t.variablesList[e].id;Object(s.b)(a).then(function(){t.$message.success("删除成功"),t.getvariablesList()})}).catch(function(){t.$message.info("已取消删除")})},isUniqueDetail:function(e){for(var t=0;t<this.variablesList.length;t++)if(this.variablesList[t].id!==e.id&&this.variablesList[t].variablesname===e.variablesname)return this.$message.error("随机变量名已存在"),!1;return!0}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-form",{attrs:{inline:!0}},[a("el-form-item",[e.hasPermission("variables:list")?a("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(t){return t.preventDefault(),e.getvariablesList(t)}}},[e._v("刷新")]):e._e(),e._v(" "),e.hasPermission("variables:add")?a("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(t){return t.preventDefault(),e.showAddvariablesDialog(t)}}},[e._v("添加随机变量")]):e._e()],1),e._v(" "),e.hasPermission("variables:search")?a("span",[a("el-form-item",[a("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"随机变量名"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchBy(t)}},model:{value:e.search.variablesname,callback:function(t){e.$set(e.search,"variablesname",t)},expression:"search.variablesname"}})],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchBy}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.variablesList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"随机变量名",align:"center",prop:"variablesname",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{label:"变量类型",align:"center",prop:"variablestype",width:"120"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"变量条件",align:"center",prop:"variablecondition",width:"100"}}),e._v(" "),a("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"随机描述",align:"center",prop:"memo",width:"250"}}),e._v(" "),a("el-table-column",{attrs:{label:"维护人",align:"center",prop:"creator",width:"70"}}),e._v(" "),a("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),a("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n      ")]}}])}),e._v(" "),e.hasPermission("variables:update")||e.hasPermission("variables:delete")?a("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.hasPermission("variables:update")&&t.row.id!==e.id?a("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(a){return a.preventDefault(),e.showUpdatevariablesDialog(t.$index)}}},[e._v("修改")]):e._e(),e._v(" "),e.hasPermission("variables:delete")&&t.row.id!==e.id?a("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(a){return a.preventDefault(),e.removevariables(t.$index)}}},[e._v("删除")]):e._e()]}}],null,!1,1135086694)}):e._e()],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.search.page,"page-size":e.search.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),a("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],width:"800px",visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[a("el-form",{ref:"tmpvariables",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpvariables}},[a("el-form-item",{attrs:{label:"随机变量名",prop:"variablesname",required:""}},[a("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"50",type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:e.tmpvariables.variablesname,callback:function(t){e.$set(e.tmpvariables,"variablesname",t)},expression:"tmpvariables.variablesname"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"变量类型",prop:"variablestype",required:""}},[a("el-select",{staticStyle:{width:"500px"},attrs:{placeholder:"变量类型"},on:{change:function(t){return e.variablestypeselectChanged(t)}},model:{value:e.tmpvariables.variablestype,callback:function(t){e.$set(e.tmpvariables,"variablestype",t)},expression:"tmpvariables.variablestype"}},[a("el-option",{attrs:{label:"当前时间",value:"当前时间"}}),e._v(" "),a("el-option",{attrs:{label:"当前日期",value:"当前日期"}}),e._v(" "),a("el-option",{attrs:{label:"当前时间戳",value:"当前时间戳"}}),e._v(" "),a("el-option",{attrs:{label:"随机IP",value:"随机IP"}}),e._v(" "),a("el-option",{attrs:{label:"随机小数",value:"随机小数"}}),e._v(" "),a("el-option",{attrs:{label:"GUID",value:"GUID"}}),e._v(" "),a("el-option",{attrs:{label:"随机字符串",value:"随机字符串"}}),e._v(" "),a("el-option",{attrs:{label:"随机整数",value:"随机整数"}}),e._v(" "),a("el-option",{attrs:{label:"随机数组值",value:"随机数组值"}})],1)],1),e._v(" "),e.variablecondition?a("div",[a("el-form-item",{attrs:{label:"变量条件",prop:"variablecondition"}},[a("el-input",{staticStyle:{width:"500px"},attrs:{type:"textarea",rows:"3",cols:"30",placeholder:e.variableconditionplaceholder,"prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpvariables.variablecondition,callback:function(t){e.$set(e.tmpvariables,"variablecondition",t)},expression:"tmpvariables.variablecondition"}})],1)],1):e._e(),e._v(" "),a("el-form-item",{attrs:{label:"变量描述",prop:"memo"}},[a("el-input",{staticStyle:{width:"500px"},attrs:{type:"text",maxlength:"60",placeholder:"变量描述","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpvariables.memo,callback:function(t){e.$set(e.tmpvariables,"memo",t)},expression:"tmpvariables.memo"}})],1)],1),e._v(" "),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.dialogStatus?a("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpvariables.resetFields()}}},[e._v("重置")]):e._e(),e._v(" "),"add"===e.dialogStatus?a("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addvariables(t)}}},[e._v("添加")]):e._e(),e._v(" "),"update"===e.dialogStatus?a("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updatevariables(t)}}},[e._v("修改")]):e._e()],1)],1)],1)},staticRenderFns:[]},v=a("VU/8")(o,c,!1,null,null,null);t.default=v.exports}});
//# sourceMappingURL=33.30b970d9b55e4813bd0b.js.map