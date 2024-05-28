<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('executeplanbatch:list')"
            @click.native.prevent="getexecuteplanbatchList"
          >刷新</el-button>
        </el-form-item>

        <span v-if="hasPermission('executeplanbatch:search')">
          <el-form-item label="集合：" prop="executeplanname" >
          <el-select v-model="search.executeplanname" filterable clearable placeholder="集合" @change="testplanselectChanged($event)">
            <el-option label="请选择" />
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
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
      ref="fileTable"
      :data="executeplanbatchList"
      :key="itemKey"
      @row-click="handleClickTableRow"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="编号" align="center" width="50">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="测试集合名" align="center" prop="executeplanname" width="150"/>
      <el-table-column label="执行计划" :show-overflow-tooltip="true" align="center" prop="batchname" width="150"/>
      <el-table-column label="测试场景" :show-overflow-tooltip="true" align="center" prop="scenename" width="150"/>
      <el-table-column label="状态" align="center" prop="status" width="70"/>
      <el-table-column label="来源" align="center" prop="source" width="60"/>
      <el-table-column label="执行类型" align="center" prop="exectype" width="80"/>
      <el-table-column label="执行时间" align="center" :show-overflow-tooltip="true" prop="execdate" width="140"/>
      <el-table-column label="维护人" align="center" prop="creator" width="70"/>
      <el-table-column :show-overflow-tooltip="true"  label="备注" align="center" prop="memo" width="70"/>
      <el-table-column label="创建时间" :show-overflow-tooltip="true" align="center" prop="createTime" width="130">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" :show-overflow-tooltip="true" align="center" prop="lastmodifyTime" width="130">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
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
  </div>
</template>
<script>
  import { search } from '@/api/executecenter/executeplanbatch'
  import { getallexplan as getallexplan } from '@/api/executecenter/executeplan'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '计划执行',
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
        execplanList: [], // 计划列表
        tmpexecuteplanbatchname: '',
        tmpexecuteplanname: '',
        executeplanbatchList: [], // 环境列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改执行计划批次',
          update: '修改执行计划批次',
          add: '添加执行计划批次'
        },
        btnLoading: false, // 按钮等待动画
        tmpexecuteplanbatch: {
          id: '',
          executeplanbatchname: '',
          envtype: '',
          memo: ''
        },
        search: {
          page: 1,
          size: 10,
          accountId: null,
          executeplanname: null,
          projectid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    created() {
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getallexplan()
      this.getexecuteplanbatchList()
    },

    activated() {
      this.getallexplan()
      this.getexecuteplanbatchList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取执行计划列表
       */
      getallexplan() {
        getallexplan(this.search).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载计划列表失败')
        })
      },

      /**
       * 获取字典列表
       */
      getexecuteplanbatchList() {
        this.search.executeplanname = this.tmpexecuteplanname
        this.listLoading = true
        search(this.search).then(response => {
          this.executeplanbatchList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载执行计划批次列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.executeplanbatchList = response.data.list
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
        this.getexecuteplanbatchList()
        // this.getexecuteplanbatchList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getexecuteplanbatchList()
        // this.getexecuteplanbatchList()
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
       * 显示添加执行计划批次对话框
       */
      showAddexecuteplanbatchDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpexecuteplanbatch.id = ''
        this.tmpexecuteplanbatch.executeplanbatchname = ''
        this.tmpexecuteplanbatch.memo = ''
        this.tmpexecuteplanbatch.envtype = ''
      },

      /**
       * 显示修改执行计划批次对话框
       * @param index测试集合批次下标
       */
      showUpdateexecuteplanbatchDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpexecuteplanbatch.id = this.executeplanbatchList[index].id
        this.tmpexecuteplanbatch.executeplanbatchname = this.executeplanbatchList[index].executeplanbatchname
        this.tmpexecuteplanbatch.envtype = this.executeplanbatchList[index].envtype
        this.tmpexecuteplanbatch.memo = this.executeplanbatchList[index].memo
      },

      handleClickTableRow(row, event, column) {
      },

      /**
       *测试集合批次是否唯一
       * @param测试集合批次
       */
      isUniqueDetail(executeplanbatch) {
        for (let i = 0; i < this.executeplanbatchList.length; i++) {
          if (this.executeplanbatchList[i].id !== executeplanbatch.id) { // 排除自己
            if (this.executeplanbatchList[i].executeplanbatchname === executeplanbatch.executeplanbatchname) {
              this.$message.error('执行计划执行计划已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
