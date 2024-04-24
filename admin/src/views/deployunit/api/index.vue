<template xmlns="http://www.w3.org/1999/html">
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('api:list')"
            @click.native.prevent="getapiList"
          >刷新
          </el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('api:add')"
            @click.native.prevent="showAddapiDialog"
          >添加API
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('api:add')"
            @click.native.prevent="showCopyapiDialog"
          >复制API
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('api:add')"
            @click.native.prevent="showPostManDialog"
          >导入PostMan
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('api:add')"
            @click.native.prevent="showSwaggerDialog"
          >导入Swagger
          </el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('api:add')"
            @click.native.prevent="removebatchapi"
          >批量删除
          </el-button>
        </el-form-item>

        <span v-if="hasPermission('api:search')">
          <el-form-item  label="API名:">
            <el-input style="width: 130px" v-model="search.apiname" clearable @keyup.enter.native="searchBy" placeholder="API名"></el-input>
          </el-form-item>
          <el-form-item  label="微服务名:">
            <el-input style="width: 130px" v-model="search.deployunitname" clearable @keyup.enter.native="searchBy" placeholder="微服务名"></el-input>
          </el-form-item>
          <el-form-item  label="模块名:">
            <el-input style="width: 130px" v-model="search.modelname" clearable @keyup.enter.native="searchBy" placeholder="模块名"></el-input>
          </el-form-item>
           <el-form-item  label="Url路径:">
            <el-input style="width: 130px" v-model="search.path" clearable @keyup.enter.native="searchBy" placeholder="Url路径"></el-input>
          </el-form-item>
          <el-form-item  label="范围:">
            <el-select v-model="search.nickname" clearable placeholder="范围"  @change="creatorselectChanged($event)">
              <el-option label="我的" value="我的" />
              <el-option label="全部" value="全部" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>

        </span>


      </el-form>
    </div>
    <el-table
      :data="apiList"
      :key="itemKey"
      @selection-change="handleSelectionChange"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="40">
      </el-table-column>
      <el-table-column label="编号" align="center" width="45">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="API" align="center" prop="apiname" width="120"/>
      <el-table-column label="微服务" align="center" prop="deployunitname" width="130"/>
      <el-table-column label="模块" align="center" prop="modelname" width="80"/>
      <el-table-column label="风格" align="center" prop="apistyle" width="80"/>
      <el-table-column label="访问方式" align="center" prop="visittype" width="80"/>
      <el-table-column label="Url路径" align="center" prop="path" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.path }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="请求格式" align="center" prop="requestcontenttype" width="80"/>
      <el-table-column label="用例数" align="center" prop="casecounts" width="60"/>
      <el-table-column :show-overflow-tooltip="true"   label="维护人" align="center" prop="mnickname" width="60"/>
      <el-table-column :show-overflow-tooltip="true"   label="操作人" align="center" prop="creator" width="60"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="240"
                       v-if="hasPermission('api:update')  || hasPermission('api:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('api:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateapiDialog(scope.$index)"
          >修改
          </el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('api:delete') && scope.row.id !== id"
            @click.native.prevent="removeapi(scope.$index)"
          >删除
          </el-button>
          <!--          <el-button-->
          <!--            type="primary"-->
          <!--            size="mini"-->
          <!--            v-if="hasPermission('api:delete') && scope.row.id !== id"-->
          <!--            @click.native.prevent="ShowParamsDialog(scope.$index)"-->
          <!--          >API参数-->
          <!--          </el-button>-->
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('api:delete') && scope.row.id !== id"
            @click.native.prevent="ShowNewParamsDialog(scope.$index)"
          >API参数
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="search.page"
      :page-size="search.size"
      :total="total"
      :page-sizes="[10, 20, 30, 40]"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 450px; margin-left:50px;"
        :model="tmpapi"
        ref="tmpapi"
      >
        <el-form-item label="API名称:" prop="apiname" required>
          <el-input
            placeholder="API名称"
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapi.apiname"
          />
        </el-form-item>
        <el-form-item label="请求方法:" prop="visittype" required>
          <el-select v-model="tmpapi.visittype" placeholder="请求方法" style="width:100%"
                     @change="visitypeselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(vistype, index) in visittypeList" :key="index">
              <el-option :label="vistype.dicitmevalue" :value="vistype.dicitmevalue" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="API风格:" prop="apistyle" required>
          <el-select v-model="tmpapi.apistyle" placeholder="api风格" style="width:100%">
            <el-option label="传统方式" value="传统方式"></el-option>
            <el-option label="Restful" value="Restful"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Url路径:" prop="path" >
          <el-input
            type="text"
            placeholder="不用填写域名或者IP，例如：/user/login"
            maxlength="200"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model.trim="tmpapi.path"
          />
        </el-form-item>
        <el-form-item label="微服务:" prop="deployunitname" required>
          <el-select v-model="tmpapi.deployunitname" filterable clearable placeholder="微服务" style="width:100%"
                     @change="selectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="模块:" prop="modelname" >
          <el-select v-model="tmpapi.modelname" filterable clearable placeholder="模块" style="width:100%"
                     @change="modelselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(model, index) in modelList" :key="index">
              <el-option :label="model.modelname" :value="model.modelname" required/>
            </div>
          </el-select>
        </el-form-item>

        <!--        <el-form-item label="数据提交方式:" prop="requesttype" required >-->
        <!--          <el-select v-model="tmpapi.requesttype" placeholder="数据提交方式" style="width:100%">-->
        <!--            <el-option label="Url传值" value="Url传值"></el-option>-->
        <!--            <el-option label="Body传值" value="Body传值"></el-option>-->
        <!--            <el-option label="Both" value="Both"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->

        <div v-if="requestcontenttypeVisible">
          <el-form-item label="请求数据格式:" prop="requestcontenttype" required>
            <el-select v-model="tmpapi.requestcontenttype" placeholder="请求数据格式" style="width:100%">
              <el-option label="请选择" value="''" style="display: none"/>
              <div v-for="(type, index) in requestcontentList" :key="index">
                <el-option :label="type.dicitmevalue" :value="type.dicitmevalue" required/>
              </div>
            </el-select>
          </el-form-item>
        </div>

        <!--        <el-form-item label="响应格式:" prop="responecontenttype" required>-->
        <!--          <el-select v-model="tmpapi.responecontenttype" placeholder="响应数据格式" style="width:100%">-->
        <!--            <el-option label="请选择" value="''" style="display: none" />-->
        <!--            <div v-for="(type, index) in responecontenttypeList" :key="index">-->
        <!--              <el-option :label="type.dicitmevalue" :value="type.dicitmevalue" required/>-->
        <!--            </div>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->

        <el-form-item label="维护人:" prop="mnickname" required>
          <el-select v-model="tmpapi.mnickname" filterable clearable placeholder="维护人" style="width:100%"
                     @change="mnicknameselectChanged($event)">
            <div v-for="(mnickname, index) in accountList" :key="index">
              <el-option :label="mnickname.nickname" :value="mnickname.nickname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="备注:" prop="memo">
          <el-input
            type="text"
            maxlength="100"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpapi.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapi'].resetFields()"
        >重置
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapi"
        >添加
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapi"
        >修改
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="CopyApi" :visible.sync="CopydialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpcopyapi"
        ref="tmpcopyapi"
      >
        <el-form-item label="源微服务" prop="sourcedeployunitname" required>
          <el-select v-model="tmpcopyapi.sourcedeployunitname" placeholder="微服务" style="width:100%"
                     @change="CopyAPISourceDeployselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API来源名" prop="sourceapiname" required>
          <el-select v-model="tmpcopyapi.sourceapiname" placeholder="api" style="width:100%"
                     @change="CopySourceAPIChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(testapi, index) in sourceapiList" :key="index">
              <el-option :label="testapi.apiname" :value="testapi.apiname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="目标微服务" prop="objectdeployunitname" required>
          <el-select v-model="tmpcopyapi.objectdeployunitname" placeholder="微服务" style="width:100%"
                     @change="CopyObjectAPIDeployUnitChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="目标API名" prop="newapiname" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpcopyapi.newapiname"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="CopydialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="copyapi"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title='API老参数' :visible.sync="ParamsdialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('apiparams:add')"
              @click.native.prevent="showAddapiparamsDialog"
            >添加API参数
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="apiparamsList"
        :key="itemKey"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="paramgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="API" align="center" prop="apiname" width="120"/>
        <el-table-column label="属性类型" align="center" prop="propertytype" width="80"/>
        <el-table-column label="提交类型" align="center" prop="bodytype" width="80"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="130"/>
        <el-table-column label="参数名" align="center" prop="keynamebak" width="100">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>{{ scope.row.keynamebak }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">...</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('apiparams:update')  || hasPermission('apiparams:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('apiparams:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateapiparamsDialog(scope.$index)"
            >修改
            </el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('apiparams:delete') && scope.row.id !== id"
              @click.native.prevent="removeapiparams(scope.$index)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog  title='API参数' width="900px" :visible.sync="NewParamsdialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <template>
            <el-tabs v-model="activeName" @tab-click="tabclick" type="card" ref="tabs">
              <el-tab-pane label="Header" name="zero">
                <template>
                  <el-table :data="Headertabledatas" border @selection-change="handleSelectionChange">
                    <el-table-column label="参数" prop="keyname" align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="参数名" v-model="scope.row.keyname"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="默认值" prop="keydefaultvalue" align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="默认值" v-model="scope.row.keydefaultvalue"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="300">
                      <template slot-scope="scope">
<!--                        <el-button type="primary" size="mini" @click="UseParams(scope.row,scope.$index)">使用变量-->
<!--                        </el-button>-->
                        <el-button type="primary" size="mini" @click="copeHeader(scope.row,scope.$index)">新增
                        </el-button>
                        <el-button type="primary" size="mini" @click="delectHeader(scope.$index)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-tab-pane>
              <el-tab-pane label="Params" name="first">
                <template>
                  <el-table :data="Paramstabledatas" border @selection-change="handleSelectionChange">
                    <el-table-column label="参数" prop="keyname" align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="参数名" v-model="scope.row.keyname"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="值类型" prop="keytype" align="center">
                      <template slot-scope="scope">
                        <el-select v-model="scope.row.keytype" style="width:100%" placeholder="值类型">
                          <el-option label="Number" value="Number"></el-option>
                          <el-option label="String" value="String"></el-option>
                          <el-option label="Array" value="Array"></el-option>
                        </el-select>
                      </template>
                    </el-table-column>
                    <el-table-column label="默认值" prop="keydefaultvalue" align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="默认值" v-model="scope.row.keydefaultvalue"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" width="250">
                      <template slot-scope="scope">
<!--                        <el-button type="primary" size="mini" @click="UseParams(scope.row,scope.$index)">使用变量-->
<!--                        </el-button>-->
                        <el-button type="primary" size="mini" @click="copeParam(scope.row,scope.$index)">新增
                        </el-button>
                        <el-button type="primary" size="mini" @click="delectParam(scope.$index)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-tab-pane>
              <el-tab-pane label="Body" name="second">
                <template>
                  <div v-if="BodyParamDataVisible" class="components-container">
                    <el-table :data="Bodytabledatas" border>
                      <el-table-column label="参数" prop="keyname" align="center">
                        <template slot-scope="scope">
                          <el-input size="mini" placeholder="参数名" v-model="scope.row.keyname"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="值类型" prop="keytype" align="center">
                        <template slot-scope="scope">
                          <el-select v-model="scope.row.keytype" style="width:100%" placeholder="值类型">
                            <el-option label="Number" value="Number"></el-option>
                            <el-option label="String" value="String"></el-option>
                            <el-option label="Array" value="Array"></el-option>
                          </el-select>
                        </template>
                      </el-table-column>
                      <el-table-column label="默认值" prop="keydefaultvalue" align="center">
                        <template slot-scope="scope">
                          <el-input size="mini" placeholder="默认值" v-model="scope.row.keydefaultvalue"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" align="center" width="220">
                        <template slot-scope="scope">
<!--                          <el-button type="primary" size="mini" @click="UseParams(scope.row,scope.$index)">使用变量-->
<!--                          </el-button>-->
                          <el-button type="primary" size="mini" @click="copeBodyParam(scope.row,scope.$index)">新增
                          </el-button>
                          <el-button type="primary" size="mini" @click="delectBodyParam(scope.$index)">删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div >
                  <div v-if="BodyDataVisible" >
                    <el-form
                      :model="tmpapiparams"
                      ref="tmpapiparams">
                      <el-form-item  prop="keyname">
<!--                        <div class="editor-container">-->
<!--                          <json-editor ref="jsonEditor" v-model="tmpapiparams.keyname" />-->
<!--                        </div>-->

<!--                        <el-button type="primary" size="mini" @click="UseParams">使用变量-->
<!--                        </el-button>-->
                        <el-input
                            type="textarea"
                          style="width: 650px;height: 400px;color: #0000FF"
                          rows="20" cols="70"
                          prefix-icon="el-icon-message"
                          auto-complete="off"
                          v-model="tmpapiparams.keyname"
                          :placeholder="paramsplaceholder"
                        />
                      </el-form-item>
                    </el-form>
                  </div>
                </template>
              </el-tab-pane>
              <el-tab-pane label="变量使用" name="third">
                <uservariables></uservariables>
              </el-tab-pane>
            </el-tabs>
          </template>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="NewParamsdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addapiallparams"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="paramstextMap[ParamsdialogStatus]" :visible.sync="ModifydialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpapiparams"
        ref="tmpapiparams"
      >
        <el-form-item label="参数类型" prop="propertytype" required>
          <el-select v-model="tmpapiparams.propertytype" placeholder="参数类型" style="width:100%"
                     @change="paratypeselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(para, index) in paramlist" :key="index">
              <el-option :label="para.value" :value="para.value"/>
            </div>
          </el-select>
        </el-form-item>

        <div v-if="BodyVisible">
          <el-form-item label="提交类型" prop="bodytype" required>
            <el-select v-model="tmpapiparams.bodytype" style="width:100%" placeholder="提交类型"
                       @change="submittypeselectChanged($event)">
              <el-option label="表单" value="表单"></el-option>
              <el-option label="Json" value="Json"></el-option>
              <el-option label="Xml" value="Xml"></el-option>
            </el-select>
          </el-form-item>
        </div>


        <el-form-item label="参数名：" prop="keyname" required>
          <el-input
            type="textarea"
            rows="20" cols="50"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model.trim="tmpapiparams.keyname"
            :placeholder="paramsplaceholder"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="ModifydialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="ParamsdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapiparams"
        >添加
        </el-button>
        <el-button
          type="success"
          v-if="ParamsdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapiparams"
        >修改
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title='导入PostMan(V2.1)' :visible.sync="dialogAddFile">
      <el-form
        :model="uploadData"
        ref="uploadData"
      >
        <el-form-item label="导入到微服务:" prop="deptname" required>
          <el-select v-model="uploadData.deptname" placeholder="微服务" style="width:100%"
                     @change="uploadselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API风格:" prop="apistyle" required>
          <el-select v-model="uploadData.apistyle" placeholder="api风格" style="width:100%">
            <el-option label="传统方式" value="传统方式"></el-option>
            <el-option label="Restful" value="restful"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="维护人:" prop="mnickname" required>
          <el-select v-model="uploadData.mnickname" filterable clearable placeholder="维护人" style="width:100%"
                     @change="mnicknameselectChanged($event)">
            <div v-for="(mnickname, index) in accountList" :key="index">
              <el-option :label="mnickname.nickname" :value="mnickname.nickname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-upload
            style="display: inline"
            :auto-upload="false"
            :on-change="handleChange"
            :on-remove="removehandleChange"
            :file-list="this.fileList"
            accept=".json"
            limit="1"
            action="#">
            <el-button  type="success">选择文件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button  type="success" @click="upload">点击上传</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog title='导入Swagger(V2.0)' :visible.sync="SwaggerdialogAddFile">
      <el-form
        :model="uploadData"
        ref="uploadData"
      >
        <el-form-item label="导入到微服务:" prop="deptname" required>
          <el-select v-model="uploadData.deptname" placeholder="微服务" style="width:100%"
                     @change="uploadselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none"/>
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API风格:" prop="apistyle" required>
          <el-select v-model="uploadData.apistyle" placeholder="api风格" style="width:100%">
            <el-option label="传统方式" value="传统方式"></el-option>
            <el-option label="Restful" value="restful"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="维护人:" prop="mnickname" required>
          <el-select v-model="uploadData.mnickname" filterable clearable placeholder="维护人" style="width:100%"
                     @change="mnicknameselectChanged($event)">
            <div v-for="(mnickname, index) in accountList" :key="index">
              <el-option :label="mnickname.nickname" :value="mnickname.nickname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-upload
            style="display: inline"
            :auto-upload="false"
            :on-change="SWhandleChange"
            :on-remove="removehandleChange"
            :file-list="this.SwfileList"
            accept=".json"
            limit="1"
            action="#">
            <el-button  type="success">选择文件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button  type="success" @click="uploadsw">点击上传</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import axios from 'axios'
import { searchallaccount as searchallaccount } from '@/api/account'
import { search, addapi, updateapi, removeapi, getapisbydeployunitid, copyapi, removebatchapi } from '@/api/deployunit/api'
import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
import { getdatabydiccodeList as getdatabydiccodeList } from '@/api/system/dictionary'
import {
  addapiparams,
  getBodyNoFormbyapiid,
  addapiallparams,
  searchbyidandproperty,
  updateapiparams,
  removeapiparams,
  searchparamsbyapiid as searchparamsbyapiid
} from '@/api/deployunit/apiparams'
import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'
import { search as searchtestvariables } from '@/api/testvariables/testvariables'
import { unix2CurrentTime } from '@/utils'
import { mapGetters } from 'vuex'
import store from '@/store'
import JsonEditor from '@/components/JsonEditor'
import uservariables from '@/components/testvariables'

export default {
  name: 'API接口',
  components: { JsonEditor, uservariables },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      accountList: [],
      id: null,
      fileName: '',
      fileList: [],
      SwfileList: [],
      modelList: [],
      dialogAddFile: false, // 导入Postman对话框显示
      Headertabledatas: [],
      Paramstabledatas: [],
      Bodytabledatas: [],
      multipleSelection: [],
      activeName: 'zero',
      variablesactiveName: 'zero',
      fileurl: window.g.SERVER_URL + '/api/exportpostman',
      swfileurl: window.g.SERVER_URL + '/api/exportswagger',
      paramsplaceholder: '',
      itemKey: null,
      variablesitemKey: null,
      tmpapiname: '',
      tmpdeployunitname: '',
      tmpmodelname: '',
      tmppath: '',
      apiList: [], // api列表
      sourceapiList: [],
      visittypeList: [], // api访问方式列表
      deployunitList: [], // 服务列表
      apiparamsList: [], // apiparams列表
      paramlist: [],
      requestcontentList: [], // 字典表获取api请求数据格式
      responecontenttypeList: [], // 字典表获取api响应数据格式
      VariablesList: [], // 变量列表
      requestcontenttypeVisible: true, // 请求方式是否显示标志
      BodyVisible: false, // 参数类型为Body显示
      BodyDataVisible: false, // 参数类型为Body显示
      BodyParamDataVisible: false, // 参数类型为Body显示
      SearchParamsdialogFormVisible: false, // 使用变量
      listLoading: false, // 数据加载等待动画
      dicvisitypeQuery: {
        page: 1, // 页码
        size: 30, // 每页数量
        diccode: 'httpvisittype' // 获取字典表入参
      },
      dicrequestypeQuery: {
        page: 1, // 页码
        size: 30, // 每页数量
        diccode: 'requestcontentype' // 获取字典表入参
      },
      dicresponetypeQuery: {
        page: 1, // 页码
        size: 30, // 每页数量
        diccode: 'responecontenttype' // 获取字典表入参
      },
      total: 0, // 数据总数
      variablestotal: 0, // 数据总数
      listQuery: {
        page: 1, // 页码
        size: 20, // 每页数量
        listLoading: true,
        apiname: '',
        deployunitname: ''
      },
      dialogStatus: 'add',
      ParamsdialogStatus: 'add',
      CopyApi: '复制API',
      CopydialogFormVisible: false,
      dialogFormVisible: false,
      ParamsdialogFormVisible: false,
      NewParamsdialogFormVisible: false,
      ModifydialogFormVisible: false,
      SwaggerdialogAddFile: false,
      textMap: {
        updateRole: '修改API',
        update: '修改API',
        add: '添加API'
      },
      paramstextMap: {
        updateRole: '修改API参数',
        update: '修改API参数',
        add: '添加API参数'
      },
      btnLoading: false, // 按钮等待动画
      tmpapi: {
        id: '',
        deployunitid: '',
        deployunitname: '',
        modelid: '',
        modelname: '',
        apiname: '',
        visittype: '',
        requesttype: '',
        apistyle: '',
        path: '',
        requestcontenttype: '',
        responecontenttype: '',
        memo: '',
        creator: '',
        casecounts: 0,
        projectid: '',
        mnickname: '',
        creatorid: '',
        mid: ''
      },
      tmpcopyapi: {
        sourceapiid: '',
        sourceapiname: '',
        sourcedeployunitid: '',
        sourcedeployunitname: '',
        objectdeployunitid: '',
        objectdeployunitname: '',
        newapiname: ''
      },
      tmpapiparams: {
        id: '',
        apiid: '',
        deployunitid: '',
        apiname: '',
        deployunitname: '',
        propertytype: '',
        keyname: '',
        keytype: '',
        keydefaultvalue: '',
        creator: ''
      },
      tmpqueryparams: {
        apiid: '',
        propertytype: ''
      },
      tmpqueryheader: {
        apiid: '',
        propertytype: ''
      },
      tmpquerybody: {
        apiid: '',
        propertytype: ''
      },
      search: {
        page: 1,
        size: 10,
        apiname: null,
        deployunitname: null,
        modelname: null,
        path: null,
        projectid: '',
        mid: ''
      },
      searchvaraibles: {
        page: 1,
        size: 10,
        varaiblestype: '',
        variablesname: ''
      },
      tmpmodelquery: {
        page: 1,
        size: 100,
        deployunitid: ''
      },
      uploadData: {
        deployid: '',
        deptname: '',
        apistyle: '',
        mid: '',
        mnickname: ''
      }
    }
  },
  computed: {
    ...mapGetters(['name', 'nickname', 'sidebar', 'projectlist', 'projectid', 'accountId'])
  },
  created() {
    this.getaccountLists()
    this.search.projectid = window.localStorage.getItem('pid')
    this.Headertabledatas = [
      { id: '', keyname: '', keytype: '', keydefaultvalue: '', propertytype: 'Header', creator: '' }
    ]
    this.Paramstabledatas = [
      { id: '', keyname: '', keytype: '', keydefaultvalue: '', propertytype: 'Params', creator: '' }
    ]
    this.Bodytabledatas = [
      { id: '', keyname: '', keytype: '', keydefaultvalue: '', propertytype: 'Body', creator: '' }
    ]
    this.Paramstabledatas.map(i => {
      i.show = false
      return i
    })
    this.getapiList()
    this.getvisittypeList()
    this.getrequestcontenttypeList()
    this.getresponecontenttypeList()
    this.getdepunitLists()
    // this.editAll()
    // this.editParamAll()
  },

  activated() {
    this.getapiList()
    this.getdepunitLists()
  },
  methods: {
    creatorselectChanged(e) {
      if (e === '全部') {
        this.search.mid = null
      } else {
        this.search.mid = this.accountId
      }
    },
    tabclick(tab, event) {
      console.log(tab, event)
    },
    UseParams(val, index) {
      console.log(val)
      console.log(index)
      this.SearchParamsdialogFormVisible = true
    },
    // 单个复制
    copeHeader(val, index) {
      var newrow = {
        id: '',
        keyname: '',
        keytype: '',
        keydefaultvalue: '',
        propertytype: 'Header',
        apiid: '',
        apiname: '',
        deployunitid: '',
        deployunitname: '',
        creatorid: this.accountId,
        creator: this.nickname
      }
      console.log(newrow)
      this.Headertabledatas.splice(index + 1, 0, JSON.parse(JSON.stringify(newrow)))
    },
    copeParam(val, index) {
      var newrow = {
        id: '',
        keyname: '',
        keytype: '',
        keydefaultvalue: '',
        propertytype: 'Params',
        apiid: '',
        apiname: '',
        deployunitid: '',
        deployunitname: '',
        creatorid: this.accountId,
        creator: this.nickname
      }
      console.log(newrow)
      this.Paramstabledatas.splice(index + 1, 0, JSON.parse(JSON.stringify(newrow)))
    },
    copeBodyParam(val, index) {
      var newrow = {
        id: '',
        keyname: '',
        keytype: '',
        keydefaultvalue: '',
        propertytype: 'Body',
        apiid: '',
        apiname: '',
        deployunitid: '',
        deployunitname: '',
        creatorid: this.accountId,
        creator: this.nickname
      }
      console.log(newrow)
      this.Bodytabledatas.splice(index + 1, 0, JSON.parse(JSON.stringify(newrow)))
    },
    // 单个删除Header
    delectHeader(index) {
      // this.tabledatas.splice(index, 1)
      console.log(this.Headertabledatas)
      if (this.Headertabledatas[index].id !== '') {
        // 服务端删除
        this.$confirm('删除参数，用到此参数的所有用例值会跟随一起删除？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.Headertabledatas[index].id
          removeapiparams(id).then(() => {
            this.$message.success('删除成功')
            this.Headertabledatas.splice(index, 1)
            if (this.Headertabledatas.length === 0) {
              var newrow = {
                id: '',
                keyname: '',
                keytype: '',
                keydefaultvalue: '',
                propertytype: 'Header',
                apiid: '',
                apiname: '',
                deployunitid: '',
                deployunitname: '',
                creatorid: this.accountId,
                creator: this.nickname
              }
              this.Headertabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
              console.log(this.Headertabledatas)
            }
            this.$foreceUpdate()
          })
        }).catch(() => {
          this.$message.info('删除取消')
        })
      } else {
        this.Headertabledatas.splice(index, 1)
        console.log(this.Headertabledatas.length)
        if (this.Headertabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Header',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creatorid: this.accountId,
            creator: this.nickname
          }
          this.Headertabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
          console.log(this.Headertabledatas)
        }
        this.$foreceUpdate()
      }
    },
    // 单个删除Params
    delectParam(index) {
      console.log(this.Paramstabledatas)
      if (this.Paramstabledatas[index].id !== '') {
        // 服务端删除
        this.$confirm('删除参数，用到此参数的所有用例值会跟随一起删除？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.Paramstabledatas[index].id
          removeapiparams(id).then(() => {
            this.$message.success('删除成功')
            this.Paramstabledatas.splice(index, 1)
            if (this.Paramstabledatas.length === 0) {
              var newrow = {
                id: '',
                keyname: '',
                keytype: '',
                keydefaultvalue: '',
                propertytype: 'Params',
                apiid: '',
                apiname: '',
                deployunitid: '',
                deployunitname: '',
                creatorid: this.accountId,
                creator: this.nickname
              }
              this.Paramstabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
              console.log(this.Paramstabledatas)
            }
            this.$foreceUpdate()
          })
        }).catch(() => {
          this.$message.info('删除取消')
        })
      } else {
        this.Paramstabledatas.splice(index, 1)
        console.log(this.Paramstabledatas.length)
        if (this.Paramstabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Params',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creatorid: this.accountId,
            creator: this.nickname
          }
          this.Paramstabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
          console.log(this.Paramstabledatas)
        }
        this.$foreceUpdate()
      }
    },
    // 单个删除BodyParams
    delectBodyParam(index) {
      console.log(this.Bodytabledatas)
      if (this.Bodytabledatas[index].id !== '') {
        // 服务端删除
        this.$confirm('删除参数，用到此参数的所有用例值会跟随一起删除？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.Bodytabledatas[index].id
          removeapiparams(id).then(() => {
            this.$message.success('删除成功')
            this.Bodytabledatas.splice(index, 1)
            if (this.Bodytabledatas.length === 0) {
              var newrow = {
                id: '',
                keyname: '',
                keytype: '',
                keydefaultvalue: '',
                propertytype: 'Body',
                apiid: '',
                apiname: '',
                deployunitid: '',
                deployunitname: '',
                creatorid: this.accountId,
                creator: this.nickname
              }
              this.Bodytabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
              console.log(this.Bodytabledatas)
            }
            this.$foreceUpdate()
          })
        }).catch(() => {
          this.$message.info('删除取消')
        })
      } else {
        this.Bodytabledatas.splice(index, 1)
        console.log(this.Bodytabledatas.length)
        if (this.Bodytabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Body',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creatorid: this.accountId,
            creator: this.nickname
          }
          this.Bodytabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
          console.log(this.Bodytabledatas)
        }
        this.$foreceUpdate()
      }
    },
    unix2CurrentTime,
    // submitUpload() {
    //   console.log('aaaaaaaaaaaaaaaaaaa')
    //   console.log('上传' + this.files.name)
    //   if (this.fileName === '') {
    //     this.$message.warning('请选择要上传的文件！')
    //     return false
    //   }
    //   const fileFormData = new FormData()
    //   this.uploadData.deployid = 1
    //   this.uploadData.deployunitname = 'aaaa'
    //   fileFormData.append('deployid', this.uploadData.deployid)
    //   fileFormData.append('deployunitname', this.uploadData.deployunitname)
    //   fileFormData.append('file', this.files, this.fileName)
    //   const requestConfig = {
    //     headers: {
    //       'Authorization': getToken(),
    //       'Content-Type': 'multipart/form-data'
    //     }
    //   }
    //   console.log('aaaaaaaaaaaaaaaaaaa')
    //   console.log(fileFormData)
    //   this.axis.post(this.fileurl, fileFormData, requestConfig).then((res) => {
    //   })
    // },
    // //
    // handleChange(file, fileList) {
    //   this.fileList = fileList
    //   console.log(fileList)
    // },

    searchdeployunitmodel() {
      searchdeployunitmodel(this.tmpmodelquery).then(response => {
        this.modelList = response.data.list
      }).catch(res => {
        this.$message.error('加载服务模块列表失败')
      })
    },

    handleSelectionChange(rows) {
      // console.log(rows)
      this.multipleSelection = rows
      console.log('00000000000000000000000000')
      console.log(this.multipleSelection)
    },

    removebatchapi() {
      this.$confirm('删除所选的API？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        if (this.multipleSelection.length === 0) {
          this.$message.error('请选择需要删除的api')
        } else {
          this.apiremovetList = []
          console.log(this.multipleSelection)
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.apiremovetList.push({
              'id': this.multipleSelection[i].id
            })
          }
          removebatchapi(this.multipleSelection).then(() => {
            this.$message.success('批量删除成功')
            this.getapiList()
          }).catch(res => {
            this.$message.error('批量删除失败')
          })
        }
      }).catch(() => {
        this.$message.info('批量删除异常')
      })
    },

    handleChange(file, fileList) {
      this.fileList = fileList
      console.log(fileList)
    },
    SWhandleChange(file, fileList) {
      this.SwfileList = fileList
      console.log(fileList)
    },
    removehandleChange(file, fileList) {
      this.fileList = []
      console.log(fileList)
    },
    upload() {
      this.$refs.uploadData.validate(valid => {
        if (valid) {
          if (this.fileList.length === 0) {
            this.$message.warning('请选择导入的json文件！')
            return false
          }
          const fd = new FormData()
          fd.append('deployid', this.uploadData.deployid)
          fd.append('deployunitname', this.uploadData.deptname)
          fd.append('apistyle', this.uploadData.apistyle)
          fd.append('creator', this.nickname)
          fd.append('creatorid', this.accountId)
          fd.append('mnickname', this.uploadData.mnickname)
          fd.append('mid', this.uploadData.mid)
          fd.append('projectid', window.localStorage.getItem('pid'))
          this.fileList.forEach(item => {
            fd.append('file', item.raw)
          })
          axios.post(this.fileurl, fd, {
            headers: {
              Authorization: store.getters.token,
              'Content-Type': 'multipart/form-data'
            }
          }).then(res => {
            if (res.data.code === 200) {
              this.dialogAddFile = false
              this.getapiList()
              this.$message.success('上传完成')
            } else {
              this.$message.error(res.data.message)
            }
          })
        }
      })
    },

    uploadsw() {
      this.$refs.uploadData.validate(valid => {
        if (valid) {
          if (this.SwfileList.length === 0) {
            this.$message.warning('请选择导入的json文件！')
            return false
          }
          const fd = new FormData()
          fd.append('deployid', this.uploadData.deployid)
          fd.append('deployunitname', this.uploadData.deptname)
          fd.append('apistyle', this.uploadData.apistyle)
          fd.append('creator', this.nickname)
          fd.append('creatorid', this.accountId)
          fd.append('mnickname', this.uploadData.mnickname)
          fd.append('mid', this.uploadData.mid)
          fd.append('projectid', window.localStorage.getItem('pid'))
          this.SwfileList.forEach(item => {
            fd.append('file', item.raw)
          })
          axios.post(this.swfileurl, fd, {
            headers: {
              Authorization: store.getters.token,
              'Content-Type': 'multipart/form-data'
            }
          }).then(res => {
            if (res.data.code === 200) {
              this.SwaggerdialogAddFile = false
              this.getapiList()
              this.$message.success('上传完成')
            } else {
              this.$message.error(res.data.message)
            }
          })
        }
      })
    },
    /**
     * 服务下拉选择事件获取服务id  e的值为options的选值
     */
    selectChanged(e) {
      this.tmpapi.modelname = ''
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.tmpapi.deployunitid = this.deployunitList[i].id
          this.tmpmodelquery.deployunitid = this.deployunitList[i].id
        }
        console.log(this.deployunitList[i].id)
        this.searchdeployunitmodel()
      }
    },

    mnicknameselectChanged(e) {
      for (let i = 0; i < this.accountList.length; i++) {
        if (this.accountList[i].nickname === e) {
          this.tmpapi.mid = this.accountList[i].id
          this.uploadData.mid = this.accountList[i].id
        }
      }
    },

    modelselectChanged(e) {
      for (let i = 0; i < this.modelList.length; i++) {
        if (this.modelList[i].modelname === e) {
          this.tmpapi.modelid = this.modelList[i].id
        }
      }
    },

    uploadselectChanged(e) {
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.uploadData.deployid = this.deployunitList[i].id
        }
        console.log(this.deployunitList[i].id)
      }
    },
    paratypeselectChanged(e) {
      this.tmpapiparams.keyname = ''
      this.tmpapiparams.bodytype = ''
      if (this.tmpapiparams.propertytype === 'Body') {
        this.BodyVisible = true
        this.paramsplaceholder = '支持json，xml格式，例如：\n{\n' +
          '  "id": "",\n' +
          '  "enviromentname": "sdsds",\n' +
          '  "envtype": "功能",\n' +
          '  "memo": "sadadada",\n' +
          '  "creator": "admin"\n' +
          '}'
      } else {
        this.paramsplaceholder = '多个参数使用英文逗号隔开，例如：id,enviromentname,envtype,memo,creator'
        this.BodyVisible = false
      }
    },
    submittypeselectChanged(e) {
      this.tmpapiparams.keyname = ''
      if (this.tmpapiparams.propertytype === 'Body') {
        this.BodyVisible = true
        if (this.tmpapiparams.bodytype === '表单') {
          this.paramsplaceholder = '多个参数使用英文逗号隔开，例如：id,enviromentname,envtype,memo,creator'
        } else {
          this.paramsplaceholder = '支持json，xml格式，例如：\n{\n' +
            '  "id": "",\n' +
            '  "enviromentname": "sdsds",\n' +
            '  "envtype": "功能",\n' +
            '  "memo": "sadadada",\n' +
            '  "creator": "admin"\n' +
            '}'
        }
      } else {
        this.paramsplaceholder = '多个参数使用英文逗号隔开，例如：id,enviromentname,envtype,memo,creator'
        this.BodyVisible = false
      }
    },
    /**
     * 服务下拉选择事件获取服务id  e的值为options的选值,获取用例
     */
    CopyAPISourceDeployselectChanged(e) {
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.tmpcopyapi.sourcedeployunitid = this.deployunitList[i].id
        }
      }
      getapisbydeployunitid(this.tmpcopyapi).then(response => {
        this.sourceapiList = response.data
      }).catch(res => {
        this.$message.error('根据服务id获取api列表失败')
      })
    },
    /**
     * 源API下拉选择事件获取用例id  e的值为options
     */
    CopySourceAPIChanged(e) {
      for (let i = 0; i < this.sourceapiList.length; i++) {
        if (this.sourceapiList[i].apiname === e) {
          this.tmpcopyapi.sourceapiid = this.sourceapiList[i].id
        }
      }
    },
    /**
     * 目标服务下拉选择事件获取服务id  e的值为options
     */
    CopyObjectAPIDeployUnitChanged(e) {
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.tmpcopyapi.objectdeployunitid = this.deployunitList[i].id
        }
      }
    },
    /**
     * 复制API
     */
    copyapi() {
      this.$refs.tmpcopyapi.validate(valid => {
        if (valid) {
          this.btnLoading = true
          copyapi(this.tmpcopyapi).then(() => {
            this.$message.success('复制成功')
            this.getapiList()
            this.CopydialogFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('复制失败')
            this.btnLoading = false
          })
        }
      })
    },
    /**
     * 访问方式下拉，为get，不显示请求数据格式  e的值为options的选值
     */
    visitypeselectChanged(e) {
      // if (e === 'get') {
      //   this.requestcontenttypeVisible = false
      //   this.tmpapi.requestcontenttype = '/'
      // } else {
      //   this.requestcontenttypeVisible = true
      //   this.tmpapi.requestcontenttype = ''
      // }
    },
    /**
     * 获取api列表
     */
    getapiList() {
      console.log(this.search)
      this.listLoading = true
      this.search.apiname = this.tmpapiname
      this.search.deployunitname = this.tmpdeployunitname
      this.search.modelname = this.tmpmodelname
      this.search.path = this.tmppath
      search(this.search).then(response => {
        this.apiList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载api列表失败')
      })
    },
    /**
     * 获取apiparams列表
     */
    searchparamsbyapiid() {
      searchparamsbyapiid(this.tmpparamsapi).then(response => {
        this.apiparamsList = response.data.list
        // this.total = response.data.total
      }).catch(res => {
        this.$message.error('加载apiparams列表失败')
      })
    },
    /**
     * 获取字典访问方式列表
     */
    getvisittypeList() {
      getdatabydiccodeList(this.dicvisitypeQuery).then(response => {
        this.visittypeList = response.data.list
      }).catch(res => {
        this.$message.error('加载字典访问方式列表失败')
      })
    },
    /**
     * 获取字典请求数据格式列表
     */
    getrequestcontenttypeList() {
      getdatabydiccodeList(this.dicrequestypeQuery).then(response => {
        this.requestcontentList = response.data.list
      }).catch(res => {
        this.$message.error('加载请求数据格式列表失败')
      })
    },
    /**
     * 获取Body非Form表单数据
     */
    getBodyNoFormbyapiid() {
      var bodypara = { apiid: this.tmpapi.id, propertytype: 'Body', keytype: this.tmpapi.requestcontenttype }
      getBodyNoFormbyapiid(bodypara).then(response => {
        this.tmpapiparams = response.data
        console.log(this.tmpapiparams.keyname)
      }).catch(res => {
        this.$message.error('获取Body非Form表单数据失败')
      })
    },
    /**
     * 根据apiid和property获取参数Params
     */
    searchParamsbyidandproperty() {
      searchbyidandproperty(this.tmpqueryparams).then(response => {
        this.Paramstabledatas = response.data
        if (this.Paramstabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Params',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creator: this.nickname,
            creatorid: this.accountId
          }
          this.Paramstabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
        }
      }).catch(res => {
        this.$message.error('加载请求数据格式列表失败')
      })
    },
    /**
     * 根据apiid和property获取参数Header
     */
    searchHeaderbyidandproperty() {
      searchbyidandproperty(this.tmpqueryheader).then(response => {
        this.Headertabledatas = response.data
        if (this.Headertabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Header',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creator: this.nickname,
            creatorid: this.accountId
          }
          this.Headertabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
        }
      }).catch(res => {
        this.$message.error('加载请求数据格式列表失败')
      })
    },
    /**
     * 根据apiid和property获取参数Body
     */
    searchBodybyidandproperty() {
      searchbyidandproperty(this.tmpquerybody).then(response => {
        this.Bodytabledatas = response.data
        if (this.Bodytabledatas.length === 0) {
          var newrow = {
            id: '',
            keyname: '',
            keytype: '',
            keydefaultvalue: '',
            propertytype: 'Body',
            apiid: '',
            apiname: '',
            deployunitid: '',
            deployunitname: '',
            creator: this.nickname,
            creatorid: this.accountId
          }
          this.Bodytabledatas.splice(1, 0, JSON.parse(JSON.stringify(newrow)))
        }
      }).catch(res => {
        this.$message.error('加载请求数据格式列表失败')
      })
    },
    /**
     * 获取字典响应数据格式列表
     */
    getresponecontenttypeList() {
      getdatabydiccodeList(this.dicresponetypeQuery).then(response => {
        this.responecontenttypeList = response.data.list
      }).catch(res => {
        this.$message.error('加载响应数据格式列表失败')
      })
    },

    getaccountLists() {
      this.accountList = null
      searchallaccount().then(response => {
        this.accountList = response.data
      }).catch(res => {
        this.$message.error('加载服务列表失败')
      })
    },
    /**
     * 获取服务列表
     */
    getdepunitLists() {
      this.listLoading = true
      getdepunitLists(this.search).then(response => {
        this.deployunitList = response.data
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载服务列表失败')
      })
    },
    searchBy() {
      this.search.page = 1
      this.listLoading = true
      search(this.search).then(response => {
        this.itemKey = Math.random()
        this.apiList = response.data.list
        this.total = response.data.total
      }).catch(res => {
        this.$message.error('搜索失败')
      })
      this.listLoading = false
      this.btnLoading = false
      this.tmpapiname = this.search.apiname
      this.tmpdeployunitname = this.search.deployunitname
      this.tmpmodelname = this.search.modelname
      this.tmppath = this.search.path
    },

    searchVariablesBy() {
      this.variablesitemKey = Math.random()
      if (this.searchvaraibles.varaiblestype === '接口变量') {
        searchtestvariables(this.searchvaraibles).then(response => {
          this.VariablesList = response.data.list
          this.variablestotal = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
      }
      if (this.searchvaraibles.varaiblestype === '数据库变量') {
        searchtestvariables(this.searchvaraibles).then(response => {
          this.VariablesList = response.data.list
          this.variablestotal = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
      }
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.search.page = 1
      this.search.size = size
      this.getapiList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.search.page = page
      this.getapiList()
    },
    /**
     * 表格序号
     * 可参考自定义表格序号
     * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
     * @param index 数据下标
     * @returns 表格序号
     */
    getIndex(index) {
      return (this.search.page - 1) * this.search.size + index + 1
    },
    /**
     * 表格序号
     * 可参考自定义表格序号
     * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
     * @param index 数据下标
     * @returns 表格序号
     */
    paramgetIndex(index) {
      return (this.searchvaraibles.page - 1) * this.searchvaraibles.size + index + 1
    },
    /**
     * 显示添加apiparams对话框
     */
    showAddapiparamsDialog() {
      // 显示新增对话框
      this.BodyVisible = false
      this.ModifydialogFormVisible = true
      this.ParamsdialogStatus = 'add'
      this.tmpapiparams.id = ''
      this.tmpapiparams.keyname = ''
      this.tmpapiparams.deployunitname = ''
      this.tmpapiparams.apiname = ''
      this.tmpapiparams.propertytype = ''
      this.tmpapiparams.deployunitid = ''
      this.tmpapiparams.bodytype = ''
      this.tmpapiparams.creator = this.nickname
      this.tmpapiparams.creatorid = this.accountId
    },
    /**
     * 显示添加api对话框
     */
    showAddapiDialog() {
      // 显示新增对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'add'
      this.tmpapi.id = ''
      this.tmpapi.deployunitid = ''
      this.tmpapi.deployunitname = ''
      this.tmpapi.modelid = ''
      this.tmpapi.modelname = ''
      this.tmpapi.apiname = ''
      this.tmpapi.visittype = ''
      this.tmpapi.requesttype = ''
      this.tmpapi.apistyle = ''
      this.tmpapi.path = ''
      this.tmpapi.requestcontenttype = ''
      this.tmpapi.responecontenttype = ''
      this.tmpapi.memo = ''
      this.tmpapi.mid = ''
      this.tmpapi.mnickname = ''
      this.tmpapi.creatorid = this.accountId
      this.tmpapi.creator = this.nickname
      this.tmpapi.projectid = window.localStorage.getItem('pid')
      console.log(window.localStorage.getItem('pid'))
    },
    /**
     * 显示添加复制api对话框
     */
    showPostManDialog() {
      // 显示新增对话框
      this.dialogAddFile = true
      this.uploadData.deployid = ''
      this.uploadData.deptname = ''
      this.uploadData.apistyle = ''
      this.uploadData.mnickname = ''
      this.fileList = []
    },

    showSwaggerDialog() {
      // 显示新增对话框
      this.SwaggerdialogAddFile = true
      this.uploadData.deployid = ''
      this.uploadData.deptname = ''
      this.uploadData.apistyle = ''
      this.uploadData.mnickname = ''
      this.SwfileList = []
    },
    /**
     * 显示添加复制api对话框
     */
    showCopyapiDialog() {
      // 显示新增对话框
      this.CopydialogFormVisible = true
      this.tmpcopyapi.sourceapiid = ''
      this.tmpcopyapi.sourceapiname = ''
      this.tmpcopyapi.sourcedeployunitid = ''
      this.tmpcopyapi.sourcedeployunitname = ''
      this.tmpcopyapi.objectdeployunitid = ''
      this.tmpcopyapi.objectdeployunitname = ''
      this.tmpcopyapi.newapiname = ''
    },
    /**
     * 添加api
     */
    addapi() {
      this.$refs.tmpapi.validate(valid => {
        if (valid) {
          this.btnLoading = true
          this.tmpapi.path = this.tmpapi.path.trim()
          addapi(this.tmpapi).then(() => {
            this.$message.success('添加成功')
            this.getapiList()
            this.dialogFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加失败')
            this.btnLoading = false
          })
        }
      })
    },
    /**
     * 添加api所有参数
     */
    addapiallparams(index) {
      for (let i = 0; i < this.Headertabledatas.length; i++) {
        this.Headertabledatas[i].apiid = this.tmpapi.id
        this.Headertabledatas[i].apiname = this.tmpapi.apiname
        this.Headertabledatas[i].deployunitid = this.tmpapi.deployunitid
        this.Headertabledatas[i].deployunitname = this.tmpapi.deployunitname
        this.Headertabledatas[i].creatorid = this.accountId
      }
      addapiallparams(this.Headertabledatas).then(() => {
        this.$message.success('添加Header成功')
      }).catch(res => {
        this.$message.error('添加Header失败')
      })
      for (let i = 0; i < this.Paramstabledatas.length; i++) {
        this.Paramstabledatas[i].apiid = this.tmpapi.id
        this.Paramstabledatas[i].apiname = this.tmpapi.apiname
        this.Paramstabledatas[i].deployunitid = this.tmpapi.deployunitid
        this.Paramstabledatas[i].deployunitname = this.tmpapi.deployunitname
        this.Paramstabledatas[i].creatorid = this.accountId
        this.Paramstabledatas[i].creator = this.nickname
      }
      addapiallparams(this.Paramstabledatas).then(() => {
        this.$message.success('添加Params成功')
      }).catch(res => {
        this.$message.error('添加Params失败')
      })
      console.log(this.tmpapi.requestcontenttype)
      if (this.tmpapi.requestcontenttype === 'Form表单') {
        for (let i = 0; i < this.Bodytabledatas.length; i++) {
          this.Bodytabledatas[i].apiid = this.tmpapi.id
          this.Bodytabledatas[i].apiname = this.tmpapi.apiname
          this.Bodytabledatas[i].deployunitid = this.tmpapi.deployunitid
          this.Bodytabledatas[i].deployunitname = this.tmpapi.deployunitname
          this.Bodytabledatas[i].creatorid = this.accountId
          this.Paramstabledatas[i].creator = this.nickname
        }
        addapiallparams(this.Bodytabledatas).then(() => {
          this.$message.success('添加Body成功')
        }).catch(res => {
          this.$message.error('添加Body失败')
        })
      } else {
        console.log(this.tmpapiparams.keyname)
        if (this.tmpapiparams.keyname === undefined) {
          this.tmpapiparams.keyname = ''
          this.addapiparams()
        } else {
          this.addapiparams()
        }
      }
      this.NewParamsdialogFormVisible = false
    },
    /**
     * 添加apiparams
     */
    addapiparams() {
      this.$refs.tmpapiparams.validate(valid => {
        if (valid) {
          this.tmpapiparams.keydefaultvalue = 'NoForm'
          this.tmpapiparams.propertytype = 'Body'
          this.tmpapiparams.keytype = this.tmpapi.requestcontenttype
          this.tmpapiparams.apiid = this.tmpapi.id
          this.tmpapiparams.apiname = this.tmpapi.apiname
          this.tmpapiparams.deployunitid = this.tmpapi.deployunitid
          this.tmpapiparams.deployunitname = this.tmpapi.deployunitname
          this.tmpapiparams.creatorid = this.accountId
          this.tmpapiparams.creator = this.nickname
          addapiparams(this.tmpapiparams).then(() => {
            this.$message.success('添加Body成功')
          }).catch(res => {
            this.$message.error('添加Body失败')
          })
        }
      })
    },
    /**
     * 显示参数对话框
     */
    ShowParamsDialog(index) {
      this.tmpparamsapi.apiid = this.apiList[index].id
      this.searchparamsbyapiid(this.tmpparamsapi)
      this.ParamsdialogFormVisible = true
      this.tmpapiforparam.apiid = this.apiList[index].id
      this.tmpapiforparam.apiname = this.apiList[index].apiname
      this.tmpapiforparam.deployunitname = this.apiList[index].deployunitname
      this.tmpapiforparam.deployunitid = this.apiList[index].deployunitid
      this.tmpapiforparam.visittype = this.apiList[index].id.visittype
      this.tmpapiforparam.requestcontenttype = this.apiList[index].requestcontenttype
      this.paramlist = []
      if (this.tmpapiparams.visittype === 'get') {
        var getheaddata = { value: 'Header' }
        var getparamsdata = { value: 'Params' }
        this.paramlist.push(getheaddata)
        this.paramlist.push(getparamsdata)
        console.log(this.paramlist)
      } else {
        console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>')
        var headdata = { value: 'Header' }
        var paramsdata = { value: 'Params' }
        var postdata = { value: 'Body' }
        this.paramlist.push(headdata)
        this.paramlist.push(paramsdata)
        this.paramlist.push(postdata)
        console.log(this.paramlist)
      }
    },
    /**
     * 显示参数对话框
     */
    ShowNewParamsDialog(index) {
      this.activeName = 'zero'
      this.tmpapi.id = this.apiList[index].id
      this.tmpapi.deployunitid = this.apiList[index].deployunitid
      this.tmpapi.deployunitname = this.apiList[index].deployunitname
      this.tmpapi.apiname = this.apiList[index].apiname
      this.NewParamsdialogFormVisible = true
      this.tmpqueryparams.apiid = this.apiList[index].id
      this.tmpapi.requestcontenttype = this.apiList[index].requestcontenttype
      if (this.tmpapi.requestcontenttype === 'Form表单') {
        this.BodyParamDataVisible = true
        this.BodyDataVisible = false
      } else {
        this.BodyDataVisible = true
        this.paramsplaceholder = '支持Json，Xml，Text等其他文本格式，例如：\n{\n' +
          '  "id": "",\n' +
          '  "enviromentname": "sdsds",\n' +
          '  "envtype": "功能",\n' +
          '  "memo": "sadadada",\n' +
          '  "creator": "admin"\n' +
          '}'
        this.BodyParamDataVisible = false
      }
      this.tmpqueryparams.propertytype = 'Params'
      this.searchParamsbyidandproperty()
      this.tmpqueryheader.apiid = this.apiList[index].id
      this.tmpqueryheader.propertytype = 'Header'
      this.searchHeaderbyidandproperty()
      this.tmpquerybody.apiid = this.apiList[index].id
      this.tmpquerybody.propertytype = 'Body'
      if (this.tmpapi.requestcontenttype === 'Form表单') {
        this.searchBodybyidandproperty()
      } else {
        this.getBodyNoFormbyapiid()
      }
    },
    /**
     * 显示修改api对话框
     * @param index api下标
     */
    showUpdateapiDialog(index) {
      this.dialogFormVisible = true
      this.dialogStatus = 'update'
      this.tmpapi.id = this.apiList[index].id
      this.tmpapi.deployunitid = this.apiList[index].deployunitid
      this.tmpapi.deployunitname = this.apiList[index].deployunitname
      this.tmpapi.modelid = this.apiList[index].modelid
      this.tmpapi.modelname = this.apiList[index].modelname
      this.tmpapi.apiname = this.apiList[index].apiname
      this.tmpapi.visittype = this.apiList[index].visittype
      this.tmpapi.requesttype = this.apiList[index].requesttype
      this.tmpapi.path = this.apiList[index].path
      this.tmpapi.apistyle = this.apiList[index].apistyle
      this.tmpapi.responecontenttype = this.apiList[index].responecontenttype
      this.tmpapi.memo = this.apiList[index].memo
      this.tmpapi.casecounts = this.apiList[index].casecounts
      this.tmpapi.creator = this.apiList[index].creator
      this.tmpapi.mnickname = this.apiList[index].mnickname
      this.tmpapi.mid = this.apiList[index].mid
      this.tmpapi.creatorid = this.accountId
      this.tmpapi.requestcontenttype = this.apiList[index].requestcontenttype
      this.tmpapi.projectid = window.localStorage.getItem('pid')
      // if (this.tmpapi.visittype === 'GET') {
      //   this.requestcontenttypeVisible = false
      //   this.tmpapi.requestcontenttype = '/'
      // } else {
      //   this.tmpapi.requestcontenttype = this.apiList[index].requestcontenttype
      //   this.requestcontenttypeVisible = true
      // }
    },
    /**
     * 更新api
     */
    updateapi() {
      this.$refs.tmpapi.validate(valid => {
        if (valid) {
          this.tmpapi.path = this.tmpapi.path.trim()
          updateapi(this.tmpapi).then(() => {
            this.$message.success('更新成功')
            this.getapiList()
            this.dialogFormVisible = false
          }).catch(res => {
            this.$message.error('添加失败')
            this.btnLoading = false
          })
        }
      })
    },
    /**
     * 显示修改apiparams对话框
     * @param index apiparams下标
     */
    showUpdateapiparamsDialog(index) {
      this.ModifydialogFormVisible = true
      this.ParamsdialogStatus = 'update'
      this.tmpapiparams.id = this.apiparamsList[index].id
      this.tmpapiparams.keyname = this.apiparamsList[index].keynamebak
      this.tmpapiparams.apiid = this.tmpapiforparam.apiid
      this.tmpapiparams.apiname = this.tmpapiforparam.apiname
      this.tmpapiparams.deployunitid = this.tmpapiforparam.deployunitid
      this.tmpapiparams.deployunitname = this.apiparamsList[index].deployunitname
      this.tmpapiparams.propertytype = this.apiparamsList[index].propertytype
      this.tmpapiparams.bodytype = this.apiparamsList[index].bodytype
      this.tmpapiparams.creator = this.nickname
      if (this.tmpapiparams.propertytype === 'Body') {
        this.BodyVisible = true
        this.paralabel = '参数(支持Json，Xml)'
      } else {
        this.BodyVisible = false
        this.paralabel = '参数(英文逗号隔开)'
      }
    },
    /**
     * 更新apiparams
     */
    updateapiparams() {
      this.$refs.tmpapiparams.validate(valid => {
        if (valid) {
          this.tmpapiparams.keynamebak = this.tmpapiparams.keyname
          updateapiparams(this.tmpapiparams).then(() => {
            this.$message.success('更新成功')
            this.searchparamsbyapiid()
            this.ModifydialogFormVisible = false
          }).catch(res => {
            this.$message.error('添加失败')
            this.btnLoading = false
          })
        }
      })
    },
    /**
     * 删除api
     * @param index api下标
     */
    removeapi(index) {
      this.$confirm('删除该api？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const id = this.apiList[index].id
        removeapi(id).then(() => {
          this.$message.success('删除成功')
          this.getapiList()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * 删除api参数
     * @param index apiparams下标
     */
    removeapiparams(index) {
      this.$confirm('删除参数，用到此参数的所有用例值会跟随一起删除？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const id = this.apiparamsList[index].id
        removeapiparams(id).then(() => {
          this.$message.success('删除成功')
          this.searchparamsbyapiid()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    /**
     * api资料是否唯一
     * @param api
     */
    isUniqueDetail(api) {
      console.log(api.id)
      for (let i = 0; i < this.apiList.length; i++) {
        if (this.apiList[i].id !== api.id) { // 排除自己
          console.log(this.apiList[i].id)
          if (this.apiList[i].apiname === api.apiname) {
            this.$message.error('api名已存在')
            return false
          }
        }
      }
      return true
    }
  }
}
</script>
<style scoped>
.editor-container{
  width: auto;
  position: relative;
  height: 100%;
}
</style>

