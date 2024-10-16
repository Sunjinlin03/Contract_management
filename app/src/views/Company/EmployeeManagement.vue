<template>
  <div class="Employee-Management">
    <!-- 搜索框 -->
    <el-input v-model="searchText" placeholder="请输入员工姓名" style="width: 300px;" />
    <el-button type="primary" @click="searchEmployees" style="margin-left: 10px;">搜索</el-button>

    <!-- 添加员工按钮 -->
    <el-button type="primary" @click="toggleAddEmployeeForm" style="margin-left: 10px;">{{ addEmployeeFormVisible ? '取消添加' : '添加员工' }}</el-button>

    <!-- 添加员工表单（内嵌式） -->
    <el-form v-if="addEmployeeFormVisible" :model="employeeForm" label-width="100px" style="margin-top: 20px;">
      <el-form-item label="员工编号">
        <el-input v-model="employeeForm.employeeNumber" />
      </el-form-item>
      <el-form-item label="员工姓名">
        <el-input v-model="employeeForm.employeeName" />
      </el-form-item>
      <el-form-item label="员工账号">
        <el-input v-model="employeeForm.employeeAccount" />
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="employeeForm.contactInfo" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitAddEmployee">确认添加</el-button>
        <el-button @click="toggleAddEmployeeForm">取消</el-button>
      </el-form-item>
    </el-form>

    <!-- 员工表格 -->
    <el-table :data="employeeList" style="width: 100%; margin-top: 20px;">
      <!-- 新增员工ID列 -->
      <el-table-column prop="employeeId" label="员工ID" />
      <el-table-column label="员工编号">
        <template #default="scope">
          <el-input v-if="editingRowId === scope.row.employeeId" v-model="scope.row.employeeNumber" />
          <span v-else>{{ scope.row.employeeNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="employeeAccount" label="员工账号" />
      <el-table-column label="员工姓名">
        <template #default="scope">
          <el-input v-if="editingRowId === scope.row.employeeId" v-model="scope.row.employeeName" />
          <span v-else>{{ scope.row.employeeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系方式">
        <template #default="scope">
          <el-input v-if="editingRowId === scope.row.employeeId" v-model="scope.row.contactInfo" />
          <span v-else>{{ scope.row.contactInfo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <!-- 如果当前行处于编辑状态，显示保存和取消按钮 -->
          <template v-if="editingRowId === scope.row.employeeId">
            <el-button size="mini" type="primary" @click="submitEdit(scope.row)">保存</el-button>
            <el-button size="mini" @click="cancelEdit">取消</el-button>
            </template>
            <!-- 如果当前行不处于编辑状态，显示修改和删除按钮 -->
            <template v-else>
            <el-button size="mini" @click="enableEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.employeeIdId)">删除</el-button>
            </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      :current-page.sync="currentPage"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import { getAllEmployees, addEmployee, deleteEmployee, updateEmployee } from '@/api/employee';

export default {
  data() {
    return {
      searchText: '',
      employeeList: [],
      addEmployeeFormVisible: false,
      employeeForm: {
        employeeNumber: '',
        employeeName: '',
        employeeAccount: '',
        contactInfo: '',
      },
      currentPage: 1,
      pageSize: 10,
      total: 0,
      editingRowId: null, // 当前正在编辑的员工ID
      
    };
  },
  methods: {
    toggleAddEmployeeForm() {
      this.addEmployeeFormVisible = !this.addEmployeeFormVisible;
      if (!this.addEmployeeFormVisible) {
        this.resetForm();
      }
    },
    resetForm() {
      this.employeeForm = {
        employeeNumber: '',
        employeeName: '',
        employeeAccount: '',
        contactInfo: '',
      };
    },
    async submitAddEmployee() {
      try {
        const res = await addEmployee(this.employeeForm);
        if (res.code === 200) {
          this.$message.success('员工添加成功');
          this.toggleAddEmployeeForm();
          this.getEmployees();
        } else {
          this.$message.error(res.message);
        }
      } catch (error) {
        this.$message.error('添加员工失败');
      }
    },
    async getEmployees() {
      const res = await getAllEmployees(this.currentPage, this.pageSize, this.searchText);
      if (res.code === 200) {
        this.employeeList = res.data.records;
        this.total = res.data.total;
      }
    },
    async searchEmployees() {
      this.currentPage = 1;
      await this.getEmployees();
    },
    async handleDelete(row) {
      try {
        await deleteEmployee(row.employeeId);
        this.$message.success('删除成功');
        this.getEmployees();
      } catch (error) {
        this.$message.error('删除失败');
      }
    },
    handlePageChange(page) {
      this.currentPage = page;
      this.getEmployees();
    },
    enableEdit(row) {
    this.editingRowId = row.employeeId; // 将当前行切换为编辑状态
    },
    cancelEdit() {
      this.editingRowId = null; // 取消编辑，恢复状态
      this.getEmployees(); // 可选：刷新表格数据，确保没有修改
    },
    async submitEdit(row) {
      try {
        // 发送更新请求，传递更新后的数据
        const res = await updateEmployee(row);
        if (res.code === 200) {
          this.$message.success('修改成功');
          this.editingRowId = null; // 退出编辑状态
          this.getEmployees(); // 刷新员工列表
        } else {
          this.$message.error(res.message);
        }
      } catch (error) {
        this.$message.error('修改失败');
      }
    },
  },
  mounted() {
    this.getEmployees();
  }
};
</script>

<style scoped>
.el-form {
  margin-bottom: 20px;
}
.el-input {
  width: 300px;
}
.el-table {
  margin-top: 20px;
}
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
