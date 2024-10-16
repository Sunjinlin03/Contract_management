package com.baomidou.system.controller;
import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.entity.Employee;
import com.baomidou.system.service.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *企业管理员工模块
 * @author Sun
 * @since 2024-09-26
 */
@Tag(name = "员工信息自管理模块")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    // 获取当前员工信息
    @Operation(summary = "获取当前员工信息")
    @GetMapping("/getEmployeeInfo")
    public ApiResponse<Employee> getEmployeeInfo(HttpServletRequest request) {
        // 从Session中获取employee_id
        Long employeeId = getEmployeeIdFromSession(request);

        if (employeeId == null) {
            return ApiResponse.error(401, "用户未登录或员工ID不存在");
        }

        // 通过Service层查询员工信息
        Employee employee = employeeService.getEmployeeById(employeeId);
        return ApiResponse.success(employee);
    }

    // 修改当前员工信息
    @Operation(summary = "修改当前员工信息")
    @PutMapping("/updateInfo")
    public ApiResponse<Void> updateEmployeeInfo(
            @RequestParam Long employeeId,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String contactInfo) {

        employeeService.updateEmployeeInfo(employeeId, newPassword, contactInfo);
        return ApiResponse.success(null);
    }

    // 从会话中获取员工ID的方法
    private Long getEmployeeIdFromSession(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("employeeId");
    }
}
