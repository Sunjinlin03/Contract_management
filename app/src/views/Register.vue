<template>
  <div class="register-page">
    <div class="register-container">
      <h2 class="register-title">公司注册</h2>
      <el-form :model="form" label-position="top" :rules="rules" ref="registerForm" class="register-form">
        <el-form-item label="公司名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入公司名称"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input v-model="form.account" placeholder="请输入账号" prefix-icon="User">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码">
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <div class="register-actions">
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button type="text" @click="goToLogin">已有账号？去登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { User, Lock } from '@element-plus/icons-vue';
import { registerCompany } from '@/api/auth'; // 导入后端注册接口

export default {
  data() {
    return {
      form: {
        companyName: '',
        account: '',
        password: '',
        confirmPassword: '',
      },
      rules: {
        companyName: [
          { required: true, message: '请输入公司名称', trigger: 'blur' },
        ],
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (value !== this.form.password) {
                callback(new Error('两次输入密码不一致'));
              } else {
                callback();
              }
            }, 
            trigger: 'blur' 
          },
        ],
      },
    };
  },
  components: {
    User,
    Lock,
  },
  setup() {
    const router = useRouter();
    const registerForm = ref(null);

    return { router, registerForm };
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          const params = {
            companyName: this.form.companyName,
            companyAccount: this.form.account,
            companyPassword: this.form.password,
          };

          // 调用后端注册接口
          registerCompany(params)
            .then(response => {
              console.log('注册成功', response);
              this.$router.push('/login'); // 注册成功后跳转到登录页面
            })
            .catch(error => {
              console.error('注册失败', error);
              // 处理错误，比如显示错误信息
            });
        } else {
          console.log('注册表单填写有误');
        }
      });
    },
    goToLogin() {
      this.$router.push('/login'); // 跳转到登录页面
    },
  },
};
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #116548;
}

.register-container {
  background-color: rgba(255, 255, 255, 0.557);
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
  width: 400px;
}

.register-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.register-form {
  width: 100%;
}

.register-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.el-button--primary {
  background-color: #116548;
  border-color: #116548;
}

.el-button--primary:hover {
  background-color: #189f3f;
}

.el-button--text {
  color:#333;
  font-weight: bold;
}
</style>
