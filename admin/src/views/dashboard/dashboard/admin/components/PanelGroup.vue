<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handlemachineData()">
        <div class="card-panel-icon-wrapper icon-money">
          <icon-svg icon-class="machine"  class-name="card-panel-icon" >
          </icon-svg>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            测试服务器
          </div>
          <count-to :start-val="machinenum.start" :end-val="machinenum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <icon-svg icon-class="enviroment"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            测试环境
          </div>
          <count-to :start-val="enviroemnt.start" :end-val="enviroemnt.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="deployunitChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-money">
          <icon-svg icon-class="deploy"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            微服务
          </div>
          <count-to :start-val="deployunum.start" :end-val="deployunum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="apiChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <icon-svg icon-class="api"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            API
          </div>
          <count-to :start-val="apinum.start" :end-val="apinum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="functioncaseChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-money">
          <icon-svg icon-class="funtest"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            功能用例
          </div>
          <count-to :start-val="funcasenum.start" :end-val="funcasenum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="performancecasecaseChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <icon-svg icon-class="pertest"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            性能用例
          </div>
          <count-to :start-val="percasenum.start" :end-val="percasenum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="planChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-money">
          <icon-svg icon-class="plan"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            测试集合
          </div>
          <count-to :start-val="execplan.start" :end-val="execplan.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="slaverChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <icon-svg icon-class="slaver"  class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            测试执行机
          </div>
          <count-to :start-val="slavernum.start" :end-val="slavernum.end" :duration="2600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { getmachinenum as getmachinenum } from '@/api/assets/machine'
import { getenviromentnum as getenviromentnum } from '@/api/enviroment/testenviroment'
import { getcasenum as getcasenum, getperformancecasenum as getperformancecasenum } from '@/api/assets/apicases'
import { getapinum as getapinum } from '@/api/deployunit/api'
import { getdeploynum as getdeploynum } from '@/api/deployunit/depunit'
import { getslavernum as getslavernum } from '@/api/dispatch/slaver'
import { getexecuteplannum as getexecuteplannum } from '@/api/executecenter/executeplan'
import { mapGetters } from 'vuex'

export default {
  components: {
    CountTo
  },
  data() {
    return {
      funtestcase: {
        casetype: '',
        projectid: ''
      },
      pertestcase: {
        casetype: '',
        projectid: ''
      },
      machinenum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      enviroemnt: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      funcasenum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      percasenum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      apinum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      deployunum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      slavernum: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
      execplan: {
        start: 0, // 开始计数
        end: 0 // 结束计数
      },
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
    this.pertestcase.projectid = window.localStorage.getItem('pid')
    this.funtestcase.projectid = window.localStorage.getItem('pid')
    this.getmachinecount()
    this.getenviromentnum()
    this.getfuncasenum()
    this.getperformancecasenum()
    this.getapinum()
    this.getdeploynum()
    this.getslavernum()
    this.getexecuteplannum()
  },

  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/enviroment/testenviroment/list' })
    },
    deployunitChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/deployunit/depunit/list' })
    },
    apiChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/deployunit/api/list' })
    },
    planChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/executecenter/executeplan/list' })
    },
    slaverChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/dispatch/slaver/list' })
    },
    functioncaseChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/executecenter/apicases/list', query: { casetype: '功能' }})
    },
    performancecasecaseChartData(type) {
      this.$emit('handleSetLineChartData', type)
      this.$router.push({ path: '/executecenter/apicases/list', query: { casetype: '性能' }})
    },
    handlemachineData() {
      this.$emit('handleSetLineChartData')
      this.$router.push({ path: '/enviroment/machine/list' })
    },
    getmachinecount() {
      getmachinenum(this.search).then(response => {
        this.machinenum.end = response.data
      }).catch(res => {
        this.$message.error('加载服务器数量失败')
      })
    },
    getfuncasenum() {
      this.funtestcase.casetype = '功能'
      console.log('功能用例。。。。。。。。。。。。。。。。。。。。。。。。。')
      console.log(this.funtestcase)
      getcasenum(this.funtestcase).then(response => {
        this.funcasenum.end = response.data
      }).catch(res => {
        this.$message.error('加载服务器数量失败')
      })
    },
    getperformancecasenum() {
      console.log('性能用例。。。。。。。。。。。。。。。。。。。。。。。。。')
      console.log(this.pertestcase)
      this.pertestcase.casetype = '性能'
      getperformancecasenum(this.pertestcase).then(response => {
        this.percasenum.end = response.data
      }).catch(res => {
        this.$message.error('加载服务器数量失败')
      })
    },
    getenviromentnum() {
      getenviromentnum(this.search).then(response => {
        this.enviroemnt.end = response.data
      }).catch(res => {
        this.$message.error('加载环境数量失败')
      })
    },
    getapinum() {
      getapinum(this.search).then(response => {
        this.apinum.end = response.data
      }).catch(res => {
        this.$message.error('加载api数量失败')
      })
    },
    getdeploynum() {
      getdeploynum(this.search).then(response => {
        this.deployunum.end = response.data
      }).catch(res => {
        this.$message.error('加载微服务数量失败')
      })
    },
    getexecuteplannum() {
      getexecuteplannum(this.search).then(response => {
        this.execplan.end = response.data
      }).catch(res => {
        this.$message.error('加载计划数量失败')
      })
    },
    getslavernum() {
      getslavernum().then(response => {
        this.slavernum.end = response.data
      }).catch(res => {
        this.$message.error('加载执行机数量失败')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 10px;

  .card-panel-col {
    margin-bottom: 20px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 30px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
