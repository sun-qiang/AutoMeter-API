webpackJsonp([10,20],{H12d:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("4aJU"),r=n("Ounb"),s=n("u2wq"),o=n("2f+2"),l={components:{mycreateinfo:a.default,RunTable:r.default,myrecentfunctioninfo:s.default,myrecentperformanceinfo:o.default},data:function(){return{}},methods:{}},i={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"dashboard-editor-container"},[n("el-row",{attrs:{gutter:1}},[n("div",[n("label",[t._v("我创建的:")])]),t._v(" "),n("mycreateinfo")],1),t._v(" "),n("el-row",{attrs:{gutter:1}},[n("div",[n("label",[t._v("我运行的:")])]),t._v(" "),n("el-col",{staticStyle:{"padding-right":"8px","margin-bottom":"5px"},attrs:{xs:{span:24},sm:{span:24},md:{span:24},lg:{span:100},xl:{span:12}}},[n("RunTable")],1)],1),t._v(" "),n("el-row",{attrs:{gutter:1}},[n("div",[n("label",[t._v("最近的功能测试:")])]),t._v(" "),n("el-col",{staticStyle:{"padding-right":"8px","margin-bottom":"5px"},attrs:{xs:{span:24},sm:{span:24},md:{span:24},lg:{span:100},xl:{span:12}}},[n("myrecentfunctioninfo")],1)],1),t._v(" "),n("el-row",{attrs:{gutter:1}},[n("div",[n("label",[t._v("最近的性能测试:")])]),t._v(" "),n("el-col",{staticStyle:{"padding-right":"8px","margin-bottom":"5px"},attrs:{xs:{span:24},sm:{span:24},md:{span:24},lg:{span:100},xl:{span:12}}},[n("myrecentperformanceinfo")],1)],1)],1)},staticRenderFns:[]};var d=n("VU/8")(l,i,!1,function(t){n("Hf+H")},"data-v-29822db2",null);e.default=d.exports},"Hf+H":function(t,e){},"x+yh":function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("Dd8w"),r=n.n(a),s=n("NYxO"),o={components:{adminDashboard:n("H12d").default},data:function(){return{currentRole:"adminDashboard"}},computed:r()({},Object(s.b)(["roles"])),created:function(){this.roles.includes("admin")||(this.currentRole="editorDashboard")}},l={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",{staticClass:"dashboard-container"},[e(this.currentRole,{tag:"component"})],1)},staticRenderFns:[]},i=n("VU/8")(o,l,!1,null,null,null);e.default=i.exports}});
//# sourceMappingURL=10.a9101774d8894243ea05.js.map