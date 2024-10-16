<template>
    <div class="payment-page">

    <!-- 添加收款信息按钮 -->
    <el-button type="primary" @click="toggleAddPaymentForm" style="margin-left: 10px;">{{ addPaymentFormVisible ? '取消添加' : '添加收款信息' }}</el-button>

    <!-- 返回按钮 -->
    <el-button type="primary" @click="goback" style="margin-left: 10px;">返回</el-button>
    <!-- 添加收款信息表单（内嵌式） -->
    <el-form v-if="addPaymentFormVisible" :model="PaymentForm" label-width="100px" style="margin-top: 20px;">
        <el-form-item label="收款日期">
          <el-input v-model="paymentForm.paymentDate" />
        </el-form-item>
        <el-form-item label="收款金额">
          <el-input v-model="paymentForm.paymentAmount" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAddPayment">确认添加</el-button>
          <el-button @click="toggleAddPaymentForm">取消</el-button>
        </el-form-item>
      </el-form>

      <!--收款信息表格-->
      <el-table :data="payments" border style="width: 100%">
        <el-table-column prop="paymentId" label="收款ID" width="150"></el-table-column>
        <el-table-column prop="paymentDate" label="收款日期" width="250">
          <template v-slot="scope">
            <el-input v-if="editableRows[scope.$index]" v-model="scope.row.paymentDate"></el-input>
            <span v-else>{{ scope.row.paymentDate }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentAmount" label="收款金额" width="345">
          <template v-slot="scope">
            <el-input v-if="editableRows[scope.$index]" v-model="scope.row.paymentAmount"></el-input>
            <span v-else>{{ scope.row.paymentAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="400">
          <template v-slot="scope">
            <!-- 保存按钮，仅当当前行处于可编辑状态时显示 -->
            <el-button v-if="editableRows[scope.$index]" type="primary" @click="savePayment(scope.row)">保存</el-button>
            
            <!-- 取消按钮，仅当当前行处于可编辑状态时显示 -->
            <el-button v-if="editableRows[scope.$index]" @click="cancelEdit(scope.$index)">取消</el-button>

            <!-- 开票按钮，始终显示 -->
            <el-button v-if="!editableRows[scope.$index]" type="success" @click="goToInvoicePage(scope.row.paymentId)">开票</el-button>
            
            <!-- 修改按钮，仅当当前行不处于可编辑状态时显示 -->
            <el-button v-if="!editableRows[scope.$index]" type="warning" @click="editPayment(scope.$index)">修改</el-button>
            
            <!-- 删除按钮，始终显示 -->
            <el-button v-if="!editableRows[scope.$index]" type="danger" @click="deletePayment(scope.row.paymentId)">删除</el-button>
            
          </template>
        </el-table-column>
        
      </el-table>
  
      <!-- 分页 -->
      <el-pagination
        @current-change="handlePageChange"
        :current-page="pageNo"
        :page-size="pageSize"
        :total="totalPayments"
        layout="prev, pager, next">
      </el-pagination>
    </div>
  </template>
  
  <script>
  import { getPaymentsByContractId, updatePayment, deletePayment, addPayment } from '@/api/payment';
  
  export default {
    data() {
      return {
        contractId: this.$route.params.contractId, // 获取从路由传递过来的 contractId
        payments: [],          // 收款信息数据
        addPaymentFormVisible: false,
        paymentForm: {
            paymentDate: '',
            paymentAmount: '',
            contractId: this.$route.params.contractId,
      },
        editableRows: [],      // 记录哪些行处于编辑状态
        pageNo: 1,             // 当前页码
        pageSize: 10,          // 每页显示数量
        totalPayments: 0,      // 总收款数
      };
    },
    created() {
      this.fetchPayments(); // 初始化时获取收款信息
    },
    methods: {

    toggleAddPaymentForm() {
      this.addPaymentFormVisible = !this.addPaymentFormVisible;
      if (!this.addPaymentFormVisible) {
        this.resetForm();
      }
    },
    resetForm() {
      this.paymentForm = {
        paymentDate: '',
        paymentAmount: '',
      };
    },
    async submitAddPayment() {
      try {
        const res = await addPayment(this.paymentForm);
        if (res.code === 200) {
          this.$message.success('收款信息添加成功');
          this.toggleAddPaymentForm();
          this.fetchPayments();
        } else {
          this.$message.error(res.message);
        }
      } catch (error) {
        this.$message.error('添加收款信息失败');
      }
    },

      // 获取收款信息
      fetchPayments() {
        getPaymentsByContractId(this.contractId, this.pageNo, this.pageSize).then(response => {
          this.payments = response.data.records;
          this.totalPayments = response.data.total;
          this.editableRows = this.payments.map(() => false); // 初始化所有行不可编辑
        });
      },
  
      // 分页处理
      handlePageChange(newPageNo) {
        this.pageNo = newPageNo;
        this.fetchPayments();
      },
  
      // 编辑按钮点击事件
      editPayment(index) {
        this.editableRows[index] = true; // 直接设置可编辑状态
      },

      cancelEdit(index) {
        this.editableRows[index] = false; // 直接设置不可编辑状态
        this.fetchPayments(); // 重新获取数据，恢复原始状态
      },

      goback(){
        this.$router.push({ path: `/home/tasklist`});
      },


      // 保存修改
      async savePayment(row) {
        try {
          // 删除 paymentId，因为它已经在 URL 中
          const { paymentDate, paymentAmount } = row; // 只提取需要更新的字段

          const response = await updatePayment(row.paymentId, {
            paymentDate,    // 只包含收款日期
            paymentAmount,  // 只包含收款金额
          });

          if (response.code === 200) {
            this.$message.success('收款信息更新成功');
            this.fetchPayments(); // 重新获取数据，刷新表格
          } else {
            this.$message.error('更新失败');
          }
        } catch (error) {
          console.error(error);
          this.$message.error('更新失败');
        }
      },

      // 删除收款信息
      deletePayment(paymentId) {
        deletePayment(paymentId).then(response => {
          if (response.code === 200) {
            this.$message.success('收款信息删除成功');
            this.fetchPayments(); // 重新获取数据，刷新表格
          } else {
            this.$message.error('删除失败');
          }
        }).catch(error => {
          console.error(error);
          this.$message.error('删除失败');
        });
      },

      // 跳转到开票操作页面
      goToInvoicePage(paymentId) {
            this.$router.push({ path: `/home/tasklist/${this.contractId}/payment/${paymentId}` });
        },
    }
  };
  </script>
  
  <style scoped>
  .payment-page {
    padding: 20px;
  }

  .el-form {
    margin-bottom: 20px;
  }
  
  .el-input {
    width: 300px;
  }
  .el-table th, .el-table td {
    text-align: center;
  }
  
  .el-table {
     margin-top: 20px;
  }

  .el-button {
    margin-right: 10px;
  }

  .el-pagination {
    margin-top: 20px;
    text-align: right;
  }
  </style>
  