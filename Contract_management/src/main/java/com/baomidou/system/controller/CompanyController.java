package com.baomidou.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.common.BusinessException;
import com.baomidou.system.entity.Company;
import com.baomidou.system.entity.Contract;
import com.baomidou.system.entity.Employee;
import com.baomidou.system.service.ICompanyService;
import com.baomidou.system.service.IContractService;
import com.baomidou.system.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "企业管理员工信息模块")
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IContractService contractService; // 引入合同服务

    // 显示所有员工
    @Operation(summary = "显示所有员工（分页+姓名模糊查询）")
    @GetMapping("/employee/list")
    public ApiResponse<Page<Employee>> getAllEmployees(HttpServletRequest request,
           @RequestParam(defaultValue = "1") int pageNo,  // 默认页码为1
           @RequestParam(defaultValue = "10") int pageSize,  // 默认每页10条数据
           @RequestParam(required = false) String employeeName) {  // 可选的员工姓名查询参数

        // 从Session中获取公司ID
        Long companyId = getCompanyIdFromSession(request);
        if (companyId == null) {
            throw new BusinessException(400, "未登录或会话已过期");
        }

        // 创建分页对象
        Page<Employee> page = new Page<>(pageNo, pageSize);

        // 调用分页查询方法并传入公司ID和员工姓名
        Page<Employee> employeesPage = employeeService.getEmployeesPage(companyId, employeeName, page);

        // 返回分页结果
        return ApiResponse.success(employeesPage);
    }



    //查看员工详细信息
    @Operation(summary = "查看员工详细信息")
    @GetMapping("/employee/view/{employeeId}")
    public ApiResponse<Employee> getEmployeeById(@PathVariable Long employeeId) {
        // 使用 Service 层根据 ID 查询员工
        Employee employee = employeeService.getById(employeeId);

        // 判断员工是否存在
        if (employee == null) {
            return ApiResponse.error(400, "员工未找到");
        }

        return ApiResponse.success(employee);
    }

    // 添加员工
    @Operation(summary = "添加员工")
    @PostMapping("/employee/add")
    public ApiResponse<Employee> addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        Long companyId = getCompanyIdFromSession(request);
        if (companyId == null) {
            throw new BusinessException(401, "未登录或会话已过期");
        }

        //设置员工的登录密码(默认为123456）
        employee.setEmployeePassword("123456");
        // 设置员工的 companyId
        employee.setCompanyId(companyId);

        // 调用服务层添加员工
        Employee addedEmployee = employeeService.addEmployee(employee);
        return ApiResponse.success(addedEmployee);
    }

    // 更新员工信息
    @Operation(summary = "更新员工信息")
    @PutMapping("/employee/update")
    public ApiResponse<Employee> updateEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        Long companyId = (Long) request.getSession().getAttribute("companyId");
        if (companyId == null) {
            throw new BusinessException(401, "未登录或会话已过期");
        }

        // 从数据库中获取现有员工信息
        Employee existingEmployee = employeeService.getById(employee.getEmployeeId());
        if (existingEmployee == null) {
            throw new BusinessException(400, "员工不存在");
        }

        // 只更新允许的字段，保持 companyId 不变
        existingEmployee.setEmployeeNumber(employee.getEmployeeNumber());
        existingEmployee.setEmployeeName(employee.getEmployeeName());
        existingEmployee.setContactInfo(employee.getContactInfo());

        // 保存更新后的员工信息
        employeeService.updateById(existingEmployee);
        return ApiResponse.success(existingEmployee);
    }

    // 删除员工
    @Operation(summary = "删除员工")
    @DeleteMapping("/employee/delete/{id}")
    public ApiResponse<Void> deleteEmployee(@PathVariable Long id, HttpServletRequest request) {
        Long companyId = getCompanyIdFromSession(request);
        if (companyId == null) {
            throw new BusinessException(401, "未登录或会话已过期");
        }

        // 检查员工是否存在
        Employee existingEmployee = employeeService.getById(id);
        if (existingEmployee == null) {
            throw new BusinessException(400, "员工不存在");
        }

        // 检查员工是否属于当前企业（可选）
        if (!existingEmployee.getCompanyId().equals(companyId)) {
            throw new BusinessException(403, "无权限删除该员工");
        }

        // 移除该员工对合同的权限
        List<Contract> contracts = contractService.getContractsByEmployeeId(id);
        if (contracts != null && !contracts.isEmpty()) {
            for (Contract contract : contracts) {
                contract.setEmployeeId(null); // 清空合同的员工ID
                contractService.updateById(contract); // 更新合同信息
            }
        }

        // 执行删除操作
        employeeService.removeById(id);
        return ApiResponse.success(null);
    }

    // 从会话中获取公司ID的方法
    private Long getCompanyIdFromSession(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("companyId");
    }
}
