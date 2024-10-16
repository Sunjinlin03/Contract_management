package com.baomidou.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.BusinessException;
import com.baomidou.system.entity.Company;
import com.baomidou.system.entity.Employee;
import com.baomidou.system.mapper.CompanyMapper;
import com.baomidou.system.mapper.EmployeeMapper;
import com.baomidou.system.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    //员工登录验证
    @Override
    public Employee validateEmployee(String account, String password) {
        // 使用 MyBatis-Plus 的 lambdaQuery 查询
        Employee employee = this.lambdaQuery()
                .eq(Employee::getEmployeeAccount, account) // 查询员工账号
                .one(); // 返回单个结果

        // 验证密码
        if (employee != null && employee.getEmployeePassword().equals(password)) {
            return employee; // 验证成功，返回员工对象
        }
        return null; // 验证失败
    }

    //注册判断是否有相同账号
    @Override
    public boolean existsByEmployeeAccount(String employeeAccount) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_account", employeeAccount);
        return employeeMapper.selectCount(queryWrapper) > 0;
    }

    // 查询指定公司下的所有员工
    /**
     * 查询指定公司下的所有员工（分页）
     * @param companyId 公司ID
     * @param page 分页对象
     * @return 分页后的员工列表
     */
    @Override
    public Page<Employee> getEmployeesPage(Long companyId, String employeeName,Page<Employee> page) {
        // 使用分页查询，传递分页对象和公司ID
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);

        // 如果传入了员工姓名，则进行模糊查询
        if (employeeName != null && !employeeName.isEmpty()) {
            queryWrapper.like("employee_name", employeeName);
        }

        // 返回分页结果
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    // 保存员工信息
    @Override
    public Employee addEmployee(Employee employee) {
        this.save(employee);
        return employee; // 返回保存的员工对象
    }

    // 根据员工ID查询员工信息
    @Override
    public Employee getEmployeeById(Long employeeId) {
        Employee employee = this.getById(employeeId);
        if (employee == null) {
            throw new BusinessException(400,"员工不存在");
        }
        return employee;
    }

    // 修改员工信息，只允许修改密码和联系方式
    @Override
    public void updateEmployeeInfo(Long employeeId, String newPassword, String contactInfo) {
        Employee employee = this.getById(employeeId);
        if (employee == null) {
            throw new BusinessException(400,"员工不存在");
        }

        // 更新密码和联系方式
        if (newPassword != null) {
            employee.setEmployeePassword(newPassword); // 密码应进行加密处理
        }
        if (contactInfo != null) {
            employee.setContactInfo(contactInfo);
        }

        // 保存修改
        this.updateById(employee);
    }

}
