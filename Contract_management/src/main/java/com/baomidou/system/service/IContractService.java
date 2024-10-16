package com.baomidou.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.entity.Contract;
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
public interface IContractService extends IService<Contract> {

    Page<Contract> getContractsByCompany(Long companyId, String projectName, Page<Contract> page);//(企业）根据公司ID和项目名查询合同接口（分页）"

    Page<Contract> getContractsByStatusAndCompany(Long statusId, Long companyId, String projectName,Page<Contract> page);//(企业）根据状态ID、公司ID和项目名查询合同接口（分页）"

//    List<Contract> getContractsByStatus(int statusId);//根据合同状态查询相应状态的所有合同

    Contract createContract(Contract contract);//创建合同

    void deleteContract(Long contractId);//删除合同信息

    boolean distributeTask(Long contractId, Long employeeId);// 发放任务：修改合同状态为"待接收"，并指定员工ID

    boolean transferContract(Long contractId, Long newEmployeeId);// 修改合同的员工ID（转移合同权限给其他员工）

    boolean auditContract(Long contractId);// 审核员工提交的任务（将合同状态修改为已审核）

    List<Contract> getContractsByEmployeeId(Long employeeId);//查询与员工相关的合同

    //List<Contract> getContractsByStatusAndEmployee(Long statusId, Long employeeId);// 根据状态ID和员工ID查询合同
    Page<Contract> getContractsByStatusAndEmployee(Long statusId, Long employeeId,String projectName, Page<Contract> page);

    boolean receiveTask(Long contractId, Long employeeId);// 接收任务，修改合同状态为已接收

    boolean submitContract(Long contractId, Long employeeId);// 提交合同，修改合同状态为已提交
}
