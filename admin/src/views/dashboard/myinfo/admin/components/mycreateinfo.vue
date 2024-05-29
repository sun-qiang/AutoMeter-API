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
      <el-table-column label="API数" align="center" prop="apiNums" width="180">
      </el-table-column>
      <el-table-column label="功能用例数" align="center" prop="apiFunctionCaseNums" width="300"/>
      <el-table-column label="性能用例数" align="center" prop="apiPerformanceCaseNums" width="300"/>
      <el-table-column label="功能测试集合数" align="center" prop="execplanFunnums" width="300"/>
      <el-table-column label="性能测试集合数" align="center" prop="execplanPerformancenums" width="300"/>
      <el-table-column label="场景数" align="center" prop="testConditions" width="380"/>
    </el-table>
  </div>
</template>

<script>
import { searchmycreateinfo } from '@/api/dashboard/myinfo'
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      listLoading: false, // 数据加载等待动画
      list: null,
      tmpcreator: {
        id: '',
        creatorid: '',
        projectid: ''
      }
    }
  },
  computed: {
    ...mapGetters(['name', 'sidebar', 'avatar', 'accountId'])
  },
  created() {
    this.tmpcreator.projectid = window.localStorage.getItem('pid')
    this.tmpcreator.creatorid = this.accountId
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.tmpcreator.creator = this.name
      console.log(this.tmpcreator)
      searchmycreateinfo(this.tmpcreator).then(response => {
        this.list = response.data
      })
    }
  }
}
</script>
