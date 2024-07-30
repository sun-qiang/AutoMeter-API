<template>
  <div class="dashboard-editor-container">
    <div class="filter-container">
      <el-form :inline="true"    :model="search"
               ref="searchgrap">

        <span v-if="hasPermission('apiperformancestatistics:search')">
          <el-form-item label="测试集合" prop="executeplanname"  required>
          <el-select v-model="search.executeplanname" filterable placeholder="测试集合" @change="testplanselectChanged($event)">
            <el-option label="请选择"/>
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
            </div>
          </el-select>
        </el-form-item>

          <el-form-item label="场景" prop="scenename" required>
          <el-select  v-model="search.scenename" filterable placeholder="测试场景" @change="testsceneselectChanged($event)">
            <el-option label="请选择"/>
            <div v-for="(testscene, index) in testsceneList" :key="index">
              <el-option :label="testscene.scenename" :value="testscene.scenename" />
            </div>
          </el-select>
        </el-form-item>

           <el-form-item label="执行计划" prop="batchname" required>
            <el-select  v-model="search.batchname" filterable placeholder="执行计划">
                        <el-option label="请选择"/>

              <div v-for="(planbatch, index) in planbatchList" :key="index">
              <el-option :label="planbatch.batchname" :value="planbatch.batchname" />
            </div>
          </el-select>
          </el-form-item>

          <el-form-item label="用例" prop="casename" >
          <el-select  v-model="search.casename" filterable placeholder="测试用例" @change="testcaseselectChanged($event)">
                        <el-option label="请选择"/>
            <div v-for="(testcase, index) in testcaseList" :key="index">
              <el-option :label="testcase.casename" :value="testcase.casename" />
            </div>
          </el-select>
        </el-form-item>


          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>

    <el-row  style="background:#fff;padding:16px 16px 15px;margin-bottom:15px;">
      <Chart className="testplanchar" id="testplan" LineName="并发线程" :PlanDateData="LineDateData" :StaticsData="PlanStaticsData" height="300%" width="100%" />
    </el-row>

    <el-row  style="background:#fff;padding:16px 16px 15px;margin-bottom:25px;">
      <Chart className="deployunitchar" id="thread" LineName="响应时间" :PlanDateData="responetimexdatas" :StaticsData="responetimeydatas" height="300%" width="100%" />
    </el-row>

    <el-row  style="background:#fff;padding:16px 16px 15px;margin-bottom:25px;">
      <Chart className="deployunitchar" id="deployunit" LineName="TPS" :PlanDateData="TPSXData" :StaticsData="TPSydatas" height="300%" width="100%" />
    </el-row>



  </div>

</template>

<script>
import PanelGroup from '@/views/dashboard/dashboard/admin/components/PanelGroup'
import LineChart from '@/views/dashboard/dashboard/admin/components/LineChart'
import Chart from '@/views/dashboard/dashboard/admin/components/LineMarker'
import PieChart from '@/views/dashboard/dashboard/admin/components/PieChart'
import { mapGetters } from 'vuex'
import { getallexplanbytype as getallexplanbytype } from '@/api/executecenter/executeplan'
import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
import { findscenebyexecplanid as findscenebyexecplanid } from '@/api/executecenter/testplantestscene'
import { findcasebyscenenid as findcasebyscenenid } from '@/api/executecenter/testscenetestcase'
import { gettpsydatas, gettpsxdatas, getperformancereportthreaddatas, getperformancereportthreadtimedatas, getresponetimexdatas, getresponetimeydatas } from '@/api/reportcenter/performancereportthread'
// import BarChart from './components/BarChart'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  components: {
    PanelGroup,
    LineChart,
    Chart,
    PieChart
    // BarChart
  },
  data() {
    return {
      tmpapiperformancestatistics: {
        executeplanid: '', // 计划id
        id: '',
        deployunitid: '',
        deployunitname: '',
        batchname: '',
        apiperformancestatisticsname: '',
        visittype: '',
        path: '',
        memo: ''
      },
      tmpexecplantype: {
        usetype: '',
        projectid: ''
      },
      testsceneList: [],
      testcaseList: [],
      planbatchList: [], // 执行计划列表
      execplanList: [], // 计划列表
      search: {
        page: 1,
        size: 10,
        executeplanname: '',
        testplanid: '',
        batchname: '',
        scenename: '',
        testscenenid: '',
        casename: '',
        caseid: '',
        projectid: ''
      },
      lineChartData: lineChartData.newVisitis,
      piedeployunittypedata: [],
      pietypedeployunitcaseValueData: [],
      pietypedeployunitapiValueData: [],
      pieplancasetypedata: [],
      pieplancaseValueData: [],
      PlanStaticsData: [],
      DeployUnitStaticsData: [],
      LineDateData: [],
      responetimexdatas: [],
      responetimeydatas: [],
      TPSXData: [],
      TPSydatas: [],
      searchtest: {
        projectid: ''
      }
    }
  },

  computed: {
    ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
  },

  created() {
    this.searchtest.projectid = window.localStorage.getItem('pid')
    this.tmpexecplantype.projectid = window.localStorage.getItem('pid')
    this.getexecplanList()
  },

  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },

    searchBy() {
      console.log(this.search)
      this.$refs.searchgrap.validate(valid => {
        if (valid) {
          this.PlanStaticsData = []
          this.LineDateData = []
          this.responetimexdatas = []
          this.responetimeydatas = []
          this.TPSXData = []
          this.TPSydatas = []
          this.getperformancereportthreadtimedatas()
          this.getperformancereportthreaddatas()
          this.getresponetimexdatas()
          this.getresponetimeydatas()
          this.gettpsxdatas()
          this.gettpsydatas()
        }
      })
    },

    testplanselectChanged(e) {
      this.search.testplanid = null
      this.search.testscenenid = null
      this.search.scenename = null
      this.search.caseid = null
      this.search.casename = null
      this.search.batchname = null
      this.tmpapiperformancestatistics.executeplanid = 0
      this.testcaseList = null
      this.testsceneList = null
      this.planbatchList = null
      for (let i = 0; i < this.execplanList.length; i++) {
        if (this.execplanList[i].executeplanname === e) {
          this.tmpapiperformancestatistics.executeplanid = this.execplanList[i].id
          this.search.testplanid = this.execplanList[i].id
        }
      }
      getbatchbyplan(this.tmpapiperformancestatistics).then(response => {
        this.planbatchList = response.data
      }).catch(res => {
        this.$message.error('加载执行计划列表失败')
      })
      findscenebyexecplanid(this.search).then(response => {
        this.testsceneList = response.data.list
      }).catch(res => {
        this.$message.error('加载执行计划列表失败')
      })
    },

    testsceneselectChanged(e) {
      this.search.testscenenid = null
      this.search.caseid = null
      this.search.casename = null
      this.testcaseList = null
      for (let i = 0; i < this.testsceneList.length; i++) {
        if (this.testsceneList[i].scenename === e) {
          this.search.testscenenid = this.testsceneList[i].testscenenid
        }
        findcasebyscenenid(this.search).then(response => {
          this.testcaseList = response.data.list
        }).catch(res => {
          this.$message.error('加载执行计划列表失败')
        })
      }
    },

    testcaseselectChanged(e) {
      this.search.caseid = null
      for (let i = 0; i < this.testcaseList.length; i++) {
        if (this.testcaseList[i].casename === e) {
          this.search.caseid = this.testcaseList[i].testcaseid
        }
      }
    },

    getexecplanList() {
      this.tmpexecplantype.usetype = '性能'
      getallexplanbytype(this.tmpexecplantype).then(response => {
        this.execplanList = response.data
        console.log(this.execplanList)
      }).catch(res => {
        this.$message.error('加载计划列表失败')
      })
    },

    gettpsxdatas() {
      gettpsxdatas(this.search).then(response => {
        this.TPSXData = response.data
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    gettpsydatas() {
      gettpsydatas(this.search).then(response => {
        this.TPSydatas = response.data
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    getresponetimexdatas() {
      getresponetimexdatas(this.search).then(response => {
        this.responetimexdatas = response.data
        console.log(this.responetimexdatas)
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    getresponetimeydatas() {
      getresponetimeydatas(this.search).then(response => {
        this.responetimeydatas = response.data
        console.log(this.responetimeydatas)
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    getperformancereportthreadtimedatas() {
      getperformancereportthreadtimedatas(this.search).then(response => {
        this.LineDateData = response.data
        console.log('15天日期PlanDateData...................')
        console.log(this.LineDateData)
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    getperformancereportthreaddatas() {
      getperformancereportthreaddatas(this.search).then(response => {
        this.PlanStaticsData = response.data
        console.log('15天日期PlanDateData...................')
        console.log(this.LineDateData)
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    }
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
