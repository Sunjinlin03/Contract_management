package com.baomidou.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Company;
import com.baomidou.system.entity.Employee;
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
public interface IEmployeeService extends IService<Employee> {

    Employee validateEmployee(String account, String password);// 验证员工登录的方法

    boolean existsByEmployeeAccount(String employeeAccount);//注册判断是否有相同账号

    //List<Employee> getAllEmployees(Long companyId);// 查询员工
    Page<Employee> getEmployeesPage(Long companyId,String employeeName,  Page<Employee> page);

    Employee addEmployee(Employee employee);// 添加员工

    Employee getEmployeeById(Long employeeId);// 根据员工ID查询员工信息

    void updateEmployeeInfo(Long employeeId, String newPassword, String contactInfo);//修改当前员工信息


}
