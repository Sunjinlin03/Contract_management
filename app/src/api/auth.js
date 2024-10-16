import request from '@/utils/request'; 

/** 
 * 企业登录
 * @param {object} params LoginRequest
 * @param {string} params.account 
 * @param {string} params.password 
 * @returns
 */
export function loginCompany(params) {
  return request.post(`/auth/login/company`, params);
}

/** 
 * 员工登录
 * @param {object} params LoginRequest
 * @param {string} params.account 
 * @param {string} params.password 
 * @returns
 */
export function loginEmployee(params) {
  return request.post(`/auth/login/employee`, params);
}

/** 
 * 企业注册
 * @param {object} params 企业实体
 * @param {number} params.companyId 企业id
 * @param {string} params.companyName 企业名称
 * @param {string} params.companyAccount 企业账号
 * @param {string} params.companyPassword 企业密码
 * @returns
 */
export function registerCompany(params) {
  return request.post(`/auth/register/company`, params);
}
