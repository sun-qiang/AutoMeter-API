<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('executeplan:list')"
            @click.native.prevent="getexecuteplanList"
          >刷新</el-button>
          <el-button
            type="success"
            size="mini"
            v-if="hasPermission('executeplan:list')"
            @click.native.prevent="showplanbatchDialog"
          >运行</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('executeplan:add')"
            @click.native.prevent="showAddexecuteplanDialog"
          >添加测试集合</el-button>
        </el-form-item>

        <span v-if="hasPermission('executeplan:search')">
          <el-form-item label="测试集合:">
            <el-input v-model="search.executeplanname" clearable @keyup.enter.native="searchBy" placeholder="测试集合"></el-input>
          </el-form-item>

          <el-form-item  label="业务类型:">
            <el-select v-model="search.businesstype" clearable placeholder="业务类型">
              <el-option label="请选择" value />
              <div v-for="(businessdicitem, index) in planbusinessdiclist" :key="index">
                <el-option :label="businessdicitem.dicitmevalue" :value="businessdicitem.dicitmevalue"/>
              </div>
            </el-select>
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
      ref="fileTable"
      :data="executeplanList"
      :key="itemplanKey"
      @row-click="handleClickTableRow"
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
      <el-table-column label="编号" align="center" width="50">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true"  label="集合名" align="center" prop="executeplanname" width="100"/>
      <el-table-column label="envid" align="center" v-if="show" prop="envid" width="50"/>
      <el-table-column label="状态" align="center" prop="status" v-if="show" width="50"/>
      <el-table-column :show-overflow-tooltip="true" label="业务类型" align="center" prop="businesstype" width="70"/>
      <el-table-column :show-overflow-tooltip="true" label="执行环境" align="center" prop="enviromentname" width="80"/>
      <el-table-column label="类型" align="center" prop="usetype" width="50"/>
      <el-table-column label="运行模式" align="center" prop="runmode" width="70"/>
      <el-table-column label="场景数" align="center" prop="scenenums" width="60"/>
      <el-table-column label="用例数" align="center" prop="casecounts" width="60"/>

<!--      <el-table-column :show-overflow-tooltip="true" label="通知钉钉token" align="center" prop="dingdingtoken" width="110"/>-->


      <el-table-column label="维护人" align="center" prop="creator" width="60"/>
<!--      <el-table-column :show-overflow-tooltip="true" label="描述" align="center" prop="memo" width="80"/>-->
      <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="110">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="110">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="150"
                       v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateexecuteplanDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
            @click.native.prevent="removeexecuteplan(scope.$index)"
          >删除</el-button>
        </template>
      </el-table-column>
      <el-table-column label="集合操作" align="center" width="500"
                       v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showtestsceneDialog(scope.$index)"
          >测试场景</el-button>

          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showtestplanConditionDialog(scope.$index)"
          >前置条件</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showstopbatchDialog(scope.$index)"
          >停止运行</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showplanparamsDialog(scope.$index)"
          >全局Header</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:update') && scope.row.id !== id"
            @click.native.prevent="showplannmessageDialog(scope.$index)"
          >集合通知</el-button>
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
        :model="tmpexecuteplan"
        ref="tmpexecuteplan"
      >
        <el-form-item label="集合名" prop="executeplanname" required>
          <el-input
            type="text"
            maxlength="50"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model.trim="tmpexecuteplan.executeplanname"
          />
        </el-form-item>
        <el-form-item label="类型" prop="usetype" required>
          <el-select v-model="tmpexecuteplan.usetype" placeholder="类型" style="width:100%">
            <el-option label="功能" value="功能" />
            <el-option label="性能" value="性能" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行环境" prop="enviromentname"  required>
          <el-select v-model="tmpexecuteplan.enviromentname" placeholder="执行环境" style="width:100%" @change="enviromentselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="业务类型" prop="businesstype"  required>
          <el-select v-model="tmpexecuteplan.businesstype" placeholder="业务类型" style="width:100%" @change="businesstypeselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(dicitem, index) in planbusinessdiclist" :key="index">
              <el-option :label="dicitem.dicitmevalue" :value="dicitem.dicitmevalue" required/>
            </div>
          </el-select>
        </el-form-item>

          <el-form-item label="运行模式" prop="runmode" required>
            <el-select v-model="tmpexecuteplan.runmode" placeholder="运行模式" style="width:100%">
              <el-option label="单机运行" value="单机运行" />
              <el-option label="多机并行" value="多机并行" />
            </el-select>
          </el-form-item>

          <el-form-item label="钉钉通知token" prop="dingdingtoken">
            <el-input
              maxlength="200"
              type="text"
              prefix-icon="el-icon-message"
              auto-complete="off"
              v-model="tmpexecuteplan.dingdingtoken"
            />
          </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="200"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpexecuteplan.memo"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addexecuteplan"
        >保存
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateexecuteplan"
        >修改</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="loadcase" :visible.sync="casedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchcase" ref="searchcase" >
          <span v-if="hasPermission('apicases:search')">
          <el-form-item label="微服务:" prop="deployunitname" required>
            <el-select v-model="searchcase.deployunitname" placeholder="微服务" @change="selectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in deployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" required/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="API:">
            <el-select v-model="searchcase.apiname" placeholder="api名">
              <el-option label="请选择" value />
              <div v-for="(api, index) in apiList" :key="index">
                <el-option :label="api.apiname" :value="api.apiname"/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchcaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
        </el-form>
      </div>
      <el-table
        ref="caseTable"
        :data="testcaselastList"
        :key="itemcaseKey"
        @row-click="casehandleClickTableRow"
        @selection-change="casehandleSelectionChange"
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

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="apiid" v-if="show" align="center" prop="apiid" width="120"/>
        <el-table-column label="deployunitid" v-if="show" align="center" prop="deployunitid" width="120"/>
        <el-table-column label="用例名" align="center" prop="casename" width="120"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="120"/>
        <el-table-column label="API" align="center" prop="apiname" width="120"/>
        <el-table-column label="期望值" align="center" prop="expect" width="120"/>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="casedialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addexecuteplantestcase"
        >装载</el-button>
        <el-button
          type="warning"
          :loading="btnLoading"
          @click.native.prevent="removeexecuteplantestcase"
        >取消装载</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="loadbatch" :visible.sync="batchdialogFormVisible">
      <div class="filter-container" >
        <el-form  :model="tmpplanbatch" ref="tmpplanbatch" >
          <el-form-item label="执行计划："  prop="batchname" required>
            <el-input
              type="text"
              maxlength="50"
              style="width:60%"
              placeholder="例如2020-10-21-tag-101"
              prefix-icon="el-icon-edit"
              auto-complete="off"
              v-model.trim="tmpplanbatch.batchname"
            />
          </el-form-item>
          <el-form-item label="执行类型：" prop="exectype" required >
            <el-select v-model="tmpplanbatch.exectype" placeholder="执行类型" style="width:60%" @change="exectypeselectChanged($event)">
              <el-option label="立即执行" value="立即执行"></el-option>
              <el-option label="某天定时" value="某天定时"></el-option>
              <el-option label="每天定时" value="每天定时"></el-option>
            </el-select>
          </el-form-item>
          <div v-if="datevisible">
            <el-form-item label="选择日期：" prop="exectmpdate" required >
              <el-date-picker style="width:60%"
                              v-model="tmpplanbatch.exectmpdate"
                              type="date"
                              format="yyyy-MM-dd"
                              value-format="yyyy-MM-dd"
                              placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </div>
          <div v-if="timevisible">
            <el-form-item label="选择时刻：" prop="exectime" required >
              <el-time-select style="width:60%"
                              v-model="tmpplanbatch.exectime"
                              :picker-options="{
              start: '00:05',
              step: '00:10',
              end: '23:55'
            }"
                              placeholder="选择时间">
              </el-time-select>
            </el-form-item>
          </div>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="batchdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="execbtnLoading"
          @click.native.prevent="savebatchandexecuteplancase"
        >执行</el-button>
      </div>
    </el-dialog>
    <el-dialog title='停止运行计划' :visible.sync="stopbatchdialogFormVisible">
      <div class="filter-container" >
        <el-form  :model="tmpstopplanbatch" ref="tmpstopplanbatch" >
          <el-form-item label="执行计划：" prop="batchname" required >
            <el-select v-model="tmpstopplanbatch.batchname" clearable style="width:70%" placeholder="执行计划" @change="stopplanbatchselectChanged($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(tmpstopplanbatch, index) in stopplanbatchList" :key="index">
                <el-option :label="tmpstopplanbatch.batchname" :value="tmpstopplanbatch.batchname" required/>
              </div>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="stopbatchdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          @click.native.prevent="updatebatchstatus"
        >停止</el-button>
      </div>
    </el-dialog>
    <el-dialog title="全局Header" :visible.sync="CollectionParamsFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('executeplan:add')"
              @click.native.prevent="showAddapiparamsDialog"
            >添加全局Header</el-button>
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
    </el-dialog>
    <el-dialog :title="paramstextMap[ParamsdialogStatus]" :visible.sync="modifyparamdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpparam"
        ref="tmpparam"
      >
        <el-form-item label="全局Header:"  prop="globalheadername" required >
          <el-select style="width:415px" v-model="tmpparam.globalheadername" placeholder="全局Header" @change="notexistheaderselectChanged($event)">
            <div v-for="(globalheader, index) in globalheaderallList" :key="index">
              <el-option :label="globalheader.globalheadername" :value="globalheader.globalheadername" />
            </div>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="modifyparamdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="ParamsdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addparams"
        >添加</el-button>
        <el-button
          type="success"
          v-if="ParamsdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatparam"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="集合通知" :visible.sync="CollectionNoticeFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('plannmessage:add')"
              @click.native.prevent="showCollectionNoticeDialog"
            >添加集合通知</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="planmessageList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="plannmessagegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="通知类型" align="center" prop="messagetype" width="80"/>
        <el-table-column :show-overflow-tooltip="true"   label="通知token" align="center" prop="hookcontent" width="380"/>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('plannmessage:update')  || hasPermission('plannmessage:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('plannmessage:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateplannmessageDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('plannmessage:delete') && scope.row.id !== id"
              @click.native.prevent="removeplannmessageparam(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog :title="plannmessagetextMap[plannmessagedialogStatus]" :visible.sync="modifyplannmessagedialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpplannmessage"
        ref="tmpplannmessage"
      >
        <el-form-item label="通知类型:"  prop="messagetype" required >
          <el-select v-model="tmpplannmessage.messagetype" placeholder="通知类型" style="width:100%" >
            <el-option label="钉钉" value="钉钉"></el-option>
            <el-option label="飞书" value="飞书"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="通知token:"  prop="hookcontent" required >
          <el-input
            type="text"
            maxlength="500"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplannmessage.hookcontent"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="modifyplannmessagedialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="plannmessagedialogStatus === 'add'"
          @click.native.prevent="addplannmessageparam"
        >添加</el-button>
        <el-button
          type="success"
          v-if="plannmessagedialogStatus === 'update'"
          @click.native.prevent="updateplannmessageparams"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog width="840px" title='测试场景' :visible.sync="testscenedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true"  >
          <el-form-item>
            <el-button type="primary" @click="ShowAddScenceDialog" :loading="btnLoading">添加场景</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="testplansceneTable"
        :data="testplansceneList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="planscenegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="场景名" align="center" prop="scenename" width="280"/>
        <el-table-column width="120" align="center" label="场景顺序">
          <template slot-scope="{row}">
            <template v-if="row.edit">
              <el-input v-model="row.ordernum" class="edit-input"
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
            <span v-else>{{ row.ordernum }}</span>
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
              @click.native.prevent="removetestplanscene(scope.$index)"
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
        @size-change="planscenehandleSizeChange"
        @current-change="planscenehandleCurrentChange"
        :current-page="searchscene.page"
        :page-size="searchscene.size"
        :total="testplanscenetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>
    <el-dialog width="800px" title='添加场景' :visible.sync="addtestscenedialogFormVisible">
      <el-table
        ref="addsceneTable"
        :data="testsceneList"
        :key="itemaddsceneKey"
        @row-click="addscenehandleClickTableRow"
        @selection-change="addscenehandleSelectionChange"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="addscenegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="场景名" align="center" prop="scenename" width="370"/>
        <el-table-column label="场景类型" align="center" prop="usetype" width="140"/>
        <el-table-column label="用例数" align="center" prop="casenums" width="140"/>
      </el-table>
      <el-pagination
        @size-change="addscenehandleSizeChange"
        @current-change="addscenehandleCurrentChange"
        :current-page="addsearchscene.page"
        :page-size="addsearchscene.size"
        :total="addscenetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="addtestscenedialogFormVisible = false">取消</el-button>

        <el-button
          type="primary"
          :loading="btnLoading"
          @click.native.prevent="addplantestscene"
        >添加</el-button>
      </div>
    </el-dialog>

    <el-dialog title="集合前置条件" width="1150px" :visible.sync="ConditionFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="ShowAddPlancaseconditionDialog"
            >添加接口前置条件</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="AddcasedbconditionDialog"
            >添加数据库前置条件
            </el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddplanscriptDialog"
            >添加脚本前置条件
            </el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showplanconditionorderDialog"
            >设置前置条件顺序
            </el-button>
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
            <span v-text="planapiconditioncaseIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="所属集合" align="center" prop="conditionname" width="150"/>
        <el-table-column label="前置条件名" align="center" prop="subconditionname" width="150"/>
        <el-table-column label="前置接口" align="center" prop="casename" width="150"/>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="120">
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
              @click.native.prevent="showCaseVariablesforplanConditionDialog(scope.$index)"
            >提取变量
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      2.数据库前置条件：
      <el-table
        :data="dbconditioncaseList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="plandbgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="所属集合" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="100"/>
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
              @click.native.prevent="showplandbvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>

      3.脚本前置条件
      <el-table
        :data="planscriptconditionList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="planscriptgetIndex(scope.$index)"></span>
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
              @click.native.prevent="showUpdateplanscriptconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('scriptcondition:delete') && scope.row.id !== id"
              @click.native.prevent="removeplanscriptcondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('dbcondition:delete') && scope.row.id !== id"
              @click.native.prevent="showplanscriptvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog title="场景前置条件" width="1150px" :visible.sync="scenecaseConditionFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="ShowAddPlanSceneconditionDialog"
            >添加接口前置条件</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddScenedelayconditionDialog"
            >添加前置延时</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddScenedbconditionDialog"
            >添加数据库前置条件</el-button>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showAddscriptDialog"
            >添加脚本前置条件</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('testscene:scenecasecondition')"
              @click.native.prevent="showsceneconditionorderDialog"
            >设置前置条件顺序</el-button>
          </el-form-item>
        </el-form>
      </div>

      1.接口前置条件：
      <el-table
        :data="sceneapiconditioncaseList"
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
        <el-table-column :show-overflow-tooltip="true" label="前置条件名" align="center" prop="subconditionname" width="120"/>
        <el-table-column :show-overflow-tooltip="true" label="所属场景" align="center" prop="conditionname" width="120"/>
        <el-table-column :show-overflow-tooltip="true" label="微服务" align="center" prop="deployunitname" width="120"/>
        <el-table-column :show-overflow-tooltip="true" label="前置接口" align="center" prop="casename" width="120"/>
        <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="120">
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
              @click.native.prevent="showUpdatesceneconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('testscene:casedeleteapicondition') && scope.row.id !== id"
              @click.native.prevent="removecasescenecondition(scope.$index)"
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
        :data="scenedelayconditionList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="scenedelaygetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="前置条件名" align="center" prop="subconditionname" width="200"/>
        <el-table-column label="所属场景" align="center" prop="conditionname" width="150"/>
        <el-table-column label="等待时间(秒)" align="center" prop="delaytime" width="150">
        </el-table-column>
        <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="120">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="120">
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
              @click.native.prevent="removescenedelaycondition(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      3.数据库前置条件：
      <el-table
        :data="scenedbconditioncaseList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="scenedbgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="前置条件名" :show-overflow-tooltip="true" align="center" prop="subconditionname" width="100"/>
        <el-table-column label="所属场景" :show-overflow-tooltip="true" align="center" prop="conditionname" width="110"/>
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

        <el-table-column label="管理" align="center" width="260"
                         v-if="hasPermission('testscene:caseupdatedbcondition')  || hasPermission('testscene:casedeletedbcondition')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('testscene:caseupdatedbcondition') && scope.row.id !== id"
              @click.native.prevent="showUpdatescenedbconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('testscene:casedeletedbcondition') && scope.row.id !== id"
              @click.native.prevent="removescenedbcondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
              @click.native.prevent="showscenedbvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>
      4.脚本前置条件
      <el-table
        :data="scenescriptconditionList"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="scenescriptgetIndex(scope.$index)"></span>
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
              @click.native.prevent="showUpdatescenescriptconditionDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('scriptcondition:delete') && scope.row.id !== id"
              @click.native.prevent="removescenescriptcondition(scope.$index)"
            >删除</el-button>
            <el-button
              type="primary"
              size="mini"
              v-if="hasPermission('scriptcondition:delete') && scope.row.id !== id"
              @click.native.prevent="showscenescriptvariablesDialog(scope.$index)"
            >提取变量</el-button>
          </template>
        </el-table-column>
      </el-table>

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
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testcase, index) in conditioncaseList" :key="index">
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
    <el-dialog :title="SceneapiconditiontextMap[SceneconditiondialogStatus]" :visible.sync="SceneconditiondialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 450px; margin-left:50px;"
        :model="tmpsceneapicondition"
        ref="tmpsceneapicondition"
      >

        <el-form-item label="前置条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpsceneapicondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpsceneapicondition.deployunitname" filterable placeholder="微服务" style="width:100%" @change="sceneconditiondeployunitselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item  label="模块:" prop="modelname" >
          <el-select v-model="tmpsceneapicondition.modelname" filterable placeholder="模块" style="width:100%" @change="sceneconditionmodelselectChanged($event)">
            <el-option label="请选择" value />
            <div v-for="(model, index) in sceneconditionmodelList" :key="index">
              <el-option :label="model.modelname" :value="model.modelname" />
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API" prop="apiname" required >
          <el-select v-model="tmpsceneapicondition.apiname" filterable placeholder="API" style="width:100%" @change="sceneconditionapiselectChanged($event)">
            <div v-for="(api, index) in sceneconditionapiList" :key="index">
              <el-option :label="api.apiname" :value="api.apiname"/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="接口" prop="casename" required >
          <el-select v-model="tmpsceneapicondition.casename" filterable placeholder="接口" style="width:100%" @change="sceneconditiontestcaseselectChanged($event)">
            <div v-for="(testcase, index) in sceneconditioncaseList" :key="index">
              <el-option :label="testcase.casename" :value="testcase.casename" required/>
            </div>
          </el-select>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="SceneconditiondialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="SceneconditiondialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapicondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="SceneconditiondialogStatus === 'add'"
          @click.native.prevent="addscenecondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="SceneconditiondialogStatus === 'update'"
          @click.native.prevent="updatescenecondition"
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
        <el-form-item label="延时条件名" prop="subconditionname" required>
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

    <el-dialog :title="planscripttextMap[planscriptdialogStatus]" :visible.sync="planscriptdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:50px;"
        :model="tmpplanscriptcondition"
        ref="tmpplanscriptcondition"
      >
        <el-form-item label="脚本条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplanscriptcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="Java代码" prop="script" required >
          <el-input
            type="textarea"
            rows="10" cols="50"
            maxlength="4000"
            v-model="tmpplanscriptcondition.script"
            placeholder="Java 代码"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="planscriptdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="planscriptdialogStatus === 'add'"
          @click.native.prevent="$refs['plantmpscriptcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="planscriptdialogStatus === 'add'"
          @click.native.prevent="addplanscriptcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="planscriptdialogStatus === 'update'"
          @click.native.prevent="updateplanscriptcondition"
        >修改</el-button>
      </div>
    </el-dialog>
    <el-dialog width="1050px" title='脚本变量列表' :visible.sync="planscriptVariablesDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAddplanscriptvariablesDialog"
            >添加脚本变量</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="planscriptvariablesList"
        :key="itemKey"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="planscriptvariablesgetIndex(scope.$index)"></span>
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
              @click.native.prevent="showUpdateplanscriptvariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('scriptvariables:delete') && scope.row.id !== id"
              @click.native.prevent="removeplanscriptvariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="planscriptvariableshandleSizeChange"
        @current-change="planscriptvariableshandleCurrentChange"
        :current-page="searchplanscriptvariables.page"
        :page-size="searchplanscriptvariables.size"
        :total="planscriptvariablestotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>
    <el-dialog :title="planscriptVariablestextMap[planscriptVariablesdialogStatus]" :visible.sync="addplanscriptVariablesdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
        :model="tmpplanscriptvariables"
        ref="tmpplanscriptvariables"
      >
        <el-form-item label="变量名" prop="scriptvariablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplanscriptvariables.scriptvariablesname"
          />
        </el-form-item>

        <el-form-item label="变量描述" prop="variablesdes" required>
          <el-input
            maxlength="20"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplanscriptvariables.variablesdes"
          />
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select v-model="tmpplanscriptvariables.valuetype" placeholder="变量值类型" style="width:100%">
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
            v-model="tmpplanscriptvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="addplanscriptVariablesdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="planscriptVariablesdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpplanscriptvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="planscriptVariablesdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addplanscriptvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="planscriptVariablesdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateplanscriptvariables"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="scenedbtextMap[scenedbdialogStatus]" :visible.sync="scenedbconditiondialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpscenedbcondition"
        ref="tmpscenedbcondition"
      >
        <el-form-item label="数据库条件名：" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpscenedbcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="环境：" prop="enviromentname" required >
          <el-select v-model="tmpscenedbcondition.enviromentname" filterable  placeholder="环境" style="width:100%" @change="sceneselectChangedEN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="组件：" prop="assemblename" required >
          <el-select v-model="tmpscenedbcondition.assemblename" filterable placeholder="组件" style="width:100%" @change="SceneConditionselectChangedAS($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(macname, index) in enviroment_assembleList" :key="index">
              <el-option :label="macname.deployunitname" :value="macname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="操作类型：" prop="dbtype" required >
          <el-select v-model="tmpscenedbcondition.dbtype" placeholder="操作类型" style="width:100%" @change="selectChangedDBType($event)">
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
            v-model="tmpscenedbcondition.dbcontent"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="scenedbconditiondialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="scenedbdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpscenedbcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="scenedbdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addscenedbcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="scenedbdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatescenedbcondition"
        >修改</el-button>
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

        <el-table-column label="管理" align="center"  width="200"
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

    <el-dialog :title="plandbtextMap[plandbdialogStatus]" :visible.sync="dbconditiondialogFormVisible">
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
          v-if="plandbdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="plandbdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddbcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="plandbdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedbcondition"
        >修改</el-button>
      </div>
    </el-dialog>
    <el-dialog width="1100px" title='数据库变量列表' :visible.sync="plandbVariablesDialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAddplandbvariablesDialog"
            >添加数据库变量</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="plandbvariablesList"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="plandbVariablesgetIndex(scope.$index)"></span>
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

        <el-table-column label="管理" align="center"  width="200"
                         v-if="hasPermission('dbvariables:update')  || hasPermission('dbvariables:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('dbvariables:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateplandbvariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('dbvariables:delete') && scope.row.id !== id"
              @click.native.prevent="removeplandbvariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="plandbvariableshandleSizeChange"
        @current-change="plandbvariableshandleCurrentChange"
        :current-page="searchplandbvariables.page"
        :page-size="searchplandbvariables.size"
        :total="plandbvariablestotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>
    <el-dialog :title="plandbvariablestextMap[plandbvariablesdialogStatus]" :visible.sync="addplandbvariablesdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
        :model="tmpplandbvariables"
        ref="tmpplandbvariables"
      >
        <el-form-item label="变量名" prop="dbvariablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplandbvariables.dbvariablesname"
          />
        </el-form-item>

        <el-form-item label="变量描述" prop="variablesdes" required>
          <el-input
            maxlength="20"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpplandbvariables.variablesdes"
          />
        </el-form-item>

        <el-form-item label="变量值类型" prop="valuetype" required >
          <el-select v-model="tmpplandbvariables.valuetype" placeholder="变量值类型" style="width:100%">
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
            v-model="tmpplandbvariables.fieldname"
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
            v-model="tmpplandbvariables.roworder"
          />
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpplandbvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="addplandbvariablesdialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="plandbvariablesdialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="plandbvariablesdialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addplandbvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="plandbvariablesdialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateplandbvariables"
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
  import { searchconditionorder, addconditionorder } from '@/api/condition/conditionorder'
  import {
    getapicasesList as getapicasesList,
    search as getapicases
  } from '@/api/assets/apicases'
  import { search as getapiList } from '@/api/deployunit/api'
  import { getdepunitLists } from '@/api/deployunit/depunit'
  import { getstopplanbatchList as getstopplanbatchList, updatebatchstatus } from '@/api/executecenter/executeplanbatch'
  import { searchcases as searchtestplancases, addexecuteplantestcase, removeexecuteplantestcase } from '@/api/executecenter/executeplantestcase'
  import { checkplancondition as checkplancondition, search, addexecuteplan, updateexecuteplan, removeexecuteplan, executeplan, updateexecuteplanstatus } from '@/api/executecenter/executeplan'
  import { unix2CurrentTime } from '@/utils'
  import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
  import { getdatabydiccodeList as getdatabydiccodeList } from '@/api/system/dictionary'
  import { getglobalheaderallList } from '@/api/testvariables/globalheader'
  import { mapGetters } from 'vuex'
  import { search as searchscence } from '@/api/executecenter/testscene'
  import { addapicasesdebug, removeapicasesdebug, updateapicasesdebug, searchheaderbyepid } from '@/api/assets/globalheaderuse'
  import { addtestplantestscene, findscenebyexecplanid, updateplanscenenorder } from '@/api/executecenter/testplantestscene'
  import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
  import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'
  import { getassembleallnameList as getassembleallnameList } from '@/api/enviroment/enviromentassemble'
  import { search as searchapicondition, addapicondition, removeapicondition, updateapicondition } from '@/api/condition/apicondition'
  import { search as searchdbcondition, adddbcondition, updatedbcondition, removedbcondition } from '@/api/condition/dbcondition'
  import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
  import { removetestplanscene } from '@/api/executecenter/testplantestscene'
  import { adddelaycondition, updatedelaycondition, removedelaycondition, searchbytype } from '@/api/condition/delaycondition'
  import { addtestvariables, updatetestvariables, removetestvariables, findtestvariablesbycaseid } from '@/api/testvariables/testvariables'
  import { search as searchscriptvariables, addscriptvariables, updatescriptvariables, removescriptvariables } from '@/api/testvariables/scriptvariables'
  import { search as getscriptconditionList, addscriptcondition, updatescriptcondition, removescriptcondition } from '@/api/condition/scriptcondition'
  import { search as searchdbvariables, adddbvariables, updatedbvariables, removedbvariables } from '@/api/testvariables/dbvariables'
  import { findMacAndDepWithEnv as findMacAndDepWithEnv } from '@/api/enviroment/macdepunit'
  import { search as searchplannmessage, addplannmessageparam, updateplannmessageparams, removeplannmessageparam } from '@/api/executecenter/plannmessage'

  export default {
    name: '测试集合',
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
        CollectionNoticeFormVisible: false,
        modifyplannmessagedialogFormVisible: false,
        plannmessagedialogStatus: 'add',
        plannmessagetextMap: {
          update: '修改集合通知',
          add: '添加集合通知'
        },
        planmessageList: [], // 条件顺序显示列表
        searchnotice: {
          page: 1,
          size: 10,
          executeplanid: 0
        },
        tmpplannmessage: {
          id: '',
          executeplanid: '',
          messagetype: '',
          hookcontent: '',
          mid: '',
          memo: ''
        },
        tmpsubconditionid: '',
        conditionorderList: [], // 条件顺序显示列表
        saveconditionorderList: [], // 条件顺序保存列表
        searchconditionorder: {
          subconditionid: '',
          conditiontype: ''
        },
        ConditionOrderdialogFormVisible: false,
        id: null,
        datevisible: false,
        timevisible: false,
        itemKey: null,
        itemplanKey: null,
        itemcaseKey: null,
        planbusinessdiclist: [], // 执行计划字典表业务类型列表
        tmpexecuteplanname: '',
        tmpcasedeployunitname: null,
        tmpcaseapiname: null,
        tmpcaseexecuteplanid: null,
        tmpcasecasetype: null,
        show: false,
        PerformanceVisible: false, // 显示性能运行模式
        enviromentnameList: [], // 环境列表
        apiList: [], // api列表
        deployunitList: [], // 微服务列表
        multipleSelection: [], // 执行计划表格被选中的内容
        casemultipleSelection: [], // 用例表格被选中的内容
        globalheaderallList: [], // 全局header列表
        executeplanList: [], // 执行计划列表
        testcaseList: [], // 用例列表
        testcaselastList: [], // 经过用例列表和已装载的用例组合出的结果列表
        executeplancaseList: [], // 执行计划装载用例列表
        executeplancaseexistList: [], // 查询执行计划已存在的用例列表
        executeplancaseremovetList: [], // 查询执行计划需要删除存在的用例列表
        executeplanstopList: [], // 停止执行计划列表
        tmpplanbatchList: [], // 计划批次列表
        paramsList: [], // 参数列表
        addtestscenelastList: [],
        testplansceneList: [],
        testsceneList: [], // 列表
        addtestsceneList: [], // 列表
        apiconditioncaseList: [], // 列表
        sceneapiconditioncaseList: [], // 列表
        conditioncaseList: [], // 列表
        sceneconditioncaseList: [], // 列表
        apiconditionmodelList: [],
        sceneconditionmodelList: [],
        apiconditionapiList: [],
        sceneconditionapiList: [],
        scenedelayconditionList: [],
        itemaddsceneKey: null,
        scenemultipleSelection: [], // 查询用例表格被选中的内容
        delayconditionList: [],
        stopplanbatchList: [], // 期望停止的计划
        dbconditioncaseList: [], // 集合数据库前置
        scenedbconditioncaseList: [],
        enviroment_assembleList: [],
        tmptestscenename: null,
        scenetotal: 0,
        addscenetotal: 0,
        testplanscenetotal: 0,
        SceneapiconditiontextMap: {
          update: '修改前置接口条件',
          add: '添加前置接口条件'
        },
        DelaytextMap: {
          update: '修改延时条件',
          add: '添加延时条件'
        },
        addsearchscene: {
          page: 1,
          size: 10,
          usetype: null,
          projectid: ''
        },
        searchscene: {
          page: 1,
          size: 10,
          testplanid: 0,
          projectid: ''
        },
        listLoading: false, // 数据加载等待动画
        caselistLoading: false, // 用例列表页面数据加载等待动画
        total: 0, // 数据总数
        casetotal: 0, // 用例数据总数
        apiQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          deployunitname: '' // 获取字典表入参
        },
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true,
          executeplanname: '',
          projectid: '',
          creator: ''
        },
        caselistQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true,
          deployunitname: null,
          apiname: null,
          executeplanid: null,
          casetype: null
        },
        ParamsdialogStatus: 'add',
        DelaydialogStatus: 'add',
        dialogStatus: 'add',
        SceneconditiondialogStatus: 'add',
        plandbdialogStatus: 'add',
        scenedbdialogStatus: 'add',
        dialogFormVisible: false,
        casedialogFormVisible: false,
        batchdialogFormVisible: false,
        CollectionParamsFormVisible: false,
        modifyparamdialogFormVisible: false,
        ConditionFormVisible: false,
        testscenedialogFormVisible: false,
        addtestscenedialogFormVisible: false,
        scenecaseConditionFormVisible: false,
        SceneconditiondialogFormVisible: false,
        DelaydialogFormVisible: false,
        stopbatchdialogFormVisible: false, // 停止运行
        dbconditiondialogFormVisible: false,
        scenedbconditiondialogFormVisible: false,
        loadcase: '装载用例',
        loadbatch: '执行计划',
        textMap: {
          updateRole: '修改测试集合',
          update: '修改测试集合',
          add: '添加测试集合'
        },
        plandbtextMap: {
          updateRole: '修改测试集合',
          update: '修改数据库前置条件',
          add: '添加数据库前置条件'
        },
        scenedbtextMap: {
          updateRole: '修改测试集合',
          update: '修改数据库前置条件',
          add: '添加数据库前置条件'
        },
        apiconditiontextMap: {
          update: '修改前置接口条件',
          add: '添加前置接口条件'
        },
        apiconditiondialogStatus: 'add',
        caseconditiondialogFormVisible: false,
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
        tmpsceneapicondition: {
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
          conditiontype: 'execplan',
          creator: '',
          projectid: ''
        },
        tmpscenedbcondition: {
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
          conditiontype: 'scene',
          creator: '',
          projectid: ''
        },
        paramstextMap: {
          updateRole: '修改参数',
          update: '修改参数',
          add: '添加参数'
        },
        diclevelQuery: {
          page: 1, // 页码
          size: 100, // 每页数量
          diccode: 'planbusinesstype' // 获取字典表入参
        },
        btnLoading: false, // 按钮等待动画
        casebtnLoading: false, // 按钮等待动画
        execbtnLoading: false, // 按钮等待动画
        tmpexecuteplan: {
          id: '',
          executeplanname: '',
          enviromentname: '',
          envid: '',
          status: '',
          usetype: '',
          businesstype: '',
          ip: '',
          memo: '',
          creator: '',
          dingdingtoken: '',
          runmode: '',
          projectid: ''
        },
        tmpplanbatch: {
          executeplanid: '',
          executeplanname: '',
          batchid: '',
          batchname: '',
          creator: '',
          exectype: '',
          exectmpdate: '',
          execdate: '',
          exectime: '',
          projectid: ''
        },
        tmpstopplanbatch: {
          executeplanid: '',
          batchid: '',
          batchname: '',
          projectid: ''
        },
        tmpplanenv: {
          id: '',
          envid: '',
          projectid: '',
          enviromentname: ''
        },
        tmpapicases: {
          id: '',
          casename: '',
          deployunitname: '',
          apiname: '',
          casejmxname: '',
          casecontent: '',
          expect: '',
          level: '',
          memo: '',
          creator: 'admin'
        },
        tmpparam: {
          id: '',
          executeplanid: '',
          paramstype: '',
          keyname: '',
          keyvalue: '',
          globalheadername: '',
          globalheaderid: 0
        },
        tmpep: {
          executeplanid: ''
        },
        search: {
          page: 1,
          size: 10,
          executeplanname: null,
          businesstype: '',
          creator: '',
          nickname: '',
          accountId: null,
          projectid: ''
        },
        Scenedelaysearch: {
          page: 1,
          size: 10,
          conditionid: null,
          conditiontype: null,
          projectid: null
        },
        searchcase: {
          page: 1,
          size: 10,
          deployunitname: null,
          apiname: null,
          executeplanid: null,
          casetype: null,
          projectid: ''
        },
        searchparam: {
          page: 1,
          size: 10
        },
        searchapicondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        searchplanapicondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        tmpexecplan: {
          execplanid: 0,
          execplanname: null
        },
        searchdbcondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        searchapicasevariables: {
          page: 1,
          size: 10,
          // caseid: '',
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
        caseaddvariablesdialogFormVisible: false,
        caseVariablesDialogFormVisible: false,
        apicasevariablestotal: 0,
        ApicasesVariablesList: [],
        caseaddvariablesdialogStatus: 'add',
        caseaddvariablestextMap: {
          update: '修改变量',
          add: '添加变量'
        },
        scriptdialogFormVisible: false,
        scriptdialogStatus: 'add',
        tmpscriptcondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          script: '',
          creator: '',
          projectid: ''
        },
        scripttextMap: {
          updateRole: '修改环境',
          update: '修改脚本前置条件',
          add: '添加脚本前置条件'
        },
        searchscriptcondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        scenescriptconditionList: [],
        planscriptdialogFormVisible: false,
        planscriptdialogStatus: 'add',
        tmpplanscriptcondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          script: '',
          creator: '',
          projectid: ''
        },
        planscripttextMap: {
          updateRole: '修改环境',
          update: '修改脚本前置条件',
          add: '添加脚本前置条件'
        },
        searchplanscriptcondition: {
          page: 1,
          size: 10,
          conditionid: '',
          conditiontype: '',
          projectid: ''
        },
        planscriptconditionList: [],
        tmpplanscriptvariables: {
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
        planscriptVariablestextMap: {
          updateRole: '修改API用例',
          update: '修改脚本变量',
          add: '添加脚本变量'
        },
        planscriptVariablesdialogStatus: 'add',
        addplanscriptVariablesdialogFormVisible: false,
        planscriptVariablesDialogFormVisible: false,
        searchplanscriptvariables: {
          page: 1,
          size: 10,
          conditionid: '',
          projectid: ''
        },
        planscriptvariablesList: [],
        planscriptvariablestotal: 0,
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
        dbVariablesDialogFormVisible: false,
        adddbvariablesdialogFormVisible: false,
        searchdbvariables: {
          page: 1,
          size: 10,
          conditionid: '',
          projectid: ''
        },
        dbvariablesList: [],
        dbvariablestotal: 0,
        dbvariablestextMap: {
          updateRole: '修改API用例',
          update: '修改数据库变量',
          add: '添加数据库变量'
        },
        dbvariablesdialogStatus: 'add',
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
        plandbVariablesDialogFormVisible: false,
        addplandbvariablesdialogFormVisible: false,
        searchplandbvariables: {
          page: 1,
          size: 10,
          conditionid: '',
          projectid: ''
        },
        plandbvariablesList: [],
        plandbvariablestotal: 0,
        plandbvariablestextMap: {
          updateRole: '修改API用例',
          update: '修改数据库变量',
          add: '添加数据库变量'
        },
        plandbvariablesdialogStatus: 'add',
        tmpplandbvariables: {
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
        searchassemble: {
          page: 1,
          size: 100,
          assembletype: '组件',
          envid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'nickname', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    created() {
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.tmpplanbatch.projectid = window.localStorage.getItem('pid')
      this.tmpplanenv.projectid = window.localStorage.getItem('pid')
      this.listQuery.projectid = window.localStorage.getItem('pid')
      this.addsearchscene.projectid = window.localStorage.getItem('pid')
      this.searchapicondition.projectid = window.localStorage.getItem('pid')
      this.Scenedelaysearch.projectid = window.localStorage.getItem('pid')
      this.tmpstopplanbatch.projectid = window.localStorage.getItem('pid')
      this.searchdbcondition.projectid = window.localStorage.getItem('pid')
      this.getexecuteplanList()
      // this.getapiList()
      this.getdepunitList()
      this.getdatabydiccodeList()
      this.getglobalheaderallList()
    },

    activated() {
      // this.getapiList()
      this.getexecuteplanList()
      this.getdepunitList()
      this.getenviromentallList()
      this.getdatabydiccodeList()
      this.getglobalheaderallList()
    },

    methods: {
      showplanconditionorderDialog() {
        // 显示新增对话框
        this.ConditionOrderdialogFormVisible = true
        this.searchconditionorder.subconditionid = this.tmpsubconditionid
        this.searchconditionorder.conditiontype = 'execplan'
        this.searchConditionorder()
      },
      showsceneconditionorderDialog() {
        // 显示新增对话框
        this.ConditionOrderdialogFormVisible = true
        this.searchconditionorder.subconditionid = this.tmpsubconditionid
        this.searchconditionorder.conditiontype = 'scene'
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

      findMacAndDepWithEnv() {
        findMacAndDepWithEnv(this.searchassemble).then(response => {
          this.enviroment_assembleList = response.data.list
        }).catch(res => {
          this.$message.error('获取组件列表失败')
        })
      },

      planscriptvariableshandleSizeChange(size) {
        this.searchplanscriptvariables.page = 1
        this.searchplanscriptvariables.size = size
        this.getplanscriptvariablesList()
      },

      planscriptvariableshandleCurrentChange(page) {
        this.searchplanscriptvariables.page = page
        this.getplanscriptvariablesList()
      },

      planscriptvariablesgetIndex(index) {
        return (this.searchplanscriptvariables.page - 1) * this.searchplanscriptvariables.size + index + 1
      },
      showAddplanscriptvariablesDialog() {
        // 显示新增对话框
        this.addplanscriptVariablesdialogFormVisible = true
        this.planscriptVariablesdialogStatus = 'add'
        this.tmpplanscriptvariables.id = ''
        this.tmpplanscriptvariables.scriptvariablesname = ''
        this.tmpplanscriptvariables.variablesdes = ''
        this.tmpplanscriptvariables.valuetype = ''
        this.tmpplanscriptvariables.memo = ''
        this.tmpplanscriptvariables.creator = this.name
        this.tmpplanscriptvariables.projectid = window.localStorage.getItem('pid')
      },
      showUpdateplanscriptvariablesDialog(index) {
        this.addplanscriptVariablesdialogFormVisible = true
        this.planscriptVariablesdialogStatus = 'update'
        this.tmpplanscriptvariables.id = this.planscriptvariablesList[index].id
        this.tmpplanscriptvariables.scriptvariablesname = this.planscriptvariablesList[index].scriptvariablesname
        this.tmpplanscriptvariables.variablesdes = this.planscriptvariablesList[index].variablesdes
        this.tmpplanscriptvariables.valuetype = this.planscriptvariablesList[index].valuetype
        this.tmpplanscriptvariables.memo = this.planscriptvariablesList[index].memo
        this.tmpplanscriptvariables.creator = this.name
      },
      updateplanscriptvariables() {
        this.$refs.tmpplanscriptvariables.validate(valid => {
          if (valid) {
            updatescriptvariables(this.tmpplanscriptvariables).then(() => {
              this.$message.success('更新成功')
              this.getplanscriptvariablesList()
              this.addplanscriptVariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },
      removeplanscriptvariables(index) {
        this.$confirm('删除该变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.planscriptvariablesList[index].id
          removescriptvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getplanscriptvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      addplanscriptvariables() {
        this.$refs.tmpplanscriptvariables.validate(valid => {
          if (valid) {
            addscriptvariables(this.tmpplanscriptvariables).then(() => {
              this.$message.success('添加成功')
              this.getplanscriptvariablesList()
              this.addplanscriptVariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      getplanscriptvariablesList() {
        searchscriptvariables(this.searchplanscriptvariables).then(response => {
          this.planscriptvariablesList = response.data.list
          this.planscriptvariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },
      showplanscriptvariablesDialog(index) {
        // 显示新增对话框
        this.planscriptVariablesDialogFormVisible = true
        this.tmpplanscriptvariables.conditionid = this.planscriptconditionList[index].id
        this.searchplanscriptvariables.conditionid = this.planscriptconditionList[index].id
        this.tmpplanscriptvariables.conditionname = this.planscriptconditionList[index].subconditionname
        this.getplanscriptvariablesList()
      },

      removeplanscriptcondition(index) {
        this.$confirm('删除该脚本条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.planscriptconditionList[index].id
          removescriptcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getplanscriptconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },
      /**
       * 更新脚本条件
       */
      updateplanscriptcondition() {
        this.$refs.tmpplanscriptcondition.validate(valid => {
          if (valid) {
            updatescriptcondition(this.tmpplanscriptcondition).then(() => {
              this.$message.success('更新成功')
              this.getplanscriptconditionList()
              this.planscriptdialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },
      getplanscriptconditionList() {
        getscriptconditionList(this.searchplanscriptcondition).then(response => {
          this.planscriptconditionList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试脚本条件列表失败')
        })
      },
      /**
       * 添加脚本条件
       */
      addplanscriptcondition() {
        this.$refs.tmpplanscriptcondition.validate(valid => {
          if (valid) {
            addscriptcondition(this.tmpplanscriptcondition).then(() => {
              this.$message.success('添加成功')
              this.getplanscriptconditionList()
              this.planscriptdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },
      scriptvariableshandleSizeChange(size) {
        this.searchscriptvariables.page = 1
        this.searchscriptvariables.size = size
        this.getscenescriptvariablesList()
      },

      scriptvariableshandleCurrentChange(page) {
        this.searchscriptvariables.page = page
        this.getscenescriptvariablesList()
      },

      scriptvariablesgetIndex(index) {
        return (this.searchscriptvariables.page - 1) * this.searchscriptvariables.size + index + 1
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
      updatescriptvariables() {
        this.$refs.tmpscriptvariables.validate(valid => {
          if (valid) {
            updatescriptvariables(this.tmpscriptvariables).then(() => {
              this.$message.success('更新成功')
              this.getscenescriptvariablesList()
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
            this.getscenescriptvariablesList()
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
              this.getscenescriptvariablesList()
              this.addscriptVariablesdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      getscenescriptvariablesList() {
        searchscriptvariables(this.searchscriptvariables).then(response => {
          this.scriptvariablesList = response.data.list
          this.scriptvariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },
      showscenescriptvariablesDialog(index) {
        // 显示新增对话框
        this.scriptVariablesDialogFormVisible = true
        this.tmpscriptvariables.conditionid = this.scenescriptconditionList[index].id
        this.searchscriptvariables.conditionid = this.scenescriptconditionList[index].id
        this.tmpscriptvariables.conditionname = this.scenescriptconditionList[index].subconditionname
        this.getscenescriptvariablesList()
      },

      showUpdatescenescriptconditionDialog(index) {
        this.scriptdialogFormVisible = true
        this.scriptdialogStatus = 'update'
        this.tmpscriptcondition.id = this.scenescriptconditionList[index].id
        this.tmpscriptcondition.subconditionname = this.scenescriptconditionList[index].subconditionname
        this.tmpscriptcondition.conditionname = this.scenescriptconditionList[index].conditionname
        this.tmpscriptcondition.script = this.scenescriptconditionList[index].script
        this.tmpscriptcondition.creator = this.name
      },

      showUpdateplanscriptconditionDialog(index) {
        this.planscriptdialogFormVisible = true
        this.planscriptdialogStatus = 'update'
        this.tmpplanscriptcondition.id = this.planscriptconditionList[index].id
        this.tmpplanscriptcondition.subconditionname = this.planscriptconditionList[index].subconditionname
        this.tmpplanscriptcondition.conditionname = this.planscriptconditionList[index].conditionname
        this.tmpplanscriptcondition.script = this.planscriptconditionList[index].script
        this.tmpplanscriptcondition.creator = this.name
      },

      removescenescriptcondition(index) {
        this.$confirm('删除该脚本条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scenescriptconditionList[index].id
          removescriptcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getscriptconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
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
      getscriptconditionList() {
        getscriptconditionList(this.searchscriptcondition).then(response => {
          this.scenescriptconditionList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试脚本条件列表失败')
        })
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

      showAddplanscriptDialog() {
        // 显示新增对话框
        this.planscriptdialogFormVisible = true
        this.planscriptdialogStatus = 'add'
        this.tmpplanscriptcondition.id = ''
        this.tmpplanscriptcondition.subconditionname = ''
        this.tmpplanscriptcondition.script = ''
        this.tmpplanscriptcondition.creator = this.name
        this.tmpplanscriptcondition.projectid = window.localStorage.getItem('pid')
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
        this.tmptestvariables.caseid = this.sceneapiconditioncaseList[index].caseid
        this.tmptestvariables.casename = this.sceneapiconditioncaseList[index].casename
        this.findtestvariablesbycaseid()
      },

      showCaseVariablesforplanConditionDialog(index) {
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

      showUpdateplandbvariablesDialog(index) {
        this.addplandbvariablesdialogFormVisible = true
        this.plandbvariablesdialogStatus = 'update'
        this.tmpplandbvariables.id = this.plandbvariablesList[index].id
        this.tmpplandbvariables.dbvariablesname = this.plandbvariablesList[index].dbvariablesname
        this.tmpplandbvariables.variablesdes = this.plandbvariablesList[index].variablesdes
        this.tmpplandbvariables.valuetype = this.plandbvariablesList[index].valuetype
        this.tmpplandbvariables.roworder = this.plandbvariablesList[index].roworder
        this.tmpplandbvariables.fieldname = this.plandbvariablesList[index].fieldname
        this.tmpplandbvariables.memo = this.plandbvariablesList[index].memo
        this.tmpplandbvariables.creator = this.name
      },
      getdbvariablesList() {
        searchdbvariables(this.searchdbvariables).then(response => {
          this.dbvariablesList = response.data.list
          this.dbvariablestotal = response.data.total
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },

      getplandbvariablesList() {
        searchdbvariables(this.searchplandbvariables).then(response => {
          this.plandbvariablesList = response.data.list
          this.plandbvariablestotal = response.data.total
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

      removeplandbvariables(index) {
        this.$confirm('删除该变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.plandbvariablesList[index].id
          removedbvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getplandbvariablesList()
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

      updateplandbvariables() {
        this.$refs.tmpplandbvariables.validate(valid => {
          if (valid) {
            updatedbvariables(this.tmpplandbvariables).then(() => {
              this.$message.success('更新成功')
              this.getplandbvariablesList()
              this.addplandbvariablesdialogFormVisible = false
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

      addplandbvariables() {
        this.$refs.tmpplandbvariables.validate(valid => {
          if (valid) {
            adddbvariables(this.tmpplandbvariables).then(() => {
              this.$message.success('添加成功')
              this.getplandbvariablesList()
              this.addplandbvariablesdialogFormVisible = false
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

      showAddplandbvariablesDialog() {
        // 显示新增对话框
        this.addplandbvariablesdialogFormVisible = true
        this.plandbvariablesdialogStatus = 'add'
        this.tmpplandbvariables.id = ''
        this.tmpplandbvariables.dbvariablesname = ''
        this.tmpplandbvariables.variablesdes = ''
        this.tmpplandbvariables.roworder = ''
        this.tmpplandbvariables.fieldname = ''
        this.tmpplandbvariables.memo = ''
        this.tmpplandbvariables.valuetype = ''
        this.tmpplandbvariables.creator = this.name
        this.tmpplandbvariables.projectid = window.localStorage.getItem('pid')
      },

      dbvariableshandleCurrentChange(page) {
        this.searchdbvariables.page = page
        this.getdbvariablesList()
      },

      plandbvariableshandleCurrentChange(page) {
        this.searchplandbvariables.page = page
        this.getplandbvariablesList()
      },

      dbvariableshandleSizeChange(size) {
        this.searchdbvariables.page = 1
        this.searchdbvariables.size = size
        this.getdbvariablesList()
      },

      plandbvariableshandleSizeChange(size) {
        this.searchplandbvariables.page = 1
        this.searchplandbvariables.size = size
        this.getplandbvariablesList()
      },

      dbVariablesgetIndex(index) {
        return (this.searchdbvariables.page - 1) * this.searchdbvariables.size + index + 1
      },

      plandbVariablesgetIndex(index) {
        return (this.searchplandbvariables.page - 1) * this.searchplandbvariables.size + index + 1
      },

      showscenedbvariablesDialog(index) {
        // 显示新增对话框
        this.dbVariablesDialogFormVisible = true
        this.tmpdbvariables.conditionid = this.scenedbconditioncaseList[index].id
        this.searchdbvariables.conditionid = this.scenedbconditioncaseList[index].id
        this.tmpdbvariables.conditionname = this.scenedbconditioncaseList[index].subconditionname
        this.getdbvariablesList()
      },
      showplandbvariablesDialog(index) {
        // 显示新增对话框
        this.plandbVariablesDialogFormVisible = true
        this.tmpplandbvariables.conditionid = this.dbconditioncaseList[index].id
        this.searchplandbvariables.conditionid = this.dbconditioncaseList[index].id
        this.tmpplandbvariables.conditionname = this.dbconditioncaseList[index].subconditionname
        this.getplandbvariablesList()
      },

      removescenedbcondition(index) {
        this.$confirm('删除该数据库条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scenedbconditioncaseList[index].id
          removedbcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getscenedbconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
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

      showUpdatedbconditionDialog(index) {
        this.dbconditiondialogFormVisible = true
        this.plandbdialogStatus = 'update'
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

      showUpdatescenedbconditionDialog(index) {
        this.scenedbconditiondialogFormVisible = true
        this.scenedbdialogStatus = 'update'
        this.tmpscenedbcondition.id = this.scenedbconditioncaseList[index].id
        this.tmpscenedbcondition.conditionid = this.scenedbconditioncaseList[index].conditionid
        this.tmpscenedbcondition.assembleid = this.scenedbconditioncaseList[index].assembleid
        this.tmpscenedbcondition.enviromentid = this.scenedbconditioncaseList[index].enviromentid
        this.tmpscenedbcondition.enviromentname = this.scenedbconditioncaseList[index].enviromentname
        this.tmpscenedbcondition.assemblename = this.scenedbconditioncaseList[index].assemblename
        this.tmpscenedbcondition.conditionname = this.scenedbconditioncaseList[index].conditionname
        this.tmpscenedbcondition.subconditionname = this.scenedbconditioncaseList[index].subconditionname
        this.tmpscenedbcondition.dbtype = this.scenedbconditioncaseList[index].dbtype
        this.tmpscenedbcondition.dbcontent = this.scenedbconditioncaseList[index].dbcontent
        this.tmpscenedbcondition.creator = this.name
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

      updatescenedbcondition() {
        this.$refs.tmpscenedbcondition.validate(valid => {
          if (valid) {
            updatedbcondition(this.tmpscenedbcondition).then(() => {
              this.$message.success('更新成功')
              this.getscenedbconditionList()
              this.scenedbconditiondialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      getplandbconditionList() {
        searchdbcondition(this.searchdbcondition).then(response => {
          this.dbconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载数据库条件列表失败')
        })
      },

      getscenedbconditionList() {
        searchdbcondition(this.searchdbcondition).then(response => {
          this.scenedbconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载数据库条件列表失败')
        })
      },
      creatorselectChanged(e) {
        if (e === '全部') {
          this.search.creator = 'admin'
        } else {
          this.search.creator = this.name
        }
      },

      ConditionselectChangedAS(e) {
        for (let i = 0; i < this.enviroment_assembleList.length; i++) {
          if (this.enviroment_assembleList[i].deployunitname === e) {
            this.tmpdbcondition.assembleid = this.enviroment_assembleList[i].assembleid
          }
        }
      },

      SceneConditionselectChangedAS(e) {
        for (let i = 0; i < this.enviroment_assembleList.length; i++) {
          if (this.enviroment_assembleList[i].deployunitname === e) {
            this.tmpscenedbcondition.assembleid = this.enviroment_assembleList[i].assembleid
          }
        }
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

      sceneselectChangedEN(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpscenedbcondition.enviromentid = this.enviromentnameList[i].id
            this.searchassemble.envid = this.enviromentnameList[i].id
            this.findMacAndDepWithEnv()
          }
        }
      },

      dbconditioncaseIndex(index) {
        return (this.searchdbcondition.page - 1) * this.searchdbcondition.size + index + 1
      },

      getdbconditionList() {
        searchdbcondition(this.searchdbcondition).then(response => {
          this.dbconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试数据库条件列表失败')
        })
      },

      showAddScenedbconditionDialog(index) {
        this.scenedbconditiondialogFormVisible = true
        this.scenedbdialogStatus = 'add'
        this.tmpscenedbcondition.id = ''
        this.tmpscenedbcondition.assembleid = ''
        this.tmpscenedbcondition.enviromentid = ''
        this.tmpscenedbcondition.enviromentname = ''
        this.tmpscenedbcondition.assemblename = ''
        this.tmpscenedbcondition.subconditionname = ''
        this.tmpscenedbcondition.dbtype = ''
        this.tmpscenedbcondition.dbcontent = ''
        this.tmpscenedbcondition.creator = this.name
        this.getassembleallnameList()
        this.getenviromentallList()
      },
      AddcasedbconditionDialog(index) {
        this.dbconditiondialogFormVisible = true
        this.plandbdialogStatus = 'add'
        this.tmpdbcondition.id = ''
        this.tmpdbcondition.assembleid = ''
        this.tmpdbcondition.enviromentid = ''
        this.tmpdbcondition.enviromentname = ''
        this.tmpdbcondition.assemblename = ''
        this.tmpdbcondition.subconditionname = ''
        this.tmpdbcondition.dbtype = ''
        this.tmpdbcondition.dbcontent = ''
        this.tmpdbcondition.creator = this.name
        this.getassembleallnameList()
        this.getenviromentallList()
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

      addscenedbcondition() {
        this.$refs.tmpscenedbcondition.validate(valid => {
          if (valid) {
            adddbcondition(this.tmpscenedbcondition).then(() => {
              this.$message.success('添加成功')
              this.scenedbconditiondialogFormVisible = false
              this.getscenedbconditionList()
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      getstopplanbatchList() {
        getstopplanbatchList(this.tmpstopplanbatch).then(response => {
          this.stopplanbatchList = response.data
        }).catch(res => {
          this.$message.error('加载停止计划列表失败')
        })
      },
      /**
       * 获取延时条件列表
       */
      getdelayconditionList() {
        this.Scenedelaysearch.conditiontype = 'scene'
        searchbytype(this.Scenedelaysearch).then(response => {
          this.scenedelayconditionList = response.data.list
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
        this.tmpdelaycondition.id = this.scenedelayconditionList[index].id
        this.tmpdelaycondition.subconditionname = this.scenedelayconditionList[index].subconditionname
        this.tmpdelaycondition.delaytime = this.scenedelayconditionList[index].delaytime
        this.tmpdelaycondition.creator = this.name
      },
      /**
       * 删除延时条件
       * @param index 延时条件下标
       */
      removescenedelaycondition(index) {
        this.$confirm('删除该延时条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scenedelayconditionList[index].id
          removedelaycondition(id).then(() => {
            this.$message.success('删除成功')
            this.getdelayconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      updatebatchstatus(index) {
        this.$confirm('确认停止该执行计划？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          updatebatchstatus(this.tmpstopplanbatch).then(() => {
            this.$message.success('停止成功')
            this.stopbatchdialogFormVisible = false
            this.getdelayconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消停止')
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
      /**
       * 显示添加延时条件对话框
       */
      showAddScenedelayconditionDialog() {
        // 显示新增对话框
        this.DelaydialogFormVisible = true
        this.DelaydialogStatus = 'add'
        this.tmpdelaycondition.id = ''
        this.tmpdelaycondition.subconditionname = ''
        this.tmpdelaycondition.conditionid = this.tmpsceneapicondition.conditionid
        this.tmpdelaycondition.conditionname = this.tmpsceneapicondition.conditionname
        this.tmpdelaycondition.conditiontype = 'scene'
        this.tmpdelaycondition.delaytime = ''
        this.tmpdelaycondition.creator = this.name
        this.tmpdelaycondition.projectid = window.localStorage.getItem('pid')
      },
      showtestscenecaseConditionDialog(index) {
        this.scenecaseConditionFormVisible = true
        this.tmpsubconditionid = this.testplansceneList[index].testscenenid
        this.tmpsceneapicondition.conditionid = this.testplansceneList[index].testscenenid
        this.tmpsceneapicondition.conditionname = this.testplansceneList[index].scenename
        this.tmpsceneapicondition.conditiontype = 'scene'
        this.searchapicondition.conditiontype = 'scene'
        this.searchapicondition.conditionid = this.testplansceneList[index].testscenenid
        this.Scenedelaysearch.conditionid = this.testplansceneList[index].testscenenid
        this.searchdbcondition.conditiontype = 'scene'
        this.searchdbcondition.conditionid = this.testplansceneList[index].testscenenid
        this.tmpscenedbcondition.conditionid = this.testplansceneList[index].testscenenid
        this.tmpscenedbcondition.conditionname = this.testplansceneList[index].scenename
        this.tmpscenedbcondition.conditiontype = 'scene'
        this.tmpscriptcondition.conditionid = this.testplansceneList[index].id
        this.tmpscriptcondition.conditionname = this.testplansceneList[index].scenename
        this.tmpscriptcondition.conditiontype = 'scene'
        this.searchscriptcondition.conditiontype = 'scene'
        this.searchscriptcondition.conditionid = this.testplansceneList[index].id
        this.getsceneapiconditionList()
        this.getdelayconditionList()
        this.getscenedbconditionList()
        this.getscriptconditionList()
      },
      /**
       * 删除测试场景
       * @param index 测试场景下标
       */
      removetestplanscene(index) {
        this.$confirm('删除该测试场景？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.testplansceneList[index].id
          removetestplanscene(id).then(() => {
            this.$message.success('删除成功')
            this.findscenebyexecplanid()
            this.getexecuteplanList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
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
      },

      showUpdatesceneconditionDialog(index) {
        this.SceneconditiondialogFormVisible = true
        this.SceneconditiondialogStatus = 'update'
        this.tmpsceneapicondition.id = this.sceneapiconditioncaseList[index].id
        this.tmpsceneapicondition.subconditionname = this.sceneapiconditioncaseList[index].subconditionname
        this.tmpsceneapicondition.deployunitname = this.sceneapiconditioncaseList[index].deployunitname
        this.tmpsceneapicondition.apiname = this.sceneapiconditioncaseList[index].apiname
        this.tmpsceneapicondition.casename = this.sceneapiconditioncaseList[index].casename
        this.tmpsceneapicondition.modelname = this.sceneapiconditioncaseList[index].modelname
        this.tmpsceneapicondition.memo = this.sceneapiconditioncaseList[index].memo
        this.tmpsceneapicondition.creator = this.name
        this.tmpsceneapicondition.projectid = window.localStorage.getItem('pid')
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

      updatescenecondition() {
        this.$refs.tmpsceneapicondition.validate(valid => {
          if (valid) {
            updateapicondition(this.tmpsceneapicondition).then(() => {
              this.$message.success('更新成功')
              this.getsceneapiconditionList()
              this.SceneconditiondialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },
      removecasescenecondition(index) {
        this.$confirm('删除该前置条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.sceneapiconditioncaseList[index].id
          removeapicondition(id).then(() => {
            this.$message.success('删除成功')
            this.getsceneapiconditionList()
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

      apiconditiontestcaseselectChanged(e) {
        for (let i = 0; i < this.conditioncaseList.length; i++) {
          if (this.conditioncaseList[i].casename === e) {
            this.tmpapicondition.caseid = this.conditioncaseList[i].id
          }
        }
      },

      sceneconditiontestcaseselectChanged(e) {
        for (let i = 0; i < this.sceneconditioncaseList.length; i++) {
          if (this.sceneconditioncaseList[i].casename === e) {
            this.tmpsceneapicondition.caseid = this.sceneconditioncaseList[i].id
          }
        }
      },
      sceneconditionmodelselectChanged(e) {
        this.tmpsceneapicondition.apiname = null
        this.tmpsceneapicondition.casename = null
        for (let i = 0; i < this.sceneconditionmodelList.length; i++) {
          if (this.sceneconditionmodelList[i].modelname === e) {
            this.tmpsceneapicondition.modelid = this.sceneconditionmodelList[i].id
          }
        }
        if (e === '') {
          this.tmpsceneapicondition.modelid = 0
          this.tmpsceneapicondition.modelname = ''
        }
        getapiListbydeploy(this.tmpsceneapicondition).then(response => {
          this.sceneconditionapiList = response.data
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

      apiconditionapiselectChanged(e) {
        this.tmpapicondition.casename = null
        for (let i = 0; i < this.apiconditionapiList.length; i++) {
          if (this.apiconditionapiList[i].apiname === e) {
            this.tmpapicondition.apiid = this.apiconditionapiList[i].id
          }
        }
        findcasesbyname(this.tmpapicondition).then(response => {
          this.conditioncaseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },

      sceneconditionapiselectChanged(e) {
        this.tmpsceneapicondition.casename = null
        for (let i = 0; i < this.sceneconditionapiList.length; i++) {
          if (this.sceneconditionapiList[i].apiname === e) {
            this.tmpsceneapicondition.apiid = this.sceneconditionapiList[i].id
          }
        }
        findcasesbyname(this.tmpsceneapicondition).then(response => {
          this.sceneconditioncaseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },
      showtestplanConditionDialog(index) {
        this.ConditionFormVisible = true
        this.tmpsubconditionid = this.executeplanList[index].id
        this.searchapicondition.conditiontype = 'execplan'
        this.searchapicondition.conditionid = this.executeplanList[index].id
        this.tmpapicondition.conditionid = this.executeplanList[index].id
        this.tmpapicondition.conditionname = this.executeplanList[index].executeplanname
        this.tmpapicondition.conditiontype = 'execplan'
        this.searchdbcondition.conditiontype = 'execplan'
        this.searchdbcondition.conditionid = this.executeplanList[index].id
        this.tmpdbcondition.conditionid = this.executeplanList[index].id
        this.tmpdbcondition.conditionname = this.executeplanList[index].executeplanname
        this.tmpdbcondition.conditiontype = 'execplan'
        this.searchplanscriptcondition.conditiontype = 'execplan'
        this.searchplanscriptcondition.conditionid = this.executeplanList[index].id
        this.tmpplanscriptcondition.conditionid = this.executeplanList[index].id
        this.tmpplanscriptcondition.conditionname = this.executeplanList[index].executeplanname
        this.tmpplanscriptcondition.conditiontype = 'execplan'
        this.getapiconditionList()
        this.getplandbconditionList()
        this.getplanscriptconditionList()
      },

      apiconditioncaseIndex(index) {
        return (this.searchapicondition.page - 1) * this.searchapicondition.size + index + 1
      },

      scenedelaygetIndex(index) {
        return (this.Scenedelaysearch.page - 1) * this.Scenedelaysearch.size + index + 1
      },

      scenedbgetIndex(index) {
        return (this.searchdbcondition.page - 1) * this.searchdbcondition.size + index + 1
      },

      scenescriptgetIndex(index) {
        return (this.searchscriptcondition.page - 1) * this.searchscriptcondition.size + index + 1
      },
      planapiconditioncaseIndex(index) {
        return (this.searchplanapicondition.page - 1) * this.searchplanapicondition.size + index + 1
      },
      plandbgetIndex(index) {
        return (this.searchdbcondition.page - 1) * this.searchdbcondition.size + index + 1
      },

      planscriptgetIndex(index) {
        return (this.searchplanscriptcondition.page - 1) * this.searchplanscriptcondition.size + index + 1
      },

      getapiconditionList() {
        searchapicondition(this.searchapicondition).then(response => {
          this.apiconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试接口条件列表失败')
        })
      },

      getsceneapiconditionList() {
        searchapicondition(this.searchapicondition).then(response => {
          this.sceneapiconditioncaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载测试接口条件列表失败')
        })
      },

      addscenecondition() {
        this.$refs.tmpsceneapicondition.validate(valid => {
          if (valid) {
            addapicondition(this.tmpsceneapicondition).then(() => {
              this.$message.success('添加成功')
              this.SceneconditiondialogFormVisible = false
              this.getsceneapiconditionList()
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
      sceneconditiondeployunitselectChanged(e) {
        this.tmpsceneapicondition.casename = null
        this.tmpsceneapicondition.apiname = null
        this.tmpsceneapicondition.modelname = null
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpsceneapicondition.deployunitid = this.deployunitList[i].id
          }
        }
        searchdeployunitmodel(this.tmpsceneapicondition).then(response => {
          this.sceneconditionmodelList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
        this.sceneconditionapiList = null
        getapiListbydeploy(this.tmpsceneapicondition).then(response => {
          this.sceneconditionapiList = response.data
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

      cancelEdit(row) {
        row.ordernum = row.oldcaseorder
        row.edit = false
        // this.$message({
        //   message: 'The title has been restored to the original value',
        //   type: 'warning'
        // })
      },
      confirmEdit(row) {
        row.edit = false
        console.log(row)
        updateplanscenenorder(row).then(response => {
          row.oldcaseorder = row.ordernum
          this.$message.success('修改顺序成功')
        }).catch(res => {
          row.ordernum = row.oldcaseorder
          this.$message.error('修改顺序失败')
        })
        // console.log(22222222222222222)
        // console.log(row)
        // this.$message({
        //   message: 'The title has been edited',
        //   type: 'success'
        // })
      },

      findscenebyexecplanid() {
        findscenebyexecplanid(this.searchscene).then(response => {
          this.testplansceneList = response.data.list
          const items = response.data.list
          this.testplansceneList = items.map(v => {
            this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
            v.oldcaseorder = v.ordernum //  will be used when user click the cancel botton
            return v
          })
          this.testplanscenetotal = response.data.total
        }).catch(res => {
          this.$message.error('加载场景用例列表失败')
        })
      },

      addscenehandleClickTableRow(row, event, column) {
      },
      addscenehandleSelectionChange(rows) {
        this.scenemultipleSelection = rows
      },

      addscenehandleCurrentChange(page) {
        this.addsearchscene.page = page
        this.gettestsceneList()
      },

      planscenehandleCurrentChange(page) {
        this.searchscene.page = page
        this.findscenebyexecplanid()
      },

      addscenegetIndex(index) {
        return (this.addsearchscene.page - 1) * this.addsearchscene.size + index + 1
      },

      gettestsceneList() {
        this.addsearchscene.scenename = this.tmptestscenename
        searchscence(this.addsearchscene).then(response => {
          this.testsceneList = response.data.list
          this.addscenetotal = response.data.total
        }).catch(res => {
          this.$message.error('加载场景列表失败')
        })
      },

      addscenehandleSizeChange(size) {
        this.addsearchscene.page = 1
        this.addsearchscene.size = size
        this.gettestsceneList()
      },

      planscenehandleSizeChange(size) {
        this.searchscene.page = 1
        this.searchscene.size = size
        this.findscenebyexecplanid()
      },
      /**
       * 获取组件名字典列表
       */
      getdatabydiccodeList() {
        getdatabydiccodeList(this.diclevelQuery).then(response => {
          this.planbusinessdiclist = response.data.list
        }).catch(res => {
          this.$message.error('加载组件名字典列表失败')
        })
      },

      unix2CurrentTime,

      getglobalheaderallList() {
        getglobalheaderallList(this.search).then(response => {
          this.globalheaderallList = response.data
        }).catch(res => {
          this.$message.error('加载全局Header列表失败')
        })
      },

      /**
       * 获取header列表
       */
      searchheaderbyepid() {
        console.log(this.tmpep)
        searchheaderbyepid(this.tmpep).then(response => {
          this.paramsList = response.data.list
        }).catch(res => {
          this.$message.error('加载header列表失败')
        })
      },

      searchplannmessagebyepid() {
        searchplannmessage(this.searchnotice).then(response => {
          this.planmessageList = response.data.list
        }).catch(res => {
          this.$message.error('加载header列表失败')
        })
      },

      /**
       * 停止执行计划
       */
      stopexecuteplanList() {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.multipleSelection[i].status === 'waiting' || this.multipleSelection[i].status === 'new' || this.multipleSelection[i].status === 'finish' || this.multipleSelection[i].status === 'stop') {
            this.multipleSelection.splice(i)
          }
        }
        if (this.multipleSelection.length === 0) {
          this.$message.error('未选择执行计划,或者只有运行中才可以停止')
        } else {
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.executeplanstopList.push({
              'id': this.multipleSelection[i].id,
              'status': 'stop'
            })
          }
          updateexecuteplanstatus(this.executeplanstopList).then(() => {
            this.$message.success('已停止执行计划')
            this.btnLoading = false
          }).catch(res => {
            this.$message.error('停止计划执行失败')
            this.btnLoading = false
          })
        }
        this.getexecuteplanList()
      },
      /**
       * 添加执行计划批次
       */
      savebatchandexecuteplancase() {
        this.$refs.tmpplanbatch.validate(valid => {
          if (valid) {
            if (this.multipleSelection.length === 0) {
              this.$message.error('未选择执行计划,或者所选计划已经在执行中')
              return
            }
            if (this.multipleSelection.length > 1) {
              this.$message.error('不支持多执行计划一起提交，单个提交')
              return
            }
            this.tmpplanbatch.executeplanname = this.multipleSelection[0].executeplanname
            this.tmpplanbatch.execdate = this.tmpplanbatch.exectmpdate + ' ' + this.tmpplanbatch.exectime + ':00'
            if (this.tmpplanbatch.execdate === ':00') {
              this.tmpplanbatch.execdate = '/'
            }
            if (this.tmpplanbatch.exectmpdate === '') {
              this.tmpplanbatch.execdate = this.tmpplanbatch.exectime + ':00'
            }
            this.executeplancase()
            // addexecuteplanbatch(this.tmpplanbatch).then(() => {
            //   this.executeplancase()
            //   this.batchdialogFormVisible = false
            // }).catch(res => {
            //   this.$message.error('计划执行失败')
            // })
          }
        })
      },
      /**
       * 添加执行计划批次并且执行计划用例
       */
      // savebatchandexecuteplancase() {
      // this.addexecuteplanbatch()
      // this.executeplancase()
      // this.batchdialogFormVisible = false
      // },
      /**
       * 执行执行计划
       */
      executeplancase() {
        this.tmpplanbatchList = []
        this.$refs.tmpplanbatch.validate(valid => {
          if (valid) {
            for (let i = 0; i < this.multipleSelection.length; i++) {
              if (this.multipleSelection[i].status === 'running') {
                this.multipleSelection.splice(i)
              }
            }
            if (this.multipleSelection.length === 0) {
              this.$message.error('未选择执行计划,或者所选计划已经在执行中')
            } else {
              if (this.multipleSelection.length > 1) {
                this.$message.error('不支持多执行计划一起提交，单个提交')
              } else {
                for (let i = 0; i < this.multipleSelection.length; i++) {
                  this.tmpplanbatchList.push({
                    // 'planid': this.multipleSelection[0].id,
                    // 'planname': this.multipleSelection[0].execplanname,
                    // 'batchname': this.tmpplanbatch.batchname
                    'executeplanname': this.tmpplanbatch.executeplanname,
                    'batchname': this.tmpplanbatch.batchname,
                    'exectype': this.tmpplanbatch.exectype,
                    'execdate': this.tmpplanbatch.execdate,
                    'creator': this.name,
                    'projectid': this.tmpplanbatch.projectid,
                    'exectime': this.tmpplanbatch.exectime,
                    'executeplanid': this.tmpplanbatch.executeplanid,
                    'exectmpdate': this.tmpplanbatch.exectmpdate,
                    'source': '平台'
                  })
                }
                executeplan(this.tmpplanbatchList).then(() => {
                  this.$message.success('测试集合已经提交，即将开始执行')
                  this.batchdialogFormVisible = false
                  this.getexecuteplanList()
                }).catch(res => {
                  this.batchdialogFormVisible = true
                  this.$message.error('执行测试集合失败')
                })
              }
            }
          }
        })
      },

      handleClickTableRow(row, event, column) {
        // console.log(row)
        // console.log(column)
        // this.$refs.fileTable.toggleRowSelection(row)
      },
      handleSelectionChange(rows) {
        // console.log(rows)
        this.multipleSelection = rows
        console.log('00000000000000000000000000')
        console.log(this.multipleSelection)
      },
      casehandleClickTableRow(row, event, column) {
        console.log(row)
      },

      casehandleSelectionChange(rows) {
        this.casemultipleSelection = rows
        // console.log(this.casemultipleSelection)
      },

      exectypeselectChanged(e) {
        if (e === '立即执行') {
          this.datevisible = false
          this.timevisible = false
        }
        if (e === '某天定时') {
          this.datevisible = true
          this.timevisible = true
          this.tmpplanbatch.execdate = ''
          this.tmpplanbatch.exectime = ''
        }
        if (e === '每天定时') {
          this.datevisible = false
          this.timevisible = true
          this.tmpplanbatch.exectime = ''
        }
      },
      /**
       * 参数胡类型选择  e的值为options的选值
       */
      paramstypeselectChanged(e) {
        this.tmpparam.keyname = ''
        this.tmpparam.keyvalue = ''
      },
      /**
       * 获取执行计划列表
       */
      getexecuteplanList() {
        this.search.execplanname = this.tmpexecplanname
        this.search.batchname = this.tmpbatchname
        this.search.creator = this.name
        this.listLoading = true
        search(this.search).then(response => {
          this.executeplanList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载字典列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.executeplanList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpexecuteplanname = this.search.executeplanname
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
        this.getexecuteplanList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getexecuteplanList()
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

      paramgetIndex(index) {
        return (this.searchparam.page - 1) * this.searchparam.size + index + 1
      },

      plannmessagegetIndex(index) {
        return (this.searchnotice.page - 1) * this.searchnotice.size + index + 1
      },

      planscenegetIndex(index) {
        return (this.searchscene.page - 1) * this.searchscene.size + index + 1
      },
      /**
       * 环境下拉框活的环境id
       */
      enviromentselectChanged(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpexecuteplan.envid = this.enviromentnameList[i].id
          }
          console.log(this.enviromentnameList[i].id)
        }
      },
      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChanged(e) {
        this.apiList = null
        this.apiQuery.deployunitname = e
        getapiList(this.apiQuery).then(response => {
          this.apiList = response.data.list
          // console.log(this.apiList)
          // this.casetotal = response.data.total
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },
      /**
       * 获取api列表
       */
      getapiList() {
        this.caselistLoading = true
        this.listQuery.creator = this.name
        getapiList(this.listQuery).then(response => {
          this.apiList = response.data.list
          // this.casetotal = response.data.total
          this.caselistLoading = false
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
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

      /**
       * 获取微服务列表
       */
      getdepunitList() {
        this.caselistLoading = true
        getdepunitLists(this.listQuery).then(response => {
          this.deployunitList = response.data
          // this.casetotal = response.data.total
          // this.caselistLoading = false
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      /**
       * 获取用例列表
       */
      getcaseList() {
        this.caselistLoading = true
        getapicasesList(this.listQuery).then(response => {
          this.apicasesList = response.data.list
          // this.casetotal = response.data.total
          this.caselistLoading = false
        }).catch(res => {
          this.$message.error('加载用例列表失败')
        })
      },

      /**
       * 获取微服务和api的用例
       */
      searchcaseBy() {
        this.searchtestplanexistcase()
      },

      // 已废弃
      async gettestcaselastList() {
        console.log(this.testcaseList.length)
        console.log(this.executeplancaseexistList.length)
        for (let i = 0; i < this.testcaseList.length; i++) {
          for (let j = 0; j < this.executeplancaseexistList.length; j++) {
            if (this.testcaseList[i].id === this.executeplancaseexistList[j].testcaseid) {
              this.testcaselastList.push({
                'checkstats': true,
                'deployunitname': this.testcaseList[i].deployunitname,
                'apiname': this.testcaseList[i].apiname,
                'casename': this.testcaseList[i].casename,
                'expect': this.testcaseList[i].expect
              })
            } else {
              this.testcaselastList.push({
                'checkstats': false,
                'deployunitname': this.testcaseList[i].deployunitname,
                'apiname': this.testcaseList[i].apiname,
                'casename': this.testcaseList[i].casename,
                'expect': this.testcaseList[i].expect
              })
            }
          }
        }
        console.log(this.testcaselastList)
      },

      /**
       * 获取微服务和api的用例，已废弃
       */
      async searchcaseBydepandapi() {
        this.$refs.searchcase.validate(valid => {
          if (valid) {
            this.casebtnLoading = true
            this.caselistLoading = true
            this.searchcase.page = this.caselistQuery.page
            this.searchcase.size = this.caselistQuery.size
            getapicases(this.searchcase).then(response => {
              this.testcaseList = response.data.list
              console.log(this.testcaseList.length)
              this.casetotal = response.data.total
            }).catch(res => {
              this.$message.error('搜索失败')
            })
            this.caselistLoading = false
            this.casebtnLoading = false
          }
        })
      },

      /**
       * 获取执行计划下微服务和api已的用例,如果是已装载过的，会带上装载状态
       */
      searchtestplanexistcase() {
        this.testcaselastList = []
        this.$refs.searchcase.validate(valid => {
          if (valid) {
            this.searchcase.executeplanid = this.tmpexecuteplan.id
            this.searchcase.casetype = this.tmpexecuteplan.usetype
            // this.searchcase.page = this.caselistQuery.page
            // this.searchcase.size = this.caselistQuery.size
            this.searchcase.page = 1
            searchtestplancases(this.searchcase).then(response => {
              this.testcaselastList = response.data.list
              this.casetotal = response.data.total
              this.$nextTick(() => {
                for (let i = 0; i < this.testcaselastList.length; i++) {
                  if (this.testcaselastList[i].status === true) {
                    this.$refs.caseTable.toggleRowSelection(this.testcaselastList[i], true)
                  }
                }
              })
            }).catch(res => {
              this.$message.error('获取已装载的用例失败')
            })
          }
        })
        this.tmpcasedeployunitname = this.searchcase.deployunitname
        this.tmpcaseapiname = this.searchcase.apiname
        this.tmpcaseexecuteplanid = this.searchcase.executeplanid
        this.tmpcasecasetype = this.searchcase.casetype
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      casehandleSizeChange(size) {
        this.searchcase.page = 1
        this.searchcase.size = size
        this.searchtestplanexistcase()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      casehandleCurrentChange(page) {
        this.searchcase.page = page
        this.searchtestplanexistcase()
      },
      /**
       * 表格序号
       * 可参考自定义表格序号
       * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
       * @param index 数据下标
       * @returns 表格序号
       */
      casegetIndex(index) {
        return (this.searchcase.page - 1) * this.searchcase.size + index + 1
      },
      /**
       * 显示添加执行计划对话框
       */
      showAddexecuteplanDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpexecuteplan.id = ''
        this.tmpexecuteplan.executeplanname = ''
        this.tmpexecuteplan.status = 'new'
        this.tmpexecuteplan.memo = ''
        this.tmpexecuteplan.usetype = ''
        this.tmpexecuteplan.enviromentname = ''
        this.tmpexecuteplan.businesstype = ''
        this.tmpexecuteplan.creator = this.name
        this.tmpexecuteplan.runmode = ''
        this.tmpexecuteplan.dingdingtoken = ''
        this.tmpexecuteplan.projectid = window.localStorage.getItem('pid')
      },

      showplanparamsDialog(index) {
        // 显示新增对话框
        this.CollectionParamsFormVisible = true
        this.tmpparam.executeplanid = this.executeplanList[index].id
        this.tmpep.executeplanid = this.executeplanList[index].id
        this.searchheaderbyepid()
      },

      showplannmessageDialog(index) {
        // 显示新增对话框
        this.CollectionNoticeFormVisible = true
        this.tmpplannmessage.executeplanid = this.executeplanList[index].id
        this.searchnotice.executeplanid = this.executeplanList[index].id
        this.searchplannmessagebyepid()
      },
      showstopbatchDialog(index) {
        // 显示新增对话框
        this.stopbatchdialogFormVisible = true
        this.tmpstopplanbatch.batchname = ''
        this.tmpstopplanbatch.executeplanid = this.executeplanList[index].id
        this.getstopplanbatchList()
      },
      showtestsceneDialog(index) {
        // 显示新增对话框
        this.testscenedialogFormVisible = true
        this.tmpexecplan.execplanid = this.executeplanList[index].id
        this.tmpexecplan.execplanname = this.executeplanList[index].executeplanname
        this.searchscene.testplanid = this.executeplanList[index].id
        this.addsearchscene.usetype = this.executeplanList[index].usetype
        this.findscenebyexecplanid()
      },

      ShowAddScenceDialog(index) {
        // 显示新增对话框
        this.addtestscenedialogFormVisible = true
        this.gettestsceneList()
      },

      ShowAddPlancaseconditionDialog(index) {
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

      ShowAddPlanSceneconditionDialog(index) {
        this.SceneconditiondialogFormVisible = true
        this.SceneconditiondialogStatus = 'add'
        this.tmpsceneapicondition.id = ''
        this.tmpsceneapicondition.subconditionname = ''
        this.tmpsceneapicondition.deployunitname = ''
        this.tmpsceneapicondition.apiname = ''
        this.tmpsceneapicondition.modelname = ''
        this.tmpsceneapicondition.casename = ''
        this.tmpsceneapicondition.memo = ''
        this.tmpsceneapicondition.creator = this.name
        this.tmpsceneapicondition.projectid = window.localStorage.getItem('pid')
      },

      // 显示新增对话框
      showAddapiparamsDialog() {
        this.modifyparamdialogFormVisible = true
        this.ParamsdialogStatus = 'add'
        this.tmpparam.id = ''
        this.tmpparam.globalheaderid = ''
        this.tmpparam.globalheadername = ''
      },

      showCollectionNoticeDialog() {
        this.modifyplannmessagedialogFormVisible = true
        this.plannmessagedialogStatus = 'add'
        this.tmpplannmessage.id = ''
        this.tmpplannmessage.memo = ''
        this.tmpplannmessage.mid = this.accountId
        this.tmpplannmessage.messagetype = ''
        this.tmpplannmessage.hookcontent = ''
      },

      showUpdateparamsDialog(index) {
        this.modifyparamdialogFormVisible = true
        this.ParamsdialogStatus = 'update'
        this.tmpparam.id = this.paramsList[index].id
        this.tmpparam.globalheadername = this.paramsList[index].globalheadername
      },

      showUpdateplannmessageDialog(index) {
        this.modifyplannmessagedialogFormVisible = true
        this.plannmessagedialogStatus = 'update'
        this.tmpplannmessage.id = this.planmessageList[index].id
        this.tmpplannmessage.memo = this.planmessageList[index].memo
        this.tmpplannmessage.hookcontent = this.planmessageList[index].hookcontent
        this.tmpplannmessage.messagetype = this.planmessageList[index].messagetype
      },
      /**
       * 显示添加执行计划批次对话框
       */
      showplanbatchDialog() {
        this.tmpplanbatch.exectmpdate = ''
        this.tmpplanbatch.exectime = ''
        this.tmpplanbatch.execdate = ''
        this.tmpplanbatch.exectype = ''
        this.timevisible = false
        this.datevisible = false
        // 显示新增对话框
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.multipleSelection[i].status === 'running') {
            this.multipleSelection.splice(i)
          }
        }
        if (this.multipleSelection.length === 0) {
          this.$message.error('未选择执行计划,或者所选计划已经在执行中')
        } else {
          if (this.multipleSelection.length > 1) {
            this.$message.error('不支持多执行计划一起提交，单个提交')
          } else {
            this.tmpplanenv.id = this.multipleSelection[0].id
            this.tmpplanenv.envid = this.multipleSelection[0].envid
            this.tmpplanenv.enviromentname = this.multipleSelection[0].enviromentname
            checkplancondition(this.tmpplanenv).then(() => {
              this.batchdialogFormVisible = true
              this.tmpplanbatch.executeplanid = this.multipleSelection[0].id
              this.tmpplanbatch.creator = this.name
              this.tmpplanbatch.batchname = ''
            }).catch(res => {
              // this.$message.error('执行失败')
            })
          }
        }
      },
      /**
       * 添加执行计划
       */
      addexecuteplan() {
        this.$refs.tmpexecuteplan.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addexecuteplan(this.tmpexecuteplan).then(() => {
              this.$message.success('添加成功')
              this.getexecuteplanList()
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
       * 添加params
       */
      addparams() {
        this.$refs.tmpparam.validate(valid => {
          if (valid) {
            addapicasesdebug(this.tmpparam).then(() => {
              this.$message.success('添加成功')
              this.modifyparamdialogFormVisible = false
              this.searchheaderbyepid()
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      addplannmessageparam() {
        this.$refs.tmpplannmessage.validate(valid => {
          if (valid) {
            addplannmessageparam(this.tmpplannmessage).then(() => {
              this.$message.success('添加成功')
              this.modifyplannmessagedialogFormVisible = false
              this.searchplannmessagebyepid()
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      /**
       * 更新param
       */
      updatparam() {
        this.$refs.tmpparam.validate(valid => {
          if (valid) {
            updateapicasesdebug(this.tmpparam).then(() => {
              this.$message.success('更新成功')
              this.searchheaderbyepid()
              this.modifyparamdialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      updateplannmessageparams() {
        this.$refs.tmpplannmessage.validate(valid => {
          if (valid) {
            updateplannmessageparams(this.tmpplannmessage).then(() => {
              this.$message.success('更新成功')
              this.searchplannmessagebyepid()
              this.modifyplannmessagedialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
            })
          }
        })
      },

      /**
       * 删除param
       * @param index api下标
       */
      removeexecuteplanparam(index) {
        this.$confirm('删除该参数？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.paramsList[index].id
          removeapicasesdebug(id).then(() => {
            this.$message.success('删除成功')
            this.searchheaderbyepid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removeplannmessageparam(index) {
        this.$confirm('删除该通知？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.planmessageList[index].id
          removeplannmessageparam(id).then(() => {
            this.$message.success('删除成功')
            this.searchplannmessagebyepid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 显示修改执行计划对话框
       * @param index 执行计划下标
       */
      showUpdateexecuteplanDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpexecuteplan.id = this.executeplanList[index].id
        this.tmpexecuteplan.executeplanname = this.executeplanList[index].executeplanname
        this.tmpexecuteplan.status = this.executeplanList[index].status
        this.tmpexecuteplan.usetype = this.executeplanList[index].usetype
        this.tmpexecuteplan.memo = this.executeplanList[index].memo
        this.tmpexecuteplan.enviromentname = this.executeplanList[index].enviromentname
        this.tmpexecuteplan.businesstype = this.executeplanList[index].businesstype
        this.tmpexecuteplan.creator = this.name
        this.tmpexecuteplan.runmode = this.executeplanList[index].runmode
        this.tmpexecuteplan.dingdingtoken = this.executeplanList[index].dingdingtoken
        this.tmpexecuteplan.projectid = window.localStorage.getItem('pid')
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === this.tmpexecuteplan.enviromentname) {
            this.tmpexecuteplan.envid = this.enviromentnameList[i].id
          }
        }
      },

      /**
       * 装载执行计划的用例
       */
      addplantestscene() {
        this.addtestsceneList = []
        if (this.scenemultipleSelection.length === 0) {
          this.$message.error('请选择添加的场景')
        } else {
          for (let i = 0; i < this.scenemultipleSelection.length; i++) {
            this.addtestsceneList.push({
              'testscenenid': this.scenemultipleSelection[i].id,
              'scenename': this.scenemultipleSelection[i].scenename,
              'testplanid': this.tmpexecplan.execplanid,
              'planname': this.tmpexecplan.execplanname,
              'ordernum': 0,
              'creator': this.name,
              'projectid': window.localStorage.getItem('pid')
            })
          }
          addtestplantestscene(this.addtestsceneList).then(() => {
            this.$message.success('装载成功')
            this.addtestscenedialogFormVisible = false
            this.findscenebyexecplanid()
            this.getexecuteplanList()
          }).catch(res => {
            this.$message.error('装载失败')
          })
        }
      },

      /**
       * 装载执行计划的用例
       */
      addexecuteplantestcase() {
        this.executeplancaseList = []
        if (this.casemultipleSelection.length === 0) {
          this.$message.error('请选择装载的用例')
        } else {
          for (let i = 0; i < this.casemultipleSelection.length; i++) {
            this.executeplancaseList.push({
              'executeplanid': this.tmpexecuteplan.id,
              'deployunitid': this.casemultipleSelection[i].deployunitid,
              'apiid': this.casemultipleSelection[i].apiid,
              'deployunitname': this.casemultipleSelection[i].deployunitname,
              'apiname': this.casemultipleSelection[i].apiname,
              'testcaseid': this.casemultipleSelection[i].id,
              'casename': this.casemultipleSelection[i].casename
            })
          }
          addexecuteplantestcase(this.executeplancaseList).then(() => {
            this.$message.success('装载成功')
          }).catch(res => {
            this.$message.error('装载失败')
          })
        }
      },

      /**
       * 删除装载的用例
       */
      removeexecuteplantestcase() {
        this.executeplancaseremovetList = []
        if (this.testcaselastList.length === this.casemultipleSelection.length) {
          this.$message.error('未找到需要取消装载的用例')
        } else {
          for (let i = 0; i < this.testcaselastList.length; i++) {
            var findflag = false
            for (let j = 0; j < this.casemultipleSelection.length; j++) {
              if (this.testcaselastList[i].id === this.casemultipleSelection[j].id) {
                findflag = true
                break
              }
            }
            // 表示未选中的记录，需要删除
            this.testcaselastList[i].id
            if (!findflag) {
              this.executeplancaseremovetList.push({
                'executeplanid': this.tmpexecuteplan.id,
                'deployunitname': this.testcaselastList[i].deployunitname,
                'apiname': this.testcaselastList[i].apiname,
                'testcaseid': this.testcaselastList[i].id,
                'casename': this.testcaselastList[i].casename
              })
            }
          }
          console.log(this.executeplancaseremovetList)
          removeexecuteplantestcase(this.executeplancaseremovetList).then(() => {
            this.$message.success('取消装载用例成功')
          }).catch(res => {
            this.$message.error('取消装载用例失败')
          })
        }
      },
      /**
       * 显示用例对话框
       * @param index 执行计划下标
       */
      showTestCaseDialog(index) {
        this.casedialogFormVisible = true
        this.tmpexecuteplan.id = this.executeplanList[index].id
        this.tmpexecuteplan.executeplanname = this.executeplanList[index].executeplanname
        this.tmpexecuteplan.status = this.executeplanList[index].status
        this.tmpexecuteplan.usetype = this.executeplanList[index].usetype
        this.tmpexecuteplan.memo = this.executeplanList[index].memo
        this.searchcase.deployunitname = null
        this.searchcase.apiname = null
        this.testcaselastList = []
        this.casetotal = 0
      },
      /**
       * 更新执行计划
       */
      updateexecuteplan() {
        this.$refs.tmpexecuteplan.validate(valid => {
          if (valid) {
            updateexecuteplan(this.tmpexecuteplan).then(() => {
              this.$message.success('更新成功')
              this.getexecuteplanList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
              this.btnLoading = false
            })
          }
        })
      },
      /**
       * 删除字典
       * @param index 执行计划下标
       */
      removeexecuteplan(index) {
        this.$confirm('删除该执行计划？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.executeplanList[index].id
          removeexecuteplan(id).then(() => {
            this.$message.success('删除成功')
            this.getexecuteplanList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 执行计划资料是否唯一
       * @param 执行计划
       */
      isUniqueDetail(executeplan) {
        for (let i = 0; i < this.executeplanList.length; i++) {
          if (this.executeplanList[i].id !== executeplan.id) { // 排除自己
            if (this.executeplanList[i].executeplanname === executeplan.executeplanname) {
              this.$message.error('执行计划名已存在')
              return false
            }
          }
        }
        return true
      },
      notexistheaderselectChanged(e) {
        for (let i = 0; i < this.globalheaderallList.length; i++) {
          if (this.globalheaderallList[i].globalheadername === e) {
            this.tmpparam.globalheaderid = this.globalheaderallList[i].id
          }
        }
      }
    }
  }
</script>
