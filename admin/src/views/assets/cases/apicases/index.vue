<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            :loading="btnLoading"
            icon="el-icon-refresh"
            v-if="hasPermission('apicases:list')"
            @click.native.prevent="getapicasesList"
          >刷新
          </el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('apicases:add')"
            @click.native.prevent="showAddapicasesDialog"
          >添加用例
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:copy')"
            @click.native.prevent="showCopyapicasesDialog"
          >复制用例
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:batchcopy')"
            @click.native.prevent="showCopyBatchapicasesDialog"
          >批量复制
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:batchassert')"
            @click.native.prevent="showBatchAssertDialog"
          >批量接口断言
          </el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('apicases:batchdelete')"
            @click.native.prevent="removebatchapicase"
          >批量删除
          </el-button>

<!--          <el-button-->
<!--            type="primary"-->
<!--            size="mini"-->
<!--            icon="el-icon-plus"-->
<!--            v-if="hasPermission('apicases:add')"-->
<!--            @click.native.prevent="showAddapicasesconditionnotexistDialog"-->
<!--          >调试前置条件-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            type="danger"-->
<!--            size="mini"-->
<!--            v-if="hasPermission('apicases:add')"-->
<!--            @click.native.prevent="showdeleteapicasesconditionnotexistDialog"-->
<!--          >取消调试前置-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            size="mini"-->
<!--            icon="el-icon-plus"-->
<!--            v-if="hasPermission('apicases:add')"-->
<!--            @click.native.prevent="showGlobalHeadernotexistDialog"-->
<!--          >调试全局Header-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            type="danger"-->
<!--            size="mini"-->
<!--            v-if="hasPermission('apicases:add')"-->
<!--            @click.native.prevent="showGlobalHeaderexistDialog"-->
<!--          >取消全局Header-->
<!--          </el-button>-->
        </el-form-item>
<!--        </el-form>-->
<!--      <el-form :inline="true">-->
        <span v-if="hasPermission('apicases:search')" >
          <el-form-item label="微服务：">
            <el-select style="width: 120px" v-model="search.deployunitname" filterable placeholder="微服务" clearable @change="deployunitselectChanged($event)">
              <el-option label="请选择" value="请选择" />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname"/>
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="模块：">
            <el-select style="width: 120px" v-model="search.modelname" filterable placeholder="模块" clearable @change="searchmodelselectChanged($event)">
              <el-option label="请选择" value="请选择" />
              <div v-for="(model, index) in modelList" :key="index">
                <el-option :label="model.modelname" :value="model.modelname"/>
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="API：">
            <el-select style="width: 120px" v-model="search.apiname" filterable placeholder="API" clearable @change="searchapiselectChanged($event)">
              <el-option label="请选择" value="请选择" />
              <div v-for="(api, index) in apiList" :key="index">
                <el-option :label="api.apiname" :value="api.apiname"/>
              </div>
            </el-select>
          </el-form-item>

           <el-form-item  label="Url路径:">
            <el-input style="width: 130px" v-model="search.path" clearable @keyup.enter.native="searchBy" placeholder="Url路径"></el-input>
          </el-form-item>

          <el-form-item label="用例类型：">
          <el-select style="width: 120px" v-model="search.casetype" placeholder="用例类型" clearable>
            <el-option label="请选择" value />
            <el-option label="功能" value="功能"></el-option>
            <el-option label="性能" value="性能"></el-option>
          </el-select>
         </el-form-item>

          <el-form-item label="用例：">
            <el-input   clearable v-model="search.casename" clearable @keyup.enter.native="searchBy" placeholder="用例" style="width:150px">
            </el-input>
          </el-form-item>

          <el-form-item  label="范围:">
            <el-select v-model="search.nickname" clearable placeholder="范围"  @change="creatorselectChanged($event)">
              <el-option label="我的" value="我的" />
              <el-option label="全部" value="全部" />
            </el-select>
          </el-form-item>

          <el-form-item >
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="apicasesList"
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

      <el-table-column label="用例名" :show-overflow-tooltip="true" align="center" prop="casename" width="100"/>
      <el-table-column :show-overflow-tooltip="true" label="微服务" align="center" prop="deployunitname" width="120"/>
      <el-table-column :show-overflow-tooltip="true" label="模块" align="center" prop="modelname" width="60"/>
      <el-table-column :show-overflow-tooltip="true" label="API" align="center" prop="apiname" width="100"/>
<!--      <el-table-column label="Jmeter-Class" align="center" prop="casejmxname" width="100"/>-->
      <el-table-column label="类型" align="center" prop="casetype" width="50"/>
      <el-table-column label="线程" align="center" prop="threadnum" width="50"/>
      <el-table-column label="循环" align="center" prop="loops" width="50"/>
      <el-table-column :show-overflow-tooltip="true" label="用例描述" align="center" prop="casecontent" width="80"/>
      <el-table-column :show-overflow-tooltip="true"  label="维护人" align="center" prop="mnickname" width="60"/>
      <el-table-column :show-overflow-tooltip="true"  label="操作人" align="center" prop="creator" width="60"/>
      <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="120">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>
      <el-table-column label="管理" align="center" width="150"
                       v-if="hasPermission('apicases:update')  || hasPermission('apicases:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('apicases:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateapicasesDialog(scope.$index)"
          >修改
          </el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('apicases:delete') && scope.row.id !== id"
            @click.native.prevent="removeapicases(scope.$index)"
          >删除
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="用例操作" align="center" width="350"
                       v-if="hasPermission('apicases:update')  || hasPermission('apicases:delete')">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:params') && scope.row.id !== id"
            @click.native.prevent="showcasedataDialog(scope.$index)"
          >用例数据
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:params') && scope.row.id !== id"
            @click.native.prevent="showAssertDialog(scope.$index)"
          >断言
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:params') && scope.row.id !== id"
            @click.native.prevent="showTestDialog(scope.$index)"
          >调试
          </el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('apicases:params') && scope.row.id !== id"
            @click.native.prevent="showCaseVariablesDialog(scope.$index)"
          >提取变量
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
        style="width: 400px; margin-left:50px;"
        :model="tmpapicases"
        ref="tmpapicases"
      >
        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpapicases.deployunitname" clearable style="width:100%" placeholder="微服务" @change="selectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="模块" prop="modelname" >
          <el-select v-model="tmpapicases.modelname" clearable style="width:100%" placeholder="模块" @change="modelselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(model, index) in modelList" :key="index">
              <el-option :label="model.modelname" :value="model.modelname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API" prop="apiname" required >
          <el-select v-model="tmpapicases.apiname" clearable style="width:100%" placeholder="API" @change="apiselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(apiname, index) in apiList" :key="index">
              <el-option :label="apiname.apiname" :value="apiname.apiname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="用例类型" prop="casetype" required >
          <el-select v-model="tmpapicases.casetype" style="width:100%" placeholder="用例类型" @change="funorperformChanged($event)">
            <el-option label="功能" value="功能"></el-option>
            <el-option label="性能" value="性能"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用例名" prop="casename" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model.trim="tmpapicases.casename"
          />
        </el-form-item>

        <div v-if="threadloopvisible">
        <el-form-item label="线程数" prop="threadnum" required>
          <el-input
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='20'
            type="number"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicases.threadnum"
          />
        </el-form-item>
        </div>


        <el-form-item label="循环次数" prop="loops" required>
          <el-input placeholder="用例循环执行次数"
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='20'
            type="number"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicases.loops"
          />
        </el-form-item>

        <div v-if="JmeterClassVisible">
        <el-form-item label="Jmeter" prop="casejmxname" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicases.casejmxname"
          />
        </el-form-item>
        </div>

        <el-form-item label="维护人:" prop="mnickname" required>
          <el-select v-model="tmpapicases.mnickname" filterable clearable placeholder="维护人" style="width:100%"
                     @change="mnicknameselectChanged($event)">
            <div v-for="(mnickname, index) in accountList" :key="index">
              <el-option :label="mnickname.nickname" :value="mnickname.nickname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="用例描述" prop="casecontent" required>
          <el-input
            type="text"
            maxlength="50"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicases.casecontent"
          />
        </el-form-item>
<!--        <el-form-item label="优先级" prop="level" required>-->
<!--          <el-input-->
<!--            oninput="value=value.replace(/[^\d]/g,'')"-->
<!--            maxLength='10'-->
<!--            type="number"-->
<!--            prefix-icon="el-icon-message"-->
<!--            auto-complete="off"-->
<!--            v-model="tmpapicases.level"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="备注" prop="memo" >
          <el-input
            type="text"
            maxlength="100"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpapicases.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapicases'].resetFields()"
        >重置
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapicases"
        >添加
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapicases"
        >修改
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="paramtitle" :visible.sync="paramdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
        :model="tmpapicasesdata"
        ref="tmpapicasesdata"
      >
        <el-form-item label="用例名">
          <el-input
            readonly="true"
            v-model="tmpapicases.casename"
          />
        </el-form-item>
        <el-form-item label="参数类型" prop="propertytype" required >
          <el-select v-model="tmpapicasesdata.propertytype" placeholder="参数类型" style="width:100%" @change="selectparamsChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(paramtype, index) in caseparamtypelist" :key="index">
              <el-option :label="paramtype.propertytype" :value="paramtype.propertytype" required/>
            </div>
          </el-select>
        </el-form-item>

        <div v-if="HeaderandParamsVisible" >
          <el-form-item
            v-for="(param, index) in tmpcaseparamslist"
            :label="param"
            :key="index"
          >
            <el-input
              type="text"
              prefix-icon="el-icon-edit"
              v-model="paraList[index]"
            />
          </el-form-item>
        </div>

        <div v-if="BodyVisible">
          <el-form-item label="Body值：" prop="keyname" required>
            <el-input
              type="textarea"
              rows="30" cols="50"
              prefix-icon="el-icon-message"
              auto-complete="off"
              v-model.trim="tmpapicasesdata.keyname"
              :placeholder="keyholder"
            />
          </el-form-item>
        </div>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="paramdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addapicasesdata"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="用例值"  width="1000px"  :visible.sync="casedataialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 900px; margin-left:50px;"
        :model="tmpapicasesdata"
        ref="tmpapicasesdata"
      >
        <el-form-item label="用例名:">
          <el-input
            readonly="true"
            v-model="tmpapicases.casename"
          />
        </el-form-item>

        <template>
          <el-tabs v-model="activeName" type="card" ref="tabs">
            <el-tab-pane label="Header" name="zero">
              <template>
                <el-table :data="Headertabledatas" border>
                  <el-table-column label="参数" prop="apiparam" align="center">
                    <template slot-scope="scope">
                      <el-input size="mini" readonly="true" v-model="scope.row.apiparam"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="值" prop="apiparamvalue" align="center">
                    <template slot-scope="scope">
                      <el-input size="mini" placeholder="值" v-model="scope.row.apiparamvalue"></el-input>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </el-tab-pane>
            <el-tab-pane label="Params" name="first">
              <template>
                <el-table :data="Paramstabledatas" border>
                  <el-table-column label="参数"  align="center">
                    <template slot-scope="scope">
                      <el-input size="mini" readonly="true" v-model="scope.row.apiparam"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="值类型"  align="center">
                    <template slot-scope="scope">
                      <el-input size="mini" readonly="true" v-model="scope.row.paramstype"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="值"  align="center">
                    <template slot-scope="scope">
                      <el-input size="mini" placeholder="值" v-model="scope.row.apiparamvalue"></el-input>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </el-tab-pane>
            <el-tab-pane label="Body" name="second">
              <template>
                <div v-if="BodyParamDataVisible">
                  <el-table :data="Bodytabledatas" border>
                    <el-table-column label="参数"  align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="参数名" v-model="scope.row.apiparam"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="值类型"  align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" readonly="true" v-model="scope.row.paramstype"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="默认值"  align="center">
                      <template slot-scope="scope">
                        <el-input size="mini" placeholder="默认值" v-model="scope.row.apiparamvalue"></el-input>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
                <div v-if="BodyDataVisible">
                    <el-form-item label="Body值：" prop="apiparamvalue" >
                      <el-input
                        type="textarea"
                        rows="20" cols="70"
                        prefix-icon="el-icon-message"
                        auto-complete="off"
                        v-model.trim="tmpapicasesbodydata.apiparamvalue"
                      />
                    </el-form-item>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="使用变量" name="third">
              <uservariables></uservariables>
            </el-tab-pane>
          </el-tabs>
        </template>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="casedataialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addnewapicasesdata"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="CopyApiCase" :visible.sync="CopydialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpcopycase"
        ref="tmpcopycase"
      >      <el-form-item label="源微服务" prop="sourcedeployunitname" required >
        <el-select v-model="tmpcopycase.sourcedeployunitname" placeholder="微服务" style="width:100%" @change="CopyCasesSourceDeployselectChanged($event)">
          <el-option label="请选择" value="''" style="display: none" />
          <div v-for="(depunitname, index) in deployunitList" :key="index">
            <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
          </div>
        </el-select>
      </el-form-item>

      <el-form-item label="用例来源" prop="sourcecasename" required >
        <el-select v-model="tmpcopycase.sourcecasename" placeholder="用例" style="width:100%" @change="CopySourceCasesChanged($event)">
          <el-option label="请选择" value="''" style="display: none" />
          <div v-for="(testcase, index) in sourcetestcaseList" :key="index">
            <el-option :label="testcase.casename" :value="testcase.casename" required/>
          </div>
        </el-select>
      </el-form-item>
      <el-form-item label="新用例名" prop="newcasename" required>
        <el-input
          type="text"
          maxlength="40"
          prefix-icon="el-icon-edit"
          auto-complete="off"
          v-model="tmpcopycase.newcasename"
        />
      </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="CopydialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="copycases"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="CopyBatchApiCase" :visible.sync="CopybatchdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpbatchcopycase"
        ref="tmpbatchcopycase"
      >
        <el-form-item label="源微服务" prop="sourcedeployunitname" required >
        <el-select v-model="tmpbatchcopycase.sourcedeployunitname" placeholder="微服务" style="width:100%" @change="CopyBatchCasesSourceDeployselectChanged($event)">
          <el-option label="请选择" value="''" style="display: none" />
          <div v-for="(depunitname, index) in deployunitList" :key="index">
            <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
          </div>
        </el-select>
      </el-form-item>
        <el-form-item label="目标微服务" prop="destinationdeployunitname" required >
          <el-select v-model="tmpbatchcopycase.destinationdeployunitname" placeholder="微服务" style="width:100%" @change="CopyBatchDesiCasesSourceDeployselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="CopybatchdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="copybatchcases"
        >保存
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="loadassert" width="1000px" :visible.sync="AssertdialogFormVisible">
        <el-tabs v-model="assertactiveName"  type="card" ref="asserttabs">
          <el-tab-pane label="接口断言" name="zero">
            <template>
              <div class="filter-container">
                <el-form :inline="true"  :model="searchassert" ref="searchcase" >
                  <el-form-item>
                    <el-button
                      type="primary"
                      size="mini"
                      icon="el-icon-plus"
                      @click.native.prevent="showAddassertDialog"
                    >添加接口断言
                    </el-button>
                  </el-form-item>

                  <el-form-item label="接口断言类型:"  prop="asserttype" >
                    <el-select v-model="searchassert.asserttype" placeholder="断言类型" >
                      <el-option label="Respone断言" value="Respone"></el-option>
                      <el-option label="Json断言" value="Json"></el-option>
                      <el-option label="Xml断言" value="Xml"></el-option>
                    </el-select>
                  </el-form-item>

                  <el-form-item>
                    <el-button type="primary" @click="searchassertBy" :loading="btnLoading">查询</el-button>
                  </el-form-item>

                </el-form>
              </div>
              <el-table
                ref="assertTable"
                :data="assertList"
                :key="assertitemKey"
                element-loading-text="loading"
                border
                fit
                highlight-current-row
              >
                <el-table-column label="编号" align="center" width="60">
                  <template slot-scope="scope">
                    <span v-text="assertgetIndex(scope.$index)"></span>
                  </template>
                </el-table-column>
                <el-table-column label="断言类型"  align="center" prop="asserttype" width="80"/>
                <el-table-column label="断言子类型"  align="center" prop="assertsubtype" width="120"/>
                <el-table-column label="表达式"  align="center" prop="expression" width="150"/>
                <el-table-column label="条件" align="center" prop="assertcondition" width="90"/>
                <el-table-column label="期望值"  align="center" prop="assertvalues" width="180"/>
                <el-table-column label="断言值类型"  align="center" prop="assertvaluetype" width="100"/>
                <el-table-column label="管理" align="center"  width="180">
                  <template slot-scope="scope">
                    <el-button
                      type="warning"
                      size="mini"
                      @click.native.prevent="showUpdateapicasesassertDialog(scope.$index)"
                    >修改
                    </el-button>
                    <el-button
                      type="danger"
                      size="mini"
                      @click.native.prevent="removeapicasesassert(scope.$index)"
                    >删除
                    </el-button>

                  </template>
                </el-table-column>

              </el-table>
              <el-pagination
                @size-change="asserthandleSizeChange"
                @current-change="asserthandleCurrentChange"
                :current-page="searchassert.page"
                :page-size="searchassert.size"
                :total="asserttotal"
                :page-sizes="[10, 20, 30, 40]"
                layout="total, sizes, prev, pager, next, jumper"
              ></el-pagination>
            </template>
          </el-tab-pane>
          <el-tab-pane label="数据库断言" name="first">
            <template>
              <div class="filter-container">
                <el-form :inline="true"  :model="searchdbassert" ref="searchdbassert" >
                  <el-form-item>
                    <el-button
                      type="primary"
                      size="mini"
                      icon="el-icon-plus"
                      @click.native.prevent="showAdddbassertDialog"
                    >添加数据库断言
                    </el-button>
                  </el-form-item>

                  <el-form-item label="环境：" prop="enviroment"  >
                    <el-select clearable style="width:200px" v-model="searchdbassert.enviroment"  placeholder="环境" @change="dbassertEnviromentselectChanged($event)"   >
                      <el-option label="请选择"  value=""  />
                      <div v-for="(enviroment, index) in enviromentnameList" :key="index">
                        <el-option :label="enviroment.enviromentname" :value="enviroment.enviromentname" required />
                      </div>
                    </el-select>
                  </el-form-item>

                  <el-form-item label="数据库组件：" prop="assemblename"  >
                    <el-select clearable v-model="searchdbassert.assemblename" filterable placeholder="数据库组件" style="width:100%">
                      <el-option label="请选择" value="''" style="display: none" />
                      <div v-for="(macname, index) in enviroment_assembleList" :key="index">
                        <el-option :label="macname.deployunitname" :value="macname.deployunitname" required/>
                      </div>
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="searchdbassertBy" :loading="btnLoading">查询</el-button>
                  </el-form-item>

                </el-form>
              </div>
              <el-table
                ref="assertTable"
                :data="dbassertList"
                :key="dbassertitemKey"
                element-loading-text="loading"
                border
                fit
                highlight-current-row
              >
                <el-table-column label="编号" align="center" width="60">
                  <template slot-scope="scope">
                    <span v-text="dbassertgetIndex(scope.$index)"></span>
                  </template>
                </el-table-column>
                <el-table-column label="环境"  align="center" prop="enviroment" width="150"/>
                <el-table-column label="组件"  align="center" prop="assemblename" width="120"/>
                <el-table-column  :show-overflow-tooltip="true"   label="查询sql"  align="center" prop="expression" width="300"/>
                <el-table-column label="结果条数"  align="center" prop="expectrecordsnums" width="100"/>
                <el-table-column label="管理" align="center"  width="230">
                  <template slot-scope="scope">
                    <el-button
                      type="warning"
                      size="mini"
                      @click.native.prevent="showUpdateapicasesdbassertDialog(scope.$index)"
                    >修改
                    </el-button>
                    <el-button
                      type="danger"
                      size="mini"
                      @click.native.prevent="removeapicasesdbassert(scope.$index)"
                    >删除
                    </el-button>
                    <el-button
                      type="primary"
                      size="mini"
                      @click.native.prevent="showapicasesdbassertvalueDialog(scope.$index)"
                    >断言值
                    </el-button>
                  </template>
                </el-table-column>

              </el-table>
              <el-pagination
                @size-change="dbasserthandleSizeChange"
                @current-change="dbasserthandleCurrentChange"
                :current-page="searchdbassert.page"
                :page-size="searchdbassert.size"
                :total="dbasserttotal"
                :page-sizes="[10, 20, 30, 40]"
                layout="total, sizes, prev, pager, next, jumper"
              ></el-pagination>
            </template>
          </el-tab-pane>
        </el-tabs>
<!--      </div>-->
    </el-dialog>
    <el-dialog :title="dbAsserttextMap[dbAssertdialogStatus]" width="800px" :visible.sync="dbAssertAUdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="150px"
        style="width: 400px; margin-left:50px;"
        :model="tmpdbassert"
        ref="tmpdbassert"
      >
        <el-form-item label="环境：" prop="enviroment"  required>
          <el-select clearable style="width:500px" v-model="tmpdbassert.enviroment"  placeholder="环境" @change="dbassertEnviromentselectChanged($event)" >
            <div v-for="(enviroment, index) in enviromentnameList" :key="index">
              <el-option :label="enviroment.enviromentname" :value="enviroment.enviromentname" required />
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="数据库组件：" prop="assemblename" required >
          <el-select clearable style="width:500px" v-model="tmpdbassert.assemblename" filterable placeholder="数据库组件"  @change="dbassertselectChangedAS($event)">
            <div v-for="(macname, index) in enviroment_assembleList" :key="index">
              <el-option :label="macname.deployunitname" :value="macname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

          <el-form-item label="查询sql" prop="expression" required>
            <el-input style="width:500px"
              type="textarea" rows="20" cols="90"
              maxlength="4000"
              prefix-icon="el-icon-edit"
              auto-complete="off"
              v-model="tmpdbassert.expression"
            />
          </el-form-item>

        <el-form-item label="结果条数" prop="expectrecordsnums" required>
          <el-input style="width:500px"
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='10'
            type="number"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbassert.expectrecordsnums"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dbAssertAUdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="dbAssertdialogStatus === 'add'"
          @click.native.prevent="adddbassert"
        >保存
        </el-button>
        <el-button
          type="success"
          v-if="dbAssertdialogStatus === 'update'"
          @click.native.prevent="updatedbassert"
        >更新
        </el-button>
      </div>
    </el-dialog>
    <el-dialog width="1000px" title='数据库断言值列表' :visible.sync="dbassertvaluelistDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('apicasesdbassertvalue:add')"
              @click.native.prevent="showAddApicasesdbassertvalueDialog"
            >新增断言值</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="ApicasesdbassertvalueList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="ApicasesdbassertvaluegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="列名" align="center" prop="fieldname" width="90"/>
        <el-table-column :show-overflow-tooltip="true" label="行号" align="center" prop="roworder" width="70"/>
        <el-table-column :show-overflow-tooltip="true" label="期望值" align="center" prop="expectvalue" width="130"/>
        <el-table-column :show-overflow-tooltip="true" label="值类型" align="center" prop="valuetype" width="80"/>
        <el-table-column :show-overflow-tooltip="true" label="条件" align="center" prop="assertcondition" width="70"/>
        <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center" width="180"
                         v-if="hasPermission('apicasesdbassertvalue:update')  || hasPermission('apicasesdbassertvalue:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('apicasesdbassertvalue:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatedbassertvalueDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('apicasesdbassertvalue:delete') && scope.row.id !== id"
              @click.native.prevent="removedbassertvalue(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="dbassertvaluehandleSizeChange"
        @current-change="dbassertvaluehandleCurrentChange"
        :current-page="searchdbassertvalue.page"
        :page-size="searchdbassertvalue.size"
        :total="dbassertvaluetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>
    <el-dialog :title="dbAssertvaluetextMap[dbAssertvaluedialogStatus]" width="800px" :visible.sync="dbassertvalueDialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="150px"
        style="width: 400px; margin-left:50px;"
        :model="tmpdbassertvalue"
        ref="tmpdbassertvalue"
      >
        <el-form-item label="列名" prop="fieldname" required>
          <el-input style="width:500px"
                    type="text"
                    maxlength="60"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    v-model="tmpdbassertvalue.fieldname"
          />
        </el-form-item>

        <el-form-item label="行号" prop="roworder" required>
          <el-input style="width:500px"
                    type="text"
                    maxlength="20"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    v-model="tmpdbassertvalue.roworder"
          />
        </el-form-item>

        <el-form-item label="期望值" prop="expectvalue" required>
          <el-input style="width:500px"
                    maxlength="200"
                    type="text"
                    prefix-icon="el-icon-edit"
                    auto-complete="off"
                    v-model="tmpdbassertvalue.expectvalue"
          />
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select style="width:500px" v-model="tmpdbassertvalue.valuetype" placeholder="变量值类型">
            <el-option label="Number" value="Number"></el-option>
            <el-option label="String" value="String"></el-option>
            <el-option label="Array" value="Array"></el-option>
            <el-option label="Bool" value="Bool"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="条件" prop="assertcondition" required >
          <el-select v-model="tmpdbassertvalue.assertcondition" style="width:500px" placeholder="条件">
            <el-option label="等于" value="="></el-option>
            <el-option label="大于" value=">"></el-option>
            <el-option label="小于" value="<"></el-option>
            <el-option label="包含" value="Contain"></el-option>
            <el-option label="不包含" value="NoContain"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dbassertvalueDialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="dbAssertvaluedialogStatus === 'add'"
          @click.native.prevent="adddbassertvalue"
        >保存
        </el-button>
        <el-button
          type="success"
          v-if="dbAssertvaluedialogStatus === 'update'"
          @click.native.prevent="updatedbassertvalue"
        >更新
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="AsserttextMap[AssertdialogStatus]" :visible.sync="AssertAUdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="150px"
        style="width: 400px; margin-left:50px;"
        :model="tmpassert"
        ref="tmpassert"
      >
      <el-form-item label="断言类型" prop="asserttype" required >
        <el-select v-model="tmpassert.asserttype" style="width:100%" placeholder="断言类型" @change="asserttypeselectChanged($event)">
          <el-option label="Respone断言" value="Respone"></el-option>
          <el-option label="Json断言" value="Json"></el-option>
          <el-option label="Xml断言" value="Xml"></el-option>
        </el-select>
      </el-form-item>

        <div v-if="AssertSubVisible">
        <el-form-item label="断言子类型" prop="assertsubtype" required >
          <el-select v-model="tmpassert.assertsubtype" style="width:100%" placeholder="断言子类型">
            <el-option label="Code" value="Code"></el-option>
            <el-option label="文本" value="文本"></el-option>
          </el-select>
        </el-form-item>
        </div>

        <div v-if="ExpressionVisible">
        <el-form-item label="表达式" prop="expression" required>
          <el-input
            type="text"
            maxlength="400"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpassert.expression"
          />
          <div class="right">
            <el-tooltip placement="right-end">
              <div slot="content">1.如果断言类型是Json则使用JsonPath表示, 例如：$.store.book[0].title  在线解析网站：http://www.e123456.com/aaaphp/online/jsonpath/<br/>2.如果断言类型为XML，则使用XPath表示， 例如：//div/h3//text()=hello|//div/h4//text()   在线解析网站： http://www.ab173.com/other/xpath.php</div>
              <el-button>表达式语法</el-button>
            </el-tooltip>
          </div>
        </el-form-item>
        </div>

        <el-form-item label="条件" prop="assertcondition" required >
          <el-select v-model="tmpassert.assertcondition" style="width:100%" placeholder="条件" @change="assertnameselectChanged($event)">
            <el-option label="等于" value="="></el-option>
            <el-option label="大于" value=">"></el-option>
            <el-option label="小于" value="<"></el-option>
            <el-option label="包含" value="Contain"></el-option>
            <el-option label="不包含" value="NoContain"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="断言值" prop="assertvalues" required>
          <el-input
            type="text"
            maxlength="2000"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpassert.assertvalues"
          />
        </el-form-item>
        <el-form-item label="断言值类型" prop="assertvaluetype" required >
          <el-select v-model="tmpassert.assertvaluetype" style="width:100%" placeholder="断言值类型">
            <el-option label="int" value="int"></el-option>
            <el-option label="Long" value="Long"></el-option>
            <el-option label="Float" value="Float"></el-option>
            <el-option label="Double" value="Double"></el-option>
            <el-option label="Decimal" value="Decimal"></el-option>
            <el-option label="字符串" value="String"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="AssertAUdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          v-if="AssertdialogStatus === 'add'"
          @click.native.prevent="addassert"
        >保存
        </el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          v-if="AssertdialogStatus === 'update'"
          @click.native.prevent="updateassert"
        >更新
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="用例调试" :visible.sync="TestdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="100px"
        style="width: 600px; margin-left:50px;"
      >
        <div class="filter-container">
          <el-form :inline="true" :model="tmptest" ref="tmptest">
            <el-form-item label="用例名：">
              <el-input style="width:500px" readonly="true" v-model="tmpapicases.casename"/>
            </el-form-item>

            <el-form-item label="环境：" prop="enviromentname"  required>
              <el-select style="width:500px" v-model="tmptest.enviromentname"  placeholder="环境" @change="EnviromentselectChanged($event)" >
                <el-option label="请选择"  value=""  />
                <div v-for="(enviroment, index) in enviromentnameList" :key="index">
                  <el-option :label="enviroment.enviromentname" :value="enviroment.enviromentname" required />
                </div>
              </el-select>
            </el-form-item>

<!--            <el-form-item label="前置条件:"  prop="conditionname" >-->
<!--              <el-select style="width:490px" v-model="tmptest.conditionname" placeholder="前置父条件" @change="notexistconditionnameselectChanged($event)">-->
<!--                <el-option label="请选择" value="请选择" />-->
<!--                <div v-for="(testcase, index) in conditionList" :key="index">-->
<!--                  <el-option :label="testcase.conditionname" :value="testcase.conditionname" />-->
<!--                </div>-->
<!--              </el-select>-->
<!--            </el-form-item>-->


            <el-form-item label="全局Header:"  prop="globalheadername" >
              <el-select style="width:315px" v-model="tmptest.globalheadername" placeholder="全局Header" @change="notexistheaderselectChanged($event)">
                <el-option label="请选择" value="请选择" />
                <div v-for="(globalheader, index) in globalheaderallList" :key="index">
                  <el-option :label="globalheader.globalheadername" :value="globalheader.globalheadername" />
                </div>
              </el-select>
              <el-button type="primary" :loading="btnLoading" @click.native.prevent="runtest">调试</el-button>
              <el-button type="primary" :loading="btnLoading" @click.native.prevent="showtestcaseConditionDialog">前置条件</el-button>
            </el-form-item>

            <template>
              <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
                <el-tab-pane label="通用" name="zero">
                  <el-input
                    type="textarea"
                    style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                    rows="20" cols="90"
                    maxlength="4000"
                    v-model="tmptest.general"
                  />
                </el-tab-pane>

                <el-tab-pane label="响应内容" name="first">
                    <el-input
                      type="textarea"
                      style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                      rows="20" cols="90"
                      maxlength="4000"
                      v-model="tmptest.respone"
                    />
                </el-tab-pane>

                <el-tab-pane label="请求Header" name="seven">
                  <el-table
                    :data="requestHeadList"
                    element-loading-text="loading"
                    border
                    fit
                    highlight-current-row
                  >
                    <el-table-column label="Name" align="center" prop="keyName" width="250"/>
                    <el-table-column label="Value" align="center" prop="keyValue" width="350"/>
                  </el-table>
                </el-tab-pane>

                <el-tab-pane label="请求Params" name="nine">
                  <el-table
                    :data="requestParamsList"
                    element-loading-text="loading"
                    border
                    fit
                    highlight-current-row
                  >
                    <el-table-column label="Name" align="center" prop="keyName" width="250"/>
                    <el-table-column label="Value" align="center" prop="keyValue" width="350"/>
                  </el-table>
                </el-tab-pane>

                <el-tab-pane label="请求Body" name="eight">
                  <el-input
                    type="textarea"
                    style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                    rows="20" cols="90"
                    maxlength="4000"
                    v-model="tmptest.requestdata"
                  />
                </el-tab-pane>

                <el-tab-pane label="响应Header" name="five">
                  <el-table
                    :data="headerList"
                    element-loading-text="loading"
                    border
                    fit
                    highlight-current-row
                  >
                    <el-table-column label="Name" align="center" prop="name" width="250"/>
                    <el-table-column label="Value" align="center" prop="value" width="350"/>
                  </el-table>
                </el-tab-pane>

                <el-tab-pane label="响应码" name="second">
                  <el-input
                    type="textarea"
                    style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                    rows="20" cols="90"
                    maxlength="4000"
                    v-model="tmptest.code"
                  />
                </el-tab-pane>
                <el-tab-pane label="响应时间(ms)" name="third">
                  <el-input
                    type="textarea"
                    style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                    rows="20" cols="90"
                    maxlength="4000"
                    v-model="tmptest.responeTime"
                  />
                </el-tab-pane>
                <el-tab-pane label="大小(B)" name="fourth">
                  <el-input
                    type="textarea"
                    style="width: 100%;border: none;outline: none;resize:none;overflow:hidden" readonly
                    rows="20" cols="90"
                    maxlength="4000"
                    v-model="tmptest.size"
                  />
                </el-tab-pane>

                <el-tab-pane label="Cookies" name="six">
                  <el-table
                    :data="cookies"
                    element-loading-text="loading"
                    border
                    fit
                    highlight-current-row
                  >
                    <el-table-column label="Name" align="center" prop="name" width="250"/>
                    <el-table-column label="Value" align="center" prop="value" width="350"/>
                  </el-table>
                </el-tab-pane>
              </el-tabs>
            </template>
          </el-form>
        </div>

      </el-form>
    </el-dialog>

    <el-dialog title="添加调试前置条件用例" :visible.sync="casedebugconditionnotexistdialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchnotexistcase" ref="searchnotexistcase" >

          <el-form-item label="前置调试父条件:"  prop="conditionname" required>
            <el-select v-model="searchnotexistcase.conditionname" placeholder="前置父条件" @change="notexistconditionnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(testcase, index) in conditionList" :key="index">
                <el-option :label="testcase.conditionname" :value="testcase.conditionname" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="微服务:" prop="deployunitname" required>
            <el-select v-model="searchnotexistcase.deployunitname" placeholder="微服务" @change="notexistdeployunitnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchnotexistcaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="caseTable"
        :data="apicasesnotexistList"
        :key="casenotexistKey"
        @row-click="casenotexisthandleClickTableRow"
        @selection-change="casenotexisthandleSelectionChange"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="casenotexistgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="用例名" align="center" prop="casename" width="350"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="220"/>
      </el-table>
      <el-pagination
        @size-change="casenotexisthandleSizeChange"
        @current-change="casenotexisthandleCurrentChange"
        :current-page="searchnotexistcase.page"
        :page-size="searchnotexistcase.size"
        :total="casenotexisttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="casedebugconditionnotexistdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="adddebugconditiontestcase"
        >添加前置</el-button>
      </div>
    </el-dialog>

    <el-dialog title="取消调试前置条件用例" :visible.sync="casedebugconditionexistdialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchexistcase" ref="searchexistcase" >

          <el-form-item label="前置调试父条件:"  prop="conditionname" required>
            <el-select v-model="searchexistcase.conditionname" placeholder="前置父条件" @change="existconditionnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(testcase, index) in conditionList" :key="index">
                <el-option :label="testcase.conditionname" :value="testcase.conditionname" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="微服务:" prop="deployunitname" required>
            <el-select v-model="searchexistcase.deployunitname" placeholder="微服务" @change="existdeployunitnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchexistcaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table
        ref="caseTable"
        :data="apicasesexistList"
        :key="caseexistKey"
        @row-click="caseexisthandleClickTableRow"
        @selection-change="caseexisthandleSelectionChange"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="caseexistgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="用例名" align="center" prop="casename" width="350"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="220"/>
      </el-table>
      <el-pagination
        @size-change="caseexisthandleSizeChange"
        @current-change="caseexisthandleCurrentChange"
        :current-page="searchexistcase.page"
        :page-size="searchexistcase.size"
        :total="caseexisttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="casedebugconditionexistdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="delatedebugconditiontestcase"
        >取消前置</el-button>
      </div>
    </el-dialog>

    <el-dialog title="添加全局Header用例" :visible.sync="caseglobalheadernotexistdialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchglobalnotexistheadercase" ref="searchglobalnotexistheadercase" >

          <el-form-item label="全局Header:"  prop="globalheadername" required>
            <el-select v-model="searchglobalnotexistheadercase.globalheadername" placeholder="全局Header" @change="notexistheaderselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(globalheader, index) in globalheaderallList" :key="index">
                <el-option :label="globalheader.globalheadername" :value="globalheader.globalheadername" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="微服务:" prop="deployunitname" required>
            <el-select v-model="searchglobalnotexistheadercase.deployunitname" placeholder="微服务" @change="notexistdeployunitnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchheadernotexist" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="caseTable"
        :data="apicasesheadernotexistList"
        :key="headercasenotexistKey"
        @row-click="casenotexisthandleClickTableRow"
        @selection-change="headercasenotexisthandleSelectionChange"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="headercasenotexistgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="用例名" align="center" prop="testcasename" width="350"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="220"/>
      </el-table>
      <el-pagination
        @size-change="headercasenotexisthandleSizeChange"
        @current-change="headercasenotexisthandleCurrentChange"
        :current-page="searchglobalnotexistheadercase.page"
        :page-size="searchglobalnotexistheadercase.size"
        :total="headercasenotexisttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="caseglobalheadernotexistdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addheadercasesdebug"
        >添加Header</el-button>
      </div>
    </el-dialog>

    <el-dialog title="取消全局Header用例" :visible.sync="caseglobalheaderexistdialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchglobalheadercase" ref="searchglobalheadercase" >

          <el-form-item label="全局Header:"  prop="globalheadername" required>
            <el-select v-model="searchglobalheadercase.globalheadername" placeholder="全局Header" @change="existheaderselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(globalheader, index) in globalheaderallList" :key="index">
                <el-option :label="globalheader.globalheadername" :value="globalheader.globalheadername" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="微服务:" prop="deployunitname" required>
            <el-select v-model="searchglobalheadercase.deployunitname" placeholder="微服务" @change="existdeployunitnameselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchheaderexist" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="caseTable"
        :data="apicasesheaderexistList"
        :key="headercaseexistKey"
        @row-click="casenotexisthandleClickTableRow"
        @selection-change="headercaseexisthandleSelectionChange"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="headercaseexistgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="用例名" align="center" prop="testcasename" width="350"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="220"/>
      </el-table>
      <el-pagination
        @size-change="headercaseexisthandleSizeChange"
        @current-change="headercaseexisthandleCurrentChange"
        :current-page="searchglobalheadercase.page"
        :page-size="searchglobalheadercase.size"
        :total="headercaseexisttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="caseglobalheaderexistdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="deleteheadercase"
        >取消Header</el-button>
      </div>
    </el-dialog>

    <el-dialog title="批量断言" :visible.sync="BatchAssertdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="150px"
        style="width: 400px; margin-left:50px;"
        :model="tmpbatchassert"
        ref="tmpbatchassert"
      >
        <el-form-item label="断言类型" prop="asserttype" required >
          <el-select v-model="tmpbatchassert.asserttype" style="width:100%" placeholder="断言类型" @change="asserttypeselectChanged($event)">
            <el-option label="Respone断言" value="Respone"></el-option>
            <el-option label="Json断言" value="Json"></el-option>
            <el-option label="Xml断言" value="Xml"></el-option>
          </el-select>
        </el-form-item>

        <div v-if="AssertSubVisible">
          <el-form-item label="断言子类型" prop="assertsubtype" required >
            <el-select v-model="tmpbatchassert.assertsubtype" style="width:100%" placeholder="断言子类型">
              <el-option label="Code" value="Code"></el-option>
              <el-option label="文本" value="文本"></el-option>
            </el-select>
          </el-form-item>
        </div>

        <div v-if="ExpressionVisible">
          <el-form-item label="表达式" prop="expression" required>
            <el-input
              type="text"
              maxlength="400"
              prefix-icon="el-icon-edit"
              auto-complete="off"
              v-model="tmpbatchassert.expression"
            />
            <div class="right">
              <el-tooltip placement="right-end">
                <div slot="content">1.如果断言类型是Json则使用JsonPath表示, 例如：$.store.book[0].title  在线解析网站：http://www.e123456.com/aaaphp/online/jsonpath/<br/>2.如果断言类型为XML，则使用XPath表示， 例如：//div/h3//text()=hello|//div/h4//text()   在线解析网站： http://www.ab173.com/other/xpath.php</div>
                <el-button>表达式语法</el-button>
              </el-tooltip>
            </div>
          </el-form-item>
        </div>

        <el-form-item label="条件" prop="assertcondition" required >
          <el-select v-model="tmpbatchassert.assertcondition" style="width:100%" placeholder="条件">
            <el-option label="等于" value="="></el-option>
            <el-option label="大于" value=">"></el-option>
            <el-option label="小于" value="<"></el-option>
            <el-option label="包含" value="Contain"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="断言值" prop="assertvalues" required>
          <el-input
            type="text"
            maxlength="2000"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpbatchassert.assertvalues"
          />
        </el-form-item>
        <el-form-item label="断言值类型" prop="assertvaluetype" required >
          <el-select v-model="tmpbatchassert.assertvaluetype" style="width:100%" placeholder="断言值类型">
            <el-option label="int" value="int"></el-option>
            <el-option label="Long" value="Long"></el-option>
            <el-option label="Float" value="Float"></el-option>
            <el-option label="Double" value="Double"></el-option>
            <el-option label="Decimal" value="Decimal"></el-option>
            <el-option label="字符串" value="String"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="BatchAssertdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addbatchassert"
        >保存
        </el-button>
      </div>
    </el-dialog>

    <el-dialog width="1200px" title='用例提取变量列表' :visible.sync="caseVariablesDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAddApicasesVariablesDialog"
            >提取变量</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="ApicasesVariablesList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="ApicasesVariablesgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="变量名" align="center" prop="testvariablesname" width="80"/>
        <el-table-column :show-overflow-tooltip="true" label="所属用例" align="center" prop="casename" width="80"/>
        <el-table-column :show-overflow-tooltip="true" label="所属API" align="center" prop="apiname" width="80"/>
        <el-table-column :show-overflow-tooltip="true" label="所属微服务" align="center" prop="deployunitname" width="90"/>
        <el-table-column label="变量来源" align="center" prop="testvariablestype" width="70"/>
        <el-table-column label="变量值类型" align="center" prop="valuetype" width="90"/>
        <el-table-column :show-overflow-tooltip="true" label="变量值提取表达" align="center" prop="variablesexpress" width="110"/>
        <el-table-column :show-overflow-tooltip="true" label="变量描述" align="center" prop="variablesdes" width="110"/>
        <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center" width="180"
                         v-if="hasPermission('ApicasesVariables:update')  || hasPermission('ApicasesVariables:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('ApicasesVariables:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatetestvariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('ApicasesVariables:delete') && scope.row.id !== id"
              @click.native.prevent="removetestvariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="apicasevariableshandleSizeChange"
        @current-change="apicasevariableshandleCurrentChange"
        :current-page="searchapicasevariables.page"
        :page-size="searchapicasevariables.size"
        :total="apicasevariablestotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>

    <el-dialog :title="caseaddvariablestextMap[caseaddvariablesdialogStatus]" :visible.sync="caseaddvariablesdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
        :model="tmptestvariables"
        ref="tmptestvariables"
      >
        <el-form-item label="变量名" prop="testvariablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmptestvariables.testvariablesname"
          />
        </el-form-item>

        <el-form-item label="变量描述" prop="variablesdes" required>
          <el-input
            maxlength="20"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmptestvariables.variablesdes"
          />
        </el-form-item>


        <el-form-item label="变量来源" prop="testvariablestype" required >
          <el-select v-model="tmptestvariables.testvariablestype" placeholder="变量来源" style="width:100%" @change="testvariablestypeselectChanged($event)">
            <el-option label="Header" value="Header"></el-option>
            <el-option label="Cookies" value="Cookies"></el-option>
            <el-option label="Body" value="Body"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select v-model="tmptestvariables.valuetype" placeholder="变量值类型" style="width:100%">
            <el-option label="Number" value="Number"></el-option>
            <el-option label="String" value="String"></el-option>
            <el-option label="Array" value="Array"></el-option>
            <el-option label="Bool" value="Bool"></el-option>
          </el-select>
        </el-form-item>



        <el-form-item :label="expressname" prop="variablesexpress" required>
          <el-input
            type="textarea"
            rows="3"
            cols="10"
            maxlength="200"
            placeholder="例如 $.store.book[0].title"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmptestvariables.variablesexpress"
          />
          <div class="right">
            <el-tooltip placement="right-start">
              <div slot="content">1.如果获取变量值的接口返回数据类型是Json则使用JsonPath表达式提取变量值，例如：$.store.book[0].title   在线解析网站：http://www.e123456.com/aaaphp/online/jsonpath/<br/>2.如果获取变量值接口返回是html，xml则使用XPath表达式提取变量值， 例如：//div/h3//text()   在线解析网站： http://www.ab173.com/other/xpath.php</div>
              <el-button>变量值提取表达语法规则</el-button>
            </el-tooltip>
          </div>
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            maxlength="60"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmptestvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="caseaddvariablesdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="caseaddvariablesdialogStatus === 'add'"
          @click.native.prevent="$refs['tmptestvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="caseaddvariablesdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addtestvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="caseaddvariablesdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatetestvariables"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="用例调试前置条件" width="1120px" :visible.sync="scenecaseConditionFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="ShowAddcasecaseconditionDialog"
            >添加前置接口</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="AddcasedbconditionDialog"
            >添加前置数据库</el-button>
<!--            <el-button-->
<!--              type="primary"-->
<!--              size="mini"-->
<!--              icon="el-icon-plus"-->
<!--              v-if="hasPermission('testscene:scenecasecondition')"-->
<!--              @click.native.prevent="showAddSceneCasedelayconditionDialog"-->
<!--            >添加前置延时</el-button>-->
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddscriptDialog"
            >添加前置脚本</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showconditionorderDialog"
            >设置前置条件顺序</el-button>
          </el-form-item>
        </el-form>
      </div>

      1.接口前置条件：
      <el-table
        :data="apiconditioncaseList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="apiconditioncaseIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="前置条件名" align="center" prop="subconditionname" width="150"/>
        <el-table-column label="所属用例" align="center" prop="conditionname" width="150"/>
        <el-table-column label="前置接口" align="center" prop="casename" width="150"/>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="150">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="150">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('testscene:caseupdateapicondition')  || hasPermission('testscene:casedeleteapicondition')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('testscene:caseupdateapicondition') && scope.row.id !== id"
              @click.native.prevent="showUpdateapiconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('testscene:casedeleteapicondition') && scope.row.id !== id"
              @click.native.prevent="removecaseapicondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('apicases:params') && scope.row.id !== id"
              @click.native.prevent="showCaseVariablesforConditionDialog(scope.$index)"
            >提取变量
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      2 .数据库前置条件：
      <el-table
        :data="dbconditioncaseList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="dbconditioncaseIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="100"/>
        <el-table-column label="所属用例" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
        <el-table-column label="环境" align="center" prop="enviromentname" width="100"/>
        <el-table-column label="组件名" align="center" prop="assemblename" width="100"/>
        <el-table-column label="Sql类型" align="center" prop="dbtype" width="65"/>
        <el-table-column label="Sql内容" align="center" prop="dbcontent" width="80">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>{{ scope.row.dbcontent }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">...</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"  width="240"
                         v-if="hasPermission('dbcondition:update')  || hasPermission('dbcondition:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('dbcondition:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatedbconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('dbcondition:delete') && scope.row.id !== id"
              @click.native.prevent="removedbcondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('dbcondition:delete') && scope.row.id !== id"
              @click.native.prevent="showdbvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>
      3 .脚本前置条件
      <el-table
        :data="scriptconditionList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="scriptconditioncaseIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="150"/>
        <el-table-column label="所属用例" :show-overflow-tooltip="true" align="center" prop="conditionname" width="150"/>
        <el-table-column label="脚本" align="center" prop="script" width="150">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>{{ scope.row.script }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">...</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" :show-overflow-tooltip="true" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" :show-overflow-tooltip="true" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('scriptcondition:update')  || hasPermission('scriptcondition:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('scriptcondition:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatescriptconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('scriptcondition:delete') && scope.row.id !== id"
              @click.native.prevent="removescriptcondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('dbcondition:delete') && scope.row.id !== id"
              @click.native.prevent="showscriptvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="scripttextMap[scriptdialogStatus]" :visible.sync="scriptdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:50px;"
        :model="tmpscriptcondition"
        ref="tmpscriptcondition"
      >
        <el-form-item label="脚本条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpscriptcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="Java代码" prop="script" required >
          <el-input
            type="textarea"
            rows="10" cols="50"
            maxlength="4000"
            v-model="tmpscriptcondition.script"
            placeholder="Java 代码"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="scriptdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="scriptdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpscriptcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="scriptdialogStatus === 'add'"
          @click.native.prevent="addscriptcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="scriptdialogStatus === 'update'"
          @click.native.prevent="updatescriptcondition"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="apiconditiontextMap[apiconditiondialogStatus]" :visible.sync="caseconditiondialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 450px; margin-left:50px;"
        :model="tmpapicondition"
        ref="tmpapicondition"
      >

        <el-form-item label="前置条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpapicondition.deployunitname" filterable placeholder="微服务" style="width:100%" @change="apiconditiondeployunitselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item  label="模块:" prop="modelname" >
          <el-select v-model="tmpapicondition.modelname" filterable placeholder="模块" style="width:100%"  @change="apiconditionmodelselectChanged($event)">
            <el-option label="请选择" value />
            <div v-for="(model, index) in apiconditionmodelList" :key="index">
              <el-option :label="model.modelname" :value="model.modelname" />
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API" prop="apiname" required >
          <el-select v-model="tmpapicondition.apiname" filterable placeholder="API" style="width:100%" @change="apiconditionapiselectChanged($event)">
            <div v-for="(api, index) in apiconditionapiList" :key="index">
              <el-option :label="api.apiname" :value="api.apiname"/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="接口" prop="casename" required >
          <el-select v-model="tmpapicondition.casename" filterable placeholder="接口" style="width:100%" @change="apiconditiontestcaseselectChanged($event)">
            <div v-for="(testcase, index) in conditionapicaseList" :key="index">
              <el-option :label="testcase.casename" :value="testcase.casename" required/>
            </div>
          </el-select>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="caseconditiondialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="apiconditiondialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapicondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="apiconditiondialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapicondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="apiconditiondialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapicondition"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="dbtextMap[dbdialogStatus]" :visible.sync="dbconditiondialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpdbcondition"
        ref="tmpdbcondition"
      >
        <el-form-item label="数据库条件名：" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="环境：" prop="enviromentname" required >
          <el-select v-model="tmpdbcondition.enviromentname" filterable  placeholder="环境" style="width:100%" @change="selectChangedEN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="组件：" prop="assemblename" required >
          <el-select v-model="tmpdbcondition.assemblename" filterable placeholder="组件" style="width:100%" @change="ConditionselectChangedAS($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(macname, index) in enviroment_assembleList" :key="index">
              <el-option :label="macname.deployunitname" :value="macname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="操作类型：" prop="dbtype" required >
          <el-select v-model="tmpdbcondition.dbtype" placeholder="操作类型" style="width:100%" @change="selectChangedDBType($event)">
            <el-option label="新增" value="Insert"  />
            <el-option label="删除" value="Delete"  />
            <el-option label="修改" value="Update"  />
            <el-option label="查询" value="Select"  />
          </el-select>
        </el-form-item>

        <el-form-item label="Sql语句：" prop="dbcontent" required>
          <el-input
            type="textarea"
            rows="10" cols="50"
            maxlength="2000"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbcondition.dbcontent"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dbconditiondialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dbdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dbdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddbcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dbdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedbcondition"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog width="1000px" title='数据库变量列表' :visible.sync="dbVariablesDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAdddbvariablesDialog"
            >添加数据库变量</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="dbvariablesList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="dbVariablesgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="数据库变量名" align="center" :show-overflow-tooltip="true" prop="dbvariablesname" width="100"/>
        <el-table-column :show-overflow-tooltip="true" label="来源前置条件" align="center" prop="conditionname" width="100"/>
        <el-table-column :show-overflow-tooltip="true" label="变量描述" align="center" prop="variablesdes" width="100"/>
        <el-table-column label="变量值类型" align="center" prop="valuetype" width="85"/>
        <el-table-column label="列名" :show-overflow-tooltip="true" align="center" prop="fieldname" width="100"/>
        <el-table-column label="行号" align="center" prop="roworder" width="45"/>
        <el-table-column label="操作人" align="center" prop="creator" width="60"/>
        <el-table-column label="创建时间"  :show-overflow-tooltip="true" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间"  :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"  width="170"
                         v-if="hasPermission('dbvariables:update')  || hasPermission('dbvariables:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('dbvariables:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatedbvariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
              @click.native.prevent="removedbvariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="dbvariableshandleSizeChange"
        @current-change="dbvariableshandleCurrentChange"
        :current-page="searchdbvariables.page"
        :page-size="searchdbvariables.size"
        :total="dbvariablestotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>

    <el-dialog :title="dbvariablestextMap[dbvariablesdialogStatus]" :visible.sync="adddbvariablesdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
        :model="tmpdbvariables"
        ref="tmpdbvariables"
      >
        <el-form-item label="变量名" prop="dbvariablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbvariables.dbvariablesname"
          />
        </el-form-item>

        <el-form-item label="变量描述" prop="variablesdes" required>
          <el-input
            maxlength="20"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbvariables.variablesdes"
          />
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select v-model="tmpdbvariables.valuetype" placeholder="变量值类型" style="width:100%">
            <el-option label="Number" value="Number"></el-option>
            <el-option label="String" value="String"></el-option>
            <el-option label="Array" value="Array"></el-option>
            <el-option label="Bool" value="Bool"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="表列名" prop="fieldname" required>
          <el-input
            placeholder="数据库查询结果集表列名"
            maxLength='50'
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdbvariables.fieldname"
          />
        </el-form-item>

        <el-form-item label="表行号" prop="roworder" required>
          <el-input
            placeholder="数据库查询结果集行号"
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='10'
            type="number"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdbvariables.roworder"
          />
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdbvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="adddbvariablesdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dbvariablesdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dbvariablesdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddbvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dbvariablesdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedbvariables"
        >修改</el-button>
      </div>
    </el-dialog>
    <el-dialog width="1050px" title='脚本变量列表' :visible.sync="scriptVariablesDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAddscriptvariablesDialog"
            >添加脚本变量</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="scriptvariablesList"
        :key="itemKey"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="scriptvariablesgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="脚本变量名" align="center" prop="scriptvariablesname" width="120"/>
        <el-table-column :show-overflow-tooltip="true" label="来源前置条件" align="center" prop="conditionname" width="100"/>
        <el-table-column :show-overflow-tooltip="true" label="变量描述" align="center" prop="variablesdes" width="100"/>
        <el-table-column label="变量值类型" align="center" prop="valuetype" width="85"/>
        <el-table-column label="操作人" align="center" prop="creator" width="70"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('scriptvariables:update')  || hasPermission('scriptvariables:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('scriptvariables:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatescriptvariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('scriptvariables:delete') && scope.row.id !== id"
              @click.native.prevent="removescriptvariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="scriptvariableshandleSizeChange"
        @current-change="scriptvariableshandleCurrentChange"
        :current-page="searchscriptvariables.page"
        :page-size="searchscriptvariables.size"
        :total="scriptvariablestotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>
    <el-dialog :title="scriptVariablestextMap[scriptVariablesdialogStatus]" :visible.sync="addscriptVariablesdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
        :model="tmpscriptvariables"
        ref="tmpscriptvariables"
      >
        <el-form-item label="变量名" prop="scriptvariablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpscriptvariables.scriptvariablesname"
          />
        </el-form-item>

        <el-form-item label="变量描述" prop="variablesdes" required>
          <el-input
            maxlength="20"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpscriptvariables.variablesdes"
          />
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select v-model="tmpscriptvariables.valuetype" placeholder="变量值类型" style="width:100%">
            <el-option label="Number" value="Number"></el-option>
            <el-option label="String" value="String"></el-option>
            <el-option label="Array" value="Array"></el-option>
            <el-option label="Bool" value="Bool"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpscriptvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="addscriptVariablesdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="scriptVariablesdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpscriptvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="scriptVariablesdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addscriptvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="scriptVariablesdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatescriptvariables"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="DelaytextMap[DelaydialogStatus]" :visible.sync="DelaydialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:50px;"
        :model="tmpdelaycondition"
        ref="tmpdelaycondition"
      >
        <el-form-item label="条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdelaycondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="等待时间(秒)" prop="delaytime" required>
          <el-input
            placeholder="等待时间(秒)"
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='10'
            type="number"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdelaycondition.delaytime"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="DelaydialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="DelaydialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdelaycondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="DelaydialogStatus === 'add'"
          @click.native.prevent="adddelaycondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="DelaydialogStatus === 'update'"
          @click.native.prevent="updatedelaycondition"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="前置条件顺序" width="840px" :visible.sync="ConditionOrderdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
      >
        <el-table
          :data="conditionorderList"
          border
          fit
          highlight-current-row
          style="width: 100%" >
          <el-table-column label="编号" align="center" width="50">
            <template slot-scope="scope">
              <span v-text="getIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column prop="subconditionname" align="center" label="前置条件名"  width="200px"></el-table-column>
          <el-table-column prop="conditionname" align="center" label="条件来源" width="200px"></el-table-column>
          <el-table-column prop="subconditiontype" align="center" label="条件类型"  width="110px"></el-table-column>
          <el-table-column prop="orderstatus" align="center" label="状态"  width="60px"></el-table-column>
          <el-table-column prop="conditionorder" align="center" label="顺序"  width="50px"></el-table-column>

          <el-table-column label="操作排序" align="center" width="140px" >
            <template slot-scope="scope">
              <el-button
                size="mini"
                :disabled="scope.$index===0"
                @click="moveUp(scope.$index,scope.row)"><i class="el-icon-arrow-up"></i></el-button>
              <el-button
                size="mini"
                :disabled="scope.$index===(conditionorderList.length-1)"
                @click="moveDown(scope.$index,scope.row)"><i class="el-icon-arrow-down"></i></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="ConditionOrderdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          @click.native.prevent="saveconditionorder"
        >保存</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
  import {
    findcasesbyname as findcasesbyname,
    runtest,
    search,
    addapicases,
    updateapicases,
    removeapicases,
    copycases,
    copybatchcases,
    getcasebydeployunitid,
    removebatchapicase
  } from '@/api/assets/apicases'
  import { addtestvariables, updatetestvariables, removetestvariables, findtestvariablesbycaseid } from '@/api/testvariables/testvariables'
  import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
  import { addapicasesdata as addapicasesdata, getparamvaluebycaseidandtype as getparamvaluebycaseidandtype, casevalueforbody as casevalueforbody, updatepropertydata, updateapicasesdata } from '@/api/assets/apicasesdata'
  import { getapiListbydeploy as getapiListbydeploy, getapi } from '@/api/deployunit/api'
  import { getcaseparatype as getcaseparatype } from '@/api/deployunit/apiparams'
  import { getdepunitLists as getdepunitLists, findDeployNameValueWithCode as findDeployNameValueWithCode } from '@/api/deployunit/depunit'
  import { unix2CurrentTime } from '@/utils'
  import { addapicasesassert, getassertbycaseid as getassertbycaseid, searchassert as searchassert, removeapicasesassert, updateapicasesassert, batchassertapicase } from '@/api/assets/apicasesassert'
  import { mapGetters } from 'vuex'
  import { searchheadernotexist, searchheaderexist, addheadercasesdebug, deleteheadercase } from '@/api/assets/globalheaderuse'
  import { searchnotexist, searchexist, addcasesdebugcondition, delatedebugconditiontestcase } from '@/api/assets/apicasesdebugcondition'
  import { getalltestcondition } from '@/api/condition/condition'
  import { getglobalheaderallList } from '@/api/testvariables/globalheader'
  import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'
  import { search as searchdbcondition, adddbcondition, updatedbcondition, removedbcondition } from '@/api/condition/dbcondition'
  import { search as searchapicondition, addapicondition, removeapicondition, updateapicondition } from '@/api/condition/apicondition'
  import { search as getscriptconditionList, addscriptcondition, updatescriptcondition, removescriptcondition } from '@/api/condition/scriptcondition'
  import { adddelaycondition, updatedelaycondition, removedelaycondition, searchbytype } from '@/api/condition/delaycondition'
  import { getassembleallnameList as getassembleallnameList } from '@/api/enviroment/enviromentassemble'
  import { search as searchdbvariables, adddbvariables, updatedbvariables, removedbvariables } from '@/api/testvariables/dbvariables'
  import { search as searchscriptvariables, addscriptvariables, updatescriptvariables, removescriptvariables } from '@/api/testvariables/scriptvariables'
  import uservariables from '@/components/testvariables'
  import { searchconditionorder, addconditionorder } from '@/api/condition/conditionorder'
  import { findMacAndDepWithEnv as findMacAndDepWithEnv } from '@/api/enviroment/macdepunit'
  import { addapicasesdbassert, searchdbassert as searchdbassert, removeapicasesdbassert, updateapicasesdbassert } from '@/api/assets/apicasesdbassert'
  import { searchdbassertvalue, addapicasesdbassertvalue, updateapicasesdbassertvalue, removeapicasesdbassertvalue } from '@/api/assets/apicasesdbassertvalue'
  import { searchallaccount as searchallaccount } from '@/api/account'

  export default {
    name: '用例库',
    components: { uservariables },
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
        tmpdbassertvalue: {
          id: '',
          dbassertid: '',
          fieldname: '',
          roworder: '',
          valuetype: '',
          assertcondition: '',
          expectvalue: '',
          memo: '',
          creator: ''
        },
        tmpfieldname: '',
        dbassertvaluetotal: 0,
        searchdbassertvalue: {
          page: 1,
          size: 10,
          fieldname: null,
          dbassertid: null
        },
        ApicasesdbassertvalueList: [],
        dbassertvalueDialogFormVisible: false,
        dbassertvaluelistDialogFormVisible: false,
        dbAssertvaluetextMap: {
          update: '修改断言值',
          add: '添加断言值'
        },
        dbAssertvaluedialogStatus: 'add',
        tmpdbasserttype: '',
        tmpdbassertenviromrnt: '',
        tmpdbassert: {
          id: '',
          caseid: '',
          assembleid: '',
          assemblename: '',
          expectrecordsnums: null,
          envid: '',
          expression: '',
          enviroment: '',
          creator: ''
        },
        dbasserttotal: 0,
        dbAssertAUdialogFormVisible: false,
        dbAssertdialogStatus: 'add',
        dbassertList: [],
        dbassertitemKey: null,
        searchdbassert: {
          page: 1,
          size: 10,
          assemblename: null,
          enviroment: null,
          caseid: null
        },
        assertactiveName: 'zero',
        conditionorderList: [], // 条件顺序显示列表
        saveconditionorderList: [], // 条件顺序保存列表
        searchconditionorder: {
          subconditionid: '',
          conditiontype: ''
        },
        ConditionOrderdialogFormVisible: false,
        id: null,
        Headertabledatas: [],
        Paramstabledatas: [],
        Bodytabledatas: [],
        ApicasesVariablesList: [],
        apicasesList: [],
        modelList: [],
        checked: 'false',
        activeName: 'zero',
        itemKey: null,
        headercasenotexistKey: null,
        headercaseexistKey: null,
        casenotexistKey: null,
        caseexistKey: null,
        assertitemKey: null,
        tmpasserttype: null,
        tmpprotocal: null,
        // tmpdeployunitname: null,
        // tmpapiname: null,
        tmpapiid: null,
        tmppath: null,
        tmpdeployunitid: null,
        tmpcasetype: null,
        tmpcasename: null,
        tmpnotexistdeployunitid: 0,
        tmpexistdeployunitid: 0,
        tmpnotexistconditionid: 0,
        tmpexistconditionid: 0,
        tmpnotexistglobalheaderid: 0,
        tmpnotexistheaderdeployunitid: 0,
        tmpexistglobalheaderid: 0,
        tmpexistheaderdeployunitid: 0,
        globalheaderallList: [],
        paraList: [], // paraList参数值列表
        paravaluemap: [], // 参数值map
        notexistcasemultipleSelection: [], // 查询调试用例未添加条件表格被选中的内容
        existcasemultipleSelection: [], // 查询调试用例已添加条件表格被选中的内容
        headernotexistcasemultipleSelection: [], // 查询调试用例未添加条件表格被选中的内容
        headerexistcasemultipleSelection: [], // 查询调试用例已添加条件表格被选中的内容
        multipleSelection: [], // 用例表格被选中的内容
        apiList: [], // api列表
        enviromentnameList: [], // 环境列表
        dbconditionenviromentnameList: [], // 环境列表
        enviroment_assembleList: [],
        deployunitList: [], // 微服务列表
        caseparamtypelist: [], // 用例参数类型列表
        caseparamsbytypelist: [], // 根据类型获取用例参数名列表
        tmpcaseparamslist: [], // 获取用例参数临时列表，getcaseparamsbytype（）调用
        sourcetestcaseList: [],
        assertList: [],
        headerList: [], // Header列表
        cookies: [], // cookies列表
        requestHeadList: [], // Header列表
        requestParamsList: [], // Params列表
        conditionList: [], // 条件列表
        apicasesnotexistList: [], // 未添加条件调试用例列表
        apicasesheadernotexistList: [], // 未添加条件调试用例列表
        apicasesheaderexistList: [], // 已添加条件调试用例列表
        apicasesexistList: [], // 已添加条件调试用例列表
        apiconditioncaseList: [],
        conditionapicaseList: [],
        apiconditionmodelList: [],
        apiconditionapiList: [],
        dbconditioncaseList: [],
        scriptconditionList: [],
        dbvariablesList: [],
        scriptvariablesList: [],
        listLoading: false, // 数据加载等待动画
        threadloopvisible: false, // 线程，循环显示
        JmeterClassVisible: false, // JmeterClassVisible显示
        ExpressionVisible: false, // 断言表达式显示
        AssertSubVisible: false, // 断言子条件显示
        AssertdialogFormVisible: false,
        AssertAUdialogFormVisible: false,
        BatchAssertdialogFormVisible: false,
        TestdialogFormVisible: false,
        HeaderandParamsVisible: false,
        casedataialogFormVisible: false,
        CopybatchdialogFormVisible: false,
        casedebugconditionnotexistdialogFormVisible: false, // 还未添加前置条件的用例
        casedebugconditionexistdialogFormVisible: false, // 已经存在前置条件的用例
        scriptVariablesDialogFormVisible: false,
        BodyVisible: false,
        BodyParamDataVisible: false,
        BodyDataVisible: false,
        caseglobalheadernotexistdialogFormVisible: false,
        caseglobalheaderexistdialogFormVisible: false,
        caseVariablesDialogFormVisible: false,
        caseaddvariablesdialogFormVisible: false,
        scenecaseConditionFormVisible: false,
        adddbvariablesdialogFormVisible: false,
        addscriptVariablesdialogFormVisible: false,
        dbVariablesDialogFormVisible: false,
        caseindex: '',
        total: 0, // 数据总数
        asserttotal: 0, // 数据总数
        casenotexisttotal: 0, // 未添加条件用例总数
        headercasenotexisttotal: 0, // 未添加条件用例总数
        headercaseexisttotal: 0, // 已添加条件用例总数
        globalheaderctotal: 0,
        caseexisttotal: 0, // 已添加条件用例总数
        apicasevariablestotal: 0,
        dbvariablestotal: 0,
        scriptvariablestotal: 0,
        apiSearchQuery: {
          deployunitname: '', // 微服务名
          apiname: '' // api名
        },
        apiQuery: {
          deployunitid: '',
          deployunitname: '', // 获取字典表入参
          modelid: 0
        },
        dialogStatus: 'add',
        AssertdialogStatus: 'add',
        DelaydialogStatus: 'add',
        apiconditiondialogStatus: 'add',
        scriptdialogStatus: 'add',
        dbdialogStatus: 'add',
        dbvariablesdialogStatus: 'add',
        scriptVariablesdialogStatus: 'add',
        paramtitle: '用例参数值',
        AssertTitle: '新增修改断言',
        CopyApiCase: '复制用例',
        CopyBatchApiCase: '批量复制用例',
        loadassert: '断言',
        dialogFormVisible: false,
        CopydialogFormVisible: false,
        paramdialogFormVisible: false,
        caseconditiondialogFormVisible: false,
        dbconditiondialogFormVisible: false,
        DelaydialogFormVisible: false,
        scriptdialogFormVisible: false,
        caseaddvariablesdialogStatus: 'add',
        caseaddvariablestextMap: {
          update: '修改变量',
          add: '添加变量'
        },
        scripttextMap: {
          updateRole: '修改环境',
          update: '修改脚本前置条件',
          add: '添加脚本前置条件'
        },
        dbtextMap: {
          updateRole: '修改环境',
          update: '修改数据库前置条件',
          add: '添加数据库前置条件'
        },

        apiconditiontextMap: {
          update: '修改前置接口',
          add: '添加前置接口'
        },
        DelaytextMap: {
          update: '修改延时条件',
          add: '添加延时条件'
        },
        textMap: {
          updateRole: '修改API用例',
          update: '修改API用例',
          add: '添加API用例'
        },
        dbvariablestextMap: {
          updateRole: '修改API用例',
          update: '修改数据库变量',
          add: '添加数据库变量'
        },
        scriptVariablestextMap: {
          updateRole: '修改API用例',
          update: '修改脚本变量',
          add: '添加脚本变量'
        },
        AsserttextMap: {
          updateRole: '修改用例断言',
          update: '修改用例断言',
          add: '添加用例断言'
        },
        dbAsserttextMap: {
          updateRole: '修改数据库断言',
          update: '修改数据库断言',
          add: '添加数据库断言'
        },
        diclevelQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          diccode: 'casecondition' // 获取字典表入参
        },
        btnLoading: false, // 按钮等待动画
        tmpapicases: {
          id: '',
          apiid: '',
          deployunitid: '',
          casename: '',
          deployunitname: '',
          apiname: '',
          casejmxname: '',
          casecontent: '',
          casetype: '',
          threadnum: '',
          loops: '',
          expect: '',
          middleparam: '',
          level: 0,
          memo: '',
          creator: '',
          modelid: '',
          modelname: '',
          mnickname: '',
          creatorid: '',
          mid: ''
        },
        tmpmodelquery: {
          page: 1,
          size: 100,
          deployunitid: ''
        },
        tmpapicasesdata: {
          id: '',
          caseid: '',
          casename: '',
          propertytype: '',
          memo: '',
          casedataMap: [],
          keyname: ''
        },
        tmpapicasesbodydata: {
          id: '',
          caseid: '',
          casename: '',
          propertytype: '',
          memo: '',
          apiparam: '',
          apiparamvalue: '',
          paramstype: '',
          mid: ''
        },
        tmpcopycase: {
          sourcecaseid: '',
          sourcedeployunitid: '',
          sourcedeployunitname: '',
          sourcecasename: '',
          newcasename: ''
        },
        tmpbatchcopycase: {
          sourcedeployunitid: '',
          sourcedeployunitname: '',
          destinationdeployunitid: '',
          destinationdeployunitname: ''
        },
        tmpassert: {
          id: '',
          caseid: '',
          asserttype: '',
          assertsubtype: '',
          expression: '',
          assertcondition: '',
          assertvalues: '',
          assertvaluetype: '',
          creator: ''
        },
        tmpbatchassert: {
          id: '',
          caseid: '',
          asserttype: '',
          assertsubtype: '',
          expression: '',
          assertcondition: '',
          assertvalues: '',
          assertvaluetype: '',
          creator: ''
        },
        tmptest: {
          globalheadername: '',
          globalheaderid: 0,
          conditionname: '',
          conditionid: 0,
          enviromentname: '',
          respone: '',
          code: '',
          responeTime: '',
          size: '',
          general: '',
          requestdata: ''
        },
        tmpheader: {
          name: '',
          value: ''
        },
        tmptestdata: {
          conditionid: 0,
          globalheaderid: 0,
          caseid: '',
          enviromentid: '',
          prixflag: '',
          projectid: ''
        },
        tmpapi: {
          id: '',
          deployunitid: '',
          deployunitname: '',
          apiname: '',
          visittype: '',
          requesttype: '',
          apistyle: '',
          path: '',
          requestcontenttype: '',
          responecontenttype: '',
          memo: '',
          creator: ''
        },
        casevalue: {
          apiid: '',
          caseid: '',
          propertytype: ''
        },
        search: {
          page: 1,
          size: 10,
          deployunitname: null,
          modelname: null,
          apiid: '',
          deployunitid: '',
          modelid: '',
          apiname: null,
          path: null,
          casetype: null,
          casename: null,
          projectid: '',
          mid: ''
        },
        searchassert: {
          page: 1,
          size: 10,
          asserttype: null,
          caseid: null
        },
        searchnotexistcase: {
          page: 1,
          size: 10,
          conditionid: 0,
          conditionname: '',
          deployunitid: 0,
          deployunitname: ''
        },
        searchexistcase: {
          page: 1,
          size: 10,
          conditionid: 0,
          conditionname: '',
          deployunitid: 0,
          deployunitname: ''
        },
        searchglobalnotexistheadercase: {
          page: 1,
          size: 10,
          globalheaderid: 0,
          globalheadername: '',
          deployunitid: 0,
          deployunitname: ''
        },
        searchglobalheadercase: {
          page: 1,
          size: 10,
          globalheaderid: 0,
          globalheadername: '',
          deployunitid: 0,
          deployunitname: ''
        },
        tmpconditionquery: {
          objecttype: '',
          projectid: ''
        },
        tmptestvariables: {
          id: '',
          caseid: '',
          casename: '',
          testvariablesname: '',
          variablesdes: '',
          valuetype: '',
          testvariablestype: '',
          variablesexpress: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        tmpscriptcondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          script: '',
          creator: '',
          projectid: ''
        },
        tmpapicondition: {
          page: 1,
          size: 10,
          id: '',
          modelid: 0,
          modelname: '',
          conditionid: '',
          subconditionname: '',
          conditionname: '',
          deployunitid: '',
          deployunitname: '',
          apiname: '',
          apiid: '',
          caseid: '',
          casename: '',
          memo: '',
          conditiontype: '',
          creator: '',
          projectid: ''
        },
        tmpdelaycondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          conditiontype: '',
          delaytime: '',
          creator: '',
          projectid: ''
        },
        tmpdbcondition: {
          id: '',
          conditionid: '',
          conditionname: '',
          assembleid: '',
          assemblename: '',
          subconditionname: '',
          enviromentid: '',
          enviromentname: '',
          dbtype: '',
          dbcontent: '',
          connectstr: '',
          memo: '',
          conditiontype: '',
          creator: '',
          projectid: ''
        },
        tmpdbvariables: {
          id: '',
          dbvariablesname: '',
          variablesdes: '',
          valuetype: '',
          fieldname: '',
          roworder: '',
          conditionid: '',
          conditionname: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        tmpscriptvariables: {
          id: '',
          scriptvariablesname: '',
          variablesdes: '',
          valuetype: '',
          memo: '',
          creator: '',
          conditionid: '',
          conditionname: '',
          projectid: ''
        },
        searchapicondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        Scenedelaysearch: {
          page: 1,
          size: 10,
          conditionid: null,
          conditiontype: null,
          projectid: null
        },
        searchscriptcondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        searchdbcondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        searchdbvariables: {
          page: 1,
          size: 10,
          conditionid: '',
          projectid: ''
        },
        searchscriptvariables: {
          page: 1,
          size: 10,
          conditionid: '',
          projectid: ''
        },
        searchapicasevariables: {
          page: 1,
          size: 10,
          // caseid: '',
          projectid: ''
        },
        searchparam: {
          page: 1,
          size: 10,
          paramtype: ''
        },
        searchassemble: {
          page: 1,
          size: 100,
          assembletype: '组件',
          envid: ''
        }
      }
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.tmpconditionquery.projectid = window.localStorage.getItem('pid')
      this.tmptestdata.projectid = window.localStorage.getItem('pid')
      this.searchapicondition.projectid = window.localStorage.getItem('pid')
      this.searchdbcondition.projectid = window.localStorage.getItem('pid')
      this.searchscriptcondition.projectid = window.localStorage.getItem('pid')
      this.searchapicasevariables.projectid = window.localStorage.getItem('pid')
      this.Scenedelaysearch.projectid = window.localStorage.getItem('pid')
      this.getaccountLists()
      this.getenviromentallList()
      this.getapicasesList()
      this.getdepunitLists()
      this.getalltestcondition()
      this.getglobalheaderallList()
    },

    activated() {
      this.getapicasesList()
      this.getdepunitLists()
      this.getalltestcondition()
      this.getglobalheaderallList()
      this.getenviromentallList()
    },

    computed: {
      ...mapGetters(['name', 'nickname', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    methods: {
      unix2CurrentTime,
      mnicknameselectChanged(e) {
        for (let i = 0; i < this.accountList.length; i++) {
          if (this.accountList[i].nickname === e) {
            this.tmpapicases.mid = this.accountList[i].id
          }
        }
      },

      getaccountLists() {
        this.accountList = null
        searchallaccount().then(response => {
          this.accountList = response.data
        }).catch(res => {
          this.$message.error('加载服务列表失败')
        })
      },

      showapicasesdbassertvalueDialog(index) {
        // 显示新增对话框
        this.tmpdbassertvalue.dbassertid = this.dbassertList[index].id
        this.dbassertvaluelistDialogFormVisible = true
        this.searchdbassertvalue.dbassertid = this.dbassertList[index].id
        this.getdbassertvaluebyid()
      },
      showconditionorderDialog() {
        // 显示新增对话框
        this.ConditionOrderdialogFormVisible = true
        this.searchconditionorder.subconditionid = this.tmptestdata.caseid
        this.searchconditionorder.conditiontype = 'case'
        this.searchConditionorder()
      },

      searchConditionorder() {
        searchconditionorder(this.searchconditionorder).then(response => {
          this.conditionorderList = response.data
        }).catch(res => {
          this.$message.error('查询条件顺序失败')
        })
      },
      /**
       * 添加条件顺序
       */
      saveconditionorder() {
        this.saveconditionorderList = []
        for (let i = 0; i < this.conditionorderList.length; i++) {
          this.saveconditionorderList.push({
            'id': this.conditionorderList[i].id,
            'conditionid': this.conditionorderList[i].conditionid,
            'subconditionid': this.conditionorderList[i].subconditionid,
            'subconditiontype': this.conditionorderList[i].subconditiontype,
            'orderstatus': '已排序',
            'subconditionname': this.conditionorderList[i].subconditionname,
            'conditionname': this.conditionorderList[i].conditionname,
            'conditionorder': i + 1,
            'creator': this.name
          })
        }
        console.log(this.saveconditionorderList)
        addconditionorder(this.saveconditionorderList).then(() => {
          this.$message.success('条件顺序保存成功')
        }).catch(res => {
          this.$message.error('条件顺序保存失败')
        })
        this.ConditionOrderdialogFormVisible = false
      },

      // 上移
      moveUp(index, row) {
        var that = this
        if (index > 0) {
          const upDate = that.conditionorderList[index - 1]
          that.conditionorderList.splice(index - 1, 1)
          that.conditionorderList.splice(index, 0, upDate)
        } else {
          alert('已经是第一条，不可上移')
        }
        console.log(that.conditionorderList)
      },

      // 下移
      moveDown(index, row) {
        var that = this
        console.log('下移', index, row)
        if ((index + 1) === that.conditionorderList.length) {
          alert('已经是最后一条，不可下移')
        } else {
          console.log(index)
          const downDate = that.conditionorderList[index + 1]
          that.conditionorderList.splice(index + 1, 1)
          that.conditionorderList.splice(index, 0, downDate)
        }
      },

      removedbvariables(index) {
        this.$confirm('删除该变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dbvariablesList[index].id
          removedbvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getdbvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removescriptvariables(index) {
        this.$confirm('删除该变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scriptvariablesList[index].id
          removescriptvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getscriptvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      showUpdatedbvariablesDialog(index) {
        this.adddbvariablesdialogFormVisible = true
        this.dbvariablesdialogStatus = 'update'
        this.tmpdbvariables.id = this.dbvariablesList[index].id
        this.tmpdbvariables.dbvariablesname = this.dbvariablesList[index].dbvariablesname
        this.tmpdbvariables.variablesdes = this.dbvariablesList[index].variablesdes
        this.tmpdbvariables.valuetype = this.dbvariablesList[index].valuetype
        this.tmpdbvariables.roworder = this.dbvariablesList[index].roworder
        this.tmpdbvariables.fieldname = this.dbvariablesList[index].fieldname
        this.tmpdbvariables.memo = this.dbvariablesList[index].memo
        this.tmpdbvariables.creator = this.name
      },

      showUpdatescriptvariablesDialog(index) {
        this.addscriptVariablesdialogFormVisible = true
        this.scriptVariablesdialogStatus = 'update'
        this.tmpscriptvariables.id = this.scriptvariablesList[index].id
        this.tmpscriptvariables.scriptvariablesname = this.scriptvariablesList[index].scriptvariablesname
        this.tmpscriptvariables.variablesdes = this.scriptvariablesList[index].variablesdes
        this.tmpscriptvariables.valuetype = this.scriptvariablesList[index].valuetype
        this.tmpscriptvariables.memo = this.scriptvariablesList[index].memo
        this.tmpscriptvariables.creator = this.name
      },
      /**
       * 更新变量
       */
      updatedbvariables() {
        this.$refs.tmpdbvariables.validate(valid => {
          if (valid) {
            updatedbvariables(this.tmpdbvariables).then(() => {
              this.$message.success('更新成功')
              this.getdbvariablesList()
              this.adddbvariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      updatescriptvariables() {
        this.$refs.tmpscriptvariables.validate(valid => {
          if (valid) {
            updatescriptvariables(this.tmpscriptvariables).then(() => {
              this.$message.success('更新成功')
              this.getscriptvariablesList()
              this.addscriptVariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      getdbvariablesList() {
        searchdbvariables(this.searchdbvariables).then(response => {
          this.dbvariablesList = response.data.list
          this.dbvariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },

      getscriptvariablesList() {
        searchscriptvariables(this.searchscriptvariables).then(response => {
          this.scriptvariablesList = response.data.list
          this.scriptvariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },
      showdbvariablesDialog(index) {
        // 显示新增对话框
        this.dbVariablesDialogFormVisible = true
        this.tmpdbvariables.conditionid = this.dbconditioncaseList[index].id
        this.searchdbvariables.conditionid = this.dbconditioncaseList[index].id
        this.tmpdbvariables.conditionname = this.dbconditioncaseList[index].subconditionname
        this.getdbvariablesList()
      },

      showscriptvariablesDialog(index) {
        // 显示新增对话框
        this.scriptVariablesDialogFormVisible = true
        this.tmpscriptvariables.conditionid = this.scriptconditionList[index].id
        this.searchscriptvariables.conditionid = this.scriptconditionList[index].id
        this.tmpscriptvariables.conditionname = this.scriptconditionList[index].subconditionname
        this.getscriptvariablesList()
      },
      showAdddbvariablesDialog() {
        // 显示新增对话框
        this.adddbvariablesdialogFormVisible = true
        this.dbvariablesdialogStatus = 'add'
        this.tmpdbvariables.id = ''
        this.tmpdbvariables.dbvariablesname = ''
        this.tmpdbvariables.variablesdes = ''
        this.tmpdbvariables.roworder = ''
        this.tmpdbvariables.fieldname = ''
        this.tmpdbvariables.memo = ''
        this.tmpdbvariables.valuetype = ''
        this.tmpdbvariables.creator = this.name
        this.tmpdbvariables.projectid = window.localStorage.getItem('pid')
      },

      showAddscriptvariablesDialog() {
        // 显示新增对话框
        this.addscriptVariablesdialogFormVisible = true
        this.scriptVariablesdialogStatus = 'add'
        this.tmpscriptvariables.id = ''
        this.tmpscriptvariables.scriptvariablesname = ''
        this.tmpscriptvariables.variablesdes = ''
        this.tmpscriptvariables.valuetype = ''
        this.tmpscriptvariables.memo = ''
        this.tmpscriptvariables.creator = this.name
        this.tmpscriptvariables.projectid = window.localStorage.getItem('pid')
      },

      adddbvariables() {
        this.$refs.tmpdbvariables.validate(valid => {
          if (valid) {
            adddbvariables(this.tmpdbvariables).then(() => {
              this.$message.success('添加成功')
              this.getdbvariablesList()
              this.adddbvariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      addscriptvariables() {
        this.$refs.tmpscriptvariables.validate(valid => {
          if (valid) {
            addscriptvariables(this.tmpscriptvariables).then(() => {
              this.$message.success('添加成功')
              this.getscriptvariablesList()
              this.addscriptVariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      showUpdatescriptconditionDialog(index) {
        this.scriptdialogFormVisible = true
        this.scriptdialogStatus = 'update'
        this.tmpscriptcondition.id = this.scriptconditionList[index].id
        this.tmpscriptcondition.subconditionname = this.scriptconditionList[index].subconditionname
        this.tmpscriptcondition.conditionname = this.scriptconditionList[index].conditionname
        this.tmpscriptcondition.script = this.scriptconditionList[index].script
        this.tmpscriptcondition.creator = this.name
      },

      removedbcondition(index) {
        this.$confirm('删除该数据库条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dbconditioncaseList[index].id
          removedbcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getdbconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      updatedbcondition() {
        this.$refs.tmpdbcondition.validate(valid => {
          if (valid) {
            updatedbcondition(this.tmpdbcondition).then(() => {
              this.$message.success('更新成功')
              this.getdbconditionList()
              this.dbconditiondialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      showUpdatedbconditionDialog(index) {
        this.dbconditiondialogFormVisible = true
        this.dbdialogStatus = 'update'
        this.tmpdbcondition.id = this.dbconditioncaseList[index].id
        this.tmpdbcondition.conditionid = this.dbconditioncaseList[index].conditionid
        this.tmpdbcondition.assembleid = this.dbconditioncaseList[index].assembleid
        this.tmpdbcondition.enviromentid = this.dbconditioncaseList[index].enviromentid
        this.tmpdbcondition.enviromentname = this.dbconditioncaseList[index].enviromentname
        this.tmpdbcondition.assemblename = this.dbconditioncaseList[index].assemblename
        this.tmpdbcondition.conditionname = this.dbconditioncaseList[index].conditionname
        this.tmpdbcondition.subconditionname = this.dbconditioncaseList[index].subconditionname
        this.tmpdbcondition.dbtype = this.dbconditioncaseList[index].dbtype
        this.tmpdbcondition.dbcontent = this.dbconditioncaseList[index].dbcontent
        this.tmpdbcondition.creator = this.name
      },

      getassembleallnameList() {
        getassembleallnameList(this.searchdbcondition).then(response => {
          this.enviroment_assembleList = response.data
        }).catch(res => {
          this.$message.error('获取组件列表失败')
        })
      },

      findMacAndDepWithEnv() {
        findMacAndDepWithEnv(this.searchassemble).then(response => {
          this.enviroment_assembleList = response.data.list
        }).catch(res => {
          this.$message.error('获取组件列表失败')
        })
      },

      selectChangedEN(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpdbcondition.enviromentid = this.enviromentnameList[i].id
            this.searchassemble.envid = this.enviromentnameList[i].id
            this.tmpdbcondition.assembleid = null
            this.tmpdbcondition.assemblename = null
            this.findMacAndDepWithEnv()
          }
        }
      },

      ConditionselectChangedAS(e) {
        for (let i = 0; i < this.enviroment_assembleList.length; i++) {
          if (this.enviroment_assembleList[i].deployunitname === e) {
            this.tmpdbcondition.assembleid = this.enviroment_assembleList[i].assembleid
          }
        }
      },
      dbassertselectChangedAS(e) {
        for (let i = 0; i < this.enviroment_assembleList.length; i++) {
          if (this.enviroment_assembleList[i].deployunitname === e) {
            this.tmpdbassert.assembleid = this.enviroment_assembleList[i].assembleid
          }
        }
      },
      apiconditioncaseIndex(index) {
        return (this.searchapicondition.page - 1) * this.searchapicondition.size + index + 1
      },
      dbconditioncaseIndex(index) {
        return (this.searchdbcondition.page - 1) * this.searchdbcondition.size + index + 1
      },

      scriptconditioncaseIndex(index) {
        return (this.searchscriptcondition.page - 1) * this.searchscriptcondition.size + index + 1
      },

      apiconditiontestcaseselectChanged(e) {
        for (let i = 0; i < this.conditionapicaseList.length; i++) {
          if (this.conditionapicaseList[i].casename === e) {
            this.tmpapicondition.caseid = this.conditionapicaseList[i].id
          }
        }
      },

      apiconditionapiselectChanged(e) {
        this.tmpapicondition.casename = null
        for (let i = 0; i < this.apiconditionapiList.length; i++) {
          if (this.apiconditionapiList[i].apiname === e) {
            this.tmpapicondition.apiid = this.apiconditionapiList[i].id
          }
        }
        findcasesbyname(this.tmpapicondition).then(response => {
          this.conditionapicaseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },

      apiconditionmodelselectChanged(e) {
        this.tmpapicondition.apiname = null
        this.tmpapicondition.casename = null
        for (let i = 0; i < this.apiconditionmodelList.length; i++) {
          if (this.apiconditionmodelList[i].modelname === e) {
            this.tmpapicondition.modelid = this.apiconditionmodelList[i].id
          }
        }
        if (e === '') {
          this.tmpapicondition.modelid = 0
          this.tmpapicondition.modelname = ''
        }
        getapiListbydeploy(this.tmpapicondition).then(response => {
          this.apiconditionapiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      apiconditiondeployunitselectChanged(e) {
        this.tmpapicondition.casename = null
        this.tmpapicondition.apiname = null
        this.tmpapicondition.modelname = null
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpapicondition.deployunitid = this.deployunitList[i].id
          }
        }
        searchdeployunitmodel(this.tmpapicondition).then(response => {
          this.apiconditionmodelList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
        this.apiconditionapiList = null
        getapiListbydeploy(this.tmpapicondition).then(response => {
          this.apiconditionapiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      updatedelaycondition() {
        this.$refs.tmpdelaycondition.validate(valid => {
          if (valid) {
            updatedelaycondition(this.tmpdelaycondition).then(() => {
              this.$message.success('更新成功')
              this.getdelayconditionList()
              this.DelaydialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      showUpdateapiconditionDialog(index) {
        this.caseconditiondialogFormVisible = true
        this.apiconditiondialogStatus = 'update'
        this.tmpapicondition.id = this.apiconditioncaseList[index].id
        this.tmpapicondition.subconditionname = this.apiconditioncaseList[index].subconditionname
        this.tmpapicondition.deployunitname = this.apiconditioncaseList[index].deployunitname
        this.tmpapicondition.apiname = this.apiconditioncaseList[index].apiname
        this.tmpapicondition.casename = this.apiconditioncaseList[index].casename
        this.tmpapicondition.modelname = this.apiconditioncaseList[index].modelname
        this.tmpapicondition.memo = this.apiconditioncaseList[index].memo
        this.tmpapicondition.creator = this.name
        this.tmpapicondition.projectid = window.localStorage.getItem('pid')

        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === this.apiconditioncaseList[index].deployunitname) {
            this.tmpapicondition.deployunitid = this.deployunitList[i].id
          }
        }
        searchdeployunitmodel(this.tmpapicondition).then(response => {
          this.apiconditionmodelList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
        this.apiconditionapiList = null
        getapiListbydeploy(this.tmpapicondition).then(response => {
          this.apiconditionapiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
        for (let i = 0; i < this.apiconditionapiList.length; i++) {
          if (this.apiconditionapiList[i].apiname === this.apiconditioncaseList[index].apiname) {
            this.tmpapicondition.apiid = this.apiconditionapiList[i].id
          }
        }
        findcasesbyname(this.tmpapicondition).then(response => {
          this.conditionapicaseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },

      updateapicondition() {
        this.$refs.tmpapicondition.validate(valid => {
          if (valid) {
            updateapicondition(this.tmpapicondition).then(() => {
              this.$message.success('更新成功')
              this.getapiconditionList()
              this.caseconditiondialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      removedelaycondition(index) {
        this.$confirm('删除该延时条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.delayconditionList[index].id
          removedelaycondition(id).then(() => {
            this.$message.success('删除成功')
            this.getdelayconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removescriptcondition(index) {
        this.$confirm('删除该脚本条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scriptconditionList[index].id
          removescriptcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getscriptconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removecaseapicondition(index) {
        this.$confirm('删除该前置条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.apiconditioncaseList[index].id
          removeapicondition(id).then(() => {
            this.$message.success('删除成功')
            this.getapiconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      adddelaycondition() {
        this.$refs.tmpdelaycondition.validate(valid => {
          if (valid) {
            adddelaycondition(this.tmpdelaycondition).then(() => {
              this.$message.success('添加成功')
              this.getdelayconditionList()
              this.DelaydialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      addapicondition() {
        this.$refs.tmpapicondition.validate(valid => {
          if (valid) {
            addapicondition(this.tmpapicondition).then(() => {
              this.$message.success('添加成功')
              this.caseconditiondialogFormVisible = false
              this.getapiconditionList()
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      adddbcondition() {
        this.$refs.tmpdbcondition.validate(valid => {
          if (valid) {
            adddbcondition(this.tmpdbcondition).then(() => {
              this.$message.success('添加成功')
              this.dbconditiondialogFormVisible = false
              this.getdbconditionList()
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      addscriptcondition() {
        this.$refs.tmpscriptcondition.validate(valid => {
          if (valid) {
            addscriptcondition(this.tmpscriptcondition).then(() => {
              this.$message.success('添加成功')
              this.getscriptconditionList()
              this.scriptdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      updatescriptcondition() {
        this.$refs.tmpscriptcondition.validate(valid => {
          if (valid) {
            updatescriptcondition(this.tmpscriptcondition).then(() => {
              this.$message.success('更新成功')
              this.getscriptconditionList()
              this.scriptdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      showAddscriptDialog() {
        // 显示新增对话框
        this.scriptdialogFormVisible = true
        this.scriptdialogStatus = 'add'
        this.tmpscriptcondition.id = ''
        this.tmpscriptcondition.subconditionname = ''
        this.tmpscriptcondition.script = ''
        this.tmpscriptcondition.creator = this.name
        this.tmpscriptcondition.projectid = window.localStorage.getItem('pid')
      },

      AddcasedbconditionDialog(index) {
        this.dbconditiondialogFormVisible = true
        this.dbdialogStatus = 'add'
        this.tmpdbcondition.id = ''
        this.tmpdbcondition.enviromentid = ''
        this.tmpdbcondition.enviromentname = ''
        this.tmpdbcondition.assembleid = ''
        this.tmpdbcondition.assemblename = ''
        this.tmpdbcondition.subconditionname = ''
        this.tmpdbcondition.dbtype = ''
        this.tmpdbcondition.dbcontent = ''
        this.tmpdbcondition.creator = this.name
        this.tmpdbcondition.projectid = window.localStorage.getItem('pid')
      },

      showAddSceneCasedelayconditionDialog() {
        // 显示新增对话框
        this.DelaydialogFormVisible = true
        this.DelaydialogStatus = 'add'
        this.tmpdelaycondition.id = ''
        this.tmpdelaycondition.subconditionname = ''
        this.tmpdelaycondition.conditionid = this.tmpapicondition.conditionid
        this.tmpdelaycondition.conditionname = this.tmpapicondition.conditionname
        this.tmpdelaycondition.conditiontype = 'scencecase'
        this.tmpdelaycondition.delaytime = ''
        this.tmpdelaycondition.creator = this.name
        this.tmpdelaycondition.projectid = window.localStorage.getItem('pid')
      },

      ShowAddcasecaseconditionDialog(index) {
        this.caseconditiondialogFormVisible = true
        this.apiconditiondialogStatus = 'add'
        this.tmpapicondition.id = ''
        this.tmpapicondition.subconditionname = ''
        this.tmpapicondition.deployunitname = ''
        this.tmpapicondition.apiname = ''
        this.tmpapicondition.modelname = ''
        this.tmpapicondition.casename = ''
        this.tmpapicondition.memo = ''
        this.tmpapicondition.creator = this.name
        this.tmpapicondition.projectid = window.localStorage.getItem('pid')
      },

      getdbconditionList() {
        searchdbcondition(this.searchdbcondition).then(response => {
          this.dbconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试数据库条件列表失败')
        })
      },

      getapiconditionList() {
        searchapicondition(this.searchapicondition).then(response => {
          this.apiconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试接口条件列表失败')
        })
      },

      getdelayconditionList() {
        this.Scenedelaysearch.conditiontype = 'case'
        searchbytype(this.Scenedelaysearch).then(response => {
          this.delayconditionList = response.data
        }).catch(res => {
          this.$message.error('加载测试延时条件列表失败')
        })
      },

      getscriptconditionList() {
        getscriptconditionList(this.searchscriptcondition).then(response => {
          this.scriptconditionList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试脚本条件列表失败')
        })
      },

      showtestcaseConditionDialog() {
        this.scenecaseConditionFormVisible = true
        this.getapiconditionList()
        this.getdelayconditionList()
        this.getdbconditionList()
        this.getscriptconditionList()
      },

      creatorselectChanged(e) {
        if (e === '全部') {
          this.search.mid = null
        } else {
          this.search.mid = this.accountId
        }
      },
      /**
       * 删除变量
       * @param index 变量下标
       */
      removetestvariables(index) {
        this.$confirm('删除该变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.ApicasesVariablesList[index].id
          removetestvariables(id).then(() => {
            this.$message.success('删除成功')
            this.findtestvariablesbycaseid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      updatetestvariables() {
        this.$refs.tmptestvariables.validate(valid => {
          if (valid) {
            updatetestvariables(this.tmptestvariables).then(() => {
              this.$message.success('更新成功')
              this.findtestvariablesbycaseid()
              this.caseaddvariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 显示修改变量对话框
       * @param index 变量下标
       */
      showUpdatetestvariablesDialog(index) {
        this.caseaddvariablesdialogFormVisible = true
        this.caseaddvariablesdialogStatus = 'update'
        this.tmptestvariables.id = this.ApicasesVariablesList[index].id
        this.tmptestvariables.testvariablesname = this.ApicasesVariablesList[index].testvariablesname
        this.tmptestvariables.variablesdes = this.ApicasesVariablesList[index].variablesdes
        this.tmptestvariables.testvariablestype = this.ApicasesVariablesList[index].testvariablestype
        this.tmptestvariables.variablesexpress = this.ApicasesVariablesList[index].variablesexpress
        this.tmptestvariables.tmptestvariables = this.ApicasesVariablesList[index].tmptestvariables
        this.tmptestvariables.valuetype = this.ApicasesVariablesList[index].valuetype
        this.tmptestvariables.memo = this.ApicasesVariablesList[index].memo
        this.tmptestvariables.creator = this.name
      },

      showAddApicasesVariablesDialog(index) {
        // 显示新增对话框
        this.caseaddvariablesdialogFormVisible = true
        this.caseaddvariablesdialogStatus = 'add'
        this.tmptestvariables.id = ''
        this.tmptestvariables.testvariablesname = ''
        this.tmptestvariables.variablesdes = ''
        this.tmptestvariables.testvariablestype = ''
        this.tmptestvariables.variablesexpress = ''
        this.tmptestvariables.memo = ''
        this.tmptestvariables.valuetype = ''
        this.tmptestvariables.tmptestvariables = ''
        this.tmptestvariables.creator = this.name
        this.tmptestvariables.projectid = window.localStorage.getItem('pid')
      },

      /**
       * 添加变量
       */
      addtestvariables() {
        this.$refs.tmptestvariables.validate(valid => {
          if (valid) {
            addtestvariables(this.tmptestvariables).then(() => {
              this.$message.success('添加成功')
              this.caseaddvariablesdialogFormVisible = false
              this.findtestvariablesbycaseid()
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      findtestvariablesbycaseid() {
        findtestvariablesbycaseid(this.searchapicasevariables).then(response => {
          this.ApicasesVariablesList = response.data.list
          this.apicasevariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('获取用例变量列表失败')
        })
      },

      handleClick(tab, event) {
      },

      change() {
        this.$forceUpdate()
      },

      removebatchapicase() {
        this.$confirm('删除所选的用例？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          if (this.multipleSelection.length === 0) {
            this.$message.error('请选择需要删除的用例')
          } else {
            console.log(this.multipleSelection)
            removebatchapicase(this.multipleSelection).then(() => {
              this.$message.success('批量删除成功')
              this.getapicasesList()
            }).catch(res => {
              this.$message.error('批量删除失败')
            })
          }
        }).catch(() => {
          this.$message.info('批量删除异常')
        })
      },
      /**
       * 获取Header参数
       */
      getheaderdatabycaseidandtype() {
        var casedata = { caseid: this.tmpapicases.id, propertytype: 'Header' }
        getparamvaluebycaseidandtype(casedata).then(response => {
          this.Headertabledatas = response.data.list
        }).catch(res => {
          this.$message.error('获取Header参数值失败')
        })
      },

      /**
       * 获取Params参数值
       */
      getparamdatabycaseidandtype(property) {
        var casedata = { caseid: this.tmpapicases.id, propertytype: 'Params' }
        getparamvaluebycaseidandtype(casedata).then(response => {
          this.Paramstabledatas = response.data.list
        }).catch(res => {
          this.$message.error('获取Params参数值失败')
        })
      },

      /**
       * 获取Body参数值
       */
      getbodydatabycaseidandtype(property) {
        var casedata = { caseid: this.tmpapicases.id, propertytype: 'Body' }
        getparamvaluebycaseidandtype(casedata).then(response => {
          this.Bodytabledatas = response.data.list
        }).catch(res => {
          this.$message.error('获取Body参数值失败')
        })
      },

      /**
       * 获取Body-文本参数值
       */
      getbodytextdatabycaseidandtype() {
        var casedata = { caseid: this.tmpapicases.id, propertytype: 'Body' }
        getparamvaluebycaseidandtype(casedata).then(response => {
          console.log(response.data.list)
          console.log(33333333333)
          if (response.data.list.length > 0) {
            this.tmpapicasesbodydata = response.data.list[0]
            console.log(1111111)
            console.log(this.tmpapicasesbodydata)
          } else {
            console.log(2222)
            this.tmpapicasesbodydata.id = ''
            this.tmpapicasesbodydata.apiparamvalue = ''
          }
        }).catch(res => {
          this.$message.error('获取Body文本参数值失败')
        })
      },
      handleClickTableRow(row, event, column) {
        // this.$refs.fileTable.toggleRowSelection(row)
      },
      handleSelectionChange(rows) {
        this.multipleSelection = rows
        console.log(this.multipleSelection)
      },
      runprexchange(e) {
        this.checked = e
        this.tmptestdata.prixflag = e
      },
      /**
       * 功能性能选择  e的值为options的选值
       */
      funorperformChanged(e) {
        if (e === '性能') {
          this.threadloopvisible = true
          this.tmpapicases.threadnum = ''
          this.tmpapicases.loops = ''
        } else {
          this.threadloopvisible = false
          this.tmpapicases.threadnum = 1
          this.tmpapicases.loops = 1
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectparamsChanged(e) {
        this.getcaseparamsbytype(e)
      },

      EnviromentselectChanged(e) {
        this.tmptest.respone = ''
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmptestdata.enviromentid = this.enviromentnameList[i].id
            this.tmpdbassert.envid = this.enviromentnameList[i].id
          }
        }
      },

      dbassertEnviromentselectChanged(e) {
        this.searchdbassert.assemblename = null
        this.tmpdbassert.assemblename = null
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpdbassert.envid = this.enviromentnameList[i].id
            this.searchassemble.envid = this.enviromentnameList[i].id
          }
        }
        this.findMacAndDepWithEnv()
      },

      asserttypeselectChanged(e) {
        if (e === 'Respone') {
          this.ExpressionVisible = false
          this.AssertSubVisible = true
        } else {
          this.AssertSubVisible = false
          this.ExpressionVisible = true
        }
        this.tmpassert.assertsubtype = ''
        this.tmpassert.assertcondition = ''
        this.tmpassert.expression = ''
        this.tmpassert.assertvalues = ''
      },
      /**
       * 参数类型下拉框的值为e,来获取参数值
       */
      getcaseparamsbytype(e) {
        if (e === 'Body') {
          console.log('Body的数据，如果没有用例值，则从参数中获取，如果有，则永远取用例中的数据')
          this.HeaderandParamsVisible = false
          this.BodyVisible = true
          this.casevalue.apiid = this.apicasesList[this.caseindex].apiid
          this.casevalue.caseid = this.apicasesList[this.caseindex].id
          this.casevalue.propertytype = e
          casevalueforbody(this.casevalue).then(response => {
            this.tmpapicasesdata.keyname = response.data
          }).catch(res => {
            this.$message.error()
          })
        } else {
          this.HeaderandParamsVisible = true
          this.BodyVisible = false
          this.tmpcaseparamslist = null
          // this.paraList = null
          this.paravaluemap === null
          for (let i = 0; i < this.caseparamsbytypelist.length; i++) {
            if (this.caseparamsbytypelist[i].propertytype === e) {
              this.tmpcaseparamslist = this.caseparamsbytypelist[i].keyname.split(',')
              // todo 根据参数类型获取已存在的数据，用例id，参数类型
              this.casevalue.caseid = this.apicasesList[this.caseindex].id
              this.casevalue.propertytype = e
              getparamvaluebycaseidandtype(this.casevalue).then(response => {
                this.paraList = []
                this.paravaluemap = new Map()
                for (let j = 0; j < response.data.list.length; j++) {
                  this.paravaluemap.set(response.data.list[j].apiparam, response.data.list[j].apiparamvalue)
                }
                for (let k = 0; k < this.tmpcaseparamslist.length; k++) {
                  if (this.paravaluemap.has(this.tmpcaseparamslist[k])) {
                    this.paraList.push(this.paravaluemap.get(this.tmpcaseparamslist[k]))
                  } else {
                    this.paraList.push('')
                  }
                  console.log(this.paraList)
                }
                if (this.paraList === null) {
                  this.paraList = new Array(this.tmpcaseparamslist.length)
                }
              }).catch(res => {
                this.$message.error()
              })
            }
          }
        }
      },
      /**
       * 获取环境列表
       */
      getenviromentallList() {
        getenviromentallList(this.search).then(response => {
          this.enviromentnameList = response.data
        }).catch(res => {
          this.$message.error('加载环境列表失败')
        })
      },
      getglobalheaderallList() {
        getglobalheaderallList(this.search).then(response => {
          this.globalheaderallList = response.data
        }).catch(res => {
          this.$message.error('加载全局Header列表失败')
        })
      },
      /**
       * 获取用例列表
       */
      getapicasesList() {
        this.listLoading = true
        this.search.deployunitid = this.tmpdeployunitid
        this.search.apiid = this.tmpapiid
        this.search.casetype = this.tmpcasetype
        this.search.casename = this.tmpcasename
        this.search.path = this.tmppath
        search(this.search).then(response => {
          this.apicasesList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载用例列表失败')
        })
      },

      getassertbycaseid() {
        getassertbycaseid(this.searchassert).then(response => {
          this.assertList = response.data.list
          this.asserttotal = response.data.total
        }).catch(res => {
          this.$message.error('加载用例断言列表失败')
        })
      },

      getdbassertbycaseid() {
        this.searchdbassert.enviroment = this.tmpdbassertenviromrnt
        this.searchdbassert.assemblename = this.tmpdbasserttype
        searchdbassert(this.searchdbassert).then(response => {
          this.dbassertList = response.data.list
          this.dbasserttotal = response.data.total
        }).catch(res => {
          this.$message.error('加载用例断言列表失败')
        })
      },

      getdbassertvaluebyid() {
        this.searchdbassertvalue.fieldname = this.tmpfieldname
        searchdbassertvalue(this.searchdbassertvalue).then(response => {
          this.ApicasesdbassertvalueList = response.data.list
          this.dbassertvaluetotal = response.data.total
        }).catch(res => {
          this.$message.error('加载用例断言列表失败')
        })
      },

      searchdeployunitmodel() {
        searchdeployunitmodel(this.tmpmodelquery).then(response => {
          this.modelList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
      },
      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChanged(e) {
        this.apiList = []
        this.tmpapicases.apiname = ''
        this.apiQuery.deployunitname = e
        this.apiQuery.modelid = 0
        this.tmpapicases.modelname = ''
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpapicases.deployunitid = this.deployunitList[i].id
            this.apiQuery.deployunitid = this.deployunitList[i].id
            this.tmpmodelquery.deployunitid = this.deployunitList[i].id
          }
        }
        this.searchdeployunitmodel()
        getapiListbydeploy(this.apiQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
        // 获取微服务对应的协议，是http或者https则不需要显示JmeterClass，其余显示
        findDeployNameValueWithCode(this.apiQuery).then(response => {
          this.tmpprotocal = response.data.protocal
          if (this.tmpprotocal === 'http' || this.tmpprotocal === 'https') {
            this.JmeterClassVisible = false
          } else {
            this.JmeterClassVisible = true
            this.tmpapicases.casejmxname = ''
          }
        }).catch(res => {
          this.$message.error('加载微服务信息失败')
        })
      },
      modelselectChanged(e) {
        for (let i = 0; i < this.modelList.length; i++) {
          if (this.modelList[i].modelname === e) {
            this.apiQuery.modelid = this.modelList[i].id
            this.tmpapicases.modelid = this.modelList[i].id
          }
        }
        getapiListbydeploy(this.apiQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },
      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值,获取用例
       */
      CopyCasesSourceDeployselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpcopycase.sourcedeployunitid = this.deployunitList[i].id
          }
        }
        getcasebydeployunitid(this.tmpcopycase).then(response => {
          this.sourcetestcaseList = response.data
        }).catch(res => {
          this.$message.error('根据微服务id获取用例列表失败')
        })
      },

      /**
       * 批量复制用例微服务下拉选择事件获取微服务id  e的值为options的选值,获取用例
       */
      CopyBatchCasesSourceDeployselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpbatchcopycase.sourcedeployunitid = this.deployunitList[i].id
          }
        }
      },

      /**
       * 批量复制用例微服务下拉选择事件获取微服务id  e的值为options的选值,获取用例
       */
      CopyBatchDesiCasesSourceDeployselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpbatchcopycase.destinationdeployunitid = this.deployunitList[i].id
          }
        }
      },
      /**
       * 源用例下拉选择事件获取用例id  e的值为options
       */
      CopySourceCasesChanged(e) {
        for (let i = 0; i < this.sourcetestcaseList.length; i++) {
          if (this.sourcetestcaseList[i].casename === e) {
            this.tmpcopycase.sourcecaseid = this.sourcetestcaseList[i].id
          }
        }
      },

      /**
       * api下拉选择事件获取apiid  e的值为options的选值
       */
      apiselectChanged(e) {
        this.apiSearchQuery.apiname = e
        for (let i = 0; i < this.apiList.length; i++) {
          if (this.apiList[i].apiname === e) {
            console.log(this.tmpapicases.apiid)
            this.tmpapicases.apiid = this.apiList[i].id
          }
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      deployunitselectChanged(e) {
        this.apiList = null
        this.search.deployunitid = ''
        this.search.modelid = ''
        this.search.modelname = ''
        this.search.apiname = ''
        this.search.apiid = ''
        this.search.casetype = ''
        this.apiQuery.deployunitid = 0
        this.apiQuery.deployunitname = e
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpapicases.deployunitid = this.deployunitList[i].id
            this.apiQuery.deployunitid = this.deployunitList[i].id
            this.search.deployunitid = this.deployunitList[i].id
            this.tmpmodelquery.deployunitid = this.deployunitList[i].id
          }
        }
        this.searchdeployunitmodel()
      },

      searchmodelselectChanged(e) {
        this.search.modelid = 0
        for (let i = 0; i < this.modelList.length; i++) {
          if (this.modelList[i].modelname === e) {
            this.search.modelid = this.modelList[i].id
          }
        }
        getapiListbydeploy(this.search).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      searchapiselectChanged(e) {
        this.search.apiid = ''
        console.log(e)
        for (let i = 0; i < this.apiList.length; i++) {
          if (this.apiList[i].apiname === e) {
            this.search.apiid = this.apiList[i].id
            console.log(this.search.apiid)
          }
        }
      },
      /**
       * 获取微服务列表
       */
      getdepunitLists() {
        getdepunitLists(this.search).then(response => {
          this.deployunitList = response.data
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.apicasesList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpdeployunitid = this.search.deployunitid
        this.tmpapiid = this.search.apiid
        this.tmpcasetype = this.search.casetype
        this.tmpcasename = this.search.casename
        this.tmppath = this.search.path
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.search.page = 1
        this.search.size = size
        this.getapicasesList()
      },

      apicasevariableshandleSizeChange(size) {
        this.searchapicasevariables.page = 1
        this.searchapicasevariables.size = size
        this.findtestvariablesbycaseid()
      },

      dbvariableshandleSizeChange(size) {
        this.searchdbvariables.page = 1
        this.searchdbvariables.size = size
        this.getdbvariablesList()
      },

      scriptvariableshandleSizeChange(size) {
        this.searchscriptvariables.page = 1
        this.searchscriptvariables.size = size
        this.getscriptvariablesList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getapicasesList()
      },

      apicasevariableshandleCurrentChange(page) {
        this.searchapicasevariables.page = page
        this.findtestvariablesbycaseid()
      },

      dbvariableshandleCurrentChange(page) {
        this.searchdbvariables.page = page
        this.getdbvariablesList()
      },

      scriptvariableshandleCurrentChange(page) {
        this.searchscriptvariables.page = page
        this.getscriptvariablesList()
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

      scriptvariablesgetIndex(index) {
        return (this.searchscriptvariables.page - 1) * this.searchscriptvariables.size + index + 1
      },

      ApicasesVariablesgetIndex(index) {
        return (this.searchapicasevariables.page - 1) * this.searchapicasevariables.size + index + 1
      },

      dbVariablesgetIndex(index) {
        return (this.searchdbvariables.page - 1) * this.searchdbvariables.size + index + 1
      },

      searchassertBy() {
        this.searchassert.page = 1
        this.searchassert.caseid = this.tmpassert.caseid
        searchassert(this.searchassert).then(response => {
          this.assertitemKey = Math.random()
          this.assertList = response.data.list
          this.asserttotal = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpasserttype = this.searchassert.asserttype
      },

      searchdbassertBy() {
        this.searchdbassert.page = 1
        searchdbassert(this.searchdbassert).then(response => {
          this.dbassertitemKey = Math.random()
          this.dbassertList = response.data.list
          this.dbasserttotal = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpdbasserttype = this.searchdbassert.assemblename
        this.tmpdbassertenviromrnt = this.searchdbassert.enviroment
      },
      /**
       * 改变每页数量
       * @param size 页大小
       */
      asserthandleSizeChange(size) {
        this.searchassert.page = 1
        this.searchassert.size = size
        this.getassertbycaseid(this.tmpassert)
      },
      dbasserthandleSizeChange(size) {
        this.searchdbassert.page = 1
        this.searchdbassert.size = size
        this.getdbassertbycaseid()
      },
      dbassertvaluehandleSizeChange(size) {
        this.searchdbassertvalue.page = 1
        this.searchdbassertvalue.size = size
        this.getdbassertvaluebyid()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      asserthandleCurrentChange(page) {
        this.searchassert.page = page
        this.getassertbycaseid(this.tmpassert)
      },

      dbasserthandleCurrentChange(page) {
        this.searchdbassert.page = page
        this.getdbassertbycaseid(this.tmpassert)
      },

      dbassertvaluehandleCurrentChange(page) {
        this.searchdbassertvalue.page = page
        this.getdbassertvaluebyid()
      },
      /**
       * 表格序号
       * 可参考自定义表格序号
       * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
       * @param index 数据下标
       * @returns 表格序号
       */
      assertgetIndex(index) {
        return (this.searchassert.page - 1) * this.searchassert.size + index + 1
      },
      dbassertgetIndex(index) {
        return (this.searchdbassert.page - 1) * this.searchdbassert.size + index + 1
      },
      ApicasesdbassertvaluegetIndex(index) {
        return (this.searchdbassertvalue.page - 1) * this.searchdbassertvalue.size + index + 1
      },
      /**
       * 显示添加用例对话框
       */
      showAddapicasesDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpapicases.id = ''
        this.tmpapicases.casename = ''
        this.tmpapicases.apiname = ''
        this.tmpapicases.casetype = ''
        this.tmpapicases.deployunitname = ''
        this.tmpapicases.casejmxname = ''
        this.tmpapicases.casecontent = ''
        this.tmpapicases.expect = ''
        this.tmpapicases.middleparam = ''
        this.tmpapicases.level = 0
        this.tmpapicases.memo = ''
        this.tmpapicases.creator = this.nickname
        this.tmpapicases.creatorid = this.accountId
        this.tmpapicases.projectid = window.localStorage.getItem('pid')
        this.tmpapicases.modelid = ''
        this.tmpapicases.modelname = ''
        this.tmpapicases.mid = ''
        this.tmpapicases.mnickname = ''
        this.apiQuery.modelid = 0
        console.log(this.name)
      },

      /**
       * 显示增加断言对话框
       */
      showAddassertDialog() {
        // 显示新增对话框
        this.AssertdialogStatus = 'add'
        this.AssertAUdialogFormVisible = true
        this.tmpassert.id = ''
        this.tmpassert.assertcondition = ''
        this.tmpassert.assertsubtype = ''
        this.tmpassert.asserttype = ''
        this.tmpassert.assertvalues = ''
        this.tmpassert.expression = ''
        this.tmpassert.assertvaluetype = ''
        this.tmpassert.creator = this.name
      },

      showAdddbassertDialog() {
        // 显示新增对话框
        this.dbAssertdialogStatus = 'add'
        this.dbAssertAUdialogFormVisible = true
        this.tmpdbassert.id = ''
        this.tmpdbassert.enviroment = ''
        this.tmpdbassert.envid = ''
        this.tmpdbassert.assembleid = ''
        this.tmpdbassert.assemblename = ''
        this.tmpdbassert.expectrecordsnums = null
        this.tmpdbassert.expression = ''
        this.tmpdbassert.creator = this.name
      },

      showAddApicasesdbassertvalueDialog() {
        // 显示新增对话框
        this.dbAssertvaluedialogStatus = 'add'
        this.dbassertvalueDialogFormVisible = true
        this.tmpdbassertvalue.id = ''
        this.tmpdbassertvalue.expectvalue = ''
        this.tmpdbassertvalue.valuetype = ''
        this.tmpdbassertvalue.roworder = ''
        this.tmpdbassertvalue.fieldname = ''
        this.tmpdbassertvalue.assertcondition = ''
        this.tmpdbassertvalue.creator = this.name
      },

      /**
       * 显示Copy用例对话框
       */
      showCopyapicasesDialog() {
        // 显示新增对话框
        this.CopydialogFormVisible = true
        this.tmpcopycase.newcasename = ''
        this.tmpcopycase.sourcedeployunitname = ''
        this.tmpcopycase.sourcecasename = ''
        this.tmpcopycase.sourcedeployunitid = ''
        this.tmpcopycase.sourcecaseid = ''
      },

      /**
       * 显示批量Copy用例对话框
       */
      showCopyBatchapicasesDialog() {
        // 显示新增对话框
        this.CopybatchdialogFormVisible = true
        this.tmpbatchcopycase.destinationdeployunitid = ''
        this.tmpbatchcopycase.sourcedeployunitname = ''
        this.tmpbatchcopycase.destinationdeployunitname = ''
        this.tmpbatchcopycase.sourcedeployunitid = ''
      },

      showBatchAssertDialog() {
        // 显示新增对话框
        this.BatchAssertdialogFormVisible = true
        this.tmpbatchassert.assertvaluetype = ''
        this.tmpbatchassert.expression = ''
        this.tmpbatchassert.assertcondition = ''
        this.tmpbatchassert.assertvalues = ''
        this.tmpbatchassert.asserttype = ''
        this.tmpbatchassert.caseid = ''
        this.tmpbatchassert.creator = ''
        this.tmpbatchassert.assertsubtype = ''
      },
      /**
       * 批量用例断言
       */
      addbatchassert() {
        this.testcasebatchassertList = []
        if (this.multipleSelection.length === 0) {
          this.$message.error('请选择需要断言的用例')
        } else {
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.testcasebatchassertList.push({
              'asserttype': this.tmpbatchassert.asserttype,
              'assertsubtype': this.tmpbatchassert.assertsubtype,
              'assertvalues': this.tmpbatchassert.assertvalues,
              'expression': this.tmpbatchassert.expression,
              'assertcondition': this.tmpbatchassert.assertcondition,
              'assertvaluetype': this.tmpbatchassert.assertvaluetype,
              'caseid': this.multipleSelection[i].id,
              'creator': this.name
            })
          }
          batchassertapicase(this.testcasebatchassertList).then(() => {
            this.getapicasesList()
            this.$message.success('添加成功')
            this.BatchAssertdialogFormVisible = false
          }).catch(res => {
            this.$message.error('添加失败')
          })
        }
      },
      /**
       * 复制用例
       */
      copycases() {
        this.$refs.tmpcopycase.validate(valid => {
          if (valid) {
            this.btnLoading = true
            copycases(this.tmpcopycase).then(() => {
              this.$message.success('复制成功')
              this.getapicasesList()
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
       * 批量复制用例
       */
      copybatchcases() {
        this.$refs.tmpbatchcopycase.validate(valid => {
          if (valid) {
            copybatchcases(this.tmpbatchcopycase).then(() => {
              this.$message.success('批量复制成功')
              this.getapicasesList()
              // this.getalltestconditionbytype(this.tmpconditionquery)
              this.CopybatchdialogFormVisible = false
              this.btnLoading = false
            }).catch(res => {
              this.$message.error('批量复制失败')
              this.btnLoading = false
            })
          }
        })
      },

      /**
       * 添加用例
       */
      addapicases() {
        this.$refs.tmpapicases.validate(valid => {
          if (valid) {
            this.btnLoading = true
            this.tmpapicases.expect = this.tmpapicases.expect.trim()
            if (this.tmpprotocal === 'http' || this.tmpprotocal === 'https') {
              this.tmpapicases.casejmxname = 'httpapi'
            }
            addapicases(this.tmpapicases).then(() => {
              this.$message.success('添加成功')
              this.getapicasesList()
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
       * 添加断言
       */
      addassert() {
        this.$refs.tmpassert.validate(valid => {
          if (valid) {
            addapicasesassert(this.tmpassert).then(() => {
              this.$message.success('添加断言成功')
              this.searchassert.caseid = this.tmpassert.caseid
              this.getassertbycaseid(this.searchassert)
              this.AssertAUdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加断言失败')
            })
          }
        })
      },

      adddbassert() {
        this.$refs.tmpdbassert.validate(valid => {
          if (valid) {
            addapicasesdbassert(this.tmpdbassert).then(() => {
              this.$message.success('添加断言成功')
              this.getdbassertbycaseid(this.searchdbassert)
              this.dbAssertAUdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加断言失败')
            })
          }
        })
      },

      adddbassertvalue() {
        this.$refs.tmpdbassertvalue.validate(valid => {
          if (valid) {
            addapicasesdbassertvalue(this.tmpdbassertvalue).then(() => {
              this.$message.success('添加断言成功')
              this.getdbassertvaluebyid()
              this.dbassertvalueDialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加断言失败')
            })
          }
        })
      },
      /**
       * 调试
       */
      runtest() {
        this.$refs.tmptest.validate(valid => {
          if (valid) {
            runtest(this.tmptestdata).then(response => {
              this.tmptest.general = '1.请求地址：' + response.data.responeGeneral.url + '\n' + '2.协议：' + response.data.responeGeneral.protocal + '\n' + '3.请求风格：' + response.data.responeGeneral.apistyle + '\n' + '4.请求方法：' + response.data.responeGeneral.method
              this.tmptest.requestdata = response.data.responeGeneral.postData
              this.headerList = response.data.headerList
              this.cookies = response.data.cookies
              this.requestHeadList = response.data.requestHeadList
              this.requestParamsList = response.data.requestParamsList
              this.tmptest.respone = response.data.responeContent
              this.tmptest.code = response.data.responeCode
              this.tmptest.responeTime = response.data.responeTime
              this.tmptest.size = response.data.size
            }).catch(res => {
              this.$message.error('调试失败')
            })
          }
        })
      },
      /**
       * 添加用例数据
       */
      addapicasesdata() {
        this.tmpapicasesdata.casedataMap = []
        this.$refs.tmpapicasesdata.validate(valid => {
          if (valid) {
            if (this.tmpapicasesdata.propertytype === 'Body') {
              var Bodyparadata = { caseid: this.apicasesList[this.caseindex].id, casename: this.apicasesList[this.caseindex].casename, apiparam: 'Body', apiparamvalue: this.tmpapicasesdata.keyname, propertytype: this.tmpapicasesdata.propertytype, memo: 'memo' }
              this.tmpapicasesdata.casedataMap.push(Bodyparadata)
              console.log('处理Body')
            } else {
              console.log('处理Header，Params')
              for (let i = 0; i < this.tmpcaseparamslist.length; i++) {
                for (let j = 0; j < this.paraList.length; j++) {
                  if (i === j) {
                    var paradata = { caseid: this.apicasesList[this.caseindex].id, casename: this.apicasesList[this.caseindex].casename, apiparam: this.tmpcaseparamslist[i], apiparamvalue: this.paraList[j], propertytype: this.tmpapicasesdata.propertytype, memo: 'memo' }
                    this.tmpapicasesdata.casedataMap.push(paradata)
                  }
                }
              }
            }
            // 再增加
            addapicasesdata(this.tmpapicasesdata).then(() => {
              this.$message.success('添加成功')
              this.btnLoading = false
              this.paramdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      /**
       * 保存用例数据
       */
      async addnewapicasesdata() {
        for (let i = 0; i < this.Headertabledatas.length; i++) {
          this.Headertabledatas[i].mid = this.accountId
        }
        this.updateHeaderpropertydata(this.Headertabledatas)
        for (let i = 0; i < this.Paramstabledatas.length; i++) {
          this.Paramstabledatas[i].mid = this.accountId
        }
        this.updateHeaderpropertydata(this.Paramstabledatas)
        await this.getapi()
        if (this.tmpapi.requestcontenttype === 'Form表单') {
          for (let i = 0; i < this.Bodytabledatas.length; i++) {
            this.Bodytabledatas[i].mid = this.accountId
          }
          this.updateHeaderpropertydata(this.Bodytabledatas)
        } else {
          this.updateapicasesdata()
        }
        this.$message.success('保存成功')
        this.casedataialogFormVisible = false
      },

      updateapicasesdata() {
        // this.tmpapicasesbodydata.caseid = this.tmpapicases.id
        // this.tmpapicasesbodydata.casename = this.tmpapicases.casename
        this.tmpapicasesbodydata.apiparam = 'Body'
        this.tmpapicasesbodydata.paramstype = this.tmpapi.requestcontenttype
        this.tmpapicasesbodydata.propertytype = 'Body'
        this.tmpapicasesbodydata.mid = this.accountId
        updateapicasesdata(this.tmpapicasesbodydata).then(response => {
        }).catch(res => {
          this.$message.error('更新用例Body值失败')
        })
      },

      updateHeaderpropertydata(datas) {
        updatepropertydata(datas).then(response => {
        }).catch(res => {
          this.$message.error('更新用例Header,Params值失败')
        })
      },
      /**
       * 显示修改用例对话框
       * @param index 用例下标
       */
      showUpdateapicasesDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpapicases.id = this.apicasesList[index].id
        this.tmpapicases.casename = this.apicasesList[index].casename
        this.tmpapicases.apiname = this.apicasesList[index].apiname
        this.tmpapicases.apiid = this.apicasesList[index].apiid
        this.tmpapicases.casetype = this.apicasesList[index].casetype
        this.tmpapicases.deployunitname = this.apicasesList[index].deployunitname
        this.tmpapicases.deployunitid = this.apicasesList[index].deployunitid
        this.tmpapicases.casejmxname = this.apicasesList[index].casejmxname
        this.tmpapicases.casecontent = this.apicasesList[index].casecontent
        this.tmpapicases.expect = this.apicasesList[index].expect
        this.tmpapicases.middleparam = this.apicasesList[index].middleparam
        this.tmpapicases.level = this.apicasesList[index].level
        this.tmpapicases.memo = this.apicasesList[index].memo
        this.tmpapicases.loops = this.apicasesList[index].loops
        this.tmpapicases.modelid = this.apicasesList[index].modelid
        this.tmpapicases.modelname = this.apicasesList[index].modelname
        this.tmpapicases.creator = this.apicasesList[index].creator
        this.tmpapicases.mid = this.apicasesList[index].mid
        this.tmpapicases.creatorid = this.accountId
        this.tmpapicases.mnickname = this.apicasesList[index].mnickname
        this.tmpapicases.projectid = window.localStorage.getItem('pid')
        if (this.tmpapicases.casetype === '性能') {
          this.threadloopvisible = true
          this.tmpapicases.threadnum = this.apicasesList[index].threadnum
          // this.tmpapicases.loops = this.apicasesList[index].loops
        } else {
          this.threadloopvisible = false
          this.tmpapicases.threadnum = 1
          // this.tmpapicases.loops = 1
        }
        this.apiList = []
        this.apiQuery.deployunitname = this.apicasesList[index].deployunitname
        this.apiQuery.modelid = 0
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === this.apicasesList[index].deployunitname) {
            this.apiQuery.deployunitid = this.deployunitList[i].id
            this.tmpmodelquery.deployunitid = this.deployunitList[i].id
          }
        }
        this.searchdeployunitmodel()
        getapiListbydeploy(this.apiQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      async getapi() {
        await getapi(this.tmpapicases.apiid).then(response => {
          this.tmpapi = response.data
        }).catch(res => {
          this.$message.error('获取api失败')
        })
      },
      /**
       * 显示用例值对话框
       * @param index 用例下标
       */
      async showcasedataDialog(index) {
        this.activeName = 'zero'
        this.casedataialogFormVisible = true
        this.tmpapicases.casename = this.apicasesList[index].casename
        this.tmpapicases.id = this.apicasesList[index].id
        this.tmpapicases.apiid = this.apicasesList[index].apiid
        this.tmpapicasesbodydata.caseid = this.apicasesList[index].id
        this.tmpapicasesbodydata.casename = this.apicasesList[index].casename
        this.getheaderdatabycaseidandtype()
        this.getparamdatabycaseidandtype()
        await this.getapi()
        if (this.tmpapi.requestcontenttype === 'Form表单') {
          this.BodyParamDataVisible = true
          this.BodyDataVisible = false
          // 获取Body参数数据
          this.getbodydatabycaseidandtype()
        } else {
          // 获取body文本数据
          this.BodyDataVisible = true
          this.BodyParamDataVisible = false
          this.getbodytextdatabycaseidandtype()
          //
        }
      },
      /**
       * 显示用例参数对话框
       * @param index 用例下标
       */
      showUpdateapicasesparamsDialog(index) {
        this.BodyVisible = false
        this.tmpapicasesdata.keyname = ''
        this.tmpapicasesdata.caseid = this.apicasesList[index].id
        this.tmpapicases.casename = this.apicasesList[index].casename
        this.tmpapicases.deployunitname = this.apicasesList[index].deployunitname
        this.tmpapicases.apiname = this.apicasesList[index].apiname
        this.caseindex = index
        this.tmpcaseparamslist = null
        this.tmpapicasesdata.propertytype = null
        this.caseparamtypelist = null
        this.paramdialogFormVisible = true
        getcaseparatype(this.tmpapicases).then(response => {
          this.caseparamsbytypelist = response.data.list
          this.caseparamtypelist = this.caseparamsbytypelist
          console.log(this.caseparamtypelist)
          // 去重
          // const res = new Map()
          // this.caseparamtypelist.filter((arr) => !res.has(this.caseparamtypelist.propertytype) && res.set(this.caseparamtypelist.propertytype, 1))
        }).catch(res => {
          this.$message.error()
          // this.$message.error('获取用例参数类型失败')
        })
      },

      /**
       * 显示修改用例断言对话框
       * @param index 用例下标
       */
      showUpdateapicasesassertDialog(index) {
        this.AssertdialogStatus = 'update'
        this.AssertAUdialogFormVisible = true
        this.tmpassert.id = this.assertList[index].id
        this.tmpassert.asserttype = this.assertList[index].asserttype
        this.tmpassert.assertsubtype = this.assertList[index].assertsubtype
        this.tmpassert.expression = this.assertList[index].expression
        this.tmpassert.assertcondition = this.assertList[index].assertcondition
        this.tmpassert.assertvalues = this.assertList[index].assertvalues
        this.tmpassert.assertvaluetype = this.assertList[index].assertvaluetype
        this.tmpassert.creator = this.name
        if (this.tmpassert.asserttype === 'Respone') {
          this.ExpressionVisible = false
          this.AssertSubVisible = true
        } else {
          this.AssertSubVisible = false
          this.ExpressionVisible = true
        }
      },

      showUpdateapicasesdbassertDialog(index) {
        this.dbAssertdialogStatus = 'update'
        this.dbAssertAUdialogFormVisible = true
        this.tmpdbassert.id = this.dbassertList[index].id
        this.tmpdbassert.assemblename = this.dbassertList[index].assemblename
        this.tmpdbassert.envid = this.dbassertList[index].envid
        this.tmpdbassert.assembleid = this.dbassertList[index].assembleid
        this.tmpdbassert.enviroment = this.dbassertList[index].enviroment
        this.tmpdbassert.expression = this.dbassertList[index].expression
        this.tmpdbassert.expectrecordsnums = this.dbassertList[index].expectrecordsnums
        this.tmpdbassert.creator = this.name
      },

      showUpdatedbassertvalueDialog(index) {
        this.dbAssertvaluedialogStatus = 'update'
        this.dbassertvalueDialogFormVisible = true
        this.tmpdbassertvalue.id = this.ApicasesdbassertvalueList[index].id
        this.tmpdbassertvalue.fieldname = this.ApicasesdbassertvalueList[index].fieldname
        this.tmpdbassertvalue.roworder = this.ApicasesdbassertvalueList[index].roworder
        this.tmpdbassertvalue.valuetype = this.ApicasesdbassertvalueList[index].valuetype
        this.tmpdbassertvalue.assertcondition = this.ApicasesdbassertvalueList[index].assertcondition
        this.tmpdbassertvalue.expectvalue = this.ApicasesdbassertvalueList[index].expectvalue
        this.tmpdbassertvalue.memo = this.ApicasesdbassertvalueList[index].memo
        this.tmpdbassertvalue.creator = this.name
      },

      /**
       * 显示断言对话框
       */
      showAssertDialog(index) {
        this.assertactiveName = 'zero'
        this.tmpapicases.id = this.apicasesList[index].id
        this.tmpassert.caseid = this.tmpapicases.id
        this.tmpdbassert.caseid = this.apicasesList[index].id
        this.searchassert.caseid = this.tmpassert.caseid
        this.searchdbassert.caseid = this.apicasesList[index].id
        this.getassertbycaseid(this.searchassert)
        this.getdbassertbycaseid(this.searchdbassert)
        this.AssertdialogFormVisible = true
        this.searchassert.asserttype = ''
      },

      /**
       * 显示断言对话框
       */
      showTestDialog(index) {
        this.tmptestdata.caseid = this.apicasesList[index].id
        this.tmptestdata.conditionid = this.tmptest.conditionid
        this.tmptestdata.globalheaderid = this.tmptest.globalheaderid
        this.checked = false
        this.tmptestdata.prixflag = this.checked
        this.tmpapicases.casename = this.apicasesList[index].casename
        this.activeName = 'zero'
        this.tmptest.globalheaderid = 0
        this.tmptest.conditionid = 0
        this.tmptest.globalheadername = ''
        this.tmptest.conditionname = ''
        this.tmptest.general = ''
        this.headerList = null
        this.cookies = null
        this.requestHeadList = null
        this.tmptest.size = ''
        this.tmptest.code = ''
        this.tmptest.responeTime = ''
        this.tmptest.enviromentname = ''
        this.tmptest.respone = ''
        this.TestdialogFormVisible = true
        this.tmpapicondition.conditionid = this.apicasesList[index].id
        this.tmpapicondition.conditionname = this.apicasesList[index].casename
        this.tmpapicondition.conditiontype = 'case'
        this.searchapicondition.conditiontype = 'case'
        this.searchapicondition.conditionid = this.apicasesList[index].id
        this.searchdbcondition.conditiontype = 'case'
        this.searchdbcondition.conditionid = this.apicasesList[index].id
        this.tmpdbcondition.conditionid = this.apicasesList[index].id
        this.tmpdbcondition.conditionname = this.apicasesList[index].casename
        this.tmpdbcondition.conditiontype = 'case'
        this.Scenedelaysearch.conditionid = this.apicasesList[index].id
        this.tmpscriptcondition.conditionid = this.apicasesList[index].id
        this.tmpscriptcondition.conditionname = this.apicasesList[index].casename
        this.tmpscriptcondition.conditiontype = 'case'
        this.searchscriptcondition.conditiontype = 'case'
        this.searchscriptcondition.conditionid = this.apicasesList[index].id
        // this.getassembleallnameList()
      },
      /**
       * 显示用例变量对话框
       */
      showCaseVariablesDialog(index) {
        this.caseVariablesDialogFormVisible = true
        // this.searchapicasevariables.caseid = this.apicasesList[index].id
        this.tmptestvariables.caseid = this.apicasesList[index].id
        this.tmptestvariables.casename = this.apicasesList[index].casename
        this.findtestvariablesbycaseid()
      },

      showCaseVariablesforConditionDialog(index) {
        this.caseVariablesDialogFormVisible = true
        // this.searchapicasevariables.caseid = this.apicasesList[index].id
        this.tmptestvariables.caseid = this.apiconditioncaseList[index].caseid
        this.tmptestvariables.casename = this.apiconditioncaseList[index].casename
        this.findtestvariablesbycaseid()
      },

      /**
       * 显示前置条件对话框
       */
      showpreconditionDialog() {
        if (this.multipleSelection.length > 1) {
          this.$message.error('不支持多个用例一起设置条件，选择单个设置')
        } else {
          this.preconditiondialogFormVisible = true
          this.tmpapicases.id = this.multipleSelection[0].id
        }
      },

      /**
       * 更新用例
       */
      updateapicases() {
        this.$refs.tmpapicases.validate(valid => {
          if (valid) {
            this.tmpapicases.expect = this.tmpapicases.expect.trim()
            if (this.tmpprotocal === 'http' || this.tmpprotocal === 'https') {
              this.tmpapicases.casejmxname = 'httpapi'
            }
            updateapicases(this.tmpapicases).then(() => {
              this.$message.success('更新成功')
              this.getapicasesList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 更新用例断言
       */
      updateassert() {
        this.$refs.tmpassert.validate(valid => {
          if (valid) {
            updateapicasesassert(this.tmpassert).then(() => {
              this.$message.success('更新成功')
              this.getassertbycaseid()
              this.AssertAUdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      updatedbassert() {
        this.$refs.tmpdbassert.validate(valid => {
          if (valid) {
            updateapicasesdbassert(this.tmpdbassert).then(() => {
              this.$message.success('更新成功')
              this.getdbassertbycaseid()
              this.dbAssertAUdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      updatedbassertvalue() {
        this.$refs.tmpdbassertvalue.validate(valid => {
          if (valid) {
            updateapicasesdbassertvalue(this.tmpdbassertvalue).then(() => {
              this.$message.success('更新成功')
              this.getdbassertvaluebyid()
              this.dbassertvalueDialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除用例
       * @param index 用例下标
       */
      removeapicases(index) {
        this.$confirm('删除该用例？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.apicasesList[index].id
          removeapicases(id).then(() => {
            this.$message.success('删除成功')
            this.getapicasesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 删除用例断言
       * @param index 用例下标
       */
      removeapicasesassert(index) {
        this.$confirm('删除该用例断言？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.assertList[index].id
          removeapicasesassert(id).then(() => {
            this.$message.success('删除成功')
            this.getassertbycaseid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removeapicasesdbassert(index) {
        this.$confirm('删除该用例断言？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dbassertList[index].id
          removeapicasesdbassert(id).then(() => {
            this.$message.success('删除成功')
            this.getdbassertbycaseid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removedbassertvalue(index) {
        this.$confirm('删除该用例断言？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.ApicasesdbassertvalueList[index].id
          removeapicasesdbassertvalue(id).then(() => {
            this.$message.success('删除成功')
            this.getdbassertvaluebyid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 用例是否唯一
       * @param 用例
       */
      isUniqueDetail(apicases) {
        for (let i = 0; i < this.apicasesList.length; i++) {
          if (this.apicasesList[i].id !== apicases.id) { // 排除自己
            if (this.apicasesList[i].deployunitname === apicases.deployunitname) {
              if (this.apicasesList[i].apiname === apicases.apiname) {
                if (this.apicasesList[i].casename === apicases.casename) {
                  this.$message.error('用例名已存在')
                  return false
                }
              }
            }
          }
        }
        return true
      },
      showAddapicasesconditionnotexistDialog() {
        // 显示新增对话框
        this.casedebugconditionnotexistdialogFormVisible = true
        this.casenotexisttotal = 0
        this.apicasesnotexistList = ''
        this.searchnotexistcase.conditionname = ''
        this.searchnotexistcase.deployunitname = ''
      },
      showGlobalHeadernotexistDialog() {
        // 显示新增对话框
        this.caseglobalheadernotexistdialogFormVisible = true
        this.apicasesheadernotexistList = ''
        this.headercasenotexisttotal = 0
        this.searchglobalnotexistheadercase.globalheadername = ''
        this.searchglobalnotexistheadercase.deployunitname = ''
      },

      showGlobalHeaderexistDialog() {
        // 显示新增对话框
        this.caseglobalheaderexistdialogFormVisible = true
        this.apicasesheaderexistList = ''
        this.headercaseexisttotal = 0
        this.searchglobalheadercase.globalheadername = ''
        this.searchglobalheadercase.deployunitname = ''
      },

      showdeleteapicasesconditionnotexistDialog() {
        // 显示取消对话框
        this.casedebugconditionexistdialogFormVisible = true
        this.caseexisttotal = 0
        this.apicasesexistList = ''
        this.searchexistcase.conditionname = ''
        this.searchexistcase.deployunitname = ''
      },
      casenotexisthandleClickTableRow(row, event, column) {
        console.log(row)
      },

      casenotexisthandleSelectionChange(rows) {
        this.notexistcasemultipleSelection = rows
      },
      headercasenotexisthandleSelectionChange(rows) {
        this.headernotexistcasemultipleSelection = rows
      },
      headercaseexisthandleSelectionChange(rows) {
        this.headerexistcasemultipleSelection = rows
      },
      caseexisthandleClickTableRow(row, event, column) {
        console.log(row)
      },

      caseexisthandleSelectionChange(rows) {
        this.existcasemultipleSelection = rows
      },
      /**
       * 装载测试集合的用例
       */
      addheadercasesdebug() {
        this.testcaseList = []
        if (this.headernotexistcasemultipleSelection.length === 0) {
          this.$message.error('请选择添加的用例')
        } else {
          for (let i = 0; i < this.headernotexistcasemultipleSelection.length; i++) {
            this.testcaseList.push({
              'globalheaderid': this.searchglobalnotexistheadercase.globalheaderid,
              'globalheadername': this.searchglobalnotexistheadercase.globalheadername,
              'deployunitname': this.searchglobalnotexistheadercase.deployunitname,
              'deployunitid': this.searchglobalnotexistheadercase.deployunitid,
              'testcasename': this.headernotexistcasemultipleSelection[i].testcasename,
              'testcaseid': this.headernotexistcasemultipleSelection[i].id,
              'execplanid': 0
            })
          }
          addheadercasesdebug(this.testcaseList).then(() => {
            this.getheadercasenotexistList()
            this.$message.success('添加成功')
          }).catch(res => {
            this.$message.error('添加失败')
          })
        }
      },

      /**
       * 装载header的用例
       */
      adddebugconditiontestcase() {
        this.testcaseList = []
        if (this.notexistcasemultipleSelection.length === 0) {
          this.$message.error('请选择添加条件的用例')
        } else {
          for (let i = 0; i < this.notexistcasemultipleSelection.length; i++) {
            this.testcaseList.push({
              'conditionid': this.searchnotexistcase.conditionid,
              'deployunitid': this.searchnotexistcase.deployunitid,
              'conditionname': this.searchnotexistcase.conditionname,
              'deployunitname': this.searchnotexistcase.deployunitname,
              'casename': this.notexistcasemultipleSelection[i].casename,
              'caseid': this.notexistcasemultipleSelection[i].id,
              'creator': this.name
            })
          }
          addcasesdebugcondition(this.testcaseList).then(() => {
            this.getcasenotexistconditionList()
            this.$message.success('添加成功')
          }).catch(res => {
            this.$message.error('添加失败')
          })
        }
      },

      delatedebugconditiontestcase() {
        this.testcaseList = []
        if (this.existcasemultipleSelection.length === 0) {
          this.$message.error('请选择添加条件的用例')
        } else {
          for (let i = 0; i < this.existcasemultipleSelection.length; i++) {
            this.testcaseList.push({
              'conditionid': this.searchexistcase.conditionid,
              'deployunitid': this.searchexistcase.deployunitid,
              'conditionname': this.searchexistcase.conditionname,
              'deployunitname': this.searchexistcase.deployunitname,
              'casename': this.existcasemultipleSelection[i].casename,
              'caseid': this.existcasemultipleSelection[i].caseid,
              'creator': this.name
            })
          }
          delatedebugconditiontestcase(this.testcaseList).then(() => {
            this.getcaseexistconditionList()
            this.$message.success('取消成功')
          }).catch(res => {
            this.$message.error('取消失败')
          })
        }
      },

      deleteheadercase() {
        this.testcaseList = []
        if (this.headerexistcasemultipleSelection.length === 0) {
          this.$message.error('请选择取消的用例')
        } else {
          for (let i = 0; i < this.headerexistcasemultipleSelection.length; i++) {
            this.testcaseList.push({
              'globalheaderid': this.searchglobalheadercase.globalheaderid,
              'globalheadername': this.searchglobalheadercase.globalheadername,
              'deployunitname': this.searchglobalheadercase.deployunitname,
              'deployunitid': this.searchglobalheadercase.deployunitid,
              'testcasename': this.headerexistcasemultipleSelection[i].testcasename,
              'testcaseid': this.headerexistcasemultipleSelection[i].testcaseid,
              'execplanid': 0
            })
          }
          deleteheadercase(this.testcaseList).then(() => {
            this.getheadercaseexistList()
            this.$message.success('取消成功')
          }).catch(res => {
            this.$message.error('取消失败')
          })
        }
      },
      getalltestcondition() {
        this.tmpconditionquery.objecttype = '测试集合'
        getalltestcondition(this.tmpconditionquery).then(response => {
          this.conditionList = response.data
          this.casenotexisttotal = response.data.total
        }).catch(res => {
          this.$message.error('加载条件列表失败')
        })
      },

      notexistconditionnameselectChanged(e) {
        if (e === '请选择') {
          this.tmptestdata.conditionid = 0
        } else {
          for (let i = 0; i < this.conditionList.length; i++) {
            if (this.conditionList[i].conditionname === e) {
              this.searchnotexistcase.conditionid = this.conditionList[i].id
              this.tmptestdata.conditionid = this.conditionList[i].id
            }
          }
        }
      },
      existconditionnameselectChanged(e) {
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === e) {
            this.searchexistcase.conditionid = this.conditionList[i].id
          }
        }
      },

      notexistheaderselectChanged(e) {
        if (e === '请选择') {
          this.tmptestdata.globalheaderid = 0
        } else {
          for (let i = 0; i < this.globalheaderallList.length; i++) {
            if (this.globalheaderallList[i].globalheadername === e) {
              this.searchglobalnotexistheadercase.globalheaderid = this.globalheaderallList[i].id
              this.tmptestdata.globalheaderid = this.globalheaderallList[i].id
            }
          }
        }
      },

      existheaderselectChanged(e) {
        for (let i = 0; i < this.globalheaderallList.length; i++) {
          if (this.globalheaderallList[i].globalheadername === e) {
            this.searchglobalheadercase.globalheaderid = this.globalheaderallList[i].id
          }
        }
      },

      notexistdeployunitnameselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.searchnotexistcase.deployunitid = this.deployunitList[i].id
            this.searchglobalnotexistheadercase.deployunitid = this.deployunitList[i].id
          }
        }
      },
      existdeployunitnameselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.searchexistcase.deployunitid = this.deployunitList[i].id
            this.searchglobalheadercase.deployunitid = this.deployunitList[i].id
            console.log(this.searchglobalheadercase)
          }
        }
      },

      getcasenotexistconditionList() {
        this.searchnotexistcase.conditionid = this.tmpnotexistconditionid
        this.searchnotexistcase.deployunitid = this.tmpnotexistdeployunitid
        searchnotexist(this.searchnotexistcase).then(response => {
          this.apicasesnotexistList = response.data.list
          this.casenotexisttotal = response.data.total
        }).catch(res => {
          this.$message.error('获取未添加条件用例失败')
        })
      },

      searchnotexistcaseBy() {
        this.searchnotexistcase.page = 1
        console.log(this.searchnotexistcase)
        this.$refs.searchnotexistcase.validate(valid => {
          if (valid) {
            searchnotexist(this.searchnotexistcase).then(response => {
              this.casenotexistKey = Math.random()
              this.apicasesnotexistList = response.data.list
              this.casenotexisttotal = response.data.total
            }).catch(res => {
              this.$message.error('获取未添加条件用例失败')
            })
            this.tmpnotexistdeployunitid = this.searchnotexistcase.deployunitid
            this.tmpnotexistconditionid = this.searchnotexistcase.conditionid
          }
        })
      },

      searchheadernotexist() {
        this.searchglobalnotexistheadercase.page = 1
        this.$refs.searchglobalnotexistheadercase.validate(valid => {
          if (valid) {
            searchheadernotexist(this.searchglobalnotexistheadercase).then(response => {
              this.headercasenotexistKey = Math.random()
              this.apicasesheadernotexistList = response.data.list
              this.headercasenotexisttotal = response.data.total
            }).catch(res => {
              this.$message.error('获取未添加header用例失败')
            })
            this.tmpnotexistglobalheaderid = this.searchglobalnotexistheadercase.globalheaderid
            this.tmpnotexistheaderdeployunitid = this.searchglobalnotexistheadercase.deployunitid
          }
        })
      },
      searchheaderexist() {
        this.searchglobalheadercase.page = 1
        this.$refs.searchglobalheadercase.validate(valid => {
          if (valid) {
            searchheaderexist(this.searchglobalheadercase).then(response => {
              this.headercaseexistKey = Math.random()
              this.apicasesheaderexistList = response.data.list
              this.headercaseexisttotal = response.data.total
            }).catch(res => {
              this.$message.error('获取添加header用例失败')
            })
            this.tmpexistglobalheaderid = this.searchglobalheadercase.globalheaderid
            this.tmpexistheaderdeployunitid = this.searchglobalheadercase.deployunitid
          }
        })
      },
      getheadercaseexistList() {
        this.searchglobalheadercase.globalheaderid = this.tmpexistglobalheaderid
        this.searchglobalheadercase.deployunitid = this.tmpexistheaderdeployunitid
        searchheaderexist(this.searchglobalheadercase).then(response => {
          this.apicasesheaderexistList = response.data.list
          this.headercaseexisttotal = response.data.total
        }).catch(res => {
          this.$message.error('获取添加header用例失败')
        })
      },

      getheadercasenotexistList() {
        this.searchglobalnotexistheadercase.globalheaderid = this.tmpnotexistglobalheaderid
        this.searchglobalnotexistheadercase.deployunitid = this.tmpnotexistheaderdeployunitid
        searchheadernotexist(this.searchglobalnotexistheadercase).then(response => {
          this.apicasesheadernotexistList = response.data.list
          this.headercasenotexisttotal = response.data.total
        }).catch(res => {
          this.$message.error('获取未添加header用例失败')
        })
      },
      /**
       * 改变每页数量
       * @param size 页大小
       */
      casenotexisthandleSizeChange(size) {
        console.log(size)
        this.searchnotexistcase.page = 1
        this.searchnotexistcase.size = size
        this.getcasenotexistconditionList()
      },
      caseexisthandleSizeChange(size) {
        this.searchexistcase.page = 1
        this.searchexistcase.search.size = size
        this.getcaseexistconditionList()
      },
      headercasenotexisthandleSizeChange(size) {
        this.searchglobalnotexistheadercase.page = 1
        this.searchglobalnotexistheadercase.size = size
        this.getheadercasenotexistList()
      },
      headercaseexisthandleSizeChange(size) {
        this.searchglobalheadercase.page = 1
        this.searchglobalheadercase.size = size
        this.getheadercaseexistList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      caseexisthandleCurrentChange(page) {
        this.searchexistcase.page = page
        this.getcaseexistconditionList()
      },
      casenotexisthandleCurrentChange(page) {
        this.searchnotexistcase.page = page
        this.getcasenotexistconditionList()
      },
      headercasenotexisthandleCurrentChange(page) {
        this.searchglobalnotexistheadercase.page = page
        this.getheadercasenotexistList()
      },
      headercaseexisthandleCurrentChange(page) {
        this.searchglobalheadercase.page = page
        this.getheadercaseexistList()
      },
      /**
       * 表格序号
       * 可参考自定义表格序号
       * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
       * @param index 数据下标
       * @returns 表格序号
       */
      caseexistgetIndex(index) {
        return (this.searchexistcase.page - 1) * this.searchexistcase.size + index + 1
      },
      casenotexistgetIndex(index) {
        return (this.searchnotexistcase.page - 1) * this.searchnotexistcase.size + index + 1
      },
      headercasenotexistgetIndex(index) {
        return (this.searchglobalnotexistheadercase.page - 1) * this.searchglobalnotexistheadercase.size + index + 1
      },
      headercaseexistgetIndex(index) {
        return (this.searchglobalheadercase.page - 1) * this.searchglobalheadercase.size + index + 1
      },

      getcaseexistconditionList() {
        this.searchexistcase.conditionid = this.tmpexistconditionid
        this.searchexistcase.deployunitid = this.tmpexistdeployunitid
        searchexist(this.searchexistcase).then(response => {
          this.apicasesexistList = response.data.list
          this.caseexisttotal = response.data.total
        }).catch(res => {
          this.$message.error('获取未添加条件用例失败')
        })
      },

      searchexistcaseBy() {
        this.searchexistcase.page = 1
        console.log(this.searchexistcase)
        this.$refs.searchexistcase.validate(valid => {
          if (valid) {
            searchexist(this.searchexistcase).then(response => {
              this.apicasesexistList = response.data.list
              this.caseexisttotal = response.data.total
            }).catch(res => {
              this.$message.error('获取未添加条件用例失败')
            })
            this.tmpexistdeployunitid = this.searchexistcase.deployunitid
            this.tmpexistconditionid = this.searchexistcase.conditionid
          }
        })
      }
    }
  }
</script>
