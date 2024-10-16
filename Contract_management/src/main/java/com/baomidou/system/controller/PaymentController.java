package com.baomidou.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.service.IPaymentService;
import com.baomidou.system.entity.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Slf4j
@Tag(name = "收款信息管理模块")
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    /**
     * 根据 contract_id 分页查询收款信息
     */
    @Operation(summary = "根据 contract_id 分页查询收款信息")
    @GetMapping("/byContractId")
    public ApiResponse<Page<Payment>> getPaymentsByContractId(
            @RequestParam Long contractId,
            @RequestParam(defaultValue = "1") int pageNo,  // 默认页码为1
            @RequestParam(defaultValue = "10") int pageSize) {  // 默认每页10条数据

        // 创建分页对象
        Page<Payment> page = new Page<>(pageNo, pageSize);

        // 调用Service层分页查询
        Page<Payment> paymentsPage = paymentService.getPaymentsByContractId(contractId, page);
        return ApiResponse.success(paymentsPage);
    }

    //添加收款信息
    @Operation(summary = "添加收款信息")
    @PostMapping("/add")
    public ApiResponse<Payment> addPayment(@RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentService.addPayment(payment);
            return ApiResponse.success(savedPayment);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 修改收款信息
    @Operation(summary = "修改收款信息")
    @PutMapping("/update/{paymentId}")
    public ApiResponse<Payment> updatePayment(@PathVariable Long paymentId, @RequestBody Payment payment) {
        try {
            log.info("paymentId:{}", paymentId);
            Payment updatedPayment = paymentService.updatePayment(paymentId, payment);
            return ApiResponse.success(updatedPayment);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 删除收款信息
    @Operation(summary = "删除收款信息")
    @DeleteMapping("/delete/{paymentId}")
    public ApiResponse<Void> deletePayment(@PathVariable Long paymentId) {
        try {
            paymentService.deletePayment(paymentId);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }
}
