<template>
  <el-dialog title="条件顺序" :visible.sync="FormVisible">
    <el-form
      status-icon
      class="small-space"
      label-position="left"
      label-width="120px"
      style="width: 600px; margin-left:30px;"
    >
      <el-table
        :data="conditionorderList"
        border
        fit
        highlight-current-row
        style="width: 100%" >
        <el-table-column label="编号" align="center" width="50">
          <template slot-scope="scope">
            <span v-text="getIndex(scope.$index)"></span>
          </template>
        </el-table-column>
        <el-table-column prop="conditionname" align="center" label="父条件" width="150px"></el-table-column>
        <el-table-column prop="subconditionname" align="center" label="子条件"  width="80px"></el-table-column>
        <el-table-column prop="subconditiontype" align="center" label="条件类型"  width="70px"></el-table-column>
        <el-table-column prop="orderstatus" align="center" label="状态"  width="60px"></el-table-column>
        <el-table-column prop="conditionorder" align="center" label="顺序"  width="50px"></el-table-column>

        <el-table-column label="操作排序" align="center" width="140px" >
          <template slot-scope="scope">
            <el-button
              size="mini"
              :disabled="scope.$index===0"
              @click="moveUp(scope.$index,scope.row)"><i class="el-icon-arrow-up"></i></el-button>
            <el-button
              size="mini"
              :disabled="scope.$index===(conditionorderList.length-1)"
              @click="moveDown(scope.$index,scope.row)"><i class="el-icon-arrow-down"></i></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click.native.prevent="FormVisible = false">取消</el-button>
      <el-button
        type="success"
        @click.native.prevent="saveconditionorder"
      >保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { searchconditionorder, addconditionorder } from '@/api/condition/conditionorder'

export default {
  name: 'conditionorder',
  props: {
    FormVisible: false,
    searchconditionorder: {
      conditionid: '',
      conditiontype: ''
    }
  },
  watch: {
    FormVisible(val) {
      console.log(val)
    }
  },
  data() {
    return {
      conditionorderList: [], // 条件顺序显示列表
      total: 0, // 数据总数
      search: {
        page: 1,
        size: 10,
        projectid: ''
      }
    }
  },
  created() {
    this.search.projectid = window.localStorage.getItem('pid')
    console.log(1111111111111)
    this.searchConditionorder()
    console.log(2222222222222)
  },
  methods: {
    // 上移
    moveUp(index, row) {
      var that = this
      if (index > 0) {
        const upDate = that.conditionorderList[index - 1]
        that.conditionorderList.splice(index - 1, 1)
        that.conditionorderList.splice(index, 0, upDate)
      } else {
        alert('已经是第一条，不可上移')
      }
      console.log(that.conditionorderList)
    },

    // 下移
    moveDown(index, row) {
      var that = this
      console.log('下移', index, row)
      if ((index + 1) === that.conditionorderList.length) {
        alert('已经是最后一条，不可下移')
      } else {
        console.log(index)
        const downDate = that.conditionorderList[index + 1]
        that.conditionorderList.splice(index + 1, 1)
        that.conditionorderList.splice(index, 0, downDate)
      }
    },

    /**
     * 获取条件列表
     */
    searchConditionorder() {
      searchconditionorder(this.searchconditionorder).then(response => {
        this.conditionorderList = response.data
      }).catch(res => {
        this.$message.error('查询条件顺序失败')
      })
    },

    /**
     * 添加条件顺序
     */
    saveconditionorder() {
      this.saveconditionorderList = []
      for (let i = 0; i < this.conditionorderList.length; i++) {
        this.saveconditionorderList.push({
          'conditionid': this.conditionorderList[i].conditionid,
          'subconditionid': this.conditionorderList[i].subconditionid,
          'subconditiontype': this.conditionorderList[i].subconditiontype,
          'orderstatus': '已排序',
          'subconditionname': this.conditionorderList[i].subconditionname,
          'conditionname': this.conditionorderList[i].conditionname,
          'conditionorder': this.conditionorderList[i].conditionorder,
          'creator': this.name
        })
      }
      console.log(this.saveconditionorderList)
      addconditionorder(this.saveconditionorderList).then(() => {
        this.$message.success('条件顺序保存成功')
      }).catch(res => {
        this.$message.error('条件顺序保存失败')
      })
      this.ConditionOrderdialogFormVisible = false
    },

    /**
     * 改变每页数量
     * @param size 页大小
     */
    handleSizeChange(size) {
      this.search.page = 1
      this.search.size = size
      this.getconditionList()
    },
    /**
     * 改变页码
     * @param page 页号
     */
    handleCurrentChange(page) {
      this.search.page = page
      this.getconditionList()
    },
    /**
     * 表格序号
     * 可参考自定义表格序号
     * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
     * @param index 数据下标
     * @returns 表格序号
     */
    getIndex(index) {
      return (this.search.page - 1) * this.search.size + index + 1
    }

  }
}
</script>
