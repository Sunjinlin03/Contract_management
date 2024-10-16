package com.baomidou.system.service;

import com.baomidou.system.entity.Company;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
public interface ICompanyService extends IService<Company> {

    Company validateCompany(String account, String password);// 添加验证企业登录的方法

    boolean existsByCompanyAccount(String companyAccount);//注册判断是否有相同账号
}
