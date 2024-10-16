package com.baomidou.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.entity.Contract;
import com.baomidou.system.service.IContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "企业/员工管理合同信息模块")
@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private IContractService contractService;


    /**
     * 根据公司ID查询合同（分页）
     */
    @Operation(summary = "(企业）根据公司ID和项目名查询合同接口（分页）")
    @GetMapping("/C/byCompany")
    public ApiResponse<Page<Contract>> getContractByCompany(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10")int pageSize,
            @RequestParam(required = false) String projectName) {

        // 通过Session获取company_id
        Long companyId = getCompanyIdFromSession(request);

        if (companyId == null) {
            return ApiResponse.error(401, "用户未登录或公司ID不存在");
        }

        // 创建分页对象
        Page<Contract> page = new Page<>(pageNo, pageSize);

        Page<Contract> contractsPage = contractService.getContractsByCompany(companyId, projectName, page);

        return ApiResponse.success(contractsPage);
    }

    /**
     * 根据状态ID和公司ID查询合同（分页）
     */
    @Operation(summary = "（企业）根据状态ID、公司ID和项目名查询合同接口（分页）")
    @GetMapping("/C/byStatusAndCompany")
    public ApiResponse<Page<Contract>> getContractsByStatusAndCompany(
            @RequestParam Long statusId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int pageNo,  // 默认页码为1
            @RequestParam(defaultValue = "10") int pageSize,  // 默认每页10条数据
            @RequestParam(required = false) String projectName) {  // 可选的项目名称查询参数

        // 通过Session获取company_id
        Long companyId = getCompanyIdFromSession(request);

        if (companyId == null) {
            return ApiResponse.error(401, "用户未登录或公司ID不存在");
        }

        // 创建分页对象
        Page<Contract> page = new Page<>(pageNo, pageSize);

        // 调用Service层方法查询合同
        Page<Contract> contractsPage = contractService.getContractsByStatusAndCompany(statusId, companyId, projectName, page);
        return ApiResponse.success(contractsPage);
    }


    //查看合同详细信息(企业模块与员工模块共用）
    @Operation(summary = "(企业/员工）查看合同详细信息")
    @GetMapping("/view/{contractId}")
    public ApiResponse<Contract> getContractById(@PathVariable Long contractId) {
        Contract contract = contractService.getById(contractId);
        if (contract == null) {
            return ApiResponse.error(400, "合同未找到");
        }
        return ApiResponse.success(contract);
    }

    //创建合同
    @Operation(summary = "（企业）创建合同")
    @PostMapping("/C/create")
    public ApiResponse<Contract> createContract(@RequestBody Contract contract, HttpServletRequest request) {
        // 从请求中获取 company_id
        Long companyId = getCompanyIdFromSession(request);

        if (companyId == null) {
            return ApiResponse.error(400, "未找到公司信息");
        }

        contract.setCompanyId(companyId); // 设置公司 ID
        contract.setEmployeeId(null); // 保持员工 ID 为 null
        contract.setStatusId(1); // 设置状态为待发放

        // 调用服务层创建合同
        Contract addedcontract = contractService.createContract(contract);

        return ApiResponse.success(addedcontract); // 返回成功响应
    }

    //更新合同信息
    @Operation(summary = "（企业）更新合同信息")
    @PutMapping("/C/update/{contractId}")
    public ApiResponse<Contract> updateContract(@PathVariable Long contractId, @RequestBody Contract contract) {
        // 查询合同
        Contract existingContract = contractService.getById(contractId);
        if (existingContract == null) {
            return ApiResponse.error(400, "合同未找到");
        }

        // 更新允许修改的字段
        existingContract.setProjectName(contract.getProjectName());
        existingContract.setPartyA(contract.getPartyA());
        existingContract.setPartyB(contract.getPartyB());
        existingContract.setTotalPrice(contract.getTotalPrice());
        existingContract.setTaxAmount(contract.getTaxAmount());

        // 调用服务层更新合同
        contractService.updateById(existingContract);

        return ApiResponse.success(existingContract); // 返回成功响应
    }

    // 删除合同
    @Operation(summary = "（企业）删除合同")
    @DeleteMapping("/C/delete/{contractId}")
    public ApiResponse<Void> deleteContract(@PathVariable Long contractId) {
        try {
            contractService.deleteContract(contractId);
            return ApiResponse.success(null);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    // 发放任务接口，指定合同和员工ID
    @Operation(summary = "（企业）指定合同和员工ID发放任务接口")
    @PostMapping("/C/distributeTask")
    public ApiResponse<Void> distributeTask(@RequestParam Long contractId, @RequestParam Long employeeId) {
        boolean success = contractService.distributeTask(contractId, employeeId);
        if (success) {
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(500, "任务发放失败");
        }
    }

    // 修改合同权限接口，转移合同权限给新的员工
    @Operation(summary = "（企业）转移合同权限接口")
    @PostMapping("/C/transfer")
    public ApiResponse<Void> transferContract(@RequestParam Long contractId, @RequestParam Long newEmployeeId) {
        boolean success = contractService.transferContract(contractId, newEmployeeId);
        if (success) {
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(500, "合同权限转移失败");
        }
    }

    // 审核合同任务接口，审核后将状态修改为已审核
    @Operation(summary = "（企业）审核合同任务接口")
    @PostMapping("/C/audit")
    public ApiResponse<Void> auditContract(@RequestParam Long contractId) {
        boolean success = contractService.auditContract(contractId);
        if (success) {
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(500, "审核失败");
        }
    }


    /**
     * 根据状态ID、员工ID和项目名分页查询合同
     */
    @Operation(summary = "（员工）根据状态ID、员工ID和项目名查询合同接口（分页）")
    @GetMapping("/E/byStatusAndEmployee")
    public ApiResponse<Page<Contract>> getContractsByStatusAndEmployee(
            @RequestParam Long statusId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") int pageNo,  // 默认页码为1
            @RequestParam(defaultValue = "10") int pageSize,  // 默认每页10条数据
            @RequestParam(required = false) String projectName) {  // 可选的项目名称查询参数

        // 通过Session获取employee_id
        Long employeeId = getEmployeeIdFromSession(request);

        if (employeeId == null) {
            return ApiResponse.error(401, "用户未登录或员工ID不存在");
        }

        // 创建分页对象
        Page<Contract> page = new Page<>(pageNo, pageSize);

        // 调用Service层方法分页查询合同
        Page<Contract> contractsPage = contractService.getContractsByStatusAndEmployee(statusId, employeeId, projectName, page);
        return ApiResponse.success(contractsPage);
    }


    // 接收任务接口，将合同状态修改为已接收
    @Operation(summary = "（员工）接收任务接口")
    @PostMapping("/E/receiveTask")
    public ApiResponse<String> receiveTask(@RequestParam Long contractId, HttpServletRequest request) {
        // 通过Session获取employee_id
        Long employeeId = getEmployeeIdFromSession(request);

        if (employeeId == null) {
            return ApiResponse.error(401, "用户未登录或员工ID不存在");
        }

        // 调用Service层方法接收任务
        boolean success = contractService.receiveTask(contractId, employeeId);
        if (success) {
            return ApiResponse.success("任务接收成功，合同状态已修改为已接收");
        } else {
            return ApiResponse.error(500, "任务接收失败");
        }
    }

    // 提交合同接口，将合同状态修改为已提交
    @Operation(summary = "（员工）提交合同接口")
    @PostMapping("/E/submitContract")
    public ApiResponse<String> submitContract(@RequestParam Long contractId, HttpServletRequest request) {
        // 通过Session获取employee_id
        Long employeeId = getEmployeeIdFromSession(request);

        if (employeeId == null) {
            return ApiResponse.error(401, "用户未登录或员工ID不存在");
        }

        // 调用Service层方法提交合同
        boolean success = contractService.submitContract(contractId, employeeId);
        if (success) {
            return ApiResponse.success("合同提交成功，状态已修改为已提交");
        } else {
            return ApiResponse.error(500, "合同提交失败");
        }
    }

    // 从会话中获取公司ID的方法
    private Long getCompanyIdFromSession(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("companyId");
    }

    // 从会话中获取员工ID的方法
    private Long getEmployeeIdFromSession(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("employeeId");
    }
}
