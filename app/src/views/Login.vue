<template>
  <div class="login-page">
    <div class="login-container">
      <h2 class="login-title">欢迎登录</h2>
      <el-form :model="form" label-position="top" class="login-form">
        <el-form-item>
          <el-input v-model="form.account" placeholder="请输入账号" prefix-icon="User">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="请输入密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="选择身份">
          <el-radio-group v-model="form.userType">
            <el-radio label="company">企业</el-radio>
            <el-radio label="employee">员工</el-radio>
          </el-radio-group>
        </el-form-item>
        <div class="login-actions">
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button type="text" @click="goToRegister">没有账号？去注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { User, Lock } from '@element-plus/icons-vue'; // 引入图标
import { loginCompany, loginEmployee } from '@/api/auth'; // 导入登录 API

export default {
  data() {
    return {
      form: {
        account: '',
        password: '',
        userType: 'company', // 默认选择企业身份
      },
    };
  },
  components: {
    User,
    Lock,
  },
  setup() {
    const router = useRouter();
    return { router };
  },
  methods: {
    async handleLogin() {
      try {
        // 根据用户类型选择不同的登录方法
        const loginParams = {
          account: this.form.account,
          password: this.form.password,
        };

        let response;
        if (this.form.userType === 'company') {
          response = await loginCompany(loginParams);
        } else {
          response = await loginEmployee(loginParams);
        }

        // 检查响应状态
        if (response.code === 200) {
          // 登录成功，保存用户类型
          sessionStorage.setItem('userType', this.form.userType);
          this.$router.push('/home'); // 登录成功后跳转主页
        } else {
          this.$message.error(response.message); // 提示错误信息
        }
      } catch (error) {
        console.error(error);
        this.$message.error('登录失败，请重试');
      }
    },
    goToRegister() {
      this.$router.push('/register'); // 跳转到注册页面
    },
  },
};
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #0b4495;
}

.login-container {
  background-color: rgba(255, 255, 255, 0.557);
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.login-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.login-form {
  width: 100%;
}

.login-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.el-button--primary {
  background-color: #042b62;
  border-color:  #042b62;
}

.el-button--primary:hover {
  background-color: #2f639b;
}

.el-button--text {
  color: #333;
  font-weight: bold;
}
</style>
