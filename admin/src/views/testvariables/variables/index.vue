<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('variables:list')"
            @click.native.prevent="getvariablesList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('variables:add')"
            @click.native.prevent="showAddvariablesDialog"
          >添加随机变量</el-button>
        </el-form-item>

        <span v-if="hasPermission('variables:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.variablesname" @keyup.enter.native="searchBy" placeholder="随机变量名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="variablesList"
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
      <el-table-column label="随机变量名" align="center" prop="variablesname" width="120"/>
      <el-table-column label="变量类型" align="center" prop="variablestype" width="120"/>
      <el-table-column label="变量条件" align="center" prop="variablecondition" width="100"/>
      <el-table-column label="随机描述" align="center" prop="memo" width="250"/>
      <el-table-column label="维护人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>
      <el-table-column label="管理" align="center"
                       v-if="hasPermission('variables:update')  || hasPermission('variables:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('variables:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatevariablesDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('variables:delete') && scope.row.id !== id"
            @click.native.prevent="removevariables(scope.$index)"
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
        :model="tmpvariables"
        ref="tmpvariables"
      >
        <el-form-item label="随机变量名" prop="variablesname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpvariables.variablesname"
          />
        </el-form-item>

        <el-form-item label="变量类型" prop="variablestype" required >
          <el-select v-model="tmpvariables.variablestype" placeholder="变量类型" style="width:100%" @change="variablestypeselectChanged($event)">
            <el-option label="当前时间" value="当前时间"></el-option>
            <el-option label="当前日期" value="当前日期"></el-option>
            <el-option label="当前时间戳" value="当前时间戳"></el-option>
            <el-option label="随机IP" value="随机IP"></el-option>
            <el-option label="随机小数" value="随机小数"></el-option>
            <el-option label="GUID" value="GUID"></el-option>
            <el-option label="随机字符串" value="随机字符串"></el-option>
            <el-option label="随机整数" value="随机整数"></el-option>
            <el-option label="随机数组值" value="随机数组值"></el-option>
          </el-select>
        </el-form-item>

        <div v-if="variablecondition">
          <el-form-item label="变量条件" prop="variablecondition" >
            <el-input
              type="textarea"
              rows="3" cols="30"
              :placeholder='variableconditionplaceholder'
              prefix-icon="el-icon-message"
              auto-complete="off"
              v-model="tmpvariables.variablecondition"
            />
          </el-form-item>
        </div>

        <el-form-item label="变量描述" prop="memo" >
          <el-input
            type="text"
            maxlength="60"
            placeholder="变量描述"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpvariables.memo"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpvariables'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addvariables"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatevariables"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addvariables, updatevariables, removevariables } from '@/api/testvariables/variables'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '随机变量',
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
        variablecondition: false,
        variableconditionplaceholder: '1.数字范围使用英文逗号分开，例如：1,100 \n 2.字符串长度例如：4',
        variablesList: [], // 变量列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改变量',
          update: '修改变量',
          add: '添加变量'
        },
        btnLoading: false, // 按钮等待动画
        tmpvariables: {
          id: '',
          variablesname: '',
          variablestype: '',
          variablecondition: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          accountId: null,
          variablesname: null,
          projectid: ''
        }
      }
    },

    created() {
      this.tmpvariables.creator = this.name
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getvariablesList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取环境列表
       */
      getvariablesList() {
        this.listLoading = true
        this.search.variablesname = this.tmpvariablesname
        search(this.search).then(response => {
          this.variablesList = response.data.list
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
          this.variablesList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpvariablesname = this.search.variablesname
        this.listLoading = false
        this.btnLoading = false
      },

      /**
       * 条件下拉选择事件获取条件id  e的值为options的选值
       */
      variablestypeselectChanged(e) {
        if (e === '随机字符串' || e === '随机整数' || e === '随机小数' || e === '当前日期' || e === '随机数组值') {
          this.variablecondition = true
        } else {
          this.variablecondition = false
        }
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.search.page = 1
        this.search.size = size
        this.getvariablesList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getvariablesList()
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
       * 显示添加随机变量对话框
       */
      showAddvariablesDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpvariables.id = ''
        this.tmpvariables.variablesname = ''
        this.tmpvariables.memo = ''
        this.tmpvariables.variablecondition = ''
        this.tmpvariables.variablestype = ''
        this.tmpvariables.creator = this.name
        this.tmpvariables.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加随机变量
       */
      addvariables() {
        this.$refs.tmpvariables.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addvariables(this.tmpvariables).then(() => {
              this.$message.success('添加成功')
              this.getvariablesList()
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
       * 显示修改随机变量对话框
       * @param index 随机变量下标
       */
      showUpdatevariablesDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpvariables.id = this.variablesList[index].id
        this.tmpvariables.variablesname = this.variablesList[index].variablesname
        this.tmpvariables.variablestype = this.variablesList[index].variablestype
        if (this.tmpvariables.variablestype === '随机字符串' || this.tmpvariables.variablestype === '随机整数' || this.tmpvariables.variablestype === '随机小数') {
          this.variablecondition = true
        } else {
          this.variablecondition = false
        }
        this.tmpvariables.variablecondition = this.variablesList[index].variablecondition
        this.tmpvariables.memo = this.variablesList[index].memo
        this.tmpvariables.creator = this.name
      },
      /**
       * 更新随机变量
       */
      updatevariables() {
        this.$refs.tmpvariables.validate(valid => {
          if (valid) {
            updatevariables(this.tmpvariables).then(() => {
              this.$message.success('更新成功')
              this.getvariablesList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除随机变量
       * @param index 随机变量下标
       */
      removevariables(index) {
        this.$confirm('删除该随机变量？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.variablesList[index].id
          removevariables(id).then(() => {
            this.$message.success('删除成功')
            this.getvariablesList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 随机变量是否唯一
       * @param 随机变量
       */
      isUniqueDetail(variables) {
        for (let i = 0; i < this.variablesList.length; i++) {
          if (this.variablesList[i].id !== variables.id) { // 排除自己
            if (this.variablesList[i].variablesname === variables.variablesname) {
              this.$message.error('随机变量名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
