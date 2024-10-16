package com.baomidou.system.controller;

import com.baomidou.system.common.ApiResponse;
import com.baomidou.system.common.BusinessException;
import com.baomidou.system.entity.Company;
import com.baomidou.system.entity.Employee;
import com.baomidou.system.entity.LoginRequest;
import com.baomidou.system.service.ICompanyService;
import com.baomidou.system.service.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 * 登陆注册模块
 * @author Sun
 * @since 2024-09-26
 */
@Slf4j
@Tag(name = "登陆注册模块")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ICompanyService companyService;

    // 企业注册
    @Operation(summary = "企业注册")
    @PostMapping("/register/company")
    public ApiResponse<String> registerCompany(@RequestBody Company company) {
        // 检查是否存在相同的公司账号
        if (companyService.existsByCompanyAccount(company.getCompanyAccount())) {
            throw new BusinessException(409, "该企业账号已存在"); // 使用自定义异常
        }

        // 进行企业注册逻辑
        companyService.save(company);
        return ApiResponse.success("企业注册成功");
    }

    // 员工注册
    @Operation(summary = "员工注册")
    @PostMapping("/register/employee")
    public ApiResponse<String> registerEmployee(@RequestBody Employee employee) {
        // 检查是否存在相同的员工账号
        if (employeeService.existsByEmployeeAccount(employee.getEmployeeAccount())) {
            throw new BusinessException(409, "该员工账号已存在"); // 使用自定义异常
        }

        // 进行员工注册逻辑
        employeeService.save(employee);
        return ApiResponse.success("员工注册成功");
    }

    // 企业登录
    @Operation(summary = "企业登录")
    @PostMapping("/login/company")
    public ApiResponse<Object> loginCompany(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Company company = companyService.validateCompany(loginRequest.getAccount(), loginRequest.getPassword());
        if (company != null) {
            // 登录成功，将企业ID存入session
            session.setAttribute("companyId", company.getCompanyId());

            // 返回企业信息，包括企业ID
            return ApiResponse.success(company);
        } else {
            throw new BusinessException(401, "企业账号或密码错误"); // 使用自定义异常
        }
    }

    // 员工登录
    @Operation(summary = "员工登录")
    @PostMapping("/login/employee")
    public ApiResponse<Object> loginEmployee(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Employee employee = employeeService.validateEmployee(loginRequest.getAccount(), loginRequest.getPassword());
        if (employee != null) {
            // 登录成功，将员工ID存入session
            session.setAttribute("employeeId", employee.getEmployeeId());

            // 返回员工信息，包括员工ID
            return ApiResponse.success(employee);
        } else {
            throw new BusinessException(401, "用户名或密码错误"); // 使用自定义异常
        }
    }

    // 注销登录
    @Operation(summary = "注销登录")
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        try {
            httpSession.invalidate(); // 使 session 失效
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(500, "注销失败");
        }
    }
}