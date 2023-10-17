<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('condition:list')"
            @click.native.prevent="getconditionList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('condition:add')"
            @click.native.prevent="showAddconditionDialog"
          >添加条件</el-button>
        </el-form-item>

        <span v-if="hasPermission('condition:search')">
          <el-form-item label="父条件名：">
            <el-input clearable v-model="search.conditionname" @keyup.enter.native="searchBy" placeholder="条件名"></el-input>
          </el-form-item>

          <el-form-item label="目标类型：">
          <el-select v-model="search.objecttype" placeholder="目标类型" clearable>
            <el-option label="请选择" value />
            <el-option label="测试集合" value="测试集合"></el-option>
            <el-option label="测试用例" value="测试用例"></el-option>
          </el-select>
         </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="conditionList"
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
      <el-table-column label="条件目标" align="center" prop="objectname" width="120"/>
      <el-table-column label="目标类型" align="center" prop="objecttype" width="100"/>
      <el-table-column label="条件类型" align="center" prop="conditiontype" width="100"/>
      <el-table-column :show-overflow-tooltip="true" label="备注" align="center" prop="memo" width="50"/>
      <el-table-column label="操作人" align="center" prop="creator" width="70"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('condition:update')  || hasPermission('condition:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('condition:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateconditionDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('condition:delete') && scope.row.id !== id"
            @click.native.prevent="removecondition(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('condition:update') && scope.row.id !== id"
            @click.native.prevent="showconditionorderDialog(scope.$index)"
          >子条件排序</el-button>
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
        :model="tmpcondition"
        ref="tmpcondition"
      >
        <el-form-item label="父条件名" prop="conditionname" required>
          <el-input
            type="text"
            maxlength="40"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpcondition.conditionname"
          />
        </el-form-item>

        <el-form-item label="目标类型" prop="objecttype" required >
          <el-select v-model="tmpcondition.objecttype" placeholder="目标类型" style="width:100%" @change="targetChanged($event)">
            <el-option label="测试用例" value="测试用例"/>
            <el-option label="测试集合" value="测试集合"></el-option>
<!--            <el-option label="调试用例" value="调试用例"></el-option>-->
          </el-select>
        </el-form-item>

        <div v-if="testcasevisible">
          <el-form-item label="微服务" prop="deployunitname" required >
            <el-select v-model="tmpcondition.deployunitname" filterable placeholder="微服务" style="width:100%" @change="selectChanged($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(depunitname, index) in deployunitList" :key="index">
                <el-option :label="depunitname.deployunitname" :value="depunitname.deployunitname" required/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="API" prop="apiname" required >
            <el-select v-model="tmpcondition.apiname" filterable placeholder="API"  style="width:100%" @change="apiselectChanged($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(apiname, index) in apiList" :key="index">
                <el-option :label="apiname.apiname" :value="apiname.apiname" required/>
              </div>
            </el-select>
          </el-form-item>
          <el-form-item label="用例" prop="objectname" required >
            <el-select v-model="tmpcondition.objectname" filterable placeholder="用例" style="width:100%"  @change="testcaseChanged($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(testcase, index) in caseList" :key="index">
                <el-option :label="testcase.casename" :value="testcase.casename" required/>
              </div>
            </el-select>
          </el-form-item>
        </div>

        <div v-if="executeplanVisible">
          <el-form-item label="测试集合" prop="objectname"  required>
            <el-select v-model="tmpcondition.objectname" filterable placeholder="测试集合" style="width:100%" @change="execplanChanged($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(plan, index) in execplanList" :key="index">
                <el-option :label="plan.executeplanname" :value="plan.executeplanname" />
              </div>
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="条件类型" prop="conditiontype" required >
          <el-select v-model="tmpcondition.conditiontype" placeholder="条件类型" style="width:100%">
            <el-option label="前置条件" value="前置条件"/>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="memo" >
          <el-input
            type="text"
            maxlength="100"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpcondition.memo"
          />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpcondition'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addcondition"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatecondition"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="子条件顺序" :visible.sync="ConditionOrderdialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:30px;"
      >
        <el-table
          :data="conditionorderList"
          border
          fit
          highlight-current-row
          style="width: 100%" >
          <el-table-column label="编号" align="center" width="50">
            <template slot-scope="scope">
              <span v-text="getIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column prop="conditionname" align="center" label="父条件" width="150px"></el-table-column>
          <el-table-column prop="subconditionname" align="center" label="子条件"  width="80px"></el-table-column>
          <el-table-column prop="subconditiontype" align="center" label="条件类型"  width="70px"></el-table-column>
          <el-table-column prop="orderstatus" align="center" label="状态"  width="60px"></el-table-column>
          <el-table-column prop="conditionorder" align="center" label="顺序"  width="50px"></el-table-column>

          <el-table-column label="操作排序" align="center" width="140px" >
            <template slot-scope="scope">
              <el-button
                size="mini"
                :disabled="scope.$index===0"
                @click="moveUp(scope.$index,scope.row)"><i class="el-icon-arrow-up"></i></el-button>
              <el-button
                size="mini"
                :disabled="scope.$index===(conditionorderList.length-1)"
                @click="moveDown(scope.$index,scope.row)"><i class="el-icon-arrow-down"></i></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="ConditionOrderdialogFormVisible = false">取消</el-button>
        <el-button
          type="success"
          @click.native.prevent="saveconditionorder"
        >保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addcondition, updatecondition, removecondition } from '@/api/condition/condition'
  import { searchconditionorder, addconditionorder } from '@/api/condition/conditionorder'
  import { getallexplan as getallexplan } from '@/api/executecenter/executeplan'
  import { findcasesbyname as findcasesbyname } from '@/api/assets/apicases'
  import { getapiListbydeploy as getapiListbydeploy } from '@/api/deployunit/api'
  import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '父条件管理',
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
        tmpobjecttype: '',
        execplanList: [], // 计划列表
        conditionList: [], // 条件服务器列表
        conditionorderList: [], // 条件顺序显示列表
        saveconditionorderList: [], // 条件顺序保存列表
        apiList: [], // api列表
        caseList: [], // 用例列表
        deployunitList: [], // 微服务列表
        enviromentnameList: [], // 环境列表
        machinenameList: [], // 服务器列表
        testcasevisible: false,
        executeplanVisible: false,
        ConditionOrderdialogFormVisible: false,
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        deployunitquery: {
          deployunitid: '',
          modelid: 0,
          deployunitname: ''
        },
        apiquery: {
          apiid: '',
          deployunitid: '',
          casedeployunitname: '',
          caseapiname: ''
        },
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改条件',
          update: '修改条件',
          add: '添加条件'
        },
        btnLoading: false, // 按钮等待动画
        tmpcondition: {
          id: '',
          conditionname: '',
          objecttype: '',
          objectid: '',
          objectname: '',
          apiid: '',
          deployunitid: '',
          conditiontype: '',
          memo: '',
          creator: '',
          deployunitname: '',
          apiname: '',
          projectid: ''
        },
        tmpconditionorder: {
          id: '',
          conditionid: '',
          subconditionid: '',
          conditionname: '',
          subconditionname: '',
          subconditiontype: '',
          conditionorder: '',
          orderstatus: '',
          creator: ''
        },
        search: {
          page: 1,
          size: 10,
          conditionname: null,
          objecttype: null,
          projectid: ''
        }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.getconditionList()
      this.getdepunitLists()
    },

    activated() {
      this.getdepunitLists()
    },

    methods: {
      unix2CurrentTime,

      // 上移
      moveUp(index, row) {
        var that = this
        if (index > 0) {
          const upDate = that.conditionorderList[index - 1]
          that.conditionorderList.splice(index - 1, 1)
          that.conditionorderList.splice(index, 0, upDate)
        } else {
          alert('已经是第一条，不可上移')
        }
        console.log(that.conditionorderList)
      },

      // 下移
      moveDown(index, row) {
        var that = this
        console.log('下移', index, row)
        if ((index + 1) === that.conditionorderList.length) {
          alert('已经是最后一条，不可下移')
        } else {
          console.log(index)
          const downDate = that.conditionorderList[index + 1]
          that.conditionorderList.splice(index + 1, 1)
          that.conditionorderList.splice(index, 0, downDate)
        }
      },
      /**
       * 条件目标下拉选择  e的值为options的选值
       */
      targetChanged(e) {
        if (e === '测试集合') {
          this.executeplanVisible = true
          this.testcasevisible = false
          this.tmpcondition.objectid = ''
          this.tmpcondition.objectname = ''
          this.tmpcondition.apiid = 0
          this.tmpcondition.deployunitid = 0
          this.getallexplan()
        }
        if (e === '调试用例') {
          this.executeplanVisible = false
          this.testcasevisible = false
          this.tmpcondition.objectid = 0
          this.tmpcondition.objectname = '调试用例'
          this.tmpcondition.apiid = 0
          this.tmpcondition.deployunitid = 0
        }
        if (e === '测试用例') {
          this.executeplanVisible = false
          this.testcasevisible = true
          this.tmpcondition.objectid = ''
          this.tmpcondition.objectname = ''
          this.tmpcondition.deployunitname = ''
          this.tmpcondition.deployunitname = ''
          this.tmpcondition.apiname = ''
        }
      },

      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      selectChanged(e) {
        this.tmpcondition.apiname = ''
        for (let i = 0; i < this.deployunitList.length; i++) {
          if (this.deployunitList[i].deployunitname === e) {
            this.deployunitquery.deployunitid = this.deployunitList[i].id
            this.apiquery.deployunitid = this.deployunitList[i].id
            this.tmpcondition.deployunitid = this.deployunitList[i].id
          }
        }
        this.deployunitquery.deployunitname = e
        this.tmpcondition.apiname = ''
        this.tmpcondition.objectname = ''
        getapiListbydeploy(this.deployunitquery).then(response => {
          this.apiList = response.data
        }).catch(res => {
          this.$message.error('加载api列表失败')
        })
      },

      /**
       * API下拉选择事件获取用例  e的值为options的选值
       */
      apiselectChanged(e) {
        this.tmpcondition.objectname = ''
        for (let i = 0; i < this.apiList.length; i++) {
          if (this.apiList[i].apiname === e) {
            this.apiquery.apiid = this.apiList[i].id
            this.tmpcondition.apiid = this.apiList[i].id
          }
        }
        this.apiquery.caseapiname = e
        this.apiquery.casedeployunitname = this.deployunitquery.deployunitname
        findcasesbyname(this.apiquery).then(response => {
          this.caseList = response.data
        }).catch(res => {
          this.$message.error('加载api用例列表失败')
        })
      },

      /**
       * 测试集合下拉选择获取id  e的值为options的选值
       */
      execplanChanged(e) {
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.tmpcondition.objectid = this.execplanList[i].id
            this.tmpcondition.objectname = this.execplanList[i].executeplanname
          }
        }
      },

      /**
       * 用例下拉选择获取id  e的值为options的选值
       */
      testcaseChanged(e) {
        for (let i = 0; i < this.caseList.length; i++) {
          if (this.caseList[i].casename === e) {
            this.tmpcondition.objectid = this.caseList[i].id
          }
        }
      },

      /**
       * 获取微服务列表
       */
      getdepunitLists() {
        this.listLoading = true
        getdepunitLists(this.search).then(response => {
          this.deployunitList = response.data
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      getallexplan() {
        getallexplan(this.search).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载测试集合列表失败')
        })
      },
      /**
       * 获取条件列表
       */
      getconditionList() {
        this.listLoading = true
        this.search.enviromentname = this.tmpenviromentname
        this.search.objecttype = this.tmpobjecttype
        search(this.search).then(response => {
          this.conditionList = response.data.list
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
          this.conditionList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpconditionname = this.search.conditionname
        this.tmpobjecttype = this.search.objecttype
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
        this.getconditionList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getconditionList()
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
       * 显示添加条件对话框
       */
      showAddconditionDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.testcasevisible = false
        this.dialogStatus = 'add'
        this.tmpcondition.id = ''
        this.tmpcondition.conditionname = ''
        this.tmpcondition.objectid = ''
        this.tmpcondition.objectname = ''
        this.tmpcondition.deployunitname = ''
        this.tmpcondition.apiname = ''
        this.tmpcondition.objecttype = ''
        this.tmpcondition.conditiontype = ''
        this.tmpcondition.memo = ''
        this.tmpcondition.creator = this.name
        this.tmpcondition.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加条件
       */
      addcondition() {
        this.$refs.tmpcondition.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addcondition(this.tmpcondition).then(() => {
              this.$message.success('添加成功')
              this.getconditionList()
              this.dialogFormVisible = false
              this.btnLoading = false
              this.testcasevisible = false
              this.tmpcondition.id = ''
              this.tmpcondition.conditionname = ''
              this.tmpcondition.objectid = ''
              this.tmpcondition.objectname = ''
              this.tmpcondition.objecttype = ''
              this.tmpcondition.deployunitname = ''
              this.tmpcondition.apiname = ''
              this.tmpcondition.memo = ''
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },
      /**
       * 添加条件顺序
       */
      saveconditionorder() {
        this.saveconditionorderList = []
        for (let i = 0; i < this.conditionorderList.length; i++) {
          this.saveconditionorderList.push({
            'conditionid': this.conditionorderList[i].conditionid,
            'subconditionid': this.conditionorderList[i].subconditionid,
            'subconditiontype': this.conditionorderList[i].subconditiontype,
            'orderstatus': '已排序',
            'subconditionname': this.conditionorderList[i].subconditionname,
            'conditionname': this.conditionorderList[i].conditionname,
            'conditionorder': i + 1,
            'creator': this.name
          })
        }
        console.log(this.saveconditionorderList)
        addconditionorder(this.saveconditionorderList).then(() => {
          this.$message.success('条件顺序保存成功')
        }).catch(res => {
          this.$message.error('条件顺序保存失败')
        })
        this.ConditionOrderdialogFormVisible = false
      },

      /**
       * 显示修改条件对话框
       * @param index 条件下标
       */
      showUpdateconditionDialog(index) {
        this.getallexplan()
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        if (this.conditionList[index].objecttype === '测试集合') {
          this.executeplanVisible = true
          this.testcasevisible = false
        }
        if (this.conditionList[index].objecttype === '测试用例') {
          this.testcasevisible = true
          this.executeplanVisible = false
        }
        this.tmpcondition.id = this.conditionList[index].id
        this.tmpcondition.objectid = this.conditionList[index].objectid
        this.tmpcondition.conditionname = this.conditionList[index].conditionname
        this.tmpcondition.apiname = this.conditionList[index].apiname
        this.tmpcondition.deployunitname = this.conditionList[index].deployunitname
        this.tmpcondition.objectname = this.conditionList[index].objectname
        this.tmpcondition.objecttype = this.conditionList[index].objecttype
        this.tmpcondition.conditiontype = this.conditionList[index].conditiontype
        this.tmpcondition.memo = this.conditionList[index].memo
        this.tmpcondition.creator = this.name
      },

      showconditionorderDialog(index) {
        this.ConditionOrderdialogFormVisible = true
        this.tmpconditionorder.conditionid = this.conditionList[index].id
        this.searchConditionorder()
      },

      searchConditionorder() {
        searchconditionorder(this.tmpconditionorder).then(response => {
          this.conditionorderList = response.data
        }).catch(res => {
          this.$message.error('查询条件顺序失败')
        })
      },

      /**
       * 更新条件
       */
      updatecondition() {
        this.$refs.tmpcondition.validate(valid => {
          if (valid) {
            updatecondition(this.tmpcondition).then(() => {
              this.$message.success('更新成功')
              this.getconditionList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除条件
       * @param index 条件下标
       */
      removecondition(index) {
        this.$confirm('删除该条件？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.conditionList[index].id
          removecondition(id).then(() => {
            this.$message.success('删除成功')
            this.getconditionList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 条件是否唯一
       * @param 条件
       */
      isUniqueDetail(condition) {
        for (let i = 0; i < this.conditionList.length; i++) {
          if (this.conditionList[i].id !== condition.id) { // 排除自己
            if (this.conditionList[i].enviromentname === condition.enviromentname) {
              if (this.conditionList[i].machinename === condition.machinename) {
                this.$message.error('条件名已存在')
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
