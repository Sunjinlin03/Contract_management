package com.baomidou.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.system.entity.Company;
import com.baomidou.system.mapper.CompanyMapper;
import com.baomidou.system.service.ICompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    //公司登录验证
    @Override
    public Company validateCompany(String account, String password) {
        // 使用 MyBatis-Plus 的 lambdaQuery 查询
        Company company = this.lambdaQuery()
                .eq(Company::getCompanyAccount, account) // 查询账号
                .one(); // 返回单个结果

        // 验证密码
        if (company != null && company.getCompanyPassword().equals(password)) {
            return company; // 验证成功，返回企业对象
        }
        return null; // 验证失败
    }

    //注册判断是否有相同账号
    @Override
    public boolean existsByCompanyAccount(String companyAccount) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_account", companyAccount);
        return companyMapper.selectCount(queryWrapper) > 0;
    }
}
