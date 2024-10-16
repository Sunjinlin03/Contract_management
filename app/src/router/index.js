import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';
import Home from '@/views/Home.vue';
import EmployeeManagement from '@/views/Company/EmployeeManagement.vue';
import ContractManagement from '@/views/Company/ContractManagement.vue';
import TaskManagement from '@/views/Company/TaskManagement.vue';
import PersonalInfo from '@/views/Employee/PersonalInfo.vue';
import TaskList from '@/views/Employee/TaskList.vue';
import PaymentPage from '@/components/PaymentPage.vue'
import InvoicePage from '@/components/InvoicePage.vue'
import PaymentPageCompany from '@/components/PaymentPageCompany.vue'
import InvoicePageCompany from '@/components/InvoicePageCompany.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/home',
    component: Home,
    children: [
      { path: 'employees', component: EmployeeManagement, meta: { requiresCompany: true }},
      { path: 'contracts', component: ContractManagement, meta: { requiresCompany: true }},
      { path: 'tasks', component: TaskManagement, meta: { requiresCompany: true }},
      { 
        path: 'tasks/:contractId/payment', 
        component: PaymentPageCompany, 
        meta: { requiresCompany: true }  
      },
      {
        path: 'tasks/:contractId/payment/:paymentId',
        component: InvoicePageCompany,
        meta: { requiresCompany: true }  
      },
      { path: 'info', component: PersonalInfo, meta: { requiresEmployee: true }},
      { path: 'tasklist', component: TaskList, meta: { requiresEmployee: true }},
      {
        path: 'tasklist/:contractId/payment',
        component: PaymentPage,
        meta: { requiresEmployee: true }  // 通过任务列表访问 PaymentPage
      },
      {
        path: 'tasklist/:contractId/payment/:paymentId',
        component: InvoicePage,
        meta: { requiresEmployee: true }  // 通过任务列表访问 InvoicePage
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 导航守卫
router.beforeEach((to, from, next) => {
  const userType = sessionStorage.getItem('userType'); // 从 session 中获取用户类型
  if (to.meta.requiresCompany && userType !== 'company') {
    next('/login');
  } else if (to.meta.requiresEmployee && userType !== 'employee') {
    next('/login');
  } else {
    next();
  }
});

export default router;
