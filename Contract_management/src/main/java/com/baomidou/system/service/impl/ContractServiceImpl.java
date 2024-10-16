package com.baomidou.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.system.common.ContractNotFoundException;
import com.baomidou.system.entity.Contract;
import com.baomidou.system.entity.Invoice;
import com.baomidou.system.entity.Payment;
import com.baomidou.system.mapper.ContractMapper;
import com.baomidou.system.service.IContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.system.service.IInvoiceService;
import com.baomidou.system.service.IPaymentService;
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
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IInvoiceService invoiceService;


    //(企业）根据公司ID和项目名查询合同接口（分页）"
    @Override
    public Page<Contract> getContractsByCompany(Long companyId, String projectName, Page<Contract> page) {
        // 创建查询条件
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);

        // 如果传入了项目名，则进行模糊查询
        if (projectName != null && !projectName.isEmpty()) {
            queryWrapper.like("project_name", projectName);
        }

        // 使用分页查询
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据状态ID和公司ID分页查询合同
     * @param statusId 合同状态ID
     * @param companyId 公司ID
     * @param page 分页对象
     * @return 分页后的合同列表
     */
    @Override
    public Page<Contract> getContractsByStatusAndCompany(Long statusId, Long companyId, String projectName, Page<Contract> page) {
        // 创建查询条件
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status_id", statusId)
                .eq("company_id", companyId);

        // 如果传入了项目名，则进行模糊查询
        if (projectName != null && !projectName.isEmpty()) {
            queryWrapper.like("project_name", projectName);
        }

        // 使用分页查询
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    //创建合同
    @Override
    public Contract createContract(Contract contract) {
        this.save(contract); // 使用 ServiceImpl 的 save 方法插入合同
        return contract;
    }

    //删除合同
    @Override
    public void deleteContract(Long contractId) {
        // 查找现有合同信息
        Contract existingContract = getById(contractId);
        if (existingContract == null) {
            throw new RuntimeException("合同信息不存在");
        }

        // 查找关联的收款信息
        List<Payment> payments = paymentService.lambdaQuery()
                .eq(Payment::getContractId, contractId)
                .list();

        // 删除关联的收款信息及其开票信息
        if (!payments.isEmpty()) {
            for (Payment payment : payments) {
                // 查找并删除相关的开票信息
                List<Invoice> invoices = invoiceService.lambdaQuery()
                        .eq(Invoice::getPaymentId, payment.getPaymentId())
                        .list();
                for (Invoice invoice : invoices) {
                    invoiceService.removeById(invoice.getInvoiceId());
                }
                // 删除收款信息
                paymentService.removeById(payment.getPaymentId());
            }
        }

        // 删除合同信息
        removeById(contractId);
    }

    // 发放任务：修改合同状态为"待接收"，并指定员工ID
    @Override
    public boolean distributeTask(Long contractId, Long employeeId) {
        // 查询对应的合同
        Contract contract = this.getById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 修改合同状态为“待接收”，并将合同关联到指定员工
        contract.setStatusId(2); // 2表示"待接收"状态
        contract.setEmployeeId(employeeId); // 更新为指定的员工ID
        return this.updateById(contract);
    }

    // 修改合同的员工ID（转移合同权限给其他员工）
    @Override
    public boolean transferContract(Long contractId, Long newEmployeeId) {
        // 查询对应的合同
        Contract contract = this.getById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 将合同的employeeId更新为新员工ID
        contract.setEmployeeId(newEmployeeId);
        return this.updateById(contract);
    }

    // 审核员工提交的任务（将合同状态修改为已审核）
    @Override
    public boolean auditContract(Long contractId) {
        // 查询对应的合同
        Contract contract = this.getById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 假设 5 是“已审核”的状态ID
        Integer auditStatusId = 5;

        // 将合同的状态修改为已审核
        contract.setStatusId(auditStatusId);
        return this.updateById(contract);
    }

    // 查询与员工相关的合同
    @Override
    public List<Contract> getContractsByEmployeeId(Long employeeId) {
        return this.list(new QueryWrapper<Contract>().eq("employee_id", employeeId));
    }

    /**
     * 根据状态ID和员工ID分页查询合同
     * @param statusId 合同状态ID
     * @param employeeId 员工ID
     * @param page 分页对象
     * @return 分页后的合同列表
     */
    @Override
    public Page<Contract> getContractsByStatusAndEmployee(Long statusId, Long employeeId, String projectName,Page<Contract> page) {
        // 创建查询条件
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status_id", statusId)
                .eq("employee_id", employeeId);

        // 如果传入了项目名，则进行模糊查询
        if (projectName != null && !projectName.isEmpty()) {
            queryWrapper.like("project_name", projectName);
        }

        // 使用分页查询
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    // 接收任务，修改合同状态为已接收
    @Override
    public boolean receiveTask(Long contractId, Long employeeId) {
        // 先查询合同是否存在
        Contract contract = this.getById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 检查该合同是否已经被其他人接收
        if (contract.getEmployeeId() != null && !contract.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("任务已被其他人接收");
        }

        // 更新合同的状态为"已接收" (假设 3 是已接收状态)
        contract.setStatusId(3);  // 3 代表已接收
        contract.setEmployeeId(employeeId);  // 关联接收任务的员工

        // 保存修改
        return this.updateById(contract);
    }

    // 提交合同，修改合同状态为已提交
    public boolean submitContract(Long contractId, Long employeeId) {
        // 先查询合同是否存在
        Contract contract = this.getById(contractId);
        if (contract == null) {
            throw new RuntimeException("合同不存在");
        }

        // 检查该合同是否属于当前员工
        if (!contract.getEmployeeId().equals(employeeId)) {
            throw new RuntimeException("没有权限提交此合同");
        }

        // 更新合同的状态为"已提交" (假设 4 是已提交状态)
        contract.setStatusId(4);  // 4 代表已提交

        // 保存修改
        return this.updateById(contract);
    }
}
