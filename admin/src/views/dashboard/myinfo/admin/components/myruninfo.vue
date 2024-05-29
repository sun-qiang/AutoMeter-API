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
      <el-table-column label="功能用例次数" align="center" prop="funtotalcasenums" width="230"/>
      <el-table-column label="功能用例时长(秒)" align="center" prop="funtotalcosttime" width="230"/>
      <el-table-column label="功能成功次数" align="center" prop="funtotalpasscasenums" width="230"/>
      <el-table-column label="功能失败次数" align="center" prop="funtotalfailcasenums" width="230"/>
      <el-table-column label="性能用例次数" align="center" prop="pertotalcasenums" width="230"/>
      <el-table-column label="性能用例时长(秒)" align="center" prop="pertotalcosttime" width="230"/>
      <el-table-column label="性能成功次数" align="center" prop="pertotalpasscasenums" width="230"/>
      <el-table-column label="性能失败次数" align="center" prop="pertotalfailcasenums" width="230"/>
    </el-table>
  </div>
</template>

<script>
import { searchmyruninfo } from '@/api/dashboard/myinfo'
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
      searchmyruninfo(this.tmpcreator).then(response => {
        this.list = response.data
      })
    }
  }
}
</script>
