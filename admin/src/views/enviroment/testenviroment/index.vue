<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('enviroment:list')"
            @click.native.prevent="getenviromentList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('enviroment:add')"
            @click.native.prevent="showAddenviromentDialog"
          >添加测试环境</el-button>
        </el-form-item>

        <span v-if="hasPermission('enviroment:search')">
          <el-form-item>
            <el-input clearable maxlength="40" v-model="search.enviromentname" @keyup.enter.native="searchBy" placeholder="测试环境名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy"  :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="enviromentList"
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
      <el-table-column label="测试环境名" align="center" prop="enviromentname" width="150"/>
      <el-table-column label="环境类型" align="center" prop="envtype" width="80"/>
      <el-table-column label="描述" align="center" prop="memo" width="150"/>
      <el-table-column label="操作人" align="center" prop="creator" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center" width="360"
                       v-if="hasPermission('enviroment:update')  || hasPermission('enviroment:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('enviroment:delete') && scope.row.id !== id"
            @click.native.prevent="removeenviroment(scope.$index)"
          >删除</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showdeployserviceDialog(scope.$index)"
          >部署微服务</el-button>
          <el-button
            type="primary"
            size="mini"
            v-if="hasPermission('enviroment:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateenviromentDialog(scope.$index)"
          >部署中间件</el-button>
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
        :model="tmpenviroment"
        ref="tmpenviroment"
      >
        <el-form-item label="测试环境名" prop="enviromentname" required>
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            v-model="tmpenviroment.enviromentname"
          />
        </el-form-item>

        <el-form-item label="环境类型" prop="envtype" required >
          <el-select v-model="tmpenviroment.envtype" placeholder="环境类型" style="width:100%">
            <el-option label="功能" value="功能"></el-option>
            <el-option label="性能" value="性能"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="备注" prop="memo">
          <el-input
            maxlength="60"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            v-model="tmpenviroment.memo"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpenviroment'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addenviroment"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateenviroment"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="deployservicetextMap[deployservicedialogStatus]" :visible.sync="adddeployservicedialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpmacdepunit"
        ref="tmpmacdepunit"
      >

        <el-form-item label="服务器" prop="machinename" required >
          <el-select v-model="tmpmacdepunit.machinename" filterable placeholder="服务器" style="width:100%" @change="selectChangedMN($event)">
            <el-option label="请选择" value="''" style="display: none" />
            <div v-for="(macname, index) in machinenameList" :key="index">
              <el-option :label="`${macname.machinename} ：${macname.ip}`" :value="macname.machinename" required/>
            </div>
          </el-select>
        </el-form-item>

        <el-form-item label="组件类型" prop="assembletype" required >
          <el-select v-model="tmpmacdepunit.assembletype" placeholder="组件类型" style="width:100%" @change="selectChangedAandD($event)">
            <el-option label="组件" value="组件"></el-option>
            <el-option label="微服务" value="微服务"></el-option>
          </el-select>
        </el-form-item>

        <div v-if="deployunitVisible">
          <el-form-item label="微服务" prop="deployunitname" required >
            <el-select v-model="tmpmacdepunit.deployunitname" filterable placeholder="微服务" style="width:100%" @change="selectChangedDU($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(depunit, index) in deployUnitList" :key="index">
                <el-option :label="depunit.deployunitname" :value="depunit.deployunitname" required/>
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="访问方式" prop="visittype" required >
            <el-select v-model="tmpmacdepunit.visittype" placeholder="访问方式" style="width:100%" @change="selectChangedVisittype($event)">
              <el-option label="ip" value="ip"></el-option>
              <el-option label="域名" value="域名"></el-option>
            </el-select>
          </el-form-item>

          <div v-if="domianVisible">
            <el-form-item label="访问域名" prop="domain" required>
              <el-input v-model="tmpmacdepunit.domain"  placeholder="访问域名" required></el-input>
            </el-form-item>
          </div>
        </div>

        <div v-if="assembleVisible">
          <el-form-item label="组件" prop="deployunitname" required >
            <el-select v-model="tmpmacdepunit.deployunitname" filterable placeholder="组件" style="width:100%" @change="selectChangedAS($event)">
              <el-option label="请选择" value="''" style="display: none" />
              <div v-for="(assemble, index) in assembleList" :key="index">
                <el-option :label="assemble.assemblename" :value="assemble.assemblename" required/>
              </div>
            </el-select>
          </el-form-item>

          <el-form-item label="访问方式" prop="visittype" required >
            <el-select v-model="tmpmacdepunit.visittype" placeholder="访问方式" style="width:100%" @change="selectChangedVisittype($event)">
              <el-option label="ip" value="ip"></el-option>
              <el-option label="域名" value="域名"></el-option>
            </el-select>
          </el-form-item>

          <div v-if="domianVisible">
            <el-form-item label="访问域名" prop="domain" required>
              <el-input v-model="tmpmacdepunit.domain" maxlength="110" placeholder="访问域名" required></el-input>
            </el-form-item>
          </div>
        </div>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpmacdepunit'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addmacdepunit"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updatemacdepunit"
        >修改</el-button>
      </div>
    </el-dialog>

    <el-dialog title="微服务部署" width="1000px" :visible.sync="ServiceDeployFormVisible">
      <div class="filter-container">
        <el-form :inline="true">
          <el-form-item>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              v-if="hasPermission('enviroment:add')"
              @click.native.prevent="showadddeployserviceDialog"
            >部署微服务</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="macdepunitList"
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
        <el-table-column label="微服务,组件名" align="center" prop="deployunitname" width="120"/>
        <el-table-column label="组件类型" align="center" prop="assembletype" width="70"/>
        <el-table-column label="访问方式" align="center" prop="visittype" width="70"/>
        <el-table-column label="访问域名" align="center" prop="domain" width="150"/>
        <el-table-column label="操作人" align="center" prop="creator" width="60"/>
        <el-table-column label="创建时间" align="center" prop="createTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
          <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
          </template>
        </el-table-column>

        <el-table-column label="管理" align="center"
                         v-if="hasPermission('macdepunit:update')  || hasPermission('macdepunit:delete')">
          <template slot-scope="scope">
            <el-button
              type="warning"
              size="mini"
              v-if="hasPermission('macdepunit:update') && scope.row.id !== id"
              @click.native.prevent="showUpdatemacdepunitDialog(scope.$index)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              v-if="hasPermission('macdepunit:delete') && scope.row.id !== id"
              @click.native.prevent="removemacdepunit(scope.$index)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="servicehandleSizeChange"
        @current-change="servicehandleCurrentChange"
        :current-page="servicesearch.page"
        :page-size="servicesearch.size"
        :total="servicetotal"
        :page-sizes="[10, 20, 30, 40]"
        layout="total, sizes, prev, pager, next, jumper"
      ></el-pagination>
    </el-dialog>


  </div>
</template>
<script>
  import { search, addenviroment, updateenviroment, removeenviroment } from '@/api/enviroment/testenviroment'
  import { unix2CurrentTime } from '@/utils'
  import { getdepunitLists as getdepunitLists } from '@/api/deployunit/depunit'
  import { mapGetters } from 'vuex'
  import { search as searchmacdep, addmacdepunit, updatemacdepunit, removemacdepunit } from '@/api/enviroment/macdepunit'
  import { getmachineLists as getmachineLists } from '@/api/assets/machine'

  export default {
    name: '测试环境',
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
        tmpserviceenviromentid: '',
        tmpservicedeployunitid: '',
        tmpserviceenviromentname: '',
        tmpservicedeployunitname: '',
        tmpenviromentname: '',
        enviromentList: [], // 环境列表
        machinenameList: [], // 服务器列表
        macdepunitList: [],
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        servicetotal: 0,
        dialogStatus: 'add',
        deployservicedialogStatus: 'add',
        deployunitVisible: false,
        assembleVisible: false,
        domianVisible: false,
        dialogFormVisible: false,
        adddeployservicedialogFormVisible: false,
        ServiceDeployFormVisible: false,
        textMap: {
          updateRole: '修改环境',
          update: '修改环境',
          add: '添加环境'
        },
        deployservicetextMap: {
          updateRole: '修改微服务部署',
          update: '修改微服务部署',
          add: '添加微服务部署'
        },
        btnLoading: false, // 按钮等待动画
        tmpenviroment: {
          id: '',
          enviromentname: '',
          envtype: '',
          memo: '',
          creator: '',
          projectid: ''
        },
        tmpmacdepunit: {
          id: '',
          envid: '',
          machineid: '',
          depunitid: '',
          assembleid: '',
          assembletype: '',
          enviromentname: '',
          machinename: '',
          deployunitname: '',
          domain: '',
          visittype: '',
          creator: '',
          projectid: ''
        },
        search: {
          page: 1,
          size: 10,
          enviromentname: null,
          creator: '',
          projectid: ''
        },
        servicesearch: {
          page: 1,
          size: 10,
          enviromentname: null,
          depunitid: null,
          envid: null,
          deployunitname: null,
          projectid: ''
        },
        searchproject: {
          projectid: ''
        }
      }
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.searchproject.projectid = window.localStorage.getItem('pid')
      this.servicesearch.projectid = window.localStorage.getItem('pid')
      this.getenviromentList()
      this.getmachineLists()
      this.getdepunitLists()
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    methods: {
      unix2CurrentTime,

      getdepunitLists() {
        getdepunitLists(this.searchproject).then(response => {
          this.deployUnitList = response.data
          console.log(this.deployunitList)
        }).catch(res => {
          this.$message.error('加载微服务列表失败')
        })
      },

      selectChangedVisittype(e) {
        if (e === '域名') {
          this.domianVisible = true
          this.tmpmacdepunit.domain = ''
        }
        if (e === 'ip') {
          this.domianVisible = false
          this.tmpmacdepunit.domain = '/'
        }
      },

      selectChangedAandD(e) {
        if (e === '组件') {
          this.deployunitVisible = false
          this.assembleVisible = true
        }
        if (e === '微服务') {
          this.assembleVisible = false
          this.deployunitVisible = true
          this.domianVisible = false
        }
        this.tmpmacdepunit.deployunitname = ''
        this.tmpmacdepunit.assembleid = ''
        this.tmpmacdepunit.depunitid = ''
        this.tmpmacdepunit.visittype = ''
        this.tmpmacdepunit.domain = ''
      },

      selectChangedMN(e) {
        for (let i = 0; i < this.machinenameList.length; i++) {
          if (this.machinenameList[i].machinename === e) {
            this.tmpmacdepunit.machineid = this.machinenameList[i].id
          }
          console.log(this.machinenameList[i].id)
        }
      },

      selectChangedDU(e) {
        for (let i = 0; i < this.deployUnitList.length; i++) {
          if (this.deployUnitList[i].deployunitname === e) {
            this.tmpmacdepunit.depunitid = this.deployUnitList[i].id
          }
        }
        this.tmpmacdepunit.assembleid = ''
      },

      selectChangedAS(e) {
        for (let i = 0; i < this.assembleList.length; i++) {
          if (this.assembleList[i].assemblename === e) {
            this.tmpmacdepunit.assembleid = this.assembleList[i].id
          }
        }
        this.tmpmacdepunit.depunitid = ''
      },

      getmachineLists() {
        getmachineLists(this.searchproject).then(response => {
          this.machinenameList = response.data
        }).catch(res => {
          this.$message.error('加载服务器列表失败')
        })
      },

      removemacdepunit(index) {
        this.$confirm('删除该测试环境？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.macdepunitList[index].id
          removemacdepunit(id).then(() => {
            this.$message.success('删除成功')
            this.getmacdepunitList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      updatemacdepunit() {
        this.$refs.tmpmacdepunit.validate(valid => {
          if (valid) {
            if (this.tmpmacdepunit.visittype === 'ip') {
              this.tmpmacdepunit.domain = ''
            }
            if (this.tmpmacdepunit.assembletype === '组件') {
              // this.tmpmacdepunit.visittype = ''
              this.tmpmacdepunit.depunitid = ''
              // this.tmpmacdepunit.domain = ''
            }
            if (this.tmpmacdepunit.assembletype === '微服务') {
              this.tmpmacdepunit.assembleid = ''
            }
            updatemacdepunit(this.tmpmacdepunit).then(() => {
              this.$message.success('更新成功')
              this.getmacdepunitList()
              this.adddeployservicedialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      addmacdepunit() {
        this.$refs.tmpmacdepunit.validate(valid => {
          if (valid) {
            addmacdepunit(this.tmpmacdepunit).then(() => {
              this.$message.success('添加成功')
              this.getmacdepunitList()
              this.adddeployservicedialogFormVisible = false
            }).catch(res => {
              this.$message.error('添加失败')
              this.btnLoading = false
            })
          }
        })
      },

      getmacdepunitList() {
        this.servicesearch.enviromentname = this.tmpserviceenviromentname
        this.servicesearch.deployunitname = this.tmpservicedeployunitname
        this.servicesearch.envid = this.tmpserviceenviromentid
        this.servicesearch.depunitid = this.tmpservicedeployunitid
        searchmacdep(this.servicesearch).then(response => {
          this.macdepunitList = response.data.list
          this.servicetotal = response.data.total
        }).catch(res => {
          this.$message.error('加载环境部署列表失败')
        })
      },

      searchserviceBy() {
        this.servicesearch.page = 1
        searchmacdep(this.search).then(response => {
          this.itemKey = Math.random()
          this.macdepunitList = response.data.list
          this.servicetotalt = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpserviceenviromentname = this.servicesearch.enviromentname
        this.tmpservicedeployunitname = this.servicesearch.deployunitname
        this.tmpserviceenviromentid = this.servicesearch.envid
        this.tmpservicedeployunitid = this.servicesearch.depunitid
      },
      /**
       * 获取环境列表
       */
      getenviromentList() {
        this.listLoading = true
        this.search.enviromentname = this.tmpenviromentname
        this.search.creator = this.name
        search(this.search).then(response => {
          this.enviromentList = response.data.list
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
          this.enviromentList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpenviromentname = this.search.enviromentname
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
        this.getenviromentList()
      },

      servicehandleSizeChange(size) {
        this.servicesearch.page = 1
        this.servicesearch.size = size
        this.getmacdepunitList()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.search.page = page
        this.getenviromentList()
      },

      servicehandleCurrentChange(page) {
        this.servicesearch.page = page
        this.getmacdepunitList()
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

      servicegetIndex(index) {
        return (this.servicesearch.page - 1) * this.servicesearch.size + index + 1
      },
      /**
       * 显示添加测试环境对话框
       */
      showAddenviromentDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpenviroment.id = ''
        this.tmpenviroment.enviromentname = ''
        this.tmpenviroment.memo = ''
        this.tmpenviroment.envtype = ''
        this.tmpenviroment.creator = this.name
        this.tmpenviroment.projectid = window.localStorage.getItem('pid')
      },
      /**
       * 添加测试环境
       */
      addenviroment() {
        this.$refs.tmpenviroment.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addenviroment(this.tmpenviroment).then(() => {
              this.$message.success('添加成功')
              this.getenviromentList()
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
       * 显示修改测试环境对话框
       * @param index 测试环境下标
       */
      showUpdateenviromentDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpenviroment.id = this.enviromentList[index].id
        this.tmpenviroment.enviromentname = this.enviromentList[index].enviromentname
        this.tmpenviroment.envtype = this.enviromentList[index].envtype
        this.tmpenviroment.memo = this.enviromentList[index].memo
        this.tmpenviroment.creator = this.name
        this.tmpenviroment.projectid = window.localStorage.getItem('pid')
      },

      showdeployserviceDialog(index) {
        this.ServiceDeployFormVisible = true
        this.tmpmacdepunit.envid = this.enviromentList[index].id
        this.tmpmacdepunit.enviromentname = this.enviromentList[index].enviromentname
      },

      showadddeployserviceDialog(index) {
        this.adddeployservicedialogFormVisible = true
      },
      /**
       * 更新测试环境
       */
      updateenviroment() {
        this.$refs.tmpenviroment.validate(valid => {
          if (valid) {
            updateenviroment(this.tmpenviroment).then(() => {
              this.$message.success('更新成功')
              this.getenviromentList()
              this.dialogFormVisible = false
            }).catch(res => {
              this.$message.error('更新失败')
            })
          }
        })
      },

      /**
       * 删除测试环境
       * @param index 测试环境下标
       */
      removeenviroment(index) {
        this.$confirm('删除该测试环境？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.enviromentList[index].id
          removeenviroment(id).then(() => {
            this.$message.success('删除成功')
            this.getenviromentList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 测试环境是否唯一
       * @param 测试环境
       */
      isUniqueDetail(enviroment) {
        for (let i = 0; i < this.enviromentList.length; i++) {
          if (this.enviromentList[i].id !== enviroment.id) { // 排除自己
            if (this.enviromentList[i].enviromentname === enviroment.enviromentname) {
              this.$message.error('测试环境名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
