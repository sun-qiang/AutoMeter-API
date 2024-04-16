<template>
  <div class="dashboard-editor-container">
    <github-corner class="github-corner" />
    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <!--<el-row>-->
      <!--<line-chart :chart-data="lineChartData" />-->
    <!--</el-row>-->

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div id="11" class="chart-wrapper">
          <PieChart BusinessName="微服务:测试用例" :typeData="piedeployunittypedata" :typeValueData="pietypedeployunitcaseValueData"/>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div id="12" class="chart-wrapper">
          <PieChart BusinessName="微服务:API接口" :typeData="piedeployunittypedata"  :typeValueData="pietypedeployunitapiValueData"/>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <PieChart BusinessName="测试集合:场景" :typeData="pieplancasetypedata"  :typeValueData="pieplancaseValueData"/>
        </div>
      </el-col>
    </el-row>

    <el-row  style="background:#fff;padding:16px 16px 15px;margin-bottom:15px;">
      <Chart className="deployunitchar" id="deployunit" LineName="微服务用例成功率" :PlanDateData="LineDateData" :StaticsData="DeployUnitStaticsData" height="300%" width="100%" />
    </el-row>

    <el-row  style="background:#fff;padding:16px 16px 15px;margin-bottom:15px;">
      <Chart className="testplanchar" id="testplan" LineName="测试集合用例成功率" :PlanDateData="LineDateData" :StaticsData="PlanStaticsData" height="300%" width="100%" />
    </el-row>


    <!--<el-row :gutter="8">-->
      <!--<el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 12}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:30px;">-->
        <!--<transaction-table />-->
      <!--</el-col>-->
      <!--<el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">-->
        <!--<todo-list />-->
      <!--</el-col>-->
      <!--<el-col :xs="{span: 24}" :sm="{span: 12}" :md="{span: 12}" :lg="{span: 6}" :xl="{span: 6}" style="margin-bottom:30px;">-->
        <!--<box-card />-->
      <!--</el-col>-->
    <!--</el-row>-->
  </div>

</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import Chart from './components/LineMarker'
import PieChart from './components/PieChart'
import { getstaticsdeployapi as getstaticsdeployapi } from '@/api/deployunit/api'
import { getstaticsdeployunitcases as getstaticsdeployunitcases } from '@/api/assets/apicases'
import { getstaticsdeploynames as getstaticsdeploynames } from '@/api/deployunit/depunit'
import { getstaticsplancases as getstaticsplancases } from '@/api/executecenter/testplantestscene'
import { getstaticsplan as getstaticsplan } from '@/api/executecenter/executeplan'
import { getStaticsPlanCasesList as getStaticsPlanCasesList, getStaticsgetlastdays, getStaticsDeployUnitCasesList } from '@/api/dashboard/StaticsPlanandcases'
import { mapGetters } from 'vuex'
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
      lineChartData: lineChartData.newVisitis,
      piedeployunittypedata: [],
      pietypedeployunitcaseValueData: [],
      pietypedeployunitapiValueData: [],
      pieplancasetypedata: [],
      pieplancaseValueData: [],
      PlanStaticsData: [],
      DeployUnitStaticsData: [],
      LineDateData: [],
      search: {
        projectid: ''
      }
    }
  },

  computed: {
    ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid'])
  },

  created() {
    this.search.projectid = window.localStorage.getItem('pid')
    this.getstaticsdeploynames()
    this.getstaticsplan()
    this.getstaticsplancases()
    this.getstaticsdeployapi()
    this.getstaticsdeployunitcases()
    this.getStaticsPlanCasesList()
    this.getStaticsDeployUnitCasesList()
    this.getStaticsgetlastdays()
  },

  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },

    /**
     * 获取统计执行计划X轴最近15天日期
     */
    getStaticsgetlastdays() {
      getStaticsgetlastdays().then(response => {
        this.LineDateData = response.data
        console.log('15天日期PlanDateData...................')
        console.log(this.LineDateData)
      }).catch(res => {
        this.$message.error('加载统计执行计划X轴最近15天日期失败')
      })
    },

    /**
     * 获取统计微服务用例成功率
     */
    getStaticsDeployUnitCasesList() {
      getStaticsDeployUnitCasesList(this.search).then(response => {
        this.DeployUnitStaticsData = response.data
      }).catch(res => {
        this.$message.error('加载统计微服务用例成功率失败')
      })
    },

    /**
     * 获取统计执行计划用例成功率
     */
    getStaticsPlanCasesList() {
      getStaticsPlanCasesList(this.search).then(response => {
        this.PlanStaticsData = response.data
        console.log('获取统计执行计划成功率...................')
        console.log(this.PlanStaticsData)
      }).catch(res => {
        this.$message.error('加载统计执行计划成功率失败')
      })
    },
    /**
     * 获取统计执行计划列表
     */
    getstaticsplan() {
      getstaticsplan(this.search).then(response => {
        this.pieplancasetypedata = response.data
      }).catch(res => {
        this.$message.error('加载统计执行计划列表失败')
      })
    },
    /**
     * 获取统计微服务列表
     */
    getstaticsdeploynames() {
      getstaticsdeploynames(this.search).then(response => {
        this.piedeployunittypedata = response.data
        console.log('pppppppppppppppppppppppppppppp')
        console.log(this.piedeployunittypedata)
      }).catch(res => {
        this.$message.error('加载统计微服务列表失败')
      })
    },

    /**
     * 获取统计执行计划用例列表
     */
    getstaticsplancases() {
      getstaticsplancases(this.search).then(response => {
        this.pieplancaseValueData = response.data
      }).catch(res => {
        this.$message.error('加载统计执行计划用例列表失败')
      })
    },

    /**
     * 获取统计微服务api列表
     */
    getstaticsdeployapi() {
      getstaticsdeployapi(this.search).then(response => {
        this.pietypedeployunitapiValueData = response.data
      }).catch(res => {
        this.$message.error('加载统计微服务api列表失败')
      })
    },

    /**
     * 获取统计微服务用例列表
     */
    getstaticsdeployunitcases() {
      getstaticsdeployunitcases(this.search).then(response => {
        this.pietypedeployunitcaseValueData = response.data
      }).catch(res => {
        this.$message.error('加载统计微服务用例失败')
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
