<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('scriptcondition:list')"
            @click.native.prevent="getscriptconditionList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('scriptcondition:add')"
            @click.native.prevent="showAddscriptconditionDialog"
          >添加脚本条件</el-button>
        </el-form-item>

        <span v-if="hasPermission('scriptcondition:search')">
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
      :data="scriptconditionList"
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
      <el-table-column label="脚本" align="center" prop="script" width="150">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.script }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('scriptcondition:update')  || hasPermission('scriptcondition:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('scriptcondition:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatescriptconditionDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('scriptcondition:delete') && scope.row.id !== id"
            @click.native.prevent="removescriptcondition(scope.$index)"
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
        :model="tmpscriptcondition"
        ref="tmpscriptcondition"
      >
        <el-form-item label="子条件名" prop="subconditionname" required>
          <el-input
            type="text"
            maxlength="30"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpscriptcondition.subconditionname"
          />
        </el-form-item>

        <el-form-item label="父条件名" prop="conditionname" required >
          <el-select v-model="tmpscriptcondition.conditionname" filterable  placeholder="父条件名" style="width:100%" @change="ConditionselectChanged($event)" >
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(condition, index) in conditionList" :key="index">
              <el-option :label="condition.conditionname" :value="condition.conditionname" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="Java代码" prop="script" required >
          <el-input
            type="textarea"
            rows="10" cols="50"
            maxlength="4000"
            v-model="tmpscriptcondition.script"
            placeholder="Java 代码"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpscriptcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addscriptcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatescriptcondition"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addscriptcondition, updatescriptcondition, removescriptcondition } from '@/api/condition/scriptcondition'
  import { gettestconditionforscripyanddelay } from '@/api/condition/condition'
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
        tmpconditionname: '',
        conditionList: [], // 条件列表
        scriptconditionList: [], // 脚本条件列表
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
          updateRole: '修改脚本条件',
          update: '修改脚本条件',
          add: '添加脚本条件'
        },
        btnLoading: false, // 按钮等待动画
        tmpconditionquery: {
          objecttype: '',
          projectid: ''
        },
        tmpscriptcondition: {
          id: '',
          subconditionname: '',
          conditionid: '',
          conditionname: '',
          script: '',
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
      this.getscriptconditionList()
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
            this.tmpscriptcondition.conditionid = this.conditionList[i].id
          }
        }
      },

      /**
       * 获取服务器环境列表
       */
      getscriptconditionList() {
        this.listLoading = true
        this.search.conditionname = this.tmpconditionname
        search(this.search).then(response => {
          this.scriptconditionList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载测试脚本条件列表失败')
        })
      },

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
          this.scriptconditionList = response.data.list
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
        this.getscriptconditionList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getscriptconditionList()
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
       * 显示添加脚本条件对话框
       */
      showAddscriptconditionDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpscriptcondition.id = ''
        this.tmpscriptcondition.subconditionname = ''
        this.tmpscriptcondition.conditionname = ''
        this.tmpscriptcondition.script = ''
        this.tmpscriptcondition.creator = this.name
        this.tmpscriptcondition.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加脚本条件
       */
      addscriptcondition() {
        this.$refs.tmpscriptcondition.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addscriptcondition(this.tmpscriptcondition).then(() => {
              this.$message.success('添加成功')
              this.getscriptconditionList()
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
       * 显示修改脚本条件对话框
       * @param index 脚本条件下标
       */
      showUpdatescriptconditionDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpscriptcondition.id = this.scriptconditionList[index].id
        this.tmpscriptcondition.subconditionname = this.scriptconditionList[index].subconditionname
        this.tmpscriptcondition.conditionname = this.scriptconditionList[index].conditionname
        this.tmpscriptcondition.script = this.scriptconditionList[index].script
        this.tmpscriptcondition.creator = this.name
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].conditionname === this.scriptconditionList[index].conditionname) {
            this.tmpscriptcondition.conditionid = this.conditionList[i].id
          }
        }
      },
      /**
       * 更新脚本条件
       */
      updatescriptcondition() {
        this.$refs.tmpscriptcondition.validate(valid => {
          if (valid) {
            updatescriptcondition(this.tmpscriptcondition).then(() => {
              this.$message.success('更新成功')
              this.getscriptconditionList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除脚本条件
       * @param index 脚本条件下标
       */
      removescriptcondition(index) {
        this.$confirm('删除该脚本条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.scriptconditionList[index].id
          removescriptcondition(id).then(() => {
            this.$message.success('删除成功')
            this.getscriptconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 脚本条件是否唯一
       * @param 脚本条件
       */
      isUniqueDetail(scriptcondition) {
        for (let i = 0; i < this.scriptconditionList.length; i++) {
          if (this.scriptconditionList[i].id !== scriptcondition.id) { // 排除自己
            if (this.scriptconditionList[i].enviromentname === scriptcondition.enviromentname) {
              if (this.scriptconditionList[i].machinename === scriptcondition.machinename) {
                this.$message.error('脚本条件名已存在')
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
