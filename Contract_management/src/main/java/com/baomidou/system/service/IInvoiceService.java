package com.baomidou.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Invoice;
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
public interface IInvoiceService extends IService<Invoice> {

//    List<Invoice> getInvoicesByPaymentId(Long paymentId);// 根据 payment_id 查询开票信息
    Page<Invoice> getInvoicesByPaymentId(Long paymentId, Page<Invoice> page);

    Invoice addInvoice(Invoice invoice);// 添加开票信息

    Invoice updateInvoice(Long invoiceId, Invoice invoice);// 修改开票信息

    void deleteInvoice(Long invoiceId);//删除开票信息

}
