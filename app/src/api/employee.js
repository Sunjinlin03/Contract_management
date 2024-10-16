import request from '@/utils/request'; 

/**
 * 获取员工列表（分页 + 姓名模糊查询）
 * @param {number} pageNo - 当前页码
 * @param {number} pageSize - 每页显示的记录数
 * @param {string} employeeName - 员工姓名（可选，用于模糊查询）
 * @returns {Promise} - 请求返回的员工列表数据
 */
export function getAllEmployees(pageNo, pageSize, employeeName = '') {
  return request.get(`/company/employee/list`, {
    params: {
      pageNo,
      pageSize,
      employeeName,
    },
  });
  
}

/**
 * 添加员工
 */
export function addEmployee(params) {
  return request.post(`/company/employee/add`, params);
}

/** 
 * 删除员工
 * @param {string} id 
  * @returns
 */
export function deleteEmployee(id) {
  return request.delete(`/company/employee/delete/${id}`);
}

/** 
 * 更新员工信息
 * @param {object} params Employee对象
 * @param {number} params.employeeId 员工id
 * @param {string} params.employeeNumber 员工编号
 * @param {string} params.employeeName 员工名
 * @param {string} params.employeeAccount 员工账号
 * @param {string} params.employeePassword 员工密码
 * @param {string} params.contactInfo 联系方式
 * @param {number} params.companyId 企业id
 * @returns
 */
export function updateEmployee(params) {
  return request.put(`/company/employee/update`, params);
}