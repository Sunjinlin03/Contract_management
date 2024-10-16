import request from '@/utils/request'; 

/** 
 * 根据 payment_id 分页查询开票信息
 * @param {string} paymentId 
  * @param {string} pageNo 
  * @param {string} pageSize 
  * @returns
 */
export function getInvoicesByPaymentId(paymentId, pageNo, pageSize) {
    return request.get(`/invoice/byPaymentId?paymentId=${paymentId}&pageNo=${pageNo}&pageSize=${pageSize}`);
}

/** 
 * 添加开票信息
 * @param {object} params Invoice对象
 * @param {number} params.invoiceId 开票id
 * @param {object} params.invoiceDate 开票日期
 * @param {string} params.invoiceNumber 发票号
 * @param {number} params.amountExcludingTax 不含税金额
 * @param {number} params.amountIncludingTax 价税合计
 * @param {number} params.paymentId 收款id
 * @param {number} params.contractId 合同id
 * @returns
 */
export function addInvoice(params) {
    return request.post(`/invoice`, params);
}

/** 
 * 修改开票信息
 * @param {string} invoiceId 
  * @param {object} params Invoice对象
 * @param {number} params.invoiceId 开票id
 * @param {object} params.invoiceDate 开票日期
 * @param {string} params.invoiceNumber 发票号
 * @param {number} params.amountExcludingTax 不含税金额
 * @param {number} params.amountIncludingTax 价税合计
 * @param {number} params.paymentId 收款id
 * @param {number} params.contractId 合同id
 * @returns
 */
export function updateInvoice(invoiceId, params) {
    return request.put(`/invoice/update/${invoiceId}`, params);
}

/** 
 * 删除开票信息
 * @param {string} invoiceId 
  * @returns
 */
export function deleteInvoice(invoiceId) {
    return request.delete(`/invoice/delete/${invoiceId}`);
}