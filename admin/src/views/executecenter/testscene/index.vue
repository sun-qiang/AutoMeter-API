<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('testscene:list')"
            @click.native.prevent="gettestsceneList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('testscene:add')"
            @click.native.prevent="showAddtestsceneDialog"
          >添加测试场景</el-button>

          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('testscene:add')"
            @click.native.prevent="showCopytestsceneDialog"
          >复制测试场景</el-button>
        </el-form-item>

        <span v-if="hasPermission('testscene:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.scenename" @keyup.enter.native="searchBy" placeholder="测试场景名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="testsceneList"
      :key="itemKey"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="编号" align="center" width="60">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="测试场景名" align="center" prop="scenename" width="150"/>
      <el-table-column label="类型" align="center" prop="usetype" width="100"/>
      <el-table-column label="用例数" align="center" prop="casenums" width="100"/>
      <el-table-column label="维护人" align="center" prop="creator" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('testscene:update')  || hasPermission('testscene:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('testscene:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatetestsceneDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('testscene:delete') && scope.row.id !== id"
            @click.native.prevent="removetestscene(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"用例前置条件
            icon="el-icon-plus"
            v-if="hasPermission('testscene:loadcase') && scope.row.id !== id"
            @click.native.prevent="showtestsceneCaseDialog(scope.$index)"
          >装载用例</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('testscene:loadcase') && scope.row.id !== id"
            @click.native.prevent="showDebugSceneDialog(scope.$index)"
          >调试场景</el-button>
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
        :model="tmptestscene"
        ref="tmptestscene"
      >
        <el-form-item label="测试场景名" prop="scenename" required>
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmptestscene.scenename"
          />
        </el-form-item>

        <el-form-item label="场景类型" prop="usetype" required >
          <el-select v-model="tmptestscene.usetype" placeholder="场景类型" style="width:100%">
            <el-option label="功能" value="功能"></el-option>
            <el-option label="性能" value="性能"></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmptestscene.memo"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmptestscene'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addtestscene"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatetestscene"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title='复制场景' :visible.sync="CopysceneFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpcopyscene"
        ref="tmpcopyscene"
      >
        <el-form-item label="源场景" prop="sourcescenename" required >
          <el-select v-model="tmpcopyscene.sourcescenename" placeholder="源场景" style="width:100%" @change="CopySourceSceneChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testcase, index) in sourcetestsceneList" :key="index">
              <el-option :label="testcase.scenename" :value="testcase.scenename" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="新场景名" prop="newscenename" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpcopyscene.newscenename"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="CopysceneFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="copyscene"
        >保存
        </el-button>
      </div>
    </el-dialog>

    <el-dialog width="1000px" title='场景用例' :visible.sync="testscenecasedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true"  >
          <el-form-item  label="微服务:" prop="deployunitname" >
            <el-select style="width: 120px" v-model="searchcase.deployunitname" filterable placeholder="微服务" @change="deployunitselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item  label="模块:" prop="modelname" >
            <el-select style="width: 120px" v-model="searchcase.modelname" filterable placeholder="模块" @change="modelselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(model, index) in modelList" :key="index">
                <el-option :label="model.modelname" :value="model.modelname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="API:">
            <el-select style="width: 120px" v-model="searchcase.apiname" filterable placeholder="api名" @change="ApiselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(api, index) in apiList" :key="index">
                <el-option :label="api.apiname" :value="api.apiname"/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchscenecaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="ShowAddCaseDialog" :loading="btnLoading">装载用例</el-button>
          </el-form-item>
<!--          <el-form-item>-->
<!--            <el-button type="danger" @click="ShowAddCaseDialog" :loading="btnLoading">批量删除</el-button>-->
<!--          </el-form-item>-->
        </el-form>

      </div>
      <el-table
        ref="caseTable"
        :data="testscenecaseList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="scenecasegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="apiid" v-if="show" align="center" prop="apiid" width="130"/>
        <el-table-column label="deployunitid" v-if="show" align="center" prop="deployunitid" width="130"/>
        <el-table-column label="用例名" align="center" prop="casename" width="110"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="110"/>
        <el-table-column label="API" align="center" prop="apiname" width="110"/>

        <el-table-column width="90" align="center" label="执行步骤">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.caseorder" class="edit-input"
                        oninput="value=value.replace(/[^\d]/g,'')"
                        maxLength='10'
                        size="small" />
              <el-button
                class="cancel-btn"
                size="small"
                icon="el-icon-refresh"
                type="warning"
                @click="cancelEdit(row)"
              >
                取消
              </el-button>
            </template>
            <span v-else>{{ row.caseorder }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="120">
          <template slot-scope="{row}">
            <el-button
              v-if="row.edit"
              type="success"
              size="small"
              icon="el-icon-circle-check-outline"
              @click="confirmEdit(row)"
            >
              保存
            </el-button>
            <el-button
              v-else
              type="primary"
              size="small"
              @click="row.edit=!row.edit"
            >
              设置步骤
            </el-button>
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center" width="370"
                         v-if="hasPermission('testscene:deletecase')  || hasPermission('testscene:scenecondition')">
          <template slot-scope="scope">
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('testscene:deletecase') && scope.row.id !== id"
              @click.native.prevent="removetestscenecase(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecondition') && scope.row.id !== id"
              @click.native.prevent="showtestscenecaseConditionDialog(scope.$index)"
            >前置条件</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecondition') && scope.row.id !== id"
              @click.native.prevent="showtestscenecaseLogicDialog(scope.$index)"
            >逻辑设置</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecondition') && scope.row.id !== id"
              @click.native.prevent="showtestcasedataDialog(scope.$index)"
            >用例数据</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="casehandleSizeChange"
        @current-change="casehandleCurrentChange"
        :current-page="searchcase.page"
        :page-size="searchcase.size"
        :total="casetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>

    <el-dialog title="场景前置条件" :visible.sync="sceneConditionFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('executeplan:add')"
              @click.native.prevent="showAddapiparamsDialog"
            >添加前置接口</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('executeplan:add')"
              @click.native.prevent="showAddapiparamsDialog"
            >添加前置数据库</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('executeplan:add')"
              @click.native.prevent="showAddapiparamsDialog"
            >添加前置脚本</el-button>

            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('executeplan:add')"
              @click.native.prevent="showAddSceneCasedelayconditionDialog"
            >添加前置延时</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="paramsList"
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
        <el-table-column label="Header" align="center" prop="globalheadername" width="280"/>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('executeplan:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateparamsDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
              @click.native.prevent="removeexecuteplanparam(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-table
        :data="paramsList"
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
        <el-table-column label="Header" align="center" prop="globalheadername" width="280"/>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('executeplan:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateparamsDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
              @click.native.prevent="removeexecuteplanparam(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-table
        :data="paramsList"
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
        <el-table-column label="Header" align="center" prop="globalheadername" width="280"/>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('executeplan:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateparamsDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
              @click.native.prevent="removeexecuteplanparam(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>


      <el-table
        :data="paramsList"
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
        <el-table-column label="Header" align="center" prop="globalheadername" width="280"/>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('executeplan:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateparamsDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
              @click.native.prevent="removeexecuteplanparam(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="用例前置条件" width="1150px" :visible.sync="scenecaseConditionFormVisible">
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
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddSceneCasedelayconditionDialog"
            >添加前置延时</el-button>
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
              @click.native.prevent="showscenecaseconditionorderDialog"
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
        <el-table-column label="前置条件名" :show-overflow-tooltip="true"  align="center" prop="subconditionname" width="130"/>
        <el-table-column label="所属用例" :show-overflow-tooltip="true"  align="center" prop="conditionname" width="130"/>
        <el-table-column label="前置接口" :show-overflow-tooltip="true"  align="center" prop="casename" width="130"/>
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
      2.延时前置条件：
      <el-table
        :data="delayconditionList"
        :key="itemKey"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="delaycasegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="前置条件名" align="center" prop="subconditionname" width="200"/>
        <el-table-column label="所属用例" align="center" prop="conditionname" width="150"/>
        <el-table-column label="等待时间(秒)" align="center" prop="delaytime" width="150">
        </el-table-column>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="150">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="150">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>
        <el-table-column label="管理" align="center"
                         v-if="hasPermission('delaycondition:update')  || hasPermission('delaycondition:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('delaycondition:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatedelayconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('delaycondition:delete') && scope.row.id !== id"
              @click.native.prevent="removedelaycondition(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      3.数据库前置条件：
      <el-table
        :data="dbconditioncaseList"
        table-layout='auto'
        class="tableAuto"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="dbconditioncaseIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="100"/>
        <el-table-column label="所属用例" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
        <el-table-column label="环境" align="center" prop="enviromentname" width="100"/>
        <el-table-column label="组件名" align="center" prop="assemblename" width="100"/>
        <el-table-column label="Sql类型" align="center" prop="dbtype" width="70"/>
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

        <el-table-column label="管理" align="center"  width="260"
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
              v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
              @click.native.prevent="showdbvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>
      4.脚本前置条件
      <el-table
        :data="scriptconditionList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="scriptcasegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="130"/>
        <el-table-column label="所属用例" :show-overflow-tooltip="true" align="center" prop="conditionname" width="130"/>
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
        <el-table-column label="操作人" align="center" prop="creator" width="70"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" width="250" align="center"
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
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item  label="模块:" prop="modelname" >
          <el-select v-model="tmpapicondition.modelname" filterable placeholder="模块" style="width:100%"  @change="apiconditionmodelselectChanged($event)">
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

    <el-dialog width="900px" title='添加用例' :visible.sync="addtestscenecasedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true"  >
          <el-form-item  label="微服务:" prop="deployunitname" >
            <el-select style="width: 120px" v-model="addsearchcase.deployunitname" required filterable placeholder="微服务" @change="addcasedeployunitselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item  label="模块:" prop="modelname" >
            <el-select style="width: 120px" v-model="addsearchcase.modelname" filterable placeholder="模块" @change="addcasemodelselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(model, index) in modelList" :key="index">
                <el-option :label="model.modelname" :value="model.modelname" />
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="API:">
            <el-select style="width: 120px" v-model="addsearchcase.apiname" filterable placeholder="api名" @change="addcaseApiselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(api, index) in addcaseapiList" :key="index">
                <el-option :label="api.apiname" :value="api.apiname"/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchaddcaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table
        ref="addcaseTable"
        :data="addtestcaselastList"
        :key="itemaddcaseKey"
        @row-click="addcasehandleClickTableRow"
        @selection-change="addcasehandleSelectionChange"
        v-loading.body="addcaselistLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="sceneaddcasegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="apiid" v-if="show" align="center" prop="apiid" width="120"/>
        <el-table-column label="deployunitid" v-if="show" align="center" prop="deployunitid" width="120"/>
        <el-table-column label="用例名" align="center" prop="casename" width="250"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="180"/>
        <el-table-column label="API" align="center" prop="apiname" width="250"/>
        <el-table-column label="类型" align="center" prop="casetype" width="70"/>
      </el-table>
      <el-pagination
        @size-change="addcasehandleSizeChange"
        @current-change="addcasehandleCurrentChange"
        :current-page="addsearchcase.page"
        :page-size="addsearchcase.size"
        :total="addcasetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="addtestscenecasedialogFormVisible = false">取消</el-button>

        <el-button
          type="primary"
          :loading="btnLoading"
          @click.native.prevent="addtestscenetestcase"
        >添加</el-button>
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

    <el-dialog title="逻辑设置" :visible.sync="LogicdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpscenecaselogic"
        ref="tmpscenecaselogic"
      >
        <el-form-item label="循环次数" prop="loopnums" required>
          <el-input
            type="text"
            maxlength="50"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model.trim="tmpscenecaselogic.loopnums"
          />
        </el-form-item>
        <el-form-item label="断言失败停止项" prop="stopflag" required>
          <el-select v-model="tmpscenecaselogic.stopflag" placeholder="类型" style="width:100%">
            <el-option label="无" value="无" />
            <el-option label="当前场景" value="当前场景" />
            <el-option label="当前集合" value="当前集合" />
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="LogicdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          @click.native.prevent="updatescenecaselogic"
        >保存</el-button>
      </div>
    </el-dialog>

    <el-dialog width="1100px" title='数据库变量列表' :visible.sync="dbVariablesDialogFormVisible">
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
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="dbVariablesgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="数据库变量名" align="center" :show-overflow-tooltip="true" prop="dbvariablesname" width="130"/>
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

        <el-table-column label="管理" align="center"  width="250"
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
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
              @click.native.prevent="removedbvariables(scope.$index)"
            >使用变量</el-button>
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

    <el-dialog title="用例值" width="1000px" :visible.sync="casedataialogFormVisible">
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
            v-model="tmpapicase.casename"
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

    <el-dialog title="调试测试场景" width="1340px" :visible.sync="DebugScenedialogFormVisible">
      <div class="container">
        <div class="left-component">
          <div class="tree-scroll">
            <el-input style="width: 300px"
                      placeholder="输入关键字进行过滤"
                      v-model="filterText">
            </el-input>
            <el-tree
              class="flow-tree"
              :data="SeceneDebugdata"
              :props="defaultProps"
              default-expand-all
              :expand-on-click-node ="false"
              :filter-node-method="filterNode"
              @node-click="clickNode"
              ref="tree">
            </el-tree>
          </div>
        </div>
        <div class="right-component">
          <el-form inline="true"
            status-icon
            class="small-space"
            label-position="left"
            label-width="80px"
            style="width: 950px; margin-left:50px;"
            :model="tmpapicasesdata"
            ref="tmpapicasesdata"
          >
            <el-form :inline="true" :model="tmptest" ref="tmptest">
              <el-form-item label="调试目标：" required>
                <el-input style="width:250px" placeholder="未选择"  readonly="true" v-model="tmptest.casename"/>
              </el-form-item>
            <el-form-item label="环境：" prop="enviromentname"  required>
              <el-select style="width:250px" v-model="tmptest.enviromentname"  placeholder="环境" @change="EnviromentselectChanged($event)" >
                <el-option label="请选择"  value=""  />
                <div v-for="(enviroment, index) in enviromentnameList" :key="index">
                  <el-option :label="enviroment.enviromentname" :value="enviroment.enviromentname" required />
                </div>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                type="success"
                size="mini"
                v-if="hasPermission('testscene:list')"
                @click.native.prevent="addnewapicasesdata"
              >保存用例数据</el-button>
              <el-button
                type="success"
                size="mini"
                v-if="hasPermission('testscene:list')"
                @click.native.prevent="runtest"
              >调试</el-button>
            </el-form-item>
            <el-tabs v-model="activebbbName" type="card" >
              <el-tab-pane label="用例数据" name="xxx">
                <el-tabs v-model="activeaaaName" type="card" ref="tabs">
                  <el-tab-pane label="Header" name="zero">
                    <template>
                      <el-table :data="Headertabledatas" fit
                                highlight-current-row border>
                        <el-table-column label="参数" prop="apiparam" align="center" width="450">
                          <template slot-scope="scope">
                            <el-input size="mini" readonly="true" v-model="scope.row.apiparam"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="值" prop="apiparamvalue" align="center" width="500">
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
                        <el-table-column label="参数"  align="center" width="450">
                          <template slot-scope="scope">
                            <el-input size="mini" readonly="true" v-model="scope.row.apiparam"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="值类型"  align="center" width="250">
                          <template slot-scope="scope">
                            <el-input size="mini" readonly="true" v-model="scope.row.paramstype"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="值"  align="center" width="250">
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
                        <el-form-item  prop="apiparamvalue" >
                        <el-input style="width: 950px"
                            type="textarea"
                            rows="15" cols="10"
                            prefix-icon="el-icon-message"
                            auto-complete="off"
                            v-model.trim="tmpapicasesbodydata.apiparamvalue"
                          />
                        </el-form-item>
                      </div>
                    </template>

                  </el-tab-pane>
                  <el-tab-pane label="使用变量" name="third">
                    <uservariables style="width: 950px"></uservariables>
                  </el-tab-pane>
                </el-tabs>
              </el-tab-pane>
              <el-tab-pane label="前置条件" name="xxx1">
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
                      <el-button
                        type="primary"
                        size="mini"
                        icon="el-icon-plus"
                        v-if="hasPermission('testscene:scenecasecondition')"
                        @click.native.prevent="showAddSceneCasedelayconditionDialog"
                      >添加前置延时</el-button>
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
                        @click.native.prevent="showscenecaseconditionorderDialog"
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
                  <el-table-column label="前置条件名" :show-overflow-tooltip="true"  align="center" prop="subconditionname" width="110"/>
                  <el-table-column label="前置所属" :show-overflow-tooltip="true"  align="center" prop="conditionname" width="110"/>
                  <el-table-column label="前置接口" :show-overflow-tooltip="true"  align="center" prop="casename" width="110"/>
                  <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="110">
                    <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
                  </el-table-column>
                  <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="110">
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
                2.延时前置条件：
                <el-table
                  :data="delayconditionList"
                  :key="itemKey"
                  v-loading.body="listLoading"
                  element-loading-text="loading"
                  border
                  fit
                  highlight-current-row
                >
                  <el-table-column label="编号" align="center" width="60">
                    <template slot-scope="scope">
                      <span v-text="delaycasegetIndex(scope.$index)"></span>
                    </template>
                  </el-table-column>

                  <el-table-column label="前置条件名" align="center" prop="subconditionname" width="110"/>
                  <el-table-column label="前置所属" align="center" prop="conditionname" width="110"/>
                  <el-table-column label="等待时间(秒)" align="center" prop="delaytime" width="110">
                  </el-table-column>
                  <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="110">
                    <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
                  </el-table-column>
                  <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="110">
                    <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
                    </template>
                  </el-table-column>
                  <el-table-column label="管理" align="center"
                                   v-if="hasPermission('delaycondition:update')  || hasPermission('delaycondition:delete')">
                    <template slot-scope="scope">
                      <el-button
                        type="warning"
                        size="mini"
                        v-if="hasPermission('delaycondition:update') && scope.row.id !== id"
                        @click.native.prevent="showUpdatedelayconditionDialog(scope.$index)"
                      >修改</el-button>
                      <el-button
                        type="danger"
                        size="mini"
                        v-if="hasPermission('delaycondition:delete') && scope.row.id !== id"
                        @click.native.prevent="removedelaycondition(scope.$index)"
                      >删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                3.数据库前置条件：
                <el-table
                  :data="dbconditioncaseList"
                  table-layout='auto'
                  class="tableAuto"
                  element-loading-text="loading"
                  border
                  fit
                  highlight-current-row
                >
                  <el-table-column label="编号" align="center" width="50">
                    <template slot-scope="scope">
                      <span v-text="dbconditioncaseIndex(scope.$index)"></span>
                    </template>
                  </el-table-column>
                  <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="100"/>
                  <el-table-column label="前置所属" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
                  <el-table-column label="环境" align="center" prop="enviromentname" width="100"/>
                  <el-table-column label="组件名" align="center" prop="assemblename" width="100"/>
                  <el-table-column label="Sql类型" align="center" prop="dbtype" width="70"/>
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

                  <el-table-column label="管理" align="center"  width="250"
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
                        v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
                        @click.native.prevent="showdbvariablesDialog(scope.$index)"
                      >提取变量</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                4.脚本前置条件
                <el-table
                  :data="scriptconditionList"
                  element-loading-text="loading"
                  border
                  fit
                  highlight-current-row
                >
                  <el-table-column label="编号" align="center" width="60">
                    <template slot-scope="scope">
                      <span v-text="scriptcasegetIndex(scope.$index)"></span>
                    </template>
                  </el-table-column>

                  <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="110"/>
                  <el-table-column label="前置所属" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
                  <el-table-column label="脚本" align="center" prop="script" width="110">
                    <template slot-scope="scope">
                      <el-popover trigger="hover" placement="top">
                        <p>{{ scope.row.script }}</p>
                        <div slot="reference" class="name-wrapper">
                          <el-tag size="medium">...</el-tag>
                        </div>
                      </el-popover>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作人" align="center" prop="creator" width="70"/>
                  <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="110">
                    <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
                  </el-table-column>
                  <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="110">
                    <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
                    </template>
                  </el-table-column>

                  <el-table-column label="管理" width="250" align="center"
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
              </el-tab-pane>
            </el-tabs>
            </el-form>
          </el-form>
        </div>
      </div>

      <div style="height: 350px">
        <el-form :inline="true">
          <el-form-item label="状态:" prop="status" >
            <el-select v-model="debugsearch.status" style="width:100%" placeholder="状态">
              <el-option label="成功" value="成功"></el-option>
              <el-option label="失败" value="失败"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input clearable maxlength="40" v-model="debugsearch.batchname" @keyup.enter.native="searchcaseReportBy" placeholder="调试批次"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input clearable maxlength="40" v-model="debugsearch.casename" @keyup.enter.native="searchcaseReportBy" placeholder="用例名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchcaseReportBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>
          <el-table
            :data="apireportList"
            :key="itemKey"
            v-loading.body="listLoading"
            element-loading-text="loading"
            border
            fit
            highlight-current-row
          >
            <el-table-column label="编号" align="center" width="50">
              <template slot-scope="scope">
                <span v-text="debuggetIndex(scope.$index)"></span>
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true"  label="调试批次" align="center" prop="batchname" width="210"/>
            <el-table-column label="测试场景" align="center" prop="scenename" width="80"/>
            <el-table-column label="用例名" align="center" prop="casename" width="120"/>
<!--            <el-table-column label="API" align="center" prop="apiname" width="80"/>-->
            <el-table-column label="请求方式" align="center" prop="requestmethod" width="80"/>

<!--            <el-table-column label="状态" align="center" prop="status" width="50">-->
<!--              <template slot-scope="scope">-->
<!--                <span v-if="scope.row.status === '失败'" style="color:red">{{ scope.row.status }}</span>-->
<!--                <span v-else style="color: #37B328">{{ scope.row.status }}</span>-->
<!--              </template>-->
<!--            </el-table-column>-->
<!--            <el-table-column label="微服务" align="center" prop="deployunitname" width="120"/>-->


            <el-table-column :show-overflow-tooltip="true"  label="请求地址" align="center" prop="url" width="190">
            </el-table-column>

            <el-table-column label="请求头" align="center" prop="requestheader" width="80">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.requestheader }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">...</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column label="请求数据" align="center" prop="requestdatas" width="80">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top-start">
                  <p>{{ scope.row.requestdatas }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">...</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column label="响应" align="center" prop="respone" width="80">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.respone }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">...</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column label="断言" align="center" prop="expect" width="80">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.expect }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">...</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>

<!--            <el-table-column label="断言结果" align="center" prop="assertvalue" width="80">-->
<!--              <template slot-scope="scope">-->
<!--                <el-popover trigger="hover" placement="top">-->
<!--                  <p>{{ scope.row.assertvalue }}</p>-->
<!--                  <div slot="reference" class="name-wrapper">-->
<!--                    <el-tag size="medium" >...</el-tag>-->
<!--                  </div>-->
<!--                </el-popover>-->
<!--              </template>-->
<!--            </el-table-column>-->

            <el-table-column label="运行时间(ms)" align="center" prop="runtime" width="100"/>

<!--            <el-table-column label="异常信息" align="center" prop="errorinfo" width="80">-->
<!--              <template slot-scope="scope">-->
<!--                <el-popover trigger="hover" placement="top">-->
<!--                  <p>{{ scope.row.errorinfo }}</p>-->
<!--                  <div v-if="scope.row.errorinfo !== ''" slot="reference" class="name-wrapper">-->
<!--                    <el-tag size="medium" style="color:red">异常...</el-tag>-->
<!--                  </div>-->
<!--                  <div v-if="scope.row.errorinfo === ''" slot="reference" class="name-wrapper">-->
<!--                    <el-tag size="medium" style="color:green">...</el-tag>-->
<!--                  </div>-->
<!--                </el-popover>-->
<!--              </template>-->
<!--            </el-table-column>-->

            <el-table-column label="创建时间" align="center" prop="createTime" width="150">
              <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
            </el-table-column>
          </el-table>

        <el-pagination
          @size-change="debughandleSizeChange"
          @current-change="debughandleCurrentChange"
          :current-page="debugsearch.page"
          :page-size="debugsearch.size"
          :total="debugtotal"
          :page-sizes="[5]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import {
  search as searchcase
} from '@/api/assets/apicases'
import { search as searchscriptvariables, addscriptvariables, updatescriptvariables, removescriptvariables } from '@/api/testvariables/scriptvariables'
import { search as getscriptconditionList, addscriptcondition, updatescriptcondition, removescriptcondition } from '@/api/condition/scriptcondition'
import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
import { search, addtestscene, updatetestscene, removetestscene, getsceneallList, copyscene, searchscenetreedata } from '@/api/executecenter/testscene'
import { findcasebyscenenid, findscenecasebyid, addtestscenetestcase, updatescenenCaseorder, updatescenecaselogic, removetestscenecase } from '@/api/executecenter/testscenetestcase'
import { unix2CurrentTime } from '@/utils'
import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'
import { search as searchapicondition, addapicondition, removeapicondition, updateapicondition } from '@/api/condition/apicondition'
import { search as searchdbcondition, adddbcondition, updatedbcondition, removedbcondition } from '@/api/condition/dbcondition'
import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
import { getassembleallnameList as getassembleallnameList } from '@/api/enviroment/enviromentassemble'
import { adddelaycondition, updatedelaycondition, removedelaycondition, searchbytype } from '@/api/condition/delaycondition'
import { mapGetters } from 'vuex'
import { search as searchdbvariables, adddbvariables, updatedbvariables, removedbvariables } from '@/api/testvariables/dbvariables'
import uservariables from '@/components/testvariables'
import { addtestvariables, updatetestvariables, removetestvariables, findtestvariablesbycaseid } from '@/api/testvariables/testvariables'
import { getparamvaluebycaseidandtype as getparamvaluebycaseidandtype, casevalueforbody as casevalueforbody, updatepropertydata, updateapicasesdata } from '@/api/assets/apicasesdata'
import { getapi } from '@/api/deployunit/api'
import { findMacAndDepWithEnv as findMacAndDepWithEnv } from '@/api/enviroment/macdepunit'
import { searchconditionorder, addconditionorder } from '@/api/condition/conditionorder'
import { runscenecasetest, runscenetest } from '@/api/assets/apicases'
import { findscenecasedebugreportWithName } from '@/api/reportcenter/scenecases_debug_report'

export default {
  name: '测试场景',
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

  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  data() {
    return {
      tmpstatus: '',
      tmpcasename: '',
      tmpbatchname: '',
      debugsearch: {
        page: 1,
        size: 5,
        batchname: '',
        casename: '',
        status: '',
        sceneid: '',
        projectid: ''
      },
      debugtotal: 0,
      apireportList: [],
      tmpnode: null,
      tmptestdata: {
        conditionid: 0,
        globalheaderid: 0,
        caseid: '',
        sceneid: '',
        scenename: '',
        casename: '',
        enviromentid: '',
        prixflag: '',
        projectid: '',
        batchname: 'test111'
      },
      tmptest: {
        casename: '',
        enviromentid: '',
        enviromentname: '',
        respone: '',
        code: '',
        responeTime: '',
        size: '',
        general: '',
        requestdata: ''
      },
      activeinfoName: 'zero',
      searchtree: {
        scenename: null,
        sceneid: ''
      },
      activebbbName: 'xxx',
      activeaaaName: 'zero',
      filterText: '',
      SeceneDebugdatatest: null,
      SeceneDebugdata: [{
        id: 1,
        label: '测试场景',
        children: [{
          id: 4,
          label: '用例1'
        }, {
          id: 4,
          label: '用例2'
        }
        ]
      }
      ],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      value: null,
      DebugScenedialogFormVisible: false,
      tmpsubconditionid: '',
      conditionorderList: [], // 条件顺序显示列表
      saveconditionorderList: [], // 条件顺序保存列表
      searchconditionorder: {
        subconditionid: '',
        conditiontype: ''
      },
      ConditionOrderdialogFormVisible: false,
      id: null,
      activeName: 'zero',
      ApicasesVariablesList: [],
      Headertabledatas: [],
      xxxHeadertabledatas: [],
      Paramstabledatas: [],
      Bodytabledatas: [],
      BodyVisible: false,
      BodyParamDataVisible: false,
      BodyDataVisible: false,
      tmpexecuteplanid: null,
      tmpaddcaseexecuteplanid: null,
      tmpdeployunitid: null,
      tmpaddcasedeployunitid: null,
      tmpaddcaseapiid: null,
      tmpapiid: null,
      itemKey: null,
      itemaddcaseKey: null,
      tmptestscenename: '',
      testsceneList: [], // 列表
      testscenecaseList: [], // 列表
      addtestcaselastList: [],
      casemultipleSelection: [], // 查询用例表格被选中的内容
      testcaseList: [], // 装载用例列表
      apiList: [], // api列表
      addcaseapiList: [], // api列表
      apiconditionapiList: [],
      addcasemodelList: [],
      modelList: [],
      apiconditionmodelList: [],
      addcasedeployunitList: [], // 微服务列表
      deployunitList: [], // 微服务列表
      apiconditioncaseList: [],
      conditionapicaseList: [],
      dbconditioncaseList: [],
      enviromentnameList: [],
      enviroment_assembleList: [],
      sourcetestsceneList: [],
      delayconditionList: [],
      scriptconditionList: [],
      dbvariablesList: [],
      listLoading: false, // 数据加载等待动画
      addcaselistLoading: false, // 用例列表页面数据加载等待动画
      total: 0, // 数据总数
      casetotal: 0, // 用例数据总数
      addcasetotal: 0, // 用例数据总数
      dbvariablestotal: 0,
      apicasevariablestotal: 0,
      dialogStatus: 'add',
      DelaydialogStatus: 'add',
      apiconditiondialogStatus: 'add',
      scriptdialogStatus: 'add',
      dbdialogStatus: 'add',
      dbvariablesdialogStatus: 'add',
      dialogFormVisible: false,
      testscenecasedialogFormVisible: false,
      addtestscenecasedialogFormVisible: false,
      sceneConditionFormVisible: false,
      scenecaseConditionFormVisible: false,
      caseconditiondialogFormVisible: false,
      dbconditiondialogFormVisible: false,
      CopysceneFormVisible: false,
      DelaydialogFormVisible: false,
      scriptdialogFormVisible: false,
      LogicdialogFormVisible: false,
      dbVariablesDialogFormVisible: false,
      adddbvariablesdialogFormVisible: false,
      caseVariablesDialogFormVisible: false,
      caseaddvariablesdialogFormVisible: false,
      casedataialogFormVisible: false,
      tmpcopyscene: {
        sourcesceneid: '',
        sourcescenename: '',
        newscenename: ''
      },
      dbvariablestextMap: {
        updateRole: '修改API用例',
        update: '修改数据库变量',
        add: '添加数据库变量'
      },
      textMap: {
        updateRole: '修改场景',
        update: '修改场景',
        add: '添加场景'
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
      caseaddvariablestextMap: {
        update: '修改变量',
        add: '添加变量'
      },
      caseaddvariablesdialogStatus: 'add',
      btnLoading: false, // 按钮等待动画
      caseQuery: {
        page: 1, // 页码
        size: 10, // 每页数量
        deployunitid: 0,
        modelid: 0,
        deployunitname: ''
      },
      addcaseQuery: {
        page: 1, // 页码
        size: 10, // 每页数量
        deployunitid: 0,
        modelid: 0,
        deployunitname: ''
      },
      tmpscenecase: {
        scenecaseid: '',
        loop: '',
        stopitem: ''
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
      tmptestscene: {
        id: '',
        scenename: '',
        usetype: '',
        creator: '',
        memo: '',
        projectid: ''
      },
      tmpscenecaselogic: {
        id: '',
        loopnums: '',
        stopflag: ''
      },
      tmpsearchscenecase: {
        id: ''
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
      searchcase: {
        page: 1,
        size: 10,
        testscenenid: '',
        testscenenname: null,
        scenentype: '',
        executeplanid: null,
        executeplanname: null,
        deployunitid: null,
        deployunitname: null,
        modelid: 0,
        modelname: '',
        apiid: null,
        apiname: null,
        casetype: null
      },
      addsearchcase: {
        page: 1,
        size: 10,
        executeplanid: null,
        executeplanname: null,
        deployunitid: null,
        deployunitname: null,
        modelid: null,
        modelname: null,
        apiid: null,
        apiname: null,
        creator: '',
        casetype: null,
        projectid: ''
      },
      search: {
        page: 1,
        size: 10,
        scenename: null,
        accountId: null,
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
      searchallscene: {
        projectid: ''
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
      searchapicasevariables: {
        page: 1,
        size: 10,
        // caseid: '',
        projectid: ''
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
        paramstype: ''
      },
      tmpapicase: {
        caseid: '',
        casename: '',
        apiid: ''
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
      scriptVariablestextMap: {
        updateRole: '修改API用例',
        update: '修改脚本变量',
        add: '添加脚本变量'
      },
      scriptVariablesdialogStatus: 'add',
      addscriptVariablesdialogFormVisible: false,
      scriptVariablesDialogFormVisible: false,
      searchscriptvariables: {
        page: 1,
        size: 10,
        conditionid: '',
        projectid: ''
      },
      scriptvariablesList: [],
      scriptvariablestotal: 0,
      searchassemble: {
        page: 1,
        size: 100,
        assembletype: '组件',
        envid: ''
      }
    }
  },

  created() {
    this.search.accountId = this.accountId
    this.addsearchcase.projectid = window.localStorage.getItem('pid')
    this.tmptestdata.projectid = window.localStorage.getItem('pid')
    this.debugsearch.projectid = window.localStorage.getItem('pid')
    this.search.projectid = window.localStorage.getItem('pid')
    this.getdepunitLists()
    this.searchapicondition.projectid = window.localStorage.getItem('pid')
    this.searchdbcondition.projectid = window.localStorage.getItem('pid')
    this.searchscriptcondition.projectid = window.localStorage.getItem('pid')
    this.searchallscene.projectid = window.localStorage.getItem('pid')
    this.Scenedelaysearch.projectid = window.localStorage.getItem('pid')
    this.searchdbvariables.projectid = window.localStorage.getItem('pid')
    this.searchscriptvariables.projectid = window.localStorage.getItem('pid')
    this.searchapicasevariables.projectid = window.localStorage.getItem('pid')
    this.gettestsceneList()
    this.getassembleallnameList()
    this.getenviromentallList()
    this.getsceneallList()
  },

  activated() {
    this.getdepunitLists()
    this.gettestsceneList()
  },

  computed: {
    ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
  },

  methods: {
    getscenedebugreportList() {
      this.debugsearch.status = this.tmpstatus
      this.debugsearch.batchname = this.tmpbatchname
      this.debugsearch.casename = this.tmpcasename
      findscenecasedebugreportWithName(this.debugsearch).then(response => {
        this.apireportList = response.data.list
        this.debugtotal = response.data.total
      }).catch(res => {
        this.$message.error('加载用例列表失败')
      })
    },

    searchcaseReportBy() {
      findscenecasedebugreportWithName(this.debugsearch).then(response => {
        this.apireportList = response.data.list
        this.debugtotal = response.data.total
      }).catch(res => {
        this.$message.error('加载用例结果报告列表失败')
      })
      this.tmpstatus = this.debugsearch.status
      this.tmpbatchname = this.debugsearch.batchname
      this.tmpcasename = this.debugsearch.casename
    },

    EnviromentselectChanged(e) {
      this.tmptest.respone = ''
      for (let i = 0; i < this.enviromentnameList.length; i++) {
        if (this.enviromentnameList[i].enviromentname === e) {
          this.tmptestdata.enviromentid = this.enviromentnameList[i].id
        }
      }
    },
    /**
     * 调试
     */
    runtest() {
      if (this.tmptest.casename === '') {
        this.$message.error('请选择需要调试的目标')
        return
      }
      this.$refs.tmptest.validate(valid => {
        if (valid) {
          if (this.tmpnode.level === 1) {
            this.tmptestdata.sceneid = this.tmpnode.data.id
            this.tmptestdata.scenename = this.tmpnode.data.label
            runscenetest(this.tmptestdata).then(response => {
              // this.apireportList = response.data.list
              this.$message.success('调试进行中，可到调试报告中查询结果')
            }).catch(res => {
              this.$message.error('调试失败')
            })
            console.log('测试场景')
          } else {
            console.log('测试用例-----------------------------------------------')
            this.tmptestdata.sceneid = this.tmpnode.parent.data.id
            this.tmptestdata.scenename = this.tmpnode.parent.data.label
            this.tmptestdata.caseid = this.tmpnode.data.caseid
            this.tmptestdata.casename = this.tmpnode.data.label
            console.log(this.tmptestdata)
            runscenecasetest(this.tmptestdata).then(response => {
              this.apireportList = response.data
              this.$message.success('调试进行中，可到调试报告中查询结果')
              console.log(response.data)
              this.$message.error(response.data)
            }).catch(res => {
              this.$message.error('调试失败')
            })
          }
        }
      })
    },

    async clickNode(data, node, obj) {
      this.tmpnode = node
      this.tmptest.casename = node.data.label
      if (node.level === 1) {
        console.log('测试场景')
        this.searchconditionorder.conditiontype = 'scene'
        this.Headertabledatas = []
        this.Paramstabledatas = []
        this.tmpapicasesbodydata.apiparamvalue = null
        this.Bodytabledatas = []
        this.tmpsubconditionid = node.data.id
        this.tmpapicondition.conditionid = node.data.id
        this.tmpapicondition.conditionname = node.data.label
        this.tmpapicondition.conditiontype = 'scene'
        this.searchapicondition.conditiontype = 'scene'
        this.searchapicondition.conditionid = node.data.id
        this.searchdbcondition.conditiontype = 'scene'
        this.searchdbcondition.conditionid = node.data.id
        this.tmpdbcondition.conditionid = node.data.id
        this.tmpdbcondition.conditionname = node.data.label
        this.tmpdbcondition.conditiontype = 'scene'
        this.Scenedelaysearch.conditionid = node.data.id
        this.Scenedelaysearch.conditiontype = 'scene'
        this.tmpdelaycondition.conditionid = node.data.id
        this.tmpdelaycondition.conditionname = node.data.label
        this.tmpdelaycondition.conditiontype = 'scene'
        this.tmpscriptcondition.conditionid = node.data.id
        this.tmpscriptcondition.conditionname = node.data.label
        this.tmpscriptcondition.conditiontype = 'scene'
        this.searchscriptcondition.conditiontype = 'scene'
        this.searchscriptcondition.conditionid = node.data.id
      } else {
        console.log('测试场景用例')
        this.searchconditionorder.conditiontype = 'case'
        this.tmptestdata.caseid = node.data.id
        this.activeaaaName = 'zero'
        this.tmpapicase.caseid = node.data.id
        this.tmpapicase.casename = node.data.label
        this.tmpapicase.apiid = node.data.apiid
        this.tmpapicasesbodydata.caseid = node.data.id
        this.tmpapicasesbodydata.casename = node.data.label
        this.getheaderdatabycaseidandtype()
        this.getparamdatabycaseidandtype()
        await this.getapi()
        this.getbodytextdatabycaseidandtype()
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
        }
        // 获取前置条件
        this.tmpsubconditionid = node.data.caseid
        this.tmpapicondition.conditionid = node.data.caseid
        this.tmpapicondition.conditionname = node.data.label
        this.tmpapicondition.conditiontype = 'case'
        this.searchapicondition.conditiontype = 'case'
        this.searchapicondition.conditionid = node.data.caseid
        this.searchdbcondition.conditiontype = 'case'
        this.searchdbcondition.conditionid = node.data.caseid
        this.tmpdbcondition.conditionid = node.data.caseid
        this.tmpdbcondition.conditionname = node.data.label
        this.tmpdbcondition.conditiontype = 'case'
        this.Scenedelaysearch.conditionid = node.data.caseid
        this.Scenedelaysearch.conditiontype = 'case'
        this.tmpdelaycondition.conditionid = node.data.caseid
        this.tmpdelaycondition.conditionname = node.data.label
        this.tmpdelaycondition.conditiontype = 'case'
        this.tmpscriptcondition.conditionid = node.data.caseid
        this.tmpscriptcondition.conditionname = node.data.label
        this.tmpscriptcondition.conditiontype = 'case'
        this.searchscriptcondition.conditiontype = 'case'
        this.searchscriptcondition.conditionid = node.data.caseid
      }
      this.getapiconditionList()
      this.getdelayconditionList()
      this.getdbconditionList()
      this.getscriptconditionList()
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    unix2CurrentTime,
    showscenecaseconditionorderDialog() {
      // 显示新增对话框
      this.ConditionOrderdialogFormVisible = true
      this.searchconditionorder.subconditionid = this.tmpsubconditionid
      this.searchConditionorder()
    },

    searchConditionorder() {
      searchconditionorder(this.searchconditionorder).then(response => {
        this.conditionorderList = response.data
      }).catch(res => {
        this.$message.error('查询条件顺序失败')
      })
    },

    searchscenetreedata() {
      searchscenetreedata(this.searchtree).then(response => {
        this.SeceneDebugdata = response.data
        console.log(response.data)
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

    findMacAndDepWithEnv() {
      findMacAndDepWithEnv(this.searchassemble).then(response => {
        this.enviroment_assembleList = response.data.list
      }).catch(res => {
        this.$message.error('获取组件列表失败')
      })
    },

    scriptvariableshandleSizeChange(size) {
      this.searchscriptvariables.page = 1
      this.searchscriptvariables.size = size
      this.getscriptvariablesList()
    },

    scriptvariableshandleCurrentChange(page) {
      this.searchscriptvariables.page = page
      this.getscriptvariablesList()
    },

    scriptvariablesgetIndex(index) {
      return (this.searchscriptvariables.page - 1) * this.searchscriptvariables.size + index + 1
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

    getscriptvariablesList() {
      searchscriptvariables(this.searchscriptvariables).then(response => {
        this.scriptvariablesList = response.data.list
        this.scriptvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载变量列表失败')
      })
    },
    showscriptvariablesDialog(index) {
      // 显示新增对话框
      this.scriptVariablesDialogFormVisible = true
      this.tmpscriptvariables.conditionid = this.scriptconditionList[index].id
      this.searchscriptvariables.conditionid = this.scriptconditionList[index].id
      this.tmpscriptvariables.conditionname = this.scriptconditionList[index].subconditionname
      this.getscriptvariablesList()
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

    apicasevariableshandleCurrentChange(page) {
      this.searchapicasevariables.page = page
      this.findtestvariablesbycaseid()
    },

    apicasevariableshandleSizeChange(size) {
      this.searchapicasevariables.page = 1
      this.searchapicasevariables.size = size
      this.findtestvariablesbycaseid()
    },

    ApicasesVariablesgetIndex(index) {
      return (this.searchapicasevariables.page - 1) * this.searchapicasevariables.size + index + 1
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

    showCaseVariablesforConditionDialog(index) {
      this.caseVariablesDialogFormVisible = true
      this.tmptestvariables.caseid = this.apiconditioncaseList[index].caseid
      this.tmptestvariables.casename = this.apiconditioncaseList[index].casename
      this.findtestvariablesbycaseid()
    },

    findtestvariablesbycaseid() {
      findtestvariablesbycaseid(this.searchapicasevariables).then(response => {
        this.ApicasesVariablesList = response.data.list
        this.apicasevariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('获取用例变量列表失败')
      })
    },

    selectparamsChanged(e) {
      this.getcaseparamsbytype(e)
    },
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
    getheaderdatabycaseidandtype() {
      var casedata = { caseid: this.tmpapicase.caseid, propertytype: 'Header' }
      getparamvaluebycaseidandtype(casedata).then(response => {
        this.Headertabledatas = response.data.list
      }).catch(res => {
        this.$message.error('获取Header参数值失败')
      })
    },
    getparamdatabycaseidandtype(property) {
      var casedata = { caseid: this.tmpapicase.caseid, propertytype: 'Params' }
      getparamvaluebycaseidandtype(casedata).then(response => {
        this.Paramstabledatas = response.data.list
      }).catch(res => {
        this.$message.error('获取Params参数值失败')
      })
    },

    getbodydatabycaseidandtype(property) {
      var casedata = { caseid: this.tmpapicase.caseid, propertytype: 'Body' }
      getparamvaluebycaseidandtype(casedata).then(response => {
        this.Bodytabledatas = response.data.list
      }).catch(res => {
        this.$message.error('获取Body参数值失败')
      })
    },

    getbodytextdatabycaseidandtype() {
      var casedata = { caseid: this.tmpapicase.caseid, propertytype: 'Body' }
      getparamvaluebycaseidandtype(casedata).then(response => {
        console.log(response.data.list)
        if (response.data.list.length > 0) {
          this.tmpapicasesbodydata = response.data.list[0]
          console.log(666666666666666)
          console.log(this.tmpapicasesbodydata)
        } else {
          this.tmpapicasesbodydata.id = ''
          this.tmpapicasesbodydata.apiparamvalue = ''
        }
      }).catch(res => {
        this.$message.error('获取Body文本参数值失败')
      })
    },

    updateapicasesdata() {
      // this.tmpapicasesbodydata.caseid = this.tmpapicases.id
      // this.tmpapicasesbodydata.casename = this.tmpapicases.casename
      this.tmpapicasesbodydata.apiparam = 'Body'
      this.tmpapicasesbodydata.paramstype = this.tmpapi.requestcontenttype
      this.tmpapicasesbodydata.propertytype = 'Body'
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
    async addnewapicasesdata() {
      this.updateHeaderpropertydata(this.Headertabledatas)
      this.updateHeaderpropertydata(this.Paramstabledatas)
      await this.getapi()
      if (this.tmpapi.requestcontenttype === 'Form表单') {
        this.updateHeaderpropertydata(this.Bodytabledatas)
      } else {
        this.updateapicasesdata()
      }
      this.$message.success('保存成功')
      this.casedataialogFormVisible = false
    },
    async showtestcasedataDialog(index) {
      console.log(index)
      console.log(this.testscenecaseList)
      this.casedataialogFormVisible = true
      this.activeName = 'zero'
      this.tmpapicase.caseid = this.testscenecaseList[index].testcaseid
      this.tmpapicase.casename = this.testscenecaseList[index].casename
      this.tmpapicase.apiid = this.testscenecaseList[index].apiid
      this.tmpapicasesbodydata.caseid = this.testscenecaseList[index].testcaseid
      this.tmpapicasesbodydata.casename = this.testscenecaseList[index].casename
      console.log(11111111111111111111111)
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

    async getapi() {
      await getapi(this.tmpapicase.apiid).then(response => {
        this.tmpapi = response.data
      }).catch(res => {
        this.$message.error('获取api失败')
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

    getdbvariablesList() {
      searchdbvariables(this.searchdbvariables).then(response => {
        this.dbvariablesList = response.data.list
        this.dbvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载变量列表失败')
      })
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

    dbvariableshandleCurrentChange(page) {
      this.searchdbvariables.page = page
      this.getdbvariablesList()
    },

    dbvariableshandleSizeChange(size) {
      this.searchdbvariables.page = 1
      this.searchdbvariables.size = size
      this.getdbvariablesList()
    },

    dbVariablesgetIndex(index) {
      return (this.searchdbvariables.page - 1) * this.searchdbvariables.size + index + 1
    },

    showdbvariablesDialog(index) {
      // 显示新增对话框
      this.dbVariablesDialogFormVisible = true
      this.tmpdbvariables.conditionid = this.dbconditioncaseList[index].id
      this.searchdbvariables.conditionid = this.dbconditioncaseList[index].id
      this.tmpdbvariables.conditionname = this.dbconditioncaseList[index].subconditionname
      this.getdbvariablesList()
    },

    updatescenecaselogic() {
      this.$refs.tmpscenecaselogic.validate(valid => {
        if (valid) {
          updatescenecaselogic(this.tmpscenecaselogic).then(() => {
            this.$message.success('更新成功')
            this.tmpsearchscenecase.id = this.tmpscenecaselogic.id
            this.LogicdialogFormVisible = false
          }).catch(res => {
            this.$message.error('更新失败')
          })
        }
      })
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
    /**
     * 更新脚本条件
     */
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

    showAddscriptconditionDialog() {
      // 显示新增对话框
      this.scriptdialogFormVisible = true
      this.scriptdialogStatus = 'add'
      this.tmpscriptcondition.id = ''
      this.tmpscriptcondition.subconditionname = ''
      this.tmpscriptcondition.conditionname = ''
      this.tmpscriptcondition.script = ''
      this.tmpscriptcondition.creator = this.name
      this.tmpscriptcondition.projectid = window.localStorage.getItem('pid')
    },
    /**
     * 添加脚本条件
     */
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

    getscriptconditionList() {
      getscriptconditionList(this.searchscriptcondition).then(response => {
        this.scriptconditionList = response.data.list
      }).catch(res => {
        this.$message.error('加载测试脚本条件列表失败')
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

    showUpdatescriptconditionDialog(index) {
      this.scriptdialogFormVisible = true
      this.scriptdialogStatus = 'update'
      this.tmpscriptcondition.id = this.scriptconditionList[index].id
      this.tmpscriptcondition.subconditionname = this.scriptconditionList[index].subconditionname
      this.tmpscriptcondition.conditionname = this.scriptconditionList[index].conditionname
      this.tmpscriptcondition.script = this.scriptconditionList[index].script
      this.tmpscriptcondition.creator = this.name
    },
    /**
     * 显示添加延时条件对话框
     */
    showAddSceneCasedelayconditionDialog() {
      // 显示新增对话框
      this.DelaydialogFormVisible = true
      this.DelaydialogStatus = 'add'
      this.tmpdelaycondition.id = ''
      this.tmpdelaycondition.subconditionname = ''
      // this.tmpdelaycondition.conditionid = this.tmpapicondition.conditionid
      // this.tmpdelaycondition.conditionname = this.tmpapicondition.conditionname
      // this.tmpdelaycondition.conditiontype = 'scencecase'
      this.tmpdelaycondition.delaytime = ''
      this.tmpdelaycondition.creator = this.name
      this.tmpdelaycondition.projectid = window.localStorage.getItem('pid')
    },
    /**
     * 获取延时条件列表
     */
    getdelayconditionList() {
      searchbytype(this.Scenedelaysearch).then(response => {
        this.delayconditionList = response.data.list
      }).catch(res => {
        this.$message.error('加载测试延时条件列表失败')
      })
    },
    /**
     * 显示修改延时条件对话框
     * @param index 延时条件下标
     */
    showUpdatedelayconditionDialog(index) {
      this.DelaydialogFormVisible = true
      this.DelaydialogStatus = 'update'
      this.tmpdelaycondition.id = this.delayconditionList[index].id
      this.tmpdelaycondition.subconditionname = this.delayconditionList[index].subconditionname
      this.tmpdelaycondition.delaytime = this.delayconditionList[index].delaytime
      this.tmpdelaycondition.creator = this.name
    },
    /**
     * 删除延时条件
     * @param index 延时条件下标
     */
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
    /**
     * 添加延时条件
     */
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

    copyscene() {
      this.$refs.tmpcopyscene.validate(valid => {
        if (valid) {
          copyscene(this.tmpcopyscene).then(() => {
            this.$message.success('复制成功')
            this.gettestsceneList()
            this.CopysceneFormVisible = false
          }).catch(res => {
            this.$message.error('复制失败')
          })
        }
      })
    },

    CopySourceSceneChanged(e) {
      for (let i = 0; i < this.sourcetestsceneList.length; i++) {
        if (this.sourcetestsceneList[i].scenename === e) {
          this.tmpcopyscene.sourcesceneid = this.sourcetestsceneList[i].id
        }
      }
    },
    getsceneallList() {
      getsceneallList(this.searchallscene).then(response => {
        this.sourcetestsceneList = response.data
      }).catch(res => {
        this.$message.error('加载数据库条件列表失败')
      })
    },

    getenviromentallList() {
      this.listLoading = true
      getenviromentallList(this.search).then(response => {
        this.enviromentnameList = response.data
      }).catch(res => {
        this.$message.error('加载数据库条件列表失败')
      })
    },

    getassembleallnameList() {
      getassembleallnameList(this.searchdbcondition).then(response => {
        this.enviroment_assembleList = response.data
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

    getapiconditionList() {
      searchapicondition(this.searchapicondition).then(response => {
        this.apiconditioncaseList = response.data.list
      }).catch(res => {
        this.$message.error('加载测试接口条件列表失败')
      })
    },

    getdbconditionList() {
      searchdbcondition(this.searchdbcondition).then(response => {
        this.dbconditioncaseList = response.data.list
      }).catch(res => {
        this.$message.error('加载测试数据库条件列表失败')
      })
    },

    cancelEdit(row) {
      row.caseorder = row.oldcaseorder
      row.edit = false
      // this.$message({
      //   message: 'The title has been restored to the original value',
      //   type: 'warning'
      // })
    },
    confirmEdit(row) {
      row.edit = false
      updatescenenCaseorder(row).then(response => {
        row.oldcaseorder = row.caseorder
        this.$message.success('修改顺序成功')
      }).catch(res => {
        row.caseorder = row.oldcaseorder
        this.$message.error('修改顺序失败')
      })
      // console.log(22222222222222222)
      // console.log(row)
      // this.$message({
      //   message: 'The title has been edited',
      //   type: 'success'
      // })
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
    /**
     * 获取环境列表
     */
    gettestsceneList() {
      this.listLoading = true
      this.search.scenename = this.tmptestscenename
      search(this.search).then(response => {
        this.testsceneList = response.data.list
        this.total = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载环境列表失败')
      })
    },

    searchBy() {
      this.search.page = 1
      this.listLoading = true
      search(this.search).then(response => {
        this.itemKey = Math.random()
        this.testsceneList = response.data.list
        this.total = response.data.total
      }).catch(res => {
        this.$message.error('搜索失败')
      })
      this.tmptestscenename = this.search.scenename
      this.listLoading = false
      this.btnLoading = false
    },
    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.search.page = 1
      this.search.size = size
      this.gettestsceneList()
    },

    debughandleSizeChange(size) {
      this.debugsearch.page = 1
      this.debugsearch.size = size
      this.getscenedebugreportList()
    },

    casehandleSizeChange(size) {
      this.searchcase.page = 1
      this.searchcase.size = size
      this.gettestscenecaseList()
    },

    addcasehandleSizeChange(size) {
      this.addsearchcase.page = 1
      this.addsearchcase.size = size
      this.getaddcasesList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.search.page = page
      this.gettestsceneList()
    },

    debughandleCurrentChange(page) {
      this.debugsearch.page = page
      this.getscenedebugreportList()
    },

    /**
     * 改变页码
     * @param page 页号
     */
    casehandleCurrentChange(page) {
      this.searchcase.page = page
      this.gettestscenecaseList()
    },

    addcasehandleCurrentChange(page) {
      this.addsearchcase.page = page
      this.getaddcasesList()
    },
    addcasehandleClickTableRow(row, event, column) {
    },
    addcasehandleSelectionChange(rows) {
      this.casemultipleSelection = rows
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

    debuggetIndex(index) {
      return (this.debugsearch.page - 1) * this.debugsearch.size + index + 1
    },

    delaycasegetIndex(index) {
      return (this.Scenedelaysearch.page - 1) * this.Scenedelaysearch.size + index + 1
    },

    scriptcasegetIndex(index) {
      return (this.searchscriptcondition.page - 1) * this.searchscriptcondition.size + index + 1
    },

    scenecasegetIndex(index) {
      return (this.searchcase.page - 1) * this.searchcase.size + index + 1
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

    sceneaddcasegetIndex(index) {
      return (this.addsearchcase.page - 1) * this.addsearchcase.size + index + 1
    },

    gettestscenecaseList() {
      this.searchcase.executeplanid = this.tmpcaseexecuteplanid
      this.searchcase.deployunitid = this.tmpcasedeployunitid
      this.searchcase.apiid = this.tmpcaseapiid
      findcasebyscenenid(this.searchcase).then(response => {
        this.testscenecaseList = response.data.list
        const items = response.data.list
        this.testscenecaseList = items.map(v => {
          this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
          v.oldcaseorder = v.caseorder //  will be used when user click the cancel botton
          return v
        })
        this.casetotal = response.data.total
        this.listLoading = false
      }).catch(res => {
        this.$message.error('加载场景用例列表失败')
      })
    },

    searchscenecaseBy() {
      findcasebyscenenid(this.searchcase).then(response => {
        this.testscenecaseList = response.data.list
        this.casetotal = response.data.total
      }).catch(res => {
        this.$message.error('搜索失败')
      })
      this.tmpcaseexecuteplanid = this.searchcase.executeplanid
      this.tmpcasedeployunitid = this.searchcase.deployunitid
      this.tmpcaseapiid = this.searchcase.apiid
    },

    getaddcasesList() {
      this.addsearchcase.executeplanid = this.tmpaddcaseexecuteplanid
      this.addsearchcase.deployunitid = this.tmpaddcasedeployunitid
      this.addsearchcase.apiid = this.tmpaddcaseapiid
      this.addsearchcase.casetype = this.searchcase.casetype
      this.addsearchcase.creator = this.name
      searchcase(this.addsearchcase).then(response => {
        this.addtestcaselastList = response.data.list
        this.addcasetotal = response.data.total
        this.addcaselistLoading = false
      }).catch(res => {
        this.$message.error('加载用例列表失败')
      })
    },

    searchaddcaseBy() {
      this.addsearchcase.casetype = this.searchcase.casetype
      this.addsearchcase.creator = this.name
      searchcase(this.addsearchcase).then(response => {
        this.itemaddcaseKey = Math.random()
        this.addtestcaselastList = response.data.list
        this.addcasetotal = response.data.total
      }).catch(res => {
        this.$message.error('搜索失败')
      })
      this.tmpaddcaseexecuteplanid = this.addsearchcase.executeplanid
      this.tmpaddcasedeployunitid = this.addsearchcase.deployunitid
      this.tmpaddcaseapiid = this.addsearchcase.apiid
      this.listLoading = false
      this.btnLoading = false
      // this.getaddcasesList()
    },
    /**
     * 显示添加测试场景对话框
     */
    showAddtestsceneDialog() {
      // 显示新增对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'add'
      this.tmptestscene.id = ''
      this.tmptestscene.scenename = ''
      this.tmptestscene.usetype = ''
      this.tmptestscene.memo = ''
      this.tmptestscene.creator = this.name
      this.tmptestscene.projectid = window.localStorage.getItem('pid')
    },

    showCopytestsceneDialog() {
      // 显示新增对话框
      this.CopysceneFormVisible = true
      this.tmpcopyscene.newscenename = ''
      this.tmpcopyscene.sourcescenename = ''
      this.tmpcopyscene.sourcesceneid = ''
    },
    showtestsceneConditionDialog(index) {
      this.sceneConditionFormVisible = true
    },

    showtestscenecaseLogicDialog(index) {
      this.LogicdialogFormVisible = true
      this.tmpscenecaselogic.id = this.testscenecaseList[index].id
      this.tmpsearchscenecase.id = this.testscenecaseList[index].id
      findscenecasebyid(this.tmpsearchscenecase).then(response => {
        this.tmpscenecaselogic.loopnums = response.data.loopnums
        this.tmpscenecaselogic.stopflag = response.data.stopflag
      }).catch(res => {
        this.$message.error('搜索失败')
      })
    },

    showtestscenecaseConditionDialog(index) {
      this.scenecaseConditionFormVisible = true
      this.tmpsubconditionid = this.testscenecaseList[index].testcaseid
      this.tmpapicondition.conditionid = this.testscenecaseList[index].testcaseid
      this.tmpapicondition.conditionname = this.testscenecaseList[index].casename
      this.tmpapicondition.conditiontype = 'case'
      this.searchapicondition.conditiontype = 'case'
      this.searchapicondition.conditionid = this.testscenecaseList[index].testcaseid
      this.searchdbcondition.conditiontype = 'case'
      this.searchdbcondition.conditionid = this.testscenecaseList[index].testcaseid
      this.tmpdbcondition.conditionid = this.testscenecaseList[index].testcaseid
      this.tmpdbcondition.conditionname = this.testscenecaseList[index].casename
      this.tmpdbcondition.conditiontype = 'case'
      this.Scenedelaysearch.conditionid = this.testscenecaseList[index].testcaseid
      this.Scenedelaysearch.conditiontype = 'case'
      this.tmpdelaycondition.conditionid = this.testscenecaseList[index].testcaseid
      this.tmpdelaycondition.conditionname = this.testscenecaseList[index].casename
      this.tmpdelaycondition.conditiontype = 'case'
      this.tmpscriptcondition.conditionid = this.testscenecaseList[index].testcaseid
      this.tmpscriptcondition.conditionname = this.testscenecaseList[index].casename
      this.tmpscriptcondition.conditiontype = 'case'
      this.searchscriptcondition.conditiontype = 'case'
      this.searchscriptcondition.conditionid = this.testscenecaseList[index].testcaseid
      this.getapiconditionList()
      this.getdelayconditionList()
      this.getdbconditionList()
      this.getscriptconditionList()
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
    async showDebugSceneDialog(index) {
      this.debugtotal = 0
      this.apireportList = null
      this.tmptest.casename = ''
      this.tmptestdata.enviromentid = ''
      this.tmptest.enviromentname = ''
      this.Headertabledatas = null
      this.Paramstabledatas = null
      this.Bodytabledatas = null
      this.tmpapicasesbodydata.apiparamvalue = null
      this.apiconditioncaseList = null
      this.delayconditionList = null
      this.dbconditioncaseList = null
      this.scriptconditionList = null
      this.debugsearch.sceneid = this.testsceneList[index].id
      this.searchtree.sceneid = this.testsceneList[index].id
      this.searchtree.scenename = this.testsceneList[index].scenename
      this.searchscenetreedata()
      this.searchcase.testscenenid = this.testsceneList[index].id
      // this.gettestscenecaseList()
      this.activebbbName = 'xxx'
      this.activeaaaName = 'zero'
      this.DebugScenedialogFormVisible = true
    },
    /**
     * 显示用例对话框
     * @param index 测试集合下标
     */
    showtestsceneCaseDialog(index) {
      this.addtestcaselastList = null
      this.testscenecasedialogFormVisible = true
      this.searchcase.testscenenid = this.testsceneList[index].id
      this.searchcase.testscenenname = this.testsceneList[index].scenename
      this.searchcase.casetype = this.testsceneList[index].usetype
      this.gettestscenecaseList()
    },

    ShowAddCaseDialog(index) {
      this.addtestscenecasedialogFormVisible = true
    },
    /**
     * 添加测试场景
     */
    addtestscene() {
      this.$refs.tmptestscene.validate(valid => {
        if (valid) {
          this.btnLoading = true
          addtestscene(this.tmptestscene).then(() => {
            this.$message.success('添加成功')
            this.gettestsceneList()
            this.dialogFormVisible = false
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('添加失败')
            this.btnLoading = false
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

    /**
     * 装载测试集合的用例
     */
    addtestscenetestcase() {
      this.testcaseList = []
      if (this.casemultipleSelection.length === 0) {
        this.$message.error('请选择装载的用例')
      } else {
        for (let i = 0; i < this.casemultipleSelection.length; i++) {
          this.testcaseList.push({
            'testscenenid': this.searchcase.testscenenid,
            'scenename': this.searchcase.testscenenname,
            'modelid': this.searchcase.modelid,
            'modelname': this.searchcase.modelname,
            'deployunitid': this.casemultipleSelection[i].deployunitid,
            'apiid': this.casemultipleSelection[i].apiid,
            'deployunitname': this.casemultipleSelection[i].deployunitname,
            'apiname': this.casemultipleSelection[i].apiname,
            'testcaseid': this.casemultipleSelection[i].id,
            'caseorder': 0,
            'casename': this.casemultipleSelection[i].casename,
            'creator': this.name,
            'projectid': window.localStorage.getItem('pid')
          })
        }
        addtestscenetestcase(this.testcaseList).then(() => {
          this.addtestscenecasedialogFormVisible = false
          this.gettestscenecaseList()
          this.gettestsceneList()
          this.$message.success('装载成功')
        }).catch(res => {
          this.$message.error('装载失败')
        })
      }
    },
    /**
     * 显示修改测试场景对话框
     * @param index 测试场景下标
     */
    showUpdatetestsceneDialog(index) {
      this.dialogFormVisible = true
      this.dialogStatus = 'update'
      this.tmptestscene.id = this.testsceneList[index].id
      this.tmptestscene.scenename = this.testsceneList[index].scenename
      this.tmptestscene.usetype = this.testsceneList[index].usetype
      this.tmptestscene.memo = this.testsceneList[index].memo
      this.tmptestscene.creator = this.name
      this.tmptestscene.projectid = window.localStorage.getItem('pid')
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
    },
    /**
     * 更新测试场景
     */
    updatetestscene() {
      this.$refs.tmptestscene.validate(valid => {
        if (valid) {
          updatetestscene(this.tmptestscene).then(() => {
            this.$message.success('更新成功')
            this.gettestsceneList()
            this.dialogFormVisible = false
          }).catch(res => {
            this.$message.error('更新失败')
          })
        }
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
    /**
     * 删除测试场景
     * @param index 测试场景下标
     */
    removetestscene(index) {
      this.$confirm('删除该测试场景？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const id = this.testsceneList[index].id
        removetestscene(id).then(() => {
          this.$message.success('删除成功')
          this.gettestsceneList()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    /**
     * 删除测试场景
     * @param index 测试场景下标
     */
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
    /**
     * 删除测试场景
     * @param index 测试场景下标
     */
    removetestscenecase(index) {
      this.$confirm('删除该测试场景用例？', '警告', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }).then(() => {
        const id = this.testscenecaseList[index].id
        removetestscenecase(id).then(() => {
          this.$message.success('删除成功')
          this.gettestscenecaseList()
          this.gettestsceneList()
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    /**
     * 测试场景是否唯一
     * @param 测试场景
     */
    isUniqueDetail(testscene) {
      for (let i = 0; i < this.testsceneList.length; i++) {
        if (this.testsceneList[i].id !== testscene.id) { // 排除自己
          if (this.testsceneList[i].scenename === testscene.scenename) {
            this.$message.error('测试场景名已存在')
            return false
          }
        }
      }
      return true
    },

    searchdeployunitmodel(Query) {
      searchdeployunitmodel(Query).then(response => {
        this.modelList = response.data.list
      }).catch(res => {
        this.$message.error('加载服务模块列表失败')
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
    /**
     * 装载层微服务下拉选择事件获取微服务id  e的值为options的选值
     */
    deployunitselectChanged(e) {
      this.searchcase.modelid = 0
      this.searchcase.apiid = 0
      this.searchcase.deployunitid = 0
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.searchcase.deployunitid = this.deployunitList[i].id
        }
      }
      this.searchdeployunitmodel(this.searchcase)
      this.apiList = null
      this.searchcase.apiname = ''
      getapiListbydeploy(this.searchcase).then(response => {
        this.apiList = response.data
      }).catch(res => {
        this.$message.error('加载api列表失败')
      })
    },
    addcasedeployunitselectChanged(e) {
      this.addsearchcase.modelid = 0
      this.addsearchcase.apiid = 0
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.addsearchcase.deployunitid = this.deployunitList[i].id
        }
      }
      this.searchdeployunitmodel(this.addsearchcase)
      this.addcaseapiList = null
      this.addsearchcase.apiname = ''
      this.addsearchcase.deployunitname = e
      console.log(222222222222222222222222222222222222222222222)
      getapiListbydeploy(this.addsearchcase).then(response => {
        this.addcaseapiList = response.data
      }).catch(res => {
        this.$message.error('加载api列表失败')
      })
    },
    modelselectChanged(e) {
      this.searchcase.modelid = 0
      this.searchcase.apiid = 0
      for (let i = 0; i < this.modelList.length; i++) {
        if (this.modelList[i].modelname === e) {
          this.searchcase.modelid = this.modelList[i].id
        }
      }
      this.apiList = null
      this.searchcase.apiname = ''
      getapiListbydeploy(this.caseQuery).then(response => {
        this.apiList = response.data
      }).catch(res => {
        this.$message.error('加载api列表失败')
      })
    },

    addcasemodelselectChanged(e) {
      this.addsearchcase.modelid = 0
      this.addsearchcase.apiid = 0
      for (let i = 0; i < this.addcasemodelList.length; i++) {
        if (this.addcasemodelList[i].modelname === e) {
          this.addsearchcase.modelid = this.addcasemodelList[i].id
        }
      }
      this.addcaseapiList = null
      this.addsearchcase.apiname = ''
      this.addsearchcase.deployunitname = e
      getapiListbydeploy(this.addsearchcase).then(response => {
        this.addcaseapiList = response.data
      }).catch(res => {
        this.$message.error('加载api列表失败')
      })
    },
    /**
     * API下拉选择事件获取微服务id  e的值为options的选值
     */
    ApiselectChanged(e) {
      this.searchcase.apiid = 0
      for (let i = 0; i < this.apiList.length; i++) {
        if (this.apiList[i].apiname === e) {
          this.searchcase.apiid = this.apiList[i].id
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

    apiconditiontestcaseselectChanged(e) {
      for (let i = 0; i < this.conditionapicaseList.length; i++) {
        if (this.conditionapicaseList[i].casename === e) {
          this.tmpapicondition.caseid = this.conditionapicaseList[i].id
        }
      }
    },

    addcaseApiselectChanged(e) {
      this.addsearchcase.apiid = 0
      for (let i = 0; i < this.addcaseapiList.length; i++) {
        if (this.addcaseapiList[i].apiname === e) {
          this.addsearchcase.apiid = this.addcaseapiList[i].id
        }
      }
    }
  }
}
</script>


<style>
.container {
  display: flex;
  justify-content: space-between; /* 水平间隔地分布每个元素 */
}

.left-component {
  /* 左边组件的样式 */
}

.right-component {
  /* 右边组件的样式 */
}
.tree-scroll {
  width: 300px;
  border: 1px solid #E7E7E7;
  height: 100%
}
.flow-tree{
  overflow: auto;
  height: 200px;
  margin:  10px;

  >>>.el-tree-node{
> .el-tree-node__children{
  overflow: visible !important
}
}
}
.custom-tree {
  width: 300px; /* 设置宽度 */
  /* 其他样式 */
}
.tableAuto.el-table .cell {
  white-space: nowrap;
}
</style>
