<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('globalheader:list')"
            @click.native.prevent="getglobalheaderList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('globalheader:add')"
            @click.native.prevent="showAddglobalheaderDialog"
          >添加全局Header</el-button>
        </el-form-item>

        <span v-if="hasPermission('globalheader:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.globalheadername" @keyup.enter.native="searchBy" placeholder="全局Header名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="globalheaderList"
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
      <el-table-column label="全局Header名" align="center" prop="globalheadername" width="280"/>
      <el-table-column label="维护人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="200">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('globalheader:update')  || hasPermission('globalheader:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('globalheader:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateglobalheaderDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('globalheader:delete') && scope.row.id !== id"
            @click.native.prevent="removeglobalheader(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('globalheader:update') && scope.row.id !== id"
            @click.native.prevent="showglobalheaderparamsDialog(scope.$index)"
          >Header参数</el-button>
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
        style="width: 500px; margin-left:50px;"
        :model="tmpglobalheader"
        ref="tmpglobalheader"
      >
        <el-form-item label="全局Header名" prop="globalheadername" required>
          <el-input
            maxlength="50"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpglobalheader.globalheadername"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpglobalheader'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addglobalheader"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateglobalheader"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="BindtextMap[BinddialogStatus]" :visible.sync="BindFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
        :model="tmpglobalheaderparams"
        ref="tmpglobalheaderparams"
      >

        <el-form-item label="参数名：" prop="keyname" required>
          <el-input
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model.trim="tmpglobalheaderparams.keyname"
          />
        </el-form-item>
        <el-form-item label="参数值：" prop="keyvalue" required>
          <el-input
            type="textarea"
            rows="15" cols="50"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model.trim="tmpglobalheaderparams.keyvalue"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="BindFormVisible = false">取消</el-button>
        <el-button
          type="success"
          v-if="BinddialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addglobalheaderparams"
        >保存</el-button>
        <el-button
          type="success"
          v-if="BinddialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateglobalheaderparams"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title='全局Header参数' :visible.sync="BindVariablesDialogVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('globalheader:add')"
              @click.native.prevent="showparamsDialog"
            >添加参数</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        :data="globalheaderParamsList"
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
        <el-table-column label="参数名" align="center" prop="keyname" width="100"/>
        <el-table-column :show-overflow-tooltip="true" label="参数值" align="center" prop="keyvalue" width="100"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('globalheader:update')  || hasPermission('globalheader:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('globalheader:update') && scope.row.id !== id"
              @click.native.prevent="showglobalheaderparams(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('globalheader:delete') && scope.row.id !== id"
              @click.native.prevent="removeglobalheaderparams(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
  import { search as searchheaderparams, addglobalheaderparams, updateglobalheaderparams, removeglobalheaderparams } from '@/api/testvariables/globalheaderparams'
  import { search, addglobalheader, updateglobalheader, removeglobalheader } from '@/api/testvariables/globalheader'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '全局Header',
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
        tmpglobalheadername: '',
        globalheaderList: [], // 全局Header列表
        globalheaderParamsList: [], // 用例全局Header参数列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        BinddialogStatus: 'add',
        dialogFormVisible: false,
        BindVariablesDialogVisible: false,
        BindFormVisible: false,
        textMap: {
          updateRole: '修改接口全局Header',
          update: '修改接口全局Header',
          add: '添加接口全局Header'
        },
        BindtextMap: {
          updateRole: '修改绑定全局Header',
          update: '修改绑定全局Header',
          add: '添加绑定全局Header'
        },
        paramsquery: {
          page: 1,
          size: 10,
          globalheaderid: ''
        },
        btnLoading: false, // 按钮等待动画
        tmpglobalheader: {
          id: '',
          globalheadername: '',
          variablesdes: '',
          valuetype: '',
          globalheadertype: '接口全局Header',
          variablesexpress: '',
          memo: '',
          creator: ''
        },
        tmpglobalheaderparams: {
          id: '',
          globalheaderid: '',
          keyname: '',
          keyvalue: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          accountId: null,
          globalheadername: null,
          projectid: ''
        }
      }
    },

    created() {
      this.tmpglobalheader.creator = this.name
      this.search.accountId = this.accountId
      this.search.projectid = window.localStorage.getItem('pid')
      this.getglobalheaderList()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取全局Header列表
       */
      getglobalheaderList() {
        this.listLoading = true
        this.search.globalheadername = this.tmpglobalheadername
        search(this.search).then(response => {
          this.globalheaderList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载全局Header列表失败')
        })
      },

      /**
       * 获取全局Header列表
       */
      searchheaderparams() {
        searchheaderparams(this.paramsquery).then(response => {
          this.globalheaderParamsList = response.data.list
        }).catch(res => {
          this.$message.error('加载全局Header参数列表失败')
        })
      },
      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.globalheaderList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpglobalheadername = this.search.globalheadername
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
        this.getglobalheaderList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getglobalheaderList()
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
       * 显示绑定全局Header列表对话框
       */
      showglobalheaderparamsDialog(index) {
        // 显示新增对话框
        this.BindVariablesDialogVisible = true
        this.tmpglobalheaderparams.globalheaderid = this.globalheaderList[index].id
        this.paramsquery.globalheaderid = this.globalheaderList[index].id
        this.searchheaderparams()
      },
      /**
       * 显示添加绑定用例全局Header对话框
       */
      showparamsDialog(index) {
        // 显示新增对话框
        this.BindFormVisible = true
        this.BinddialogStatus = 'add'
        this.tmpglobalheaderparams.id = ''
        this.tmpglobalheaderparams.keyname = ''
        this.tmpglobalheaderparams.keyvalue = ''
        this.tmpglobalheaderparams.globalheaderid = this.globalheaderList[index].id
      },
      /**
       * 显示添加全局Header对话框
       */
      showAddglobalheaderDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpglobalheader.id = ''
        this.tmpglobalheader.globalheadername = ''
        this.tmpglobalheader.variablesdes = ''
        this.tmpglobalheader.globalheadertype = ''
        this.tmpglobalheader.variablesexpress = ''
        this.tmpglobalheader.memo = ''
        this.tmpglobalheader.valuetype = ''
        this.tmpglobalheader.tmpglobalheader = ''
        this.tmpglobalheader.creator = this.name
        this.tmpglobalheader.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加全局Header
       */
      addglobalheader() {
        this.$refs.tmpglobalheader.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addglobalheader(this.tmpglobalheader).then(() => {
              this.$message.success('添加成功')
              this.getglobalheaderList()
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
       * 添加用例全局Header
       */
      addglobalheaderparams() {
        this.$refs.tmpglobalheaderparams.validate(valid => {
          if (valid) {
            addglobalheaderparams(this.tmpglobalheaderparams).then(() => {
              this.$message.success('添加成功')
              this.BindFormVisible = false
              this.searchheaderparams()
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      /**
       * 显示修改用例全局Header对话框
       * @param index 用例全局Header下标
       */
      showglobalheaderparams(index) {
        this.BindFormVisible = true
        this.BinddialogStatus = 'update'
        this.tmpglobalheaderparams.id = this.globalheaderParamsList[index].id
        this.tmpglobalheaderparams.keyname = this.globalheaderParamsList[index].keyname
        this.tmpglobalheaderparams.keyvalue = this.globalheaderParamsList[index].keyvalue
      },
      /**
       * 更新用例全局Header
       */
      updateglobalheaderparams() {
        this.$refs.tmpglobalheaderparams.validate(valid => {
          if (valid) {
            updateglobalheaderparams(this.tmpglobalheaderparams).then(() => {
              this.$message.success('更新成功')
              this.searchheaderparams()
              this.BindFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 显示修改全局Header对话框
       * @param index 全局Header下标
       */
      showUpdateglobalheaderDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpglobalheader.id = this.globalheaderList[index].id
        this.tmpglobalheader.globalheadername = this.globalheaderList[index].globalheadername
      },
      /**
       * 更新全局Header
       */
      updateglobalheader() {
        this.$refs.tmpglobalheader.validate(valid => {
          if (valid) {
            updateglobalheader(this.tmpglobalheader).then(() => {
              this.$message.success('更新成功')
              this.getglobalheaderList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除用例全局Header
       * @param index 用例全局Header下标
       */
      removeglobalheaderparams(index) {
        this.$confirm('删除该参数？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.globalheaderParamsList[index].id
          removeglobalheaderparams(id).then(() => {
            this.$message.success('删除成功')
            this.searchheaderparams()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },
      /**
       * 删除全局Header
       * @param index 全局Header下标
       */
      removeglobalheader(index) {
        this.$confirm('删除该全局Header？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.globalheaderList[index].id
          removeglobalheader(id).then(() => {
            this.$message.success('删除成功')
            this.getglobalheaderList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 全局Header是否唯一
       * @param 全局Header
       */
      isUniqueDetail(globalheader) {
        for (let i = 0; i < this.globalheaderList.length; i++) {
          if (this.globalheaderList[i].id !== globalheader.id) { // 排除自己
            if (this.globalheaderList[i].globalheadername === globalheader.globalheadername) {
              this.$message.error('全局Header名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
