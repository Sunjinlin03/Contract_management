<template>
    <div class="invoice-page">

    <!-- 添加开票信息按钮 -->
    <el-button type="primary" @click="toggleAddInvoiceForm" style="margin-left: 10px;">{{ addInvoiceFormVisible ? '取消开票' : '开票' }}</el-button>

    <!-- 返回按钮 -->
    <el-button type="primary" @click="goback" style="margin-left: 10px;">返回</el-button>
    <!-- 添加开票信息表单（内嵌式） -->
    <el-form v-if="addInvoiceFormVisible" :model="InvoiceForm" label-width="100px" style="margin-top: 20px;">
        <el-form-item label="开票日期">
          <el-input v-model="invoiceForm.invoiceDate" />
        </el-form-item>
        <el-form-item label="发票号">
          <el-input v-model="invoiceForm.invoiceNumber" />
        </el-form-item>
        <el-form-item label="不含税金额">
          <el-input v-model="invoiceForm.amountExcludingTax" />
        </el-form-item>
        <el-form-item label="价税总计">
          <el-input v-model="invoiceForm.amountIncludingTax" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAddInvoice">确认添加</el-button>
          <el-button @click="toggleAddInvoiceForm">取消</el-button>
        </el-form-item>
      </el-form>

      <!--收款信息表格-->
      <el-table :data="invoices" border style="width: 100%">
        <el-table-column prop="invoiceId" label="开票ID" width="70"></el-table-column>
        <el-table-column prop="invoiceDate" label="开票日期" width="150"></el-table-column>
        <el-table-column prop="invoiceNumber" label="发票号" width="195"></el-table-column>
        <el-table-column prop="amountExcludingTax" label="金额（不含税）/元" width="265">
          <template v-slot="scope">
            <el-input v-if="editableRows[scope.$index]" v-model="scope.row.amountExcludingTax"></el-input>
            <span v-else>{{ scope.row.amountExcludingTax }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="amountIncludingTax" label="价税总计/元" width="265">
          <template v-slot="scope">
            <el-input v-if="editableRows[scope.$index]" v-model="scope.row.amountIncludingTax"></el-input>
            <span v-else>{{ scope.row.amountIncludingTax }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <!-- 保存按钮，仅当当前行处于可编辑状态时显示 -->
            <el-button v-if="editableRows[scope.$index]" type="primary" @click="saveInvoice(scope.row)">保存</el-button>
            
            <!-- 取消按钮，仅当当前行处于可编辑状态时显示 -->
            <el-button v-if="editableRows[scope.$index]" @click="cancelEdit(scope.$index)">取消</el-button>
            
            <!-- 修改按钮，仅当当前行不处于可编辑状态时显示 -->
            <el-button v-if="!editableRows[scope.$index]" type="warning" @click="editInvoice(scope.$index)">修改</el-button>
            
            <!-- 删除按钮，始终显示 -->
            <el-button v-if="!editableRows[scope.$index]" type="danger" @click="deleteInvoice(scope.row.invoiceId)">删除</el-button>
            
          </template>
        </el-table-column>
        
      </el-table>
  
      <!-- 分页 -->
      <el-pagination
        @current-change="handlePageChange"
        :current-page="pageNo"
        :page-size="pageSize"
        :total="totalInvoices"
        layout="prev, pager, next">
      </el-pagination>
    </div>
  </template>
  
  <script>
  import { getInvoicesByPaymentId, updateInvoice, deleteInvoice, addInvoice } from '@/api/invoice';
  
  export default {
    data() {
      return {
        contractId: this.$route.params.contractId,
        paymentId: this.$route.params.paymentId, // 获取从路由传递过来的
        invoices: [],          // 收款信息数据
        addInvoiceFormVisible: false,
        invoiceForm: {
            contractId : this.$route.params.contractId,
            paymentId: this.$route.params.paymentId,
            invoiceDate:'',
            invoiceNumber:'',
            amountExcludingTax:'',
            amountIncludingTax:'',
      },
        editableRows: [],      // 记录哪些行处于编辑状态
        pageNo: 1,             // 当前页码
        pageSize: 10,          // 每页显示数量
        totalInvoices: 0,      // 总收款数
      };
    },
    created() {
      this.fetchInvoices(); // 初始化时获取收款信息
    },
    methods: {

    toggleAddInvoiceForm() {
      this.addInvoiceFormVisible = !this.addInvoiceFormVisible;
      if (!this.addInvoiceFormVisible) {
        this.resetForm();
      }
    },
    resetForm() {
      this.invoiceForm = {
        invoiceDate:'',
        invoiceNumber:'',
        amountExcludingTax:'',
        amountIncludingTax:'',
      };
    },
    async submitAddInvoice() {
      try {
        const res = await addInvoice(this.invoiceForm);
        if (res.code === 200) {
          this.$message.success('收款信息添加成功');
          this.toggleAddInvoiceForm();
          this.fetchInvoices();
        } else {
          this.$message.error(res.message);
        }
      } catch (error) {
        this.$message.error('添加收款信息失败');
      }
    },

      // 获取收款信息
      fetchInvoices() {
        getInvoicesByPaymentId(this.paymentId, this.pageNo, this.pageSize).then(response => {
          this.invoices = response.data.records;
          this.totalInvoices = response.data.total;
          this.editableRows = this.invoices.map(() => false); // 初始化所有行不可编辑
        });
      },
  
      // 分页处理
      handlePageChange(newPageNo) {
        this.pageNo = newPageNo;
        this.fetchInvoices();
      },
  
      // 编辑按钮点击事件
      editInvoice(index) {
        this.editableRows[index] = true; // 直接设置可编辑状态
      },

      cancelEdit(index) {
        this.editableRows[index] = false; // 直接设置不可编辑状态
        this.fetchInvoices(); // 重新获取数据，恢复原始状态
      },

      goback(){
        this.$router.push({ path: `/home/tasks/${this.contractId}/payment`});
      },


      // 保存修改
      async saveInvoice(row) {
        try {
          
          const { amountExcludingTax, amountIncludingTax } = row; // 只提取需要更新的字段

          const response = await updateInvoice(row.invoiceId, {
            amountExcludingTax,    
            amountIncludingTax,  
          });

          if (response.code === 200) {
            this.$message.success('收款信息更新成功');
            this.fetchInvoices(); // 重新获取数据，刷新表格
          } else {
            this.$message.error('更新失败');
          }
        } catch (error) {
          console.error(error);
          this.$message.error('更新失败');
        }
      },


  
      // 删除收款信息
      deleteInvoice(invoiceId) {
        deleteInvoice(invoiceId).then(response => {
          if (response.code === 200) {
            this.$message.success('收款信息删除成功');
            this.fetchInvoices(); // 重新获取数据，刷新表格
          } else {
            this.$message.error('删除失败');
          }
        }).catch(error => {
          console.error(error);
          this.$message.error('删除失败');
        });
      }
    }
  };
  </script>
  
  <style scoped>
  .invoice-page {
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
  