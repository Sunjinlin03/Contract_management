package com.baomidou.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.entity.Invoice;
import com.baomidou.system.service.IInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Tag(name = "开票信息管理模块")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;

    /**
     * 根据 payment_id 分页查询开票信息
     */
    @Operation(summary = "根据 payment_id 分页查询开票信息")
    @GetMapping("/byPaymentId")
    public ApiResponse<Page<Invoice>> getInvoicesByPaymentId(
            @RequestParam Long paymentId,
            @RequestParam(defaultValue = "1") int pageNo,  // 默认页码为1
            @RequestParam(defaultValue = "10") int pageSize) {  // 默认每页10条数据

        try {
            // 创建分页对象
            Page<Invoice> page = new Page<>(pageNo, pageSize);

            // 调用Service层分页查询
            Page<Invoice> invoicesPage = invoiceService.getInvoicesByPaymentId(paymentId, page);
            return ApiResponse.success(invoicesPage);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
    // 添加开票信息
    @Operation(summary = "添加开票信息")
    @PostMapping
    public ApiResponse<Invoice> addInvoice(@RequestBody Invoice invoice) {
        try {
            Invoice savedInvoice = invoiceService.addInvoice(invoice);
            return ApiResponse.success(savedInvoice);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 修改开票信息
    @Operation(summary = "修改开票信息")
    @PutMapping("/update/{invoiceId}")
    public ApiResponse<Invoice> updateInvoice(@PathVariable Long invoiceId, @RequestBody Invoice invoice) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, invoice);
            return ApiResponse.success(updatedInvoice);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 删除开票信息
    @Operation(summary = "删除开票信息")
    @DeleteMapping("/delete/{invoiceId}")
    public ApiResponse<Void> deleteInvoice(@PathVariable Long invoiceId) {
        try {
            invoiceService.deleteInvoice(invoiceId);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
