<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('enviromentvariables:list')"
            @click.native.prevent="getenviromentvariablesList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('enviromentvariables:add')"
            @click.native.prevent="showAddenviromentvariablesDialog"
          >添加环境变量</el-button>
        </el-form-item>

        <span v-if="hasPermission('enviromentvariables:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.variablesname" @keyup.enter.native="searchBy" placeholder="环境变量名"></el-input>
          </el-form-item>
          <el-form-item label="环境：" prop="envname"  >
          <el-select clearable  v-model="search.envname" filterable  placeholder="环境" >
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="envname.index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" />
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
      :data="enviromentvariablesList"
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
      <el-table-column label="环境变量名" align="center" prop="variablesname" width="180"/>
      <el-table-column label="环境名" align="center" prop="envname" width="120"/>
      <el-table-column :show-overflow-tooltip="true" label="变量值" align="center" prop="variablesvalue" width="350"/>
      <el-table-column label="维护人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="160"
                       v-if="hasPermission('enviromentvariables:update')  || hasPermission('enviromentvariables:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('enviromentvariables:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentvariablesDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('enviromentvariables:delete') && scope.row.id !== id"
            @click.native.prevent="removeenviromentvariables(scope.$index)"
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
        :model="tmpenviromentvariables"
        ref="tmpenviromentvariables"
      >
        <el-form-item label="环境变量名" prop="variablesname" required>
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpenviromentvariables.variablesname"
          />
        </el-form-item>

        <el-form-item label="环境：" prop="envname" required >
          <el-select clearable  v-model="tmpenviromentvariables.envname" filterable  placeholder="环境" style="width:100%" @change="selectChangedEN($event)">
            <div v-for="(envname, index) in enviromentnameList" :key="envname.index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="变量值" prop="variablesvalue" required>
          <el-input
            type="textarea"
            rows="15" cols="50"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpenviromentvariables.variablesvalue"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpenviromentvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addenviromentvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateenviromentvariables"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
  import { search, addenviromentvariables, updateenviromentvariables, removeenviromentvariables } from '@/api/testvariables/enviromentvariables'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '环境变量',
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
        tmpvariablesname: '',
        tmpenvname: '',
        enviromentvariablesList: [], // 环境变量列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改环境变量',
          update: '修改环境变量',
          add: '添加环境变量'
        },
        btnLoading: false, // 按钮等待动画
        tmpenviromentvariables: {
          id: '',
          envid: '',
          variablesname: '',
          variablesvalue: '',
          envname: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          accountId: null,
          variablesname: null,
          envname: null,
          projectid: ''
        },
        searchenv: {
          page: 1,
          size: 10,
          projectid: ''
        }
      }
    },

    created() {
      this.searchenv.projectid = window.localStorage.getItem('pid')
      this.tmpenviromentvariables.creator = this.name
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getenviromentvariablesList()
      this.getenviromentallList()
    },

    activated() {
      this.getenviromentallList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    methods: {
      unix2CurrentTime,
      selectChangedEN(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpenviromentvariables.envid = this.enviromentnameList[i].id
          }
        }
      },
      /**
       * 获取环境列表
       */
      getenviromentallList() {
        getenviromentallList(this.searchenv).then(response => {
          this.enviromentnameList = response.data
        }).catch(res => {
          this.$message.error('加载环境列表失败')
        })
      },
      /**
       * 获取环境变量列表
       */
      getenviromentvariablesList() {
        this.listLoading = true
        this.search.variablesname = this.tmpvariablesname
        this.search.envname = this.tmpenvname
        search(this.search).then(response => {
          this.enviromentvariablesList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载环境变量列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.enviromentvariablesList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpenvname = this.search.envname
        this.tmpvariablesname = this.search.variablesname
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
        this.getenviromentvariablesList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getenviromentvariablesList()
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
       * 显示添加环境变量对话框
       */
      showAddenviromentvariablesDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpenviromentvariables.id = ''
        this.tmpenviromentvariables.variablesname = ''
        this.tmpenviromentvariables.envname = ''
        this.tmpenviromentvariables.envid = ''
        this.tmpenviromentvariables.variablesvalue = ''
        this.tmpenviromentvariables.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加环境变量
       */
      addenviromentvariables() {
        this.$refs.tmpenviromentvariables.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addenviromentvariables(this.tmpenviromentvariables).then(() => {
              this.$message.success('添加成功')
              this.getenviromentvariablesList()
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
       * 显示修改环境变量对话框
       * @param index 环境变量下标
       */
      showUpdateenviromentvariablesDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpenviromentvariables.id = this.enviromentvariablesList[index].id
        this.tmpenviromentvariables.variablesname = this.enviromentvariablesList[index].variablesname
        this.tmpenviromentvariables.variablesvalue = this.enviromentvariablesList[index].variablesvalue
        this.tmpenviromentvariables.envname = this.enviromentvariablesList[index].envname
      },
      /**
       * 更新环境变量
       */
      updateenviromentvariables() {
        this.$refs.tmpenviromentvariables.validate(valid => {
          if (valid) {
            updateenviromentvariables(this.tmpenviromentvariables).then(() => {
              this.$message.success('更新成功')
              this.getenviromentvariablesList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除环境变量
       * @param index 环境变量下标
       */
      removeenviromentvariables(index) {
        this.$confirm('删除该环境变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.enviromentvariablesList[index].id
          removeenviromentvariables(id).then(() => {
            this.$message.success('删除成功')
            this.getenviromentvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 环境变量是否唯一
       * @param 环境变量
       */
      isUniqueDetail(enviromentvariables) {
        for (let i = 0; i < this.enviromentvariablesList.length; i++) {
          if (this.enviromentvariablesList[i].id !== enviromentvariables.id) { // 排除自己
            if (this.enviromentvariablesList[i].variablesname === enviromentvariables.variablesname) {
              this.$message.error('环境变量名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
