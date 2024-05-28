<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button
            type="success"
            size="mini"
            icon="el-icon-refresh"
            v-if="hasPermission('dictionary:list')"
            @click.native.prevent="getDictionaryList"
          >刷新</el-button>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            v-if="hasPermission('dictionary:add')"
            @click.native.prevent="showAddDicDialog"
          >添加字典</el-button>
        </el-form-item>

        <span v-if="hasPermission('dictionary:search')">
          <el-form-item>
            <el-input v-model="search.dicname" @keyup.enter.native="searchBy" placeholder="字典名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="search.dicitemname" @keyup.enter.native="searchBy" placeholder="字典项名"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchBy" :loading="btnLoading">查询</el-button>
          </el-form-item>
        </span>
      </el-form>
    </div>
    <el-table
      :data="dictionaryList"
      v-loading.body="listLoading"
      element-loading-text="loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="编号" align="center" width="60">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"></span>
        </template>
      </el-table-column>
      <el-table-column label="字典名" align="center" prop="dicname" width="140"/>
      <el-table-column label="字典编码" align="center" prop="diccode" width="140"/>
      <el-table-column :show-overflow-tooltip="true" label="字典项名" align="center" prop="dicitemname" width="140"/>
      <el-table-column :show-overflow-tooltip="true" label="字典项值" align="center" prop="dicitmevalue" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="最后修改时间" align="center" prop="lastmodifyTime" width="160">
        <template slot-scope="scope">{{ unix2CurrentTime(scope.row.lastmodifyTime) }}
        </template>
      </el-table-column>

      <el-table-column label="管理" align="center"
                       v-if="hasPermission('dictionary:update')  || hasPermission('dictionary:delete')">
        <template slot-scope="scope">
          <el-button
            type="warning"
            size="mini"
            v-if="hasPermission('dictionary:update') && scope.row.id !== id"
            @click.native.prevent="showUpdateDictionaryDialog(scope.$index)"
          >修改</el-button>
          <el-button
            type="danger"
            size="mini"
            v-if="hasPermission('dictionary:delete') && scope.row.id !== id"
            @click.native.prevent="removeDictionary(scope.$index)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.page"
      :page-size="listQuery.size"
      :total="total"
      :page-sizes="[10, 20, 30, 40]"
      layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <el-dialog :title="textMap[dialogStatus]" width="800px"  :visible.sync="dialogFormVisible">
      <el-form
        status-icon
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 400px; margin-left:50px;"
        :model="tmpDictionary"
        ref="tmpDictionary"
      >
        <el-form-item label="字典名" prop="dicname" required>
          <el-input style="width: 500px"
            type="text"
            prefix-icon="el-icon-edit"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpDictionary.dicname"
          />
        </el-form-item>
        <el-form-item label="字典编码" prop="diccode">
          <el-input style="width: 500px"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpDictionary.diccode"
          />
        </el-form-item>
        <el-form-item label="字典项名" prop="dicitemname">
          <el-input style="width: 500px"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpDictionary.dicitemname"
          />
        </el-form-item>
        <el-form-item label="字典项值" prop="dicitmevalue">
          <el-input style="width: 500px"
            type="text"
            prefix-icon="el-icon-message"
            auto-complete="off"
            :disabled="dialogStatus === 'updateRole'"
            v-model="tmpDictionary.dicitmevalue"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native.prevent="dialogFormVisible = false">取消</el-button>
        <el-button
          type="danger"
          v-if="dialogStatus === 'add'"
          @click.native.prevent="$refs['tmpDictionary'].resetFields()"
        >重置</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'add'"
          :loading="btnLoading"
          @click.native.prevent="addDic"
        >添加</el-button>
        <el-button
          type="success"
          v-if="dialogStatus === 'update'"
          :loading="btnLoading"
          @click.native.prevent="updateDictionary"
        >修改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { search, addDic, updateDic, removeDic } from '@/api/system/dictionary'
  import { unix2CurrentTime } from '@/utils'

  export default {
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
        id: null,
        tmpdicname: null,
        tmpdicitemname: null,
        dictionaryList: [], // 字典列表
        listLoading: false, // 数据加载等待动画
        total: 0, // 数据总数
        listQuery: {
          page: 1, // 页码
          size: 10, // 每页数量
          listLoading: true,
          dicname: null,
          dicitemname: null
        },
        dialogStatus: 'add',
        dialogFormVisible: false,
        textMap: {
          updateRole: '修改字典',
          update: '修改字典',
          add: '添加字典'
        },
        btnLoading: false, // 按钮等待动画
        tmpDictionary: {
          id: '',
          dicname: '',
          diccode: '',
          dicitemname: '',
          dicitmevalue: ''
        },
        search: {
          page: 1,
          size: 10,
          dicname: null,
          dicitemname: null
        }
      }
    },

    created() {
      this.getDictionaryList()
    },

    methods: {
      unix2CurrentTime,

      /**
       * 获取字典列表
       */
      getDictionaryList() {
        this.listLoading = true
        this.search.dicitemname = this.tmpdicitemname
        search(this.search).then(response => {
          this.dictionaryList = response.data.list
          this.total = response.data.total
          this.listLoading = false
        }).catch(res => {
          this.$message.error('加载字典列表失败')
        })
      },
      searchBy() {
        this.btnLoading = true
        this.listLoading = true
        this.search.page = this.listQuery.page
        this.search.size = this.listQuery.size
        search(this.search).then(response => {
          this.dictionaryList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.tmpdicname = this.search.dicname
        this.tmpdicitemname = this.search.dicitemname
        this.btnLoading = false
        this.listLoading = false
      },

      searchBypageing() {
        this.btnLoading = true
        this.listLoading = true
        this.listQuery.dicname = this.tmpdicname
        this.listQuery.dicitemname = this.tmpdicitemname
        search(this.listQuery).then(response => {
          this.dictionaryList = response.data.list
          this.total = response.data.total
        }).catch(res => {
          this.$message.error('搜索失败')
        })
        this.listLoading = false
        this.btnLoading = false
      },

      /**
       * 改变每页数量
       * @param size 页大小
       */
      handleSizeChange(size) {
        this.listQuery.size = size
        this.listQuery.page = 1
        this.searchBypageing()
      },
      /**
       * 改变页码
       * @param page 页号
       */
      handleCurrentChange(page) {
        this.listQuery.page = page
        this.searchBypageing()
      },
      /**
       * 表格序号
       * 可参考自定义表格序号
       * http://element-cn.eleme.io/#/zh-CN/component/table#zi-ding-yi-suo-yin
       * @param index 数据下标
       * @returns 表格序号
       */
      getIndex(index) {
        return (this.listQuery.page - 1) * this.listQuery.size + index + 1
      },
      /**
       * 显示添加用户对话框
       */
      showAddDicDialog() {
        // 显示新增对话框
        this.dialogFormVisible = true
        this.dialogStatus = 'add'
        this.tmpDictionary.id = ''
        this.tmpDictionary.dicname = ''
        this.tmpDictionary.diccode = ''
        this.tmpDictionary.dicitemname = ''
        this.tmpDictionary.dicitmevalue = ''
      },
      /**
       * 添加字典
       */
      addDic() {
        this.$refs.tmpDictionary.validate(valid => {
          if (valid) {
            this.btnLoading = true
            addDic(this.tmpDictionary).then(() => {
              this.$message.success('添加成功')
              this.getDictionaryList()
              this.dialogFormVisible = false
              this.btnLoading = false
            }).catch(res => {
              this.$message.error('添加字典失败')
              this.btnLoading = false
            })
          }
        })
      },
      /**
       * 显示修改用户对话框
       * @param index 用户下标
       */
      showUpdateDictionaryDialog(index) {
        this.dialogFormVisible = true
        this.dialogStatus = 'update'
        this.tmpDictionary.id = this.dictionaryList[index].id
        this.tmpDictionary.dicname = this.dictionaryList[index].dicname
        this.tmpDictionary.diccode = this.dictionaryList[index].diccode
        this.tmpDictionary.dicitemname = this.dictionaryList[index].dicitemname
        this.tmpDictionary.dicitmevalue = this.dictionaryList[index].dicitmevalue
      },
      /**
       * 更新用户
       */
      updateDictionary() {
        updateDic(this.tmpDictionary).then(() => {
          this.$message.success('更新成功')
          this.getDictionaryList()
          this.dialogFormVisible = false
        }).catch(res => {
          this.$message.error('更新失败')
        })
      },

      /**
       * 删除字典
       * @param index 用户下标
       */
      removeDictionary(index) {
        this.$confirm('删除该字典？', '警告', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          const id = this.dictionaryList[index].id
          removeDic(id).then(() => {
            this.$message.success('删除成功')
            this.getDictionaryList()
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      },

      /**
       * 字典资料是否唯一
       * @param 字典
       */
      isUniqueDetail(dictionary) {
        for (let i = 0; i < this.dictionaryList.length; i++) {
          if (this.dictionaryList[i].id !== dictionary.id) { // 排除自己
            if (this.dictionaryList[i].dicname === dictionary.dicname) {
              this.$message.error('字典名已存在')
              return false
            }
            if (this.dictionaryList[i].dicitemname === dictionary.dicitemname) {
              this.$message.error('字典项名已存在')
              return false
            }
          }
        }
        return true
      }
    }
  }
</script>
