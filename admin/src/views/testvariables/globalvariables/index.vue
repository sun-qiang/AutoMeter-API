<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('globalvariables:list')"
            @click.native.prevent="getglobalvariablesList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('globalvariables:add')"
            @click.native.prevent="showAddglobalvariablesDialog"
          >添加全局变量</el-button>
        </el-form-item>

        <span v-if="hasPermission('globalvariables:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.keyname" @keyup.enter.native="searchBy" placeholder="全局变量名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="globalvariablesList"
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
      <el-table-column label="全局变量名" align="center" prop="keyname" width="150"/>
      <el-table-column :show-overflow-tooltip="true" label="变量值" align="center" prop="keyvalue" width="80"/>
      <el-table-column label="描述" align="center" prop="memo" width="250"/>
      <el-table-column label="维护人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('globalvariables:update')  || hasPermission('globalvariables:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('globalvariables:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateglobalvariablesDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('globalvariables:delete') && scope.row.id !== id"
            @click.native.prevent="removeglobalvariables(scope.$index)"
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
        style="width: 550px; margin-left:50px;"
        :model="tmpglobalvariables"
        ref="tmpglobalvariables"
      >
        <el-form-item label="全局变量名" prop="keyname" required>
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpglobalvariables.keyname"
          />
        </el-form-item>

        <el-form-item label="变量值" prop="keyvalue" required>
          <el-input
            type="textarea"
            rows="15" cols="50"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpglobalvariables.keyvalue"
          />
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpglobalvariables.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpglobalvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addglobalvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateglobalvariables"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addglobalvariables, updateglobalvariables, removeglobalvariables } from '@/api/testvariables/globalvariables'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '全局变量',
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
        tmpglobalvariablesname: '',
        globalvariablesList: [], // 全局变量列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改全局变量',
          update: '修改全局变量',
          add: '添加全局变量'
        },
        btnLoading: false, // 按钮等待动画
        tmpglobalvariables: {
          id: '',
          keyname: '',
          keyvalue: '',
          memo: '',
          projectid: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          keyname: null,
          accountId: null,
          projectid: ''
        }
      }
    },

    created() {
      this.tmpglobalvariables.creator = this.name
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getglobalvariablesList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取全局变量列表
       */
      getglobalvariablesList() {
        this.listLoading = true
        this.search.globalvariablesname = this.tmpglobalvariablesname
        search(this.search).then(response => {
          this.globalvariablesList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载全局变量列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.globalvariablesList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpglobalvariablesname = this.search.globalvariablesname
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
        this.getglobalvariablesList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getglobalvariablesList()
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
       * 显示添加全局变量对话框
       */
      showAddglobalvariablesDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpglobalvariables.id = ''
        this.tmpglobalvariables.keyname = ''
        this.tmpglobalvariables.keyvalue = ''
        this.tmpglobalvariables.memo = ''
        this.tmpglobalvariables.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加全局变量
       */
      addglobalvariables() {
        this.$refs.tmpglobalvariables.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addglobalvariables(this.tmpglobalvariables).then(() => {
              this.$message.success('添加成功')
              this.getglobalvariablesList()
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
       * 显示修改全局变量对话框
       * @param index 全局变量下标
       */
      showUpdateglobalvariablesDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpglobalvariables.id = this.globalvariablesList[index].id
        this.tmpglobalvariables.keyname = this.globalvariablesList[index].keyname
        this.tmpglobalvariables.keyvalue = this.globalvariablesList[index].keyvalue
        this.tmpglobalvariables.memo = this.globalvariablesList[index].memo
      },
      /**
       * 更新全局变量
       */
      updateglobalvariables() {
        this.$refs.tmpglobalvariables.validate(valid => {
          if (valid) {
            updateglobalvariables(this.tmpglobalvariables).then(() => {
              this.$message.success('更新成功')
              this.getglobalvariablesList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除全局变量
       * @param index 全局变量下标
       */
      removeglobalvariables(index) {
        this.$confirm('删除该全局变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.globalvariablesList[index].id
          removeglobalvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getglobalvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 全局变量是否唯一
       * @param 全局变量
       */
      isUniqueDetail(globalvariables) {
        for (let i = 0; i < this.globalvariablesList.length; i++) {
          if (this.globalvariablesList[i].id !== globalvariables.id) { // 排除自己
            if (this.globalvariablesList[i].globalvariablesname === globalvariables.globalvariablesname) {
              this.$message.error('全局变量名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
