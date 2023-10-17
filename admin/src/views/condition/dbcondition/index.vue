<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('dbcondition:list')"
            @click.native.prevent="getdbconditionList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('dbcondition:add')"
            @click.native.prevent="showAdddbconditionDialog"
          >添加DB条件</el-button>
        </el-form-item>

        <span v-if="hasPermission('dbcondition:search')">
          <el-form-item label="父条件名:">
            <el-input clearable v-model="search.conditionname" @keyup.enter.native="searchBy" placeholder="父条件名"></el-input>
          </el-form-item>
          <el-form-item label="数据库条件名:">
            <el-input clearable v-model="search.subconditionname" @keyup.enter.native="searchBy" placeholder="数据库条件名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="dbconditionList"
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
      <el-table-column label="父条件名" align="center" prop="conditionname" width="120"/>
      <el-table-column label="数据库条件名" align="center" prop="subconditionname" width="130"/>
      <el-table-column label="环境" align="center" prop="enviromentname" width="120"/>
      <el-table-column label="组件名" align="center" prop="assemblename" width="100"/>
      <el-table-column label="Sql类型" align="center" prop="dbtype" width="80"/>
      <el-table-column label="Sql内容" align="center" prop="dbcontent" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.dbcontent }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>>
      <el-table-column label="操作人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="150"
                       v-if="hasPermission('dbcondition:update')  || hasPermission('dbcondition:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('dbcondition:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatedbconditionDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('dbcondition:delete') && scope.row.id !== id"
            @click.native.prevent="removedbcondition(scope.$index)"
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
        style="width: 600px; margin-left:30px;"
        :model="tmpdbcondition"
        ref="tmpdbcondition"
      >
        <el-form-item label="数据库条件名：" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="父条件：" prop="conditionname" required >
          <el-select v-model="tmpdbcondition.conditionname" filterable  placeholder="父条件名" style="width:100%" @change="ConditionselectChangedPC($event)" >
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(condition, index) in conditionList" :key="index">
              <el-option :label="condition.conditionname" :value="condition.conditionname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="环境：" prop="enviromentname" required >
          <el-select v-model="tmpdbcondition.enviromentname" filterable  placeholder="环境" style="width:100%" @change="selectChangedEN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(envname, index) in enviromentnameList" :key="index">
              <el-option :label="envname.enviromentname" :value="envname.enviromentname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="组件：" prop="assemblename" required >
          <el-select v-model="tmpdbcondition.assemblename" filterable placeholder="组件" style="width:100%" @change="ConditionselectChangedAS($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(macname, index) in enviroment_assembleList" :key="index">
              <el-option :label="macname.assemblename" :value="macname.assemblename" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="操作类型：" prop="dbtype" required >
          <el-select v-model="tmpdbcondition.dbtype" placeholder="操作类型" style="width:100%" @change="selectChangedDBType($event)">
            <el-option label="新增" value="Insert"  />
            <el-option label="删除" value="Delete"  />
            <el-option label="修改" value="Update"  />
            <el-option label="查询" value="Select"  />
          </el-select>
        </el-form-item>

        <el-form-item label="Sql语句：" prop="dbcontent" required>
          <el-input
            type="textarea"
            rows="10" cols="50"
            maxlength="2000"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdbcondition.dbcontent"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdbcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddbcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedbcondition"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { getenviromentallList as getenviromentallList } from '@/api/enviroment/testenviroment'
  import { getconditionallList as getconditionallList } from '@/api/condition/condition'
  import { getassembleallnameList as getassembleallnameList } from '@/api/enviroment/enviromentassemble'
  import { search, adddbcondition, updatedbcondition, removedbcondition } from '@/api/condition/dbcondition'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '数据库子条件',
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
        tmpsubconditionname: '',
        dbconditionList: [], // 环境服务器列表
        conditionList: [], // 条件服务器列表
        enviroment_assembleList: [], // 环境组件列表
        enviromentnameList: [], // 环境列表
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
          updateRole: '修改数据库条件',
          update: '修改数据库条件',
          add: '添加数据库条件'
        },
        btnLoading: false, // 按钮等待动画
        tmpdbcondition: {
          id: '',
          conditionid: '',
          conditionname: '',
          assembleid: '',
          assemblename: '',
          subconditionname: '',
          enviromentid: '',
          enviromentname: '',
          dbtype: '',
          dbcontent: '',
          connectstr: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          conditionname: null,
          subconditionname: null,
          projectid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.getconditionallList()
      this.getassembleallnameList()
      this.getdbconditionList()
      this.getenviromentallList()
      // this.getmachineLists()
    },

    activated() {
      this.getenviromentallList()
      this.getconditionallList()
      this.getassembleallnameList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取环境列表
       */
      getenviromentallList() {
        this.listLoading = true
        getenviromentallList(this.search).then(response => {
          this.enviromentnameList = response.data
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载数据库条件列表失败')
        })
      },

      /**
       * 获取父条件列表
       */
      getconditionallList() {
        getconditionallList(this.search).then(response => {
          this.conditionList = response.data
        }).catch(res => {
          this.$message.error('获取父条件列表失败')
        })
      },

      /**
       * 获取组件列表
       */
      getassembleallnameList() {
        getassembleallnameList(this.search).then(response => {
          this.enviroment_assembleList = response.data
        }).catch(res => {
          this.$message.error('获取组件列表失败')
        })
      },
      /**
       * 环境下拉选择事件获取环境id  e的值为options的选值
       */
      selectChangedEN(e) {
        for (let i = 0; i < this.enviromentnameList.length; i++) {
          if (this.enviromentnameList[i].enviromentname === e) {
            this.tmpdbcondition.enviromentid = this.enviromentnameList[i].id
          }
          console.log(this.enviromentnameList[i].id)
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      ConditionselectChangedPC(e) {
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === e) {
            this.tmpdbcondition.conditionid = this.conditionList[i].id
          }
        }
      },

      /**
       * 组件下拉选择事件获取组件id  e的值为options的选值
       */
      ConditionselectChangedAS(e) {
        for (let i = 0; i < this.enviroment_assembleList.length; i++) {
          if (this.enviroment_assembleList[i].assemblename === e) {
            this.tmpdbcondition.assembleid = this.enviroment_assembleList[i].id
          }
        }
      },
      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChangedMN(e) {
        for (let i = 0; i < this.machinenameList.length; i++) {
          if (this.machinenameList[i].machinename === e) {
            this.tmpdbcondition.machineid = this.machinenameList[i].id
          }
          console.log(this.machinenameList[i].id)
        }
      },

      /**
       * 获取服务器环境列表
       */
      getdbconditionList() {
        this.listLoading = true
        this.search.conditionname = this.tmpconditionname
        this.search.subconditionname = this.tmpsubconditionname
        search(this.search).then(response => {
          this.dbconditionList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载数据库条件服务器列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.dbconditionList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpconditionname = this.search.conditionname
        this.tmpsubconditionname = this.search.subconditionname
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
        this.getdbconditionList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getdbconditionList()
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
       * 显示添加数据库条件对话框
       */
      showAdddbconditionDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpdbcondition.id = ''
        this.tmpdbcondition.conditionname = ''
        this.tmpdbcondition.enviromentid = ''
        this.tmpdbcondition.enviromentname = ''
        this.tmpdbcondition.assembleid = ''
        this.tmpdbcondition.assemblename = ''
        this.tmpdbcondition.subconditionname = ''
        this.tmpdbcondition.dbtype = ''
        this.tmpdbcondition.dbcontent = ''
        this.tmpdbcondition.creator = this.name
        this.tmpdbcondition.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加数据库条件
       */
      adddbcondition() {
        this.$refs.tmpdbcondition.validate(valid => {
          if (valid) {
            this.btnLoading = true
            adddbcondition(this.tmpdbcondition).then(() => {
              this.$message.success('添加成功')
              this.getdbconditionList()
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
       * 显示修改数据库条件对话框
       * @param index 数据库条件下标
       */
      showUpdatedbconditionDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpdbcondition.id = this.dbconditionList[index].id
        this.tmpdbcondition.conditionid = this.dbconditionList[index].conditionid
        this.tmpdbcondition.assembleid = this.dbconditionList[index].assembleid
        this.tmpdbcondition.enviromentid = this.dbconditionList[index].enviromentid
        this.tmpdbcondition.enviromentname = this.dbconditionList[index].enviromentname
        this.tmpdbcondition.assemblename = this.dbconditionList[index].assemblename
        this.tmpdbcondition.conditionname = this.dbconditionList[index].conditionname
        this.tmpdbcondition.subconditionname = this.dbconditionList[index].subconditionname
        this.tmpdbcondition.dbtype = this.dbconditionList[index].dbtype
        this.tmpdbcondition.dbcontent = this.dbconditionList[index].dbcontent
        this.tmpdbcondition.creator = this.name
      },
      /**
       * 更新数据库条件
       */
      updatedbcondition() {
        this.$refs.tmpdbcondition.validate(valid => {
          if (valid) {
            updatedbcondition(this.tmpdbcondition).then(() => {
              this.$message.success('更新成功')
              this.getdbconditionList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除数据库条件
       * @param index 数据库条件下标
       */
      removedbcondition(index) {
        this.$confirm('删除该数据库条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dbconditionList[index].id
          removedbcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getdbconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 数据库条件是否唯一
       * @param 数据库条件
       */
      isUniqueDetail(dbcondition) {
        for (let i = 0; i < this.dbconditionList.length; i++) {
          if (this.dbconditionList[i].id !== dbcondition.id) { // 排除自己
            if (this.dbconditionList[i].enviromentname === dbcondition.enviromentname) {
              if (this.dbconditionList[i].machinename === dbcondition.machinename) {
                this.$message.error('数据库条件名已存在')
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
