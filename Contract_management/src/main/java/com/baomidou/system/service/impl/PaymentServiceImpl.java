package com.baomidou.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Invoice;
import com.baomidou.system.entity.Payment;
import com.baomidou.system.mapper.PaymentMapper;
import com.baomidou.system.service.IInvoiceService;
import com.baomidou.system.service.IPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {

    @Autowired
    private IInvoiceService invoiceService;

    /**
     * 根据 contract_id 分页查询收款信息
     * @param contractId 合同ID
     * @param page 分页对象
     * @return 分页后的收款信息列表
     */
    public Page<Payment> getPaymentsByContractId(Long contractId, Page<Payment> page) {
        // 创建查询条件
        QueryWrapper<Payment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);

        // 使用分页查询
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    //添加收款信息
    @Override
    public Payment addPayment(Payment payment) {
        // 验证收款信息
        if (payment.getPaymentDate() == null ||
                payment.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0 ||
                payment.getContractId() == null) {
            throw new RuntimeException("收款信息不完整或金额无效");
        }

        // 插入数据库
        save(payment);
        return payment;
    }

    //修改收款信息
    @Override
    public Payment updatePayment(Long paymentId, Payment payment) {
        // 验证收款信息
        if (payment.getPaymentDate() == null ||
                payment.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("收款信息不完整或金额无效");
        }

        // 查找现有收款信息
        Payment existingPayment = getById(paymentId);
        if (existingPayment == null) {
            throw new RuntimeException("收款信息不存在");
        }

        // 更新收款信息
        existingPayment.setPaymentDate(payment.getPaymentDate());
        existingPayment.setPaymentAmount(payment.getPaymentAmount());
        existingPayment.setContractId(payment.getContractId());

        // 保存更新
        updateById(existingPayment);
        return existingPayment;
    }

    // 删除收款信息
    @Override
    public void deletePayment(Long paymentId) {
        // 查找现有收款信息
        Payment existingPayment = getById(paymentId);
        if (existingPayment == null) {
            throw new RuntimeException("收款信息不存在");
        }

        // 查找关联的开票信息
        List<Invoice> invoices = invoiceService.lambdaQuery()
                .eq(Invoice::getPaymentId, paymentId)
                .list();

        // 删除关联的开票信息
        for (Invoice invoice : invoices) {
            invoiceService.removeById(invoice.getInvoiceId());
        }

        // 删除收款信息
        removeById(paymentId);
    }
}
