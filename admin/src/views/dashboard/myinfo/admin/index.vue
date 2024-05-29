<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="1">
      <div>
        <label>我创建的:</label>
      </div>
<!--      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 1200}" :lg="{span: 100}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:5px;">-->
        <mycreateinfo />
<!--      </el-col>-->
    </el-row>
    <el-row :gutter="1">
      <div>
        <label>我运行的:</label>
      </div>
<!--      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 100}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:5px;">-->
        <RunTable />
<!--      </el-col>-->
    </el-row>
    <el-row :gutter="1">
      <div>
        <label>最近的功能测试:</label>
      </div>
<!--      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 100}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:5px;">-->
        <myrecentfunctioninfo />
<!--      </el-col>-->
    </el-row>
    <el-row :gutter="1">
      <div>
        <label>最近的性能测试:</label>
      </div>
<!--      <el-col :xs="{span: 24}" :sm="{span: 24}" :md="{span: 24}" :lg="{span: 100}" :xl="{span: 12}" style="padding-right:8px;margin-bottom:5px;">-->
        <myrecentperformanceinfo />
<!--      </el-col>-->
    </el-row>
  </div>
</template>

<script>

import mycreateinfo from './components/mycreateinfo'
import RunTable from './components/myruninfo'
import myrecentfunctioninfo from './components/myrecentfunctioninfo'
import myrecentperformanceinfo from './components/myrecentperformanceinfo'

export default {
  components: {
    mycreateinfo,
    RunTable,
    myrecentfunctioninfo,
    myrecentperformanceinfo
  },
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 10px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 10px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 10px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
