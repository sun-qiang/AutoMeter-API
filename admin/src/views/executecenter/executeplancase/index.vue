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
            @click.native.prevent="getexecuteplancaseList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('executeplan:list')"
            @click.native.prevent="showTestCaseDialog"
          >装载用例</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:list')"
            @click.native.prevent="showTestCaseorderDialog"
          >用例顺序</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('executeplan:list')"
            @click.native.prevent="DeleteBatchPlanTestCase"
          >取消装载</el-button>
        </el-form-item>
        <span v-if="hasPermission('executeplan:search')">
          <el-form-item  prop="executeplanname" >
          <el-select v-model="search.executeplanname" filterable clearable placeholder="测试集合" @change="loadtestplanselectChanged($event)">
              <el-option label="请选择" value />
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
            </div>
          </el-select>
        </el-form-item>

          <el-form-item prop="deployunitname">
            <el-select v-model="search.deployunitname" filterable clearable placeholder="微服务" @change="loaddeployunitselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(depname, index) in loaddeployunitList" :key="index">
                <el-option :label="depname.deployunitname" :value="depname.deployunitname" required/>
              </div>
            </el-select>
          </el-form-item>

          <el-form-item prop="apiname">
            <el-select v-model="search.apiname" filterable clearable placeholder="api名" @change="loadApiselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(api, index) in loadapiList" :key="index">
                <el-option :label="api.apiname" :value="api.apiname"/>
              </div>
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
      :data="executeplancaseList"
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
      <el-table-column label="测试集合名" align="center" prop="executeplanname" width="150"/>
      <el-table-column label="微服务" align="center" prop="deployunitname" width="130"/>
      <el-table-column label="用例名" align="center" prop="casename" width="150"/>
      <el-table-column label="API" align="center" prop="apiname" width="150"/>
      <el-table-column label="操作人" align="center" prop="creator" width="80"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="290"
                       v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
            @click.native.prevent="removeexecuteplantestcase(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
            @click.native.prevent="removeexecuteplantestcase(scope.$index)"
          >提取变量</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('executeplan:delete') && scope.row.id !== id"
            @click.native.prevent="removeexecuteplantestcase(scope.$index)"
          >前置条件</el-button>
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
    <el-dialog width="900px" :title="loadcase" :visible.sync="casedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchcase" ref="searchcase" >

          <el-form-item label="测试集合:"  prop="executeplanname" required>
            <el-select style="width: 120px" v-model="searchcase.executeplanname" filterable placeholder="测试集合" @change="testplanselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(testplan, index) in execplanList" :key="index">
                <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item  label="微服务:" prop="deployunitname" required>
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
            <el-button type="primary" @click="searchcaseBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table
        ref="caseTable"
        :data="testcaselastList"
        :key="itemcaseKey"
        @row-click="casehandleClickTableRow"
        @selection-change="casehandleSelectionChange"
        v-loading.body="caselistLoading"
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
        <el-table-column label="用例名" align="center" prop="casename" width="280"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="180"/>
        <el-table-column label="API" align="center" prop="apiname" width="280"/>
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
      </div>
    </el-dialog>

    <el-dialog title='用例顺序' :visible.sync="testcasedialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchexistcase" ref="searchexistcase" >

          <el-form-item label="测试集合:"  prop="executeplanname" required>
            <el-select v-model="searchexistcase.executeplanname" filterable placeholder="测试集合" @change="existtestplanselectChanged($event)">
              <el-option label="请选择" value />
              <div v-for="(testplan, index) in execplanList" :key="index">
                <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
              </div>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="SearchExistCases" :loading="casebtnLoading">查询</el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table
        ref="caseTable"
        :data="testcaseexistList"
        :key="itemexistcaseKey"
        v-loading.body="existcaselistLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >

        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="existcasegetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column label="apiid" v-if="show" align="center" prop="apiid" width="120"/>
        <el-table-column label="deployunitid" v-if="show" align="center" prop="deployunitid" width="120"/>
        <el-table-column label="用例名" align="center" prop="casename" width="100"/>
        <el-table-column label="微服务" align="center" prop="deployunitname" width="100"/>
        <el-table-column label="API" align="center" prop="apiname" width="100"/>

        <el-table-column min-width="200px" align="center" label="用例顺序">
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

      </el-table>
      <el-pagination
        @size-change="existcasehandleSizeChange"
        @current-change="existcasehandleCurrentChange"
        :current-page="searchexistcase.page"
        :page-size="searchexistcase.size"
        :total="existcasetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

    </el-dialog>

  </div>
</template>
<script>
  import { searchleftcase } from '@/api/assets/apicases'
  import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
  import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
  import { search as searchtestplancases, updatePlanCaseorder, getplancasesbyplanidandorder, addexecuteplantestcase, removebatchexecuteplantestcase, removeexecuteplantestcase } from '@/api/executecenter/executeplantestcase'
  import { getallexplan } from '@/api/executecenter/executeplan'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'
  import { searchdeployunitmodel } from '@/api/deployunit/depunitmodel'

  export default {
    name: '集合用例',
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
        itemplanKey: null,
        itemcaseKey: null,
        itemexistcaseKey: null,
        tmpplancasedeployunitname: null,
        tmpplancaseexecuteplanname: null,
        tmpplancaseapiname: null,
        tmpcasecasetype: null,
        tmpcasedeployunitname: null,
        tmpcaseapiname: null,
        tmpexecuteplanid: null,
        tmpexistcaseexecuteplanid: null,
        tmploadexecuteplanid: null,
        tmpdeployunitid: null,
        tmploaddeployunitid: null,
        tmploadapiid: null,
        tmpapiid: null,
        execplanList: [], // 计划列表
        apiList: [], // api列表
        modelList: [],
        loadapiList: [], // api列表
        deployunitList: [], // 微服务列表
        loaddeployunitList: [], // 微服务列表
        multipleSelection: [], // 首页装载表格被选中的内容
        casemultipleSelection: [], // 查询用例表格被选中的内容
        executeplancaseList: [], // 首页测试集合用例列表
        executeplancaseremovetList: [], // 查询执行计划需要删除存在的用例列表
        testcaseList: [], // 装载用例列表
        testcaselastList: [], // 显示希望装载的用例列表
        testcaseexistList: [], // 显示已经装载的用例列表
        listLoading: false, // 数据加载等待动画
        caselistLoading: false, // 用例列表页面数据加载等待动画
        existcaselistLoading: false, // 用例列表页面数据加载等待动画
        show: false,
        total: 0, // 数据总数
        casetotal: 0, // 用例数据总数
        existcasetotal: 0, // 已经装载用例数据总数
        apiQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          deployunitid: 0,
          modelid: 0,
          deployunitname: '' // 获取字典表入参
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        casedialogFormVisible: false,
        testcasedialogFormVisible: false,
        loadcase: '装载用例',
        btnLoading: false, // 按钮等待动画
        casebtnLoading: false, // 按钮等待动画
        search: {
          page: 1,
          size: 10,
          executeplanid: null,
          deployunitid: null,
          apiid: null,
          executeplanname: null,
          deployunitname: null,
          apiname: null,
          projectid: ''
        },
        searchcase: {
          page: 1,
          size: 10,
          executeplanid: null,
          executeplanname: null,
          deployunitid: null,
          deployunitname: null,
          modelname: null,
          apiid: null,
          apiname: null,
          casetype: null
        },
        searchexistcase: {
          page: 1,
          size: 10,
          executeplanname: null,
          executeplanid: null
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.getexecplanList()
      this.getloadexecplanList()
      this.getexecuteplancaseList()
      this.getdepunitLists()
      this.getloaddepunitLists()
      // this.getenviromentallList()
      // this.getdatabydiccodeList()
    },

    activated() {
      this.getdepunitLists()
      this.getexecplanList()
    },

    methods: {
      unix2CurrentTime,

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
        updatePlanCaseorder(row).then(response => {
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

      handleSelectionChange(rows) {
        // console.log(rows)
        this.multipleSelection = rows
      },

      casehandleClickTableRow(row, event, column) {
      },
      handleClickTableRow(row, event, column) {
      },
      casehandleSelectionChange(rows) {
        this.casemultipleSelection = rows
        // console.log(this.casemultipleSelection)
      },
      searchdeployunitmodel() {
        searchdeployunitmodel(this.apiQuery).then(response => {
          this.modelList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
      },
      /**
       * 获取测试集合列表
       */
      getexecplanList() {
        getallexplan(this.search).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载计划列表失败')
        })
      },

      /**
       * 获取测试集合列表
       */
      getloadexecplanList() {
        getallexplan(this.search).then(response => {
          this.loadexecplanList = response.data
        }).catch(res => {
          this.$message.error('加载计划列表失败')
        })
      },

      /**
       * 获取测试集合用例列表
       */
      getexecuteplancaseList() {
        this.search.executeplanid = this.tmploadexecuteplanid
        this.search.deployunitid = this.tmploaddeployunitid
        this.search.apiid = this.tmploadapiid
        this.listLoading = true
        searchtestplancases(this.search).then(response => {
          this.executeplancaseList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试集合用例列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        // this.search.executeplanid = this.tmploadexecuteplanid
        // this.search.deployunitid = this.tmploaddeployunitid
        // this.search.apiid = this.tmploadapiid
        this.listLoading = true
        console.log(this.search)
        searchtestplancases(this.search).then(response => {
          this.itemKey = Math.random()
          this.executeplancaseList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmploadexecuteplanid = this.search.executeplanid
        this.tmploaddeployunitid = this.search.deployunitid
        this.tmploadapiid = this.search.apiid
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
        this.getexecuteplancaseList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getexecuteplancaseList()
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
       * 计划下拉选择事件获取微服务id  e的值为options的选值
       */
      testplanselectChanged(e) {
        this.tmpexecuteplanid = null
        this.tmpcasecasetype = null
        console.log(this.execplanList)
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.tmpexecuteplanid = this.execplanList[i].id
            this.tmpcasecasetype = this.execplanList[i].usetype
            console.log('1111111111111111111111')
            console.log(this.tmpcasecasetype)
          }
        }
      },

      existtestplanselectChanged(e) {
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.searchexistcase.executeplanid = this.execplanList[i].id
          }
        }
      },

      /**
       * 首页计划下拉选择事件获取微服务id  e的值为options的选值
       */
      loadtestplanselectChanged(e) {
        this.tmploadapiid = null
        this.tmploaddeployunitid = null
        this.search.apiname = null
        this.search.apiid = null
        this.search.deployunitname = null
        this.search.deployunitid = null
        this.tmploadexecuteplanid = null
        for (let i = 0; i < this.loadexecplanList.length; i++) {
          if (this.loadexecplanList[i].executeplanname === e) {
            this.tmploadexecuteplanid = this.loadexecplanList[i].id
            this.search.executeplanid = this.loadexecplanList[i].id
            console.log(this.tmploadexecuteplanid)
          }
        }
      },

      /**
       * 首页API下拉选择事件获取微服务id  e的值为options的选值
       */
      loadApiselectChanged(e) {
        this.tmploadapiid = null
        for (let i = 0; i < this.loadapiList.length; i++) {
          if (this.loadapiList[i].apiname === e) {
            this.tmploadapiid = this.loadapiList[i].id
            this.search.apiid = this.loadapiList[i].id
          }
        }
      },

      /**
       * API下拉选择事件获取微服务id  e的值为options的选值
       */
      ApiselectChanged(e) {
        this.tmpapiid = null
        for (let i = 0; i < this.apiList.length; i++) {
          if (this.apiList[i].apiname === e) {
            this.tmpapiid = this.apiList[i].id
          }
        }
      },

      /**
       * 首页微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      loaddeployunitselectChanged(e) {
        this.tmploadapiid = null
        this.tmploaddeployunitid = null
        this.search.apiid = null
        this.search.apiname = null
        for (let i = 0; i < this.loaddeployunitList.length; i++) {
          if (this.loaddeployunitList[i].deployunitname === e) {
            this.tmploaddeployunitid = this.loaddeployunitList[i].id
            this.search.deployunitid = this.loaddeployunitList[i].id
            this.apiQuery.deployunitid = this.loaddeployunitList[i].id
          }
        }
        this.loadapiList = null
        this.apiQuery.deployunitname = e
        getapiListbydeploy(this.apiQuery).then(response => {
          this.loadapiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      /**
       * 装载层微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      deployunitselectChanged(e) {
        this.tmpapiid = null
        this.tmpdeployunitid = null
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpdeployunitid = this.deployunitList[i].id
            this.apiQuery.deployunitid = this.deployunitList[i].id
          }
        }
        this.searchdeployunitmodel()
        this.apiList = null
        this.searchcase.apiname = ''
        this.apiQuery.deployunitname = e
        getapiListbydeploy(this.apiQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      modelselectChanged(e) {
        for (let i = 0; i < this.modelList.length; i++) {
          if (this.modelList[i].modelname === e) {
            this.apiQuery.modelid = this.modelList[i].id
          }
        }
        this.apiList = null
        this.searchcase.apiname = ''
        this.apiQuery.deployunitname = e
        getapiListbydeploy(this.apiQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
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
       * 获取微服务列表
       */
      getloaddepunitLists() {
        getdepunitLists(this.search).then(response => {
          this.loaddeployunitList = response.data
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      /**
       * 获取选择用例列表
       */
      getapicasesList() {
        this.caselistLoading = true
        this.searchcase.executeplanid = this.tmpexecuteplanid
        this.searchcase.deployunitid = this.tmpdeployunitid
        this.searchcase.apiid = this.tmpapiid
        this.searchcase.casetype = this.tmpcasecasetype
        searchleftcase(this.searchcase).then(response => {
          this.testcaselastList = response.data.list
          this.casetotal = response.data.total
          this.caselistLoading = false
        }).catch(res => {
          this.$message.error('加载用例列表失败')
        })
      },

      /**
       * 获取微服务和api的用例
       */
      searchcaseBy() {
        this.searchcase.page = 1
        this.searchcase.executeplanid = this.tmpexecuteplanid
        this.searchcase.deployunitid = this.tmpdeployunitid
        this.searchcase.apiid = this.tmpapiid
        this.searchcase.casetype = this.tmpcasecasetype
        console.log(this.searchcase)
        this.caselistLoading = true
        this.$refs.searchcase.validate(valid => {
          if (valid) {
            searchleftcase(this.searchcase).then(response => {
              this.testcaselastList = response.data.list
              this.casetotal = response.data.total
            }).catch(res => {
              this.$message.error('获取已装载的用例失败')
            })
          }
        })
        this.tmpexecuteplanid = this.searchcase.executeplanid
        this.tmpdeployunitid = this.searchcase.deployunitid
        this.tmpapiid = this.searchcase.apiid
        this.tmpcasecasetype = this.searchcase.casetype
        this.caselistLoading = false
      },

      getplancasesbyplanidandorder() {
        this.searchexistcase.executeplanid = this.tmpexistcaseexecuteplanid
        this.$refs.searchexistcase.validate(valid => {
          if (valid) {
            getplancasesbyplanidandorder(this.searchexistcase).then(response => {
              this.testcaseexistList = response.data.list
              const items = response.data.list
              this.testcaseexistList = items.map(v => {
                this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
                v.oldcaseorder = v.caseorder //  will be used when user click the cancel botton
                return v
              })
              this.existcasetotal = response.data.total
            }).catch(res => {
              this.$message.error('获取已装载的用例失败')
            })
          }
        })
        this.existcaselistLoading = false
      },

      SearchExistCases() {
        this.searchexistcase.page = 1
        this.$refs.searchexistcase.validate(valid => {
          if (valid) {
            getplancasesbyplanidandorder(this.searchexistcase).then(response => {
              this.testcaseexistList = response.data.list
              const items = response.data.list
              this.testcaseexistList = items.map(v => {
                this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
                v.oldcaseorder = v.caseorder //  will be used when user click the cancel botton
                return v
              })
              this.existcasetotal = response.data.total
            }).catch(res => {
              this.$message.error('获取已装载的用例失败')
            })
          }
        })
        this.tmpexistcaseexecuteplanid = this.searchexistcase.executeplanid
        this.existcaselistLoading = false
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      casehandleSizeChange(size) {
        this.searchcase.page = 1
        this.searchcase.size = size
        this.getapicasesList()
      },

      existcasehandleSizeChange(size) {
        this.searchexistcase.page = 1
        this.searchexistcase.size = size
        this.getplancasesbyplanidandorder()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      casehandleCurrentChange(page) {
        this.searchcase.page = page
        this.getapicasesList()
      },

      existcasehandleCurrentChange(page) {
        this.searchexistcase.page = page
        this.getplancasesbyplanidandorder()
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

      existcasegetIndex(index) {
        return (this.searchexistcase.page - 1) * this.searchexistcase.size + index + 1
      },

      /**
       * 显示修改测试集合对话框
       * @param index 测试集合下标
       */
      showUpdateexecuteplanDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpexecuteplan.id = this.executeplancaseList[index].id
        this.tmpexecuteplan.executeplanname = this.executeplancaseList[index].executeplanname
        this.tmpexecuteplan.status = this.executeplancaseList[index].status
        this.tmpexecuteplan.usetype = this.executeplancaseList[index].usetype
        this.tmpexecuteplan.memo = this.executeplancaseList[index].memo
        this.tmpexecuteplan.enviromentname = this.executeplancaseList[index].enviromentname
        this.tmpexecuteplan.businesstype = this.executeplancaseList[index].businesstype
        this.tmpexecuteplan.creator = this.name
        this.tmpexecuteplan.runmode = this.executeplancaseList[index].runmode
        console.log(this.tmpexecuteplan.runmode)
        if (this.tmpexecuteplan.usetype === '性能') {
          this.PerformanceVisible = true
        } else {
          this.PerformanceVisible = false
          this.tmpexecuteplan.runmode = '多机执行'
        }
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === this.tmpexecuteplan.enviromentname) {
            this.tmpexecuteplan.envid = this.enviromentnameList[i].id
          }
        }
      },

      /**
       * 装载测试集合的用例
       */
      addexecuteplantestcase() {
        this.testcaseList = []
        if (this.casemultipleSelection.length === 0) {
          this.$message.error('请选择装载的用例')
        } else {
          for (let i = 0; i < this.casemultipleSelection.length; i++) {
            this.testcaseList.push({
              'executeplanid': this.tmpexecuteplanid,
              'deployunitid': this.casemultipleSelection[i].deployunitid,
              'apiid': this.casemultipleSelection[i].apiid,
              'deployunitname': this.casemultipleSelection[i].deployunitname,
              'apiname': this.casemultipleSelection[i].apiname,
              'testcaseid': this.casemultipleSelection[i].id,
              'caseorder': this.casemultipleSelection[i].id,
              'casename': this.casemultipleSelection[i].casename,
              'creator': this.name,
              'projectid': window.localStorage.getItem('pid')
            })
          }
          addexecuteplantestcase(this.testcaseList).then(() => {
            this.casedialogFormVisible = false
            this.getexecuteplancaseList()
            this.$message.success('装载成功')
          }).catch(res => {
            this.$message.error('装载失败')
          })
        }
      },

      /**
       * 批量删除装载的用例
       */
      removebatchexecuteplantestcase() {
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
          removebatchexecuteplantestcase(this.executeplancaseremovetList).then(() => {
            this.$message.success('取消装载用例成功')
          }).catch(res => {
            this.$message.error('取消装载用例失败')
          })
        }
      },
      /**
       * 显示用例对话框
       * @param index 测试集合下标
       */
      showTestCaseDialog() {
        this.casedialogFormVisible = true
        this.dialogStatus = 'add'
        this.searchcase.executeplanname = null
        this.searchcase.deployunitname = null
        this.searchcase.apiname = null
        this.searchcase.modelname = null
        this.testcaselastList = []
        this.casetotal = 0
      },

      showTestCaseorderDialog() {
        this.testcasedialogFormVisible = true
        this.searchexistcase.executeplanid = null
        this.searchexistcase.executeplanname = null
        this.testcaseexistList = null
      },

      /**
       * 删除用例
       * @param index 测试集合下标
       */
      removeexecuteplantestcase(index) {
        this.$confirm('删除该测试集合用例？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.executeplancaseList[index].id
          removeexecuteplantestcase(id).then(() => {
            this.$message.success('删除成功')
            this.getexecuteplancaseList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },
      /**
       * 批量删除用例
       */
      DeleteBatchPlanTestCase() {
        this.$confirm('取消所选测试集合装载的用例？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          if (this.multipleSelection.length === 0) {
            this.$message.error('请选择需要取消的用例')
          } else {
            this.executeplancaseremovetList = []
            console.log(this.multipleSelection)
            for (let i = 0; i < this.multipleSelection.length; i++) {
              this.executeplancaseremovetList.push({
                'executeplanid': this.multipleSelection[i].executeplanid,
                'deployunitname': this.multipleSelection[i].deployunitname,
                'apiname': this.multipleSelection[i].apiname,
                'testcaseid': this.multipleSelection[i].testcaseid,
                'casename': this.multipleSelection[i].casename
              })
            }
            removebatchexecuteplantestcase(this.executeplancaseremovetList).then(() => {
              this.$message.success('取消装载用例成功')
              this.getexecuteplancaseList()
            }).catch(res => {
              this.$message.error('取消装载用例失败')
            })
          }
        }).catch(() => {
          this.$message.info('取消装载用例异常')
        })
      }
    }
  }
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
