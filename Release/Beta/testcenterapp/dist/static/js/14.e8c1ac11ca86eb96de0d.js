webpackJsonp([14],{GAVG:function(e,n){},"T+/8":function(e,n,t){"use strict";Object.defineProperty(n,"__esModule",{value:!0});var o=t("E4LH"),a={name:"login",data:function(){return{loading:!1,loginForm:{nameOrEmail:"",password:""},loginRules:{nameOrEmail:[{required:!0,trigger:"blur",validator:function(e,n,t){n.length<3?t(new Error("账户名长度必须在3或以上")):t()}}],password:[{required:!0,trigger:"blur",validator:function(e,n,t){n.length<6?t(new Error("密码长度必须在6或以上")):t()}}]},passwordType:"password",btnLoading:!1}},methods:{showPwd:function(){this.passwordType="password"===this.passwordType?"":this.passwordType="password"},handleLogin:function(){var e=this;this.$refs.loginForm.validate(function(n){if(n){var t={};Object(o.a)(e.loginForm.nameOrEmail)?t.email=e.loginForm.nameOrEmail:t.name=e.loginForm.nameOrEmail,t.password=e.loginForm.password,e.loading=!0,e.$store.dispatch("Login",t).then(function(){e.loading=!1,e.$router.push({path:"/dashboard"})})}})}}},r={render:function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("div",{staticClass:"login-container"},[t("el-form",{ref:"loginForm",staticClass:"card-box login-form",attrs:{autocomplete:"on",model:e.loginForm,rules:e.loginRules,"status-icon":"","label-position":"left","label-width":"0px"}},[t("h3",{staticClass:"title"},[e._v("AutoMeter")]),e._v(" "),t("el-form-item",{attrs:{prop:"nameOrEmail"}},[t("span",{staticClass:"svg-container svg-container_login"},[t("icon-svg",{attrs:{"icon-class":"name"}})],1),e._v(" "),t("el-input",{attrs:{type:"text",autocomplete:"on",placeholder:"请输入账户名或邮箱"},nativeOn:{keyup:function(n){return!n.type.indexOf("key")&&e._k(n.keyCode,"enter",13,n.key,"Enter")?null:e.handleLogin(n)}},model:{value:e.loginForm.nameOrEmail,callback:function(n){e.$set(e.loginForm,"nameOrEmail",n)},expression:"loginForm.nameOrEmail"}})],1),e._v(" "),t("el-form-item",{attrs:{prop:"password"}},[t("span",{staticClass:"svg-container"},[t("icon-svg",{attrs:{"icon-class":"password"}})],1),e._v(" "),t("el-input",{attrs:{type:e.passwordType,autocomplete:"on",placeholder:"请输入密码"},nativeOn:{keyup:function(n){return!n.type.indexOf("key")&&e._k(n.keyCode,"enter",13,n.key,"Enter")?null:e.handleLogin(n)}},model:{value:e.loginForm.password,callback:function(n){e.$set(e.loginForm,"password",n)},expression:"loginForm.password"}}),e._v(" "),t("span",{staticClass:"show-pwd",nativeOn:{click:function(n){return n.preventDefault(),e.showPwd(n)}}},[t("icon-svg",{attrs:{"icon-class":"eye"}})],1)],1),e._v(" "),t("el-form-item",[t("el-button",{staticStyle:{width:"100%"},attrs:{type:"primary",loading:e.btnLoading},nativeOn:{click:function(n){return n.preventDefault(),e.handleLogin(n)}}},[e._v("登录")])],1)],1)],1)},staticRenderFns:[]};var i=t("VU/8")(a,r,!1,function(e){t("GAVG")},null,null);n.default=i.exports}});
//# sourceMappingURL=14.e8c1ac11ca86eb96de0d.js.map