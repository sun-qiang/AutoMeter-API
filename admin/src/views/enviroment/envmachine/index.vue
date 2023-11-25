<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('envmachine:list')"
            @click.native.prevent="getenvmachineList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('envmachine:add')"
            @click.native.prevent="showAddenvmachineDialog"
          >添加环境服务器</el-button>
        </el-form-item>

        <span v-if="hasPermission('envmachine:search')">
          <el-form-item>
            <el-input clearable v-model="search.enviromentname" @keyup.enter.native="searchBy" placeholder="测试环境名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="envmachineList"
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
      <el-table-column label="测试环境" align="center" prop="enviromentname" width="120"/>
      <el-table-column label="服务器" align="center" prop="machinename" width="120"/>
      <el-table-column label="ip" align="center" prop="ip" width="120"/>
      <el-table-column label="操作人" align="center" prop="creator" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('envmachine:update')  || hasPermission('envmachine:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('envmachine:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenvmachineDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('envmachine:delete') && scope.row.id !== id"
            @click.native.prevent="removeenvmachine(scope.$index)"
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
        :model="tmpenvmachine"
        ref="tmpenvmachine"
      >
        <el-form-item label="测试环境" prop="enviromentname" required >
          <el-select v-model="tmpenvmachine.enviromentname"  placeholder="测试环境名" style="width:100%" @change="selectChangedEN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="服务器" prop="machinename" required >
          <el-select v-model="tmpenvmachine.machinename" placeholder="服务器" style="width:100%" @change="selectChangedMN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(macname, index) in machinenameList" :key="index">
              <el-option :label="`${macname.machinename} ：${macname.ip}`" :value="macname.machinename" required/>
            </div>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpenvmachine'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addenvmachine"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateenvmachine"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addenvmachine, updateenvmachine, removeenvmachine } from '@/api/enviroment/envmachine'
  import { getmachineLists as getmachineLists } from '@/api/assets/machine'
  import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
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
        tmpenviromentname: '',
        envmachineList: [], // 环境服务器列表
        enviromentnameList: [], // 环境列表
        machinenameList: [], // 服务器列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true,
          enviromentname: ''
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改测试环境',
          update: '修改测试环境',
          add: '添加测试环境'
        },
        btnLoading: false, // 按钮等待动画
        tmpenvmachine: {
          id: '',
          envid: '',
          machineid: '',
          enviromentname: '',
          machinename: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          enviromentname: null,
          creator: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'avatar'])
    },

    created() {
      this.getenvmachineList()
      this.getenviromentallList()
      this.getmachineLists()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChangedEN(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpenvmachine.envid = this.enviromentnameList[i].id
          }
          console.log(this.enviromentnameList[i].id)
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChangedMN(e) {
        for (let i = 0; i < this.machinenameList.length; i++) {
          if (this.machinenameList[i].machinename === e) {
            this.tmpenvmachine.machineid = this.machinenameList[i].id
          }
          console.log(this.machinenameList[i].id)
        }
      },

      /**
       * 获取服务器环境列表
       */
      getenvmachineList() {
        this.listLoading = true
        this.search.enviromentname = this.tmpenviromentname
        this.search.creator = this.name
        search(this.search).then(response => {
          this.envmachineList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试环境服务器列表失败')
        })
      },

      /**
       * 获取环境列表
       */
      getenviromentallList() {
        this.listLoading = true
        getenviromentallList().then(response => {
          this.enviromentnameList = response.data
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试环境列表失败')
        })
      },

      /**
       * 获取服务器列表
       */
      getmachineLists() {
        this.listLoading = true
        getmachineLists().then(response => {
          this.machinenameList = response.data
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
          this.envmachineList = response.data.list
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
        this.getenvmachineList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getenvmachineList()
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
      showAddenvmachineDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpenvmachine.id = ''
        this.tmpenvmachine.envid = ''
        this.tmpenvmachine.machineid = ''
        this.tmpenvmachine.machinename = ''
        this.tmpenvmachine.enviromentname = ''
        this.tmpenvmachine.creator = this.name
      },
      /**
       * 添加测试环境
       */
      addenvmachine() {
        this.$refs.tmpenvmachine.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addenvmachine(this.tmpenvmachine).then(() => {
              this.$message.success('添加成功')
              this.getenvmachineList()
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
      showUpdateenvmachineDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpenvmachine.id = this.envmachineList[index].id
        this.tmpenvmachine.envid = this.envmachineList[index].envid
        this.tmpenvmachine.machineid = this.envmachineList[index].machineid
        this.tmpenvmachine.machinename = this.envmachineList[index].machinename
        this.tmpenvmachine.enviromentname = this.envmachineList[index].enviromentname
        this.tmpenvmachine.creator = this.name
      },
      /**
       * 更新测试环境
       */
      updateenvmachine() {
        this.$refs.tmpenvmachine.validate(valid => {
          if (valid) {
            updateenvmachine(this.tmpenvmachine).then(() => {
              this.$message.success('更新成功')
              this.getenvmachineList()
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
      removeenvmachine(index) {
        this.$confirm('删除该测试环境？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.envmachineList[index].id
          removeenvmachine(id).then(() => {
            this.$message.success('删除成功')
            this.getenvmachineList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 测试环境是否唯一
       * @param 测试环境
       */
      isUniqueDetail(envmachine) {
        for (let i = 0; i < this.envmachineList.length; i++) {
          if (this.envmachineList[i].id !== envmachine.id) { // 排除自己
            if (this.envmachineList[i].enviromentname === envmachine.enviromentname) {
              if (this.envmachineList[i].machinename === envmachine.machinename) {
                this.$message.error('测试环境名已存在')
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
