<template>
  <div class="dashboard-editor-container">
<!--    <div class="filter-container">-->
<!--      <el-form :inline="true" ref="search">-->

<!--        <span v-if="hasPermission('apiperformancestatistics:search')">-->
<!--          <el-form-item label="测试集合" prop="executeplanname"  >-->
<!--          <el-select v-model="search.executeplanname" filterable placeholder="测试集合" @change="testplanselectChanged($event)">-->
<!--            <el-option label="请选择"/>-->
<!--            <div v-for="(testplan, index) in execplanList" :key="index">-->
<!--              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />-->
<!--            </div>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

<!--          <el-form-item label="场景" prop="scenename" >-->
<!--          <el-select  v-model="search.scenename" filterable placeholder="测试场景" @change="testsceneselectChanged($event)">-->
<!--            <el-option label="请选择"/>-->
<!--            <div v-for="(testscene, index) in testsceneList" :key="index">-->
<!--              <el-option :label="testscene.scenename" :value="testscene.scenename" />-->
<!--            </div>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

<!--          <el-form-item label="用例" prop="casename" >-->
<!--          <el-select  v-model="search.casename" filterable placeholder="测试用例" @change="testcaseselectChanged($event)">-->
<!--                        <el-option label="请选择"/>-->
<!--            <div v-for="(testcase, index) in testcaseList" :key="index">-->
<!--              <el-option :label="testcase.casename" :value="testcase.casename" />-->
<!--            </div>-->
<!--          </el-select>-->
<!--        </el-form-item>-->

<!--          <el-form-item label="执行计划" prop="batchname" >-->
<!--            <el-select  v-model="search.batchname" filterable placeholder="执行计划">-->
<!--                        <el-option label="请选择"/>-->

<!--              <div v-for="(planbatch, index) in planbatchList" :key="index">-->
<!--              <el-option :label="planbatch.batchname" :value="planbatch.batchname" />-->
<!--            </div>-->
<!--          </el-select>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>-->
<!--          </el-form-item>-->
<!--        </span>-->
<!--      </el-form>-->
<!--    </div>-->
    <component :is="currentRole" />


  </div>
</template>
<style>
  .el-table.cell.el-tooltip {
    white-space: pre-wrap;
  }
</style>
<script>
  // import { getapiperformancestatisticsList as getapiperformancestatisticsList, search, addapiperformancestatistics, updateapiperformancestatistics, removeapiperformancestatistics } from '@/api/reportcenter/apiperformancestatistics'
  // import { getdepunitList as getdepunitList } from '@/api/deployunit/depunit'
  // import { getallexplanbytype as getallexplanbytype } from '@/api/executecenter/executeplan'
  // import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
  // import { findscenebyexecplanid as findscenebyexecplanid } from '@/api/executecenter/testplantestscene'
  // import { findcasebyscenenid as findcasebyscenenid } from '@/api/executecenter/testscenetestcase'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'
  import adminDashboard from '@/components/grap'

  export default {
    components: { adminDashboard },
    name: '性能统计报告',
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
        currentRole: 'adminDashboard'
        // tmptestplanname: '',
        // tmptestplanid: null,
        // tmpbatchname: null,
        // testsceneList: [],
        // testcaseList: [],
        // visittypeList: [],
        // apiperformancestatisticsList: [], // api报告列表
        // apiList: [], // api列表
        // planbatchList: [], // 执行计划列表
        // execplanList: [], // 计划列表
        // deployunitList: [], // 微服务列表
        // listLoading: false, // 数据加载等待动画
        // dicvisitypeQuery: {
        //   page: 1, // 页码
        //   size: 30, // 每页数量
        //   diccode: 'httpvisittype' // 获取字典表入参
        // },
        // total: 0, // 数据总数
        // listQuery: {
        //   page: 1, // 页码
        //   size: 20, // 每页数量
        //   listLoading: true,
        //   testplanname: '',
        //   testplanid: null,
        //   batchname: null
        // },
        // dialogStatus: 'add',
        // dialogFormVisible: false,
        // textMap: {
        //   updateRole: '修改apiperformancestatistics',
        //   update: '修改apiperformancestatistics',
        //   add: '添加apiperformancestatistics'
        // },
        // btnLoading: false, // 按钮等待动画
        // tmpapiperformancestatistics: {
        //   executeplanid: '', // 计划id
        //   id: '',
        //   deployunitid: '',
        //   deployunitname: '',
        //   batchname: '',
        //   apiperformancestatisticsname: '',
        //   visittype: '',
        //   path: '',
        //   memo: ''
        // },
        // tmpexecplantype: {
        //   usetype: '',
        //   projectid: ''
        // },
        // search: {
        //   page: 1,
        //   size: 10,
        //   executeplanname: '',
        //   testplanid: '',
        //   batchname: '',
        //   scenename: '',
        //   testscenenid: '',
        //   casename: '',
        //   caseid: '',
        //   projectid: ''
        // }
      }
    },

    computed: {
      ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
    },

    created() {
      this.search.projectid = window.localStorage.getItem('pid')
      this.tmpexecplantype.projectid = window.localStorage.getItem('pid')
      // this.getexecplanList()
      // this.getdepunitList()
      // this.getapiperformancestatisticsList()
    },

    activated() {
      // this.getexecplanList()
    },

    methods: {
      unix2CurrentTime
      /**
       * 微服务下拉选择事件获取微服务id  e的值为options的选值
       */
      // testplanselectChanged(e) {
      //   this.search.testplanid = null
      //   this.search.testscenenid = null
      //   this.search.scenename = null
      //   this.search.caseid = null
      //   this.search.casename = null
      //   this.search.batchname = null
      //   this.tmpapiperformancestatistics.executeplanid = 0
      //   this.testcaseList = null
      //   this.testsceneList = null
      //   this.planbatchList = null
      //   for (let i = 0; i < this.execplanList.length; i++) {
      //     if (this.execplanList[i].executeplanname === e) {
      //       this.tmpapiperformancestatistics.executeplanid = this.execplanList[i].id
      //       this.search.testplanid = this.execplanList[i].id
      //     }
      //   }
      //   getbatchbyplan(this.tmpapiperformancestatistics).then(response => {
      //     this.planbatchList = response.data
      //   }).catch(res => {
      //     this.$message.error('加载执行计划列表失败')
      //   })
      //   findscenebyexecplanid(this.search).then(response => {
      //     this.testsceneList = response.data.list
      //   }).catch(res => {
      //     this.$message.error('加载执行计划列表失败')
      //   })
      // },
      //
      // testsceneselectChanged(e) {
      //   this.search.testscenenid = null
      //   this.search.caseid = null
      //   this.search.casename = null
      //   this.testcaseList = null
      //   for (let i = 0; i < this.testsceneList.length; i++) {
      //     if (this.testsceneList[i].scenename === e) {
      //       this.search.testscenenid = this.testsceneList[i].testscenenid
      //     }
      //     findcasebyscenenid(this.search).then(response => {
      //       this.testcaseList = response.data.list
      //     }).catch(res => {
      //       this.$message.error('加载执行计划列表失败')
      //     })
      //   }
      // },
      //
      // testcaseselectChanged(e) {
      //   this.search.caseid = null
      //   for (let i = 0; i < this.testcaseList.length; i++) {
      //     if (this.testcaseList[i].casename === e) {
      //       this.search.caseid = this.testcaseList[i].testcaseid
      //     }
      //   }
      // },
      //
      // /**
      //  * 微服务下拉选择事件获取微服务id  e的值为options的选值
      //  */
      // selectChanged(e) {
      //   for (let i = 0; i < this.deployunitList.length; i++) {
      //     if (this.deployunitList[i].deployunitname === e) {
      //       this.tmpapiperformancestatistics.deployunitid = this.deployunitList[i].id
      //     }
      //     console.log(this.deployunitList[i].id)
      //   }
      // },
      //
      // /**
      //  * 获取api报告列表
      //  */
      // getapiperformancestatisticsList() {
      //   this.listLoading = true
      //   getapiperformancestatisticsList(this.listQuery).then(response => {
      //     this.apiperformancestatisticsList = response.data.list
      //     this.total = response.data.total
      //     this.listLoading = false
      //   }).catch(res => {
      //     this.$message.error('加载api报告列表失败')
      //   })
      // },
      //
      // /**
      //  * 获取测试集合列表
      //  */
      // getexecplanList() {
      //   this.tmpexecplantype.usetype = '性能'
      //   getallexplanbytype(this.tmpexecplantype).then(response => {
      //     this.execplanList = response.data
      //   }).catch(res => {
      //     this.$message.error('加载计划列表失败')
      //   })
      // },
      // /**
      //  * 获取微服务列表
      //  */
      // getdepunitList() {
      //   getdepunitList(this.listQuery).then(response => {
      //     this.deployunitList = response.data.list
      //   }).catch(res => {
      //     this.$message.error('加载微服务列表失败')
      //   })
      // },
      //
      // searchBy() {
      //   this.btnLoading = true
      //   this.listLoading = true
      //   this.search.page = this.listQuery.page
      //   this.search.size = this.listQuery.size
      //   this.search.testplanid = this.tmpapiperformancestatistics.executeplanid
      //   search(this.search).then(response => {
      //     this.apiperformancestatisticsList = response.data.list
      //     this.total = response.data.total
      //   }).catch(res => {
      //     this.$message.error('搜索失败')
      //   })
      //   this.tmptestplanname = this.search.testplanname
      //   this.tmptestplanid = this.search.testplanid
      //   this.tmpbatchname = this.search.batchname
      //   this.listLoading = false
      //   this.btnLoading = false
      // },
      // searchBypageing() {
      //   this.btnLoading = true
      //   this.listLoading = true
      //   this.listQuery.testplanname = this.tmptestplanname
      //   this.listQuery.testplanid = this.tmptestplanid
      //   this.listQuery.batchname = this.tmpbatchname
      //   search(this.listQuery).then(response => {
      //     this.apiperformancestatisticsList = response.data.list
      //     this.total = response.data.total
      //   }).catch(res => {
      //     this.$message.error('搜索失败')
      //   })
      //   this.listLoading = false
      //   this.btnLoading = false
      // },
      //
      // /**
      //  * 改变每页数量
      //  * @param size 页大小
      //  */
      // handleSizeChange(size) {
      //   this.listQuery.size = size
      //   this.listQuery.page = 1
      //   this.searchBypageing()
      // },
      // /**
      //  * 改变页码
      //  * @param page 页号
      //  */
      // handleCurrentChange(page) {
      //   this.listQuery.page = page
      //   this.searchBypageing()
      // },
      // /**
      //  * 表格序号
      //  * 可参考自定义表格序号
      //  * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
      //  * @param index 数据下标
      //  * @returns 表格序号
      //  */
      // getIndex(index) {
      //   return (this.listQuery.page - 1) * this.listQuery.size + index + 1
      // },
      // /**
      //  * 显示添加apiperformancestatistics对话框
      //  */
      // showAddapiperformancestatisticsDialog() {
      //   // 显示新增对话框
      //   this.dialogFormVisible = true
      //   this.dialogStatus = 'add'
      //   this.tmpapiperformancestatistics.id = ''
      //   this.tmpapiperformancestatistics.deployunitid = ''
      //   this.tmpapiperformancestatistics.deployunitname = ''
      //   this.tmpapiperformancestatistics.apiperformancestatisticsname = ''
      //   this.tmpapiperformancestatistics.visittype = ''
      //   this.tmpapiperformancestatistics.path = ''
      //   this.tmpapiperformancestatistics.memo = ''
      // },
      // /**
      //  * 添加apiperformancestatistics
      //  */
      // addapiperformancestatistics() {
      //   this.$refs.tmpapiperformancestatistics.validate(valid => {
      //     if (valid && this.isUniqueDetail(this.tmpapiperformancestatistics)) {
      //       this.btnLoading = true
      //       addapiperformancestatistics(this.tmpapiperformancestatistics).then(() => {
      //         this.$message.success('添加成功')
      //         this.getapiperformancestatisticsList()
      //         this.dialogFormVisible = false
      //         this.btnLoading = false
      //       }).catch(res => {
      //         this.$message.error('添加失败')
      //         this.btnLoading = false
      //       })
      //     }
      //   })
      // },
      // /**
      //  * 显示修改apiperformancestatistics对话框
      //  * @param index apiperformancestatistics下标
      //  */
      // showUpdateapiperformancestatisticsDialog(index) {
      //   this.dialogFormVisible = true
      //   this.dialogStatus = 'update'
      //   this.tmpapiperformancestatistics.id = this.apiperformancestatisticsList[index].id
      //   this.tmpapiperformancestatistics.deployunitid = this.apiperformancestatisticsList[index].deployunitid
      //   this.tmpapiperformancestatistics.deployunitname = this.apiperformancestatisticsList[index].deployunitname
      //   this.tmpapiperformancestatistics.apiperformancestatisticsname = this.apiperformancestatisticsList[index].apiperformancestatisticsname
      //   this.tmpapiperformancestatistics.visittype = this.apiperformancestatisticsList[index].visittype
      //   this.tmpapiperformancestatistics.path = this.apiperformancestatisticsList[index].path
      //   this.tmpapiperformancestatistics.memo = this.apiperformancestatisticsList[index].memo
      // },
      // /**
      //  * 更新apiperformancestatistics
      //  */
      // updateapiperformancestatistics() {
      //   if (this.isUniqueDetail(this.tmpapiperformancestatistics)) {
      //     updateapiperformancestatistics(this.tmpapiperformancestatistics).then(() => {
      //       this.$message.success('更新成功')
      //       this.getapiperformancestatisticsList()
      //       this.dialogFormVisible = false
      //     }).catch(res => {
      //       this.$message.error('更新失败')
      //     })
      //   }
      // },
      //
      // /**
      //  * 删除字典
      //  * @param index apiperformancestatistics下标
      //  */
      // removeapiperformancestatistics(index) {
      //   this.$confirm('删除该apiperformancestatistics？', '警告', {
      //     confirmButtonText: '是',
      //     cancelButtonText: '否',
      //     type: 'warning'
      //   }).then(() => {
      //     const id = this.apiperformancestatisticsList[index].id
      //     removeapiperformancestatistics(id).then(() => {
      //       this.$message.success('删除成功')
      //       this.getapiperformancestatisticsList()
      //     })
      //   }).catch(() => {
      //     this.$message.info('已取消删除')
      //   })
      // },
      //
      // /**
      //  * apiperformancestatistics资料是否唯一
      //  * @param apiperformancestatistics
      //  */
      // isUniqueDetail(apiperformancestatistics) {
      //   console.log(apiperformancestatistics.id)
      //   for (let i = 0; i < this.apiperformancestatisticsList.length; i++) {
      //     if (this.apiperformancestatisticsList[i].id !== apiperformancestatistics.id) { // 排除自己
      //       console.log(this.apiperformancestatisticsList[i].id)
      //       if (this.apiperformancestatisticsList[i].apiperformancestatisticsname === apiperformancestatistics.apiperformancestatisticsname) {
      //         this.$message.error('apiperformancestatistics名已存在')
      //         return false
      //       }
      //     }
      //   }
      //   return true
      // }
    }
  }
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 20px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fffffb;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }

  .chart-container{
    position: relative;
    width: 100%;
    height: calc(100vh - 84px);
  }
}
</style>
