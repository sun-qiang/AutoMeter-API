webpackJsonp([34],{"/Nxf":function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var l=t("Dd8w"),i=t.n(l),s=t("CwVP"),n=t("0xDb"),o=t("NYxO"),r={name:"全局变量",filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{id:null,itemKey:null,tmpglobalvariablesname:"",globalvariablesList:[],listLoading:!1,total:0,dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改全局变量",update:"修改全局变量",add:"添加全局变量"},btnLoading:!1,tmpglobalvariables:{id:"",keyname:"",keyvalue:"",memo:"",projectid:"",creator:""},search:{page:1,size:10,keyname:null,accountId:null,projectid:""}}},created:function(){this.tmpglobalvariables.creator=this.name,this.search.accountId=this.accountId,this.search.projectid=window.localStorage.getItem("pid"),this.getglobalvariablesList()},computed:i()({},Object(o.b)(["name","sidebar","projectlist","projectid","accountId"])),methods:{unix2CurrentTime:n.b,getglobalvariablesList:function(){var e=this;this.listLoading=!0,this.search.globalvariablesname=this.tmpglobalvariablesname,Object(s.c)(this.search).then(function(a){e.globalvariablesList=a.data.list,e.total=a.data.total,e.listLoading=!1}).catch(function(a){e.$message.error("加载全局变量列表失败")})},searchBy:function(){var e=this;this.search.page=1,this.listLoading=!0,Object(s.c)(this.search).then(function(a){e.itemKey=Math.random(),e.globalvariablesList=a.data.list,e.total=a.data.total}).catch(function(a){e.$message.error("搜索失败")}),this.tmpglobalvariablesname=this.search.globalvariablesname,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(e){this.search.page=1,this.search.size=e,this.getglobalvariablesList()},handleCurrentChange:function(e){this.search.page=e,this.getglobalvariablesList()},getIndex:function(e){return(this.search.page-1)*this.search.size+e+1},showAddglobalvariablesDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpglobalvariables.id="",this.tmpglobalvariables.keyname="",this.tmpglobalvariables.keyvalue="",this.tmpglobalvariables.memo="",this.tmpglobalvariables.projectid=window.localStorage.getItem("pid")},addglobalvariables:function(){var e=this;this.$refs.tmpglobalvariables.validate(function(a){a&&(e.btnLoading=!0,Object(s.a)(e.tmpglobalvariables).then(function(){e.$message.success("添加成功"),e.getglobalvariablesList(),e.dialogFormVisible=!1,e.btnLoading=!1}).catch(function(a){e.$message.error("添加失败"),e.btnLoading=!1}))})},showUpdateglobalvariablesDialog:function(e){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpglobalvariables.id=this.globalvariablesList[e].id,this.tmpglobalvariables.keyname=this.globalvariablesList[e].keyname,this.tmpglobalvariables.keyvalue=this.globalvariablesList[e].keyvalue,this.tmpglobalvariables.memo=this.globalvariablesList[e].memo},updateglobalvariables:function(){var e=this;this.$refs.tmpglobalvariables.validate(function(a){a&&Object(s.d)(e.tmpglobalvariables).then(function(){e.$message.success("更新成功"),e.getglobalvariablesList(),e.dialogFormVisible=!1}).catch(function(a){e.$message.error("更新失败")})})},removeglobalvariables:function(e){var a=this;this.$confirm("删除该全局变量？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var t=a.globalvariablesList[e].id;Object(s.b)(t).then(function(){a.$message.success("删除成功"),a.getglobalvariablesList()})}).catch(function(){a.$message.info("已取消删除")})},isUniqueDetail:function(e){for(var a=0;a<this.globalvariablesList.length;a++)if(this.globalvariablesList[a].id!==e.id&&this.globalvariablesList[a].globalvariablesname===e.globalvariablesname)return this.$message.error("全局变量名已存在"),!1;return!0}}},c={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",{staticClass:"app-container"},[t("div",{staticClass:"filter-container"},[t("el-form",{attrs:{inline:!0}},[t("el-form-item",[e.hasPermission("globalvariables:list")?t("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(a){return a.preventDefault(),e.getglobalvariablesList(a)}}},[e._v("刷新")]):e._e(),e._v(" "),e.hasPermission("globalvariables:add")?t("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(a){return a.preventDefault(),e.showAddglobalvariablesDialog(a)}}},[e._v("添加全局变量")]):e._e()],1),e._v(" "),e.hasPermission("globalvariables:search")?t("span",[t("el-form-item",[t("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"全局变量名"},nativeOn:{keyup:function(a){return!a.type.indexOf("key")&&e._k(a.keyCode,"enter",13,a.key,"Enter")?null:e.searchBy(a)}},model:{value:e.search.keyname,callback:function(a){e.$set(e.search,"keyname",a)},expression:"search.keyname"}})],1),e._v(" "),t("el-form-item",[t("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchBy}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),t("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.globalvariablesList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[t("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("span",{domProps:{textContent:e._s(e.getIndex(a.$index))}})]}}])}),e._v(" "),t("el-table-column",{attrs:{label:"全局变量名",align:"center",prop:"keyname",width:"150"}}),e._v(" "),t("el-table-column",{attrs:{"show-overflow-tooltip":!0,label:"变量值",align:"center",prop:"keyvalue",width:"80"}}),e._v(" "),t("el-table-column",{attrs:{label:"描述",align:"center",prop:"memo",width:"250"}}),e._v(" "),t("el-table-column",{attrs:{label:"维护人",align:"center",prop:"creator",width:"70"}}),e._v(" "),t("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(a){return[e._v(e._s(e.unix2CurrentTime(a.row.createTime)))]}}])}),e._v(" "),t("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(a){return[e._v(e._s(e.unix2CurrentTime(a.row.lastmodifyTime))+"\n      ")]}}])}),e._v(" "),e.hasPermission("globalvariables:update")||e.hasPermission("globalvariables:delete")?t("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[e.hasPermission("globalvariables:update")&&a.row.id!==e.id?t("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(t){return t.preventDefault(),e.showUpdateglobalvariablesDialog(a.$index)}}},[e._v("修改")]):e._e(),e._v(" "),e.hasPermission("globalvariables:delete")&&a.row.id!==e.id?t("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(t){return t.preventDefault(),e.removeglobalvariables(a.$index)}}},[e._v("删除")]):e._e()]}}],null,!1,1457505926)}):e._e()],1),e._v(" "),t("el-pagination",{attrs:{"current-page":e.search.page,"page-size":e.search.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),t("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],width:"800px",visible:e.dialogFormVisible},on:{"update:visible":function(a){e.dialogFormVisible=a}}},[t("el-form",{ref:"tmpglobalvariables",staticClass:"small-space",staticStyle:{width:"550px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpglobalvariables}},[t("el-form-item",{attrs:{label:"全局变量名",prop:"keyname",required:""}},[t("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:e.tmpglobalvariables.keyname,callback:function(a){e.$set(e.tmpglobalvariables,"keyname",a)},expression:"tmpglobalvariables.keyname"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"变量值",prop:"keyvalue",required:""}},[t("el-input",{staticStyle:{width:"500px"},attrs:{type:"textarea",rows:"15",cols:"50","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpglobalvariables.keyvalue,callback:function(a){e.$set(e.tmpglobalvariables,"keyvalue",a)},expression:"tmpglobalvariables.keyvalue"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"备注",prop:"memo"}},[t("el-input",{staticStyle:{width:"500px"},attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpglobalvariables.memo,callback:function(a){e.$set(e.tmpglobalvariables,"memo",a)},expression:"tmpglobalvariables.memo"}})],1)],1),e._v(" "),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{nativeOn:{click:function(a){a.preventDefault(),e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.dialogStatus?t("el-button",{attrs:{type:"danger"},nativeOn:{click:function(a){return a.preventDefault(),e.$refs.tmpglobalvariables.resetFields()}}},[e._v("重置")]):e._e(),e._v(" "),"add"===e.dialogStatus?t("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(a){return a.preventDefault(),e.addglobalvariables(a)}}},[e._v("添加")]):e._e(),e._v(" "),"update"===e.dialogStatus?t("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(a){return a.preventDefault(),e.updateglobalvariables(a)}}},[e._v("修改")]):e._e()],1)],1)],1)},staticRenderFns:[]},b=t("VU/8")(r,c,!1,null,null,null);a.default=b.exports}});
//# sourceMappingURL=34.532b387bd3db91a13de2.js.map