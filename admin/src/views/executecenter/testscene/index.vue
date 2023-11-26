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
      <el-table-column label="类型" align="center" prop="usetype" width="150"/>
      <el-table-column label="操作人" align="center" prop="creator" width="100"/>
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
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('testscene:loadcase') && scope.row.id !== id"
            @click.native.prevent="showtestsceneCaseDialog(scope.$index)"
          >装载用例</el-button>
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

        <el-table-column label="apiid" v-if="show" align="center" prop="apiid" width="150"/>
        <el-table-column label="deployunitid" v-if="show" align="center" prop="deployunitid" width="150"/>
        <el-table-column label="用例名" align="center" prop="casename" width="150"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="150"/>
        <el-table-column label="API" align="center" prop="apiname" width="150"/>

        <el-table-column width="120" align="center" label="用例顺序">
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
              设置顺序
            </el-button>
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center" width="220"
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

    <el-dialog title="用例前置条件" :visible.sync="scenecaseConditionFormVisible">
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
<!--            <el-button-->
<!--              type="primary"-->
<!--              size="mini"-->
<!--              icon="el-icon-plus"-->
<!--              v-if="hasPermission('testscene:scenecasecondition')"-->
<!--              @click.native.prevent="AddcasedbconditionDialog"-->
<!--            >添加前置数据库</el-button>-->
<!--            <el-button-->
<!--              type="primary"-->
<!--              size="mini"-->
<!--              icon="el-icon-plus"-->
<!--              v-if="hasPermission('testscene:scenecasecondition')"-->
<!--              @click.native.prevent="showAddapiparamsDialog"-->
<!--            >添加前置脚本</el-button>-->

            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddSceneCasedelayconditionDialog"
            >添加前置延时</el-button>
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
        <el-table-column label="前置接口" align="center" prop="casename" width="150"/>
        <el-table-column label="所属用例" align="center" prop="conditionname" width="150"/>

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
            <span v-text="getIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="前置条件名" align="center" prop="subconditionname" width="200"/>
        <el-table-column label="等待时间(秒)" align="center" prop="delaytime" width="150">
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

<!--      2.数据库前置条件：-->

<!--      <el-table-->
<!--        :data="dbconditioncaseList"-->
<!--        v-loading.body="listLoading"-->
<!--        element-loading-text="loading"-->
<!--        border-->
<!--        fit-->
<!--        highlight-current-row-->
<!--      >-->
<!--        <el-table-column label="编号" align="center" width="45">-->
<!--          <template slot-scope="scope">-->
<!--            <span v-text="dbconditioncaseIndex(scope.$index)"></span>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="接口条件" align="center" prop="subconditionname" width="150"/>-->
<!--        <el-table-column label="接口" align="center" prop="conditionname" width="150"/>-->

<!--        <el-table-column label="管理" align="center"-->
<!--                         v-if="hasPermission('testscene:caseupdatedbcondition')  || hasPermission('testscene:casedeletedbcondition')">-->
<!--          <template slot-scope="scope">-->
<!--            <el-button-->
<!--              type="warning"-->
<!--              size="mini"-->
<!--              v-if="hasPermission('testscene:caseupdatedbcondition') && scope.row.id !== id"-->
<!--              @click.native.prevent="showUpdateparamsDialog(scope.$index)"-->
<!--            >修改</el-button>-->
<!--            <el-button-->
<!--              type="danger"-->
<!--              size="mini"-->
<!--              v-if="hasPermission('testscene:casedeletedbcondition') && scope.row.id !== id"-->
<!--              @click.native.prevent="removeexecuteplanparam(scope.$index)"-->
<!--            >删除</el-button>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--      </el-table>-->

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
            <el-option label="请选择" value />
            <div v-for="(api, index) in apiconditionapiList" :key="index">
              <el-option :label="api.apiname" :value="api.apiname"/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="接口" prop="casename" required >
          <el-select v-model="tmpapicondition.casename" filterable placeholder="接口" style="width:100%" @change="apiconditiontestcaseselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testcase, index) in apiconditioncaseList" :key="index">
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

    <el-dialog title="前置数据库" :visible.sync="dbconditiondialogFormVisible">
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
              <el-option :label="macname.assemblename" :value="macname.assemblename" required/>
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
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddbcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
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
        <el-table-column label="用例名" align="center" prop="casename" width="280"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="180"/>
        <el-table-column label="API" align="center" prop="apiname" width="280"/>
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

  </div>
</template>
<script>
import {
  search as searchcase
} from '@/api/assets/apicases'
import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
import { search, addtestscene, updatetestscene, removetestscene, getsceneallList, copyscene } from '@/api/executecenter/testscene'
import { findcasebyscenenid, addtestscenetestcase, updatescenenCaseorder, removetestscenecase } from '@/api/executecenter/testscenetestcase'
import { unix2CurrentTime } from '@/utils'
import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'
import { search as searchapicondition, addapicondition, removeapicondition, updateapicondition } from '@/api/condition/apicondition'
import { search as searchdbcondition, adddbcondition } from '@/api/condition/dbcondition'
import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
import { getassembleallnameList as getassembleallnameList } from '@/api/enviroment/enviromentassemble'
import { adddelaycondition, updatedelaycondition, removedelaycondition, searchbytype } from '@/api/condition/delaycondition'
import { mapGetters } from 'vuex'

export default {
  name: '测试场景',
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
      id: null,
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
      dbconditioncaseList: [],
      enviromentnameList: [],
      enviroment_assembleList: [],
      sourcetestsceneList: [],
      delayconditionList: [],
      listLoading: false, // 数据加载等待动画
      addcaselistLoading: false, // 用例列表页面数据加载等待动画
      total: 0, // 数据总数
      casetotal: 0, // 用例数据总数
      addcasetotal: 0, // 用例数据总数
      dialogStatus: 'add',
      DelaydialogStatus: 'add',
      apiconditiondialogStatus: 'add',
      dialogFormVisible: false,
      testscenecasedialogFormVisible: false,
      addtestscenecasedialogFormVisible: false,
      sceneConditionFormVisible: false,
      scenecaseConditionFormVisible: false,
      caseconditiondialogFormVisible: false,
      dbconditiondialogFormVisible: false,
      CopysceneFormVisible: false,
      DelaydialogFormVisible: false,
      tmpcopyscene: {
        sourcesceneid: '',
        sourcescenename: '',
        newscenename: ''
      },
      textMap: {
        updateRole: '修改环境',
        update: '修改环境',
        add: '添加环境'
      },
      apiconditiontextMap: {
        update: '修改前置接口',
        add: '添加前置接口'
      },
      DelaytextMap: {
        update: '修改延时条件',
        add: '添加延时条件'
      },
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
        casetype: null
      },
      search: {
        page: 1,
        size: 10,
        scenename: null,
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
        conditionid: null,
        conditiontype: null,
        projectid: null
      },
      searchallscene: {
        projectid: ''
      },
      searchdbcondition: {
        page: 1,
        size: 10,
        conditionid: '',
        conditiontype: '',
        projectid: ''
      }
    }
  },

  created() {
    this.search.projectid = window.localStorage.getItem('pid')
    this.getdepunitLists()
    this.searchapicondition.projectid = window.localStorage.getItem('pid')
    this.searchdbcondition.projectid = window.localStorage.getItem('pid')
    this.searchallscene.projectid = window.localStorage.getItem('pid')
    this.Scenedelaysearch.projectid = window.localStorage.getItem('pid')
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
    ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
  },

  methods: {
    unix2CurrentTime,

    /**
     * 显示添加延时条件对话框
     */
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
    /**
     * 获取延时条件列表
     */
    getdelayconditionList() {
      this.Scenedelaysearch.conditiontype = 'scencecase'
      searchbytype(this.Scenedelaysearch).then(response => {
        this.delayconditionList = response.data
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
        }
      }
    },

    ConditionselectChangedAS(e) {
      for (let i = 0; i < this.enviroment_assembleList.length; i++) {
        if (this.enviroment_assembleList[i].assemblename === e) {
          this.tmpdbcondition.assembleid = this.enviroment_assembleList[i].id
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

    casehandleSizeChange(size) {
      this.searchcase.page = 1
      this.searchcase.size = size
      this.gettestscenecaseList()
    },

    addcasehandleSizeChange(size) {
      this.searchcase.page = 1
      this.searchcase.size = size
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

    scenecasegetIndex(index) {
      return (this.searchcase.page - 1) * this.searchcase.size + index + 1
    },

    apiconditioncaseIndex(index) {
      return (this.searchapicondition.page - 1) * this.searchapicondition.size + index + 1
    },

    dbconditioncaseIndex(index) {
      return (this.searchdbcondition.page - 1) * this.searchdbcondition.size + index + 1
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
      this.getaddcasesList()
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

    showtestscenecaseConditionDialog(index) {
      this.scenecaseConditionFormVisible = true
      this.tmpapicondition.conditionid = this.testscenecaseList[index].id
      this.tmpapicondition.conditionname = this.testscenecaseList[index].casename
      this.tmpapicondition.conditiontype = 'scencecase'
      this.searchapicondition.conditiontype = 'scencecase'
      this.searchapicondition.conditionid = this.testscenecaseList[index].id
      this.searchdbcondition.conditiontype = 'scencecase'
      this.searchdbcondition.conditionid = this.testscenecaseList[index].id
      this.tmpdbcondition.conditionid = this.testscenecaseList[index].id
      this.tmpdbcondition.conditionname = this.testscenecaseList[index].casename
      this.tmpdbcondition.conditiontype = 'scencecase'
      this.Scenedelaysearch.conditionid = this.testscenecaseList[index].id
      this.getapiconditionList()
      this.getdelayconditionList()
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
    },
    /**
     * 显示用例对话框
     * @param index 测试集合下标
     */
    showtestsceneCaseDialog(index) {
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
        this.apiconditioncaseList = response.data
      }).catch(res => {
        this.$message.error('加载apicase列表失败')
      })
    },

    apiconditiontestcaseselectChanged(e) {
      for (let i = 0; i < this.apiconditioncaseList.length; i++) {
        if (this.apiconditioncaseList[i].casename === e) {
          this.tmpapicondition.caseid = this.apiconditioncaseList[i].id
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
