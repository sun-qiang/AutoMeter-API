<template>
  <div class="app-container">
    <div class="filter-container">

      <el-form :inline="true" :model="searchcase" ref="searchcase">
        <span v-if="hasPermission('apireportstatics:search')">

          <el-form-item label="测试集合" prop="testplanname" required>
            <el-select v-model="searchcase.testplanname" filterable placeholder="测试集合" @change="testplanselectChanged($event)">
              <el-option label="请选择" value/>
              <div v-for="(testplan, index) in execplanList" :key="index">
                <el-option :label="testplan.executeplanname" :value="testplan.executeplanname"/>
              </div>
            </el-select>
          </el-form-item>

        <el-form-item label="执行测试集合" prop="batchname" requied >
          <el-select v-model="searchcase.batchname" filterable placeholder="执行测试集合">
            <el-option label="请选择" value/>
            <div v-for="(planbatch, index) in planbatchList" :key="index">
              <el-option :label="planbatch.batchname" :value="planbatch.batchname"/>
            </div>
          </el-select>
        </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading"
            >查询</el-button>
          </el-form-item>

        </span>
      </el-form>

    </div>
    <el-table
      :data="apireportstaticsList"
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
      <el-table-column label="测试集合" align="center" prop="planname" width="200"/>
      <el-table-column label="执行测试集合" align="center" prop="batchname" width="200"/>
      <el-table-column label="用例数" align="center" prop="testcasenums" width="200"/>
      <el-table-column label="成功数" align="center" prop="passnums" width="200"/>
      <el-table-column label="失败数" align="center" prop="failnums" width="200"/>
      <el-table-column label="消耗时间(ms)" align="center" prop="costtimes" width="200"/>
<!--      <el-table-column label="管理" align="center"-->
<!--                       v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            size="mini"-->
<!--            @click.native.prevent="showcasestaticsdetail(scope.$index)"-->
<!--          >统计详情-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            size="mini"-->
<!--            @click.native.prevent="showcasedetail(scope.$index)"-->
<!--          >用例明细-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

  </div>
</template>
<style>
.el-table.cell.el-tooltip {
  white-space: pre-wrap;
}
</style>
<script>
import { getstaticsreport as getstaticsreport } from '@/api/reportcenter/apireport'
// import { getdepunitList as getdepunitList } from '@/api/deployunit/depunit'
import { getallexplanbytype as getallexplanbytype } from '@/api/executecenter/executeplan'
import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
import { unix2CurrentTime } from '@/utils'

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
      tmptestplanname: '',
      tmptestplanid: null,
      tmpbatchname: null,
      apireportstaticsList: [], // api报告列表
      apiList: [], // api列表
      planbatchList: [], // 执行测试集合列表
      execplanList: [], // 测试集合列表
      deployunitList: [], // 微服务列表
      listLoading: false, // 数据加载等待动画
      dicvisitypeQuery: {
        page: 1, // 页码
        size: 30, // 每页数量
        diccode: 'httpvisittype' // 获取字典表入参
      },
      total: 0, // 数据总数
      // listQuery: {
      //   page: 1, // 页码
      //   size: 10, // 每页数量
      //   listLoading: true,
      //   testplanname: '',
      //   testplanid: null,
      //   batchname: null
      // },
      dialogStatus: 'add',
      dialogFormVisible: false,
      textMap: {
        updateRole: '修改apireportstatics',
        update: '修改apireportstatics',
        add: '添加apireportstatics'
      },
      btnLoading: false, // 按钮等待动画
      tmpapireportstatics: {
        executeplanid: '', // 测试集合id
        id: '',
        deployunitid: '',
        deployunitname: '',
        batchname: '',
        apireportstaticsname: '',
        visittype: '',
        path: '',
        memo: ''
      },
      tmpexecplantype: {
        usetype: ''
      },
      searchcase: {
        page: 1,
        size: 10,
        testplanname: null,
        testplanid: null,
        batchname: null
      }
    }
  },

  created() {
    this.getexecplanList()
    this.getdepunitList()
    // this.getapireportstaticsList()
  },

  methods: {
    unix2CurrentTime,

    /**
     * 微服务下拉选择事件获取微服务id  e的值为options的选值
     */
    testplanselectChanged(e) {
      for (let i = 0; i < this.execplanList.length; i++) {
        if (this.execplanList[i].executeplanname === e) {
          this.tmpapireportstatics.executeplanid = this.execplanList[i].id
        }
      }
      getbatchbyplan(this.tmpapireportstatics).then(response => {
        this.planbatchList = response.data
      }).catch(res => {
        this.$message.error('加载执行测试集合列表失败')
      })
    },

    /**
     * 微服务下拉选择事件获取微服务id  e的值为options的选值
     */
    selectChanged(e) {
      for (let i = 0; i < this.deployunitList.length; i++) {
        if (this.deployunitList[i].deployunitname === e) {
          this.tmpapireportstatics.deployunitid = this.deployunitList[i].id
        }
        console.log(this.deployunitList[i].id)
      }
    },

    /**
     * 获取api报告列表
     */
    // getapireportstaticsList() {
    //   this.listLoading = true
    //   getstaticsreport(this.listQuery).then(response => {
    //     this.apireportstaticsList = response.data
    //     this.total = response.data.total
    //     this.listLoading = false
    //   }).catch(res => {
    //     this.$message.error('加载api报告列表失败')
    //   })
    // },

    /**
     * 获取测试集合列表
     */
    getexecplanList() {
      this.tmpexecplantype.usetype = '功能'
      getallexplanbytype(this.tmpexecplantype).then(response => {
        this.execplanList = response.data
      }).catch(res => {
        this.$message.error('加载测试集合列表失败')
      })
    },

    /**
     * 获取微服务列表
     */
    // getdepunitList() {
    //   getdepunitList(this.listQuery).then(response => {
    //     this.deployunitList = response.data.list
    //   }).catch(res => {
    //     this.$message.error('加载微服务列表失败')
    //   })
    // },

    searchBy() {
      this.$refs.searchcase.validate(valid => {
        if (valid) {
          this.btnLoading = true
          this.listLoading = true
          this.searchcase.testplanid = this.tmpapireportstatics.executeplanid
          getstaticsreport(this.searchcase).then(response => {
            this.apireportstaticsList = response.data.list
          }).catch(res => {
            this.$message.error('搜索失败')
          })
          this.tmptestplanname = this.searchcase.testplanname
          this.tmptestplanid = this.searchcase.testplanid
          this.tmpbatchname = this.searchcase.batchname
          this.listLoading = false
          this.btnLoading = false
        }
      })
    },

    /**
     * 表格序号
     * 可参考自定义表格序号
     * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
     * @param index 数据下标
     * @returns 表格序号
     */
    getIndex(index) {
      return index + 1
    },

    /**
     * 显示添加apireportstatics对话框
     */
    showcasestaticsdetail(index) {
      // 显示新增对话框
      this.dialogFormVisible = true
      this.dialogStatus = 'add'
      this.tmpapireportstatics.id = ''
      this.tmpapireportstatics.deployunitid = ''
      this.tmpapireportstatics.deployunitname = ''
      this.tmpapireportstatics.apireportstaticsname = ''
      this.tmpapireportstatics.visittype = ''
      this.tmpapireportstatics.path = ''
      this.tmpapireportstatics.memo = ''
    },

    /**
     * 显示修改apireportstatics对话框
     * @param index apireportstatics下标
     */
    showcasedetail(index) {
      console.log(this.apireportstaticsList[index].batchname)
    }
  }
}
</script>
