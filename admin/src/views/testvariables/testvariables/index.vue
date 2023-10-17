<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('testvariables:list')"
            @click.native.prevent="gettestvariablesList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('testvariables:add')"
            @click.native.prevent="showAddtestvariablesDialog"
          >添加接口变量</el-button>
        </el-form-item>

        <span v-if="hasPermission('testvariables:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.testvariablesname" @keyup.enter.native="searchBy" placeholder="变量名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="testvariablesList"
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
      <el-table-column label="变量名" align="center" prop="testvariablesname" width="80"/>
      <el-table-column :show-overflow-tooltip="true" label="变量描述" align="center" prop="variablesdes" width="150"/>
      <el-table-column label="变量来源" align="center" prop="testvariablestype" width="80"/>
      <el-table-column label="变量值类型" align="center" prop="valuetype" width="90"/>
      <el-table-column label="变量值提取表达" align="center" prop="variablesexpress" width="120"/>
      <el-table-column :show-overflow-tooltip="true" label="备注" align="center" prop="memo" width="100"/>
      <el-table-column label="操作人" align="center" prop="creator" width="80"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="240"
                       v-if="hasPermission('testvariables:update')  || hasPermission('testvariables:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('testvariables:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatetestvariablesDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('testvariables:delete') && scope.row.id !== id"
            @click.native.prevent="removetestvariables(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('testvariables:update') && scope.row.id !== id"
            @click.native.prevent="showApicasesVariablesDialog(scope.$index)"
          >绑定接口</el-button>
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
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmptestvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addtestvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatetestvariables"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="BindtextMap[BinddialogStatus]" :visible.sync="BindFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 450px; margin-left:50px;"
        :model="tmpApicasesVariables"
        ref="tmpApicasesVariables"
      >

        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpApicasesVariables.deployunitname" filterable placeholder="微服务" style="width:100%" @change="deployunitselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API" prop="apiname" required >
          <el-select v-model="tmpApicasesVariables.apiname" filterable placeholder="API" style="width:100%" @change="apiselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(api, index) in apiList" :key="index">
              <el-option :label="api.apiname" :value="api.apiname"/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="绑定接口" prop="casename" required >
          <el-select v-model="tmpApicasesVariables.casename" filterable placeholder="绑定接口" style="width:100%" @change="testcaseselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testcase, index) in caseList" :key="index">
              <el-option :label="testcase.casename" :value="testcase.casename" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpApicasesVariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="BindFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="BinddialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addApicasesVariables"
        >保存</el-button>
        <el-button
          type="success"
          v-if="BinddialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateApicasesVariables"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title='绑定接口' :visible.sync="BindVariablesDialogVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('ApicasesVariables:add')"
              @click.native.prevent="showAddApicasesVariablesDialog"
            >绑定接口</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="ApicasesVariablesList"
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
        <el-table-column label="绑定接口名" align="center" prop="casename" width="180"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('ApicasesVariables:update')  || hasPermission('ApicasesVariables:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('ApicasesVariables:update') && scope.row.id !== id"
              @click.native.prevent="showUpdateApicasesVariablesDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('ApicasesVariables:delete') && scope.row.id !== id"
              @click.native.prevent="removeApicasesVariables(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addtestvariables, updatetestvariables, removetestvariables } from '@/api/testvariables/testvariables'
  import { addApicasesVariables, getbyvariablesid, updateApicasesVariables, removeApicasesVariables } from '@/api/assets/apicasesvariables'
  import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
  import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
  import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '接口变量',
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
        expressname: '变量值提取表达',
        id: null,
        itemKey: null,
        tmptestvariablesname: '',
        apiList: [], // api列表
        caseList: [], // 用例列表
        deployunitList: [], // 微服务列表
        testvariablesList: [], // 变量列表
        ApicasesVariablesList: [], // 用例变量列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        BinddialogStatus: 'add',
        dialogFormVisible: false,
        BindVariablesDialogVisible: false,
        BindFormVisible: false,
        textMap: {
          updateRole: '修改接口变量',
          update: '修改接口变量',
          add: '添加接口变量'
        },
        BindtextMap: {
          updateRole: '修改绑定接口',
          update: '修改绑定接口',
          add: '添加绑定接口'
        },
        apiquery: {
          casedeployunitname: '',
          caseapiname: '',
          apiid: '',
          deployunitid: ''
        },
        deployunitQuery: {
          deployunitid: '', // 获取字典表入参
          modelid: 0,
          deployunitname: '' // 获取字典表入参
        },
        VariablescaseQuery: {
          variablesid: '' // 获取字典表入参
        },

        btnLoading: false, // 按钮等待动画
        tmptestvariables: {
          id: '',
          testvariablesname: '',
          variablesdes: '',
          valuetype: '',
          testvariablestype: '',
          variablesexpress: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        tmpApicasesVariables: {
          id: '',
          apiid: '',
          caseid: '',
          deployunitid: '',
          deployunitname: '',
          apiname: '',
          casename: '',
          variablesid: '',
          variablesname: '',
          memo: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          testvariablesname: null,
          projectid: ''
        }
      }
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.gettestvariablesList()
      this.getdepunitLists()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    methods: {
      unix2CurrentTime,

      testvariablestypeselectChanged(e) {
        if (e === 'Body') {
          this.expressname = '变量值提取表达'
        } else {
          this.expressname = 'Key名'
        }
      },
      /**
       * 获取变量列表
       */
      gettestvariablesList() {
        this.listLoading = true
        this.search.testvariablesname = this.tmptestvariablesname
        search(this.search).then(response => {
          this.testvariablesList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },

      /**
       * 获取变量列表
       */
      getbyvariablesid() {
        getbyvariablesid(this.VariablescaseQuery).then(response => {
          this.ApicasesVariablesList = response.data
        }).catch(res => {
          this.$message.error('加载变量列表失败')
        })
      },
      /**
       * 获取微服务列表
       */
      getdepunitLists() {
        this.listLoading = true
        getdepunitLists(this.search).then(response => {
          this.deployunitList = response.data
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      deployunitselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpApicasesVariables.deployunitid = this.deployunitList[i].id
            this.deployunitQuery.deployunitid = this.deployunitList[i].id
            this.apiquery.deployunitid = this.deployunitList[i].id
          }
        }
        this.tmpApicasesVariables.apiname = ''
        this.tmpApicasesVariables.casename = ''
        this.deployunitQuery.deployunitname = e
        getapiListbydeploy(this.deployunitQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      /**
       * api下拉选择事件获取微服务id  e的值为options的选值
       */
      apiselectChanged(e) {
        for (let i = 0; i < this.apiList.length; i++) {
          if (this.apiList[i].apiname === e) {
            this.tmpApicasesVariables.apiid = this.apiList[i].id
            this.apiquery.apiid = this.apiList[i].id
          }
        }
        this.tmpApicasesVariables.casename = ''
        this.apiquery.caseapiname = e
        this.apiquery.casedeployunitname = this.tmpApicasesVariables.deployunitname
        findcasesbyname(this.apiquery).then(response => {
          this.caseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },

      testcaseselectChanged(e) {
        for (let i = 0; i < this.caseList.length; i++) {
          if (this.caseList[i].casename === e) {
            this.tmpApicasesVariables.caseid = this.caseList[i].id
          }
        }
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.testvariablesList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmptestvariablesname = this.search.testvariablesname
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
        this.gettestvariablesList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.gettestvariablesList()
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
       * 显示绑定变量列表对话框
       */
      showApicasesVariablesDialog(index) {
        // 显示新增对话框
        this.BindVariablesDialogVisible = true
        this.VariablescaseQuery.variablesid = this.testvariablesList[index].id
        this.tmpApicasesVariables.variablesid = this.testvariablesList[index].id
        this.tmpApicasesVariables.variablesname = this.testvariablesList[index].testvariablesname
        this.getbyvariablesid()
      },
      /**
       * 显示添加绑定用例变量对话框
       */
      showAddApicasesVariablesDialog(index) {
        // 显示新增对话框
        this.BindFormVisible = true
        this.BinddialogStatus = 'add'
        this.tmpApicasesVariables.id = ''
        this.tmpApicasesVariables.caseid = ''
        this.tmpApicasesVariables.deployunitname = ''
        this.tmpApicasesVariables.apiname = ''
        this.tmpApicasesVariables.casename = ''
        this.tmpApicasesVariables.memo = ''
        this.tmpApicasesVariables.creator = this.name
      },
      /**
       * 显示添加变量对话框
       */
      showAddtestvariablesDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
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
            this.btnLoading = true
            addtestvariables(this.tmptestvariables).then(() => {
              this.$message.success('添加成功')
              this.gettestvariablesList()
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
       * 添加用例变量
       */
      addApicasesVariables() {
        this.$refs.tmpApicasesVariables.validate(valid => {
          if (valid) {
            addApicasesVariables(this.tmpApicasesVariables).then(() => {
              this.$message.success('添加成功')
              this.BindFormVisible = false
              this.getbyvariablesid()
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      /**
       * 显示修改用例变量对话框
       * @param index 用例变量下标
       */
      showUpdateApicasesVariablesDialog(index) {
        this.BindFormVisible = true
        this.BinddialogStatus = 'update'
        this.tmpApicasesVariables.id = this.ApicasesVariablesList[index].id
        this.tmpApicasesVariables.caseid = this.ApicasesVariablesList[index].caseid
        this.tmpApicasesVariables.variablesid = this.VariablescaseQuery.variablesid
        this.tmpApicasesVariables.deployunitname = this.ApicasesVariablesList[index].deployunitname
        this.tmpApicasesVariables.deployunitid = this.ApicasesVariablesList[index].deployunitid
        this.tmpApicasesVariables.apiid = this.ApicasesVariablesList[index].apiid
        this.deployunitQuery.deployunitname = this.tmpApicasesVariables.deployunitname
        this.deployunitQuery.deployunitid = this.ApicasesVariablesList[index].deployunitid
        getapiListbydeploy(this.deployunitQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
        this.tmpApicasesVariables.apiname = this.ApicasesVariablesList[index].apiname
        this.apiquery.caseapiname = this.tmpApicasesVariables.apiname
        this.apiquery.casedeployunitname = this.tmpApicasesVariables.deployunitname
        this.apiquery.deployunitid = this.deployunitQuery.deployunitid
        this.apiquery.apiid = this.ApicasesVariablesList[index].apiid
        findcasesbyname(this.apiquery).then(response => {
          this.caseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
        this.tmpApicasesVariables.casename = this.ApicasesVariablesList[index].casename
        this.tmpApicasesVariables.variablesname = this.ApicasesVariablesList[index].variablesname
        this.tmpApicasesVariables.memo = this.ApicasesVariablesList[index].memo
        this.tmpApicasesVariables.creator = this.name
      },

      /**
       * 更新用例变量
       */
      updateApicasesVariables() {
        this.$refs.tmpApicasesVariables.validate(valid => {
          if (valid) {
            updateApicasesVariables(this.tmpApicasesVariables).then(() => {
              this.$message.success('更新成功')
              this.getbyvariablesid()
              this.BindFormVisible = false
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
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmptestvariables.id = this.testvariablesList[index].id
        this.tmptestvariables.testvariablesname = this.testvariablesList[index].testvariablesname
        this.tmptestvariables.variablesdes = this.testvariablesList[index].variablesdes
        this.tmptestvariables.testvariablestype = this.testvariablesList[index].testvariablestype
        this.tmptestvariables.variablesexpress = this.testvariablesList[index].variablesexpress
        this.tmptestvariables.tmptestvariables = this.testvariablesList[index].tmptestvariables
        this.tmptestvariables.valuetype = this.testvariablesList[index].valuetype
        this.tmptestvariables.memo = this.testvariablesList[index].memo
        this.tmptestvariables.creator = this.name
      },
      /**
       * 更新变量
       */
      updatetestvariables() {
        this.$refs.tmptestvariables.validate(valid => {
          if (valid) {
            updatetestvariables(this.tmptestvariables).then(() => {
              this.$message.success('更新成功')
              this.gettestvariablesList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除用例变量
       * @param index 用例变量下标
       */
      removeApicasesVariables(index) {
        this.$confirm('删除该接口绑定？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.ApicasesVariablesList[index].id
          removeApicasesVariables(id).then(() => {
            this.$message.success('删除成功')
            this.getbyvariablesid()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
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
          const id = this.testvariablesList[index].id
          removetestvariables(id).then(() => {
            this.$message.success('删除成功')
            this.gettestvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 变量是否唯一
       * @param 变量
       */
      isUniqueDetail(testvariables) {
        for (let i = 0; i < this.testvariablesList.length; i++) {
          if (this.testvariablesList[i].id !== testvariables.id) { // 排除自己
            if (this.testvariablesList[i].testvariablesname === testvariables.testvariablesname) {
              this.$message.error('变量名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
