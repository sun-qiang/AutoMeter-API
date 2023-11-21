<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('delaycondition:list')"
            @click.native.prevent="getdelayconditionList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('delaycondition:add')"
            @click.native.prevent="showAdddelayconditionDialog"
          >添加延时条件</el-button>
        </el-form-item>

        <span v-if="hasPermission('delaycondition:search')">
          <el-form-item label="父条件名:">
            <el-select v-model="search.conditionname" filterable clearable placeholder="父条件名">
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
      :data="delayconditionList"
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

      <el-table-column label="子条件名" align="center" prop="subconditionname" width="200"/>
      <el-table-column label="父条件名" align="center" prop="conditionname" width="200"/>
      <el-table-column label="等待时间(秒)" align="center" prop="delaytime" width="150">
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('delaycondition:update')  || hasPermission('delaycondition:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('delaycondition:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatedelayconditionDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('delaycondition:delete') && scope.row.id !== id"
            @click.native.prevent="removedelaycondition(scope.$index)"
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
        style="width: 600px; margin-left:50px;"
        :model="tmpdelaycondition"
        ref="tmpdelaycondition"
      >
        <el-form-item label="子条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdelaycondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="父条件名" prop="conditionname" required >
          <el-select v-model="tmpdelaycondition.conditionname" filterable   placeholder="父条件名" style="width:100%" @change="ConditionselectChanged($event)" >
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(condition, index) in conditionList" :key="index">
              <el-option :label="condition.conditionname" :value="condition.conditionname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="等待时间(秒)" prop="delaytime" required>
          <el-input
            placeholder="数据库子条件查询Sql结果行号"
            oninput="value=value.replace(/[^\d]/g,'')"
            maxLength='10'
            type="number"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdelaycondition.delaytime"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdelaycondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddelaycondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedelaycondition"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, adddelaycondition, updatedelaycondition, removedelaycondition } from '@/api/condition/delaycondition'
  import { unix2CurrentTime } from '@/utils'
  import { gettestconditionforscripyanddelay } from '@/api/condition/condition'
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
        delayconditionList: [], // 延时条件列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改延时条件',
          update: '修改延时条件',
          add: '添加延时条件'
        },
        btnLoading: false, // 按钮等待动画
        tmpconditionquery: {
          objecttype: '',
          projectid: ''
        },
        tmpdelaycondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          delaytime: '',
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
      this.tmpconditionquery.projectid = window.localStorage.getItem('pid')
      this.getdelayconditionList()
      this.getalltestconditionbytype()
    },

    activated() {
      this.getalltestconditionbytype()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 条件下拉选择事件获取条件id  e的值为options的选值
       */
      ConditionselectChanged(e) {
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === e) {
            this.tmpdelaycondition.conditionid = this.conditionList[i].id
          }
        }
      },

      /**
       * 获取服务器环境列表
       */
      getdelayconditionList() {
        this.listLoading = true
        this.search.conditionname = this.tmpconditionname
        search(this.search).then(response => {
          this.delayconditionList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试延时条件列表失败')
        })
      },

      // /**
      //  * 获取条件列表
      //  */
      // getalltestcondition() {
      //   this.listLoading = true
      //   getalltestcondition().then(response => {
      //     this.conditionList = response.data
      //     this.total = response.data.total
      //     this.listLoading = false
      //   }).catch(res => {
      //     this.$message.error('加载条件列表失败')
      //   })
      // },

      /**
       * 获取条件列表
       */
      getalltestconditionbytype() {
        this.listLoading = true
        this.tmpconditionquery.objecttype = '测试用例'
        gettestconditionforscripyanddelay(this.tmpconditionquery).then(response => {
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
          this.delayconditionList = response.data.list
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
        this.getdelayconditionList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getdelayconditionList()
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
       * 显示添加延时条件对话框
       */
      showAdddelayconditionDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpdelaycondition.id = ''
        this.tmpdelaycondition.subconditionname = ''
        this.tmpdelaycondition.conditionname = ''
        this.tmpdelaycondition.delaytime = ''
        this.tmpdelaycondition.creator = this.name
        this.tmpdelaycondition.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加延时条件
       */
      adddelaycondition() {
        this.$refs.tmpdelaycondition.validate(valid => {
          if (valid) {
            this.btnLoading = true
            adddelaycondition(this.tmpdelaycondition).then(() => {
              this.$message.success('添加成功')
              this.getdelayconditionList()
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
       * 显示修改延时条件对话框
       * @param index 延时条件下标
       */
      showUpdatedelayconditionDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpdelaycondition.id = this.delayconditionList[index].id
        this.tmpdelaycondition.subconditionname = this.delayconditionList[index].subconditionname
        this.tmpdelaycondition.conditionname = this.delayconditionList[index].conditionname
        this.tmpdelaycondition.delaytime = this.delayconditionList[index].delaytime
        this.tmpdelaycondition.creator = this.name
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === this.delayconditionList[index].conditionname) {
            this.tmpdelaycondition.conditionid = this.conditionList[i].id
          }
        }
      },
      /**
       * 更新延时条件
       */
      updatedelaycondition() {
        this.$refs.tmpdelaycondition.validate(valid => {
          if (valid) {
            updatedelaycondition(this.tmpdelaycondition).then(() => {
              this.$message.success('更新成功')
              this.getdelayconditionList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除延时条件
       * @param index 延时条件下标
       */
      removedelaycondition(index) {
        this.$confirm('删除该延时条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.delayconditionList[index].id
          removedelaycondition(id).then(() => {
            this.$message.success('删除成功')
            this.getdelayconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 延时条件是否唯一
       * @param 延时条件
       */
      isUniqueDetail(delaycondition) {
        for (let i = 0; i < this.delayconditionList.length; i++) {
          if (this.delayconditionList[i].id !== delaycondition.id) { // 排除自己
            if (this.delayconditionList[i].enviromentname === delaycondition.enviromentname) {
              if (this.delayconditionList[i].machinename === delaycondition.machinename) {
                this.$message.error('延时条件名已存在')
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
