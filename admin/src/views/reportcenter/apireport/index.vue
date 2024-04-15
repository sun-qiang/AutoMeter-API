<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('apireport:list')"
            @click.native.prevent="getapireportList"
          >刷新</el-button>
        </el-form-item>

        <span v-if="hasPermission('apireport:search')">
          <el-form-item label="测试集合" prop="testplanname" >
          <el-select v-model="search.testplanname" filterable placeholder="测试集合" @change="testplanselectChanged($event)">
            <el-option label="请选择"/>
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
            </div>
          </el-select>
        </el-form-item>
          <el-form-item label="执行计划" prop="batchname" >
            <el-select v-model="search.batchname" filterable placeholder="执行计划">
            <el-option label="请选择"/>
            <div v-for="(planbatch, index) in planbatchList" :key="index">
              <el-option :label="planbatch.batchname" :value="planbatch.batchname" />
            </div>
          </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="apireportList"
      :key="itemKey"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="编号" align="center" width="50">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="执行计划" align="center" prop="batchname" width="80"/>
      <el-table-column :show-overflow-tooltip="true" label="用例名" align="center" prop="casename" width="120"/>
      <el-table-column :show-overflow-tooltip="true" label="API" align="center" prop="apiname" width="80"/>
      <el-table-column label="请求方式" align="center" prop="requestmethod" width="80"/>

      <el-table-column label="状态" align="center" prop="status" width="50">
      <template slot-scope="scope">
        <span v-if="scope.row.status === '失败'" style="color:red">{{ scope.row.status }}</span>
        <span v-else style="color: #37B328">{{ scope.row.status }}</span>
      </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="微服务" align="center" prop="deployunitname" width="120"/>

      <el-table-column label="请求地址" align="center" prop="url" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.url }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="请求头" align="center" prop="requestheader" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.requestheader }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="请求数据" align="center" prop="requestdatas" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top-start">
            <p>{{ scope.row.requestdatas }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="响应" align="center" prop="respone" width="80">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <p>{{ scope.row.respone }}</p>
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium">...</el-tag>
          </div>
        </el-popover>
      </template>
      </el-table-column>

      <el-table-column label="断言" align="center" prop="expect" width="80">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>{{ scope.row.expect }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">...</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column label="断言结果" align="center" prop="assertvalue" width="80">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <p>{{ scope.row.assertvalue }}</p>
          <div slot="reference" class="name-wrapper">
            <el-tag size="medium" >...</el-tag>
          </div>
        </el-popover>
      </template>
      </el-table-column>

      <el-table-column label="运行时间(ms)" align="center" prop="runtime" width="100"/>
      <el-table-column label="异常信息" align="center" prop="errorinfo" width="80">
      <template slot-scope="scope">
        <el-popover trigger="hover" placement="top">
          <p>{{ scope.row.errorinfo }}</p>
          <div v-if="scope.row.errorinfo !== ''" slot="reference" class="name-wrapper">
            <el-tag size="medium" style="color:red">异常...</el-tag>
          </div>
          <div v-if="scope.row.errorinfo === ''" slot="reference" class="name-wrapper">
            <el-tag size="medium" style="color:green">...</el-tag>
          </div>
        </el-popover>
      </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
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
        :model="tmpapireport"
        ref="tmpapireport"
      >
        <el-form-item label="apireport名" prop="apireportname" required>
          <el-input
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpapireport.apireportname"
          />
        </el-form-item>
        <el-form-item label="访问方式" prop="visittype" required>
          <el-select v-model="tmpapireport.visittype" placeholder="访问方式">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(vistype, index) in visittypeList" :key="index">
              <el-option :label="vistype.dicitmevalue" :value="vistype.dicitmevalue" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="资源路径" prop="path" required>
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpapireport.path"
          />
        </el-form-item>
        <el-form-item label="微服务" prop="deployunitname" required >
          <el-select v-model="tmpapireport.deployunitname" placeholder="微服务" @change="selectChanged($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(depunitname, index) in deployunitList" :key="index">
              <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
            </div>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="memo">
          <el-input
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpapireport.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpapireport'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addapireport"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateapireport"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style>
  .el-table.cell.el-tooltip {
    white-space: pre-wrap;
  }
</style>
<script>
  import { search, addapireport, updateapireport, removeapireport } from '@/api/reportcenter/apireport'
  import { getdepunitList as getdepunitList } from '@/api/deployunit/depunit'
  import { getallexplanbytype as getallexplanbytype } from '@/api/executecenter/executeplan'
  import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '功能报告明细',
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
        tmptestplanname: '',
        tmptestplanid: null,
        tmpbatchname: null,
        visittypeList: [],
        apireportList: [], // api报告列表
        apiList: [], // api列表
        planbatchList: [], // 执行计划列表
        execplanList: [], // 计划列表
        deployunitList: [], // 微服务列表
        listLoading: false, // 数据加载等待动画
        dicvisitypeQuery: {
          page: 1, // 页码
          size: 30, // 每页数量
          diccode: 'httpvisittype' // 获取字典表入参
        },
        total: 0, // 数据总数
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true,
          testplanname: '',
          testplanid: null,
          batchname: null
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改apireport',
          update: '修改apireport',
          add: '添加apireport'
        },
        btnLoading: false, // 按钮等待动画
        tmpapireport: {
          executeplanid: '', // 计划id
          id: '',
          deployunitid: '',
          deployunitname: '',
          batchname: '',
          requestheader: '',
          requestdatas: '',
          visittype: '',
          path: '',
          url: '',
          requestmethod: '',
          memo: ''
        },
        tmpexecplantype: {
          usetype: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          testplanname: '',
          testplanid: null,
          batchname: null,
          projectid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.tmpexecplantype.projectid = window.localStorage.getItem('pid')
      this.getexecplanList()
      this.getdepunitList()
      this.getapireportList()
    },

    activated() {
      this.getexecplanList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 计划下拉选择事件获取微服务id  e的值为options的选值
       */
      testplanselectChanged(e) {
        this.search.batchname = null
        this.tmptestplanid = null
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.tmptestplanid = this.execplanList[i].id
            this.tmpapireport.executeplanid = this.execplanList[i].id
          }
        }
        getbatchbyplan(this.tmpapireport).then(response => {
          this.planbatchList = response.data
        }).catch(res => {
          this.$message.error('加载执行计划列表失败')
        })
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChanged(e) {
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.tmpapireport.deployunitid = this.deployunitList[i].id
          }
          console.log(this.deployunitList[i].id)
        }
      },

      /**
       * 获取api报告列表
       */
      getapireportList() {
        this.listLoading = true
        this.search.testplanname = this.tmptestplanname
        this.search.batchname = this.tmpbatchname
        search(this.search).then(response => {
          this.apireportList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载api报告列表失败')
        })
      },

      /**
       * 获取测试集合列表
       */
      getexecplanList() {
        this.tmpexecplantype.usetype = '功能'
        getallexplanbytype(this.tmpexecplantype).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载计划列表失败')
        })
      },

      /**
       * 获取微服务列表
       */
      getdepunitList() {
        getdepunitList(this.listQuery).then(response => {
          this.deployunitList = response.data.list
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      searchBy() {
        this.search.page = 1
        this.search.testplanid = this.tmptestplanid
        this.listLoading = true
        search(this.search).then(response => {
          this.itemKey = Math.random()
          this.apireportList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmptestplanname = this.search.testplanname
        this.tmpbatchname = this.search.batchname
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
        this.getapireportList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getapireportList()
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
       * 显示添加apireport对话框
       */
      showAddapireportDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpapireport.id = ''
        this.tmpapireport.deployunitid = ''
        this.tmpapireport.deployunitname = ''
        this.tmpapireport.apireportname = ''
        this.tmpapireport.visittype = ''
        this.tmpapireport.path = ''
        this.tmpapireport.memo = ''
      },
      /**
       * 添加apireport
       */
      addapireport() {
        this.$refs.tmpapireport.validate(valid => {
          if (valid && this.isUniqueDetail(this.tmpapireport)) {
            this.btnLoading = true
            addapireport(this.tmpapireport).then(() => {
              this.$message.success('添加成功')
              this.getapireportList()
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
       * 显示修改apireport对话框
       * @param index apireport下标
       */
      showUpdateapireportDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpapireport.id = this.apireportList[index].id
        this.tmpapireport.deployunitid = this.apireportList[index].deployunitid
        this.tmpapireport.deployunitname = this.apireportList[index].deployunitname
        this.tmpapireport.apireportname = this.apireportList[index].apireportname
        this.tmpapireport.visittype = this.apireportList[index].visittype
        this.tmpapireport.path = this.apireportList[index].path
        this.tmpapireport.memo = this.apireportList[index].memo
      },
      /**
       * 更新apireport
       */
      updateapireport() {
        if (this.isUniqueDetail(this.tmpapireport)) {
          updateapireport(this.tmpapireport).then(() => {
            this.$message.success('更新成功')
            this.getapireportList()
            this.dialogFormVisible = false
          }).catch(res => {
            this.$message.error('更新失败')
          })
        }
      },

      /**
       * 删除字典
       * @param index apireport下标
       */
      removeapireport(index) {
        this.$confirm('删除该apireport？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.apireportList[index].id
          removeapireport(id).then(() => {
            this.$message.success('删除成功')
            this.getapireportList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * apireport资料是否唯一
       * @param apireport
       */
      isUniqueDetail(apireport) {
        console.log(apireport.id)
        for (let i = 0; i < this.apireportList.length; i++) {
          if (this.apireportList[i].id !== apireport.id) { // 排除自己
            console.log(this.apireportList[i].id)
            if (this.apireportList[i].apireportname === apireport.apireportname) {
              this.$message.error('apireport名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
