webpackJsonp([49],{aWE7:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("Dd8w"),a=n.n(i),r=n("W/rB"),o=n("0xDb"),s=n("NYxO"),l={name:"测试环境",filters:{statusFilter:function(e){return{published:"success",draft:"gray",deleted:"danger"}[e]}},data:function(){return{id:null,itemKey:null,tmpenviromentname:"",enviromentList:[],listLoading:!1,total:0,dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改环境",update:"修改环境",add:"添加环境"},btnLoading:!1,tmpenviroment:{id:"",enviromentname:"",envtype:"",memo:"",creator:"",projectid:""},search:{page:1,size:10,enviromentname:null,projectid:""}}},created:function(){this.search.projectid=window.localStorage.getItem("pid"),this.getenviromentList()},computed:a()({},Object(s.b)(["name","sidebar","projectlist","projectid"])),methods:{unix2CurrentTime:o.b,getenviromentList:function(){var e=this;this.listLoading=!0,this.search.enviromentname=this.tmpenviromentname,Object(r.f)(this.search).then(function(t){e.enviromentList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载环境列表失败")})},searchBy:function(){var e=this;this.search.page=1,this.listLoading=!0,Object(r.f)(this.search).then(function(t){e.itemKey=Math.random(),e.enviromentList=t.data.list,e.total=t.data.total}).catch(function(t){e.$message.error("搜索失败")}),this.tmpenviromentname=this.search.enviromentname,this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(e){this.search.page=1,this.search.size=e,this.getenviromentList()},handleCurrentChange:function(e){this.search.page=e,this.getenviromentList()},getIndex:function(e){return(this.search.page-1)*this.search.size+e+1},showAddenviromentDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpenviroment.id="",this.tmpenviroment.enviromentname="",this.tmpenviroment.memo="",this.tmpenviroment.envtype="",this.tmpenviroment.creator=this.name,this.tmpenviroment.projectid=window.localStorage.getItem("pid")},addenviroment:function(){var e=this;this.$refs.tmpenviroment.validate(function(t){t&&(e.btnLoading=!0,Object(r.a)(e.tmpenviroment).then(function(){e.$message.success("添加成功"),e.getenviromentList(),e.dialogFormVisible=!1,e.btnLoading=!1}).catch(function(t){e.$message.error("添加失败"),e.btnLoading=!1}))})},showUpdateenviromentDialog:function(e){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmpenviroment.id=this.enviromentList[e].id,this.tmpenviroment.enviromentname=this.enviromentList[e].enviromentname,this.tmpenviroment.envtype=this.enviromentList[e].envtype,this.tmpenviroment.memo=this.enviromentList[e].memo,this.tmpenviroment.creator=this.name,this.tmpenviroment.projectid=window.localStorage.getItem("pid")},updateenviroment:function(){var e=this;this.$refs.tmpenviroment.validate(function(t){t&&Object(r.g)(e.tmpenviroment).then(function(){e.$message.success("更新成功"),e.getenviromentList(),e.dialogFormVisible=!1}).catch(function(t){e.$message.error("更新失败")})})},removeenviroment:function(e){var t=this;this.$confirm("删除该测试环境？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var n=t.enviromentList[e].id;Object(r.e)(n).then(function(){t.$message.success("删除成功"),t.getenviromentList()})}).catch(function(){t.$message.info("已取消删除")})},isUniqueDetail:function(e){for(var t=0;t<this.enviromentList.length;t++)if(this.enviromentList[t].id!==e.id&&this.enviromentList[t].enviromentname===e.enviromentname)return this.$message.error("测试环境名已存在"),!1;return!0}}},m={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-form",{attrs:{inline:!0}},[n("el-form-item",[e.hasPermission("enviroment:list")?n("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(t){return t.preventDefault(),e.getenviromentList(t)}}},[e._v("刷新")]):e._e(),e._v(" "),e.hasPermission("enviroment:add")?n("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(t){return t.preventDefault(),e.showAddenviromentDialog(t)}}},[e._v("添加测试环境")]):e._e()],1),e._v(" "),e.hasPermission("enviroment:search")?n("span",[n("el-form-item",[n("el-input",{attrs:{clearable:"",maxlength:"40",placeholder:"测试环境名"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchBy(t)}},model:{value:e.search.enviromentname,callback:function(t){e.$set(e.search,"enviromentname",t)},expression:"search.enviromentname"}})],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary",loading:e.btnLoading},on:{click:e.searchBy}},[e._v("查询")])],1)],1):e._e()],1)],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],key:e.itemKey,attrs:{data:e.enviromentList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{domProps:{textContent:e._s(e.getIndex(t.$index))}})]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"测试环境名",align:"center",prop:"enviromentname",width:"150"}}),e._v(" "),n("el-table-column",{attrs:{label:"环境类型",align:"center",prop:"envtype",width:"80"}}),e._v(" "),n("el-table-column",{attrs:{label:"描述",align:"center",prop:"memo",width:"150"}}),e._v(" "),n("el-table-column",{attrs:{label:"操作人",align:"center",prop:"creator",width:"100"}}),e._v(" "),n("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.createTime)))]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(e._s(e.unix2CurrentTime(t.row.lastmodifyTime))+"\n      ")]}}])}),e._v(" "),e.hasPermission("enviroment:update")||e.hasPermission("enviroment:delete")?n("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e.hasPermission("enviroment:update")&&t.row.id!==e.id?n("el-button",{attrs:{type:"primary",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),e.showUpdateenviromentDialog(t.$index)}}},[e._v("中间件")]):e._e(),e._v(" "),e.hasPermission("enviroment:update")&&t.row.id!==e.id?n("el-button",{attrs:{type:"primary",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),e.showUpdateenviromentDialog(t.$index)}}},[e._v("部署")]):e._e(),e._v(" "),e.hasPermission("enviroment:update")&&t.row.id!==e.id?n("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),e.showUpdateenviromentDialog(t.$index)}}},[e._v("修改")]):e._e(),e._v(" "),e.hasPermission("enviroment:delete")&&t.row.id!==e.id?n("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(n){return n.preventDefault(),e.removeenviroment(t.$index)}}},[e._v("删除")]):e._e()]}}],null,!1,4130071635)}):e._e()],1),e._v(" "),n("el-pagination",{attrs:{"current-page":e.search.page,"page-size":e.search.size,total:e.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),e._v(" "),n("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"tmpenviroment",staticClass:"small-space",staticStyle:{width:"400px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"120px",model:e.tmpenviroment}},[n("el-form-item",{attrs:{label:"测试环境名",prop:"enviromentname",required:""}},[n("el-input",{attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:e.tmpenviroment.enviromentname,callback:function(t){e.$set(e.tmpenviroment,"enviromentname",t)},expression:"tmpenviroment.enviromentname"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"环境类型",prop:"envtype",required:""}},[n("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"环境类型"},model:{value:e.tmpenviroment.envtype,callback:function(t){e.$set(e.tmpenviroment,"envtype",t)},expression:"tmpenviroment.envtype"}},[n("el-option",{attrs:{label:"功能",value:"功能"}}),e._v(" "),n("el-option",{attrs:{label:"性能",value:"性能"}})],1)],1),e._v(" "),n("el-form-item",{attrs:{label:"备注",prop:"memo"}},[n("el-input",{attrs:{maxlength:"60",type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:e.tmpenviroment.memo,callback:function(t){e.$set(e.tmpenviroment,"memo",t)},expression:"tmpenviroment.memo"}})],1)],1),e._v(" "),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{nativeOn:{click:function(t){t.preventDefault(),e.dialogFormVisible=!1}}},[e._v("取消")]),e._v(" "),"add"===e.dialogStatus?n("el-button",{attrs:{type:"danger"},nativeOn:{click:function(t){return t.preventDefault(),e.$refs.tmpenviroment.resetFields()}}},[e._v("重置")]):e._e(),e._v(" "),"add"===e.dialogStatus?n("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.addenviroment(t)}}},[e._v("添加")]):e._e(),e._v(" "),"update"===e.dialogStatus?n("el-button",{attrs:{type:"success",loading:e.btnLoading},nativeOn:{click:function(t){return t.preventDefault(),e.updateenviroment(t)}}},[e._v("修改")]):e._e()],1)],1)],1)},staticRenderFns:[]},c=n("VU/8")(l,m,!1,null,null,null);t.default=c.exports}});
//# sourceMappingURL=49.38a35f13e13749cd9139.js.map