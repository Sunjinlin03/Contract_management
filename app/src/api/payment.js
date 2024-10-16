import request from '@/utils/request'; 

/** 
 * 根据 contract_id 分页查询收款信息
 * @param {string} contractId 
  * @param {string} pageNo 
  * @param {string} pageSize 
  * @returns
 */
export function getPaymentsByContractId(contractId, pageNo, pageSize) {
    return request.get(`/payment/byContractId?contractId=${contractId}&pageNo=${pageNo}&pageSize=${pageSize}`);
}

/** 
 * 添加收款信息
 * @param {object} params Payment对象
 * @param {number} params.paymentId 收款id
 * @param {object} params.paymentDate 收款日期
 * @param {number} params.paymentAmount 收款金额
 * @param {number} params.contractId 合同id
 * @returns
 */
export function addPayment(params) {
    return request.post(`/payment/add`, params);
}

/** 
 * 修改收款信息
 * @param {string} paymentId 
  * @param {object} params Payment对象
 * @param {number} params.paymentId 收款id
 * @param {object} params.paymentDate 收款日期
 * @param {number} params.paymentAmount 收款金额
 * @param {number} params.contractId 合同id
 * @returns
 */
export function updatePayment(paymentId, params) {
    return request.put(`/payment/update/${paymentId}`, params);
}

/** 
 * 删除收款信息
 * @param {string} paymentId 
  * @returns
 */
export function deletePayment(paymentId) {
    return request.delete(`/payment/delete/${paymentId}`);
}