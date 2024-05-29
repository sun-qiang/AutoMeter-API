<template>
  <div id="12" class="chart-wrapper">
    <el-table
      style="width: 1200vh"
      :data="list"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="测试集合" align="center" prop="executeplanname" width="230"/>
      <el-table-column label="执行计划" align="center" prop="batchname" width="230"/>
      <el-table-column label="tps" align="center" prop="tps" width="230"/>
      <el-table-column label="运行次数" align="center" prop="samples" width="230"/>
      <el-table-column label="错误次数" align="center" prop="errorcount" width="230"/>
      <el-table-column label="错误率" align="center" prop="errorrate" width="230"/>
      <el-table-column label="average(ms)" align="center" prop="average" width="230"/>
      <el-table-column label="min(ms)" align="center" prop="min" width="230"/>
      <el-table-column label="max(ms)" align="center" prop="max" width="230"/>
      <el-table-column label="median(ms)" align="center" prop="median" width="230"/>
      <el-table-column label="90th pct(ms)" align="center" prop="nzpct" width="230"/>
      <el-table-column label="95th pct(ms)" align="center" prop="nfpct" width="230"/>
      <el-table-column label="99th pct(ms)" align="center" prop="nnpct" width="230"/>
      <el-table-column label="消耗时间(s)" align="center" prop="runtime" width="230"/>
    </el-table>
  </div>
</template>

<script>
import { searchmyrecentperfermanceinfo } from '@/api/dashboard/myinfo'
import { mapGetters } from 'vuex'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'success',
        pending: 'danger'
      }
      return statusMap[status]
    },
    orderNoFilter(str) {
      return str.substring(0, 30)
    }
  },
  data() {
    return {
      listLoading: false, // 数据加载等待动画
      list: null,
      tmpcreator: {
        creatorid: '',
        projectid: ''
      }
    }
  },
  computed: {
    ...mapGetters(['name', 'sidebar', 'projectlist', 'projectid', 'accountId'])
  },
  created() {
    this.tmpcreator.projectid = window.localStorage.getItem('pid')
    this.tmpcreator.creatorid = this.accountId
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.tmpcreator.creator = this.name
      searchmyrecentperfermanceinfo(this.tmpcreator).then(response => {
        this.list = response.data
      })
    }
  }
}
</script>
