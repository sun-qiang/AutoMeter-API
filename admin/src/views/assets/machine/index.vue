<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('machine:list')"
            @click.native.prevent="getmachineList"
          >刷新
          </el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('machine:add')"
            @click.native.prevent="showAddmachineDialog"
          >添加服务器
          </el-button>
        </el-form-item>

        <span v-if="hasPermission('machine:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.machinename" @keyup.enter.native="searchBy" placeholder="服务器名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.ip" @keyup.enter.native="searchBy" placeholder="ip"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="machineList"
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
      <el-table-column label="服务器名" align="center" prop="machinename" width="120"/>
      <el-table-column label="IP" align="center" prop="ip" width="120"/>
      <el-table-column label="CPU" align="center" prop="cpu" width="80"/>
      <el-table-column label="DISK" align="center" prop="disk" width="80"/>
      <el-table-column label="MEM" align="center" prop="mem" width="80"/>
      <el-table-column label="维护人" align="center" prop="creator" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('machine:update')  || hasPermission('machine:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('machine:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatemachineDialog(scope.$index)"
          >修改
          </el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('machine:delete') && scope.row.id !== id"
            @click.native.prevent="removemachine(scope.$index)"
          >删除
          </el-button>
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
        :model="tmpmachine"
        ref="tmpmachine"
      >
        <el-form-item label="服务器名" prop="machinename" required>
          <el-input
            maxlength="40"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpmachine.machinename"
          />
        </el-form-item>
        <el-form-item label="ip" prop="ip" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model.trim="tmpmachine.ip"
          />
        </el-form-item>
        <el-form-item label="cpu" prop="cpu" required>
          <el-input
            maxlength="40"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpmachine.cpu"
          />
        </el-form-item>
        <el-form-item label="disk" prop="disk" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpmachine.disk"
          />
        </el-form-item>
        <el-form-item label="mem" prop="mem" required>
          <el-input
            type="text"
            maxlength="300"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpmachine.mem"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpmachine'].resetFields()"
        >重置
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addmachine"
        >添加
        </el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatemachine"
        >修改
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    search,
    addmachine,
    updatemachine,
    removemachine
  } from '@/api/assets/machine'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '测试服务器',
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
        tmpmachinename: null,
        tmpip: null,
        machineList: [], // 服务器列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改服务器',
          update: '修改服务器',
          add: '添加服务器'
        },
        btnLoading: false, // 按钮等待动画
        tmpmachine: {
          id: '',
          machinename: '',
          ip: '',
          cpu: '',
          disk: '',
          mem: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          accountId: null,
          machinename: null,
          ip: null,
          projectid: '',
          creator: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    created() {
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getmachineList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取服务器列表
       */
      getmachineList() {
        this.listLoading = true
        this.search.machinename = this.tmpmachinename
        this.search.ip = this.tmpip
        this.search.creator = this.name
        search(this.search).then(response => {
          this.machineList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载服务器列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.machineList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpmachinename = this.search.machinename
        this.tmpip = this.search.ip
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
        this.getmachineList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getmachineList()
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
       * 显示添加服务器对话框
       */
      showAddmachineDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpmachine.id = ''
        this.tmpmachine.machinename = ''
        this.tmpmachine.ip = ''
        this.tmpmachine.cpu = ''
        this.tmpmachine.disk = ''
        this.tmpmachine.mem = ''
        this.tmpmachine.creator = this.name
        this.tmpmachine.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加服务器
       */
      addmachine() {
        this.$refs.tmpmachine.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addmachine(this.tmpmachine).then(() => {
              this.$message.success('添加成功')
              this.getmachineList()
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
       * 显示修改服务器对话框
       * @param index 服务器下标
       */
      showUpdatemachineDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpmachine.id = this.machineList[index].id
        this.tmpmachine.machinename = this.machineList[index].machinename
        this.tmpmachine.ip = this.machineList[index].ip
        this.tmpmachine.cpu = this.machineList[index].cpu
        this.tmpmachine.disk = this.machineList[index].disk
        this.tmpmachine.mem = this.machineList[index].mem
        this.tmpmachine.creator = this.name
        this.tmpmachine.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 更新服务器
       */
      updatemachine() {
        this.$refs.tmpmachine.validate(valid => {
          if (valid) {
            updatemachine(this.tmpmachine).then(() => {
              this.$message.success('更新成功')
              this.getmachineList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除服务器
       * @param index 服务器下标
       */
      removemachine(index) {
        this.$confirm('删除该服务器？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.machineList[index].id
          removemachine(id).then(() => {
            this.$message.success('删除成功')
            this.getmachineList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 服务器是否唯一
       * @param 服务器
       */
      isUniqueDetail(machine) {
        for (let i = 0; i < this.machineList.length; i++) {
          if (this.machineList[i].id !== machine.id) { // 排除自己
            if (this.machineList[i].machinename === machine.machinename) {
              this.$message.error('服务器名已存在')
              return false
            }
            if (this.machineList[i].ip === machine.ip) {
              this.$message.error('服务器名ip已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
