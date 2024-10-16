<template>
    <div class="Contract-Management">
      <!-- 搜索框 -->
      <el-input v-model="searchText" placeholder="请输入项目名称" style="width: 300px;" />
      <el-button type="primary" @click="searchContracts" style="margin-left: 10px;">搜索</el-button>
  
      <!-- 添加合同按钮 -->
      <el-button type="primary" @click="toggleAddContractForm" style="margin-left: 10px;">{{ addContractFormVisible ? '取消添加' : '添加合同' }}</el-button>
  
      <!-- 添加/修改合同表单 -->
      <el-form v-if="addContractFormVisible" :model="contractForm" label-width="100px" style="margin-top: 20px;">
        <el-form-item label="合同编号">
          <el-input v-model="contractForm.contractNumber" />
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="contractForm.projectName" />
        </el-form-item>
        <el-form-item label="甲方">
          <el-input v-model="contractForm.partyA" />
        </el-form-item>
        <el-form-item label="乙方">
          <el-input v-model="contractForm.partyB" />
        </el-form-item>
        <el-form-item label="总价">
          <el-input v-model="contractForm.totalPrice" />
        </el-form-item>
        <el-form-item label="税额">
          <el-input v-model="contractForm.taxAmount" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitContract">确认添加</el-button>
          <el-button @click="toggleAddContractForm">取消</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 合同表格 -->
      <el-table :data="contractList" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="contractId" label="合同ID" />
        <el-table-column label="合同编号">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.contractNumber" />
              <span v-else>{{ scope.row.contractNumber }}</span>
            </template>
        </el-table-column>
        <el-table-column label="项目名称">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.projectName" />
              <span v-else>{{ scope.row.projectName }}</span>
            </template>
        </el-table-column>
        <el-table-column label="甲方">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.partyA" />
              <span v-else>{{ scope.row.partyA }}</span>
            </template>
        </el-table-column>
        <el-table-column label="乙方">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.partyB" />
              <span v-else>{{ scope.row.partyB }}</span>
            </template>
        </el-table-column>
        <el-table-column label="签订时间">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.signingDate" />
              <span v-else>{{ scope.row.signingDate }}</span>
            </template>
        </el-table-column>
        <el-table-column label="总价">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.totalPrice" />
              <span v-else>{{ scope.row.totalPrice }}</span>
            </template>
        </el-table-column>
        <el-table-column label="税额">
            <template #default="scope">
              <el-input v-if="editingRowId === scope.row.contractId" v-model="scope.row.taxAmount" />
              <span v-else>{{ scope.row.taxAmount }}</span>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="170">
          <template #default="scope">
            <!-- 如果当前行处于编辑状态，显示保存和取消按钮 -->
            <template v-if="editingRowId === scope.row.contractId">
            <el-button size="mini" type="primary" @click="submitEdit(scope.row)">保存</el-button>
            <el-button size="mini" @click="cancelEdit">取消</el-button>
            </template>
            <!-- 如果当前行不处于编辑状态，显示修改和删除按钮 -->
            <template v-else>
            <el-button size="mini" @click="enableEdit(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.contractId)">删除</el-button>
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
  import { createContract, deleteContract, updateContract, getContractByCompany } from '@/api/contract';
  
  export default {
    data() {
      return {
        searchText: '',
        contractList: [],
        addContractFormVisible: false,
        contractForm: {
          contractNumber: '',
          projectName: '',
          partyA: '',
          partyB: '',
          signingDate: '',
          totalPrice: '',
          taxAmount: '',
        },
        currentPage: 1,
        pageSize: 10,
        total: 0,
        editingRowId: null, // 当前正在编辑的合同ID
      };
    },
    methods: {
      // 显示或隐藏添加合同表单
      toggleAddContractForm() {
        this.addContractFormVisible = !this.addContractFormVisible;
        if (!this.addContractFormVisible) {
          this.resetForm();
        }
      },
      // 重置表单
      resetForm() {
        this.contractForm = {
          contractNumber: '',
          projectName: '',
          partyA: '',
          partyB: '',
          totalPrice: '',
          taxAmount: '',
        };
      },
      // 提交合同
      async submitContract() {
        try {
            // 添加合同
            const res = await createContract(this.contractForm);
            if (res.code === 200) {
              this.$message.success('合同添加成功');
              this.toggleAddContractForm();
              this.getContracts();
            } else {
              this.$message.error(res.message);
            }
        } catch (error) {
          this.$message.error('添加员工失败');
        }
      },
      // 获取合同列表
      async getContracts() {
        const res = await getContractByCompany(this.currentPage, this.pageSize, this.searchText);
        if (res.code === 200) {
          this.contractList = res.data.records;
          this.total = res.data.total;
        }
      },
      // 搜索合同
      async searchContracts() {
        this.currentPage = 1;
        await this.getContracts();
      },
      // 删除合同
      async handleDelete(contractId) {
        try {
          const res = await deleteContract(contractId);
          if (res.code === 200) {
            this.$message.success('合同删除成功');
            this.getContracts();
          } else {
            this.$message.error(res.message);
          }
        } catch (error) {
          this.$message.error('删除失败');
        }
      },
      // 处理分页
      handlePageChange(page) {
        this.currentPage = page;
        this.getContracts();
      },
      enableEdit(row) {
      this.editingRowId = row.contractId; // 将当前行切换为编辑状态
      },
      cancelEdit() {
        this.editingRowId = null; // 取消编辑，恢复状态
        this.getContracts(); // 可选：刷新表格数据，确保没有修改
      },
      async submitEdit(row) {
        try {
          // 发送更新请求，传递更新后的数据
          const res = await updateContract(row);
          if (res.code === 200) {
            this.$message.success('修改成功');
            this.editingRowId = null; // 退出编辑状态
            this.getContracts(); // 刷新员工列表
          } else {
            this.$message.error(res.message);
          }
        } catch (error) {
          this.$message.error('修改失败');
        }
      },
    },
    mounted() {
      this.getContracts();
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
  