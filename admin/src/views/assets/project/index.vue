<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('project:list')"
            @click.native.prevent="getprojectList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('project:add')"
            @click.native.prevent="showAddprojectDialog"
          >创建项目</el-button>
        </el-form-item>

        <span v-if="hasPermission('project:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.projectname" @keyup.enter.native="searchBy" placeholder="项目名"></el-input>
          </el-form-item>

          <el-form-item label="状态" prop="status"  >
          <el-select v-model="search.status" clearable  placeholder="状态" style="width:100%">
            <el-option label="开始" value="开始"></el-option>
            <el-option label="暂停" value="暂停"></el-option>
            <el-option label="关闭" value="关闭"></el-option>
          </el-select>
        </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="projectList"
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
      <el-table-column label="项目名称" align="center" prop="projectname" width="150"/>
      <el-table-column label="状态" align="center" prop="status" width="80"/>
      <el-table-column :show-overflow-tooltip="true" label="项目简介" align="center" prop="memo" width="250"/>
      <el-table-column label="创建人" align="center" prop="creator" width="80"/>
<!--      <el-table-column label="成员" align="center" prop="creator" width="80"/>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('project:update')  || hasPermission('project:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('project:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateprojectDialog(scope.$index)"
          >修改</el-button>
<!--          <el-button-->
<!--            type="danger"-->
<!--            size="mini"-->
<!--            v-if="hasPermission('project:delete') && scope.row.id !== id"-->
<!--            @click.native.prevent="removeproject(scope.$index)"-->
<!--          >删除</el-button>-->
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('project:update') && scope.row.id !== id"
            @click.native.prevent="showprojectaccount(scope.$index)"
          >成员</el-button>
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
        :model="tmpproject"
        ref="tmpproject"
      >
        <el-form-item label="项目名" prop="projectname" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpproject.projectname"
          />
        </el-form-item>

        <el-form-item label="状态" prop="status" required >
          <el-select v-model="tmpproject.status" placeholder="状态" style="width:100%">
            <el-option label="开始" value="开始"></el-option>
            <el-option label="暂停" value="暂停"></el-option>
            <el-option label="关闭" value="关闭"></el-option>
          </el-select>
        </el-form-item>

<!--        <el-form-item label="开始时间：" prop="startTime" required >-->
<!--          <el-date-picker style="width:100%"-->
<!--                          v-model="tmpproject.startTime"-->
<!--                          type="datetime"-->
<!--                          format="yyyy-MM-dd HH:mm:ss"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="开始时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="结束时间：" prop="endTime" required >-->
<!--          <el-date-picker style="width:100%"-->
<!--                          v-model="tmpproject.endTime"-->
<!--                          type="datetime"-->
<!--                          format="yyyy-MM-dd HH:mm:ss"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="结束时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->

        <el-form-item label="项目描述" prop="memo" required>
          <el-input
            type="textarea"
            rows="10" cols="50"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpproject.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpproject'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addproject"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateproject"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="项目成员" :visible.sync="ProjectAccountdialogFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('project:add')"
              @click.native.prevent="showAddprojectAccountDialog"
            >增加成员</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="projectaccountTable"
        :data="projectaccountList"
        :key="projectaccountitemKey"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="projectaccountgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="项目名"  align="center" prop="projectname" width="180"/>
        <el-table-column label="用户"  align="center" prop="nickname" width="190"/>
        <el-table-column label="管理" align="center">
          <template slot-scope="scope">
            <el-button
              type="danger"
              size="mini"
              @click.native.prevent="removeprojectaccount(scope.$index)"
            >删除
            </el-button>
          </template>
        </el-table-column>

      </el-table>
      <el-pagination
        @size-change="projectaccounthandleSizeChange"
        @current-change="projectaccounthandleCurrentChange"
        :current-page="searchprojectaccount.page"
        :page-size="searchprojectaccount.size"
        :total="projectaccounttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>

    <el-dialog title="选择成员" :visible.sync="accountdialogFormVisible">
      <div class="filter-container" >
        <el-form :inline="true" :model="searchaccount" ref="searchaccount" >

          <el-form-item label="成员名:"  prop="nickname" >
              <el-input clearable maxlength="40" v-model="searchaccount.nickname" @keyup.enter.native="searchBy" placeholder="成员名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchaccountBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </el-form>

      </div>
      <el-table
        ref="caseTable"
        :data="accountList"
        :key="itemaccountKey"
        @selection-change="accounthandleSelectionChange"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="60">
          <template slot-scope="scope">
            <span v-text="accountgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>

        <el-table-column type="selection" prop="status" width="50"/>
        <el-table-column label="用户名" align="center" prop="nickname" width="520"/>
      </el-table>
      <el-pagination
        @size-change="accounthandleSizeChange"
        @current-change="accounthandleCurrentChange"
        :current-page="searchaccount.page"
        :page-size="searchaccount.size"
        :total="accounttotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="accountdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          :loading="btnLoading"
          @click.native.prevent="addprojectaccount"
        >新增</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
  import { search, addproject, updateproject, removeproject } from '@/api/assets/project'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'
  import { findaccountbyprojectid } from '@/api/assets/project_account'
  import { searchaccount } from '@/api/account'
  import { addprojectaccounts, removeprojectaccount } from '@/api/assets/project_account'

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
        itemKey: null,
        projectaccountitemKey: null,
        itemaccountKey: null,
        tmpprojectname: '',
        accountmultipleSelection: [], // 查询用例表格被选中的内容
        projectList: [], // 项目迭代列表
        projectaccountList: [], // 项目成员列表
        projectaccountsList: [], // 项目成员列表
        accountList: [], // 成员列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        projectaccounttotal: 0, // 数据总数
        accounttotal: 0,
        dialogStatus: 'add',
        dialogFormVisible: false,
        ProjectAccountdialogFormVisible: false,
        accountdialogFormVisible: false,
        textMap: {
          updateRole: '修改项目迭代',
          update: '修改项目迭代',
          add: '添加项目迭代'
        },
        btnLoading: false, // 按钮等待动画
        tmpproject: {
          id: '',
          projectname: '',
          status: '',
          startTime: '',
          endTime: '',
          memo: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          projectname: null,
          accountId: null,
          status: null
        },
        searchprojectaccount: {
          page: 1,
          size: 10,
          projectid: null,
          projectname: null,
          status: null
        },
        searchaccount: {
          page: 1,
          size: 10,
          nickname: null
        }
      }
    },

    created() {
      this.search.accountId = this.accountId
      this.getprojectList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'avatar', 'accountId'])
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取项目迭代列表
       */
      getprojectList() {
        this.listLoading = true
        this.search.projectname = this.tmpprojectname
        search(this.search).then(response => {
          this.projectList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载项目迭代列表失败')
        })
      },

      /**
       * 获取项目迭代列表
       */
      getprojectaccountList() {
        findaccountbyprojectid(this.searchprojectaccount).then(response => {
          this.projectaccountList = response.data.list
          this.projectaccounttotal = response.data.total
        }).catch(res => {
          this.$message.error('加载项目成员迭代列表失败')
        })
      },

      getaccountList() {
        searchaccount(this.searchaccount).then(response => {
          this.accountList = response.data.list
          this.accounttotal = response.data.total
        }).catch(res => {
          this.$message.error('加载成员迭代列表失败')
        })
      },
      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.projectList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpprojectname = this.search.projectname
        this.listLoading = false
        this.btnLoading = false
      },

      searchaccountBy() {
        this.searchaccount.page = 1
        searchaccount(this.searchaccount).then(response => {
          this.accountList = response.data.list
          this.accounttotal = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
      },
      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.search.page = 1
        this.search.size = size
        this.getprojectList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getprojectList()
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
       * 改变每页数量
       * @param size 页大小
       */
      projectaccounthandleSizeChange(size) {
        this.searchprojectaccount.page = 1
        this.searchprojectaccount.size = size
        this.getprojectaccountList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      projectaccounthandleCurrentChange(page) {
        this.searchprojectaccount.page = page
        this.getprojectaccountList()
      },
      /**
       * 表格序号
       * 可参考自定义表格序号
       * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
       * @param index 数据下标
       * @returns 表格序号
       */
      projectaccountgetIndex(index) {
        return (this.searchprojectaccount.page - 1) * this.searchprojectaccount.size + index + 1
      },

      accounthandleSizeChange(size) {
        this.searchaccount.page = 1
        this.searchaccount.size = size
        this.getaccountList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      accounthandleCurrentChange(page) {
        this.searchaccount.page = page
        this.getaccountList()
      },
      accountgetIndex(index) {
        return (this.searchaccount.page - 1) * this.searchaccount.size + index + 1
      },
      accounthandleSelectionChange(rows) {
        this.accountmultipleSelection = rows
        // console.log(this.casemultipleSelection)
      },
      /**
       * 显示添加项目迭代对话框
       */
      showAddprojectDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpproject.id = ''
        this.tmpproject.projectname = ''
        this.tmpproject.memo = ''
        this.tmpproject.status = ''
        this.tmpproject.startTime = ''
        this.tmpproject.endTime = ''
        this.tmpproject.creator = this.name
      },

      showAddprojectAccountDialog() {
        // 显示新增对话框
        this.accountdialogFormVisible = true
        this.getaccountList()
      },

      showprojectaccount(index) {
        // 显示新增对话框
        this.ProjectAccountdialogFormVisible = true
        this.searchprojectaccount.projectid = this.projectList[index].id
        this.searchprojectaccount.projectname = this.projectList[index].projectname
        this.getprojectaccountList()
      },
      /**
       * 添加项目迭代
       */
      addproject() {
        this.$refs.tmpproject.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addproject(this.tmpproject).then(() => {
              this.$message.success('添加成功')
              this.getprojectList()
              this.dialogFormVisible = false
              this.btnLoading = false
              // window.localStorage.setItem()
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      addprojectaccount() {
        this.projectaccountsList = []
        if (this.accountmultipleSelection.length === 0) {
          this.$message.error('请选择用户')
        } else {
          for (let i = 0; i < this.accountmultipleSelection.length; i++) {
            this.projectaccountsList.push({
              'projectid': this.searchprojectaccount.projectid,
              'accountid': this.accountmultipleSelection[i].id,
              'projectname': this.searchprojectaccount.projectname,
              'nickname': this.accountmultipleSelection[i].nickname,
              'creator': this.name
            })
          }
          addprojectaccounts(this.projectaccountsList).then(() => {
            this.accountdialogFormVisible = false
            this.getprojectaccountList()
            this.$message.success('添加成功')
          }).catch(res => {
            this.$message.error('添加失败')
          })
        }
      },
      /**
       * 显示修改项目迭代对话框
       * @param index 项目迭代下标
       */
      showUpdateprojectDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpproject.id = this.projectList[index].id
        this.tmpproject.projectname = this.projectList[index].projectname
        this.tmpproject.status = this.projectList[index].status
        this.tmpproject.startTime = this.projectList[index].startTime
        this.tmpproject.endTime = this.projectList[index].endTime
        this.tmpproject.memo = this.projectList[index].memo
        this.tmpproject.creator = this.name
      },
      /**
       * 更新项目迭代
       */
      updateproject() {
        this.$refs.tmpproject.validate(valid => {
          if (valid) {
            updateproject(this.tmpproject).then(() => {
              this.$message.success('更新成功')
              this.getprojectList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除项目迭代
       * @param index 项目迭代下标
       */
      removeproject(index) {
        this.$confirm('删除该项目迭代？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.projectList[index].id
          removeproject(id).then(() => {
            this.$message.success('删除成功')
            this.getprojectList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removeprojectaccount(index) {
        this.$confirm('删除该用户？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.projectaccountList[index].id
          removeprojectaccount(id).then(() => {
            this.$message.success('删除成功')
            this.getprojectaccountList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 项目迭代是否唯一
       * @param 项目迭代
       */
      isUniqueDetail(project) {
        for (let i = 0; i < this.projectList.length; i++) {
          if (this.projectList[i].id !== project.id) { // 排除自己
            if (this.projectList[i].projectname === project.projectname) {
              this.$message.error('项目名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
