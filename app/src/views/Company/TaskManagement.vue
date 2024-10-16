<template>
    <div class="task-management">
      <!-- 模糊查询输入框 -->
      <div class="filter-section">
        <el-input v-model="projectName" placeholder="请输入项目名称" class="search-input">
          <template #prepend>项目名称</template>
        </el-input>
        <el-button type="primary" @click="fetchContracts" class="search-button">搜索</el-button>
      </div>
    
      <!-- 状态选择器 -->
      <div class="status-selector">
        <el-select v-model="statusId" placeholder="请选择状态" @change="fetchContracts">
          <el-option label="待发放" :value="1"></el-option>
          <el-option label="待接收" :value="2"></el-option>
          <el-option label="已接收" :value="3"></el-option>
          <el-option label="已提交" :value="4"></el-option>
          <el-option label="已审核" :value="5"></el-option>
        </el-select>
      </div>
    
      <!-- 根据 statusId 动态展示不同的表格 -->
      <div v-if="statusId === 1">
        <!-- 待发放状态下的表格 -->
        <el-table :data="contracts" border style="width: 100%">
          <el-table-column prop="contractId" label="合同ID" width="100"></el-table-column>
          <el-table-column prop="contractNumber" label="合同编号" width="300"></el-table-column>
          <el-table-column prop="projectName" label="项目名称" width="395"></el-table-column>
          <el-table-column label="负责员工" width="150">
            <template v-slot="scope">
              <el-input v-model="scope.row.employeeId" placeholder="请输入员工ID"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template v-slot="scope">
              <el-button type="primary" @click="handleDistributeTask(scope.row)">发放任务</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <div v-else-if="statusId === 2">
        <!-- 待接收状态下的表格 -->
        <el-table :data="contracts" border style="width: 100%">
          <el-table-column prop="contractId" label="合同ID" width="100"></el-table-column>
          <el-table-column prop="contractNumber" label="合同编号" width="300"></el-table-column>
          <el-table-column prop="projectName" label="项目名称" width="395"></el-table-column>
          <el-table-column label="负责员工" width="150">
            <template v-slot="scope">
              <span>{{ scope.row.employeeId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template v-slot="scope">
              <el-button type="warning">提醒接收</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <div v-else-if="statusId === 3">
        <!-- 已接收状态下的表格 -->
        <el-table :data="contracts" border style="width: 100%">
          <el-table-column prop="contractId" label="合同ID" width="100"></el-table-column>
          <el-table-column prop="contractNumber" label="合同编号" width="300"></el-table-column>
          <el-table-column prop="projectName" label="项目名称" width="295"></el-table-column>
          <el-table-column label="负责员工" width="150">
            <template v-slot="scope">
              <el-input v-model="scope.row.employeeId" placeholder="请输入新员工ID"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300">
            <template v-slot="scope">
              <el-button type="success" @click="goToPaymentPage(scope.row.contractId)">查看进度</el-button>
              <el-button type="success" @click="handleTransferContract(scope.row)">转移权限</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <div v-else-if="statusId === 4">
        <!-- 已提交状态下的表格 -->
        <el-table :data="contracts" border style="width: 100%">
          <el-table-column prop="contractId" label="合同ID" width="100"></el-table-column>
          <el-table-column prop="contractNumber" label="合同编号" width="300"></el-table-column>
          <el-table-column prop="projectName" label="项目名称" width="395"></el-table-column>
          <el-table-column label="负责员工" width="150">
            <template v-slot="scope">
              <span>{{ scope.row.employeeId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template v-slot="scope">
              <el-button type="success" @click="handleAuditContract(scope.row)">审核通过</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
  
      <div v-else-if="statusId === 5">
        <!-- 已审核状态下的表格 -->
        <el-table :data="contracts" border style="width: 100%">
          <el-table-column prop="contractId" label="合同ID" width="100"></el-table-column>
          <el-table-column prop="contractNumber" label="合同编号" width="300"></el-table-column>
          <el-table-column prop="projectName" label="项目名称" width="395"></el-table-column>
          <el-table-column label="负责员工" width="150">
            <template v-slot="scope">
              <span>{{ scope.row.employeeId }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="200">
            <template v-slot="scope">
              <span>已审核</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    
      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @current-change="handlePageChange"
          :current-page="pageNo"
          :page-size="pageSize"
          :total="totalContracts"
          layout="prev, pager, next">
        </el-pagination>
      </div>
    </div>
  </template>
  
  <script>
  import { getContractsByStatusAndCompany, distributeTask, transferContract, auditContract } from '@/api/contract';
  
  export default {
    data() {
      return {
        statusId: 1,          // 当前的状态ID，根据不同状态显示不同表格
        projectName: '',      // 用于模糊查询
        contracts: [],        // 合同数据列表
        pageNo: 1,            // 当前页码
        pageSize: 10,         // 每页显示数量
        totalContracts: 0,    // 总合同数
      };
    },
    created() {
      this.fetchContracts(); // 初始化时获取合同数据
    },
    methods: {
      // 获取合同数据，支持模糊查询和分页
      fetchContracts() {
        getContractsByStatusAndCompany(this.statusId, this.pageNo, this.pageSize, this.projectName).then(response => {
          this.contracts = response.data.records;  // 修正为 response.data.records
          this.totalContracts = response.data.total;
        });
      },
  
      // 分页处理
      handlePageChange(newPageNo) {
        this.pageNo = newPageNo;
        this.fetchContracts();
      },
  
      // 发放任务操作
      handleDistributeTask(row) {
        const { contractId, employeeId } = row;
        if (!employeeId) {
          this.$message.error('请输入员工ID');
          return;
        }
        distributeTask(contractId, employeeId).then(() => {
          this.$message.success('任务已成功发放');
          this.fetchContracts(); // 刷新数据
        }).catch(() => {
          this.$message.error('发放任务失败');
        });
      },

      // 跳转到收款操作页面
      goToPaymentPage(contractId) {
            this.$router.push({ path: `/home/tasks/${contractId}/payment` });
      },
  
      // 转移合同操作
      handleTransferContract(row) {
        const { contractId, employeeId } = row;
        if (!employeeId) {
          this.$message.error('请输入新员工ID');
          return;
        }
        transferContract(contractId, employeeId).then(() => {
          this.$message.success('合同权限已成功转移');
          this.fetchContracts(); // 刷新数据
        }).catch(() => {
          this.$message.error('转移权限失败');
        });
      },
  
      // 审核通过操作
      handleAuditContract(row) {
        const { contractId } = row;
        auditContract(contractId).then(() => {
          this.$message.success('审核通过');
          this.fetchContracts(); // 刷新数据
        }).catch(() => {
          this.$message.error('审核失败');
        });
      },
    }
  };
  </script>   
  
  <style scoped>
  

  .filter-section,
  .status-selector,
  .pagination-section {
    margin-bottom: 20px;
  }
  
  .filter-section {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .search-input {
    width: 400px;
  }
  
  .search-button {
    margin-left: 10px;
  }
  
  .pagination-section {
    text-align: center;
    margin-top: 20px;
  }
  
  .el-button {
    margin-right: 10px;
  }
  
  .el-table th, .el-table td {
    text-align: center;
  }
  </style>
  