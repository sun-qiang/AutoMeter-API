<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true"
               :model="tmpquery"
               ref="tmpquery"
      >
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
          <el-form-item label="性能测试集合" prop="testplanname" required>
          <el-select v-model="tmpquery.testplanname" filterable clearable placeholder="测试集合" @change="testplanselectChanged($event)">
            <el-option label="请选择"/>
            <div v-for="(testplan, index) in execplanList" :key="index">
              <el-option :label="testplan.executeplanname" :value="testplan.executeplanname" />
            </div>
          </el-select>
        </el-form-item>
          <el-form-item label="执行计划" prop="batchname" required>
            <el-select v-model="tmpquery.batchname" filterable clearable placeholder="执行计划" @change="testbatchselectChanged($event)">
            <el-option label="请选择"/>
            <div v-for="(planbatch, index) in planbatchList" :key="index">
              <el-option :label="planbatch.batchname" :value="planbatch.batchname" />
            </div>
          </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getperformancestatics" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>

    <div class="dashboard-editor-container">
      <github-corner class="github-corner" />
      <el-row :gutter="50">
        <el-col :xs="24" :sm="24" :lg="9">
          <div id="22" class="chart-wrapper">
            <template>
              <div id="mian" style="width: 300px;height:380px;"></div>
            </template>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="15">
          <div id="12" class="chart-wrapper">
            <el-table
              style="width: 290vh;height:100px;"
              :data="casestaticsList"
              v-loading.body="listLoading"
              element-loading-text="loading"
              border
              fit
              highlight-current-row
            >
              <el-table-column label="用例总数" align="center" prop="caseNum" width="80"/>
              <el-table-column label="线程总数" align="center" prop="threadnums" width="80"/>
              <el-table-column label="循环总数" align="center" prop="loops" width="80"/>
              <el-table-column label="执行机总数" align="center" prop="slavernums" width="100"/>
              <el-table-column label="运行总数" align="center" prop="execCaseNums" width="80"/>
              <el-table-column label="成功总数" align="center" prop="successCaseNums" width="80"/>
              <el-table-column label="失败总数" align="center" prop="failCaseNums" width="80"/>
              <el-table-column label="总耗时(秒)" align="center" prop="costtime" width="90"/>
            </el-table>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="15">
          <div id="13" class="chart-wrapper">
            <el-table
              style="width: 120vh;height:120px;"
              :data="performancecasesstaticsList"
              :key="itemKey"
              v-loading.body="listLoading"
              size="mini"
              element-loading-text="loading"
              border
              highlight-current-row
            >
              <el-table-column label="TPS" align="center" prop="tps" width="80"/>
              <el-table-column label="运行次数" align="center" prop="samples" width="80"/>
              <el-table-column label="错误次数" align="center" prop="errorcount" width="80"/>
              <el-table-column label="错误率%" align="center" prop="errorrate" width="80"/>
              <el-table-column label="average(ms)" align="center" prop="average" width="100"/>
              <el-table-column label="90th pct(ms)" align="center" prop="nzpct" width="100"/>
              <el-table-column label="95th pct(ms)" align="center" prop="nfpct" width="100"/>
              <el-table-column label="99th pct(ms)" align="center" prop="nnpct" width="100"/>
              <el-table-column label="消耗时间(s)" align="center" prop="runtime" width="120"/>
              <el-table-column label="min(ms)" align="center" prop="min" width="80"/>
              <el-table-column label="max(ms)" align="center" prop="max" width="80"/>
              <el-table-column label="median(ms)" align="center" prop="median" width="100"/>

            </el-table>
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="15">
          <div id="14" class="chart-wrapper">
            <el-table
              style="width: 100vh"
              :data="salvercaseList"
              :key="itemKey"
              v-loading.body="listLoading"
              size="mini"
              element-loading-text="loading"
              border
              highlight-current-row
            >
              <el-table-column label="分布执行机" align="center" prop="slaverName" width="160"/>
              <el-table-column label="线程数" align="center" prop="threadnums" width="80"/>
              <el-table-column label="循环数" align="center" prop="loops" width="80"/>
              <el-table-column label="运行总数" align="center" prop="execCaseNums" width="80"/>
              <el-table-column label="成功总数" align="center" prop="successCaseNums" width="80"/>
              <el-table-column label="失败总数" align="center" prop="failCaseNums" width="80"/>
              <el-table-column label="耗时(秒)" align="center" prop="costtime" width="110"/>
            </el-table>
          </div>
        </el-col>
<!--        <el-col :xs="24" :sm="24" :lg="15">-->
<!--          <div id="15" class="chart-wrapper">-->
<!--            <el-table-->
<!--              style="width: 100vh"-->
<!--              :data="casesconditiontaticsList"-->
<!--              :key="itemKey"-->
<!--              v-loading.body="listLoading"-->
<!--              size="mini"-->
<!--              element-loading-text="loading"-->
<!--              border-->
<!--              highlight-current-row-->
<!--            >-->
<!--              <el-table-column label="测试集合前置条件数" align="center" prop="testCollectionConditionsNUms" width="350">-->
<!--              </el-table-column>-->
<!--              <el-table-column label="测试用例前置条件数" align="center" prop="caseConditionNums" width="340"/>-->
<!--            </el-table>-->
<!--          </div>-->
<!--        </el-col>-->
      </el-row>
    </div>

    <el-tabs v-model="activeName" type="card" ref="tabs">
      <el-tab-pane label="用例执行报告" name="zero">
        <div class="filter-container">
          <el-form :inline="true">
          <el-form-item label="状态:" prop="testplanname" >
              <el-select v-model="tmpquery.caseststus" style="width:100%" placeholder="值类型">
                <el-option label="成功" value="成功"></el-option>
                <el-option label="失败" value="失败"></el-option>
              </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchcaseReportBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
          </el-form>
        </div>

        <template>
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
            <el-table-column label="执行计划" align="center" prop="batchname" width="80"/>
            <el-table-column label="测试场景" align="center" prop="scenename" width="80"/>
            <el-table-column label="用例名" align="center" prop="casename" width="120"/>
            <el-table-column label="API" align="center" prop="apiname" width="80"/>
            <el-table-column label="请求方式" align="center" prop="requestmethod" width="80"/>

            <el-table-column label="状态" align="center" prop="status" width="50">
              <template slot-scope="scope">
                <span v-if="scope.row.status === '失败'" style="color:red">{{ scope.row.status }}</span>
                <span v-else style="color: #37B328">{{ scope.row.status }}</span>
              </template>
            </el-table-column>
            <el-table-column label="微服务" align="center" prop="deployunitname" width="120"/>


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
        </template>
      </el-tab-pane>
      <el-tab-pane label="前置条件执行结果" name="first">
        <template>
          <el-table
            :data="caseconditionreport"
            :key="itemKey"
            v-loading.body="listLoading"
            element-loading-text="loading"
            border
            fit
            highlight-current-row
          >
            <el-table-column label="编号" align="center" width="60">
              <template slot-scope="scope">
                <span v-text="conditiongetIndex(scope.$index)"></span>
              </template>
            </el-table-column>
            <el-table-column label="集合/用例名" align="center" prop="planname" width="150"/>
            <el-table-column label="执行计划名" align="center" prop="batchname" width="120"/>
            <el-table-column label="父条件名" align="center" prop="conditionname" width="120"/>
            <el-table-column label="子条件名" align="center" prop="subconditionname" width="180"/>
            <el-table-column label="子条件类型" align="center" prop="subconditiontype" width="100"/>
            <el-table-column label="条件结果" align="center" prop="conditionresult" width="100">
              <template slot-scope="scope">
                <el-popover trigger="hover" placement="top">
                  <p>{{ scope.row.conditionresult }}</p>
                  <div slot="reference" class="name-wrapper">
                    <el-tag size="medium">...</el-tag>
                  </div>
                </el-popover>
              </template>
            </el-table-column>
            <el-table-column label="条件状态" align="center" prop="conditionstatus" width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.conditionstatus === '失败'" style="color:red">{{ scope.row.conditionstatus }}</span>
                <span v-else style="color: #37B328">{{ scope.row.conditionstatus }}</span>
              </template>
            </el-table-column>
            <el-table-column label="消耗时长(ms)" align="center" prop="runtime" width="100"/>
            <el-table-column label="创建时间" align="center" prop="createTime" width="160">
              <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
            </el-table-column>

          </el-table>
          <el-pagination
            @size-change="conditionhandleSizeChange"
            @current-change="conditionhandleCurrentChange"
            :current-page="tmpconditionquery.page"
            :page-size="tmpconditionquery.size"
            :total="conditiontotal"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
          ></el-pagination>
        </template>
      </el-tab-pane>
      <el-tab-pane label="异常用例" name="three">
        <template>
          <el-table
            :data="dispatchdata"
            :key="itemKey"
            v-loading.body="listLoading"
            element-loading-text="loading"
            border
            fit
            highlight-current-row
          >
            <el-table-column label="编号" align="center" width="60">
              <template slot-scope="scope">
                <span v-text="dispatchgetIndex(scope.$index)"></span>
              </template>
            </el-table-column>
            <el-table-column label="执行机" align="center" prop="slavername" width="150"/>
            <el-table-column label="测试集合" align="center" prop="execplanname" width="150"/>
            <el-table-column label="执行计划" align="center" prop="batchname" width="150"/>
            <el-table-column label="执行用例" align="center" prop="testcasename" width="150"/>
            <el-table-column label="状态" align="center" prop="status" width="100"/>
            <el-table-column label="备注" align="center" prop="memo" width="150">
              <template slot-scope="scope">
                <span v-if="scope.row.memo !== ''" style="color:red">{{ scope.row.memo }}</span>
              </template>
            </el-table-column>>


            <el-table-column label="创建时间" align="center" prop="createTime" width="140">
              <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
            </el-table-column>
            <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="140">
              <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            @size-change="dispatchhandleSizeChange"
            @current-change="dispatchhandleCurrentChange"
            :current-page="tmpdispatchquery.page"
            :page-size="tmpdispatchquery.size"
            :total="dispatchtotal"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
          ></el-pagination>
        </template>
      </el-tab-pane>

    </el-tabs>


  </div>
</template>

<script>
  import { getDispatchWithstatus } from '@/api/dispatch/dispatch'
  import { search as search, getperformancecasestatics as getperformancecasestatics, findApicasereportWithNameandStatus as findApicasereportWithNameandStatus, getperformanceCaseSandF as getperformanceCaseSandF, getperformanceallstatics as getperformanceallstatics, getperformanceslaverstatics as getperformanceslaverstatics } from '@/api/reportcenter/apiperformancereport'
  import { findconditionreport } from '@/api/reportcenter/testconditionreport'
  import { getbatchbyplan as getbatchbyplan } from '@/api/executecenter/executeplanbatch'
  import { getallexplanbytype as getallexplanbytype } from '@/api/executecenter/executeplan'
  import { unix2CurrentTime } from '@/utils'
  import { mapGetters } from 'vuex'

  export default {
    name: '集合性能报告',
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
    // components: {
    //   PieChart
    // },
    data() {
      return {
        activeName: 'zero',
        chartPie: null,
        seriesData: [],
        casestaticsList: [], // api报告列表
        performancecasesstaticsList: [], // api报告列表
        salvercaseList: [],
        caseconditionreport: [],
        dispatchdata: [],
        itemKey: null,
        tmptestplanname: '',
        tmptestplanid: null,
        tmpbatchname: null,
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
        conditiontotal: 0, // 条件报告总数
        dispatchtotal: 0, // 调度总数
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
        tmpquery: {
          page: 1,
          size: 10,
          testplanname: '',
          executeplanid: '',
          batchid: '',
          batchname: '',
          caseststus: '',
          projectid: ''
        },
        tmpconditionquery: {
          page: 1,
          size: 10,
          executeplanid: 0,
          batchid: 0,
          batchname: ''
        },
        tmpdispatchquery: {
          page: 1,
          size: 10,
          executeplanid: 0,
          batchid: 0,
          batchname: ''
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
      this.tmpquery.projectid = window.localStorage.getItem('pid')
      this.getexecplanList()
    },

    activated() {
      this.getexecplanList()
      this.testplanselectChanged(this.tmpquery.testplanname)
    },

    mounted() {
      this.drawLine()
    },

    methods: {
      unix2CurrentTime,
      /**
       * 获取测试集合列表
       */
      drawLine() {
        const echarts = require('echarts')
        const myChart = echarts.init(document.getElementById('mian'))
        // 绘制图表
        var option
        option = {
          title: {
            text: '性能用例执行结果比例',
            // subtext: '纯属虚构',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          color: ['#44c23f', '#d7212f'],
          series: [
            {
              name: '类型',
              type: 'pie',
              radius: '75%',
              center: ['50%', '48%'],
              data: this.seriesData
            }
          ]
        }
        myChart.setOption(option)
      },
      // 明细报告
      getapireportList() {
        this.$refs.tmpquery.validate(valid => {
          if (valid) {
            search(this.tmpquery).then(response => {
              this.apireportList = response.data.list
              this.total = response.data.total
            }).catch(res => {
              this.$message.error('加载api报告列表失败')
            })
          }
        })
      },

      findconditionreport() {
        findconditionreport(this.tmpconditionquery).then(response => {
          this.caseconditionreport = response.data.list
          this.conditiontotal = response.data.total
        }).catch(res => {
          this.$message.error('加载api报告列表失败')
        })
      },

      getperformanceallstatics() {
        getperformanceallstatics(this.tmpconditionquery).then(response => {
          this.casestaticsList = response.data
        }).catch(res => {
          this.$message.error('加载api报告列表失败')
        })
      },

      getperformanceslaverstatics() {
        getperformanceslaverstatics(this.tmpconditionquery).then(response => {
          this.salvercaseList = response.data
        }).catch(res => {
          this.$message.error('加载api报告列表失败')
        })
      },

      getDispatchWithstatus() {
        getDispatchWithstatus(this.tmpdispatchquery).then(response => {
          this.dispatchdata = response.data.list
          this.dispatchtotal = response.data.total
        }).catch(res => {
          this.$message.error('加载api报告列表失败')
        })
      },

      searchcaseReportBy() {
        this.$refs.tmpquery.validate(valid => {
          if (valid) {
            findApicasereportWithNameandStatus(this.tmpquery).then(response => {
              this.apireportList = response.data.list
              this.total = response.data.total
            }).catch(res => {
              this.$message.error('加载用例结果报告列表失败')
            })
          }
        })
      },
      getexecplanList() {
        this.tmpexecplantype.usetype = '性能'
        getallexplanbytype(this.tmpexecplantype).then(response => {
          this.execplanList = response.data
        }).catch(res => {
          this.$message.error('加载计划列表失败')
        })
      },

      // 饼图数据
      async getperformanceCaseSandF() {
        await getperformanceCaseSandF(this.tmpquery).then(response => {
          this.seriesData = response.data
        }).catch(res => {
          // this.$message.error('加载用例结果比例列表失败')
        })
      },

      async getperformancestatics() {
        // 明细
        this.getapireportList()
        this.getperformanceallstatics()
        this.getperformanceslaverstatics()
        // this.getfunctionconditionstatics()
        await this.getperformanceCaseSandF()
        this.drawLine()
        // this.getapireportList()
        this.getperformancecasestatics()
        this.findconditionreport()
        this.getDispatchWithstatus()
      },

      // tps等统计信息
      getperformancecasestatics() {
        this.$refs.tmpquery.validate(valid => {
          if (valid) {
            getperformancecasestatics(this.tmpquery).then(response => {
              this.performancecasesstaticsList = response.data.list
            }).catch(res => {
            })
          }
        })
      },
      /**
       * 计划下拉选择事件获取微服务id  e的值为options的选值
       */
      testplanselectChanged(e) {
        for (let i = 0; i < this.execplanList.length; i++) {
          if (this.execplanList[i].executeplanname === e) {
            this.tmpquery.executeplanid = this.execplanList[i].id
            this.tmpconditionquery.executeplanid = this.execplanList[i].id
            this.tmpdispatchquery.executeplanid = this.execplanList[i].id
          }
        }
        getbatchbyplan(this.tmpquery).then(response => {
          this.planbatchList = response.data
        }).catch(res => {
          this.$message.error('加载执行计划列表失败')
        })
      },

      testbatchselectChanged(e) {
        this.tmpquery.batchname = e
        for (let i = 0; i < this.planbatchList.length; i++) {
          if (this.planbatchList[i].batchname === e) {
            this.tmpquery.batchid = this.planbatchList[i].id
            this.tmpconditionquery.batchid = this.planbatchList[i].id
            this.tmpdispatchquery.batchid = this.planbatchList[i].id
            this.tmpdispatchquery.batchname = e
            this.tmpconditionquery.batchname = e
          }
        }
      },

      getIndex(index) {
        return (this.tmpquery.page - 1) * this.tmpquery.size + index + 1
      },

      conditiongetIndex(index) {
        return (this.tmpconditionquery.page - 1) * this.tmpconditionquery.size + index + 1
      },
      dispatchgetIndex(index) {
        return (this.tmpdispatchquery.page - 1) * this.tmpdispatchquery.size + index + 1
      },
      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.tmpquery.page = 1
        this.tmpquery.size = size
        this.getapireportList()
      },

      conditionhandleSizeChange(size) {
        this.tmpconditionquery.page = 1
        this.tmpconditionquery.size = size
        this.findconditionreport()
      },

      dispatchhandleSizeChange(size) {
        this.tmpdispatchquery.page = 1
        this.tmpdispatchquery.size = size
        this.getDispatchWithstatus()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.tmpquery.page = page
        this.getapireportList()
      },
      conditionhandleCurrentChange(page) {
        this.tmpconditionquery.page = page
        this.findconditionreport()
      },
      dispatchhandleCurrentChange(page) {
        this.tmpdispatchquery.page = page
        this.getDispatchWithstatus()
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
    margin-bottom: 10px;
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
