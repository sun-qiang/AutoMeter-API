webpackJsonp([23],{gWT0:function(t,e){},"xLw/":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("fqUa"),s=a("vuyV"),n=a("W5b6"),l={filters:{statusFilter:function(t){return{published:"success",draft:"gray",deleted:"danger"}[t]}},data:function(){return{tmptestplanname:"",tmptestplanid:null,tmpbatchname:null,apireportstaticsList:[],apiList:[],planbatchList:[],execplanList:[],deployunitList:[],listLoading:!1,dicvisitypeQuery:{page:1,size:30,diccode:"httpvisittype"},total:0,dialogStatus:"add",dialogFormVisible:!1,textMap:{updateRole:"修改apireportstatics",update:"修改apireportstatics",add:"添加apireportstatics"},btnLoading:!1,tmpapireportstatics:{executeplanid:"",id:"",deployunitid:"",deployunitname:"",batchname:"",apireportstaticsname:"",visittype:"",path:"",memo:""},tmpexecplantype:{usetype:""},searchcase:{page:1,size:10,testplanname:null,testplanid:null,batchname:null}}},created:function(){this.getexecplanList(),this.getdepunitList()},methods:{unix2CurrentTime:a("0xDb").b,testplanselectChanged:function(t){for(var e=this,a=0;a<this.execplanList.length;a++)this.execplanList[a].executeplanname===t&&(this.tmpapireportstatics.executeplanid=this.execplanList[a].id);Object(n.a)(this.tmpapireportstatics).then(function(t){e.planbatchList=t.data}).catch(function(t){e.$message.error("加载执行测试集合列表失败")})},selectChanged:function(t){for(var e=0;e<this.deployunitList.length;e++)this.deployunitList[e].deployunitname===t&&(this.tmpapireportstatics.deployunitid=this.deployunitList[e].id),console.log(this.deployunitList[e].id)},getexecplanList:function(){var t=this;this.tmpexecplantype.usetype="功能",Object(s.e)(this.tmpexecplantype).then(function(e){t.execplanList=e.data}).catch(function(e){t.$message.error("加载测试集合列表失败")})},searchBy:function(){var t=this;this.$refs.searchcase.validate(function(e){e&&(t.btnLoading=!0,t.listLoading=!0,t.searchcase.testplanid=t.tmpapireportstatics.executeplanid,Object(i.f)(t.searchcase).then(function(e){t.apireportstaticsList=e.data.list}).catch(function(e){t.$message.error("搜索失败")}),t.tmptestplanname=t.searchcase.testplanname,t.tmptestplanid=t.searchcase.testplanid,t.tmpbatchname=t.searchcase.batchname,t.listLoading=!1,t.btnLoading=!1)})},getIndex:function(t){return t+1},showcasestaticsdetail:function(t){this.dialogFormVisible=!0,this.dialogStatus="add",this.tmpapireportstatics.id="",this.tmpapireportstatics.deployunitid="",this.tmpapireportstatics.deployunitname="",this.tmpapireportstatics.apireportstaticsname="",this.tmpapireportstatics.visittype="",this.tmpapireportstatics.path="",this.tmpapireportstatics.memo=""},showcasedetail:function(t){console.log(this.apireportstaticsList[t].batchname)}}},r={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"app-container"},[a("div",{staticClass:"filter-container"},[a("el-form",{ref:"searchcase",attrs:{inline:!0,model:t.searchcase}},[t.hasPermission("apireportstatics:search")?a("span",[a("el-form-item",{attrs:{label:"测试集合",prop:"testplanname",required:""}},[a("el-select",{attrs:{filterable:"",placeholder:"测试集合"},on:{change:function(e){return t.testplanselectChanged(e)}},model:{value:t.searchcase.testplanname,callback:function(e){t.$set(t.searchcase,"testplanname",e)},expression:"searchcase.testplanname"}},[a("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),t._l(t.execplanList,function(t,e){return a("div",{key:e},[a("el-option",{attrs:{label:t.executeplanname,value:t.executeplanname}})],1)})],2)],1),t._v(" "),a("el-form-item",{attrs:{label:"执行测试集合",prop:"batchname",requied:""}},[a("el-select",{attrs:{filterable:"",placeholder:"执行测试集合"},model:{value:t.searchcase.batchname,callback:function(e){t.$set(t.searchcase,"batchname",e)},expression:"searchcase.batchname"}},[a("el-option",{attrs:{label:"请选择",value:""}}),t._v(" "),t._l(t.planbatchList,function(t,e){return a("div",{key:e},[a("el-option",{attrs:{label:t.batchname,value:t.batchname}})],1)})],2)],1),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",loading:t.btnLoading},on:{click:t.searchBy}},[t._v("查询")])],1)],1):t._e()])],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:t.listLoading,expression:"listLoading",modifiers:{body:!0}}],attrs:{data:t.apireportstaticsList,"element-loading-text":"loading",border:"",fit:"","highlight-current-row":""}},[a("el-table-column",{attrs:{label:"编号",align:"center",width:"50"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("span",{domProps:{textContent:t._s(t.getIndex(e.$index))}})]}}])}),t._v(" "),a("el-table-column",{attrs:{label:"测试集合",align:"center",prop:"planname",width:"200"}}),t._v(" "),a("el-table-column",{attrs:{label:"执行测试集合",align:"center",prop:"batchname",width:"200"}}),t._v(" "),a("el-table-column",{attrs:{label:"用例数",align:"center",prop:"testcasenums",width:"200"}}),t._v(" "),a("el-table-column",{attrs:{label:"成功数",align:"center",prop:"passnums",width:"200"}}),t._v(" "),a("el-table-column",{attrs:{label:"失败数",align:"center",prop:"failnums",width:"200"}}),t._v(" "),a("el-table-column",{attrs:{label:"消耗时间(ms)",align:"center",prop:"costtimes",width:"200"}})],1)],1)},staticRenderFns:[]};var c=a("VU/8")(l,r,!1,function(t){a("gWT0")},null,null);e.default=c.exports}});
//# sourceMappingURL=23.b8def05d33b6ad4d334e.js.map