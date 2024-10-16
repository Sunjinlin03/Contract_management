package com.baomidou.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Invoice;
import com.baomidou.system.mapper.InvoiceMapper;
import com.baomidou.system.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {

    /**
     * 根据 payment_id 分页查询开票信息
     * @param paymentId 支付ID
     * @param page 分页对象
     * @return 分页后的开票信息列表
     */
    public Page<Invoice> getInvoicesByPaymentId(Long paymentId, Page<Invoice> page) {
        // 创建查询条件
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("payment_id", paymentId);

        // 使用分页查询
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    //添加开票信息
    @Override
    public Invoice addInvoice(Invoice invoice) {
        // 验证开票信息
        if (invoice.getInvoiceDate() == null ||
                invoice.getInvoiceNumber() == null ||
                invoice.getAmountExcludingTax() == null ||
                invoice.getAmountIncludingTax() == null ||
                invoice.getPaymentId() == null ||
                invoice.getContractId() == null) {
            throw new RuntimeException("开票信息不完整");
        }

        // 插入数据库
        save(invoice);
        return invoice;
    }

    //修改开票信息
    @Override
    public Invoice updateInvoice(Long invoiceId, Invoice invoice) {
        // 验证开票信息
        if (invoice.getInvoiceDate() == null ||
                invoice.getInvoiceNumber() == null ||
                invoice.getAmountExcludingTax() == null ||
                invoice.getAmountIncludingTax() == null ||
                invoice.getPaymentId() == null ||
                invoice.getContractId() == null) {
            throw new RuntimeException("开票信息不完整");
        }

        // 查找现有开票信息
        Invoice existingInvoice = getById(invoiceId);
        if (existingInvoice == null) {
            throw new RuntimeException("开票信息不存在");
        }

        // 更新开票信息
        existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
        existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
        existingInvoice.setAmountExcludingTax(invoice.getAmountExcludingTax());
        existingInvoice.setAmountIncludingTax(invoice.getAmountIncludingTax());
        existingInvoice.setPaymentId(invoice.getPaymentId());
        existingInvoice.setContractId(invoice.getContractId());

        // 保存更新
        updateById(existingInvoice);
        return existingInvoice;
    }

    //删除开票信息
    @Override
    public void deleteInvoice(Long invoiceId) {
        // 查找现有开票信息
        Invoice existingInvoice = getById(invoiceId);
        if (existingInvoice == null) {
            throw new RuntimeException("开票信息不存在");
        }

        // 删除开票信息
        removeById(invoiceId);
    }
}
