<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('dispatch:list')"
            @click.native.prevent="getdispatchList"
          >刷新</el-button>
        </el-form-item>

        <span v-if="hasPermission('dispatch:search')">
          <el-form-item label="测试集合" prop="execplanname" >
          <el-select v-model="search.execplanname" filterable clearable placeholder="测试集合" @change="testplanselectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
            </div>
          </el-select>
        </el-form-item>
          <el-form-item label="批次" prop="batchname" >
            <el-select v-model="search.batchname" filterable clearable placeholder="批次">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(planbatch, index) in planbatchList" :key="index">
              <el-option :label="planbatch.batchname" :value="planbatch.batchname" />
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
      :data="dispatchList"
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
      <el-table-column label="执行机" align="center" prop="slavername" width="150"/>
      <el-table-column label="测试集合" align="center" prop="execplanname" width="150"/>
      <el-table-column :show-overflow-tooltip="true" label="执行计划" align="center" prop="batchname" width="150"/>
      <el-table-column label="测试场景" align="center" prop="scenename" width="180"/>
      <el-table-column label="执行用例" align="center" prop="testcasename" width="150"/>
      <el-table-column label="用例顺序" align="center" prop="caseorder" width="80"/>
      <el-table-column label="状态" align="center" prop="status" width="100"/>
      <el-table-column :show-overflow-tooltip="true" label="异常详情" align="center" prop="memo" width="150">
      <template slot-scope="scope">
        <span v-if="scope.row.memo !== ''" style="color:red">{{ scope.row.memo }}</span>
      </template>
      </el-table-column>>


      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="100px"
        style="width: 300px; margin-left:50px;"
        :model="tmpdispatch"
        ref="tmpdispatch"
      >
        <el-form-item label="执行机名" prop="dispatchname" required>
          <el-input
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdispatch.dispatchname"
          />
        </el-form-item>
        <el-form-item label="ip" prop="ip">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdispatch.ip"
          />
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdispatch.port"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdispatch.status"
          />
        </el-form-item>
        <el-form-item label="类型" prop="stype">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdispatch.stype"
          />
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdispatch.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdispatch'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddispatch"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedispatch"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, adddispatch, updatedispatch, removedispatch } from '@/api/dispatch/dispatch'
  import { unix2CurrentTime } from '@/utils'
  import { getallexplan as getallexplan } from '@/api/executecenter/executeplan'
  import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
  import { mapGetters } from 'vuex'

  export default {
    name: '调度管理',
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
        tmpexecplanname: null,
        tmpbatchname: null,
        dispatchList: [], // 调度列表
        execplanList: [], // 计划列表
        planbatchList: [], // 批次列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改执行机',
          update: '修改执行机',
          add: '添加执行机'
        },
        btnLoading: false, // 按钮等待动画
        tmpdispatch: {
          id: '',
          dispatchname: '',
          ip: '',
          port: '',
          status: '',
          stype: '',
          memo: ''
        },
        tmpbatchquery: {
          executeplanid: ''
        },
        search: {
          page: 1,
          size: 10,
          execplanname: null,
          accountId: null,
          batchname: null,
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
      this.getdispatchList()
    },

    activated() {
      this.getallexplan()
      this.getdispatchList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      testplanselectChanged(e) {
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.tmpbatchquery.executeplanid = this.execplanList[i].id
          }
        }
        getbatchbyplan(this.tmpbatchquery).then(response => {
          this.planbatchList = response.data
          this.search.batchname = null
        }).catch(res => {
          this.$message.error('加载批次列表失败')
        })
      },

      /**
       * 获取调度列表
       */
      getdispatchList() {
        this.search.execplanname = this.tmpexecplanname
        this.search.batchname = this.tmpbatchname
        this.listLoading = true
        search(this.search).then(response => {
          this.dispatchList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载字典列表失败')
        })
      },

      /**
       * 测试集合
       */
      getallexplan() {
        getallexplan(this.search).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载测试集合列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.dispatchList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpexecplanname = this.search.execplanname
        this.tmpbatchname = this.search.batchname
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
        this.getdispatchList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getdispatchList()
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
       * 显示添加执行机对话框
       */
      showAdddispatchDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpdispatch.id = ''
        this.tmpdispatch.dispatchname = ''
        this.tmpdispatch.ip = ''
        this.tmpdispatch.port = ''
        this.tmpdispatch.status = ''
        this.tmpdispatch.stype = ''
        this.tmpdispatch.memo = ''
      },
      /**
       * 添加字典
       */
      adddispatch() {
        this.$refs.tmpdispatch.validate(valid => {
          if (valid && this.isUniqueDetail(this.tmpdispatch)) {
            this.btnLoading = true
            adddispatch(this.tmpdispatch).then(() => {
              this.$message.success('添加成功')
              this.getdispatchList()
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
       * 显示修改执行机对话框
       * @param index 执行机下标
       */
      showUpdatedispatchDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpdispatch.id = this.dispatchList[index].id
        this.tmpdispatch.dispatchname = this.dispatchList[index].dispatchname
        this.tmpdispatch.ip = this.dispatchList[index].ip
        this.tmpdispatch.port = this.dispatchList[index].port
        this.tmpdispatch.status = this.dispatchList[index].status
        this.tmpdispatch.stype = this.dispatchList[index].stype
        this.tmpdispatch.memo = this.dispatchList[index].memo
      },
      /**
       * 更新执行机
       */
      updatedispatch() {
        if (this.isUniqueDetail(this.tmpdispatch)) {
          updatedispatch(this.tmpdispatch).then(() => {
            this.$message.success('更新成功')
            this.getdispatchList()
            this.dialogFormVisible = false
          }).catch(res => {
            this.$message.error('更新失败')
          })
        }
      },

      /**
       * 删除字典
       * @param index 执行机下标
       */
      removedispatch(index) {
        this.$confirm('删除该执行机？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dispatchList[index].id
          removedispatch(id).then(() => {
            this.$message.success('删除成功')
            this.getdispatchList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 执行机资料是否唯一
       * @param 执行机
       */
      isUniqueDetail(dispatch) {
        for (let i = 0; i < this.dispatchList.length; i++) {
          if (this.dispatchList[i].id !== dispatch.id) { // 排除自己
            if (this.dispatchList[i].dispatchname === dispatch.dispatchname) {
              this.$message.error('执行机名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
