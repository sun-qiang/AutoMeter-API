webpackJsonp([64],{zSiE:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=s("vLgD");function r(t){return Object(i.a)({url:"/tester/search",method:"post",data:t})}var a=s("L6vm"),n={filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{tmptestername:null,tmptestertitle:null,tmptesterorg:null,testerList:[],testertitleList:[],testerorgList:[],listLoading:!1,diclevelQuery:{page:1,size:30,diccode:"testerlevel"},dicorgQuery:{page:1,size:30,diccode:"testerorg"},total:0,listQuery:{page:1,size:10,listLoading:!0,testername:null,testertitle:null,testerorg:null},dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改测试人员",update:"修改测试人员",add:"添加测试人员"},btnLoading:!1,tmptester:{id:"",testername:"",testertitle:"",testerorg:"",testermemo:""},search:{page:null,size:null,testername:null,testertitle:null,testerorg:null}}},created:function(){this.gettesterList(),this.gettestertitleList(),this.gettesterorgList()},methods:{unix2CurrentTime:s("0xDb").b,gettestertitleList:function(){var t=this;Object(a.b)(this.diclevelQuery).then(function(e){t.testertitleList=e.data.list}).catch(function(e){t.$message.error("加载角色职务失败")})},gettesterorgList:function(){var t=this;Object(a.b)(this.dicorgQuery).then(function(e){t.testerorgList=e.data.list,console.log(t.testerorgList)}).catch(function(e){t.$message.error("加载角色组织失败")})},gettesterList:function(){var t,e=this;this.listLoading=!0,(t=this.listQuery,Object(i.a)({url:"/tester",method:"get",params:t})).then(function(t){e.testerList=t.data.list,e.total=t.data.total,e.listLoading=!1}).catch(function(t){e.$message.error("加载测试人员列表失败")})},searchBy:function(){var t=this;this.btnLoading=!0,this.listLoading=!0,this.search.page=this.listQuery.page,this.search.size=this.listQuery.size,r(this.search).then(function(e){t.testerList=e.data.list,t.total=e.data.total}).catch(function(e){t.$message.error("搜索失败")}),this.tmptestername=this.search.testername,this.tmptestertitle=this.search.testertitle,this.tmptesterorg=this.search.testerorg,this.listLoading=!1,this.btnLoading=!1},searchBypageing:function(){var t=this;this.btnLoading=!0,this.listLoading=!0,this.listQuery.testername=this.tmptestername,this.listQuery.testertitle=this.tmptestertitle,this.listQuery.testerorg=this.tmptesterorg,r(this.listQuery).then(function(e){t.testerList=e.data.list,t.total=e.data.total}).catch(function(e){t.$message.error("搜索失败")}),this.listLoading=!1,this.btnLoading=!1},handleSizeChange:function(t){this.listQuery.size=t,this.listQuery.page=1,this.searchBypageing()},handleCurrentChange:function(t){this.listQuery.page=t,this.searchBypageing()},getIndex:function(t){return(this.listQuery.page-1)*this.listQuery.size+t+1},showAddtesterDialog:function(){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmptester.id="",this.tmptester.testername="",this.tmptester.testertitle="",this.tmptester.testerorg="",this.tmptester.testermemo=""},addtester:function(){var t=this;this.$refs.tmptester.validate(function(e){var s;e&&t.isUniqueDetail(t.tmptester)&&(t.btnLoading=!0,(s=t.tmptester,Object(i.a)({url:"/tester",method:"post",data:s})).then(function(){t.$message.success("添加成功"),t.gettesterList(),t.dialogFormVisible=!1,t.btnLoading=!1}).catch(function(e){t.$message.error("添加失败"),t.btnLoading=!1}))})},showUpdatetesterDialog:function(t){this.dialogFormVisible=!0,this.dialogStatus="update",this.tmptester.id=this.testerList[t].id,this.tmptester.testername=this.testerList[t].testername,this.tmptester.testertitle=this.testerList[t].testertitle,this.tmptester.testerorg=this.testerList[t].testerorg,this.tmptester.testermemo=this.testerList[t].testermemo},updatetester:function(){var t=this;this.$refs.tmptester.validate(function(e){var s;e&&t.isUniqueDetail(t.tmptester)&&(s=t.tmptester,Object(i.a)({url:"/tester/detail",method:"put",data:s})).then(function(){t.$message.success("更新成功"),t.gettesterList(),t.dialogFormVisible=!1}).catch(function(e){t.$message.error("更新失败")})})},removetester:function(t){var e=this;this.$confirm("删除该测试人员？","警告",{confirmButtonText:"是",cancelButtonText:"否",type:"warning"}).then(function(){var s,r=e.testerList[t].id;(s=r,Object(i.a)({url:"/tester/"+s,method:"delete"})).then(function(){e.$message.success("删除成功"),e.gettesterList()})}).catch(function(){e.$message.info("已取消删除")})},isUniqueDetail:function(t){for(var e=0;e<this.testerList.length;e++)if(this.testerList[e].id!==t.id&&this.testerList[e].testername===t.testername)return this.$message.error("测试人员名已存在"),!1;return!0}}},l={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"app-container"},[s("div",{staticClass:"filter-container"},[s("el-form",{attrs:{inline:!0}},[s("el-form-item",[t.hasPermission("tester:list")?s("el-button",{attrs:{type:"success",size:"mini",icon:"el-icon-refresh"},nativeOn:{click:function(e){return e.preventDefault(),t.gettesterList(e)}}},[t._v("刷新\n        ")]):t._e(),t._v(" "),t.hasPermission("tester:add")?s("el-button",{attrs:{type:"primary",size:"mini",icon:"el-icon-plus"},nativeOn:{click:function(e){return e.preventDefault(),t.showAddtesterDialog(e)}}},[t._v("添加测试人员\n        ")]):t._e()],1),t._v(" "),t.hasPermission("tester:search")?s("span",[s("el-form-item",[s("el-input",{attrs:{placeholder:"测试人员名"},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.searchBy(e)}},model:{value:t.search.testername,callback:function(e){t.$set(t.search,"testername",e)},expression:"search.testername"}})],1),t._v(" "),s("el-form-item",[s("el-select",{attrs:{placeholder:"职位"},model:{value:t.search.testertitle,callback:function(e){t.$set(t.search,"testertitle",e)},expression:"search.testertitle"}},[s("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),t._l(t.testertitleList,function(t,e){return s("div",{key:e},[s("el-option",{attrs:{label:t.dicitemname,value:t.dicitemname}})],1)})],2)],1),t._v(" "),s("el-form-item",[s("el-select",{attrs:{placeholder:"组织"},model:{value:t.search.testerorg,callback:function(e){t.$set(t.search,"testerorg",e)},expression:"search.testerorg"}},[s("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),t._l(t.testerorgList,function(t,e){return s("div",{key:e},[s("el-option",{attrs:{label:t.dicitemname,value:t.dicitemname}})],1)})],2)],1),t._v(" "),s("el-form-item",[s("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()],1)],1),t._v(" "),s("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],attrs:{data:t.testerList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[s("el-table-column",{attrs:{label:"编号",align:"center",width:"60"},scopedSlots:t._u([{key:"default",fn:function(e){return[s("span",{domProps:{textContent:t._s(t.getIndex(e.$index))}})]}}])}),t._v(" "),s("el-table-column",{attrs:{label:"测试人员名",align:"center",prop:"testername",width:"120"}}),t._v(" "),s("el-table-column",{attrs:{label:"职务",align:"center",prop:"testertitle",width:"100"}}),t._v(" "),s("el-table-column",{attrs:{label:"所属组织",align:"center",prop:"testerorg",width:"100"}}),t._v(" "),s("el-table-column",{attrs:{label:"备注",align:"center",prop:"testermemo",width:"100"}}),t._v(" "),s("el-table-column",{attrs:{label:"创建时间",align:"center",prop:"createTime",width:"160"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(t._s(t.unix2CurrentTime(e.row.createTime)))]}}])}),t._v(" "),s("el-table-column",{attrs:{label:"最后修改时间",align:"center",prop:"lastmodifyTime",width:"160"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(t._s(t.unix2CurrentTime(e.row.lastmodifyTime))+"\n      ")]}}])}),t._v(" "),t.hasPermission("tester:update")||t.hasPermission("tester:delete")?s("el-table-column",{attrs:{label:"管理",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[t.hasPermission("tester:update")&&e.row.id!==t.id?s("el-button",{attrs:{type:"warning",size:"mini"},nativeOn:{click:function(s){return s.preventDefault(),t.showUpdatetesterDialog(e.$index)}}},[t._v("修改\n        ")]):t._e(),t._v(" "),t.hasPermission("tester:delete")&&e.row.id!==t.id?s("el-button",{attrs:{type:"danger",size:"mini"},nativeOn:{click:function(s){return s.preventDefault(),t.removetester(e.$index)}}},[t._v("删除\n        ")]):t._e()]}}],null,!1,3316146950)}):t._e()],1),t._v(" "),s("el-pagination",{attrs:{"current-page":t.listQuery.page,"page-size":t.listQuery.size,total:t.total,"page-sizes":[10,20,30,40],layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}}),t._v(" "),s("el-dialog",{attrs:{title:t.textMap[t.dialogStatus],visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[s("el-form",{ref:"tmptester",staticClass:"small-space",staticStyle:{width:"300px","margin-left":"50px"},attrs:{"status-icon":"","label-position":"left","label-width":"100px",model:t.tmptester}},[s("el-form-item",{attrs:{label:"测试人员名",prop:"testername",required:""}},[s("el-input",{attrs:{type:"text","prefix-icon":"el-icon-edit","auto-complete":"off"},model:{value:t.tmptester.testername,callback:function(e){t.$set(t.tmptester,"testername",e)},expression:"tmptester.testername"}})],1),t._v(" "),s("el-form-item",{attrs:{label:"职位",prop:"testertitle",required:""}},[s("el-select",{attrs:{placeholder:"职位"},model:{value:t.tmptester.testertitle,callback:function(e){t.$set(t.tmptester,"testertitle",e)},expression:"tmptester.testertitle"}},[s("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.testertitleList,function(t,e){return s("div",{key:e},[s("el-option",{attrs:{label:t.dicitemname,value:t.dicitemname,required:""}})],1)})],2)],1),t._v(" "),s("el-form-item",{attrs:{label:"组织",prop:"testerorg",required:""}},[s("el-select",{attrs:{placeholder:"组织"},model:{value:t.tmptester.testerorg,callback:function(e){t.$set(t.tmptester,"testerorg",e)},expression:"tmptester.testerorg"}},[s("el-option",{staticStyle:{display:"none"},attrs:{label:"请选择",value:"''"}}),t._v(" "),t._l(t.testerorgList,function(t,e){return s("div",{key:e},[s("el-option",{attrs:{label:t.dicitemname,value:t.dicitemname,required:""}})],1)})],2)],1),t._v(" "),s("el-form-item",{attrs:{label:"备注",prop:"testermemo",required:""}},[s("el-input",{attrs:{type:"text","prefix-icon":"el-icon-message","auto-complete":"off"},model:{value:t.tmptester.testermemo,callback:function(e){t.$set(t.tmptester,"testermemo",e)},expression:"tmptester.testermemo"}})],1)],1),t._v(" "),s("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{nativeOn:{click:function(e){e.preventDefault(),t.dialogFormVisible=!1}}},[t._v("取消")]),t._v(" "),"add"===t.dialogStatus?s("el-button",{attrs:{type:"danger"},nativeOn:{click:function(e){return e.preventDefault(),t.$refs.tmptester.resetFields()}}},[t._v("重置\n      ")]):t._e(),t._v(" "),"add"===t.dialogStatus?s("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.addtester(e)}}},[t._v("添加\n      ")]):t._e(),t._v(" "),"update"===t.dialogStatus?s("el-button",{attrs:{type:"success",loading:t.btnLoading},nativeOn:{click:function(e){return e.preventDefault(),t.updatetester(e)}}},[t._v("修改\n      ")]):t._e()],1)],1)],1)},staticRenderFns:[]},o=s("VU/8")(n,l,!1,null,null,null);e.default=o.exports}});
//# sourceMappingURL=64.1c3422fe35916cfc0c10.js.map