<template>
  <el-container style="height: 100vh;">
    <!-- 侧边栏 -->
    <el-aside width="200px" class="aside">
      <el-menu :default-active="activeMenu" @select="handleSelect" class="menu">
        <el-menu-item v-if="userType === 'company'" index="employees">
          <el-icon><User /></el-icon>
          <span>员工管理</span>
        </el-menu-item>
        <el-menu-item v-if="userType === 'company'" index="contracts">
          <el-icon><Document /></el-icon>
          <span>合同管理</span>
        </el-menu-item>
        <el-menu-item v-if="userType === 'company'" index="tasks">
          <el-icon><Setting /></el-icon>
          <span>任务管理</span>
        </el-menu-item>
        <el-menu-item v-if="userType === 'employee'" index="info">
          <el-icon><User /></el-icon>
          <span>个人信息管理</span>
        </el-menu-item>
        <el-menu-item v-if="userType === 'employee'" index="tasklist">
          <el-icon><List /></el-icon>
          <span>任务列表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体内容 -->
    <el-container style="flex-direction: column;">
      <!-- 头部 -->
      <el-header class="header" style="flex-shrink: 0;">
        合同管理系统
        <!-- 切换账号文本 -->
        <el-button class="switch-account" type="text" @click="switchAccount">切换账号</el-button>
      </el-header>

      <!-- 主体内容区 -->
      <el-main class="main-content" style="flex: 1; overflow-y: auto;">
        <router-view />
      </el-main>

      <!-- 页脚 -->
      <el-footer class="footer" style="flex-shrink: 0;">
        &copy; 2024 系统管理平台 | 版权所有
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import { User, Document, Setting, List } from '@element-plus/icons-vue'; // 引入图标

export default {
  components: {
    User,
    Document,
    Setting,
    List,
  },
  data() {
    return {
      activeMenu: 'employees',
      userType: sessionStorage.getItem('userType'),
    };
  },
  methods: {
    handleSelect(key) {
      this.activeMenu = key; // 设置选中的菜单
      this.$router.push(`/home/${key}`);
    },
    switchAccount() {
      // 清除会话信息
      sessionStorage.removeItem('userType');
      sessionStorage.removeItem('account');
      sessionStorage.removeItem('password');
      
      // 跳转到登录页面
      this.$router.push('/login');
    }
  },
};
</script>

<style scoped>
.el-container {
  height: 100vh;
}

.el-aside {
  background-color: #042b62a9;
  color: white;
  position: relative;
}

.el-header {
  background-color: #042b62a9;
  color: white;
  padding: 10px 20px;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  flex-shrink: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.switch-account {
  color: white;
  cursor: pointer;
  
}

.switch-account:hover {
  color: #4ab7e3;
}

.el-main {
  padding: 20px;
  background-color: #042b62a9;
  flex: 1;
  overflow-y: auto;
}

.el-footer {
  background-color: #042b62a9;
  color: white;
  text-align: center;
  padding: 10px;
  font-size: 14px;
  flex-shrink: 0;
}

.menu {
  background-color: #042b62a9;
  height: 100%;
}

.menu-item {
  padding: 12px;
  font-size: 16px;
  color: #ffffff;
}

.menu-item:hover {
  background-color: #4A6D89;
}

.el-icon {
  margin-right: 10px;
  color: #ffffff;
}

.el-menu-item {
  color: white;
}

.el-menu-item:hover {
  background-color: #4A6D89;
}
</style>
