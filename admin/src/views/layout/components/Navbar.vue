<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened" />
    <Levelbar />

      <el-select  class="project-container" v-model="projectname"   placeholder="默认项目" @change="exectypeselectChanged($event)" >
        <div v-for="(project, index) in pList" :key="index">
          <el-option :label="project.projectname" :value="project.projectname" required />
        </div>
      </el-select>

    <el-dropdown class="account-container">
      <span class="el-dropdown-link">
        {{ nickname }}
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <router-link class="inlineBlock" to="/account/detail">
          <el-dropdown-item>账户中心</el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="logout" style="display:block;">注销</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Levelbar from './Levelbar'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Levelbar,
    Hamburger
  },
  data() {
    return {
      pList: [],
      pid: 1,
      projectname: '演示项目'
    }
  },
  computed: {
    ...mapGetters(['nickname', 'sidebar', 'projectlist', 'projectid', 'pname'])
  },

  created() {
    this.getprojectList()
    var ppid = window.localStorage.getItem('pid')
    console.log(ppid)
    if (ppid === null) {
      window.localStorage.setItem('pid', 1)
    }
    this.pid = window.localStorage.getItem('pid')
    var ppname = window.localStorage.getItem('pname')
    console.log(ppname)
    if (ppname === null) {
      window.localStorage.setItem('pname', '演示项目')
    }
    this.projectname = window.localStorage.getItem('pname')
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    async getprojectList() {
      await this.$store.dispatch('Prjectinfo')
      this.pList = this.projectlist
    },
    exectypeselectChanged(e) {
      for (let i = 0; i < this.projectlist.length; i++) {
        if (this.projectlist[i].projectname === e) {
          this.pid = this.projectlist[i].id
        }
      }
      console.log(this.pid)
      console.log(e)
      window.localStorage.removeItem('pid')
      window.localStorage.removeItem('pname')
      window.localStorage.setItem('pid', this.pid)
      window.localStorage.setItem('pname', e)
      const path = this.$route.path
      // this.$router.replace('/refresh')
      location.reload()
      this.$router.replace(path)
      // this.$router.replace({
      //   // query: {
      //   //   t: e
      //   // }
      // })
      // this.pList = JSON.parse(window.localStorage.getItem('projectlist'))
      // this.$store.commit('SETPJ', this.pList)

      // this.$store.commit('SET_PID', this.pid)
      // this.$store.commit('SET_PName', e)
    },
    logout() {
      window.sessionStorage.removeItem('tabViews')
      this.$store.dispatch('tagsView/delAllVisitedViews')
      this.$store.dispatch('Logout').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 55px;
  line-height: 55px;
  border-radius: 0 !important;
  .hamburger-container {
    line-height: 58px;
    height: 55px;
    float: left;
    padding: 0 10px;
  }
  .errLog-container {
    display: inline-block;
    position: absolute;
    right: 150px;
  }
  .account-container {
    height: 45px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .el-dropdown-link {
      cursor: pointer;
      color: #409eff;
    }
    .el-icon-arrow-down {
      font-size: 12px;
    }
  }
  .project-container {
    height: 45px;
    width: 200px;
    display: inline-block;
    position: absolute;
    right: 120px;
    .el-dropdown-link {
      cursor: pointer;
      color: #409eff;
    }
    .el-icon-arrow-down {
      font-size: 12px;
    }
  }
}
</style>
