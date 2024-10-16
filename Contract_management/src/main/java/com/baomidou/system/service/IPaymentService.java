package com.baomidou.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Payment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
public interface IPaymentService extends IService<Payment> {

    //List<Payment> getPaymentsByContractId(Long contractId);//根据 contract_id 查询收款信息
    Page<Payment> getPaymentsByContractId(Long contractId, Page<Payment> page);

    Payment addPayment(Payment payment);//添加收款信息

    Payment updatePayment(Long paymentId, Payment payment);// 修改收款信息

    void deletePayment(Long paymentId);// 删除收款信息

}
