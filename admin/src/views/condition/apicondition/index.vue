<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('apicondition:list')"
            @click.native.prevent="getapiconditionList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('apicondition:add')"
            @click.native.prevent="showAddapiconditionDialog"
          >添加接口条件</el-button>
        </el-form-item>

        <span v-if="hasPermission('apicondition:search')">
          <el-form-item label="父条件名：">
            <el-select v-model="search.conditionname" filterable placeholder="父条件名" clearable>
              <el-option label="请选择" value />
              <div v-for="(condition, index) in conditionList" :key="index">
                <el-option :label="condition.conditionname" :value="condition.conditionname"/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="apiconditionList"
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
      <el-table-column  label="父条件名" align="center" prop="conditionname" width="180"/>
      <el-table-column  label="子条件名" align="center" prop="subconditionname" width="170"/>
      <el-table-column label="微服务名" align="center" prop="deployunitname" width="120"/>
      <el-table-column label="API名" align="center" prop="apiname" width="100"/>
      <el-table-column label="接口名" align="center" prop="casename" width="100"/>
      <el-table-column label="操作人" align="center" prop="creator" width="70"/>
      <el-table-column :show-overflow-tooltip="true" label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="最后修改时间" align="center" prop="lastmodifyTime" width="120">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="150"
                       v-if="hasPermission('apicondition:update')  || hasPermission('apicondition:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('apicondition:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateapiconditionDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('apicondition:delete') && scope.row.id !== id"
            @click.native.prevent="removeapicondition(scope.$index)"
          >删除</el-button>
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
        :model="tmpapicondition"
        ref="tmpapicondition"
      >

        <el-form-item label="子条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapicondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="父条件名" prop="conditionname" required >
          <el-select v-model="tmpapicondition.conditionname" filterable  placeholder="父条件名" style="width:100%" @change="ConditionselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(condition, index) in conditionList" :key="index">
              <el-option :label="condition.conditionname" :value="condition.conditionname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpapicondition.deployunitname" filterable placeholder="微服务" style="width:100%" @change="deployunitselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="API" prop="apiname" required >
          <el-select v-model="tmpapicondition.apiname" filterable placeholder="API" style="width:100%" @change="apiselectChanged($event)">
            <el-option label="请选择" value />
            <div v-for="(api, index) in apiList" :key="index">
              <el-option :label="api.apiname" :value="api.apiname"/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="接口" prop="casename" required >
          <el-select v-model="tmpapicondition.casename" filterable placeholder="接口" style="width:100%" @change="testcaseselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testcase, index) in caseList" :key="index">
              <el-option :label="testcase.casename" :value="testcase.casename" required/>
            </div>
          </el-select>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapicondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapicondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapicondition"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addapicondition, updateapicondition, removeapicondition } from '@/api/condition/apicondition'
  import { getalltestcondition } from '@/api/condition/condition'
  import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
  import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
  import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
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
        itemKey: null,
        tmpconditionname: '',
        conditionList: [], // 条件列表
        apiconditionList: [], // 接口条件列表
        apiList: [], // api列表
        caseList: [], // 用例列表
        deployunitList: [], // 微服务列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        deployunitQuery: {
          deployunitid: '',
          modelid: 0,
          deployunitname: '' // 获取字典表入参
        },
        apiquery: {
          casedeployunitname: '',
          caseapiname: '',
          apiid: '',
          deployunitid: ''
        },
        apicaseQuery: {
          apiname: '' // 获取字典表入参
        },
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改接口条件',
          update: '修改接口条件',
          add: '添加接口条件'
        },
        btnLoading: false, // 按钮等待动画
        tmpapicondition: {
          id: '',
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
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          conditionname: null,
          projectid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.getapiconditionList()
      this.getdepunitLists()
      this.getalltestcondition()
    },

    activated() {
      this.getdepunitLists()
      this.getalltestcondition()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      deployunitselectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpapicondition.deployunitid = this.deployunitList[i].id
            this.deployunitQuery.deployunitid = this.deployunitList[i].id
            this.apiquery.deployunitid = this.deployunitList[i].id
          }
        }
        this.tmpapicondition.apiname = ''
        this.tmpapicondition.casename = ''
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
            this.tmpapicondition.apiid = this.apiList[i].id
          }
        }
        this.tmpapicondition.casename = ''
        this.apiquery.caseapiname = e
        this.apiquery.casedeployunitname = this.deployunitQuery.deployunitname
        this.apiquery.apiid = this.tmpapicondition.apiid
        findcasesbyname(this.apiquery).then(response => {
          this.caseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })
      },

      testcaseselectChanged(e) {
        for (let i = 0; i < this.caseList.length; i++) {
          if (this.caseList[i].casename === e) {
            this.tmpapicondition.caseid = this.caseList[i].id
          }
        }
      },

      /**
       * 条件下拉选择事件获取条件id  e的值为options的选值
       */
      ConditionselectChanged(e) {
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === e) {
            this.tmpapicondition.conditionid = this.conditionList[i].id
          }
          this.tmpapicondition.deployunitname = ''
          this.tmpapicondition.apiname = ''
          this.tmpapicondition.casename = ''
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChangedMN(e) {
        for (let i = 0; i < this.machinenameList.length; i++) {
          if (this.machinenameList[i].machinename === e) {
            this.tmpapicondition.machineid = this.machinenameList[i].id
          }
          console.log(this.machinenameList[i].id)
        }
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
       * 获取服务器环境列表
       */
      getapiconditionList() {
        this.listLoading = true
        this.search.conditionname = this.tmpconditionname
        search(this.search).then(response => {
          this.apiconditionList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试接口条件列表失败')
        })
      },

      /**
       * 获取条件列表
       */
      getalltestcondition() {
        this.listLoading = true
        getalltestcondition(this.search).then(response => {
          this.conditionList = response.data
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载条件列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.apiconditionList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpconditionname = this.search.conditionname
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
        this.getapiconditionList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getapiconditionList()
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
       * 显示添加接口条件对话框
       */
      showAddapiconditionDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpapicondition.id = ''
        this.tmpapicondition.subconditionname = ''
        this.tmpapicondition.conditionname = ''
        this.tmpapicondition.deployunitname = ''
        this.tmpapicondition.apiname = ''
        this.tmpapicondition.casename = ''
        this.tmpapicondition.creator = this.name
        this.tmpapicondition.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加接口条件
       */
      addapicondition() {
        this.$refs.tmpapicondition.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addapicondition(this.tmpapicondition).then(() => {
              this.$message.success('添加成功')
              this.getapiconditionList()
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
       * 显示修改接口条件对话框
       * @param index 接口条件下标
       */
      showUpdateapiconditionDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpapicondition.id = this.apiconditionList[index].id
        this.tmpapicondition.conditionid = this.apiconditionList[index].conditionid
        this.tmpapicondition.deployunitid = this.apiconditionList[index].deployunitid
        this.tmpapicondition.caseid = this.apiconditionList[index].caseid
        this.tmpapicondition.apiid = this.apiconditionList[index].apiid
        this.tmpapicondition.conditionname = this.apiconditionList[index].conditionname
        this.tmpapicondition.deployunitname = this.apiconditionList[index].deployunitname
        this.tmpapicondition.subconditionname = this.apiconditionList[index].subconditionname
        this.deployunitQuery.deployunitname = this.tmpapicondition.deployunitname
        this.deployunitQuery.deployunitid = this.apiconditionList[index].deployunitid
        getapiListbydeploy(this.deployunitQuery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })

        this.tmpapicondition.apiname = this.apiconditionList[index].apiname
        this.apiquery.caseapiname = this.tmpapicondition.apiname
        this.apiquery.casedeployunitname = this.tmpapicondition.deployunitname
        this.apiquery.deployunitid = this.apiconditionList[index].deployunitid
        this.apiquery.apiid = this.apiconditionList[index].apiid
        findcasesbyname(this.apiquery).then(response => {
          this.caseList = response.data
        }).catch(res => {
          this.$message.error('加载apicase列表失败')
        })

        this.tmpapicondition.casename = this.apiconditionList[index].casename
        this.tmpapicondition.creator = this.name
      },
      /**
       * 更新接口条件
       */
      updateapicondition() {
        this.$refs.tmpapicondition.validate(valid => {
          if (valid) {
            updateapicondition(this.tmpapicondition).then(() => {
              this.$message.success('更新成功')
              this.getapiconditionList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除接口条件
       * @param index 接口条件下标
       */
      removeapicondition(index) {
        this.$confirm('删除该接口条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.apiconditionList[index].id
          removeapicondition(id).then(() => {
            this.$message.success('删除成功')
            this.getapiconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 接口条件是否唯一
       * @param 接口条件
       */
      isUniqueDetail(apicondition) {
        for (let i = 0; i < this.apiconditionList.length; i++) {
          if (this.apiconditionList[i].id !== apicondition.id) { // 排除自己
            if (this.apiconditionList[i].enviromentname === apicondition.enviromentname) {
              if (this.apiconditionList[i].machinename === apicondition.machinename) {
                this.$message.error('接口条件名已存在')
                return false
              }
            }
          }
        }
        return true
      }
    }
  }
</script>
