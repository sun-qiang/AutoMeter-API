<template>
  <el-tabs v-model="variablesactiveName"  type="card" ref="variablestabs">
    <el-tab-pane label="接口变量" name="zero">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchtestvaraibles.testvariablesname" clearable @keyup.enter.native="searchTestVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchTestVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="TestVariablesList"
          :key="TestvariablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="TestVariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column label="变量名" align="center" prop="testvariablesname" width="100"/>
          <el-table-column label="变量类型" align="center" prop="valuetype" width="80"/>
          <el-table-column label="来源用例" align="center" prop="casename" width="100"/>
          <el-table-column label="来源微服务" align="center" prop="deployunitname" width="120"/>
          <el-table-column label="来源类型" align="center" prop="testvariablestype" width="80"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量表达式" align="center" prop="variablesexpress" width="90"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="variablesdes" width="120"/>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="searchtestvaraibles.page"
          :page-size="searchtestvaraibles.size"
          :total="Testvariablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
    <el-tab-pane label="数据库变量" name="first">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchdbvaraibles.dbvariablesname" clearable @keyup.enter.native="searchDbVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchDbVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="DbVariablesList"
          :key="DbvariablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="DbVariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="变量名" align="center" prop="dbvariablesname" width="150"/>
          <el-table-column label="变量类型" align="center" prop="valuetype" width="80"/>
          <el-table-column label="表列名" align="center" prop="fieldname" width="120"/>
          <el-table-column label="表行号" align="center" prop="roworder" width="60"/>
          <el-table-column :show-overflow-tooltip="true" label="来源" align="center" prop="conditionname" width="150"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="variablesdes" width="120"/>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickDbCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="DbhandleSizeChange"
          @current-change="DbhandleCurrentChange"
          :current-page="searchdbvaraibles.page"
          :page-size="searchdbvaraibles.size"
          :total="Dbvariablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
    <el-tab-pane label="脚本变量" name="second">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchscriptvaraibles.scriptvariablesname" clearable @keyup.enter.native="searchScriptVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchScriptVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="ScriptVariablesList"
          :key="ScriptvariablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="ScriptVariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column label="变量名" align="center" prop="scriptvariablesname" width="180"/>
          <el-table-column label="变量类型" align="center" prop="valuetype" width="80"/>
          <el-table-column label="来源" align="center" prop="conditionname" width="180"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="variablesdes" width="160"/>
          <el-table-column label="操作" align="center"
                           v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickScriptCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="ScripthandleSizeChange"
          @current-change="ScripthandleCurrentChange"
          :current-page="searchscriptvaraibles.page"
          :page-size="searchscriptvaraibles.size"
          :total="Scriptvariablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
    <el-tab-pane label="全局变量" name="third">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchglobalvaraibles.keyname" clearable @keyup.enter.native="searchGlobalVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchGlobalVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="GlobalVariablesList"
          :key="GlobalvariablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="GlobalVariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column label="变量名" align="center" prop="keyname" width="100"/>
          <el-table-column label="变量类型" align="center" prop="keyvalue" width="250"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="memo" width="120"/>
          <el-table-column label="操作" align="center"
                           v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickGlobalCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="GlobalhandleSizeChange"
          @current-change="GlobalhandleCurrentChange"
          :current-page="searchglobalvaraibles.page"
          :page-size="searchglobalvaraibles.size"
          :total="Globalvariablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
    <el-tab-pane label="随机变量" name="four">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchvaraibles.variablesname" clearable @keyup.enter.native="searchVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="VariablesList"
          :key="variablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="VariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column label="变量名" align="center" prop="variablesname" width="150"/>
          <el-table-column label="变量类型" align="center" prop="variablestype" width="120"/>
          <el-table-column label="变量条件" align="center" prop="variablecondition" width="140"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="memo" width="180"/>
          <el-table-column label="操作" align="center"
                           v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickRadomCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="RadomhandleSizeChange"
          @current-change="RadomhandleCurrentChange"
          :current-page="searchvaraibles.page"
          :page-size="searchvaraibles.size"
          :total="variablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
    <el-tab-pane label="环境变量" name="five">
      <template>
        <div class="filter-container">
          <el-form :inline="true">
            <el-form-item  label="变量名:">
              <el-input style="width: 130px" v-model="searchtestvaraibles.testvariablesname" clearable @keyup.enter.native="searchTestVariablesBy" placeholder="变量名"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchTestVariablesBy" :loading="btnLoading">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="TestVariablesList"
          :key="TestvariablesitemKey"
          element-loading-text="loading"
          border
          fit
          highlight-current-row
        >
          <el-table-column label="编号" align="center" width="45">
            <template slot-scope="scope">
              <span v-text="TestVariablesIndex(scope.$index)"></span>
            </template>
          </el-table-column>
          <el-table-column label="变量名" align="center" prop="testvariablesname" width="100"/>
          <el-table-column label="变量类型" align="center" prop="valuetype" width="80"/>
          <el-table-column label="来源用例" align="center" prop="casename" width="100"/>
          <el-table-column label="来源微服务" align="center" prop="deployunitname" width="120"/>
          <el-table-column label="来源类型" align="center" prop="testvariablestype" width="80"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量表达式" align="center" prop="variablesexpress" width="90"/>
          <el-table-column :show-overflow-tooltip="true"  label="变量描述" align="center" prop="variablesdes" width="120"/>
          <el-table-column label="操作" align="center"
                           v-if="hasPermission('executeplan:update')  || hasPermission('executeplan:delete')">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="mini"
                @click="clickCopy(scope.$index)"
              >复制使用</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="searchtestvaraibles.page"
          :page-size="searchtestvaraibles.size"
          :total="Testvariablestotal"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
        ></el-pagination>
      </template>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { search as searchtestvariables } from '@/api/testvariables/testvariables'
import { search as searchdbvariables } from '@/api/testvariables/dbvariables'
import { search as searchscriptvariables } from '@/api/testvariables/scriptvariables'
import { search as searchglobalvariables } from '@/api/testvariables/globalvariables'
import { search as searchvariables } from '@/api/testvariables/variables'

export default {
  name: 'uservariables',
  created() {
  },
  data() {
    return {
      tmpvariablesname: '',
      tmpglobalvariablesname: '',
      tmptestvariablesname: '',
      tmpdbvariablesname: '',
      tmpscriptvariablesname: '',
      TestvariablesitemKey: null,
      DbvariablesitemKey: null,
      ScriptvariablesitemKey: null,
      GlobalvariablesitemKey: null,
      variablesitemKey: null,
      variablesactiveName: 'zero',
      TestVariablesList: [], // 变量列表
      Testvariablestotal: 0, // 数据总数
      DbVariablesList: [], // 变量列表
      Dbvariablestotal: 0, // 数据总数
      ScriptVariablesList: [], // 变量列表
      Scriptvariablestotal: 0, // 数据总数
      GlobalVariablesList: [], // 变量列表
      Globalvariablestotal: 0, // 数据总数
      VariablesList: [], // 变量列表
      variablestotal: 0, // 数据总数
      searchtestvaraibles: {
        page: 1,
        size: 10,
        testvariablesname: ''
      },
      searchdbvaraibles: {
        page: 1,
        size: 10,
        dbvariablesname: ''
      },
      searchscriptvaraibles: {
        page: 1,
        size: 10,
        scriptvariablesname: ''
      },
      searchglobalvaraibles: {
        page: 1,
        size: 10,
        keyname: ''
      },
      searchvaraibles: {
        page: 1,
        size: 10,
        variablesname: ''
      }
    }
  },
  methods: {
    clickCopy(index) {
      this.$copyText('<' + this.TestVariablesList[index].testvariablesname + '>').then(function(e) {
        console.log('【复制成功】', e)
      }, function(e) {
        console.log('【复制失败】', e)
      })
      this.$message.success('变量已复制到剪切板！' + '<' + this.TestVariablesList[index].testvariablesname + '>')
    },
    clickDbCopy(index) {
      this.$copyText('<<' + this.DbVariablesList[index].dbvariablesname + '>>').then(function(e) {
        console.log('【复制成功】', e)
      }, function(e) {
        console.log('【复制失败】', e)
      })
      this.$message.success('变量已复制到剪切板！' + '[' + this.DbVariablesList[index].dbvariablesname + ']')
    },
    clickScriptCopy(index) {
      this.$copyText('{' + this.ScriptVariablesList[index].scriptvariablesname + '}').then(function(e) {
        console.log('【复制成功】', e)
      }, function(e) {
        console.log('【复制失败】', e)
      })
      this.$message.success('变量已复制到剪切板！' + '{' + this.ScriptVariablesList[index].scriptvariablesname + '}')
    },
    clickGlobalCopy(index) {
      this.$copyText('$' + this.GlobalVariablesList[index].keyname + '$').then(function(e) {
        console.log('【复制成功】', e)
      }, function(e) {
        console.log('【复制失败】', e)
      })
      this.$message.success('变量已复制到剪切板！' + '$' + this.GlobalVariablesList[index].keyname + '$')
    },
    clickRadomCopy(index) {
      this.$copyText('[' + this.VariablesList[index].variablesname + ']').then(function(e) {
        console.log('【复制成功】', e)
      }, function(e) {
        console.log('【复制失败】', e)
      })
      this.$message.success('变量已复制到剪切板！' + '[' + this.VariablesList[index].variablesname + ']')
    },
    getvariablesList() {
      this.searchvaraibles.variablesname = this.tmpvariablesname
      searchvariables(this.searchvaraibles).then(response => {
        this.VariablesList = response.data.list
        this.variablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
    },
    gettestvariablesList() {
      this.searchtestvaraibles.testvariablesname = this.tmptestvariablesname
      searchtestvariables(this.searchtestvaraibles).then(response => {
        this.TestVariablesList = response.data.list
        this.Testvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
    },
    getdbvariablesList() {
      this.searchdbvaraibles.dbvariablesname = this.tmpdbvariablesname
      searchdbvariables(this.searchdbvaraibles).then(response => {
        this.DbVariablesList = response.data.list
        this.Dbvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
    },
    getscriptvariablesList() {
      this.searchscriptvaraibles.scriptvariablesname = this.tmpscriptvariablesname
      searchscriptvariables(this.searchscriptvaraibles).then(response => {
        this.ScriptVariablesList = response.data.list
        this.Scriptvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
    },
    getglobalvariablesList() {
      this.searchglobalvaraibles.keyname = this.tmpglobalvariablesname
      searchglobalvariables(this.searchglobalvaraibles).then(response => {
        this.GlobalVariablesList = response.data.list
        this.Globalvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
    },
    searchVariablesBy() {
      this.variablesitemKey = Math.random()
      searchvariables(this.searchvaraibles).then(response => {
        this.VariablesList = response.data.list
        this.variablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
      this.tmpvariablesname = this.searchvaraibles.variablesname
    },
    searchGlobalVariablesBy() {
      this.GlobalvariablesitemKey = Math.random()
      searchglobalvariables(this.searchglobalvaraibles).then(response => {
        this.GlobalVariablesList = response.data.list
        this.Globalvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
      this.tmpglobalvariablesname = this.searchglobalvaraibles.keyname
    },
    searchScriptVariablesBy() {
      this.ScriptvariablesitemKey = Math.random()
      searchscriptvariables(this.searchscriptvaraibles).then(response => {
        this.ScriptVariablesList = response.data.list
        this.Scriptvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
      this.tmpscriptvariablesname = this.searchscriptvaraibles.scriptvariablesname
    },
    searchDbVariablesBy() {
      this.DbvariablesitemKey = Math.random()
      searchdbvariables(this.searchdbvaraibles).then(response => {
        this.DbVariablesList = response.data.list
        this.Dbvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
      this.tmpdbvariablesname = this.searchdbvaraibles.dbvariablesname
    },
    searchTestVariablesBy() {
      this.TestvariablesitemKey = Math.random()
      searchtestvariables(this.searchtestvaraibles).then(response => {
        this.TestVariablesList = response.data.list
        this.Testvariablestotal = response.data.total
      }).catch(res => {
        this.$message.error('加载列表失败')
      })
      this.tmptestvariablesname = this.searchtestvaraibles.testvariablesname
    },
    VariablesIndex(index) {
      return (this.searchvaraibles.page - 1) * this.searchvaraibles.size + index + 1
    },
    TestVariablesIndex(index) {
      return (this.searchtestvaraibles.page - 1) * this.searchtestvaraibles.size + index + 1
    },
    DbVariablesIndex(index) {
      return (this.searchdbvaraibles.page - 1) * this.searchdbvaraibles.size + index + 1
    },
    ScriptVariablesIndex(index) {
      return (this.searchscriptvaraibles.page - 1) * this.searchscriptvaraibles.size + index + 1
    },
    GlobalVariablesIndex(index) {
      return (this.searchglobalvaraibles.page - 1) * this.searchglobalvaraibles.size + index + 1
    },
    RadomhandleSizeChange(size) {
      this.searchvaraibles.page = 1
      this.searchvaraibles.size = size
      this.getvariablesList()
    },
    handleSizeChange(size) {
      this.searchtestvaraibles.page = 1
      this.searchtestvaraibles.size = size
      this.gettestvariablesList()
    },
    DbhandleSizeChange(size) {
      this.searchdbvaraibles.page = 1
      this.searchdbvaraibles.size = size
      this.getdbvariablesList()
    },
    ScripthandleSizeChange(size) {
      this.searchscriptvaraibles.page = 1
      this.searchscriptvaraibles.size = size
      this.getscriptvariablesList()
    },
    GlobalhandleSizeChange(size) {
      this.searchglobalvaraibles.page = 1
      this.searchglobalvaraibles.size = size
      this.getglobalvariablesList()
    },
    handleCurrentChange(page) {
      this.searchtestvaraibles.page = page
      this.gettestvariablesList()
    },
    DbhandleCurrentChange(page) {
      this.searchdbvaraibles.page = page
      this.getdbvariablesList()
    },
    ScripthandleCurrentChange(page) {
      this.searchscriptvaraibles.page = page
      this.getscriptvariablesList()
    },
    GlobalhandleCurrentChange(page) {
      this.searchglobalvaraibles.page = page
      this.getglobalvariablesList()
    },
    RadomhandleCurrentChange(page) {
      this.searchvaraibles.page = page
      this.getvariablesList()
    }
  }
}
</script>
