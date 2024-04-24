<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('depunit:list')"
            @click.native.prevent="getdepunitList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('depunit:add')"
            @click.native.prevent="showAdddepunitDialog"
          >添加服务</el-button>
        </el-form-item>

        <span v-if="hasPermission('depunit:search')">
          <el-form-item label="服务名:">
            <el-input clearable v-model="search.deployunitname" clearable @keyup.enter.native="searchBy" placeholder="服务名"></el-input>
          </el-form-item>
          <el-form-item label="协议:">
            <el-input clearable v-model="search.protocal" clearable @keyup.enter.native="searchBy" placeholder="协议"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="depunitList"
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
      <el-table-column label="服务名" align="center" prop="deployunitname" width="120"/>
      <el-table-column label="协议" align="center" prop="protocal" width="50"/>
      <el-table-column label="访问端口" align="center" prop="port" width="80"/>
      <el-table-column label="基础路径" align="center" prop="baseurl" width="100"/>
      <el-table-column label="API数" align="center" prop="apicounts" sortable width="80"/>
      <el-table-column label="描述" align="center" prop="memo" width="100"/>
      <el-table-column label="维护人" align="center" prop="creator" width="80"/>
      <el-table-column label="创建时间" align="center" prop="createTime" sortable width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('depunit:update')  || hasPermission('depunit:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('depunit:update') && scope.row.id !== id"
            @click.native.prevent="showUpdatedepunitDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('depunit:delete') && scope.row.id !== id"
            @click.native.prevent="removedepunit(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('depunit:delete') && scope.row.id !== id"
            @click.native.prevent="showmodelsDialog(scope.$index)"
          >模块</el-button>
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
        :model="tmpdepunit"
        ref="tmpdepunit"
      >
        <el-form-item label="服务名" prop="deployunitname" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpdepunit.deployunitname"
          />
        </el-form-item>
        <el-form-item label="协议" prop="protocal" required>
          <el-select v-model="tmpdepunit.protocal" style="width:100%" placeholder="协议">
            <el-option label="http" value="http"></el-option>
            <el-option label="https" value="https"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="端口" prop="port" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdepunit.port"
          />
        </el-form-item>

        <el-form-item label="基础路径" prop="baseurl" >
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdepunit.baseurl"
          />
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            maxlength="300"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpdepunit.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpdepunit'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="adddepunit"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatedepunit"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="模块管理" :visible.sync="ModelsFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click.native.prevent="showAddModelsDialog"
            >添加模块</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="modelsList"
        :key="itemmodelKey"
        v-loading.body="listLoading"
        element-loading-text="loading"
        border
        fit
        highlight-current-row
      >
        <el-table-column label="编号" align="center" width="45">
          <template slot-scope="scope">
            <span v-text="modelgetIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column label="模块名" align="center" prop="modelname" width="280"/>
        <el-table-column label="管理" align="center">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              @click.native.prevent="showUpdatedepunitmodelDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              @click.native.prevent="removedeployunitmodel(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="modeltextMap[modeldialogStatus]" :visible.sync="modeldialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpmodel"
        ref="tmpmodel"
      >
        <el-form-item label="模块名" prop="modelname" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpmodel.modelname"
          />
        </el-form-item>
        <el-form-item label="备注" prop="memo" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpmodel.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="modeldialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="modeldialogStatus === 'add'"
          @click.native.prevent="$refs['tmpmodel'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="modeldialogStatus === 'add'"
          @click.native.prevent="adddeployunitmodel"
        >添加</el-button>
        <el-button
          type="success"
          v-if="modeldialogStatus === 'update'"
          @click.native.prevent="updatedeployunitmodel"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, adddepunit, updatedepunit, removedepunit } from '@/api/deployunit/depunit'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'
  import { searchdeployunitmodel, adddeployunitmodel, updatedeployunitmodel, removedeployunitmodel } from '@/api/deployunit/depunitmodel'

  export default {
    name: '微服务',
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
      const validateDeployName = (rule, value, callback) => {
        if (value.length < 3) {
          callback(new Error('账户名长度必须 ≥ 3'))
        } else {
          callback()
        }
      }
      return {
        id: null,
        itemKey: null,
        itemmodelKey: null,
        tmpdeployunitname: null,
        tmpprotocal: null,
        depunitList: [], // 服务列表
        modelsList: [], // 服务模块列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        dialogStatus: 'add',
        modeldialogStatus: 'add',
        dialogFormVisible: false,
        ModelsFormVisible: false,
        modeldialogFormVisible: false,
        textMap: {
          updateRole: '修改服务',
          update: '修改服务',
          add: '添加服务'
        },
        modeltextMap: {
          updateRole: '修改模块',
          update: '修改模块',
          add: '添加模块'
        },
        btnLoading: false, // 按钮等待动画
        tmpdepunit: {
          id: '',
          deployunitname: '',
          protocal: '',
          port: '',
          baseurl: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        tmpdeploymodel: {
          page: 1,
          size: 10,
          deployunitid: ''
        },
        tmpmodel: {
          id: '',
          deployunitid: '',
          modelname: '',
          memo: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          deployunitname: null,
          protocal: null,
          accountId: this.accountId,
          creator: this.name,
          projectid: ''
        },
        createRules: {
          email: [{ required: true, trigger: 'blur', validator: validateDeployName }]
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
    },

    created() {
      console.log(111111111111111)
      console.log(this.accountId)
      this.search.accountId = this.accountId
      this.getdepunitList()
      this.search.projectid = window.localStorage.getItem('pid')
      console.log(window.localStorage.getItem('pid'))
    },

    activated() {
      this.getdepunitList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取服务列表
       */
      getdepunitList() {
        this.search.deployunitname = this.tmpdeployunitname
        this.search.protocal = this.tmpprotocal
        this.search.creator = this.name
        this.listLoading = true
        search(this.search).then(response => {
          this.depunitList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载服务列表失败')
        })
      },

      searchdeployunitmodel() {
        searchdeployunitmodel(this.tmpdeploymodel).then(response => {
          this.modelsList = response.data.list
        }).catch(res => {
          this.$message.error('加载服务模块列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.depunitList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpdeployunitname = this.search.deployunitname
        this.tmpprotocal = this.search.protocal
        this.listLoading = false
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.search.page = 1
        this.search.size = size
        this.getdepunitList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getdepunitList()
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

      modelgetIndex(index) {
        return (this.tmpdeploymodel.page - 1) * this.tmpdeploymodel.size + index + 1
      },
      /**
       * 显示添加服务对话框
       */
      showAdddepunitDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpdepunit.id = ''
        this.tmpdepunit.deployunitname = ''
        this.tmpdepunit.protocal = ''
        this.tmpdepunit.port = ''
        this.tmpdepunit.baseurl = ''
        this.tmpdepunit.memo = ''
        this.tmpdepunit.creator = this.name
        this.tmpdepunit.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加服务
       */
      adddepunit() {
        this.$refs.tmpdepunit.validate(valid => {
          if (valid) {
            this.btnLoading = true
            adddepunit(this.tmpdepunit).then(() => {
              this.$message.success('添加成功')
              this.getdepunitList()
              this.dialogFormVisible = false
              this.btnLoading = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      adddeployunitmodel() {
        this.$refs.tmpmodel.validate(valid => {
          if (valid) {
            this.btnLoading = true
            adddeployunitmodel(this.tmpmodel).then(() => {
              this.$message.success('添加成功')
              this.searchdeployunitmodel()
              this.modeldialogFormVisible = false
              this.btnLoading = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },
      /**
       * 显示修改服务对话框
       * @param index 服务下标
       */
      showUpdatedepunitDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpdepunit.id = this.depunitList[index].id
        this.tmpdepunit.deployunitname = this.depunitList[index].deployunitname
        this.tmpdepunit.protocal = this.depunitList[index].protocal
        this.tmpdepunit.port = this.depunitList[index].port
        this.tmpdepunit.baseurl = this.depunitList[index].baseurl
        this.tmpdepunit.memo = this.depunitList[index].memo
        this.tmpdepunit.creator = this.name
        this.tmpdepunit.projectid = window.localStorage.getItem('pid')
      },

      showUpdatedepunitmodelDialog(index) {
        this.modeldialogFormVisible = true
        this.modeldialogStatus = 'update'
        this.tmpmodel.id = this.modelsList[index].id
        this.tmpmodel.modelname = this.modelsList[index].modelname
        this.tmpmodel.memo = this.modelsList[index].memo
        this.tmpmodel.creator = this.name
      },

      showmodelsDialog(index) {
        // 显示新增对话框
        this.ModelsFormVisible = true
        this.tmpdeploymodel.deployunitid = this.depunitList[index].id
        this.searchdeployunitmodel()
      },

      showAddModelsDialog(index) {
        // 显示新增对话框
        this.modeldialogStatus = 'add'
        this.modeldialogFormVisible = true
        this.tmpmodel.id = ''
        this.tmpmodel.deployunitid = this.tmpdeploymodel.deployunitid
        this.tmpmodel.modelname = ''
        this.tmpmodel.memo = ''
        this.tmpmodel.creator = this.name
      },
      /**
       * 更新服务
       */
      updatedepunit() {
        this.$refs.tmpdepunit.validate(valid => {
          if (valid) {
            updatedepunit(this.tmpdepunit).then(() => {
              this.$message.success('更新成功')
              this.getdepunitList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
              this.btnLoading = false
            })
          }
        })
      },

      updatedeployunitmodel() {
        this.$refs.tmpmodel.validate(valid => {
          if (valid) {
            updatedeployunitmodel(this.tmpmodel).then(() => {
              this.$message.success('更新成功')
              this.searchdeployunitmodel()
              this.modeldialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
              this.btnLoading = false
            })
          }
        })
      },

      /**
       * 删除服务
       * @param index 服务下标
       */
      removedepunit(index) {
        this.$confirm('删除该服务？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.depunitList[index].id
          removedepunit(id).then(() => {
            this.$message.success('删除成功')
            this.getdepunitList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      removedeployunitmodel(index) {
        this.$confirm('删除该模块？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.modelsList[index].id
          removedeployunitmodel(id).then(() => {
            this.$message.success('删除成功')
            this.searchdeployunitmodel()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },
      /**
       * 服务资料是否唯一
       * @param 服务
       */
      isUniqueDetail(depunit) {
        for (let i = 0; i < this.depunitList.length; i++) {
          if (this.depunitList[i].id !== depunit.id) { // 排除自己
            if (this.depunitList[i].deployunitname === depunit.deployunitname) {
              this.$message.error('服务名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
