<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('slaver:list')"
            @click.native.prevent="getslaverList"
          >刷新</el-button>
        </el-form-item>

        <span v-if="hasPermission('slaver:search')">
          <el-form-item label="执行机名:">
            <el-input clearable v-model="search.slavername" @keyup.enter.native="searchBy" placeholder="执行机名"></el-input>
          </el-form-item>
          <el-form-item label="IP:">
            <el-input clearable v-model="search.ip" @keyup.enter.native="searchBy" placeholder="ip"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="slaverList"
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
      <el-table-column label="执行机名" align="center" prop="slavername" width="180"/>
      <el-table-column label="IP" align="center" prop="ip" width="120"/>
      <el-table-column label="端口" align="center" prop="port" width="60"/>
      <el-table-column label="状态" align="center" prop="status" width="60"/>
      <el-table-column label="类型" align="center" prop="stype" width="80"/>
      <el-table-column label="描述" align="center" prop="memo" width="180"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="150">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="150">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('slaver:update')  || hasPermission('slaver:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('slaver:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateslaverDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('slaver:delete') && scope.row.id !== id"
            @click.native.prevent="removeslaver(scope.$index)"
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
        label-width="100px"
        style="width: 300px; margin-left:50px;"
        :model="tmpslaver"
        ref="tmpslaver"
      >
        <el-form-item label="执行机名" prop="slavername" required>
          <el-input
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpslaver.slavername"
          />
        </el-form-item>
        <el-form-item label="ip" prop="ip">
          <el-input
            type="text"
            readonly="true"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpslaver.ip"
          />
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input
            type="text"
            readonly="true"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpslaver.port"
          />
        </el-form-item>

        <el-form-item label="类型" prop="stype" required >
          <el-select v-model="tmpslaver.stype" disabled placeholder="类型">
            <el-option label="功能" value="功能"/>
            <el-option label="性能" value="性能"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpslaver.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpslaver'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addslaver"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateslaver"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addslaver, updateslaver, removeslaver } from '@/api/dispatch/slaver'
  import { unix2CurrentTime } from '@/utils'

  export default {
    name: '测试执行机',
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
        tmpslavername: null,
        tmpip: null,
        slaverList: [], // 字典列表
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
        tmpslaver: {
          id: '',
          slavername: '',
          ip: '',
          port: '',
          status: '',
          stype: '',
          memo: ''
        },
        search: {
          page: 1,
          size: 10,
          slavername: null,
          ip: null
        }
      }
    },

    created() {
      this.getslaverList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取字典列表
       */
      getslaverList() {
        this.search.slavername = this.tmpslavername
        this.search.ip = this.tmpip
        this.listLoading = true
        search(this.search).then(response => {
          this.slaverList = response.data.list
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
          this.slaverList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpslavername = this.search.slavername
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
        this.getslaverList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getslaverList()
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
      showAddslaverDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpslaver.id = ''
        this.tmpslaver.slavername = ''
        this.tmpslaver.ip = ''
        this.tmpslaver.port = ''
        this.tmpslaver.status = ''
        this.tmpslaver.stype = ''
        this.tmpslaver.memo = ''
      },
      /**
       * 添加字典
       */
      addslaver() {
        this.$refs.tmpslaver.validate(valid => {
          if (valid && this.isUniqueDetail(this.tmpslaver)) {
            this.btnLoading = true
            addslaver(this.tmpslaver).then(() => {
              this.$message.success('添加成功')
              this.getslaverList()
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
      showUpdateslaverDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpslaver.id = this.slaverList[index].id
        this.tmpslaver.slavername = this.slaverList[index].slavername
        this.tmpslaver.ip = this.slaverList[index].ip
        this.tmpslaver.port = this.slaverList[index].port
        this.tmpslaver.status = this.slaverList[index].status
        this.tmpslaver.stype = this.slaverList[index].stype
        this.tmpslaver.memo = this.slaverList[index].memo
      },
      /**
       * 更新执行机
       */
      updateslaver() {
        if (this.isUniqueDetail(this.tmpslaver)) {
          updateslaver(this.tmpslaver).then(() => {
            this.$message.success('更新成功')
            this.getslaverList()
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
      removeslaver(index) {
        this.$confirm('删除该执行机？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.slaverList[index].id
          removeslaver(id).then(() => {
            this.$message.success('删除成功')
            this.getslaverList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 执行机资料是否唯一
       * @param 执行机
       */
      isUniqueDetail(slaver) {
        for (let i = 0; i < this.slaverList.length; i++) {
          if (this.slaverList[i].id !== slaver.id) { // 排除自己
            if (this.slaverList[i].slavername === slaver.slavername) {
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
