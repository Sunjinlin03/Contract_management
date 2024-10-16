import request from '@/utils/request'; 

/** 
 * （企业）创建合同
 * @param {object} params 合同实体
 * @param {number} params.contractId 合同id
 * @param {string} params.contractNumber 合同编号
 * @param {string} params.projectName 项目名称
 * @param {string} params.partyA 甲方
 * @param {string} params.partyB 乙方
 * @param {object} params.signingDate 签订日期
 * @param {number} params.totalPrice 总价
 * @param {number} params.taxAmount 税额
 * @returns
 */
export function createContract(params) {
    return request.post(`/contract/C/create`, params);
}

/** 
 * (企业）根据公司ID和项目名查询合同接口（分页）
 * @param {string} pageNo 
  * @param {string} pageSize 
  * @param {string} projectName 
  * @returns
 */
export function getContractByCompany(pageNo, pageSize, projectName) {
    return request.get(`/contract/C/byCompany?pageNo=${pageNo}&pageSize=${pageSize}&projectName=${projectName}`);
  }

/** 
 * （企业）删除合同
 * @param {string} contractId 
  * @returns
 */
export function deleteContract(contractId) {
    return request.delete(`/contract/C/delete/${contractId}`);
}

/** 
 * （企业）更新合同信息
 * @param {string} contractId 
  * @param {object} params 合同实体
 * @param {number} params.contractId 合同id
 * @param {string} params.projectName 项目名称
 * @param {string} params.partyA 甲方
 * @param {string} params.partyB 乙方
 * @param {number} params.totalPrice 总价
 * @param {number} params.taxAmount 税额
 * @returns
 */
export function updateContract(contractId, params) {
    return request.put(`/contract/C/update/${contractId}`, params);
}

/** 
 * （企业）根据状态ID、公司ID和项目名查询合同接口（分页）
 * @param {string} statusId 
  * @param {string} pageNo 
  * @param {string} pageSize 
  * @param {string} projectName 
  * @returns
 */
export function getContractsByStatusAndCompany(statusId, pageNo, pageSize, projectName) {
    return request.get(`/contract/C/byStatusAndCompany?statusId=${statusId}&pageNo=${pageNo}&pageSize=${pageSize}&projectName=${projectName}`);
}

/** 
 * （企业）指定合同和员工ID发放任务接口
 * @param {string} contractId 
  * @param {string} employeeId 
  * @returns
 */
export function distributeTask(contractId, employeeId) {
    return request.post(`/contract/C/distributeTask?contractId=${contractId}&employeeId=${employeeId}`);
}

/** 
 * （企业）转移合同权限接口
 * @param {string} contractId 
  * @param {string} newEmployeeId 
  * @returns
 */
export function transferContract(contractId, newEmployeeId) {
    return request.post(`/contract/C/transfer?contractId=${contractId}&newEmployeeId=${newEmployeeId}`);
}

/** 
 * （企业）审核合同任务接口
 * @param {string} contractId 
  * @returns
 */
export function auditContract(contractId) {
    return request.post(`/contract/C/audit?contractId=${contractId}`);
}

/** 
 * （员工）根据状态ID、员工ID和项目名查询合同接口（分页）
 * @param {string} statusId 
  * @param {string} pageNo 
  * @param {string} pageSize 
  * @param {string} projectName 
  * @returns
 */
export function getContractsByStatusAndEmployee(statusId, pageNo, pageSize, projectName) {
    return request.get(`/contract/E/byStatusAndEmployee?statusId=${statusId}&pageNo=${pageNo}&pageSize=${pageSize}&projectName=${projectName}`);
  }

/** 
 * （员工）接收任务接口
 * @param {string} contractId 
  * @returns
 */
export function receiveTask(contractId) {
    return request.post(`/contract/E/receiveTask?contractId=${contractId}`);
}

/** 
 * （员工）提交合同接口
 * @param {string} contractId 
  * @returns
 */
export function submitContract(contractId) {
    return request.post(`/contract/E/submitContract?contractId=${contractId}`);
}