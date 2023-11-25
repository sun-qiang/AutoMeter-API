<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('enviroment:list')"
            @click.native.prevent="getenviromentList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('enviroment:add')"
            @click.native.prevent="showAddenviromentDialog"
          >添加测试环境</el-button>
        </el-form-item>

        <span v-if="hasPermission('enviroment:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.enviromentname" @keyup.enter.native="searchBy" placeholder="测试环境名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="enviromentList"
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
      <el-table-column label="测试环境名" align="center" prop="enviromentname" width="150"/>
      <el-table-column label="环境类型" align="center" prop="envtype" width="80"/>
      <el-table-column label="描述" align="center" prop="memo" width="150"/>
      <el-table-column label="操作人" align="center" prop="creator" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('enviroment:update')  || hasPermission('enviroment:delete')">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentDialog(scope.$index)"
          >中间件</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentDialog(scope.$index)"
          >部署</el-button>
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('enviroment:delete') && scope.row.id !== id"
            @click.native.prevent="removeenviroment(scope.$index)"
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
        style="width: 400px; margin-left:50px;"
        :model="tmpenviroment"
        ref="tmpenviroment"
      >
        <el-form-item label="测试环境名" prop="enviromentname" required>
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpenviroment.enviromentname"
          />
        </el-form-item>

        <el-form-item label="环境类型" prop="envtype" required >
          <el-select v-model="tmpenviroment.envtype" placeholder="环境类型" style="width:100%">
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
            v-model="tmpenviroment.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpenviroment'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addenviroment"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateenviroment"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addenviroment, updateenviroment, removeenviroment } from '@/api/enviroment/testenviroment'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '测试环境',
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
        tmpenviromentname: '',
        enviromentList: [], // 环境列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改环境',
          update: '修改环境',
          add: '添加环境'
        },
        btnLoading: false, // 按钮等待动画
        tmpenviroment: {
          id: '',
          enviromentname: '',
          envtype: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          enviromentname: null,
          creator: '',
          projectid: ''
        }
      }
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.getenviromentList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取环境列表
       */
      getenviromentList() {
        this.listLoading = true
        this.search.enviromentname = this.tmpenviromentname
        this.search.creator = this.name
        search(this.search).then(response => {
          this.enviromentList = response.data.list
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
          this.enviromentList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpenviromentname = this.search.enviromentname
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
        this.getenviromentList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getenviromentList()
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
       * 显示添加测试环境对话框
       */
      showAddenviromentDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpenviroment.id = ''
        this.tmpenviroment.enviromentname = ''
        this.tmpenviroment.memo = ''
        this.tmpenviroment.envtype = ''
        this.tmpenviroment.creator = this.name
        this.tmpenviroment.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加测试环境
       */
      addenviroment() {
        this.$refs.tmpenviroment.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addenviroment(this.tmpenviroment).then(() => {
              this.$message.success('添加成功')
              this.getenviromentList()
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
       * 显示修改测试环境对话框
       * @param index 测试环境下标
       */
      showUpdateenviromentDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpenviroment.id = this.enviromentList[index].id
        this.tmpenviroment.enviromentname = this.enviromentList[index].enviromentname
        this.tmpenviroment.envtype = this.enviromentList[index].envtype
        this.tmpenviroment.memo = this.enviromentList[index].memo
        this.tmpenviroment.creator = this.name
        this.tmpenviroment.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 更新测试环境
       */
      updateenviroment() {
        this.$refs.tmpenviroment.validate(valid => {
          if (valid) {
            updateenviroment(this.tmpenviroment).then(() => {
              this.$message.success('更新成功')
              this.getenviromentList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除测试环境
       * @param index 测试环境下标
       */
      removeenviroment(index) {
        this.$confirm('删除该测试环境？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.enviromentList[index].id
          removeenviroment(id).then(() => {
            this.$message.success('删除成功')
            this.getenviromentList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 测试环境是否唯一
       * @param 测试环境
       */
      isUniqueDetail(enviroment) {
        for (let i = 0; i < this.enviromentList.length; i++) {
          if (this.enviromentList[i].id !== enviroment.id) { // 排除自己
            if (this.enviromentList[i].enviromentname === enviroment.enviromentname) {
              this.$message.error('测试环境名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
