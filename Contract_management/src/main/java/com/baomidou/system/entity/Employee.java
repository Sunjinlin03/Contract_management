package com.baomidou.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Data
@Schema(description = "Employee对象")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "员工id")
    @TableId(value = "employee_id", type = IdType.AUTO)
    private Long employeeId;

    @Schema(description = "员工编号")
    private String employeeNumber;

    @Schema(description = "员工名")
    private String employeeName;

    @Schema(description = "员工账号")
    private String employeeAccount;

    @Schema(description = "员工密码")
    private String employeePassword;

    @Schema(description = "联系方式")
    private String contactInfo;

    @Schema(description = "企业id")
    private Long companyId;

}
